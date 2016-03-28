package com.poison.paycenter.dao;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.model.RewardDetail;
import com.poison.paycenter.model.RewardStatistical;

/**
 * @author yan_dz
 *
 */
public interface RewardDetailDao {
	
	/**
	 * @param rewardDetail
	 * @return
	 */
	public int insertRewardDetail(RewardDetail rewardDetail) throws Exception;
	
	/**
	 * @param userId
	 * @return
	 */
	public List<RewardDetail> findListRewardDetail (long userId);
	
	/**
	 * @param rewardDetail
	 * @return
	 */
	public int updateRewardDetail(RewardDetail rewardDetail) throws Exception;
	
	/**
	 * @param id
	 * @param name
	 * @return
	 */
	public RewardDetail selectRewardDetailInfoByUserId(long id, String outTradeNo) throws Exception;
	
	/**
	 * @param outTradeNo
	 * @return
	 * @throws Exception
	 */
	public RewardDetail selectRewardDetailInfoByUserId(String outTradeNo) throws Exception;
	
	/**
	 * 查询被打赏人被打赏次数
	 * @param id
	 * @param tradeStatus
	 * @return
	 */
	public Map<String, Object> selectOtherPersonRewardCount(long id, int sendStatus);
	
	/**
	 * 查询打赏人发起打赏并成功的次数
	 * @param id
	 * @param tradeStatus
	 * @return
	 */
	public Map<String, Object> selectRewardOtherPersonCount(long id, int sendStatus);
	
	/**
	 * 查询被打赏人当天被打赏次数
	 * @param id
	 * @param tradeStatus
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> selectOtherPersonDayRewardCount(long id, int sendStatus, long startTime, long endTime);
	
	/**
	 * 查询打赏种类被打赏成功次数
	 * @param sourceId
	 * @param sourceType 
	 * @param tradeStatus
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> selectRewardTypeCount(long sourceId, int sendStatus, int sourceType);
	
	/**
	 * 查询打赏种类当天被打赏成功次数
	 * @param id
	 * @param tradeStatus
	 * @param startTime
	 * @param endTime
	 * @param rewardType 
	 * @return
	 */
	public Map<String, Object> selectRewardTypeDayCount(long id, int sendStatus, long startTime, long endTime, int rewardType);
	
	/**
	 * 查询打赏人当天打赏成功次数
	 * @param id
	 * @param tradeStatus
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> selectRewardOtherPersonDayCount(long id, int sendStatus, long startTime, long endTime);
	
	/**
	 * 查询打赏人打赏业务明细
	 * @param id
	 * @param sendStatus
	 * @return
	 * @throws Exception 
	 */
	public List<RewardDetail> selectRewardOtherPersonDetail(long id, int sendStatus) throws Exception;
	
	/**
	 * 查询被打赏人业务明细
	 * @param id
	 * @param sendStatus
	 * @return
	 * @throws Exception 
	 */
	public List<RewardDetail> selectOtherPersonRewardDetail(long id, int sendStatus) throws Exception;
	
	/**
	 * 查询被打赏种类业务明细
	 * @param id
	 * @param type
	 * @param sendStatus
	 * @return
	 * @throws Exception 
	 */
	public List<RewardDetail> selectRewardTypeDetail(long id, int type, int sendStatus) throws Exception;

	/**
	 * 根据打赏业务ID查询打赏明细
	 * @param rewardId
	 * @param rEWARD_STATUS_3 
	 * @return
	 */
	public RewardDetail selectRewardById(long userId,long rewardId, int rEWARD_STATUS_3);

	/**
	 * @param rewardDetail
	 * @return
	 */
	public Map<String, Object> insertRewardDetailNew(RewardDetail rewardDetail) throws Exception;

	/**
	 * @param inMap
	 * @return
	 */
	public List<RewardDetail> selectDayRewardList(Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	public List<RewardDetail> selectRewardList(Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	public List<RewardDetail> selectRewardListByUserId(Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	public List<RewardDetail> selectDayRewardListByUserId(
			Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	public List<RewardDetail> selectRewardListByRecvUserId(
			Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	public RewardStatistical selectCountByRecvUserId(Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	//public Map<String, Object> getDayMoneyByUserId(Map<String, Object> inMap);
}
