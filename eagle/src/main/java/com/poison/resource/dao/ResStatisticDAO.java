package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.ResStatistic;

public interface ResStatisticDAO {

	/**
	 * 
	 * <p>Title: insertResStatistic</p> 
	 * <p>Description: 插入资源统计类</p> 
	 * @author :changjiang
	 * date 2014-12-22 下午6:22:14
	 * @param resStatistic
	 * @return
	 */
	public int insertResStatistic(ResStatistic resStatistic);
	
	/**
	 * 
	 * <p>Title: findResStatisticById</p> 
	 * <p>Description: 根据id查询统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-22 下午6:53:52
	 * @param id
	 * @return
	 */
	public ResStatistic findResStatisticById(ResStatistic resStatistic);
	
	/**
	 * 
	 * <p>Title: findResStatisticRankByPraise</p> 
	 * <p>Description: 根据点赞信息查询排行榜</p> 
	 * @author :changjiang
	 * date 2015-6-30 下午3:42:52
	 * @param resLinkId
	 * @param resLinkType
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByPraise(long resLinkId,String resLinkType,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findResStatisticRankByUseful</p> 
	 * <p>Description: 查询有用排行榜</p> 
	 * @author :changjiang
	 * date 2015-7-27 下午6:43:28
	 * @param resLinkId
	 * @param resLinkType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByUseful(long resLinkId,String resLinkType,long stageid,int pageIndex,int pageSize);

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
	public List<ResStatistic> findResStatisticRankByUsefulAndType(String type,long resLinkId,String resLinkType,long stageid,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: updateResStatistic</p> 
	 * <p>Description: 更新资源统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-23 上午11:23:02
	 * @param resStatistic
	 * @return
	 */
	public int updateResStatistic(ResStatistic resStatistic);
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
	 * 查询热门总数
	 * @param resLinkId
	 * @param type
	 * @return
	 */
	public Map<String, Object> findResStatisticCountByLinkIdAndType(long resLinkId, String type);
}
