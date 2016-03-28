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
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.common.perf.Profiler;
import com.keel.utils.web.HttpHeaderUtils;

/**
* 类说明
*/
public class PerformanceMonitorFilter extends AbstractFilter implements Filter {

    private final static Log logger    = LogFactory.getLog(PerformanceMonitorFilter.class);

    /** 以毫秒表示的阈值 */
    private int              threshold = 500;

    /**
     * 缺省的构造方法.
     */
    public PerformanceMonitorFilter() {
        super();
    }

    @Override
    protected void doFilterLogic(ServletRequest request, ServletResponse response, FilterChain chain)
                                                                                                     throws IOException,
                                                                                                     ServletException {

        String url = ((HttpServletRequest) request).getRequestURI();
        Profiler.start("Invoking URL: " + url);

        try {
            chain.doFilter(request, response);
        } finally {
            Profiler.release();
            long elapseTime = Profiler.getDuration();

            if (elapseTime > threshold) {
                StringBuilder builder = new StringBuilder();
                builder.append("URL:").append(url);
                builder.append("的执行时间超过阈值").append(threshold).append("毫秒,");
                builder.append("实际执行时间为").append(elapseTime).append("毫秒.\r\n");
                //builder.append("URL:").append(
                //    HttpHeaderUtils.getRequestURLWithParameter(((HttpServletRequest) request)))
                //    .append("\r\n");
                builder.append(Profiler.dump());
                logger.info(builder.toString());
            } else {
                if (logger.isDebugEnabled()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("URL").append(url);
                    builder.append("实际执行时间为").append(elapseTime).append("毫秒.\r\n");
                    //builder.append("URL:").append(
                    //    HttpHeaderUtils.getRequestURLWithParameter(((HttpServletRequest) request)))
                    //    .append("\r\n");
                    logger.debug(builder.toString());
                }
            }
            //清除线程相关的资源
            Profiler.reset();
        }
    }

    @Override
    protected void doInit(FilterConfig filterConfig) throws ServletException {
        String sthreshold = filterConfig.getInitParameter("threshold");
        if (StringUtils.isNotBlank(sthreshold)) {
            threshold = NumberUtils.toInt(sthreshold);
        }
        logger.warn("init threshold = " + threshold);
    }
}
