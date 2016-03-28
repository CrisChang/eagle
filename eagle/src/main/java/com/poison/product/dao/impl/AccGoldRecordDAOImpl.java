package com.poison.product.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.product.dao.AccGoldRecordDao;
import com.poison.product.model.AccGoldRecord;

public class AccGoldRecordDAOImpl extends SqlMapClientDaoSupport implements AccGoldRecordDao{

	private static final  Log LOG = LogFactory.getLog(AccGoldRecordDAOImpl.class);
	/**
	 *  保存一个金币账户变动记录
	 */
	@Override
	public int insertIntoGoldRecord(AccGoldRecord accGoldRecord){
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoGoldRecord", accGoldRecord);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			i = ResultUtils.ERROR;
		}
		return i;
	}
}
