package com.poison.store.domain.repository;

import java.util.List;

import com.poison.store.dao.BkSearchDAO;
import com.poison.store.model.BkSearch;

public class BkSearchDomainRepository {

	private BkSearchDAO bkSearchDAO;

	public void setBkSearchDAO(BkSearchDAO bkSearchDAO) {
		this.bkSearchDAO = bkSearchDAO;
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
		return bkSearchDAO.findBkRanking(starttime, endtime, start, pagesize);
	}
}
