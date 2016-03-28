package com.poison.store.client.impl;

import java.util.List;

import com.poison.store.client.PopChartsFacade;
import com.poison.store.model.ChartsDb;
import com.poison.store.model.PopCharts;
import com.poison.store.service.PopChartsService;

public class PopChartsFacadeImpl implements PopChartsFacade{

	private PopChartsService popChartsService;
	
	public void setPopChartsService(PopChartsService popChartsService) {
		this.popChartsService = popChartsService;
	}

	/**
	 * 根据等级查询排行榜
	 */
	@Override
	public List<PopCharts> findPopChartsByLevel() {
		return popChartsService.findPopChartsByLevel();
	}

	/**
	 * 根据type查询排行榜
	 */
	@Override
	public List<PopCharts> findPopChartsByType(String type) {
		return popChartsService.findPopChartsByType(type);
	}

	/**
	 * 根据分组查询排行榜
	 */
	@Override
	public List<ChartsDb> findChartsByGroup(String chartGroup) {
		return popChartsService.findChartsByGroup(chartGroup);
	}

	
}
