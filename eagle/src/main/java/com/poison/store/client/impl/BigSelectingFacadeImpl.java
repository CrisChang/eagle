package com.poison.store.client.impl;

import java.util.List;

import com.poison.resource.client.BigFacade;
import com.poison.store.client.BigSelectingFacade;
import com.poison.store.model.BigSelecting;
import com.poison.store.service.BigSelectingService;

public class BigSelectingFacadeImpl implements BigSelectingFacade{

	private BigSelectingService bigSelectingService;
	
	public void setBigSelectingService(BigSelectingService bigSelectingService) {
		this.bigSelectingService = bigSelectingService;
	}

	/**
	 * 查找逼格的选择题
	 */
	@Override
	public List<BigSelecting> findAllBigSelecting() {
		return bigSelectingService.findAllBigSelecting();
	}

	/**
	 * 根据id查询一个逼格选择题
	 */
	@Override
	public BigSelecting findBigSelectingById(long id) {
		return bigSelectingService.findBigSelectingById(id);
	}

}
