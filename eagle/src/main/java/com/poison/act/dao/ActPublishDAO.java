package com.poison.act.dao;

import java.util.List;
import java.util.Map;

import com.poison.act.model.ActPublish;

public interface ActPublishDAO {

	/**
	 * 
	 * <p>Title: insertPublishInfo</p> 
	 * <p>Description: 插入一条推荐信息</p> 
	 * @author :changjiang
	 * date 2014-8-10 下午5:57:29
	 * @param publish
	 * @return
	 */
	public int insertPublishInfo(ActPublish publish);
	
	/**
	 * 
	 * <p>Title: findPublishList</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-8-10 下午5:59:41
	 * @return
	 */
	public List<ActPublish> findPublishList(Long resId);
	
	/**
	 * 
	 * <p>Title: findPublishById</p> 
	 * <p>Description: 根据ID查询推荐信息</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午2:59:02
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishById(long resId);
	
	/**
	 * 
	 * <p>Title: updatePublishById</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午3:16:49
	 * @param resId
	 * @return
	 */
	public int updatePublishById(ActPublish publish);
	
	/**
	 * 
	 * <p>Title: findOnePublish</p> 
	 * <p>Description: 查询一条发布信息</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午2:30:41
	 * @param id
	 * @return
	 */
	public ActPublish findOnePublish(long id);
	
	/**
	 * 
	 * <p>Title: findPublishListByUid</p> 
	 * <p>Description: 根据用户ID查询该用户的发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午7:03:59
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishListByUid(long userId,Long resId);
	
	/**
	 * 
	 * <p>Title: findPublishListByType</p> 
	 * <p>Description: 根据type查询发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-27 上午12:24:36
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishListByType(String type,Long resId);
	
	/**
	 * 
	 * <p>Title: findPublishListByUsersId</p> 
	 * <p>Description: 根据用户id查询发布信息</p> 
	 * @author :changjiang
	 * date 2014-10-27 上午11:37:56
	 * @param type
	 * @param resId
	 * @param usersId
	 * @return
	 */
	public List<ActPublish> findPublishListByUsersId(String type, Long resId,List<Long> usersId);
	
	/**
	 *
	 * <p>Title: findPublishListByUserId</p> 
	 * <p>Description: 根据用户id查询</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午2:44:47
	 * @param type
	 * @param resId
	 * @param userId
	 * @return
	 */
	public List<ActPublish> findPublishListByUserId(String type, Long resId,Long userId);
	
	/**
	 * 
	 * <p>Title: findPublishListByRecommendType</p> 
	 * <p>Description: 查询推荐的type类型</p> 
	 * @author :changjiang
	 * date 2014-11-25 下午6:39:05
	 * @param recommendType
	 * @return
	 */
	public List<ActPublish> findPublishListByRecommendType(String recommendType);
	
	/**
	 * 
	 * <p>Title: findPublishCount</p> 
	 * <p>Description: 查询发布的总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:36:50
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPublishCount(long userId);
	
	/**
	 * 
	 * <p>Title: findPublishByUidAndResIdAndType</p> 
	 * <p>Description: 根据</p> 
	 * @author :changjiang
	 * date 2015-2-5 下午12:25:11
	 * @param userId
	 * @param resId
	 * @param resType
	 * @return
	 */
	public ActPublish findPublishByUidAndResIdAndType(long userId,long resId,String resType);
	
}
