package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActAtDAO;
import com.poison.act.model.ActAt;
import com.poison.eagle.utils.ResultUtils;

public class ActAtDAOImpl extends SqlMapClientDaoSupport implements ActAtDAO{

	private static final  Log LOG = LogFactory.getLog(ActAtDAOImpl.class);
	/**
	 * 插入at信息
	 */
	@Override
	public int insertintoActAt(ActAt actAt) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertintoActAt", actAt);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 查找某个资源相关的at列表
	 */
	@Override
	public List<ActAt> findResAt(long resourceid,Long id) {
		List<ActAt> actAtList = new ArrayList<ActAt>();
		ActAt actAt = new ActAt();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceid", resourceid);
			map.put("id", id);
			actAtList = getSqlMapClientTemplate().queryForList("findResComment", map);
			if(null==actAtList){
				actAtList = new ArrayList<ActAt>();
				actAt.setFlag(ResultUtils.DATAISNULL);
				actAtList.add(actAt);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actAtList = new ArrayList<ActAt>();
			actAt.setFlag(ResultUtils.QUERY_ERROR);
			actAtList.add(actAt);
		}
		return actAtList;
	}

	/**
	 * 查找某个人at别人的列表
	 */
	@Override
	public List<ActAt> findUserAt(long userid,Long id) {
		List<ActAt> actAtList = new ArrayList<ActAt>();
		ActAt actAt = new ActAt();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userid", userid);
			map.put("id", id);
			actAtList = getSqlMapClientTemplate().queryForList("findUserAt", map);
			if(null==actAtList){
				actAtList = new ArrayList<ActAt>();
				actAt.setFlag(ResultUtils.DATAISNULL);
				actAtList.add(actAt);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actAtList = new ArrayList<ActAt>();
			actAt.setFlag(ResultUtils.QUERY_ERROR);
			actAtList.add(actAt);
		}
		return actAtList;
	}
	
	/**
	 * 查找某个人被at的列表
	 */
	@Override
	public List<ActAt> findAtUser(long atUserid,Long id) {
		List<ActAt> actAtList = new ArrayList<ActAt>();
		ActAt actAt = new ActAt();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("atUserid", atUserid);
			map.put("id", id);
			actAtList = getSqlMapClientTemplate().queryForList("findAtUser", map);
			if(null==actAtList){
				actAtList = new ArrayList<ActAt>();
				actAt.setFlag(ResultUtils.DATAISNULL);
				actAtList.add(actAt);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actAtList = new ArrayList<ActAt>();
			actAt.setFlag(ResultUtils.QUERY_ERROR);
			actAtList.add(actAt);
		}
		return actAtList;
	}
	
	/**
	 * 查询某个资源相关的at的总数
	 */
	@Override
	public int findResAtCount(long resourceid) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findResAtCount",resourceid);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.QUERY_ERROR;
		}
		return i;
	}
	
	/**
	 * 查询某个资源相关的at的总数
	 */
	@Override
	public int findUserAtCount(long userid) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findUserAtCount",userid);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.QUERY_ERROR;
		}
		return i;
	}
	
	/**
	 * 查询某个人被at的总数
	 */
	@Override
	public int findAtUserCount(long atUserid) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findAtUserCount",atUserid);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.QUERY_ERROR;
		}
		return i;
	}

	/**
	 * 查询at信息根据id
	 */
	@Override
	public ActAt findAtById(long id) {
		ActAt actAt = new ActAt();
		try{
			actAt = (ActAt) getSqlMapClientTemplate().queryForObject("findAtById", id);
			if(actAt==null){
				actAt = new ActAt();
				actAt.setFlag(ResultUtils.DATAISNULL);
				return actAt;
			}
			actAt.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actAt.setFlag(ResultUtils.QUERY_ERROR);
		}
		return actAt;
	}

	/**
	 * 删除一个at信息
	 */
	@Override
	public int deleteActAt(ActAt actAt) {
		int i = ResultUtils.ERROR;
		try{
			i=getSqlMapClientTemplate().update("deleteActAt", actAt);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.UPDATE_ERROR;
		}
		return i;
	}
}