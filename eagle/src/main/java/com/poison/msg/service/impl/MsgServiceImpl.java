package com.poison.msg.service.impl;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.poison.msg.domain.repository.MsgDomainRepository;
import com.poison.msg.model.MsgAt;
import com.poison.msg.service.MsgService;

public class MsgServiceImpl implements MsgService{

	private MsgDomainRepository msgDomainRepository;
	
	public void setMsgDomainRepository(MsgDomainRepository msgDomainRepository) {
		this.msgDomainRepository = msgDomainRepository;
	}

	/**
	 * 插入at信息
	 */
	@Override
	public MsgAt doAt(ProductContext productContext, MsgAt msgAt) {
		return msgDomainRepository.doAt(msgAt);
	}

	/**
	 * 查询用户的at列表
	 */
	@Override
	public List<MsgAt> findUserAtList(long userId, Long resId) {
		return msgDomainRepository.findUserAtList(userId, resId);
	}

}
