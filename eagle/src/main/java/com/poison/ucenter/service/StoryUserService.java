package com.poison.ucenter.service;

import com.poison.ucenter.model.StoryThirdLogin;
import com.poison.ucenter.model.StoryUser;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 15:59
 */
public interface StoryUserService {

    /**
     * 插入
     * @param loginName
     * @param password
     * @param mobilephone
     * @param name
     * @param faceAddress
     * @return
     */
    public StoryUser insertStoryUser(String loginName, String password, String mobilephone, String name, String faceAddress);

    /**
     * 查询小说用户信息
     * @param userId
     * @return
     */
    public StoryUser findStoryUserByUserid(long userId);

    /**
     * 插入第三方登录的信息
     * @param storyThirdLogin
     * @param uid
     * @return
     */
    public StoryThirdLogin insertStoryThirdParty(StoryThirdLogin storyThirdLogin,long uid);

    /**
     * 根据用户名和密码查询小说用户
     * @param loginName
     * @param password
     * @return
     */
    public StoryUser findStoryUserByLoginnameAndPassword(String loginName, String password);

    /**
     * 更新小说的用户名和密码
     * @param uid
     * @param newPassword
     * @param newPasswordRdm
     * @return
     */
    public StoryUser editStoryPassword(long uid, String newPassword, String newPasswordRdm);

    /**
     * 忘记小说密码
     * @param mobilephone
     * @param newPassword
     * @param verifyPassword
     * @return
     */
    public StoryUser forgetStoryPassword(String mobilephone,String newPassword,String verifyPassword);

    /**
     * 绑定小说的手机号
     * @param userId
     * @param mobilephone
     * @param newPassword
     * @return
     */
    public StoryUser bindingStoryMobile(Long userId, String mobilephone, String newPassword);

    /**
     * 更新小说的头像和昵称
     * @param uid
     * @param nickname
     * @param faceurl
     * @return
     */
    public StoryUser editStoryFaceAndNickName(long uid,String nickname,String faceurl);
}
