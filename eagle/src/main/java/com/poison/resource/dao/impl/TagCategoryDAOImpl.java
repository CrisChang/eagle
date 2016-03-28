package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.TagCategoryDAO;
import com.poison.resource.model.TagCategory;

public class TagCategoryDAOImpl extends SqlMapClientDaoSupport implements TagCategoryDAO{

	private static final  Log LOG = LogFactory.getLog(TagCategoryDAOImpl.class);
	/**
	 * 查询标签的顺序
	 */
	@Override
	public List<TagCategory> findTagCategoryByLevel() {
		List<TagCategory> list = new ArrayList<TagCategory>();
		TagCategory tagCategory = new TagCategory();
		try{
			list = getSqlMapClientTemplate().queryForList("findTagCategoryByLevel");
			if(null==list||list.size()==0){
				list = new ArrayList<TagCategory>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<TagCategory>();
			tagCategory.setFlag(ResultUtils.QUERY_ERROR);
			list.add(tagCategory);
		}
		return list;
	}
	
	/**
	 * 根据type查询类别信息
	 */
	@Override
	public List<TagCategory> findTagCategoryByType(String type) {
		List<TagCategory> tagList = new ArrayList<TagCategory>();
		TagCategory tagCategory = new TagCategory();
		try{
			tagList = getSqlMapClientTemplate().queryForList("findTagCategoryByType",type);
			if(null==tagList||tagList.size()==0){
				tagList = new ArrayList<TagCategory>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tagList = new ArrayList<TagCategory>();
			tagCategory.setFlag(ResultUtils.QUERY_ERROR);
			tagList.add(tagCategory);
		}
		return tagList;
	}

}
