package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.PayLogDao;
import com.poison.paycenter.model.PayLog;

/**
 * @author yan_dz
 *
 */
public class PayLogDaoImpl extends SqlMapClientDaoSupport implements PayLogDao {

	private static final Log LOG = LogFactory.getLog(PayLogDaoImpl.class);
	
	@Override
	public int insertPayLog(PayLog paylog) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoPayLogNew",paylog);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public int updatePayLog(PayLog paylog) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("",paylog);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public List<PayLog> selectPayLogList(long userId) {
		List<PayLog> list = new ArrayList<PayLog>();
		try{
			list = getSqlMapClientTemplate().queryForList("");
			if(null==list||list.size()==0){
				list = new ArrayList<PayLog>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			list = new ArrayList<PayLog>();
			PayLog payLog = new PayLog();
			payLog.setFlag(ResultUtils.QUERY_ERROR);
			list.add(payLog);
		}
		return list;
	}

	@Override
	public PayLog selectPayLogInfoByUserId(long id, String name) {
		PayLog payLog = new PayLog();
		try{
			payLog = (PayLog) getSqlMapClientTemplate().queryForObject("",id);
			if(null==payLog){
				payLog = new PayLog();
				payLog.setFlag(ResultUtils.DATAISNULL);
				return payLog;
			}
			payLog.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			payLog = new PayLog();
			payLog.setFlag(ResultUtils.QUERY_ERROR);
		}
		return payLog;
	}

}
