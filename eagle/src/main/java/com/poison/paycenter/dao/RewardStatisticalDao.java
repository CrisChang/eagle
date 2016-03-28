package com.poison.paycenter.dao;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.model.RewardStatistical;

public interface RewardStatisticalDao {

	/**
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public int updateRewardStatistical(Map<String, Object> input) throws Exception;

	/**
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public int insertRewardStatistical(RewardStatistical rewardStatistical) throws Exception;

	/**
	 * @param inMap
	 * @return
	 */
	//public RewardStatistical selectMoneyByUserid(Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	public RewardStatistical selectMoneyBySourceId(Map<String, Object> inMap);

	/**
	 * @param sourceId
	 * @return
	 */
	public RewardStatistical selectCountBySourceId(long sourceId);

	/**
	 * @return
	 */
	public List<RewardStatistical> selectRewardStatisticalListBySourceIdDesc(long start);

}
