package com.poison.store.dao;

import com.poison.store.model.OnlineRead;

public interface BkOnlineReadDAO {

	/**
	 * 
	 * <p>Title: insertBkOnlineRead</p> 
	 * <p>Description: 插入试读部分</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午4:21:05
	 * @param read
	 * @return
	 */
	public int insertBkOnlineRead(OnlineRead read);
	
	/**
	 * 
	 * <p>Title: findOnlineReadByBkId</p> 
	 * <p>Description: 查询一本书的试读</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午4:45:18
	 * @param bkId
	 * @return
	 */
	public OnlineRead findOnlineReadByBkId(int bkId,String resType);
	
	/**
	 * 
	 * <p>Title: updateBkOnLineRead</p> 
	 * <p>Description: 更新试读</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午3:02:12
	 * @param read
	 * @return
	 */
	public int updateBkOnLineRead(OnlineRead read);
}
