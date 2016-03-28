package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.GroupDAO;
import com.poison.ucenter.model.Group;

public class GroupDAOImpl extends SqlMapClientDaoSupport implements GroupDAO{

	private static final  Log LOG = LogFactory.getLog(GroupDAOImpl.class);
	
	/**
	 * 插入群组
	 */
	@Override
	public int insertintoGroup(Group group) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoGroup",group);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 查询群组信息根据群组id
	 */
	@Override
	public Group findGroup(String groupid){
		Group group = new Group();
		try{
			group = (Group) getSqlMapClientTemplate().queryForObject("findGroup",groupid);
			if(null==group){
				group = new Group();
				group.setFlag(ResultUtils.DATAISNULL);
				return group;
			}
			group.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			group = new Group();
			group.setFlag(ResultUtils.ERROR);
		}
		return group;
	}
	
	/**
	 * 根据用户id查询
	 */
	@Override
	public List<Group> findGroupsByUserid(Long uid){
		List<Group> list = new ArrayList<Group>();
		try {
			list = getSqlMapClientTemplate().queryForList("findGroupsByUserid",uid);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<Group>();
			Group group = new Group();
			group.setFlag(ResultUtils.QUERY_ERROR);
			list.add(group);
			return list;
		}
		return list;
	}
	
	/**
	 * 根据多个群组id查询
	 */
	@Override
	public List<Group> findGroupsByIds(List<String> groupidList){
		List<Group> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupidList", groupidList);
		try {
			list = getSqlMapClientTemplate().queryForList("findGroupsByIds",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<Group>();
			Group group = new Group();
			group.setFlag(ResultUtils.QUERY_ERROR);
			list.add(group);
			return list;
		}
		return list;
	}
	
	@Override
	public int updateGroup(Group group) {
		try {
			getSqlMapClientTemplate().update("updateGroup", group);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public int deleteGroup(Group group) {
		try {
			getSqlMapClientTemplate().update("deleteGroup", group);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
}
