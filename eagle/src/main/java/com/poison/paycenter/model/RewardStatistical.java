package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

public class RewardStatistical extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3164627221999941293L;
	/**
	 * 
	 */
	private long id;
	/**
	 * 
	 */
	private long userId;
	/**
	 * 
	 */
	private long sourceId;
	/**
	 * 
	 */
	private int sourceType;
	/**
	 * 
	 */
	private int totalAmt;
	/**
	 * 
	 */
	private int totalCount;
	/**
	 * 
	 */
	private String extendField1;
	/**
	 * 
	 */
	private String extendField2;
	/**
	 * 
	 */
	private String extendField3;
	/**
	 * 
	 */
	private int flag;
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
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
	public long getSourceId() {
		return sourceId;
	}
	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}	
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}
	public int getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(int totalAmt) {
		this.totalAmt = totalAmt;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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
