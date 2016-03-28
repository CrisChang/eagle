package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.BkInfoDAO;
import com.poison.store.model.BkInfo;

public class BkInfoDAOImpl extends SqlMapClientDaoSupport implements BkInfoDAO{

	private static final  Log LOG = LogFactory.getLog(BkInfoDAOImpl.class);
	
	@Override
	public BkInfo findBkInfo(int id) {
		BkInfo bkInfo = new BkInfo();
		try{
			bkInfo = (BkInfo) getSqlMapClientTemplate().queryForObject("findBkInfo", id);
			if(null!=bkInfo){
				bkInfo.setFlag(ResultUtils.SUCCESS);
			}else{
				bkInfo = new BkInfo();
				bkInfo.setFlag(ResultUtils.DATAISNULL);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkInfo = new BkInfo();
			bkInfo.setFlag(ResultUtils.ERROR);
		}
		return bkInfo;
	}

	/**
	 * 根据书名查询一本书的信息
	 */
	@Override
	public List<BkInfo> findBkInfoByName(String name) {
		List<BkInfo> BkList = new ArrayList<BkInfo>();
		BkInfo bkInfo = new BkInfo();
		try{
			BkList = getSqlMapClientTemplate().queryForList("findBkInfoByName", name);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkInfo = new BkInfo();
			bkInfo.setFlag(ResultUtils.ERROR);
		}
		return BkList;
	}

	/**
	 * 根据书名模糊查询
	 */
	@Override
	public List<BkInfo> findBkInfoByLikeName(String name) {
		List<BkInfo> BkList = new ArrayList<BkInfo>();
		BkInfo bkInfo = new BkInfo();
		try{
			BkList = getSqlMapClientTemplate().queryForList("findBkInfoByLikeName", name);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkInfo = new BkInfo();
			bkInfo.setFlag(ResultUtils.ERROR);
		}
		return BkList;
	}

	/**
	 * 插入一条信息
	 */
	@Override
	public int insertBkInfo(BkInfo bkInfo) {
		int flag = ResultUtils.ERROR;
		try{
			flag = (Integer)getSqlMapClientTemplate().insert("insertintoBkInfo",bkInfo);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 根据isbn查询书的详情
	 */
	@Override
	public List<BkInfo> findBkInfoByIsbn(String isbn) {
		List<BkInfo> bkInfoList = new ArrayList<BkInfo>();
		try{
			bkInfoList = getSqlMapClientTemplate().queryForList("findBkInfoByIsbn",isbn);
		}catch (Exception e) {
			BkInfo bkInfo = new BkInfo();
			bkInfoList = new ArrayList<BkInfo>();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkInfo = new BkInfo();
			bkInfo.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bkInfoList;
	}
	
	/**
	 * 根据bookurl查询书的详情
	 */
	@Override
	public List<BkInfo> findBkInfoBybookurl(String bookurl){
		List<BkInfo> bkInfoList = new ArrayList<BkInfo>();
		try{
			bkInfoList = getSqlMapClientTemplate().queryForList("findBkInfoBybookurl",bookurl);
		}catch (Exception e) {
			BkInfo bkInfo = new BkInfo();
			bkInfoList = new ArrayList<BkInfo>();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkInfo = new BkInfo();
			bkInfo.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bkInfoList;
	}
	
	/**
	 * 根据id集合查询书的信息集合
	 */
	@Override
	public List<BkInfo> findBkInfosByIds(long[] ids) {
		List<BkInfo> bklist=new ArrayList<BkInfo>();
		BkInfo bkInfo=new BkInfo();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String idstr = "0";
			if(ids!=null && ids.length>0){
				for(int i=0;i<ids.length;i++){
					idstr = idstr+","+ids[i];
				}
			}
			map.put("ids", idstr);
			bklist=getSqlMapClientTemplate().queryForList("findBkInfosByIds",map);
			if(bklist==null||bklist.size()==0){
				bklist=new ArrayList<BkInfo>();
				//bkInfo.setFlag(ResultUtils.DATAISNULL);
				//bklist.add(bkInfo);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bklist=new ArrayList<BkInfo>();
			bkInfo.setFlag(ResultUtils.QUERY_ERROR);
			bklist.add(bkInfo);
			return bklist;
		}
		return bklist;
	}

}
