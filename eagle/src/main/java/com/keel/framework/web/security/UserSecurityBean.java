package com.keel.framework.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.utils.secret.AESSecret;


/**
 * 安全性还不够好！
 * 1. 加强数据的变化频率，考虑加入噪声的机制（噪声间隔！！）。
 * */
public abstract class UserSecurityBean {
	private static final Log LOG = LogFactory.getLog(UserSecurityBean.class);
	
	public static final String USER_SECURITY  = "I";
	public static final String USER_SECURITY_SIGN  = "S";
	
	protected final static String KEY_STR = "5d41b6cac96117d2319d2402831f481b";
	protected final AESSecret secret;

	protected final static String SIGN_KEY_STR = "A7Bx9cXVGIzY18LJy7GaZbeYAGeOfPNDqCPLVUtvJfQPXbCCLWA8ac";
	
	public UserSecurityBean() {
		super();
		this.secret = new AESSecret(UserSecurityBean.KEY_STR);
	}
	
	/***
	 * 1. null 2. userCookie
	 * */
	public UserSecurityData getUserSecurityData(HttpServletRequest request) {
		UserSecurityData sData = null;

		String value = this.readUserSecurity(request);
		String sign = this.readUserSecuritySign(request);

		if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(sign)) {
			String tempSign = DigestUtils.md5Hex(UserSecurityBean.SIGN_KEY_STR
					+ "_" + value);
			if (StringUtils.equals(sign, tempSign)) {
				try {
					value = this.secret.decrypt(value);// 解密

					sData = new UserSecurityData(value);
					// timeout
					if (sData.getTs() < System.currentTimeMillis()) {
						sData = null;
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
					/*如果自动登录验证失败，返回null，之未登录状态*/
					//throw new RuntimeException("ERROR! to get user security data!");
				}
			}
		}

		return sData;
	}
	
	protected void setUserSecurityData(HttpServletResponse response,
			long userId, String userIP, long ts, int maxAge) {
		// FIXME: to do check!
		UserSecurityData sData = new UserSecurityData(userId, userIP, ts);

		String value;
		try {
			value = this.secret.encrypt(sData.toSecurityString());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			throw new RuntimeException("ERROR! to set user security data!");
		}
		this.writeUserSecurity(response, value, maxAge);

		String sign = DigestUtils.md5Hex(UserSecurityBean.SIGN_KEY_STR + "_"
				+ value);
		this.writeUserSecuritySign(response, sign, maxAge);
	}
	
	/*-----------------abstract method---------------------------*/
	public abstract void setShortUserSecurityData(HttpServletResponse response,
			long userId, String userIP);

	public abstract void setLongUserSecurityData(HttpServletResponse response,
			long userId, String userIP, int maxAge);
	
	protected abstract String readUserSecurity(HttpServletRequest request);
	
	protected abstract String readUserSecuritySign(HttpServletRequest request);
	
	protected abstract void writeUserSecurity(HttpServletResponse response, String value, int maxAge);
	
	protected abstract void writeUserSecuritySign(HttpServletResponse response, String value, int maxAge);
}
