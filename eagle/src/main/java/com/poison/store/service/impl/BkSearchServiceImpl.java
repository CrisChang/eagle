package com.poison.store.service.impl;

import java.util.List;

import com.poison.store.domain.repository.BkSearchDomainRepository;
import com.poison.store.model.BkSearch;
import com.poison.store.service.BkSearchService;

public class BkSearchServiceImpl implements BkSearchService{

	private BkSearchDomainRepository bkSearchDomainRepository;

	public void setBkSearchDomainRepository(
			BkSearchDomainRepository bkSearchDomainRepository) {
		this.bkSearchDomainRepository = bkSearchDomainRepository;
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
		return bkSearchDomainRepository.findBkRanking(starttime, endtime, start, pagesize);
	}
	
}
