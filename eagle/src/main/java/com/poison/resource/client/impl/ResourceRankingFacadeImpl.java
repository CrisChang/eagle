package com.poison.resource.client.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.resource.client.ResourceRankingFacade;
import com.poison.resource.model.ResourceRanking;
import com.poison.resource.service.ResourceRankingService;

public class ResourceRankingFacadeImpl implements ResourceRankingFacade{

	private static final  Log LOG = LogFactory.getLog(ResourceRankingFacadeImpl.class);
	
	private ResourceRankingService resourceRankingService;

	public void setResourceRankingService(
			ResourceRankingService resourceRankingService) {
		this.resourceRankingService = resourceRankingService;
	}

	@Override
	public List<ResourceRanking> findResRanking(long starttime, long endtime,
			long start) {
		return resourceRankingService.findResRanking(starttime, endtime, start);
	}
}
