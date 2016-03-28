package com.poison.paycenter.dao;

import java.util.List;

import com.poison.paycenter.model.AmtRecord;

/**
 * @author yan_dz
 *
 */
public interface AmtRecordDao {

	/**
	 * @param amtRecord
	 * @return
	 * @throws Exception 
	 */
	public int insertAmtRecord(AmtRecord amtRecord) throws Exception;
	
	/**
	 * @param amtRecord
	 * @return
	 */
	public int updateAmtRecord(AmtRecord amtRecord) throws Exception;
	
	/**
	 * @param userId
	 * @return
	 */
	public List<AmtRecord> selectAmtRecordList(long userId);
	
	/**
	 * @param sendUserId
	 * @param sendUserName
	 * @return
	 */
	public AmtRecord selectAmtRecordInfoByUserId(long id, String name);
}
