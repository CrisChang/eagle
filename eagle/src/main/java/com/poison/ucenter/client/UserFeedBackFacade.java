package com.poison.ucenter.client;

import com.poison.ucenter.model.UserFeedBack;

public interface UserFeedBackFacade {

	/**
	 * 
	 * <p>Title: insertintoUserfeedback</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-1-23 下午4:22:06
	 * @param userFeedBack
	 * @return
	 */
	public UserFeedBack insertintoUserfeedback(long userId,long resourceId,String resType,String description,String otherInfo);
}
