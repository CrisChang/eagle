package com.poison.product.dao;

import com.poison.product.model.AccGold;

/**
 * @author wzs
 *
 */
public interface AccGoldDao {

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
}
