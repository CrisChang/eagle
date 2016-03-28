package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.BkSearchDAO;
import com.poison.store.model.BkSearch;

public class BkSearchDAOImpl extends SqlMapClientDaoSupport implements BkSearchDAO{

	private static final  Log LOG = LogFactory.getLog(BkSearchDAOImpl.class);
	
	/**
	 * 查询图书的热搜榜
	 */
	@Override
	public List<BkSearch> findBkRanking(String starttime,String endtime,long start,int pagesize) {
		List<BkSearch> bkSearchs = new ArrayList<BkSearch>();
		BkSearch bkSearch = new BkSearch();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("start", start);
		map.put("pagesize", pagesize);
		try{
			bkSearchs = getSqlMapClientTemplate().queryForList("findBkRanking",map);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkSearchs = new ArrayList<BkSearch>();
			bkSearch.setFlag(ResultUtils.QUERY_ERROR);
			bkSearchs.add(bkSearch);
		}
		return bkSearchs;
	}
	
}
