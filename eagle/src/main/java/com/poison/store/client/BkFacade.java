package com.poison.store.client;

import java.util.List;

import com.poison.store.model.BkInfo;

public interface BkFacade {

	/**
	 * 
	 * <p>Title: findBkInfo</p> 
	 * <p>Description: 查询一本书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午2:36:17
	 * @param id
	 * @return
	 */
	public BkInfo findBkInfo(int id);
	
	/**
	 * 
	 * <p>Title: findBkInfoByName</p> 
	 * <p>Description: 根据书名查询书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午12:01:18
	 * @param name
	 * @return
	 */
	public List<BkInfo> findBkInfoByName(String name);
	
	/**
	 * 
	 * <p>Title: insertBkInfo</p> 
	 * <p>Description: 插入一本书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-13 下午5:20:28
	 * @param bkInfo
	 * @return
	 */
	public BkInfo insertBkInfo(BkInfo bkInfo);
	
	/**
	 * 
	 * <p>Title: findBkInfoByIsbn</p> 
	 * <p>Description: 根据isbn查询一本书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-28 下午8:41:51
	 * @param isbn
	 * @return
	 */
	public BkInfo findBkInfoByIsbn(String isbn);
	/**
	 * <p>Title: findBkInfoBybookurl</p>
	 * <p>Description: 根据bookurl查询一本书的信息</p> 
	 * @author :guandapeng
	 * date 2014-11-25 下午16:43:15  
	 * @param bookurl
	 * @return
	 */
	public BkInfo findBkInfoBybookurl(String bookurl);
	/**
	 * 根据id集合查询图书信息集合
	 */
	public List<BkInfo> findBkInfosByIds(long ids[]);
}
