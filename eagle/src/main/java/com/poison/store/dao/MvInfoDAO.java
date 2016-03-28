package com.poison.store.dao;

import java.util.List;

import com.poison.store.model.MvInfo;

/**
 * 
 * 类的作用:此类的作用是与数据库进行持久化操作
 * 作者:闫前刚
 * 创建时间:2014-8-7上午11:30:11
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface MvInfoDAO {
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id查询电影的信息
	 * @param id
	 * @return
	 */
	public MvInfo queryById(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据movieUrl查询电影的信息
	 * @param movieUrl
	 * @return
	 */
	public MvInfo queryByMvURL(String movieUrl);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据名称模糊查询
	 * @param name
	 * @return
	 */
	public List<MvInfo> findMvInfoByLikeName(String name);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据名称准确匹配
	 * @param name
	 * @return
	 */
	public List<MvInfo> findMvInfoByName(MvInfo info);
	
	
	/**
	 * 
	 * <p>Title: insertMvInfo</p> 
	 * <p>Description: 插入电影信息</p> 
	 * @author :changjiang
	 * date 2014-8-23 下午5:35:42
	 * @param mvInfo
	 * @return
	 */
	public long insertMvInfo(MvInfo mvInfo);
	
	/**
	 * 
	 * <p>Title: updateMvInfoDescribe</p> 
	 * <p>Description: 更新电影的简介</p> 
	 * @author :changjiang
	 * date 2015-3-5 上午12:18:29
	 * @param id
	 * @param describe
	 */
	public void updateMvInfoDescribe(long id,String describe);
	
	/**
	 * 
	 * <p>Title: updateMvInfoDescribe</p> 
	 * <p>Description: 更新电影的简介</p> 
	 * @author :zhangqi
	 * @param id
	 * @param releaseDateSort 上映日期排序
	 */
	public void updateMvInfoReleaseDateSort(long id,long releaseDateSort);
	
	/**
	 * 
	 * <p>Title: updateMvInfoActor</p> 
	 * <p>Description: 更新电影演员信息</p> 
	 * @author :changjiang
	 * date 2015-3-5 上午1:27:37
	 * @param id
	 * @param actor
	 */
	public void updateMvInfoActor(long id, String actor);
	/**
	 * 根据id集合查询电影信息集合
	 */
	public List<MvInfo> findMvInfosByIds(long ids[]);
}
