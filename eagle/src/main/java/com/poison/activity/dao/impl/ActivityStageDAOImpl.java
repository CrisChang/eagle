package com.poison.activity.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.activity.dao.ActivityStageDAO;
import com.poison.activity.model.ActivityStage;
import com.poison.eagle.utils.ResultUtils;

public class ActivityStageDAOImpl extends SqlMapClientDaoSupport implements ActivityStageDAO{

	private static final  Log LOG = LogFactory.getLog(ActivityStageDAOImpl.class);
	/**
	 * 查找最新的有效活动阶段信息
	 */
	@Override
	public ActivityStage findLatestActivityStage(String type) {
		ActivityStage activityStage = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		try{
			activityStage = (ActivityStage) getSqlMapClientTemplate().queryForObject("findLatestActivityStage",map);
			if(activityStage!=null){
				activityStage.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityStage = new ActivityStage();
			activityStage.setFlag(ResultUtils.QUERY_ERROR);
		}
		return activityStage;
	}
	/**
	 * 根据状态查询最新的活动阶段信息
	 */
	@Override
	public ActivityStage findLatestActivityStageByState(String type,int state) {
		ActivityStage activityStage = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("state", state);
		try{
			activityStage = (ActivityStage) getSqlMapClientTemplate().queryForObject("findLatestActivityStageByState",map);
			if(activityStage!=null){
				activityStage.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityStage = new ActivityStage();
			activityStage.setFlag(ResultUtils.QUERY_ERROR);
		}
		return activityStage;
	}
}
