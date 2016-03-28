package com.poison.store.service.impl;

import java.util.List;

import com.poison.store.domain.repository.PopChartsDomainRepository;
import com.poison.store.model.ChartsDb;
import com.poison.store.model.PopCharts;
import com.poison.store.service.PopChartsService;

public class PopChartsServiceImpl implements PopChartsService{

	private PopChartsDomainRepository popChartsDomainRepository;
	
	public void setPopChartsDomainRepository(
			PopChartsDomainRepository popChartsDomainRepository) {
		this.popChartsDomainRepository = popChartsDomainRepository;
	}

	/**
	 * 查询排行榜等级
	 */
	@Override
	public List<PopCharts> findPopChartsByLevel() {
		return popChartsDomainRepository.findPopChartsByLevel();
	}

	/**
	 * 根据type查询排行榜
	 */
	@Override
	public List<PopCharts> findPopChartsByType(String type) {
		return popChartsDomainRepository.findPopChartsByType(type);
	}

	/**
	 * 根据分组选择排行榜
	 */
	@Override
	public List<ChartsDb> findChartsByGroup(String chartGroup) {
		return popChartsDomainRepository.findChartsByGroup(chartGroup);
	}

}
