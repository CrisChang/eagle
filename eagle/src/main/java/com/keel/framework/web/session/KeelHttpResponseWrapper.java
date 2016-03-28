package com.keel.framework.web.session;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.framework.runtime.ProductContextHolder;

/**
* 该类提供了HTTP错误发生时，页面转发的能力
*/
public class KeelHttpResponseWrapper extends HttpServletResponseWrapper {
	private static final  Log LOG = LogFactory.getLog(KeelHttpResponseWrapper.class);
	
    /** 当系统发生error时，将页面转发到哪儿 */
    private String           errorPage;

    /** 当前Http应用的跟地址 */
    private String           rootAddress;

    public KeelHttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public void setRootAddress(String rootAddress) {
        this.rootAddress = rootAddress;
    }

    //---------- 重载业务方法
    /**
     * The default behavior of this method is to call sendError(int sc, String msg)
     * on the wrapped response object.
     */
    public void sendError(int sc, String msg) throws IOException {
        if (StringUtils.isNotBlank(errorPage)) {
            if (sc == 404) {
                logError(sc, msg);
                super.sendRedirect(rootAddress + errorPage);
            } else {
                if (sc != 304) {
                    logError(sc, msg);
                }
                super.sendError(sc, msg);
            }
        } else {
            super.sendError(sc, msg);
        }
    }

    /**
     * The default behavior of this method is to call sendError(int sc)
     * on the wrapped response object.
     */
    public void sendError(int sc) throws IOException {
        this.sendError(sc, "");
    }

    //--------------------------- HELP METHOD ----------------------------

    private final static String TIP_ONE   = "sendError , URL ";
    private final static String TIP_TWO   = " , sc =";
    private final static String TIP_THREE = "| msg = ";

    private void logError(int sc, String msg) {
        String url = ProductContextHolder.getProductContext().getUrl();
        StringBuilder builder = new StringBuilder();
        builder.append(TIP_ONE);
        builder.append(url);
        builder.append(TIP_TWO);
        builder.append(sc);
        builder.append(TIP_THREE);
        builder.append(msg);
        LOG.error(builder.toString());
    }
}
