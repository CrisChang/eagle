package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ResLinkDAO;
import com.poison.resource.model.ResourceLink;

public class ResLinkDAOImpl extends SqlMapClientDaoSupport implements ResLinkDAO{

	private static final  Log LOG = LogFactory.getLog(ResLinkDAOImpl.class);
	/**
	 * 根据type查询资源列表
	 */
	@Override
	public List<ResourceLink> findResList(String type) {
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		ResourceLink resourceLink = new ResourceLink();
		try{
			list = getSqlMapClientTemplate().queryForList("findResLink",type);
			if(null==list||list.size()==0){
				list = new ArrayList<ResourceLink>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ResourceLink>();
			resourceLink.setFlag(ResultUtils.ERROR);
			list.add(resourceLink);
		}
		return list;
	}
	
	/**
	 * 根据type查询资源列表--分页查询
	 */
	@Override
	public List<ResourceLink> findResLinkByPage(String type,long start,int pagesize) {
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		ResourceLink resourceLink = new ResourceLink();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("type", type);
			map.put("start", start);
			map.put("pagesize", pagesize);
			list = getSqlMapClientTemplate().queryForList("findResLinkByPage",map);
			if(null==list||list.size()==0){
				list = new ArrayList<ResourceLink>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ResourceLink>();
			resourceLink.setFlag(ResultUtils.ERROR);
			list.add(resourceLink);
		}
		return list;
	}
	
	/**
	 * 插入资源关系
	 */
	@Override
	public int insertResLink(ResourceLink resourceLink) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertResLink",resourceLink);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 根据资源id和link类型查询资源关系
	 */
	@Override
	public List<ResourceLink> findResListByResIdAndLinkType(long resId,
			String linkType) {
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		Map<String, Object> map = new HashMap<String, Object>();
		ResourceLink resourceLink = new ResourceLink();
		try{
			map.put("resId", resId);
			map.put("linkType", linkType);
			list = getSqlMapClientTemplate().queryForList("findResListByResIdAndLinkType",map);
			if(null==list||list.size()==0){
				list = new ArrayList<ResourceLink>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ResourceLink>();
			resourceLink.setFlag(ResultUtils.ERROR);
			list.add(resourceLink);
		}
		return list;
	}

	/**
	 * 根据link类型和type查询资源关系
	 */
	@Override
	public List<ResourceLink> findResListByLinkTypeAndType(String linkType,
			String type) {
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		Map<String, Object> map = new HashMap<String, Object>();
		ResourceLink resourceLink = new ResourceLink();
		try{
			map.put("linkType", linkType);
			map.put("type", type);
			list = getSqlMapClientTemplate().queryForList("findResListByResIdAndLinkType",map);
			if(null==list||list.size()==0){
				list = new ArrayList<ResourceLink>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ResourceLink>();
			resourceLink.setFlag(ResultUtils.ERROR);
			list.add(resourceLink);
		}
		return list;
	}

	@Override
	public List<ResourceLink> findResListByResIdAndType(long resId, String type) {
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		Map<String, Object> map = new HashMap<String, Object>();
		ResourceLink resourceLink = new ResourceLink();
		try{
			map.put("resId", resId);
			map.put("type", type);
			list = getSqlMapClientTemplate().queryForList("findResListByResIdAndType",map);
			if(null==list||list.size()==0){
				list = new ArrayList<ResourceLink>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ResourceLink>();
			resourceLink.setFlag(ResultUtils.ERROR);
			list.add(resourceLink);
		}
		return list;
	}
	
	@Override
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId, String type,Long start,Integer pagesize) {
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		Map<String, Object> map = new HashMap<String, Object>();
		ResourceLink resourceLink = new ResourceLink();
		try{
			map.put("resLinkId", resLinkId);
			map.put("type", type);
			map.put("start", start);
			map.put("pagesize", pagesize);
			list = getSqlMapClientTemplate().queryForList("findResListByResLinkIdAndType",map);
			if(null==list||list.size()==0){
				list = new ArrayList<ResourceLink>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ResourceLink>();
			resourceLink.setFlag(ResultUtils.ERROR);
			list.add(resourceLink);
		}
		return list;
	}

}
