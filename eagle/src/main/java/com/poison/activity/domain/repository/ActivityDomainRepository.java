package com.poison.activity.domain.repository;

import java.util.List;

import com.poison.activity.dao.ActivityRecDAO;
import com.poison.activity.dao.ActivityStageDAO;
import com.poison.activity.dao.ActivityStateDAO;
import com.poison.activity.dao.ActivityUserMatchDAO;
import com.poison.activity.model.ActivityRec;
import com.poison.activity.model.ActivityStage;
import com.poison.activity.model.ActivityState;
import com.poison.activity.model.ActivityUserMatch;
import com.poison.eagle.utils.ResultUtils;

public class ActivityDomainRepository {
	
	private ActivityStageDAO activityStageDAO;
	
	private ActivityStateDAO activityStateDAO;
	
	private ActivityRecDAO activityRecDAO;
	
	private ActivityUserMatchDAO activityUserMatchDAO;

	public void setActivityStageDAO(ActivityStageDAO activityStageDAO) {
		this.activityStageDAO = activityStageDAO;
	}
	public void setActivityStateDAO(ActivityStateDAO activityStateDAO) {
		this.activityStateDAO = activityStateDAO;
	}
	
	public void setActivityRecDAO(ActivityRecDAO activityRecDAO) {
		this.activityRecDAO = activityRecDAO;
	}
	public void setActivityUserMatchDAO(ActivityUserMatchDAO activityUserMatchDAO) {
		this.activityUserMatchDAO = activityUserMatchDAO;
	}
	/**
	 * 查找最新的有效活动阶段信息
	 * @return
	 */
	public ActivityStage findLatestActivityStage(String type){
		return activityStageDAO.findLatestActivityStage(type);
	}
	/**
	 * 根据状态查询最新的活动阶段信息
	 */
	public ActivityStage findLatestActivityStageByState(String type,int state){
		return activityStageDAO.findLatestActivityStageByState(type,state);
	}
	/**
	 * 根据userid查询参赛状态
	 * @param userid
	 * @return
	 */
	public ActivityState findActivityStateByUserid(long userid){
		return activityStateDAO.findActivityStateByUserid(userid);
	}
	/**
	 * 报名--插入报名状态信息
	 */
	public ActivityState addActivityState(ActivityState activityState){
		int flag = activityStateDAO.addActivityState(activityState);
		if(flag==ResultUtils.SUCCESS){
			return activityStateDAO.findActivityStateByUserid(activityState.getUserid());
		}
		activityState.setFlag(flag);
		return activityState;
	}
	/**
	 * 修改报名状态信息
	 */
	public ActivityState updateActivityState(ActivityState activityState){
		int flag = activityStateDAO.updateActivityState(activityState);
		if(flag==ResultUtils.SUCCESS){
			return activityStateDAO.findActivityStateByUserid(activityState.getUserid());
		}
		activityState.setFlag(flag);
		return activityState;
	}
	
	/**
	 * 查询推荐列表
	 * @param userid
	 * @return
	 */
	public List<ActivityRec> findActivityRec(long stageid,String restype,long start,int pagesize){
		return activityRecDAO.findActivityRec(stageid, restype, start, pagesize);
	}
	/**
	 * 按照发表影评数量查询用户列表信息
	 */
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyMvcount(long stageid,long start,int pagesize){
		return activityUserMatchDAO.findActivityUserMatchByStageidOrderbyMvcount(stageid, start, pagesize);
	}
	/**
	 * 按照最新发表影评查询用户列表信息
	 */
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyId(long stageid,long start,int pagesize){
		return activityUserMatchDAO.findActivityUserMatchByStageidOrderbyId(stageid, start, pagesize);
	}
	/**
	 * 根据userid集合查询影评数量 
	 */
	public List<ActivityUserMatch> findMvCountByStageidAndUserids(long stageid,List<Long> userids){
		return activityUserMatchDAO.findMvCountByStageidAndUserids(stageid, userids);
	}
	/**
	 * 根据用户id集合查询出某个活动阶段的所有的影评id
	 */
	public List<ActivityUserMatch> findResidsByStageidAndUserids(long stageid,List<Long> userids){
		return activityUserMatchDAO.findResidsByStageidAndUserids(stageid, userids);
	}
	/**
	 * 查询每个人的获得的有用数量（在一定的资源id集合中）
	 */
	public List<ActivityUserMatch> findUsefulCountForUser(List<Long> resids){
		return activityUserMatchDAO.findUsefulCountForUser(resids);
	}
	/**
	 * 查询每个资源的有用数量
	 */
	public List<ActivityUserMatch> findUsefulCountForRes(List<Long> resids){
		return activityUserMatchDAO.findUsefulCountForRes(resids);
	}
}
