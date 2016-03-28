package com.poison.act.dao;

import java.util.List;

import com.poison.act.model.ActPraise;

public interface ActPraiseDAO {

	/**
	 * 
	 * <p>Title: insertPraise</p> 
	 * <p>Description: 插入点赞信息</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午10:05:56
	 * @param actPraise
	 * @return
	 */
	public int insertPraise(ActPraise actPraise);
	
	/**
	 * 
	 * <p>Title: findActPraise</p> 
	 * <p>Description: 查询点赞信息</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午10:11:26
	 * @return
	 */
	public ActPraise findActPraise(ActPraise actPraise);
	
	/**
	 * 
	 * <p>Title: findPraiseCount</p> 
	 * <p>Description: 查询点赞的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:06:02
	 * @return
	 */
	public int findPraiseCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: findLowCount</p> 
	 * <p>Description: 查询low的总数</p> 
	 * @author :changjiang
	 * date 2014-10-11 上午12:01:19
	 * @param resourceId
	 * @return
	 */
	public int findLowCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: updatePraise</p> 
	 * <p>Description: 更新点赞信息</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午9:26:59
	 * @param actPraise
	 * @return
	 */
	public int updatePraise(ActPraise actPraise);
	
	/**
	 * 
	 * <p>Title: findPraiseListByResIdAndType</p> 
	 * <p>Description: 根据资源id和资源类型查询点赞列表</p> 
	 * @author :changjiang
	 * date 2015-2-25 下午4:53:07
	 * @param resId
	 * @param resType
	 * @param id
	 * @return
	 */
	public List<ActPraise> findPraiseListByResIdAndType(long resId,Long id);
	
	/**
	 * 
	 * <p>Title: findPraiseListByResUid</p> 
	 * <p>Description: 根据用户id查询用户的点赞列表</p> 
	 * @author :changjiang
	 * date 2015-3-11 下午8:05:43
	 * @param resUid
	 * @param id
	 * @return
	 */
	public List<ActPraise> findPraiseListByResUid(long resUid,Long id);
	
}
