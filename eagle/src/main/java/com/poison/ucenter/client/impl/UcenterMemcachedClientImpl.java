package com.poison.ucenter.client.impl;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.keel.framework.web.session.SessionStoreImplForMemcached;
import com.poison.ucenter.client.UcenterMemcachedFacade;
import com.poison.ucenter.model.UserAllInfo;

public class UcenterMemcachedClientImpl implements UcenterMemcachedFacade{

	private static final  Log LOG = LogFactory.getLog(UcenterMemcachedClientImpl.class);
	
	private final static int UCENTER_MEMCACHED_TIME_OUT = 30 * 60 * 1000;
	private int ucenterMemcachedTimeout = UCENTER_MEMCACHED_TIME_OUT;
	
	private MemcachedClient ucenterMemcachedClient = null;
	
	public void setUcenterMemcachedTimeout(int ucenterMemcachedTimeout) {
		this.ucenterMemcachedTimeout = ucenterMemcachedTimeout;
	}

	public MemcachedClient getUcenterMemcachedClient() {
		return ucenterMemcachedClient;
	}

	public void setUcenterMemcachedClient(MemcachedClient ucenterMemcachedClient) {
		this.ucenterMemcachedClient = ucenterMemcachedClient;
	}

	@Override
	public boolean saveUserAllInfo(UserAllInfo userInfo) {
		long id = userInfo.getUserId();
		try {
			this.ucenterMemcachedClient.set("userAllInfo"+id, this.ucenterMemcachedTimeout, userInfo);
			return true;
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
	public UserAllInfo findUserAllInfo(long userId) {
		try {
			return this.ucenterMemcachedClient.get("userAllInfo"+userId);
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return null;
	}

}
