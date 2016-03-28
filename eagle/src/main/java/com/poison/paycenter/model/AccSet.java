package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

/**
 * 账户总设置(包括提现次数、时间、金额等等的设置)
 * @author wei
 *
 */
public class AccSet extends BaseDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2516759696907292887L;
	/**
	 * 用户号ID
	 */
	private long id;
	/**
	 * 提现最低金额
	 */
	private int leastAmount;
	
	/**
	 * 每月提现次数限制
	 */
	private int limitTimes;
	/**
	 * 每月提现开始时间（从月初开始后的多长时间，单位天）
	 */
	private int startTime;
	/**
	 * 每月提现结束时间(从月初开始后的多长时间，单位天)
	 */
	private int endTime;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 修改时间
	 */
	private long updateTime;
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
	
	public int getLeastAmount() {
		return leastAmount;
	}

	public void setLeastAmount(int leastAmount) {
		this.leastAmount = leastAmount;
	}

	public int getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(int limitTimes) {
		this.limitTimes = limitTimes;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
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
