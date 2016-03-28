package com.poison.activity.service.impl;

import java.util.List;

import com.poison.activity.domain.repository.ActivityDomainRepository;
import com.poison.activity.model.ActivityRec;
import com.poison.activity.model.ActivityStage;
import com.poison.activity.model.ActivityState;
import com.poison.activity.model.ActivityUserMatch;
import com.poison.activity.service.ActivityService;

public class ActivityServiceImpl implements ActivityService{

	private ActivityDomainRepository activityDomainRepository;

	public void setActivityDomainRepository(
			ActivityDomainRepository activityDomainRepository) {
		this.activityDomainRepository = activityDomainRepository;
	}

	/**
	 * 根据userid查询参赛状态
	 * @param userid
	 * @return
	 */
	@Override
	public ActivityState findActivityStateByUserid(long userid){
		return activityDomainRepository.findActivityStateByUserid(userid);
	}
	/**
	 * 根据状态查询最新的活动阶段信息
	 */
	@Override
	public ActivityStage findLatestActivityStageByState(String type,int state){
		return activityDomainRepository.findLatestActivityStageByState(type,state);
	}
	/**
	 * 查找最新的有效活动阶段信息
	 * @return
	 */
	@Override
	public ActivityStage findLatestActivityStage(String type){
		return activityDomainRepository.findLatestActivityStage(type);
	}
	/**
	 * 报名--插入报名状态信息
	 */
	@Override
	public ActivityState addActivityState(ActivityState activityState){
		return activityDomainRepository.addActivityState(activityState);
	}
	/**
	 * 修改报名状态信息
	 */
	@Override
	public ActivityState updateActivityState(ActivityState activityState){
		return activityDomainRepository.updateActivityState(activityState);
	}
	/**
	 * 查询推荐列表
	 * @param userid
	 * @return
	 */
	@Override
	public List<ActivityRec> findActivityRec(long stageid,String restype,long start,int pagesize){
		return activityDomainRepository.findActivityRec(stageid, restype, start, pagesize);
	}
	/**
	 * 按照发表影评数量查询用户列表信息
	 */
	@Override
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyMvcount(long stageid,long start,int pagesize){
		return activityDomainRepository.findActivityUserMatchByStageidOrderbyMvcount(stageid, start, pagesize);
	}
	/**
	 * 按照最新发表影评查询用户列表信息
	 */
	@Override
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyId(long stageid,long start,int pagesize){
		return activityDomainRepository.findActivityUserMatchByStageidOrderbyId(stageid, start, pagesize);
	}
	/**
	 * 根据userid集合查询影评数量 
	 */
	@Override
	public List<ActivityUserMatch> findMvCountByStageidAndUserids(long stageid,List<Long> userids){
		return activityDomainRepository.findMvCountByStageidAndUserids(stageid, userids);
	}
	/**
	 * 根据用户id集合查询出某个活动阶段的所有的影评id
	 */
	@Override
	public List<ActivityUserMatch> findResidsByStageidAndUserids(long stageid,List<Long> userids){
		return activityDomainRepository.findResidsByStageidAndUserids(stageid, userids);
	}
	/**
	 * 查询每个人的获得的有用数量（在一定的资源id集合中）
	 */
	@Override
	public List<ActivityUserMatch> findUsefulCountForUser(List<Long> resids){
		return activityDomainRepository.findUsefulCountForUser(resids);
	}
	/**
	 * 查询每个资源的有用数量
	 */
	@Override
	public List<ActivityUserMatch> findUsefulCountForRes(List<Long> resids){
		return activityDomainRepository.findUsefulCountForRes(resids);
	}
}