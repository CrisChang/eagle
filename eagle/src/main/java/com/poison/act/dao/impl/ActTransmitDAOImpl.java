package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActTransmitDAO;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActTransmit;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.model.UserInfo;

public class ActTransmitDAOImpl extends SqlMapClientDaoSupport implements ActTransmitDAO{

	private static final  Log LOG = LogFactory.getLog(ActTransmitDAOImpl.class);
	/**
	 * 增加转发内容
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int insertTransimit(ActTransmit actTransmit) {
		@SuppressWarnings("unused")
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertintoActTransmit", actTransmit);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 查询用户转发列表
	 * 
	 */
	@Override
	public List<ActTransmit> findTransmit(long userId) {
		List<ActTransmit> transmitList = new ArrayList<ActTransmit>();
		ActTransmit actTransmit = new ActTransmit();
		try{
			transmitList = getSqlMapClientTemplate().queryForList("findActTransmit", userId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			transmitList = new ArrayList<ActTransmit>();
			actTransmit.setFlag(ResultUtils.ERROR);
			transmitList.add(actTransmit);
		}
		return transmitList;
	}

	/**
	 * 查找资源转发的总数
	 */
	@Override
	public int findTransmitCount(long resourceId) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findTransmitCount",resourceId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.QUERY_ERROR;
		}
		return i;
	}

	/**
	 * 根据多个userid查询
	 */
	@Override
	public List<ActTransmit> findTransmitListByUserId(List<Long> usersIdList) {
		List<ActTransmit> transmitList = new ArrayList<ActTransmit>();
		ActTransmit actTransmit = new ActTransmit();
		Map<String, List<Long>> transmitMap = new HashMap<String, List<Long>>();
		try{
			transmitMap.put("usersIdList", usersIdList);
			transmitList = getSqlMapClientTemplate().queryForList("findTransmitListByUsersId",transmitMap);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			transmitList = new ArrayList<ActTransmit>();
			actTransmit.setFlag(ResultUtils.QUERY_ERROR);
			transmitList.add(actTransmit);
		}
		return transmitList;
	}

	/**
	 * 查询转发的所有信息
	 */
	@Override
	public List<ActTransmit> findAllTransmitList(Long resId) {
		List<ActTransmit> list = new ArrayList<ActTransmit>();
		try{
			list = getSqlMapClientTemplate().queryForList("findAllTransmitList",resId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ActTransmit>();
			ActTransmit actTransmit = new ActTransmit();
			actTransmit.setFlag(ResultUtils.QUERY_ERROR);
			list.add(actTransmit);
		}
		return list;
	}

	/**
	 * 查询一条转发详情
	 */
	@Override
	public ActTransmit findOneTransmit(long id) {
		ActTransmit actTransmit = new ActTransmit();
		try{
			actTransmit = (ActTransmit) getSqlMapClientTemplate().queryForObject("findOneTransmit",id);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actTransmit = new ActTransmit();
			actTransmit.setFlag(ResultUtils.QUERY_ERROR);
		}
		return actTransmit;
	}

	/**
	 * 根据uid和type查询转发列表
	 */
	@Override
	public List<ActTransmit> findTransmitListByTypeAndUsersId(String type,
			Long resId, List<Long> usersId) {
		List<ActTransmit>  transmitList = new ArrayList<ActTransmit>();
		Map<String, Object> map = new HashMap<String, Object>();
		ActTransmit actTransmit =new ActTransmit();
		try{
			map.put("type", type);
			map.put("resId", resId);
			map.put("usersId", usersId);
			transmitList = getSqlMapClientTemplate().queryForList("findTransmitListByTypeAndUsersId",map);
			if(null==transmitList||transmitList.size()==0){
				transmitList = new ArrayList<ActTransmit>();
				actTransmit.setFlag(ResultUtils.DATAISNULL);
				transmitList.add(actTransmit);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			transmitList = new ArrayList<ActTransmit>();
			actTransmit.setFlag(ResultUtils.QUERY_ERROR);
			transmitList.add(actTransmit);
		}
		return transmitList;
	}

	/**
	 * 根据uid查询转发的总数
	 */
	@Override
	public Map<String, Object> findTransmitCountByUid(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findTransmitCountByUid",userId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map.put("count", count);
		map.put("flag", flag);
		return map;
	}

	
}
