package com.poison.store.client.impl;

import java.util.List;

import com.poison.store.client.NetBookFacade;
import com.poison.store.model.NetBook;
import com.poison.store.service.NetBookService;

public class NetBookFacadeImpl implements NetBookFacade{

	private NetBookService netBookService;
	
	public void setNetBookService(NetBookService netBookService) {
		this.netBookService = netBookService;
	}

	/**
	 * 根据id查询网络小说
	 */
	@Override
	public NetBook findNetBookInfoById(long id) {
		return netBookService.findNetBookInfoById(id);
	}
	/**
	 * 根据id集合查询网络小说信息集合
	 */
	@Override
	public List<NetBook> findNetBkInfosByIds(long[] ids){
		return netBookService.findNetBkInfosByIds(ids);
	}
}
