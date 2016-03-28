package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.ResourceLink;

public interface ResLinkDAO {

	/**
	 * 
	 * <p>Title: findResList</p> 
	 * <p>Description: 查询资源列表</p> 
	 * @author :changjiang
	 * date 2014-12-11 上午10:54:48
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
	 * <p>Title: insertResLink</p> 
	 * <p>Description: 插入资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午3:06:07
	 * @param resourceLink
	 * @return
	 */
	public int insertResLink(ResourceLink resourceLink);
	
	/**
	 * 
	 * <p>Title: findResListByResIdAndLinkType</p> 
	 * <p>Description: 根据资源id和linktype类型查询</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午7:52:56
	 * @param resId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResIdAndLinkType(long resId,String linkType);
	/**
	 * 
	 * <p>Title: findResListByResIdAndLinkType</p> 
	 * <p>Description: 根据资源id和linktype类型查询</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午7:52:56
	 * @param resId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResIdAndType(long resId,String type);
	
	/**
	 * 
	 * <p>Title: findResListByLinkTypeAndType</p> 
	 * <p>Description: 根据link类型和type查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:04:12
	 * @param linkType
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByLinkTypeAndType(String linkType,String type);
	
	/**
	 * 
	 * <p>Title: findResListByLinkTypeAndType</p> 
	 * <p>Description: 根据 resLinkId类型和type查询资源关系</p> 
	 * @author zhangqi
	 * @param linkType
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId, String type,Long start,Integer pagesize) ;
	
}
