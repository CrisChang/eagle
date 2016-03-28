package com.poison.resource.service.impl;

import com.poison.resource.domain.repository.BigDomainRepository;
import com.poison.resource.model.Big;
import com.poison.resource.service.BigService;

public class BigServiceImpl implements BigService{

	private BigDomainRepository bigDomainRepository;
	
	public void setBigDomainRepository(BigDomainRepository bigDomainRepository) {
		this.bigDomainRepository = bigDomainRepository;
	}

	/**
	 * 查询big详情
	 */
	@Override
	public Big findBig(String attribute, String branch, String value) {
		return bigDomainRepository.findBig(attribute, branch, value);
	}

}
