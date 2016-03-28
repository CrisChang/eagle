package com.poison.eagle.manager;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import com.alibaba.fastjson.JSONObject;
import com.alipay.util.AlipayNotify;
import com.poison.eagle.action.PaycenterController;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.MD5Utils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.PaycenterUtil;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.paycenter.constant.PaycenterConstant;
import com.poison.paycenter.ext.apple.ApplePayQuery;
import com.poison.paycenter.ext.wx.WXPay;
import com.poison.paycenter.ext.wx.common.XMLParser;
import com.poison.paycenter.ext.wx.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.poison.paycenter.model.PayLog;
import com.poison.product.client.AccGoldFacade;
import com.poison.product.client.ProductFacade;
import com.poison.product.model.AccGold;
import com.poison.product.model.Product;
import com.poison.product.model.ProductOrder;
import com.poison.story.client.StoryFacade;
import com.poison.story.model.StoryChapter;
import com.poison.story.model.StoryPay;

public class ProductManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ProductManager.class);
	
	//private int flagint;
	private ProductFacade productFacade;
	
	private AccGoldFacade accGoldFacade;
	
	private StoryFacade storyFacade;

	public void setProductFacade(ProductFacade productFacade) {
		this.productFacade = productFacade;
	}

	public void setAccGoldFacade(AccGoldFacade accGoldFacade) {
		this.accGoldFacade = accGoldFacade;
	}

	public void setStoryFacade(StoryFacade storyFacade) {
		this.storyFacade = storyFacade;
	}

	/**
	 * 查询金币账户信息
	 * @return
	 */
	public String getAccGold(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		//转化成可读类型
		try {
			//req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			//req = (Map<String, Object>) req.get("req");
			//dataq = (Map<String, Object>) req.get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		
		int flagint = 0;
		
		AccGold accGold = accGoldFacade.findAccGoldByUserId(uid);
		long goldamount = 0;
		if(accGold!=null){
			flagint = accGold.getFlag();
			goldamount = accGold.getGoldamount();
		}
		if(flagint == ResultUtils.SUCCESS || flagint == ResultUtils.DATAISNULL || flagint==0){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			Map<String,Object> map = new HashMap<String,Object>(1);
			map.put("goldamount", goldamount);
			datas.put("map",map);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 * 查询金币商品信息
	 * @return
	 */
	public String getGoldProducts(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		
		int flagint = 0;
		
		List<Product> products = productFacade.findProductsByType(Product.TYPE_GOLD);
		if(products!=null && products.size()==0 && products.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list",products);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 * 查询商品信息(月票商品、打赏商品)
	 * @return
	 */
	public String getProducts(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String type = "";
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			type = (String) dataq.get("type");//商品类型
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		if(type==null || "".equals(type.trim()) || !Product.containsType(type)){
			flag = CommentUtils.RES_FLAG_ERROR;
			error = "错误的商品类型";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		int flagint = 0;
		
		List<Product> products = productFacade.findProductsByType(type);
		if(products!=null && products.size()==0 && products.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list",products);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	//生成购物订单，返回客户端支付操作的必要数据(支付宝、微信)
	public String createProductOrder(String reqs,Long uid){
		//try{
//			LOG.info("客户端json数据："+reqs);
			Map<String, Object> req =null;
			Map<String, Object> dataq=null;
			Map<String, Object> datas =null;
			String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
			String error="";
			String resString="";//返回数据
			//去掉空格
			if(reqs!=null){
				reqs = reqs.trim();
			}
			long pid = 0;//商品id
			String paymode = "";//支付方式
			String ip="";
			//转化成可读类型
			try {
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				req = (Map<String, Object>) req.get("req");
				dataq = (Map<String, Object>) req.get("data");
				String pidStr = dataq.get("pid")+"";
				if(StringUtils.isInteger(pidStr)){
					pid = Long.valueOf(pidStr);
				}
				paymode = dataq.get("paymode")+"";//支付方式
				ip = dataq.get("ip")+"";
			} catch (Exception e) {
				e.printStackTrace();
			}
			datas = new HashMap<String, Object>();
			
			//如果没有传送支付方式或者传送了无效的支付方式，返回错误
			if(!ProductOrder.hasPayMode(paymode)){
				//此处需要向客户端返回错误----------------------------------
				flag = CommentUtils.RES_FLAG_ERROR;
				error = "支付方式参数错误";
				datas.put("error", error);
				datas.put("flag", flag);
				//处理返回数据
				resString = getResponseData(datas);
				return resString;
			}
			
			int flagint = 0;
			Product product = null;
			if(ProductOrder.PAYMODE_APPLE.equals(paymode)){
				//苹果支付方式，商品查询是另一种方式
				String ptype = Product.TYPE_GOLD;
				product = productFacade.findProductByPriceAndType(pid, ptype);
			}else{
				product = productFacade.findProductByPid(pid);
			}
			if(product==null){
				//此处应该向客户端返回错误------------------------------------
				flag = CommentUtils.RES_FLAG_ERROR;
				error = "商品不存在";
				datas.put("error", error);
				datas.put("flag", flag);
				//处理返回数据
				resString = getResponseData(datas);
				return resString;
			}
			
			if(product!=null){
				flagint = product.getFlag();
			}
			Map<String, String> map = new HashMap<String, String>();
			if(product!=null && product.getId()>0){
				Map<String, String> inMap = new HashMap<String, String>();
				inMap.put("uid",uid+"");
				inMap.put("pid",product.getId()+"");
				inMap.put("amount",product.getPrice()+"");
				inMap.put("virtualamount",product.getVirtualprice()+"");
				inMap.put("title",product.getTitle());
				inMap.put("remark",product.getRemark());
				inMap.put("paymode",paymode);
				inMap.put("ptype",product.getPtype());//商品类型
				inMap.put("ip",ip);
				//生成订单，返回客户端付款操作需要的数据，需要区分具体支付方式
				if(ProductOrder.PAYMODE_ALIPAY.equals(paymode)){
					//支付宝支付逻辑
					map = productFacade.createAlipayOrder(inMap);
				}else if(ProductOrder.PAYMODE_WX.equals(paymode)){
					//微信支付逻辑
					if (ip == null && "".equals(ip.trim())) {				
						//此处应该向客户端返回错误------------------------------------
						flag = CommentUtils.RES_FLAG_ERROR;
						error = "微信支付，ip信息不能为空";
						datas.put("error", error);
						datas.put("flag", flag);
						//处理返回数据
						resString = getResponseData(datas);
						return resString;
					}
					map = productFacade.createWeixinPayOrder(inMap);
				}else if(ProductOrder.PAYMODE_APPLE.equals(paymode)){
					//苹果支付的逻辑
					map = productFacade.createAppleOrder(inMap);
				}
				String flagstr = map.get("flag");
				if(StringUtils.isInteger(flagstr)){
					flagint =Integer.valueOf(flagstr);
				}
			}
			if(flagint == ResultUtils.SUCCESS){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				map.remove("flag");	
				datas.put("map", map);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		//}catch(Exception e){
			//e.printStackTrace();
		//}
		//return "";
	}
	
	
	//虚拟金币购买章节(购买小说章节扣除金币，和更改小说章节付费状态)
	public String payStoryChapter(String reqs,Long uid){
		//try{
//				LOG.info("客户端json数据："+reqs);
			Map<String, Object> req =null;
			Map<String, Object> dataq=null;
			Map<String, Object> datas =null;
			String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
			String error="";
			String resString="";//返回数据
			//去掉空格
			if(reqs!=null){
				reqs = reqs.trim();
			}
			long cid = 0;//章节id
			String goldAmount = "0";
			//转化成可读类型
			try {
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				req = (Map<String, Object>) req.get("req");
				dataq = (Map<String, Object>) req.get("data");
				String cidStr = dataq.get("cid")+"";
				if(StringUtils.isInteger(cidStr)){
					cid = Long.valueOf(cidStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			datas = new HashMap<String, Object>();
			
			if(cid<=0){
				//此处应该向客户端返回错误------------------------------------
				flag = CommentUtils.RES_FLAG_ERROR;
				error = "章节信息不存在";
				datas.put("error", error);
				datas.put("flag", flag);
				//处理返回数据
				resString = getResponseData(datas);
				return resString;
			}
			
			String flagint = 0+"";
			StoryChapter storyChapter = storyFacade.findStoryChapterById(cid);
			if(storyChapter==null || (ResultUtils.DATAISNULL+"").equals(storyChapter.getFlag())){
				//此处应该向客户端返回错误------------------------------------
				flag = CommentUtils.RES_FLAG_ERROR;
				error = "章节信息不存在";
				datas.put("error", error);
				datas.put("flag", flag);
				//处理返回数据
				resString = getResponseData(datas);
				return resString;
			}
			if(storyChapter!=null){
				flagint = storyChapter.getFlag();
			}
			if((ResultUtils.SUCCESS+"").equals(flagint)){
				StoryPay storyPay = storyFacade.findStoryPayByChapterId(cid, uid);
				if(storyPay!=null && storyPay.getId()>0 && storyPay.getPayed()==1){
					//已经付过费
					flag = CommentUtils.RES_FLAG_ERROR;
					error = "该章节已经付过费了";
					datas.put("error", error);
					datas.put("flag", flag);
					//处理返回数据
					resString = getResponseData(datas);
					return resString;
				}else{
					//
					if(storyPay==null || (ResultUtils.DATAISNULL+"").equals(storyPay.getFlag()) || storyPay.getPayed()==0){
						//没有支付
						Map<String, String> inMap = new HashMap<String,String>(3);
						inMap.put("cid",cid+"");
						inMap.put("storyId", storyChapter.getStoryId()+"");
						inMap.put("uid", uid+"");
						inMap.put("amount",storyChapter.getPrice()+"");
						Map<String, String> map = productFacade.payStoryChapter(inMap);
						flagint = map.get("flag");
						String goldAmountStr = map.get("goldAmount");
						if(StringUtils.isInteger(goldAmountStr)){
							goldAmount = goldAmountStr;
						}
					}else{
						//查询出错
						flag = CommentUtils.RES_FLAG_ERROR;
						error = "该章节的付费信息查询失败";
						datas.put("error", error);
						datas.put("flag", flag);
						//处理返回数据
						resString = getResponseData(datas);
						return resString;
					}
				}
			}
			if((ResultUtils.SUCCESS+"").equals(flagint)){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				Map<String, Object> map = new HashMap<String,Object>(1);
				map.put("cid", cid+"");
				map.put("goldAmount", goldAmount);
				datas.put("map", map);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(Integer.valueOf(flagint));
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		//}catch(Exception e){
			//e.printStackTrace();
		//}
		//return "";
	}
	
	
	/**
	 * 支付宝返回支付结果信息
	 * @param reqs
	 * @return
	 */
	public String notifyPayResult(Map reqs) {	
		LOG.info(reqs);
		String resString = null;
		Map<String,String> params = new HashMap<String,String>();
		for (Iterator iter = reqs.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) reqs.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		System.out.println("验证之前"+params);
		if(!AlipayNotify.verify(params)){//验证成功
			resString = "fail";
			return resString;
		}		
		System.out.println(params);
		PayLog paylog = new PayLog();
		String notifyTime = params.get("notify_time");
		String notifyType = params.get("notify_type");
		String notifyId = params.get("notify_id");
		String signType = params.get("sign_type");
		String sign = params.get("sign");
		String outTradeNo = params.get("out_trade_no");
		String subject = params.get("subject");
		String paymentType = params.get("payment_type");
		String paymethod = ProductOrder.PAYMODE_ALIPAY;
		String tradeNo = params.get("trade_no");
		String tradeStatus = params.get("trade_status");
		String sellerEmail = params.get("seller_email");
		String sellerId = params.get("seller_id");
		String buyerId = params.get("buyer_id");
		String buyerEmail = params.get("buyer_email");
		String totalFee = params.get("total_fee");
		String body = params.get("body");
		String gmtCreate = params.get("gmt_create");
		String gmtPayment = params.get("gmt_payment");
		
		if (tradeStatus.equals(PaycenterConstant.TRADE_FINISHED) || tradeStatus.equals(PaycenterConstant.TRADE_SUCCESS)) {
			paylog.setNotifyTime(DateUtil.formatLong(notifyTime, "yyyy-MM-dd HH:mm:ss"));
			paylog.setNotifyType(notifyType);
			paylog.setNotifyId(notifyId);
			paylog.setSignType(signType);
			paylog.setSign(sign);
			paylog.setOutTradeNo(outTradeNo);
			paylog.setSubject(subject);
			paylog.setPaymentType(paymentType);
			paylog.setPaymethod(paymethod);
			paylog.setTradeNo(tradeNo);
			paylog.setTradeStatus(tradeStatus);	
			if (sellerId != null && !"".equals(sellerId)) {
				paylog.setParterId(PaycenterUtil.getPartnerIdInt(sellerId));
			}			
			if (sellerEmail != null && !"".equals(sellerEmail)) {
				paylog.setSellerId(PaycenterUtil.getAplipayValue(sellerEmail));
			}			
			paylog.setBuyerId(buyerId);
			paylog.setBuyerEmail(buyerEmail);
			if (totalFee != null && !"".equals(totalFee)) {
				paylog.setTotalFee(PaycenterUtil.yuanToFen(totalFee));
			}
			paylog.setBody(body);
			if (null != gmtCreate && !"".equals(gmtCreate)) {
				paylog.setGmtCreate(DateUtil.formatLong(gmtCreate, "yyyy-MM-dd HH:mm:ss"));
			}
			if (null != gmtPayment && !"".equals(gmtPayment)) {
				paylog.setGmtPayment(DateUtil.formatLong(gmtPayment, "yyyy-MM-dd HH:mm:ss"));
			}	
			Map<String, String> map = new HashMap<String, String>();	
			System.out.println("payLog:"+paylog);
			map = productFacade.notifyResult(paylog);
			int resFlag = Integer.valueOf(map.get("flag"));
			if (ResultUtils.SUCCESS == resFlag) {
				resString = "success";
			}else {
				resString = "fail";
			}
			return resString;
		}else {
			resString ="fail";
			return resString;
		}
	}
	
	/**
	 * 微信返回支付结果信息
	 * @param reqs
	 * @return
	 */
	public String notifyWeixinPayResult(String xmlstring) {
		LOG.info(xmlstring);
		String resString = null;
		//System.out.println("验证之前"+params);
		//Map<String,String> resMap = new HashMap<String,String>();
		try{
			if(xmlstring!=null && xmlstring.trim().startsWith("<xml>")){
				xmlstring = "<?xml   version=\"1.0\"   encoding=\"gb2312\"?>"+xmlstring;
			}
			Map<String,Object> params = XMLParser.getMapFromXML(xmlstring);
			
			if(!WXPay.verify(params)){//验证失败
				//resMap.put("return_code", "FAIL");
				//resMap.put("return_msg", "签名失败");
				
				//resString = WXPay.changeObjectToXml(resMap);
				resString = returnWeixinStr("FAIL","签名失败");
				return resString;
			}
			System.out.println(params);
			PayLog paylog = new PayLog();
			String notifyTime = "";
			String notifyType = "weixin_notify";
			String notifyId = "";
			String signType = "";
			String sign = (String)params.get("sign");
			String outTradeNo = (String)params.get("out_trade_no");
			String subject = "";
			String paymentType = "";
			String paymethod = ProductOrder.PAYMODE_WX;
			String tradeNo = (String)params.get("transaction_id ");//微信支付订单号
			String tradeStatus = (String)params.get("result_code");
			String sellerEmail = "";
			String sellerId = "0";//(String) params.get("mch_id");
			String buyerId = "0";//(String) params.get("openid");
			String buyerEmail = "";
			String totalFee = (String) params.get("total_fee");
			String body = "";
			String gmtCreate = "";
			String gmtPayment = "";
			
			if ("SUCCESS".equals(tradeStatus)) {
				paylog.setNotifyTime(System.currentTimeMillis());
				paylog.setNotifyType(notifyType);
				paylog.setNotifyId(notifyId);
				paylog.setSignType(signType);
				paylog.setSign(sign);
				paylog.setOutTradeNo(outTradeNo);
				paylog.setSubject(subject);
				paylog.setPaymentType(paymentType);
				paylog.setPaymethod(paymethod);
				paylog.setTradeNo(tradeNo);
				paylog.setTradeStatus(tradeStatus);	
				if (sellerId != null && !"".equals(sellerId)) {
					paylog.setParterId(0);
				}			
				if (sellerEmail != null && !"".equals(sellerEmail)) {
					paylog.setSellerId(0);
				}			
				paylog.setBuyerId(buyerId);
				paylog.setBuyerEmail(buyerEmail);
				if (totalFee != null && !"".equals(totalFee)) {
					if(StringUtils.isInteger(totalFee)){
						paylog.setTotalFee(Integer.valueOf(totalFee));
					}
				}
				paylog.setBody(body);
				if (null != gmtCreate && !"".equals(gmtCreate)) {
					paylog.setGmtCreate(DateUtil.formatLong(gmtCreate, "yyyy-MM-dd HH:mm:ss"));
				}
				if (null != gmtPayment && !"".equals(gmtPayment)) {
					paylog.setGmtPayment(DateUtil.formatLong(gmtPayment, "yyyy-MM-dd HH:mm:ss"));
				}	
				Map<String, String> map = new HashMap<String, String>();	
				System.out.println("payLog:"+paylog);
				map = productFacade.notifyResult(paylog);
				int resFlag = Integer.valueOf(map.get("flag"));
				if (ResultUtils.SUCCESS == resFlag) {
					//resString = "success";
				}else {
					//resString = "fail";
				}
				//return resString;
			}else {
				//resString ="fail";
				//return resString;
			}
			//resMap.put("return_code", "SUCCESS");
			resString = returnWeixinStr("SUCCESS","");
			//resString = WXPay.changeObjectToXml(resMap);
			return resString;
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(e);
		}
		//resMap.put("return_code", "FAIL");
		//resMap.put("return_msg", "参数格式校验错误");
		resString = returnWeixinStr("FAIL","参数格式校验错误");
		return resString;
	}
	//组织成返回给微信的格式的数据
	private String returnWeixinStr(String return_code,String return_msg){
		String str = "<?xml   version=\"1.0\"   encoding=\"gb2312\"?><xml><return_code><![CDATA["+return_code+"]]></return_code><return_msg><![CDATA["+return_msg+"]]></return_msg></xml> ";
		return str;
	}
	
	/**
	 * 主动查询微信支付结果信息
	 * @param reqs
	 * @return
	 */
	public String queryWeixinPayResult(String reqs, long uid) {
		LOG.info(reqs);
		
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		//去掉空格
		reqs = reqs.trim();
		String transaction_id="";          	//微信的订单号，优先使用 
		String out_trade_no="";  	//商户系统内部的订单号，当没提供transaction_id时需要传这个。 
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			if(dataq.get("transaction_id")!=null){
				transaction_id = (String)dataq.get("transaction_id");
			}
			if(dataq.get("out_trade_no")!=null){
				out_trade_no = (String)dataq.get("out_trade_no");
			}
			if ((out_trade_no == null || "".equals(out_trade_no)) && (transaction_id == null || "".equals(transaction_id))) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "out_trade_no", datas);
				return resString;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData(transaction_id,out_trade_no,null);
		
		try{
			String xmlString = WXPay.requestScanPayQueryService(scanPayQueryReqData);
			
			PaycenterController.queryreturnxml=xmlString;
			
			if(xmlString!=null && xmlString.trim().startsWith("<xml>")){
				xmlString = "<?xml   version=\"1.0\"   encoding=\"gb2312\"?>"+xmlString;
			}
			//String xmlString = "<?xml   version=\"1.0\"   encoding=\"UTF8\"?><xml>    <return_code><![CDATA[SUCCESS]]></return_code>    <return_msg><![CDATA[OK]]></return_msg>    <appid><![CDATA[wx1441086740e20837]]></appid>    <mch_id><![CDATA[10000100]]></mch_id>    <device_info><![CDATA[1000]]></device_info>    <nonce_str><![CDATA[TN55wO9Pba5yENl8]]></nonce_str>    <sign><![CDATA[BDF0099C15FF7BC6B1585FBB110AB635]]></sign>    <result_code><![CDATA[SUCCESS]]></result_code>    <openid><![CDATA[oUpF8uN95-Ptaags6E_roPHg7AG0]]></openid><is_subscribe><![CDATA[Y]]></is_subscribe><trade_type><![CDATA[MICROPAY]]></trade_type>    <bank_type><![CDATA[CCB_DEBIT]]></bank_type><total_fee>100</total_fee><fee_type><![CDATA[CNY]]></fee_type><transaction_id><![CDATA[1008450740201411110005820873]]></transaction_id><out_trade_no><![CDATA[a_100000000000192]]></out_trade_no>    <attach><![CDATA[订单额外描述]]></attach><time_end><![CDATA[20141111170043]]></time_end><trade_state><![CDATA[SUCCESS]]></trade_state></xml>";
			
			//需要解析返回值
			Map<String,Object> resultMap = XMLParser.getMapFromXML(xmlString);
			
			if(resultMap!=null && resultMap.size()>0){
				if("SUCCESS".equals(resultMap.get("return_code"))){
					if(!WXPay.verify(resultMap)){//验证失败
						//resMap.put("return_code", "FAIL");
						//resMap.put("return_msg", "签名失败");
						
						//resString = WXPay.changeObjectToXml(resMap);
						resString = "签名验证失败,请求微信返回数据为："+xmlString;
						return resString;
					}
				}else{
					resString = "微信返回错误信息:"+resultMap.get("return_msg")+",请求微信返回数据为："+xmlString;
					return resString;
				}
				if("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))){
					//查看支付结果
					String trade_state  = (String) resultMap.get("trade_state");
					if("SUCCESS".equals(trade_state)){
						PayLog paylog = new PayLog();
						String notifyTime = "";
						String notifyType = "query_weixin";
						String notifyId = "";
						String signType = "";
						String sign = (String)resultMap.get("sign");
						String outTradeNo = (String)resultMap.get("out_trade_no");
						String subject = "";
						String paymentType = "";
						String paymethod = ProductOrder.PAYMODE_WX;
						String tradeNo = (String)resultMap.get("transaction_id");//微信支付订单号
						String tradeStatus = (String)resultMap.get("trade_state");
						String sellerEmail = "";
						String sellerId = "";
						String buyerId = "";
						String buyerEmail = "";
						String totalFee = (String) resultMap.get("total_fee");
						String body = "";
						String gmtCreate = "";
						String gmtPayment = "";

						paylog.setNotifyTime(System.currentTimeMillis());
						paylog.setNotifyType(notifyType);
						paylog.setNotifyId(notifyId);
						paylog.setSignType(signType);
						paylog.setSign(sign);
						paylog.setOutTradeNo(outTradeNo);
						paylog.setSubject(subject);
						paylog.setPaymentType(paymentType);
						paylog.setPaymethod(paymethod);
						paylog.setTradeNo(tradeNo);
						paylog.setTradeStatus(tradeStatus);	
						if (sellerId != null && !"".equals(sellerId)) {
							paylog.setParterId(0);
						}			
						if (sellerEmail != null && !"".equals(sellerEmail)) {
							paylog.setSellerId(0);
						}			
						paylog.setBuyerId(buyerId);
						paylog.setBuyerEmail(buyerEmail);
						if (totalFee != null && !"".equals(totalFee)) {
							if(StringUtils.isInteger(totalFee)){
								paylog.setTotalFee(Integer.valueOf(totalFee));
							}
						}
						paylog.setBody(body);
						if (null != gmtCreate && !"".equals(gmtCreate)) {
							paylog.setGmtCreate(DateUtil.formatLong(gmtCreate, "yyyy-MM-dd HH:mm:ss"));
						}
						if (null != gmtPayment && !"".equals(gmtPayment)) {
							paylog.setGmtPayment(DateUtil.formatLong(gmtPayment, "yyyy-MM-dd HH:mm:ss"));
						}
						Map<String, String> map = new HashMap<String, String>();	
						System.out.println("payLog:"+paylog);
						map = productFacade.notifyResult(paylog);
						int resFlag = Integer.valueOf(map.get("flag"));
						if (ResultUtils.SUCCESS == resFlag) {
							resString = "微信支付成功，业务操作也成功";
						}else {
							resString = "微信支付成功，业务操作失败";
						}
						//return resString;
					}else {
						resString ="微信支付结果未成功，trade_state:"+trade_state;
						//return resString;
					}
				}else{
					resString = "return_code:"+resultMap.get("return_code")+" result_code："+resultMap.get("result_code");
				}
			}else{
				resString = "解析的map集合为空，请求微信返回数据为："+xmlString;
			}
		}catch(Exception e){
			LOG.error(e);
			resString = "异常了:"+e.getMessage();
		}
		return resString;
	}
	
	
	/** 
     * 客户端向服务器验证 
     *  
     *  
     *   * checkState  A  验证成功有效(返回收据) 
     *             B  账单有效，但己经验证过 
     *             C  服务器数据库中没有此账单(无效账单) 
     *             D  不处理 
     *  
     * @return 
     * @throws IOException  
     */  
    public String IOSVerify(String reqs, long uid){
    	Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		//去掉空格
		reqs = reqs.trim();
		String receipt="";   //微信的订单号，优先使用 
		String out_trade_no="";//商户系统内部的订单号，当没提供transaction_id时需要传这个。 
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			if(dataq.get("receipt")!=null){
				receipt = (String)dataq.get("receipt");
			}
			if(dataq.get("out_trade_no")!=null){
				out_trade_no = (String)dataq.get("out_trade_no");
			}
			if (receipt == null || "".equals(receipt.trim())) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "receipt", datas);
				return resString;
			}
			if (out_trade_no == null || "".equals(out_trade_no.trim())) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "out_trade_no", datas);
				return resString;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
        //System.out.println(new  Date().toLocaleString()+"  来自苹果端的验证...");
        //苹果客户端传上来的收据,是最原据的收据  
        //System.out.println(receipt);
        //拿到收据的MD5
        String md5_receipt=MD5Utils.md5(receipt);
        //默认是无效账单  
        String result="初始状态";
        //查询数据库，看是否是己经验证过的账号  
        ProductOrder productOrder=productFacade.findProductOrderByTn(md5_receipt);
        String verifyResult=null;
        if(productOrder==null || (productOrder.getId()==0 && productOrder.getFlag()!=ResultUtils.QUERY_ERROR)){
            verifyResult=ApplePayQuery.buyAppVerify(receipt);
            //System.out.println(verifyResult);
            if(verifyResult==null){
                //苹果服务器没有返回验证结果  
                result="苹果服务器没有返回验证结果";
            }else{
            	try{
            		//跟苹果验证有返回结果------------------
                    Map<String, Object> resmap = getObjectMapper().readValue(verifyResult,  new TypeReference<Map<String, Object>>(){});
                    String states=resmap.get("status")+"";  
                    if("0".equals(states))//验证成功  
                    {  
                    	LinkedHashMap linkedHashMap = (LinkedHashMap) resmap.get("receipt");
                        //String r_receipt=(String) resmap.get("receipt");  
                        //Map<String, Object> resmap2 = getObjectMapper().readValue(r_receipt,  new TypeReference<Map<String, Object>>(){});
                        //产品ID  
                        String product_id=(String) linkedHashMap.get("product_id");  
                        //数量  
                        String quantityStr=linkedHashMap.get("quantity")+"";
                        //跟苹果的服务器验证成功  
                        result="跟苹果的服务器验证成功";  
                        //交易日期  
                        String purchase_date=(String) linkedHashMap.get("purchase_date");  
                        //交易成功的后续逻辑处理
                        
                        int quantity = 1;
                        if(StringUtils.isInteger(quantityStr)){
                        	quantity = Integer.valueOf(quantityStr);
                        }
                        PayLog paylog = new PayLog();
    					String notifyTime = "";
    					String notifyType = "query_apple";
    					String notifyId = "";
    					String signType = "";
    					String sign = "";
    					String outTradeNo = out_trade_no;
    					String subject = "";
    					String paymentType = "";
    					String paymethod = ProductOrder.PAYMODE_APPLE;
    					String tradeNo = md5_receipt;//苹果原始收据的MD5加密数据
    					String tradeStatus = states;
    					String sellerEmail = "";
    					String sellerId = "";
    					String buyerId = "";
    					String buyerEmail = "";
    					String totalFee = "";
    					String body = product_id;
    					String gmtCreate = "";
    					String gmtPayment = purchase_date;

    					paylog.setQuantity(quantity);
    					paylog.setNotifyTime(System.currentTimeMillis());
    					paylog.setNotifyType(notifyType);
    					paylog.setNotifyId(notifyId);
    					paylog.setSignType(signType);
    					paylog.setSign(sign);
    					paylog.setOutTradeNo(outTradeNo);
    					paylog.setSubject(subject);
    					paylog.setPaymentType(paymentType);
    					paylog.setPaymethod(paymethod);
    					paylog.setTradeNo(tradeNo);
    					paylog.setTradeStatus(tradeStatus);	
    					if (sellerId != null && !"".equals(sellerId)) {
    						paylog.setParterId(0);
    					}
    					if (sellerEmail != null && !"".equals(sellerEmail)) {
    						paylog.setSellerId(0);
    					}
    					paylog.setBuyerId(buyerId);
    					paylog.setBuyerEmail(buyerEmail);
    					if (totalFee != null && !"".equals(totalFee)) {
    						if(StringUtils.isInteger(totalFee)){
    							paylog.setTotalFee(Integer.valueOf(totalFee));
    						}
    					}
    					paylog.setBody(body);
    					if (null != gmtCreate && !"".equals(gmtCreate)) {
    						paylog.setGmtCreate(DateUtil.formatLong(gmtCreate, "yyyy-MM-dd HH:mm:ss"));
    					}
    					if (null != gmtPayment && !"".equals(gmtPayment)) {
    						paylog.setGmtPayment(DateUtil.formatLong(gmtPayment, "yyyy-MM-dd HH:mm:ss"));
    					}
    					Map<String, String> map = new HashMap<String, String>();	
    					System.out.println("payLog:"+paylog);
    					map = productFacade.notifyResult(paylog);
    					int resFlag = Integer.valueOf(map.get("flag"));
    					if (ResultUtils.SUCCESS == resFlag) {
    						flag = CommentUtils.RES_FLAG_SUCCESS;
    						String goldAmount = "0";
    						String goldAmountStr = map.get("goldAmount");
    						if(StringUtils.isInteger(goldAmountStr)){
    							goldAmount = goldAmountStr;
    						}
    						Map<String, Object> map2 = new HashMap<String,Object>(1);
    						map2.put("goldAmount", goldAmount);
    						datas.put("map", map2);
    						result = "苹果支付成功，业务操作也成功";
    					}else {
    						result = "苹果支付成功，业务操作失败";
    					}
                    }else{
                        //账单无效  
                        result="账单无效";
                    }
            	}catch(Exception e){
            		e.printStackTrace();
            		result = "服务器异常";
            	}
                //跟苹果验证有返回结果------------------  
            }
            //传上来的收据有购买信息==end=============  
        }else{
            //账单有效，但己验证过  
            result="账单有效，但己验证过";  
        }
        datas.put("flag",flag);
        datas.put("error",result);
        //处理返回数据
  		resString = getResponseData(datas);
  		return resString;
    }
	
	
	/**
	 * 必输参数需要输入时的判断
	 * @param flag
	 * @param service
	 * @param datas
	 * @return
	 */
	private String returnErrorStr(String flag, String service, Map<String, Object> datas) {
		String str = null;
		String error = MessageUtils.getResultMessage(ResultUtils.NOT_NULL);
		LOG.error(flag);
		datas.put("error", service+error);
		datas.put("flag", flag);
		//处理返回数据
		str = getResponseData(datas);
		return str;
	}
	
	private String returnErrorStr(String flag, String fieldName, Map<String, Object> datas, int resultCode) {
		String str = null;
		String error = MessageUtils.getResultMessage(resultCode);
		LOG.error(flag);
		datas.put("error", error);
		datas.put("flag", flag);
		//处理返回数据
		str = getResponseData(datas);
		return str;
	}
}
