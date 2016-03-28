package com.poison.store.service;

import java.util.List;

import com.poison.store.model.BigSelecting;

public interface BigSelectingService {

	/**
	 * 
	 * <p>Title: findAllBigSelecting</p> 
	 * <p>Description: 查找逼格选择题</p> 
	 * @author :changjiang
	 * date 2014-10-6 下午2:38:39
	 * @return
	 */
	public List<BigSelecting> findAllBigSelecting();
	
	/**
	 * 
	 * <p>Title: findBigSelectingById</p> 
	 * <p>Description: 根据id查询一个逼格选择题</p> 
	 * @author :changjiang
	 * date 2014-10-9 下午11:25:22
	 * @param id
	 * @return
	 */
	public BigSelecting findBigSelectingById(long id);
}
