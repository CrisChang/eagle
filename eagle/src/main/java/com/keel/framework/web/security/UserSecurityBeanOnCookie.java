package com.keel.framework.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.keel.framework.web.WebConstant;
import com.keel.utils.web.HttpHeaderUtils;

public class UserSecurityBeanOnCookie extends UserSecurityBean {
	private static final Log LOG = LogFactory.getLog(UserSecurityBeanOnCookie.class);
	
	public static final String USER_COOKIE  = "I";
	public static final String USER_COOKIE_SIGN  = "S";
	
	private final static long DEFAULT_USER_COOKIE_TIMEOUT_FOR_LOGIN = 24 * 60 * 60 * 1000L; // 24后小时过期
	private final static long DEFAULT_USER_COOKIE_TIMEOUT_FOR_AUTOLOGIN = 30 * 24 * 60 * 60 * 1000L; // 30天
	

	@Override
	public void setShortUserSecurityData(HttpServletResponse response,
			long userId, String userIP) {
		long expiresTime = System.currentTimeMillis()
				+ UserSecurityBeanOnCookie.DEFAULT_USER_COOKIE_TIMEOUT_FOR_LOGIN;
		this.setUserSecurityData(response, userId, userIP, expiresTime, -1);
	}
	
	@Override
	public void setLongUserSecurityData(HttpServletResponse response,
			long userId, String userIP, int maxAge) {
		long expiresTime = System.currentTimeMillis()
				+ UserSecurityBeanOnCookie.DEFAULT_USER_COOKIE_TIMEOUT_FOR_AUTOLOGIN;
		// FIXME: 有缺陷，but，系统最大设置一个月
		this.setUserSecurityData(response, userId, userIP, expiresTime,
				(int) (UserSecurityBeanOnCookie.DEFAULT_USER_COOKIE_TIMEOUT_FOR_AUTOLOGIN / 1000));
	}

	@Override
	protected String readUserSecurity(HttpServletRequest request) {
		return HttpHeaderUtils.getCookieValue(
				UserSecurityBeanOnCookie.USER_COOKIE, request);
	}

	@Override
	protected String readUserSecuritySign(HttpServletRequest request) {
		return HttpHeaderUtils.getCookieValue(
				UserSecurityBeanOnCookie.USER_COOKIE_SIGN, request);
	}

	@Override
	protected void writeUserSecurity(HttpServletResponse response, String value, int maxAge) {
		HttpHeaderUtils.saveCookie(response, UserSecurityBeanOnCookie.USER_COOKIE,
				value, maxAge, WebConstant.ROOT_DOMAIN, true);
	}

	@Override
	protected void writeUserSecuritySign(HttpServletResponse response, String value, int maxAge) {
		HttpHeaderUtils.saveCookie(response, UserSecurityBeanOnCookie.USER_COOKIE_SIGN, value,
				maxAge, WebConstant.ROOT_DOMAIN, true);
	}
}
