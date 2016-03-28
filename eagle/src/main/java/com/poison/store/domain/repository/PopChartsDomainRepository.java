package com.poison.store.domain.repository;

import java.util.List;

import com.poison.store.dao.ChartsDbDAO;
import com.poison.store.dao.PopChartsDAO;
import com.poison.store.model.ChartsDb;
import com.poison.store.model.PopCharts;

public class PopChartsDomainRepository{
	
	private PopChartsDAO popChartsDAO;
	private ChartsDbDAO chartsDbDAO;

	public void setChartsDbDAO(ChartsDbDAO chartsDbDAO) {
		this.chartsDbDAO = chartsDbDAO;
	}

	public void setPopChartsDAO(PopChartsDAO popChartsDAO) {
		this.popChartsDAO = popChartsDAO;
	}
	
	/**
	 * 
	 * <p>Title: findPopChartsByLevel</p> 
	 * <p>Description: 根据等级查询</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午10:12:11
	 * @return
	 */
	public List<PopCharts> findPopChartsByLevel(){
		return popChartsDAO.findPopChartsByLevel();
	}
	
	/**
	 * 
	 * <p>Title: findPopChartsByType</p> 
	 * <p>Description: 根据type查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午10:24:17
	 * @param type
	 * @return
	 */
	public List<PopCharts> findPopChartsByType(String type){
		return popChartsDAO.findPopChartsByType(type);
	}
	
	/**
	 * 
	 * <p>Title: findChartsByGroup</p> 
	 * <p>Description: 根据分组查询排行榜</p> 
	 * @author :changjiang
	 * date 2014-12-5 上午11:06:28
	 * @param chartGroup
	 * @return
	 */
	public List<ChartsDb> findChartsByGroup(String chartGroup){
		return chartsDbDAO.findChartsByGroup(chartGroup);
	}
}