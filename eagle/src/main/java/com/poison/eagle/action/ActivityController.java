package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.ActivityManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.StringUtils;
/**
 * 按时间或者评分查询影评大赛的影评列表
 * @author Administrator
 *
 */
@Controller  
public class ActivityController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(ActivityController.class);
	
	private ActivityManager activityManager;
	
	public void setActivityManager(ActivityManager activityManager) {
		this.activityManager = activityManager;
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @param res 跳转到活动首页
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/wap/activity",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String activity(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}
		String uidstr = request.getParameter("uid");
		if(StringUtils.isInteger(uidstr)){
			uid = Long.valueOf(uidstr);
		}
		String res = activityManager.getMvCommentMatchState(request, uid);
		try{
			response.sendRedirect("http://pre.duyao001.com/sy/0.1.14/moviecomp.html?"+res);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webaction/join_mvcomment_match",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String joinMvcommentMatch(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			
		}
		//调用manager方法获取返回数据
		String res = activityManager.joinMvCommentMatch(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/check_mvcomment_match",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String checkMvcommentMatch(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			
		}
		//调用manager方法获取返回数据
		String res = activityManager.checkMvCommentMatch(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_match_mvcomments",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getMatchMvcomments(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getMatchMvComments(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_match_mvcomments_bytime",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getMatchMvcommentsByTime(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		request.setAttribute("type", "1");
		String res = activityManager.getMatchMvComments(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_rec_match_mvcomments",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getRecMatchMvcomments(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getRecMatchMvComments(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_movie_match_mvcomments",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getMovieMatchMvComments(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		String reqs = "";
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = activityManager.getMovieMatchMvComments(reqs, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_match_users",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getMatchUsers(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getMatchUsers(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_rec_match_users",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getRecMatchUsers(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getRecMatchUsers(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_user_match_mvcomments",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getUserMatchMvComments(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getUserMatchMvComments(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webview/get_activity_userinfo",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getUserInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getUserinfo(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_WEB+"/webaction/choose_useful",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String chooseUseful(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			
		}
		//调用manager方法获取返回数据
		String res = activityManager.chooseUseful(request, uid);
		return res;
	}
	
	//设置允许跨域
	private void setAlloweCrossDomain(HttpServletResponse response){
		//response.addHeader("Access-Control-Allow-Origin", "http://m.duyao001.com");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");	
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
	
	//============================以下是app端访问的接口============================================
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientaction/join_mvcomment_match",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String joinAppMvcommentMatch(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			
		}
		//调用manager方法获取返回数据
		String res = activityManager.joinMvCommentMatch(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/check_mvcomment_match",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String checkAppMvcommentMatch(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			
		}
		//调用manager方法获取返回数据
		String res = activityManager.checkMvCommentMatch(request, uid);
		return res;
	}
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/get_match_mvcomments",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getAppMatchMvcomments(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getMatchMvComments(request, uid);
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/get_rec_match_mvcomments",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getAppRecMatchMvcomments(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getRecMatchMvComments(request, uid);
		return res;
	}
	
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientaction/choose_vote",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String chooseVote(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		String reqs = "";
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{

		}

		if(uid==11){//游客不能投票
			return RES_TOURIST_NOTLOGIN;
		}

		//获取客户端json数据
		try {
			reqs = (String)request.getParameter("req");
		} catch (Exception e) {
			reqs = "0";
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			//return RES_DATA_NOTGET;
		}
		//System.out.println("得到的参数为"+reqs);
		//request.getRemoteAddr();
		String ip = getIpAddr(request);
		//System.out.println("得到的Ip地址为"+ip);
		//调用manager方法获取返回数据
		String res = activityManager.chooseVote(reqs,uid,ip);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			res = callback+"("+res+")";
		}
		return res;
	}
	
	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value=CommentUtils.REQUEST_FROM_MOBLIE+"/clientview/get_user_match_mvcomments",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getAppUserMatchMvComments(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		setAlloweCrossDomain(response);
		long uid=0l;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据
		String res = activityManager.getUserMatchMvComments(request, uid);
		return res;
	}

	public String getIpAddr(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (java.net.UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress= inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
			if(ipAddress.indexOf(",")>0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
