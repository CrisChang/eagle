package com.poison.store.dao;

import java.util.List;

import com.poison.store.model.PopCharts;

public interface PopChartsDAO {

	/**
	 * 
	 * <p>Title: findPopChartsByLevel</p> 
	 * <p>Description: 根据等级查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午9:50:21
	 * @return
	 */
	public List<PopCharts> findPopChartsByLevel();
	
	/**
	 * 
	 * <p>Title: findPopChartsByType</p> 
	 * <p>Description: 根据type查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午9:51:41
	 * @param type
	 * @return
	 */
	public List<PopCharts> findPopChartsByType(String type);
}
