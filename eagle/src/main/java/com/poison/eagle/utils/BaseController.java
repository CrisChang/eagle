package com.poison.eagle.utils;

import javax.servlet.http.HttpServletRequest;

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.ucenter.client.UcenterFacade;

public class BaseController {
	
	/**
	 * 用户没有登陆
	 */
	public final String RES_USER_NOTLOGIN = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_USERNOTLOGIN+CommentUtils.RES_ERROR_END;
	public final String RES_DATA_NOTGET = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_DATANOTGET+CommentUtils.RES_ERROR_END;
	public final String RES_TOURIST_NOTLOGIN = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_TOURISTLOGIN+CommentUtils.RES_ERROR_END;
	private UcenterFacade ucenterFacade;

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	/**
	 * 检查用户是否登陆
	 * @return
	 */
	public boolean checkUserIsLogin(HttpServletRequest request){
		boolean flag = false;
		if(ProductContextHolder.getProductContext().isAuthenticated()){//当用户正确
			flag = true;
		}
		return flag;
	}

	public boolean checkUserIsLogin(){
		boolean flag = false;
		if(ProductContextHolder.getProductContext().isAuthenticated()){//当用户正确
			long userId = Long.valueOf(ProductContextHolder.getProductContext().getProductUser().getUserId());
			if(userId==11){//游客
				flag = true;
			}else{
				String state = ucenterFacade.findUserStateByUserId(userId);
				if("0".equals(state)){//用户正常
					flag = true;
				}else{//用户账号被封
					flag =  false;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 获取登陆用户id
	 * @return
	 */
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
}
