package com.poison.ucenter.service;

import java.util.Map;

import com.poison.ucenter.model.UserStatistics;

public interface UserStatisticsService {

	/**
	 * 
	 * <p>Title: insertUserStatistics</p> 
	 * <p>Description: 插入用户统计</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午3:28:50
	 * @param userId
	 * @return
	 */
	public UserStatistics insertUserStatistics(long userId);
	
	/**
	 * 
	 * <p>Title: findUserStatisticsByUid</p> 
	 * <p>Description: 根据uid查询用户统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午8:40:05
	 * @param userId
	 * @return
	 */
	public UserStatistics findUserStatisticsByUid(long userId);
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:47:25
	 * @param userId
	 * @return
	 */
	public UserStatistics updateBkcommentCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:47:25
	 * @param userId
	 * @return
	 */
	public UserStatistics updateBkcommentNewCount(long userId,long bkcommentCount);
	
	/**
	 * 
	 * <p>Title: updateMvcommentCount</p> 
	 * <p>Description: 更新影评总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:47:57
	 * @param userId
	 * @return
	 */
	public UserStatistics updateMvcommentCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateMvcommentCount</p> 
	 * <p>Description: 更新影评总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:47:57
	 * @param userId
	 * @return
	 */
	public UserStatistics updateMvcommentNewCount(long userId,long mvcommentCount);
	
	/**
	 * 
	 * <p>Title: updateBklistCount</p> 
	 * <p>Description: 更新书单总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:48:24
	 * @param userId
	 * @return
	 */
	public UserStatistics updateBklistCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateMvlistCount</p> 
	 * <p>Description: 更新影单总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:48:50
	 * @param userId
	 * @return
	 */
	public UserStatistics updateMvlistCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateTransmitCount</p> 
	 * <p>Description: 更新转发总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:49:34
	 * @param userId
	 * @return
	 */
	public UserStatistics updateTransmitCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 更新日记总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:51:14
	 * @param userId
	 * @return
	 */
	public UserStatistics updateDiaryCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 更新日记总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:51:14
	 * @param userId
	 * @return
	 */
	public UserStatistics updateDiaryNewCount(long userId,long diaryCount);
	
	/**
	 * 
	 * <p>Title: updatePostCount</p> 
	 * <p>Description: 更新帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:52:20
	 * @param userId
	 * @return
	 */
	public UserStatistics updatePostCount(long userId);
	
	
	/**
	 * 
	 * <p>Title: updatePostCount</p> 
	 * <p>Description: 更新新的长文章总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:52:20
	 * @param userId
	 * @return
	 */
	public UserStatistics updateArticleCount(long userId);
	/**
	 * 
	 * <p>Title: updateCommentSwitch</p> 
	 * <p>Description: 更新评论的推送开关</p> 
	 * @author :changjiang
	 * date 2015-4-29 下午2:52:38
	 * @param userId
	 * @param commentSwitch
	 * @return
	 */
	public UserStatistics updateCommentSwitch(long userId, int commentSwitch);
	
	/**
	 * 
	 * <p>Title: updateGiveSwitch</p> 
	 * <p>Description: 更新打赏的推送开关</p> 
	 * @author :changjiang
	 * date 2015-4-29 下午2:53:30
	 * @param userId
	 * @param giveSwitch
	 * @return
	 */
	public UserStatistics updateGiveSwitch(long userId, int giveSwitch);
	
	/**
	 * 
	 * <p>Title: updateAtSwitch</p> 
	 * <p>Description: 更新@的推送开关</p> 
	 * @author :changjiang
	 * date 2015-4-29 下午2:54:01
	 * @param userId
	 * @param atSwitch
	 * @return
	 */
	public UserStatistics updateAtSwitch(long userId, int atSwitch);
	/**
	 * 查询总条数
	 */
	public Map<String, Object> findTotalNum();
	/**
	 * 查询小于书评数量的条数
	 */
	public Map<String, Object> findNumByLessBkcommentCount(long bkcommentCount);
	/**
	 * 查询小于影评数量的条数
	 */
	public Map<String, Object> findNumByLessMvcommentCount(long mvcommentCount);
}
