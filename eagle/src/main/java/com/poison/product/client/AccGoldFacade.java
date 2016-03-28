package com.poison.product.client;

import com.poison.product.model.AccGold;
import com.poison.product.model.AccGoldRecord;

public interface AccGoldFacade {
	/**
	 * @param
	 * @return
	 */
	public int insertIntoAccGold(long userId,long sequence,long goldamount,String extendField1,String extendField2,String extendField3);

	/**
	 *
	 * @return
	 */
	public AccGold findAccGoldByUserId(long userId);
	
	/**
	 * @param accGold
	 * @return
	 */
	public int updateAccGoldByUserId(long userId,long goldamount);

	/**
	 * @param accGold
	 * @return
	 */
	public int addAccGoldByUserId(long userId,long goldamount);

	public int reduceAccGoldByUserId(long userId,long goldamount);
	/**
	 * @param
	 * @returno
	 */
	public int insertIntoGoldRecord(long userId,long tradeAmount,long restAmount,String ordernum,String remark,long sequence,String type,String changeDesc,String cause,int shadow,String extendField1,String extendField2,String extendField3);
}
