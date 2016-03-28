package com.poison.product.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.product.dao.AccGoldDao;
import com.poison.product.model.AccGold;
import com.poison.resource.model.Topic;

public class AccGoldDAOImpl extends SqlMapClientDaoSupport implements AccGoldDao{

	private static final  Log LOG = LogFactory.getLog(AccGoldDAOImpl.class);
	/**
	 *  插入一个金币账户信息
	 */
	@Override
	public int insertIntoAccGold(AccGold accGold){
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertIntoAccGold", accGold);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.ERROR;
		}
		return i;
	}

	/**
	 *  根据用户id查询账户信息
	 */
	@Override
	public AccGold findAccGoldByUserId(long userId){
		AccGold gccGold = null;
		try{
			gccGold = (AccGold) getSqlMapClientTemplate().queryForObject("findAccGoldByUserId",userId);
			if(gccGold!=null){
				gccGold.setFlag(ResultUtils.SUCCESS);
			}else{
				gccGold = new AccGold();
				gccGold.setFlag(ResultUtils.DATAISNULL);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			gccGold = new AccGold();
			gccGold.setFlag(ResultUtils.QUERY_ERROR);
		}
		return gccGold;
	}
	/**
	 *  更新账户金币金额根据用户id
	 */
	@Override
	public int updateAccGoldByUserId(AccGold accGold){
		int flag =ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateAccGoldByUserId",accGold);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag =ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}
	
	/**
	 * 根据用户id增加金币额
	 */
	@Override
	public int addAccGoldByUserId(AccGold accGold) {
		int flag =ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("addAccGoldByUserId",accGold);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag =ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 根据用户id减少金币额
	 */
	@Override
	public int reduceAccGoldByUserId(AccGold accGold) {
		int flag =ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("reduceAccGoldByUserId",accGold);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag =ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}
}
