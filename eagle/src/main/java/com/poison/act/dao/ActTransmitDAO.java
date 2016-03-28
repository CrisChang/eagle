package com.poison.act.dao;

import java.util.List;
import java.util.Map;

import com.poison.act.model.ActPublish;
import com.poison.act.model.ActTransmit;

public interface ActTransmitDAO {

	/**
	 * 
	 * <p>Title: insertTransimit</p> 
	 * <p>Description: 添加转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-24 上午10:54:44
	 * @param actTransmit
	 * @return
	 */
	public int insertTransimit(ActTransmit actTransmit);
	
	/**
	 * 
	 * <p>Title: findTransmitCount</p> 
	 * <p>Description: 查询转发的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:12:14
	 * @return
	 */
	public int findTransmitCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: findTransmit</p> 
	 * <p>Description: 查询转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-26 下午2:06:09
	 * @return
	 */
	public List<ActTransmit> findTransmit(long userId);
	
	/**
	 * 
	 * <p>Title: findTransmitListByUserId</p> 
	 * <p>Description: 根据多个userid查询转发详情</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午7:11:43
	 * @param usersId
	 * @return
	 */
	public List<ActTransmit> findTransmitListByUserId(List<Long> usersIdList);
	
	/**
	 * 
	 * <p>Title: findAllTransmitList</p> 
	 * <p>Description: 查询转发列表所有信息</p> 
	 * @author :changjiang
	 * date 2014-8-18 上午12:44:24
	 * @param resId
	 * @return
	 */
	public List<ActTransmit> findAllTransmitList(Long resId);
	
	/**
	 * 
	 * <p>Title: findOneTransmit</p> 
	 * <p>Description: 查询一条转发的详情</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午2:51:13
	 * @param id
	 * @return
	 */
	public ActTransmit findOneTransmit(long id);
	
	/**
	 * 
	 * <p>Title: findTransmitListByTypeAndUsersId</p> 
	 * <p>Description: 根据id和type查询转发列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午4:55:46
	 * @param type
	 * @param resId
	 * @param usersId
	 * @return
	 */
	public List<ActTransmit> findTransmitListByTypeAndUsersId(String type, Long resId,
			List<Long> usersId);
	
	/**
	 * 
	 * <p>Title: findTransmitCount</p> 
	 * <p>Description: 查询转发总数</p> 
	 * @author :changjiang
	 * date 2014-12-10 下午3:01:12
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findTransmitCountByUid(long userId);
}
