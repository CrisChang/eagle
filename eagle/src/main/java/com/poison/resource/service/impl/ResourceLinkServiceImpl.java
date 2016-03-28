package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.ResourceLinkDomainRepository;
import com.poison.resource.model.ResourceLink;
import com.poison.resource.service.ResourceLinkService;

public class ResourceLinkServiceImpl implements ResourceLinkService{

	private ResourceLinkDomainRepository resourceLinkDomainRepository;
	
	public void setResourceLinkDomainRepository(
			ResourceLinkDomainRepository resourceLinkDomainRepository) {
		this.resourceLinkDomainRepository = resourceLinkDomainRepository;
	}

	/**
	 * 查询资源列表
	 */
	@Override
	public List<ResourceLink> findResList(String type) {
		return resourceLinkDomainRepository.findResList(type);
	}
	/**
	 * 根据type查询资源列表--分页查询
	 */
	@Override
	public List<ResourceLink> findResLinkByPage(String type,long start,int pagesize){
		return resourceLinkDomainRepository.findResLinkByPage(type, start, pagesize);
	}
	/**
	 * 根据resid和linktype查询资源关系
	 */
	@Override
	public List<ResourceLink> findResListByResIdAndLinkType(long resId,
			String linkType) {
		return resourceLinkDomainRepository.findResListByResIdAndLinkType(resId, linkType);
	}

	/**
	 * 根据linktype和type查询资源关系
	 */
	@Override
	public List<ResourceLink> findResListByLinkTypeAndType(String linkType,
			String type) {
		return resourceLinkDomainRepository.findResListByLinkTypeAndType(linkType, type);
	}

	@Override
	public List<ResourceLink> findResListByResIdAndType(long resId, String type) {
		return resourceLinkDomainRepository.findResListByResIdAndType(resId,type);
	}
	@Override
	public List<ResourceLink> findResListByResLinkIdAndType(long resLinkId,String type,Long start,Integer pagesize){
		return resourceLinkDomainRepository.findResListByResLinkIdAndType(resLinkId,type,start,pagesize);
	}
}
