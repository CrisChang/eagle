package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.domain.repository.ResStatisticDomainRepository;
import com.poison.resource.model.ResCollectNum;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.ResStatisticService;

public class ResStatisticServiceImpl implements ResStatisticService{

	private ResStatisticDomainRepository resStatisticDomainRepository;
	
	public void setResStatisticDomainRepository(
			ResStatisticDomainRepository resStatisticDomainRepository) {
		this.resStatisticDomainRepository = resStatisticDomainRepository;
	}

	/**
	 * 插入资源统计信息
	 */
	@Override
	public ResStatistic insertResStatistic(ResStatistic resStatistic) {
		return resStatisticDomainRepository.insertResStatistic(resStatistic);
	}

	/**
	 * 插入资源收藏信息
	 */
	@Override
	public ResCollectNum insertResCollectNum(ResCollectNum ResCollectNum) {
		return resStatisticDomainRepository.insertResCollectNum(ResCollectNum);
	}
	
	@Override
	public ResStatistic findResStatisticById(ResStatistic resStatistic) {
		return resStatisticDomainRepository.findResStatisticById(resStatistic);
	}
	
	@Override
	public int updateResStatistic(ResStatistic resStatistic){
		return resStatisticDomainRepository.updateResStatistic(resStatistic);
	}

	/**
	 * 根据点赞查询排行榜
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByPraise(long resLinkId,
			String resLinkType, int pageIndex, int pageSize) {
		return resStatisticDomainRepository.findResStatisticRankByPraise(resLinkId, resLinkType, pageIndex, pageSize);
	}

	/**
	 * 查询有用排行榜
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByUseful(long resLinkId,
			String resLinkType,long stageid, int pageIndex, int pageSize) {
		return resStatisticDomainRepository.findResStatisticRankByUseful(resLinkId, resLinkType,stageid, pageIndex, pageSize);
	}

	/**
	 *
	 * @param type
	 * @param resLinkId
	 * @param resLinkType
	 * @param stageid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByUsefulAndType(String type, long resLinkId, String resLinkType, long stageid, int pageIndex, int pageSize) {
		return resStatisticDomainRepository.findResStatisticRankByUsefulAndType(type, resLinkId, resLinkType, stageid, pageIndex, pageSize);
	}

	/**
	 * 根据投票数查询排行
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByVoteNum(long resLinkId,
			String resLinkType,long stageid, int pageIndex, int pageSize) {
		return resStatisticDomainRepository.findResStatisticRankByVoteNum(resLinkId, resLinkType,stageid, pageIndex, pageSize);
	}
	/**
	 * 根据阅读数查询排行（某个类型资源的）
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByReadNum(String type,String secondtype,long pageIndex, int pageSize){
		return resStatisticDomainRepository.findResStatisticRankByReadNum(type,secondtype, pageIndex, pageSize);
	}
	
	/**
	 * 根据阅读数查询排行(不区分资源，所有资源的排行)
	 */
	@Override
	public List<ResStatistic> findAllResStatisticRankByReadNum(long pageIndex, int pageSize){
		return resStatisticDomainRepository.findAllResStatisticRankByReadNum(pageIndex, pageSize);
	}

	/**
	 * 查询总数
	 * @param resLinkId
	 * @param type
	 * @return
	 */
	@Override
	public Map<String, Object> findResStatisticByLinkIdAndType(long resLinkId, String type) {
		return resStatisticDomainRepository.findResStatisticCountByLinkIdAndType(resLinkId, type);
	}
}
