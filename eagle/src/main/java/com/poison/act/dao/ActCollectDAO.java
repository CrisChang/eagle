package com.poison.act.dao;

import java.util.List;
import java.util.Map;

import com.poison.act.model.ActCollect;

public interface ActCollectDAO {

	/**
	 * 
	 * <p>Title: doCollect</p> 
	 * <p>Description: 添加收藏</p> 
	 * @author :changjiang
	 * date 2014-9-22 上午11:46:46
	 * @return
	 */
	public int doCollect(ActCollect actCollect);
	
	/**
	 * 
	 * <p>Title: findCollectCount</p> 
	 * <p>Description: 查找资源被多少人收藏</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:43:24
	 * @return
	 */
	public int findCollectCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: findCollectIsExist</p> 
	 * <p>Description: 查询收藏信息是否存在</p> 
	 * @author :changjiang
	 * date 2014-9-23 上午10:48:35
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActCollect findCollectIsExist(long userId,long resId);
	
	/**
	 * 
	 * <p>Title: findCollectById</p> 
	 * <p>Description: 根据id查询这条收藏信息</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午1:32:05
	 * @param id
	 * @return
	 */
	public ActCollect findCollectById(long id);
	
	/**
	 * 
	 * <p>Title: findCollectList</p> 
	 * <p>Description: 查询用户收藏信息列表</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午9:36:32
	 * @param userId
	 * @return
	 */
	public List<ActCollect> findCollectList(long userId,String type);
	
	/**
	 * 
	 * <p>Title: updateCollect</p> 
	 * <p>Description: 更新收藏信息</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午9:47:33
	 * @return
	 */
	public int updateCollect(ActCollect actCollect);
	
	/**
	 * 
	 * <p>Title: findUserCollectCount</p> 
	 * <p>Description: 查询用户的收藏总数</p> 
	 * @author :changjiang
	 * date 2015-3-31 下午2:55:10
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findUserCollectCount(long userId);
	
	/**
	 * 
	 * <p>Title: findUserCollectedList</p> 
	 * <p>Description: 查询用户的收藏列表</p> 
	 * @author :changjiang
	 * date 2015-4-7 上午10:56:10
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<ActCollect> findUserCollectedList(long userId, Long resId);
}
