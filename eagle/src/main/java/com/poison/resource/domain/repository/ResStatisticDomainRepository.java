package com.poison.resource.domain.repository;

import java.util.List;
import java.util.Map;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ResCollectNumDAO;
import com.poison.resource.dao.ResStatisticDAO;
import com.poison.resource.model.ResCollectNum;
import com.poison.resource.model.ResStatistic;

public class ResStatisticDomainRepository {

	private ResStatisticDAO resStatisticDAO;
	private ResCollectNumDAO resCollectNumDAO;
	
	public void setResCollectNumDAO(ResCollectNumDAO resCollectNumDAO) {
		this.resCollectNumDAO = resCollectNumDAO;
	}

	public void setResStatisticDAO(ResStatisticDAO resStatisticDAO) {
		this.resStatisticDAO = resStatisticDAO;
	}
	
	/**
	 * 
	 * <p>Title: insertResStatistic</p> 
	 * <p>Description: 插入统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-22 下午7:04:24
	 * @param resStatistic
	 * @return
	 */
	public ResStatistic insertResStatistic(ResStatistic resStatistic){
		ResStatistic resourceStatistic = resStatisticDAO.findResStatisticById(resStatistic);
		int flag = resourceStatistic.getFlag();
		if(ResultUtils.DATAISNULL == flag){
			flag = resStatisticDAO.insertResStatistic(resStatistic);
		}
		long beforeFalseVisit = resourceStatistic.getFalseVisit();
		long beforeVisitNumber = resourceStatistic.getVisitNumber();
		long falseVisit = resStatistic.getFalseVisit();
		long visitNumber = resStatistic.getVisitNumber();
		//增量更新
		if((falseVisit-beforeFalseVisit>0)&&(visitNumber-beforeVisitNumber>0)){
			flag = resStatisticDAO.updateResStatistic(resStatistic);
			resourceStatistic = resStatisticDAO.findResStatisticById(resStatistic);
		}
		return resourceStatistic;
	}
	
	/**
	 * 
	 * <p>Title: insertResCollectNum</p> 
	 * <p>Description: 插入资源收藏</p> 
	 * @author :changjiang
	 * date 2014-12-27 下午2:02:15
	 * @param ResCollectNum
	 * @return
	 */
	public ResCollectNum insertResCollectNum(ResCollectNum ResCollectNum){
		long id = ResCollectNum.getId();
		ResCollectNum resCollectNum = resCollectNumDAO.findResCollectNumById(ResCollectNum);
		int flag = resCollectNum.getFlag();
		if(ResultUtils.DATAISNULL == flag){
			flag = resCollectNumDAO.insertResCollectNum(ResCollectNum);
		}
		long beforeFalseCollectNum = resCollectNum.getFalseCollectNum();
		long beforeIsCollectedNum = resCollectNum.getIsCollectedNum();
		long falseCollectNum = ResCollectNum.getFalseCollectNum();
		long isCollectedNum = ResCollectNum.getIsCollectedNum();
		//增量更新
		if((falseCollectNum-beforeFalseCollectNum>0)&&(isCollectedNum-beforeIsCollectedNum>0)){
			flag = resCollectNumDAO.updateResCollectNum(ResCollectNum);
		}
		resCollectNum = resCollectNumDAO.findResCollectNumById(ResCollectNum);
		return resCollectNum;
	}
	
	/**
	 * 
	 * <p>Title: findResStatisticRankByPraise</p> 
	 * <p>Description: 查询点赞的排行</p> 
	 * @author :changjiang
	 * date 2015-6-30 下午4:16:56
	 * @param resLinkId
	 * @param resLinkType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByPraise(long resLinkId,
			String resLinkType, int pageIndex, int pageSize){
		return resStatisticDAO.findResStatisticRankByPraise(resLinkId, resLinkType, pageIndex, pageSize);
	}
	
	/**
	 * 
	 * <p>Title: findResStatisticRankByUseful</p> 
	 * <p>Description: 查询有用排行</p> 
	 * @author :changjiang
	 * date 2015-7-27 下午6:47:05
	 * @param resLinkId
	 * @param resLinkType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByUseful(long resLinkId,
			String resLinkType,long stageid, int pageIndex, int pageSize){
		return resStatisticDAO.findResStatisticRankByUseful(resLinkId, resLinkType,stageid, pageIndex, pageSize);
	}

	/**
	 * 查询长书评长影评
	 * @param type
	 * @param resLinkId
	 * @param resLinkType
	 * @param stageid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<ResStatistic> findResStatisticRankByUsefulAndType(String type, long resLinkId, String resLinkType, long stageid, int pageIndex, int pageSize) {
		return resStatisticDAO.findResStatisticRankByUsefulAndType(type, resLinkId, resLinkType, stageid, pageIndex, pageSize);
	}

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
			String resLinkType,long stageid, int pageIndex, int pageSize){
		return resStatisticDAO.findResStatisticRankByVoteNum(resLinkId, resLinkType,stageid, pageIndex, pageSize);
	}
	/**
	 * 根据阅读数查询排行（某个类型资源的）
	 */
	public List<ResStatistic> findResStatisticRankByReadNum(String type,String secondtype,long pageIndex, int pageSize){
		return resStatisticDAO.findResStatisticRankByReadNum(type,secondtype, pageIndex, pageSize);
	}
	
	/**
	 * 根据阅读数查询排行(不区分资源，所有资源的排行)
	 */
	public List<ResStatistic> findAllResStatisticRankByReadNum(long pageIndex, int pageSize){
		return resStatisticDAO.findAllResStatisticRankByReadNum(pageIndex, pageSize);
	}
	
	public ResStatistic findResStatisticById(ResStatistic resStatistic) {
		return resStatisticDAO.findResStatisticById(resStatistic);
	}
	
	public int updateResStatistic(ResStatistic resStatistic) {
		return resStatisticDAO.updateResStatistic(resStatistic);
	}

	/**
	 *
	 * @param resLinkId
	 * @param type
	 * @return
	 */
	public Map<String,Object> findResStatisticCountByLinkIdAndType(long resLinkId,String type){
		return resStatisticDAO.findResStatisticCountByLinkIdAndType(resLinkId, type);
	}
}
