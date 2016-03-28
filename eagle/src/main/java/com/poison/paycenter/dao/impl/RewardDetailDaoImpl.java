package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.constant.ReturnMapUtil;
import com.poison.paycenter.dao.RewardDetailDao;
import com.poison.paycenter.model.RewardDetail;
import com.poison.paycenter.model.RewardStatistical;

/**
 * 打赏业务明细表操作层
 * 
 * @author yan_dz
 * 
 */
@SuppressWarnings("deprecation")
public class RewardDetailDaoImpl extends SqlMapClientDaoSupport implements RewardDetailDao {
	private static final Log LOG = LogFactory.getLog(RewardDetailDaoImpl.class);

	@Override
	public int insertRewardDetail(RewardDetail rewardDetail) throws Exception {
		LOG.info(rewardDetail);
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().insert("insertIntoRewardDetail", rewardDetail);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> findListRewardDetail(long userId) {
		List<RewardDetail> list = new ArrayList<RewardDetail>();
		try {
			list = getSqlMapClientTemplate().queryForList("");
			if (null == list || list.size() == 0) {
				list = new ArrayList<RewardDetail>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			list = new ArrayList<RewardDetail>();
			RewardDetail rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			list.add(rewardDetail);
		}
		return list;
	}

	@Override
	public int updateRewardDetail(RewardDetail rewardDetail) throws Exception {
		LOG.info(rewardDetail);
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().update("updateRewardDetailByID", rewardDetail);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public RewardDetail selectRewardDetailInfoByUserId(long id, String outTradeNo) throws Exception {
		RewardDetail rewardDetail = new RewardDetail();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sendUserId", id);
			map.put("outTradeNo", outTradeNo);
			rewardDetail = (RewardDetail) getSqlMapClientTemplate().queryForObject("findRewardDetailByIdAndNo", map);
			if (null == rewardDetail) {
				rewardDetail = new RewardDetail();
				rewardDetail.setFlag(ResultUtils.DATAISNULL);
				return rewardDetail;
			}
			rewardDetail.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			throw e;
		}
		return rewardDetail;
	}

	@Override
	public Map<String, Object> selectOtherPersonRewardCount(long id, int tradeStatus) {
		// int i = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiveUserId", id);
		map.put("sendStatus", tradeStatus);
		int count;
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("findOtherPersonRewardCount", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			reMap.put("flag", ResultUtils.QUERY_ERROR);
			return reMap;
		}
		reMap.put("flag", ResultUtils.SUCCESS);
		reMap.put("count", count);
		return reMap;
	}

	@Override
	public Map<String, Object> selectRewardOtherPersonCount(long id, int tradeStatus) {
		// int i = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendUserId", id);
		map.put("sendStatus", tradeStatus);
		int count;
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("findRewardOtherPersonCount", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			reMap.put("flag", ResultUtils.QUERY_ERROR);
			return reMap;
		}
		reMap.put("flag", ResultUtils.SUCCESS);
		reMap.put("count", count);
		return reMap;
	}

	@Override
	public Map<String, Object> selectOtherPersonDayRewardCount(long id, int tradeStatus, long startTime, long endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiveUserId", id);
		map.put("sendStatus", tradeStatus);
		map.put("date1", startTime);
		map.put("date2", endTime);
		int count;
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("findOtherPersonDayRewardCount", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			reMap.put("flag", ResultUtils.QUERY_ERROR);
			return reMap;
		}
		reMap.put("flag", ResultUtils.SUCCESS);
		reMap.put("dayCount", count);
		return reMap;
	}

	@Override
	public Map<String, Object> selectRewardOtherPersonDayCount(long id, int tradeStatus, long startTime, long endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendUserId", id);
		map.put("sendStatus", tradeStatus);
		map.put("date1", startTime);
		map.put("date2", endTime);
		int count;
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("findRewardOtherPersonDayCount", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			reMap.put("flag", ResultUtils.QUERY_ERROR);
			return reMap;
		}
		reMap.put("flag", ResultUtils.SUCCESS);
		reMap.put("dayCount", count);
		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> selectRewardOtherPersonDetail(long id, int sendStatus) throws Exception {
		List<RewardDetail> rewardList = new ArrayList<RewardDetail>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sendUserId", id);
		map.put("sendStatus", sendStatus);
		try {
			rewardList = getSqlMapClientTemplate().queryForList("findRewardOtherPersonDetail", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			throw e;
		}
		return rewardList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> selectOtherPersonRewardDetail(long id, int sendStatus) throws Exception {
		List<RewardDetail> rewardList = new ArrayList<RewardDetail>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiveUserId", id);
		map.put("sendStatus", sendStatus);
		LOG.info(map);
		try {
			rewardList = getSqlMapClientTemplate().queryForList("findOtherPersonRewardDetail", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			throw e;
		}
		return rewardList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> selectRewardTypeDetail(long id, int type, int sendStatus) throws Exception {
		List<RewardDetail> rewardList = new ArrayList<RewardDetail>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceId", id);
		map.put("sendStatus", sendStatus);
		map.put("sourceType", type);
		LOG.info(map);
		try {
			rewardList = getSqlMapClientTemplate().queryForList("findRewardTypeDetail", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			throw e;
		}
		return rewardList;
	}

	/**
	 * 获取资源的打赏总数
	 * 
	 * @author zhangqi
	 */
	@Override
	public Map<String, Object> selectRewardTypeCount(long sourceId, int sendStatus, int sourceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceId", sourceId);
		map.put("sourceType", sourceType);
		map.put("sendStatus", sendStatus);
		LOG.info(map);
		int count;
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("findRewardTypeCount", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			reMap.put("flag", ResultUtils.QUERY_ERROR);
			return reMap;
		}
		reMap.put("flag", ResultUtils.SUCCESS);
		reMap.put("dayCount", count);
		return reMap;
	}

	public Map<String, Object> selectRewardTypeCountBack(long id, int sendStatus, int rewardType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rewardTypeId", id);
		map.put("rewardType", rewardType);
		map.put("sendStatus", sendStatus);
		LOG.info(map);
		int count;
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("findRewardTypeCount", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			reMap.put("flag", ResultUtils.QUERY_ERROR);
			return reMap;
		}
		reMap.put("flag", ResultUtils.SUCCESS);
		reMap.put("dayCount", count);
		return reMap;
	}

	@Override
	public Map<String, Object> selectRewardTypeDayCount(long id, int sendStatus, long startTime, long endTime, int rewardType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rewardTypeId", id);
		map.put("rewardType", rewardType);
		map.put("sendStatus", sendStatus);
		map.put("date1", startTime);
		map.put("date2", endTime);
		LOG.info(map);
		int count;
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("findRewardTypeDayCount", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			reMap.put("flag", ResultUtils.QUERY_ERROR);
			return reMap;
		}
		reMap.put("flag", ResultUtils.SUCCESS);
		reMap.put("dayCount", count);
		return reMap;
	}

	@Override
	public RewardDetail selectRewardById(long userId, long id, int sendStatus) {
		RewardDetail rewardDetail = new RewardDetail();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("id", id);
			map.put("sendStatus", sendStatus);
			LOG.info(map);
			rewardDetail = (RewardDetail) getSqlMapClientTemplate().queryForObject("findRewardDetailById", map);
			if (null == rewardDetail) {
				rewardDetail = new RewardDetail();
				rewardDetail.setFlag(ResultUtils.DATAISNULL);
				return rewardDetail;
			}
			rewardDetail.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			// rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
		}
		return rewardDetail;
	}

	@Override
	public Map<String, Object> insertRewardDetailNew(RewardDetail rewardDetail) throws Exception {
		try {
			long id = (Long) getSqlMapClientTemplate().insert("insertIntoRewardDetailNew", rewardDetail);
			Map<String, Object> reMap = ReturnMapUtil.getSuccessMap();
			reMap.put("id", id);
			return reMap;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.poison.paycenter.dao.RewardDetailDao#selectDayRewardList(java.util
	 * .Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> selectDayRewardList(Map<String, Object> inMap) {
		List<RewardDetail> list = new ArrayList<RewardDetail>();
		try {
			list = getSqlMapClientTemplate().queryForList("getDayRewardDetailList", inMap);
			if (null == list || list.size() == 0) {
				list = new ArrayList<RewardDetail>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			list = new ArrayList<RewardDetail>();
			RewardDetail rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			list.add(rewardDetail);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.poison.paycenter.dao.RewardDetailDao#selectRewardList(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> selectRewardList(Map<String, Object> inMap) {
		List<RewardDetail> list = new ArrayList<RewardDetail>();
		try {
			list = getSqlMapClientTemplate().queryForList("getRewardDetailList", inMap);
			if (null == list || list.size() == 0) {
				list = new ArrayList<RewardDetail>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			list = new ArrayList<RewardDetail>();
			RewardDetail rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			list.add(rewardDetail);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> selectRewardListByUserId(Map<String, Object> inMap) {
		LOG.info(inMap);
		List<RewardDetail> list = new ArrayList<RewardDetail>();
		try {
			list = getSqlMapClientTemplate().queryForList("findRewardOtherPersonDetail", inMap);
			if (null == list || list.size() == 0) {
				list = new ArrayList<RewardDetail>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			list = new ArrayList<RewardDetail>();
			RewardDetail rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			list.add(rewardDetail);
		}
		return list;
	}

	@Override
	public List<RewardDetail> selectDayRewardListByUserId(Map<String, Object> inMap) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardDetail> selectRewardListByRecvUserId(Map<String, Object> inMap) {
		List<RewardDetail> list = new ArrayList<RewardDetail>();
		try {
			list = getSqlMapClientTemplate().queryForList("findOtherPersonRewardDetail", inMap);
			if (null == list || list.size() == 0) {
				list = new ArrayList<RewardDetail>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			list = new ArrayList<RewardDetail>();
			RewardDetail rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			list.add(rewardDetail);
		}
		return list;
	}

	@Override
	public RewardStatistical selectCountByRecvUserId(Map<String, Object> inMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RewardDetail selectRewardDetailInfoByUserId(String outTradeNo) throws Exception {
		RewardDetail rewardDetail = new RewardDetail();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("outTradeNo", outTradeNo);
			rewardDetail = (RewardDetail) getSqlMapClientTemplate().queryForObject("findRewardDetailByTradeNo", map);
			if (null == rewardDetail) {
				rewardDetail = new RewardDetail();
				rewardDetail.setFlag(ResultUtils.DATAISNULL);
				return rewardDetail;
			}
			rewardDetail.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			rewardDetail = new RewardDetail();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			throw e;
		}
		return rewardDetail;
	}
}
