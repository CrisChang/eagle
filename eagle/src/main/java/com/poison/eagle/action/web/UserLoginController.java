package com.poison.eagle.action.web;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.JacksonObjectMapperFactoryBean;
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.WebConstant;
import com.keel.framework.web.security.UserSecurityBeanOnCookie;
import com.keel.utils.web.HttpHeaderUtils;
import com.poison.eagle.manager.QRCodeManager;
import com.poison.eagle.manager.web.UserLoginManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.qrcode.QRCodeUtils;
/**
 * 用户编辑信息manager
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_WEB+"/webaction")
public class UserLoginController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(UserLoginController.class);
	private UserLoginManager userLoginManager;
	private int flag = ResultUtils.ERROR;

	private String res;
	
	private int type;
	private String pnum;
	private String passwd;
	private String name;
	
	private String path = "/web_eagle/duyaoIndex.jsp";
	
	private QRCodeUtils qrCodeUtils = QRCodeUtils.getInstance();
	private QRCodeManager qrCodeManager;
	
	/**
	 * 手机号登录
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String phoneLogin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		
		
		res = userLoginManager.login(request,response);
		
		try {
			request.getRequestDispatcher(path).forward(request, response);
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 登出
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String logout(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
		
		Cookie cookie = new Cookie("U", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("I", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("S", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("user", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("user", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/w/webaction/");
        response.addCookie(cookie);
		
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * <p>Title: loginByQrcode</p> 
	 * <p>Description: 通过二维码登录</p> 
	 * @author :changjiang
	 * date 2015-5-5 下午4:27:14
	 * @return
	 */
	@RequestMapping(value="/get_twocode",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getTwoCode(HttpServletRequest request, HttpServletResponse response){
		/* OutputStream output = new FileOutputStream(imgPath);
	     qrCodeUtils.encoderQRCode(encoderContent, output);*/
		String callback = (String) request.getParameter("callback");
		
		String reqs = qrCodeManager.getTwoCode();
		
		if(null!=callback){
			reqs = callback+"("+reqs+")";
		}
		
		return reqs;
	}
	
	/**
	 * 
	 * <p>Title: loginByQrcode</p> 
	 * <p>Description: 通过二维码登录</p> 
	 * @author :changjiang
	 * date 2015-5-6 下午4:12:43
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/twocode_login",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String loginByQrcode(HttpServletRequest request, HttpServletResponse response){
		/* OutputStream output = new FileOutputStream(imgPath);
	     qrCodeUtils.encoderQRCode(encoderContent, output);*/
		String callback = (String) request.getParameter("callback");
		String uniqune = (String) request.getParameter("uniqune");
		System.out.println("得到的唯一标示为"+uniqune);
		String res="";
		String req="";
		//获取客户端json数据
		//reqs = getParameter(request, "req");
		
		
		res = qrCodeManager.webLoginByQrcode(req,uniqune,response);
		
		if(null!=callback){
			res = callback+"("+res+")";
		}
		
		return res;
	}
	
	
	public void setQrCodeManager(QRCodeManager qrCodeManager) {
		this.qrCodeManager = qrCodeManager;
	}
	public void setUserLoginManager(UserLoginManager userLoginManager) {
		this.userLoginManager = userLoginManager;
	}
	
	//============================以下是新写的web相关的接口=====================================
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/userlogin",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String userLogin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		res = userLoginManager.userlogin(request,response);
		return res;
	}
	/**
	 * 登出
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/userlogout",produces = { "text/html;charset=utf-8" })
	public String userlogout(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
		
		Cookie cookie = new Cookie("U", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("I", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("S", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("user", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("user", "");  
        cookie.setMaxAge(0);  
        cookie.setDomain(WebConstant.ROOT_DOMAIN);
        cookie.setPath("/w/webaction/");
        response.addCookie(cookie);
        
        String LOGOUT_SUCCESS = "{\"res\":{\"data\":{\"flag\":\"0\"}}}";
        return LOGOUT_SUCCESS;
	}
	
	/**
	 * 检查登录状态
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/getuserinfo",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String checkLogin(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
		/*Cookie cookies[]=request.getCookies();
		if(cookies!=null){
			for(int i=0;i<cookies.length;i++){
				System.out.println("======================"+i+":"+cookies[i].getName()+":"+cookies[i].getValue());
			}
		}else{
			System.out.println("=======================cookies为null");
		}*/
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			String RES_USER_NOTLOGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\""+CommentUtils.ERROR_USERNOTLOGIN+"\",\"code\":1}}}";
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = userLoginManager.getUserinfo(request, response, uid);
		//response.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	
	public static String message = "";
	//微信登陆
	@RequestMapping(value="/weixinlogin",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public void weixinlogin(HttpServletRequest request, HttpServletResponse response) throws IOException{
		setAlloweCrossDomain(response);
		int result = userLoginManager.weixinThirdPartyLogin(request, response);
		if(result == 1){
			//登陆成功，重定向到首页
			response.sendRedirect("");
		}else if(result == -1){
			//未登录成功（可能异常或未获取到用户信息）,跳转到登陆页面
			
		}else if(result == 0){
			//获取微信用户信息成功，但是用户还未注册到毒药用户中，暂时不开放web注册，需要提示下载app
			
		}else{
			//其他情况，跳转到登陆页面
			
		}
	}
	
	//设置允许跨域
	private void setAlloweCrossDomain(HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "http://dev.duyao001.com");
		//response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");	
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
	
	
	
	
	/*@RequestMapping(value = "/gethx",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String gethx(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 1l;
		}
		//调用manager方法获取返回数据
		String result = userLoginManager.getHX(request, response, uid);
		
		return result;
	}*/
	
	/*@RequestMapping(value = "/saveuserlatest",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String gethx(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String result = userLoginManager.saveUserLatest();
		
		return result;
	}*/
}
