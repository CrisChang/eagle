package com.poison.store.dao;

import java.util.List;

import com.poison.store.model.ChartsDb;

public interface ChartsDbDAO {

	/**
	 * 
	 * <p>Title: findChartsByGroup</p> 
	 * <p>Description: 根据分组查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-5 上午10:57:28
	 * @param chartGroup
	 * @return
	 */
	public List<ChartsDb> findChartsByGroup(String chartGroup);
}
