package com.poison.store.client.impl;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.store.client.BkOnlineReadFacade;
import com.poison.store.ext.constant.MemcacheStoreConstant;
import com.poison.store.model.OnlineRead;
import com.poison.store.service.BkOnlineReadService;

public class BkOnlineReadFacadeImpl implements BkOnlineReadFacade{

	private static final  Log LOG = LogFactory.getLog(BkOnlineReadFacadeImpl.class);
	
	private BkOnlineReadService bkOnlineReadService;
	private MemcachedClient storeMemcachedClient;
	
	public void setStoreMemcachedClient(MemcachedClient storeMemcachedClient) {
		this.storeMemcachedClient = storeMemcachedClient;
	}

	public void setBkOnlineReadService(BkOnlineReadService bkOnlineReadService) {
		this.bkOnlineReadService = bkOnlineReadService;
	}
	
	/**
	 * 
	 * <p>Title: insertBkOnlineRead</p> 
	 * <p>Description: 插入试读部分</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午5:35:54
	 * @param read
	 * @return
	 */
	public int insertBkOnlineRead(int bkId,String onlineRead){
		OnlineRead read = new OnlineRead();
		read = findOnlineReadByBkId(bkId,"27");
		int flag = read.getFlag();
		if(ResultUtils.DATAISNULL == flag){
			UKeyWorker uk=new UKeyWorker(0,1);
			long sysdate = System.currentTimeMillis();
			read.setId(uk.getId());
			read.setBkId(bkId);
			read.setOnlineRead(onlineRead);
			read.setIsDelete(0);
			read.setOther("");
			read.setCreateDate(sysdate);
			read.setLatestRevisionDate(sysdate);
			read.setTwoDimensionCode("");
			flag = bkOnlineReadService.insertBkOnlineRead(read);
		}
		return flag;
	}

	/**
	 *查询一本书的试读
	 */
	@Override
	public OnlineRead findOnlineReadByBkId(int bkId,String resType) {
		OnlineRead onlineRead = new OnlineRead();
		try {
			onlineRead =  storeMemcachedClient.get(MemcacheStoreConstant.STORE_BKONLINEREAD_ID+bkId+resType);
			if(null==onlineRead||onlineRead.getId()==0){
				onlineRead = bkOnlineReadService.findOnlineReadByBkId(bkId,resType);
				storeMemcachedClient.set(MemcacheStoreConstant.STORE_BKONLINEREAD_ID+bkId, MemcacheStoreConstant.TIME_INTERVALS, onlineRead);
			}
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return onlineRead;
	}

	/**
	 * 增加
	 */
	@Override
	public OnlineRead addBkTwoDimensionCode(long bkId, String twoDimensionCode,String resType) {
		OnlineRead onlineRead = new OnlineRead();
		UKeyWorker uk=new UKeyWorker(0,1);
		long sysdate = System.currentTimeMillis();
		onlineRead.setId(uk.getId());
		onlineRead.setBkId(bkId);
		onlineRead.setOnlineRead("");
		onlineRead.setOther("");
		onlineRead.setTwoDimensionCode(twoDimensionCode);
		onlineRead.setResType(resType);
		onlineRead.setCreateDate(sysdate);
		onlineRead.setLatestRevisionDate(sysdate);
		return bkOnlineReadService.updateBkOnLineRead(onlineRead);
	}
	
}
