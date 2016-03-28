
package com.keel.framework.web.session;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class SessionStoreImplForMemcached implements ISessionStore {
	private static final  Log LOG = LogFactory.getLog(SessionStoreImplForMemcached.class);
	
	private final static int SESSION_MEMCACHED_TIME_OUT = 30 * 60 * 1000;
	private int sessionMemcachedTimeout = SESSION_MEMCACHED_TIME_OUT;
	
	private MemcachedClient sessionMemcachedClient = null;

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionMemcachedTimeout = sessionTimeout;
	}

	public void setSessionMemcachedClient(MemcachedClient sessionMemcachedClient) {
		this.sessionMemcachedClient = sessionMemcachedClient;
	}

	@Override
	public SessionData loadSession(String jSessionId) {
        try {
			return (SessionData) this.sessionMemcachedClient.get(jSessionId);
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return null;
	}

	@Override
	public boolean removeSession(String jSessionId) {
		try {
			return this.sessionMemcachedClient.delete(jSessionId);
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return false;
		
	}

	@Override
	public boolean saveSession(String jSessionId, SessionData session) {
		try {
			this.sessionMemcachedClient.set(jSessionId, this.sessionMemcachedTimeout, session);
			return true;
		}  catch (TimeoutException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}

		return false;
	}
	
	public static void main(String[] argv){
		//net.rubyeye.xmemcached.XMemcachedClientBuilder
	}
}
