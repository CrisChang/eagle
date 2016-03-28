package com.poison.paycenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.CompanyAccountLogDao;
import com.poison.paycenter.model.CompanyAccountLog;

public class CompanyAccountLogDaoImpl extends SqlMapClientDaoSupport implements
		CompanyAccountLogDao {
	private static final Log LOG = LogFactory.getLog(CompanyAccountLogDaoImpl.class);
	@Override
	public int insertCompanyAccLog(CompanyAccountLog companyAccLog) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoCompanyAccountLog", companyAccLog);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}
}
