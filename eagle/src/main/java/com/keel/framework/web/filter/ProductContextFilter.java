package com.keel.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;

import com.keel.framework.runtime.ProductContext;
import com.keel.framework.runtime.ProductEnvironment;
import com.keel.framework.runtime.ProductUser;
import com.keel.framework.runtime.ServerBean;
import com.keel.framework.web.WebProductContextHolder;
import com.keel.framework.web.utils.WebHttpSpringUtils;
import com.keel.utils.web.HttpHeaderUtils;

/**
 * 产品上下文过滤器
 * */
public class ProductContextFilter extends AbstractFilter implements Filter {
	private static final  Log LOG = LogFactory.getLog(ProductContextFilter.class);
	
	private ServerBean serverBean;
	private static final String SERVER_BEAN = "serverBean";
	private static final String CLIENT_STR = "C";

    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {
    	super.doInit(filterConfig);
        
        this.serverBean = (ServerBean) WebHttpSpringUtils.getBean(ProductContextFilter.SERVER_BEAN, this.getServletContext());
        if (null == this.serverBean){
			throw new IllegalArgumentException(String.format(
					"ProductContextFilter has to need the bean of serverBean!"));
        }
        
        LOG.info("init");
    }

    @Override
    public void destroy() {
    	super.destroy();

    	this.serverBean = null;
    }

	@Override
	protected void doFilterLogic(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {
            /* set Session */
            WebProductContextHolder.setHttpSession(httpRequest.getSession());
            
            /* get product context */
            ProductContext productContext = WebProductContextHolder.getProductContext();
            // 设置sessionID FIXME：
            productContext.setSessionId(httpRequest.getRequestedSessionId());
            // 设置运营活动ID（预留）
            productContext.setActivityId("");
            // 设置partner（预留）
            productContext.setPartnerId("");
            // 设置referer
            productContext.setReferer(HttpHeaderUtils.getReferer(httpRequest));
            // 设置用户URL
            productContext.setUrl(HttpHeaderUtils.getRequestURLWithParameter(httpRequest));
            // 设置操作时间
            productContext.setAccessTs(System.currentTimeMillis());

            /* init enviroment */
            ProductEnvironment environment = productContext.getEnv();
            // 客户端IP
            String clientIp = HttpHeaderUtils.getClientIP(httpRequest);
            environment.setClientIP(clientIp);
            // 设置服务{ip,serverName}
            environment.addService(this.serverBean.getIp(), this.serverBean.getServerName());
            // 设置代理IP
            environment.setProxyIP(HttpHeaderUtils.getProxyIP(httpRequest));
            // 设置代理port(预留)
            environment.setProxyPort(null);
            // 设置客户端类型（预留）
            environment.setClientType(ProductEnvironment.CLINET_NONE);
            
            //提取客户端特征标识
            String clientStr = HttpHeaderUtils.getRequestHeader(httpRequest, ProductContextFilter.CLIENT_STR);
            if(StringUtils.isBlank(clientStr)) {
            	clientStr = "NULL";
            }
            MDC.put("CLIENT", clientStr);
            String clientOS = "";
            String clinetVersion = "";
            if(StringUtils.isNotBlank(clientStr)){
            	clientOS = StringUtils.substringBefore(clientStr, "/");
            	clinetVersion = StringUtils.substringAfterLast(clientStr, "/");
            }
            environment.setClientOS(clientOS);
            environment.setClinetVersion(clinetVersion);

            /* init product user */
            ProductUser productUser = productContext.getProductUser();
            //productUser的内容在AbstractAutoLoginFilter里填入

            if (LOG.isDebugEnabled()) {
                debug(LOG, productContext.toString());
            }
            
            chain.doFilter(request, response);

        } catch (ServletException sx) {
            LOG.error(sx.getMessage(), sx);
        } catch (IOException iox) {
            LOG.error(iox.getMessage(), iox);
        } finally {
            WebProductContextHolder.clean();
            MDC.remove("CLIENT");
            MDC.remove("A");
        }
    }


}
