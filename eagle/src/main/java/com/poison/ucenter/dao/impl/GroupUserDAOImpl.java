package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.GroupUserDAO;
import com.poison.ucenter.model.GroupUser;

public class GroupUserDAOImpl extends SqlMapClientDaoSupport implements GroupUserDAO{

	private static final  Log LOG = LogFactory.getLog(GroupUserDAOImpl.class);
	
	/**
	 * 加入群组成员
	 */
	@Override
	public int insertintoGroupUser(GroupUser groupUser) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoGroupUser",groupUser);
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
	public GroupUser findGroupUserByGUid(String groupid,long uid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupid", groupid);
		map.put("uid", uid);
		GroupUser groupUser = null;
		try{
			groupUser = (GroupUser) getSqlMapClientTemplate().queryForObject("findGroupUserByGUid",map);
			if(null==groupUser){
				groupUser = new GroupUser();
				groupUser.setFlag(ResultUtils.DATAISNULL);
				return groupUser;
			}
			groupUser.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			groupUser = new GroupUser();
			groupUser.setFlag(ResultUtils.ERROR);
		}
		return groupUser;
	}
	/**
	 * 根据群组id查询
	 */
	@Override
	public List<GroupUser> findGroupUser(String groupid){
		List<GroupUser> list = new ArrayList<GroupUser>();
		try {
			list = getSqlMapClientTemplate().queryForList("findGroupUser",groupid);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<GroupUser>();
			GroupUser groupUser = new GroupUser();
			groupUser.setFlag(ResultUtils.QUERY_ERROR);
			list.add(groupUser);
			return list;
		}
		return list;
	}
	/**
	 * 根据用户id查询
	 */
	@Override
	public List<GroupUser> findGroupUserByUserid(Long uid){
		List<GroupUser> list = new ArrayList<GroupUser>();
		try {
			list = getSqlMapClientTemplate().queryForList("findGroupUserByUserid",uid);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<GroupUser>();
			GroupUser groupUser = new GroupUser();
			groupUser.setFlag(ResultUtils.QUERY_ERROR);
			list.add(groupUser);
			return list;
		}
		return list;
	}
	/**
	 * 删除一个群组成员
	 */
	@Override
	public int deleteGroupUser(GroupUser groupUser) {
		try {
			getSqlMapClientTemplate().update("deleteGroupUser", groupUser);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
}
