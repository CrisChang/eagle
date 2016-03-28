package com.poison.act.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActWeixinUserDAO;
import com.poison.act.model.ActWeixinUser;
import com.poison.eagle.utils.ResultUtils;

public class ActWeixinUserDAOImpl extends SqlMapClientDaoSupport implements ActWeixinUserDAO{

	private static final  Log LOG = LogFactory.getLog(ActWeixinUserDAOImpl.class);
	/**
	 * 插入用户信息
	 */
	@Override
	public int insertUser(ActWeixinUser actWeixinUser) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertintoActWeixinUser", actWeixinUser);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	@Override
	public long findCountByScore(int score) {
		long i = -1;
		try{
			i = (Long) getSqlMapClientTemplate().queryForObject("findCountByScore",score);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}

	
	@Override
	public long findUserCount() {
		long i = -1;
		try{
			i = (Long) getSqlMapClientTemplate().queryForObject("findUserCount");
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}
	
	@Override
	public ActWeixinUser findUserById(String openid) {
		ActWeixinUser actWeixinUser = new ActWeixinUser();
		try{
			actWeixinUser = (ActWeixinUser) getSqlMapClientTemplate().queryForObject("findUserById", openid);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actWeixinUser.setFlag(ResultUtils.QUERY_ERROR);
		}
		return actWeixinUser;
	}
	/**
	 * 更新用户信息
	 */
	@Override
	public int updateUser(ActWeixinUser actWeixinUser) {
		int i = ResultUtils.ERROR;
		try{
			i=getSqlMapClientTemplate().update("updateUser", actWeixinUser);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.UPDATE_ERROR;
		}
		return i;
	}
}