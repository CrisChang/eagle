package com.keel.framework.web.session;

public interface ISessionStore {

    /**
     * 获取Session
     */
    public SessionData loadSession(String j_sessionId);

    /**
     * 更新Session
     */
    public boolean saveSession(String key, SessionData session);

    /**
     * 删除Session
     */
    public boolean removeSession(String j_sessionId);

}
