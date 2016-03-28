package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActCollectDAO;
import com.poison.act.model.ActCollect;
import com.poison.eagle.action.HelloworldController;
import com.poison.eagle.utils.ResultUtils;

public class ActCollectDAOImpl extends SqlMapClientDaoSupport implements ActCollectDAO{

	private static final  Log LOG = LogFactory.getLog(ActCollectDAOImpl.class);
	
	@Override
	public int findCollectCount(long resourceId) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findActCollectCount",resourceId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.QUERY_ERROR;
		}
		return i;
	}

	/**
	 * 查询用户收藏列表
	 */
	@Override
	public List<ActCollect> findCollectList(long userId,String type) {
		List<ActCollect> userCollectList = new ArrayList<ActCollect>();
		ActCollect actCollect = new ActCollect();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("type", type);
			userCollectList = getSqlMapClientTemplate().queryForList("findActCollect", map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actCollect.setFlag(ResultUtils.QUERY_ERROR);
			 userCollectList.add(actCollect);
		}
		return userCollectList;
	}

	/**
	 * 更新收藏信息
	 * 
	 */
	@Override
	public int updateCollect(ActCollect actCollect) {
		int i = ResultUtils.ERROR;
		try{
			i = getSqlMapClientTemplate().update("updateActCollect", actCollect);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.UPDATE_ERROR;
		}
		return i;
	}

	/**
	 * 添加收藏
	 */
	@Override
	public int doCollect(ActCollect actCollect) {
		int flag = ResultUtils.INSERT_ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoActCollect",actCollect);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 查询这个收藏信息是否存在
	 */
	@Override
	public ActCollect findCollectIsExist(long userId, long resId) {
		ActCollect actCollect = new ActCollect();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			actCollect = (ActCollect) getSqlMapClientTemplate().queryForObject("findCollectIsExist",map);
			if(null==actCollect){
				actCollect = new ActCollect();
				actCollect.setFlag(ResultUtils.DATAISNULL);
				return actCollect;
			}
			actCollect.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actCollect = new ActCollect();
			actCollect.setFlag(ResultUtils.QUERY_ERROR);
		}
		return actCollect;
	}

	/**
	 * 根据ID查询收藏信息
	 */
	@Override
	public ActCollect findCollectById(long id) {
		ActCollect actCollect = new ActCollect();
		try{
			actCollect = (ActCollect) getSqlMapClientTemplate().queryForObject("findCollectById",id);
			if(null==actCollect){
				actCollect = new ActCollect();
				actCollect.setFlag(ResultUtils.DATAISNULL);
				return actCollect;
			}
			actCollect.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actCollect = new ActCollect();
			actCollect.setFlag(ResultUtils.QUERY_ERROR);
		}
		return actCollect;
	}

	/**
	 * 根据用户id查询用户的收藏总数
	 */
	@Override
	public Map<String, Object> findUserCollectCount(long userId) {
		int flag = ResultUtils.ERROR;
		int count = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findUserActCollectCount",userId);
			map.put("count", count);
			map.put("flag", ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			map.put("flag", ResultUtils.ERROR);
		}
		return map;
	}

	/**
	 * 查询用户的收藏列表
	 */
	@Override
	public List<ActCollect> findUserCollectedList(long userId, Long resId) {
		List<ActCollect> userCollectList = new ArrayList<ActCollect>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("resId", resId);
			userCollectList = getSqlMapClientTemplate().queryForList("findUserCollectedList",map);
			if(null==userCollectList||userCollectList.size()==0){
				userCollectList = new ArrayList<ActCollect>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userCollectList = new ArrayList<ActCollect>();
		}
		return userCollectList;
	}

}
