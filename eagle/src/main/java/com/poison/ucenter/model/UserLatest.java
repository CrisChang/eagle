package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class UserLatest  extends BaseDO{
	/**
	 * UserLatest序列号
	 */
	private static final long serialVersionUID = -3661224663568481214L;
	private long userid;							//基本主键
	private long resourceid;					//资源id
	private String type;						//资源类型
	private int isdel;							//是否删除
	private long createtime;//创建时间
	private long updatetime;//最后更新时间
	private long pushtime;//最后推送时间
	private int flag;								//判断标示
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	public long getPushtime() {
		return pushtime;
	}
	public void setPushtime(long pushtime) {
		this.pushtime = pushtime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
