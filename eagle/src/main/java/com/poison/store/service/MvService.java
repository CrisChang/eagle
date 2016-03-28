package com.poison.store.service;

import java.util.List;

import com.poison.store.model.MvInfo;

public interface MvService {
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id查询电影信息
	 * @param id
	 * @return
	 */
	public MvInfo queryById(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据movieUrl查询电影信息
	 * @param movieUrl
	 * @return
	 */
	public MvInfo queryByMvURL(String movieUrl);
	/**
	 * 
	 * 方法的描述 :此方法的作用是精准、模糊查询 根据名称
	 * @return
	 */
	public MvInfo findMvInfoByName(String name,String releaseDate);
	
	/**
	 * 
	 * <p>Title: insertMvInfo</p> 
	 * <p>Description: 插入电影信息</p> 
	 * @author :changjiang
	 * date 2014-8-23 下午5:46:26
	 * @return
	 */
	public long insertMvInfo(MvInfo mvInfo);
	
	/**
	 * 
	 * <p>Title: updateMvInfoDescribe</p> 
	 * <p>Description: 更新电影简介</p> 
	 * @author :changjiang
	 * date 2015-3-5 上午12:28:01
	 * @param id
	 * @param describe
	 */
	public void updateMvInfoDescribe(long id, String describe);
	
	/**
	 * 
	 * <p>Title: updateMvInfoDescribe</p> 
	 * <p>Description: 更新电影简介</p> 
	 * @param id
	 * @param releaseDateSort
	 */
	public void updateMvInfoReleaseDateSort(long id, long releaseDateSort);
	
	/**
	 * 
	 * <p>Title: updateMvInfoActor</p> 
	 * <p>Description: 更新电影演员</p> 
	 * @author :changjiang
	 * date 2015-3-5 上午1:31:42
	 * @param id
	 * @param actor
	 */
	public void updateMvInfoActor(long id, String actor);
	/**
	 * 根据id集合查询电影信息集合
	 */
	public List<MvInfo> findMvInfosByIds(long ids[]);
}
