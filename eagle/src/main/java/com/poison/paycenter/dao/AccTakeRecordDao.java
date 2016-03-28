package com.poison.paycenter.dao;

import java.util.List;

import com.poison.paycenter.model.AccTakeRecord;

/**
 * @author weizhensong
 *
 */
public interface AccTakeRecordDao {

	/**
	 * @param accTakeRecord
	 * @return
	 */
	public int insertAccTakeRecord(AccTakeRecord accTakeRecord);
	
	/**
	 * @param uid
	 * @return
	 */
	public List<AccTakeRecord> findAccTakeRecords(long uid,long start,int limit);
	/**
	 * 
	 * @Title: findAccTakeRecords 
	 * @Description: TODO
	 * @author weizhensong
	 * @date 2015-3-25 下午2:45:50
	 * @param @param uid
	 * @param @param tradeStatus
	 * @param @return
	 * @return List<AccTakeRecord>
	 * @throws
	 */
	public List<AccTakeRecord> findAccTakeRecords(long uid,int tradeStatus,long start,int limit);

	/**
	 * 根据提现订单号和用户id查询
	 * @param uid
	 * @param takeno 
	 * @return
	 */
	public AccTakeRecord selectAccTakeRecordInfoBytakeNo(long uid,String takeno);
	/**
	 * 查询一个时间段的某个用户的提现订单数量
	 * @author weizhensong
	 */
	public int selectAccTakeRecordCount(long uid,long starttime,long endtime);
	/**
	 * 查询某个用户的成功提现总额
	 */
	public long getAccTakeTotal(long uid);
	/**
	 * 根据交易状态查询提现总额(等待、成功、失败)
	 */
	public long getAccTakeTotal(long uid,int tradeStatus);
}
