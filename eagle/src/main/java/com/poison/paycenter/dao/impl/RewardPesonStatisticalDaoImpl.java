package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.RewardPesonStatisticalDao;
import com.poison.paycenter.model.RewardPesonStatistical;

@SuppressWarnings("deprecation")
public class RewardPesonStatisticalDaoImpl extends SqlMapClientDaoSupport
		implements RewardPesonStatisticalDao {
	private static final Log LOG = LogFactory.getLog(RewardPesonStatisticalDaoImpl.class);
	@Override
	/*public int insertInfo(Map<String, Object> input) {
		LOG.info(input);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertInfo",input);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}*/
	public int insertInfo(RewardPesonStatistical rewardPesonStatistical) throws Exception{
		LOG.info(rewardPesonStatistical);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertInfo",rewardPesonStatistical);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public RewardPesonStatistical selectInfo(Map<String, Object> inputMap) {
		LOG.info(inputMap);
		RewardPesonStatistical rewardPesonStatistical = null;
		try {
			rewardPesonStatistical = (RewardPesonStatistical)getSqlMapClientTemplate().queryForObject("findByUserId", inputMap);
			if (rewardPesonStatistical == null) {
				rewardPesonStatistical = new RewardPesonStatistical();
				rewardPesonStatistical.setFlag(ResultUtils.DATAISNULL);
			}else {
				rewardPesonStatistical.setFlag(ResultUtils.SUCCESS);
			}			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			rewardPesonStatistical = new RewardPesonStatistical();
			rewardPesonStatistical.setFlag(ResultUtils.QUERY_ERROR);
		}
		return rewardPesonStatistical;
	}
	
	@Override
	public int updatePayInfo(Map<String, Object> inputMap) throws Exception{
		LOG.info(inputMap);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updatePayInfo",inputMap);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public int updateCollInfo(Map<String, Object> inputMap) throws Exception{
		LOG.info(inputMap);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateCollInfo",inputMap);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardPesonStatistical> selectCollInfoListByUserIdDesc(long start) {
		List<RewardPesonStatistical> list = new ArrayList<RewardPesonStatistical>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start", start);
			list = getSqlMapClientTemplate().queryForList("findByTotalCollAmtDesc",map);
			if(null==list||list.size()==0){
				list = new ArrayList<RewardPesonStatistical>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<RewardPesonStatistical>();
			RewardPesonStatistical rewardDetail = new RewardPesonStatistical();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			list.add(rewardDetail);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RewardPesonStatistical> selectPayInfoListByUserIdDesc(long start) {
		List<RewardPesonStatistical> list = new ArrayList<RewardPesonStatistical>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start", start);
			list = getSqlMapClientTemplate().queryForList("findByTotalPayAmtDesc",map);
			if(null==list||list.size()==0){
				list = new ArrayList<RewardPesonStatistical>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<RewardPesonStatistical>();
			RewardPesonStatistical rewardDetail = new RewardPesonStatistical();
			rewardDetail.setFlag(ResultUtils.QUERY_ERROR);
			list.add(rewardDetail);
		}
		return list;
	}

	@Override
	/*public int updateInfo(Map<String, Object> inputMap) {
		LOG.info(inputMap);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateInfo",inputMap);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			flag = ResultUtils.ERROR;
		}
		return flag;
	}*/
	public int updateInfo(RewardPesonStatistical rewardPesonStatistical) throws Exception{
		LOG.info(rewardPesonStatistical);
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateInfo",rewardPesonStatistical);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}
}
