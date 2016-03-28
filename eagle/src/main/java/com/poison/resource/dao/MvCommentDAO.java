package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvComment;

public interface MvCommentDAO {
	/**
	 * 
	 * 方法的描述 :插入一条评论
	 * @param commemt
	 * @return
	 */
	public int addMvComment(MvComment comment);
	/**
	 * 
	 * 方法的描述 :查询这部电影所有的评论
	 * @param movieId 电影id
	 * @param id
	 * @return
	 */
	public List<MvComment> findAllMvComment(long movieId,Long id,String type,String resourceType);
	
	public List<MvComment> findAllMvCommentForOld(long movieId, Long id,String type,String resourceType,Integer pageIndex, Integer pageSize);

	/**
	 * 分页查询影评列表
	 * @param movieId
	 * @param resourceType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<MvComment> findOneMvCommentListByResTypeAndPage(long movieId,String resourceType,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByType</p> 
	 * <p>Description: 根据type查询电影评论</p> 
	 * @author :changjiang
	 * date 2014-9-18 下午1:07:26
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<MvComment> findAllMvCommentListByType(Long userId,String type,Long resId);
	
	/**
	 * 
	 * 方法的描述 :查询但部电影 的反对意见
	 * @param movieId
	 * @param id
	 * @return
	 */
	public List<MvComment> findAllOpposiationComment(int movieId,Long id);
	/**
	 * 
	 * 方法的描述 :查询所有评论
	 * @param id
	 * @return
	 */
	public List<MvComment> findAllComment(Long id);
	
	/**
	 * 
	 * <p>Title: findUserComment</p> 
	 * <p>Description: 查询用户对一部电影的评论</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午12:10:18
	 * @param userId
	 * @param movieId
	 * @return
	 */
	public List<MvComment> findUserMvComment(MvComment mvComment);
	
	/**
	 * 
	 * <p>Title: updateMyMvComment</p> 
	 * <p>Description: 更新我的影评</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午3:32:20
	 * @param mvComment
	 * @return
	 */
	public int updateMyMvComment(MvComment mvComment);
	
	/**
	 * 
	 * <p>Title: findMvCommentCount</p> 
	 * <p>Description: 查询一部电影的评论总数</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午3:40:34
	 * @param movieId
	 * @return
	 */
	public int findMvCommentCount(long movieId);
	
	
	/**
	 * 
	 * <p>Title: findMvCommentIsExist</p> 
	 * <p>Description: 查询这个影评是否存在</p> 
	 * @author :changjiang
	 * date 2014-8-26 下午4:08:37
	 * @param id
	 * @return
	 */
	public MvComment findMvCommentIsExist(long id);
	
	/**
	 * 
	 * <p>Title: updateMvCommentBigValue</p> 
	 * <p>Description: 更新影评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午1:50:51
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public int updateMvCommentBigValue(long id,float bigValue);
	
	/**
	 * 
	 * <p>Title: findAllMvCommentListByUsersId</p> 
	 * <p>Description: 根据用户id查询电影评论列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午1:30:42
	 * @param usersId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<MvComment> findAllMvCommentListByUsersId(List<Long> usersId,String type,Long resId);
	
	/**
	 * 
	 * <p>Title: findMvCommentCountByUid</p> 
	 * <p>Description: 根据uid查询电影评论总数1</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:18:54
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findMvCommentCountByUid(long userId);
	
	/**
	 * 
	 * <p>Title: findUserLongMvCommentListByUserId</p> 
	 * <p>Description: 根据用户id查询用户的长影评</p> 
	 * @author :changjiang
	 * date 2015-6-24 下午6:09:49
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MvComment> findUserLongMvCommentListByUserId(long userId,Long resId);
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
	
}
