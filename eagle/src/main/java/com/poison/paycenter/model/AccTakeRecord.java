package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 提现记录模型
 * @author wei
 *
 */
public class AccTakeRecord extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2514459586907292887L;
	/**
	 * 提现记录id
	 */
	private long id;
	/**
	 * 提现订单号
	 */
	private String takeNo;
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
	 * 金额
	 */
	private long takeFee;
	/**
	 * 提现后余额
	 */
	private long restFee;
	/**
	 * 电话号码
	 */
	private String phoneNumber;
	/**
	 * 交易状态
	 */
	private int tradeStatus;
	/**
	 * 提现方账号
	 */
	private String receiveAccount;
	/**
	 * 提现方账号开户名
	 */
	private String receiveName;
	/**
	 * 提现方账户开户行（如果提现到银行卡）
	 */
	private String receiveBank;
	/**
	 * 身份证号
	 */
	private String sid;
	/**
	 * 提现类型
	 */
	private int takeType;//支付宝、银行卡等(1：支付宝，2：银行卡)
	/**
	 * 申请提现时间
	 */
	private long applyTime;
	/**
	 * 完成提现时间
	 */
	private long takeTime;
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
	
	private String applyTimeStr;
	
	private String takeTimeStr;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTakeNo() {
		return takeNo;
	}

	public void setTakeNo(String takeNo) {
		this.takeNo = takeNo;
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

	public long getTakeFee() {
		return takeFee;
	}

	public void setTakeFee(long takeFee) {
		this.takeFee = takeFee;
	}

	public long getRestFee() {
		return restFee;
	}

	public void setRestFee(long restFee) {
		this.restFee = restFee;
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

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public int getTakeType() {
		return takeType;
	}

	public void setTakeType(int takeType) {
		this.takeType = takeType;
	}

	public long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}

	public long getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(long takeTime) {
		this.takeTime = takeTime;
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

	public String getReceiveAccount() {
		return receiveAccount;
	}

	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}

	public String getReceiveBank() {
		return receiveBank;
	}

	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}

	public String getApplyTimeStr() {
		return applyTimeStr;
	}

	public void setApplyTimeStr(String applyTimeStr) {
		this.applyTimeStr = applyTimeStr;
	}

	public String getTakeTimeStr() {
		return takeTimeStr;
	}

	public void setTakeTimeStr(String takeTimeStr) {
		this.takeTimeStr = takeTimeStr;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}
