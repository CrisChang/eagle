package com.poison.ucenter.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.keel.framework.runtime.ProductContext;
import com.poison.eagle.entity.VersionInfo;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserInfoDAO;
import com.poison.ucenter.model.UserInfo;

public class UserInfoDAOImpl extends SqlMapClientDaoSupport implements UserInfoDAO{

	private static final  Log LOG = LogFactory.getLog(UserInfoDAOImpl.class);
	/**
	 * 插入用户基本信息
	 */
	@Override
	public int insertUserInfo(ProductContext productContext,UserInfo userInfo) {
		int flag = ResultUtils.ERROR;
		try{
			flag = (Integer) getSqlMapClientTemplate().insert("insertintoUserInfo",userInfo);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	@Override
	public void deleteUserInfo() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 修改用户信息
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int updateUserInfo(ProductContext productContext,UserInfo userinfo) {
		Object object = new Object();
		try{
			object =  getSqlMapClientTemplate().update("updateUserInfo", userinfo);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 修改用户的头像
	 * @param userId
	 * @param faceUrl
	 * @return
	 */
	@Override
	public int updateUserFaceUrl(long userId, String faceUrl) {
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.put("userId",userId);
			map.put("faceUrl",faceUrl);
			getSqlMapClientTemplate().update("updateUserFaceUrl", map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
		return 0;
	}

	/**
	 * 查询用户的所有信息
	 */
	@SuppressWarnings("deprecation")
	@Override
	public UserInfo findUserInfo(ProductContext productContext,long userId) {
		UserInfo userInfo = new UserInfo();
		try{
			userInfo = (UserInfo) getSqlMapClientTemplate().queryForObject("findUserInfo",userId);
			if(null!=userInfo){
				userInfo.setFlag(ResultUtils.SUCCESS);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userInfo = new UserInfo();
			userInfo.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userInfo;
	}

	/**
	 * 根据手机号查询用户信息
	 */
	@Override
	public UserInfo findUserInfoByMobilePhone(ProductContext productContext,
			String mobilePhone) {
		UserInfo userInfo = new UserInfo();
		try{
			userInfo = (UserInfo) getSqlMapClientTemplate().queryForObject("findUserInfoByMobilPhone",mobilePhone);
			if(null==userInfo){
				userInfo = new UserInfo();
				userInfo.setFlag(ResultUtils.DATAISNULL);
				return userInfo;
			}
			userInfo.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			userInfo = new UserInfo();
			userInfo.setFlag(ResultUtils.QUERY_ERROR);
			return userInfo;
		}
		return userInfo;
	}

	/**
	 * 根据用户名查询用户
	 */
	@Override
	public UserInfo findUserInfoByLoginName(ProductContext productContext,
			String loginName) {
		UserInfo userInfo = new UserInfo();
		try{
			userInfo = (UserInfo) getSqlMapClientTemplate().queryForObject("findUserInfoByLoginName",loginName);
			if(null==userInfo){
				userInfo = new UserInfo();
				userInfo.setFlag(ResultUtils.DATAISNULL);
				return userInfo;
			}
			userInfo.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userInfo.setFlag(ResultUtils.QUERY_ERROR);
			return userInfo;
		}
		return userInfo;
	}

	/**
	 * 根据手机号码查询注册过的用户
	 */
	@Override
	public List<UserInfo> findRegistListByMobileList(ProductContext productContext,
			List<String> mobilePhoneList) {
		List<UserInfo> registList = new ArrayList<UserInfo>();
		if(null==mobilePhoneList||mobilePhoneList.size()==0){
			return registList;
		}
		Map<String, List<String>> mobileList = new HashMap<String, List<String>>();
		mobileList.put("mobileList", mobilePhoneList);
		UserInfo userInfo = new UserInfo();
		try{
			registList = getSqlMapClientTemplate().queryForList("findRegistListByMobileList",mobileList);
			if(null==registList||registList.size()==0){
				registList = new ArrayList<UserInfo>();
				userInfo.setFlag(ResultUtils.DATAISNULL);
				registList.add(userInfo);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			registList = new ArrayList<UserInfo>();
			userInfo.setFlag(ResultUtils.ERROR);
			registList.add(userInfo);
		}
		return registList;
	}

	/**
	 * 查询朋友关注人列表
	 */
	@Override
	public List<UserInfo> findFriendsAttentionList(
			List<Long> friendsAttentionSet) {
		List<UserInfo> friendsAttentionUsersList = new ArrayList<UserInfo>();
		UserInfo userInfo = new UserInfo();
		if(null == friendsAttentionSet||friendsAttentionSet.size()==0){
			userInfo.setFlag(ResultUtils.DATAISNULL);
			friendsAttentionUsersList.add(userInfo);
			return friendsAttentionUsersList;
		}
		Map<String, List<Long>> friendsMap = new HashMap<String, List<Long>>();
		friendsMap.put("friendIdList", friendsAttentionSet);
		try{
			friendsAttentionUsersList = getSqlMapClientTemplate().queryForList("findFriendsAttentionList",friendsMap);
			if(null==friendsAttentionUsersList){
				friendsAttentionUsersList = new ArrayList<UserInfo>();
				userInfo.setFlag(ResultUtils.DATAISNULL);
				friendsAttentionUsersList.add(userInfo);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			friendsAttentionUsersList = new ArrayList<UserInfo>();
			userInfo.setFlag(ResultUtils.ERROR);
			friendsAttentionUsersList.add(userInfo);
		}
		return friendsAttentionUsersList;
	}
	
	/**
	 * 查询用户列表根据用户id集合
	 */
	@Override
	public List<UserInfo> findUserListByUseridList(ProductContext productContext,List<Long> userids) {
		List<UserInfo> userList = new ArrayList<UserInfo>();
		UserInfo userInfo = new UserInfo();
		if(null == userids||userids.size()==0){
			return userList;
		}
		Map<String, List<Long>> map = new HashMap<String, List<Long>>();
		map.put("useridList", userids);
		try{
			userList = getSqlMapClientTemplate().queryForList("findUserListByUseridList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userList = new ArrayList<UserInfo>();
			userInfo.setFlag(ResultUtils.ERROR);
			userList.add(userInfo);
		}
		return userList;
	}

	/**
	 * 查询用户昵称是否存在
	 */
	@Override
	public UserInfo findUserNameIsExist(UserInfo userInfo) {
		UserInfo info = new UserInfo();
		try{
			info = (UserInfo) getSqlMapClientTemplate().queryForObject("findUserNameIsExist", userInfo);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return info;
	}

	/**
	 * 根据昵称查找用户信息
	 */
	@Override
	public UserInfo findUserInfoByName(String name) {
		UserInfo userInfo = new UserInfo();
		try{
			userInfo =  (UserInfo) getSqlMapClientTemplate().queryForObject("findUserInfoByName", name);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userInfo.setFlag(ResultUtils.QUERY_ERROR);
		}
		return userInfo;
	}

	@Override
	public VersionInfo findLatestVersion() {
		VersionInfo versionInfo = new VersionInfo();
		try{
			versionInfo = (VersionInfo) getSqlMapClientTemplate().queryForObject("findIsUpdateVersion");
			if(null==versionInfo){
				versionInfo = new VersionInfo();
				versionInfo.setFlag(ResultUtils.SUCCESS);
			}
			versionInfo.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return versionInfo;
	}

	/**
	 * 更新用户二维码
	 */
	@Override
	public int updateTwoDimensionCode(UserInfo userInfo) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateTwoDimensionCode",userInfo);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 根据用户id查询用户的状态
	 */
	@Override
	public String findUserStateByUserId(long userId) {
		String state = "0";
		try{
			state = (String) getSqlMapClientTemplate().queryForObject("findUserStateByUserId",userId);
			if(null==state){
				state = "2";
			}
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			state = "0";
		}
		return state;
	}

	/**
	 * 修改密码
	 */
	@Override
	public int editPassword(long uid, String newPassword,
			String newPasswordRdm,long sysTime) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("uid", uid);
			map.put("newPassword", newPassword);
			map.put("newPasswordRdm", newPasswordRdm);
			map.put("sysTime", sysTime);
			getSqlMapClientTemplate().update("editPassword",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 搜索毒药的标示
	 */
	@Override
	public int findSearchDuYao() {
		int flag = 0;
		try{
			flag = (Integer) getSqlMapClientTemplate().queryForObject("findIsSearchDuyao");
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 绑定手机号
	 * @param uid
	 * @param mobilePhone
	 * @param newPassword
	 * @param newPasswordRdm
	 * @param sysTime
	 * @return
	 */
	@Override
	public int bindingMobile(long uid, String mobilePhone, String newPassword, String newPasswordRdm, long sysTime) {
		int flag = ResultUtils.ERROR;
		try{
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("userId",uid);
			map.put("mobilePhone",mobilePhone);
			map.put("newPassword",newPassword);
			map.put("newPasswordRdm",newPasswordRdm);
			map.put("sysTime",sysTime);
			getSqlMapClientTemplate().update("bindingMobile",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

}
