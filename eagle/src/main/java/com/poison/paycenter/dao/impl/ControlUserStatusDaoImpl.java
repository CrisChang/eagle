package com.poison.paycenter.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.manager.PaycenterManager;
import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.ControlUserStatusDao;
import com.poison.paycenter.model.ControlUserStatus;
import com.poison.paycenter.model.PayRecord;

public class ControlUserStatusDaoImpl extends SqlMapClientDaoSupport implements
		ControlUserStatusDao {
	private static final Log LOG = LogFactory.getLog(ControlUserStatusDaoImpl.class);
	@Override
	public ControlUserStatus selectUserStatus(long userId) {
		ControlUserStatus controlUserStatus = new ControlUserStatus();
		try {			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			controlUserStatus = (ControlUserStatus) getSqlMapClientTemplate().queryForObject("findControlUserStatusbyId", map);
			if(null==controlUserStatus){
				controlUserStatus = new ControlUserStatus();
				controlUserStatus.setFlag(ResultUtils.DATAISNULL);
				return controlUserStatus;
			}
			controlUserStatus.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			controlUserStatus = new ControlUserStatus();
			controlUserStatus.setFlag(ResultUtils.QUERY_ERROR);
		}		
		return controlUserStatus;
	}

	@Override
	public int insertControlUserStatus(ControlUserStatus controlUserStatusNew){
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoControlUserStatus", controlUserStatusNew);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	@Override
	public int updateControlUserStatus(long userId, int count, long time) {
		int flag = ResultUtils.ERROR;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rollCount", count);
			map.put("rollLastTime", time);
			map.put("userId", userId);
			getSqlMapClientTemplate().update("updateControlUserStatus",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

}
