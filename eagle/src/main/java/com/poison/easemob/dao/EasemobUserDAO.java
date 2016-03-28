package com.poison.easemob.dao;

import com.poison.easemob.model.EasemobUser;

public interface EasemobUserDAO {

	/**
	 * 
	 * <p>Title: insertEasemobUser</p> 
	 * <p>Description: 插入环信用户</p> 
	 * @author :changjiang
	 * date 2014-12-26 下午3:19:42
	 * @param easemobUser
	 * @return
	 */
	public int insertEasemobUser(EasemobUser easemobUser);
	
	/**
	 * 
	 * <p>Title: findEasemobUserByUid</p> 
	 * <p>Description: 根据uid查询环信用户</p> 
	 * @author :changjiang
	 * date 2014-12-26 下午3:25:07
	 * @param userId
	 * @return
	 */
	public EasemobUser findEasemobUserByUid(long userId);
}
