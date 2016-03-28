package com.poison.easemob.client;

import com.poison.easemob.model.EasemobUser;

public interface EasemobUserFacade {

	/**
	 * 
	 * <p>Title: insertEasemobUser</p> 
	 * <p>Description: 插入环信用户</p> 
	 * @author :changjiang
	 * date 2014-12-26 下午3:39:23
	 * @param easemobUser
	 * @return
	 */
	public EasemobUser insertEasemobUser(long userId);
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
	/**
	 * 向环信注册单个用户
	 * @Title: regIMUserSingle 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-7 下午2:37:38
	 * @param @param userId
	 * @param @return
	 * @return EasemobUser
	 * @throws
	 */
	public EasemobUser regIMUserSingle(long userId);
	/**
	 * 检查环信是否存在用户
	 * @Title: existIMUser 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-7 下午2:39:22
	 * @param @param userId
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean existIMUser(long userId);
}
