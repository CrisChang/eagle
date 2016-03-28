package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.ResLinkDAO;
import com.poison.resource.model.ResourceLink;

public class ResourceLinkDomainRepository {
	
	private ResLinkDAO resLinkDAO;

	public void setResLinkDAO(ResLinkDAO resLinkDAO) {
		this.resLinkDAO = resLinkDAO;
	}
	
	/**
	 * 
	 * <p>Title: findResList</p> 
	 * <p>Description: 查询资源类型表</p> 
	 * @author :changjiang
	 * date 2014-12-11 上午11:06:32
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResList(String type){
		return resLinkDAO.findResList(type);
	}
	/**
	 * 根据type查询资源列表--分页查询
	 */
	public List<ResourceLink> findResLinkByPage(String type,long start,int pagesize){
		return resLinkDAO.findResLinkByPage(type, start, pagesize);
	}
	/**
	 * 
	 * <p>Title: insertResLink</p> 
	 * <p>Description: 插入资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午3:17:41
	 * @return
	 */
	public ResourceLink insertResLink(ResourceLink resourceLink){
		
		return null;
	}
	
	/**
	 * 
	 * <p>Title: findResListByResIdAndLinkType</p> 
	 * <p>Description: 根据资源id和link类型查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:18:13
	 * @param resId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResIdAndLinkType(long resId,
			String linkType){
		return resLinkDAO.findResListByResIdAndLinkType(resId, linkType);
	}
	/**
	 * 
	 * <p>Title: findResListByResIdAndLinkType</p> 
	 * <p>Description: 根据资源id和link类型查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:18:13
	 * @param resId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResIdAndType(long resId,
			String type){
		return resLinkDAO.findResListByResIdAndType(resId, type);
	}
	
	/**
	 * 
	 * <p>Title: findResListByLinkTypeAndType</p> 
	 * <p>Description: 根据link类型和type查询资源关系</p> 
	 * @author :changjiang
	 * date 2014-12-11 下午8:25:27
	 * @param linkType
	 * @param type
	 * @return
	 */
	public List<ResourceLink> findResListByLinkTypeAndType(String linkType,
			String type){
		return resLinkDAO.findResListByLinkTypeAndType(linkType, type);
	}
	
	/**
	 * 
	 * <p>Title: findResListByResLinkIdAndType</p> 
	 * <p>Description: 根据资源resLinkId和type类型查询资源关系</p> 
	 * @author :zhangqi
	 * @param resLinkId
	 * @param linkType
	 * @return
	 */
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId,
			String type,Long start,Integer pagesize){
		return resLinkDAO.findResListByResLinkIdAndType(resLinkId, type,start,pagesize);
	}

}
