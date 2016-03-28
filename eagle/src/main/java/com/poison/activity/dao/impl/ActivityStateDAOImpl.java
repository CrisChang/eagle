package com.poison.activity.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.activity.dao.ActivityStateDAO;
import com.poison.activity.model.ActivityState;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.model.MvComment;

public class ActivityStateDAOImpl extends SqlMapClientDaoSupport implements ActivityStateDAO{

	private static final  Log LOG = LogFactory.getLog(ActivityStateDAOImpl.class);
	/**
	 * 根据userid查询参赛状态
	 */
	@Override
	public ActivityState findActivityStateByUserid(long userid) {
		ActivityState activityState = null;
		try{
			activityState = (ActivityState) getSqlMapClientTemplate().queryForObject("findActivityStateByUserid",userid);
			if(activityState!=null){
				activityState.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityState = new ActivityState();
			activityState.setFlag(ResultUtils.QUERY_ERROR);
		}
		return activityState;
	}
	
	/**
	 * 报名--插入报名状态信息
	 */
	@Override
	public int addActivityState(ActivityState activityState) {
		try {
			getSqlMapClientTemplate().insert("addActivityState",activityState);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
	}
	/**
	 * 修改报名状态信息
	 */
	@Override
	public int updateActivityState(ActivityState activityState) {
		try {
			getSqlMapClientTemplate().insert("updateActivityState",activityState);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
	}
}
