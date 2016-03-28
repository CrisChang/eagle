package com.poison.store.service;

import java.util.List;

import com.poison.store.model.ChartsDb;
import com.poison.store.model.PopCharts;

public interface PopChartsService {

	/**
	 * 
	 * <p>Title: findPopChartsByLevel</p> 
	 * <p>Description: 根据等级查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午10:28:12
	 * @return
	 */
	public List<PopCharts> findPopChartsByLevel();
	
	/**
	 * 
	 * <p>Title: findPopChartsByType</p> 
	 * <p>Description: 根据type查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午10:29:08
	 * @param type
	 * @return
	 */
	public List<PopCharts> findPopChartsByType(String type);
	
	/**
	 * 
	 * <p>Title: findChartsByGroup</p> 
	 * <p>Description: 根据分组查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-5 上午11:07:23
	 * @param chartGroup
	 * @return
	 */
	public List<ChartsDb> findChartsByGroup(String chartGroup);
}
