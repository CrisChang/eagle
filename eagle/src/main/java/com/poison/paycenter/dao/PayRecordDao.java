package com.poison.paycenter.dao;

import java.util.Map;

import com.poison.paycenter.model.PayRecord;

/**
 * @author yan_dz
 *
 */
public interface PayRecordDao {

	/**
	 * @param payRecord
	 * @return
	 */
	public int insertPayRecord(PayRecord payRecord) throws Exception;
	
	/**
	 * @param payRecord
	 * @return
	 */
	public int updatePayRecord(PayRecord payRecord) throws Exception;
	
	/**
	 * @param userId
	 * @return
	 */
	/*public List<PayRecord> selectPayRecord(long userId);*/
	
	/**
	 * @param id
	 * @param name
	 * @return
	 */
	public PayRecord selectPayRecordInfoByUserId(long id, String name);

	/**
	 * 根据充值订单号，和充值状态
	 * @param outTradeNo
	 * @param tradeStatus 
	 * @return
	 */
	public PayRecord selectPayRecordInfoByOutTradeId(long id, int tradeStatus) throws Exception;

	public Map<String, Object> insertPayRecordNew(PayRecord payRecord) throws Exception;
}
