package com.poison.ucenter.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.keel.framework.runtime.ProductContext;
import com.poison.eagle.entity.VersionInfo;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.domain.repository.UcenterDomainRepository;
import com.poison.ucenter.model.TalentZone;
import com.poison.ucenter.model.TalentZoneLink;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;
import com.poison.ucenter.model.UserLatest;
import com.poison.ucenter.model.UserSummary;
import com.poison.ucenter.service.UcenterService;

public class UcenterServiceImpl implements UcenterService{

	private UcenterDomainRepository ucenterDomainRepository;
	
	public void setUcenterDomainRepository(
			UcenterDomainRepository ucenterDomainRepository) {
		this.ucenterDomainRepository = ucenterDomainRepository;
	}
	/**
	 * 查询用户的所有信息
	 */
	@Override
	public UserInfo findUserInfo(ProductContext productContext,long userId) {
		UserInfo userInfo = new UserInfo();
		userInfo = ucenterDomainRepository.findUserInfo(productContext,userId);
		return userInfo;
	}

	/**
	 * 更新
	 */
	@Override
	public int updateUserInfo(ProductContext productContext,UserInfo userInfo) {
		return ucenterDomainRepository.updateUserInfo(productContext,userInfo);
	}
	
	/**
	 * 添加用户基本信息
	 */
	@Override
	public void insertintoUserInfo(ProductContext productContext,UserInfo userInfo) {
		ucenterDomainRepository.insertintoUserInfo(productContext,userInfo);
	}
	
	/** 
	 * 查询用户关注人列表
	 */
	@Override
	public List<UserInfo> findUserAttention(ProductContext productContext,long userId,int count,int pageIndex,int pageSize) {
		 List<UserInfo> userAttention=ucenterDomainRepository.findUserAttention(productContext,userId,count,pageIndex, pageSize);
		//相互关注的人信息表
		List<UserInfo> userAttentionEachList = ucenterDomainRepository.findUserAttentionEachOther(userId);
		userAttention.removeAll(userAttentionEachList);
		 return userAttention;
	}
	
	/**
	 * 查询用户粉丝列表
	 */
	@Override
	public List<UserInfo> findUserFens(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize) {
		List<UserInfo> userFens =ucenterDomainRepository.findUserFens(productContext,userAttentionId,count,pageIndex, pageSize);
		//相互关注的人信息表
		List<UserInfo> userAttentionEachList = ucenterDomainRepository.findUserAttentionEachOther(userAttentionId);
		userFens.removeAll(userAttentionEachList);
		return userFens;
	}
	
	/**
	 * 插入用户简介
	 */
	@Override
	public int insertUserSummary(ProductContext productContext,
			UserSummary userSummary) {
		return ucenterDomainRepository.insertUserSummary(productContext, userSummary);
	}
	
	/**
	 * 更新用户简介
	 */
	@Override
	public int updateUserSummary(ProductContext productContext,
			UserSummary userSummary) {
		return ucenterDomainRepository.updateUserSummary(productContext, userSummary);
	}
	
	/**
	 * 查询用户简介
	 */
	@Override
	public UserSummary findUserSummaryByUserid(ProductContext productContext,
			long userId) {
		return ucenterDomainRepository.findUserSummaryById(productContext, userId);
	}
	
	/**
	 * 根据手机号码注册
	 */
	@Override
	public UserInfo registUserByMobilePhone(ProductContext productContext,
			UserInfo userInfo) {
		return ucenterDomainRepository.registUserInfoByMobilePhone(productContext, userInfo);
	}
	
	/**
	 * 根据用户名注册
	 */
	@Override
	public UserInfo registUserByLoginName(ProductContext productContext,
			UserInfo userInfo) {
		return ucenterDomainRepository.registUserInfoByLoginName(productContext, userInfo);
	}
	
	/**
	 * 手机号码登录
	 */
	@Override
	public UserAllInfo checkLoginByMobile(ProductContext productContext,
			String mobilePhone, String password) {
		return ucenterDomainRepository.checkLoginByMobilePhone(productContext, mobilePhone, password);
	}
	
	/**
	 * 用户名密码登录
	 */
	@Override
	public UserAllInfo checkLoginByLoginName(ProductContext productContext,
			String loginName, String password) {
		return ucenterDomainRepository.checkLoginByLoginName(productContext, loginName, password);
	}
	
	/**
	 * 用户点击关注
	 */
	@Override
	public UserAttention doAttention(ProductContext productContext,
			UserAttention userAttention) {
		return ucenterDomainRepository.doAttention(productContext, userAttention);
	}
	
	/**
	 * 用户取消关注
	 */
	@Override
	public UserAttention cancelAttention(ProductContext productContext,
			UserAttention userAttention) {
		return ucenterDomainRepository.cancelAttention(productContext, userAttention);
	}
	
	/**
	 * 查询已经注册的列表
	 */
	@Override
	public List<UserInfo> findRegistListByMobileList(
			ProductContext productContext, List<String> mobilePhoneList) {
		return ucenterDomainRepository.findRegistListByMobileList(productContext, mobilePhoneList);
	}
	
	/**
	 * 查询未注册的列表
	 */
	@Override
	public List<UserInfo> findUnRegistListByMobileList(
			ProductContext productContext, List<String> mobilePhoneList) {
		//List<String> mobilePhoneClone = ArrayList<String>(mobilePhoneList);
		List<UserInfo> list = new ArrayList<UserInfo>();
		//用户已经注册的列表
		list = ucenterDomainRepository.findRegistListByMobileList(productContext, mobilePhoneList);
		List<String> mobileList = new ArrayList<String>();
		Iterator<UserInfo> iterator = list.iterator();
		//UserInfo userInfo2 = new UserInfo();
		while(iterator.hasNext()){
			UserInfo userInfo = iterator.next();
			//userInfo2.setMobilePhone(userInfo.getMobilePhone());
			mobileList.add(userInfo.getMobilePhone());
		}
		List<String> mobileClone = new ArrayList<String>(mobilePhoneList);
		mobileClone.removeAll(mobileList);
		
		List<UserInfo> unRegist = new ArrayList<UserInfo>();
		Iterator<String> iterator1 = mobileClone.iterator();
		String mobileStr = "";
		while(iterator1.hasNext()){
			mobileStr = iterator1.next();
			UserInfo userInfo = new UserInfo();
			userInfo.setMobilePhone(mobileStr);
			unRegist.add(userInfo);
		}
		//mobileClone = list(set(mobileClone)-set());
		//unRegist.removeAll(mobileList);
		return unRegist;
	}
	
	/**
	 * 查询通讯录中已关注的列表
	 */
	@Override
	public List<UserInfo> findAttentionByMobileList(
			ProductContext productContext, List<String> mobilePhoneList,long userId,int pageIndex,int pageSize) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		//用户已经注册的列表
		list = ucenterDomainRepository.findRegistListByMobileList(productContext, mobilePhoneList);
		//用户已经关注的列表
		List<UserInfo> attentionList = new ArrayList<UserInfo>();
		int count=0;
		//-----------------------------------------
	/*	long userId=1;
		int count=0;
		int pageIndex = 1;
		int pageSize = 5;*/
		attentionList = ucenterDomainRepository.findUserAttention(productContext, userId, count, pageIndex, pageSize);
		
		//通讯录中未关注
		List<UserInfo> attentionByMobile = new ArrayList<UserInfo>(list);
		//Iterator<UserInfo> iterator = attentionList.iterator();
/*		while(iterator.hasNext()){
			if(attentionByMobile.contains(iterator.next())){
				attentionByMobile.remove(iterator);
				iterator.remove();
			}
		}*/
		attentionByMobile.retainAll(attentionList);
		if(attentionByMobile.size()==0){
			UserInfo userInfo = new UserInfo();
			userInfo.setFlag(ResultUtils.DATAISNULL);
			attentionByMobile.add(userInfo);
		}
		//attentionByMobile.addAll(attentionList);
		return attentionByMobile;
	}
	
	/**
	 * 查询通讯录中未关注的列表
	 */
	@Override
	public List<UserInfo> findUnAttentionByMobileList(
			ProductContext productContext, List<String> mobilePhoneList,long userId,int pageIndex,int pageSize) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		//用户已经注册的列表
		list = ucenterDomainRepository.findRegistListByMobileList(productContext, mobilePhoneList);
		//用户已经关注列表
		List<UserInfo> attentionList = new ArrayList<UserInfo>();
		attentionList = findAttentionByMobileList(productContext,mobilePhoneList,userId,pageIndex,pageSize);
		list.removeAll(attentionList);
		if(null!=list && list.size()==0){
			UserInfo userInfo = new UserInfo();
			userInfo.setFlag(ResultUtils.DATAISNULL);
			list.add(userInfo);
		}
		return list;
	}
	
	/**
	 * 查询朋友关注的我未关注的人列表
	 */
	@Override
	public List<UserInfo> findFriendsUnAttentionList(
			ProductContext productContext, long userId,int count,int pageIndex,int pageSize) {
		//查找用户关注人列表
		List<UserInfo> userAttentionList = new ArrayList<UserInfo>();
		List<UserInfo> friendsAttentionUserList = new ArrayList<UserInfo>();
		userAttentionList = ucenterDomainRepository.findUserAttention(productContext, userId, count, pageIndex, pageSize);
		Iterator<UserInfo> userAttentionIterator = userAttentionList.iterator();
		UserInfo userInfo = new UserInfo();
		List<Long> userAttentionIdList = new ArrayList<Long>();
		while(userAttentionIterator.hasNext()){
			userInfo = userAttentionIterator.next();
			if(ResultUtils.DATAISNULL==userInfo.getFlag()){
				friendsAttentionUserList.add(userInfo);
				return friendsAttentionUserList;
			}
			userAttentionIdList.add(userInfo.getUserId());
		}
		//朋友关注人列表（去重）
		List<Long> friendsAttentionList = new ArrayList<Long>();
		friendsAttentionList = ucenterDomainRepository.findFriendsAttentionList(userAttentionIdList);
		//朋友关注人信息表
		
		friendsAttentionUserList = ucenterDomainRepository.findFriendsAttentionUser(friendsAttentionList);
		
		//我关注人的信息表
		friendsAttentionUserList.removeAll(userAttentionList);
		if(null==friendsAttentionUserList||friendsAttentionUserList.size()==0){
			userInfo.setFlag(ResultUtils.DATAISNULL);
			friendsAttentionUserList.add(userInfo);
		}
		return friendsAttentionUserList;
	}
	
	/**
	 * 朋友关注的我已经关注的人列表
	 */
	@Override
	public List<UserInfo> findFriendsIsAttentionList(
			ProductContext productContext, long userId, int count,
			int pageIndex, int pageSize) {
		//查找用户关注人列表
		List<UserInfo> userAttentionList = new ArrayList<UserInfo>();
		userAttentionList = ucenterDomainRepository.findUserAttention(productContext, userId, count, pageIndex, pageSize);
		Iterator<UserInfo> userAttentionIterator = userAttentionList.iterator();
		UserInfo userInfo = new UserInfo();
		List<Long> userAttentionIdList = new ArrayList<Long>();
		while(userAttentionIterator.hasNext()){
			userInfo = userAttentionIterator.next();
			userAttentionIdList.add(userInfo.getUserId());
		}
		//朋友关注人列表（去重）
		List<Long> friendsAttentionList = new ArrayList<Long>();
		friendsAttentionList = ucenterDomainRepository.findFriendsAttentionList(userAttentionIdList);
		/*Set<String> friendsAttentionSet = new HashSet<String>();
		Iterator<String> friendsAttentionIterator = friendsAttentionList.iterator();
		String userIdStr = "";
		while(friendsAttentionIterator.hasNext()){
			userIdStr = friendsAttentionIterator.next();
			friendsAttentionSet.add(userIdStr);
		}
		List<String> list1 = new ArrayList<String>();
		list1.addAll(friendsAttentionSet);*/
		//朋友关注人信息表
		List<UserInfo> friendsAttentionUserList = new ArrayList<UserInfo>();
		friendsAttentionUserList = ucenterDomainRepository.findFriendsAttentionUser(friendsAttentionList);
		
		friendsAttentionUserList.retainAll(userAttentionList);
		if(null==friendsAttentionUserList||friendsAttentionUserList.size()==0){
			userInfo.setFlag(ResultUtils.DATAISNULL);
			friendsAttentionUserList.add(userInfo);
		}
		//相互关注的人信息表
		List<UserInfo> userAttentionEachList = ucenterDomainRepository.findUserAttentionEachOther(userId);
		friendsAttentionUserList.removeAll(userAttentionEachList);
		return friendsAttentionUserList;
	}
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionEachList</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午2:25:23
	 * @return
	 */
	public List<UserInfo> findFriendsAttentionEachList(ProductContext productContext, long userId, int count,
			int pageIndex, int pageSize){
		//查找用户关注人列表
		List<UserInfo> userAttentionList = new ArrayList<UserInfo>();
		userAttentionList = ucenterDomainRepository.findUserAttention(productContext, userId, count, pageIndex, pageSize);
		Iterator<UserInfo> userAttentionIterator = userAttentionList.iterator();
		UserInfo userInfo = new UserInfo();
		List<Long> userAttentionIdList = new ArrayList<Long>();
		while(userAttentionIterator.hasNext()){
			userInfo = userAttentionIterator.next();
			userAttentionIdList.add(userInfo.getUserId());
		}
		//朋友关注人列表（去重）
		List<Long> friendsAttentionList = new ArrayList<Long>();
		friendsAttentionList = ucenterDomainRepository.findFriendsAttentionList(userAttentionIdList);
		/*Set<String> friendsAttentionSet = new HashSet<String>();
		Iterator<String> friendsAttentionIterator = friendsAttentionList.iterator();
		String userIdStr = "";
		while(friendsAttentionIterator.hasNext()){
			userIdStr = friendsAttentionIterator.next();
			friendsAttentionSet.add(userIdStr);
		}
		List<String> list1 = new ArrayList<String>();
		list1.addAll(friendsAttentionSet);*/
		//朋友关注人信息表
		List<UserInfo> friendsAttentionUserList = new ArrayList<UserInfo>();
		friendsAttentionUserList = ucenterDomainRepository.findFriendsAttentionUser(friendsAttentionList);
		
		friendsAttentionUserList.retainAll(userAttentionList);
		if(null==friendsAttentionUserList||friendsAttentionUserList.size()==0){
			userInfo.setFlag(ResultUtils.DATAISNULL);
			friendsAttentionUserList.add(userInfo);
		}
		//相互关注的人信息表
		List<UserInfo> userAttentionEachList = ucenterDomainRepository.findUserAttentionEachOther(userId);
		friendsAttentionUserList.retainAll(userAttentionEachList);
		return friendsAttentionUserList;
	}
	
	/**
	 * 查询用户相互关注列表
	 */
	@Override
	public List<UserInfo> userAttentionEachList(long userId) {
		return ucenterDomainRepository.findUserAttentionEachOther(userId);
	}
	
	/**
	 * 查询用户关注人总数
	 */
	@Override
	public int findUserAttentionCount(long userId) {
		return ucenterDomainRepository.findUserAttentionCount(userId);
	}
	
	/**
	 * 查询用户粉丝总数
	 */
	@Override
	public int findUserFensCount(long userId) {
		return ucenterDomainRepository.findUserFensCount(userId);
	}
	
	/**
	 * 编辑用户信息
	 */
	@Override
	public UserAllInfo editUserInfo(UserInfo userInfo, UserSummary userSummary) {
		return ucenterDomainRepository.editUserInfo(userInfo, userSummary);
	}
	
	/**
	 * 查询用户关注人列表
	 */
	@Override
	public List<Long> findUserAttentionList(long userId, int pageIndex,
			int pageSize) {
		return ucenterDomainRepository.findUserAttentionList(userId, pageIndex, pageSize);
	}
	
	/**
	 * 根据昵称查询用户信息
	 */
	@Override
	public UserInfo findUserInfoByName(String name) {
		return ucenterDomainRepository.findUserInfoByName(name);
	}
	
	@Override
	public VersionInfo findLatestVersion() {
		return ucenterDomainRepository.findLatestVersion();
	}
	
	/**
	 * 查询是否包含有相同的昵称
	 */
	@Override
	public UserInfo userNameIsExist(UserInfo userInfo) {
		return ucenterDomainRepository.userNameIsExist(userInfo);
	}
	
	/**
	 * 更新用户二维码
	 */
	@Override
	public int updateTwoDimensionCode(UserInfo userInfo) {
		return ucenterDomainRepository.updateTwoDimensionCode(userInfo);
	}
	
	/**
	 * 查询达人圈信息
	 */
	@Override
	public TalentZone findTalentZoneInfo(long id) {
		return ucenterDomainRepository.findTalentZoneInfo(id);
	}
	
	/**
	 * 查询达人圈信息
	 */
	@Override
	public List<TalentZoneLink> findTalentZoneLinkList(long talentZoneId) {
		return ucenterDomainRepository.findTalentZoneLinkList(talentZoneId);
	}
	
	/**
	 * 根据type查询达人圈信息
	 */
	@Override
	public TalentZone findTalentZoneInfoByType(String type) {
		return ucenterDomainRepository.findTalentZoneInfoByType(type);
	}
	
	/**
	 * 查询关注的达人
	 */
	@Override
	public List<UserAttention> findUserAttentionTalentedPersons(long userId,
			String type) {
		return ucenterDomainRepository.findUserAttentionTalentedPersons(userId, type);
	}
	
	/**
	 * 查询所有的达人圈
	 */
	@Override
	public List<TalentZone> findAllTalentZone() {
		return ucenterDomainRepository.findAllTalentZone();
	}
	
	/**
	 * 插入用户的逼格值
	 */
	@Override
	public UserBigValue insertintoUserBigValue(UserBigValue userBigValue) {
		return ucenterDomainRepository.insertintoUserBigValue(userBigValue);
	}
	
	/**
	 * 插入用户自测题逼格值
	 */
	@Override
	public UserBigValue insertintoUserSelfTest(UserBigValue userBigValue) {
		return ucenterDomainRepository.insertintoUserSelfTestValue(userBigValue);
	}
	
	/**
	 * 根据userid查询用户的逼格值
	 */
	@Override
	public UserBigValue findUserBigValueByUserId(long userId) {
		return ucenterDomainRepository.findUserBigValueByUserId(userId);
	}
	
	/**
	 * 查询打败的百分比
	 */
	@Override
	public float findUserBeatPercent(float selfTest) {
		return ucenterDomainRepository.findUserBeatPercent(selfTest);
	}
	
	/**
	 * 插入用户相册
	 */
	@Override
	public UserAlbum insertintoUserAlbum(long userId, List contentList, String type) {
		return ucenterDomainRepository.insertintoUserAlbum(userId,contentList,type);
	}
	
	/**
	 * 根据ID查询用户相册
	 */
	@Override
	public UserAlbum findUserAlbumById(long id) {
		return ucenterDomainRepository.findUserAlbumById(id);
	}
	
	/**
	 * 删除用户的图片
	 */
	@Override
	public UserAlbum deleteUserPicture(long id, List contentList) {
		return ucenterDomainRepository.deleteUserPicture(id, contentList);
	}
	
	/**
	 * 根据uid查询用户个人相册
	 */
	@Override
	public List<UserAlbum> findUserAlbumByUid(long userId) {
		return ucenterDomainRepository.findUserAlbumByUid(userId);
	}
	
	/**
	 * 根据电话查找用户信息
	 */
	@Override
	public UserInfo findUserInfoByMobilePhone(ProductContext productContext,
			String mobilePhone) {
		return ucenterDomainRepository.findUserInfoByMobilePhone(productContext, mobilePhone);
	}
	
	/**
	 * 查找关注这个人的列表
	 */
	@Override
	public List<UserAttention> findAttentionUserList(
			ProductContext productContext, long userAttentionId, int count,
			int pageIndex, int pageSize) {
		return ucenterDomainRepository.findAttentionUserList(productContext, userAttentionId, count, pageIndex, pageSize);
	}
	
	/**
	 * 根据用户id查找用户的状态码
	 */
	@Override
	public String findUserStateByUserId(long userId) {
		return ucenterDomainRepository.findUserStateByUserId(userId);
	}
	
	/**
	 * 查询是否已经关注过
	 */
	@Override
	public UserAttention findUserAttentionIsExist(UserAttention userAttention) {
		return ucenterDomainRepository.findUserAttentionIsExist(userAttention);
	}
	
	/**
	 * 修改密码
	 */
	@Override
	public UserInfo editPassword(long uid, String oldPassword,
			String newPassword, String verifyPassword) {
		return ucenterDomainRepository.editPassword(uid, oldPassword, newPassword, verifyPassword);
	}
	
	/**
	 * 忘记密码
	 */
	@Override
	public UserInfo forgetPassword(String mobilephone, String newPassword,
			String verifyPassword) {
		return ucenterDomainRepository.forgetPassword(mobilephone, newPassword, verifyPassword);
	}
	@Override
	public List<UserInfo> findUserListByUseridList(
			ProductContext productContext, List<Long> userids) {
		return ucenterDomainRepository.findUserListByUseridList(productContext, userids);
	}
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询详细用户列表根据用户id集合</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:15:45
	 * @return
	 */
	@Override
	public List<UserAllInfo> findUserAllInfoListByUseridList(ProductContext productContext,List<Long> userids){
		return ucenterDomainRepository.findUserAllInfoListByUseridList(productContext, userids);
	}
	
	/**
	 * 搜索毒药的标示
	 */
	@Override
	public int findSearchDuYao() {
		return ucenterDomainRepository.findSearchDuYao();
	}
	/**
	 * 保存用户的催更推送信息
	 * @Title: saveUserLatestInfo 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-6-18 上午11:08:38
	 * @param @param userLatest
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean saveUserLatestInfo(UserLatest userLatest){
		return ucenterDomainRepository.saveUserLatestInfo(userLatest);
	}

	/**
	 * 绑定手机号
	 * @param userId
	 * @param mobilephone
	 * @param newPassword
	 * @return
	 */
	@Override
	public UserInfo bindingMobile(Long userId, String mobilephone, String newPassword) {
		return ucenterDomainRepository.bindingMobile(userId, mobilephone, newPassword);
	}
}
