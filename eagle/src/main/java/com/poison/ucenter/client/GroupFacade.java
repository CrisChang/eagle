package com.poison.ucenter.client;

import java.util.List;

import com.poison.ucenter.model.Group;
import com.poison.ucenter.model.GroupBlackList;
import com.poison.ucenter.model.GroupUser;

public interface GroupFacade {

	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 插入群组</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public Group insertintoGroup(String groupid,long uid,String name,String imageurl,String tags,int amount,String intro);
	
	/**
	 * 
	 * <p>Title: findGroup</p> 
	 * <p>Description: 查询群组信息根据群组id</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public Group findGroup(String groupid);
	/**
	 * 查询某个人加入的群组列表
	 * @Title: getUserGroups 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-4 下午3:37:42
	 * @param @param uid
	 * @param @return
	 * @return List<Group>
	 * @throws
	 */
	public List<Group> getUserGroups(Long uid);
	/**
	 * 更新群组
	 * @Title: updateGroup 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-29 下午2:33:38
	 * @param @param group
	 * @param @return
	 * @return int
	 * @throws
	 */
	public Group updateGroup(String groupid,long uid,String name,String imageurl,String tags,String intro);
	/**
	 * 删除一个群组
	 * @Title: deleteGroup 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-29 下午2:34:26
	 * @param @param group
	 * @param @return
	 * @return int
	 * @throws
	 */
	public Group deleteGroup(String groupid,long uid);
	
	
	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 增加群组成员</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public GroupUser insertintoGroupUser(String groupid,long uid);
	
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
	public GroupUser deleteGroupUser(String groupid,long uid);
	
	
	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 增加群组黑名单</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public GroupBlackList insertintoGroupBlacklist(String groupid,long uid);
	
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public GroupBlackList findGroupBlacklistByGUid(String groupid,long uid);
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public List<GroupBlackList> findGroupBlacklist(String groupid);
	/**
	 * 删除一个群组黑名单
	 * @Title: deleteGroup 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-29 下午2:34:26
	 * @param @param groupUser
	 * @param @return
	 * @return int
	 * @throws
	 */
	public GroupBlackList deleteGroupBlacklist(String groupid,long uid);
}
