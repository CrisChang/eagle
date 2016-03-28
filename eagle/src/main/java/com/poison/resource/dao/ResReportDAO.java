package com.poison.resource.dao;

import com.poison.resource.model.ResReport;

public interface ResReportDAO {

	/**
	 * 
	 * <p>Title: insertResReport</p> 
	 * <p>Description: 插入举报信息</p> 
	 * @author :changjiang
	 * date 2014-12-1 下午8:23:03
	 * @param report
	 * @return
	 */
	public int insertResReport(ResReport report);
	
	/**
	 * 
	 * <p>Title: findResReport</p> 
	 * <p>Description: 根据id查询举报信息</p> 
	 * @author :changjiang
	 * date 2014-12-1 下午8:50:18
	 * @param id
	 * @return
	 */
	public ResReport findResReportById(long id);
	
	public ResReport findResReportIsExist(long userId,long resId);
}
