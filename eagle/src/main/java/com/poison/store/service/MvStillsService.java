package com.poison.store.service;

import com.poison.store.model.MovieStills;

public interface MvStillsService {

	/**
	 * 
	 * <p>Title: insertintoMvOlineStills</p> 
	 * <p>Description: 插入一个电影的剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午2:30:56
	 * @param movieStills
	 * @return
	 */
	public int insertintoMvOlineStills(MovieStills movieStills);
	
	/**
	 * 
	 * <p>Title: findMvOlineStillsByBkId</p> 
	 * <p>Description: 查询一个电影的剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午2:31:42
	 * @param mvId
	 * @return
	 */
	public MovieStills findMvOlineStillsByBkId(long mvId);
	
	/**
	 * 
	 * <p>Title: updateMvStills</p> 
	 * <p>Description: 更新剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:23:10
	 * @param movieStills
	 * @return
	 */
	public int updateMvStills(MovieStills movieStills);
	
	/**
	 * 
	 * <p>Title: updateMvOther</p> 
	 * <p>Description: 更新电影片花</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:23:39
	 * @param movieStills
	 * @return
	 */
	public int updateMvOther(MovieStills movieStills);
	
	/**
	 * 
	 * <p>Title: updateMvTwoDimensionCode</p> 
	 * <p>Description: 更新电影二维码</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午5:32:55
	 * @param movieStills
	 * @return
	 */
	public MovieStills updateMvTwoDimensionCode(MovieStills movieStills);
}
