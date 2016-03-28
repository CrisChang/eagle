package com.poison.eagle.action;

import com.ibatis.common.logging.Log;
import com.poison.eagle.manager.StoryManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.StringUtils;

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
 * Date: 15/11/27
 * Time: 16:05
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class StoryController extends BaseController{

    private static final org.apache.commons.logging.Log LOG = LogFactory
            .getLog(ShareController.class);

    private StoryManager storyManager;

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    @RequestMapping(value = "/clientview/view_story", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String viewStoryDetail(HttpServletRequest request,
                                  HttpServletResponse response) throws UnsupportedEncodingException {


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

        //调用manager方法获取返回数据
        String res =storyManager.viewStoryDetail(reqs,uid);//shareManager.writeShare(reqs,uid);
        LOG.info(res);
        return res;
    }

    /**
     * 查看章节列表
     * @return
     */
    @RequestMapping(value = "/clientview/view_chapterlist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
    @ResponseBody
    public String viewChapterList(HttpServletRequest request,
                                  HttpServletResponse response)throws UnsupportedEncodingException{

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
        //调用manager方法获取返回数据
        String res =storyManager.viewStoryChapterList(reqs,uid);

        return res;
    }

    /**
     * 查询章节详情
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/clientview/view_chapter_content", method = RequestMethod.GET,produces = { "application/octet-stream;charset=utf-8" })
    @ResponseBody
    public String viewChapterContent(HttpServletRequest request,
                                  HttpServletResponse response)throws UnsupportedEncodingException{

        String reqs = "";
        long uid=0;
        //获取用户id
        if(checkUserIsLogin(null)){
            uid = getUserId();
        }else{
        	//下载章节信息的时候传送的用户id
        	String uidStr = request.getParameter("uid");
        	if(StringUtils.isInteger(uidStr)){
        		uid = Long.valueOf(uidStr);
        	}
//            uid = 0;
//            LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//            return RES_USER_NOTLOGIN;
        }

        //获取客户端json数据
        try {
            reqs = request.getParameter("id");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(CommentUtils.ERROR_DATANOTGET);
            return RES_DATA_NOTGET;
        }
        //调用manager方法获取返回数据
        String res =storyManager.viewStoryChapterContent(reqs,uid);

        //System.out.println(res);
        return res;
    }

    @RequestMapping(value = "/clientview/view_myshelf", method = RequestMethod.POST,produces = { "application/octet-stream;charset=utf-8" })
    @ResponseBody
    public String viewMyShelf(HttpServletRequest request,
                                     HttpServletResponse response)throws UnsupportedEncodingException{

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
        //调用manager方法获取返回数据
        String res =storyManager.viewStoryShelf(reqs,uid);
                //viewStoryChapterContent(reqs,uid);

        return res;
    }

    /**
     * 加入书架
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/clientaction/put_myshelf", method = RequestMethod.POST,produces = { "application/octet-stream;charset=utf-8" })
    @ResponseBody
    public String putMyShelf(HttpServletRequest request,
                              HttpServletResponse response)throws UnsupportedEncodingException{

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
        //调用manager方法获取返回数据
        String res =storyManager.putStoryshelf(reqs,uid);
        //viewStoryChapterContent(reqs,uid);

        return res;
    }

    /**
     * 去除书架
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/clientaction/remove_myshelf", method = RequestMethod.POST,produces = { "application/octet-stream;charset=utf-8" })
    @ResponseBody
    public String removeMyShelf(HttpServletRequest request,
                             HttpServletResponse response)throws UnsupportedEncodingException{

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
        //调用manager方法获取返回数据
        String res =storyManager.removeStoryshelf(reqs,uid);
                //putStoryshelf(reqs,uid);
        //viewStoryChapterContent(reqs,uid);

        return res;
    }

    @RequestMapping(value = "/clientview/view_story_ranking_operation", method = RequestMethod.POST,produces = { "application/octet-stream;charset=utf-8" })
    @ResponseBody
    public String viewStoryRanking(HttpServletRequest request,
                              HttpServletResponse response)throws UnsupportedEncodingException{
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
        //调用manager方法获取返回数据
        String res =storyManager.viewStoryRanking(reqs, uid);
        //viewStoryChapterContent(reqs,uid);

        return res;
    }


    @RequestMapping(value = "/clientview/view_story_ranking", method = RequestMethod.POST,produces = { "application/octet-stream;charset=utf-8" })
    @ResponseBody
    public String viewStoryRankingSort(HttpServletRequest request,
                                   HttpServletResponse response)throws UnsupportedEncodingException{
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
        //调用manager方法获取返回数据
        String res =storyManager.viewStoryRanking(reqs, uid);
        //viewStoryChapterContent(reqs,uid);

        return res;
    }

    @RequestMapping(value = "/clientview/view_story_enumerate", method = RequestMethod.POST,produces = { "application/octet-stream;charset=utf-8" })
    @ResponseBody
    public String viewStoryRankingEnumerate(HttpServletRequest request,
                                       HttpServletResponse response)throws UnsupportedEncodingException{

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
        //调用manager方法获取返回数据
        String res =storyManager.viewStoryEnumerate(reqs,uid);
        //viewStoryChapterContent(reqs,uid);

        return res;
    }


}
