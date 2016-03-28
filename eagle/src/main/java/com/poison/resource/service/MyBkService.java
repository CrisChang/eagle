package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.MyBk;
import com.poison.resource.model.ResReport;
import com.poison.resource.model.Tag;
import com.poison.resource.model.TagCategory;
import com.poison.resource.model.UserTag;

public interface MyBkService {

	/**
	 * 
	 * <p>Title: addMyBkInfo</p> 
	 * <p>Description: 插入一本书</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午2:53:12
	 * @param myBk
	 * @return
	 */
	public int addMyBkInfo(MyBk myBk);
	
	/**
	 * 
	 * <p>Title: findMyBkIsExist</p> 
	 * <p>Description: 查询这本书是否已经存在</p> 
	 * @author :changjiang
	 * date 2014-8-9 上午2:22:31
	 * @param myBk
	 * @return
	 */
	public MyBk findMyBkIsExist(MyBk myBk);
	
	/**
	 * 
	 * <p>Title: findMyBkList</p> 
	 * <p>Description: 查询自己库中的书</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午3:18:39
	 * @param userId
	 * @param name
	 * @return
	 */
	public List<MyBk> findMyBkList(long userId,String name);
	
	/**
	 * 
	 * <p>Title: findMyBkInfo</p> 
	 * <p>Description: 根据一本书查询书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午4:22:50
	 * @return
	 */
	public MyBk findMyBkInfo(int id);
	
	/**
	 * 
	 * <p>Title: findTagById</p> 
	 * <p>Description: 根据id查询tag</p> 
	 * @author :changjiang
	 * date 2014-11-19 下午1:20:12
	 * @param id
	 * @return
	 */
	public Tag findTagById(long id);
	
	/**
	 * 
	 * <p>Title: findTagListByType</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2014-11-19 下午1:20:29
	 * @param type
	 * @return
	 */
	public List<Tag> findTagListByType(String type);
	/**
	 * 
	 * <p>Description: 根据type查询标签列表按自增id倒序</p> 
	 */
	public List<Tag> findTagByTypeOrderById(String type);
	
	/**
	 * 
	 * <p>Title: insertUserTag</p> 
	 * <p>Description: 插入用户标签</p> 
	 * @author :changjiang
	 * date 2014-11-20 下午9:40:20
	 * @param userTag
	 * @return
	 */
	public List<UserTag> insertUserTag(UserTag userTag);
	
	/**
	 * 
	 * <p>Title: findUserHistoryTagListByUid</p> 
	 * <p>Description: 查询用户的历史标签</p> 
	 * @author :changjiang
	 * date 2014-11-21 下午1:52:26
	 * @param userId
	 * @return
	 */
	public List<UserTag> findUserHistoryTagListByUid(long userId);
	
	/**
	 * 
	 * <p>Title: findUserFavoriteTagListByUid</p> 
	 * <p>Description: 查询用户的常用标签</p> 
	 * @author :changjiang
	 * date 2014-11-21 下午1:53:17
	 * @param userId
	 * @return
	 */
	public List<UserTag> findUserFavoriteTagListByUid(long userId);
	
	/**
	 * 
	 * <p>Title: insertResReport</p> 
	 * <p>Description: 插入举报信息</p> 
	 * @author :changjiang
	 * date 2014-12-1 下午9:06:02
	 * @param report
	 * @return
	 */
	public ResReport insertResReport(ResReport report);
	
	/**
	 * 
	 * <p>Title: findTagCategoryByLevel</p> 
	 * <p>Description: 查询</p> 
	 * @author :changjiang
	 * date 2014-12-3 下午9:14:01
	 * @return
	 */
	public List<TagCategory> findTagCategoryByLevel();
	
	/**
	 * 
	 * <p>Title: findHotTagListByTagGroup</p> 
	 * <p>Description: 查询热门标签</p> 
	 * @author :changjiang
	 * date 2014-12-4 上午11:36:18
	 * @param tagGroup
	 * @return
	 */
	public List<Tag> findHotTagListByTagGroup(String tagGroup);
	
	/**
	 * 
	 * <p>Title: findAllTagListByTagGroup</p> 
	 * <p>Description: 查询全部标签</p> 
	 * @author :changjiang
	 * date 2014-12-4 上午11:36:43
	 * @param tagGroup
	 * @return
	 */
	public List<Tag> findAllTagListByTagGroup(String tagGroup);
	
	/**
	 * 
	 * <p>Title: findTagCategoryByType</p> 
	 * <p>Description: 根据type查询类别</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:13:34
	 * @param type
	 * @return
	 */
	public List<TagCategory> findTagCategoryByType(String type);

	/**
	 * 
	 * <p>Title: selectSensitiveWord</p> 
	 * <p>Description: 查询所有的敏感词汇</p> 
	 * @author :yandongzhe
	 * date 2014-12-25 下午5:14:35
	 * @return
	 */
	public List<String> selectSensitiveWord(Map<String, Object> map);
	
	/**
	 * 
	 * <p>Title: findTaggroupByTagName</p> 
	 * <p>Description: 根据标签名字查询标签分组</p> 
	 * @author :changjiang
	 * date 2015-1-19 下午2:37:22
	 * @param tagName
	 * @param type
	 * @return
	 */
	public Tag findTaggroupByTagName(String tagName, String type);
}
