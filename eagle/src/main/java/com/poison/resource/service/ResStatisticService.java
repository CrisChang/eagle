package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.ResCollectNum;
import com.poison.resource.model.ResStatistic;

public interface ResStatisticService {

	/**
	 * 
	 * <p>Title: insertResStatistic</p> 
	 * <p>Description: 插入资源统计类</p> 
	 * @author :changjiang
	 * date 2014-12-23 上午10:27:36
	 * @param resStatistic
	 * @return
	 */
	public ResStatistic insertResStatistic(ResStatistic resStatistic);
	
	/**
	 * 
	 * <p>Title: insertResCollectNum</p> 
	 * <p>Description: 插入资源收藏</p> 
	 * @author :changjiang
	 * date 2014-12-27 下午5:16:53
	 * @param ResCollectNum
	 * @return
	 */
	public ResCollectNum insertResCollectNum(ResCollectNum ResCollectNum);
	
	
	public ResStatistic findResStatisticById(ResStatistic resStatistic) ;
	
	public int updateResStatistic(ResStatistic resStatistic);
	
	/**
	 * 
	 * <p>Title: findResStatisticRankByPraise</p> 
	 * <p>Description: 根据点赞查询排行榜</p> 
	 * @author :changjiang
	 * date 2015-6-30 下午4:17:48
	 * @param resLinkId
	 * @param resLinkType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByPraise(long resLinkId,
			String resLinkType, int pageIndex, int pageSize);
	
	/**
	 * 
	 * <p>Title: findResStatisticRankByUseful</p> 
	 * <p>Description: 查询有用排行榜</p> 
	 * @author :changjiang
	 * date 2015-7-27 下午6:49:37
	 * @param resLinkId
	 * @param resLinkType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByUseful(long resLinkId,
			String resLinkType,long stageid, int pageIndex, int pageSize);

	/**
	 * 查询长书评或者长影评
	 * @param type
	 * @param resLinkId
	 * @param resLinkType
	 * @param stageid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByUsefulAndType(String type, long resLinkId, String resLinkType, long stageid, int pageIndex, int pageSize);

	/**
	 * 根据投票数查询排行
	 * @param resLinkId
	 * @param resLinkType
	 * @param stageid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByVoteNum(long resLinkId,
			String resLinkType,long stageid, int pageIndex, int pageSize);
	/**
	 * 根据阅读数查询排行（某个类型资源的）
	 */
	public List<ResStatistic> findResStatisticRankByReadNum(String type,String secondtype,long pageIndex, int pageSize);
	/**
	 * 根据阅读数查询排行(不区分资源，所有资源的排行)
	 */
	public List<ResStatistic> findAllResStatisticRankByReadNum(long pageIndex, int pageSize);

	/**
	 * 查询热门列表
	 * @param resLinkId
	 * @param type
	 * @return
	 */
	public Map<String,Object> findResStatisticByLinkIdAndType(long resLinkId,String type);
}
