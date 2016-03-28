package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.UserTag;

public interface UserTagDAO {

	/**
	 * 
	 * <p>Title: insertUserTag</p> 
	 * <p>Description: 插入用户标签</p> 
	 * @author :changjiang
	 * date 2014-11-20 下午9:07:10
	 * @param userTag
	 * @return
	 */
	public int insertUserTag(UserTag userTag);
	
	/**
	 * 
	 * <p>Title: queryUserTagById</p> 
	 * <p>Description: 根据id查询用户标签</p> 
	 * @author :changjiang
	 * date 2014-11-20 下午9:29:49
	 * @param id
	 * @return
	 */
	public UserTag queryUserTagById(long id);
	
	/**
	 * 
	 * <p>Title: findUserTagByTagName</p> 
	 * <p>Description: 根据标签名字查询用户标签</p> 
	 * @author :changjiang
	 * date 2014-11-20 下午10:36:26
	 * @return
	 */
	public UserTag findUserTagByTagName(long userId,String tagName);
	
	/**
	 * 
	 * <p>Title: findUserTagList</p> 
	 * <p>Description: 查询用户的标签列表</p> 
	 * @author :changjiang
	 * date 2014-12-2 下午1:59:07
	 * @param userId
	 * @return
	 */
	public List<UserTag> findUserTagList(long userId);
	
	/**
	 * 
	 * <p>Title: updateUserTagCount</p> 
	 * <p>Description: 更新用户标签总数</p> 
	 * @author :changjiang
	 * date 2014-11-20 下午10:45:25
	 * @param id
	 * @return
	 */
	public int updateUserTagCount(long id);
	
	/**
	 * 
	 * <p>Title: deleteUserTag</p> 
	 * <p>Description: 删除用户标签</p> 
	 * @author :changjiang
	 * date 2014-12-2 下午3:05:09
	 * @param id
	 * @return
	 */
	public int deleteUserTag(long id);
	
	/**
	 * 
	 * <p>Title: updateUserTagCountAndIsDel</p> 
	 * <p>Description: 增加次数并且更改状态位</p> 
	 * @author :changjiang
	 * date 2014-12-2 下午5:42:01
	 * @param id
	 * @return
	 */
	public int updateUserTagCountAndIsDel(long id);
	
	/**
	 * 
	 * <p>Title: findUserTagListByUid</p> 
	 * <p>Description: 查询用户的历史记录标签列表</p> 
	 * @author :changjiang
	 * date 2014-11-21 上午11:54:02
	 * @param userId
	 * @return
	 */
	public List<UserTag> findUserHistoryTagListByUid(long userId);
	
	/**
	 * 
	 * <p>Title: findUserFavoriteTagListByUid</p> 
	 * <p>Description: 查询用户的喜爱标签列表</p> 
	 * @author :changjiang
	 * date 2014-11-21 上午11:57:25
	 * @param userId
	 * @return
	 */
	public List<UserTag> findUserFavoriteTagListByUid(long userId);
}
