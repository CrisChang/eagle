package com.poison.store.client;

import com.poison.store.model.MovieStills;

public interface MvStillsFacade {

	/**
	 * 
	 * <p>Title: insertintoMvOlineStills</p> 
	 * <p>Description: 插入剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午2:37:57
	 * @param movieStills
	 * @return
	 */
	//public int insertintoMvOlineStills(long mvId,String movieStills);
	
	/**
	 * 
	 * <p>Title: findMvOlineStillsByBkId</p> 
	 * <p>Description: 根据电影id查询剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 上午2:38:58
	 * @param mvId
	 * @return
	 */
	public MovieStills findMvOlineStillsByBkId(long mvId);
	
	/**
	 * 
	 * <p>Title: updateMvStills</p> 
	 * <p>Description: 更新剧照</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:27:41
	 * @param movieStills
	 * @param mvId
	 * @return
	 */
	public MovieStills updateMvStills(String movieStills,long mvId);
	
	/**
	 * 
	 * <p>Title: updateMvOther</p> 
	 * <p>Description: 更新片花</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午3:28:29
	 * @param movieStills
	 * @return
	 */
	public MovieStills updateMvOther(String other,long mvId);
	
	/**
	 * 
	 * <p>Title: addMvTwoDimensionCode</p> 
	 * <p>Description: 添加电影的二维码</p> 
	 * @author :changjiang
	 * date 2014-10-30 下午5:34:40
	 * @param mvId
	 * @param twoDimensionCode
	 * @return
	 */
	public MovieStills addMvTwoDimensionCode(long mvId,String twoDimensionCode);
}
