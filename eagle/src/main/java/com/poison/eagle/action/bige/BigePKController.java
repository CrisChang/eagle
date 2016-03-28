package com.poison.eagle.action.bige;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.act.model.ActWeixinUser;
import com.poison.eagle.action.web.BaseController;
import com.poison.eagle.entity.WeixinUserInfo;
import com.poison.eagle.manager.ActManager;
import com.poison.eagle.manager.bige.BigeManager;
import com.poison.eagle.manager.bige.BigePKManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;

@Controller
@RequestMapping("/bigepk")
public class BigePKController extends BaseController {

	private static final Log LOG = LogFactory.getLog(BigePKController.class);

	private BigePKManager bigePKManager;

	private ActManager actManager;

	public static String message;

	/**
	 * 需要的表： 问题表 选项分值表 微信用户表(包含答题得分字段) 好友对用户评价表
	 */

	List<Object> sessions = new ArrayList<Object>();

	public void setActManager(ActManager actManager) {
		this.actManager = actManager;
	}

	public void setBigePKManager(BigePKManager bigePKManager) {
		this.bigePKManager = bigePKManager;
	}

	// 如何通过网页获取微信用户的基本信息
	@RequestMapping(value = "/weixinLogin", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String weixinLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String resString = "";
		try {
			// 第一步，用户同意授权，获取code code=CODE&state=STATE
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			String openid = state;// 要查看的用户的信息的openid
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
			if (code != null && code.length() > 0) {
				resString = bigePKManager.weinXinLogin(code, state);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resString;
	}

	// 如何通过网页获取微信用户的基本信息
	@RequestMapping(value = "/weixinLoginUp", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public void weixinLoginBackUp(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 第一步，用户同意授权，获取code code=CODE&state=STATE
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String openid = state;// 要查看的用户的信息的openid
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

		if (code != null && code.length() > 0) {
			// code不为空，说明用户同意授权获取用户信息了
			ActWeixinUser wxuser = bigePKManager.getWeixinUserinfo(code, state);
			if (wxuser != null && wxuser.getOpenId() != null) {
				String domain = basePath;
				if (domain.indexOf(":80/") > 0) {
					domain = domain.replace(":80", "");
				}
				// 放入cookie
				Cookie cookie = new Cookie("uopenid", wxuser.getOpenId());
				cookie.setMaxAge(3600 * 24);
				cookie.setDomain(domain);
				cookie.setPath("/");
				response.addCookie(cookie);
				response.addHeader("Set-Cookie", cookie.toString());

				request.setAttribute("wxuser", wxuser);
				request.setAttribute("uopenid", wxuser.getOpenId());
				// 最后重定向到需要展示的页面(从session中获取需要跳转的路径和参数，如果为空，则跳转到首页)
				if (openid != null && openid.length() > 0 && !"null".equals(openid)) {
					response.sendRedirect(basePath + "bige/result?openid=" + openid + "&uopenid=" + wxuser.getOpenId());
					// request.getRequestDispatcher("/bige/result?openid="+openid).forward(request,
					// response);
				} else {
					// 没有访问别人的页面
					// response.sendRedirect(basePath+"bigepage/index.jsp?uopenid="+wxuser.getOpenId());
					request.setAttribute("key", "uopenid");
					request.setAttribute("value", wxuser.getOpenId());
					request.setAttribute("requesturl", basePath + "bigepage/index.jsp");
					request.getRequestDispatcher("/bigepage/tiao.jsp").forward(request, response);
				}
			} else {
				// 获取微信用户信息失败
				request.setAttribute("message", message);
				request.getRequestDispatcher("/bigepage/error.jsp?message=fail").forward(request, response);
				// response.sendRedirect(basePath+"bigepage/error.jsp?message=fail");
			}
		} else {
			// 用户拒绝获取微信用户信息
			response.sendRedirect(basePath + "bigepage/error.jsp?message=stop");
		}
	}

	// @RequestMapping(value = "/clientaction/view_actopics", method =
	// RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	// @ResponseBody
	// public String viewAcTopics(HttpServletRequest request,
	// HttpServletResponse response) throws UnsupportedEncodingException {
	// String reqs = "";
	// long uid = 0;
	// // 获取用户id
	// if (checkUserIsLogin()) {
	// uid = getUserId();
	// } else {
	// uid = 0;
	// LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
	// return RES_USER_NOTLOGIN;
	// }
	// // 获取客户端json数据
	// try {
	// reqs = request.getParameter("req");
	// } catch (Exception e) {
	// e.printStackTrace();
	// LOG.error(CommentUtils.ERROR_DATANOTGET);
	// return RES_DATA_NOTGET;
	// }
	// // 调用manager方法获取返回数据
	// String res = actManager.viewAcTopics(reqs, uid);
	// return res;
	// }
	//
	// @RequestMapping(value = "/clientaction/view_actopic_detail", method =
	// RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	// @ResponseBody
	// public String viewAcTopicDetail(HttpServletRequest request,
	// HttpServletResponse response) throws UnsupportedEncodingException {
	// String reqs = "";
	// long uid = 0;
	// // 获取用户id
	// if (checkUserIsLogin()) {
	// uid = getUserId();
	// } else {
	// uid = 0;
	// LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
	// return RES_USER_NOTLOGIN;
	// }
	// // 获取客户端json数据
	// try {
	// reqs = request.getParameter("req");
	// } catch (Exception e) {
	// e.printStackTrace();
	// LOG.error(CommentUtils.ERROR_DATANOTGET);
	// return RES_DATA_NOTGET;
	// }
	// // 调用manager方法获取返回数据
	// String res = actManager.viewAcTopicDetail(reqs, uid);
	// return res;
	// }

	// 如何通过网页获取微信用户的基本信息 由微信后台发起访问此链接
	@RequestMapping(value = "/weixin2", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public void weixin2(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 第一步，用户同意授权，获取code code=CODE&state=STATE
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String openid = state;// 要查看的用户的信息的openid
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

		if (code != null && code.length() > 0) {
			// code不为空，说明用户同意授权获取用户信息了
			ActWeixinUser wxuser = bigePKManager.getWeixinUserinfo(code, state);
			if (wxuser != null && wxuser.getOpenId() != null) {
				String domain = basePath;
				if (domain.indexOf(":80/") > 0) {
					domain = domain.replace(":80", "");
				}
				// 放入cookie
				Cookie cookie = new Cookie("uopenid", wxuser.getOpenId());
				cookie.setMaxAge(3600 * 24);
				cookie.setDomain(domain);
				cookie.setPath("/");
				response.addCookie(cookie);
				response.addHeader("Set-Cookie", cookie.toString());

				request.setAttribute("wxuser", wxuser);
				request.setAttribute("uopenid", wxuser.getOpenId());
				// 最后重定向到需要展示的页面(从session中获取需要跳转的路径和参数，如果为空，则跳转到首页)
				if (openid != null && openid.length() > 0 && !"null".equals(openid)) {
					response.sendRedirect(basePath + "bige/result?openid=" + openid + "&uopenid=" + wxuser.getOpenId());
					// request.getRequestDispatcher("/bige/result?openid="+openid).forward(request,
					// response);
				} else {
					// 没有访问别人的页面
					// response.sendRedirect(basePath+"bigepage/index.jsp?uopenid="+wxuser.getOpenId());
					request.setAttribute("key", "uopenid");
					request.setAttribute("value", wxuser.getOpenId());
					request.setAttribute("requesturl", basePath + "bigepage/index.jsp");
					request.getRequestDispatcher("/bigepage/tiao.jsp").forward(request, response);
				}
			} else {
				// 获取微信用户信息失败
				request.setAttribute("message", message);
				request.getRequestDispatcher("/bigepage/error.jsp?message=fail").forward(request, response);
				// response.sendRedirect(basePath+"bigepage/error.jsp?message=fail");
			}
		} else {
			// 用户拒绝获取微信用户信息
			response.sendRedirect(basePath + "bigepage/error.jsp?message=stop");
		}
	}
}