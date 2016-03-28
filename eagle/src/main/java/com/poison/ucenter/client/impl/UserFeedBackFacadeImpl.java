package com.poison.ucenter.client.impl;

import com.keel.utils.UKeyWorker;
import com.poison.ucenter.client.UserFeedBackFacade;
import com.poison.ucenter.model.UserFeedBack;
import com.poison.ucenter.service.UserFeedBackService;

public class UserFeedBackFacadeImpl implements UserFeedBackFacade{

	private UserFeedBackService userFeedBackService;
	private UKeyWorker reskeyWork;
	
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setUserFeedBackService(UserFeedBackService userFeedBackService) {
		this.userFeedBackService = userFeedBackService;
	}

	/**
	 * 插入用户反馈
	 */
	@Override
	public UserFeedBack insertintoUserfeedback(long userId,long resourceId,String resType,String description,String otherInfo) {
		UserFeedBack userFeedBack = new UserFeedBack();
		userFeedBack.setId(reskeyWork.getId());
		userFeedBack.setUserId(userId);
		userFeedBack.setResourceId(resourceId);
		userFeedBack.setResType(resType);
		userFeedBack.setDescription(description);
		userFeedBack.setOtherInfo(otherInfo);
		long sysdate = System.currentTimeMillis();
		userFeedBack.setCreateDate(sysdate);
		userFeedBack.setLatestRevisionDate(sysdate);
		return userFeedBackService.insertintoUserfeedback(userFeedBack);
	}

}
