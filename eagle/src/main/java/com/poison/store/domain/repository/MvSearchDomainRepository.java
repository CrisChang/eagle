package com.poison.store.domain.repository;

import java.util.List;

import com.poison.store.dao.MvSearchDAO;
import com.poison.store.model.MvSearch;

public class MvSearchDomainRepository {

	private MvSearchDAO mvSearchDAO;

	public void setMvSearchDAO(MvSearchDAO mvSearchDAO) {
		this.mvSearchDAO = mvSearchDAO;
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
		return mvSearchDAO.findMvRanking(starttime, endtime, start, pagesize);
	}
}
