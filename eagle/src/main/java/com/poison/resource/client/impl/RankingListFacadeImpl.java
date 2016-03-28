package com.poison.resource.client.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.resource.client.RankingListFacade;
import com.poison.resource.model.RankingList;
import com.poison.resource.service.RankingListService;

public class RankingListFacadeImpl implements RankingListFacade {

	private static final Log LOG = LogFactory.getLog(RankingListFacadeImpl.class);

	private RankingListService rankingListService;

	public void setRankingListService(RankingListService rankingListService) {
		this.rankingListService = rankingListService;
	}

	public List<RankingList> findRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		return rankingListService.findRankingListByScore(score, pageSize, pageStart);
	}

	public long countRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		return rankingListService.countRankingListByScore(score, pageSize, pageStart);
	}

	public List<RankingList> findRankingListByScoreWithoutTopshow(Long score, Integer pageSize, Integer pageStart, Long starttime, Long endtime) {
		return rankingListService.findRankingListByScoreWithoutTopshow(score, pageSize, pageStart, starttime, endtime);
	}

	public List<RankingList> findRankingListByTopshow(String restype, String type, Integer pageStart, Integer pageSize) {
		return rankingListService.findRankingListByTopshow(restype, type, pageStart, pageSize);
	}

	public long countRankingList(String restype, String type) {
		return rankingListService.countRankingList(restype, type);
	}

	public RankingList findRankingListByResidAndType(long resid, String restype, String type) {
		return rankingListService.findRankingListByResidAndType(resid, restype, type);
	}

}
