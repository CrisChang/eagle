package com.poison.store.domain.repository;

import java.util.List;

import com.poison.store.dao.NetBookDAO;
import com.poison.store.model.NetBook;

public class NetBookDomainRepository {

	private NetBookDAO netBookDAO;

	public void setNetBookDAO(NetBookDAO netBookDAO) {
		this.netBookDAO = netBookDAO;
	}
	
	/**
	 * 
	 * <p>Title: findNetBookInfoById</p> 
	 * <p>Description: 根据id搜索网络小说</p> 
	 * @author :changjiang
	 * date 2015-1-14 下午4:48:42
	 * @param id
	 * @return
	 */
	public NetBook findNetBookInfoById(long id){
		return netBookDAO.findNetBookInfoById(id);
	}
	/**
	 * 根据id集合查询网络小说信息集合
	 */
	public List<NetBook> findNetBkInfosByIds(long ids[]){
		return netBookDAO.findNetBkInfosByIds(ids);
	}
}
