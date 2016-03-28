package com.poison.ucenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserFeedBackDAO;
import com.poison.ucenter.model.UserFeedBack;

public class UserFeedBackDAOImpl extends SqlMapClientDaoSupport implements UserFeedBackDAO{

	private static final  Log LOG = LogFactory.getLog(UserFeedBackDAOImpl.class);
	
	/**
	 * 插入用户反馈信息
	 */
	@Override
	public int insertintoUserfeedback(UserFeedBack userFeedBack) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoUserfeedback",userFeedBack);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 查询用户反馈信息
	 */
	@Override
	public UserFeedBack findUserFeedBack(long id) {
		UserFeedBack userFeedBack = new UserFeedBack();
		try{
			userFeedBack = (UserFeedBack) getSqlMapClientTemplate().queryForObject("findUserfeedback",id);
			if(null==userFeedBack){
				userFeedBack = new UserFeedBack();
				userFeedBack.setFlag(ResultUtils.DATAISNULL);
				return userFeedBack;
			}
			userFeedBack.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userFeedBack = new UserFeedBack();
			userFeedBack.setFlag(ResultUtils.ERROR);
		}
		return userFeedBack;
	}

}
