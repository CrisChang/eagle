package com.poison.eagle.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.alipay.util.AlipayNotify;
import com.poison.eagle.action.PaycenterController;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.MD5Utils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.PaycenterUtil;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SIDHelper;
import com.poison.eagle.utils.StringUtils;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.paycenter.constant.PaycenterConstant;
import com.poison.paycenter.ext.wx.WXPay;
import com.poison.paycenter.ext.wx.common.XMLParser;
import com.poison.paycenter.ext.wx.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.poison.paycenter.model.AccAmt;
import com.poison.paycenter.model.AccSet;
import com.poison.paycenter.model.AccTakeRecord;
import com.poison.paycenter.model.AccTakeSet;
import com.poison.paycenter.model.PayLog;


/**
 * @author yan_dz
 *
 */
public class PaycenterManager extends BaseManager{
	/**
	 * 日志
	 */
	private static final Log LOG = LogFactory.getLog(PaycenterManager.class);

	private PaycenterFacade paycenterFacade;

	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}
	/**
	 * 生成调用支付宝的订单，并存储一条打赏记录
	 * @param uid 
	 * @param reqs 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String createReloadOrderInfo(String reqs, long uid) {
		System.out.println(reqs);
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		//去掉空格
		reqs = reqs.trim();
		String userId;          	//用户Id号
		String userName;  	//用户登陆号
		String totalFee;		  	//打赏金额
		String service;		//调用接口名称
		String rewardUserId;
		String sourceId;
		String sourceName;
		String sourceType;
		String rewardUserName;
		String rewardPostscript;
		int rewardtype = 1;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			userId = (String)dataq.get("user_id");
			if (userId == null && "".equals(userId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "user_id", datas);
				return resString;				
			}
			if ("0".equals(userId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas, ResultUtils.NO_EXISTED_USER);
				return resString;
			}
			userName = (String)dataq.get("user_name");			
			totalFee = dataq.get("total_fee").toString();
			if (totalFee == null && "".equals(totalFee)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "total_fee", datas);
				return resString;
			}
			service = (String) dataq.get("service");
			if (service == null && "".equals(service)) {				
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "service", datas);
				return resString;				
			}
			rewardUserId = (String)dataq.get("reward_user_id");
			if (rewardUserId == null && "".equals(rewardUserId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas);
				return resString;	
			}
			if ("0".equals(rewardUserId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas, ResultUtils.NO_EXISTED_USER);
				return resString;
			}
			rewardUserName = (String) dataq.get("reward_user_name");
			rewardPostscript = (String) dataq.get("reward_postscript");
			sourceId = (String)dataq.get("source_id");	
			if (sourceId == null && "".equals(sourceId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_id", datas);
				return resString;	
			}
			if ("0".equals(rewardUserId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_id", datas, ResultUtils.NOT_NULL);
				return resString;
			}
			sourceType = (String)dataq.get("source_type"); 	
			if (sourceType == null && "".equals(sourceType)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_type", datas);
				return resString;	
			}
			sourceName = (String) dataq.get("source_name");
			String rewardtypestr = (String)dataq.get("reward_type");
			if(StringUtils.isInteger(rewardtypestr)){
				rewardtype = Integer.valueOf(rewardtypestr);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		//调用后台服务生成充值打赏订单，并存储与打赏有关的各个数据表
		Map<String, String> inMap = new HashMap<String, String>();
		inMap.put("userId", userId);
		inMap.put("userName", userName);
		inMap.put("totalFee", totalFee);
		inMap.put("service", service);
		inMap.put("rewardUserId", rewardUserId);
		inMap.put("rewardPostscript", rewardPostscript);
		inMap.put("rewardUserName", rewardUserName);
		inMap.put("sourceId", sourceId);
		inMap.put("sourceName", sourceName);
		inMap.put("sourceType", sourceType);
		inMap.put("rewardtype", rewardtype+"");
		//生成充值订单，并存储打赏业务记录
		Map<String, String> map = new HashMap<String, String>();
		map = paycenterFacade.createReloadOrderInfoByMap(inMap);
		int resFlag = Integer.valueOf(map.get("flag"));			
		if (ResultUtils.SUCCESS == resFlag) {
			map.remove("flag");	
			datas.put("map", map);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(resFlag);
			LOG.error(map.get("flag"));
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
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
			map = paycenterFacade.notifyResult(paylog);
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
				map = paycenterFacade.notifyResult(paylog);
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
						map = paycenterFacade.notifyResult(paylog);
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

	
	/**
	 * 生成调用微信支付的订单，并存储一条打赏记录
	 * @param uid 
	 * @param reqs 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String createWeixinReloadOrderInfo(String reqs, long uid) {
		System.out.println(reqs);
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		//去掉空格
		reqs = reqs.trim();
		String userId;          	//用户Id号
		String userName;  	//用户登陆号
		String totalFee;		  	//打赏金额
		String rewardUserId;
		String sourceId;
		String sourceName;
		String sourceType;
		String rewardUserName;
		String rewardPostscript;
		String ip;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			userId = (String)dataq.get("user_id");
			if (userId == null && "".equals(userId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "user_id", datas);
				return resString;				
			}
			if ("0".equals(userId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas, ResultUtils.NO_EXISTED_USER);
				return resString;
			}
			userName = (String)dataq.get("user_name");			
			totalFee = dataq.get("total_fee").toString();
			if (totalFee == null && "".equals(totalFee)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "total_fee", datas);
				return resString;
			}
			ip = (String) dataq.get("ip");
			if (ip == null && "".equals(ip)) {				
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "ip", datas);
				return resString;				
			}
			rewardUserId = (String)dataq.get("reward_user_id");
			if (rewardUserId == null && "".equals(rewardUserId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas);
				return resString;	
			}
			if ("0".equals(rewardUserId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas, ResultUtils.NO_EXISTED_USER);
				return resString;
			}
			rewardUserName = (String) dataq.get("reward_user_name");
			rewardPostscript = (String) dataq.get("reward_postscript");
			sourceId = (String)dataq.get("source_id");	
			if (sourceId == null && "".equals(sourceId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_id", datas);
				return resString;	
			}
			if ("0".equals(rewardUserId)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_id", datas, ResultUtils.NOT_NULL);
				return resString;
			}
			sourceType = (String)dataq.get("source_type"); 	
			if (sourceType == null && "".equals(sourceType)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_type", datas);
				return resString;	
			}
			sourceName = (String) dataq.get("source_name");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		//调用后台服务生成充值打赏订单，并存储与打赏有关的各个数据表
		Map<String, String> inMap = new HashMap<String, String>();
		inMap.put("userId", userId);
		inMap.put("userName", userName);
		inMap.put("totalFee", totalFee);
		inMap.put("ip", ip);
		inMap.put("rewardUserId", rewardUserId);
		inMap.put("rewardPostscript", rewardPostscript);
		inMap.put("rewardUserName", rewardUserName);
		inMap.put("sourceId", sourceId);
		inMap.put("sourceName", sourceName);
		inMap.put("sourceType", sourceType);
		//生成充值订单，并存储打赏业务记录
		Map<String, String> map = new HashMap<String, String>();
		map = paycenterFacade.createWeixinReloadOrderInfoByMap(inMap);
		int resFlag = Integer.valueOf(map.get("flag"));			
		if (ResultUtils.SUCCESS == resFlag) {
			map.remove("flag");
			String out_trade_no = map.get("out_trade_no");
			map.remove("out_trade_no");
			datas.put("map", map);
			datas.put("out_trade_no",out_trade_no);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(resFlag);
			LOG.error(map.get("flag"));
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	
	/**
	 * 查询打赏条数
	 * @param reqs
	 * @return
	 */
	public String selectCountReward(String reqs) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String userId ;
		String userName;
		String userType;
		String sourceId;
		String sourceType;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");	
			userId = (String) dataq.get("user_id");
			userName = (String) dataq.get("user_name");
			userType = (String) dataq.get("user_type");
			sourceId = (String) dataq.get("source_id");
			sourceType = (String) dataq.get("source_type");			
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("userId", userId);
		inMap.put("userName", userName);
		inMap.put("userType", userType);
		inMap.put("sourceId", sourceId);
		inMap.put("sourceType", sourceType);
		System.out.println(inMap);
		Map<String, Object> countMap = paycenterFacade.selectCountReward(inMap);
		int resFlag = Integer.valueOf((String)countMap.get("flag"));			
		if (ResultUtils.SUCCESS == resFlag) {
			countMap.remove("flag");
			datas.put("map", countMap);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(resFlag);
			LOG.error(countMap.get("flag"));
			datas.put("error", error);
		}		
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}


	/**
	 * 查询打赏明细
	 * @param reqs
	 * @return
	 */
	public String selectRewardDetail(String reqs) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String userId ;
		String userName;
		String userType;
		String rewardTypeId;
		String rewardType;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");	
			userId = (String) dataq.get("user_id");
			userName = (String) dataq.get("user_name");
			userType = (String) dataq.get("user_type");
			rewardTypeId = (String) dataq.get("reward_type_id");
			rewardType = (String) dataq.get("reward_type");			
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("userId", userId);
		inMap.put("userName", userName);
		inMap.put("userType", userType);
		inMap.put("rewardTypeId", rewardTypeId);
		inMap.put("rewardType", rewardType);
		Map<String, Object> outMap = paycenterFacade.selectRewardDetail(inMap);
		
		int resFlag = (Integer)outMap.get("flag");
		//List<RewardDetail> rewardDetailList = new ArrayList<RewardDetail>();
		//outMap.remove("flag");	
		
		if (ResultUtils.SUCCESS == resFlag) {
			//rewardDetailList = (List<RewardDetail>) outMap.get("rewardDetailList");
			outMap.remove("flag");
			datas.put("map", outMap);			
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(resFlag);
			LOG.error(outMap.get("flag"));
			datas.put("error", error);
		}		
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
		
	}
	
	/**
	 * 查询固定打赏金额
	 * @param reqs
	 * @return
	 */
	public String getRewardFixedAmount(String reqs) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		/*try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");		
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}*/
		
		flag = CommentUtils.RES_FLAG_SUCCESS;	
		datas.put("flag", flag);
		datas.put("amount", 100);//固定金额，单位分
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}


	/**
	 * 生成摇一摇信息
	 * @param reqs
	 * @param uid 
	 * @return
	 */
	public String createRollInfo(String reqs, long uid) {
		System.out.println(reqs);
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		//去掉空格
		reqs = reqs.trim();
		long userId;          	//用户Id号
		String userIdStr;
		String userNameStr;
		String rewardUserIdStr;
		String rewardUserNameStr;
		String sourceIdStr;
		String sourceNameStr;
		String sourceTypeStr;
		
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			System.out.println(dataq);
			userIdStr = (String) dataq.get("user_id");
			userNameStr = (String) dataq.get("user_name");
			rewardUserIdStr = (String) dataq.get("reward_user_id");
			rewardUserNameStr = (String) dataq.get("reward_user_name");
			sourceIdStr = (String) dataq.get("source_id");
			sourceNameStr = (String) dataq.get("source_name");
			sourceTypeStr = (String) dataq.get("source_type");
			
			if (userIdStr == null && "".equals(userIdStr)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "user_id", datas);
				return resString;
			}
			if ("0".equals(userIdStr)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "user_id", datas, ResultUtils.NO_EXISTED_USER);
				return resString;
			}
			if (rewardUserIdStr == null && "".equals(rewardUserIdStr)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas);
				return resString;
			}	
			if ("0".equals(rewardUserIdStr)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "reward_user_id", datas, ResultUtils.NO_EXISTED_USER);
				return resString;
			}
			if (sourceIdStr == null && "".equals(sourceIdStr)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_id", datas);
				return resString;
			}
			if (sourceTypeStr == null || "".equals(sourceTypeStr)) {
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "source_type", datas);
				return resString;
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		if (Long.valueOf(userIdStr).compareTo(Long.valueOf(rewardUserIdStr))==0){
//			resString = returnErrorStr1(CommentUtils.RES_FLAG_ERROR, datas);
//			return resString;
//		}
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("userId", userIdStr);
		inMap.put("userName", userNameStr);
		inMap.put("rewardUserId", rewardUserIdStr);
		inMap.put("rewardUserName", rewardUserNameStr);
		inMap.put("sourceId", sourceIdStr);				
		inMap.put("sourceName", sourceNameStr);
		inMap.put("sourceType", sourceTypeStr);	
		
		Map<String, Object> map = paycenterFacade.createRollInfo(inMap);
		int resFlag = (Integer)map.get("flag");
		if (ResultUtils.SUCCESS == resFlag) {
			map.remove("flag");
			datas.put("map", map);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(resFlag);
			LOG.error(map.get("flag"));
			datas.put("error", error);
		}		
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}

	/**
	 * 摇一摇打赏成功返回信息
	 * @param reqs
	 * @return
	 */
	public String backRollSuccess(String reqs) {
		System.out.println(reqs);
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		//去掉空格
		reqs = reqs.trim();
		String userIdStr;
		String userNameStr;
		String postscriptStr;
		String rewardIdStr;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			System.out.println(dataq);
			userIdStr = (String)dataq.get("user_id");
			userNameStr = (String)dataq.get("user_name");
			postscriptStr = (String)dataq.get("reward_postscript");
			rewardIdStr = (String)dataq.get("reward_id");			
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("userId", userIdStr);
		reqMap.put("userName", userNameStr);
		reqMap.put("postscript", postscriptStr);
		reqMap.put("rewardId", rewardIdStr);		
		Map<String, Object> resMap = paycenterFacade.backRollInfo(reqMap);
		int resFlag =  Integer.valueOf((String)resMap.get("flag"));
		if(resFlag == ResultUtils.SUCCESS) {
			resMap.remove("flag"); 
			datas.put("map", resMap);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(resFlag);
			LOG.error(resMap.get("flag"));
			datas.put("error", error);
		}

		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}	
	
	/**
	 *设置提现密码
	 * @param reqs
	 * @return
	 */
	public String setTakePassword(String reqs,Long uid) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String password;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			password = (String) dataq.get("password");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		if(password==null || password.length()==0){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "password", datas, ResultUtils.PASSWORD_ISNULL);
			return resString;
		}
		AccTakeSet accTakeSet = paycenterFacade.selectAccTakeSetByUserId(uid);
		if(accTakeSet!=null && accTakeSet.getPassword()!=null && accTakeSet.getPassword().length()>0){
			//已经设置了密码了，抛出错误
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(ResultUtils.REPEAT_TAKE_PASSWORD);
			datas.put("error", error);
		}else{
			//
			if(accTakeSet.getFlag()==ResultUtils.DATAISNULL){
				//无数据
				accTakeSet = new AccTakeSet();
				accTakeSet.setCreateTime(System.currentTimeMillis());
				accTakeSet.setExtendField1("");
				accTakeSet.setExtendField2("");
				accTakeSet.setExtendField3("");
				accTakeSet.setPassword(MD5Utils.md5(password));
				accTakeSet.setUpdateTime(System.currentTimeMillis());
				accTakeSet.setUserId(uid);
				int result = paycenterFacade.insertAccTakeSet(accTakeSet);
				if(ResultUtils.SUCCESS==result){
					//插入成功了
					flag = CommentUtils.RES_FLAG_SUCCESS;
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(result);
					datas.put("error", error);
				}
			}else if(accTakeSet.getFlag()==ResultUtils.SUCCESS){
				//有数据，但是无密码
				accTakeSet.setPassword(MD5Utils.md5(password));
				accTakeSet.setUpdateTime(System.currentTimeMillis());
				accTakeSet.setUserId(uid);
				int result = paycenterFacade.updateAccTakeSet(accTakeSet);
				if(ResultUtils.SUCCESS==result){
					//更新成功了
					flag = CommentUtils.RES_FLAG_SUCCESS;
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(result);
					datas.put("error", error);
				}
			}else{
				//查询出错
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(accTakeSet.getFlag());
				datas.put("error", error);
			}
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 *获取提现密码是否存在了
	 * @param reqs
	 * @return
	 */
	public String getTakePasswordStatus(String reqs,Long uid) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		AccTakeSet accTakeSet = paycenterFacade.selectAccTakeSetByUserId(uid);
		if(accTakeSet!=null && accTakeSet.getPassword()!=null && accTakeSet.getPassword().length()>0){
			//已经设置了密码了
			flag = CommentUtils.RES_FLAG_SUCCESS;
			Map<String,Object> resMap= new HashMap<String,Object>();
			resMap.put("status", "1");
			datas.put("map", resMap);
		}else{
			//
			if(accTakeSet.getFlag()==ResultUtils.DATAISNULL){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				Map<String,Object> resMap= new HashMap<String,Object>();
				resMap.put("status", "0");
				datas.put("map", resMap);
			}else if(accTakeSet.getFlag()==ResultUtils.SUCCESS){
				//有数据，但是无密码
				flag = CommentUtils.RES_FLAG_SUCCESS;
				Map<String,Object> resMap= new HashMap<String,Object>();
				resMap.put("status", "0");
				datas.put("map", resMap);
			}else{
				//查询出错
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(accTakeSet.getFlag());
				datas.put("error", error);
			}
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 *修改提现密码
	 * @param reqs
	 * @return
	 */
	public String updateTakePassword(String reqs,Long uid) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String password;
		String oldpassword;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			password = (String) dataq.get("password");
			oldpassword = (String) dataq.get("oldpassword");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		if(oldpassword==null || oldpassword.length()==0){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "oldpassword", datas, ResultUtils.PASSWORD_ISNULL);
			return resString;
		}
		if(password==null || password.length()==0){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "password", datas, ResultUtils.PASSWORD_ISNULL);
			return resString;
		}
		AccTakeSet accTakeSet = paycenterFacade.selectAccTakeSetByUserId(uid);
		if(accTakeSet!=null && accTakeSet.getPassword()!=null && accTakeSet.getPassword().length()>0){
			//已经设置了密码了
			if(MD5Utils.md5(oldpassword).equals(accTakeSet.getPassword())){
				//旧密码正确，允许修改
				accTakeSet.setPassword(MD5Utils.md5(password));
				accTakeSet.setUpdateTime(System.currentTimeMillis());
				accTakeSet.setUserId(uid);
				int result = paycenterFacade.updateAccTakeSet(accTakeSet);
				if(ResultUtils.SUCCESS==result){
					//更新成功了
					flag = CommentUtils.RES_FLAG_SUCCESS;
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(result);
					datas.put("error", error);
				}
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(ResultUtils.OLDPASSWORD_WRONG);
				datas.put("error", error);
			}
		}else{
			//
			if(accTakeSet.getFlag()==ResultUtils.DATAISNULL){
				//无数据
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(ResultUtils.OLDPASSWORD_WRONG);
				datas.put("error", error);
			}else if(accTakeSet.getFlag()==ResultUtils.SUCCESS){
				//有数据，但是无密码
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(ResultUtils.OLDPASSWORD_WRONG);
				datas.put("error", error);
			}else{
				//查询出错
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(accTakeSet.getFlag());
				datas.put("error", error);
			}
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 *提交提现订单
	 * @param reqs
	 * @return
	 */
	public String saveTakeRecord(String reqs,Long uid) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String password;
		int take_type;//提现类型：1支付宝，2银行卡
		long take_fee;
		String receive_account;
		String receive_name;
		String receive_bank;
		String sid;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			password = (String) dataq.get("password");
			take_type = Integer.valueOf(dataq.get("take_type")+"");
			take_fee = Long.valueOf(dataq.get("take_fee")+"");
			receive_account = (String)dataq.get("receive_account");
			receive_name = (String)dataq.get("receive_name");
			receive_bank = (String)dataq.get("receive_bank");
			sid = (String) dataq.get("sid");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		if(password==null || password.length()==0){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "password", datas, ResultUtils.PASSWORD_ISNULL);
			return resString;
		}
		if(take_type!=1 && take_type!=2){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "take_type", datas, ResultUtils.TAKE_TYPE_ERROR);
			return resString;
		}
		if(receive_name==null || receive_name.length()==0){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "receive_name", datas, ResultUtils.RECEIVE_NAME_ISNULL);
			return resString;
		}
		if(receive_account==null || receive_account.length()==0){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "receive_account", datas, ResultUtils.RECEIVE_ACCOUNT_ISNULL);
			return resString;
		}
		if(take_type==2){
			//如果是银行卡，需要填写银行
			if(receive_bank==null || receive_bank.length()==0){
				resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "receive_bank", datas, ResultUtils.RECEIVE_BANK_ISNULL);
				return resString;
			}
		}else if(take_type==1){
			receive_bank="支付宝";
		}
		/*if(sid==null || sid.length()==0 || !SIDHelper.isValidateSID(sid)){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "sid", datas, ResultUtils.SID_WRONG);
			return resString;
		}*/
		if(sid==null){
			sid="";
		}
		AccSet set = paycenterFacade.getAccSet();//查询账户设置信息，需要查询提现相关设置
		if(set==null || set.getFlag()!=ResultUtils.SUCCESS){
			int resultcode = ResultUtils.QUERY_ERROR;
			if(set!=null){
				resultcode = set.getFlag();
			}
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "take_fee", datas, resultcode);
			return resString;
		}
		int leastfee = set.getLeastAmount();//最低取款金额，单位为分
		if(take_fee<leastfee){
			resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "take_fee", datas, ResultUtils.LOW_TAKE_MONEY);
			return resString;
		}
		AccTakeSet accTakeSet = paycenterFacade.selectAccTakeSetByUserId(uid);
		accTakeSet.setUserId(uid);
		if(accTakeSet!=null && accTakeSet.getPassword()!=null && accTakeSet.getPassword().length()>0){
			//已经设置了密码了
			//需要判断是否在24小时内连续输错3次密码
			String wronginfo = accTakeSet.getWronginfo();
			String newwronginfo =  wronginfo;
			if(wronginfo!=null && wronginfo.length()>0){
				String times[] = wronginfo.split(",");
				if(times!=null && times.length>=3){
					long end = Long.valueOf(times[times.length-1]);
					long start = Long.valueOf(times[times.length-3]);
					//先判断是否是24小时内连续输错的
					long cha = end - start;
					if(cha<24*60*60*1000L){
						//如果是24小时内输错的,再判断是否过了24小时
						if(System.currentTimeMillis()-end<24*60*60*1000L){
							//还没有解锁呢
							resString = returnErrorStr(CommentUtils.RES_FLAG_ERROR, "password", datas, ResultUtils.OVER_PASSWORD_WRONG_TIMES);
							return resString;
						}else{
							//解锁了
							newwronginfo = "";
						}
					}else{
						//不是24小时内连续输错的
						newwronginfo = times[times.length-2]+","+times[times.length-1];
					}
					
				}else{
					//不到三次输错密码
					
				}
			}
			//判断密码是否正确
			if(MD5Utils.md5(password).equals(accTakeSet.getPassword())){
				//密码正确,需要清楚密码输入错误记录
				if(accTakeSet.getWronginfo()!=null && accTakeSet.getWronginfo().length()>0){
					accTakeSet.setWronginfo("");
					accTakeSet.setUpdateTime(System.currentTimeMillis());
					paycenterFacade.updateAccTakeSet(accTakeSet);
				}
				//需要判断当前月是否已经提交过提现订单
				long starttime = DateUtil.getTheMonthStartTime();
				long endtime = DateUtil.getTheMonthEndTime();
				//AccSet set = paycenterFacade.getAccSet();//查询账户设置信息，需要查询提现相关设置
				if(set!=null && set.getFlag()==ResultUtils.SUCCESS){
					//先判断是否是当月的允许提现时间,只有允许提现时间才可以提现
					long nowtime = System.currentTimeMillis();
					if(nowtime>=starttime+set.getStartTime()*24*60*60*1000L && nowtime<starttime+set.getEndTime()*24*60*60*1000L){
						//当前时间处于允许提现时间
						int count = paycenterFacade.selectAccTakeRecordCount(uid, starttime, endtime);
						int limittimes = set.getLimitTimes();
						if(count>=0 && count<limittimes){
							//当月没有申请过提现，可以保存提现订单
							//需要判断余额是否充足
							AccAmt accAmt = paycenterFacade.getAccAmtInfo(uid);
							if(accAmt.getFlag()!=ResultUtils.SUCCESS){
								//查询出错
								flag = CommentUtils.RES_FLAG_ERROR;
								error = MessageUtils.getResultMessage(accAmt.getFlag());
								datas.put("error", error);
							}else if(accAmt.getRewardAmount()>=take_fee){
								//余额充足，允许提现
								AccTakeRecord record = new AccTakeRecord();
								record.setApplyTime(System.currentTimeMillis());
								record.setExtendField1("");
								record.setExtendField2("");
								record.setExtendField3("");
								record.setParterId(0);
								record.setPhoneNumber("");
								record.setReceiveAccount(receive_account);
								record.setReceiveBank(receive_bank);
								record.setReceiveName(receive_name);
								record.setRestFee(accAmt.getRewardAmount()-take_fee);
								record.setSellerId(0);
								record.setTakeFee(take_fee);
								String takeno = PaycenterUtil.generateOrderNo();
								record.setTakeNo(takeno);
								record.setTakeTime(0);
								record.setTakeType(take_type);
								record.setTradeStatus(0);
								record.setUserId(uid);
								record.setSid(sid);
								
								int result = paycenterFacade.insertAccTakeRecord(record);
								if(ResultUtils.SUCCESS==result){
									//插入成功了
									flag = CommentUtils.RES_FLAG_SUCCESS;
									//Map<String,Object> resMap= new HashMap<String,Object>();
									//resMap.put("record", record);
									datas.put("map", record);
								}else{
									flag = CommentUtils.RES_FLAG_ERROR;
									error = MessageUtils.getResultMessage(result);
									datas.put("error", error);
								}
							}else{
								//余额不足
								flag = CommentUtils.RES_FLAG_ERROR;
								error = MessageUtils.getResultMessage(ResultUtils.ACC_AMT_NOT_ENOUGH);
								datas.put("error", error);
							}
						}else if(count>=limittimes){
							//当月申请过提现，不能再申请提现
							flag = CommentUtils.RES_FLAG_ERROR;
							error = MessageUtils.getResultMessage(ResultUtils.OVER_TAKETIMES);
							datas.put("error", error);
						}else{
							//查询出错
							flag = CommentUtils.RES_FLAG_ERROR;
							error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
							datas.put("error", error);
						}
					}else{
						//超过了允许提现的时间
						flag = CommentUtils.RES_FLAG_ERROR;
						error = MessageUtils.getResultMessage(ResultUtils.OUT_TAKE_TIME);
						datas.put("error", error);
					}
				}else{
					//
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(set.getFlag());
					datas.put("error", error);
				}
				
				
			}else{
				//密码错误,需要记入密码输入错误信息
				if(newwronginfo==null){
					newwronginfo="";
				}
				if(newwronginfo.length()==0){
					newwronginfo=System.currentTimeMillis()+"";
				}else{
					newwronginfo=newwronginfo+","+System.currentTimeMillis();
				}
				accTakeSet.setUpdateTime(System.currentTimeMillis());
				accTakeSet.setWronginfo(newwronginfo);
				paycenterFacade.updateAccTakeSet(accTakeSet);
				
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(ResultUtils.PASSWORD_WRONG);
				datas.put("error", error);
			}
		}else{
			//
			if(accTakeSet.getFlag()==ResultUtils.DATAISNULL){
				//无数据
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(ResultUtils.PASSWORD_NOTSET);
				datas.put("error", error);
			}else if(accTakeSet.getFlag()==ResultUtils.SUCCESS){
				//有数据，但是无密码
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(ResultUtils.PASSWORD_NOTSET);
				datas.put("error", error);
			}else{
				//查询出错
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(accTakeSet.getFlag());
				datas.put("error", error);
			}
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 *查询当月是否超越提现次数
	 * @param reqs
	 * @return
	 */
	public String getTakeStatus(String reqs,Long uid) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		//需要判断当前月是否已经提交过提现订单
		long starttime = DateUtil.getTheMonthStartTime();
		long endtime = DateUtil.getTheMonthEndTime();
		AccSet set = paycenterFacade.getAccSet();//查询账户设置信息，需要查询提现相关设置
		if(set!=null && set.getFlag()==ResultUtils.SUCCESS){
			//先判断是否是当月的允许提现时间,只有允许提现时间才可以提现
			long nowtime = System.currentTimeMillis();
			if(nowtime>=starttime+set.getStartTime()*24*60*60*1000L && nowtime<starttime+set.getEndTime()*24*60*60*1000L){
				//当前时间处于允许提现时间
				int count = paycenterFacade.selectAccTakeRecordCount(uid, starttime, endtime);
				int limittimes = set.getLimitTimes();
				if(count>=0 && count<limittimes){
					//当月没有超过限制次数
					flag = CommentUtils.RES_FLAG_SUCCESS;
					Map<String,Object> resMap= new HashMap<String,Object>();
					resMap.put("status", "0");
					datas.put("map", resMap);
				}else if(count>=limittimes){
					//当月申请过提现，不能再申请提现
					flag = CommentUtils.RES_FLAG_SUCCESS;
					Map<String,Object> resMap= new HashMap<String,Object>();
					resMap.put("status", "1");
					datas.put("map", resMap);
				}else{
					//查询出错
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
					datas.put("error", error);
				}
			}else{
				//超过了允许提现的时间
				flag = CommentUtils.RES_FLAG_SUCCESS;
				Map<String,Object> resMap= new HashMap<String,Object>();
				resMap.put("status", "2");
				datas.put("map", resMap);
			}
		}else{
			//
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(set.getFlag());
			datas.put("error", error);
		}
		
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 *查询账户余额
	 * @param reqs
	 * @return
	 */
	public String getAccAmt(String reqs,Long uid) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		int accamt = paycenterFacade.getAccAmt(uid);//账户余额
		long acctaketotal = paycenterFacade.getAccTakeTotal(uid);//成功提现的总额
		long waitAcctaketotal = paycenterFacade.getAccTakeTotal(uid, 0);//等待提现的总额
		flag = CommentUtils.RES_FLAG_SUCCESS;
		Map<String,Object> resMap= new HashMap<String,Object>();
		resMap.put("AccAmt", accamt+"");
		resMap.put("AccTakeTotal", acctaketotal+"");
		resMap.put("WaitAccTakeTotal", waitAcctaketotal+"");
		datas.put("map", resMap);
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 *查询提现记录列表
	 * @param reqs
	 * @return
	 */
	public String getAccTakeRecords(String reqs,Long uid) {
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		int pagesize = 20;
		int pageno = 1;
		String pagenostr;
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//pagesize = Integer.valueOf(dataq.get("pagesize")+"");
			pagenostr = dataq.get("pageno")+"";
			if(pagenostr!=null && pagenostr.matches("^[0-9]+$")){
				pageno = Integer.valueOf(pagenostr);
			}
			if(pageno<1){
				pageno=1;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		long start = PageUtils.getRecordStart(pagesize, pageno);
		//int tradeStatus = 1;
		List<AccTakeRecord> records = paycenterFacade.findAccTakeRecords(uid,start,pagesize);
		//需要转换时间格式
		if(records!=null && records.size()>0){
			for(int i=0;i<records.size();i++){
				AccTakeRecord record = records.get(i);
				record.setApplyTimeStr(DateUtil.format(record.getApplyTime(),"yyyy-MM-dd HH:mm:ss"));
				record.setTakeTimeStr(DateUtil.format(record.getTakeTime(),"yyyy-MM-dd HH:mm:ss"));
			}
		}
		flag = CommentUtils.RES_FLAG_SUCCESS;
		datas.put("list", records);
		datas.put("pageNo", pageno);
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
}
