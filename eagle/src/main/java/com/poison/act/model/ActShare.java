package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActShare extends BaseDO{

	/**
	 * ActShare序列号
	 */
	private static final long serialVersionUID = -643101302124122704L;
	private long id;
	private long userId;
	private long resourceId;
	private String shareToZone;
	private String shareContext;
	private long shareDate;
	private String ip;
	
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
	public String getShareToZone() {
		return shareToZone;
	}
	public void setShareToZone(String shareToZone) {
		this.shareToZone = shareToZone;
	}
	public String getShareContext() {
		return shareContext;
	}
	public void setShareContext(String shareContext) {
		this.shareContext = shareContext;
	}
	public long getShareDate() {
		return shareDate;
	}
	public void setShareDate(long shareDate) {
		this.shareDate = shareDate;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
