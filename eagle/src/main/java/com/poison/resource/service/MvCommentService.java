package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;

public interface MvCommentService {
	/**
	 * 
	 * 方法的描述 :此方法的作用是插入一条评论
	 * @param comment
	 * @return
	 */
	public MvComment addMvComment(MvComment comment);
	/**
	 * 
	 * 方法的描述 :查询所有评论信息
	 * @param id
	 * @return
	 */
	public List<MvComment> findAllComment(Long id);
	/**
	 * 
	 * 方法的描述 :查询这部电影的评论信息
	 * @param movieId
	 * @param id
	 * @return
	 */
	public List<MvComment> findAllMvComment(long movieId, Long id,String type,String resourceType);
	
	public List<MvComment> findAllMvCommentForOld(long movieId, Long id,String type,String resourceType,Integer pageIndex, Integer pageSize);
	/**
	 * 
	 * <p>Title: findUserMvComment</p> 
	 * <p>Description: 查询用户对一部电影的评论信息</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午12:33:44
	 * @param mvComment
	 * @return
	 */
	public List<MvComment> findUserMvComment(MvComment mvComment);
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByType</p> 
	 * <p>Description: 根据type查询电影的评论</p> 
	 * @author :changjiang
	 * date 2014-9-18 下午1:20:40
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<MvComment> findAllMvCommentListByType(Long userId, String type,
			Long resId);
	
	/**
	 * 
	 * <p>Title: updateMyMvComment</p> 
	 * <p>Description: 更新评论信息</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:49:29
	 * @param id
	 * @param content
	 * @param score
	 * @return
	 */
	public MvComment updateMyMvComment(long id,String content,String score,String title,String cover);
	
	/**
	 * 
	 * <p>Title: deleteMvComment</p> 
	 * <p>Description: 删除评论</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:50:19
	 * @param id
	 * @return
	 */
	public MvComment deleteMvComment(long id);
	
	/**
	 * 
	 * <p>Title: findMvCommentCount</p> 
	 * <p>Description: 查询一部电影评论总数</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:50:53
	 * @param movieId
	 * @return
	 */
	public int findMvCommentCount(long movieId);
	
	/**
	 * 
	 * <p>Title: findMvCommentIsExist</p> 
	 * <p>Description: 查询影评</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午9:56:50
	 * @param id
	 * @return
	 */
	public MvComment findMvCommentIsExist(long id);
	
	/**
	 * 
	 * <p>Title: addMvAvgMark</p> 
	 * <p>Description: 增加一个</p> 
	 * @author :changjiang
	 * date 2014-9-10 上午10:45:18
	 * @param mvAvgMark
	 * @return
	 */
	public MvAvgMark addMvAvgMark(MvAvgMark mvAvgMark);
	
	/**
	 * 
	 * <p>Title: updateMvAvgMark</p> 
	 * <p>Description: 更新一个评分</p> 
	 * @author :changjiang
	 * date 2014-9-10 上午11:30:44
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark updateMvAvgMark(MvAvgMark mvAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: deleteMvAvgMark</p> 
	 * <p>Description: 删除电影的评分</p> 
	 * @author :changjiang
	 * date 2014-9-18 下午12:16:09
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark deleteMvAvgMark(MvAvgMark mvAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: findMvAvgMarkByMvId</p> 
	 * <p>Description: 查询电影的评分</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午3:09:55
	 * @param mvId
	 * @return
	 */
	public MvAvgMark findMvAvgMarkByMvId(long mvId);
	/**
	 * 根据电影的id集合查询评分信息
	 */
	public List<MvAvgMark> findMvAvgMarkByMvIds(List<Long> mvids);
	/**
	 * 
	 * <p>Title: updateMvCommentBigValue</p> 
	 * <p>Description: 更新影评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午2:26:36
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public MvComment updateMvCommentBigValue(long id, float bigValue);
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByUsersId</p> 
	 * <p>Description: 根据用户id查询影评</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午4:31:13
	 * @param usersId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<MvComment> findAllMvCommentListByUsersId(List<Long> usersId,
			String type, Long resId);
	
	/**
	 * 
	 * <p>Title: findMvCommentCountByUid</p> 
	 * <p>Description: 根据uid查询电影评论</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:24:13
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findMvCommentCountByUid(long userId);
	
	/**
	 * 
	 * <p>Title: addExpertsAvgMark</p> 
	 * <p>Description: 添加专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 下午12:24:43
	 * @param mvAvgMark
	 * @return
	 */
	public MvAvgMark addExpertsAvgMark(MvAvgMark mvAvgMark);
	
	/**
	 * 
	 * <p>Title: updateExpertsAvgMark</p> 
	 * <p>Description: 更新专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 下午12:25:23
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark updateExpertsAvgMark(MvAvgMark mvAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: deleteExpertsAvgMark</p> 
	 * <p>Description: 删除专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 下午12:26:06
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark deleteExpertsAvgMark(MvAvgMark mvAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: findUserLongMvCommentListByUserId</p> 
	 * <p>Description: 查询用户的长影评列表</p> 
	 * @author :changjiang
	 * date 2015-6-24 下午6:31:32
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MvComment> findUserLongMvCommentListByUserId(long userId,
			Long resId);
	/**
	 * 查询某个用户某个时间段的影评
	 */
	public List<MvComment> findUserMvCommentsByTime(long userId,Long starttime,Long endtime);
	/**
	 * 根据标题和内容模糊查询，查询某个时间段的
	 */
	public List<MvComment> searchMvCommentByLike(long userId,String keyword,Long starttime,Long endtime,long start,int pageSize);
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	public Map<String, Object> findMvCommentCountByLike(long userId,String keyword,Long starttime,Long endtime);
	
	/**
	 * 根据阶段id查询影评列表根据评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByStageidOrderbyPoint(long stageid,long start,int pagesize);
	
	/**
	 * 根据阶段id查询影评列表根据评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByStageidOrderbyId(long stageid,long start,int pagesize);
	/**
	 * 根据阶段id查询用户影评得分排行榜按评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentUserRankByStageid(long stageid,long start,int pagesize);
	/**
	 * 根据阶段id查询某个人的影评列表根据评委评分排序
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByUseridAndStageid(long userId,long stageid,long start,int pagesize);
	/**
	 * 根据阶段id查询某个电影的影评列表
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByMovieidAndStageid(long movieId,long stageid,Long resId,int pagesize);
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	public List<MvComment> findMvCommentsByIdsAndStageid(long stageid,List<Long> commentids);
	public List<MvComment> findMvCommentsByIds(List<Long> commentids);
	/**
	 * 查询是否存在某个用户的影评记录
	 */
	public long findMvCommentRecord(long userid);

	/**
	 * 根据resType查询影评列表
	 * @param movieId
	 * @param resourceType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MvComment> findOneMvCommentListByResTypeAndPage(long movieId, String resourceType, int pageIndex, int pageSize);
}
