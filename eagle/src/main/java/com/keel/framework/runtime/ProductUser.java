package com.keel.framework.runtime;

import com.keel.common.lang.BaseDO;

/**
 * 用户的基础属性（跨系统间常用属性）
 * */
public class ProductUser extends BaseDO {

	private static final long serialVersionUID = -4687905409141596298L;
	
	//登录ID
	private String logonId;
	//操作员ID
	private String platformId;
	//用户ID
	private String userId;

	//用户nick
	private String nickName;
	//用户email地址
	private String userEmail;
	//用户电话
	private String userPhone;

	//用户登录IP地址
	private String lastLoginIP;
	//用户登录时间
	private long lastLoginTs;
	//登录类型
	private int logonType = 0;

	//用户具有的角色
	private String[] roles;

	public String getLogonId() {
		return logonId;
	}

	public void setLogonId(String logonId) {
		this.logonId = logonId;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public long getLastLoginTs() {
		return lastLoginTs;
	}

	public void setLastLoginTs(long lastLoginTs) {
		this.lastLoginTs = lastLoginTs;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public int getLogonType() {
		return logonType;
	}

	public void setLogonType(int logonType) {
		this.logonType = logonType;
	}
	
	public void copy(ProductUser modelUser) {
		if (null != modelUser) {

			// 设置用户电话
			this.setUserPhone(modelUser.getUserPhone());
			// 设置用户ID
			this.setUserId(modelUser.getUserId());
			// 设置登录ID
			this.setLogonId(modelUser.getLogonId());
			// 最后登陆时间
			this.setLastLoginTs(modelUser.getLastLoginTs());
			// 最后登陆IP
			this.setLastLoginIP(modelUser.getLastLoginIP());
			// 用户邮箱
			this.setUserEmail(modelUser.getUserEmail());
			// 设置用户昵称
			this.setNickName(modelUser.getNickName());
			// 设置登录类型
			this.setLogonType(modelUser.getLogonType());
			// 设置平台ID
			this.setPlatformId(modelUser.getPlatformId());
			// 设置角色（预留）-------- FIXME: 有缺陷
			this.setRoles(modelUser.getRoles());
		}
	}
}
