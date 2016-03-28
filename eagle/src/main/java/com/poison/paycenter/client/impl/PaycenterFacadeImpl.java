package com.poison.paycenter.client.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poison.paycenter.client.PaycenterFacade;
import com.poison.paycenter.model.AccAmt;
import com.poison.paycenter.model.AccSet;
import com.poison.paycenter.model.AccTakeRecord;
import com.poison.paycenter.model.AccTakeSet;
import com.poison.paycenter.model.LegendHero;
import com.poison.paycenter.model.PayLog;
import com.poison.paycenter.model.RewardDetail;
import com.poison.paycenter.model.RewardPesonStatistical;
import com.poison.paycenter.model.RewardStatistical;
import com.poison.paycenter.service.PaycenterService;

/**
 * 
 * @author yan_dz
 * 
 */
public class PaycenterFacadeImpl implements PaycenterFacade {

	private PaycenterService paycenterService;

	public void setPaycenterService(PaycenterService paycenterService) {
		this.paycenterService = paycenterService;
	}

	@Override
	public Map<String, String> notifyResult(PayLog paylog) {
		return paycenterService.notifyResult(paylog);
	}

	@Override
	public Map<String, Object> selectCountReward(Map<String, Object> inMap) {
		return paycenterService.selectCountReward(inMap);
	}

	@Override
	public Map<String, Object> selectRewardDetail(Map<String, Object> inMap) {
		return paycenterService.selectRewardDetail(inMap);
	}

	@Override
	public Map<String, Object> createRollInfo(Map<String, Object> inMap) {
		return paycenterService.createRollInfo(inMap);
	}

	@Override
	public Map<String, Object> backRollInfo(Map<String, Object> reqMap) {
		return paycenterService.backRollInfo(reqMap);
	}

	@Override
	public Map<String, String> createReloadOrderInfoByMap(
			Map<String, String> inMap) {
		return paycenterService.createReloadOrderInfoByMap(inMap);
	}
	
	@Override
	public Map<String, String> createWeixinReloadOrderInfoByMap(
			Map<String, String> inMap) {
		return paycenterService.createWeixinReloadOrderInfoByMap(inMap);
	}
	/**
	 * 自动打赏
	 */
	@Override
	public boolean autoReward(String rewardUserId,String rewardUserName,String sourceId,String sourceName,String sourceType){
		Map<String, String> inMap = new HashMap<String,String>();
		inMap.put("rewardUserId", rewardUserId);
		inMap.put("rewardUserName", rewardUserName);
		inMap.put("sourceId", sourceId);
		inMap.put("sourceName", sourceName);
		inMap.put("sourceType", sourceType);
		return paycenterService.autoReward(inMap);
	}
	@Override
	public RewardPesonStatistical getMoneyByUserId(long userId) {
		return paycenterService.getMoneyByUserId(userId);
	}

	@Override
	public RewardStatistical getMoneyBySourceId(long sourceId) {
		return paycenterService.getMoneyBySourceId(sourceId);
	}

	@Override
	public List<RewardDetail> getRewardListInfoBySourceId(long sourceId,
			Long lastId) {
		return paycenterService.getRewardListInfoBySourceId(sourceId, lastId);
	}

	// @Override
	// public List<RewardDetail> getDayRewardListInfoBySourceId(long sourceId) {
	// return paycenterService.getDayRewardListInfoBySourceId(sourceId);
	// }

	@Override
	public List<RewardDetail> getRewardListInfoByUserId(long userId, Long lastId) {
		return paycenterService.getRewardListInfoByUserId(userId, lastId);
	}

	// @Override
	// public List<RewardDetail> getDayRewardListInfoByUserId(long userId) {
	// return paycenterService.getDayRewardListInfoByUserId(userId);
	// }

	@Override
	public List<RewardDetail> getRewardListInfoByRecvUserId(long recvUserId,
			Long lastId) {
		return paycenterService.getRewardListInfoByRecvUserId(recvUserId,
				lastId);
	}

	// @Override
	// public RewardStatistical getRewardCountByRecvUserID(long userId) {
	// return paycenterService.getRewardCountByRecvUserID(userId);
	// }

	@Override
	public int getAccAmt(long userId) {
		return paycenterService.getAccAmt(userId);
	}

	@Override
	public List<RewardPesonStatistical> getCollListInfoByUserIdDesc(
			long startIndex) {
		return paycenterService.getCollListInfoByUserIdDesc(startIndex);
	}

	@Override
	public List<RewardPesonStatistical> getPayListInfoByUserIdDesc(
			long startIndex) {
		return paycenterService.getPayListInfoByUserIdDesc(startIndex);
	}

	@Override
	public int getDayMoneyByUserId(long userId) {
		return paycenterService.getDayMoneyByUserId(userId);
	}

	@Override
	public int getRewardCountBySourceId(long sourceId) {
		return paycenterService.getRewardCountBySourceId(sourceId);
	}

	@Override
	public int getRewardCountByUserId(long userId) {
		return paycenterService.getRewardCountByUserId(userId);
	}

	@Override
	public int getRewardCountByRecvUserId(long userId) {
		return paycenterService.getRewardCountByRecvUserId(userId);
	}

	@Override
	public List<RewardStatistical> getRewardStatisticalListBySourceIdDesc(long start) {
		return paycenterService.getRewardStatisticalListBySourceIdDesc(start);
	}

	@Override
	public int insertAccTakeSet(AccTakeSet set) {
		return paycenterService.insertAccTakeSet(set);
	}

	@Override
	public int updateAccTakeSet(AccTakeSet set) {
		return paycenterService.updateAccTakeSet(set);
	}

	@Override
	public AccTakeSet selectAccTakeSetByUserId(long uid) {
		return paycenterService.selectAccTakeSetByUserId(uid);
	}

	@Override
	public int insertAccTakeRecord(AccTakeRecord accTakeRecord) {
		return paycenterService.insertAccTakeRecord(accTakeRecord);
	}

	@Override
	public List<AccTakeRecord> findAccTakeRecords(long uid, long start,
			int limit) {
		return paycenterService.findAccTakeRecords(uid, start, limit);
	}

	@Override
	public List<AccTakeRecord> findAccTakeRecords(long uid, int tradeStatus,
			long start, int limit) {
		return paycenterService.findAccTakeRecords(uid, tradeStatus, start,
				limit);
	}

	@Override
	public AccTakeRecord selectAccTakeRecordInfoBytakeNo(long uid, String takeno) {
		return paycenterService.selectAccTakeRecordInfoBytakeNo(uid, takeno);
	}

	@Override
	public int selectAccTakeRecordCount(long uid, long starttime, long endtime) {
		return paycenterService.selectAccTakeRecordCount(uid, starttime,
				endtime);
	}

	@Override
	public long getAccTakeTotal(long uid) {
		return paycenterService.getAccTakeTotal(uid);
	}

	@Override
	public long getAccTakeTotal(long uid, int tradeStatus) {
		return paycenterService.getAccTakeTotal(uid, tradeStatus);
	}

	@Override
	public AccAmt getAccAmtInfo(long userId) {
		return paycenterService.getAccAmtInfo(userId);
	}

	@Override
	public AccSet getAccSet() {
		return paycenterService.getAccSet();
	}

	@Override
	public int insertIntoLegendHero(LegendHero hero) {
		return paycenterService.insertIntoLegendHero(hero);
	}

	@Override
	public List<LegendHero> findAllLegendHero() {
		return paycenterService.findAllLegendHero();
	}

	@Override
	public LegendHero findLegendHeroById(long id) {
		return paycenterService.findLegendHeroById(id);
	}

	@Override
	public LegendHero findLegendHeroByUserId(long userId) {
		return paycenterService.findLegendHeroByUserId(userId);
	}

	@Override
	public int updateLegendHeroById(LegendHero hero) {
		return paycenterService.updateLegendHeroById(hero);
	}

	@Override
	public int updateLegendHeroByUserId(LegendHero hero) {
		return paycenterService.updateLegendHeroByUserId(hero);
	}

	@Override
	public int deleteLegendHeroById(long id) {
		return paycenterService.deleteLegendHeroById(id);
	}

	@Override
	public int deleteLegendHeroByUserId(long userId) {
		return paycenterService.deleteLegendHeroByUserId(userId);
	}
}
