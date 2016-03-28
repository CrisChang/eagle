package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.TagCategory;

public interface TagCategoryDAO {

	/**
	 * 
	 * <p>Title: findTagCategoryByLevel</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-12-3 下午8:48:44
	 * @return
	 */
	public List<TagCategory> findTagCategoryByLevel();
	
	/**
	 * 
	 * <p>Title: findTagCategoryByType</p> 
	 * <p>Description: 根据type查询类别</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:05:46
	 * @param type
	 * @return
	 */
	public List<TagCategory> findTagCategoryByType(String type);
}
