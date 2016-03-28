package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poison.eagle.manager.SensitiveManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.MessageCenterManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

/**
 * 
 * <p>Title: MessageCenterController.java</p> 
 * <p>Description: 消息中心</p> 
 * @author :changjiang
 * date 2015-3-9 下午6:15:22
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class MessageCenterController extends BaseController{
	
	private static final Log LOG = LogFactory.getLog(MessageCenterController.class);

	private SensitiveManager sensitiveManager;

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}
	
	private MessageCenterManager messageCenterManager;

	public void setMessageCenterManager(MessageCenterManager messageCenterManager) {
		this.messageCenterManager = messageCenterManager;
	}

	/**
	 * 
	 * <p>Title: viewUserCommentMSG</p> 
	 * <p>Description: 访问用户的评论中心列表</p> 
	 * @author :changjiang
	 * date 2015-3-9 下午8:20:44
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/msg/view_comment", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUserCommentMSG(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		try{
			reqs = request.getParameter("req");
			if(null==reqs){
				return RES_DATA_NOTGET;
			}
		}catch (Exception e) {
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}	
		
		String rep = messageCenterManager.viewUserCommentMSG(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			rep = sensitiveManager.checkSensitive(rep);
		}
		//System.out.println(rep);
		return rep;
	}

	@RequestMapping(value = "/clientview/msg/story_view_comment", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewStoryUserCommentMSG(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		/*String reqs = "";
		try{
			reqs = request.getParameter("req");
			if(null==reqs){
				return RES_DATA_NOTGET;
			}
		}catch (Exception e) {
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}*/	
		
		String rep = messageCenterManager.viewStoryUserCommentMSG(request.getParameter("req"));
		//去除敏感字
		if(null!=sensitiveManager){
			rep = sensitiveManager.checkSensitive(rep);
		}
		//System.out.println(rep);
		return rep;
	}
	
	
	
	@RequestMapping(value = "/clientview/msg/view_praise", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUserPraiseMSG(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = request.getParameter("req");
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}	
		
		String rep = messageCenterManager.viewUserPraiseMSG(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			rep = sensitiveManager.checkSensitive(rep);
		}
		//System.out.println(rep);
		return rep;
	}
	
	/**
	 * 
	 * <p>Title: viewUserCommentMSG</p> 
	 * <p>Description: 访问用户的at中心列表</p> 
	 * @author :changjiang
	 * date 2015-3-9 下午8:20:44
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/msg/view_at", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewAtUserMSG(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = request.getParameter("req");
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}	
		
		String rep = messageCenterManager.viewAtUserMSG(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			rep = sensitiveManager.checkSensitive(rep);
		}
		//System.out.println(rep);
		return rep;
	}
	
	
	@RequestMapping(value = "/clientview/msg/view_useful", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUserUsefulMSG(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = request.getParameter("req");
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}	
		
		String rep = messageCenterManager.viewUserUsefulMSG(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			rep = sensitiveManager.checkSensitive(rep);
		}
				//viewUserPraiseMSG(reqs);
		//System.out.println(rep);
		return rep;
	}
	@RequestMapping(value = "/clientview/msg/story_view_useful", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewStoryUserUsefulMSG(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		LOG.info("请求小说用户的赞");
		String rep = messageCenterManager.viewStoryUserUsefulMSG(request.getParameter("req"));
		//去除敏感字
		if(null!=sensitiveManager){
			rep = sensitiveManager.checkSensitive(rep);
		}
		return rep;
	}
}
