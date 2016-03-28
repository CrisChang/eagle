package com.poison.product.domain.repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.poison.story.model.Story;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.config.AlipayConfig;
import com.alipay.sign.RSA;
import com.alipay.util.AlipayCore;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.action.PaycenterController;
import com.poison.eagle.utils.PaycenterUtil;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.paycenter.constant.PaycenterConstant;
import com.poison.paycenter.dao.PayLogDao;
import com.poison.paycenter.ext.wx.WXPay;
import com.poison.paycenter.ext.wx.common.Configure;
import com.poison.paycenter.ext.wx.common.RandomStringGenerator;
import com.poison.paycenter.ext.wx.common.Signature;
import com.poison.paycenter.ext.wx.common.XMLParser;
import com.poison.paycenter.ext.wx.protocol.pay_protocol.ScanPayReqData;
import com.poison.paycenter.model.PayLog;
import com.poison.product.dao.AccGoldDao;
import com.poison.product.dao.AccGoldRecordDao;
import com.poison.product.dao.ProductDao;
import com.poison.product.dao.ProductOrderDao;
import com.poison.product.model.AccGold;
import com.poison.product.model.AccGoldRecord;
import com.poison.product.model.Product;
import com.poison.product.model.ProductOrder;
import com.poison.story.dao.StoryDAO;
import com.poison.story.model.StoryPay;
import com.poison.story.repository.StoryStatisticDomainRepository;

public class ProductDomainRepository {
	private static final Log LOG = LogFactory.getLog(ProductDomainRepository.class);
	
	private ProductDao productDao;
	
	private ProductOrderDao productOrderDao;
	
	private TransactionTemplate productMysqlTransactionTemplate;
	
	private PayLogDao payLogDao;
	
	private AccGoldDao accGoldDao;
	
	private AccGoldRecordDao accGoldRecordDao;
	
	private StoryDAO storyDAO;
	
	private UKeyWorker reskeyWork;
	
	private StoryStatisticDomainRepository storyStatisticDomainRepository;
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	public void setProductOrderDao(ProductOrderDao productOrderDao) {
		this.productOrderDao = productOrderDao;
	}
	public void setProductMysqlTransactionTemplate(
			TransactionTemplate productMysqlTransactionTemplate) {
		this.productMysqlTransactionTemplate = productMysqlTransactionTemplate;
	}
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	public void setPayLogDao(PayLogDao payLogDao) {
		this.payLogDao = payLogDao;
	}
	public void setAccGoldDao(AccGoldDao accGoldDao) {
		this.accGoldDao = accGoldDao;
	}
	public void setAccGoldRecordDao(AccGoldRecordDao accGoldRecordDao) {
		this.accGoldRecordDao = accGoldRecordDao;
	}
	public void setStoryDAO(StoryDAO storyDAO) {
		this.storyDAO = storyDAO;
	}
	public void setStoryStatisticDomainRepository(
			StoryStatisticDomainRepository storyStatisticDomainRepository) {
		this.storyStatisticDomainRepository = storyStatisticDomainRepository;
	}
	/**
	 *
	 * @return
	 */
	public List<Product> findProductsByType(String ptype){
		return productDao.findProductsByType(ptype);
	}
	/**
	 * @param
	 * @returno
	 */
	public int insertIntoProductOrder(ProductOrder productOrder){
		return productOrderDao.insertIntoProductOrder(productOrder);
	}
	
	/**
	 * @param
	 * @returno
	 */
	public int updateProductOrderPaystatus(ProductOrder productOrder){
		return productOrderDao.updateProductOrderPaystatus(productOrder);
	}
	
	/**
	 * 根据商品id查询商品信息
	 */
	public Product findProductByPid(long pid){
		return productDao.findProductByPid(pid);
	}
	/**
	 * 根据商品价格和商品类型查询
	 */
	public Product findProductByPriceAndType(long price,String type){
		return productDao.findProductByPriceAndType(price, type);
	}
	/**
	 * 根据tn号查询订单信息(主要用于苹果支付查询--用于苹果支付的原始收据的MD5加密数据查询)
	 */
	public ProductOrder findProductOrderByTn(String tn){
		return productOrderDao.findProductOrderByTn(tn);
	}
	
	/**
	 * 支付宝付款前的服务器逻辑
	 * @return
	 */
	public Map<String, String> createAlipayOrder(final Map<String, String> inMap) {
		//组织发往支付宝的请求参数
		final Map<String, String> requestParameter = new HashMap<String, String>();
		productMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {				
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {		
				int flag = 1000;
				try {
					//获取所有参数
					long sysdate = System.currentTimeMillis();
					String uidstr = inMap.get("uid")+"";
					String pidstr = inMap.get("pid")+"";
					String amountstr = inMap.get("amount")+"";
					String virtualamountstr = inMap.get("virtualamount")+"";
					long uid = 0;
					long pid = 0;
					long amount = 0;
					long virtualamount=0;
					if(StringUtils.isInteger(uidstr)){
						uid = Long.valueOf(uidstr);
					}
					if(StringUtils.isInteger(pidstr)){
						pid = Long.valueOf(pidstr);
					}
					if(StringUtils.isInteger(amountstr)){
						amount = Long.valueOf(amountstr);
					}
					if(StringUtils.isInteger(virtualamountstr)){
						virtualamount = Long.valueOf(virtualamountstr);
					}
					
					
					String title = (String)inMap.get("title");
					String remark = (String)inMap.get("remark");
					//String paymode = (String)inMap.get("paymode");//此方法就是支付宝支付
					String paymode = ProductOrder.PAYMODE_ALIPAY;
					String ptype = (String)inMap.get("ptype");//商品类型
					/**
					 * 生成一个订单信息,并获取订单号，用于发往支付宝
					 */
					ProductOrder productOrder = new ProductOrder();
					long id = reskeyWork.getId();
					productOrder.setId(id);
					productOrder.setOrdernum(PaycenterUtil.getOrderNumberById(id));
					productOrder.setAmount(amount);
					productOrder.setCreatetime(sysdate);
					productOrder.setPaymode(paymode);
					productOrder.setPid(pid);
					productOrder.setPtype(ptype);
					productOrder.setRemark(remark);
					productOrder.setTitle(title);
					productOrder.setUid(uid);
					productOrder.setUpdatetime(sysdate);
					productOrder.setVirtualamount(virtualamount);
					productOrder.setNum(1);//先默认购买一个商品
					
					flag = productOrderDao.insertIntoProductOrder(productOrder);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					//生成发往支付宝参数
					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("id", productOrder.getId());
					map1.put("service", "");
					map1.put("totalFee", productOrder.getAmount());
					map1.put("subject", title);
					map1.put("body", "购买"+title);
					Map<String, String> reMap = getParametersReturnParameter(map1);
					
					/**
					 * 记录发往支付宝的所有参数
					 */		
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.putAll(reMap);
					map2.put("userId", uid);
					map2.put("sellerId", PaycenterUtil.getAplipayValue(PaycenterUtil.getAlipyNumber()));
					map2.put("parterId", PaycenterUtil.getPartnerIdInt(PaycenterUtil.getPartner()));
					map2.put("totalFee", amount);
					
					PayLog paylogNew = getParametersPayLogNew(map2, PaycenterConstant.PAY_MARK_1, PaycenterConstant.LOG_STATUS_1);
					flag = payLogDao.insertPayLog(paylogNew);	
					paylogNew.setLogCreate(sysdate);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					requestParameter.putAll(reMap);
					requestParameter.put("flag", String.valueOf(flag));
					return requestParameter;
				}catch (Exception e) {
					LOG.error(e);
					status.setRollbackOnly();
					requestParameter.put("flag", String.valueOf(ResultUtils.ERROR));
					return requestParameter;
				}
				
			}
		});	
		LOG.info(requestParameter);
		return requestParameter;	
	}
	
	/**
	 * 付款后的异步通知支付结果(支付宝、微信)
	 * @param paylog
	 * @return
	 */
	public Map<String, String> notifyResult(final PayLog paylog) {
		LOG.info("ProductDomainRepository.notifyResult:"+paylog);
		Map<String, String> resMap = payment(paylog);
		return resMap;
	}

	public Map<String, String> payment(final PayLog paylog) {
		final Map<String, String> map = new HashMap<String, String>();
		productMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {
				int flag = 1000;
				try {		
					final long sysdate = System.currentTimeMillis();
					/**
					 * 一、记录一条支付宝返回信息日志到支付宝记录表,修改充值表记录，并修改账户资金，记录
					 * 一条账户资金变更信息
					 */
					//1获取付款人的用户ID
					String outTradeNo = paylog.getOutTradeNo();
					long oid = PaycenterUtil.getIdByOrderNumber(outTradeNo);
					ProductOrder productOrder = productOrderDao.findProductOrderById(oid);
					if (productOrder.getFlag() != ResultUtils.SUCCESS) {
						map.put("flag", String.valueOf(productOrder.getFlag()));
						return map;
					}
					long userId = productOrder.getUid();											
					//2生成一条响应记录
					PayLog paylog1 = new PayLog();	
					paylog.setUserId(userId);
					paylog.setUserName("");
					paylog.setLogCreate(sysdate);
					paylog1 = getParametersPayLog1(paylog, PaycenterConstant.PAY_MARK_2, PaycenterConstant.LOG_STATUS_2,PaycenterConstant.COMPANY_ACC_ID_1,PaycenterConstant.PAYMENT_TYPE_1);
					flag = payLogDao.insertPayLog(paylog1);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//3同时将充值记录表改为成功
					productOrder.setTn(paylog.getTradeNo());
					productOrder.setPaytime(sysdate);
					productOrder.setStatus(PaycenterConstant.TRADE_STATUS_1);
					productOrder.setPaystatus(1);
					productOrder.setUpdatetime(sysdate);
					
					flag = productOrderDao.updateProductOrderPaystatus(productOrder);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						map.put("flag", String.valueOf(flag));
						return map;
					}
					//需要判断是否是金币商品，如果是金币商品，需要进行金币账户的充值
					if(Product.TYPE_GOLD.equals(productOrder.getPtype())){
						//4查询金币账户表中用户是否存在，如果存在，将付款人的金币账户余额和本次金额相加，如果没有，生成一条账户信息
						//先查询出应该增加的金币额
						Product product = productDao.findProductByPid(productOrder.getPid());
						if(product==null || product.getFlag()!=ResultUtils.SUCCESS){
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(product.getFlag()));
							return map;
						}
						AccGold accGold  = accGoldDao.findAccGoldByUserId(userId);
						//-------------------
						long restamount = 0;
						if(accGold!=null && accGold.getUserId()>0){
							restamount = accGold.getGoldamount();
						}
						restamount = restamount+product.getPackamount()+product.getGiveamount();
						//------------------
						if (accGold!=null && accGold.getFlag()==ResultUtils.SUCCESS) {
							accGold.setChangetime(sysdate);
							accGold.setGoldamount(product.getPackamount()+product.getGiveamount());
							flag = accGoldDao.addAccGoldByUserId(accGold);
						}else if (accGold==null || accGold.getFlag() == ResultUtils.DATAISNULL){
							accGold.setChangetime(sysdate);
							accGold.setCreatetime(sysdate);
							accGold.setGoldamount(product.getPackamount()+product.getGiveamount());
							accGold.setSequence(1);
							accGold.setUserId(userId);
							flag = accGoldDao.insertIntoAccGold(accGold);
						}else {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
						//5记录一条资金变更记录信息
						AccGoldRecord accGoldRecord = new AccGoldRecord();
						accGoldRecord.setAdminUser("");
						accGoldRecord.setCause("金币充值,购买"+product.getPackamount()+"金币，赠送"+product.getGiveamount()+"金币");
						accGoldRecord.setChangeDesc("金币充值,购买"+product.getPackamount()+"金币，赠送"+product.getGiveamount()+"金币");
						accGoldRecord.setChangetime(sysdate);
						accGoldRecord.setCreatetime(sysdate);
						accGoldRecord.setOrdernum(productOrder.getOrdernum());
						accGoldRecord.setRemark("金币充值变更记录");
						accGoldRecord.setRestAmount(restamount);
						accGoldRecord.setSequence(accGold.getSequence()+1);
						accGoldRecord.setTradeAmount(product.getPackamount()+product.getGiveamount());
						accGoldRecord.setType("充值");
						accGoldRecord.setUpdatetime(sysdate);
						accGoldRecord.setUserId(userId);
						flag = accGoldRecordDao.insertIntoGoldRecord(accGoldRecord);
						if (flag != ResultUtils.SUCCESS) {
							status.setRollbackOnly();	
							map.put("flag", String.valueOf(flag));
							return map;
						}
						map.put("goldAmount", restamount+"");
					}
					status.isCompleted();
				} catch (Exception e) {
					LOG.debug(e);
					e.printStackTrace();
					status.setRollbackOnly();
					map.put("flag", String.valueOf(ResultUtils.ERROR));
					return map;
				}
				map.put("flag", String.valueOf(flag));
				map.put("success", "success");
				LOG.info(map);
				return map;
			}
		});
		return map;
	}
	
	
	
	/**
	 * 微信支付，需要向微信下订单,微信付款前的服务器逻辑
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createWeixinPayOrder(final Map<String, String> inMap) {
		LOG.info("ProductDomainRepository.createWeixinReloadOrderInfoByMap:"+inMap);
		//组织发往微信的请求参数
		final Map<String, String> requestParameter = new HashMap<String, String>();
		productMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {				
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {		
				int flag = 1000;
				try {
					
					//获取所有参数
					long sysdate = System.currentTimeMillis();
					String uidstr = inMap.get("uid")+"";
					String pidstr = inMap.get("pid")+"";
					String amountstr = inMap.get("amount")+"";
					String virtualamountstr = inMap.get("virtualamount")+"";
					long uid = 0;
					long pid = 0;
					long amount = 0;
					long virtualamount=0;
					if(StringUtils.isInteger(uidstr)){
						uid = Long.valueOf(uidstr);
					}
					if(StringUtils.isInteger(pidstr)){
						pid = Long.valueOf(pidstr);
					}
					if(StringUtils.isInteger(amountstr)){
						amount = Long.valueOf(amountstr);
					}
					if(StringUtils.isInteger(virtualamountstr)){
						virtualamount = Long.valueOf(virtualamountstr);
					}
					
					
					String title = (String)inMap.get("title");
					String remark = (String)inMap.get("remark");
					//String paymode = (String)inMap.get("paymode");//此方法就是微信支付方法
					String paymode = ProductOrder.PAYMODE_WX;
					String ptype = (String)inMap.get("ptype");//商品类型
					String ip = (String)inMap.get("ip");
					/**
					 * 生成一个订单信息,并获取订单号，用于发往微信
					 */
					ProductOrder productOrder = new ProductOrder();
					long id = reskeyWork.getId();
					productOrder.setId(id);
					productOrder.setOrdernum(PaycenterUtil.getOrderNumberById(id));
					productOrder.setAmount(amount);
					productOrder.setCreatetime(sysdate);
					productOrder.setPaymode(paymode);
					productOrder.setPid(pid);
					productOrder.setPtype(ptype);
					productOrder.setRemark(remark);
					productOrder.setTitle(title);
					productOrder.setUid(uid);
					productOrder.setUpdatetime(sysdate);
					productOrder.setVirtualamount(virtualamount);
					productOrder.setNum(1);//先默认购买一个商品
					
					flag = productOrderDao.insertIntoProductOrder(productOrder);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					//向微信下订单的参数
					ScanPayReqData scanPayReqData = getRequestWeixinParameters(productOrder.getOrdernum(),(int)amount,ip);
					
					
					Map<String, String> reMap = new HashMap<String,String>();
					Map<String,Object> signmap = new HashMap<String,Object>();
					reMap.put("appid",Configure.getAppid());
					reMap.put("partnerid",Configure.getMchid());
					reMap.put("package", "Sign=WXPay");
					String noncestr = RandomStringGenerator.getRandomStringByLength(32);
					String timestamp = System.currentTimeMillis()/1000+"";
					reMap.put("noncestr",noncestr);
					reMap.put("timestamp",timestamp);
					
					signmap.put("appid",Configure.getAppid());
					signmap.put("partnerid",Configure.getMchid());
					signmap.put("package", "Sign=WXPay");
					signmap.put("noncestr",noncestr);
					signmap.put("timestamp",timestamp);
					
					try{
						//请求微信支付服务器进行下单操作(暂时有个问题，请求参数不能带中文，如果有中文会有乱码问题导致微信服务器验证签名错误)
						String xmlString = WXPay.requestScanPayService(scanPayReqData);
						PaycenterController.payreturnxml=xmlString;
						//String xmlString = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx2421b1c4370ec43b]]></appid><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str><sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id><trade_type><![CDATA[JSAPI]]></trade_type></xml>";
						if(xmlString!=null && xmlString.trim().startsWith("<xml>")){
							xmlString = "<?xml   version=\"1.0\"   encoding=\"gb2312\"?>"+xmlString;
						}
						//需要解析返回值
						Map<String,Object> resultMap = XMLParser.getMapFromXML(xmlString);
						
						if(resultMap!=null && resultMap.size()>0){
							if("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))){
								//网络向微信下订单成功
								String prepay_id  = (String) resultMap.get("prepay_id");
								reMap.put("prepayid",prepay_id);
								signmap.put("prepayid",prepay_id);
								String sign = Signature.getSign(signmap);
								reMap.put("sign",sign);
							}else{
								flag = ResultUtils.ERROR;
								status.setRollbackOnly();	
								requestParameter.put("flag", String.valueOf(flag));
								requestParameter.put("error","向微信下订单出现异常");
								return requestParameter;
							}
						}else{
							flag = ResultUtils.ERROR;
							status.setRollbackOnly();	
							requestParameter.put("flag", String.valueOf(flag));
							requestParameter.put("error","向微信下订单出现异常");
							return requestParameter;
						}
						
					}catch(Exception e){
						e.printStackTrace();
						LOG.error(e);
						flag = ResultUtils.ERROR;
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						requestParameter.put("error","向微信下订单出现异常");
						return requestParameter;
					}
					
					/**
					 * 记录发往微信的所有参数
					 */
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("userId", uid);
					map2.put("userName", "");
					map2.put("totalFee", amount);
					map2.put("sellerId", 0);
					map2.put("parterId", 0);
					map2.put("subject", title);
					map2.put("body", "购买"+title);
					
					PayLog paylogNew = getWeixinParametersPayLogNew(scanPayReqData,map2, PaycenterConstant.PAY_MARK_1, PaycenterConstant.LOG_STATUS_1);
					flag = payLogDao.insertPayLog(paylogNew);	
					paylogNew.setLogCreate(sysdate);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					status.isCompleted();
					
					requestParameter.putAll(reMap);
					requestParameter.put("out_trade_no", scanPayReqData.getOut_trade_no());
					requestParameter.put("flag", String.valueOf(flag));
					return requestParameter;
				}catch (Exception e) {
					e.printStackTrace();
					LOG.error(e);
					status.setRollbackOnly();
					requestParameter.put("flag", String.valueOf(ResultUtils.ERROR));
					return requestParameter;
				}
				
			}
		});	
		LOG.info(requestParameter);
		return requestParameter;
	}
	
	/**
	 * 苹果付款前的服务器逻辑
	 * @return
	 */
	public Map<String, String> createAppleOrder(final Map<String, String> inMap) {
		//组织发往支付宝的请求参数
		final Map<String, String> requestParameter = new HashMap<String, String>(3);
		productMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {				
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {		
				int flag = 1000;
				try {
					//获取所有参数
					long sysdate = System.currentTimeMillis();
					String uidstr = inMap.get("uid")+"";
					String pidstr = inMap.get("pid")+"";
					String amountstr = inMap.get("amount")+"";
					String virtualamountstr = inMap.get("virtualamount")+"";
					long uid = 0;
					long pid = 0;
					long amount = 0;
					long virtualamount=0;
					if(StringUtils.isInteger(uidstr)){
						uid = Long.valueOf(uidstr);
					}
					if(StringUtils.isInteger(pidstr)){
						pid = Long.valueOf(pidstr);
					}
					if(StringUtils.isInteger(amountstr)){
						amount = Long.valueOf(amountstr);
					}
					if(StringUtils.isInteger(virtualamountstr)){
						virtualamount = Long.valueOf(virtualamountstr);
					}
					
					
					String title = (String)inMap.get("title");
					String remark = (String)inMap.get("remark");
					
					String paymode = ProductOrder.PAYMODE_APPLE;
					String ptype = (String)inMap.get("ptype");//商品类型
					/**
					 * 生成一个订单信息,并获取订单号，用于发往支付宝
					 */
					ProductOrder productOrder = new ProductOrder();
					long id = reskeyWork.getId();
					productOrder.setId(id);
					productOrder.setOrdernum(PaycenterUtil.getOrderNumberById(id));
					productOrder.setAmount(amount);
					productOrder.setCreatetime(sysdate);
					productOrder.setPaymode(paymode);
					productOrder.setPid(pid);
					productOrder.setPtype(ptype);
					productOrder.setRemark(remark);
					productOrder.setTitle(title);
					productOrder.setUid(uid);
					productOrder.setUpdatetime(sysdate);
					productOrder.setVirtualamount(virtualamount);
					productOrder.setNum(1);//先默认购买一个商品
					
					flag = productOrderDao.insertIntoProductOrder(productOrder);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();	
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					status.isCompleted();
					
					requestParameter.put("out_trade_no",productOrder.getOrdernum());
					requestParameter.put("flag", String.valueOf(flag));
					return requestParameter;
				}catch (Exception e) {
					LOG.error(e);
					status.setRollbackOnly();
					requestParameter.put("flag", String.valueOf(ResultUtils.ERROR));
					return requestParameter;
				}
				
			}
		});	
		LOG.info(requestParameter);
		return requestParameter;	
	}
	
	/**
	 * 虚拟金币购买章节的事物逻辑
	 * @return
	 */
	public Map<String, String> payStoryChapter(final Map<String, String> inMap) {
		//组织发往支付宝的请求参数
		final Map<String, String> requestParameter = new HashMap<String, String>();
		productMysqlTransactionTemplate.execute(new TransactionCallback<Map<String, String>>() {				
			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {		
				int flag = 1000;
				try {
					//获取所有参数
					long sysdate = System.currentTimeMillis();
					String uidstr = inMap.get("uid")+"";
					String cidstr = inMap.get("cid")+"";
					String storyIdStr = inMap.get("storyId")+"";
					String amountstr = inMap.get("amount")+"";
					long uid = 0;
					long cid = 0;
					int amount = 0;
					long storyId = 0;//小说id
					if(StringUtils.isInteger(uidstr)){
						uid = Long.valueOf(uidstr);
					}
					if(StringUtils.isInteger(cidstr)){
						cid = Long.valueOf(cidstr);
					}
					if(StringUtils.isInteger(amountstr)){
						amount = Integer.valueOf(amountstr);
					}
					if(StringUtils.isInteger(storyIdStr)){
						storyId = Long.valueOf(storyIdStr);
					}
					
					AccGold accGold = accGoldDao.findAccGoldByUserId(uid);
					long accAmount = 0;
					if(accGold!=null && accGold.getUserId()>0){
						accAmount = accGold.getGoldamount();
					}else if(accGold==null || accGold.getFlag()==ResultUtils.DATAISNULL){
						accAmount = 0;
					}else{
						//余额查询失败
						status.setRollbackOnly();
						flag = ResultUtils.QUERY_ERROR;
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					if(accAmount<amount){
						//余额不足
						status.setRollbackOnly();
						flag = ResultUtils.ACC_GOLD_NOT_ENOUGH;
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					//减去要扣的金币
					if(accGold!=null && accGold.getUserId()>0){
						accGold.setChangetime(sysdate);
						accGold.setGoldamount(-amount);
						flag = accGoldDao.addAccGoldByUserId(accGold);
					}else if(accGold==null || accGold.getFlag() == ResultUtils.DATAISNULL){
						accGold.setChangetime(sysdate);
						accGold.setCreatetime(sysdate);
						accGold.setGoldamount(accAmount-amount);
						accGold.setSequence(1);
						accGold.setUserId(uid);
						flag = accGoldDao.insertIntoAccGold(accGold);
					}
					if(flag != ResultUtils.SUCCESS){
						status.setRollbackOnly();
						//flag = ResultUtils.ACC_GOLD_NOT_ENOUGH;
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					//记录金币账户变更记录
					AccGoldRecord accGoldRecord = new AccGoldRecord();
					accGoldRecord.setAdminUser("");
					accGoldRecord.setCause("花费金币,购买章节id为"+cid+"的小说章节，花费了"+amount+"金币");
					accGoldRecord.setChangeDesc("花费金币,购买章节id为"+cid+"的小说章节，花费了"+amount+"金币");
					accGoldRecord.setChangetime(sysdate);
					accGoldRecord.setCreatetime(sysdate);
					accGoldRecord.setOrdernum("cid:"+cid);
					accGoldRecord.setRemark("购买章节花费金币变更记录");
					accGoldRecord.setRestAmount(accAmount-amount);
					accGoldRecord.setSequence(accGold.getSequence()+1);
					accGoldRecord.setTradeAmount(-amount);
					accGoldRecord.setType("paystory");
					accGoldRecord.setUpdatetime(sysdate);
					accGoldRecord.setUserId(uid);
					flag = accGoldRecordDao.insertIntoGoldRecord(accGoldRecord);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					//增加章节的付费信息
					StoryPay storyPay = new StoryPay();
					long id = reskeyWork.getId();
					storyPay.setId(id);
					storyPay.setChapterId(cid);
					storyPay.setCreatetime(sysdate);
					storyPay.setPayAmount(amount);
					storyPay.setPayed(1);
					storyPay.setUserId(uid);
					flag = storyDAO.saveStoryPay(storyPay);
					if (flag != ResultUtils.SUCCESS) {
						status.setRollbackOnly();
						requestParameter.put("flag", String.valueOf(flag));
						return requestParameter;
					}
					//记录购买章节的金币数量
					Story story = storyDAO.findStoryById(storyId);
					if((ResultUtils.SUCCESS+"").equals(story.getFlag())){
						storyStatisticDomainRepository.addPayTotalByResourceId(story.getId(), amount,story.getChannel(),story.getType());
					}

					status.isCompleted();
					requestParameter.put("goldAmount",(accAmount-amount)+"");
					requestParameter.put("flag", String.valueOf(flag));
					return requestParameter;
				}catch (Exception e) {
					LOG.error(e);
					status.setRollbackOnly();
					requestParameter.put("flag", String.valueOf(ResultUtils.ERROR));
					return requestParameter;
				}
				
			}
		});	
		LOG.info(requestParameter);
		return requestParameter;	
	}
	
	
	protected Map<String, String> getParametersReturnParameter(Map<String, Object> map) {
		String outTradeNo = PaycenterUtil.getOrderNumberById((Long)map.get("id"));       	 				//发往支付宝的订单号		
		Map<String, String> sArray = new LinkedHashMap<String, String>();
		sArray.put("partner", PaycenterUtil.getPartner());													//
		sArray.put("seller_id", PaycenterUtil.getAlipyNumber());										//
		sArray.put("out_trade_no", outTradeNo);															//
		sArray.put("subject", map.get("subject")+"");														//		
		sArray.put("body", map.get("body")+"");															//
		sArray.put("total_fee", PaycenterUtil.fenToYuan((Integer)map.get("totalFee")));
		sArray.put("notify_url", PaycenterConstant.NOTIFY_URL_PRODUCT);											//
		sArray.put("service", (String)map.get("service"));		//mobile.securitypay.pay
		sArray.put("payment_type", PaycenterConstant.PAYMENT_TYPE);											//
		sArray.put("_input_charset", AlipayConfig.input_charset);										//
		Map<String, String> requestMap = AlipayCore.paraFilter(sArray);									
		String requestStr = AlipayCore.createLinkString(requestMap);	
		Map<String, String> reMap = new HashMap<String, String>();
		reMap.putAll(sArray);
		reMap.put("orderSpec", requestStr);
		String sign = RSA.sign(requestStr, AlipayConfig.private_key, AlipayConfig.input_charset);		
		reMap.put("sign_type", "RSA");																	//
		reMap.put("sign", sign);											//	
		return reMap;
	}
	protected PayLog getParametersPayLogNew(Map<String, Object> reMap,
			 int pAY_MARK_1, int lOG_STATUS_1) {
		PayLog paylog = new PayLog();
		paylog.setLogStatus(lOG_STATUS_1);
		paylog.setUserId((Long)reMap.get("userId"));
		paylog.setUserName((String)reMap.get("userName"));
		paylog.setOutTradeNo((String)reMap.get("out_trade_no"));
		paylog.setPayMark(pAY_MARK_1);
		paylog.setService((String)reMap.get("service"));
		paylog.setSellerId((Integer)reMap.get("sellerId"));
		paylog.setParterId((Integer)reMap.get("parterId"));
		paylog.setInputCharset((String)reMap.get("_input_charset"));
		paylog.setSignType((String)reMap.get("sign_type"));
		paylog.setSign((String)reMap.get("sign"));
		paylog.setNotifyUrl((String)reMap.get("notify_url"));
		paylog.setAppId((String)reMap.get("app_id"));
		paylog.setAppenv((String)reMap.get("appenv"));
		paylog.setSubject((String)reMap.get("subject"));
		paylog.setPaymentType((String)reMap.get("payment_type"));
		paylog.setTotalFee((Integer)reMap.get("totalFee"));
		paylog.setItBPay(null);
		paylog.setExternToken(null);
		paylog.setPaymethod(null);
		paylog.setBody((String)reMap.get("body"));
		return paylog;
	}
	/**
	 * @param paylog
	 * @return
	 */
	private PayLog getParametersPayLog1(PayLog paylog, int payMark, int logStatus,int sellerid,int parterid) {
		paylog.setPayMark(payMark);
		paylog.setLogStatus(logStatus);
		paylog.setSellerId(sellerid);
		paylog.setParterId(parterid);
		return paylog;
	}
	
	protected PayLog getWeixinParametersPayLogNew(ScanPayReqData scanPayReqData,Map<String, Object> reMap,
			 int pAY_MARK_1, int lOG_STATUS_1) {
		PayLog paylog = new PayLog();
		paylog.setLogStatus(lOG_STATUS_1);
		paylog.setUserId((Long)reMap.get("userId"));
		paylog.setUserName((String)reMap.get("userName"));
		paylog.setOutTradeNo(scanPayReqData.getOut_trade_no());
		paylog.setPayMark(pAY_MARK_1);
		paylog.setService("");
		paylog.setSellerId((Integer)reMap.get("sellerId"));
		paylog.setParterId((Integer)reMap.get("parterId"));
		paylog.setInputCharset("");
		paylog.setSignType("");
		paylog.setSign(scanPayReqData.getSign());
		paylog.setNotifyUrl(scanPayReqData.getNotify_url());
		paylog.setAppId(scanPayReqData.getAppid());
		paylog.setAppenv("");
		//paylog.setSubject(scanPayReqData.getBody());
		paylog.setSubject((String)reMap.get("subject"));
		paylog.setBody((String)reMap.get("body"));
		paylog.setPaymentType(scanPayReqData.getTrade_type());
		paylog.setTotalFee(scanPayReqData.getTotal_fee());
		paylog.setItBPay(null);
		paylog.setExternToken(null);
		paylog.setPaymethod("weixin");
		paylog.setExtendField1("weixin");
		return paylog;
	}
	
	//获取后台发往微信的请求参数
	protected ScanPayReqData getRequestWeixinParameters(String ordernum,int totalFee,String ip) {
		String authCode = "";
		String body="pay";//商品或支付单简要描述 
		String attach="";//附加数据
		String outTradeNo = ordernum;//订单号
		//int totalFee=(Integer)map2.get("totalFee");//金额，单位分
		String deviceInfo="";//终端设备号,可不传
		String spBillCreateIP=ip;//app端ip地址，必传
		String timeStart="";//交易起始时间 ，可不传
		String timeExpire="";//订单失效时间，可不传
		String goodsTag="";//商品标记，可不传
		String notifyUrl = "http://m.duyao001.com/product/notifyweixinpay.do";//支付结果异步通知地址
		String tradeType = "APP";//交易类型
		ScanPayReqData scanPayReqData = new ScanPayReqData(authCode, body, attach, outTradeNo, totalFee, deviceInfo, spBillCreateIP, timeStart, timeExpire, goodsTag,notifyUrl,tradeType,null);												//
		return scanPayReqData;
	}
}
