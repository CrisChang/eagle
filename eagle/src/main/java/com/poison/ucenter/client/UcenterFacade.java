package com.poison.ucenter.client;

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

public interface UcenterFacade {

	/**
	 * 
	 * <p>Title: findUserInfo</p> 
	 * <p>Description: 查询用户的所有信息</p> 
	 * @author changjiagn
	 * date 2014-7-18 下午3:51:20
	 * @return
	 */
	public UserAllInfo findUserInfo(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: updateUserInfo</p> 
	 * <p>Description: 更新用户的基本信息</p> 
	 * @author changjiang
	 * date 2014-7-18 下午4:49:42
	 * @param userInfo
	 */
	public UserAllInfo updateUserInfo(ProductContext productContext,UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: insertintoUserInfo</p> 
	 * <p>Description: 添加用户基本信息</p> 
	 * @author changjiagn
	 * date 2014-7-18 下午8:33:40
	 * @param userInfo
	 */
	public UserAllInfo insertintoUserInfo(ProductContext productContext,UserInfo userInfo);
	
	/**
	 * 
	 * <p>Title: findUserAttention</p> 
	 * <p>Description: 添加用户关注人列表</p> 
	 * @author changjiang
	 * date 2014-7-19 下午2:19:49
	 * @param userId
	 * @return
	 */
	public List<UserInfo> findUserAttention(ProductContext productContext,long userId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUserFens</p> 
	 * <p>Description: 查找用户粉丝</p> 
	 * @author changjiang
	 * date 2014-7-19 下午3:40:03
	 * @param userAttentionId
	 * @return
	 */
	public List<UserInfo> findUserFens(ProductContext productContext,long userAttentionId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: insertUserSummary</p> 
	 * <p>Description: 插入用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午1:04:11
	 * @param productContext
	 * @param userSummary
	 * @return
	 */
	public int insertUserSummary(ProductContext productContext,UserSummary userSummary);
	
	/**
	 * 
	 * <p>Title: updateUserSummary</p> 
	 * <p>Description: 更新用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午2:18:57
	 * @param productContext
	 * @param userSummary
	 * @return
	 */
	public int updateUserSummary(ProductContext productContext,UserSummary userSummary);
	
	/**
	 * 
	 * <p>Title: findUserSummaryByUserId</p> 
	 * <p>Description: 查询用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午2:42:41
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public UserSummary findUserSummaryByUserId(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: registUserByMobilePhone</p> 
	 * <p>Description: 根据手机号注册</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午5:43:52
	 * @return
	 */
	public UserInfo registUserByMobilePhone(ProductContext productContext,String mobilePhone,String passWord,String pushToken);
	
	/**
	 * 
	 * <p>Title: registUserByLoginName</p> 
	 * <p>Description: 根据用户名注册</p> 
	 * @author :changjiang
	 * date 2014-7-22 下午5:44:22
	 * @return
	 */
	public UserInfo registUserByLoginName(ProductContext productContext,String loginName,String password,String pushToken);
	
	/**
	 * 
	 * <p>Title: checkLoginByMobilePhone</p> 
	 * <p>Description: 手机号码登录</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午3:08:22
	 * @param productContext
	 * @param mobilePhone
	 * @param password
	 * @return
	 */
	public UserAllInfo checkLoginByMobilePhone(ProductContext productContext,String mobilePhone,String password);
	
	/**
	 * 
	 * <p>Title: checkLoginByLoginName</p> 
	 * <p>Description: 用户名密码登录</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午3:22:40
	 * @return
	 */
	public UserAllInfo checkLoginByLoginName(ProductContext productContext,String loginName,String password);
	
	/**
	 * 
	 * <p>Title: doAttention</p> 
	 * <p>Description: 用户点击关注</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午5:06:08
	 * @return
	 */
	public UserAttention doAttention(ProductContext productContext,long userId,long userAttentionId,String type);
	
	/**
	 * 
	 * <p>Title: doAttentionZone</p> 
	 * <p>Description: 关注圈子</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午4:39:31
	 * @param userId
	 * @param userAttentionId
	 * @param type
	 * @return
	 */
	//public UserAttention doAttentionZone(long userId,long userAttentionId,String type);
	
	/**
	 * 
	 * <p>Title: cancelAttention</p> 
	 * <p>Description: 用户取消关注</p> 
	 * @author :changjiang
	 * date 2014-7-23 下午6:08:38
	 * @param productContext
	 * @param userId
	 * @param userAttentionId
	 * @return
	 */
	public UserAttention cancelAttention(ProductContext productContext,long userId,long userAttentionId);
	
	/**
	 * 
	 * <p>Title: findRegistListByMobileList</p> 
	 * <p>Description: 查询已注册的列表</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午2:55:27
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findRegistListByMobileList(ProductContext productContext, List<String> mobilePhoneList);
	
	/**
	 * 
	 * <p>Title: findUnRegistListByMobileList</p> 
	 * <p>Description: 查询未注册的列表</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午5:45:50
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findUnRegistListByMobileList(ProductContext productContext, List<String> mobilePhoneList);
	
	/**
	 * 
	 * <p>Title: findIsRegistListByMobile</p> 
	 * <p>Description: 查找通讯录中已经关注的</p> 
	 * @author :changjiang
	 * date 2014-7-24 下午9:16:22
	 * @param productContext
	 * @param mobilePhoneList
	 * @return
	 */
	public List<UserInfo> findAttentionByMobile(ProductContext productContext, List<String> mobilePhoneList,long userId,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findUnAttentionByMobile</p> 
	 * <p>Description: 查询通讯录中未关注的</p> 
	 * @author :changjiang
	 * date 2014-7-25 上午11:50:13
	 * @return
	 */
	public List<UserInfo> findUnAttentionByMobile(ProductContext productContext, List<String> mobilePhoneList,long userId,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionList</p> 
	 * <p>Description: 查询朋友关注的列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午4:47:39
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public List<UserInfo> findFriendsUnAttentionList(ProductContext productContext, long userId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findFriendsIsAttentionList</p> 
	 * <p>Description: 查询朋友关注的我已经关注的人列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 下午7:54:50
	 * @return
	 */
	public List<UserInfo> findFriendsIsAttentionList(ProductContext productContext, long userId,int count,int pageIndex,int pageSize);
	
	/**
	 * 
	 * <p>Title: findFriendsAttentionEachList</p> 
	 * <p>Description: 查询朋友关注的相互关注 列表</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午2:32:31
	 * @return
	 */
	public List<UserInfo> findFriendsAttentionEachList(ProductContext productContext, long userId,int count,int pageIndex,int pageSize);
	/**
	 * 
	 * <p>Title: findUserAttentionCount</p> 
	 * <p>Description: 查询用户关注人的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:45:07
	 * @return
	 */
	public int findUserAttentionCount(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: findUserFensCount</p> 
	 * <p>Description: 查询用户粉丝总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午3:46:03
	 * @return
	 */
	public int findUserFensCount(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: findUserAttentionEachList</p> 
	 * <p>Description: 查询用户相互关注列表</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午12:43:17
	 * @return
	 */
	public List<UserInfo> findUserAttentionEachList(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: editUserInfo</p> 
	 * <p>Description: 编辑用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-31 上午10:47:42
	 * @return
	 */
	public UserAllInfo editUserInfo(long userId,String faceAddress,String name,String sex,String sign,String interest,String introduction,String affectiveStates,String residence,String profession,long birthday,String constellation,String age);
	
	/**
	 * 
	 * <p>Title: findUserAttentionList</p> 
	 * <p>Description: 查询用户关注列表</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午10:40:24
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
	 * date 2014-8-3 下午2:07:44
	 * @return
	 */
	public UserInfo findUserInfoByName(String name);
	
	/**
	 * 
	 * <p>Title: findUserInfoByNameOrMobilePhone</p> 
	 * <p>Description: 根据昵称和手机号查询用户</p> 
	 * @author :changjiang
	 * date 2015-1-29 下午12:10:22
	 * @param serachInfo
	 * @return
	 */
	public UserInfo findUserInfoByNameOrMobilePhone(String serachInfo);
	
	public VersionInfo findLatestVersion();
	
	/**
	 * 
	 * <p>Title: findSearchDuYao</p> 
	 * <p>Description: 是否搜索毒药的标示</p> 
	 * @author :changjiang
	 * date 2015-5-7 下午2:30:24
	 * @return
	 */
	public int findSearchDuYao();
	
	/**
	 * 
	 * <p>Title: userNameIsExist</p> 
	 * <p>Description: 查询用户昵称是否已经存在</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午5:00:06
	 * @param userId
	 * @param name
	 * @return
	 */
	public UserInfo userNameIsExist(long userId,String name);
	
	/**
	 * 
	 * <p>Title: updateTwoDimensionCode</p> 
	 * <p>Description: 插入用户二维码</p> 
	 * @author :changjiang
	 * date 2014-8-31 下午2:49:51
	 * @param userInfo
	 * @return
	 */
	public int addTwoDimensionCode(long userId,String twoDimensionCode);
	
	/**
	 * 
	 * <p>Title: findTalentZoneInfo</p> 
	 * <p>Description: 查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午10:50:38
	 * @param id
	 * @return
	 */
	public TalentZone findTalentZoneInfo(long id);
	
	/**
	 * 
	 * <p>Title: findTalentZoneLinkList</p> 
	 * <p>Description: 查询达人圈达人信息列表</p> 
	 * @author :changjiang
	 * date 2014-9-12 上午12:42:20
	 * @param talentZoneId
	 * @return
	 */
	public List<TalentZoneLink> findTalentZoneLinkList(long talentZoneId);
	
	/**
	 * 
	 * <p>Title: findTalentZoneInfoByType</p> 
	 * <p>Description: 根据type查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午12:08:20
	 * @param type
	 * @return
	 */
	public TalentZone findTalentZoneInfoByType(String type);
	
	/**
	 * 
	 * <p>Title: findUserAttentionTalentedPersons</p> 
	 * <p>Description: 查询关注的达人</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午3:03:21
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<UserAttention> findUserAttentionTalentedPersons(long userId,
			String type);
	
	/**
	 * 
	 * <p>Title: findAllTalentZone</p> 
	 * <p>Description: 查询所有的达人圈</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午9:51:14
	 * @return
	 */
	public List<TalentZone> findAllTalentZone();
	
	/**
	 * 
	 * <p>Title: insertintoUserBigValue</p> 
	 * <p>Description: 插入用户逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午6:12:55
	 * @param userBigValue
	 * @return
	 */
	public UserBigValue updateUserBigValue(long userId,float bigValue,int bigLevel);
	
	/**
	 * 
	 * <p>Title: updateUserSelfTest</p> 
	 * <p>Description: 更新用户自测题的分数</p> 
	 * @author :changjiang
	 * date 2014-10-9 上午11:40:36
	 * @param userId
	 * @param bigValue
	 * @return
	 */
	public UserBigValue updateUserSelfTest(long userId,float bigValue,int bigLevel);
	
	/**
	 * 
	 * <p>Title: findUserBigValueByUserId</p> 
	 * <p>Description: 根据userid查询用户的逼格值</p> 
	 * @author :changjiang
	 * date 2014-10-10 下午5:07:13
	 * @param userId
	 * @return
	 */
	public UserBigValue findUserBigValueByUserId(long userId);
	
	/**
	 * 
	 * <p>Title: findUserBeatPercent</p> 
	 * <p>Description: 查询打败的百分比</p> 
	 * @author :changjiang
	 * date 2014-10-11 下午1:13:38
	 * @param selfTest
	 * @return
	 */
	public int findUserBeatPercent(float selfTest);
	
	/**
	 * 
	 * <p>Title: insertintoUserAlbum</p> 
	 * <p>Description: 插入用户的相册</p> 
	 * @author :changjiang
	 * date 2014-10-17 下午4:14:46
	 * @param userAlbum
	 * @return
	 */
	public UserAlbum insertintoUserAlbum(long userId, List contentList, String type);
	
	/**
	 * 
	 * <p>Title: findUserAlbumById</p> 
	 * <p>Description: 根据ID查询相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 上午11:10:28
	 * @param id
	 * @return
	 */
	public UserAlbum findUserAlbumById(long id);
	
	/**
	 * 
	 * <p>Title: deleteUserAlbum</p> 
	 * <p>Description: 删除用户的相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 上午11:53:22
	 * @param id
	 * @param contentList
	 * @return
	 */
	public UserAlbum deleteUserPicture(long id,List contentList);
	
	/**
	 * 
	 * <p>Title: findUserAlbumByUid</p> 
	 * <p>Description: 更具uid查询用户的个人相册</p> 
	 * @author :changjiang
	 * date 2014-10-22 下午5:19:08
	 * @param userId
	 * @return
	 */
	public List<UserAlbum> findUserAlbumByUid(long userId);
	
	/**
	 * 
	 * <p>Title: findAttentionUserList</p> 
	 * <p>Description: 查找关注这个人的列表</p> 
	 * @author :changjiang
	 * date 2015-2-28 下午8:35:29
	 * @param productContext
	 * @param userAttentionId
	 * @param count
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<UserAttention> findAttentionUserList(
			ProductContext productContext, long userAttentionId, int count,
			int pageIndex, int pageSize);
	
	/**
	 * 
	 * <p>Title: findAttentionStatus</p> 
	 * <p>Description: 查找关注人的状态</p> 
	 * @author :changjiang
	 * date 2015-3-7 下午2:46:38
	 * @param user
	 * @param userIdList
	 * @return
	 */
	//public List<String> findAttentionStatus(long user,List<Long> userIdList);
	
	/**
	 * 
	 * <p>Title: findUserStateByUserId</p> 
	 * <p>Description: 根据用户id查询用户的状态码</p> 
	 * @author :changjiang
	 * date 2015-3-20 下午4:58:35
	 * @param userId
	 * @return
	 */
	public String findUserStateByUserId(long userId);
	
	/**
	 * 
	 * <p>Title: findUserAttentionIsExist</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-3-24 下午5:26:27
	 * @param userId
	 * @param attentionUserId
	 * @return
	 */
	public UserAttention findUserAttentionIsExist(long userId,long attentionUserId);
	
	/**
	 * 
	 * <p>Title: editPassword</p> 
	 * <p>Description: 修改密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午5:42:08
	 * @param mobilephone
	 * @param oldPassword
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	public UserInfo editPassword(long uid, String oldPassword,
			String newPassword, String verifyPassword);
	
	/**
	 * 
	 * <p>Title: forgetPassword</p> 
	 * <p>Description: 忘记密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午8:25:27
	 * @param mobilephone
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	public UserInfo forgetPassword(String mobilephone,String newPassword, String verifyPassword);
	
	/**
	 * 
	 * <p>Title: findUserInfoByUserId</p> 
	 * <p>Description: 查询一个用户信息</p> 
	 * @author :changjiang
	 * date 2015-4-13 下午6:31:28
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public UserInfo findUserInfoByUserId(ProductContext productContext,long userId);
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
	public boolean saveUserLatestInfo(long userid,long resourceid,String type);

	/**
	 * 绑定手机号
	 * @param userId
	 * @param mobilephone
	 * @param newPassword
	 * @return
	 */
	public UserInfo bindingMobile(Long userId,String mobilephone,String newPassword);
	
}
