package com.poison.msg.client.impl;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.keel.utils.UKeyWorker;
import com.poison.msg.client.MsgFacade;
import com.poison.msg.model.MsgAt;
import com.poison.msg.service.MsgService;

public class MsgFacadeImpl implements MsgFacade{

	private MsgService msgService;
	
	private UKeyWorker worker = new UKeyWorker(31L, 63L);

	public void setMsgService(MsgService msgService) {
		this.msgService = msgService;
	}

	/**
	 * 插入at信息
	 */
	@Override
	public MsgAt doAct(ProductContext productContext,long userId,long userAtId,long resourceId,String type) {
		long sysdate = System.currentTimeMillis();
		MsgAt msgAt = new MsgAt();
		msgAt.setAtId(worker.getId());
		msgAt.setUserId(userId);
		msgAt.setUserAtId(userAtId);
		msgAt.setResourceId(resourceId);
		msgAt.setIsDelete(0);
		msgAt.setIsRead(0);
		msgAt.setAtDate(sysdate);
		msgAt.setType(type);
		return msgService.doAt(productContext, msgAt);
	}

	/**
	 * 查询用户的at列表
	 */
	@Override
	public List<MsgAt> findUserAtList(long userId, Long resId) {
		return msgService.findUserAtList(userId, resId);
	}

}
