package com.poison.ucenter.service.impl;

import com.poison.ucenter.domain.repository.StoryUserDomainRepository;
import com.poison.ucenter.model.StoryThirdLogin;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.service.StoryUserService;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 16:04
 */
public class StoryUserServiceImpl implements StoryUserService {

    private StoryUserDomainRepository storyUserDomainRepository;

    public void setStoryUserDomainRepository(StoryUserDomainRepository storyUserDomainRepository) {
        this.storyUserDomainRepository = storyUserDomainRepository;
    }

    /**
     * 插入毒药小说的用户
     * @param loginName
     * @param password
     * @param mobilephone
     * @param name
     * @param faceAddress
     * @return
     */
    @Override
    public StoryUser insertStoryUser(String loginName, String password, String mobilephone, String name, String faceAddress) {
        return storyUserDomainRepository.insertStoryUser(loginName, password, mobilephone, name, faceAddress);
    }

    /**
     * 查询小说用户信息
     * @param userId
     * @return
     */
    @Override
    public StoryUser findStoryUserByUserid(long userId) {
        return storyUserDomainRepository.findStoryUserByUserid(userId);
    }

    /**
     * 插入第三方登录信息
     * @param storyThirdLogin
     * @param uid
     * @return
     */
    @Override
    public StoryThirdLogin insertStoryThirdParty(StoryThirdLogin storyThirdLogin, long uid) {
        return storyUserDomainRepository.insertStoryThirdParty(storyThirdLogin, uid);
    }

    /**
     * 根据用户名和密码查询小说用户
     * @param loginName
     * @param password
     * @return
     */
    @Override
    public StoryUser findStoryUserByLoginnameAndPassword(String loginName, String password) {
        return storyUserDomainRepository.findStoryUserByLoginnameAndPassword(loginName, password);
    }

    /**
     * 更新小说的用户名和密码
     * @param uid
     * @param newPassword
     * @param newPasswordRdm
     * @return
     */
    @Override
    public StoryUser editStoryPassword(long uid, String newPassword, String newPasswordRdm) {
        return storyUserDomainRepository.editStoryPassword(uid, newPassword, newPasswordRdm);
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
        return storyUserDomainRepository.forgetStoryPassword(mobilephone, newPassword, verifyPassword);
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
        return storyUserDomainRepository.bindingStoryMobile(userId, mobilephone, newPassword);
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
        return storyUserDomainRepository.editStoryFaceAndNickName(uid, nickname, faceurl);
    }
}
