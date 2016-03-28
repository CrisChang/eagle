package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 充值记录模型
 * @author yan_dz
 *
 */
public class PayRecord extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2514459586908292887L;
	/**
	 * 充值记录id
	 */
	private long id;
	/**
	 * 充值订单号
	 */
	private String outTradeNo;
	/**
	 * 企业账号
	 */
	private int sellerId;
	/**
	 * 企业账号唯一签约号
	 */
	private int parterId;
	/**
	 * 用户号ID
	 */
	private long userId;
	/**
	 * 用户登录号
	 */
	private String userName;
	/**
	 * 金额
	 */
	private int totalFee;
	/**
	 * 电话号码
	 */
	private String phoneNumber;
	/**
	 * 交易状态
	 */
	private int tradeStatus;
	/**
	 * 付款方账号ID
	 */
	private String buyerId;
	/**
	 * 付款方账号
	 */
	private String buyerEmail;
	/**
	 * 支付类型
	 */
	private int paymentType;
	/**
	 * 三方交易流水号
	 */
	private String tradeNo;
	/**
	 * 充值时间
	 */
	private long tradeTime;
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
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
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
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(int tradeStatus) {
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
	public int getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public long getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(long tradeTime) {
		this.tradeTime = tradeTime;
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
