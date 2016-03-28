package com.poison.paycenter.model;

import com.keel.common.lang.BaseDO;
import com.poison.ucenter.model.UserInfo;

/**
 * 封神榜排行 DO
 * 
 * @author :zhangqi
 * @time:2015-4-20下午8:09:56
 * @return
 */
public class LegendHero extends BaseDO {

	private static final long serialVersionUID = 1L;
	private long id;
	private long userId;
	private String userName;
	private String userHeaderImg;
	private String userAuth;
	private float coll;
	private int sort;
	private int groupId;
	private String describes;
	private long updateTime;
	private long createTime;
	private String extendField1;
	private String extendField2;
	private String extendField3;

	private int flag;
	private UserInfo user;

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

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHeaderImg() {
		return userHeaderImg;
	}

	public void setUserHeaderImg(String userHeaderImg) {
		this.userHeaderImg = userHeaderImg;
	}

	public String getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(String userAuth) {
		this.userAuth = userAuth;
	}

	public float getColl() {
		return coll;
	}

	public void setColl(float coll) {
		this.coll = coll;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
