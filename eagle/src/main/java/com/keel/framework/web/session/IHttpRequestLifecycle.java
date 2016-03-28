package com.keel.framework.web.session;

/**
* HttpRequest的生命周期描述
*/
public interface IHttpRequestLifecycle {
    /**
     * 生命周期的开始
     */
    public void begin();

    /**
     * 生命周期的结束
     */
    public void commit();
}
