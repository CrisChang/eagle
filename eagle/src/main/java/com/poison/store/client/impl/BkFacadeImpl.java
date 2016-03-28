package com.poison.store.client.impl;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.impl.ResourceLinkFacadeImpl;
import com.poison.resource.ext.constant.MemcacheResourceLinkConstant;
import com.poison.store.client.BkFacade;
import com.poison.store.ext.constant.MemcacheStoreConstant;
import com.poison.store.model.BkInfo;
import com.poison.store.service.BkService;

public class BkFacadeImpl implements BkFacade{

	private static final  Log LOG = LogFactory.getLog(BkFacadeImpl.class);
	
	private BkService bkService;
	//private MemcachedClient storeMemcachedClient;
	
	/*public void setStoreMemcachedClient(MemcachedClient storeMemcachedClient) {
		this.storeMemcachedClient = storeMemcachedClient;
	}*/

	public void setBkService(BkService bkService) {
		this.bkService = bkService;
	}

	/**
	 * 查询一本书的详情
	 */
	@Override
	public BkInfo findBkInfo(int id) {
		BkInfo bkInfo =new BkInfo();
	/*	try {
			bkInfo = storeMemcachedClient.get(MemcacheStoreConstant.STORE_BOOK_ID+id);*/
			if(null == bkInfo||bkInfo.getId()==0){
				bkInfo = bkService.findBkInfo(id);
				//storeMemcachedClient.set(MemcacheStoreConstant.STORE_BOOK_ID+id, MemcacheStoreConstant.TIME_INTERVALS, bkInfo);
			}
		/*} catch (TimeoutException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}*/
		
		return bkInfo;
	}

	/**
	 * 根据书名查询书的信息
	 */
	@Override
	public List<BkInfo> findBkInfoByName(String name) {
		return bkService.findBkInfoByName(name);
	}

	/**
	 * 插入一本书的信息
	 */
	@Override
	public BkInfo insertBkInfo(BkInfo bkInfo) {
		String isbn = bkInfo.getIsbn();
		String bookurl = bkInfo.getBookUrl();
		BkInfo info =new BkInfo();
		info = bkService.findBkInfoBybookurl(bookurl);
				//findBkInfoByIsbn(isbn);
		if(null!=info &&info.getId() != 0){
			return info;
		}
		 info = new BkInfo();
		int flag = bkService.insertBkInfo(bkInfo);
		if(flag == ResultUtils.ERROR){
			info.setFlag(ResultUtils.ERROR);
			return info;
		}
		info = bkService.findBkInfoBybookurl(bookurl);
				//findBkInfo(flag);
		return info;
	}

	/**
	 * 根据isbn查询一本书的信息
	 */
	@Override
	public BkInfo findBkInfoByIsbn(String isbn) {
		return bkService.findBkInfoByIsbn(isbn);
	}
	/**
	 * 根据bookurl查询一本书的信息
	 */
	@Override
	public BkInfo findBkInfoBybookurl(String bookurl) {
		return bkService.findBkInfoBybookurl(bookurl);
	}

	@Override
	public List<BkInfo> findBkInfosByIds(long[] ids) {
		return bkService.findBkInfosByIds(ids);
	}
}
