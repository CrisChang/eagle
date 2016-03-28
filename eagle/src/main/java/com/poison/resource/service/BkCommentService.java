package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;

public interface BkCommentService {

	/**
	 * 
	 * <p>Title: addOneBkComment</p> 
	 * <p>Description: 插入一条书评</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午5:00:49
	 * @param bkComment
	 * @return
	 */
	public BkComment addOneBkComment(BkComment bkComment);
	
	/**
	 * 
	 * <p>Title: findAllBkComment</p> 
	 * <p>Description: 查询一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午8:54:54
	 * @param bookId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllBkComment(int bookId,Long resId,String resType,String resourceType);
	
	/**
	 * 查询所有书的评论
	 */
	public List<BkComment> findBkCommentListForOld(int bookId,Long resId,String resType,String resourceType);
	/**
	 *  根据id集合查询
	 */
	public List<BkComment> findBkCommentListByIds(List<Long> ids);
	/**
	 * 
	 * <p>Title: findAllBkCommentListByType</p> 
	 * <p>Description: 根据type查询书评列表</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午3:38:42
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllBkCommentListByType(Integer userId, String type,
			Long resId);
	
	/**
	 * 
	 * <p>Title: findAllComment</p> 
	 * <p>Description: 查询所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午3:01:56
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllComment(Long resId);
	
	/**
	 * 
	 * <p>Title: findMyBkCommentList</p> 
	 * <p>Description: 查询自己对一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午9:12:46
	 * @param userId
	 * @param bookId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findMyBkCommentList(long userId, int bookId,Long resId,String resType);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午2:47:02
	 * @param bookId
	 * @return
	 */
	public int findCommentCount(int bookId,String resType);
	
	/**
	 * 
	 * <p>Title: updateMyCommentByBook</p> 
	 * <p>Description: 更新一条评论</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午3:39:35
	 * @param bkComment
	 * @return
	 */
	public BkComment updateMyCommentByBook(BkComment bkComment);
	
	/**
	 * 
	 * <p>Title: findCommentIsExistById</p> 
	 * <p>Description: 查询一条评论详情</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午4:36:32
	 * @param id
	 * @return
	 */
	public BkComment findCommentIsExistById(long id);
	
	/**
	 * 
	 * <p>Title: findCommentListByUserId</p> 
	 * <p>Description: 根据用户ID查询该用户</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午7:57:32
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findCommentListByUserId(long userId,Long resId);
	
	/**
	 * 
	 * <p>Title: addBkAvgMark</p> 
	 * <p>Description: 增加一个书的评论</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午5:17:58
	 * @param bkAvgMark
	 * @return
	 */
	public BkAvgMark addBkAvgMark(BkAvgMark bkAvgMark);
	
	/**
	 * 
	 * <p>Title: updateBkAvgMark</p> 
	 * <p>Description: 更新一个书的评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午5:19:53
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public BkAvgMark updateBkAvgMark(BkAvgMark bkAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: deleteBkAvgMark</p> 
	 * <p>Description: 删除一本书的评分</p> 
	 * @author :changjiang
	 * date 2014-9-18 上午11:56:13
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public BkAvgMark deleteBkAvgMark(BkAvgMark bkAvgMark,float beforeScore);
	
	/**
	 * 
	 * <p>Title: findBkAvgMarkByBkId</p> 
	 * <p>Description: 查询一本书的评分</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午2:49:10
	 * @param bkId
	 * @return
	 */
	public BkAvgMark findBkAvgMarkByBkId(int bkId);
	/**
	 * 根据书的id集合查询和书的类型查询评分信息
	 */
	public List<BkAvgMark> findBkAvgMarkByBkIds(List<Long> bkids,String type);
	
	/**
	 * 
	 * <p>Title: deleteMyCommentById</p> 
	 * <p>Description: 删除一条评论</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午3:44:27
	 * @param id
	 * @return
	 */
	public BkComment deleteMyCommentById(long id);
	
	/**
	 * 
	 * <p>Title: updateBkCommentBigValue</p> 
	 * <p>Description: 更新书评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 上午11:33:21
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public BkComment updateBkCommentBigValue(long id, float bigValue);
	
	/**
	 * 
	 * <p>Title: findBkCommentCount</p> 
	 * <p>Description: 查询评论总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午6:52:07
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findBkCommentCount(long userId);
	
	/**
	 * 
	 * <p>Title: addExpertsBkAvgMark</p> 
	 * <p>Description: 增加神人的评论分</p> 
	 * @author :changjiang
	 * date 2015-5-15 下午3:03:40
	 * @param bkId
	 * @param expertsAvgMark
	 * @return
	 */
	public BkAvgMark addExpertsBkAvgMark(BkAvgMark bkAvgMark);
	
	/**
	 * 
	 * <p>Title: updateExpertsAvgMark</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-5-15 下午5:02:50
	 * @param bkId
	 * @param expertsAvgMark
	 * @param beforeScore
	 * @return
	 */
	public BkAvgMark updateBkExpertsAvgMark(BkAvgMark bkAvgMark,
			float beforeScore);
	
	/**
	 * 
	 * <p>Title: findUserLongBkCommentListByUserId</p> 
	 * <p>Description: 根据用户id查询长书评</p> 
	 * @author :changjiang
	 * date 2015-6-26 下午4:18:39
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findUserLongBkCommentListByUserId(long userId,
			Long resId);
	/**
	 * 根据时间段查询用户的书评信息
	 */
	public List<BkComment> findMyBkCommentListByTime(long userId,Long starttime,Long endtime);
	/**
	 * 查询是否存在某个用户的书评记录
	 */
	public long findBkCommentRecord(long userid);
}
