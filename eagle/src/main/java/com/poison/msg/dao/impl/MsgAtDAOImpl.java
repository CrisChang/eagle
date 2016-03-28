package com.poison.msg.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.msg.dao.MsgAtDAO;
import com.poison.msg.model.MsgAt;

public class MsgAtDAOImpl extends SqlMapClientDaoSupport implements MsgAtDAO{

	@Override
	public int insertMsgAt(MsgAt msgAt) {
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoMsgAt", msgAt);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			//i = ResultUtils.INSERT_ERROR;
		}
		return i;
	}

	/**
	 * 查询用户的at列表
	 */
	@Override
	public List<MsgAt> findUserAtList(long userId, Long resId) {
		List<MsgAt> atList = new ArrayList<MsgAt>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			atList = getSqlMapClientTemplate().queryForList("findAtList",map);
		}catch (Exception e) {
			e.printStackTrace();
			MsgAt msgAt = new MsgAt();
			msgAt.setFlag(ResultUtils.QUERY_ERROR);
			atList.add(msgAt);
		}
		return atList;
	}

	/**
	 * 根据id查询一个at信息
	 */
	@Override
	public MsgAt findMsgAtById(long atId) {
		MsgAt msgAt = new MsgAt();
		try{
			msgAt = (MsgAt) getSqlMapClientTemplate().queryForObject("findAtById",atId);
			if(null==msgAt){
				 msgAt = new MsgAt();
				 msgAt.setFlag(ResultUtils.DATAISNULL);
				 return msgAt;
			}
			msgAt.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			 msgAt = new MsgAt();
			 msgAt.setFlag(ResultUtils.QUERY_ERROR);
		}
		return msgAt;
	}

}
