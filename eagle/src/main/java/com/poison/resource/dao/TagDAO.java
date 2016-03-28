package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.Tag;

public interface TagDAO {
	
	/**
	 * 
	 * <p>Title: findTagById</p> 
	 * <p>Description: 根据ID查询标签</p> 
	 * @author :changjiang
	 * date 2014-11-19 下午12:53:39
	 * @param id
	 * @return
	 */
	public Tag findTagById(long id);
	
	/**
	 * 
	 * <p>Title: findTagListByTag</p> 
	 * <p>Description: 根据type查询标签</p> 
	 * @author :changjiang
	 * date 2014-11-19 下午12:54:18
	 * @param tag
	 * @return
	 */
	public List<Tag> findTagListByType(String type);
	
	/**
	 * 根据类型查询标签列表按自增id倒序
	 */
	public List<Tag> findTagByTypeOrderById(String type);
	
	/**
	 * 
	 * <p>Title: findTagListByTagGroup</p> 
	 * <p>Description: 查询热门标签</p> 
	 * @author :changjiang
	 * date 2014-12-4 上午11:15:35
	 * @param tagGroup
	 * @return
	 */
	public List<Tag> findHotTagListByTagGroup(String tagGroup);
	
	/**
	 * 
	 * <p>Title: findAllTagListByTagGroup</p> 
	 * <p>Description: 查询全部标签</p> 
	 * @author :changjiang
	 * date 2014-12-4 上午11:16:59
	 * @param tagGroup
	 * @return
	 */
	public List<Tag> findAllTagListByTagGroup(String tagGroup);
	
	/**
	 * 
	 * <p>Title: findTaggroupByTagName</p> 
	 * <p>Description: 根据标签名字查询类型</p> 
	 * @author :changjiang
	 * date 2015-1-19 下午2:18:42
	 * @param tagName
	 * @param type
	 * @return
	 */
	public List<Tag> findTaggroupByTagName(String tagName,String type);

}
