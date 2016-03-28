package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.PopChartsDAO;
import com.poison.store.model.PopCharts;

public class PopChartsDAOImpl extends SqlMapClientDaoSupport implements PopChartsDAO{

	private static final  Log LOG = LogFactory.getLog(PopChartsDAOImpl.class);
	/**
	 * 根据等级查询排行榜
	 */
	@Override
	public List<PopCharts> findPopChartsByLevel() {
		List<PopCharts> list = new ArrayList<PopCharts>();
		PopCharts popCharts = new PopCharts();
		try{
			list = getSqlMapClientTemplate().queryForList("findPopChartsByLevel");
			if(null==list||list.size()==0){
				list = new ArrayList<PopCharts>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			popCharts.setFlag(ResultUtils.QUERY_ERROR);
		}
		return list;
	}

	/**
	 * 根据type查询排行榜
	 */
	@Override
	public List<PopCharts> findPopChartsByType(String type) {
		List<PopCharts> list = new ArrayList<PopCharts>();
		PopCharts popCharts = new PopCharts();
		try{
			list = getSqlMapClientTemplate().queryForList("findPopChartsByType",type);
			if(null==list||list.size()==0){
				list = new ArrayList<PopCharts>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			popCharts.setFlag(ResultUtils.QUERY_ERROR);
		}
		return list;
	}

}
