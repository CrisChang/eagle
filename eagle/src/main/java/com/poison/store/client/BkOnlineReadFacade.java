package com.poison.store.client;

import com.poison.store.model.OnlineRead;

public interface BkOnlineReadFacade {

	/**
	 * 
	 * <p>Title: insertBkOnlineRead</p> 
	 * <p>Description: 插入一个试读</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午5:40:28
	 * @param read
	 * @return
	 */
	public int insertBkOnlineRead(int bkId,String onlineRead);
	
	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 查询一本书的试读</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午5:41:36
	 * @param bkId
	 * @return
	 */
	public OnlineRead findOnlineReadByBkId(int bkId,String resType);
	
	/**
	 * 
	 * <p>Title: updateBkOnLineRead</p> 
	 * <p>Description: 更新试读</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午3:59:44
	 * @param read
	 * @return
	 */
	public OnlineRead addBkTwoDimensionCode(long bkId,String twoDimensionCode,String resType);
}
