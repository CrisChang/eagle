package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.RankingListDAO;
import com.poison.resource.model.RankingList;

@SuppressWarnings("deprecation")
public class RankingListDAOImpl extends SqlMapClientDaoSupport implements RankingListDAO {

	private static final Log LOG = LogFactory.getLog(RankingListDAOImpl.class);

	@Override
	public List<RankingList> findRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		List<RankingList> rankingList = new ArrayList<RankingList>();
		RankingList selected = new RankingList();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("score", score);
		map.put("pageSize", pageSize);
		map.put("pageStart", pageStart);
		try {
			rankingList = getSqlMapClientTemplate().queryForList("findRankingListByScore", map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			rankingList = new ArrayList<RankingList>();
			selected = new RankingList();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			rankingList.add(selected);
		}
		return rankingList;
	}

	public long countRankingListByScore(Long score, Integer pageSize, Integer pageStart) {
		long i = 0;
		try {
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("score", score);
			map.put("pageSize", pageSize);
			map.put("pageStart", pageStart);
			Long total = (Long) getSqlMapClientTemplate().queryForObject("countRankingListByScore", map);
			if (total != null) {
				i = total;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			i = -1;
		}
		return i;
	}

	@Override
	public List<RankingList> findRankingListByScoreWithoutTopshow(Long score, Integer pageSize, Integer pageStart, Long starttime, Long endtime) {
		List<RankingList> selecteds = new ArrayList<RankingList>();
		RankingList selected = new RankingList();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("score", score);
		map.put("pageStart", pageStart);
		map.put("pageSize", pageSize);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			selecteds = getSqlMapClientTemplate().queryForList("findRankingListByScoreWithoutTopshow", map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			selecteds = new ArrayList<RankingList>();
			selected = new RankingList();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}

	@Override
	public List<RankingList> findRankingListByTopshow(String restype, String type, Integer pageStart, Integer pageSize) {
		List<RankingList> selecteds = new ArrayList<RankingList>();
		RankingList selected = new RankingList();
		try {
			Map<Object, Object> input = new HashMap<Object, Object>();
			input.put("restype", restype);
			input.put("type", type);
			input.put("pageStart", pageStart);
			input.put("pageSize", pageSize);
			selecteds = getSqlMapClientTemplate().queryForList("findRankingListByTopshow", input);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			selecteds = new ArrayList<RankingList>();
			selected = new RankingList();
			selected.setFlag(ResultUtils.QUERY_ERROR);
			selecteds.add(selected);
		}
		return selecteds;
	}

	@Override
	public long countRankingList(String restype, String type) {
		long i = 0;
		try {
			Map<Object, Object> input = new HashMap<Object, Object>();
			input.put("restype", restype);
			input.put("type", type);
			Long total = (Long) getSqlMapClientTemplate().queryForObject("countRankingList", input);
			if (total != null) {
				i = total;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			i = -1;
		}
		return i;
	}

	@Override
	public RankingList findRankingListByResidAndType(long resid, String restype, String type) {
		RankingList selected = null;
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("resid", resid);
		map.put("restype", restype);
		map.put("type", type);
		try {
			selected = (RankingList) getSqlMapClientTemplate().queryForObject("findRankingListByResidAndType", map);
			if (selected != null) {
				selected.setFlag(ResultUtils.SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			selected = new RankingList();
			selected.setFlag(ResultUtils.QUERY_ERROR);
		}
		return selected;
	}

}
