package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserFeedBack extends BaseDO{

	/**
	 * 用户反馈
	 */
	private static final long serialVersionUID = 7559745513485613160L;

	private long id;							//主键ID
	private long userId;						//用户ID
	private long resourceId;				//资源ID
	private String resType;					//资源类型
	private String description;				//反馈描述
	private String otherInfo;				//其他信息
	private int isDelete;						//是否删除
	private long createDate;				//创建日期
	private long latestRevisionDate;	//最后修改时间
	private int flag;							//标识符
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
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
