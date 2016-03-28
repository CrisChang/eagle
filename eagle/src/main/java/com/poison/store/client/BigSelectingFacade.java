package com.poison.store.client;

import java.util.List;

import com.poison.store.model.BigSelecting;

public interface BigSelectingFacade {

	/**
	 * 
	 * <p>Title: findAllBigSelecting</p> 
	 * <p>Description: 查找逼格选择题</p> 
	 * @author :changjiang
	 * date 2014-10-6 下午2:51:26
	 * @return
	 */
	public List<BigSelecting> findAllBigSelecting();
	
	/**
	 * 
	 * <p>Title: findBigSelectingById</p> 
	 * <p>Description: 根据id查询一个逼格选择题</p> 
	 * @author :changjiang
	 * date 2014-10-9 下午11:26:37
	 * @param id
	 * @return
	 */
	public BigSelecting findBigSelectingById(long id);
}
