package com.poison.ucenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.ShenrenApplyDAO;
import com.poison.ucenter.model.ShenrenApply;

public class ShenrenApplyDAOImpl extends SqlMapClientDaoSupport implements ShenrenApplyDAO{

	private static final  Log LOG = LogFactory.getLog(ShenrenApplyDAOImpl.class);
	
	/**
	 * 插入群组
	 */
	@Override
	public int insertintoShenrenApply(ShenrenApply shenrenApply) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoShenrenApply",shenrenApply);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
}
