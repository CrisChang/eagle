package com.poison.msg.service;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.poison.msg.model.MsgAt;

public interface MsgService {

	/**
	 * 
	 * <p>Title: doAt</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午10:22:31
	 * @param msgAt
	 * @return
	 */
	public MsgAt doAt(ProductContext productContext,MsgAt msgAt);
	
	/**
	 * 
	 * <p>Title: findUserAtList</p> 
	 * <p>Description: 查询用户的at列表</p> 
	 * @author :changjiang
	 * date 2014-9-4 下午6:45:53
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MsgAt> findUserAtList(long userId, Long resId);
}
