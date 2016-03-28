package com.poison.eagle.utils.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.eagle.utils.CommentUtils;
/**
 * 登录拦截器
 * @author songdan
 * @date 2016年3月1日
 * @Description 
 * @version V1.0
 */
public class LoginInterceptor implements HandlerInterceptor {

	private static final Log LOG = LogFactory.getLog(LoginInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long uid;
		if (checkUserIsLogin()) {
			uid = getUserId();
			return true;
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_USERNOTLOGIN+CommentUtils.RES_ERROR_END);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
	/**
	 * 检查用户是否登陆
	 * @return
	 */
	private boolean checkUserIsLogin(){
		return ProductContextHolder.getProductContext().isAuthenticated();
	}
	/**
	 * 获取当前线程上用户的id
	 * @return
	 */
	private long getUserId(){
		return Long.valueOf(ProductContextHolder.getProductContext().getProductUser().getUserId());
	}
}
