package com.keel.framework.web.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.keel.framework.web.session.IHttpContextFactory;
import com.keel.framework.web.session.IHttpRequestLifecycle;
import com.keel.framework.web.session.KeelHttpResponseWrapper;
import com.keel.framework.web.utils.WebHttpSpringUtils;
import com.keel.utils.web.HttpHeaderUtils;

public abstract class AbstractHttpSessionFilter extends AbstractFilter {
	private static final  Log LOG = LogFactory.getLog(AbstractHttpSessionFilter.class);
	
	/**
	 * 来自于spring环境,构造函数中复制
	 * */
	private IHttpContextFactory  httpContextFactory;
	
    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {
    	super.doInit(filterConfig);
    	
        this.httpContextFactory = (IHttpContextFactory) WebHttpSpringUtils.getBean("httpContextFactory", this.getServletContext());
		if (null == this.httpContextFactory) {
			throw new IllegalArgumentException(
					String
							.format("AbstractHttpSessionFilter has to need the bean of IHttpContextFactory!"));

		}
        LOG.warn("init AbstractHttpSessionFilter OK .");
    }
    
    @Override
    public void destroy() {
        super.destroy();
        this.httpContextFactory = null;
    }

	@Override
	protected void doFilterLogic(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String sessionId = this.getSessionId(httpRequest);
        String newSessionId = null;
        
        if (null == sessionId) {
        	//newSessionId = String.valueOf(UUID.randomUUID().hashCode());
        	newSessionId = String.valueOf(UUID.randomUUID());
        	sessionId = newSessionId;
        }
        
        if (null != newSessionId) {
        	this.setSessionId(httpResponse, sessionId);
        }

        HttpServletRequest requestWrapper = httpContextFactory.createHttpServletRequest(httpRequest, sessionId);

        HttpServletResponse responseWrapper = httpContextFactory.createHttpServletResponse(httpResponse);

        // 设置该属性是为了错误页面转发，以后也可能用在其它功能中
        String rootAddress = HttpHeaderUtils.getHttpRootAddress(requestWrapper);
        ((KeelHttpResponseWrapper) responseWrapper).setRootAddress(rootAddress);
        
        try {
            if (LOG.isDebugEnabled()) {
                debug(LOG, "user Http Server session , httpRequest.getSession().getId() = "
                              + httpRequest.getSession().getId());
            }
            ((IHttpRequestLifecycle) requestWrapper).begin();
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            ((IHttpRequestLifecycle) requestWrapper).commit();
        }
	}
	
	protected abstract String getSessionId(HttpServletRequest request);
	
	protected abstract void setSessionId(HttpServletResponse response, String sessionId);
}
