package com.poison.store.client.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.store.client.BkSearchFacade;
import com.poison.store.model.BkSearch;
import com.poison.store.service.BkSearchService;

public class BkSearchFacadeImpl implements BkSearchFacade{

	private static final  Log LOG = LogFactory.getLog(BkSearchFacadeImpl.class);
	
	private BkSearchService bkSearchService;
	
	public void setBkSearchService(BkSearchService bkSearchService) {
		this.bkSearchService = bkSearchService;
	}

	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 查询图书的热搜榜</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午4:45:18
	 * @param bkId
	 * @return
	 */
	public List<BkSearch> findBkRanking(String starttime,String endtime,long start,int pagesize){
		return bkSearchService.findBkRanking(starttime, endtime, start, pagesize);
	}
}
