package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.domain.repository.MyBkDomainRepository;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.ResReport;
import com.poison.resource.model.Tag;
import com.poison.resource.model.TagCategory;
import com.poison.resource.model.UserTag;
import com.poison.resource.service.MyBkService;

public class MyBkServiceImpl implements MyBkService{

	private MyBkDomainRepository myBkDomainRepository;

	public void setMyBkDomainRepository(MyBkDomainRepository myBkDomainRepository) {
		this.myBkDomainRepository = myBkDomainRepository;
	}

	/**
	 * 插入一本书
	 */
	@Override
	public int addMyBkInfo(MyBk myBk) {
		return myBkDomainRepository.addMyBkInfo(myBk);
	}

	/**
	 * 查询这本书是否已经存在库中
	 */
	@Override
	public MyBk findMyBkIsExist(MyBk myBk) {
		return myBkDomainRepository.findMyBkIsExist(myBk);
	}

	/**
	 * 查询自己库中的书
	 */
	@Override
	public List<MyBk> findMyBkList(long userId, String name) {
		return myBkDomainRepository.findMyBkList(userId, name);
	}

	/**
	 * 根据ID查询自己库中一本书的信息
	 */
	@Override
	public MyBk findMyBkInfo(int id) {
		return myBkDomainRepository.findMyBkInfo(id);
	}

	/**
	 * 根据id查询tag
	 */
	@Override
	public Tag findTagById(long id) {
		return myBkDomainRepository.findTagById(id);
	}

	/**
	 * 根据type查询tag
	 */
	@Override
	public List<Tag> findTagListByType(String type) {
		return myBkDomainRepository.findTagListByType(type);
	}
	/**
	 * 
	 * <p>Description: 根据type查询标签列表按自增id倒序</p> 
	 */
	@Override
	public List<Tag> findTagByTypeOrderById(String type){
		return myBkDomainRepository.findTagByTypeOrderById(type);
	}
	
	/**
	 * 插入用户标签
	 */
	@Override
	public List<UserTag> insertUserTag(UserTag userTag) {
		return myBkDomainRepository.insertUserTag(userTag);
	}

	/**
	 * 查询用户的历史标签
	 */
	@Override
	public List<UserTag> findUserHistoryTagListByUid(long userId) {
		return myBkDomainRepository.findUserHistoryTagListByUid(userId);
	}

	/**
	 * 查询用户的常用标签
	 */
	@Override
	public List<UserTag> findUserFavoriteTagListByUid(long userId) {
		return myBkDomainRepository.findUserFavoriteTagListByUid(userId);
	}

	/**
	 * 插入举报信息
	 */
	@Override
	public ResReport insertResReport(ResReport report) {
		return myBkDomainRepository.insertResReport(report);
	}

	/**
	 * 查询类别
	 */
	@Override
	public List<TagCategory> findTagCategoryByLevel() {
		return myBkDomainRepository.findTagCategoryByLevel();
	}

	/**
	 * 查询热门标签
	 */
	@Override
	public List<Tag> findHotTagListByTagGroup(String tagGroup) {
		return myBkDomainRepository.findHotTagListByTagGroup(tagGroup);
	}

	/**
	 * 查询全部标签
	 */
	@Override
	public List<Tag> findAllTagListByTagGroup(String tagGroup) {
		return myBkDomainRepository.findAllTagListByTagGroup(tagGroup);
	}

	/**
	 * 根据type查询类别
	 */
	@Override
	public List<TagCategory> findTagCategoryByType(String type) {
		return myBkDomainRepository.findTagCategoryByType(type);
	}

	@Override
	public List<String> selectSensitiveWord(Map<String, Object> map) {
		return myBkDomainRepository.selectSensitiveWord(map);
	}

	/**
	 * 根据标签名字查询标签分组
	 */
	@Override
	public Tag findTaggroupByTagName(String tagName, String type) {
		return myBkDomainRepository.findTaggroupByTagName(tagName, type);
	}

}
