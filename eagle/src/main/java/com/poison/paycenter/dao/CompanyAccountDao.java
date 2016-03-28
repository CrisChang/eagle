package com.poison.paycenter.dao;

import java.util.List;
import java.util.Map;

import com.poison.paycenter.model.CompanyAccount;

public interface CompanyAccountDao {

	/**
	 * 查询公司账户大于0的公司账户列表
	 * @return
	 * @throws Exception 
	 */
	public List<CompanyAccount> selectCompanyAccountList(int totalAmount, int limitAmount, long startDate, long endDate) throws Exception;

	/**
	 * @param companyId
	 * @return
	 */
	public CompanyAccount findCompanyAccount(long companyId);

	/**
	 * @param companyAccount
	 * @return
	 */
	public int updateCompanyAccount(CompanyAccount companyAccount);
	
	/**
	 * @param comAccMap
	 * @return
	 */
	public int updateCompanyAccount(Map<String, Object> comAccMap);

	/**
	 * @param limitAmount
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	public List<CompanyAccount> selectCompanyAccountList1(int limitAmount,
			long endDate, long startDate) throws Exception;

	/**
	 * @param companyAccount1
	 * @return
	 */
	public int updateCompanyAccountOther(Map<String, Object> input) throws Exception;

	public List<CompanyAccount> selectCompanyAccountList1(long lastDate) throws Exception;
}
