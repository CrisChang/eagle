package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.RankingDAO;
import com.poison.resource.model.Ranking;

public class RankingDomainRepository {
	
	private RankingDAO rankingDAO;

	public void setRankingDAO(RankingDAO rankingDAO) {
		this.rankingDAO = rankingDAO;
	}

	/**
	 * 查询图书、影视的排行
	 */
	public List<Ranking> findRanking(long start, int pagesize, String ranktype,String restype){
		return rankingDAO.findRanking(start, pagesize, ranktype,restype);
	}

}
