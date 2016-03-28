package com.poison.paycenter.client;

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
 * 
 * @author yan_dz
 * 
 */
public interface PaycenterFacade {

	/**
	 * 支付宝交易结果通知
	 * 
	 * @param paylog
	 * @return
	 */
	public Map<String, String> notifyResult(PayLog paylog);

	/**
	 * 
	 * @param inMap
	 * @return
	 */
	public Map<String, Object> selectCountReward(Map<String, Object> inMap);

	/**
	 * 查询打赏明细
	 * 
	 * @param inMap
	 * @return
	 */
	public Map<String, Object> selectRewardDetail(Map<String, Object> inMap);

	/**
	 * 生成摇一摇信息
	 * 
	 * @param inMap
	 * @return
	 */
	public Map<String, Object> createRollInfo(Map<String, Object> inMap);

	/**
	 * 摇一摇成功返回信息
	 * 
	 * @param reqMap
	 * @return
	 */
	public Map<String, Object> backRollInfo(Map<String, Object> reqMap);

	/**
	 * 生成充值记录并记录打赏业务
	 * 
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createReloadOrderInfoByMap(
			Map<String, String> inMap);
	
	/**
	 * 生成充值记录并记录打赏业务--微信支付
	 * 
	 * @param inMap
	 * @return
	 */
	public Map<String, String> createWeixinReloadOrderInfoByMap(
			Map<String, String> inMap);
	/**
	 * 自动打赏
	 */
	public boolean autoReward(String rewardUserId,String rewardUserName,String sourceId,String sourceName,String sourceType);
	/**
	 * 1查询用户被打赏总金额
	 * 
	 * @param userId
	 * @return
	 */
	public RewardPesonStatistical getMoneyByUserId(long userId);

	/**
	 * 2查询资源被打赏总金额
	 * 
	 * @param sourceId
	 * @return
	 */
	public RewardStatistical getMoneyBySourceId(long sourceId);

	/**
	 * 3查询资源打赏列表 入参：sourceId 返回参数List<RewardDetail> 参数列表
	 * 
	 * @param sourceId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoBySourceId(long sourceId,
			Long lastId);

	/**
	 * 查询资源被打赏总数
	 * 
	 * @param sourceId
	 * @return
	 */
	public int getRewardCountBySourceId(long sourceId);

	/**
	 * 查询当天资源打赏列表 入参：sourceId 返回参数List<RewardDetail> 参数列表
	 * 
	 * @param sourceId
	 * @return
	 */
	// public List<RewardDetail> getDayRewardListInfoBySourceId(long sourceId);

	/**
	 * 4查询用户打赏他人信息列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoByUserId(long userId, Long lastId);

	/**
	 * 用户打赏他人总数
	 * 
	 * @param userId
	 * @return
	 */
	public int getRewardCountByUserId(long userId);

	/**
	 * 5查询用户被他人打赏信息列表
	 * 
	 * @param recvUserId
	 * @return
	 */
	public List<RewardDetail> getRewardListInfoByRecvUserId(long recvUserId,
			Long lastId);

	/**
	 * 用户被打赏总数
	 * 
	 * @param userId
	 * @return
	 */
	public int getRewardCountByRecvUserId(long userId);

	/**
	 * 查询用户当天打赏信息列表
	 * 
	 * @param userId
	 * @return
	 */
	// public List<RewardDetail> getDayRewardListInfoByUserId(long userId);

	/**
	 * 6查询账户当前总金额，现在即账户中的打赏金额（取完赏金会减少）
	 * 
	 * @param userId
	 * @return
	 */
	public int getAccAmt(long userId);

	/**
	 * 7被打赏人按照金额排序
	 * 
	 * @param userId
	 * @return
	 */
	public List<RewardPesonStatistical> getCollListInfoByUserIdDesc(
			long startIndex);

	/**
	 * 8打赏人按照金额排序
	 * 
	 * @param userId
	 * @return
	 */
	public List<RewardPesonStatistical> getPayListInfoByUserIdDesc(
			long startIndex);

	/**
	 * 9当日被打赏金额
	 * 
	 * @param userId
	 * @return
	 */
	public int getDayMoneyByUserId(long userId);

	/**
	 * 资源被打赏排行榜
	 * 
	 * @return
	 */
	public List<RewardStatistical> getRewardStatisticalListBySourceIdDesc(long start);

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
	 * 查询某个用户的成功提现总额
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
