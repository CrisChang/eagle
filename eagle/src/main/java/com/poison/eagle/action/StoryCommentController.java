package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.action.web.BaseController;
import com.poison.eagle.manager.ActManager;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.manager.SensitiveManager;
import com.poison.eagle.manager.StoryCommentManager;
import com.poison.eagle.manager.StoryResourceManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.story.model.Story;

/**
 * 小说评论
 * @author songdan
 * @date 2016年3月1日
 * @Description TODO
 * @version V1.0
 */
@Controller
@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE)
public class StoryCommentController extends BaseController{
	
	private static final Log LOG = LogFactory.getLog(StoryCommentController.class);
	
	
	private StoryCommentManager storyCommentManager;
	
	private StoryResourceManager storyResourceManager;
	
	private SensitiveManager sensitiveManager;
	
	private ActManager actManager;
	
	
	
	public void setActManager(ActManager actManager) {
		this.actManager = actManager;
	}




	public void setStoryResourceManager(StoryResourceManager storyResourceManager) {
		this.storyResourceManager = storyResourceManager;
	}




	public void setStoryCommentManager(StoryCommentManager storyCommentManager) {
		this.storyCommentManager = storyCommentManager;
	}

	
	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}


	/**
	 * 写小说评论
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/clientaction/write_story_comment", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeStoryComment(HttpServletRequest request,HttpServletResponse response){
		LOG.info("写入一条小说评论开始。。。。");
		String responseMsg = "";
		String requestMsg = request.getParameter("req");
		responseMsg=storyCommentManager.writeStoryComment(requestMsg,getUserId());
		LOG.info("写入一条小说评论结束。。。。");
		return responseMsg ;
	}
	
	@RequestMapping(value="/clientview/view_story_comment", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewStoryCommentList(HttpServletRequest request,HttpServletResponse response){
		LOG.info("查看小说评论列表开始。。。。");
		String responseMsg = "";
		String requestMsg = request.getParameter("req");
		responseMsg=storyCommentManager.viewStoryCommentList(requestMsg);
		LOG.info("查看小说评论列表结束。。。。");
		LOG.info("小说评论列表为。。。。");
		LOG.info(responseMsg);
		return responseMsg ;
		
	}
	
	/**
	 * 小说评论的评论列表
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/story_comment_list",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String commentList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		String reqs = "";
//		long uid=0;
		//获取用户id
//		if(checkUserIsLogin()){
//		uid = getUserId();
//		}else{
//			uid = 0;
//			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
		//获取客户端json数据
//		try {
		String	reqs = request.getParameter("req");
//		} catch (Exception e) {
//			//e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
//			return RES_DATA_NOTGET;
//		}
		String res = storyResourceManager.commentList(reqs,getUserId());
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		LOG.info("小说评论的评论查询结果：");
		LOG.info(res);
		return res;
	}
	
	/**
	 * 小说评论资源
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/story_comment_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String comment(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		String reqs = "";
//		long uid=0;
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
//		
//		//获取客户端json数据
//		try {
		String	reqs = request.getParameter("req");
//		} catch (Exception e) {
			//e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
//			return RES_DATA_NOTGET;
//		}
		String res = storyResourceManager.comment(reqs,getUserId());
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 删除资源
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/story_del_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delResource(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		String reqs = "";
//		long uid=0;
//		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
//		
//		//获取客户端json数据
//		try {
		LOG.info("删除小说评论的评论开始。。。。。");
		String	reqs = request.getParameter("req");
//		} catch (Exception e) {
//			//e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
//			return RES_DATA_NOTGET;
//		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = storyResourceManager.delResource(reqs,getUserId());
//		System.out.println(res);
		LOG.info(res);
		LOG.info("删除小说评论的评论结束。。。。。");
		return res;
	}
	/**
	 * 
	 * <p>Title: choseUseable</p> 
	 * <p>Description: 选择是否有用</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午3:27:42
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientaction/story_choose_useful", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String choseUseable(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
//		String reqs = "";
//		long uid = 0;
//		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}		
//		//获取客户端json数据
//		try {
		String	reqs = request.getParameter("req");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET);
//			return RES_DATA_NOTGET;
//		}
		
		//调用manager方法获取返回数据
		String res =actManager.chooseUseful(reqs, getUserId());
//		System.out.println(res);
		
		return res;
	}
}
