package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BkAvgMarkDAO;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.MvAvgMark;

public class BkAvgMarkDAOImpl extends SqlMapClientDaoSupport implements BkAvgMarkDAO{

	private static final  Log LOG = LogFactory.getLog(BkAvgMarkDAOImpl.class);
	/**
	 * 插入一本书的平均分
	 */
	@Override
	public int insertBkAvgMark(BkAvgMark bkAvgMark) {
		int flag = ResultUtils.INSERT_ERROR;
		try{
			getSqlMapClientTemplate().insert("insertBkAvgMark",bkAvgMark);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 根据书的ID查询平均评分
	 */
	@Override
	public BkAvgMark findBkAvgMarkByBkId(int bkId) {
		BkAvgMark bkAvgMark = new BkAvgMark();
		try{
			bkAvgMark = (BkAvgMark) getSqlMapClientTemplate().queryForObject("findBkAvgMarkByBkId",bkId);
			if(null == bkAvgMark){
				 bkAvgMark = new BkAvgMark();
				bkAvgMark.setFlag(ResultUtils.DATAISNULL);
				return bkAvgMark;
			}
			bkAvgMark.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkAvgMark = new BkAvgMark();
			bkAvgMark.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bkAvgMark;
	}
	/**
	 * 根据书的id集合查询和书的类型查询评分信息
	 */
	@Override
	public List<BkAvgMark> findBkAvgMarkByBkIds(List<Long> bkids,String type) {
		List<BkAvgMark> bkAvgMarks = null;
		if(bkids==null || bkids.size()==0){
			return null;
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("bkids", bkids);
		map.put("resType", type);
		try{
			bkAvgMarks = getSqlMapClientTemplate().queryForList("findBkAvgMarkByBkIds",map);
			
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkAvgMarks = new ArrayList<BkAvgMark>();
			BkAvgMark bkAvgMark = new BkAvgMark();
			bkAvgMark.setFlag(ResultUtils.QUERY_ERROR);
			bkAvgMarks.add(bkAvgMark);
		}
		return bkAvgMarks;
	}
	/**
	 * 更新一个评分
	 */
	@Override
	public int updateBkAvgMark(BkAvgMark bkAvgMark) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateBkAvgMark",bkAvgMark);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 更新神人的书评评分
	 */
	@Override
	public int updateExpertsAvgMark(BkAvgMark bkAvgMark) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateBkExpertsAvgMark",bkAvgMark);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	
}
