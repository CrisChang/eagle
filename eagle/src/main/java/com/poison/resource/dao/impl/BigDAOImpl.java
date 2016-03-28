package com.poison.resource.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.resource.dao.BigDAO;
import com.poison.resource.model.Big;

public class BigDAOImpl extends SqlMapClientDaoSupport implements BigDAO{

	private static final  Log LOG = LogFactory.getLog(BigDAOImpl.class);
	/**
	 * 根据输入值查询big详情
	 */
	@Override
	public Big findBig(String attribute, String branch, String value) {
		Map<String, Object> map = new HashMap<String, Object>();
		Big big = new Big();
		try{
			map.put("attribute", attribute);
			map.put("branch", branch);
			map.put("branchDetail", value);
			big = (Big) getSqlMapClientTemplate().queryForObject("findBig",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return big;
	}

}
