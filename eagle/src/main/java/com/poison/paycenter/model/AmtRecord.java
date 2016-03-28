package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 账户资金变更记录模型
 * @author yan_dz
 *
 */
public class AmtRecord extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8263517837416367524L;
	/**
	 * 账户资金id
	 */
	private long id;
	/**
	 * 用户号ID
	 */
	private long userId;
	/**
	 * 用户登陆号
	 */
	private String userName;
	/**
	 * 用户手机号
	 */
	private String phoneNumber;
	/**
	 * 交易类型
	 */
	private int tradeType;
	/**
	 * 内部ID
	 */
	private long serialId;
	/**
	 * 外部流水号
	 */
	private String outTradeNo;
	/**
	 * 交易金额
	 */
	private int tradeAmount;
	/**
	 * 交易时间
	 */
	private long tradeTime;
	/**
	 * 交易状态
	 */
	private int tradeStatus;
	/**
	 * 管理员用户名
	 */
	private long adminUser;
	/**
	 * 管理员用户名
	 */
	private String tradeDesc;
	/**
	 * 管理员用户名
	 */
	private int shadow;
	
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getTradeType() {
		return tradeType;
	}
	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}	
	public long getSerialId() {
		return serialId;
	}
	public void setSerialId(long serialId) {
		this.serialId = serialId;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public int getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(int tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public long getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(long tradeTime) {
		this.tradeTime = tradeTime;
	}
	public int getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public long getAdminUser() {
		return adminUser;
	}
	public void setAdminUser(long adminUser) {
		this.adminUser = adminUser;
	}
	public String getTradeDesc() {
		return tradeDesc;
	}
	public void setTradeDesc(String tradeDesc) {
		this.tradeDesc = tradeDesc;
	}
	public int getShadow() {
		return shadow;
	}
	public void setShadow(int shadow) {
		this.shadow = shadow;
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
