package com.poison.ucenter.client.impl;

import com.poison.ucenter.client.ShenrenApplyFacade;
import com.poison.ucenter.model.ShenrenApply;
import com.poison.ucenter.service.ShenrenApplyService;

public class ShenrenApplyFacadeImpl implements ShenrenApplyFacade{

	private ShenrenApplyService shenrenApplyService;
	
	public void setShenrenApplyService(ShenrenApplyService shenrenApplyService) {
		this.shenrenApplyService = shenrenApplyService;
	}

	/**
	 * 
	 * <p>Title: insertintoShenrenApply</p> 
	 * <p>Description: 插入神人申请记录</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	@Override
	public boolean insertintoShenrenApply(long uid,String realname,String content,String mobileno,String sid,String proof){
		ShenrenApply shenrenApply = new ShenrenApply();
		shenrenApply.setUid(uid);
		shenrenApply.setRealname(realname);
		shenrenApply.setContent(content);
		shenrenApply.setMobileno(mobileno);
		shenrenApply.setSid(sid);
		shenrenApply.setProof(proof);
		shenrenApply.setApplytime(System.currentTimeMillis());
		shenrenApply.setUpdatetime(System.currentTimeMillis());
		shenrenApply.setIsDel(0);
		shenrenApply.setStatus(0);
		return shenrenApplyService.insertintoShenrenApply(shenrenApply);
	}
}