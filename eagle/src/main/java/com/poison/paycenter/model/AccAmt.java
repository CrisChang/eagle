package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 账户资金模型
 * @author yan_dz
 *
 */
public class AccAmt extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3752801637260286510L;
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
	 * 账户余额
	 */
	private int accountAmt;
	/**
	 * 赏金
	 */
	private int rewardAmount;
	/**
	 * 最后修改时间
	 */
	private long changeTime;
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
	public int getAccountAmt() {
		return accountAmt;
	}
	public void setAccountAmt(int accountAmt) {
		this.accountAmt = accountAmt;
	}
	public int getRewardAmount() {
		return rewardAmount;
	}
	public void setRewardAmount(int rewardAmount) {
		this.rewardAmount = rewardAmount;
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
