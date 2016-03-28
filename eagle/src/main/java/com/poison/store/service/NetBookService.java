package com.poison.store.service;

import java.util.List;

import com.poison.store.model.NetBook;

public interface NetBookService {

	/**
	 * 
	 * <p>Title: findNetBookInfoById</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-1-14 下午4:57:44
	 * @param id
	 * @return
	 */
	public NetBook findNetBookInfoById(long id);
	/**
	 * 根据id集合查询网络小说信息集合
	 */
	public List<NetBook> findNetBkInfosByIds(long ids[]);
}
