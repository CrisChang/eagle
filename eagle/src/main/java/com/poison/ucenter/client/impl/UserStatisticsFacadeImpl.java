package com.poison.ucenter.client.impl;

import java.util.Map;

import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserStatistics;
import com.poison.ucenter.service.UserStatisticsService;

public class UserStatisticsFacadeImpl implements UserStatisticsFacade{

	private UserStatisticsService userStatisticsService;
	
	public void setUserStatisticsService(UserStatisticsService userStatisticsService) {
		this.userStatisticsService = userStatisticsService;
	}

	/**
	 * 插入用户的统计信息
	 */
	@Override
	public UserStatistics insertUserStatistics(long userId) {
		return userStatisticsService.insertUserStatistics(userId);
	}

	/**
	 * 更新书评总数
	 */
	@Override
	public UserStatistics updateBkcommentCount(long userId) {
		return userStatisticsService.updateBkcommentCount(userId);
	}
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数--赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:58:15
	 * @param userId
	 * @return
	 */
	@Override
	public UserStatistics updateBkcommentNewCount(long userId,long bkcommentCount){
		return userStatisticsService.updateBkcommentNewCount(userId, bkcommentCount);
	}
	/**
	 * 更新影评总数
	 */
	@Override
	public UserStatistics updateMvcommentCount(long userId) {
		return userStatisticsService.updateMvcommentCount(userId);
	}
	
	/**
	 * 更新影评总数-赋值新值
	 */
	@Override
	public UserStatistics updateMvcommentNewCount(long userId,long mvcommentCount) {
		return userStatisticsService.updateMvcommentNewCount(userId,mvcommentCount);
	}

	/**
	 * 更新书单总数
	 */
	@Override
	public UserStatistics updateBklistCount(long userId) {
		return userStatisticsService.updateBklistCount(userId);
	}

	/**
	 * 更新影单总数
	 */
	@Override
	public UserStatistics updateMvlistCount(long userId) {
		return userStatisticsService.updateMvlistCount(userId);
	}

	/**
	 * 更新转发总数
	 */
	@Override
	public UserStatistics updateTransmitCount(long userId) {
		return userStatisticsService.updateTransmitCount(userId);
	}

	/**
	 * 更新日记总数
	 */
	@Override
	public UserStatistics updateDiaryCount(long userId) {
		return userStatisticsService.updateDiaryCount(userId);
	}
	
	/**
	 * 更新日记总数-赋值新值
	 */
	@Override
	public UserStatistics updateDiaryNewCount(long userId,long diaryCount) {
		return userStatisticsService.updateDiaryNewCount(userId,diaryCount);
	}

	/**
	 * 更新帖子总数
	 */
	@Override
	public UserStatistics updatePostCount(long userId) {
		return userStatisticsService.updatePostCount(userId);
	}
	
	/**
	 * 更新新的长文章总数
	 */
	@Override
	public UserStatistics updateArticleCount(long userId) {
		return userStatisticsService.updateArticleCount(userId);
	}

	/**
	 * 根据uid查询用户的统计信息
	 */
	@Override
	public UserStatistics findUserStatisticsByUid(long userId) {
		return userStatisticsService.findUserStatisticsByUid(userId);
	}

	/**
	 * 更新评论的推送通知
	 */
	@Override
	public UserStatistics updateCommentSwitch(long userId, int commentSwitch) {
		return userStatisticsService.updateCommentSwitch(userId, commentSwitch);
	}

	/**
	 * 更新打赏的推送通知
	 */
	@Override
	public UserStatistics updateGiveSwitch(long userId, int giveSwitch) {
		return userStatisticsService.updateGiveSwitch(userId, giveSwitch);
	}

	/**
	 * 更新@的推送通知
	 */
	@Override
	public UserStatistics updateAtSwitch(long userId, int atSwitch) {
		return userStatisticsService.updateAtSwitch(userId, atSwitch);
	}
	/**
	 * 查询总条数
	 */
	@Override
	public Map<String, Object> findTotalNum(){
		return userStatisticsService.findTotalNum();
	}
	/**
	 * 查询小于书评数量的条数
	 */
	@Override
	public Map<String, Object> findNumByLessBkcommentCount(long bkcommentCount){
		return userStatisticsService.findNumByLessBkcommentCount(bkcommentCount);
	}
	/**
	 * 查询小于影评数量的条数
	 */
	@Override
	public Map<String, Object> findNumByLessMvcommentCount(long mvcommentCount){
		return userStatisticsService.findNumByLessMvcommentCount(mvcommentCount);
	}
}
