package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActSubscribeDAO;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActSubscribe;
import com.poison.eagle.utils.ResultUtils;

public class ActSubscribeDAOImpl extends SqlMapClientDaoSupport implements ActSubscribeDAO{

	private static final  Log LOG = LogFactory.getLog(ActSubscribeDAOImpl.class);
	/**
	 * 查询订阅列表
	 */
	@Override
	public List<ActSubscribe> findSubscribeList(long userId,String type) {
		List<ActSubscribe> subscribeList = new ArrayList<ActSubscribe>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("type", type);
			subscribeList = getSqlMapClientTemplate().queryForList("findActSubscribeList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			subscribeList = new ArrayList<ActSubscribe>();
			ActSubscribe actSubscribe = new ActSubscribe();
			actSubscribe.setFlag(ResultUtils.QUERY_ERROR);
			subscribeList.add(actSubscribe);
		}
		return subscribeList;
	}

	/**
	 * 插入订阅信息
	 */
	@Override
	public int insertSubscribe(ActSubscribe actSubscribe) {
		int flag = ResultUtils.INSERT_ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoActSubscribe",actSubscribe);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 查询订阅信息是否存在
	 */
	@Override
	public ActSubscribe findSubscribeIsExist(long userId, long resId) {
		ActSubscribe actSubscribe = new ActSubscribe();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			actSubscribe = (ActSubscribe) getSqlMapClientTemplate().queryForObject("findSubscribeIsExist",map);
			if(null==actSubscribe){
				actSubscribe = new ActSubscribe();
				actSubscribe.setFlag(ResultUtils.DATAISNULL);
				return actSubscribe;
			}
			actSubscribe.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actSubscribe = new ActSubscribe();
			actSubscribe.setFlag(ResultUtils.QUERY_ERROR);
		}
		return actSubscribe;
	}

	/**
	 * 更新订阅信息
	 */
	@Override
	public int updateSubscribe(ActSubscribe actSubscribe) {
		int i = ResultUtils.ERROR;
		try{
			i = getSqlMapClientTemplate().update("updateActSubscribe", actSubscribe);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.UPDATE_ERROR;
		}
		return i;
	}

	/**
	 * 查询订阅连载的列表
	 */
	@Override
	public List<ActSubscribe> findSubscribeListByResId(long resId) {
		List<ActSubscribe> list = new ArrayList<ActSubscribe>();
		try{
			list = getSqlMapClientTemplate().queryForList("findSubscribeListByResId",resId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ActSubscribe>();
			ActSubscribe actSubscribe = new ActSubscribe();
			actSubscribe.setFlag(ResultUtils.QUERY_ERROR);
			list.add(actSubscribe);
		}
		return list;
	}

}
