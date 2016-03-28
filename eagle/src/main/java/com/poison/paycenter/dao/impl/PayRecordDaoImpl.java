package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.PayRecordDao;
import com.poison.paycenter.model.PayRecord;

/**
 * @author yan_dz
 *
 */
public class PayRecordDaoImpl extends SqlMapClientDaoSupport implements PayRecordDao {
	private static final Log LOG = LogFactory.getLog(PayRecordDaoImpl.class);
	@Override
	public int insertPayRecord(PayRecord payRecord) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoPayRecord",payRecord);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public int updatePayRecord(PayRecord payRecord) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updatePayRecordByOutTradeNo",payRecord);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	/*@Override
	public List<PayRecord> selectPayRecord(long userId) {
		List<PayRecord> list = new ArrayList<PayRecord>();
		try{
			list = getSqlMapClientTemplate().queryForList("");
			if(null==list||list.size()==0){
				list = new ArrayList<PayRecord>();
			}
		}catch (Exception e) {
			e.printStackTrace();
			list = new ArrayList<PayRecord>();
			PayRecord payRecord = new PayRecord();
			payRecord.setFlag(ResultUtils.QUERY_ERROR);
			list.add(payRecord);
		}
		return list;
	}*/

	@Override
	public PayRecord selectPayRecordInfoByUserId(long id, String name) {
		PayRecord payRecord = new PayRecord();
		try{
			payRecord = (PayRecord) getSqlMapClientTemplate().queryForObject("",id);
			if(null==payRecord){
				payRecord = new PayRecord();
				payRecord.setFlag(ResultUtils.DATAISNULL);
				return payRecord;
			}
			payRecord.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			payRecord = new PayRecord();
			payRecord.setFlag(ResultUtils.QUERY_ERROR);
		}
		return payRecord;
	}

	@Override
	public PayRecord selectPayRecordInfoByOutTradeId(long id,
			int tradeStatus) throws Exception {
		//根据充值订单号和交易状态（等待处理，获取原充值记录）
		PayRecord payRecord = new PayRecord();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("tradeStatus", tradeStatus);
			payRecord = (PayRecord) getSqlMapClientTemplate().queryForObject("findPayRecordByOutTradeNo", map);
			if(null==payRecord){
				payRecord = new PayRecord();
				payRecord.setFlag(ResultUtils.DATAISNULL);
				return payRecord;
			}
			payRecord.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			payRecord = new PayRecord();
			payRecord.setFlag(ResultUtils.QUERY_ERROR);
		}
		return payRecord;
	}

	@Override
	public Map<String, Object> insertPayRecordNew(PayRecord payRecord) throws Exception{
		int flag = ResultUtils.ERROR;
		Map<String, Object>  reMap = new HashMap<String, Object>();
		try{
			long id = (Long) getSqlMapClientTemplate().insert("insertIntoPayRecordnew",payRecord);
			flag = ResultUtils.SUCCESS;			
			reMap.put("id", id);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		reMap.put("flag", flag);
		return reMap;
	}

}
