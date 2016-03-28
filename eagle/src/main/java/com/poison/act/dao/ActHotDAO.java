package com.poison.act.dao;

import java.util.Map;

import com.poison.act.model.ActHot;

public interface ActHotDAO {

	/**
	 * 
	 * <p>Title: insertHot</p> 
	 * <p>Description: 插入热度信息</p> 
	 * @author :changjiang
	 * date 2015-7-8 上午11:49:09
	 * @param actHot
	 * @return
	 */
	public int insertHot(ActHot actHot);
	
	/**
	 * 
	 * <p>Title: findActHotById</p> 
	 * <p>Description: 根据id查询热度</p> 
	 * @author :changjiang
	 * date 2015-7-8 下午5:49:49
	 * @param id
	 * @return
	 */
	public ActHot findActHotById(long id);
	
	/**
	 * 
	 * <p>Title: findIsHotByUserIdAndResIdType</p> 
	 * <p>Description: 查询用户是否加热过</p> 
	 * @author :changjiang
	 * date 2015-7-9 上午11:00:43
	 * @param userId
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public ActHot findIsHotByUserIdAndResIdType(long userId,long resourceId,String type);


	/**
	 * 根据Ip地址查询用户是否加热过
	 * @param ipAddress
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public ActHot findIsHotByIpAddressAndResIdType(String ipAddress,long resourceId,String type);

	/**
	 * 
	 * <p>Title: findHotCount</p> 
	 * <p>Description: 查找加热的总数</p> 
	 * @author :changjiang
	 * date 2015-7-9 下午3:26:02
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public Map<String, Object> findHotCount(long resourceId,String type);
}
