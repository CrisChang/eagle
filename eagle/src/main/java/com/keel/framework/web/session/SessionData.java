package com.keel.framework.web.session;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.keel.utils.time.TimeUtils;

public class SessionData implements Serializable {
	private static final long serialVersionUID = -1255080366594478408L;
	

    /**
     * Session创建时间
     */
    private long                createdTs       = System.currentTimeMillis();

    /**
     * Session上次修改时间
     */
    private long                lastModifiedTs;

    /**
     * Session数据
     */
    private Map<String, Object> sessionData;

    public long getCreatedTs() {
        return this.createdTs;
    }

    public void setCreatedTs(long createdTs) {
        this.createdTs = createdTs;
    }

    public long getLastModifiedTs() {
        return this.lastModifiedTs;
    }

    public void setLastModifiedTs(long lastModifiedTs) {
        this.lastModifiedTs = lastModifiedTs;
    }

    public Map<String, Object> getSessionData() {
        return this.sessionData;
    }

    public void setSessionData(Map<String, Object> sessionData) {
        this.sessionData = sessionData;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("createdDmt : ").append(TimeUtils.timestampToString(this.createdTs));
        buff.append("lastModifiedDmt : ").append(TimeUtils.timestampToString(this.lastModifiedTs));
        buff.append("sessionData : ");
        if (null != sessionData) {
            Set<String> keys = sessionData.keySet();
            Iterator<String> iterator = keys.iterator();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                buff.append(" [ ").append(key).append(" = ").append(sessionData.get(key)).append(
                    " ]");
            }
        }
        return buff.toString();
    }
}
