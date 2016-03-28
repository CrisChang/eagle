package com.poison.eagle.action.bige;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.act.model.ActWeixinUser;
import com.poison.eagle.action.web.BaseController;
import com.poison.eagle.manager.bige.BigeManager;

@Controller
@RequestMapping("/bige")
public class BigeController extends BaseController{
	
	private static final Log LOG = LogFactory.getLog(BigeController.class);

	private BigeManager bigeManager;
	
	@RequestMapping(value = "/getquestion", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getquestion(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		System.out.println("bige------getquestion");
		/**
		 {
		    "no": 2,
		    "bannerImage": "images/ad.jpg",
		    "question": "听着黄绮珊的歌，你觉得做点什么最应景儿？",
		    "options": [{
		        "option": "A.学韩红深情对唱",
		        "value": 2
		        }, {
		        "option": "B.照镜子看看自己的牙",
		        "value": 6
		        }, {
		        "option": "C.捂住耳朵",
		        "value": 8
		        }, {
		        "option": "D.戳瞎自己的眼睛",
		        "value": 4
		        }]
		}
		 */
		String reqs = request.getParameter("req");
		String question = bigeManager.getQuestion(reqs);
		System.out.println("======================question:"+question);
		return question;
	}
	
	
	@RequestMapping(value = "/postvalue", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String postvalue(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		System.out.println("bige------postvalue");
		String reqs = request.getParameter("req");
		System.out.println("==================postvalue:reqs:"+reqs);
		String result = bigeManager.postValue(request,reqs);
		System.out.println("======================postvalue:result:"+result);
		return result;
	}
	
	//判断是跳转到刚答完的结果页面还是跳转到用户评价页面
	@RequestMapping(value = "/result", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public void result(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		System.out.println("bige------result");
		String openid = request.getParameter("openid");
		request.setAttribute("openid",openid);
		String uopenid = request.getParameter("uopenid");
		request.setAttribute("uopenid",uopenid);
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		
		if(uopenid!=null && uopenid.length()>0 && !"null".equals(uopenid)){
			
		}else{
			//从cookie取出数据
			Cookie cookies[]=request.getCookies();
			if(cookies!=null && cookies.length>0){
				for(int i=0;i<cookies.length;i++){
					String name = cookies[i].getName();
					String value = cookies[i].getValue();
					System.out.println("cookie==:name:"+cookies[i].getName()+"  value:"+cookies[i].getValue());
					if(name!=null && name.indexOf("uopenid")>-1 && value!=null && value.length()>0 && !"null".equals(value)){
						uopenid = value;
						break;
					}
				}
			}
		}
		
		if(uopenid!=null && uopenid.length()>0 && !"null".equals(uopenid)){
			//需要查询出用户的得分和PK情况	
			if(openid != null && openid.length()>0){
				String result = (String) request.getParameter("result");
				if("result".equals(result)){
					//跳转到end.jsp
					String scorestr = request.getParameter("score");
					int score = Integer.valueOf(scorestr);
					int flag = bigeManager.updateScore(uopenid, score);//更新得分
					double percent = bigeManager.getScorePK(score);
					request.setAttribute("score",score);
					request.setAttribute("pk", percent+"%");//需要从数据库查询总人数和小于当前得分的人数
					request.getRequestDispatcher("/bigepage/end.jsp").forward(request, response);
				}else{
					if(request.getParameter("flag")!=null){
						//跳转到label.jsp						
						request.getRequestDispatcher("/bigepage/label.jsp").forward(request, response);
					}else{
						//为了去掉地址栏上的uopenid传参，需要在跳转到当前链接一次，uopenid通过request获取
						request.setAttribute("key","uopenid");
						request.setAttribute("value",uopenid);
						request.setAttribute("requesturl",basePath+"bige/result?openid="+openid);
						request.getRequestDispatcher("/bigepage/tiao.jsp").forward(request, response);
					}
				}
			}else{
				//没有传openid，重定向到index.jsp
				//response.sendRedirect(basePath+"bigepage/index.jsp?uopenid="+uopenid);
				request.getRequestDispatcher("/bigepage/index.jsp").forward(request, response);
			}
		}else{
			request.setAttribute("method","get");
			request.setAttribute("title","逼格标签");
			if(basePath.indexOf(":80/")>0){
				basePath = basePath.replace(":80", "");
			}
			String redirect_uri = URLEncoder.encode(basePath+"bige/weixin", "utf8");
			String state = openid;
			request.setAttribute("requesturl","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1441086740e20837&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state="+state+"#wechat_redirect");
			request.getRequestDispatcher("/bigepage/tiao.jsp").forward(request, response);
			
			
			/*if(basePath.indexOf(":80/")>0){
				basePath = basePath.replace(":80", "");
			}
			String redirect_uri = URLEncoder.encode(basePath+"bige/weixin", "utf8");
			String state = openid;
			response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1441086740e20837&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state="+state+"#wechat_redirect");*/
		}
	}
	
	
	//查询好友对ta的评价和ta的得分
	@RequestMapping(value = "/getusertags", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getusertags(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		System.out.println("bige------getusertags");
		String openid = request.getParameter("openid");
		String tags = bigeManager.getUserTags(openid);
		System.out.println("======================tags:"+tags);
		return tags;
	}

	//添加好友对一个用户的评价
	@RequestMapping(value = "/saveusertag", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveusertag(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		System.out.println("bige------saveusertag");
		String openid = request.getParameter("openid");
		String uopenid = request.getParameter("uopenid");
		String tag = request.getParameter("tag");
		String result = bigeManager.saveUserTag(openid, uopenid, tag);
		System.out.println("======================result:"+result);
		return result;
	}
	
	public static String message;
	//如何通过网页获取微信用户的基本信息
	@RequestMapping(value = "/weixin", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public void weixin(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		System.out.println("bige------weixin");
		//String reqs = request.getParameter("req");
		//第一步，用户同意授权，获取code
		//code=CODE&state=STATE
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		
		String openid = state;//要查看的用户的信息的openid
		
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		
		if(code!=null && code.length()>0){
			//code不为空，说明用户同意授权获取用户信息了
			ActWeixinUser wxuser = bigeManager.getWeixinUserinfo(code, state);
			if(wxuser!=null && wxuser.getOpenId()!=null){
				String domain = basePath;
				if(domain.indexOf(":80/")>0){
					domain = domain.replace(":80", "");
				}
				//放入cookie
				Cookie cookie = new Cookie("uopenid",wxuser.getOpenId());  
		        cookie.setMaxAge(3600*24);
		        cookie.setDomain(domain);
		        cookie.setPath("/");
		        response.addCookie(cookie);
		        response.addHeader("Set-Cookie", cookie.toString());
		        
				request.setAttribute("wxuser", wxuser);
				request.setAttribute("uopenid",wxuser.getOpenId());
				//最后重定向到需要展示的页面(从session中获取需要跳转的路径和参数，如果为空，则跳转到首页)
				if(openid!=null && openid.length()>0 && !"null".equals(openid)){
					response.sendRedirect(basePath+"bige/result?openid="+openid+"&uopenid="+wxuser.getOpenId());
					//request.getRequestDispatcher("/bige/result?openid="+openid).forward(request, response);
				}else{
					//没有访问别人的页面
					//response.sendRedirect(basePath+"bigepage/index.jsp?uopenid="+wxuser.getOpenId());
					request.setAttribute("key","uopenid");
					request.setAttribute("value",wxuser.getOpenId());
					request.setAttribute("requesturl",basePath+"bigepage/index.jsp");
					request.getRequestDispatcher("/bigepage/tiao.jsp").forward(request, response);
				}
			}else{
				//获取微信用户信息失败
				request.setAttribute("message",message);
				request.getRequestDispatcher("/bigepage/error.jsp?message=fail").forward(request, response);
				//response.sendRedirect(basePath+"bigepage/error.jsp?message=fail");
			}
		}else{
			//用户拒绝获取微信用户信息
			response.sendRedirect(basePath+"bigepage/error.jsp?message=stop");
		}
	}

	/**
	 * zhangqi 百幕大战测试
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	//如何通过网页获取微信用户的基本信息
	@RequestMapping(value = "/weixin2", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public void weixin2(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		System.out.println("bige------weixin");
		//String reqs = request.getParameter("req");
		//第一步，用户同意授权，获取code
		//code=CODE&state=STATE
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		
		String openid = state;//要查看的用户的信息的openid
		
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		
		if(code!=null && code.length()>0){
			//code不为空，说明用户同意授权获取用户信息了
			ActWeixinUser wxuser = bigeManager.getWeixinUserinfo2(code, state);
			if(wxuser!=null && wxuser.getOpenId()!=null){
				String domain = basePath;
				if(domain.indexOf(":80/")>0){
					domain = domain.replace(":80", "");
				}
//				//放入cookie
//				Cookie cookie = new Cookie("uopenid",wxuser.getOpenId());  
//		        cookie.setMaxAge(3600*24);
//		        cookie.setDomain(domain);
//		        cookie.setPath("/");
//		        response.addCookie(cookie);
//		        response.addHeader("Set-Cookie", cookie.toString());
//		        
//				request.setAttribute("wxuser", wxuser);
//				request.setAttribute("uopenid",wxuser.getOpenId());
//				//最后重定向到需要展示的页面(从session中获取需要跳转的路径和参数，如果为空，则跳转到首页)
//				if(openid!=null && openid.length()>0 && !"null".equals(openid)){
//					response.sendRedirect(basePath+"bige/result?openid="+openid+"&uopenid="+wxuser.getOpenId());
//					//request.getRequestDispatcher("/bige/result?openid="+openid).forward(request, response);
//				}else{
//					//没有访问别人的页面
//					//response.sendRedirect(basePath+"bigepage/index.jsp?uopenid="+wxuser.getOpenId());
//					request.setAttribute("key","uopenid");
//					request.setAttribute("value",wxuser.getOpenId());
//					request.setAttribute("requesturl",basePath+"bigepage/index.jsp");
//					request.getRequestDispatcher("/bigepage/tiao.jsp").forward(request, response);
//				}
			}else{
				//获取微信用户信息失败
				request.setAttribute("message",message);
				request.getRequestDispatcher("/bigepage/error.jsp?message=fail").forward(request, response);
				//response.sendRedirect(basePath+"bigepage/error.jsp?message=fail");
			}
		}else{
			//用户拒绝获取微信用户信息
			response.sendRedirect(basePath+"bigepage/error.jsp?message=stop");
		}
	}

	public void setBigeManager(BigeManager bigeManager) {
		this.bigeManager = bigeManager;
	}
	
	
	/**
	 * 需要的表：
	 * 问题表
	 * 选项分值表
	 * 微信用户表(包含答题得分字段)
	 * 好友对用户评价表
	 */
	
	
	List<Object> sessions = new ArrayList<Object>();
	@RequestMapping(value = "/test", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public void test(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String a = (String) request.getSession().getAttribute("a");
		//if(a==null){
			request.getSession().setAttribute("a","fsadgadfsdjfkasdfsaf==============");
		//}
		
		Cookie cookie = new Cookie("JSESSIONID",request.getSession().getId());  
        cookie.setMaxAge(3600);  
        cookie.setDomain("localhost");
        cookie.setPath("/");
        response.addCookie(cookie); 
        response.addHeader("Set-Cookie", cookie.toString());
		
		System.out.println("a:"+a+"    sessionid:"+request.getSession().getId());
		sessions.add(request.getSession());
		for(int i=0;i<sessions.size();i++){
			HttpSession session = (HttpSession) sessions.get(i);
			System.out.println("{a:"+session.getAttribute("a")+"    sessionid:"+session.getId());
		}
	}
}