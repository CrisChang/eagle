package com.poison.store.model;

import com.keel.common.lang.BaseDO;

/**
 * 电影角色中间 DO
 * 
 * @author :zhangqi
 * @time:2015-4-28下午5:53:57
 * @return
 */
public class MvActor extends BaseDO {

	private static final long serialVersionUID = 1L;

	private long id;
	private long actorId;
	private long mvId;
	private String mvUrl;
	private int actorType;
	private String actorUrl;
	private String actorName;// 角色名称
	private String name;// 角色名字

	private String extendField1;
	private String extendField2;
	private String extendField3;

	private long createTime;
	private long updateTime;

	private long flag;
	private int isDel;

	private Actor actor;

	public MvActor() {
		super();
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

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

	public long getMvId() {
		return mvId;
	}

	public void setMvId(long mvId) {
		this.mvId = mvId;
	}

	public String getMvUrl() {
		return mvUrl;
	}

	public void setMvUrl(String mvUrl) {
		this.mvUrl = mvUrl;
	}

	public int getActorType() {
		return actorType;
	}

	public void setActorType(int actorType) {
		this.actorType = actorType;
	}

	public String getActorUrl() {
		return actorUrl;
	}

	public void setActorUrl(String actorUrl) {
		this.actorUrl = actorUrl;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getExtendField1() {
		return extendField1;
	}

	public void setExtendField1(String extendField1) {
		this.extendField1 = extendField1;
	}

	public String getExtendField2() {
		return extendField2;
	}

	public void setExtendField2(String extendField2) {
		this.extendField2 = extendField2;
	}

	public String getExtendField3() {
		return extendField3;
	}

	public void setExtendField3(String extendField3) {
		this.extendField3 = extendField3;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

}
