package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.RewardStatisticalDao;
import com.poison.paycenter.model.RewardPesonStatistical;
import com.poison.paycenter.model.RewardStatistical;

@SuppressWarnings("deprecation")
public class RewardStatisticalDaoImpl extends SqlMapClientDaoSupport implements
		RewardStatisticalDao {
	private static final Log LOG = LogFactory.getLog(RewardStatisticalDaoImpl.class);
	@Override
	public int updateRewardStatistical(Map<String, Object> input)
			throws Exception {
		LOG.info("RewardStatisticalDaoImpl.updateRewardStatistical:"+input);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateRewardStatistical",input);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	/*public int insertRewardStatistical(Map<String, Object> input)
			throws Exception {
		LOG.info("RewardStatisticalDaoImpl.insertRewardStatistical:"+input);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertRewardStatistical",input);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}*/
	public int insertRewardStatistical(RewardStatistical rewardStatistical)
			throws Exception {
		LOG.info("RewardStatisticalDaoImpl.insertRewardStatistical:"+rewardStatistical);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertRewardStatistical",rewardStatistical);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		return flag;
	}

//	@Override
//	public RewardStatistical selectMoneyByUserid(Map<String, Object> inMap) {
//		RewardStatistical rewardStatistical = null;
//		try {
//			rewardStatistical = (RewardStatistical)getSqlMapClientTemplate().queryForObject("selectRewardStatisticalByUserId", inMap);
//			if (rewardStatistical == null) {
//				rewardStatistical = new RewardStatistical();
//				rewardStatistical.setFlag(ResultUtils.DATAISNULL);
//			}else {
//				rewardStatistical.setFlag(ResultUtils.SUCCESS);
//			}			
//		} catch (Exception e) {
//			rewardStatistical = new RewardStatistical();
//			rewardStatistical.setFlag(ResultUtils.QUERY_ERROR);
//		}
//		return rewardStatistical;
//	}

	@Override
	public RewardStatistical selectMoneyBySourceId(Map<String, Object> inMap) {
		LOG.info("RewardStatisticalDaoImpl.selectMoneyBySourceId:"+inMap);
		RewardStatistical rewardStatistical = null;
		try {
			rewardStatistical = (RewardStatistical)getSqlMapClientTemplate().queryForObject("selectRewardStatisticalBySourceId", inMap);
			if (rewardStatistical == null) {
				rewardStatistical = new RewardStatistical();
				rewardStatistical.setFlag(ResultUtils.DATAISNULL);
			}else {
				rewardStatistical.setFlag(ResultUtils.SUCCESS);
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			rewardStatistical = new RewardStatistical();
			rewardStatistical.setFlag(ResultUtils.QUERY_ERROR);
		}
		return rewardStatistical;
	}

	@Override
	public RewardStatistical selectCountBySourceId(long sourceId) {
		LOG.info("RewardStatisticalDaoImpl.selectMoneyBySourceId:"+sourceId);
		RewardStatistical rewardStatistical = null;
		try {
			rewardStatistical = (RewardStatistical)getSqlMapClientTemplate().queryForObject("selectRewardStatisticalBySourceId", sourceId);
			if (rewardStatistical == null) {
				rewardStatistical = new RewardStatistical();
				rewardStatistical.setFlag(ResultUtils.DATAISNULL);
			}else {
				rewardStatistical.setFlag(ResultUtils.SUCCESS);
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			rewardStatistical = new RewardStatistical();
			rewardStatistical.setFlag(ResultUtils.QUERY_ERROR);
		}
		return rewardStatistical;
	}

	@Override
	public List<RewardStatistical> selectRewardStatisticalListBySourceIdDesc(long start) {
		LOG.info("RewardStatisticalDaoImpl.selectRewardStatisticalListBySourceIdDesc:");
		List<RewardStatistical> sourceList = new ArrayList<RewardStatistical>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start", start);
			sourceList = getSqlMapClientTemplate().queryForList("findListBySourceIdDesc",map);
			if (sourceList.size() <= 0 || null == sourceList) {
				sourceList = new ArrayList<RewardStatistical>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			sourceList = new ArrayList<RewardStatistical>();
			RewardStatistical rewardStatistical = new RewardStatistical();
			rewardStatistical.setFlag(ResultUtils.QUERY_ERROR);
			sourceList.add(rewardStatistical);
		}
		return sourceList;
	}
}
