package com.poison.store.service;

import java.util.List;

import com.poison.store.model.BkInfo;

public interface BkService {

	/**
	 * 
	 * <p>Title: findBkInfo</p> 
	 * <p>Description: 查询一本书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午2:37:07
	 * @param id
	 * @return
	 */
	public BkInfo findBkInfo(int id);
	
	/**
	 * 
	 * <p>Title: findBkInfoByName</p> 
	 * <p>Description: 根据书名查询书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-7 上午11:59:18
	 * @param name
	 * @return
	 */
	public List<BkInfo> findBkInfoByName(String name);
	
	/**
	 * 
	 * <p>Title: insertBkInfo</p> 
	 * <p>Description: 插入一本书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-13 下午5:19:20
	 * @param bkInfo
	 * @return
	 */
	public int insertBkInfo(BkInfo bkInfo);
	
	/**
	 * 
	 * <p>Title: findBkInfo</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2014-8-19 上午12:30:34
	 * @param isbn
	 * @return
	 */
	public BkInfo findBkInfoByIsbn(String isbn);
	
	/**
	 * 
	 * <p>Title: findBkInfoBybookurl</p> 
	 * <p>Description: </p> 
	 * @author :guandapeng
	 * date 2014-11-25 下午16:51:01 
	 * @param bookurl
	 * @return
	 */
	public BkInfo findBkInfoBybookurl(String bookurl);
	/**
	 * 根据id集合查询电影信息集合
	 */
	public List<BkInfo> findBkInfosByIds(long ids[]);
}
