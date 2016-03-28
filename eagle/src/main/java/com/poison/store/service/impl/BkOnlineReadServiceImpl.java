package com.poison.store.service.impl;

import com.poison.store.domain.repository.BkOnlineReadDomainRepository;
import com.poison.store.model.OnlineRead;
import com.poison.store.service.BkOnlineReadService;

public class BkOnlineReadServiceImpl implements BkOnlineReadService{

	private BkOnlineReadDomainRepository bkOnlineReadDomainRepository;

	public void setBkOnlineReadDomainRepository(
			BkOnlineReadDomainRepository bkOnlineReadDomainRepository) {
		this.bkOnlineReadDomainRepository = bkOnlineReadDomainRepository;
	}

	/**
	 * 插入书的试读
	 */
	@Override
	public int insertBkOnlineRead(OnlineRead read) {
		return bkOnlineReadDomainRepository.insertBkOnlineRead(read);
	}

	/**
	 * 根据书的id查询该书的试读
	 */
	@Override
	public OnlineRead findOnlineReadByBkId(int bkId,String resType) {
		return bkOnlineReadDomainRepository.findOnlineReadByBkId(bkId,resType);
	}

	/**
	 * 更新试读
	 */
	@Override
	public OnlineRead updateBkOnLineRead(OnlineRead read) {
		return bkOnlineReadDomainRepository.updateBkOnLineRead(read);
	}
	
}
