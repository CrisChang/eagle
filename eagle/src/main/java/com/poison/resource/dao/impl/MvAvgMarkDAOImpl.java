package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MvAvgMarkDAO;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.MvAvgMark;

public class MvAvgMarkDAOImpl extends SqlMapClientDaoSupport implements MvAvgMarkDAO{

	private static final  Log LOG = LogFactory.getLog(MvAvgMarkDAOImpl.class);
	/**
	 * 增加一个电影评分
	 */
	@Override
	public int insertMvAvgMark(MvAvgMark mvAvgMark) {
		int flag = ResultUtils.INSERT_ERROR;
		try{
			getSqlMapClientTemplate().insert("insertMvAvgMark",mvAvgMark);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 查找一个电影评分
	 */
	@Override
	public MvAvgMark findMvAvgMarkByMvId(long mvId) {
		MvAvgMark mvAvgMark = new MvAvgMark();
		try{
			mvAvgMark = (MvAvgMark) getSqlMapClientTemplate().queryForObject("findMvAvgMarkByMvId",mvId);
			if(null == mvAvgMark){
				 mvAvgMark = new MvAvgMark();
				mvAvgMark.setFlag(ResultUtils.DATAISNULL);
				return mvAvgMark;
			}
			mvAvgMark.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvAvgMark = new MvAvgMark();
			mvAvgMark.setFlag(ResultUtils.QUERY_ERROR);
		}
		return mvAvgMark;
	}
	
	/**
	 * 根据电影的id集合查询评分信息
	 */
	@Override
	public List<MvAvgMark> findMvAvgMarkByMvIds(List<Long> mvids) {
		List<MvAvgMark> mvAvgMarks = null;
		if(mvids==null || mvids.size()==0){
			return null;
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("mvids", mvids);
		try{
			mvAvgMarks = getSqlMapClientTemplate().queryForList("findMvAvgMarkByMvIds",map);
			
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvAvgMarks = new ArrayList<MvAvgMark>();
			MvAvgMark mvAvgMark = new MvAvgMark();
			mvAvgMark.setFlag(ResultUtils.QUERY_ERROR);
			mvAvgMarks.add(mvAvgMark);
		}
		return mvAvgMarks;
	}

	/**
	 * 更新一个电影
	 */
	@Override
	public int updateMvAvgMark(MvAvgMark mvAvgMark) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateMvAvgMark",mvAvgMark);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 更新专家对这本书的评分
	 */
	@Override
	public int updateExpertsAvgMark(MvAvgMark mvAvgMark) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateExpertsAvgMark",mvAvgMark);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

}
