package com.poison.paycenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.AccTakeSetDao;
import com.poison.paycenter.model.AccTakeSet;

/**
 * @author yan_dz
 *
 */
public class AccTakeSetDaoImpl extends SqlMapClientDaoSupport implements AccTakeSetDao {
	private static final Log LOG = LogFactory.getLog(AccTakeSetDaoImpl.class);
	@Override
	public int insertAccTakeSet(AccTakeSet set){
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertAccTakeSet",set);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
			//throw e;
		}
		return flag;
	}

	@Override
	public int updateAccTakeSet(AccTakeSet set){
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateAccTakeSet",set);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.ERROR;
			//throw e;
		}
		return flag;
	}

	@Override
	public AccTakeSet selectAccTakeSetByUserId(long userid) {
		AccTakeSet set = new AccTakeSet();
		try{
			set = (AccTakeSet) getSqlMapClientTemplate().queryForObject("selectAccTakeSetByUserId",userid);
			if(null==set){
				set = new AccTakeSet();
				set.setFlag(ResultUtils.DATAISNULL);
				return set;
			}
			set.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			set = new AccTakeSet();
			set.setFlag(ResultUtils.QUERY_ERROR);
		}
		return set;
	}
}
