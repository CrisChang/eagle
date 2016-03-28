package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;



/**
 * 企业账户模型
 * @author yan_dz
 *
 */
public class CompanyAccount extends BaseDO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -530985780872208079L;
	/**
	 * 账户id
	 */
	private long id;
	/**
	 * 企业ID
	 */
	private long userId;
	/**
	 * 企业名称
	 */
	private String userName;
	/**
	 * 账户余额
	 */
	private int accountAmt;
	/**
	 * 最后修改时间
	 */
	private long changeTime;
	/**
	 * 出账总金额
	 */
	private int outTotalAmount;
	/**
	 * 
	 */
	private int dayOutTotalAmount;
	/**
	 * 
	 */
	private long outLastTime;
	/**
	 * 
	 */
	private int inTotalAmount;
	/**
	 * 
	 */
	private int dayInTotalAmount;
	/**
	 * 
	 */
	private long inLastTime;
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
	public int getAccountAmt() {
		return accountAmt;
	}
	public void setAccountAmt(int accountAmt) {
		this.accountAmt = accountAmt;
	}
	public long getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(long changeTime) {
		this.changeTime = changeTime;
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
	public int getOutTotalAmount() {
		return outTotalAmount;
	}
	public void setOutTotalAmount(int outTotalAmount) {
		this.outTotalAmount = outTotalAmount;
	}
	public int getDayOutTotalAmount() {
		return dayOutTotalAmount;
	}
	public void setDayOutTotalAmount(int dayOutTotalAmount) {
		this.dayOutTotalAmount = dayOutTotalAmount;
	}
	public long getOutLastTime() {
		return outLastTime;
	}
	public void setOutLastTime(long outLastTime) {
		this.outLastTime = outLastTime;
	}
	public int getInTotalAmount() {
		return inTotalAmount;
	}
	public void setInTotalAmount(int inTotalAmount) {
		this.inTotalAmount = inTotalAmount;
	}
	public int getDayInTotalAmount() {
		return dayInTotalAmount;
	}
	public void setDayInTotalAmount(int dayInTotalAmount) {
		this.dayInTotalAmount = dayInTotalAmount;
	}
	public long getInLastTime() {
		return inLastTime;
	}
	public void setInLastTime(long inLastTime) {
		this.inLastTime = inLastTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}		
	
}
