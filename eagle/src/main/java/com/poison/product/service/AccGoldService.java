package com.poison.product.service;

import com.poison.product.model.AccGold;
import com.poison.product.model.AccGoldRecord;

public interface AccGoldService {

	/**
	 * @param
	 * @return
	 */
	public int insertIntoAccGold(AccGold accGold);

	/**
	 *
	 * @return
	 */
	public AccGold findAccGoldByUserId(long userId);
	
	/**
	 * @param accGold
	 * @return
	 */
	public int updateAccGoldByUserId(AccGold accGold);

	/**
	 * @param accGold
	 * @return
	 */
	public int addAccGoldByUserId(AccGold accGold);

	public int reduceAccGoldByUserId(AccGold accGold);
	/**
	 * @param
	 * @returno
	 */
	public int insertIntoGoldRecord(AccGoldRecord accGoldRecord);
}
