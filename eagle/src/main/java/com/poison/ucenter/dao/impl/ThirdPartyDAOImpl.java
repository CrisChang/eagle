package com.poison.ucenter.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.ThirdPartyDAO;
import com.poison.ucenter.model.ThirdPartyLogin;

public class ThirdPartyDAOImpl extends SqlMapClientDaoSupport implements ThirdPartyDAO{

	private static final  Log LOG = LogFactory.getLog(ThirdPartyDAOImpl.class);
	/**
	 * 插入第三方
	 */
	@Override
	public int insertThirdParty(ThirdPartyLogin thirdParty) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoThirdParty",thirdParty);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}
	
	/**
	 * 根据openid和登录信息查询用户
	 */
	@Override
	public ThirdPartyLogin findThirdPartyByOpenIdAndLoginResource(
			String openId, String loginResource) {
		ThirdPartyLogin thirdPartyLogin = new ThirdPartyLogin();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("openId", openId);
			map.put("loginResource", loginResource);
			thirdPartyLogin = (ThirdPartyLogin) getSqlMapClientTemplate().queryForObject("findThirdPartyByOpenIdAndLoginSource",map);
			if(null==thirdPartyLogin){
				thirdPartyLogin = new ThirdPartyLogin();
				thirdPartyLogin.setFlag(ResultUtils.DATAISNULL);
				return thirdPartyLogin;
			}
			thirdPartyLogin.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			thirdPartyLogin = new ThirdPartyLogin();
			thirdPartyLogin.setFlag(ResultUtils.QUERY_ERROR);
		}
		return thirdPartyLogin;
	}

	/**
	 * 更新第三方信息
	 */
	@Override
	public int updateThirdParty(ThirdPartyLogin thirdParty) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateThirdParty",thirdParty);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

}
