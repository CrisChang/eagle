package com.poison.ucenter.service.impl;

import java.util.List;

import com.poison.ucenter.domain.repository.GroupDomainRepository;
import com.poison.ucenter.model.Group;
import com.poison.ucenter.model.GroupBlackList;
import com.poison.ucenter.model.GroupUser;
import com.poison.ucenter.service.GroupService;

public class GroupServiceImpl implements GroupService {

	private GroupDomainRepository groupDomainRepository;

	public GroupDomainRepository getGroupDomainRepository() {
		return groupDomainRepository;
	}

	public void setGroupDomainRepository(
			GroupDomainRepository groupDomainRepository) {
		this.groupDomainRepository = groupDomainRepository;
	}

	@Override
	public Group insertintoGroup(Group group) {
		return groupDomainRepository.insertintoGroup(group);
	}

	@Override
	public Group findGroup(String groupid) {
		return groupDomainRepository.findGroup(groupid);
	}
	
	@Override
	public List<Group> getUserGroups(Long uid) {
		return groupDomainRepository.getUserGroups(uid);
	}
	
	@Override
	public Group updateGroup(Group group) {
		return groupDomainRepository.updateGroup(group);
	}

	@Override
	public Group deleteGroup(Group group) {
		return groupDomainRepository.deleteGroup(group);
	}

	@Override
	public GroupUser insertintoGroupUser(GroupUser groupUser) {
		return groupDomainRepository.insertintoGroupUser(groupUser);
	}

	@Override
	public GroupUser findGroupUserByGUid(String groupid, long uid) {
		return groupDomainRepository.findGroupUserByGUid(groupid, uid);
	}

	@Override
	public List<GroupUser> findGroupUser(String groupid) {
		return groupDomainRepository.findGroupUser(groupid);
	}

	@Override
	public GroupUser deleteGroupUser(GroupUser groupUser) {
		return groupDomainRepository.deleteGroupUser(groupUser);
	}

	@Override
	public GroupBlackList insertintoGroupBlacklist(GroupBlackList groupBlackList) {
		return groupDomainRepository.insertintoGroupBlacklist(groupBlackList);
	}

	@Override
	public GroupBlackList findGroupBlacklistByGUid(String groupid, long uid) {
		return groupDomainRepository.findGroupBlacklistByGUid(groupid, uid);
	}

	@Override
	public List<GroupBlackList> findGroupBlacklist(String groupid) {
		return groupDomainRepository.findGroupBlacklist(groupid);
	}

	@Override
	public GroupBlackList deleteGroupBlacklist(GroupBlackList groupBlackList) {
		return groupDomainRepository.deleteGroupBlacklist(groupBlackList);
	}
}
