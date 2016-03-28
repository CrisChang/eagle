package com.poison.ucenter.model;

import com.keel.common.lang.BaseDO;

public class TalentZoneLink extends BaseDO{


	/**
	 * TalentZoneLink序列号
	 */
	private static final long serialVersionUID = 7790603589367297234L;
	private long id;								//主键
	private long talentZoneId;				//达人圈ID
	private int userId;							//达人ID
	private int isDel;								//是否删除
	private long createDate;					//创建时间
	private long latestRevisionDate;		//最后修改时间
	private int flag;								//标示位
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTalentZoneId() {
		return talentZoneId;
	}
	public void setTalentZoneId(long talentZoneId) {
		this.talentZoneId = talentZoneId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
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
	
	
}
