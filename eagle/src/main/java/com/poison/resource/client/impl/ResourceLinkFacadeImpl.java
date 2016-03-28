package com.poison.resource.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.poison.resource.client.ResourceLinkFacade;
import com.poison.resource.ext.constant.MemcacheResourceLinkConstant;
import com.poison.resource.model.ResourceLink;
import com.poison.resource.service.ResourceLinkService;

public class ResourceLinkFacadeImpl implements ResourceLinkFacade{

	private static final  Log LOG = LogFactory.getLog(ResourceLinkFacadeImpl.class);
	
	private ResourceLinkService resourceLinkService;
	//private MemcachedClient operationMemcachedClient;

	/*public void setOperationMemcachedClient(MemcachedClient operationMemcachedClient) {
		this.operationMemcachedClient = operationMemcachedClient;
	}*/

	public void setResourceLinkService(ResourceLinkService resourceLinkService) {
		this.resourceLinkService = resourceLinkService;
	}

	/**
	 * 根据type查询资源列表
	 */
	@Override
	public List<ResourceLink> findResListByType(String type) {
		List<ResourceLink> list = new ArrayList<ResourceLink>();
		list = resourceLinkService.findResList(type);
		//取精选页的数据，时间间隔为120秒
		/*try {
			list = operationMemcachedClient.get(MemcacheResourceLinkConstant.RES_LINK_TYPE+type);
			if(null==list||list.size()==0){
				list = resourceLinkService.findResList(type);
				operationMemcachedClient.set(MemcacheResourceLinkConstant.RES_LINK_TYPE+type, MemcacheResourceLinkConstant.TIME_INTERVALS, list);
			}
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}*/
		return list;
	}
	
	/**
	 * 根据type查询资源列表--分页查询
	 */
	@Override
	public List<ResourceLink> findResListByType(String type,long start,int pagesize){
		return resourceLinkService.findResLinkByPage(type, start, pagesize);
	}

	/**
	 * 根据resid和linktype查询资源关系
	 */
	@Override
	public List<ResourceLink> findResListByResIdAndLinkType(long resId,
			String linkType) {
		return resourceLinkService.findResListByResIdAndLinkType(resId, linkType);
	}

	/**
	 * 根据linktype和type查询资源关系
	 */
	@Override
	public List<ResourceLink> findResListByLinkTypeAndType(String linkType,
			String type) {
		return resourceLinkService.findResListByLinkTypeAndType(linkType, type);
	}

	@Override
	public List<ResourceLink> findResListByResIdAndType(long resId, String type) {
		return resourceLinkService.findResListByResIdAndType(resId,type);
	}
	
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId,String type){
		return resourceLinkService.findResListByResLinkIdAndType(resLinkId,type,null,null);
	}
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId,String type,Long start,Integer pagesize){
		return resourceLinkService.findResListByResLinkIdAndType(resLinkId,type,start,pagesize);
	}
}
