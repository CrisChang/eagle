package com.keel.framework.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keel.framework.web.security.UserSecurityBeanOnHeader;
import com.keel.utils.web.HttpHeaderUtils;

public class HeaderHttpSessionFilter extends AbstractHttpSessionFilter
		implements Filter {
	private final static String JSESSIONID = "X-SSID";
	
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
		sessionId = request.getHeader(HeaderHttpSessionFilter.JSESSIONID);
		return sessionId;
	}

	@Override
	protected void setSessionId(HttpServletResponse response, String sessionId) {
		HttpHeaderUtils.setResponseHeader(response, HeaderHttpSessionFilter.JSESSIONID, sessionId);
	}
}
