package com.poison.store.dao;

import java.util.List;

import com.poison.store.model.MvSearch;

public interface MvSearchDAO {
	
	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 查询电影的热搜榜</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午4:45:18
	 * @param bkId
	 * @return
	 */
	public List<MvSearch> findMvRanking(String starttime,String endtime,long start,int pagesize);
}
