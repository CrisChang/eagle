package com.poison.ucenter.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.keel.framework.runtime.ProductContext;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.entity.VersionInfo;
import com.poison.eagle.utils.MD5Utils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.TalentZoneDAO;
import com.poison.ucenter.dao.TalentZoneLinkDAO;
import com.poison.ucenter.dao.UserAlbumDAO;
import com.poison.ucenter.dao.UserAttentionDAO;
import com.poison.ucenter.dao.UserBigDAO;
import com.poison.ucenter.dao.UserInfoDAO;
import com.poison.ucenter.dao.UserLatestDAO;
import com.poison.ucenter.dao.UserSummaryDAO;
import com.poison.ucenter.model.TalentZone;
import com.poison.ucenter.model.TalentZoneLink;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;
import com.poison.ucenter.model.UserLatest;
import com.poison.ucenter.model.UserSummary;

public class UcenterDomainRepository {

	private UserInfoDAO userInfoDAO;
	private UserAttentionDAO userAttentionDAO;
	private UserSummaryDAO userSummaryDAO;
	private TalentZoneDAO talentZoneDAO;
	private TalentZoneLinkDAO talentZoneLinkDAO;
	private UserBigDAO userBigDAO;
	private UserAlbumDAO userAlbumDAO;
	private UKeyWorker reskeyWork;
	private UserLatestDAO userLatestDAO;
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setUserAlbumDAO(UserAlbumDAO userAlbumDAO) {
		this.userAlbumDAO = userAlbumDAO;
	}

	public void setUserBigDAO(UserBigDAO userBigDAO) {
		this.userBigDAO = userBigDAO;
	}

	public void setTalentZoneLinkDAO(TalentZoneLinkDAO talentZoneLinkDAO) {
		this.talentZoneLinkDAO = talentZoneLinkDAO;
	}

	public void setTalentZoneDAO(TalentZoneDAO talentZoneDAO) {
		this.talentZoneDAO = talentZoneDAO;
	}
	
	public void setUserInfoDAO(UserInfoDAO userInfoDAO) {
		this.userInfoDAO = userInfoDAO;
	}

	public void setUserAttentionDAO(UserAttentionDAO userAttentionDAO) {
		this.userAttentionDAO = userAttentionDAO;
	}

	public void setUserSummaryDAO(UserSummaryDAO userSummaryDAO) {
		this.userSummaryDAO = userSummaryDAO;
	}

	public void setUserLatestDAO(UserLatestDAO userLatestDAO) {
		this.userLatestDAO = userLatestDAO;
	}

	/**
	 * 
	 * <p>Title: registUserInfo</p> 
	 * <p>Description: 手机注册用户</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午3:56:29
	 */
	public UserInfo registUserInfoByMobilePhone(ProductContext productContext,UserInfo userInfo){
		UserInfo userInfoByMobilePhone = new UserInfo();
		String mobilePhoneStr = userInfo.getMobilePhone();
		userInfoByMobilePhone = userInfoDAO.findUserInfoByMobilePhone(productContext, mobilePhoneStr);
		int flag = userInfoByMobilePhone.getFlag();
		//如果出错直接返回
		/*if(ResultUtils.SUCCESS!=flag){
			return userInfoByMobilePhone;
		}*/
		//用户存在
		//手机号不为Null并且不为''时判断该手机号已经注册过
		if(null!=userInfoByMobilePhone.getMobilePhone()&&!"".equals(userInfoByMobilePhone.getMobilePhone())){
			userInfoByMobilePhone.setFlag(ResultUtils.EXISTED_USER);
			return userInfoByMobilePhone;
		}
		//用户不存在时插入并返回查询结果
		if(ResultUtils.DATAISNULL==flag){
			userInfoDAO.insertUserInfo(productContext, userInfo);
			userInfoByMobilePhone = userInfoDAO.findUserInfoByMobilePhone(productContext, mobilePhoneStr);
		}
		UserSummary userSummary = new UserSummary();
		userSummary.setUserId(userInfoByMobilePhone.getUserId());
		userSummary.setInterest("");
		userSummary.setIntroduction("");
		userSummary.setSign("");
		userSummary.setLatestRevisionDate(System.currentTimeMillis());
		userSummaryDAO.insertUserSummary(null, userSummary);
		return userInfoByMobilePhone;
	}
	
	/**
	 * 
	 * <p>Title: registUserInfoByLoginName</p> 
	 * <p>Description: 用户名注册</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午4:01:54
	 * @param productContext
	 * @param userInfo
	 */
	public UserInfo registUserInfoByLoginName(ProductContext productContext,UserInfo userInfo){
		UserInfo userInfoByLoginName = new UserInfo();
		String loginNameStr = userInfo.getLoginName();
		userInfoByLoginName = userInfoDAO.findUserInfoByLoginName(productContext, loginNameStr);
		//当出错时，直接返回
		int flag = userInfoByLoginName.getFlag();
		/*if(ResultUtils.SUCCESS!=flag){
			return userInfoByLoginName;
		}*/
		//用户存在
		if(null!=userInfoByLoginName.getLoginName()&&!"".equals(userInfoByLoginName.getLoginName())){
			userInfoByLoginName.setFlag(ResultUtils.EXISTED_USER);
			return userInfoByLoginName;
		}
		//用户不存在时插入用户信息返回查询结果
		if(ResultUtils.DATAISNULL==flag){
			userInfoDAO.insertUserInfo(productContext, userInfo);
			userInfoByLoginName = userInfoDAO.findUserInfoByLoginName(productContext, loginNameStr);
		}
		UserSummary userSummary = new UserSummary();
		userSummary.setUserId(userInfoByLoginName.getUserId());
		userSummary.setInterest("");
		userSummary.setIntroduction("");
		userSummary.setSign("");
		userSummary.setLatestRevisionDate(System.currentTimeMillis());
		userSummaryDAO.insertUserSummary(null, userSummary);
		return userInfoByLoginName;
	}
	
	/**
	 * 
	 * <p>Title: checkLoginByMobilePhone</p> 
	 * <p>Description: 验证用户手机号登录</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午2:53:14
	 * @return
	 */
	public UserAllInfo checkLoginByMobilePhone(ProductContext productContext,String mobilePhone,String password){
		UserAllInfo userAllInfo = new UserAllInfo();
		UserInfo userInfo = new UserInfo();
		userInfo = userInfoDAO.findUserInfoByMobilePhone(productContext,mobilePhone);
		int flag = userInfo.getFlag();
		//不存在该用户,直接返回
		if(ResultUtils.DATAISNULL==flag){
			userAllInfo.setFlag(ResultUtils.NO_EXISTED_USER);
			return userAllInfo;
		}
		//加密过程---------------
		String pwdMd5 = MD5Utils.md5(password);
		String pwdStr = userInfo.getPassword();
		//用户密码错误
		//if(!pwdStr.equals(pwdMd5)){
			if(pwdStr.length()<32){
				if(!pwdStr.equals(password)){
					userAllInfo.setFlag(ResultUtils.USER_PASSWORD_ERROR);
					return userAllInfo;
				}
			}else{
				if(!pwdStr.equals(pwdMd5)){
					userAllInfo.setFlag(ResultUtils.USER_PASSWORD_ERROR);
					return userAllInfo;
				}
			}
		//}
		long userId = userInfo.getUserId();
		UserSummary summary= userSummaryDAO.findUserSummaryById(null, userId);
		userAllInfo.setUserId(userId);
		userAllInfo.setLoginName(userInfo.getLoginName());
		userAllInfo.setPassword(userInfo.getPassword());
		userAllInfo.setPasswordMd5(userInfo.getPasswordMd5());
		userAllInfo.setMobilePhone(userInfo.getMobilePhone());
		userAllInfo.setName(userInfo.getName());
		userAllInfo.setFaceAddress(userInfo.getFaceAddress());
		userAllInfo.setBirthday(userInfo.getBirthday());
		userAllInfo.setSex(userInfo.getSex());
		userAllInfo.setIp(userInfo.getIp());
		userAllInfo.setLevel(userInfo.getLevel());
		userAllInfo.setCreateDate(userInfo.getCreateDate());
		userAllInfo.setLastestLoginDate(userInfo.getLastestLoginDate());
		userAllInfo.setLastestRevisionDate(userInfo.getLastestRevisionDate());
		userAllInfo.setState(userInfo.getState());
		userAllInfo.setIsOperation(userInfo.getIsOperation());
		if(null!=summary){
			userAllInfo.setSign(summary.getSign());
			userAllInfo.setInterest(summary.getInterest());
			userAllInfo.setIntroduction(summary.getIntroduction());
		}
		userAllInfo.setFlag(ResultUtils.SUCCESS);
		return userAllInfo;
    	}
	
	/**
	 * 
	 * <p>Title: checkLoginByLoginName</p> 
	 * <p>Description: 验证用户名密码登录</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午3:16:39
	 * @param productContext
	 * @param LoginName
	 * @param password
	 * @return
	 */
	public UserAllInfo checkLoginByLoginName(ProductContext productContext,String LoginName,String password){
		UserAllInfo userAllInfo = new UserAllInfo();
		UserInfo userInfo = new UserInfo();
		userInfo = userInfoDAO.findUserInfoByLoginName(productContext,LoginName);
		//不存在该用户
		if(null==userInfo.getLoginName()){
			userAllInfo.setFlag(ResultUtils.NO_EXISTED_USER);
			return userAllInfo;
		}
		//加密过程---------------
		//加密后的密码
		String pwdMd5 = MD5Utils.md5(password);
		//数据库中得密码
		String pwdStr = userInfo.getPassword();
		//用户密码错误
		//if(!pwdStr.equals(pwdMd5)){
			if(pwdStr.length()<32){
				if(!pwdStr.equals(password)){
					userAllInfo.setFlag(ResultUtils.USER_PASSWORD_ERROR);
					return userAllInfo;
					}
				}else{
					if(!pwdStr.equals(pwdMd5)){
						userAllInfo.setFlag(ResultUtils.USER_PASSWORD_ERROR);
						return userAllInfo;
						}
				}
			//}
		long userId = userInfo.getUserId();
		UserSummary summary= userSummaryDAO.findUserSummaryById(null, userId);
		userAllInfo.setUserId(userId);
		userAllInfo.setLoginName(userInfo.getLoginName());
		userAllInfo.setPassword(userInfo.getPassword());
		userAllInfo.setPasswordMd5(userInfo.getPasswordMd5());
		userAllInfo.setMobilePhone(userInfo.getMobilePhone());
		userAllInfo.setName(userInfo.getName());
		userAllInfo.setFaceAddress(userInfo.getFaceAddress());
		userAllInfo.setBirthday(userInfo.getBirthday());
		userAllInfo.setSex(userInfo.getSex());
		userAllInfo.setIp(userInfo.getIp());
		userAllInfo.setCreateDate(userInfo.getCreateDate());
		userAllInfo.setLastestLoginDate(userInfo.getLastestLoginDate());
		userAllInfo.setLastestRevisionDate(userInfo.getLastestRevisionDate());
		userAllInfo.setState(userInfo.getState());
		userAllInfo.setIsOperation(userInfo.getIsOperation());
		if(null!=summary){
			userAllInfo.setSign(summary.getSign());
			userAllInfo.setInterest(summary.getInterest());
			userAllInfo.setIntroduction(summary.getIntroduction());
		}
		userAllInfo.setFlag(ResultUtils.SUCCESS);
		return userAllInfo;
	}
	/**
	 * 
	 * <p>Title: findUserInfo</p> 
	 * <p>Description: 查询用户的所有信息</p> 
	 * @author changjiang
	 * date 2014-7-18 下午3:57:32
	 * @return
	 */
	public UserInfo findUserInfo(ProductContext productContext,long userId){
		UserInfo userInfo = userInfoDAO.findUserInfo(productContext,userId);
		return userInfo;
	}
	
	/**
	 * 
	 * <p>Title: updateUserInfo</p> 
	 * <p>Description: 更新用户基本信息</p> 
	 * @author changjiang
	 * date 2014-7-18 下午4:43:03
	 * @param userInfo
	 */
	public int updateUserInfo(ProductContext productContext,UserInfo userInfo){
		return userInfoDAO.updateUserInfo(productContext,userInfo);
	}
	
	/**
	 * 
	 * <p>Title: insertintoUserInfo</p> 
	 * <p>Description: 插入用户基本信息</p> 
	 * @author changjiagn
	 * date 2014-7-18 下午8:31:20
	 * @param userInfo
	 */
	public void insertintoUserInfo(ProductContext productContext,UserInfo userInfo){
		userInfoDAO.insertUserInfo(productContext,userInfo);
	}
	
	/**
	 * 
	 * <p>Title: findUserAttention</p> 
	 * <p>Description: 查找用户关注人的列表</p> 
	 * @author somebody
	 * date 2014-7-19 下午1:03:00
	 * @param userId
	 * @return
	 */
	public List<UserInfo> findUserAttention(ProductContext productContext,long userId,int count,int pageIndex,int pageSize){
		//用户关注信息列表
		List<UserAttention> attentionList = userAttentionDAO.findUserAttention(productContext,userId,count,pageIndex,pageSize);
		Iterator<UserAttention> iterator = attentionList.iterator();
		//用户关注人的用户信息列表
		List<UserInfo> attentionUserInfoList = new ArrayList<UserInfo>();
		UserInfo userInfo= new UserInfo();
		long userAttentionId = 0;
		while(iterator.hasNext()){
			//关注关系表
			UserAttention userAttention = iterator.next();
			if(ResultUtils.DATAISNULL==userAttention.getFlag()){
				userInfo.setFlag(ResultUtils.DATAISNULL);
				attentionUserInfoList.add(userInfo);
				return attentionUserInfoList;
			}
			userAttentionId = userAttention.getUserAttentionId();
			//关注人的信息详情
			userInfo = userInfoDAO.findUserInfo(productContext, userAttentionId);
			if(null==userInfo){
				userInfo = new UserInfo();
				userInfo.setFlag(ResultUtils.DATAISNULL);
			}
			//插入关注时间
			userInfo.setAttentionDate(userAttention.getLatestRevisionDate());
			attentionUserInfoList.add(userInfo);
		}
		return attentionUserInfoList;
	}
	
	/**
	 * 
	 * <p>Title: findUserAttentionEachOther</p> 
	 * <p>Description: 查询用户相互关注的列表</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午12:10:16
	 * @param userId
	 * @return
	 */
	public List<UserInfo> findUserAttentionEachOther(long userId){
		List<UserAttention> attentionEachOther = new ArrayList<UserAttention>();
		attentionEachOther = userAttentionDAO.findUserAttentionEachOther(userId);
		
		List<UserInfo> attentionEachUserInfoList = new ArrayList<UserInfo>();
		Iterator<UserAttention> iterator = attentionEachOther.iterator();
		UserInfo userInfo = new UserInfo();
		long userAttentionId = 0;
		while(iterator.hasNext()){
			//关注关系表
			UserAttention userAttention = iterator.next();
			userAttentionId = userAttention.getUserAttentionId();
			//关注人的信息详情
			userInfo = userInfoDAO.findUserInfo(null, userAttentionId);
			if(null==userInfo||userInfo.getUserId()==0){
				continue;
			}
			//关注人的关注时间
			userInfo.setAttentionDate(userAttention.getLatestRevisionDate());
			userInfo.setFlag(0);
			attentionEachUserInfoList.add(userInfo);
		}
		return attentionEachUserInfoList;
	}

	/**
	 * 
	 * <p>Title: doAttention</p> 
	 * <p>Description: 添加关注</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午7:37:49
	 * @return
	 */
	public UserAttention doAttention(ProductContext productContext,UserAttention userAttention){
		//String userId =
		UserAttention isExistUserAttention = new UserAttention();
		isExistUserAttention= userAttentionDAO.findUserAttentionIsExist(productContext, userAttention);
		//关注信息不存在,插入关注信息
		if(0==(Long)isExistUserAttention.getAttentionId()){
			userAttentionDAO.insertAttention(productContext, userAttention);
			isExistUserAttention= userAttentionDAO.findUserAttentionIsExist(productContext, userAttention);
			return isExistUserAttention;
		}
		int isAttention = isExistUserAttention.getIsAttention();
		//用户已经关注
		if(1==isAttention){
			isExistUserAttention.setFlag(ResultUtils.EXISTED_ATTENTION);
			return isExistUserAttention;
		}
		//用户没有关注
		long attentionId = isExistUserAttention.getAttentionId();
		userAttention.setAttentionId(attentionId);
		userAttentionDAO.updateAttention(productContext, userAttention);
		isExistUserAttention= userAttentionDAO.findUserAttentionIsExist(productContext, userAttention);
		return isExistUserAttention;
	}
	
	/**
	 * 
	 * <p>Title: findUserAttentionIsExist</p> 
	 * <p>Description: 查询用户是否关注</p> 
	 * @author :changjiang
	 * date 2015-3-24 下午5:17:39
	 * @param userAttention
	 * @return
	 */
	public UserAttention findUserAttentionIsExist(UserAttention userAttention){
		return userAttentionDAO.findUserAttentionIsExist(null, userAttention);
	}
	
	/**
	 * 
	 * <p>Title: cancelAttention</p> 
	 * <p>Description: 用户取消关注</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午5:45:12
	 * @return
	 */
	public UserAttention cancelAttention(ProductContext productContext,UserAttention userAttention){
		UserAttention isExistUserAttention = new UserAttention();
		isExistUserAttention= userAttentionDAO.findUserAttentionIsExist(productContext, userAttention);
		//关注信息不存在
		if(0==(Long)isExistUserAttention.getAttentionId()){
			isExistUserAttention.setFlag(ResultUtils.NO_EXISTED_ATTENTION);
			return isExistUserAttention;
		}
		int isAttention = isExistUserAttention.getIsAttention();
		//用户已经取消关注
		if(0==isAttention){
			isExistUserAttention.setFlag(ResultUtils.EXISTED_ATTENTION);
			return isExistUserAttention;
		}
		//用户没有关注
		long attentionId = isExistUserAttention.getAttentionId();
		userAttention.setAttentionId(attentionId);
		userAttentionDAO.updateAttention(productContext, userAttention);
		isExistUserAttention= userAttentionDAO.findUserAttentionIsExist(productContext, userAttention);
		return isExistUserAttention;
	}
	/**
	 * 
	 * <p>Title: findUserFens</p> 
	 * <p>Description: 查找用户的粉丝</p> 
	 * @author changjiang
	 * date 2014-7-19 下午3:34:53
	 * @param userAttentionId
	 * @return
	 */
	public List<UserInfo> findUserFens(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize){
		//用户关注信息列表
		List<UserAttention> attentionList = userAttentionDAO.findUserFens(productContext, userAttentionId, count, pageIndex, pageSize);
		Iterator<UserAttention> iterator = attentionList.iterator();
		//用户关注人的用户信息列表
		List<UserInfo> fensUserInfoList = new ArrayList<UserInfo>();
		UserInfo userInfo= new UserInfo();
		long userId = 0;
		while(iterator.hasNext()){
			//关注关系表
			UserAttention userAttention = iterator.next();
			userAttentionId = userAttention.getUserId();
			//关注人的信息详情
			userInfo = userInfoDAO.findUserInfo(productContext, userAttentionId);
			if(null==userInfo||userInfo.getUserId()==0){
				continue;
			}
			userInfo.setAttentionDate(userAttention.getLatestRevisionDate());
			fensUserInfoList.add(userInfo);
		}
		return fensUserInfoList;
	}
	
	/**
	 * 
	 * <p>Title: findAttentionUserList</p> 
	 * <p>Description: 查找关注这个人的列表</p> 
	 * @author :changjiang
	 * date 2015-2-28 下午8:33:38
	 * @param productContext
	 * @param userAttentionId
	 * @param count
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<UserAttention> findAttentionUserList(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize){
		return userAttentionDAO.findUserFens(productContext, userAttentionId, count, pageIndex, pageSize);
	}
	
	/**
	 * 
	 * <p>Title: insertUserSummary</p> 
	 * <p>Description: 插入用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午12:53:40
	 * @return
	 */
	public int insertUserSummary(ProductContext productContext,UserSummary userSummary){
		return userSummaryDAO.insertUserSummary(productContext, userSummary);
	}
	
	/**
	 * 
	 * <p>Title: updateUserSummary</p> 
	 * <p>Description: 更新用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午2:33:23
	 * @param productContext
	 * @param userSummary
	 * @return
	 */
	public int updateUserSummary(ProductContext productContext,UserSummary userSummary){
		return userSummaryDAO.updateUserSummary(productContext, userSummary);
	}
	
	/**
	 * 
	 * <p>Title: findUserSummaryById</p> 
	 * <p>Description: 查询用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午2:37:26
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public UserSummary findUserSummaryById(ProductContext productContext,long userId){
		return userSummaryDAO.findUserSummaryById(productContext, userId);
	}
	
	/**
	 * 
	 * <p>Title: findRegistListByMobileList</p> 
	 * <p>Description: 查询用户已经注册的列表</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午2:49:08
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findRegistListByMobileList(ProductContext productContext,List<String> mobilePhoneList){
		return userInfoDAO.findRegistListByMobileList(productContext, mobilePhoneList);
	}
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询用户列表根据用户id集合</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:15:45
	 * @return
	 */
	public List<UserInfo> findUserListByUseridList(ProductContext productContext,List<Long> userids){
		//去掉敏感信息，比如密码等
		List<UserInfo> users = userInfoDAO.findUserListByUseridList(productContext, userids);
		if(users!=null && users.size()>0){
			for(int i=0;i<users.size();i++){
				users.get(i).setPassword("");
				users.get(i).setPasswordMd5("");
			}
		}
		return users;
	}
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询详细用户列表根据用户id集合</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:15:45
	 * @return
	 */
	public List<UserAllInfo> findUserAllInfoListByUseridList(ProductContext productContext,List<Long> userids){
		//去掉敏感信息，比如密码等
		List<UserInfo> users = userInfoDAO.findUserListByUseridList(productContext, userids);
		List<UserAllInfo> userAllInfos = new ArrayList<UserAllInfo>();
		if(users!=null && users.size()>0){
			List<UserSummary> userSummarys = userSummaryDAO.findUserSummarysByUserids(productContext, userids);
			for(int i=0;i<users.size();i++){
				UserInfo userInfo = users.get(i);
				UserAllInfo userAllInfo = new UserAllInfo();
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
				userAllInfo.setAge(userInfo.getAge());
				userAllInfo.setConstellation(userInfo.getConstellation());
				userAllInfo.setSign("");
				userAllInfo.setInterest("");
				userAllInfo.setIntroduction("");
				userAllInfo.setIdentification("");
				if(userSummarys!=null && userSummarys.size()>0){
					for(int j=0;j<userSummarys.size();j++){
						UserSummary userSummary = userSummarys.get(j);
						if(userSummary.getUserId()==userInfo.getUserId()){
							userAllInfo.setSign(userSummary.getSign());
							userAllInfo.setInterest(userSummary.getInterest());
							userAllInfo.setIntroduction(userSummary.getIntroduction());
							userAllInfo.setIdentification(userSummary.getIdentification());
							break;
						}
					}
				}
				userAllInfos.add(userAllInfo);
			}
		}
		return userAllInfos;
	}
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询朋友关注的列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午2:32:12
	 * @return
	 */
	public List<Long> findFriendsAttentionList(List<Long> userIdList){
		return userAttentionDAO.findUserAttentionList(userIdList);
	}
	
	/**
	 * 
	 * <p>Title: findUserAttentionList</p> 
	 * <p>Description: 查询用户关注列表</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午10:36:40
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<Long> findUserAttentionList(long userId,int pageIndex,int pageSize){
		return userAttentionDAO.findUserAttention(userId, pageIndex, pageSize);
	}
	/**
	 * 
	 * <p>Title: findFriendsAttentionUser</p> 
	 * <p>Description: 查询朋友关注人的信息列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:41:02
	 * @param friendsAttentionSet
	 * @return
	 */
	public List<UserInfo> findFriendsAttentionUser(List<Long> friendsAttentionSet){
		return userInfoDAO.findFriendsAttentionList(friendsAttentionSet);
	}
	
	/**
	 * 
	 * <p>Title: findUserAttentionCount</p> 
	 * <p>Description: 查询用户关注的人数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:32:59
	 * @param userId
	 * @return
	 */
	public int findUserAttentionCount(long userId){
		return userAttentionDAO.findUserAttentionCount(userId);
	}
	
	/**
	 * 
	 * <p>Title: findUserFensCount</p> 
	 * <p>Description: 查询用户的粉丝数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:37:19
	 * @return
	 */
	public int findUserFensCount(long userId){
		return userAttentionDAO.findUserFensCount(userId);
	}
	
	
	
	/**
	 * 
	 * <p>Title: editUserInfo</p> 
	 * <p>Description: 编辑用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-31 上午10:07:43
	 * @return
	 */
	public UserAllInfo editUserInfo(UserInfo userInfo,UserSummary userSummary){
		UserAllInfo userAllInfo = new UserAllInfo();
		UserInfo userNameIsExist = userInfoDAO.findUserNameIsExist(userInfo);
		if(null!=userNameIsExist){
			userAllInfo.setFlag(ResultUtils.EXISTED_USER_NAME);
			if(!userInfo.getFaceAddress().equals("")){
				userInfoDAO.updateUserFaceUrl(userInfo.getUserId(),userInfo.getFaceAddress());
			}
			return userAllInfo;
		}
		
		long userId = userInfo.getUserId();
		UserInfo info = userInfoDAO.findUserInfo(null, userId);
		userInfoDAO.updateUserInfo(null, userInfo);
		userSummaryDAO.updateUserSummary(null, userSummary);
		UserSummary summary= userSummaryDAO.findUserSummaryById(null, userId);
		userAllInfo.setUserId(userId);
		userAllInfo.setLoginName(userInfo.getLoginName());
		userAllInfo.setPassword(userInfo.getPassword());
		userAllInfo.setPasswordMd5(userInfo.getPasswordMd5());
		userAllInfo.setMobilePhone(userInfo.getMobilePhone());
		userAllInfo.setName(userInfo.getName());
		userAllInfo.setFaceAddress(userInfo.getFaceAddress());
		userAllInfo.setBirthday(userInfo.getBirthday());
		userAllInfo.setSex(userInfo.getSex());
		userAllInfo.setIp(userInfo.getIp());
		userAllInfo.setCreateDate(userInfo.getCreateDate());
		//userAllInfo.setLastestLoginDate(userInfo.getLastestLoginDate());
		userAllInfo.setLastestRevisionDate(userInfo.getLastestRevisionDate());
		userAllInfo.setAffectiveStates(userInfo.getAffectiveStates());
		userAllInfo.setResidence(userInfo.getResidence());
		userAllInfo.setProfession(userInfo.getProfession());
		if(null!=summary){
			userAllInfo.setSign(summary.getSign());
			userAllInfo.setInterest(summary.getInterest());
			userAllInfo.setIntroduction(summary.getIntroduction());
		}else{
			userSummaryDAO.insertUserSummary(null, userSummary);
			summary= userSummaryDAO.findUserSummaryById(null, userId);
		}
		userAllInfo.setFlag(ResultUtils.SUCCESS);
		return userAllInfo;
	}
	
	/**
	 * 
	 * <p>Title: userNameIsExist</p> 
	 * <p>Description: 查询是否含有这个昵称</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午4:51:01
	 * @param userInfo
	 * @return
	 */
	public UserInfo userNameIsExist(UserInfo userInfo){
		UserInfo info= userInfoDAO.findUserNameIsExist(userInfo);
		if(null==info){
			info = new UserInfo();
			info.setFlag(ResultUtils.SUCCESS);
			return info;
		}
		info.setFlag(ResultUtils.EXISTED_USER_NAME);
		return info;
	}
	
	/**
	 * 
	 * <p>Title: findUserInfoByName</p> 
	 * <p>Description: 根据昵称查询用户信息</p> 
	 * @author :changjiang
	 * date 2014-8-3 下午2:04:14
	 * @return
	 */
	public UserInfo findUserInfoByName(String name){
		return userInfoDAO.findUserInfoByName(name);
	}
	
	public VersionInfo findLatestVersion(){
		return userInfoDAO.findLatestVersion();
	}
	
	/**
	 * 
	 * <p>Title: updateTwoDimensionCode</p> 
	 * <p>Description: 更新用户二维码</p> 
	 * @author :changjiang
	 * date 2014-8-31 下午2:47:49
	 * @param userInfo
	 * @return
	 */
	public int updateTwoDimensionCode(UserInfo userInfo){
		return userInfoDAO.updateTwoDimensionCode(userInfo);
	}
	
	/**
	 * 
	 * <p>Title: findTalentZoneInfo</p> 
	 * <p>Description: 查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午10:46:55
	 * @param id
	 * @return
	 */
	public TalentZone findTalentZoneInfo(long id){
		return talentZoneDAO.findTalentZoneInfo(id);
	}
	
	/**
	 * 
	 * <p>Title: findTalentZoneLinkList</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-9-12 上午12:39:15
	 * @param talentZoneId
	 * @return
	 */
	public List<TalentZoneLink> findTalentZoneLinkList(long talentZoneId){
		return talentZoneLinkDAO.findTalentZoneLinkList(talentZoneId);
	}
	
	/**
	 * 
	 * <p>Title: findTalentZoneInfoByType</p> 
	 * <p>Description: 根据type查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午12:06:07
	 * @param type
	 * @return
	 */
	public TalentZone findTalentZoneInfoByType(String type){
		return talentZoneDAO.findTalentZoneInfoByType(type);
	}
	
	/**
	 * 
	 * <p>Title: findUserAttentionTalentedPersons</p> 
	 * <p>Description: 查询关注的达人</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午3:00:58
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<UserAttention> findUserAttentionTalentedPersons(long userId,
			String type){
		return userAttentionDAO.findUserAttentionTalentedPersons(userId, type);
	}
	
	/**
	 * 
	 * <p>Title: findAllTalentZone</p> 
	 * <p>Description: 查找所有的达人圈</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午9:45:50
	 * @return
	 */
	public List<TalentZone> findAllTalentZone(){
		return talentZoneDAO.findAllTalentZone();
	}
	
	/**
	 * 
	 * <p>Title: insertintoUserBigValue</p> 
	 * <p>Description: 插入用户的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午4:47:39
	 * @param userBigValue
	 * @return
	 */
	public UserBigValue insertintoUserBigValue(UserBigValue userBigValue){
		UserBigValue userBig = new UserBigValue();
		long userId = userBigValue.getUserId();
		userBig = userBigDAO.findUserBigValue(userId);
		//当没有用户逼格值时，插入逼格值
		if(ResultUtils.DATAISNULL==userBig.getFlag()){
			int flag = userBigDAO.insertintoUserBigValue(userBigValue);
			if(ResultUtils.SUCCESS==flag){
				userBig = userBigDAO.findUserBigValue(userId);
			}
		}//当查找成功时，更新逼格值
		else if(ResultUtils.SUCCESS==userBig.getFlag()){
			float bigValue = userBig.getBigValue();
			bigValue = bigValue + userBigValue.getBigValue();
			userBig.setBigLevel(userBigValue.getBigLevel());
			userBig.setBigValue(bigValue);
			userBigDAO.updateUserBigValue(userBig);
			userBig = userBigDAO.findUserBigValue(userId);
		}
		return userBig;
	}
	
	/**
	 * 
	 * <p>Title: insertintoUserSelfTestValue</p> 
	 * <p>Description: 插入</p> 
	 * @author :changjiang
	 * date 2014-10-9 下午12:01:40
	 * @param userBigValue
	 * @return
	 */
	public UserBigValue insertintoUserSelfTestValue(UserBigValue userBigValue){
		UserBigValue userBig = new UserBigValue();
		long userId = userBigValue.getUserId();
		userBig = userBigDAO.findUserBigValue(userId);
		//当没有用户逼格值时，插入逼格值
		if(ResultUtils.DATAISNULL==userBig.getFlag()){
			int flag = userBigDAO.insertintoUserBigValue(userBigValue);
			if(ResultUtils.SUCCESS==flag){
				userBig = userBigDAO.findUserBigValue(userId);
			}
		}//当查找成功时，更新逼格值
		else if(ResultUtils.SUCCESS==userBig.getFlag()){
			float selfTestValue = userBigValue.getSelfTest();
			userBig.setBigLevel(userBigValue.getBigLevel());
			userBig.setSelfTest(selfTestValue);
			userBigDAO.updateUserBigValue(userBig);
			userBig = userBigDAO.findUserBigValue(userId);
		}
		return userBig;
	}
	
	/**
	 * 
	 * <p>Title: findUserBigValue</p> 
	 * <p>Description: 查询用户的逼格值</p> 
	 * @author :changjiang
	 * date 2014-10-10 下午5:04:36
	 * @param userId
	 * @return
	 */
	public UserBigValue findUserBigValueByUserId(long userId){
		UserBigValue userBigValue = new UserBigValue();
		userBigValue = userBigDAO.findUserBigValue(userId);
		int flag = userBigValue.getFlag();
		if(ResultUtils.DATAISNULL==flag){
			userBigValue.setUserId(userId);
			userBigValue.setBigLevel(0);
			userBigValue.setBigValue(0);
			userBigValue.setSelfTest(0);
			userBigValue.setIsDelete(0);
			userBigDAO.insertintoUserBigValue(userBigValue);
			userBigValue = userBigDAO.findUserBigValue(userId);
		}
		return userBigValue;
	}
	
	/**
	 * 
	 * <p>Title: findUserBeatPercent</p> 
	 * <p>Description: 获取打败的百分比</p> 
	 * @author :changjiang
	 * date 2014-10-11 下午1:09:29
	 * @return
	 */
	public float findUserBeatPercent(float selfTest){
		float result = 0;
		try{
			float beyondCount = userBigDAO.findUserBigBeyondCount(selfTest);
			float count = userBigDAO.findUserBigCount();
			result = beyondCount/count;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Title: insertintoUserAlbum</p> 
	 * <p>Description: 插入用户相册</p> 
	 * @author :changjiang
	 * date 2014-10-17 下午12:01:06
	 * @param userAlbum
	 * @return
	 */
	public UserAlbum insertintoUserAlbum(long userId, List contentList, String type){
		UserAlbum userAlbum = new UserAlbum();
		UserAlbum uAlbum = new UserAlbum();
		try {
		long sysdate = System.currentTimeMillis();
		long id = reskeyWork.getId();
		userAlbum.setId(id);
		userAlbum.setUserId(userId);
		userAlbum.setTitle("个人相册");
		userAlbum.setType(type);
		
		userAlbum.setIsDelete(0);
		userAlbum.setCreateDate(sysdate);
		userAlbum.setLatestRevisionDate(sysdate);
		
		int flag = ResultUtils.ERROR;
		//查找用户的个人相册
		uAlbum = userAlbumDAO.findUserAlbumByTitle(userAlbum);
		flag = uAlbum.getFlag();
		String content = uAlbum.getContent();
		ObjectMapper objectMapper  = new ObjectMapper();
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, String> imgList = new HashMap<String,String>();
		int listSize = 0;
		String contentUpdate = "";
		//当存在这个相册时，修改相册内容
		if(ResultUtils.SUCCESS==flag){
			imgList = objectMapper.readValue(content, Map.class);
			listSize = imgList.size();
			for(int i=0;i<contentList.size();i++){
				imgList.put(reskeyWork.getId()+"", (String)contentList.get(i));
				listSize++;
			}
			//imgList.add(map);
			contentUpdate = objectMapper.writeValueAsString(imgList);
			uAlbum.setContent(contentUpdate);
			flag = userAlbumDAO.updateUserAlbum(uAlbum);
			uAlbum = userAlbumDAO.findUserAlbumById(uAlbum.getId());
		}
		else{//当不存在这个相册时，新建相册
			for(int i=0;i<contentList.size();i++){
				map.put(reskeyWork.getId()+"", (String)contentList.get(i));
				listSize++;
			}
			//imgList.add(map);
			contentUpdate = objectMapper.writeValueAsString(map);
			userAlbum.setContent(contentUpdate);
			flag =  userAlbumDAO.insertintoUserAlbum(userAlbum);
			uAlbum = userAlbumDAO.findUserAlbumById(id);
		}
		uAlbum.setFlag(flag);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uAlbum;
	}
	
	/**
	 * 
	 * <p>Title: deleteUserPicture</p> 
	 * <p>Description: 删除用户照片</p> 
	 * @author :changjiang
	 * date 2014-10-22 上午11:55:36
	 * @param id
	 * @param contentList
	 * @return
	 */
	public UserAlbum deleteUserPicture(long id, List contentList){
		UserAlbum userAlbum = userAlbumDAO.findUserAlbumById(id);
		if(ResultUtils.SUCCESS==userAlbum.getFlag()){
			String contentUpdate = "";
			String content = userAlbum.getContent();
			Map<String, String> imgList = new HashMap<String,String>();
			ObjectMapper objectMapper  = new ObjectMapper();
			try {
				imgList = objectMapper.readValue(content, Map.class);
				Iterator<String> contentIt = contentList.iterator();
				Iterator<Map.Entry<String, String>> imgIt = imgList.entrySet().iterator();
				String imgString = "";
				String valueString = "";
				while(contentIt.hasNext()){
					imgString = contentIt.next();
					while(imgIt.hasNext()){
						Map.Entry<String, String> entry =imgIt.next();
						valueString = entry.getKey();
						if(valueString.equals(imgString)){
							imgIt.remove();
							break;
						}
					}
				}
				contentUpdate = objectMapper.writeValueAsString(imgList);
				userAlbum.setContent(contentUpdate);
				userAlbumDAO.updateUserAlbum(userAlbum);
				userAlbum =  userAlbumDAO.findUserAlbumById(id);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return userAlbum;
	}
	
	/**
	 * 
	 * <p>Title: findUserAlbumById</p> 
	 * <p>Description: 根据ID查询用户相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 上午11:07:23
	 * @param id
	 * @return
	 */
	public UserAlbum findUserAlbumById(long id){
		return userAlbumDAO.findUserAlbumById(id);
	}
	
	/**
	 * 
	 * <p>Title: findUserAlbumByUid</p> 
	 * <p>Description: 根据uid查询用户相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 下午5:13:38
	 * @param userId
	 * @return
	 */
	public List<UserAlbum> findUserAlbumByUid(long userId){
		List<UserAlbum> userAlbumList = userAlbumDAO.findUserAlbumByUid(userId);
		if(null==userAlbumList||userAlbumList.size()==0){
			insertintoUserAlbum(userId,new ArrayList<String>(),"0");
			userAlbumList = userAlbumDAO.findUserAlbumByUid(userId);
		}
		return userAlbumList;
	}
	
	/**
	 * 
	 * <p>Title: findUserInfoByMobilePhone</p> 
	 * <p>Description: 根据手机号查询用户</p> 
	 * @author :changjiang
	 * date 2015-1-29 下午12:39:28
	 * @param productContext
	 * @param mobilePhone
	 * @return
	 */
	public UserInfo findUserInfoByMobilePhone(ProductContext productContext,String mobilePhone){
		return userInfoDAO.findUserInfoByMobilePhone(productContext, mobilePhone);
	}
	
	/**
	 * 
	 * <p>Title: findUserStateByUserId</p> 
	 * <p>Description: 根据用户id查找用户状态</p> 
	 * @author :changjiang
	 * date 2015-3-20 下午4:56:26
	 * @param userId
	 * @return
	 */
	public String findUserStateByUserId(long userId){
		return userInfoDAO.findUserStateByUserId(userId);
	}
	
	/**
	 * 
	 * <p>Title: editPassword</p> 
	 * <p>Description: 修改密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午5:40:10
	 * @param oldPassword
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	public UserInfo editPassword(long uid, String oldPassword,String newPassword,
			String verifyPassword){
		
		int flag = ResultUtils.ERROR;
		
		//查询用户的信息
		UserInfo userInfo = userInfoDAO.findUserInfo(null, uid);
				//findUserInfoByMobilePhone(null, mobilephone);
		int resultflag = userInfo.getFlag();
		
		String userPassWord = userInfo.getPassword();//得到这个用户的密码
		
		long systime = System.currentTimeMillis();
		if(ResultUtils.SUCCESS==resultflag){//返回成功时
			if(userPassWord.length()<32){//用户密码为老密码时
				if(userPassWord.equals(oldPassword)){//相等时修改密码
					if(newPassword.equals(verifyPassword)){//密码相等时，修改密码成功
						String newPasswordMD5 = MD5Utils.md5(newPassword);//新密码加密
						String newPasswordRdm = MD5Utils.md5AndRandom(newPassword);//新密码随机加密
						userInfoDAO.editPassword(uid, newPasswordMD5, newPasswordRdm,systime);
						userInfo = userInfoDAO.findUserInfo(null, uid);
								//findUserInfoByMobilePhone(null, mobilephone);
						if(userInfo.getPassword().equals(newPasswordMD5)){//修改成功
							flag = ResultUtils.SUCCESS;
						}
						}else{
							flag = ResultUtils.ERROR_VERIFY_PASSWORD;
						}
					}else{
						flag = ResultUtils.ERROR_VERIFY_PASSWORD;
					}
			}else{//密码为新密码时
				String passwordMD5 = MD5Utils.md5(oldPassword);
				if(passwordMD5.equals(userPassWord)){//加密的密码相等时修改密码
					if(newPassword.equals(verifyPassword)){//输入密码相等时，修改密码成功
						String newPasswordMD5 = MD5Utils.md5(newPassword);//新密码加密
						String newPasswordRdm = MD5Utils.md5AndRandom(newPassword);//新密码随机加密
						userInfoDAO.editPassword(uid, newPasswordMD5, newPasswordRdm,systime);
						userInfo = userInfoDAO.findUserInfo(null, uid);
								//findUserInfoByMobilePhone(null, mobilephone);
						if(userInfo.getPassword().equals(newPasswordMD5)){//修改成功
							flag = ResultUtils.SUCCESS;
						}
					}else{
						flag = ResultUtils.ERROR_VERIFY_PASSWORD;
					}
				}else{//密码错误时
					flag = ResultUtils.ERROR_INPUT_PASSWORD;
				}
			}
		}else{
			flag = ResultUtils.IS_ALREADY_REPORT;
		}
		userInfo.setFlag(flag);
		return userInfo;
	}
	
	/**
	 * 
	 * <p>Title: forgetPassword</p> 
	 * <p>Description: 忘记密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午8:30:03
	 * @param mobilephone
	 * @param newPassword
	 * @param newPasswordRdm
	 * @param sysTime
	 * @return
	 */
	public UserInfo forgetPassword(String mobilephone,String newPassword,String verifyPassword){
		int flag = ResultUtils.ERROR;
		//查询用户的信息
		UserInfo userInfo = userInfoDAO.findUserInfoByMobilePhone(null, mobilephone);
		int resultflag = userInfo.getFlag();
		if(ResultUtils.SUCCESS==resultflag){
			long sysTime = System.currentTimeMillis();
			String newPasswordMD5 = MD5Utils.md5(newPassword);//新密码加密
			String newPasswordRdm = MD5Utils.md5AndRandom(newPassword);//新密码随机加密
			userInfoDAO.editPassword(userInfo.getUserId(), newPasswordMD5, newPasswordRdm, sysTime);
			userInfo = userInfoDAO.findUserInfoByMobilePhone(null, mobilephone);
			if(userInfo.getPassword().equals(newPasswordMD5)){//
				flag = ResultUtils.SUCCESS;
			}
		}else{
			flag = ResultUtils.NO_EXISTED_USER;
		}
		userInfo.setFlag(flag);
		return userInfo;
	}
	
	/**
	 * 
	 * <p>Title: findSearchDuYao</p> 
	 * <p>Description: 搜索毒药的标示</p> 
	 * @author :changjiang
	 * date 2015-5-7 下午2:40:02
	 * @return
	 */
	public int findSearchDuYao(){
		return userInfoDAO.findSearchDuYao();
	}
	
	/**
	 * 保存用户的催更推送相关信息
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
		if(userLatest==null || userLatest.getUserid()<=0){
			return false;
		}
		UserLatest olduserLatest = userLatestDAO.findUserlatestByUserid(userLatest.getUserid());
		if(olduserLatest!=null && olduserLatest.getUserid()>0){
			olduserLatest.setResourceid(userLatest.getResourceid());
			olduserLatest.setType(userLatest.getType());
			olduserLatest.setUpdatetime(userLatest.getUpdatetime());
			olduserLatest.setPushtime(0);//将推送时间归零，推送算法初始化
			int flag = userLatestDAO.updateUserlatest(olduserLatest);
			if(flag == ResultUtils.SUCCESS){
				return true;
			}
		}else if(olduserLatest==null){
			int flag = userLatestDAO.insertUserlatest(userLatest);
			if(flag == ResultUtils.SUCCESS){
				return true;
			}
		}
		return false;
	}

	/**
	 * 绑定手机号码
	 * @param userId
	 * @param mobilephone
	 * @param newPassword
	 * @return
	 */
	public UserInfo bindingMobile(Long userId, String mobilephone, String newPassword){
		UserInfo userInfo = userInfoDAO.findUserInfo(null,userId);
		int resultflag = userInfo.getFlag();
		if(ResultUtils.SUCCESS==resultflag){
			long sysTime = System.currentTimeMillis();
			String newPasswordMD5 = MD5Utils.md5(newPassword);//新密码加密
			String newPasswordRdm = MD5Utils.md5AndRandom(newPassword);//新密码随机加密
			userInfo = userInfoDAO.findUserInfoByMobilePhone(null,mobilephone);
			if(userInfo.getFlag()==ResultUtils.DATAISNULL){//当手机号不存在时
				resultflag = userInfoDAO.bindingMobile(userId,mobilephone,newPasswordMD5,newPassword,sysTime);
				userInfo = userInfoDAO.findUserInfo(null,userId);

			}else if(userInfo.getFlag()==ResultUtils.SUCCESS){//当用户存在时
				resultflag = ResultUtils.EXISTED_PHONE;
			}
			userInfo.setFlag(resultflag);
		}else{
			resultflag = ResultUtils.NO_EXISTED_USER;
			userInfo.setFlag(resultflag);
		}
		return userInfo;
	}
}
