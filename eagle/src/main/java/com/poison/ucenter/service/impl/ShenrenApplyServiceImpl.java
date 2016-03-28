package com.poison.ucenter.service.impl;

import com.poison.ucenter.domain.repository.ShenrenApplyDomainRepository;
import com.poison.ucenter.model.ShenrenApply;
import com.poison.ucenter.service.ShenrenApplyService;

public class ShenrenApplyServiceImpl implements ShenrenApplyService {

	private ShenrenApplyDomainRepository shenrenApplyDomainRepository;
	
	public void setShenrenApplyDomainRepository(
			ShenrenApplyDomainRepository shenrenApplyDomainRepository) {
		this.shenrenApplyDomainRepository = shenrenApplyDomainRepository;
	}

	@Override
	public boolean insertintoShenrenApply(ShenrenApply shenrenApply) {
		return shenrenApplyDomainRepository.insertintoShenrenApply(shenrenApply);
	}
	
}
