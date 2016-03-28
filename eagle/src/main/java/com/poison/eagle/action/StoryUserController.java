package com.poison.eagle.action;

import com.poison.eagle.manager.StoryUserManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/2/26
 * Time: 13:47
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE+"/clientaction")
public class StoryUserController extends BaseController{

    private static final Log LOG = LogFactory.getLog(StoryUserController.class);

    private StoryUserManager storyUserManager;

    public void setStoryUserManager(StoryUserManager storyUserManager) {
        this.storyUserManager = storyUserManager;
    }

    @RequestMapping(value="/story/register",method= RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String regStoryUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String reqs="";
        String res="";
        //获取客户端json数据
        reqs = getParameter(request, "req");

        //判断是否能正常获取数据
        if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
            return RES_DATA_NOTGET;
        }

        res = storyUserManager.regStoryuser(reqs,response);
                //userRegLoginManager.userReg(reqs, response);

        return res;
    }

    /**
     * 小说用户登录
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/story/login",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String loginStory(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String reqs="";
        String res="";
        Long uid = 0l;
        //获取客户端json数据
        reqs = getParameter(request, "req");

        //判断是否能正常获取数据
        if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
            return RES_DATA_NOTGET;
        }

        if(checkUserIsLogin(null)){
            uid = getUserId();
        }

        res = storyUserManager.loginStoryUser(reqs,response,uid);
                //userRegLoginManager.login(reqs,response,uid);
        return res;
    }


    /**
     * 小说用户第三方登录
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/story/login_from_thirdparty",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String loginStoryFromThirdparty(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String reqs="";
        String res="";
        long uid=0;
        //获取客户端json数据
        reqs = getParameter(request, "req");

        if(checkUserIsLogin(null)){
            uid = getUserId();
        }

        //判断是否能正常获取数据
        if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
            return RES_DATA_NOTGET;
        }
        res =storyUserManager.loginStoryFromThirdparty(reqs,response,uid);
                //userRegLoginManager.loginFromWeixin(reqs,response,uid);
        return res;
    }


    /**
     * 更新小说用户密码
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/story/edit_password",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String editStoryPassword(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String reqs = "";
        long uid=0;
        //获取用户id
        if(checkUserIsLogin(null)){
            uid = getUserId();
        }else{
            uid = 0;
            LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
            return RES_USER_NOTLOGIN;
        }

        //获取客户端json数据
        try {
            reqs = request.getParameter("req");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(CommentUtils.ERROR_DATANOTGET);
            return RES_DATA_NOTGET;
        }
//		System.out.println(reqs);

        //调用manager方法获取返回数据
        String res =storyUserManager.editStoryPassword(reqs,uid);
                //userInfoManager.editPassword(reqs, uid);
//		System.out.println(res);

        return res;
    }


    /**
     * 忘记小说密码
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/story/forget_password",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String forgetStoryPassword(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String reqs = "";
        long uid=0;
        //获取用户id
		/*if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}*/

        //获取客户端json数据
        try {
            reqs = request.getParameter("req");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(CommentUtils.ERROR_DATANOTGET);
            return RES_DATA_NOTGET;
        }
//		System.out.println(reqs);

        //调用manager方法获取返回数据
        String res = storyUserManager.forgetStoryPassword(reqs,uid);
//		System.out.println(res);

        return res;
    }

    /**
     * 绑定小说手机号
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/story/binding_mobile",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String bindingStoryMobile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String reqs = "";
        long uid=0;
        //获取用户id
        if(checkUserIsLogin(null)){
            uid = getUserId();
        }else{
            uid = 0;
            LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
            return RES_USER_NOTLOGIN;
        }

        //获取客户端json数据
        try {
            reqs = request.getParameter("req");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(CommentUtils.ERROR_DATANOTGET);
            return RES_DATA_NOTGET;
        }
//		System.out.println(reqs);

        //调用manager方法获取返回数据
        String res =storyUserManager.bindingStoryMobile(reqs,uid);
                //userInfoManager.bindingMobile(reqs, uid);
//		System.out.println(res);

        return res;
    }

    /**
     * 编辑用户
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/story/edit_user",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String editStoryUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        String reqs = "";
        long uid=16;
        //获取用户id
        if(checkUserIsLogin(null)){
            uid = getUserId();
        }else{
            uid = 0;
            LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
            return RES_USER_NOTLOGIN;
        }

        //获取客户端json数据
        try {
            reqs = request.getParameter("req");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(CommentUtils.ERROR_DATANOTGET);
            return RES_DATA_NOTGET;
        }
//		System.out.println(reqs);

        //调用manager方法获取返回数据
        String res =storyUserManager.editStoryUser(uid,reqs);

        return res;
    }
}
