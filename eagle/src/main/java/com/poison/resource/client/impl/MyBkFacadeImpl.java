package com.poison.resource.client.impl;

import java.util.List;
import java.util.Map;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.ResReport;
import com.poison.resource.model.Tag;
import com.poison.resource.model.TagCategory;
import com.poison.resource.model.UserTag;
import com.poison.resource.service.MyBkService;

public class MyBkFacadeImpl implements MyBkFacade{

	private MyBkService myBkService;
	private UKeyWorker reskeyWork;
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}


	public void setMyBkService(MyBkService myBkService) {
		this.myBkService = myBkService;
	}


	@Override
	public int addMyBkInfo(String title,String pages,String catalog,
			String isbn13,String image,String binding,String pubdate,String summary,String publisher,String price,String author,String isbn10,long userId) {
		MyBk myBk = new MyBk();
		myBk.setCreateId(userId);
		myBk.setBookUrl("");
		myBk.setBookPic(image);
		myBk.setName(title);
		myBk.setScore("");
		myBk.setAuthorName(author);
		myBk.setTranslator("");
		myBk.setPress("");
		myBk.setOriginalName("");
		myBk.setSubtitle("");
		myBk.setPublishingTime(pubdate);
		myBk.setNumber(Integer.valueOf(pages));
		myBk.setPrice(price);
		myBk.setBinding(binding);
		myBk.setSeriesName("");
		myBk.setTags("");
		myBk.setContent(summary);
		myBk.setAuthorInfo("");
		myBk.setCatalog(catalog);
		myBk.setSeriesInfo("");
		if(!"".equals(isbn13)){
			myBk.setIsbn(isbn13);
		}else{
			myBk.setIsbn(isbn10);
		}
		long systime = System.currentTimeMillis();
		myBk.setCollTime(systime);
		//int id = 
		/*if(id==ResultUtils.ERROR){
			myBk = new MyBk();
			myBk.setFlag(ResultUtils.ERROR);
			return myBk;
		}*/
		//MyBk bk = myBkService.findMyBkInfo(id);
		return myBkService.addMyBkInfo(myBk);
	}

	/**
	 * 查询自己库中的书
	 */
	@Override
	public List<MyBk> findMyBkList(long userId, String name) {
		return myBkService.findMyBkList(userId, name);
	}


	/**
	 * 从自己库中查询这本书信息
	 */
	@Override
	public MyBk findMyBkInfo(int id) {
		return myBkService.findMyBkInfo(id);
	}

	/**
	 * 根据id查询tag
	 */
	@Override
	public Tag findTagById(long id) {
		return myBkService.findTagById(id);
	}

	/**
	 * 根据type查询tag
	 */
	@Override
	public List<Tag> findTagListByType(String type) {
		return myBkService.findTagListByType(type);
	}
	
	/**
	 * 
	 * <p>Description: 根据type查询标签列表按自增id倒序</p> 
	 */
	@Override
	public List<Tag> findTagByTypeOrderById(String type){
		return myBkService.findTagByTypeOrderById(type);
	}

	/**
	 * 插入用户标签
	 */
	@Override
	public List<UserTag> insertUserTag(long userId,String tagName,String type) {
		UserTag userTag = new UserTag();
		long sysdate = System.currentTimeMillis();
		userTag.setUserId(userId);
		userTag.setTagId(0);
		userTag.setTagName(tagName);
		userTag.setIsDelete(0);
		userTag.setSelectCount(1);
		userTag.setType(type);
		userTag.setCreateDate(sysdate);
		userTag.setLatestRevisionDate(sysdate);
		return myBkService.insertUserTag(userTag);
	}

	/**
	 * 查询用户的历史标签
	 */
	@Override
	public List<UserTag> findUserHistoryTagListByUid(long userId) {
		return myBkService.findUserHistoryTagListByUid(userId);
	}

	/**
	 * 查询用户的
	 */
	@Override
	public List<UserTag> findUserFavoriteTagListByUid(long userId) {
		return myBkService.findUserFavoriteTagListByUid(userId);
	}

	/**
	 * 插入举报信息
	 */
	@Override
	public ResReport insertResReport(long userId,long resourceId,String type,String description) {
		ResReport resReport = new ResReport();
		long sysdate = System.currentTimeMillis();
		resReport.setId(reskeyWork.getId());
		resReport.setUserId(userId);
		resReport.setResourceId(resourceId);
		resReport.setType(type);
		resReport.setIsDelete(0);
		resReport.setDescription(description);
		resReport.setCreatedate(sysdate);
		return myBkService.insertResReport(resReport);
	}

	/**
	 * 查询分类类别
	 */
	@Override
	public List<TagCategory> findTagCategoryByLevel() {
		return myBkService.findTagCategoryByLevel();
	}


	/**
	 * 查询热门标签
	 */
	@Override
	public List<Tag> findHotTagListByTagGroup(String tagGroup) {
		return myBkService.findHotTagListByTagGroup(tagGroup);
	}

	/**
	 * 查询全部标签
	 */
	@Override
	public List<Tag> findAllTagListByTagGroup(String tagGroup) {
		return myBkService.findAllTagListByTagGroup(tagGroup);
	}

	/**
	 * 根据type查询类别
	 */
	@Override
	public List<TagCategory> findTagCategoryByType(String type) {
		return myBkService.findTagCategoryByType(type);
	}


	@Override
	public List<String> selectSensitiveWord(Map<String,Object> map) {
		return myBkService.selectSensitiveWord(map);
	}

	/**
	 * 根据标签名字查询标签分组
	 */
	@Override
	public Tag findTaggroupByTagName(String tagName, String type) {
		return myBkService.findTaggroupByTagName(tagName, type);
	}


}
