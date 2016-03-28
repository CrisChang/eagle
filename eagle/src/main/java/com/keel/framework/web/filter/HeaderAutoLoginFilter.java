package com.keel.framework.web.filter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.framework.web.manager.IUserManager;
import com.keel.framework.web.security.UserSecurityBean;
import com.keel.framework.web.security.UserSecurityBeanOnCookie;
import com.keel.framework.web.security.UserSecurityBeanOnHeader;
import com.keel.framework.web.security.UserSecurityData;
import com.keel.framework.web.utils.WebHttpSpringUtils;
import com.keel.utils.web.HttpHeaderUtils;

public class HeaderAutoLoginFilter extends AbstractAutoLoginFilter implements Filter {
	private static final  Log LOG = LogFactory.getLog(HeaderAutoLoginFilter.class);
	
	private UserSecurityBean userSecurityBean;
	private final static String USER_SECURITY_BEAN_ON_HEADER = "userSecurityBeanOnHeader";
	
	/**
	 * 最大并发自动登录用户
	 */
	public static final String MAX_CURRENT_AUTO_LOGON = "maxCurrentAutoLogin";
	/**
	 * 最大并发自动登录用户，默认20个
	 * */
	private long maxCurrentAutoLogon = 20;
	/**
	 * 计数器
	 * */
	private AtomicInteger autoLogonCount = new AtomicInteger(0);
    
    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {
    	super.doInit(filterConfig);
    	
        String temp = filterConfig.getInitParameter(CookieAutoLoginFilter.MAX_CURRENT_AUTO_LOGON);
        if (StringUtils.isNotEmpty(temp)) {
            this.maxCurrentAutoLogon = NumberUtils.toLong(temp);
        }
        
        this.userSecurityBean = (UserSecurityBeanOnHeader) WebHttpSpringUtils.getBean(HeaderAutoLoginFilter.USER_SECURITY_BEAN_ON_HEADER, this.getServletContext());
        if (null == this.userSecurityBean){
			throw new IllegalArgumentException(String.format(
					"HeaderAutoLoginFilter has to need the bean of userSecurityBeanOnHeader!"));
        }

        LOG.info("init");
    }
    
    @Override
    public void destroy() {
        super.destroy();

        this.userSecurityBean = null;
    }

	@Override
	protected void doFilterLogic(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (autoLogonCount.get() <= maxCurrentAutoLogon) {
			UserSecurityData sData = this.userSecurityBean
					.getUserSecurityData(httpRequest);

			if (null != sData && 0 < sData.getUserId()) {
				try {
					autoLogonCount.incrementAndGet();
					super.autoLogin(sData);
				} finally {
					autoLogonCount.decrementAndGet();
				}
			}
		} else {
			UserSecurityData userCookie = this.userSecurityBean
					.getUserSecurityData(httpRequest);
			if (null != userCookie) {
				LOG.error("并发自动登录用户超过最大值：" + autoLogonCount.get() + ";userId="
						+ userCookie.getUserId());
			}
		}

		if (LOG.isDebugEnabled()) {
			debug(LOG, HttpHeaderUtils.getAllCookie(httpRequest).toString());
		}

		chain.doFilter(request, response);
	}
}
