package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActSubscribe extends BaseDO{

	/**
	 * ActSubscribe序列号
	 */
	private static final long serialVersionUID = -8362550669458388346L;
	private long id;
	private long userId;
	private long resourceId;
	private int isSubscribe;
	private long subscribeDate;
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
	public int getIsSubscribe() {
		return isSubscribe;
	}
	public void setIsSubscribe(int isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	public long getSubscribeDate() {
		return subscribeDate;
	}
	public void setSubscribeDate(long subscribeDate) {
		this.subscribeDate = subscribeDate;
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
