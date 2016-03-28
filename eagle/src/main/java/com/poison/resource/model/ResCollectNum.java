package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class ResCollectNum extends BaseDO{

	/**
	 * ResCollectNum序列号
	 */
	private static final long serialVersionUID = -7258351494418694645L;
	private long id;						//主键id
	private long resId;					//资源id
	private String type;					//类型
	private long falseCollectNum;	//运营的收藏数量
	private long isCollectedNum;	//被收藏的数量
	private int isDelete;					//是否被删除
	private long latestRevisionDate;	//最后修改时间
	private int flag;						//标识符
	
	
	public long getResId() {
		return resId;
	}
	public void setResId(long resId) {
		this.resId = resId;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getFalseCollectNum() {
		return falseCollectNum;
	}
	public void setFalseCollectNum(long falseCollectNum) {
		this.falseCollectNum = falseCollectNum;
	}
	public long getIsCollectedNum() {
		return isCollectedNum;
	}
	public void setIsCollectedNum(long isCollectedNum) {
		this.isCollectedNum = isCollectedNum;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public long getLatestRevisionDate() {
		return latestRevisionDate;
	}
	public void setLatestRevisionDate(long latestRevisionDate) {
		this.latestRevisionDate = latestRevisionDate;
	}
	
	
}
