package com.poison.ucenter.client.impl;

import java.util.List;

import com.keel.utils.UKeyWorker;
import com.poison.ucenter.client.GroupFacade;
import com.poison.ucenter.model.Group;
import com.poison.ucenter.model.GroupBlackList;
import com.poison.ucenter.model.GroupUser;
import com.poison.ucenter.service.GroupService;

public class GroupFacadeImpl implements GroupFacade{

	private GroupService groupService;
	private UKeyWorker reskeyWork;
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	@Override
	public Group insertintoGroup(String groupid, long uid, String name,
			String imageurl, String tags, int amount, String intro) {
		Group group = new Group();
		group.setGroupid(groupid);
		group.setUid(uid);
		group.setName(name);
		group.setImageurl(imageurl);
		group.setTags(tags);
		group.setAmount(amount);
		group.setIntro(intro);
		group.setCreattime(System.currentTimeMillis());
		group.setUpdatetime(System.currentTimeMillis());
		return groupService.insertintoGroup(group);
	}
	@Override
	public Group findGroup(String groupid) {
		return groupService.findGroup(groupid);
	}
	@Override
	public List<Group> getUserGroups(Long uid) {
		return groupService.getUserGroups(uid);
	}
	@Override
	public Group updateGroup(String groupid, long uid, String name,
			String imageurl, String tags, String intro) {
		Group group = new Group();
		group.setGroupid(groupid);
		group.setUid(uid);
		group.setName(name);
		group.setImageurl(imageurl);
		group.setTags(tags);
		group.setIntro(intro);
		group.setUpdatetime(System.currentTimeMillis());
		return groupService.updateGroup(group);
	}
	@Override
	public Group deleteGroup(String groupid,long uid) {
		Group group = new Group();
		group.setGroupid(groupid);
		group.setUid(uid);
		group.setIsDel(1);
		group.setUpdatetime(System.currentTimeMillis());
		return groupService.deleteGroup(group);
	}
	@Override
	public GroupUser insertintoGroupUser(String groupid, long uid) {
		GroupUser groupUser = new GroupUser();
		groupUser.setGroupid(groupid);
		groupUser.setUid(uid);
		groupUser.setCreattime(System.currentTimeMillis());
		groupUser.setUpdatetime(System.currentTimeMillis());
		long id = reskeyWork.getId();
		groupUser.setId(id);
		return groupService.insertintoGroupUser(groupUser);
	}
	@Override
	public GroupUser findGroupUserByGUid(String groupid, long uid) {
		return groupService.findGroupUserByGUid(groupid, uid);
	}
	@Override
	public List<GroupUser> findGroupUser(String groupid) {
		return groupService.findGroupUser(groupid);
	}
	@Override
	public GroupUser deleteGroupUser(String groupid, long uid) {
		GroupUser groupUser = new GroupUser();
		groupUser.setGroupid(groupid);
		groupUser.setUid(uid);
		groupUser.setUpdatetime(System.currentTimeMillis());
		groupUser.setIsDel(1);
		return groupService.deleteGroupUser(groupUser);
	}
	@Override
	public GroupBlackList insertintoGroupBlacklist(String groupid, long uid) {
		GroupBlackList groupBlackList = new GroupBlackList();
		groupBlackList.setGroupid(groupid);
		groupBlackList.setUid(uid);
		return groupService.insertintoGroupBlacklist(groupBlackList);
	}
	@Override
	public GroupBlackList findGroupBlacklistByGUid(String groupid, long uid) {
		return groupService.findGroupBlacklistByGUid(groupid, uid);
	}
	@Override
	public List<GroupBlackList> findGroupBlacklist(String groupid) {
		return groupService.findGroupBlacklist(groupid);
	}
	@Override
	public GroupBlackList deleteGroupBlacklist(String groupid, long uid) {
		GroupBlackList groupBlackList = new GroupBlackList();
		groupBlackList.setGroupid(groupid);
		groupBlackList.setUid(uid);
		groupBlackList.setIsDel(1);
		groupBlackList.setUpdatetime(System.currentTimeMillis());
		return groupService.deleteGroupBlacklist(groupBlackList);
	}
}