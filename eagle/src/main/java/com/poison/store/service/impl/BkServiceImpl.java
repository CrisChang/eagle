package com.poison.store.service.impl;

import java.util.List;

import com.poison.store.domain.repository.BkDomainRepository;
import com.poison.store.model.BkInfo;
import com.poison.store.service.BkService;

public class BkServiceImpl implements BkService{

	private BkDomainRepository bkDomainRepository;

	public void setBkDomainRepository(BkDomainRepository bkDomainRepository) {
		this.bkDomainRepository = bkDomainRepository;
	}


	/**
	 * 查询一本书的详细信息
	 */
	@Override
	public BkInfo findBkInfo(int id) {
		return bkDomainRepository.findBkInfo(id);
	}

	/**
	 * 根据书名查询书的信息
	 */
	@Override
	public List<BkInfo> findBkInfoByName(String name) {
		return bkDomainRepository.findBkInfoByName(name);
	}

	/**
	 * 插入一本书的信息
	 */
	@Override
	public int insertBkInfo(BkInfo bkInfo) {
		return bkDomainRepository.insertBkInfo(bkInfo);
	}

	/**
	 * 根据isbn查询一本书的详情
	 */
	@Override
	public BkInfo findBkInfoByIsbn(String isbn) {
		return bkDomainRepository.findBkInfoByIsbn(isbn);
	}
	
	/**
	 * 根据bookurl查询一本书的详情
	 */
	@Override
	public BkInfo findBkInfoBybookurl(String bookurl){
		return bkDomainRepository.findBkInfoBybookurl(bookurl);
	}


	@Override
	public List<BkInfo> findBkInfosByIds(long[] ids) {
		return bkDomainRepository.findBkInfosByIds(ids);
	}
}
