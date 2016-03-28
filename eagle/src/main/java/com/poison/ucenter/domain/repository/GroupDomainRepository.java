package com.poison.ucenter.domain.repository;

import java.util.ArrayList;
import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.GroupBlackListDAO;
import com.poison.ucenter.dao.GroupDAO;
import com.poison.ucenter.dao.GroupUserDAO;
import com.poison.ucenter.model.Group;
import com.poison.ucenter.model.GroupBlackList;
import com.poison.ucenter.model.GroupUser;

public class GroupDomainRepository {
	
	private GroupDAO groupDAO;
	private GroupBlackListDAO groupBlackListDAO;
	private GroupUserDAO groupUserDAO;

	public GroupDAO getGroupDAO() {
		return groupDAO;
	}
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}
	public GroupBlackListDAO getGroupBlackListDAO() {
		return groupBlackListDAO;
	}
	public void setGroupBlackListDAO(GroupBlackListDAO groupBlackListDAO) {
		this.groupBlackListDAO = groupBlackListDAO;
	}
	public GroupUserDAO getGroupUserDAO() {
		return groupUserDAO;
	}
	public void setGroupUserDAO(GroupUserDAO groupUserDAO) {
		this.groupUserDAO = groupUserDAO;
	}


	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 插入群组</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public Group insertintoGroup(Group group){
		int flag = groupDAO.insertintoGroup(group);
		if(flag == ResultUtils.SUCCESS){
			return groupDAO.findGroup(group.getGroupid());
		}
		group.setFlag(flag);
		return group;
	}
	
	/**
	 * 
	 * <p>Title: findGroup</p> 
	 * <p>Description: 查询群组信息根据群组id</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public Group findGroup(String groupid){
		return groupDAO.findGroup(groupid);
	}
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
	public List<Group> getUserGroups(Long uid){
		List<Group> groups = groupDAO.findGroupsByUserid(uid);
		List<GroupUser> groupUsers = groupUserDAO.findGroupUserByUserid(uid);
		//需要去除已经查出来的groupidid
		int size1 = 0;
		if(groups!=null && groups.size()>0){
			size1 = groups.size();
		}
		List<String> exists = new ArrayList<String>(size1);
		for(int i=0;i<size1;i++){
			String groupid = groups.get(i).getGroupid();
			if(groupid!=null){
				exists.add(groupid);
			}
		}
		int size2 = 0;
		if(groupUsers!=null && groupUsers.size()>0){
			size2 = groupUsers.size();
		}
		List<String> groupids = new ArrayList<String>(size2);
		for(int i=0;i<size2;i++){
			String groupid = groupUsers.get(i).getGroupid();
			if(groupid!=null && !exists.contains(groupid)){
				groupids.add(groupid);
			}
		}
		List<Group> addgroups = groupDAO.findGroupsByIds(groupids);
		int size3 = 0;
		if(addgroups!=null && addgroups.size()>0){
			size3 = addgroups.size();
		}
		List<Group> allgroups = new ArrayList<Group>(size1+size3);
		allgroups.addAll(groups);
		allgroups.addAll(addgroups);
		
		return allgroups;
	}
	
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
	public Group updateGroup(Group group){
		int flag = groupDAO.updateGroup(group);
		if(flag == ResultUtils.SUCCESS){
			return groupDAO.findGroup(group.getGroupid());
		}
		group.setFlag(flag);
		return group;
	}
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
	public Group deleteGroup(Group group){
		Group group2 = groupDAO.findGroup(group.getGroupid());
		int flag = groupDAO.deleteGroup(group);
		if(flag == ResultUtils.SUCCESS){
			group2.setIsDel(1);
			return group2;
		}
		group.setFlag(flag);
		return group;
	}
	
	
	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 增加群组成员</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public GroupUser insertintoGroupUser(GroupUser groupUser){
		//需要判断是否已经存在在群成员里面了，或者是在群黑名单中
		int flag = ResultUtils.ERROR;
		GroupUser groupUser2 = groupUserDAO.findGroupUserByGUid(groupUser.getGroupid(), groupUser.getUid());
		GroupBlackList groupBlackList = groupBlackListDAO.findGroupBlacklistByGUid(groupUser.getGroupid(), groupUser.getUid());
		if(groupUser2!=null && groupUser2.getId()>0){
			//群组中已经存在该用户了
			flag = ResultUtils.ERROR;
		}else if(groupBlackList!=null && groupBlackList.getId()>0){
			//该用户在该群组的黑名单中
			flag = ResultUtils.ERROR;
		}else{
			flag = groupUserDAO.insertintoGroupUser(groupUser);
			if(flag == ResultUtils.SUCCESS){
				return groupUserDAO.findGroupUserByGUid(groupUser.getGroupid(), groupUser.getUid());
			}
		}
		groupUser.setFlag(flag);
		return groupUser;
	}
	
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public GroupUser findGroupUserByGUid(String groupid,long uid){
		return groupUserDAO.findGroupUserByGUid(groupid, uid);
	}
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public List<GroupUser> findGroupUser(String groupid){
		return groupUserDAO.findGroupUser(groupid);
	}
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
	public GroupUser deleteGroupUser(GroupUser groupUser){
		GroupUser groupUser2 = groupUserDAO.findGroupUserByGUid(groupUser.getGroupid(), groupUser.getUid());
		int flag = groupUserDAO.deleteGroupUser(groupUser);
		if(flag == ResultUtils.SUCCESS){
			groupUser2.setIsDel(1);
			return groupUser2;
		}
		groupUser.setFlag(flag);
		return groupUser;
	}
	
	
	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 增加群组黑名单</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public GroupBlackList insertintoGroupBlacklist(GroupBlackList groupBlackList){
		//需要判断是否已经存在在群成员里面了，或者是在群黑名单中
		int flag = ResultUtils.ERROR;
		GroupUser groupUser = groupUserDAO.findGroupUserByGUid(groupBlackList.getGroupid(), groupBlackList.getUid());
		if(groupUser!=null && groupUser.getId()>0){
			//群组中存在该用户，需要移除
			groupUserDAO.deleteGroupUser(groupUser);
		}
		GroupBlackList groupBlackList2 = groupBlackListDAO.findGroupBlacklistByGUid(groupBlackList.getGroupid(), groupBlackList.getUid());
		if(groupBlackList2!=null && groupBlackList2.getId()>0){
			//已经存在在黑名单了
			flag = ResultUtils.ERROR;
		}else{
			flag = groupBlackListDAO.insertintoGroupBlacklist(groupBlackList);
			if(flag == ResultUtils.SUCCESS){
				return groupBlackListDAO.findGroupBlacklistByGUid(groupBlackList.getGroupid(), groupBlackList.getUid());
			}
		}
		groupBlackList.setFlag(flag);
		return groupBlackList;
	}
	
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public GroupBlackList findGroupBlacklistByGUid(String groupid,long uid){
		return groupBlackListDAO.findGroupBlacklistByGUid(groupid, uid);
	}
	/**
	 * 
	 * <p>Title: findGroupUserByGUid</p> 
	 * <p>Description: 根据群组id和成员id查询</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public List<GroupBlackList> findGroupBlacklist(String groupid){
		return groupBlackListDAO.findGroupBlacklist(groupid);
	}
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
	public GroupBlackList deleteGroupBlacklist(GroupBlackList groupBlackList){
		GroupBlackList groupBlackList2 = groupBlackListDAO.findGroupBlacklistByGUid(groupBlackList.getGroupid(), groupBlackList.getUid());
		int flag = groupBlackListDAO.deleteGroupBlacklist(groupBlackList);
		if(flag == ResultUtils.SUCCESS){
			groupBlackList2.setIsDel(1);
			return groupBlackList2;
		}
		groupBlackList.setFlag(flag);
		return groupBlackList;
	}
}
