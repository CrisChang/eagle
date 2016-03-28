package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.resource.dao.SensitiveWordDAO;

/**
 * @author Administrator
 *
 */
public class SensitiveWordDAOImpl extends SqlMapClientDaoSupport implements SensitiveWordDAO {

	/* (non-Javadoc)
	 * @see com.poison.resource.dao.SensitiveWordDAO#selectSensitiveWord()
	 */
	@Override
	public List<String> selectSensitiveWord(Map<String, Object> map) {
		List<String> sensitiveWordList = new ArrayList<String>();
		sensitiveWordList = getSqlMapClientTemplate().queryForList("selectAllSensitiveName", map);
		return sensitiveWordList;
	}
}
