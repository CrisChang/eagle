package com.poison.paycenter.service;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.model.AccAmt;
import com.poison.paycenter.model.AccSet;
import com.poison.paycenter.model.AccTakeRecord;
import com.poison.paycenter.model.AccTakeSet;
import com.poison.paycenter.model.LegendHero;
import com.poison.paycenter.model.PayLog;
import com.poison.paycenter.model.RewardDetail;
import com.poison.paycenter.model.RewardPesonStatistical;
import com.poison.paycenter.model.RewardStatistical;

/**
 * @author yan_dz
 * 
 */
public interface PaycenterService {

	/**
	 * @param paylog
	 * @return
	 */
	public Map<String, String> notifyResult(PayLog paylog);

	/**
	 * @param inMap
	 * @return
	 */
	public Map<String, Object> selectCountReward(Map<String, Object> inMap);

	/**
	 * @param inMap
	 * @return
	 */
	public Map<String, Object> selectRewardDetail(Map<String, Object> inMap);

	/**
	 * @param rewardDetail
	 * @return
	 */
	public Map<String, Object> createRollInfo(Map<String, Object> inMap);

	/**
	 * @param reqMap
	 * @return
	 */
	public Map<String, Object> backRollInfo(Map<String, Object> reqMap);

	/**
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createReloadOrderInfoByMap(
			Map<String, String> inMap);
	
	/**
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createWeixinReloadOrderInfoByMap(
			Map<String, String> inMap);
	/**
	 * 自动打赏
	 */
	public boolean autoReward(final Map<String, String> inMap);
	/**
	 * @param userId
	 * @return
	 */
	public RewardPesonStatistical getMoneyByUserId(long userId);

	/**
	 * @param sourceId
	 * @return
	 */
	public RewardStatistical getMoneyBySourceId(long sourceId);

	/**
	 * @param sourceId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoBySourceId(long sourceId,
			Long lastId);

	/**
	 * @param sourceId
	 * @return
	 */
	// public List<RewardDetail> getDayRewardListInfoBySourceId(long sourceId);

	/**
	 * @param userId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoByUserId(long userId, Long lastId);

	/**
	 * @param userId
	 * @return
	 */
	// public List<RewardDetail> getDayRewardListInfoByUserId(long userId);

	/**
	 * @param userId
	 * @return
	 */
	// public RewardStatistical getRewardCountByRecvUserID(long userId);

	/**
	 * @param recvUserId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoByRecvUserId(long recvUserId,
			Long lastId);

	/**
	 * @param userId
	 * @return
	 */
	public int getAccAmt(long userId);

	/**
	 * @param userId
	 * @return
	 */
	public List<RewardPesonStatistical> getCollListInfoByUserIdDesc(long startIndex);

	/**
	 * @param userId
	 * @param startIndex
	 * @return
	 */
	public List<RewardPesonStatistical> getPayListInfoByUserIdDesc(long startIndex);

	/**
	 * @param userId
	 * @return
	 */
	public int getDayMoneyByUserId(long userId);

	/**
	 * @param sourceId
	 * @return
	 */
	public int getRewardCountBySourceId(long sourceId);

	/**
	 * @param userId
	 * @return
	 */
	public int getRewardCountByUserId(long userId);

	/**
	 * @param userId
	 * @return
	 */
	public int getRewardCountByRecvUserId(long userId);

	/**
	 * @return
	 */
	public List<RewardStatistical> getRewardStatisticalListBySourceIdDesc(long start);

	// ===以下是提现相关方法

	/**
	 * @param set
	 * @return
	 */
	public int insertAccTakeSet(AccTakeSet set);

	/**
	 * @param set
	 * @return
	 */
	public int updateAccTakeSet(AccTakeSet set);

	/**
	 * @param uid
	 * @return
	 */
	public AccTakeSet selectAccTakeSetByUserId(long uid);

	/**
	 * @param accTakeRecord
	 * @return
	 */
	public int insertAccTakeRecord(AccTakeRecord accTakeRecord);

	/**
	 * @param uid
	 * @return
	 */
	public List<AccTakeRecord> findAccTakeRecords(long uid, long start,
			int limit);

	/**
	 * 
	 * @Title: findAccTakeRecords
	 * @Description: TODO
	 * @author weizhensong
	 * @date 2015-3-25 下午2:45:50
	 * @param @param uid
	 * @param @param tradeStatus
	 * @param @return
	 * @return List<AccTakeRecord>
	 * @throws
	 */
	public List<AccTakeRecord> findAccTakeRecords(long uid, int tradeStatus,
			long start, int limit);

	/**
	 * 根据提现订单号和用户id查询
	 * 
	 * @param uid
	 * @param takeno
	 * @return
	 */
	public AccTakeRecord selectAccTakeRecordInfoBytakeNo(long uid, String takeno);

	/**
	 * 查询一个时间段的某个用户的提现订单数量
	 */
	public int selectAccTakeRecordCount(long uid, long starttime, long endtime);

	/**
	 * 查询某个用户的提现总额
	 */
	public long getAccTakeTotal(long uid);

	/**
	 * 根据交易状态查询提现总额(等待、成功、失败)
	 */
	public long getAccTakeTotal(long uid, int tradeStatus);

	/**
	 * 查询账户信息
	 * 
	 * @Title: getAccAmtInfo
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-2 下午12:28:45
	 * @param @param userId
	 * @param @return
	 * @return AccAmt
	 * @throws
	 */
	public AccAmt getAccAmtInfo(long userId);

	/**
	 * 查询账户总设置信息
	 * 
	 * @return
	 */
	public AccSet getAccSet();

	/**
	 * 插入封神榜
	 * 
	 * @param hero
	 * @return
	 */
	public int insertIntoLegendHero(LegendHero hero);

	/**
	 * 查询所有封神榜
	 * 
	 * @param hero
	 * @return
	 */
	public List<LegendHero> findAllLegendHero();

	/**
	 * 查询封神榜byId
	 * 
	 * @param hero
	 * @return
	 */
	public LegendHero findLegendHeroById(long id);

	/**
	 * 查询封神榜byUserId
	 * 
	 * @param userId
	 * @return
	 */
	public LegendHero findLegendHeroByUserId(long userId);

	/**
	 * 更新封神榜byId
	 * 
	 * @param hero
	 * @return
	 */
	public int updateLegendHeroById(LegendHero hero);

	/**
	 * 更新封神榜byUserId
	 * 
	 * @param hero
	 * @return
	 */
	public int updateLegendHeroByUserId(LegendHero hero);

	/**
	 * 删队封神榜byId
	 * 
	 * @param hero
	 * @return
	 */
	public int deleteLegendHeroById(long id);

	/**
	 * 删除封神榜byUserId
	 * 
	 * @param hero
	 * @return
	 */
	public int deleteLegendHeroByUserId(long userId);

}
