package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.RankingListDAO;
import com.poison.resource.model.RankingList;

public class RankingListDomainRepository {

	private RankingListDAO rankingListDAO;

	public void setRankingListDAO(RankingListDAO rankingListDAO) {
		this.rankingListDAO = rankingListDAO;
	}

	public List<RankingList> findRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		return rankingListDAO.findRankingListByScore(score, pageSize, pageStart);
	}

	public long countRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		return rankingListDAO.countRankingListByScore(score, pageSize, pageStart);
	}

	public List<RankingList> findRankingListByScoreWithoutTopshow(Long score, Integer pageSize, Integer pageStart, Long starttime, Long endtime) {
		return rankingListDAO.findRankingListByScoreWithoutTopshow(score, pageSize, pageStart, starttime, endtime);
	}

	public List<RankingList> findRankingListByTopshow(String restype, String type, Integer pageStart, Integer pageSize) {
		return rankingListDAO.findRankingListByTopshow(restype, type, pageStart, pageSize);
	}

	public long countRankingList(String restype, String type) {
		return rankingListDAO.countRankingList(restype, type);
	}

	public RankingList findRankingListByResidAndType(long resid, String restype, String type) {
		return rankingListDAO.findRankingListByResidAndType(resid, restype, type);
	}

}