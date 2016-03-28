package com.poison.ucenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserLatestDAO;
import com.poison.ucenter.model.UserLatest;

public class UserLatestDAOImpl extends SqlMapClientDaoSupport implements UserLatestDAO{

	private static final  Log LOG = LogFactory.getLog(UserLatestDAOImpl.class);

	/**
	 * 插入一条信息 
	 */
	@Override
	public int insertUserlatest(UserLatest userlatest){
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertUserlatest", userlatest);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.ERROR;
		}
		return i;
	}
	/**
	 * 修改推送时间
	 */
	@Override
	public int updateUserlatest(UserLatest userlatest) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateUserlatest",userlatest);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}
	/**
	 * 根据userid查询
	 */
	@Override
	public UserLatest findUserlatestByUserid(long userid) {
		UserLatest userLatest = null;
		try{
			userLatest = (UserLatest) getSqlMapClientTemplate().queryForObject("findUserlatestByUserid",userid);
			if(null!=userLatest){
				userLatest.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userLatest = new UserLatest();
			userLatest.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userLatest;
	}
}
