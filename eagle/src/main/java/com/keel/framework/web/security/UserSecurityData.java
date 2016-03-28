package com.keel.framework.web.security;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.keel.common.lang.BaseDO;

public class UserSecurityData extends BaseDO {
	private static final long serialVersionUID = -1940913983375235784L;

	/**
	 * 用户ID
	 * */
	private long  userId;
	/**
	 * 用户IP
	 * */
	private String  userIP;
	/**
	 * 设置时间
	 * */
	private long ts;
	
	public UserSecurityData(long userId, String userIP, long ts) {
		super();
		this.userId = userId;
		this.userIP = userIP;
		this.ts = ts;
		
		// check the valid user cookie!
		if (this.userId > 0 && this.ts > 0 && null != this.userIP) {
			return;
		}
		
		throw new IllegalArgumentException(String
				.format("UserCookie() id error!"));
	}
	
	/**
	 * value: userId;userIP;ts
	 * */
	public UserSecurityData(String value) {
		super();
		String[] list = StringUtils.split(value, ';');
		if (3 == list.length) {
			this.userId = NumberUtils.toLong(list[0]);
			this.userIP = list[1];
			this.ts = NumberUtils.toLong(list[2]);

			// check the valid user cookie!
			if (this.userId > 0 && this.ts > 0 && null != this.userIP) {
				return;
			}
		}

		throw new IllegalArgumentException(String
				.format("UserCookie(String) is error!"));
	}

	public String toSecurityString() {
		return String.valueOf(this.userId) + ";" + this.userIP + ";" + String.valueOf(this.ts);
	}

	public long getUserId() {
		return userId;
	}

	public String getUserIP() {
		return userIP;
	}

	public long getTs() {
		return ts;
	}
}
