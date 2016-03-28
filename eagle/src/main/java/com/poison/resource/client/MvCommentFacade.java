package com.poison.resource.client;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;

public interface MvCommentFacade {
	/**
	 * 
	 * 方法的描述 :此方法的作用是插入一条评论
	 * @param userId 用户
	 * @param movieId 电影id
	 * @param content 评论
	 * @param score 打分
	 * @param isOpposition 是否反对
	 * @param isDb 是否存在库中
	 * @return
	 */
	public MvComment addMvComment(long userId,long movieId,String content,String score,int isOpposition,int isDb,String type,Long resId,String lon,String lat,String locationDescription,String locationCity,String locationArea,String title,String cover,String resType);
	public MvComment addActivityMvComment(long userId, long movieId, String content,String score, int isOpposition, int isDb,String type,Long resId,String lon,String lat,String locationDescription,String locationCity,String locationArea,String title,String cover,String resType,long stageid);
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
	 * date 2014-8-26 下午12:37:10
	 * @param mvComment
	 * @return
	 */
	public List<MvComment> findUserMvComment(long userId,int movieId);
	/**
	 * 查询用户对一部电影的评论信息
	 */
	public List<MvComment> findUserMvComment(long userId,int movieId,long stageid);
	/**
	 * 
	 * <p>Title: findUserFriendMvComment</p> 
	 * <p>Description: 查询用户关注人对一部电影的评论信息</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午12:37:10
	 * @param mvComment
	 * @return
	 */
	public List<MvComment> findUserFriendMvComment(long userId,long movieId);
	
	/**
	 * 
	 * <p>Title: updateMyMvComment</p> 
	 * <p>Description: 更新评论信息</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:58:49
	 * @param id
	 * @param content
	 * @param score
	 * @return
	 */
	public MvComment updateMyMvComment(long id, String content, String score,String title,String cover);
	
	/**
	 * 
	 * <p>Title: deleteMvComment</p> 
	 * <p>Description: 删除一条评论</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:59:19
	 * @param id
	 * @return
	 */
	public MvComment deleteMvComment(long id);
	
	/**
	 * 
	 * <p>Title: findMvCommentCount</p> 
	 * <p>Description: 查找评论总数</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:59:47
	 * @param movieId
	 * @return
	 */
	public int findMvCommentCount(long movieId);
	
	/**
	 * 
	 * <p>Title: findMvCommentIsExist</p> 
	 * <p>Description: 查询一个影评</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午9:58:41
	 * @param id
	 * @return
	 */
	public MvComment findMvCommentIsExist(long id);
	
	/**
	 * 
	 * <p>Title: addMvAvgMark</p> 
	 * <p>Description: 增加一条评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午6:59:47
	 * @param bkAvgMark
	 * @return
	 */
	public MvAvgMark addMvAvgMark(long mvId,float mvAvgMark);
	
	/**
	 * 
	 * <p>Title: addExpertsAvgMark</p> 
	 * <p>Description: 增加专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 下午12:35:12
	 * @param mvId
	 * @param expertsAvgMark
	 * @return
	 */
	public MvAvgMark addExpertsAvgMark(long mvId,float expertsAvgMark);
	
	/**
	 * 
	 * <p>Title: updateBkAvgMark</p> 
	 * <p>Description: 更新一个评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午7:00:32
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark updateMvAvgMark(long mvId,float mvAvgMark, float beforeScore);
	
	/**
	 * 
	 * <p>Title: updateExpertsAvgMark</p> 
	 * <p>Description: 更新专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 下午12:37:28
	 * @param mvId
	 * @param mvAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark updateExpertsAvgMark(long mvId,float expertsAvgMark, float beforeScore);
	
	/**
	 * 
	 * <p>Title: deleteMvAvgMark</p> 
	 * <p>Description: 删除电影的评分</p> 
	 * @author :changjiang
	 * date 2014-9-18 下午12:18:42
	 * @param bkId
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark deleteMvAvgMark(int mvId, float mvAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: deleteExpertsAvgMark</p> 
	 * <p>Description: 删除专家评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 下午12:43:03
	 * @param mvId
	 * @param expertsAvgMark
	 * @param beforeScore
	 * @return
	 */
	public MvAvgMark deleteExpertsAvgMark(int mvId, float expertsAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: findMvAvgMarkByMvId</p> 
	 * <p>Description: 查找一个电影的评分</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午3:11:43
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
	 * <p>Title: deleteMyCommentByMvId</p> 
	 * <p>Description: 删除一部电影的评论</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午4:56:11
	 * @param id
	 * @return
	 */
	public MvComment deleteMyCommentByMvId(long id);
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByType</p> 
	 * <p>Description: 根据type查询电影评论</p> 
	 * @author :changjiang
	 * date 2014-9-18 下午1:23:11
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<MvComment> findAllMvCommentListByType(Long userId, String type,
			Long resId);
	
	/**
	 * 
	 * <p>Title: addMvCommentBigValue</p> 
	 * <p>Description: 增加影评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午2:31:54
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public MvComment addMvCommentBigValue(long id, float bigValue);
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByUsersId</p> 
	 * <p>Description: 根据用户id查询电影评论</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午4:32:30
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
	 * <p>Description: 根据uid查询电影评论总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:25:24
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findMvCommentCountByUid(long userId);
	
	/**
	 * 
	 * <p>Title: findUserLongMvCommentListByUserId</p> 
	 * <p>Description: 根据用户id查询长影评列表</p> 
	 * @author :changjiang
	 * date 2015-6-24 下午6:35:52
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
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @return
	 */
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
