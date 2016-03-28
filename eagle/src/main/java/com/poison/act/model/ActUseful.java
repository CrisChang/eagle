package com.poison.act.model;

import com.keel.common.lang.BaseDO;

/**
 * <p>Title: ActUseable.java</p> 
 * <p>Description: 示例类</p> 
 * @author :changjiang
 * date 2015-6-8 下午8:04:34
 */
public class ActUseful extends BaseDO{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -154455742755269517L;
	private long id;
	private long userId;
	private long resourceId;
	private int isUseful;
	private long createDate;
	private long latestRevisionDate;
	private int flag;
	private String resType;
	private long resUserId;
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
	public int getIsUseful() {
		return isUseful;
	}
	public void setIsUseful(int isUseful) {
		this.isUseful = isUseful;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
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
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public long getResUserId() {
		return resUserId;
	}
	public void setResUserId(long resUserId) {
		this.resUserId = resUserId;
	}
	
	
}
