package com.poison.resource.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.SerializeListDAO;
import com.poison.resource.model.SerializeList;

public class SerializeListDAOImpl extends SqlMapClientDaoSupport implements SerializeListDAO{

	private static final  Log LOG = LogFactory.getLog(SerializeListDAOImpl.class);
	/**
	 * 新建一个连载的清单
	 */
	@Override
	public int addSerializeList(SerializeList SerializeList) {
		int flag = ResultUtils.INSERT_ERROR;
		try{
			getSqlMapClientTemplate().insert("insertserializelist",SerializeList);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 根据id查询连载清单
	 */
	@Override
	public SerializeList findSerializeListById(long id) {
		SerializeList serializeList = new SerializeList();
		try{
			serializeList = (SerializeList) getSqlMapClientTemplate().queryForObject("findSerializeListById",id);
			if(null == serializeList){
				serializeList = new SerializeList();
				serializeList.setFlag(ResultUtils.DATAISNULL);
				return serializeList;
			}
			serializeList.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			serializeList = new SerializeList();
			serializeList.setFlag(ResultUtils.QUERY_ERROR);
		}
		return serializeList;
	}

	/**
	 * 更新连载清单
	 */
	@Override
	public int updateSerializeList(SerializeList serializeList) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateSerializeList",serializeList);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

}
