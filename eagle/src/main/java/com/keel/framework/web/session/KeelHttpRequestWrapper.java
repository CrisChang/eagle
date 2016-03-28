package com.keel.framework.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KeelHttpRequestWrapper extends HttpServletRequestWrapper implements
		IHttpRequestLifecycle {
	private static final  Log LOG = LogFactory.getLog(KeelHttpRequestWrapper.class);
	
	public KeelHttpSessionWrapper httpSession = null;
	
	public KeelHttpRequestWrapper(HttpServletRequest request, ISessionStore sessionStore,
            long sessionTimeout, String sessionId) {
		super(request);
        if (LOG.isDebugEnabled()) {
            LOG.debug("request.getRequestedSessionId : " + sessionId);
        }

        httpSession = new KeelHttpSessionWrapper(sessionId, sessionStore, request);

        httpSession.setTimeout(sessionTimeout);
	}
	
    /**
     * The default behavior of this method is to return getSession(boolean create)
     * on the wrapped request object.
     */
	@Override
    public HttpSession getSession(boolean create) {
        return httpSession;
    }

    /**
     * The default behavior of this method is to return getSession()
     * on the wrapped request object.
     */
	@Override
    public HttpSession getSession() {
        return httpSession;
    }

	@Override
	public void begin() {
		httpSession.begin();
	}

	@Override
	public void commit() {
        httpSession.commit();
	}
	
	@Override
	public String getRequestedSessionId(){
		if(null != httpSession){
			return httpSession.getId();
		}
		return null;
	}
}
