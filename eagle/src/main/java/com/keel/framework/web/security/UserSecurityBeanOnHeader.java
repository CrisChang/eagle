package com.keel.framework.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.utils.web.HttpHeaderUtils;

public class UserSecurityBeanOnHeader extends UserSecurityBean{
	private static final Log LOG = LogFactory.getLog(UserSecurityBeanOnHeader.class);

	public static final String USER_HEADER  = "X-I";
	public static final String USER_HEADER_SIGN  = "X-S";
	
	private final static long DEFAULT_USER_TIMEOUT_FOR_SHORT = 30 * 24 * 60 * 60 * 1000L; // 24后小时过期, 临时改为30天
	private final static long DEFAULT_USER_TIMEOUT_FOR_LONG = 30 * 24 * 60 * 60 * 1000L; // 30天
	
	@Override
	protected String readUserSecurity(HttpServletRequest request) {
		return HttpHeaderUtils.getRequestHeader(request, UserSecurityBeanOnHeader.USER_HEADER);
	}
	
	@Override
	protected String readUserSecuritySign(HttpServletRequest request) {
		return HttpHeaderUtils.getRequestHeader(request, UserSecurityBeanOnHeader.USER_HEADER_SIGN);
	}
	
	@Override
	protected void writeUserSecurity(HttpServletResponse response,
			String value, int maxAge) {
		//放弃maxAge
		HttpHeaderUtils.setResponseHeader(response, UserSecurityBeanOnHeader.USER_HEADER, value);
		
	}
	
	@Override
	protected void writeUserSecuritySign(HttpServletResponse response,
			String value, int maxAge) {
		//放弃maxAge
		HttpHeaderUtils.setResponseHeader(response, UserSecurityBeanOnHeader.USER_HEADER_SIGN, value);
	}
	
	@Override
	public void setLongUserSecurityData(HttpServletResponse response,
			long userId, String userIP, int maxAge) {
		long expiresTime = System.currentTimeMillis()
				+ UserSecurityBeanOnHeader.DEFAULT_USER_TIMEOUT_FOR_LONG;
		//放弃maxAge
		this.setUserSecurityData(response, userId, userIP, expiresTime, 0);

	}

	@Override
	public void setShortUserSecurityData(HttpServletResponse response,
			long userId, String userIP) {
		long expiresTime = System.currentTimeMillis()
				+ UserSecurityBeanOnHeader.DEFAULT_USER_TIMEOUT_FOR_SHORT;
		//放弃maxAge
		this.setUserSecurityData(response, userId, userIP, expiresTime, 0);
	}
}
