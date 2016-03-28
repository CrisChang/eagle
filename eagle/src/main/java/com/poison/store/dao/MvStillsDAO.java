package com.poison.store.dao;

import com.poison.store.model.MovieStills;

public interface MvStillsDAO {

	/**
	 * 
	 * <p>Title: insertintoMvOlineStills</p> 
	 * <p>Description: 插入电影剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午1:39:01
	 * @param movieStills
	 * @return
	 */
	public int insertintoMvOlineStills(MovieStills movieStills);
	
	/**
	 * 
	 * <p>Title: findMvOlineStillsByBkId</p> 
	 * <p>Description: 根据电影id查询电影剧照信息</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午2:13:15
	 * @param mvId
	 * @return
	 */
	public MovieStills findMvOlineStillsByBkId(long mvId);
	
	/**
	 * 
	 * <p>Title: updateMvStills</p> 
	 * <p>Description: 更新剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:06:28
	 * @param movieStills
	 * @return
	 */
	public int updateMvStills(MovieStills movieStills); 
	
	/**
	 * 
	 * <p>Title: updateMvOther</p> 
	 * <p>Description: 更新片花</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:08:44
	 * @param movieStills
	 * @return
	 */
	public int updateMvOther(MovieStills movieStills);
	
	/**
	 * 
	 * <p>Title: updateMvTwoDimensionCode</p> 
	 * <p>Description: 更新电影的二维码</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午5:23:00
	 * @param movieStills
	 * @return
	 */
	public int updateMvTwoDimensionCode(MovieStills movieStills);
}
