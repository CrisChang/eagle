package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.impl.UserTagDAOImpl;
import com.poison.store.dao.BigSelectingDAO;
import com.poison.store.model.BigSelecting;

public class BigSelectingDAOImpl extends SqlMapClientDaoSupport implements BigSelectingDAO{

	private static final  Log LOG = LogFactory.getLog(BigSelectingDAOImpl.class);
	/**
	 * 查询选择题库
	 */
	@Override
	public List<BigSelecting> findAllBigSelecting() {
		List<BigSelecting> list = new ArrayList<BigSelecting>();
		try{
			list = getSqlMapClientTemplate().queryForList("findAllBigSelecting");
			if(null==list||list.size()==0){
				list = new ArrayList<BigSelecting>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<BigSelecting>();
			BigSelecting bigSelecting = new BigSelecting();
			bigSelecting.setFlag(ResultUtils.QUERY_ERROR);
			list.add(bigSelecting);
		}
		return list;
	}

	/**
	 * 根据ID查询一个题
	 */
	@Override
	public BigSelecting findBigSelectingById(long id) {
		BigSelecting bigSelecting = new BigSelecting();
		try{
			bigSelecting = (BigSelecting) getSqlMapClientTemplate().queryForObject("findBigSelectingById",id);
			if(null==bigSelecting){
				bigSelecting = new BigSelecting();
				bigSelecting.setFlag(ResultUtils.DATAISNULL);
				return bigSelecting;
			}
			bigSelecting.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bigSelecting = new BigSelecting();
			bigSelecting.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bigSelecting;
	}

}
