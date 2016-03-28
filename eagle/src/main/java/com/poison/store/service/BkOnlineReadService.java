package com.poison.store.service;

import com.poison.store.model.OnlineRead;


public interface BkOnlineReadService {

	/**
	 * 
	 * <p>Title: insertBkOnlineRead</p> 
	 * <p>Description: 插入一个试读</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午5:08:27
	 * @param read
	 * @return
	 */
	public int insertBkOnlineRead(OnlineRead read);
	
	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 根据书的id查询书的试读</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午5:08:52
	 * @param bkId
	 * @return
	 */
	public OnlineRead findOnlineReadByBkId(int bkId,String resType);
	
	/**
	 * 
	 * <p>Title: updateBkOnLineRead</p> 
	 * <p>Description: 更新试读</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午3:58:43
	 * @param read
	 * @return
	 */
	public OnlineRead updateBkOnLineRead(OnlineRead read);
}
