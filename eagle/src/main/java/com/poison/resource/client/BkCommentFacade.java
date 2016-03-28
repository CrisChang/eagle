package com.poison.resource.client;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvAvgMark;

public interface BkCommentFacade {

	/**
	 * 
	 * <p>Title: addOneBkComment</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午5:09:27
	 * @return
	 */
	public BkComment addOneBkComment(long userId,int bookId,String comment,String score,int isOpposition,int isDb,String type,Long resId,String scan,String lon,String lat,String locationName,String locationCity,String locationArea,String level,String title,String cover,String resourceType);
	
	/**
	 * 
	 * <p>Title: addOneBkCommentByType</p> 
	 * <p>Description: 向圈子中发一个书评</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午4:08:07
	 * @param userId
	 * @param bookId
	 * @param comment
	 * @param score
	 * @param type
	 * @return
	 */
	public BkComment addOneBkCommentByType(long userId,int bookId,String comment,String score,String type,String resType);
	
	/**
	 * 
	 * <p>Title: findAllBkComment</p> 
	 * <p>Description: 查询一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午8:56:19
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
	 * <p>Title: findAllComment</p> 
	 * <p>Description: 查询所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午3:03:13
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllComment(Long resId);
	
	/**
	 * 
	 * <p>Title: findMyBkCommentList</p> 
	 * <p>Description: 查询自己对一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午9:16:24
	 * @param userId
	 * @param bookId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findMyBkCommentList(long userId, int bookId,Long resId,String resType);
	
	/**
	 * 
	 * <p>Title: findAllBkCommentListByType</p> 
	 * <p>Description: 根据type查询书评列表</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午3:54:16
	 * @param userId
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<BkComment> findAllBkCommentListByType(Integer userId, String type,
			Long resId);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询一本书的所有评论</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午2:50:22
	 * @param bookId
	 * @return
	 */
	public  int findCommentCount(int bookId,String resType);
	
	/**
	 * 
	 * <p>Title: updateMyCommentByBook</p> 
	 * <p>Description: 更新一本书的评论</p> 
	 * @author :changjiang
	 * date 2014-8-17 下午3:44:20
	 * @param id
	 * @param score
	 * @return
	 */
	public BkComment updateMyCommentByBook(long id,String score,String comment,String title,String cover) ;
	
	/**
	 * 
	 * <p>Title: findCommentIsExistById</p> 
	 * <p>Description: 查询一条评论</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午4:27:03
	 * @param id
	 * @return
	 */
	public BkComment findCommentIsExistById(long id);
	
	/**
	 * 
	 * <p>Title: deleteMyCommentById</p> 
	 * <p>Description: 删除一条评论</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午3:46:07
	 * @param id
	 * @return
	 */
	public BkComment deleteMyCommentById(long id);
	
	/**
	 * 
	 * <p>Title: findCommentListByUserId</p> 
	 * <p>Description: 根据用户ID查询该用户的评论列表</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午8:00:26
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<BkComment> findCommentListByUserId(long userId, Long resId);
	
	/**
	 * 
	 * <p>Title: addBkAvgMark</p> 
	 * <p>Description: 增加一条评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午6:59:47
	 * @param bkAvgMark
	 * @return
	 */
	public BkAvgMark addBkAvgMark(int bkId,String type,float bkAvgMark);
	
	/**
	 * 
	 * <p>Title: addExpertsBkAvgMark</p> 
	 * <p>Description: 增加专家的评分</p> 
	 * @author :changjiang
	 * date 2015-5-15 下午2:52:33
	 * @param bkId
	 * @param expertsAvgMark
	 * @return
	 */
	public BkAvgMark addExpertsBkAvgMark(int bkId,float expertsAvgMark);
	
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
	public BkAvgMark updateBkAvgMark(int bkId,float bkAvgMark, float beforeScore);
	
	/**
	 * 
	 * <p>Title: deleteBkAvgMark</p> 
	 * <p>Description: 删除一本书的评分</p> 
	 * @author :changjiang
	 * date 2014-9-18 上午11:49:48
	 * @param bkId
	 * @param bkAvgMark
	 * @param beforeScore
	 * @return
	 */
	public BkAvgMark deleteBkAvgMark(int bkId,float bkAvgMark, float beforeScore);
	
	/**
	 * 
	 * <p>Title: findBkAvgMarkByBkId</p> 
	 * <p>Description: 查询书的评分</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午2:51:09
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
	 * <p>Title: addBkCommentBigValue</p> 
	 * <p>Description: 增加书评的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 上午11:34:59
	 * @param id
	 * @param bigValue
	 * @return
	 */
	public BkComment addBkCommentBigValue(long id, float bigValue);
	
	/**
	 * 
	 * <p>Title: findBkCommentCount</p> 
	 * <p>Description: 根据userid查询书评总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午6:53:20
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findBkCommentCount(long userId);
	
	/**
	 * 
	 * <p>Title: updateExpertsAvgMark</p> 
	 * <p>Description: 修改图书的神人平均分</p> 
	 * @author :changjiang
	 * date 2015-5-15 下午4:59:32
	 * @param bkId
	 * @param expertsAvgMark
	 * @param beforeScore
	 * @return
	 */
	public BkAvgMark updateBkExpertsAvgMark(int bkId, float expertsAvgMark,
			float beforeScore);
	
	/**
	 * 
	 * <p>Title: findUserLongBkCommentListByUserId</p> 
	 * <p>Description: 根据用户id查询用户长书评</p> 
	 * @author :changjiang
	 * date 2015-6-26 下午4:21:23
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
