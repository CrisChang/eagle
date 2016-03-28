package com.poison.ucenter.domain.repository;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.ShenrenApplyDAO;
import com.poison.ucenter.model.ShenrenApply;

public class ShenrenApplyDomainRepository {
	
	private ShenrenApplyDAO shenrenApplyDAO;

	public ShenrenApplyDAO getShenrenApplyDAO() {
		return shenrenApplyDAO;
	}
	public void setShenrenApplyDAO(ShenrenApplyDAO shenrenApplyDAO) {
		this.shenrenApplyDAO = shenrenApplyDAO;
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
	public boolean insertintoShenrenApply(ShenrenApply shenrenApply){
		int flag = shenrenApplyDAO.insertintoShenrenApply(shenrenApply);
		if(flag == ResultUtils.SUCCESS){
			return true;
		}
		return false;
	}
}
