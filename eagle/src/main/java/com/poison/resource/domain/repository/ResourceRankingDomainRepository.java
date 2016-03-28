package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.ResourceRankingDAO;
import com.poison.resource.model.ResourceRanking;

public class ResourceRankingDomainRepository {
	
	private ResourceRankingDAO resourceRankingDAO;

	public void setResourceRankingDAO(ResourceRankingDAO resourceRankingDAO) {
		this.resourceRankingDAO = resourceRankingDAO;
	}

	/**
	 * 根据评论数查询资源排行
	 */
	public List<ResourceRanking> findResRanking(long starttime, long endtime, long start){
		return resourceRankingDAO.findResRanking(starttime, endtime, start);
	}

}
