package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.ResourceLink;

public interface ResourceLinkFacade {

	/**
	 * 
	 * <p>Title: findResList</p> 
	 * <p>Description: 查询资源列表</p> 
	 * @author :changjiang
	 * date 2014-12-11 上午11:49:47
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByType(String type);
	
	/**
	 * 根据type查询资源列表--分页查询
	 */
	public List<ResourceLink> findResListByType(String type,long start,int pagesize);
	
	/**
	 * 
	 * <p>Title: findResListByResIdAndLinkType</p> 
	 * <p>Description: 根据resid和linktype查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:37:41
	 * @param resId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResIdAndLinkType(long resId,
			String linkType);
	/**
	 * 
	 * <p>Title: findResListByResIdAndType</p> 
	 * <p>Description: 根据resid和linktype查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:37:41
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
	 * date 2014-12-11 下午8:38:03
	 * @param linkType
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByLinkTypeAndType(String linkType,
			String type);
	
	/**
	 * 
	 * <p>Title: findResListByLinkTypeAndType</p> 
	 * <p>Description: 根据resLinkId和type查询资源关系</p> 
	 * @author :zhangqi
	 * @param linkType
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId,String type);
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId,String type,Long start,Integer pagesize);
}
