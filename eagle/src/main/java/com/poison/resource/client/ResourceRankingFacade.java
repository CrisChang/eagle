package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.ResourceRanking;

public interface ResourceRankingFacade {
	/**
	 * 查询资源排行根据评论数量
	 * @Title: findResRanking 
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
	public List<ResourceRanking> findResRanking(long starttime, long endtime, long start);
}
