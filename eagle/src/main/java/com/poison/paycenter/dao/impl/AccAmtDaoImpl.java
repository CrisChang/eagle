package com.poison.paycenter.dao.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.AccAmtDao;
import com.poison.paycenter.model.AccAmt;

/**
 * @author yan_dz
 *
 */
public class AccAmtDaoImpl extends SqlMapClientDaoSupport implements AccAmtDao {
	private static final Log LOG = LogFactory.getLog(AccAmtDaoImpl.class);
	/* (non-Javadoc)
	 * @see com.poison.paycenter.dao.AccAmtDao#insertAccAmtDao(com.poison.paycenter.model.AccAmt)
	 */
	@Override
	public int insertAccAmt(AccAmt accAmt) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoAccAmt",accAmt);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			throw e;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.poison.paycenter.dao.AccAmtDao#selectAccAmtInfoByUserId(long, java.lang.String)
	 */
	@Override
	public AccAmt selectAccAmtInfoByUserId(long userId){
		AccAmt accAmt = new AccAmt();
		try{
			accAmt = (AccAmt) getSqlMapClientTemplate().queryForObject("findAccAmtByUserId",userId);
			if(null==accAmt){
				accAmt = new AccAmt();
				accAmt.setFlag(ResultUtils.DATAISNULL);
				return accAmt;
			}
			accAmt.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			accAmt = new AccAmt();
			accAmt.setFlag(ResultUtils.QUERY_ERROR);
		}
		return accAmt;
	}

	/* (non-Javadoc)
	 * @see com.poison.paycenter.dao.AccAmtDao#updateAccAmt(com.poison.paycenter.model.AccAmt)
	 */
	@Override
	public int updateAccAmt(AccAmt accAmt) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateAccAmtByUserId",accAmt);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public int updateAccAmt(Map<String, Object> accAmtMap) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateAccAmtByUserIdNew",accAmtMap);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}
	
	@Override
	public int updateAccAmtForPayer(Map<String, Object> accAmtMap) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateAccAmtByUserIdNewForPayer",accAmtMap);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}

	@Override
	public int updateAccAmt2(Map<String, Object> accAmtMap) throws Exception{
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateAccAmtByUserIdColl",accAmtMap);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			throw e;
		}
		return flag;
	}
}
