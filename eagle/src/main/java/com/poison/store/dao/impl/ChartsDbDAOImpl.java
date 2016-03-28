package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.ChartsDbDAO;
import com.poison.store.model.ChartsDb;

public class ChartsDbDAOImpl extends SqlMapClientDaoSupport implements ChartsDbDAO{

	private static final  Log LOG = LogFactory.getLog(ChartsDbDAOImpl.class);
	/**
	 * 根据分组查询排行榜
	 */
	@Override
	public List<ChartsDb> findChartsByGroup(String chartGroup) {
		List<ChartsDb> list = new ArrayList<ChartsDb>();
		ChartsDb chartsDb = new ChartsDb();
		try{
			list = getSqlMapClientTemplate().queryForList("findChartsByGroup",chartGroup);
			if(null==list||list.size()==0){
				list = new ArrayList<ChartsDb>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ChartsDb>();
			chartsDb.setFlag(ResultUtils.QUERY_ERROR);
			list.add(chartsDb);
		}
		return list;
	}

}
