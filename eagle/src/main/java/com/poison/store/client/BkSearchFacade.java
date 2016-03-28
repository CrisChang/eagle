package com.poison.store.client;

import java.util.List;

import com.poison.store.model.BkSearch;

public interface BkSearchFacade {
	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 查询图书的热搜榜</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午4:45:18
	 * @param bkId
	 * @return
	 */
	public List<BkSearch> findBkRanking(String starttime,String endtime,long start,int pagesize);
}
