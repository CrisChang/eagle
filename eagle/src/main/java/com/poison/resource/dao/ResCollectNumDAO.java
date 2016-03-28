package com.poison.resource.dao;

import com.poison.resource.model.ResCollectNum;

public interface ResCollectNumDAO {

	/**
	 * 
	 * <p>Title: insertResCollectNum</p> 
	 * <p>Description: 插入资源的收藏信息</p> 
	 * @author :changjiang
	 * date 2014-12-27 下午1:57:31
	 * @param ResCollectNum
	 * @return
	 */
	public int insertResCollectNum(ResCollectNum ResCollectNum);
	
	/**
	 * 
	 * <p>Title: findResCollectNumById</p> 
	 * <p>Description: 根据id查询收藏的资源</p> 
	 * @author :changjiang
	 * date 2014-12-27 下午1:59:08
	 * @param id
	 * @return
	 */
	public ResCollectNum findResCollectNumById(ResCollectNum resCollectNum);
	
	/**
	 * 
	 * <p>Title: updateResCollectNum</p> 
	 * <p>Description: 更新资源收藏</p> 
	 * @author :changjiang
	 * date 2014-12-27 下午4:56:14
	 * @param resCollectNum
	 * @return
	 */
	public int updateResCollectNum(ResCollectNum resCollectNum);
}
