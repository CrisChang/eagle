package com.poison.resource.model;

import com.keel.common.lang.BaseDO;

public class SerializeListLink extends BaseDO{

	/**
	 * SerializeListLink序列号
	 */
	private static final long serialVersionUID = -8808239900622589411L;
	private long id;
	private long serializeListId;
	private long serializeId;
	private long chapterId;
	private long userId;
	private int isDel;
	private long createDate;
	private long latestRevisionDate;
	private int flag;

	public long getChapterId() {
		return chapterId;
	}

	public void setChapterId(long chapterId) {
		this.chapterId = chapterId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSerializeListId() {
		return serializeListId;
	}
	public void setSerializeListId(long serializeListId) {
		this.serializeListId = serializeListId;
	}
	public long getSerializeId() {
		return serializeId;
	}
	public void setSerializeId(long serializeId) {
		this.serializeId = serializeId;
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
