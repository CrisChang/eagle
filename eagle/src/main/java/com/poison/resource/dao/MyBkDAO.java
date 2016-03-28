package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.MyBk;

public interface MyBkDAO {

	/**
	 * 
	 * <p>Title: insertMyBk</p> 
	 * <p>Description: 插入一条用户自己创建的书</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午2:28:46
	 * @param myBk
	 * @return
	 */
	public int insertMyBk(MyBk myBk);
	
	/**
	 * 
	 * <p>Title: findMyBkIsExist</p> 
	 * <p>Description: 查询用户添加的书是否已经存在</p> 
	 * @author :changjiang
	 * date 2014-8-9 上午2:16:38
	 * @param myBk
	 * @return
	 */
	public MyBk findMyBkIsExist(MyBk myBk);
	
	/**
	 * 
	 * <p>Title: findAllMyBkList</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2014-8-9 下午2:33:34
	 * @return
	 */
	public List<MyBk> findMyBkList(long userId,String name);
	
	/**
	 * 
	 * <p>Title: findLikeMyBkList</p> 
	 * <p>Description: 模糊查询</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午2:35:40
	 * @param userId
	 * @param name
	 * @return
	 */
	public List<MyBk> findLikeMyBkList(long userId,String name);
	
	/**
	 * 
	 * <p>Title: findMyBkInfo</p> 
	 * <p>Description: 根据ID查询自己库中书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午4:14:47
	 * @param id
	 * @return
	 */
	public MyBk findMyBkInfo(int id);
}
