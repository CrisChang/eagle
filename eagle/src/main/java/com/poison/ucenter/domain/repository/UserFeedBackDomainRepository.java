package com.poison.ucenter.domain.repository;

import com.poison.ucenter.dao.UserFeedBackDAO;
import com.poison.ucenter.model.UserFeedBack;

public class UserFeedBackDomainRepository {
	
	private UserFeedBackDAO userFeedBackDAO;

	public void setUserFeedBackDAO(UserFeedBackDAO userFeedBackDAO) {
		this.userFeedBackDAO = userFeedBackDAO;
	}

	/**
	 * 
	 * <p>Title: insertintoUserfeedback</p> 
	 * <p>Description: 插入用户反馈</p> 
	 * @author :changjiang
	 * date 2015-1-23 下午4:13:18
	 * @param userFeedBack
	 * @return
	 */
	public UserFeedBack insertintoUserfeedback(UserFeedBack userFeedBack){
		long id = userFeedBack.getId();
		userFeedBackDAO.insertintoUserfeedback(userFeedBack);
		UserFeedBack UfeedBack = userFeedBackDAO.findUserFeedBack(id);
		return UfeedBack;
	}
}
