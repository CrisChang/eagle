package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.MvAvgMark;

public interface MvAvgMarkDAO {

	/**
	 * 
	 * <p>Title: insertBkAvgMark</p> 
	 * <p>Description: 插入一部电影的平均评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午3:02:12
	 * @param bkAvgMark
	 * @return
	 */
	public int insertMvAvgMark(MvAvgMark mvAvgMark);
	
	/**
	 * 
	 * <p>Title: findBkAvgMarkByBkId</p> 
	 * <p>Description: 根据电影的id查询平均评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午3:16:14
	 * @param bkId
	 * @return
	 */
	public MvAvgMark findMvAvgMarkByMvId(long mvId);
	/**
	 * 根据电影的id集合查询评分信息
	 */
	public List<MvAvgMark> findMvAvgMarkByMvIds(List<Long> mvids);
	/**
	 * 
	 * <p>Title: updateBkAvgMark</p> 
	 * <p>Description: 更新一个电影评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午4:38:44
	 * @param bkAvgMark
	 * @return
	 */
	public int updateMvAvgMark(MvAvgMark mvAvgMark);
	
	/**
	 * 
	 * <p>Title: updateExpertsAvgMark</p> 
	 * <p>Description: 更新专家的评分</p> 
	 * @author :changjiang
	 * date 2014-12-12 上午11:23:48
	 * @param mvAvgMark
	 * @return
	 */
	public int updateExpertsAvgMark(MvAvgMark mvAvgMark);
}
