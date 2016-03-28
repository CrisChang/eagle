package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.MvSearchDAO;
import com.poison.store.model.MvSearch;

public class MvSearchDAOImpl extends SqlMapClientDaoSupport implements MvSearchDAO{

	private static final  Log LOG = LogFactory.getLog(MvSearchDAOImpl.class);
	
	/**
	 * 查询电影的热搜榜
	 */
	@Override
	public List<MvSearch> findMvRanking(String starttime,String endtime,long start,int pagesize) {
		List<MvSearch> mvSearchs = new ArrayList<MvSearch>();
		MvSearch mvSearch = new MvSearch();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("start", start);
		map.put("pagesize", pagesize);
		try{
			mvSearchs = getSqlMapClientTemplate().queryForList("findMvRanking",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvSearchs = new ArrayList<MvSearch>();
			mvSearch.setFlag(ResultUtils.QUERY_ERROR);
			mvSearchs.add(mvSearch);
		}
		return mvSearchs;
	}
}
