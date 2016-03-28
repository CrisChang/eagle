package com.poison.resource.client.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.resource.client.RankingFacade;
import com.poison.resource.model.Ranking;
import com.poison.resource.service.RankingService;

public class RankingFacadeImpl implements RankingFacade{

	private static final  Log LOG = LogFactory.getLog(RankingFacadeImpl.class);
	
	private RankingService rankingService;

	public void setRankingService(RankingService rankingService) {
		this.rankingService = rankingService;
	}

	@Override
	public List<Ranking> findRanking(long start,int pagesize,String ranktype,String restype){
		return rankingService.findRanking(start, pagesize, ranktype,restype);
	}
}
