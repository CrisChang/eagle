package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActPraise extends BaseDO{

	/**
	 * ActPraise序列号
	 */
	private static final long serialVersionUID = -6534790393798523266L;
	private long id;
	private long userId;
	private long resourceId;
	private int isPraise;
	private long praiseDate;
	private long latestRevisionDate;
	private int flag;
	private String type;
	private int isLow;
	private long resUserId;
	
	
	public long getResUserId() {
		return resUserId;
	}
	public void setResUserId(long resUserId) {
		this.resUserId = resUserId;
	}
	public int getIsLow() {
		return isLow;
	}
	public void setIsLow(int isLow) {
		this.isLow = isLow;
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
	public int getIsPraise() {
		return isPraise;
	}
	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}
	public long getPraiseDate() {
		return praiseDate;
	}
	public void setPraiseDate(long praiseDate) {
		this.praiseDate = praiseDate;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
