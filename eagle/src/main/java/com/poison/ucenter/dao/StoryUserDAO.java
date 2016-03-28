package com.poison.ucenter.dao;

import com.poison.ucenter.model.StoryThirdLogin;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.model.ThirdPartyLogin;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 14:52
 */
public interface StoryUserDAO {

    /**
     * 增加一个小说用户
     * @param loginName
     * @param password
     * @param mobilephone
     * @param name
     * @param faceAddress
     * @return
     */
    public Map<String,Object> insertStoryUser(String loginName,String password,String mobilephone,String name,String faceAddress,int isBinding);

    /**
     * 根据用户id查询小说用户
     * @param userId
     * @return
     */
    public StoryUser findStoryUserByUserid(long userId);

    /**
     * 根据用户名和密码查询小说用户
     * @param loginName
     * @param password
     * @return
     */
    public StoryUser findStoryUserByLoginnameAndPassword(String loginName,String password);

    /**
     *
     * @param mobilephone
     * @return
     */
    public StoryUser findStoryUserByMobilephone(String mobilephone);


    /**
     * 更新小说用户的用户名和密码
     * @param uid
     * @param newPassword
     * @param newPasswordRdm
     * @param sysTime
     * @return
     */
    public int editStoryPassword(long uid, String newPassword,String newPasswordRdm,long sysTime);
   // public int editStoryUserFaceurl(long userId,String faceUrl);

    /**
     * 更新小说用户的头像和昵称
     * @param uid
     * @param sysTime
     * @return
     */
    public int editStoryFaceAndNickName(long uid, String nickname,String faceurl,long sysTime);

    /**
     * 改用户状态为绑定
     * @param uid
     * @return
     */
    public int updateStoryUserIsBinding(long uid);

    /**
     * 绑定小说的手机号
     * @param uid
     * @param mobilephone
     * @param newPassword
     * @param newPasswordRdm
     * @param sysTime
     * @return
     */
    public int bindingStoryMobilephone(long uid,String mobilephone,String newPassword,String newPasswordRdm,long sysTime);

    /**
     * 插入
     * @param storyThirdLogin
     * @return
     */
    public int insertStoryThirdParty(StoryThirdLogin storyThirdLogin);

    /**
     * 根据openid还有resource来查询第三方登录用户
     * @param openId
     * @param loginResource
     * @return
     */
    public StoryThirdLogin findStoryThirdPartyByOpenIdAndLoginResource(String openId,String loginResource);

    /**
     * 更新小说第三方信息
     * @return
     */
    public int updateStoryThirdParty(StoryThirdLogin storyThirdLogin);

}
