package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.MvInfoDAO;
import com.poison.store.model.MvInfo;

@SuppressWarnings("deprecation")
public class MvInfoDAOImpl extends SqlMapClientDaoSupport implements MvInfoDAO{

	private static final  Log LOG = LogFactory.getLog(MvInfoDAOImpl.class);
	/**
	 * 根据id查询影单信息
	 */
	@Override
	public MvInfo queryById(long id) {
		MvInfo mvInfo=new MvInfo();
		try {
			mvInfo=(MvInfo)getSqlMapClientTemplate().queryForObject("findMvInfo",id);
			if(null!=mvInfo){
				mvInfo.setFlag(ResultUtils.SUCCESS);
			}else{
				mvInfo=new MvInfo();
				mvInfo.setFlag(ResultUtils.DATAISNULL);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvInfo=new MvInfo();
			mvInfo.setFlag(ResultUtils.QUERY_ERROR);
			return mvInfo;
		}
		return mvInfo;
	}
	/**
	 * 根据movieUrl查询影单信息
	 */
	@Override
	public MvInfo queryByMvURL(String movieUrl){
		MvInfo mvInfo=new MvInfo();
		try {
			mvInfo=(MvInfo)getSqlMapClientTemplate().queryForObject("findMvURL",movieUrl);
			if(null==mvInfo){
				mvInfo=new MvInfo();
				mvInfo.setFlag(ResultUtils.DATAISNULL);
				return mvInfo;
			}
				mvInfo.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvInfo=new MvInfo();
			mvInfo.setFlag(ResultUtils.QUERY_ERROR);
			return mvInfo;
		}
		return mvInfo;
	}
	/**
	 * 根据名称模糊查找
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MvInfo> findMvInfoByLikeName(String name) {
		List<MvInfo> mvlist=new ArrayList<MvInfo>();
		MvInfo mvInfo=new MvInfo();
		try {
			mvlist=getSqlMapClientTemplate().queryForList("findMvInfoByLikeName",name);
			if(mvlist!=null){
				mvlist=new ArrayList<MvInfo>();
				mvInfo.setFlag(ResultUtils.SUCCESS);
				mvlist.add(mvInfo);
			}else{
				mvlist=new ArrayList<MvInfo>();
				mvInfo.setFlag(ResultUtils.DATAISNULL);
				mvlist.add(mvInfo);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvlist=new ArrayList<MvInfo>();
			mvInfo.setFlag(ResultUtils.QUERY_ERROR);
			mvlist.add(mvInfo);
			return mvlist;
		}
		return mvlist;
	}
	/**
	 * 根据名称精准匹配
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MvInfo> findMvInfoByName(MvInfo info) {
		List<MvInfo> mvlist=new ArrayList<MvInfo>();
		MvInfo mvInfo=new MvInfo();
		try {
			mvlist=getSqlMapClientTemplate().queryForList("findMvInfoByName",info);
			if(mvlist==null||mvlist.size()==0){
				mvlist=new ArrayList<MvInfo>();
				mvInfo.setFlag(ResultUtils.DATAISNULL);
				mvlist.add(mvInfo);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvlist=new ArrayList<MvInfo>();
			mvInfo.setFlag(ResultUtils.QUERY_ERROR);
			mvlist.add(mvInfo);
			return mvlist;
		}
		return mvlist;
	}
	
	/**
	 * 插入一条电影信息
	 */
	@Override
	public long insertMvInfo(MvInfo mvInfo) {
		long flag = ResultUtils.ERROR;
		try{
			flag = (Long)getSqlMapClientTemplate().insert("insertintoMvInfo", mvInfo);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			LOG.error(":error=>" + e.getMessage() +" :po=>" + mvInfo);
			flag = ResultUtils.QUERY_ERROR;
		}
		return flag;
	}
	
	/**
	 * 更新电影的简介
	 */
	@Override
	public void updateMvInfoDescribe(long id, String describe) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("id", id);
			map.put("describe", describe);
			getSqlMapClientTemplate().update("updateMvInfoDescribe",map);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新电影的简介
	 */
	@Override
	public void updateMvInfoReleaseDateSort(long id, long releaseDateSort) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("id", id);
			map.put("releaseDateSort", releaseDateSort);
			getSqlMapClientTemplate().update("updateMvInfoReleaseDateSort",map);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新电影演员信息
	 */
	@Override
	public void updateMvInfoActor(long id, String actor) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("id", id);
			map.put("actor", actor);
			getSqlMapClientTemplate().update("updateMvInfoActor",map);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据id集合查询电影信息集合
	 */
	@Override
	public List<MvInfo> findMvInfosByIds(long ids[]) {
		List<MvInfo> mvlist=new ArrayList<MvInfo>();
		MvInfo mvInfo=new MvInfo();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String idstr = "0";
			if(ids!=null && ids.length>0){
				for(int i=0;i<ids.length;i++){
					idstr = idstr+","+ids[i];
				}
			}
			map.put("ids", idstr);
			mvlist=getSqlMapClientTemplate().queryForList("findMvInfosByIds",map);
			if(mvlist==null||mvlist.size()==0){
				mvlist=new ArrayList<MvInfo>();
				//mvInfo.setFlag(ResultUtils.DATAISNULL);
				//mvlist.add(mvInfo);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			mvlist=new ArrayList<MvInfo>();
			mvInfo.setFlag(ResultUtils.QUERY_ERROR);
			mvlist.add(mvInfo);
			return mvlist;
		}
		return mvlist;
	}
}
