package com.poison.store.service.impl;

import java.util.List;

import com.poison.store.dao.BigSelectingDAO;
import com.poison.store.domain.repository.BigSelectingDomainRepository;
import com.poison.store.model.BigSelecting;
import com.poison.store.service.BigSelectingService;

public class BigSelectingServiceImpl implements BigSelectingService{

	private BigSelectingDomainRepository bigSelectingDomainRepository;
	
	public void setBigSelectingDomainRepository(
			BigSelectingDomainRepository bigSelectingDomainRepository) {
		this.bigSelectingDomainRepository = bigSelectingDomainRepository;
	}

	/**
	 * 查找逼格的选择题
	 */
	@Override
	public List<BigSelecting> findAllBigSelecting() {
		return bigSelectingDomainRepository.findAllBigSelecting();
	}

	/**
	 * 根据ID查询一个逼格选择题
	 */
	@Override
	public BigSelecting findBigSelectingById(long id) {
		return bigSelectingDomainRepository.findBigSelectingById(id);
	}

}
