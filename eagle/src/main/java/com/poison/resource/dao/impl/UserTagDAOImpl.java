package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.UserTagDAO;
import com.poison.resource.model.UserTag;

public class UserTagDAOImpl extends SqlMapClientDaoSupport implements UserTagDAO{

	private static final  Log LOG = LogFactory.getLog(UserTagDAOImpl.class);
	/**
	 * 插入用户的标签
	 */
	@Override
	public int insertUserTag(UserTag userTag) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertUserTag",userTag);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 根据id查询标签
	 */
	@Override
	public UserTag queryUserTagById(long id) {
		UserTag userTag = new UserTag();
		try{
			userTag = (UserTag) getSqlMapClientTemplate().queryForObject("findUserTagById",id);
			if(null==userTag){
				userTag = new UserTag();
				userTag.setFlag(ResultUtils.DATAISNULL);
				return userTag;
			}
			userTag.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userTag = new UserTag();
			userTag.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userTag;
	}

	/**
	 * 根据标签名字去查询用户标签
	 */
	@Override
	public UserTag findUserTagByTagName(long userId, String tagName) {
		UserTag userTag = new UserTag();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("tagName", tagName);
			userTag =  (UserTag) getSqlMapClientTemplate().queryForObject("findUserTagByTagName",map);
			if(null==userTag){
				userTag = new UserTag();
				userTag.setFlag(ResultUtils.DATAISNULL);
				return userTag;
			}
			userTag.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userTag = new UserTag();
			userTag.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userTag;
	}

	/**
	 * 更新用户标签次数
	 */
	@Override
	public int updateUserTagCount(long id) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("id", id);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateUserTagCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询用户的历史记录
	 */
	@Override
	public List<UserTag> findUserHistoryTagListByUid(long userId) {
		List<UserTag> userTagList = new ArrayList<UserTag>();
		UserTag userTag = new UserTag();
		try{
			userTagList = getSqlMapClientTemplate().queryForList("findUserHistoryTagListByUid",userId);
			if(null==userTagList||userTagList.size()==0){
				userTagList = new ArrayList<UserTag>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userTagList = new ArrayList<UserTag>();
			userTag.setFlag(ResultUtils.ERROR);
			userTagList.add(userTag);
		}
		return userTagList;
	}

	/**
	 * 查询用户的常用标签
	 */
	@Override
	public List<UserTag> findUserFavoriteTagListByUid(long userId) {
		List<UserTag> userTagList = new ArrayList<UserTag>();
		UserTag userTag = new UserTag();
		try{
			userTagList = getSqlMapClientTemplate().queryForList("findUserFavoriteTagListByUid",userId);
			if(null==userTagList||userTagList.size()==0){
				userTagList = new ArrayList<UserTag>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userTagList = new ArrayList<UserTag>();
			userTag.setFlag(ResultUtils.ERROR);
			userTagList.add(userTag);
		}
		return userTagList;
	}

	/**
	 * 查询用户的标签列表
	 */
	@Override
	public List<UserTag> findUserTagList(long userId) {
		List<UserTag> list = new ArrayList<UserTag>();
		UserTag userTag = new UserTag();
		try{
			list = getSqlMapClientTemplate().queryForList("findUserTagList",userId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<UserTag>();
			userTag.setFlag(ResultUtils.QUERY_ERROR);
			list.add(userTag);
		}
		return list;
	}

	/**
	 * 删除用户标签
	 */
	@Override
	public int deleteUserTag(long id) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("deleteUserTagById",id);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.QUERY_ERROR;
		}
		return flag;
	}

	/**
	 * 更新次数和状态位
	 */
	@Override
	public int updateUserTagCountAndIsDel(long id) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("id", id);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateUserTagCountAndIsDel",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

}
