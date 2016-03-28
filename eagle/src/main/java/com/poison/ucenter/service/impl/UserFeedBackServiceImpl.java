package com.poison.ucenter.service.impl;

import com.poison.ucenter.domain.repository.UserFeedBackDomainRepository;
import com.poison.ucenter.model.UserFeedBack;
import com.poison.ucenter.service.UserFeedBackService;

public class UserFeedBackServiceImpl implements UserFeedBackService{

	private UserFeedBackDomainRepository userFeedBackDomainRepository;
	
	public void setUserFeedBackDomainRepository(
			UserFeedBackDomainRepository userFeedBackDomainRepository) {
		this.userFeedBackDomainRepository = userFeedBackDomainRepository;
	}

	/**
	 * 插入用户反馈
	 */
	@Override
	public UserFeedBack insertintoUserfeedback(UserFeedBack userFeedBack) {
		return userFeedBackDomainRepository.insertintoUserfeedback(userFeedBack);
	}

}
