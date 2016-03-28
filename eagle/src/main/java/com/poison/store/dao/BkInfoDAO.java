package com.poison.store.dao;

import java.util.List;

import com.poison.store.model.BkInfo;

public interface BkInfoDAO {

	/**
	 * 
	 * <p>Title: findBkInfo</p> 
	 * <p>Description: 查询书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-6 下午2:13:02
	 * @param id
	 * @return
	 */
	public BkInfo findBkInfo(int id);
	
	/**
	 * 
	 * <p>Title: findBkInfoByName</p> 
	 * <p>Description: 根据书名查询一本书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-7 上午10:09:33
	 * @param name
	 * @return
	 */
	public List<BkInfo> findBkInfoByName(String name);
	
	/**
	 * 
	 * <p>Title: findBkInfoByLikeName</p> 
	 * <p>Description: 模糊查询一本书</p> 
	 * @author :changjiang
	 * date 2014-8-7 上午11:09:40
	 * @param name
	 * @return
	 */
	public List<BkInfo> findBkInfoByLikeName(String name);
	
	/**
	 * 
	 * <p>Title: insertBkInfo</p> 
	 * <p>Description: 插入一本书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-13 下午4:50:20
	 * @param bkInfo
	 * @return
	 */
	public int insertBkInfo(BkInfo bkInfo);
	
	/**
	 * 
	 * <p>Title: findBkInfo</p> 
	 * <p>Description: 根据isbn查询一本书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-19 上午12:24:16
	 * @param isbn
	 * @return
	 */
	public List<BkInfo> findBkInfoByIsbn(String isbn);
	
	/**
	 * 
	 * <p>Title: findBkInfoBybookurl</p> 
	 * <p>Description: 根据bookurl查询一本书的详情</p> 
	 * @author :guandapeng
	 * date 2014-11-25 下午16:57:19 
	 * @param bookurl
	 * @return
	 */
	public List<BkInfo> findBkInfoBybookurl(String bookurl);
	/**
	 * 根据id集合查询图书信息集合
	 */
	public List<BkInfo> findBkInfosByIds(long ids[]);
}
