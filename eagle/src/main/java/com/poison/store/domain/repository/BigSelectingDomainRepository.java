package com.poison.store.domain.repository;

import java.util.List;

import com.poison.store.dao.BigSelectingDAO;
import com.poison.store.model.BigSelecting;

public class BigSelectingDomainRepository {

	private BigSelectingDAO bigSelectingDAO;

	public void setBigSelectingDAO(BigSelectingDAO bigSelectingDAO) {
		this.bigSelectingDAO = bigSelectingDAO;
	}
	
	/**
	 * 
	 * <p>Title: findAllBigSelecting</p> 
	 * <p>Description: 查询选择题</p> 
	 * @author :changjiang
	 * date 2014-10-6 下午2:20:34
	 * @return
	 */
	public List<BigSelecting> findAllBigSelecting(){
		return bigSelectingDAO.findAllBigSelecting();
	}
	
	/**
	 * 
	 * <p>Title: findBigSelectingById</p> 
	 * <p>Description: 根据ID查询一个逼格题</p> 
	 * @author :changjiang
	 * date 2014-10-9 下午11:22:17
	 * @param id
	 * @return
	 */
	public BigSelecting findBigSelectingById(long id){
		return bigSelectingDAO.findBigSelectingById(id);
	}
}
