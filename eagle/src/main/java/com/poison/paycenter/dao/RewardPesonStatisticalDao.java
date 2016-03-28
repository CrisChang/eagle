package com.poison.paycenter.dao;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.model.RewardPesonStatistical;

public interface RewardPesonStatisticalDao {

	public int insertInfo(RewardPesonStatistical rewardPesonStatistical) throws Exception;

	public RewardPesonStatistical selectInfo(Map<String, Object> inputMap);

	public int updatePayInfo(Map<String, Object> inputMap) throws Exception;

	public int updateCollInfo(Map<String, Object> inputMap) throws Exception;

	public List<RewardPesonStatistical> selectCollInfoListByUserIdDesc(long start);

	public List<RewardPesonStatistical> selectPayInfoListByUserIdDesc(long start);

	public int updateInfo(RewardPesonStatistical rewardPesonStatistical) throws Exception;

}
