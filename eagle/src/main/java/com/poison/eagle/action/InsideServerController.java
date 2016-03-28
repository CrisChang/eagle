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

import com.poison.eagle.manager.InsertServerManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class InsideServerController extends BaseController{

	private static final  Log LOG = LogFactory.getLog(InsideServerController.class);
	
	private InsertServerManager insertServerManager;
	
	@RequestMapping(value="/clientaction/inside_comment",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String comment(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		long begin = System.currentTimeMillis();
		boolean flag = false;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
			if(uid==46){
				flag = true;
			}
		}
		
		if(!flag){
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
		String res = insertServerManager.insertComment(reqs, uid);
		return res;
	}
	
	/**
	 * 
	 * <p>Title: delComment</p> 
	 * <p>Description: 删除评论</p> 
	 * @author :changjiang
	 * date 2015-6-12 上午11:21:04
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientaction/del_comment", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		boolean flag = false;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
			if(uid==46){
				flag = true;
			}
		}
		
		if(!flag){
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
		String res =insertServerManager.delComment(reqs, uid);
				//actManager.delCollect(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	
	/**
	 * 
	 * <p>Title: insidePraise</p> 
	 * <p>Description: 内部点赞</p> 
	 * @author :changjiang
	 * date 2015-6-12 下午12:11:33
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientaction/inside_praise",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String insidePraise(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		long begin = System.currentTimeMillis();
		//获取用户id
		boolean flag = false;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
			if(uid==46){
				flag = true;
			}
		}
		
		if(!flag){
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
		String res = insertServerManager.insidePraise(reqs, uid);
//		System.out.println(res);
		long end = System.currentTimeMillis();
		return res;
	}

	public void setInsertServerManager(InsertServerManager insertServerManager) {
		this.insertServerManager = insertServerManager;
	}
	
	
}
