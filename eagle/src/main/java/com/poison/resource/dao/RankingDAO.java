package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.Ranking;

public interface RankingDAO {
	/**
	 * 查询图书、影视的排行
	 * @Title: findRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午4:10:47
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<ResourceRanking>
	 * @throws
	 */
	public List<Ranking> findRanking(long start,int pagesize,String ranktype,String restype);	
}
