package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.RankingList;

public interface RankingListFacade {

	public List<RankingList> findRankingListByScore(Long score, Integer pageSize, Integer pageStart);

	public long countRankingListByScore(Long score, Integer pageSize, Integer pageStart);

	public List<RankingList> findRankingListByScoreWithoutTopshow(Long score, Integer pageSize, Integer pageStart, Long starttime, Long endtime);

	public List<RankingList> findRankingListByTopshow(String restype, String type, Integer pageStart, Integer pageSize);

	public long countRankingList(String restype, String type);

	public RankingList findRankingListByResidAndType(long resid, String restype, String type);

}
