package com.poison.easemob.model;

import com.keel.common.lang.BaseDO;

public class EasemobUser extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;				//用户ID
	private String easemobId;		//环信ID
	private long createDate;		//创建日期
	private String description;		//描述
	private int flag;					//正确标识
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getEasemobId() {
		return easemobId;
	}
	public void setEasemobId(String easemobId) {
		this.easemobId = easemobId;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
