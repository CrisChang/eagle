package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvComment;

public interface BkCommentDAO {

	/**
	 * 
	 * <p>Title: insertBkComment</p> 
	 * <p>Description: 插入书的评论</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午4:51:40
	 * @param bkComment
	 * @return
	 */
	public int insertBkComment(BkComment bkComment);
	
	/**
	 * 
	 * <p>Title: findAllBkComment</p> 
	 * <p>Description: 查询书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午8:13:00
	 * @param bookId
	 * @return
	 */
	public List<BkComment> findAllBkComment(int bookId,Long resId,String resType,String resourceType);
	/**
	 * 根据id集合查询
	 */
	public List<BkComment> findBkCommentListByIds(List<Long> ids);
	/**
	 * 查询所有书的评论
	 */
	public List<BkComment> findBkCommentListForOld(int bookId,Long resId,String resType,String resourceType);
	/**
	 * 
	 * <p>Title: findAllBkCommentListByType</p> 
	 * <p>Description: 根据type查询书评列表</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午3:24:19
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllBkCommentListByType(Integer userId,String type,Long resId);
	
	/**
	 * 
	 * <p>Title: findAllOpposiationComment</p> 
	 * <p>Description: 查询一本书的所有反对意见</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午8:13:22
	 * @param bookId
	 * @return
	 */
	public List<BkComment> findAllOpposiationComment(int bookId,Long resId);
	
	/**
	 * 
	 * <p>Title: findAllComment</p> 
	 * <p>Description: 查询所有的书评</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午2:52:32
	 * @return
	 */
	public List<BkComment> findAllComment(Long resId);
	
	/**
	 * 
	 * <p>Title: findAboveComment</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2014-11-11 下午12:52:08
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAboveComment(long resId);
	
	/**
	 * 
	 * <p>Title: findMyBkCommentList</p> 
	 * <p>Description: 查询自己一本书的评论</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午9:01:30
	 * @param userId
	 * @param bookId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findMyBkCommentList(long userId,int bookId,Long resId,String resType);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询一本书的评论总数</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午2:28:37
	 * @param bookId
	 * @return
	 */
	public int findCommentCount(int bookId,String resType);
	
	/**
	 * 
	 * <p>Title: updateMyCommentByBook</p> 
	 * <p>Description: 更新我对一本书的书评</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午3:11:04
	 * @param bkComment
	 * @return
	 */
	public int updateMyCommentByBook(BkComment bkComment);
	
	/**
	 * 
	 * <p>Title: deleteMyCommentById</p> 
	 * <p>Description: 删除一个书评</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午3:27:32
	 * @param id
	 * @return
	 */
	public int deleteMyCommentById(long id);
	
	/**
	 * 
	 * <p>Title: findCommentIsExistById</p> 
	 * <p>Description: 根据ID查询一条评论是否存在</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午3:26:10
	 * @param id
	 * @return
	 */
	public BkComment findCommentIsExistById(long id);
	
	/**
	 * 
	 * <p>Title: findCommentListByUserId</p> 
	 * <p>Description: 根据用户ID查询该用户的评论列表</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午7:49:05
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findCommentListByUserId(long userId,Long resId);
	
	/**
	 * 
	 * <p>Title: updateCommentBigValue</p> 
	 * <p>Description: 更新书评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 上午11:03:15
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public int updateBkCommentBigValue(long id,float bigValue);
	
	/**
	 * 
	 * <p>Title: findBkCommentCount</p> 
	 * <p>Description: 查询书评的总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午6:38:23
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findBkCommentCount(long userId);
	
	/**
	 * 
	 * <p>Title: findUserLongBkCommentListByUserId</p> 
	 * <p>Description: 根据用户id查询用户长书评</p> 
	 * @author :changjiang
	 * date 2015-6-26 下午3:52:14
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findUserLongBkCommentListByUserId(long userId,Long resId);
	/**
	 * 根据时间段查询用户的书评信息
	 */
	public List<BkComment> findMyBkCommentListByTime(long userId,Long starttime,Long endtime);
	/**
	 * 查询是否存在某个用户的书评记录
	 */
	public long findBkCommentRecord(long userid);
}
