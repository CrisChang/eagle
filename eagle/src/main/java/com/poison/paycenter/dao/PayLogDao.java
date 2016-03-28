package com.poison.paycenter.dao;

import java.util.List;

import com.poison.paycenter.model.PayLog;

/**
 * @author yan_dz
 *
 */
public interface PayLogDao {

	/**
	 * @param paylog
	 * @return
	 * @throws Exception 
	 */
	public int insertPayLog(PayLog paylog) throws Exception;
	
	/**
	 * @param paylog
	 * @return
	 */
	public int updatePayLog(PayLog paylog) throws Exception;
	
	/**
	 * @param userId
	 * @return
	 */
	public List<PayLog> selectPayLogList(long userId);
	
	/**
	 * @param id
	 * @param name
	 * @return
	 */
	public PayLog selectPayLogInfoByUserId(long id, String name);
}
