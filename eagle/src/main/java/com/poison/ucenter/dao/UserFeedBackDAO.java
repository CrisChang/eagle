package com.poison.ucenter.dao;

import com.poison.ucenter.model.UserFeedBack;

public interface UserFeedBackDAO {

	/**
	 * 
	 * <p>Title: insertintoUserfeedback</p> 
	 * <p>Description: 插入用户反馈信息</p> 
	 * @author :changjiang
	 * date 2015-1-23 下午3:59:42
	 * @param userFeedBack
	 * @return
	 */
	public int insertintoUserfeedback(UserFeedBack userFeedBack);
	
	/**
	 * 
	 * <p>Title: findUserFeedBack</p> 
	 * <p>Description: 查询用户反馈信息</p> 
	 * @author :changjiang
	 * date 2015-1-23 下午4:01:32
	 * @param id
	 * @return
	 */
	public UserFeedBack findUserFeedBack(long id);
}
