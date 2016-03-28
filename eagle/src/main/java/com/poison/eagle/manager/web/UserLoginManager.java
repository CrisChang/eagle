package com.poison.eagle.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.alibaba.fastjson.util.Base64;
import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.WebConstant;
import com.keel.framework.web.security.UserSecurityBean;
import com.keel.framework.web.security.UserSecurityBeanOnCookie;
import com.keel.framework.web.security.UserSecurityBeanOnHeader;
import com.keel.utils.web.HttpHeaderUtils;
import com.poison.eagle.action.web.UserLoginController;
import com.poison.eagle.easemobmanager.EasemobUserManager;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.AsciiUTils;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HttpRequest;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.Sendsms;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.easemob.model.EasemobUser;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.ucenter.client.ThirdPartyFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.ThirdPartyLogin;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class UserLoginManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(UserLoginManager.class);
	private UcenterFacade ucenterFacade;
	private ThirdPartyFacade thirdPartyFacade;
	private String res;
	private Map datas;
	private String resString;
	//private String type ;// 0:手机注册(登录)、1：用户名密码注册（登录）
	private int flagint = ResultUtils.ERROR;//0：成功、1：失败
	private String flag = CommentUtils.RES_FLAG_ERROR;
	private String error;//错误信息
	private UserSecurityBeanOnCookie userSecurityBeanOnCookie;
//	private UserSecurityBeanOnHeader userSecurityBeanOnHeader;
	
	private int type;
	private String pnum;
	private String passwd;
	private String name;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	
	
	/**
	 * 登录方法
	 * @param reqs
	 * @return
	 */
	public String login(HttpServletRequest request,HttpServletResponse response ){
		type = Integer.valueOf(request.getParameter("type"));
		passwd = request.getParameter("passwd");
		if(type == TRUE){
			pnum = request.getParameter("pnum");
		}else{
			name = request.getParameter("name");
		}
		
		UserAllInfo userAllInfo = null;
		if(type == TRUE){
			userAllInfo = ucenterFacade.checkLoginByMobilePhone(null, pnum, passwd);
		}else{
			userAllInfo = ucenterFacade.checkLoginByLoginName(null, name, passwd);
		}
		flagint = userAllInfo.getFlag();
		UserEntity userEntity = userUtils.putFansPlusToUser(userAllInfo, ucenterFacade);
		
		if(flagint == ResultUtils.SUCCESS){
			request.getSession().setAttribute("userInfo", userEntity);
			this.userSecurityBeanOnCookie.setShortUserSecurityData(response, userAllInfo.getUserId(), ProductContextHolder.getProductContext().getEnv().getClientIP());
			
            int seconds=1*24*60*60;  
            Cookie cookie = new Cookie("user", "");  
            cookie.setMaxAge(0);  
            cookie.setDomain(WebConstant.ROOT_DOMAIN);
            cookie.setPath("/");
            response.addCookie(cookie); 
            String nickname = userEntity.getNickName();
            if("".equals(nickname)){
            	nickname = "NONAME";
            }
            String sign = AsciiUTils.stringToAscii(userEntity.getNickName());
            HttpHeaderUtils.saveCookie(response, "U", sign, -1, WebConstant.ROOT_DOMAIN, false);
		}
		Map<String, Object> datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			String nickname = userEntity.getNickName();
            if("".equals(nickname)){
            	nickname = "NONAME";
            }
            Map<String,Object> resMap= new HashMap<String,Object>();
			resMap.put("nickname", nickname);
			datas.put("map",resMap);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
			datas.put("error", error);
		}
		request.setAttribute("flag", flag);
		datas.put("flag", flag);
		datas.put("userEntity", userEntity);
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 登录方法
	 * @param reqs
	 * @return
	 */
	public String userlogin(HttpServletRequest request,HttpServletResponse response ){
		passwd = request.getParameter("passwd");
		name = request.getParameter("name");
		
		Map<String, Object> datas = new HashMap<String, Object>();
		if(name==null || name.length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "登录名不能为空");
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		if(passwd==null || passwd.length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "密码不能为空");
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		//需要判断是否是手机号码
		UserAllInfo userAllInfo = null;
		if(StringUtils.isMobile(name)){
			pnum = name;
			userAllInfo = ucenterFacade.checkLoginByMobilePhone(null, pnum, passwd);
		}else{
			userAllInfo = ucenterFacade.checkLoginByLoginName(null, name, passwd);
		}
		flagint = userAllInfo.getFlag();
		UserEntity userEntity = userUtils.putFansPlusToUser(userAllInfo, ucenterFacade);
		
		if(flagint == ResultUtils.SUCCESS){
			request.getSession().setAttribute("userInfo", userEntity);
			this.userSecurityBeanOnCookie.setShortUserSecurityData(response, userAllInfo.getUserId(), ProductContextHolder.getProductContext().getEnv().getClientIP());
			
            int seconds=1*24*60*60;  
            Cookie cookie = new Cookie("user", "");  
            cookie.setMaxAge(0);  
            cookie.setDomain(WebConstant.ROOT_DOMAIN);
            cookie.setPath("/");
            response.addCookie(cookie); 
            String nickname = userEntity.getNickName();
            if("".equals(nickname)){
            	nickname = "NONAME";
            }
            String sign = AsciiUTils.stringToAscii(userEntity.getNickName());
            HttpHeaderUtils.saveCookie(response, "U", sign, -1, WebConstant.ROOT_DOMAIN, false);
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			String nickname = userEntity.getNickName();
            if("".equals(nickname)){
            	nickname = "NONAME";
            }
            Map<String,Object> resMap= new HashMap<String,Object>();
			resMap.put("nickname", nickname);
			datas.put("map",resMap);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
			datas.put("error", error);
		}
		request.setAttribute("flag", flag);
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 根据用户id获取用户信息--检查登录使用
	 * @param reqs
	 * @return
	 */
	public String getUserinfo(HttpServletRequest request,HttpServletResponse response,Long uid){
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		flagint = userAllInfo.getFlag();
		UserEntity userEntity = userUtils.putFansPlusToUser(userAllInfo, ucenterFacade);
		Map<String, Object> datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			String nickname = userEntity.getNickName();
			String headimage = userEntity.getFace_url();
            if("".equals(nickname)){
            	nickname = "NONAME";
            }
            Map<String,Object> resMap= new HashMap<String,Object>();
			resMap.put("nickname", nickname);
			resMap.put("headimage", headimage);
			datas.put("map",resMap);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
			datas.put("error", error);
		}
		request.setAttribute("flag", flag);
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 * 请求微信连接获取用户信息
	 */
	public int weixinThirdPartyLogin(HttpServletRequest request,HttpServletResponse response) {
		UserLoginController.message = "";
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		if(code!=null && code.length()>0){
			try{
				//code不为空，说明用户同意授权获取用户信息了
				//第二部，通过code换取网页授权access_token
				String secret = "e6c65b57845846986637f7c341b0eefd";
				String appid="wx1441086740e20837";
				String urladdress1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
				UserLoginController.message = "urladdress1:"+urladdress1;
				String json1 = HttpRequest.getUrl(urladdress1);
				UserLoginController.message = UserLoginController.message+"-json1:"+json1;
				//json1 = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200,\"refresh_token\":\"REFRESH_TOKEN\",\"openid\":\"OPENID\",\"scope\":\"SCOPE\"}";

				
				Map<String, Object> map1 = getObjectMapper().readValue(json1,  new TypeReference<Map<String, Object>>(){});
				
				String access_token = (String) map1.get("access_token");
				String openid = (String) map1.get("openid");
				String refresh_token = (String) map1.get("refresh_token");
				
				//第三步：刷新access_token（如果需要）
				
				//String urladdress2 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+appid+"&grant_type=refresh_token&refresh_token="+refresh_token;
				//String json2 = HttpRequest.getUrl(urladdress2);
				
				//json2 = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200,\"refresh_token\":\"REFRESH_TOKEN\",\"openid\":\"OPENID\",\"scope\":\"SCOPE\"}";

				
				
				/*//第四步：拉取用户信息(需scope为 snsapi_userinfo)
				String urladdress3 = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
				UserLoginController.message = UserLoginController.message+"-urladdress3:"+urladdress3;
				String json3 = HttpRequest.getUrl(urladdress3);
				UserLoginController.message = UserLoginController.message+"-json3:"+json3;
				//json3 = "{\"openid\":\"OPENID\",\"nickname\":\"NICKNAME\",\"sex\":\"1\",\"province\":\"PROVINCE\",\"city\":\"CITY\",\"country\":\"COUNTRY\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46\",\"unionid\": \"o6_bmasdasdsad6_2sgVt7hMZOPfL\"}";
				Map<String, Object> map3 = getObjectMapper().readValue(json3,  new TypeReference<Map<String, Object>>(){});
				String nickname = (String) map3.get("nickname");
				String sex = map3.get("sex")+"";
				String province = (String) map3.get("province");
				String city = (String) map3.get("city");
				String country = (String) map3.get("country");
				String headimgurl = (String) map3.get("headimgurl");
				String unionid = (String) map3.get("unionid");*/
				
				//暂时先不允许第三方默认注册web，如果数据库存在第三方信息才允许登陆web
				
				if(openid!=null && openid.length()>0 && !"null".equals(openid)){
					ThirdPartyLogin wxuser = thirdPartyFacade.findThirdPartyByOpenIdAndLoginResource(openid, "WX");
					if(wxuser!=null && wxuser.getUserId()>0){
						//存在
						UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, wxuser.getUserId());
						flagint = userAllInfo.getFlag();
						UserEntity userEntity = userUtils.putFansPlusToUser(userAllInfo, ucenterFacade);
						
						if(flagint == ResultUtils.SUCCESS){
							this.userSecurityBeanOnCookie.setShortUserSecurityData(response, userAllInfo.getUserId(), ProductContextHolder.getProductContext().getEnv().getClientIP());
							
				            int seconds=1*24*60*60;
				            Cookie cookie = new Cookie("user", "");  
				            cookie.setMaxAge(0);
				            cookie.setDomain(WebConstant.ROOT_DOMAIN);
				            cookie.setPath("/");
				            response.addCookie(cookie);
				            String nickname = userEntity.getNickName();
				            if("".equals(nickname)){
				            	nickname = "NONAME";
				            }
				            String sign = AsciiUTils.stringToAscii(userEntity.getNickName());
				            HttpHeaderUtils.saveCookie(response, "U", sign, -1, WebConstant.ROOT_DOMAIN, false);
						}
						if(flagint == ResultUtils.SUCCESS){
							return 1;
						}else{
							return -1;
						}
					}else{
						//没有注册，需要下载app
						return 0;
					}
				}
				return -1;
			}catch(Exception e){
				e.printStackTrace();
				UserLoginController.message = UserLoginController.message+"-e.getMessage():"+e.getMessage()+"-e.getCause():"+e.getCause()+"-e.getLocalizedMessage():"+e.getLocalizedMessage();
				return -1;
			}
		}else{
			//用户拒绝获取用户信息
			return -1;
		}
	}
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setUserSecurityBeanOnCookie(
			UserSecurityBeanOnCookie userSecurityBeanOnCookie) {
		this.userSecurityBeanOnCookie = userSecurityBeanOnCookie;
	}
	public void setThirdPartyFacade(ThirdPartyFacade thirdPartyFacade) {
		this.thirdPartyFacade = thirdPartyFacade;
	}
	
	
	
	/*private EasemobUserManager easemobUserManager;

	public void setEasemobUserManager(EasemobUserManager easemobUserManager) {
		this.easemobUserManager = easemobUserManager;
	}
	
	//测试环信用的
	public String getHX(HttpServletRequest request,HttpServletResponse response,Long uid){
		String idstr = request.getParameter("id");
		long id = 1;
		if(StringUtils.isInteger(idstr)){
			id = Long.valueOf(idstr);
		}
		if(id<1){
			id = 1;
		}
		EasemobUser easemobUser = easemobUserManager.regIMUserSingle(id);
		System.out.println("userid:"+id);
		System.out.println("flag:"+easemobUser.getFlag());
		System.out.println("description:"+easemobUser.getDescription());
		return "getHX method:"+"[userid:"+id+"]["+"flag:"+easemobUser.getFlag()+"]["+"description:"+easemobUser.getDescription()+"]";
	}*/
}
