package com.keel.framework.web.manager;

import com.keel.framework.runtime.ProductUser;

public interface IUserManager {
	public final static String USER_LOGIN_FLAG_IN_SESSION = "__user_login_flag_in_session__";
	
	/**
	 * 如果没有这个用户，返回NULL
	 * */
	public ProductUser autoLogin(long userId);

}
