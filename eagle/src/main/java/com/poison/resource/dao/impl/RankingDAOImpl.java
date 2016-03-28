package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.RankingDAO;
import com.poison.resource.model.Ranking;

public class RankingDAOImpl extends SqlMapClientDaoSupport implements RankingDAO{

	private static final  Log LOG = LogFactory.getLog(RankingDAOImpl.class);
	/**
	 * 查询图书、影视的排行
	 * @Title: findRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午4:10:47
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<ResourceRanking>
	 * @throws
	 */
	@Override
	public List<Ranking> findRanking(long start,int pagesize,String ranktype,String restype) {
		List<Ranking> list = null;
		Ranking ranking = new Ranking();
		try{
			Map<Object,Object> map=new HashMap<Object, Object>();
			map.put("ranktype", ranktype);
			map.put("restype", restype);
			map.put("pagesize", pagesize);
			map.put("start", start);
			list = getSqlMapClientTemplate().queryForList("findRanking",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<Ranking>();
			ranking.setFlag(ResultUtils.QUERY_ERROR);
			list.add(ranking);
		}
		return list;
	}
}