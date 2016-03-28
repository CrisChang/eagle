package com.poison.store.client;

import java.util.List;

import com.poison.store.model.NetBook;

public interface NetBookFacade {

	/**
	 * 
	 * <p>Title: findNetBookInfoById</p> 
	 * <p>Description: 根据id查询网络小说</p> 
	 * @author :changjiang
	 * date 2015-1-14 下午5:51:41
	 * @param id
	 * @return
	 */
	public NetBook findNetBookInfoById(long id);
	/**
	 * 根据id集合查询网络小说信息集合
	 */
	public List<NetBook> findNetBkInfosByIds(long[] ids);
}
