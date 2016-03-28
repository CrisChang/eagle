package com.poison.ucenter.dao;

import java.util.List;
import java.util.Set;

import com.keel.framework.runtime.ProductContext;
import com.poison.eagle.entity.VersionInfo;
import com.poison.ucenter.model.UserInfo;

public interface UserInfoDAO {

	//增
	public int insertUserInfo(ProductContext productContext,UserInfo userInfo);
	
	//删
	public void deleteUserInfo();
	
	/**
	 * 
	 * <p>Title: updateUserInfo</p> 
	 * <p>Description: 修改用户的基本信息</p> 
	 * @author changjiang
	 * date 2014-7-18 下午4:21:44
	 */
	public int updateUserInfo(ProductContext productContext,UserInfo userinfo);

	/**
	 * 修改用户头像
	 * @param userId
	 * @param faceUrl
	 * @return
	 */
	public int updateUserFaceUrl(long userId,String faceUrl);
	
	/**
	 * 
	 * <p>Title: findUserInfo</p> 
	 * <p>Description:查询用户的所有信息 </p> 
	 * @author changjiang
	 * date 2014-7-18 下午3:59:07
	 * @return
	 */
	public UserInfo findUserInfo(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: findUserInfoByMobilePhone</p> 
	 * <p>Description: 根据手机号查询用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午4:29:21
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public UserInfo findUserInfoByMobilePhone(ProductContext productContext,String mobilePhone);
	
	/**
	 * 
	 * <p>Title: findUserInfoByLoginName</p> 
	 * <p>Description: 根据用户名查询用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午5:23:18
	 * @param productContext
	 * @param userInfo
	 * @return
	 */
	public UserInfo findUserInfoByLoginName(ProductContext productContext,String loginName);
	
	/**
	 * 
	 * <p>Title: NoRegistList</p> 
	 * <p>Description: 已经注册的用户列表</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午2:09:44
	 * @return
	 */
	public List<UserInfo> findRegistListByMobileList(ProductContext productContext,List<String> mobilePhoneList);
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询朋友</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:15:45
	 * @return
	 */
	public List<UserInfo> findFriendsAttentionList(List<Long> friendsAttentionSet);
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询用户列表根据用户id集合</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:15:45
	 * @return
	 */
	public List<UserInfo> findUserListByUseridList(ProductContext productContext,List<Long> userids);
	
	/**
	 * 
	 * <p>Title: findUserNameIsExist</p> 
	 * <p>Description: 查询用户昵称是否存在</p> 
	 * @author :changjiang
	 * date 2014-7-31 上午10:34:26
	 * @param userInfo
	 * @return
	 */
	public UserInfo findUserNameIsExist(UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: findUserInfoByName</p> 
	 * <p>Description: 根据昵称查找用户信息</p> 
	 * @author :changjiang
	 * date 2014-8-3 下午1:53:06
	 * @param name
	 * @return
	 */
	public UserInfo findUserInfoByName(String name);
	
	public VersionInfo findLatestVersion();
	
	/**
	 * 
	 * <p>Title: updateTwoDimensionCode</p> 
	 * <p>Description: 更新用户二维码</p> 
	 * @author :changjiang
	 * date 2014-8-31 下午2:42:28
	 * @param userInfo
	 * @return
	 */
	public int updateTwoDimensionCode(UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: findUserStateByUserId</p> 
	 * <p>Description: 根据用户id查询用户状态位</p> 
	 * @author :changjiang
	 * date 2015-3-20 下午1:57:24
	 * @param userId
	 * @return
	 */
	public String findUserStateByUserId(long userId);
	
	/**
	 * 
	 * <p>Title: editPassword</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午4:13:01
	 * @param mobilephone
	 * @param newPassword
	 * @return
	 */
	public int editPassword(long uid,String newPassword,String newPasswordRdm,long sysTime);
	
	/**
	 * 
	 * <p>Title: findSearchDuYao</p> 
	 * <p>Description: 搜索毒药的标示</p> 
	 * @author :changjiang
	 * date 2015-5-7 下午2:33:03
	 * @return
	 */
	public int findSearchDuYao();

	/**
	 * 绑定手机号
	 * @param uid
	 * @param mobilePhone
	 * @param newPassword
	 * @param newPasswordRdm
	 * @param sysTime
	 * @return
	 */
	public int bindingMobile(long uid,String mobilePhone,String newPassword,String newPasswordRdm,long sysTime);
}
