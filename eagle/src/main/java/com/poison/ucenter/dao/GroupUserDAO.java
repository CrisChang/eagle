package com.poison.ucenter.dao;

import java.util.List;

import com.poison.ucenter.model.GroupUser;

public interface GroupUserDAO {

	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 增加群组成员</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public int insertintoGroupUser(GroupUser groupUser);
	
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public GroupUser findGroupUserByGUid(String groupid,long uid);
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public List<GroupUser> findGroupUser(String groupid);
	/**
	 * 删除一个群组成员
	 * @Title: deleteGroup 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-29 下午2:34:26
	 * @param @param groupUser
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int deleteGroupUser(GroupUser groupUser);
	/**
	 * 根据用户id查询
	 */
	public List<GroupUser> findGroupUserByUserid(Long uid);
}