package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserBigValue extends BaseDO{

	/**
	 * UserBigValue序列号
	 */
	private static final long serialVersionUID = -3586711969343060297L;
	private long userId;									//用户ID
	private int bigLevel;									//用户的逼格等级
	private float bigValue;								//用户逼格值
	private float selfTest;								//用户自测题
	private int isDelete;									//是否被删除 0为未删除，1为已删除
	private int flag;										//标识符
	
	public int getBigLevel() {
		return bigLevel;
	}
	public void setBigLevel(int bigLevel) {
		this.bigLevel = bigLevel;
	}
	public float getSelfTest() {
		return selfTest;
	}
	public void setSelfTest(float selfTest) {
		this.selfTest = selfTest;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public float getBigValue() {
		return bigValue;
	}
	public void setBigValue(float bigValue) {
		this.bigValue = bigValue;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
