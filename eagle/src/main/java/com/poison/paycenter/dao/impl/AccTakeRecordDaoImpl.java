package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.AccTakeRecordDao;
import com.poison.paycenter.model.AccTakeRecord;

/**
 * @author yan_dz
 *
 */
public class AccTakeRecordDaoImpl extends SqlMapClientDaoSupport implements AccTakeRecordDao {
	private static final Log LOG = LogFactory.getLog(AccTakeRecordDaoImpl.class);
	@Override
	public int insertAccTakeRecord(AccTakeRecord accTakeRecord){
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertAccTakeRecord",accTakeRecord);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			//throw e;
		}
		return flag;
	}

	/**
	 * 提现列表--查询某个用户所有的提现列表
	 */
	@Override
	public List<AccTakeRecord> findAccTakeRecords(long uid,long start,int limit) {
		List<AccTakeRecord> accTakeRecords = new ArrayList<AccTakeRecord>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("start", start);
			map.put("limit", limit);
			accTakeRecords = getSqlMapClientTemplate().queryForList("findAccTakeRecords", map);
			if(null==accTakeRecords){
				accTakeRecords = new ArrayList<AccTakeRecord>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			accTakeRecords = new ArrayList<AccTakeRecord>();
		}
		return accTakeRecords;
	}
	/**
	 * 提现列表--查询某个用户某个状态的提现列表
	 */
	@Override
	public List<AccTakeRecord> findAccTakeRecords(long uid,int tradeStatus,long start,int limit) {
		List<AccTakeRecord> accTakeRecords = new ArrayList<AccTakeRecord>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("tradeStatus", tradeStatus);
			map.put("start", start);
			map.put("limit", limit);
			accTakeRecords = getSqlMapClientTemplate().queryForList("findAccTakeRecords", map);
			if(null==accTakeRecords){
				accTakeRecords = new ArrayList<AccTakeRecord>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			accTakeRecords = new ArrayList<AccTakeRecord>();
		}
		return accTakeRecords;
	}

	@Override
	public AccTakeRecord selectAccTakeRecordInfoBytakeNo(long uid,String takeno){
		//根据提现订单号和用户id查询
		AccTakeRecord accTakeRecord = new AccTakeRecord();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("takeno", takeno);
			accTakeRecord = (AccTakeRecord) getSqlMapClientTemplate().queryForObject("selectAccTakeRecordInfoBytakeNo", map);
			if(null==accTakeRecord){
				accTakeRecord = new AccTakeRecord();
				accTakeRecord.setFlag(ResultUtils.DATAISNULL);
				return accTakeRecord;
			}
			accTakeRecord.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			accTakeRecord = new AccTakeRecord();
			accTakeRecord.setFlag(ResultUtils.QUERY_ERROR);
		}
		return accTakeRecord;
	}

	@Override
	public int selectAccTakeRecordCount(long uid, long starttime, long endtime) {
		int i = -1;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("starttime", starttime);
			map.put("endtime", endtime);
			i = (Integer) getSqlMapClientTemplate().queryForObject("selectAccTakeRecordCount",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}

	@Override
	public long getAccTakeTotal(long uid) {
		long i = 0;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("tradeStatus", 1);
			Long total = (Long) getSqlMapClientTemplate().queryForObject("getAccTakeTotal",map);
			if(total!=null){
				i = total;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}

	@Override
	public long getAccTakeTotal(long uid, int tradeStatus) {
		long i = 0;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("tradeStatus", tradeStatus);
			Long total = (Long) getSqlMapClientTemplate().queryForObject("getAccTakeTotal",map);
			if(total!=null){
				i = total;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}
}
