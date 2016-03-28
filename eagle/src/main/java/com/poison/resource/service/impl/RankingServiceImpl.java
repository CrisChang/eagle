package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.RankingDomainRepository;
import com.poison.resource.model.Ranking;
import com.poison.resource.service.RankingService;

public class RankingServiceImpl implements RankingService{

	private RankingDomainRepository rankingDomainRepository;

	public void setRankingDomainRepository(
			RankingDomainRepository rankingDomainRepository) {
		this.rankingDomainRepository = rankingDomainRepository;
	}

	@Override
	public List<Ranking> findRanking(long start,int pagesize,String ranktype,String restype) {
		return rankingDomainRepository.findRanking(start, pagesize, ranktype,restype);
	}

}
