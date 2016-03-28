package com.poison.product.domain.repository;

import com.poison.product.dao.AccGoldDao;
import com.poison.product.dao.AccGoldRecordDao;
import com.poison.product.model.AccGold;
import com.poison.product.model.AccGoldRecord;

public class AccGoldDomainRepository {
	
	private AccGoldDao accGoldDao;
	
	private AccGoldRecordDao accGoldRecordDao;

	public void setAccGoldDao(AccGoldDao accGoldDao) {
		this.accGoldDao = accGoldDao;
	}
	public void setAccGoldRecordDao(AccGoldRecordDao accGoldRecordDao) {
		this.accGoldRecordDao = accGoldRecordDao;
	}

	/**
	 * 插入一个金币账户
	 * @return
	 */
	public int insertIntoAccGold(AccGold accGold){
		return accGoldDao.insertIntoAccGold(accGold);
	}

	/**
	 *根据用户id查询金币账户信息
	 * @return
	 */
	public AccGold findAccGoldByUserId(long userId){
		return accGoldDao.findAccGoldByUserId(userId);
	}
	
	/**
	 * 根据用户id更新金币账户信息
	 * @param accGold
	 * @return
	 */
	public int updateAccGoldByUserId(AccGold accGold){
		return accGoldDao.updateAccGoldByUserId(accGold);
	}

	/**
	 * 在原来金币额基础上增加金币
	 * @param accGold
	 * @return
	 */
	public int addAccGoldByUserId(AccGold accGold){
		return accGoldDao.addAccGoldByUserId(accGold);
	}
	/**
	 * 在原来金币额基础上减少金币
	 * @param accGold
	 * @return
	 */
	public int reduceAccGoldByUserId(AccGold accGold){
		return accGoldDao.reduceAccGoldByUserId(accGold);
	}
	
	/**
	 * 插入一条金币账户变动记录
	 * @param
	 * @returno
	 */
	public int insertIntoGoldRecord(AccGoldRecord accGoldRecord){
		return accGoldRecordDao.insertIntoGoldRecord(accGoldRecord);
	}

}
