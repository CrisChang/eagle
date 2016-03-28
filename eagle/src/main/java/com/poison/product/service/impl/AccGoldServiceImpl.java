package com.poison.product.service.impl;

import com.poison.product.domain.repository.AccGoldDomainRepository;
import com.poison.product.model.AccGold;
import com.poison.product.model.AccGoldRecord;
import com.poison.product.service.AccGoldService;

public class AccGoldServiceImpl implements AccGoldService{

	private AccGoldDomainRepository accGoldDomainRepository;
	
	public void setAccGoldDomainRepository(
			AccGoldDomainRepository accGoldDomainRepository) {
		this.accGoldDomainRepository = accGoldDomainRepository;
	}

	/**
	 * @param
	 * @return
	 */
	@Override
	public int insertIntoAccGold(AccGold accGold){
		return accGoldDomainRepository.insertIntoAccGold(accGold);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public AccGold findAccGoldByUserId(long userId){
		return accGoldDomainRepository.findAccGoldByUserId(userId);
	}
	
	/**
	 * @param accGold
	 * @return
	 */
	@Override
	public int updateAccGoldByUserId(AccGold accGold){
		return accGoldDomainRepository.updateAccGoldByUserId(accGold);
	}

	/**
	 * @param accGold
	 * @return
	 */
	@Override
	public int addAccGoldByUserId(AccGold accGold){
		return accGoldDomainRepository.addAccGoldByUserId(accGold);
	}
	@Override
	public int reduceAccGoldByUserId(AccGold accGold){
		return accGoldDomainRepository.reduceAccGoldByUserId(accGold);
	}
	/**
	 * @param
	 * @returno
	 */
	@Override
	public int insertIntoGoldRecord(AccGoldRecord accGoldRecord){
		return accGoldDomainRepository.insertIntoGoldRecord(accGoldRecord);
	}

}
