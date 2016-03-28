package com.poison.ucenter.dao.impl;

import com.poison.eagle.action.web.LongCommentController;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.StoryUserDAO;
import com.poison.ucenter.model.StoryThirdLogin;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.model.ThirdPartyLogin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 14:58
 */
public class StoryUserDAOImpl extends SqlMapClientDaoSupport implements StoryUserDAO {

    private static final Log LOG = LogFactory.getLog(StoryUserDAOImpl.class);
    /**
     * 加入小说用户
     * @param loginName
     * @param password
     * @param mobilephone
     * @param name
     * @param faceAddress
     * @return
     */
    @Override
    public Map<String,Object> insertStoryUser(String loginName, String password, String mobilephone, String name, String faceAddress,int isBinding) {
        int flag = ResultUtils.ERROR;
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,Object> resultMap = new HashMap<String, Object>();
        long userId = 0;
        try{
            long systime = System.currentTimeMillis();
            map.put("loginName",loginName);
            map.put("password",password);
            map.put("mobilePhone",mobilephone);
            map.put("name",name);
            map.put("faceAddress",faceAddress);
            map.put("createDate",systime);
            map.put("lastestRevisionDate",systime);
            map.put("isBinding",isBinding);
            userId = (Long)getSqlMapClientTemplate().insert("insertStoryUserInfo", map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            e.printStackTrace();
            flag = ResultUtils.ERROR;
        }
        resultMap.put("flag",flag);
        resultMap.put("userId",userId);
        return resultMap;
    }

    /**
     * 根据id查询小说用户信息
     * @param userId
     * @return
     */
    @Override
    public StoryUser findStoryUserByUserid(long userId) {
        StoryUser storyUser = new StoryUser();
        int flag = ResultUtils.ERROR;
        try{
            storyUser = (StoryUser)getSqlMapClientTemplate().queryForObject("findStoryUserByUserid", userId);
            if(null==storyUser){
                storyUser = new StoryUser();
                storyUser.setFlag(ResultUtils.DATAISNULL);
                return storyUser;
            }
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            storyUser = new StoryUser();
            flag = ResultUtils.ERROR;
        }
        storyUser.setFlag(flag);
        return storyUser;
    }

    /**
     * 根据用户名和密码查询小说用户
     * @param loginName
     * @param password
     * @return
     */
    @Override
    public StoryUser findStoryUserByLoginnameAndPassword(String loginName, String password) {
        StoryUser storyUser = new StoryUser();
        int flag = ResultUtils.ERROR;
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            map.put("loginName",loginName);
            map.put("password",password);
            storyUser = (StoryUser)getSqlMapClientTemplate().queryForObject("findStoryUserByLoginnameAndPassword", map);
            if(null==storyUser){
                storyUser = new StoryUser();
                storyUser.setFlag(ResultUtils.DATAISNULL);
                return storyUser;
            }
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            storyUser = new StoryUser();
            flag = ResultUtils.ERROR;
        }
        storyUser.setFlag(flag);
        return storyUser;
    }

    /**
     * 根据电话号码查询
     * @param mobilephone
     * @return
     */
    @Override
    public StoryUser findStoryUserByMobilephone(String mobilephone) {
        StoryUser storyUser = new StoryUser();
        int flag = ResultUtils.ERROR;
        Map<String,Object> map = new HashMap<String, Object>();
        try{
            map.put("mobilephone",mobilephone);
            storyUser = (StoryUser)getSqlMapClientTemplate().queryForObject("findStoryUserByMobilephone", mobilephone);
            if(null==storyUser){
                storyUser = new StoryUser();
                storyUser.setFlag(ResultUtils.DATAISNULL);
                return storyUser;
            }
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            storyUser = new StoryUser();
            flag = ResultUtils.ERROR;
        }
        storyUser.setFlag(flag);
        return storyUser;
    }

    /**
     * 更新小说用户的用户名和密码
     * @param uid
     * @param newPassword
     * @param newPasswordRdm
     * @param sysTime
     * @return
     */
    @Override
    public int editStoryPassword(long uid, String newPassword, String newPasswordRdm, long sysTime) {
        int flag = ResultUtils.ERROR;
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            map.put("uid", uid);
            map.put("newPassword", newPassword);
            map.put("newPasswordRdm", newPasswordRdm);
            map.put("sysTime", sysTime);
            getSqlMapClientTemplate().update("editStoryPassword",map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }

    /**
     * 更新小说的头像和昵称
     * @param uid
     * @param faceurl
     * @param sysTime
     * @return
     */
    @Override
    public int editStoryFaceAndNickName(long uid, String nickname, String faceurl, long sysTime) {
        int flag = ResultUtils.ERROR;
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            map.put("uid", uid);
            map.put("nickname", nickname);
            map.put("faceurl", faceurl);
            map.put("sysTime", sysTime);
            getSqlMapClientTemplate().update("editStoryFaceAndNickName",map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }

    /**
     * 改用户状态为绑定
     * @param uid
     * @return
     */
    @Override
    public int updateStoryUserIsBinding(long uid) {
        int flag = ResultUtils.ERROR;
        long sysTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            map.put("uid", uid);
            map.put("sysTime", sysTime);
            getSqlMapClientTemplate().update("updateStoryUserIsBinding",map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }

    /**
     * 绑定小说的手机号
     * @param uid
     * @param mobilephone
     * @param newPassword
     * @param newPasswordRdm
     * @param sysTime
     * @return
     */
    @Override
    public int bindingStoryMobilephone(long uid, String mobilephone, String newPassword, String newPasswordRdm, long sysTime) {
        int flag = ResultUtils.ERROR;
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            map.put("uid", uid);
            map.put("mobilephone", mobilephone);
            map.put("newPassword", newPassword);
            map.put("newPasswordRdm", newPasswordRdm);
            map.put("sysTime", sysTime);
            getSqlMapClientTemplate().update("bindingStoryMobile",map);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }

    /**
     * 插入小说第三方登录信息
     * @param storyThirdLogin
     * @return
     */
    @Override
    public int insertStoryThirdParty(StoryThirdLogin storyThirdLogin) {
        int flag = ResultUtils.ERROR;
        try{
            getSqlMapClientTemplate().insert("insertintoStoryThirdParty",storyThirdLogin);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }

    /**
     * 查询小说第三方登录信息
     * @param openId
     * @param loginResource
     * @return
     */
    @Override
    public StoryThirdLogin findStoryThirdPartyByOpenIdAndLoginResource(String openId, String loginResource) {
        StoryThirdLogin storyThirdLogin = new StoryThirdLogin();
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            map.put("openId", openId);
            map.put("loginResource", loginResource);
            storyThirdLogin = (StoryThirdLogin) getSqlMapClientTemplate().queryForObject("findStoryThirdPartyByOpenIdAndLoginSource",map);
            if(null==storyThirdLogin){
                storyThirdLogin = new StoryThirdLogin();
                storyThirdLogin.setFlag(ResultUtils.DATAISNULL);
                return storyThirdLogin;
            }
            storyThirdLogin.setFlag(ResultUtils.SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(),e.fillInStackTrace());
            storyThirdLogin = new StoryThirdLogin();
            storyThirdLogin.setFlag(ResultUtils.QUERY_ERROR);
        }
        return storyThirdLogin;
    }

    /**
     * 更新小说第三方登录信息
     * @param storyThirdLogin
     * @return
     */
    @Override
    public int updateStoryThirdParty(StoryThirdLogin storyThirdLogin) {
        int flag = ResultUtils.ERROR;
        try{
            getSqlMapClientTemplate().update("updateStoryThirdParty",storyThirdLogin);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
        }
        return flag;
    }
}
