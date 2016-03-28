package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.BkAvgMark;

public interface BkAvgMarkDAO {

	/**
	 * 
	 * <p>Title: insertBkAvgMark</p> 
	 * <p>Description: 插入一本书的平均评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午3:02:12
	 * @param bkAvgMark
	 * @return
	 */
	public int insertBkAvgMark(BkAvgMark bkAvgMark);
	
	/**
	 * 
	 * <p>Title: findBkAvgMarkByBkId</p> 
	 * <p>Description: 根据书的id查询平均评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午3:16:14
	 * @param bkId
	 * @return
	 */
	public BkAvgMark findBkAvgMarkByBkId(int bkId);
	/**
	 * 根据书的id集合查询和书的类型查询评分信息
	 */
	public List<BkAvgMark> findBkAvgMarkByBkIds(List<Long> bkids,String type);
	
	/**
	 * 
	 * <p>Title: updateBkAvgMark</p> 
	 * <p>Description: 更新一个评分</p> 
	 * @author :changjiang
	 * date 2014-9-9 下午4:38:44
	 * @param bkAvgMark
	 * @return
	 */
	public int updateBkAvgMark(BkAvgMark bkAvgMark);
	
	/**
	 * 
	 * <p>Title: updateExpertsAvgMark</p> 
	 * <p>Description: 更新神人的书评评分</p> 
	 * @author :changjiang
	 * date 2015-5-15 下午3:21:05
	 * @param bkAvgMark
	 * @return
	 */
	public int updateExpertsAvgMark(BkAvgMark bkAvgMark);
}
