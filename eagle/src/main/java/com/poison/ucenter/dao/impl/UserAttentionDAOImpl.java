package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.keel.framework.runtime.ProductContext;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserAttentionDAO;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;

public class UserAttentionDAOImpl extends SqlMapClientDaoSupport implements UserAttentionDAO{

	private static final  Log LOG = LogFactory.getLog(UserAttentionDAOImpl.class);
	/**
	 * 查询用户关注的列表（我关注的人）
	 */
	@Override
	public List<UserAttention> findUserAttention(ProductContext productContext,long userId,int count,int pageIndex,int pageSize) {
		List<UserAttention> userAttentionList = new ArrayList<UserAttention>();
		
		Map<String, Object> attentionParameterMap = new HashMap<String, Object>();
		attentionParameterMap.put("userId", userId);
		attentionParameterMap.put("pageIndex", pageIndex);
		attentionParameterMap.put("pageSize", pageSize);
		UserAttention userAttention = new UserAttention();
		try{
			userAttentionList = getSqlMapClientTemplate().queryForList("findUserAttention", attentionParameterMap);
			if(null==userAttentionList||userAttentionList.size()==0){
				userAttentionList = new ArrayList<UserAttention>();
				userAttention.setFlag(ResultUtils.DATAISNULL);
				userAttentionList.add(userAttention);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userAttentionList = new ArrayList<UserAttention>();
			userAttention.setFlag(ResultUtils.ERROR);
			userAttentionList.add(userAttention);
		}
		return userAttentionList;
	}

	/**
	 * 查找我的粉丝（关注我的人）
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<UserAttention> findUserFens(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize) {
		List<UserAttention> userFensList = new ArrayList<UserAttention>();
		Map<String, Object> fensParameterMap = new HashMap<String, Object>();
		fensParameterMap.put("AttentionId", userAttentionId);
		fensParameterMap.put("pageIndex", pageIndex);
		fensParameterMap.put("pageSize", pageSize);
		UserAttention userAttention = new UserAttention();
		try{
			userFensList = getSqlMapClientTemplate().queryForList("findUserFens", fensParameterMap);
			if(null==userFensList){
				userFensList = new ArrayList<UserAttention>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userFensList = new ArrayList<UserAttention>();
			userAttention.setFlag(ResultUtils.ERROR);
			userFensList.add(userAttention);
		}
		return userFensList;
	}

	/**
	 * 添加关注
	 */
	@Override
	public int insertAttention(ProductContext productContext, UserAttention userAttention) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertAttention", userAttention);
		}catch (Exception e) {
			return ResultUtils.INSERT_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 更新关注
	 */
	@Override
	public int updateAttention(ProductContext productContext,  UserAttention userAttention) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().update("updateAttention", userAttention);
		}catch (Exception e) {
			return ResultUtils.UPDATE_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 用户是否存在此关注
	 */
	@Override
	public UserAttention findUserAttentionIsExist(
			ProductContext productContext, UserAttention userAttention) {
		UserAttention isExistUserAttention = new UserAttention();
		try{
			isExistUserAttention = (UserAttention) getSqlMapClientTemplate().queryForObject("findUserAttentionIsExist", userAttention);
			//不存在该关注信息
			if(null==isExistUserAttention){
				isExistUserAttention = new UserAttention();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			isExistUserAttention = new UserAttention();
			isExistUserAttention.setFlag(ResultUtils.QUERY_ERROR);
			return isExistUserAttention;
		}
		isExistUserAttention.setFlag(ResultUtils.SUCCESS);
		return isExistUserAttention;
	}

	/**
	 * 查询朋友关注的人
	 */
	@Override
	public List<Long> findUserAttentionList(List<Long> userIdList) {
		Map<String, List<Long>> userIdMap = new HashMap<String, List<Long>>();
		userIdMap.put("userIdList", userIdList);
		List<Long> attentionList = new ArrayList<Long>();
		//UserAttention userAttention = new UserAttention();
		try{
			attentionList = getSqlMapClientTemplate().queryForList("findUserAttentionList",userIdMap);
			/*if(null==attentionList){
				attentionList = new ArrayList<String>();
				userAttention.setFlag(ResultUtils.DATAISNULL);
				attentionList.add(userAttention);
			}*/
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			attentionList = new ArrayList<Long>();
			/*userAttention.setFlag(ResultUtils.ERROR);
			attentionList.add(userAttention);*/
		}
		return attentionList;
	}

	/**
	 * 查询用户关注的总人数
	 */
	@Override
	public int findUserAttentionCount(long userId) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findUserAttentionCount",userId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.QUERY_ERROR;
		}
		return i;
	}

	/**
	 * 查询用户的粉丝总数
	 */
	@Override
	public int findUserFensCount(long userId) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findUserFensCount",userId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.QUERY_ERROR;
		}
		return i;
	}

	/**
	 * 查询用户相互关注的列表
	 */
	@Override
	public List<UserAttention> findUserAttentionEachOther(long userId) {
		List<UserAttention> attentionEachOther = new ArrayList<UserAttention>();
		UserAttention userAttenton = new UserAttention();
		try{
			attentionEachOther = getSqlMapClientTemplate().queryForList("findUserAttentionEachOther",userId);
			if(null==attentionEachOther){
				attentionEachOther = new ArrayList<UserAttention>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			attentionEachOther = new ArrayList<UserAttention>();
			userAttenton.setFlag(ResultUtils.QUERY_ERROR);
			attentionEachOther.add(userAttenton);
		}
		return attentionEachOther;
	}

	/**
	 * 查询用户关注人的userid
	 */
	@Override
	public List<Long> findUserAttention(long userId,int pageIndex,int pageSize) {
		List<Long> userAttentionList = new ArrayList<Long>();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("userId", userId);
		parameterMap.put("pageIndex", pageIndex);
		parameterMap.put("pageSize", pageSize);
		try{
			userAttentionList = getSqlMapClientTemplate().queryForList("findAttentionList",parameterMap);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return userAttentionList;
	}

	/**
	 * 查询用户关注的达人信息
	 */
	@Override
	public List<UserAttention> findUserAttentionTalentedPersons(long userId,
			String type) {
		List<UserAttention> list = new ArrayList<UserAttention>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("type", type);
			list = getSqlMapClientTemplate().queryForList("findUserAttentionTalentedPersons",map);
			if(null==list||list.size()==0){
				list = new ArrayList<UserAttention>();
				UserAttention userAttention = new UserAttention();
				userAttention.setFlag(ResultUtils.DATAISNULL);
				list.add(userAttention);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<UserAttention>();
			UserAttention userAttention = new UserAttention();
			userAttention.setFlag(ResultUtils.QUERY_ERROR);
			list.add(userAttention);
		}
		return list;
	}

}
