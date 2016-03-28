package com.poison.msg.dao;

import java.util.List;

import com.poison.msg.model.MsgAt;

public interface MsgAtDAO {

	/**
	 * 
	 * <p>Title: insertMsgAt</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午10:13:26
	 * @param msgAt
	 * @return
	 */
	public int insertMsgAt(MsgAt msgAt);
	
	/**
	 * 
	 * <p>Title: findUserAtList</p> 
	 * <p>Description: 查询用户的at表</p> 
	 * @author :changjiang
	 * date 2014-9-4 下午6:41:05
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MsgAt> findUserAtList(long userId,Long resId);
	
	/**
	 * 
	 * <p>Title: findMsgAtById</p> 
	 * <p>Description: 根据id查询一个at</p> 
	 * @author :changjiang
	 * date 2014-9-5 下午2:07:49
	 * @param atId
	 * @return
	 */
	public MsgAt findMsgAtById(long atId);
}
