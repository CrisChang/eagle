package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.keel.framework.runtime.ProductContext;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserSummaryDAO;
import com.poison.ucenter.model.UserSummary;

@SuppressWarnings("deprecation")
public class UserSummaryDAOImpl extends SqlMapClientDaoSupport implements UserSummaryDAO{

	private static final  Log LOG = LogFactory.getLog(UserSummaryDAOImpl.class);
	/**
	 * 插入用户个人简介
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int insertUserSummary(ProductContext productContext,
			UserSummary userSummary) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertintoUserSummary",userSummary);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 修改用户个人简介
	 */
	@Override
	public int updateUserSummary(ProductContext productContext,
			UserSummary userSummary) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().update("updateUserSummary",userSummary);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 查询用户个人简介
	 */
	@Override
	public UserSummary findUserSummaryById(ProductContext productContext,
			long userId) {
		UserSummary userSummary = new UserSummary();
		try{
			userSummary = (UserSummary) getSqlMapClientTemplate().queryForObject("findUserSummary",userId);
			if(null!=userSummary){
				userSummary.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userSummary = new UserSummary();
		}
		return userSummary;
	}
	
	/**
	 * 根据用户id集合查询用户的简介信息 
	 */
	@Override
	public List<UserSummary> findUserSummarysByUserids(ProductContext productContext,List<Long> userids) {
		List<UserSummary> userSummarys = new ArrayList<UserSummary>();
		UserSummary userSummary = new UserSummary();
		if(null == userids||userids.size()==0){
			return userSummarys;
		}
		Map<String, List<Long>> map = new HashMap<String, List<Long>>();
		map.put("useridList", userids);
		try{
			userSummarys = getSqlMapClientTemplate().queryForList("findUserSummarysByUserids",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userSummarys = new ArrayList<UserSummary>();
			userSummary.setFlag(ResultUtils.QUERY_ERROR);
			userSummarys.add(userSummary);
		}
		return userSummarys;
	}
}