package com.poison.ucenter.domain.repository;

import java.util.Map;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserStatisticsDAO;
import com.poison.ucenter.model.UserStatistics;

public class UserStatisticsDomainRepository {

	private UserStatisticsDAO userStatisticsDAO;

	public void setUserStatisticsDAO(UserStatisticsDAO userStatisticsDAO) {
		this.userStatisticsDAO = userStatisticsDAO;
	}
	
	/**
	 * 
	 * <p>Title: insertUserStatistics</p> 
	 * <p>Description: 插入用户统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午3:20:57
	 * @param userId
	 * @return
	 */
	public UserStatistics insertUserStatistics(long userId){
		UserStatistics userStatistics= userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//如果不存在，插入一条信息
		if(ResultUtils.DATAISNULL==flag){
			flag = userStatisticsDAO.insertUserStatistics(userId);
			userStatistics= userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: findUserStatisticsByUid</p> 
	 * <p>Description: 根据uid查找用户统计信息</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午8:07:03
	 * @param userId
	 * @return
	 */
	public UserStatistics findUserStatisticsByUid(long userId){
		return userStatisticsDAO.findUserStatisticsByUid(userId);
	}
	
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午6:16:15
	 * @param userId
	 * @return
	 */
	public UserStatistics updateBkcommentCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateBkcommentCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午6:16:15
	 * @param userId
	 * @return
	 */
	public UserStatistics updateBkcommentNewCount(long userId,long bkcommentCount){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateBkcommentNewCount(userId,bkcommentCount);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	/**
	 * 
	 * <p>Title: updateMvcommentCount</p> 
	 * <p>Description: 更新电影评论</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:13:45
	 * @param userId
	 * @return
	 */
	public UserStatistics updateMvcommentCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateMvcommentCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateMvcommentCount</p> 
	 * <p>Description: 更新电影评论数量-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:13:45
	 * @param userId
	 * @return
	 */
	public UserStatistics updateMvcommentNewCount(long userId,long mvcommentCount){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateMvcommentNewCount(userId,mvcommentCount);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateBklistCount</p> 
	 * <p>Description: 更新书单</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:17:35
	 * @param userId
	 * @return
	 */
	public UserStatistics updateBklistCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateBklistCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateMvlistCount</p> 
	 * <p>Description: 更新影单</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:23:51
	 * @param userId
	 * @return
	 */
	public UserStatistics updateMvlistCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateMvlistCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateTransmitCount</p> 
	 * <p>Description: 更新转发数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:25:47
	 * @param userId
	 * @return
	 */
	public UserStatistics updateTransmitCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateTransmitCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 更新日记总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:26:44
	 * @param userId
	 * @return
	 */
	public UserStatistics updateDiaryCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateDiaryCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 更新日记总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:26:44
	 * @param userId
	 * @return
	 */
	public UserStatistics updateDiaryNewCount(long userId,long diaryCount){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateDiaryNewCount(userId,diaryCount);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updatePostCount</p> 
	 * <p>Description: 更新帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:30:26
	 * @param userId
	 * @return
	 */
	public UserStatistics updatePostCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updatePostCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updatePostCount</p> 
	 * <p>Description: 更新新的长文章总数</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午7:30:26
	 * @param userId
	 * @return
	 */
	public UserStatistics updateArticleCount(long userId){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateArticleCount(userId);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateCommentSwitch</p> 
	 * <p>Description: 更新评论的消息提醒</p> 
	 * @author :changjiang
	 * date 2015-4-29 下午12:44:45
	 * @param userId
	 * @param commentSwitch
	 * @return
	 */
	public UserStatistics updateCommentSwitch(long userId, int commentSwitch){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateCommentSwitch(userId, commentSwitch);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateGiveSwitch</p> 
	 * <p>Description: 更新打赏的消息通知</p> 
	 * @author :changjiang
	 * date 2015-4-29 下午2:47:59
	 * @param userId
	 * @param commentSwitch
	 * @return
	 */
	public UserStatistics updateGiveSwitch(long userId, int giveSwitch){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateGiveSwitch(userId, giveSwitch);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 
	 * <p>Title: updateAtSwitch</p> 
	 * <p>Description: 更新@通知的开关</p> 
	 * @author :changjiang
	 * date 2015-4-29 下午2:49:38
	 * @param userId
	 * @param atSwitch
	 * @return
	 */
	public UserStatistics updateAtSwitch(long userId, int atSwitch){
		UserStatistics userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		int flag = userStatistics.getFlag();
		//当信息存在时
		if(ResultUtils.SUCCESS==flag){
			userStatisticsDAO.updateAtSwitch(userId, atSwitch);
			userStatistics = userStatisticsDAO.findUserStatisticsByUid(userId);
		}
		return userStatistics;
	}
	
	/**
	 * 查询总条数
	 */
	public Map<String, Object> findTotalNum(){
		return userStatisticsDAO.findTotalNum();
	}
	/**
	 * 查询小于书评数量的条数
	 */
	public Map<String, Object> findNumByLessBkcommentCount(long bkcommentCount){
		return userStatisticsDAO.findNumByLessBkcommentCount(bkcommentCount);
	}
	/**
	 * 查询小于影评数量的条数
	 */
	public Map<String, Object> findNumByLessMvcommentCount(long mvcommentCount){
		return userStatisticsDAO.findNumByLessMvcommentCount(mvcommentCount);
	}
}
