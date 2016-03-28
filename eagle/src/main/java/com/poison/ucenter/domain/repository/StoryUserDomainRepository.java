package com.poison.ucenter.domain.repository;

import com.poison.eagle.utils.MD5Utils;
import com.poison.eagle.utils.RandUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.StoryUserDAO;
import com.poison.ucenter.model.StoryThirdLogin;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.model.ThirdPartyLogin;
import com.poison.ucenter.model.UserInfo;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 15:29
 */
public class StoryUserDomainRepository {

    private StoryUserDAO storyUserDAO;

    public void setStoryUserDAO(StoryUserDAO storyUserDAO) {
        this.storyUserDAO = storyUserDAO;
    }

    /**
     * 插入小说用户
     * @param loginName
     * @param password
     * @param mobilephone
     * @param name
     * @param faceAddress
     * @return
     */
    public StoryUser insertStoryUser(String loginName, String password, String mobilephone, String name, String faceAddress){
        Map<String,Object> map =storyUserDAO.insertStoryUser(loginName, password, mobilephone, name, faceAddress,0);
        int flag = (Integer)map.get("flag");
        StoryUser storyUser = new StoryUser();
        storyUser.setFlag(flag);
        if(flag== ResultUtils.SUCCESS){//当正确插入时
            long userId = (Long)map.get("userId");
            storyUser = storyUserDAO.findStoryUserByUserid(userId);
        }
        return storyUser;
    }

    /**
     * 查找毒药用户的信息
     * @param userId
     * @return
     */
    public StoryUser findStoryUserByUserid(long userId){
        return storyUserDAO.findStoryUserByUserid(userId);
    }


    /**
     * 插入小说用户的第三方登录
     * @param storyThirdLogin
     * @param uid
     * @return
     */
    public StoryThirdLogin insertStoryThirdParty(StoryThirdLogin storyThirdLogin,long uid){
        String openid = storyThirdLogin.getOpenId();
        String loginSource = storyThirdLogin.getLoginSource();
        int flag = ResultUtils.ERROR;
        long sysdate = System.currentTimeMillis();
        //查找是否存在
        StoryThirdLogin storyThirdLogin1= storyUserDAO.findStoryThirdPartyByOpenIdAndLoginResource(openid, loginSource);
        flag = storyThirdLogin1.getFlag();
        long userid = 0;
        String name = "游客"+ RandUtils.getRandomString(5);//随机生成的毒药名字
        if(ResultUtils.DATAISNULL==flag){//当用户不存在时插入用户
            if(0==uid){
                Map<String,Object> map = storyUserDAO.insertStoryUser(storyThirdLogin.getNickName(), "duyao001", name, storyThirdLogin.getNickName(), storyThirdLogin.getHeadImgUrl(),1);
                userid = (Long)map.get("userId");
            }else{
                userid = uid;
            }
            storyThirdLogin.setUserId(userid);
            flag = storyUserDAO.insertStoryThirdParty(storyThirdLogin);
            storyThirdLogin1= storyUserDAO.findStoryThirdPartyByOpenIdAndLoginResource(openid, loginSource);
        }else if(ResultUtils.SUCCESS==flag){//当存在第三方用户的时候
            StoryUser storyUser = storyUserDAO.findStoryUserByUserid(storyThirdLogin1.getUserId());
            if(storyUser.getIsBinding()==0){//当不为绑定的时候
                storyUserDAO.updateStoryUserIsBinding(storyUser.getUserId());
            }
            if(0==uid){

            }else{//当包含着uid自动登录时,去更新用户头像

            }
        }

        return storyThirdLogin1;
    }

    /**
     * 根据用户名和密码查询小说用户
     * @param loginName
     * @param password
     * @return
     */
    public StoryUser findStoryUserByLoginnameAndPassword(String loginName, String password){
        return storyUserDAO.findStoryUserByLoginnameAndPassword(loginName, password);
    }

    /**
     * 更新小说用户的用户名和密码
     * @param uid
     * @param newPassword
     * @param newPasswordRdm
     * @return
     */
    public StoryUser editStoryPassword(long uid, String newPassword, String newPasswordRdm){
        int flag = ResultUtils.ERROR;
        StoryUser storyUser = storyUserDAO.findStoryUserByUserid(uid);
        flag = storyUser.getFlag();
        if(ResultUtils.SUCCESS==flag){//当存在这个用户时
            long systime = System.currentTimeMillis();
            flag = storyUserDAO.editStoryPassword(uid,newPassword,newPasswordRdm,systime);
            storyUser.setFlag(flag);
        }
        return storyUser;
    }

    /**
     * 更新小说用户的头像和昵称
     * @param uid
     * @param nickname
     * @param faceurl
     * @return
     */
    public StoryUser editStoryFaceAndNickName(long uid,String nickname,String faceurl){
        int flag = ResultUtils.ERROR;
        StoryUser storyUser = storyUserDAO.findStoryUserByUserid(uid);
        flag = storyUser.getFlag();
        if(ResultUtils.SUCCESS==flag){//当存在这个用户时
            long systime = System.currentTimeMillis();
            flag = storyUserDAO.editStoryFaceAndNickName(uid,nickname,faceurl,systime);
            storyUser = storyUserDAO.findStoryUserByUserid(uid);
        }
        return storyUser;
    }

    /**
     * 忘记密码
     * @param mobilephone
     * @param newPassword
     * @param verifyPassword
     * @return
     */
    public StoryUser forgetStoryPassword(String mobilephone,String newPassword,String verifyPassword){
        int flag = ResultUtils.ERROR;
        //查询用户的信息
        StoryUser storyUser = storyUserDAO.findStoryUserByMobilephone(mobilephone);
        int resultflag = storyUser.getFlag();
        if(ResultUtils.SUCCESS==resultflag){
            long sysTime = System.currentTimeMillis();
            //String newPasswordMD5 = MD5Utils.md5(newPassword);//新密码加密
            //String newPasswordRdm = MD5Utils.md5AndRandom(newPassword);//新密码随机加密

            flag = storyUserDAO.editStoryPassword(storyUser.getUserId(),newPassword,newPassword,sysTime);
        }else{
            flag = ResultUtils.NO_EXISTED_USER;
        }
        storyUser.setFlag(flag);
        return storyUser;
    }

    /**
     * 绑定小说的手机号
     * @param userId
     * @param mobilephone
     * @param newPassword
     * @return
     */
    public StoryUser bindingStoryMobile(Long userId, String mobilephone, String newPassword){
        StoryUser storyUser = storyUserDAO.findStoryUserByUserid(userId);
        int resultflag = storyUser.getFlag();
        if(ResultUtils.SUCCESS==resultflag){
            long sysTime = System.currentTimeMillis();
//            String newPasswordMD5 = MD5Utils.md5(newPassword);//新密码加密
//            String newPasswordRdm = MD5Utils.md5AndRandom(newPassword);//新密码随机加密
            storyUser = storyUserDAO.findStoryUserByMobilephone(mobilephone);
            if(storyUser.getFlag()==ResultUtils.DATAISNULL){//当手机号不存在时
                resultflag = storyUserDAO.bindingStoryMobilephone(userId,mobilephone,newPassword,newPassword,sysTime);
                //userInfo = userInfoDAO.findUserInfo(null,userId);

            }else if(storyUser.getFlag()==ResultUtils.SUCCESS){//当用户存在时
                resultflag = ResultUtils.EXISTED_PHONE;
            }
            storyUser.setFlag(resultflag);
        }else{
            resultflag = ResultUtils.NO_EXISTED_USER;
            storyUser.setFlag(resultflag);
        }
        return storyUser;
    }
}
