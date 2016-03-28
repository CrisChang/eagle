package com.keel.framework.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 工厂类，提供对http context的封装，同时该类将配置在Spring环境中，
* 通过Spring的注入功能设置Http环境需要的一些参数
*/
public class KeelHttpContextFactoryImpl implements IHttpContextFactory {
	private final static long SESSION_TIME_OUT = 30 * 60 * 1000;
	
	private ISessionStore sessionStore = null;

	private long sessionTimeout = SESSION_TIME_OUT;

	private String errorPage;
    
	@Override
	public HttpServletRequest createHttpServletRequest(HttpServletRequest proxy, String sessionId) {
        // 通过Http环境工厂来传入所有需要的参数
        return new KeelHttpRequestWrapper(proxy, this.sessionStore, this.sessionTimeout, sessionId);
	}

	@Override
	public HttpServletResponse createHttpServletResponse(
			HttpServletResponse proxy) {
        KeelHttpResponseWrapper wrapper = new KeelHttpResponseWrapper(proxy);
        wrapper.setErrorPage(errorPage);
        return wrapper;
	}

	public void setSessionStore(ISessionStore sessionStore) {
		this.sessionStore = sessionStore;
	}

	public void setSessionTimeout(long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
}
