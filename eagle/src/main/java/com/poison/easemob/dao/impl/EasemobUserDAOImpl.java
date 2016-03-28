package com.poison.easemob.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.easemob.dao.EasemobUserDAO;
import com.poison.easemob.model.EasemobUser;
import com.poison.ucenter.dao.impl.TalentZoneDAOImpl;

@SuppressWarnings("deprecation")
public class EasemobUserDAOImpl extends SqlMapClientDaoSupport implements EasemobUserDAO{

	private static final  Log LOG = LogFactory.getLog(EasemobUserDAOImpl.class);
	
	/**
	 * 插入环信用户
	 */
	@Override
	public int insertEasemobUser(EasemobUser easemobUser) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertEasemobUser",easemobUser);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据uid查询环信用户
	 */
	@Override
	public EasemobUser findEasemobUserByUid(long userId) {
		EasemobUser easemobUser = null;
		try{
			easemobUser = (EasemobUser) getSqlMapClientTemplate().queryForObject("findEasemobByUid",userId);
			if(null==easemobUser){
				easemobUser = new EasemobUser();
				easemobUser.setFlag(ResultUtils.DATAISNULL);
				return easemobUser;
			}
			easemobUser.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			easemobUser = new EasemobUser();
			easemobUser.setFlag(ResultUtils.QUERY_ERROR);
		}
		return easemobUser;
	}

}
