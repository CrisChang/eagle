package com.poison.store.service.impl;

import java.util.List;

import com.poison.store.domain.repository.MvSearchDomainRepository;
import com.poison.store.model.MvSearch;
import com.poison.store.service.MvSearchService;

public class MvSearchServiceImpl implements MvSearchService{

	private MvSearchDomainRepository mvSearchDomainRepository;

	public void setMvSearchDomainRepository(
			MvSearchDomainRepository mvSearchDomainRepository) {
		this.mvSearchDomainRepository = mvSearchDomainRepository;
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
		return mvSearchDomainRepository.findMvRanking(starttime, endtime, start, pagesize);
	}
}
