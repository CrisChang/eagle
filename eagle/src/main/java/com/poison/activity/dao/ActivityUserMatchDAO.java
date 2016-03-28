package com.poison.activity.dao;

import java.util.List;

import com.poison.activity.model.ActivityUserMatch;

public interface ActivityUserMatchDAO {
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
