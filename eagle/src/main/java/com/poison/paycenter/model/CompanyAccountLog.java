package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

public class CompanyAccountLog extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4086123659286559426L;
	/**
	 * 变更记录ID
	 */
	private long id;
	/**
	 * 企业ID
	 */
	private long companyId;
	/**
	 * 企业名称
	 */
	private String companyName;
	/**
	 * 交易类型
	 */
	private int tradeType;
	/**
	 * 内部流水号
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
	 * 管理员用户ID
	 */
	private long adminUser;
	/**
	 * 交易描述
	 */
	private String tradeDesc;
	/**
	 * 隐藏标识
	 */
	private int shadow;
	/**
	 * 扩展字段
	 */
	private String extendField1;
	/**
	 * 扩展字段
	 */
	private String extendField2;
	/**
	 * 扩展字段
	 */
	private String extendField3;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
}
