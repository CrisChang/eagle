package com.poison.act.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActHotDAO;
import com.poison.act.model.ActHot;
import com.poison.eagle.utils.ResultUtils;

public class ActHotDAOImpl extends SqlMapClientDaoSupport implements ActHotDAO{

	private static final  Log LOG = LogFactory.getLog(ActHotDAOImpl.class);
	
	@Override
	public int insertHot(ActHot actHot) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoActHot",actHot);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	@Override
	public ActHot findActHotById(long id) {
		ActHot actHot = new ActHot();
		try{
			actHot = (ActHot) getSqlMapClientTemplate().queryForObject("findActHotById",id);
			if(null==actHot){
				actHot = new ActHot();
				actHot.setFlag(ResultUtils.DATAISNULL);
				return actHot;
			}
			actHot.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actHot = new ActHot();
			actHot.setFlag(ResultUtils.ERROR);
		}
		return actHot;
	}

	/**
	 * 查询用户是否加热过
	 */
	@Override
	public ActHot findIsHotByUserIdAndResIdType(long userId, long resourceId,
			String type) {
		ActHot actHot = new ActHot();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resourceId", resourceId);
			map.put("type", type);
			actHot = (ActHot) getSqlMapClientTemplate().queryForObject("findIsHotByUserIdAndResIdType",map);
			if(null==actHot){
				actHot = new ActHot();
				actHot.setFlag(ResultUtils.DATAISNULL);
				return actHot;
			}
			actHot.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actHot = new ActHot();
			actHot.setFlag(ResultUtils.ERROR);
		}
		return actHot;
	}

	/**
	 * 查询热度总数
	 */
	@Override
	public Map<String, Object> findHotCount(long resourceId, String type) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			map.put("resourceId", resourceId);
			map.put("type", type);
			count = (Integer) getSqlMapClientTemplate().queryForObject("findHotCountByResIdAndType",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		resultMap.put("count", count);
		resultMap.put("flag", flag);
		return resultMap;
	}


	/**
	 * 根据ip地址查询是否加热过
	 * @param ipAddress
	 * @param resourceId
	 * @param type
	 * @return
	 */
	@Override
	public ActHot findIsHotByIpAddressAndResIdType(String ipAddress, long resourceId, String type) {
		ActHot actHot = new ActHot();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("ipAddress", ipAddress);
			map.put("resourceId", resourceId);
			map.put("type", "7");
			actHot = (ActHot) getSqlMapClientTemplate().queryForObject("findIsHotByIpAddressAndResIdType",map);
			if(null==actHot){
				actHot = new ActHot();
				actHot.setFlag(ResultUtils.DATAISNULL);
				return actHot;
			}
			actHot.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actHot = new ActHot();
			actHot.setFlag(ResultUtils.ERROR);
		}
		return actHot;
	}
}
