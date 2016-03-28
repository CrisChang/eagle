package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.CompanyAccountDao;
import com.poison.paycenter.model.CompanyAccount;

public class CompanyAccountDaoImpl extends SqlMapClientDaoSupport implements
		CompanyAccountDao {
	private static final Log LOG = LogFactory.getLog(CompanyAccountDaoImpl.class);
	
	@Override
	public List<CompanyAccount> selectCompanyAccountList(int totalAmount, int limitAmount, long startDate, long endDate) throws Exception{
		List<CompanyAccount> companyAccountList = new ArrayList<CompanyAccount>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalAmount", totalAmount);
		map.put("limitAmount", limitAmount);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		try{
			companyAccountList = getSqlMapClientTemplate().queryForList("findCompanyAccountByAmt", map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			throw e;
		}
		return companyAccountList;
	}
	
	@Override
	public List<CompanyAccount> selectCompanyAccountList1(int limitAmount, long endDate, long startDate) throws Exception{
		List<CompanyAccount> companyAccountList = new ArrayList<CompanyAccount>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limitAmount", limitAmount);
		map.put("endDate", endDate);
		map.put("startDate", startDate);
		try{
			companyAccountList = getSqlMapClientTemplate().queryForList("findCompanyAccountByAmt1", map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			throw e;
		}
		return companyAccountList;
	}
	
	@Override
	public List<CompanyAccount> selectCompanyAccountList1(long lastDate) throws Exception{
		List<CompanyAccount> companyAccountList = new ArrayList<CompanyAccount>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lastDate", lastDate);
		try{
			companyAccountList = getSqlMapClientTemplate().queryForList("findCompanyAccountByAmt2", map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			throw e;
		}
		return companyAccountList;
	}

	@Override
	public CompanyAccount findCompanyAccount(long userId) {
		CompanyAccount companyAccount ;
		try {
			companyAccount = (CompanyAccount) getSqlMapClientTemplate().queryForObject("findCompanyAccountByCompanyId", userId);
			if(null==companyAccount){
				companyAccount = new CompanyAccount();
				companyAccount.setFlag(ResultUtils.DATAISNULL);
				return companyAccount;
			}
			companyAccount.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			companyAccount = new CompanyAccount();
			companyAccount.setFlag(ResultUtils.QUERY_ERROR);
		}
		return companyAccount;
	}

	@Override
	public int updateCompanyAccount(CompanyAccount companyAccount) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateCompanyAccount",companyAccount);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	@Override
	public int updateCompanyAccount(Map<String, Object> comAccMap) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateCompanyAccountNew",comAccMap);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	@Override
	public int updateCompanyAccountOther(Map<String, Object> input)
			throws Exception {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateCompanyAccountOther",input);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

}
