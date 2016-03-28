package com.poison.store.service.impl;

import java.util.List;

import com.poison.store.domain.repository.NetBookDomainRepository;
import com.poison.store.model.NetBook;
import com.poison.store.service.NetBookService;

public class NetBookServiceImpl implements NetBookService{

	private NetBookDomainRepository netBookDomainRepository;
	
	
	public void setNetBookDomainRepository(
			NetBookDomainRepository netBookDomainRepository) {
		this.netBookDomainRepository = netBookDomainRepository;
	}


	/**
	 * 根据id查询网络小说
	 */
	@Override
	public NetBook findNetBookInfoById(long id) {
		return netBookDomainRepository.findNetBookInfoById(id);
	}
	/**
	 * 根据id集合查询网络小说信息集合
	 */
	@Override
	public List<NetBook> findNetBkInfosByIds(long[] ids) {
		return netBookDomainRepository.findNetBkInfosByIds(ids);
	}

}
