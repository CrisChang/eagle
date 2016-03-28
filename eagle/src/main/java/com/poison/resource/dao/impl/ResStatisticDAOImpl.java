package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ResStatisticDAO;
import com.poison.resource.model.ResStatistic;

import javax.xml.transform.Result;

public class ResStatisticDAOImpl extends SqlMapClientDaoSupport implements ResStatisticDAO {

	private static final Log LOG = LogFactory.getLog(ResStatisticDAOImpl.class);

	/**
	 * 插入资源统计
	 */
	@Override
	public int insertResStatistic(ResStatistic resStatistic) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().insert("insertResStatistic", resStatistic);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据id查询统计信息
	 */
	@Override
	public ResStatistic findResStatisticById(ResStatistic resStatistic) {
		ResStatistic resourceStatistic = new ResStatistic();
		try {
			if (null == resStatistic){
				resourceStatistic.setFlag(ResultUtils.ERROR);
				return resourceStatistic;
			}
			resourceStatistic = (ResStatistic) getSqlMapClientTemplate().queryForObject("findResStatisticById", resStatistic);
			if (null == resourceStatistic) {
				resourceStatistic = new ResStatistic();
				resourceStatistic.setFlag(ResultUtils.DATAISNULL);
				return resourceStatistic;
			}
			resourceStatistic.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			LOG.error("resStatistic po=>" + resStatistic);
			resourceStatistic = new ResStatistic();
			resourceStatistic.setFlag(ResultUtils.ERROR);
		}
		return resourceStatistic;
	}

	/**
	 * 更新资源统计信息
	 */
	@Override
	public int updateResStatistic(ResStatistic resStatistic) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().update("updateResStatistic", resStatistic);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据点赞来获取排行榜
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByPraise(long resLinkId, String resLinkType, int pageIndex, int pageSize) {
		List<ResStatistic> resStatisticList = new ArrayList<ResStatistic>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("resLinkId", resLinkId);
			map.put("resLinkType", resLinkType);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			resStatisticList = getSqlMapClientTemplate().queryForList("findResStatisticRankByPraise", map);
			if (null == resStatisticList) {
				resStatisticList = new ArrayList<ResStatistic>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			resStatisticList = new ArrayList<ResStatistic>();
		}
		return resStatisticList;
	}

	/**
	 * 查询有用列表
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByUseful(long resLinkId, String resLinkType, long stageid, int pageIndex, int pageSize) {
		List<ResStatistic> resStatisticList = new ArrayList<ResStatistic>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("resLinkId", resLinkId);
			map.put("resLinkType", resLinkType);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("activityStageId", stageid);
			resStatisticList = getSqlMapClientTemplate().queryForList("findResStatisticRankByUseful", map);
			if (null == resStatisticList) {
				resStatisticList = new ArrayList<ResStatistic>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			resStatisticList = new ArrayList<ResStatistic>();
		}
		return resStatisticList;
	}

	/**
	 * 查询长影评长书评
	 * @param type
	 * @param resLinkId
	 * @param resLinkType
	 * @param stageid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByUsefulAndType(String type, long resLinkId, String resLinkType, long stageid, int pageIndex, int pageSize) {
		List<ResStatistic> resStatisticList = new ArrayList<ResStatistic>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("type", type);
			map.put("resLinkId", resLinkId);
			map.put("resLinkType", resLinkType);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("activityStageId", stageid);
			resStatisticList = getSqlMapClientTemplate().queryForList("findResStatisticRankByUsefulAndType", map);
			if (null == resStatisticList) {
				resStatisticList = new ArrayList<ResStatistic>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			resStatisticList = new ArrayList<ResStatistic>();
		}
		return resStatisticList;
	}

	/**
	 * 根据投票数查询排行
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByVoteNum(long resLinkId, String resLinkType, long stageid, int pageIndex, int pageSize) {
		List<ResStatistic> resStatisticList = new ArrayList<ResStatistic>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("resLinkId", resLinkId);
			map.put("resLinkType", resLinkType);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("activityStageId", stageid);
			resStatisticList = getSqlMapClientTemplate().queryForList("findResStatisticRankByVoteNum", map);
			if (null == resStatisticList) {
				resStatisticList = new ArrayList<ResStatistic>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			resStatisticList = new ArrayList<ResStatistic>();
		}
		return resStatisticList;
	}

	/**
	 * 根据阅读数查询排行（某个类型资源的）
	 */
	@Override
	public List<ResStatistic> findResStatisticRankByReadNum(String type, String secondtype, long pageIndex, int pageSize) {
		List<ResStatistic> resStatisticList = new ArrayList<ResStatistic>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("type", type);
			map.put("secondtype", secondtype);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			resStatisticList = getSqlMapClientTemplate().queryForList("findResStatisticRankByReadNum", map);
			if (null == resStatisticList) {
				resStatisticList = new ArrayList<ResStatistic>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			resStatisticList = new ArrayList<ResStatistic>();
		}
		return resStatisticList;
	}
	/**
	 * 根据阅读数查询排行(不区分资源，所有资源的排行)
	 */
	@Override
	public List<ResStatistic> findAllResStatisticRankByReadNum(long pageIndex, int pageSize) {
		List<ResStatistic> resStatisticList = new ArrayList<ResStatistic>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			resStatisticList = getSqlMapClientTemplate().queryForList("findAllResStatisticRankByReadNum", map);
			if (null == resStatisticList) {
				resStatisticList = new ArrayList<ResStatistic>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			resStatisticList = new ArrayList<ResStatistic>();
		}
		return resStatisticList;
	}

	/**
	 * 查询
	 * @param resLinkId
	 * @param type
	 * @return
	 */
	@Override
	public Map<String, Object> findResStatisticCountByLinkIdAndType(long resLinkId, String type) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("resLinkId", resLinkId);
			map.put("type", type);
			count = (Integer)getSqlMapClientTemplate().queryForObject("findResStatisticCountByLinkIdAndType", map);
			resultMap.put("count",count);
			resultMap.put("flag",flag);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			resultMap.put("flag", ResultUtils.ERROR);
		}
		return resultMap;
	}
}
