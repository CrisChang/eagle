package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ResourceRankingDAO;
import com.poison.resource.model.ResourceRanking;

public class ResourceRankingDAOImpl extends SqlMapClientDaoSupport implements ResourceRankingDAO{

	private static final  Log LOG = LogFactory.getLog(ResourceRankingDAOImpl.class);
	/**
	 * 根据评论数查询资源排行
	 */
	@Override
	public List<ResourceRanking> findResRanking(long starttime, long endtime, long start) {
		List<ResourceRanking> list = new ArrayList<ResourceRanking>();
		ResourceRanking resourceRanking = new ResourceRanking();
		try{
			Map<Object,Object> map=new HashMap<Object, Object>();
			map.put("starttime", starttime);
			map.put("endtime", endtime);
			map.put("start", start);
			list = getSqlMapClientTemplate().queryForList("findResRanking",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ResourceRanking>();
			resourceRanking.setFlag(ResultUtils.QUERY_ERROR);
			list.add(resourceRanking);
		}
		return list;
	}
}