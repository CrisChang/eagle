package com.keel.framework.runtime;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ProductContext implements Serializable {
	private static final long serialVersionUID = -4063755073779446315L;
	
    //用户请求的服务ID，由入口产品所在环境分配
    private String requestId;
    //操作时间
    private long accessTs;

	//操作环境
	private ProductEnvironment env;
	//产品使用者
	private ProductUser productUser;

	//操作主体是否通过身份验证(即是否成功登录)
	private boolean authenticated = false;

	//产品请求的URL地址
	private String url;
	//请求referer
	private String referer;
	
	//运营活动ID
	private String activityId;
	//合作方标识
	private String partnerId;
	
    //会话ID，session
    private String sessionId;
	
    /*----- 下面的属性主要为Thread内共享使用，不需要把其中的属性进行网络传输---------*/
    //存储环境数据
    private transient Map<String, Object> context = null;
    
    public ProductContext() {
		this(false);
	}

    public ProductContext(boolean authenticated) {
		super();
		this.authenticated = authenticated;
		
        this.env = new ProductEnvironment();
        this.productUser = new ProductUser();
        
        this.requestId = UUID.randomUUID().toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getRequestId() {
		return requestId;
	}

	public ProductEnvironment getEnv() {
		return env;
	}

	public ProductUser getProductUser() {
		return productUser;
	}

    public long getAccessTs() {
		return accessTs;
	}

	public void setAccessTs(long accessTs) {
		this.accessTs = accessTs;
	}

	/**
     * 向线程环境中放置数据
     */
    public void put(String key, Object value) {
        if (null == context) {
            context = Collections.synchronizedMap(new HashMap<String, Object>());
        }
        context.put(key, value);
    }
    
    /**
     * 从环境中获取数据
     */
    public Object find(String key) {
        if (null == context) {
            context = Collections.synchronizedMap(new HashMap<String, Object>());
        }
        return context.get(key);
    }
    
    /**
     * 释放缓存中的数据
     */
    public void clean() {
        if (null != context) {
            context.clear();
        }
    }

	public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        } catch (Exception e) {
            return super.toString();
        }
    }
}
