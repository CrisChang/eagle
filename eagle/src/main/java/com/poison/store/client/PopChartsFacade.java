package com.poison.store.client;

import java.util.List;

import com.poison.store.model.ChartsDb;
import com.poison.store.model.PopCharts;

public interface PopChartsFacade {

	/**
	 * 
	 * <p>Title: findPopChartsByLevel</p> 
	 * <p>Description: 查询等级</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午10:33:43
	 * @return
	 */
	public List<PopCharts> findPopChartsByLevel();
	
	/**
	 * 
	 * <p>Title: findPopChartsByType</p> 
	 * <p>Description: 根据type查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午10:34:12
	 * @param type
	 * @return
	 */
	public List<PopCharts> findPopChartsByType(String type);
	
	/**
	 * 
	 * <p>Title: findChartsByGroup</p> 
	 * <p>Description: 根据分组查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-5 上午11:09:48
	 * @param chartGroup
	 * @return
	 */
	public List<ChartsDb> findChartsByGroup(String chartGroup);
}
