package com.keel.framework.web.filter;

import java.io.IOException;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.framework.runtime.ProductContext;
import com.keel.framework.runtime.ProductUser;
import com.keel.framework.web.WebProductContextHolder;
import com.keel.framework.web.manager.IUserManager;
import com.keel.framework.web.security.UserSecurityData;
import com.keel.framework.web.utils.WebHttpSpringUtils;

public abstract class AbstractAutoLoginFilter extends AbstractFilter {
	private static final  Log LOG = LogFactory.getLog(AbstractAutoLoginFilter.class);
	
	private IUserManager userManager;
	private static final String USER_MANAGER_BEAN = "userManager";
	
    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {
		super.doInit(filterConfig);
		
        this.userManager = (IUserManager) WebHttpSpringUtils.getBean(AbstractAutoLoginFilter.USER_MANAGER_BEAN, this.getServletContext());
        if (null == this.userManager){
			throw new IllegalArgumentException(String.format(
					"AbstractAutoLoginFilter has to need the bean of IUserManger!"));
        }
	}
	
    @Override
    public void destroy() {
        super.destroy();
        
        this.userManager = null;
    }

	@Override
	protected void doFilterLogic(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
	}

	protected boolean autoLogin(UserSecurityData sData) {
		if(null == sData){
			return false;
		}

		ProductUser user = userManager.autoLogin(sData.getUserId());

		if (null != user) {
			ProductContext productContext = WebProductContextHolder
					.getProductContext();
			// 设置登录状态
			productContext.setAuthenticated(true);

			/* init product user */
			ProductUser productUser = productContext.getProductUser();
			// 设置product user
			productUser.copy(user);
			
			//设置日志索引A
			MDC.put("A", user.getUserId());
			
			return true;
		}
		
		return false;
	}
}
