package com.poison.paycenter.dao;

import com.poison.paycenter.model.AccTakeSet;

/**
 * @author weizhensong
 *
 */
public interface AccTakeSetDao {

	/**
	 * @param set
	 * @return
	 */
	public int insertAccTakeSet(AccTakeSet set);
	
	/**
	 * @param set
	 * @return
	 */
	public int updateAccTakeSet(AccTakeSet set);
	
	/**
	 * @param uid
	 * @return
	 */
	public AccTakeSet selectAccTakeSetByUserId(long uid);
}
