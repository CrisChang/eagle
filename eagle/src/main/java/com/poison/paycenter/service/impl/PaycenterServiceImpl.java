package com.poison.paycenter.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.domain.repository.PaycenterDomainRepository;
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
 * @author yan_dz
 * 
 */
public class PaycenterServiceImpl implements PaycenterService {

	private PaycenterDomainRepository paycenterDomainRepository;

	public void setPaycenterDomainRepository(
			PaycenterDomainRepository paycenterDomainRepository) {
		this.paycenterDomainRepository = paycenterDomainRepository;
	}

	@Override
	public Map<String, String> notifyResult(PayLog paylog) {
		return paycenterDomainRepository.notifyResult(paylog);
	}

	@Override
	public Map<String, Object> selectCountReward(Map<String, Object> inMap) {
		return paycenterDomainRepository.selectCountReward(inMap);
	}

	@Override
	public Map<String, Object> selectRewardDetail(Map<String, Object> inMap) {
		return paycenterDomainRepository.selectRewardDetail(inMap);
	}

	@Override
	public Map<String, Object> createRollInfo(Map<String, Object> inMap) {
		return paycenterDomainRepository.createRollInfo(inMap);
	}

	@Override
	public Map<String, Object> backRollInfo(Map<String, Object> reqMap) {
		return paycenterDomainRepository.backRollInfo(reqMap);
	}

	@Override
	public Map<String, String> createReloadOrderInfoByMap(
			Map<String, String> inMap) {
		return paycenterDomainRepository.createReloadOrderInfoByMap(inMap);
	}
	
	@Override
	public Map<String, String> createWeixinReloadOrderInfoByMap(
			Map<String, String> inMap){
		return paycenterDomainRepository.createWeixinReloadOrderInfoByMap(inMap);
	}
	/**
	 * 自动打赏
	 */
	@Override
	public boolean autoReward(final Map<String, String> inMap){
		return paycenterDomainRepository.autoReward(inMap);
	}
	@Override
	public RewardPesonStatistical getMoneyByUserId(long userId) {
		return paycenterDomainRepository.getMoneyByUserId(userId);
	}

	@Override
	public RewardStatistical getMoneyBySourceId(long sourceId) {
		return paycenterDomainRepository.getMoneyBySourceId(sourceId);
	}

	@Override
	public List<RewardDetail> getRewardListInfoBySourceId(long sourceId,
			Long lastId) {
		return paycenterDomainRepository.getRewardListInfoBySourceId(sourceId,
				lastId);
	}

	// @Override
	// public List<RewardDetail> getDayRewardListInfoBySourceId(long sourceId) {
	// return
	// paycenterDomainRepository.getDayRewardListInfoBySourceId(sourceId);
	// }

	@Override
	public List<RewardDetail> getRewardListInfoByUserId(long userId, Long lastId) {
		return paycenterDomainRepository.getRewardListInfoByUserId(userId,
				lastId);
	}

	// @Override
	// public List<RewardDetail> getDayRewardListInfoByUserId(long userId) {
	// return paycenterDomainRepository.getDayRewardListInfoByUserId(userId);
	// }

	// @Override
	// public RewardStatistical getRewardCountByRecvUserID(long userId) {
	// return paycenterDomainRepository.getRewardCountByRecvUserID(userId);
	// }

	@Override
	public List<RewardDetail> getRewardListInfoByRecvUserId(long recvUserId,
			Long lastId) {
		return paycenterDomainRepository.getRewardListInfoByRecvUserId(
				recvUserId, lastId);
	}

	@Override
	public int getAccAmt(long userId) {
		return paycenterDomainRepository.getAccAmt(userId);
	}

	@Override
	public List<RewardPesonStatistical> getCollListInfoByUserIdDesc(
			long startIndex) {
		return paycenterDomainRepository
				.getCollListInfoByUserIdDesc(startIndex);
	}

	@Override
	public List<RewardPesonStatistical> getPayListInfoByUserIdDesc(
			long startIndex) {
		return paycenterDomainRepository.getPayListInfoByUserIdDesc(startIndex);
	}

	@Override
	public int getDayMoneyByUserId(long userId) {
		return paycenterDomainRepository.getDayMoneyByUserId(userId);
	}

	@Override
	public int getRewardCountBySourceId(long sourceId) {
		return paycenterDomainRepository.getRewardCountBySourceId(sourceId);
	}

	@Override
	public int getRewardCountByUserId(long userId) {
		return paycenterDomainRepository.getRewardCountByUserId(userId);
	}

	@Override
	public int getRewardCountByRecvUserId(long userId) {
		return paycenterDomainRepository.getRewardCountByRecvUserId(userId);
	}

	@Override
	public List<RewardStatistical> getRewardStatisticalListBySourceIdDesc(long start) {
		return paycenterDomainRepository
				.getRewardStatisticalListBySourceIdDesc(start);
	}

	@Override
	public int insertAccTakeSet(AccTakeSet set) {
		return paycenterDomainRepository.insertAccTakeSet(set);
	}

	@Override
	public int updateAccTakeSet(AccTakeSet set) {
		return paycenterDomainRepository.updateAccTakeSet(set);
	}

	@Override
	public AccTakeSet selectAccTakeSetByUserId(long uid) {
		return paycenterDomainRepository.selectAccTakeSetByUserId(uid);
	}

	@Override
	public int insertAccTakeRecord(AccTakeRecord accTakeRecord) {
		return paycenterDomainRepository.insertAccTakeRecord(accTakeRecord);
	}

	@Override
	public List<AccTakeRecord> findAccTakeRecords(long uid, long start,
			int limit) {
		return paycenterDomainRepository.findAccTakeRecords(uid, start, limit);
	}

	@Override
	public List<AccTakeRecord> findAccTakeRecords(long uid, int tradeStatus,
			long start, int limit) {
		return paycenterDomainRepository.findAccTakeRecords(uid, tradeStatus,
				start, limit);
	}

	@Override
	public AccTakeRecord selectAccTakeRecordInfoBytakeNo(long uid, String takeno) {
		return paycenterDomainRepository.selectAccTakeRecordInfoBytakeNo(uid,
				takeno);
	}

	@Override
	public int selectAccTakeRecordCount(long uid, long starttime, long endtime) {
		return paycenterDomainRepository.selectAccTakeRecordCount(uid,
				starttime, endtime);
	}

	@Override
	public long getAccTakeTotal(long uid) {
		return paycenterDomainRepository.getAccTakeTotal(uid);
	}

	@Override
	public long getAccTakeTotal(long uid, int tradeStatus) {
		return paycenterDomainRepository.getAccTakeTotal(uid, tradeStatus);
	}

	@Override
	public AccAmt getAccAmtInfo(long userId) {
		return paycenterDomainRepository.getAccAmtInfo(userId);
	}

	@Override
	public AccSet getAccSet() {
		return paycenterDomainRepository.getAccSet();
	}

	@Override
	public int insertIntoLegendHero(LegendHero hero) {
		return paycenterDomainRepository.insertIntoLegendHero(hero);
	}

	@Override
	public List<LegendHero> findAllLegendHero() {
		return paycenterDomainRepository.findAllLegendHero();
	}

	@Override
	public LegendHero findLegendHeroById(long id) {
		return paycenterDomainRepository.findLegendHeroById(id);
	}

	@Override
	public LegendHero findLegendHeroByUserId(long userId) {
		return paycenterDomainRepository.findLegendHeroByUserId(userId);
	}

	@Override
	public int updateLegendHeroById(LegendHero hero) {
		return paycenterDomainRepository.updateLegendHeroById(hero);
	}

	@Override
	public int updateLegendHeroByUserId(LegendHero hero) {
		return paycenterDomainRepository.updateLegendHeroByUserId(hero);
	}

	@Override
	public int deleteLegendHeroById(long id) {
		return paycenterDomainRepository.deleteLegendHeroById(id);
	}

	@Override
	public int deleteLegendHeroByUserId(long userId) {
		return paycenterDomainRepository.deleteLegendHeroByUserId(userId);
	}
}
