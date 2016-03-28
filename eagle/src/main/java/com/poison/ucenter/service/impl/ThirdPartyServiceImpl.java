package com.poison.ucenter.service.impl;

import com.poison.ucenter.domain.repository.ThirdPartyDomainRepository;
import com.poison.ucenter.model.ThirdPartyLogin;
import com.poison.ucenter.service.ThirdPartyService;

public class ThirdPartyServiceImpl implements ThirdPartyService{

	private ThirdPartyDomainRepository thirdPartyDomainRepository;
	
	public void setThirdPartyDomainRepository(
			ThirdPartyDomainRepository thirdPartyDomainRepository) {
		this.thirdPartyDomainRepository = thirdPartyDomainRepository;
	}

	/**
	 * 插入第三方信息
	 */
	@Override
	public ThirdPartyLogin insertThirdParty(ThirdPartyLogin thirdParty,long uid) {
		return thirdPartyDomainRepository.insertThirdParty(thirdParty,uid);
	}

	@Override
	public ThirdPartyLogin findThirdPartyByOpenIdAndLoginResource(
			String openId, String loginResource) {
		return thirdPartyDomainRepository.findThirdPartyByOpenIdAndLoginResource(openId, loginResource);
	}

}
