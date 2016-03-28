package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActPraiseDAO;
import com.poison.act.model.ActPraise;
import com.poison.eagle.utils.ResultUtils;

public class ActPraiseDAOImpl extends SqlMapClientDaoSupport implements ActPraiseDAO{

	private static final  Log LOG = LogFactory.getLog(ActPraiseDAOImpl.class);
	/**
	 * 插入点赞信息
	 */
	@Override
	public int insertPraise(ActPraise actPraise) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertintoActPraise", actPraise);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 查询点赞信息
	 */
	@SuppressWarnings("deprecation")
	@Override
	public ActPraise findActPraise(ActPraise actPraise) {
		ActPraise Praise = new ActPraise();
		try{
			Praise = (ActPraise) getSqlMapClientTemplate().queryForObject("findActPraise",actPraise);
			if(null==Praise){
				Praise = new ActPraise();
				Praise.setFlag(ResultUtils.DATAISNULL);
			}else{
				Praise.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			Praise.setFlag(ResultUtils.QUERY_ERROR);
		}
		return Praise;
	}


	/**
	 * 查询点赞的总数
	 */
	@Override
	public int findPraiseCount(long resourceId) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findActPraiseCount", resourceId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.QUERY_ERROR;
		}
		return i;
	}

	/**
	 * 更新点赞信息
	 */
	@Override
	public int updatePraise(ActPraise actPraise) {
		int i = ResultUtils.ERROR;
		try{
			i = getSqlMapClientTemplate().update("updateActPraise", actPraise);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.UPDATE_ERROR;
		}
		return i;
	}

	/**
	 * 查询点low的总数
	 */
	@Override
	public int findLowCount(long resourceId) {
		int count = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findActLowCount",resourceId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = ResultUtils.ERROR;
		}
		return count;
	}

	/**
	 * 根据资源id和资源类型查询点赞列表
	 */
	@Override
	public List<ActPraise> findPraiseListByResIdAndType(long resId,
			 Long id) {
		List<ActPraise> praiseList = new ArrayList<ActPraise>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resId", resId);
			map.put("id", id);
			praiseList = getSqlMapClientTemplate().queryForList("findPraiseListByResIdAndType",map);
			if(null==praiseList||praiseList.size()==0){
				praiseList = new ArrayList<ActPraise>();
			}
		}catch (Exception e) {
			e.printStackTrace();
			praiseList = new ArrayList<ActPraise>();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return praiseList;
	}

	/**
	 * 根据用户id查询用户的点赞列表
	 */
	@Override
	public List<ActPraise> findPraiseListByResUid(long resUid, Long id) {
		List<ActPraise> praiseList = new ArrayList<ActPraise>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resUid", resUid);
			map.put("id", id);
			praiseList = getSqlMapClientTemplate().queryForList("findPraiseListByResUid",map);
			if(null==praiseList||praiseList.size()==0){
				praiseList = new ArrayList<ActPraise>();
			}
		}catch (Exception e) {
			e.printStackTrace();
			praiseList = new ArrayList<ActPraise>();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return praiseList;
	}

}
