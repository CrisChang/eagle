package com.poison.paycenter.dao;

import java.util.Map;

import com.poison.paycenter.model.AccAmt;

/**
 * @author yan_dz
 *
 */
public interface AccAmtDao {

	/**
	 * @param accAmt
	 * @return
	 */
	public int insertAccAmt(AccAmt accAmt) throws Exception;

	/**
	 * @param sendUserId
	 * @param sendUserName
	 * @return
	 */
	public AccAmt selectAccAmtInfoByUserId(long sendUserId);
	
	/**
	 * @param accAmt
	 * @return
	 */
	public int updateAccAmt(AccAmt accAmt) throws Exception;

	/**
	 * @param accAmtMap
	 * @return
	 */
	public int updateAccAmt(Map<String, Object> accAmtMap) throws Exception;

	public int updateAccAmtForPayer(Map<String, Object> accAmtMap1) throws Exception;

	public int updateAccAmt2(Map<String, Object> accAmtMap) throws Exception; 
}
