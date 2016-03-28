package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MyBkDAO;
import com.poison.resource.model.MyBk;

public class MyBkDAOImpl extends SqlMapClientDaoSupport implements MyBkDAO{

	private static final  Log LOG = LogFactory.getLog(MyBkDAOImpl.class);
	
	@Override
	public int insertMyBk(MyBk myBk) {
		int flag = ResultUtils.ERROR;
		Object obj = new Object();
		try{
			flag = (Integer)getSqlMapClientTemplate().insert("insertMyBk", myBk);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询用户添加的书是否存在
	 */
	@Override
	public MyBk findMyBkIsExist(MyBk myBk) {
		MyBk bk = new MyBk();
		try{
			bk = (MyBk) getSqlMapClientTemplate().queryForObject("findBookLinkIsExistByUser",myBk);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bk = new MyBk();
			bk.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bk;
	}

	/**
	 * 查询自己库中的书
	 */
	@Override
	public List<MyBk> findMyBkList(long userId, String name) {
		List<MyBk> myBkList = new ArrayList<MyBk>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("name", name);
		try{
			myBkList = getSqlMapClientTemplate().queryForList("findMyBkList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			myBkList = new ArrayList<MyBk>();
			MyBk myBk = new MyBk();
			myBk.setFlag(ResultUtils.QUERY_ERROR);
			myBkList.add(myBk);
		}
		return myBkList;
	}

	/**
	 * 模糊查询自己库中的书
	 */
	@Override
	public List<MyBk> findLikeMyBkList(long userId, String name) {
		List<MyBk> myBkList = new ArrayList<MyBk>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("name", name);
		try{
			myBkList = getSqlMapClientTemplate().queryForList("findLikeMyBkList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			myBkList = new ArrayList<MyBk>();
			MyBk myBk = new MyBk();
			myBk.setFlag(ResultUtils.QUERY_ERROR);
			myBkList.add(myBk);
		}
		return myBkList;
	}

	/**
	 * 查询自己库中这本书的详情
	 */
	@Override
	public MyBk findMyBkInfo(int id) {
		MyBk myBk = new MyBk();
		try{
			myBk = (MyBk) getSqlMapClientTemplate().queryForObject("findMyBkInfo",id);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			myBk = new MyBk();
			myBk.setFlag(ResultUtils.QUERY_ERROR);
		}
		return myBk;
	}

}
