package com.poison.msg.client;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.poison.msg.model.MsgAt;

public interface MsgFacade {

	/**
	 * 
	 * <p>Title: doAct</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午10:25:38
	 * @param msgAt
	 * @return
	 */
	public MsgAt doAct(ProductContext productContext,long userId,long userAtId,long resourceId,String type);
	
	/**
	 * 
	 * <p>Title: findUserAtList</p> 
	 * <p>Description: 查询用户的at列表</p> 
	 * @author :changjiang
	 * date 2014-9-4 下午6:47:58
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MsgAt> findUserAtList(long userId, Long resId);
}
