package com.poison.store.model;

import com.keel.common.lang.BaseDO;

/**
 * 角色剧照DO
 * 
 * @author zhangqi
 * 
 */
public class ActorStills extends BaseDO {

	private static final long serialVersionUID = 1L;
	private long id;
	private long actorId;
	private String actorUrl;
	private String actorStills;
	private String other;
	private int isDelete;
	private long createDate;
	private long latestRevisionDate;
	private String twoDimensionCode;

	private long flag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getActorId() {
		return actorId;
	}

	public void setActorId(long actorId) {
		this.actorId = actorId;
	}

	public String getActorStills() {
		return actorStills;
	}

	public void setActorStills(String actorStills) {
		this.actorStills = actorStills;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
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

	public String getTwoDimensionCode() {
		return twoDimensionCode;
	}

	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
	}

	public String getActorUrl() {
		return actorUrl;
	}

	public void setActorUrl(String actorUrl) {
		this.actorUrl = actorUrl;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

}
