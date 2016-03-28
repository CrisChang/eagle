package com.poison.eagle.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TalentZoneInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.TalentZone;
import com.poison.ucenter.model.TalentZoneLink;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class TalentUtils {
	private static TalentUtils taletUtils;
	public TalentUtils(){}
	public static TalentUtils getInstance(){
		if(taletUtils == null){
			return new TalentUtils();
		}else{
			return taletUtils;
		}
	}
	
	private FileUtils fileUtils = FileUtils.getInstance();
	
	/**
	 * 将达人圈转换为可读类型
	 * @param talentZone
	 * @param type 0:需要加圈内人，1：不需要
	 * @return
	 */
	public TalentZoneInfo putTaletZoneToInfo(TalentZone talentZone , UcenterFacade ucenterFacade,int type){
		TalentZoneInfo talentZoneInfo = new TalentZoneInfo();
		
		talentZoneInfo.setId(talentZone.getId());
		talentZoneInfo.setName(talentZone.getZoneName());
		talentZoneInfo.setLogo(talentZone.getLogoAddress());
		talentZoneInfo.setSign(talentZone.getZoneSign());
		talentZoneInfo.setDescription(talentZone.getZoneDescription());
		talentZoneInfo.setType(talentZone.getType());
		
		List<TalentZoneLink> talentZoneLinks = ucenterFacade.findTalentZoneLinkList(talentZone.getId());
		//圈内userSize
		if(talentZoneLinks.size()>0){
			talentZoneInfo.setSize(talentZoneLinks.size());
		}
		
		if(type == 0){
			List<UserEntity> userEntities = putLinkToUserListForTalent(talentZoneLinks, ucenterFacade);
			//圈内userList
			talentZoneInfo.setUserList(userEntities);
			//达人的领袖
			UserEntity userEntity = putLeaderToTalent(talentZoneLinks, ucenterFacade);
			talentZoneInfo.setLeader(userEntity);
		}
		
		return talentZoneInfo;
	}
	
	/**
	 * 将领袖圈关系link转换为userlist
	 * @param talentZoneLinks
	 * @param ucenterFacade
	 * @return
	 */
	public List<UserEntity> putLinkToUserListForTalent(List<TalentZoneLink> talentZoneLinks ,UcenterFacade ucenterFacade){
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		Iterator<TalentZoneLink> iter = talentZoneLinks.iterator();
		while(iter.hasNext()){
			TalentZoneLink link = iter.next();
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, link.getUserId());
			if(userAllInfo.getLevel() == CommentUtils.USER_LEVEL_TALENT){
				UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, 0);
				userEntities.add(userEntity);
			}
			
		}
		return userEntities;
	}
	/**
	 * 将领袖的领袖分离出来
	 * @param talentZoneLinks
	 * @param ucenterFacade
	 * @return
	 */
	public UserEntity putLeaderToTalent(List<TalentZoneLink> talentZoneLinks ,UcenterFacade ucenterFacade){
		UserEntity userEntity = new UserEntity();
		Iterator<TalentZoneLink> iter = talentZoneLinks.iterator();
		while(iter.hasNext()){
			TalentZoneLink link = iter.next();
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, link.getUserId());
			if(userAllInfo.getLevel() == CommentUtils.USER_LEVEL_LEADER){
				userEntity = fileUtils.copyUserInfo(userAllInfo, 0);
			}
			
		}
		return userEntity;
	}
}
