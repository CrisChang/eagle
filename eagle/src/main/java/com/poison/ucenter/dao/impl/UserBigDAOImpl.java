package com.poison.ucenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserBigDAO;
import com.poison.ucenter.model.UserBigValue;

public class UserBigDAOImpl extends SqlMapClientDaoSupport implements UserBigDAO{

	private static final  Log LOG = LogFactory.getLog(UserBigDAOImpl.class);
	/**
	 * 插入用户的逼格值
	 */
	@Override
	public int insertintoUserBigValue(UserBigValue userBigValue) {
		int flag = ResultUtils.INSERT_ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoUserBigValue",userBigValue);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 修改用户的逼格值
	 */
	@Override
	public int updateUserBigValue(UserBigValue userBigValue) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateUserBigValue",userBigValue);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.SUCCESS;
		}
		return flag;
	}

	/**
	 * 查找用户的逼格值
	 */
	@Override
	public UserBigValue findUserBigValue(long userId) {
		UserBigValue userBig = new UserBigValue();
		try{
			userBig = (UserBigValue) getSqlMapClientTemplate().queryForObject("findUserBigValue",userId);
			if(null == userBig){
				userBig = new UserBigValue();
				userBig.setFlag(ResultUtils.DATAISNULL);
				return userBig;
			}
			userBig.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userBig = new UserBigValue();
			userBig.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userBig;
	}

	/**
	 * 查询超出多少人
	 */
	@Override
	public float findUserBigBeyondCount(float selfTest) {
		float beyondCount = (Float) getSqlMapClientTemplate().queryForObject("findUserBigBeyondCount",selfTest);
		return beyondCount;
	}

	/**
	 * 查询一共多少人
	 */
	@Override
	public float findUserBigCount() {
		float count =  (Float) getSqlMapClientTemplate().queryForObject("findUserBigCount");
		return count;
	}

}
