package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.AmtRecordDao;
import com.poison.paycenter.model.AccAmt;
import com.poison.paycenter.model.AmtRecord;

/**
 * @author yan_dz
 *
 */
public class AmtRecordDaoImpl extends SqlMapClientDaoSupport implements AmtRecordDao {

	private static final Log LOG = LogFactory.getLog(AmtRecordDaoImpl.class);
	
	/* (non-Javadoc)
	 * @see com.poison.paycenter.dao.AmtRecordDao#insertAmtRecord(com.poison.paycenter.model.AmtRecord)
	 */
	@Override
	public int insertAmtRecord(AmtRecord amtRecord) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoAmtRecord",amtRecord);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.poison.paycenter.dao.AmtRecordDao#updateAmtRecord(com.poison.paycenter.model.AmtRecord)
	 */
	@Override
	public int updateAmtRecord(AmtRecord amtRecord) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("",amtRecord);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.poison.paycenter.dao.AmtRecordDao#selectAmtRecordList(long)
	 */
	@Override
	public List<AmtRecord> selectAmtRecordList(long userId) {
		List<AmtRecord> list = new ArrayList<AmtRecord>();
		try{
			list = getSqlMapClientTemplate().queryForList("");
			if(null==list||list.size()==0){
				list = new ArrayList<AmtRecord>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			list = new ArrayList<AmtRecord>();
			AmtRecord amtRecord = new AmtRecord();
			amtRecord.setFlag(ResultUtils.QUERY_ERROR);
			list.add(amtRecord);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.poison.paycenter.dao.AmtRecordDao#selectAmtRecordInfoByUserId(long, java.lang.String)
	 */
	@Override
	public AmtRecord selectAmtRecordInfoByUserId(long id, String name) {
		AmtRecord amtRecord = new AmtRecord();
		try{
			amtRecord = (AmtRecord) getSqlMapClientTemplate().queryForObject("",id);
			if(null==amtRecord){
				amtRecord = new AmtRecord();
				amtRecord.setFlag(ResultUtils.DATAISNULL);
				return amtRecord;
			}
			amtRecord.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			amtRecord = new AmtRecord();
			amtRecord.setFlag(ResultUtils.QUERY_ERROR);
		}
		return amtRecord;
	}

}
