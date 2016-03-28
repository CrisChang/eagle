package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class ResReport extends BaseDO{

	/**
	 * ResReport序列号
	 */
	private static final long serialVersionUID = 3948699356662845352L;
	private long id;					//主键id
	private long userId;				//用户id
	private long resourceId;		//资源id
	private String type;				//资源类型
	private int isDelete;				//是否删除
	private String description;		//举报描述
	private long createdate;		//创建时间
	private int flag;					//标志位
	
	
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
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreatedate() {
		return createdate;
	}
	public void setCreatedate(long createdate) {
		this.createdate = createdate;
	}
	
	
}
