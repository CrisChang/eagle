package com.poison.eagle.action.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.CommentUtils;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class BaseController {
	/**
	 * 用户没有登陆
	 */
	public final String RES_USER_NOTLOGIN = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_USERNOTLOGIN+CommentUtils.RES_ERROR_END;
	public final String RES_DATA_NOTGET = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_DATANOTGET+CommentUtils.RES_ERROR_END;
	
	public  final int TRUE = 0;
	public  final int FALSE = 1;
	
	public static final String WEB_SERIALIZE_PATH = "/web_eagle/serialize.jsp";
	public static final String WEB_ERROR_PATH = "/web_eagle/error.jsp";
	public static final String WEB_SUCCESS_PATH = "/web_eagle/success.jsp";
	public static final String WEB_INDEX_PATH = "/web_eagle/duyaoIndex.jsp";
	public static final String WEB_SERIALIZE_LIST_PATH = "/webview/view_serialize_list?currPage=1";
	public static final String WEB_VIEWCHAPTER_PATH = "/web_eagle/viewChapter.jsp";
	public static final String WEB_ARTICLE_LIST = "/webview/view_article_list";
	/**
	 * 检查用户是否登陆
	 * @return
	 */
	public boolean checkUserIsLogin(){
		return ProductContextHolder.getProductContext().isAuthenticated();
	}
	
	public long getUserId(){
		return Long.valueOf(ProductContextHolder.getProductContext().getProductUser().getUserId());
	}
	
	
	
	/**
	 * 获取客户端json数据
	 * @param request
	 * @param reqName
	 * @return
	 */
	public String getParameter(HttpServletRequest request , String reqName){
		
		String result = null;
		try {
			result = request.getParameter(reqName);
		} catch (Exception e) {
			result =  CommentUtils.ERROR_CODE_GETDATAERROR;
		}
		
		if(result == null){
			result =  CommentUtils.ERROR_CODE_GETDATAERROR;
		}
		
		return result;
	}
	
	
	/**
	 * 获取
	 * @param request
	 * @return
	 */
	public Long getUserId(HttpServletRequest request){
		Long uid = null;
		
		HttpSession session = request.getSession(false);
		if(session == null){
			return uid;
		}
		
		UserEntity userInfo =  (UserEntity) session.getAttribute("userInfo");
		
		if(userInfo == null){
			return uid;
		}
		uid = userInfo.getId();
		
		return uid;
	}
}
