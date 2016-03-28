package com.poison.msg.domain.repository;

import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.msg.dao.MsgAtDAO;
import com.poison.msg.model.MsgAt;

public class MsgDomainRepository {

	private MsgAtDAO msgAtDao;

	public void setMsgAtDao(MsgAtDAO msgAtDao) {
		this.msgAtDao = msgAtDao;
	}
	
	/**
	 * 
	 * <p>Title: doAt</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午10:21:49
	 * @param msgAt
	 * @return
	 */
	public MsgAt doAt(MsgAt msgAt){
		int flag = msgAtDao.insertMsgAt(msgAt);
		long id = msgAt.getAtId();
		MsgAt at = new MsgAt();
		if(ResultUtils.ERROR==flag){
			at.setFlag(flag);
			return at;
		}
		at = msgAtDao.findMsgAtById(id);
		return at;
	}
	
	/**
	 * 
	 * <p>Title: findUserAtList</p> 
	 * <p>Description: 查询用户at列表</p> 
	 * @author :changjiang
	 * date 2014-9-4 下午6:44:54
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MsgAt> findUserAtList(long userId, Long resId){
		return msgAtDao.findUserAtList(userId, resId);
	}
}
