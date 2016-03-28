package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.TagDAO;
import com.poison.resource.model.Tag;

public class TagDAOImpl extends SqlMapClientDaoSupport implements TagDAO{

	private static final  Log LOG = LogFactory.getLog(TagDAOImpl.class);
	/**
	 * 根据ID查询TAG
	 */
	@Override
	public Tag findTagById(long id) {
		Tag tag = new Tag();
		try{
			tag = (Tag) getSqlMapClientTemplate().queryForObject("findTagById",id);
			if(null==tag){
				tag = new Tag();
				tag.setFlag(ResultUtils.DATAISNULL);
				return tag;
			}
			tag.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tag = new Tag();
			tag.setFlag(ResultUtils.QUERY_ERROR);
		}
		return tag;
	}

	/**
	 * 根据类型查询标签列表
	 */
	@Override
	public List<Tag> findTagListByType(String type) {
		List<Tag> tagList = new ArrayList<Tag>();
		Tag tag = new Tag();
		try{
			tagList = getSqlMapClientTemplate().queryForList("findTagByType",type);
			if(null==tagList||tagList.size()==0){
				tagList = new ArrayList<Tag>();
				tag.setFlag(ResultUtils.DATAISNULL);
				tagList.add(tag);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tagList = new ArrayList<Tag>();
			tag.setFlag(ResultUtils.ERROR);
			tagList.add(tag);
		}
		return tagList;
	}
	
	/**
	 * 根据类型查询标签列表按自增id倒序
	 */
	@Override
	public List<Tag> findTagByTypeOrderById(String type) {
		List<Tag> tagList = new ArrayList<Tag>();
		Tag tag = new Tag();
		try{
			tagList = getSqlMapClientTemplate().queryForList("findTagByTypeOrderById",type);
			if(null==tagList||tagList.size()==0){
				tagList = new ArrayList<Tag>();
				tag.setFlag(ResultUtils.DATAISNULL);
				tagList.add(tag);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tagList = new ArrayList<Tag>();
			tag.setFlag(ResultUtils.ERROR);
			tagList.add(tag);
		}
		return tagList;
	}
	

	/**
	 * 查询热门标签
	 */
	@Override
	public List<Tag> findHotTagListByTagGroup(String tagGroup) {
		List<Tag> hotTagList = new ArrayList<Tag>();
		Tag tag = new Tag();
		try{
			hotTagList = getSqlMapClientTemplate().queryForList("findHotTagListByTagGroup",tagGroup);
			if(null==hotTagList||hotTagList.size()==0){
				hotTagList = new ArrayList<Tag>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tag.setFlag(ResultUtils.QUERY_ERROR);
			hotTagList.add(tag);
		}
		return hotTagList;
	}

	/**
	 * 查询全部标签
	 */
	@Override
	public List<Tag> findAllTagListByTagGroup(String tagGroup) {
		List<Tag> allTagList = new ArrayList<Tag>();
		Tag tag = new Tag();
		try{
			allTagList = getSqlMapClientTemplate().queryForList("findAllTagListByTagGroup",tagGroup);
			if(null==allTagList||allTagList.size()==0){
				allTagList = new ArrayList<Tag>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tag.setFlag(ResultUtils.QUERY_ERROR);
			allTagList.add(tag);
		}
		return allTagList;
	}

	/**
	 * 根据标签名字查询标签分组
	 */
	@Override
	public List<Tag> findTaggroupByTagName(String tagName, String type) {
		 List<Tag> list = new ArrayList<Tag>();
		 try{
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("tagName", tagName);
			 map.put("type", type);
			 list = getSqlMapClientTemplate().queryForList("findTaggroupByTagName",map);
			 if(null==list||list.size()==0){
				 list = new ArrayList<Tag>();
			 }
		 }catch (Exception e) {
			 LOG.error(e.getMessage(),e.fillInStackTrace());
			 list = new ArrayList<Tag>();
		}
		return list;
	}

}
