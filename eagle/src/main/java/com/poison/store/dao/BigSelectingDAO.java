package com.poison.store.dao;

import java.util.List;

import com.poison.store.model.BigSelecting;

public interface BigSelectingDAO {

	/**
	 * 
	 * <p>Title: findAllBigSelecting</p> 
	 * <p>Description: 查找逼格的选择题</p> 
	 * @author :changjiang
	 * date 2014-10-6 下午1:57:52
	 * @return
	 */
	public  List<BigSelecting> findAllBigSelecting();
	
	/**
	 * 
	 * <p>Title: findBigSelectingById</p> 
	 * <p>Description: 根据ID查询一个题</p> 
	 * @author :changjiang
	 * date 2014-10-9 下午11:14:34
	 * @param id
	 * @return
	 */
	public BigSelecting findBigSelectingById(long id);
}
