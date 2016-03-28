package com.poison.eagle.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.JacksonObjectMapperFactoryBean;
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.security.UserSecurityBeanOnCookie;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.manager.UserRegLoginManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
/**
 * 用户编辑信息manager
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE+"/clientaction")
public class UserRegLoginController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(UserRegLoginController.class);
	private UserRegLoginManager userRegLoginManager;
	
//	private String reqs="";
//	private String res="";
	
	/**
	 * 用户注册
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String userReg(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs="";
		String res="";
		//获取客户端json数据
		reqs = getParameter(request, "req");
		
		//判断是否能正常获取数据
		if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
			return RES_DATA_NOTGET;
		}
		
		res = userRegLoginManager.userReg(reqs, response);

		return res;
	}
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs="";
		String res="";
		Long uid = 0l;
		//获取客户端json数据
		reqs = getParameter(request, "req");
		
		//判断是否能正常获取数据
		if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
			return RES_DATA_NOTGET;
		}

		if(checkUserIsLogin()){
			uid = getUserId();
		}

		res = userRegLoginManager.login(reqs,response,uid);
		return res;
	}
	/**
	 * 第三方登陆登陆
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/login_from_thirdparty",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String loginFromWeixin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs="";
		String res="";
		long uid=0;
		//获取客户端json数据
		reqs = getParameter(request, "req");

		if(checkUserIsLogin()){
			uid = getUserId();
		}

		//判断是否能正常获取数据
		if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
			return RES_DATA_NOTGET;
		}
		res = userRegLoginManager.loginFromWeixin(reqs,response,uid);
		return res;
	}
	
	@RequestMapping(value="/login_from_twocode",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String loginFromTwoCode(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs="";
		String res="";
		long uid=16;
		
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		reqs = getParameter(request, "req");
		
		res = userRegLoginManager.loginFormTwoCode(reqs,uid);
		System.out.println("返回值为"+res);
		return res;
	}
	
	@RequestMapping(value="/definity_weblogin",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String definityWebLogin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs="";
		String res="";
		long uid=16;
		
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		reqs = getParameter(request, "req");
		
		res = userRegLoginManager.definityWebLogin(reqs, uid);
				//loginFormTwoCode(reqs,uid);
		System.out.println("返回值为"+res);
		return res;
	}
	
	
	/**
	 * 注册发送验证码
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/code",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String code(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs="";
		String res="";
		//获取客户端json数据
		reqs = getParameter(request, "req");
		
		//判断是否能正常获取数据
		if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
			return RES_DATA_NOTGET;
		}
		res = userRegLoginManager.code(reqs,response);
		return res;
	}
	public void setUserRegLoginManager(UserRegLoginManager userRegLoginManager) {
		this.userRegLoginManager = userRegLoginManager;
	}
	
}
