package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.ResourceRankingDomainRepository;
import com.poison.resource.model.ResourceRanking;
import com.poison.resource.service.ResourceRankingService;

public class ResourceRankingServiceImpl implements ResourceRankingService{

	private ResourceRankingDomainRepository resourceRankingDomainRepository;
	
	public void setResourceRankingDomainRepository(
			ResourceRankingDomainRepository resourceRankingDomainRepository) {
		this.resourceRankingDomainRepository = resourceRankingDomainRepository;
	}

	@Override
	public List<ResourceRanking> findResRanking(long starttime, long endtime,
			long start) {
		return resourceRankingDomainRepository.findResRanking(starttime, endtime, start);
	}

}
