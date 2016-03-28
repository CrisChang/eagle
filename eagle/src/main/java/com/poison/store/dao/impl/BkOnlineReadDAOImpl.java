package com.poison.store.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.BkOnlineReadDAO;
import com.poison.store.model.OnlineRead;

public class BkOnlineReadDAOImpl extends SqlMapClientDaoSupport implements BkOnlineReadDAO{

	private static final  Log LOG = LogFactory.getLog(BkOnlineReadDAOImpl.class);
	/**
	 * 插入试读
	 */
	@Override
	public int insertBkOnlineRead(OnlineRead read) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertintoBkOnLineRead",read);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	/**
	 * 查询一个书的试读
	 */
	@Override
	public OnlineRead findOnlineReadByBkId(int bkId,String resType) {
		OnlineRead read = new OnlineRead();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bkId", bkId);
			map.put("resType", resType);
			read = (OnlineRead) getSqlMapClientTemplate().queryForObject("findOnlineReadByBkId",map);
			if(null==read){
				read = new OnlineRead();
				read.setFlag(ResultUtils.DATAISNULL);
				return read;
			}
			read.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			read = new OnlineRead();
			read.setFlag(ResultUtils.QUERY_ERROR);
		}
		return read;
	}

	/**
	 * 更新试读
	 */
	@Override
	public int updateBkOnLineRead(OnlineRead read) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateBkOnLineRead",read);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}
	
}
