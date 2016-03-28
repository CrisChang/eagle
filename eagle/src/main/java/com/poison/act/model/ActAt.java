package com.poison.act.model;

import com.keel.common.lang.BaseDO;

public class ActAt extends BaseDO{

	/**
	 * ActAt序列号
	 */
	private static final long serialVersionUID = 3685207217695351883L;
	private long id;				//at主键
	private long userid;			//用户ID
	private long resourceid;		//资源ID
	private long resUserid;			//作者的id
	private String type;			//资源类型
	private long atUserid;			//被at人的id
	private int isDelete;			//是否被删除0为未删除1为已删除
	private long createDate;		//at时间
	private long latestRevisionDate;//最后修改时间
	private long resid;				//基础资源id
	private String restype;			//基础资源类型
	private int flag;				//标示位

	
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getResourceid() {
		return resourceid;
	}
	public void setResourceid(long resourceid) {
		this.resourceid = resourceid;
	}
	public long getResUserid() {
		return resUserid;
	}
	public void setResUserid(long resUserid) {
		this.resUserid = resUserid;
	}
	public long getAtUserid() {
		return atUserid;
	}
	public void setAtUserid(long atUserid) {
		this.atUserid = atUserid;
	}
	public long getResid() {
		return resid;
	}
	public void setResid(long resid) {
		this.resid = resid;
	}
	public String getRestype() {
		return restype;
	}
	public void setRestype(String restype) {
		this.restype = restype;
	}
}