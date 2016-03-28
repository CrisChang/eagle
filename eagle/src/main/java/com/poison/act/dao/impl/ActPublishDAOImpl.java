package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActPublishDAO;
import com.poison.act.model.ActPublish;
import com.poison.eagle.utils.ResultUtils;

public class ActPublishDAOImpl extends SqlMapClientDaoSupport implements ActPublishDAO{

	private static final  Log LOG = LogFactory.getLog(ActPublishDAOImpl.class);
	@Override
	public int insertPublishInfo(ActPublish publish) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoActPublish",publish);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询发布列表ID
	 */
	@Override
	public List<ActPublish> findPublishList(Long resId) {
		List<ActPublish> resIdList = new ArrayList<ActPublish>();
		try{
			resIdList = getSqlMapClientTemplate().queryForList("findPublishList",resId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return resIdList;
	}

	/**
	 * 根据ID查询一条推荐信息
	 */
	@Override
	public List<ActPublish> findPublishById(long resId) {
		List<ActPublish> list = new ArrayList<ActPublish>();
		ActPublish publish = new ActPublish();
		try{
			list = getSqlMapClientTemplate().queryForList("findPublishById",resId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ActPublish>();
			publish.setFlag(ResultUtils.QUERY_ERROR);
			list.add(publish);
		}
		return list;
	}

	/**
	 * 更新一条发布信息
	 */
	@Override
	public int updatePublishById(ActPublish publish) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updatePublishInfo", publish);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询一条发布信息
	 */
	@Override
	public ActPublish findOnePublish(long id) {
		ActPublish actPublish = new ActPublish();
		try{
			actPublish = (ActPublish) getSqlMapClientTemplate().queryForObject("findOnePublish",id);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actPublish = new ActPublish();
			actPublish.setFlag(ResultUtils.QUERY_ERROR);
		}
		return actPublish;
	}

	/**
	 * 根据用户ID查询该用户的发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUid(long userId, Long resId) {
		List<ActPublish> publishList = new ArrayList<ActPublish>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			publishList = getSqlMapClientTemplate().queryForList("findPublishListByUid",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			publishList = new ArrayList<ActPublish>();
			ActPublish actPublish = new ActPublish();
			actPublish.setFlag(ResultUtils.QUERY_ERROR);
			publishList.add(actPublish);
		}
		return publishList;
	}

	/**
	 * 根据type查询发布的列表
	 */
	@Override
	public List<ActPublish> findPublishListByType(String type, Long resId) {
		List<ActPublish> publishList = new ArrayList<ActPublish>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("type", type);
			map.put("resId", resId);
			publishList = getSqlMapClientTemplate().queryForList("findPublishListByType",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			publishList = new ArrayList<ActPublish>();
			ActPublish actPublish = new ActPublish();
			actPublish.setFlag(ResultUtils.QUERY_ERROR);
			publishList.add(actPublish);
		}
		return publishList;
	}

	/**
	 * 根据用户id查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUsersId(String type, Long resId,
			List<Long> usersId) {
		List<ActPublish> publishList = new ArrayList<ActPublish>();
		ActPublish actPublish = new ActPublish();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("type", type);
			map.put("resId", resId);
			map.put("usersId", usersId);
			publishList = getSqlMapClientTemplate().queryForList("findPublishListByUsersId",map);
			if(null==publishList||publishList.size()==0){
				publishList = new ArrayList<ActPublish>();
				actPublish.setFlag(ResultUtils.QUERY_ERROR);
				publishList.add(actPublish);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			publishList = new ArrayList<ActPublish>();
			actPublish.setFlag(ResultUtils.ERROR);
			publishList.add(actPublish);
		}
		return publishList;
	}

	/**
	 * 查询推荐类型
	 */
	@Override
	public List<ActPublish> findPublishListByRecommendType(String recommendType) {
		List<ActPublish> publishList = new ArrayList<ActPublish>();
		ActPublish actPublish = new ActPublish();
		try{
			publishList = getSqlMapClientTemplate().queryForList("findPublishListByRecommendType",recommendType);
			if(null==publishList||publishList.size()==0){
				publishList = new ArrayList<ActPublish>();
				return publishList;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			publishList = new ArrayList<ActPublish>();
			actPublish.setFlag(ResultUtils.QUERY_ERROR);
			publishList.add(actPublish);
		}
		return publishList;
	}

	/**
	 * 根据id查询发布的总数
	 */
	@Override
	public Map<String, Object> findPublishCount(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findPublishCount",userId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}

	/**
	 * 根据用户删除一条发布的资源
	 */
	@Override
	public ActPublish findPublishByUidAndResIdAndType(long userId, long resId,
			String resType) {
		ActPublish actPublish = new ActPublish();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("resId", resId);
			map.put("resType", resType);
			actPublish = (ActPublish) getSqlMapClientTemplate().queryForObject("findPublishByUidAndResIdAndType",map);
			if(null==actPublish){
				actPublish = new ActPublish();
				actPublish.setFlag(ResultUtils.DATAISNULL);
				return actPublish;
			}
			actPublish.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actPublish = new ActPublish();
			actPublish.setFlag(ResultUtils.ERROR);
		}
		return actPublish;
	}

	/**
	 * 根据用户id查询用户的发布状态
	 */
	@Override
	public List<ActPublish> findPublishListByUserId(String type, Long resId,
			Long userId) {
		List<ActPublish> publishList = new ArrayList<ActPublish>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("resId", resId);
		map.put("resType", type);
		try{
			publishList = getSqlMapClientTemplate().queryForList("findPublishListByUserId",map);
			if(null==publishList||publishList.size()==0){
				publishList = new ArrayList<ActPublish>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			publishList = new ArrayList<ActPublish>();
		}
		return publishList;
	}

}
