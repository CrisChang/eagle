package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActUsefulDAO;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActUseful;
import com.poison.eagle.utils.ResultUtils;

public class ActUsefulDAOImpl extends SqlMapClientDaoSupport implements ActUsefulDAO{

	private static final  Log LOG = LogFactory.getLog(ActUsefulDAOImpl.class);
	/**
	 * 插入是否有用的信息
	 */
	@Override
	public int insertUseful(ActUseful actUseful) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoActUseful",actUseful);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 更新是否有用的信息
	 */
	@Override
	public int updateUseful(long id,int isUseful,long latestRevisionDate) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("id", id);
			map.put("isUseful", isUseful);
			map.put("latestRevisionDate", latestRevisionDate);
			getSqlMapClientTemplate().update("updateUseful",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据用户id和资源id查询这条资源信息
	 */
	@Override
	public ActUseful findUsefulByResidAndUserid(long resId, long userId) {
		int flag = ResultUtils.ERROR;
		ActUseful actUseful = new ActUseful();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("resId", resId);
			map.put("userId", userId);
			actUseful = (ActUseful) getSqlMapClientTemplate().queryForObject("findActUsefulByResidAndUserid",map);
			if(null== actUseful){
				actUseful = new ActUseful();
				actUseful.setFlag(ResultUtils.DATAISNULL);
				return actUseful;
			}
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			actUseful = new ActUseful();
		}
		actUseful.setFlag(flag);
		return actUseful;
	}

	/**
	 * 根据id查询是否有用这条信息
	 */
	@Override
	public ActUseful findUserfulById(long id) {
		ActUseful actUseful = new ActUseful();
		int flag = ResultUtils.ERROR;
		try{
			actUseful = (ActUseful) getSqlMapClientTemplate().queryForObject("findActUsefulById",id);
			if(null== actUseful){
				actUseful = new ActUseful();
				actUseful.setFlag(ResultUtils.DATAISNULL);
				return actUseful;
			}
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actUseful = new ActUseful();
		}
		actUseful.setFlag(flag);
		return actUseful;
	}

	/**
	 * 查询有用的总数
	 */
	@Override
	public Map<String, Object> findUsefulCount(long resId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int flag = ResultUtils.ERROR;
		int usefulCount = 0;
		try{
			usefulCount = (Integer) getSqlMapClientTemplate().queryForObject("findUsefulCount",resId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		resultMap.put("flag", flag);
		resultMap.put("usefulCount", usefulCount);
		return resultMap;
	}

	/**
	 * 查询没用的总数
	 */
	@Override
	public Map<String, Object> findUselessCount(long resId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int flag = ResultUtils.ERROR;
		int uselessCount = 0;
		try{
			uselessCount = (Integer) getSqlMapClientTemplate().queryForObject("findUselessCount",resId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		resultMap.put("flag", flag);
		resultMap.put("uselessCount", uselessCount);
		return resultMap;
	}

	/**
	 * 根据用户的id查询用户的有用列表
	 */
	@Override
	public List<ActUseful> findUsefulListByResUid(long userid, Long lastId) {
		List<ActUseful> usefulList = new ArrayList<ActUseful>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resUid", userid);
			map.put("id", lastId);
			usefulList = getSqlMapClientTemplate().queryForList("findUsefulListByResUid",map);
			if(null==usefulList||usefulList.size()==0){
				usefulList = new ArrayList<ActUseful>();
			}
		}catch (Exception e) {
			e.printStackTrace();
			usefulList = new ArrayList<ActUseful>();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return usefulList;
	}

	/**
	 * 查询有用的列表
	 */
	@Override
	public List<ActUseful> findUsefulListByResIdAndType(long resId, Long id) {
		List<ActUseful> usefulList = new ArrayList<ActUseful>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resId", resId);
			map.put("id", id);
			usefulList = getSqlMapClientTemplate().queryForList("findUsefulListByResIdAndType",map);
			if(null==usefulList||usefulList.size()==0){
				usefulList = new ArrayList<ActUseful>();
			}
		}catch (Exception e) {
			e.printStackTrace();
			usefulList = new ArrayList<ActUseful>();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return usefulList;
	}
}
