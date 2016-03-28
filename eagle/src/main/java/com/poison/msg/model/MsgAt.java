package com.poison.msg.model;

import com.keel.common.lang.BaseDO;

public class MsgAt extends BaseDO{

	private long atId;
	private long userId;
	private long userAtId;
	private long resourceId;
	private int isDelete;
	private int isRead;
	private long atDate;
	private String type;
	private int flag;
	
	
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getAtId() {
		return atId;
	}
	public void setAtId(long atId) {
		this.atId = atId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getUserAtId() {
		return userAtId;
	}
	public void setUserAtId(long userAtId) {
		this.userAtId = userAtId;
	}
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public long getAtDate() {
		return atDate;
	}
	public void setAtDate(long atDate) {
		this.atDate = atDate;
	}
	
}
