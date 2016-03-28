package com.poison.ucenter.domain.repository;

import java.util.Random;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.dao.ThirdPartyDAO;
import com.poison.ucenter.dao.UserInfoDAO;
import com.poison.ucenter.model.ThirdPartyLogin;
import com.poison.ucenter.model.UserInfo;

public class ThirdPartyDomainRepository {

	private ThirdPartyDAO thirdPartyDAO;
	private UserInfoDAO userInfoDAO;
	private UKeyWorker reskeyWork;
	private UcenterFacade ucenterFacade;
	
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}


	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}


	public void setUserInfoDAO(UserInfoDAO userInfoDAO) {
		this.userInfoDAO = userInfoDAO;
	}



	public void setThirdPartyDAO(ThirdPartyDAO thirdPartyDAO) {
		this.thirdPartyDAO = thirdPartyDAO;
	}

	/**
	 * 
	 * <p>Title: insertThirdParty</p> 
	 * <p>Description: 插入第三方登录信息</p> 
	 * @author :changjiang
	 * date 2014-12-17 上午11:37:14
	 * @param thirdParty
	 * @return
	 */
	public ThirdPartyLogin insertThirdParty(ThirdPartyLogin thirdParty,long uid){
		String openid = thirdParty.getOpenId();
		String loginSource = thirdParty.getLoginSource();
		int flag = ResultUtils.ERROR;
		long sysdate = System.currentTimeMillis();
		//查找是否存在
		ThirdPartyLogin thirdPartyLogin= thirdPartyDAO.findThirdPartyByOpenIdAndLoginResource(openid, loginSource);
		flag = thirdPartyLogin.getFlag();
		//查找用户是否已在用户表中
		long userId = thirdPartyLogin.getUserId();
		if(0!=userId){
			UserInfo  userInfo = userInfoDAO.findUserInfo(null, userId);
			if(null==userInfo){
				flag = ResultUtils.DATAISNULL;
			}
		}
		//当不存在时插入这条信息
		if(ResultUtils.DATAISNULL==flag){
			UserInfo userInfo = new UserInfo();
			
			String name = thirdParty.getNickName();
			if(name.length()>8){//当用户名字长度大于8时
				name = name.substring(0,8);
			}
			UserInfo uInfo = userInfoDAO.findUserInfoByName(name);
			if(null==uInfo||uInfo.getUserId()==0){//当不存在这个昵称的时候
				userInfo.setName(name);
			}else{
				userInfo.setName(changeName(thirdParty.getNickName()));
			}
			//第三方登录性别为3
			userInfo.setSex("3");
			userInfo.setFaceAddress(thirdParty.getHeadImgUrl());
			userInfo.setResidence(thirdParty.getLocation());
			userInfo.setLoginName(reskeyWork.getId()+"");
			userInfo.setPassword(reskeyWork.getId()+"");
			userInfo.setPasswordMd5("");
			userInfo.setMobilePhone(reskeyWork.getId()+"");
			userInfo.setBirthday(sysdate);
			userInfo.setTwoDimensionCode("");
			userInfo.setLevel(0);
			userInfo.setIp("");
			userInfo.setCreateDate(sysdate);
			userInfo.setLastestLoginDate(sysdate);
			userInfo.setLastestRevisionDate(sysdate);
			userInfo.setPushToken(thirdParty.getPushToken());
			userInfo.setAffectiveStates("");
			userInfo.setProfession("");
			userInfo.setAge("");
			userInfo.setConstellation("");
			if(0==uid){
				userId = userInfoDAO.insertUserInfo(null, userInfo);
			}else{
				//更新用户头像
				ucenterFacade.editUserInfo(uid,thirdParty.getHeadImgUrl(),thirdParty.getNickName(),"3","","","","","","",0,"","");
				userId = uid;
			}

			
			ucenterFacade.saveUserLatestInfo(userId, 0, "");
			thirdParty.setUserId(userId);
			flag = thirdPartyDAO.insertThirdParty(thirdParty);
			thirdPartyLogin= thirdPartyDAO.findThirdPartyByOpenIdAndLoginResource(openid, loginSource);
		}//当存在时，更新这条信息
		else if(ResultUtils.SUCCESS==flag){
			flag = thirdPartyDAO.updateThirdParty(thirdPartyLogin);
			thirdPartyLogin= thirdPartyDAO.findThirdPartyByOpenIdAndLoginResource(openid, loginSource);
			thirdPartyLogin.setState(ResultUtils.EXISTED_THIRD_PARTY);
//			if(0!=uid){
//				//更新用户头像
//				ucenterFacade.editUserInfo(thirdPartyLogin.getUserId(),thirdParty.getHeadImgUrl(),thirdParty.getNickName(),"3","","","","","","",0,"","");
//			}
		}
		return thirdPartyLogin;
	}
	
	public String changeName(String name){
		if(null==name||"".equals(name)){
			return name;
		}
		Random random = new Random();
		/*int i = (int) (Math.random()*100)%24;
		int m = (int) (Math.random()*100)%24;
		char j = 'a';
		char k = (char) (j+i);
		char n =  (char) (j+m);
		String temp = name.substring(name.length()/2,name.length());
		name = name.replace(temp, "***");*/
		name = name.substring(0,name.length()/2)+random.nextInt(10000);
		return name;
	}
	
	/*public String changeName(String name){
		if(null==name||"".equals(name)){
			return name;
		}
		int i = (int) (Math.random()*100)%24;
		int n = (int) (Math.random()*100)%24;
		char j = 'a';
		char k = (char) (j+i);
		char m = (char) (j+n);
		name = name+k+m;
		return name;
	}*/
	
	public ThirdPartyLogin findThirdPartyByOpenIdAndLoginResource(
			String openId, String loginResource){
		return thirdPartyDAO.findThirdPartyByOpenIdAndLoginResource(openId, loginResource);
	}
}
