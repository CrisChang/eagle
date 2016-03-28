package com.keel.framework.web.filter;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.utils.web.HttpHeaderUtils;

/**
 * 设置用来解析request的字符集。
 * 
 * <p>
 * <code>web.xml</code>配置文件格式如下：
 * <pre><![CDATA[
 <filter>
 <filter-name>setCharacterEncoding</filter-name>
 <filter-class>com.keel.framework.web.filter.SetCharacterEncodingFilter</filter-class>
 <init-param>
 <param-name>charset</param-name>
 <param-value>GBK</param-value>
 </init-param>
 </filter>
 *  ]]></pre>
 * </p>
 * 
 * <p>
 * 其中<code>charset</code>参数表明使用该编码字符集来解析request，如果未指定，默认为<code>UTF-8</code>。
 * </p>
 *
 */
public class SetCharacterEncodingFilter extends AbstractFilter implements Filter {
	private static final  Log LOG = LogFactory.getLog(SetCharacterEncodingFilter.class);
	private String charset;
	

    /**
     * 初始化filter, 设置参数.
     */
	@Override
    public void doInit(FilterConfig filterConfig) throws ServletException {
        this.charset = findInitParameter("charset", "UTF-8");

        LOG.info(MessageFormat.format("Set character encoding of HTTP request to {0}",
            new Object[] { this.charset }));
    }

    /**
     * 执行filter.
     */
	@Override
	protected void doFilterLogic(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
        HttpServletRequest hsr = (HttpServletRequest) request;
        hsr.setCharacterEncoding(charset);

        //FIXME: Here is not null!!
        
        if (LOG.isDebugEnabled()) {
            debug(LOG, "Set input charset=" + charset + " for request "
                          + HttpHeaderUtils.getRequestURLWithParameter(hsr));
        }

        chain.doFilter(request, response);
	}
}
