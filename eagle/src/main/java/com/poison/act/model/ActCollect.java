package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActCollect extends BaseDO{

	/**
	 * ActCollect序列号
	 */
	private static final long serialVersionUID = -8397642204369469180L;
	private long id;
	private long userId;
	private long resourceId;
	private int isCollect;
	private long collectDate;
	private long latestRevisionDate;
	private String type;
	private int flag;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public int getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}
	public long getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(long collectDate) {
		this.collectDate = collectDate;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
