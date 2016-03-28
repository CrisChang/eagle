package com.poison.easemob.domain.repository;

import com.poison.eagle.utils.ResultUtils;
import com.poison.easemob.dao.EasemobUserDAO;
import com.poison.easemob.model.EasemobUser;

public class EasemobUserDomainRepository {
	
	private EasemobUserDAO easemobUserDAO;

	public void setEasemobUserDAO(EasemobUserDAO easemobUserDAO) {
		this.easemobUserDAO = easemobUserDAO;
	}
	
	/**
	 * 
	 * <p>Title: insertEasemobUser</p> 
	 * <p>Description: 插入环信用户</p> 
	 * @author :changjiang
	 * date 2014-12-26 下午3:37:24
	 * @param easemobUser
	 * @return
	 */
	public EasemobUser insertEasemobUser(EasemobUser easemobUser){
		long userId = easemobUser.getUserId();
		EasemobUser easUser = easemobUserDAO.findEasemobUserByUid(userId);
		int flag = easUser.getFlag();
		if(ResultUtils.DATAISNULL== flag){
			flag = easemobUserDAO.insertEasemobUser(easemobUser);
			easUser = easemobUserDAO.findEasemobUserByUid(userId);
		}
		return easUser;
	}
	/**
	 * 
	 * <p>Title: findEasemobUserByUid</p> 
	 * <p>Description: 根据uid查询环信用户</p> 
	 * @author :changjiang
	 * date 2014-12-26 下午3:25:07
	 * @param userId
	 * @return
	 */
	public EasemobUser findEasemobUserByUid(long userId){
		return easemobUserDAO.findEasemobUserByUid(userId);
	}
}
