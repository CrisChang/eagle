package com.poison.resource.service;

import java.util.List;

import com.poison.resource.model.ResourceLink;

public interface ResourceLinkService {

	/**
	 * 
	 * <p>Title: findResList</p> 
	 * <p>Description: 查询资源列表</p> 
	 * @author :changjiang
	 * date 2014-12-11 上午11:45:36
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResList(String type);
	/**
	 * 根据type查询资源列表--分页查询
	 */
	public List<ResourceLink> findResLinkByPage(String type,long start,int pagesize);
	
	/**
	 * 
	 * <p>Title: findResListByResIdAndLinkType</p> 
	 * <p>Description: 根据resid和linktype查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:29:48
	 * @param resId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResIdAndLinkType(long resId,
			String linkType);
	/**
	 * 
	 * <p>Title: findResListByResIdAndLinkType</p> 
	 * <p>Description: 根据resid和linktype查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:29:48
	 * @param resId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResIdAndType(long resId,
			String type);
	
	/**
	 * 
	 * <p>Title: findResListByLinkTypeAndType</p> 
	 * <p>Description: 根据linktype和type查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:30:31
	 * @param linkType
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByLinkTypeAndType(String linkType,
			String type);
	/**
	 * 
	 * <p>Title: findResListByResLinkIdAndType</p> 
	 * <p>Description: 根据linktype和type查询资源关系</p> 
	 * @author :zhangqi
	 * date 2014-12-11 下午8:30:31
	 * @param linkType
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId,String type,Long start,Integer pagesize);
}
