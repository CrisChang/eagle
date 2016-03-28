package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.RankingListDomainRepository;
import com.poison.resource.model.RankingList;
import com.poison.resource.service.RankingListService;

public class RankingListServiceImpl implements RankingListService {

	private RankingListDomainRepository rankingListDomainRepository;

	public void setRankingListDomainRepository(RankingListDomainRepository rankingListDomainRepository) {
		this.rankingListDomainRepository = rankingListDomainRepository;
	}

	public List<RankingList> findRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		return rankingListDomainRepository.findRankingListByScore(score, pageSize, pageStart);
	}

	public long countRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		return rankingListDomainRepository.countRankingListByScore(score, pageSize, pageStart);
	}

	public List<RankingList> findRankingListByScoreWithoutTopshow(Long score, Integer pageSize, Integer pageStart, Long starttime, Long endtime) {
		return rankingListDomainRepository.findRankingListByScoreWithoutTopshow(score, pageSize, pageStart, starttime, endtime);
	}

	public List<RankingList> findRankingListByTopshow(String restype, String type, Integer pageStart, Integer pageSize) {
		return rankingListDomainRepository.findRankingListByTopshow(restype, type, pageStart, pageSize);
	}

	public long countRankingList(String restype, String type) {
		return rankingListDomainRepository.countRankingList(restype, type);
	}

	public RankingList findRankingListByResidAndType(long resid, String restype, String type) {
		return rankingListDomainRepository.findRankingListByResidAndType(resid, restype, type);
	}
}
