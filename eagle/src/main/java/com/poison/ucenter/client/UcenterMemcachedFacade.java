package com.poison.ucenter.client;

import com.poison.ucenter.model.UserAllInfo;

public interface UcenterMemcachedFacade {

	/**
	 * 
	 * <p>Title: saveUserAllInfo</p> 
	 * <p>Description: 存储用户个人资料</p> 
	 * @author :changjiang
	 * date 2014-11-13 下午2:57:15
	 * @param userInfo
	 * @return
	 */
	public boolean saveUserAllInfo(UserAllInfo userInfo);
	
	/**
	 * 
	 * <p>Title: findUserAllInfo</p> 
	 * <p>Description: 获取用户信息</p> 
	 * @author :changjiang
	 * date 2014-11-13 下午3:26:20
	 * @param userMemcachedId
	 * @return
	 */
	public UserAllInfo findUserAllInfo(long userMemcachedId);
}
