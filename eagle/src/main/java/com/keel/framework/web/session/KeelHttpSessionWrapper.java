package com.keel.framework.web.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class KeelHttpSessionWrapper implements HttpSession {
	private static final  Log LOG = LogFactory.getLog(KeelHttpSessionWrapper.class);
	
	private SessionData sessionData = null;
	
	private String sessionId = null;

    /**
     * 用来判断Session是否被改变过
     * <pre>
     * 规则：
     * 读取、写入等操作均认为已经修改过过Session
     * </pre>
     */
	private boolean sessionHasChanged = false;

	long timeout = -1;

	boolean isValid = true;

	private ISessionStore sessionStore;
	
	public KeelHttpSessionWrapper(String sessionId,
			ISessionStore sessionStore, HttpServletRequest requestProxy) {
		this.sessionId = sessionId;

		this.sessionData = new SessionData();
		this.sessionData.setSessionData(new HashMap<String, Object>());
		this.sessionStore = sessionStore;

		if (LOG.isDebugEnabled()) {
			LOG.debug(" sessionId : " + sessionId);
		}
	}
    
	@Override
	public Object getAttribute(String name) {
        this.sessionHasChanged = true;
        return this.sessionData.getSessionData().get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return new KeyIterator<String>(this.sessionData.getSessionData().keySet().iterator());
	}

	@Override
	public long getCreationTime() {
		return this.sessionData.getCreatedTs();
	}

	@Override
	public String getId() {
		return this.sessionId;
	}

	@Override
	public long getLastAccessedTime() {
        return this.sessionData.getLastModifiedTs();
	}

	//FIXME:?
	@Override
	public int getMaxInactiveInterval() {
        if (-1L <= this.timeout)
            return -1;

        return (int) (this.timeout / 1000L);
	}

	@Override
	public ServletContext getServletContext() {
        return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
        return null;
	}

	@Override
	public Object getValue(String name) {
        return getAttribute(name);
	}

	@Override
	public String[] getValueNames() {
        try {
            if (!(isValid))
                throw new IllegalStateException();

            Enumeration e = getAttributeNames();
            String[] s = new String[this.sessionData.getSessionData().size()];
            int count = 0;
            while (e.hasMoreElements())
                s[(count++)] = ((String) e.nextElement());
            return s;
        } finally {

        }
	}

	@Override
	public void invalidate() {
		this.isValid = false;
        this.sessionHasChanged = false;
        this.sessionData.getSessionData().clear();
	}

	@Override
	public boolean isNew() {
        return false;
	}

	@Override
	public void putValue(String name, Object value) {
		this.sessionData.getSessionData().put(name, value);
        this.sessionHasChanged = true;
	}

	@Override
	public void removeAttribute(String name) {
		this.sessionData.getSessionData().remove(name);
        this.sessionHasChanged = true;
	}

	@Override
	public void removeValue(String name) {
        removeAttribute(name);
        this.sessionHasChanged = true;
	}

	@Override
	public void setAttribute(String name, Object value) {
        this.putValue(name, value);
        this.sessionHasChanged = true;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
        if (interval < 0)
            this.timeout = -1L;
        else
            this.timeout = (interval * 1000L);
	}

    /* ---------------- 定制的方法 ---------------------------- */

	public void begin() {
		if (StringUtils.isNotBlank(this.sessionId)) {
			// 读取Session
			try {
				SessionData tempSessionData = this.sessionStore
						.loadSession(this.sessionId);

				// FIXME:
				// ？？在Resin服务器环境，第一次sessionId可能为NULL，第二次请求时的sessionId是第一次的cookieSessonId的值
				if ((null == tempSessionData)
						&& StringUtils.isNotBlank(getId())) {
					tempSessionData = this.sessionStore.loadSession(getId());
				}

				if (tempSessionData != null) {
					this.sessionData = tempSessionData;
					long sessionModifiedTime = this.sessionData
							.getLastModifiedTs();
					if ((System.currentTimeMillis() - sessionModifiedTime) > timeout) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("session has expired : "
									+ tempSessionData);
						}
						invalidate();
					} else {
						if (LOG.isDebugEnabled()) {
							LOG.debug("loaded session data : "
									+ tempSessionData);
						}
					}
				}
			} catch (Throwable e) {
				LOG.error("loaded session data has exception: ", e);
			}
		}
	}

	public void commit() {
		try {
			if (this.sessionHasChanged
					&& !this.sessionData.getSessionData().isEmpty()) {
				this.sessionData.setLastModifiedTs(System.currentTimeMillis());
				boolean result = this.sessionStore.saveSession(this.sessionId,
						this.sessionData);
				if (LOG.isDebugEnabled()) {
					LOG.debug(result
							+ " | commit remote session , sessionId : "
							+ this.sessionId + " | data : " + sessionData);
				}
			} else if (!this.isValid) {
				this.sessionStore.removeSession(this.sessionId);
				if (LOG.isDebugEnabled()) {
					LOG.debug(" remove remote session , sessionId : "
							+ this.sessionId);
				}
			}
		} catch (Throwable e) {
			LOG.error("loaded session data has exception: ", e);
		}
	}

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }
    
    class KeyIterator<K> implements Enumeration<K> {
        private Iterator<K> iterator;

        public KeyIterator(Iterator<K> iterator) {
            this.iterator = iterator;
        }

        public boolean hasMoreElements() {
            return this.iterator.hasNext();
        }

        public K nextElement() {
            return this.iterator.next();
        }
    }
}
