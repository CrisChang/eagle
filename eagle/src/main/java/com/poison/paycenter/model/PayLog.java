package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 支付宝交易日志记录模型
 * @author yan_dz
 *
 */
public class PayLog extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8805626548747836528L;
	/**
	 * 账户资金id
	 */
	private long id;
	/**
	 * 交易记录状态
	 */
	private int logStatus;
	/**
	 * 用户ID号
	 */
	private long userId;
	/**
	 * 用户登陆名
	 */
	private String userName;
	/**
	 * 充值订单号
	 */
	private String outTradeNo;
	/**
	 * 请求/响应
	 */
	private int payMark;	
	/**
	 * 接口名称
	 */
	private String service;
	/**
	 * 企业账户
	 */
	private int sellerId;
	/**
	 * 企业账户ID
	 */
	private int parterId;
	/**
	 * 参数编码字符集
	 */
	private String inputCharset;
	/**
	 * 签名方式
	 */
	private String signType;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 服务器异步通知页面路径
	 */
	private String notifyUrl;
	/**
	 * 客户端号
	 */
	private String appId;
	/**
	 * 客户端来源
	 */
	private String appenv;
	/**
	 * 商品名称
	 */
	private String subject;
	/**
	 * 支付类型
	 */
	private String paymentType;
	/**
	 * 总金额
	 */
	private int totalFee;
	/**
	 * 商品详情
	 */
	private String body;
	/**
	 * 未付款交易的超时时间
	 */
	private String itBPay;
	/**
	 * 授权令牌
	 */
	private String externToken;
	/**
	 * 使用银行卡支付
	 */
	private String paymethod;
	/**
	 * 通知时间
	 */
	private long notifyTime;
	/**
	 * 通知类型
	 */
	private String notifyType;
	/**
	 * 通知校验ID
	 */
	private String notifyId;
	/**
	 * 支付宝交易流水号
	 */
	private String tradeNo;
	/**
	 * 交易状态
	 */
	private String tradeStatus;
	/**
	 * 买家支付宝用户号
	 */
	private String buyerId;
	/**
	 * 买家支付宝账号
	 */
	private String buyerEmail;
	/**
	 * 购买数量
	 */
	private int quantity;
	/**
	 * 商品单价
	 */
	private int price;
	/**
	 * 交易创建时间
	 */
	private long gmtCreate;
	/**
	 * 交易付款时间
	 */
	private long gmtPayment;
	/**
	 * 是否调整总价
	 */
	private String isTotalFeeAdjust;
	/**
	 * 是否使用红包买家
	 */
	private String useCoupon;
	/**
	 * 折扣
	 */
	private String discount;
	/**
	 * 退款状态
	 */
	private String refundStatus;
	/**
	 * 退款时间
	 */
	private long gmtRefund;	
	/**
	 * 日志记录时间
	 */
	private long logCreate;	
	/**
	 * 扩展字段1
	 */
	private String extendField1;
	/**
	 * 扩展字段2
	 */
	private String extendField2;
	/**
	 * 扩展字段3
	 */
	private String extendField3;
	
	private int flag;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLogStatus() {
		return logStatus;
	}
	public void setLogStatus(int logStatus) {
		this.logStatus = logStatus;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}	
	public int getPayMark() {
		return payMark;
	}
	public void setPayMark(int payMark) {
		this.payMark = payMark;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public int getParterId() {
		return parterId;
	}
	public void setParterId(int parterId) {
		this.parterId = parterId;
	}
	public String getInputCharset() {
		return inputCharset;
	}
	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppenv() {
		return appenv;
	}
	public void setAppenv(String appenv) {
		this.appenv = appenv;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getItBPay() {
		return itBPay;
	}
	public void setItBPay(String itBPay) {
		this.itBPay = itBPay;
	}
	public String getExternToken() {
		return externToken;
	}
	public void setExternToken(String externToken) {
		this.externToken = externToken;
	}
	public String getPaymethod() {
		return paymethod;
	}
	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}
	public long getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(long notifyTime) {
		this.notifyTime = notifyTime;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerEmail() {
		return buyerEmail;
	}
	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public long getGmtPayment() {
		return gmtPayment;
	}
	public void setGmtPayment(long gmtPayment) {
		this.gmtPayment = gmtPayment;
	}
	public String getIsTotalFeeAdjust() {
		return isTotalFeeAdjust;
	}
	public void setIsTotalFeeAdjust(String isTotalFeeAdjust) {
		this.isTotalFeeAdjust = isTotalFeeAdjust;
	}
	public String getUseCoupon() {
		return useCoupon;
	}
	public void setUseCoupon(String useCoupon) {
		this.useCoupon = useCoupon;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public long getGmtRefund() {
		return gmtRefund;
	}
	public void setGmtRefund(long gmtRefund) {
		this.gmtRefund = gmtRefund;
	}
	public String getExtendField1() {
		return extendField1;
	}
	public void setExtendField1(String extendField1) {
		this.extendField1 = extendField1;
	}
	public String getExtendField2() {
		return extendField2;
	}
	public void setExtendField2(String extendField2) {
		this.extendField2 = extendField2;
	}
	public String getExtendField3() {
		return extendField3;
	}
	public void setExtendField3(String extendField3) {
		this.extendField3 = extendField3;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}		
	public long getLogCreate() {
		return logCreate;
	}
	public void setLogCreate(long logCreate) {
		this.logCreate = logCreate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
