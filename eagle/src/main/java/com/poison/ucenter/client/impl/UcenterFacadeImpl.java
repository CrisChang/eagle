package com.poison.ucenter.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.keel.framework.runtime.ProductContext;
import com.poison.eagle.entity.VersionInfo;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.MD5Utils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
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

public class UcenterFacadeImpl implements UcenterFacade{
	
	private static final  Log LOG = LogFactory.getLog(UcenterFacadeImpl.class);

	private UcenterService ucenterService;
	//private UcenterMemcachedFacade ucenterMemcachedFacade;
	
	
	/*private JedisSimpleClient jedisSimpleClient;
	private MemcachedClient userInfoClientMemcachedClient;*/
	
	/*public void setUserInfoClientMemcachedClient(
			MemcachedClient userInfoClientMemcachedClient) {
		this.userInfoClientMemcachedClient = userInfoClientMemcachedClient;
	}
	public void setJedisSimpleClient(JedisSimpleClient jedisSimpleClient) {
		this.jedisSimpleClient = jedisSimpleClient;
	}*/
	public void setUcenterService(UcenterService ucenterService) {
		this.ucenterService = ucenterService;
	}
	/*public void setUcenterMemcachedFacade(
			UcenterMemcachedFacade ucenterMemcachedFacade) {
		this.ucenterMemcachedFacade = ucenterMemcachedFacade;
	}*/
	/**
	 * 查找用户的所有信息
	 */
	@Override
	public UserAllInfo findUserInfo(ProductContext productContext,long userId) {
		//UserAllInfo userAllInfo = null;//new UserAllInfo();
//		userAllInfo = ucenterMemcachedFacade.findUserAllInfo(userId);
		//if(null==userAllInfo){
			UserAllInfo userAllInfo = new UserAllInfo();
			UserInfo userInfo = new UserInfo();
			userInfo = ucenterService.findUserInfo(productContext,userId);
			if(null==userInfo){
				userAllInfo = new UserAllInfo();
				userAllInfo.setFlag(ResultUtils.DATAISNULL);
				return userAllInfo;
			}
			userAllInfo.setUserId(userId);
			userAllInfo.setLoginName(userInfo.getLoginName());
			userAllInfo.setMobilePhone(userInfo.getMobilePhone());
			userAllInfo.setName(userInfo.getName());
			userAllInfo.setFaceAddress(userInfo.getFaceAddress());
			userAllInfo.setPushToken(userInfo.getPushToken());
			userAllInfo.setBirthday(userInfo.getBirthday());
			userAllInfo.setSex(userInfo.getSex());
			userAllInfo.setLevel(userInfo.getLevel());
			userAllInfo.setTwoDimensionCode(userInfo.getTwoDimensionCode());
			userAllInfo.setAffectiveStates(userInfo.getAffectiveStates());
			userAllInfo.setResidence(userInfo.getResidence());
			userAllInfo.setProfession(userInfo.getProfession());
			userAllInfo.setAge(userInfo.getAge());
			userAllInfo.setConstellation(userInfo.getConstellation());
			userAllInfo.setIsOperation(userInfo.getIsOperation());
			userAllInfo.setState(userInfo.getState());
			UserSummary userSummary = new UserSummary();
			userSummary = ucenterService.findUserSummaryByUserid(productContext, userId);
			if(null==userSummary){
				userAllInfo.setSign("");
				userAllInfo.setInterest("");
				userAllInfo.setIntroduction("");
				userAllInfo.setIdentification("");
			}else{
				userAllInfo.setSign(userSummary.getSign());
				userAllInfo.setInterest(userSummary.getInterest());
				userAllInfo.setIntroduction(userSummary.getIntroduction());
				userAllInfo.setIdentification(userSummary.getIdentification());
			}
			userAllInfo.setFlag(userInfo.getFlag());

			//ucenterMemcachedFacade.saveUserAllInfo(userAllInfo);
		//}
		return userAllInfo;
	}

	/**
	 * 更新用户基本信息
	 */
	@Override
	public UserAllInfo updateUserInfo(ProductContext productContext,UserInfo userInfo) {
		UserAllInfo userAllInfo = new UserAllInfo();
		//获取缓存的用户信息
		long userId = userInfo.getUserId();
		//userAllInfo = ucenterMemcachedFacade.findUserAllInfo(userId);
		//当缓存内容为空时查询数据库
		/*if(null==userAllInfo){
			userAllInfo = ucenterMemcachedFacade.findUserAllInfo(userId);
		}*/
		userAllInfo = setUserInfo(userAllInfo, userInfo);
		//存入缓存
		//ucenterMemcachedFacade.saveUserAllInfo(userAllInfo);
		//更新数据库
		int flag = ucenterService.updateUserInfo(productContext,userInfo);
		return userAllInfo;
	}
	
	/**
	 * 添加用户基本信息
	 */
	@Override
	public UserAllInfo insertintoUserInfo(ProductContext productContext,UserInfo userInfo) {
		UserAllInfo userAllInfo = new UserAllInfo();
		//获取缓存的用户信息
		long userId = userInfo.getUserId();
		//userAllInfo = ucenterMemcachedFacade.findUserAllInfo(userId);
		//当缓存内容为空时查询数据库
		/*if(null==userAllInfo){
			userAllInfo = ucenterMemcachedFacade.findUserAllInfo(userId);
		}*/
		userAllInfo = setUserInfo(userAllInfo, userInfo);
		//存入缓存
		//sucenterMemcachedFacade.saveUserAllInfo(userAllInfo);
		//更新数据库
		ucenterService.insertintoUserInfo(productContext,userInfo);
		return userAllInfo;
	}
	
	/**
	 * 查询用户关注的人列表
	 */
	@Override
	public List<UserInfo> findUserAttention(ProductContext productContext,long userId,int count,int pageIndex,int pageSize) {
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		/*try{
			userInfoList = userInfoClientMemcachedClient.get("UserAttention"+userId);
			if(null==userInfoList||userInfoList.size()==0){
				userInfoList = new ArrayList<UserInfo>();
				userInfoList = ucenterService.findUserAttention(productContext,userId,count,pageIndex,pageSize);
				userInfoClientMemcachedClient.set("UserAttention"+userId, 0, userInfoList);
			}
		}catch (Exception e) {
			LOG.error("client,ucenterFacadeImpl,findUserAttention,error:userId:"+userId+":"+e.getMessage());*/
			//userInfoList = new ArrayList<UserInfo>();
			userInfoList = ucenterService.findUserAttention(productContext,userId,count,pageIndex,pageSize);
		//}
		return userInfoList;
	}
	
	/**
	 * 查询用户粉丝列表
	 */
	@Override
	public List<UserInfo> findUserFens(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize) {
		
		return ucenterService.findUserFens(productContext,userAttentionId,count,pageIndex,pageSize);
	}
	
	/**
	 * 插入用户简介
	 */
	@Override
	public int insertUserSummary(ProductContext productContext,
			UserSummary userSummary) {
		return ucenterService.insertUserSummary(productContext, userSummary);
	}
	
	/**
	 * 更新用户简介
	 */
	@Override
	public int updateUserSummary(ProductContext productContext,
			UserSummary userSummary) {
		return ucenterService.updateUserSummary(productContext, userSummary);
	}
	
	/**
	 * 查询用户简介
	 */
	@Override
	public UserSummary findUserSummaryByUserId(ProductContext productContext,
			long userId) {
		return ucenterService.findUserSummaryByUserid(productContext, userId);
	}
	
	/**
	 * 根据手机号注册
	 */
	@Override
	public UserInfo registUserByMobilePhone(ProductContext productContext,
			String mobilePhone,String passWord,String pushToken) {
		long createDate = System.currentTimeMillis();
		UserInfo userInfo = new UserInfo();
		userInfo.setMobilePhone(mobilePhone);
		userInfo.setLoginName("");
		String password = MD5Utils.md5(passWord);
		userInfo.setPassword(password);
		password = MD5Utils.md5AndRandom(password);
		userInfo.setPasswordMd5(password);
		String nameStr = changeName(mobilePhone);
		userInfo.setName(nameStr);
		userInfo.setFaceAddress("");
		userInfo.setBirthday(0);
		userInfo.setSex("0");
		userInfo.setLevel(0);
		userInfo.setTwoDimensionCode("");
		userInfo.setIp("0");
		userInfo.setPushToken(pushToken);
		userInfo.setCreateDate(createDate);
		userInfo.setLastestLoginDate(createDate);
		userInfo.setAffectiveStates("");
		userInfo.setResidence("");
		userInfo.setProfession("");
		userInfo.setAge("");
		userInfo.setConstellation("");
		return ucenterService.registUserByMobilePhone(productContext, userInfo);
	}
	
	/**
	 * 根据用户名注册
	 */
	@Override
	public UserInfo registUserByLoginName(ProductContext productContext,
			String loginName,String password,String pushToken) {
		UserInfo userInfo = new UserInfo();
		userInfo.setMobilePhone(loginName);
		userInfo.setLoginName(loginName);
		password = MD5Utils.md5(password);
		userInfo.setPassword(password);
		password = MD5Utils.md5AndRandom(password);
		userInfo.setPasswordMd5(password);
		//String nameStr = changeName(loginName);
		userInfo.setName(loginName);
		userInfo.setFaceAddress("");
		userInfo.setBirthday(0);
		userInfo.setSex("");
		userInfo.setLevel(0);
		userInfo.setTwoDimensionCode("");
		userInfo.setIp("");
		userInfo.setPushToken(pushToken);
		userInfo.setAffectiveStates("");
		userInfo.setResidence("");
		userInfo.setProfession("");
		userInfo.setAge("");
		userInfo.setConstellation("");
		long sysdate = System.currentTimeMillis();
		userInfo.setCreateDate(sysdate);
		userInfo.setLastestLoginDate(sysdate);
		return ucenterService.registUserByLoginName(productContext, userInfo);
	}
	
	/**
	 * 手机号码登录
	 */
	@Override
	public UserAllInfo checkLoginByMobilePhone(ProductContext productContext,
			String mobilePhone, String password) {
		return ucenterService.checkLoginByMobile(productContext, mobilePhone, password);
	}
	
	/**
	 * 用户名密码登录
	 */
	@Override
	public UserAllInfo checkLoginByLoginName(ProductContext productContext,
			String loginName, String password) {
		return ucenterService.checkLoginByLoginName(productContext, loginName, password);
	}
	
	/**
	 * 用户点击关注
	 */
	@Override
	public UserAttention doAttention(ProductContext productContext,
			long userId,long userAttentionId,String type) {
		long sysdate = System.currentTimeMillis();
		UserAttention userAttention = new UserAttention();
		userAttention.setUserId(userId);
		userAttention.setUserAttentionId(userAttentionId);
		userAttention.setIsAttention(1);
		userAttention.setType(type);
		userAttention.setAttentionDate(sysdate);
		userAttention.setLatestRevisionDate(sysdate);
		return ucenterService.doAttention(productContext, userAttention);
	}
	
	/**
	 * 用户取消关注
	 */
	@Override
	public UserAttention cancelAttention(ProductContext productContext,
			long userId, long userAttentionId) {
		long systime = System.currentTimeMillis();
		UserAttention userAttention = new UserAttention();
		userAttention.setUserId(userId);
		userAttention.setUserAttentionId(userAttentionId);
		userAttention.setIsAttention(0);
		userAttention.setType("");
		userAttention.setLatestRevisionDate(systime);
		return ucenterService.cancelAttention(productContext, userAttention);
	}
	
	/**
	 * 查询用户已注册的列表
	 */
	@Override
	public List<UserInfo> findRegistListByMobileList(
			ProductContext productContext, List<String> mobilePhoneList) {
		if(null==mobilePhoneList||mobilePhoneList.size()==0){
			return new ArrayList<UserInfo>();
		}
		return ucenterService.findRegistListByMobileList(productContext, mobilePhoneList);
	}
	
	/**
	 * 查询用户未注册列表
	 */
	@Override
	public List<UserInfo> findUnRegistListByMobileList(
			ProductContext productContext, List<String> mobilePhoneList) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		if(null==mobilePhoneList||mobilePhoneList.size()==0){
			return list;
		}
		list = ucenterService.findUnRegistListByMobileList(productContext, mobilePhoneList);
		return list;
	}
	
	/**
	 * 查询用户通讯录中已关注的列表
	 */
	@Override
	public List<UserInfo> findAttentionByMobile(
			ProductContext productContext, List<String> mobilePhoneList,long userId,int pageIndex,int pageSize) {
		return ucenterService.findAttentionByMobileList(productContext, mobilePhoneList,userId,pageIndex,pageSize);
	}
	
	/**
	 * 查询用户通讯录中未关注的列表
	 */
	@Override
	public List<UserInfo> findUnAttentionByMobile(
			ProductContext productContext, List<String> mobilePhoneList,long userId,int pageIndex,int pageSize) {
	return ucenterService.findUnAttentionByMobileList(productContext, mobilePhoneList,userId,pageIndex,pageSize);
	}
	
	/**
	 * 查询朋友关注的人列表
	 */
	@Override
	public List<UserInfo> findFriendsUnAttentionList(
			ProductContext productContext, long userId,int count,int pageIndex,int pageSize) {
		return ucenterService.findFriendsUnAttentionList(productContext, userId, count, pageIndex, pageSize);
	}
	
	/**
	 * 查询朋友关注的我已经关注的人列表
	 */
	@Override
	public List<UserInfo> findFriendsIsAttentionList(
			ProductContext productContext, long userId, int count,
			int pageIndex, int pageSize) {
		return ucenterService.findFriendsIsAttentionList(productContext, userId, count, pageIndex, pageSize);
	}
	
	/**
	 * 查询用户关注人总数
	 */
	@Override
	public int findUserAttentionCount(ProductContext productContext, long userId) {
		return ucenterService.findUserAttentionCount(userId);
	}
	
	/**
	 * 查询用户粉丝总数
	 */
	@Override
	public int findUserFensCount(ProductContext productContext, long userId) {
		return ucenterService.findUserFensCount(userId);
	}
	
	/**
	 * 查询用户相互关注的用户信息
	 */
	@Override
	public List<UserInfo> findUserAttentionEachList(
			ProductContext productContext, long userId) {
		return ucenterService.userAttentionEachList(userId);
	}
	
	/**
	 * 查询朋友关注的相互关注信息
	 */
	@Override
	public List<UserInfo> findFriendsAttentionEachList(
			ProductContext productContext, long userId, int count,
			int pageIndex, int pageSize) {
		return ucenterService.findFriendsAttentionEachList(productContext, userId, count, pageIndex, pageSize);
	}
	
	/**
	 * 编辑用户信息
	 */
	@Override
	public UserAllInfo editUserInfo(long userId, String faceAddress,
			String name, String sex, String sign, String interest,
			String introduction,String affectiveStates,String residence,String profession,long birthday,String constellation,String age) {
		long sysdate = System.currentTimeMillis();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setFaceAddress(faceAddress);
		userInfo.setName(CheckParams.replaceKG(name));
		userInfo.setSex(sex);
		//userInfo.setLastestLoginDate(sysdate);
		userInfo.setLastestRevisionDate(sysdate);
		userInfo.setAffectiveStates(affectiveStates);
		userInfo.setBirthday(birthday);
		userInfo.setResidence(residence);
		userInfo.setProfession(profession);
		userInfo.setConstellation(constellation);
		userInfo.setAge(age);
		UserSummary userSummary = new UserSummary();
		userSummary.setUserId(userId);
		userSummary.setSign(sign);
		userSummary.setInterest(interest);
		userSummary.setIntroduction(introduction);
		userSummary.setLatestRevisionDate(sysdate);
		return ucenterService.editUserInfo(userInfo, userSummary);
	}
	
	/**
	 * 查询用户关注列表
	 */
	@Override
	public List<Long> findUserAttentionList(long userId, int pageIndex,
			int pageSize) {
		return ucenterService.findUserAttentionList(userId, pageIndex, pageSize);
	}
	
	/**
	 * 根据昵称查询用户信息
	 */
	@Override
	public UserInfo findUserInfoByName(String name) {
		UserInfo userInfo = ucenterService.findUserInfoByName(name);
		if(null==userInfo){
			userInfo = new UserInfo();
			userInfo.setFlag(ResultUtils.DATAISNULL);
		}
		return userInfo;
	}
	
	public String changeName(String name){
		if(null==name||"".equals(name)){
			return name;
		}
		int i = (int) (Math.random()*100)%24;
		int m = (int) (Math.random()*100)%24;
		Random random = new Random();
		char j = 'a';
		char k = (char) (j+i);
		char n =  (char) (j+m);
		String temp = name.substring(name.length()/2,name.length());
		name = name.replace(temp, "vvv");
		name = name+k+n+random.nextInt(10000);
		return name;
	}
	
	/**
	 * 
	 * <p>Title: setUserInfo</p> 
	 * <p>Description: 向userAllInfo中setuserInfo</p> 
	 * @author :changjiang
	 * date 2014-11-13 下午5:52:43
	 * @param userAllInfo
	 * @param userInfo
	 * @return
	 */
	public UserAllInfo setUserInfo(UserAllInfo userAllInfo,UserInfo userInfo){
		userAllInfo.setUserId(userInfo.getUserId());
		userAllInfo.setLoginName(userInfo.getLoginName());
		userAllInfo.setMobilePhone(userInfo.getMobilePhone());
		userAllInfo.setName(userInfo.getName());
		userAllInfo.setFaceAddress(userInfo.getFaceAddress());
		userAllInfo.setPushToken(userInfo.getPushToken());
		userAllInfo.setBirthday(userInfo.getBirthday());
		userAllInfo.setSex(userInfo.getSex());
		userAllInfo.setLevel(userInfo.getLevel());
		userAllInfo.setTwoDimensionCode(userInfo.getTwoDimensionCode());
		userAllInfo.setAffectiveStates(userInfo.getAffectiveStates());
		userAllInfo.setResidence(userInfo.getResidence());
		userAllInfo.setProfession(userInfo.getProfession());
		return userAllInfo;
	}
	
	@Override
	public VersionInfo findLatestVersion() {
		return ucenterService.findLatestVersion();
	}
	
	/**
	 * 
	 */
	@Override
	public UserInfo userNameIsExist(long userId, String name) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setName(name);
		return ucenterService.userNameIsExist(userInfo);
	}
	
	/**
	 * 更新用户二维码
	 */
	@Override
	public int addTwoDimensionCode(long userId,String twoDimensionCode) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setTwoDimensionCode(twoDimensionCode);
		return ucenterService.updateTwoDimensionCode(userInfo);
	}
	
	/**
	 * 查询达人圈信息
	 */
	@Override
	public TalentZone findTalentZoneInfo(final long id) {
		TalentZone talentZone = new TalentZone();
		/*try{
			//从redis中取出达人圈信息
			talentZone = jedisSimpleClient.execute(new JedisWorker<TalentZone>(){
				@Override
				public TalentZone work(Jedis jedis) {
					byte[] talentZoneByte = jedis.get(("TalentZoneInfo"+id).getBytes());
					TalentZone talentZoneRds = (TalentZone) SerializeServiceUtil.unserialize(talentZoneByte);
					if(null==talentZoneRds){
						talentZoneRds = new TalentZone();
						talentZoneRds = ucenterService.findTalentZoneInfo(id);
						jedis.set(("TalentZoneInfo"+id).getBytes(), SerializeServiceUtil.serialize(talentZoneRds));
					}
					return talentZoneRds;
				}
			});
		}catch (Exception e) {
			LOG.error("client,ucenterFacadeImpl,findTalentZoneInfo,error:"+id+":"+e.getMessage());*/
			talentZone = new TalentZone();
			talentZone = ucenterService.findTalentZoneInfo(id);
		//}
		return talentZone;
	}
	
	/**
	 * 查询达人圈达人信息列表
	 */
	@Override
	public List<TalentZoneLink> findTalentZoneLinkList(final long talentZoneId) {
		List<TalentZoneLink> talentZoneLinkList = new ArrayList<TalentZoneLink>();
		/*try{
			talentZoneLinkList = jedisSimpleClient.execute(new JedisWorker<List<TalentZoneLink>>(){
				@Override
				public List<TalentZoneLink> work(Jedis jedis) {
					byte[] talentZoneLinkListByte = jedis.get(("talentZoneLinkList"+talentZoneId).getBytes());
					List<TalentZoneLink> talentZoneLinkListRds =  (List<TalentZoneLink>) SerializeServiceUtil.unserialize(talentZoneLinkListByte);
					if(null==talentZoneLinkListRds||talentZoneLinkListRds.size()==0){
						talentZoneLinkListRds = new ArrayList<TalentZoneLink>();
						talentZoneLinkListRds = ucenterService.findTalentZoneLinkList(talentZoneId);
						jedis.set(("talentZoneLinkList"+talentZoneId).getBytes(),SerializeServiceUtil.serialize(talentZoneLinkListRds));
					}
					return talentZoneLinkListRds;
				}
			});
		}catch (Exception e) {
			LOG.error("client,ucenterFacadeImpl,findTalentZoneLinkList,error:"+talentZoneId+":"+e.getMessage());*/
			talentZoneLinkList = new ArrayList<TalentZoneLink>();
			talentZoneLinkList = ucenterService.findTalentZoneLinkList(talentZoneId);
		//}
		return talentZoneLinkList;
	}
	
	/**
	 * 根据type查询达人圈信息
	 */
	@Override
	public TalentZone findTalentZoneInfoByType(final String type) {
		TalentZone talentZone = new TalentZone();
		/*try{
			//从redis中取出达人圈信息
			talentZone = jedisSimpleClient.execute(new JedisWorker<TalentZone>(){
				@Override
				public TalentZone work(Jedis jedis) {
					byte[] talentZoneByte = jedis.get(("TalentZoneInfoByType"+type).getBytes());
					TalentZone talentZoneRds = (TalentZone) SerializeServiceUtil.unserialize(talentZoneByte);
					if(null==talentZoneRds){
						talentZoneRds = new TalentZone();
						talentZoneRds = ucenterService.findTalentZoneInfoByType(type);
						jedis.set(("TalentZoneInfoByType"+type).getBytes(), SerializeServiceUtil.serialize(talentZoneRds));
					}
					return talentZoneRds;
				}
			});
		}catch (Exception e) {
			LOG.error("client,ucenterFacadeImpl,findTalentZoneInfoByType,error:"+type+":"+e.getMessage());*/
			talentZone = new TalentZone();
			talentZone = ucenterService.findTalentZoneInfoByType(type);
		//}
		return talentZone;
	}
	
	/**
	 * 关注圈子或者是达人信息
	 */
	/*@Override
	public UserAttention doAttentionZone(long userId, long userAttentionId,
			String type) {
		UserAttention userAttention = new UserAttention();
		long sysdate = System.currentTimeMillis();
		userAttention.setUserId(userId);
		userAttention.setUserAttentionId(userAttentionId);
		userAttention.setType(type);
		userAttention.setIsAttention(1);
		userAttention.setLatestRevisionDate(sysdate);
		return ucenterService.doAttention(null, userAttention);
	}*/
	
	/**
	 * 查询关注的达人
	 */
	@Override
	public List<UserAttention> findUserAttentionTalentedPersons(long userId,
			String type) {
		return ucenterService.findUserAttentionTalentedPersons(userId, type);
	}
	
	/**
	 * 查询所有的达人圈
	 */
	@Override
	public List<TalentZone> findAllTalentZone() {
		return ucenterService.findAllTalentZone();
	}
	
	/**
	 * 插入用户B格值
	 */
	@Override
	public UserBigValue updateUserBigValue(long userId,float bigValue,int bigLevel) {
		UserBigValue userBigValue = new UserBigValue();
		userBigValue.setUserId(userId);
		userBigValue.setBigLevel(bigLevel);
		userBigValue.setBigValue(bigValue);
		return ucenterService.insertintoUserBigValue(userBigValue);
	}
	
	/**
	 * 更新用户自测题的逼格值
	 */
	@Override
	public UserBigValue updateUserSelfTest(long userId, float selfTestValue,int bigLevel) {
		UserBigValue userBigValue = new UserBigValue();
		userBigValue.setUserId(userId);
		userBigValue.setBigLevel(bigLevel);
		userBigValue.setSelfTest(selfTestValue);
		return ucenterService.insertintoUserSelfTest(userBigValue);
	}
	
	/**
	 * 查询用户的逼格值
	 */
	@Override
	public UserBigValue findUserBigValueByUserId(long userId) {
		return ucenterService.findUserBigValueByUserId(userId);
	} 
	
	/**
	 * 查询打败的百分比
	 */
	@Override
	public int findUserBeatPercent(float selfTest) {
		float percent = ucenterService.findUserBeatPercent(selfTest);
		int percentInt =  (int) (percent*100);
		return percentInt;
	}
	
	/**
	 * 插入用户相册
	 */
	@Override
	public UserAlbum insertintoUserAlbum(long userId, List contentList, String type) {
		return ucenterService.insertintoUserAlbum(userId,contentList,type);
	}
	
	/**
	 * 根据ID查询相册
	 */
	@Override
	public UserAlbum findUserAlbumById(long id) {
		return ucenterService.findUserAlbumById(id);
	}
	
	/**
	 * 删除用户照片
	 */
	@Override
	public UserAlbum deleteUserPicture(long id, List contentList) {
		return ucenterService.deleteUserPicture(id, contentList);
	}
	
	/**
	 * 根据uid查询用户个人相册
	 */
	@Override
	public List<UserAlbum> findUserAlbumByUid(long userId) {
		return ucenterService.findUserAlbumByUid(userId);
	}
	
	/**
	 * 根据昵称或者电话号码查询用户信息
	 */
	@Override
	public UserInfo findUserInfoByNameOrMobilePhone(String serachInfo) {
		UserInfo userInfo = new UserInfo();
		//当为手机号时，根据手机号搜索
		if(CheckParams.isPhone(serachInfo)){
			userInfo=ucenterService.findUserInfoByMobilePhone(null, serachInfo);
		}else{//当为昵称的时候
			userInfo = ucenterService.findUserInfoByName(serachInfo);
		}
		if(null==userInfo){
			userInfo = new UserInfo();
			userInfo.setFlag(ResultUtils.DATAISNULL);
		}
		return userInfo;
	}
	
	/**
	 * 查找关注这个人的列表
	 */
	@Override
	public List<UserAttention> findAttentionUserList(
			ProductContext productContext, long userAttentionId, int count,
			int pageIndex, int pageSize) {
		return ucenterService.findAttentionUserList(productContext, userAttentionId, count, pageIndex, pageSize);
	}
	
	/**
	 * 根据用户id查询用户的状态码
	 */
	@Override
	public String findUserStateByUserId(long userId) {
		return ucenterService.findUserStateByUserId(userId);
	}
	
	/**
	 * 查找用户是否关注
	 */
	@Override
	public UserAttention findUserAttentionIsExist(long userId,
			long attentionUserId) {
		UserAttention uAttention = new UserAttention();
		uAttention.setUserId(userId);
		uAttention.setUserAttentionId(attentionUserId);
		return ucenterService.findUserAttentionIsExist(uAttention);
	}
	
	/**
	 * 修改密码
	 */
	@Override
	public UserInfo editPassword(long uid, String oldPassword,
			String newPassword, String verifyPassword) {
		return ucenterService.editPassword(uid, oldPassword, newPassword, verifyPassword);
	}
	
	/**
	 * 忘记密码
	 */
	@Override
	public UserInfo forgetPassword(String mobilephone, String newPassword,
			String verifyPassword) {
		return ucenterService.forgetPassword(mobilephone, newPassword, verifyPassword);
	}
	
	/**
	 * 根据用户id查询用户详情
	 */
	@Override
	public UserInfo findUserInfoByUserId(ProductContext productContext,
			long userId) {
		return ucenterService.findUserInfo(productContext, userId);
	}
	/**
	 * 查询用户列表根据用户id集合
	 */
	@Override
	public List<UserInfo> findUserListByUseridList(
			ProductContext productContext, List<Long> userids) {
		return ucenterService.findUserListByUseridList(productContext, userids);
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
		return ucenterService.findUserAllInfoListByUseridList(productContext, userids);
	}
	
	/**
	 * 搜索毒药的搜索标示
	 */
	@Override
	public int findSearchDuYao() {
		return ucenterService.findSearchDuYao();
	}
	
	/**
	 * 查找关注的状态
	 */
	/*@Override
	public List<String> findAttentionStatus(long user, List<Long> userIdList) {
		// TODO Auto-generated method stub
		return null;
	}*/
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
	public boolean saveUserLatestInfo(long userid,long resourceid,String type){
		try{
			//规定了只有书评:6、影评:7、文字:3才计算催更
			if(CommentUtils.TYPE_DIARY.equals(type) || CommentUtils.TYPE_BOOK_COMMENT.equals(type) || CommentUtils.TYPE_MOVIE_COMMENT.equals(type) || "".equals(type)){
				UserLatest userLatest = new UserLatest();
				userLatest.setUserid(userid);
				userLatest.setResourceid(resourceid);
				userLatest.setType(type);
				userLatest.setCreatetime(System.currentTimeMillis());
				userLatest.setIsdel(0);
				userLatest.setPushtime(0);
				userLatest.setUpdatetime(System.currentTimeMillis());
				return ucenterService.saveUserLatestInfo(userLatest);
			}
			return false;
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return false;
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
		return ucenterService.bindingMobile(userId, mobilephone, newPassword);
	}
}
