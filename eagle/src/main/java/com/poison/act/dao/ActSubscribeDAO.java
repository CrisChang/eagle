package com.poison.act.dao;

import java.util.List;

import com.poison.act.model.ActCollect;
import com.poison.act.model.ActSubscribe;

public interface ActSubscribeDAO {

	/**
	 * 
	 * <p>Title: findSubscribeList</p> 
	 * <p>Description: 查询订阅列表</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午5:25:11
	 * @param userId
	 * @return
	 */
	public List<ActSubscribe> findSubscribeList(long userId,String type);
	
	/**
	 * 
	 * <p>Title: findSubscribeSerializeList</p> 
	 * <p>Description: 查询订阅连载的列表</p> 
	 * @author :changjiang
	 * date 2014-9-24 上午10:21:16
	 * @param resId
	 * @return
	 */
	public List<ActSubscribe> findSubscribeListByResId(long resId);
	
	/**
	 * 
	 * <p>Title: insertSubscribe</p> 
	 * <p>Description: 插入订阅信息</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午3:01:13
	 * @param actSubscribe
	 * @return
	 */
	public int insertSubscribe(ActSubscribe actSubscribe);
	
	/**
	 * 
	 * <p>Title: findSubscribeIsExist</p> 
	 * <p>Description: 查询这条订阅信息是否存在</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午3:21:57
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActSubscribe findSubscribeIsExist(long userId,long resId);
	
	/**
	 * 
	 * <p>Title: updateSubscribe</p> 
	 * <p>Description: 更新订阅信息</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午3:37:24
	 * @param actSubscribe
	 * @return
	 */
	public int updateSubscribe(ActSubscribe actSubscribe);
}
