package com.poison.ucenter.dao;

import java.util.List;

import com.poison.ucenter.model.GroupBlackList;

public interface GroupBlackListDAO {
	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 增加群组黑名单</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public int insertintoGroupBlacklist(GroupBlackList groupBlackList);
	
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
	public int deleteGroupBlacklist(GroupBlackList groupBlackList);
}