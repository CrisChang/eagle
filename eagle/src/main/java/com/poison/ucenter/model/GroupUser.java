package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class GroupUser extends BaseDO{

	/**
	 * GroupUser序列号
	 */
	private static final long serialVersionUID = 9032857906301987296L;
	private long id;			//主键id
	private String groupid;		//群组id
	private long uid;			//用户ID
	private int isDel;			//删除标志
	private long creattime;		//创建时间
	private long updatetime;	//最后修改时间
	private int flag;			//判断标示
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}
	public long getCreattime() {
		return creattime;
	}
	public void setCreattime(long creattime) {
		this.creattime = creattime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
