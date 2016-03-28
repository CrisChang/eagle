package com.poison.store.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.MvStillsDAO;
import com.poison.store.model.MovieStills;

public class MvStillDAOImpl extends SqlMapClientDaoSupport implements MvStillsDAO{

	private static final  Log LOG = LogFactory.getLog(MvStillDAOImpl.class);
	/**
	 * 插入剧照
	 */
	@Override
	public int insertintoMvOlineStills(MovieStills movieStills) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoMvOlineStills",movieStills);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 根据影单id查询电影剧照
	 */
	@Override
	public MovieStills findMvOlineStillsByBkId(long mvId) {
		MovieStills mvStills = new MovieStills();
		try{
			mvStills = (MovieStills) getSqlMapClientTemplate().queryForObject("findMvOlineStillsByBkId",mvId);
			if(null == mvStills){
				mvStills = new MovieStills();
				mvStills.setFlag(ResultUtils.DATAISNULL);
				return mvStills;
			}
			mvStills.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvStills = new MovieStills();
			mvStills.setFlag(ResultUtils.QUERY_ERROR);
		}
		return mvStills;
	}

	/**
	 * 更新剧照
	 */
	@Override
	public int updateMvStills(MovieStills movieStills) {
		int flag  = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateMvStills",movieStills);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 更新片花
	 */
	@Override
	public int updateMvOther(MovieStills movieStills) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateMvOther",movieStills);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 更新电影的二维码
	 */
	@Override
	public int updateMvTwoDimensionCode(MovieStills movieStills) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateMvTwoDimensionCode",movieStills);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

}
