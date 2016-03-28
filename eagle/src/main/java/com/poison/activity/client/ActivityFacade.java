package com.poison.activity.client;

import java.util.List;

import com.poison.activity.model.ActivityRec;
import com.poison.activity.model.ActivityStage;
import com.poison.activity.model.ActivityState;
import com.poison.activity.model.ActivityUserMatch;

public interface ActivityFacade {
	/**
	 * 根据userid查询参赛状态
	 * @param userid
	 * @return
	 */
	public ActivityState findActivityStateByUserid(long userid);
	/**
	 * 查找最新的有效活动阶段信息
	 * @return
	 */
	public ActivityStage findLatestActivityStage(String type);
	/**
	 * 根据状态查询最新的活动阶段信息
	 */
	public ActivityStage findLatestActivityStageByState(String type,int state);
	/**
	 * 报名--插入报名状态信息
	 */
	public ActivityState addActivityState(long userid,int state,long stageid);
	/**
	 * 修改报名状态信息
	 */
	public ActivityState updateActivityState(long userid,int state,long stageid);
	/**
	 * 查询推荐列表
	 * @param userid
	 * @return
	 */
	public List<ActivityRec> findActivityRec(long stageid,String restype,long start,int pagesize);
	/**
	 * 按照发表影评数量查询用户列表信息
	 */
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyMvcount(long stageid,long start,int pagesize);
	/**
	 * 按照最新发表影评查询用户列表信息
	 */
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyId(long stageid,long start,int pagesize);
	/**
	 * 根据userid集合查询影评数量 
	 */
	public List<ActivityUserMatch> findMvCountByStageidAndUserids(long stageid,List<Long> userids);
	/**
	 * 根据用户id集合查询出某个活动阶段的所有的影评id
	 */
	public List<ActivityUserMatch> findResidsByStageidAndUserids(long stageid,List<Long> userids);
	/**
	 * 查询每个人的获得的有用数量（在一定的资源id集合中）
	 */
	public List<ActivityUserMatch> findUsefulCountForUser(List<Long> resids);
	/**
	 * 查询每个资源的有用数量
	 */
	public List<ActivityUserMatch> findUsefulCountForRes(List<Long> resids);
}
