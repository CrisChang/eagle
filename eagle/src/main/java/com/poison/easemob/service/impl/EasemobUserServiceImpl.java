package com.poison.easemob.service.impl;

import com.poison.easemob.domain.repository.EasemobUserDomainRepository;
import com.poison.easemob.model.EasemobUser;
import com.poison.easemob.service.EasemobUserService;

public class EasemobUserServiceImpl implements EasemobUserService{

	private EasemobUserDomainRepository easemobUserDomainRepository;

	public void setEasemobUserDomainRepository(
			EasemobUserDomainRepository easemobUserDomainRepository) {
		this.easemobUserDomainRepository = easemobUserDomainRepository;
	}

	/**
	 * 插入环信用户
	 */
	@Override
	public EasemobUser insertEasemobUser(EasemobUser easemobUser) {
		return easemobUserDomainRepository.insertEasemobUser(easemobUser);
	}

	@Override
	public EasemobUser findEasemobUserByUid(long userId) {
		return easemobUserDomainRepository.findEasemobUserByUid(userId);
	}
}
