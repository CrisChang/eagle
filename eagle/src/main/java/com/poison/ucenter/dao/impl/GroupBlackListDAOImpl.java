package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.GroupBlackListDAO;
import com.poison.ucenter.model.GroupBlackList;

public class GroupBlackListDAOImpl extends SqlMapClientDaoSupport implements GroupBlackListDAO{

	private static final  Log LOG = LogFactory.getLog(GroupBlackListDAOImpl.class);
	
	/**
	 * 加入群组黑名单
	 */
	@Override
	public int insertintoGroupBlacklist(GroupBlackList groupBlackList) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoGroupBlacklist",groupBlackList);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据群组id和成员id查询
	 */
	@Override
	public GroupBlackList findGroupBlacklistByGUid(String groupid,long uid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupid", groupid);
		map.put("uid", uid);
		GroupBlackList groupBlackList = null;
		try{
			groupBlackList = (GroupBlackList) getSqlMapClientTemplate().queryForObject("findGroupBlacklistByGUid",map);
			if(null==groupBlackList){
				groupBlackList = new GroupBlackList();
				groupBlackList.setFlag(ResultUtils.DATAISNULL);
				return groupBlackList;
			}
			groupBlackList.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			groupBlackList = new GroupBlackList();
			groupBlackList.setFlag(ResultUtils.ERROR);
		}
		return groupBlackList;
	}
	/**
	 * 根据群组id和成员id查询
	 */
	@Override
	public List<GroupBlackList> findGroupBlacklist(String groupid){
		List<GroupBlackList> list = new ArrayList<GroupBlackList>();
		try {
			list = getSqlMapClientTemplate().queryForList("findGroupBlacklist",groupid);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<GroupBlackList>();
			GroupBlackList groupUser = new GroupBlackList();
			groupUser.setFlag(ResultUtils.QUERY_ERROR);
			list.add(groupUser);
			return list;
		}
		return list;
	}
	/**
	 * 删除一个群组黑名单
	 */
	@Override
	public int deleteGroupBlacklist(GroupBlackList groupBlackList) {
		try {
			getSqlMapClientTemplate().update("deleteGroupBlacklist", groupBlackList);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
}