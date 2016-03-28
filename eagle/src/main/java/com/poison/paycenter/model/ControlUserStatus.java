package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 用户操作状态控制表
 * @author yan_dz
 *
 */
public class ControlUserStatus extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6654363783389049031L;
	/**
	 * 状态操作ID
	 */
	private long id;
	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 摇一摇次数
	 */
	private int rollCount;
	/**
	 * 最后摇一摇时间
	 */
	private long rollLastTime;
	/**
	 * 取款次数
	 */
	private int withdrawCount;
	/**
	 * 最后取款时间
	 */
	private long withdrawLastTime;
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
	public int getRollCount() {
		return rollCount;
	}
	public void setRollCount(int rollCount) {
		this.rollCount = rollCount;
	}
	public long getRollLastTime() {
		return rollLastTime;
	}
	public void setRollLastTime(long rollLastTime) {
		this.rollLastTime = rollLastTime;
	}
	public int getWithdrawCount() {
		return withdrawCount;
	}
	public void setWithdrawCount(int withdrawCount) {
		this.withdrawCount = withdrawCount;
	}
	public long getWithdrawLastTime() {
		return withdrawLastTime;
	}
	public void setWithdrawLastTime(long withdrawLastTime) {
		this.withdrawLastTime = withdrawLastTime;
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
