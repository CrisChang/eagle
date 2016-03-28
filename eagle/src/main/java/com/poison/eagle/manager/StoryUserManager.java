package com.poison.eagle.manager;

import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.security.UserSecurityBeanOnHeader;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.*;
import com.poison.product.client.AccGoldFacade;
import com.poison.product.model.AccGold;
import com.poison.resource.model.UserTag;
import com.poison.ucenter.client.StoryUserFacade;
import com.poison.ucenter.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/25
 * Time: 16:27
 */
public class StoryUserManager extends BaseManager{

    private static final Log LOG = LogFactory.getLog(StoryUserManager.class);
    public final String RES_USER_NOTLOGIN = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_USERNOTLOGIN+CommentUtils.RES_ERROR_END;
    private StoryUserFacade storyUserFacade;
    private UserSecurityBeanOnHeader userSecurityBeanOnHeader;
    private AccGoldFacade accGoldFacade;

    public void setAccGoldFacade(AccGoldFacade accGoldFacade) {
        this.accGoldFacade = accGoldFacade;
    }

    public void setUserSecurityBeanOnHeader(UserSecurityBeanOnHeader userSecurityBeanOnHeader) {
        this.userSecurityBeanOnHeader = userSecurityBeanOnHeader;
    }

    public void setStoryUserFacade(StoryUserFacade storyUserFacade) {
        this.storyUserFacade = storyUserFacade;
    }

    /**
     * 注册小说用户
     * @param reqs
     * @param response
     * @return
     */
    public String regStoryuser(String reqs,HttpServletResponse response){

        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";//错误信息
        String type ="";// 0:手机注册(登录)、1：用户名密码注册（登录）
        String name="";
        String passwd="";
        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            type = (String) dataq.get("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        StoryUser storyUser = new StoryUser();
        if(CommentUtils.REQ_ISON_STORY.equals(type)){//小说入口

            //毒药小说的注册方法
            name = "游客"+ RandUtils.getRandomString(5);//随机生成的毒药名字
            passwd = "duyao001";
            storyUser = storyUserFacade.insertStoryUser(name,passwd,name,name,"");
        }

        int f = storyUser.getFlag();
        datas = new HashMap<String, Object>();
        if(f == ResultUtils.SUCCESS){
            UserEntity userEntity = putStoryUserToUserEntity(storyUser);
            userSecurityBeanOnHeader.setShortUserSecurityData(response, userEntity.getId(), ProductContextHolder.getProductContext().getEnv().getClientIP());
            //setProdectUser(ui.getUserId(), response);
            flag = CommentUtils.RES_FLAG_SUCCESS;
            datas.put("userEntity", userEntity);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(f);
            LOG.error("错误代号:"+f+",错误信息:"+error);
            datas.put("error", error);
//			System.out.println(error);
        }
        datas.put("flag", flag);


        //处理返回数据
        resString = getResponseData(datas);
//		System.out.println(resString);
        return resString;
    }

    /**
     * 小说用户登录
     * @param reqs
     * @param response
     * @param uid
     * @return
     */
    public String loginStoryUser(String reqs,HttpServletResponse response,Long uid){
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";//错误信息
        String pnum="";//手机号码

        String type ="";// 0:手机注册(登录)、1：用户名密码注册（登录）
        String name="";
        String passwd="";
        String pushToken="";
        String userId = "";

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            type = (String) dataq.get("type");
            userId = (String) dataq.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            return CommentUtils.ERROR_CODE_GETDATAERROR;
        }
        //拼接数据
        datas = new HashMap<String, Object>();
        StoryUser storyUser = new StoryUser();
        if(CommentUtils.REQ_ISON_STORY.equals(type)){
            //当小说登录的时候 根据id查询
            if(null!=userId&&!"".equals(userId)){
                uid = Long.valueOf(userId);
            }

            if(null==uid||0==uid){//当没有id时
                return RES_USER_NOTLOGIN;
            }
            storyUser = storyUserFacade.findStoryUserByUserid(uid);
                    //ucenterFacade.findUserInfo(null, uid);
        }else{
            //用户自主注册的方法
            name = (String) dataq.get("pnum");
            passwd = (String) dataq.get("passwd");
            storyUser = storyUserFacade.findStoryUserByLoginnameAndPassword(name,passwd);
            //ui = ucenterFacade.checkLoginByLoginName(null, name, passwd);
        }

        int f = storyUser.getFlag();

        if(f == ResultUtils.SUCCESS){
            flag = CommentUtils.RES_FLAG_SUCCESS;
            UserEntity userEntity = putStoryUserToUserEntity(storyUser);
            //添加金币余额信息
            try{
                AccGold accGold = accGoldFacade.findAccGoldByUserId(userEntity.getId());
                if(accGold!=null && accGold.getUserId()>0){
                    userEntity.setGoldAmount(accGold.getGoldamount()+"");
                }
            }catch(Exception e){
                LOG.error("登录时查询金币余额出错:"+e.getMessage(), e.fillInStackTrace());
            }

            userSecurityBeanOnHeader.setShortUserSecurityData(response, userEntity.getId(), ProductContextHolder.getProductContext().getEnv().getClientIP());
            //setProdectUser(userEntity.getId(), response);
            datas.put("userEntity", userEntity);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(f);
            LOG.error("错误代号:"+f+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);

        //处理返回数据
        resString = getResponseData(datas);
//		System.out.println(resString);
        return resString;
    }


    /**
     * 小说用户第三方登录
     * @param reqs
     * @param response
     * @param uid
     * @return
     */
    public String loginStoryFromThirdparty(String reqs,HttpServletResponse response,long uid){

        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";//错误信息

        String openid = "";
        String nickname = "";
        int sex = 0;
        String location = "";
        String summary = "";
        String country = "";
        String headimgurl = "";
        String pushToken = "";
        String phoneModel = "";
        String loginSource = "";

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            openid = (String) dataq.get("openid");//用户标示id
            nickname = (String) dataq.get("nickname");//昵称
            try {
                sex = Integer.valueOf(dataq.get("sex").toString());//性别
            } catch (Exception e) {
                sex = 0;
            }
            location = (String) dataq.get("location");//地址
            summary = (String) dataq.get("summary");//描述
            country = (String) dataq.get("country");//国家
            headimgurl = (String) dataq.get("headimgurl");//头像地址
            loginSource = (String) dataq.get("loginSource");//用户来源 WX微信，WB微博
            pushToken = (String) dataq.get("pushToken");//手机识别号
            phoneModel = (String) dataq.get("phoneModel");//手机型号

        } catch (Exception e) {
            e.printStackTrace();
            return CommentUtils.ERROR_CODE_GETDATAERROR;
        }
        UserEntity userEntity = new UserEntity();
        StoryThirdLogin storyThirdLogin = storyUserFacade.insertStoryThirdParty(openid, nickname, sex, location, country, headimgurl, summary, "", loginSource, "", pushToken, phoneModel, uid);

        int f = storyThirdLogin.getFlag();

        StoryUser storyUser = new StoryUser();
        if(f ==  ResultUtils.SUCCESS){
            storyUser = storyUserFacade.findStoryUserByUserid(storyThirdLogin.getUserId());
            f=storyUser.getFlag();
        }

        //拼接数据
        datas = new HashMap<String, Object>();
        if(f == ResultUtils.SUCCESS){
            userEntity = putStoryUserToUserEntity(storyUser);
            userSecurityBeanOnHeader.setShortUserSecurityData(response, userEntity.getId(), ProductContextHolder.getProductContext().getEnv().getClientIP());
            flag = CommentUtils.RES_FLAG_SUCCESS;
            datas.put("userEntity", userEntity);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(f);
            LOG.error("错误代号:"+f+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);

        //处理返回数据
        resString = getResponseData(datas);
        return resString;
    }

    /**
     * 更新小说用户密码
     * @param reqs
     * @param uid
     * @return
     */
    public String editStoryPassword(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =new HashMap<String, Object>();
        Map<String, Object> dataq=new HashMap<String, Object>();
        Map<String, Object> datas =new HashMap<String, Object>();
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";
        String resString="";//返回数据
        String mobilePhone="";//电话号码
        String oldPassword="";//之前的密码
        String newPassword="";//新的密码
        String verifyPassword="";//确认的密码
        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");
            //mobilePhone = dataq.get("mobilePhone").toString();
            oldPassword = dataq.get("oldPassword").toString();
            newPassword = dataq.get("newPassword").toString();
            verifyPassword = dataq.get("verifyPassword").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
//		userEntities = getUserEntityList(userAllInfos, userEntities);
        //查询用户的信息
        StoryUser storyUser = storyUserFacade.editStoryPassword(uid,newPassword,newPassword);
       // UserInfo userInfo = ucenterFacade.editPassword(uid, oldPassword, newPassword, verifyPassword);
        int resultflag = storyUser.getFlag();
        datas = new HashMap<String, Object>();

        if(ResultUtils.SUCCESS==resultflag||resultflag==UNID){
            flag = CommentUtils.RES_FLAG_SUCCESS;
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            datas.put("error", MessageUtils.getResultMessage(resultflag));
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 忘记小说用户密码
     * @param reqs
     * @param uid
     * @return
     */
    public String forgetStoryPassword(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =new HashMap<String, Object>();
        Map<String, Object> dataq=new HashMap<String, Object>();
        Map<String, Object> datas =new HashMap<String, Object>();
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";
        String resString="";//返回数据
        String mobilePhone="";//电话号码
        String newPassword="";//新的密码
        String verifyPassword="";//确认的密码
        Long userId = 0l;//用户id
        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");
            mobilePhone = dataq.get("mobilePhone").toString();
            newPassword = dataq.get("newPassword").toString();
            verifyPassword = dataq.get("verifyPassword").toString();
            userId = Long.valueOf(CheckParams.objectToStr(dataq.get("userId")));
        } catch (Exception e) {
            e.printStackTrace();
        }
//		userEntities = getUserEntityList(userAllInfos, userEntities);
	/*	System.out.println();*/
        //查询用户的信息
        StoryUser storyUser = storyUserFacade.forgetStoryPassword(mobilePhone,newPassword,verifyPassword);
                //.forgetPassword(mobilePhone, newPassword, verifyPassword);

        int resultflag = storyUser.getFlag();
        datas = new HashMap<String, Object>();

        if(ResultUtils.SUCCESS==resultflag||resultflag==UNID){
            flag = CommentUtils.RES_FLAG_SUCCESS;
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            datas.put("error", MessageUtils.getResultMessage(resultflag));
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 绑定小说手机号
     * @param reqs
     * @param uid
     * @return
     */
    public String bindingStoryMobile(String reqs,Long uid){
        Map<String, Object> req =new HashMap<String, Object>();
        Map<String, Object> dataq=new HashMap<String, Object>();
        Map<String, Object> datas =new HashMap<String, Object>();
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";
        String resString="";//返回数据
        String mobilePhone="";//电话号码
        String newPassword="";//新的密码
        String verifyPassword="";//确认的密码
        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");
            mobilePhone = dataq.get("mobilePhone").toString();
            newPassword = dataq.get("newPassword").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
//		userEntities = getUserEntityList(userAllInfos, userEntities);
	/*	System.out.println();*/
        //查询用户的信息
        StoryUser storyUser = storyUserFacade.bindingStoryMobile(uid,mobilePhone,newPassword);

        int resultflag = storyUser.getFlag();
        datas = new HashMap<String, Object>();

        if(ResultUtils.SUCCESS==resultflag||resultflag==UNID){
            flag = CommentUtils.RES_FLAG_SUCCESS;
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            datas.put("error", MessageUtils.getResultMessage(resultflag));
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 更新小说的用户信息
     * @param uid
     * @param reqs
     * @return
     */
    public String editStoryUser(long uid,String reqs){
//		LOG.info("客户端json数据："+reqs);
        int flagint = 0;
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";
        String resString="";//返回数据

        String face_url = "";// 头像地址
        String sex = "0";// 性别
        String sign = "";// 个性签名
        String interest = "";// 用户兴趣
        String introduction = "";// 个人说明
        String uname = "";
        String affective="";//个人感情状态
        String residence="";//居住地
        String profession="";//职业
        String birthday = "" ;
        String age = "";//年龄
        String constellation = "";//星座
        long birthdayDate = 0;
        List<String> userTagsInfos = new ArrayList<String>();
        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            uname = (String) dataq.get("uname");
            face_url = (String) dataq.get("face_url");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e.fillInStackTrace());
            flagint = ResultUtils.ERROR;
        }

        //修改用户信息方法
        StoryUser storyUser = storyUserFacade.editStoryFaceAndNickName(uid,uname,face_url);
        flagint = storyUser.getFlag();
        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            flag = CommentUtils.RES_FLAG_SUCCESS;
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 转换小说
     * @param storyUser
     * @return
     */
    public UserEntity putStoryUserToUserEntity(StoryUser storyUser){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(storyUser.getUserId());
        userEntity.setFace_url(storyUser.getFaceAddress());
        userEntity.setNickName(storyUser.getName());
        //返回是否绑定
        userEntity.setIsBinding(storyUser.getIsBinding());
        return userEntity;
    }
}
