package com.poison.store.client.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.store.client.MvSearchFacade;
import com.poison.store.model.MvSearch;
import com.poison.store.service.MvSearchService;

public class MvSearchFacadeImpl implements MvSearchFacade{

	private static final  Log LOG = LogFactory.getLog(MvSearchFacadeImpl.class);
	
	private MvSearchService mvSearchService;
	
	public void setMvSearchService(MvSearchService mvSearchService) {
		this.mvSearchService = mvSearchService;
	}

	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 查询电影的热搜榜</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午4:45:18
	 * @param bkId
	 * @return
	 */
	public List<MvSearch> findMvRanking(String starttime,String endtime,long start,int pagesize){
		return mvSearchService.findMvRanking(starttime, endtime, start, pagesize);
	}
}
