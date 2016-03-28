package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;

public class RewardPesonStatistical extends BaseDO {
	
	private long id;
	private long userId;
	private int totalCollAmt;
	private int totalCollCount;
	private int totalPayAmt;
	private int totalPayCount;
	private String extendField1;
	private String extendField2;
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
	public int getTotalCollAmt() {
		return totalCollAmt;
	}
	public void setTotalCollAmt(int totalCollAmt) {
		this.totalCollAmt = totalCollAmt;
	}
	public int getTotalCollCount() {
		return totalCollCount;
	}
	public void setTotalCollCount(int totalCollCount) {
		this.totalCollCount = totalCollCount;
	}
	public int getTotalPayAmt() {
		return totalPayAmt;
	}
	public void setTotalPayAmt(int totalPayAmt) {
		this.totalPayAmt = totalPayAmt;
	}
	public int getTotalPayCount() {
		return totalPayCount;
	}
	public void setTotalPayCount(int totalPayCount) {
		this.totalPayCount = totalPayCount;
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
