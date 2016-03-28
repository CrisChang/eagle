package com.keel.framework.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 工厂类，提供对http context的创建工作
*/
public interface IHttpContextFactory {

    /**
     * 创建HttpServletRequest，而且HttpServletRequest的实现者，必须同时实现HttpRequestLifecycle

     */
    public HttpServletRequest createHttpServletRequest(HttpServletRequest proxy, String sessionId);

    /**
     * 创建HttpServletResponse
     */
    public HttpServletResponse createHttpServletResponse(HttpServletResponse proxy);
}
