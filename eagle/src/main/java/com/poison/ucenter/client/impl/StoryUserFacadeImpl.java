package com.poison.ucenter.client.impl;

import com.poison.ucenter.client.StoryUserFacade;
import com.poison.ucenter.model.StoryThirdLogin;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.service.StoryUserService;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 16:23
 */
public class StoryUserFacadeImpl implements StoryUserFacade{

    private StoryUserService storyUserService;

    public void setStoryUserService(StoryUserService storyUserService) {
        this.storyUserService = storyUserService;
    }

    /**
     * 注册用户
     * @param loginName
     * @param password
     * @param mobilephone
     * @param name
     * @param faceAddress
     * @return
     */
    @Override
    public StoryUser insertStoryUser(String loginName, String password, String mobilephone, String name, String faceAddress) {
        return storyUserService.insertStoryUser(loginName, password, mobilephone, name, faceAddress);
    }

    /**
     * 查询小说用户信息
     * @param userId
     * @return
     */
    @Override
    public StoryUser findStoryUserByUserid(long userId) {
        return storyUserService.findStoryUserByUserid(userId);
    }

    /**
     * 插入小说的第三方登录信息
     * @param uid
     * @return
     */
    @Override
    public StoryThirdLogin insertStoryThirdParty(String openid,String nickname,int gender,String location,String country,String headimgurl,
                                                 String description,String other,String loginSource,String type,String pushToken,String phoneModel,long uid) {

        StoryThirdLogin thirdPartyLogin = new StoryThirdLogin();
        long sysdate = System.currentTimeMillis();
        thirdPartyLogin.setOpenId(openid);
        thirdPartyLogin.setNickName(nickname);
        thirdPartyLogin.setGender(gender);
        thirdPartyLogin.setLocation(location);
        thirdPartyLogin.setCountry(country);
        thirdPartyLogin.setHeadImgUrl(headimgurl);
        thirdPartyLogin.setDescription(description);
        thirdPartyLogin.setOther(other);
        thirdPartyLogin.setLoginSource(loginSource);
        thirdPartyLogin.setType(type);
        thirdPartyLogin.setCreateDate(sysdate);
        thirdPartyLogin.setPushToken(pushToken);
        thirdPartyLogin.setPhoneModel(phoneModel);
        return storyUserService.insertStoryThirdParty(thirdPartyLogin, uid);
    }

    /**
     * 根据用户名和密码查询小说用户
     * @param loginName
     * @param password
     * @return
     */
    @Override
    public StoryUser findStoryUserByLoginnameAndPassword(String loginName, String password) {
        return storyUserService.findStoryUserByLoginnameAndPassword(loginName, password);
    }

    /**
     * 更新小说用户的密码
     * @param uid
     * @param newPassword
     * @param newPasswordRdm
     * @return
     */
    @Override
    public StoryUser editStoryPassword(long uid, String newPassword, String newPasswordRdm) {
        return storyUserService.editStoryPassword(uid, newPassword, newPasswordRdm);
    }

    /**
     * 忘记小说密码
     * @param mobilephone
     * @param newPassword
     * @param verifyPassword
     * @return
     */
    @Override
    public StoryUser forgetStoryPassword(String mobilephone, String newPassword, String verifyPassword) {
        return storyUserService.forgetStoryPassword(mobilephone, newPassword, verifyPassword);
    }

    /**
     * 绑定小说的手机号
     * @param userId
     * @param mobilephone
     * @param newPassword
     * @return
     */
    @Override
    public StoryUser bindingStoryMobile(Long userId, String mobilephone, String newPassword) {
        return storyUserService.bindingStoryMobile(userId, mobilephone, newPassword);
    }

    /**
     * 更新小说的昵称和头像
     * @param uid
     * @param nickname
     * @param faceurl
     * @return
     */
    @Override
    public StoryUser editStoryFaceAndNickName(long uid, String nickname, String faceurl) {
        return storyUserService.editStoryFaceAndNickName(uid, nickname, faceurl);
    }
}
