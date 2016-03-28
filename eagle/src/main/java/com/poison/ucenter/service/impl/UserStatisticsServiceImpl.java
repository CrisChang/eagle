package com.poison.ucenter.service.impl;

import java.util.Map;

import com.poison.ucenter.domain.repository.UserStatisticsDomainRepository;
import com.poison.ucenter.model.UserStatistics;
import com.poison.ucenter.service.UserStatisticsService;

public class UserStatisticsServiceImpl implements UserStatisticsService{

	private UserStatisticsDomainRepository userStatisticsDomainRepository;
	
	public void setUserStatisticsDomainRepository(
			UserStatisticsDomainRepository userStatisticsDomainRepository) {
		this.userStatisticsDomainRepository = userStatisticsDomainRepository;
	}

	/**
	 * 插入用户统计信息
	 */
	@Override
	public UserStatistics insertUserStatistics(long userId) {
		return userStatisticsDomainRepository.insertUserStatistics(userId);
	}

	/**
	 * 更新书评总数
	 */
	@Override
	public UserStatistics updateBkcommentCount(long userId) {
		return userStatisticsDomainRepository.updateBkcommentCount(userId);
	}
	
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:47:25
	 * @param userId
	 * @return
	 */
	@Override
	public UserStatistics updateBkcommentNewCount(long userId,long bkcommentCount){
		return userStatisticsDomainRepository.updateBkcommentNewCount(userId, bkcommentCount);
	}
	/**
	 * 更新影评信息
	 */
	@Override
	public UserStatistics updateMvcommentCount(long userId) {
		return userStatisticsDomainRepository.updateMvcommentCount(userId);
	}
	
	/**
	 * 更新影评信息
	 */
	@Override
	public UserStatistics updateMvcommentNewCount(long userId,long mvcommentCount) {
		return userStatisticsDomainRepository.updateMvcommentNewCount(userId,mvcommentCount);
	}

	/**
	 * 更新书单信息
	 */
	@Override
	public UserStatistics updateBklistCount(long userId) {
		return userStatisticsDomainRepository.updateBklistCount(userId);
	}

	/**
	 * 更新影单信息
	 */
	@Override
	public UserStatistics updateMvlistCount(long userId) {
		return userStatisticsDomainRepository.updateMvlistCount(userId);
	}

	/**
	 * 更新转发数量
	 */
	@Override
	public UserStatistics updateTransmitCount(long userId) {
		return userStatisticsDomainRepository.updateTransmitCount(userId);
	}

	/**
	 * 更新日志数量
	 */
	@Override
	public UserStatistics updateDiaryCount(long userId) {
		return userStatisticsDomainRepository.updateDiaryCount(userId);
	}
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 更新日记总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:51:14
	 * @param userId
	 * @return
	 */
	@Override
	public UserStatistics updateDiaryNewCount(long userId,long diaryCount){
		return userStatisticsDomainRepository.updateDiaryNewCount(userId, diaryCount);
	}
	/**
	 * 更新帖子数量
	 */
	@Override
	public UserStatistics updatePostCount(long userId) {
		return userStatisticsDomainRepository.updatePostCount(userId);
	}
	/**
	 * 更新新的长文章数量
	 */
	@Override
	public UserStatistics updateArticleCount(long userId) {
		return userStatisticsDomainRepository.updateArticleCount(userId);
	}
	
	/**
	 * 根据uid查询用户的统计信息
	 */
	@Override
	public UserStatistics findUserStatisticsByUid(long userId) {
		return userStatisticsDomainRepository.findUserStatisticsByUid(userId);
	}

	/**
	 * 更新评论的推送开关
	 */
	@Override
	public UserStatistics updateCommentSwitch(long userId, int commentSwitch) {
		return userStatisticsDomainRepository.updateCommentSwitch(userId, commentSwitch);
	}

	/**
	 * 更新打赏的推送开关
	 */
	@Override
	public UserStatistics updateGiveSwitch(long userId, int giveSwitch) {
		return userStatisticsDomainRepository.updateGiveSwitch(userId, giveSwitch);
	}

	/**
	 * 更新@的推送开关
	 */
	@Override
	public UserStatistics updateAtSwitch(long userId, int atSwitch) {
		return userStatisticsDomainRepository.updateAtSwitch(userId, atSwitch);
	}
	/**
	 * 查询总条数
	 */
	@Override
	public Map<String, Object> findTotalNum(){
		return userStatisticsDomainRepository.findTotalNum();
	}
	/**
	 * 查询小于书评数量的条数
	 */
	@Override
	public Map<String, Object> findNumByLessBkcommentCount(long bkcommentCount){
		return userStatisticsDomainRepository.findNumByLessBkcommentCount(bkcommentCount);
	}
	/**
	 * 查询小于影评数量的条数
	 */
	@Override
	public Map<String, Object> findNumByLessMvcommentCount(long mvcommentCount){
		return userStatisticsDomainRepository.findNumByLessMvcommentCount(mvcommentCount);
	}
}
