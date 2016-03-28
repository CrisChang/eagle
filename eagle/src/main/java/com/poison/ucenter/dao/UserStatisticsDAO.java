package com.poison.ucenter.dao;

import java.util.Map;

import com.poison.ucenter.model.UserStatistics;

public interface UserStatisticsDAO {

	/**
	 * 
	 * <p>Title: insertUserStatistics</p> 
	 * <p>Description: 插入用户统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午2:38:25
	 * @param userId
	 * @return
	 */
	public int insertUserStatistics(long userId);
	
	/**
	 * 
	 * <p>Title: findUserStatisticsByUid</p> 
	 * <p>Description: 根据uid查询用户的统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午2:40:22
	 * @param userId
	 * @return
	 */
	public UserStatistics findUserStatisticsByUid(long userId);
	
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:35:44
	 * @param userId
	 * @return
	 */
	public int updateBkcommentCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:35:44
	 * @param userId
	 * @return
	 */
	public int updateBkcommentNewCount(long userId,long bkcommentCount);
	
	/**
	 * 
	 * <p>Title: updateMvcommentCount</p> 
	 * <p>Description: 更新影评总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:36:28
	 * @param userId
	 * @return
	 */
	public int updateMvcommentCount(long userId);
	/**
	 * 
	 * <p>Title: updateMvcommentCount</p> 
	 * <p>Description: 更新影评总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:36:28
	 * @param userId
	 * @return
	 */
	public int updateMvcommentNewCount(long userId,long mvcommentCount);
	
	/**
	 * 
	 * <p>Title: updateBklistCount</p> 
	 * <p>Description: 更新书单总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:36:56
	 * @param userId
	 * @return
	 */
	public int updateBklistCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateMvlistCount</p> 
	 * <p>Description: 更新影单总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:37:19
	 * @param userId
	 * @return
	 */
	public int updateMvlistCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateTransmitCount</p> 
	 * <p>Description: 修改转发的总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:39:23
	 * @param userId
	 * @return
	 */
	public int updateTransmitCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 修改日记总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:40:04
	 * @param userId
	 * @return
	 */
	public int updateDiaryCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 修改日记总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:40:04
	 * @param userId
	 * @return
	 */
	public int updateDiaryNewCount(long userId,long diaryCount);
	
	/**
	 * 
	 * <p>Title: updatePostCount</p> 
	 * <p>Description: 修改帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:41:22
	 * @param userId
	 * @return
	 */
	public int updatePostCount(long userId);
	/**
	 * 
	 * <p>Title: updatePostCount</p> 
	 * <p>Description: 修改帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:41:22
	 * @param userId
	 * @return
	 */
	public int updateArticleCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateCommentSwitch</p> 
	 * <p>Description: 更新评论的消息提醒</p> 
	 * @author :changjiang
	 * date 2015-4-28 下午5:38:04
	 * @param userId
	 * @return
	 */
	public int updateCommentSwitch(long userId,int commentSwitch);
	
	/**
	 * 
	 * <p>Title: updateGiveSwitch</p> 
	 * <p>Description: 更新打赏的消息开关</p> 
	 * @author :changjiang
	 * date 2015-4-28 下午5:39:43
	 * @param userId
	 * @param commentSwitch
	 * @return
	 */
	public int updateGiveSwitch(long userId,int giveSwitch);
	
	/**
	 * 
	 * <p>Title: updateAtSwitch</p> 
	 * <p>Description: 更新@的消息提醒</p> 
	 * @author :changjiang
	 * date 2015-4-28 下午5:40:28
	 * @param userId
	 * @param atSwitch
	 * @return
	 */
	public int updateAtSwitch(long userId,int atSwitch);
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
