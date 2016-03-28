package com.poison.ucenter.client;


public interface ShenrenApplyFacade {

	/**
	 * 
	 * <p>Title: insertintoShenrenApply</p> 
	 * <p>Description: 插入神人申请记录</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public boolean insertintoShenrenApply(long uid,String realname,String content,String mobileno,String sid,String proof);
}
