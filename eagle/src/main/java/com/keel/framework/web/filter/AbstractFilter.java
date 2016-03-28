package com.keel.framework.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.framework.runtime.ProductContext;
import com.keel.framework.web.WebProductContextHolder;
import com.keel.utils.text.StringMatchUtils;
import com.keel.utils.web.HttpHeaderUtils;

public abstract class AbstractFilter implements Filter {
	private static final  Log LOG = LogFactory.getLog(AbstractFilter.class);
	
	private final static String URL_IGNORE_LIST_PATTERN = "ignoreListPattern";
	private final static String URL_IGNORE_LIST_SUFFIX = "ignoreListSuffix";
	
	/*将要忽略的资源列表*/
	private String ignoreList = "gif,css,ico,js,swf,jpg,jpeg,png,tiff,pcx";
	/*将要忽略的模式*/
	private List<String> ignorePattern = new ArrayList<String>();
	
	private FilterConfig filterConfig;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        if (checkIgnoreFilter((HttpServletRequest) request)) {
            chain.doFilter(request, response);
        } else {
            doFilterLogic(request, response, chain);
        }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
		//Get ignore pattern
		String temp = filterConfig.getInitParameter(URL_IGNORE_LIST_PATTERN);
		
        if (StringUtils.isNotBlank(temp)) {
            String[] tempIgnorePattern = StringUtils.split(temp, ",");
            for (String tip : tempIgnorePattern) {
                this.ignorePattern.add(tip);
            }
        }
        
        //Get ignore suffix
        temp = filterConfig.getInitParameter(URL_IGNORE_LIST_SUFFIX);
        if (StringUtils.isNotBlank(temp)) {
            if ( !temp.startsWith(",") ) {
                this.ignoreList += ",";
            }
            this.ignoreList += temp;
        }
        
        doInit(this.filterConfig);
	}
	
    /**
     * 子类可以继承该方法
     */
    protected void doInit(FilterConfig filterConfig) throws ServletException {
    }
    
    /**
     * 实际的过滤逻辑
     */
    protected abstract void doFilterLogic(ServletRequest request, ServletResponse response,
                                          FilterChain chain) throws IOException, ServletException;
    
    /*--------------- 业务辅助方法 ------------------------------------------*/

    /**
     * 检查是否忽略当前请求
     * */
    protected boolean checkIgnoreFilter(HttpServletRequest request) {
        String url = HttpHeaderUtils.getRequestURL(request);
        int p = url.lastIndexOf(".");
        
        //按照后缀进行检查，可以处理大部分内容
        if (p > -1) {
        	try {
				String type = url.substring(p + 1);
	    //排除掉特殊的地址（几乎只有验证码地址）
				if (ignoreList.indexOf(type) > -1 && ! url.contains("Captcha")) {
					return true;
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
        }
        
        // 2. 按照Pattern进行检查
        for (String pattern : ignorePattern) {
            if (StringMatchUtils.stringMatch(url, pattern)) {
                return true;
            }
        }
        return false;
    }
    
    // 获取在web.xml中配置的参数
    protected String findInitParameter(String paramName, String defaultValue) {
        // 取filter参数
        String value = trimToNull(getFilterConfig().getInitParameter(paramName));

        // 如果未取到，则取全局参数
        if (value == null) {
            value = trimToNull(getServletContext().getInitParameter(paramName));
        }

        // 如果未取到，则取默认值
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }
    
    protected String trimToNull(String str) {
        if (str != null) {
            str = str.trim();

            if (str.length() == 0) {
                str = null;
            }
        }

        return str;
    }
    
    protected FilterConfig getFilterConfig() {
        return filterConfig;
    }

    protected ServletContext getServletContext() {
        return getFilterConfig().getServletContext();
    }
    
    protected void debug(Log log, String msg) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Thread.currentThread().getId()).append(" | ");
        buffer.append(Thread.currentThread().getName()).append(" | ");
        ProductContext productContext = WebProductContextHolder.getProductContext();
        buffer.append(productContext.getRequestId()).append(" | [ ");
        buffer.append(msg);
        buffer.append(" ] ");
        log.debug(buffer.toString());
    }
}
