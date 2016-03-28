package com.poison.easemob.service;

import com.poison.easemob.model.EasemobUser;

public interface EasemobUserService {

	/**
	 * 
	 * <p>Title: insertEasemobUser</p> 
	 * <p>Description: 插入环信用户</p> 
	 * @author :changjiang
	 * date 2014-12-26 下午3:37:52
	 * @param easemobUser
	 * @return
	 */
	public EasemobUser insertEasemobUser(EasemobUser easemobUser);
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
