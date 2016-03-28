package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActHot extends BaseDO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2343053831096954499L;
	private long id;		//主键id
	private long userId;	//用户的主键
	private long resourceId;	//资源的主键
	private String type;	//资源的类型
	private int isHot;	//热度
	private long createDate;	//创建时间
	private long latestRevisionDate;	//最后修改时间
	private int flag;	//状态
	private String ipAddress;	//ip地址
	
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
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsHot() {
		return isHot;
	}
	public void setIsHot(int isHot) {
		this.isHot = isHot;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
