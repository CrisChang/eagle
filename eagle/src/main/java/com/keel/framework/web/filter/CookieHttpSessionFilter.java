package com.keel.framework.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keel.framework.web.WebConstant;
import com.keel.utils.web.HttpHeaderUtils;

public class CookieHttpSessionFilter extends AbstractHttpSessionFilter
		implements Filter {
	
	private final static String JSESSIONID = "DSESSIONID";
	
    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {
    	super.doInit(filterConfig);
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }

	@Override
	protected String getSessionId(HttpServletRequest request) {
		String sessionId = null;
		Cookie cookie = HttpHeaderUtils.getAllCookie(request).get(CookieHttpSessionFilter.JSESSIONID);
		if (null != cookie){
			sessionId = cookie.getValue();
		}
		return sessionId;
	}

	@Override
	protected void setSessionId(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(CookieHttpSessionFilter.JSESSIONID, sessionId);//MaxAge默认是-1
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        
        response.addCookie(cookie);
	}
}
