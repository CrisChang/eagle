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

import com.poison.eagle.manager.MessageNoticManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

/**
 * 
 * <p>Title: MessageNoticController.java</p> 
 * <p>Description: 消息提醒</p> 
 * @author :changjiang
 * date 2015-3-18 下午1:47:05
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class MessageNoticController extends BaseController{

	private static final Log LOG = LogFactory.getLog(MessageNoticController.class);
	
	private MessageNoticManager messageNoticManager;
	
	public void setMessageNoticManager(MessageNoticManager messageNoticManager) {
		this.messageNoticManager = messageNoticManager;
	}

	@RequestMapping(value = "/clientview/notic/view_comment_notic", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String ViewUserCommentNotic(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		
		String reqs = "";
		long uid=0l;
		long begin = System.currentTimeMillis();
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
//				System.out.println(reqs);
				//调用manager方法获取返回数据
				String res = messageNoticManager.ViewUserCommentNotic(uid);
						//resourceManager.comment(reqs,uid);
//				System.out.println(res);
				return res;
	}
	@RequestMapping(value = "/clientview/notic/story_view_comment_notic", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUserCommentNotic(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		
		String reqs = "";
		long uid=0l;
		long begin = System.currentTimeMillis();
		//获取用户id
		if(checkUserIsLogin(request)){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
//				System.out.println(reqs);
		//调用manager方法获取返回数据
		String res = messageNoticManager.ViewUserCommentNotic(uid);
		//resourceManager.comment(reqs,uid);
//				System.out.println(res);
		return res;
	}
}
