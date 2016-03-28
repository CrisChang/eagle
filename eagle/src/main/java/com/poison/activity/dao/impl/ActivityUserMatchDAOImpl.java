package com.poison.activity.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.activity.dao.ActivityUserMatchDAO;
import com.poison.activity.model.ActivityUserMatch;

public class ActivityUserMatchDAOImpl extends SqlMapClientDaoSupport implements ActivityUserMatchDAO{

	private static final  Log LOG = LogFactory.getLog(ActivityUserMatchDAOImpl.class);
	/**
	 * 按照发表影评数量查询用户列表信息
	 */
	@Override
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyMvcount(long stageid,long start,int pagesize){
		List<ActivityUserMatch> activityUserMatchs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stageid", stageid);
		map.put("start", start);
		map.put("pagesize", pagesize);
		try{
			activityUserMatchs = getSqlMapClientTemplate().queryForList("findActivityUserMatchByStageidOrderbyMvcount",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityUserMatchs = new ArrayList<ActivityUserMatch>();
		}
		return activityUserMatchs;
	}
	
	/**
	 * 按照最新发表影评查询用户列表信息
	 */
	@Override
	public List<ActivityUserMatch> findActivityUserMatchByStageidOrderbyId(long stageid,long start,int pagesize){
		List<ActivityUserMatch> activityUserMatchs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stageid", stageid);
		map.put("start", start);
		map.put("pagesize", pagesize);
		try{
			activityUserMatchs = getSqlMapClientTemplate().queryForList("findActivityUserMatchByStageidOrderbyId",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityUserMatchs = new ArrayList<ActivityUserMatch>();
		}
		return activityUserMatchs;
	}
	
	/**
	 * 根据userid集合查询影评数量 
	 */
	@Override
	public List<ActivityUserMatch> findMvCountByStageidAndUserids(long stageid,List<Long> userids){
		List<ActivityUserMatch> activityUserMatchs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stageid", stageid);
		map.put("userids", userids);
		try{
			activityUserMatchs = getSqlMapClientTemplate().queryForList("findMvCountByStageidAndUserids",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityUserMatchs = new ArrayList<ActivityUserMatch>();;
		}
		return activityUserMatchs;
	}
	/**
	 * 根据用户id集合查询出某个活动阶段的所有的影评id
	 */
	@Override
	public List<ActivityUserMatch> findResidsByStageidAndUserids(long stageid,List<Long> userids){
		List<ActivityUserMatch> activityUserMatchs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stageid", stageid);
		map.put("userids", userids);
		try{
			activityUserMatchs = getSqlMapClientTemplate().queryForList("findResidsByStageidAndUserids",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityUserMatchs = new ArrayList<ActivityUserMatch>();
		}
		return activityUserMatchs;
	}
	
	/**
	 * 查询每个人的获得的有用数量（在一定的资源id集合中）
	 */
	@Override
	public List<ActivityUserMatch> findUsefulCountForUser(List<Long> resids){
		List<ActivityUserMatch> activityUserMatchs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resids", resids);
		try{
			activityUserMatchs = getSqlMapClientTemplate().queryForList("findUsefulCountForUser",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityUserMatchs = new ArrayList<ActivityUserMatch>();
		}
		return activityUserMatchs;
	}
	
	/**
	 * 查询每个资源的有用数量
	 */
	@Override
	public List<ActivityUserMatch> findUsefulCountForRes(List<Long> resids){
		List<ActivityUserMatch> activityUserMatchs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resids", resids);
		try{
			activityUserMatchs = getSqlMapClientTemplate().queryForList("findUsefulCountForRes",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityUserMatchs = new ArrayList<ActivityUserMatch>();
		}
		return activityUserMatchs;
	}
}
