package com.poison.ucenter.service;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.poison.eagle.entity.VersionInfo;
import com.poison.ucenter.model.TalentZone;
import com.poison.ucenter.model.TalentZoneLink;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;
import com.poison.ucenter.model.UserLatest;
import com.poison.ucenter.model.UserSummary;

public interface UcenterService {

	/**
	 * 
	 * <p>Title: findUserInfo</p> 
	 * <p>Description: 查询用户的所有信息</p> 
	 * @author changjiang
	 * date 2014-7-18 下午3:54:43
	 * @return
	 */
	public UserInfo findUserInfo(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: updateUserInfo</p> 
	 * <p>Description: 更新用户基本信息</p> 
	 * @author changjiang
	 * date 2014-7-18 下午4:45:18
	 */
	public int updateUserInfo(ProductContext productContext,UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: insertintoUserInfo</p> 
	 * <p>Description: 添加用户基本信息</p> 
	 * @author changjiang
	 * date 2014-7-18 下午8:32:19
	 * @param userInfo
	 */
	public void insertintoUserInfo(ProductContext productContext,UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: findUserAttention</p> 
	 * <p>Description: 查询用户关注人列表</p> 
	 * @author changjiang
	 * date 2014-7-19 下午1:05:35
	 * @param userId
	 * @return
	 */
	public List<UserInfo> findUserAttention(ProductContext productContext,long userId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUserFens</p> 
	 * <p>Description: 查询用户粉丝</p> 
	 * @author changjiang
	 * date 2014-7-19 下午3:36:30
	 * @param userAttentionId
	 * @return
	 */
	public List<UserInfo> findUserFens(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: insertUserSummary</p> 
	 * <p>Description: 插入用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午12:56:35
	 * @return
	 */
	public int insertUserSummary(ProductContext productContext,UserSummary userSummary);
	
	/**
	 * 
	 * <p>Title: updateUserSummary</p> 
	 * <p>Description: 更新用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午2:16:26
	 * @param productContext
	 * @param userSummary
	 * @return
	 */
	public int updateUserSummary(ProductContext productContext,UserSummary userSummary);
	
	/**
	 * 
	 * <p>Title: findUserSummaryByUserid</p> 
	 * <p>Description: 查询用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午2:40:04
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public UserSummary findUserSummaryByUserid(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: registUserByMobilePhone</p> 
	 * <p>Description: 根据电话号码注册用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午5:39:20
	 * @param productContext
	 * @param userInfo
	 * @return
	 */
	public UserInfo registUserByMobilePhone(ProductContext productContext,UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: registUserByLoginName</p> 
	 * <p>Description: 根据用户名注册</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午5:41:53
	 * @param productContext
	 * @param userInfo
	 * @return
	 */
	public UserInfo registUserByLoginName(ProductContext productContext,UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: checkLoginByMobile</p> 
	 * <p>Description: 手机号码登录验证</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午3:07:03
	 * @param productContext
	 * @param mobilePhone
	 * @param password
	 * @return
	 */
	public UserAllInfo checkLoginByMobile(ProductContext productContext,String mobilePhone,String password);
	
	/**
	 * 
	 * <p>Title: checkLoginByLoginName</p> 
	 * <p>Description: 用户名密码登录</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午3:20:19
	 * @return
	 */
	public UserAllInfo checkLoginByLoginName(ProductContext productContext,String loginName,String password);
	
	/**
	 * 
	 * <p>Title: doAttention</p> 
	 * <p>Description: 用户点击关注</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午5:03:48
	 * @param productContext
	 * @param userAttention
	 * @return
	 */
	public UserAttention doAttention(ProductContext productContext,UserAttention userAttention);
	
	/**
	 * 
	 * <p>Title: cancelAttention</p> 
	 * <p>Description: 用户取消关注</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午6:05:17
	 * @return
	 */
	public UserAttention cancelAttention(ProductContext productContext,UserAttention userAttention);
	
	/**
	 * 
	 * <p>Title: findRegistListByMobileList</p> 
	 * <p>Description: 查询用户已经注册列表</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午2:51:04
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findRegistListByMobileList(ProductContext productContext,List<String> mobilePhoneList);
	
	/**
	 * 
	 * <p>Title: findUnRegistListByMobileList</p> 
	 * <p>Description: 查询用户未注册列表</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午4:04:40
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findUnRegistListByMobileList(ProductContext productContext,List<String> mobilePhoneList);
	
	/**
	 * 
	 * <p>Title: findAttentionByMobileList</p> 
	 * <p>Description: 查询通讯录中已关注的列表</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午7:30:47
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findAttentionByMobileList(ProductContext productContext,List<String> mobilePhoneList,long userId,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUnAttentionByMobileList</p> 
	 * <p>Description: 查询通讯录中未关注的别表</p> 
	 * @author :changjiang
	 * date 2014-7-25 上午11:37:27
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findUnAttentionByMobileList(ProductContext productContext,List<String> mobilePhoneList,long userId,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 朋友关注的我未关注列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午2:43:51
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public List<UserInfo> findFriendsUnAttentionList(ProductContext productContext,long userId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findFriendsIsAttentionList</p> 
	 * <p>Description: 朋友关注的我已经关注的</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午7:05:22
	 * @param productContext
	 * @param userId
	 * @param count
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<UserInfo> findFriendsIsAttentionList(ProductContext productContext,long userId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionEachList</p> 
	 * <p>Description: 查找朋友关注的相互关注列表</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午2:27:08
	 * @param productContext
	 * @param userId
	 * @param count
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<UserInfo> findFriendsAttentionEachList(ProductContext productContext,long userId,int count,int pageIndex,int pageSize);
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
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询详细用户列表根据用户id集合</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:15:45
	 * @return
	 */
	public List<UserAllInfo> findUserAllInfoListByUseridList(ProductContext productContext,List<Long> userids);
	/**
	 * 
	 * <p>Title: findUserAttentionCount</p> 
	 * <p>Description: 查询用户关注人总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:39:42
	 * @return
	 */
	public int findUserAttentionCount(long userId);
	
	/**
	 * 
	 * <p>Title: findUserFensCount</p> 
	 * <p>Description: 查询用户粉丝的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:42:34
	 * @return
	 */
	public int findUserFensCount(long userId);
	
	/**
	 * 
	 * <p>Title: userAttentionEachList</p> 
	 * <p>Description: 查询用户相互关注列表</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午12:18:11
	 * @param userId
	 * @return
	 */
	public List<UserInfo> userAttentionEachList(long userId);
	
	/**
	 * 
	 * <p>Title: editUserInfo</p> 
	 * <p>Description: 编辑用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-31 上午10:44:01
	 * @param userInfo
	 * @param userSummary
	 * @return
	 */
	public UserAllInfo editUserInfo(UserInfo userInfo,UserSummary userSummary);
	
	/**
	 * 
	 * <p>Title: findUserAttentionList</p> 
	 * <p>Description: 查询用户关注人列表</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午10:37:51
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<Long> findUserAttentionList(long userId,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUserInfoByName</p> 
	 * <p>Description: 根据昵称查询用户信息</p> 
	 * @author :changjiang
	 * date 2014-8-3 下午2:05:35
	 * @return
	 */
	public UserInfo findUserInfoByName(String name);
	
	public VersionInfo findLatestVersion();
	
	/**
	 * 
	 * <p>Title: userNameIsExist</p> 
	 * <p>Description: 查询是否包含相同的昵称</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午4:52:16
	 * @param userInfo
	 * @return
	 */
	public UserInfo userNameIsExist(UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: updateTwoDimensionCode</p> 
	 * <p>Description: 查询用户二维码</p> 
	 * @author :changjiang
	 * date 2014-8-31 下午2:48:41
	 * @param userInfo
	 * @return
	 */
	public int updateTwoDimensionCode(UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: findTalentZoneInfo</p> 
	 * <p>Description: 查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午10:48:59
	 * @param id
	 * @return
	 */
	public TalentZone findTalentZoneInfo(long id);
	
	/**
	 * 
	 * <p>Title: findTalentZoneLinkList</p> 
	 * <p>Description: 查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-12 上午12:40:22
	 * @param talentZoneId
	 * @return
	 */
	public List<TalentZoneLink> findTalentZoneLinkList(long talentZoneId);
	
	/**
	 * 
	 * <p>Title: findTalentZoneInfoByType</p> 
	 * <p>Description: 根据type查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午12:06:58
	 * @param type
	 * @return
	 */
	public TalentZone findTalentZoneInfoByType(String type);
	
	/**
	 * 
	 * <p>Title: findUserAttentionTalentedPersons</p> 
	 * <p>Description: 查询关注的达人</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午3:01:46
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<UserAttention> findUserAttentionTalentedPersons(long userId,String type);
	
	/**
	 * 
	 * <p>Title: findAllTalentZone</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午9:49:43
	 * @return
	 */
	public List<TalentZone> findAllTalentZone();
	
	/**
	 * 
	 * <p>Title: insertintoUserBigValue</p> 
	 * <p>Description: 插入用户逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午6:10:49
	 * @param userBigValue
	 * @return
	 */
	public UserBigValue insertintoUserBigValue(UserBigValue userBigValue);
	
	/**
	 * 
	 * <p>Title: insertintoUserSelfTest</p> 
	 * <p>Description: 插入用户自测题</p> 
	 * @author :changjiang
	 * date 2014-10-9 上午11:59:04
	 * @param userBigValue
	 * @return
	 */
	public UserBigValue insertintoUserSelfTest(UserBigValue userBigValue);
	
	/**
	 * 
	 * <p>Title: findUserBigValueByUserId</p> 
	 * <p>Description: 根据ID查询用户的逼格值</p> 
	 * @author :changjiang
	 * date 2014-10-10 下午5:06:00
	 * @param userId
	 * @return
	 */
	public UserBigValue findUserBigValueByUserId(long userId);
	
	/**
	 * 
	 * <p>Title: findUserBeatPercent</p> 
	 * <p>Description: 查询用户打败的百分比</p> 
	 * @author :changjiang
	 * date 2014-10-11 下午1:12:24
	 * @param selfTest
	 * @return
	 */
	public float findUserBeatPercent(float selfTest);
	
	/**
	 * 
	 * <p>Title: insertintoUserAlbum</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-10-17 下午4:11:53
	 * @param userAlbum
	 * @return
	 */
	public UserAlbum insertintoUserAlbum(long userId, List contentList, String type);
	
	/**
	 * 
	 * <p>Title: findUserAlbumById</p> 
	 * <p>Description: 根据ID查询用户相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 上午11:08:02
	 * @param id
	 * @return
	 */
	public UserAlbum findUserAlbumById(long id);
	
	/**
	 * 
	 * <p>Title: deleteUserPicture</p> 
	 * <p>Description: 删除用户的图片</p> 
	 * @author :changjiang
	 * date 2014-10-22 下午2:19:26
	 * @param id
	 * @param contentList
	 * @return
	 */
	public UserAlbum deleteUserPicture(long id, List contentList);
	
	/**
	 * 
	 * <p>Title: findUserAlbumByUid</p> 
	 * <p>Description: 根据uid查询用户个人相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 下午5:14:27
	 * @param userId
	 * @return
	 */
	public List<UserAlbum> findUserAlbumByUid(long userId);
	
	/**
	 * 
	 * <p>Title: findUserInfoByMobilePhone</p> 
	 * <p>Description: 根据电话号码查询用户信息</p> 
	 * @author :changjiang
	 * date 2015-1-29 下午2:36:16
	 * @param productContext
	 * @param mobilePhone
	 * @return
	 */
	public UserInfo findUserInfoByMobilePhone(ProductContext productContext,String mobilePhone);
	
	/**
	 * 
	 * <p>Title: findAttentionUserList</p> 
	 * <p>Description: 查找关注这个人的列表</p> 
	 * @author :changjiang
	 * date 2015-2-28 下午8:34:13
	 * @param productContext
	 * @param userAttentionId
	 * @param count
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<UserAttention> findAttentionUserList(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUserStateByUserId</p> 
	 * <p>Description: 根据用户id查找用户状态码</p> 
	 * @author :changjiang
	 * date 2015-3-20 下午4:57:19
	 * @param userId
	 * @return
	 */
	public String findUserStateByUserId(long userId);
	
	/**
	 * 
	 * <p>Title: findUserAttentionIsExist</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-3-24 下午5:18:50
	 * @param userAttention
	 * @return
	 */
	public UserAttention findUserAttentionIsExist(UserAttention userAttention);
	
	/**
	 * 
	 * <p>Title: editPassword</p> 
	 * <p>Description: 修改密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午5:41:07
	 * @param mobilephone
	 * @param oldPassword
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	public UserInfo editPassword(long uid, String oldPassword,String newPassword,
			String verifyPassword);
	
	/**
	 * 
	 * <p>Title: forgetPassword</p> 
	 * <p>Description: 忘记密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午8:39:54
	 * @param mobilephone
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	public UserInfo forgetPassword(String mobilephone,String newPassword,String verifyPassword);
	
	/**
	 * 
	 * <p>Title: findSearchDuYao</p> 
	 * <p>Description: 搜索毒药的标示</p> 
	 * @author :changjiang
	 * date 2015-5-7 下午2:40:44
	 * @return
	 */
	public int findSearchDuYao();
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
	public boolean saveUserLatestInfo(UserLatest userLatest);

	/**
	 * 绑定手机号
	 * @param userId
	 * @param mobilephone
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	public UserInfo bindingMobile(Long userId, String mobilephone, String newPassword);
}
