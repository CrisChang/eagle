package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poison.eagle.manager.*;
import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.easemobmanager.EasemobUserManager;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.ext.constant.MemcacheResourceLinkConstant;
/**
 * 用户编辑信息manager
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class ResourceController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(ResourceController.class);
//	private String reqs = "";
//	private long uid=0;
	private ResourceManager resourceManager;
	private JedisManager jedisManager;
	private EasemobUserManager easemobUserManager;
	private UpdateJedisManager updateJedisManager;
	
	private MemcachedClient operationMemcachedClient;
	private SensitiveManager sensitiveManager;

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}

	/**
	 * 展示首页
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/userindex",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String userInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16;
		/*long begin = System.currentTimeMillis();
		LOG.info("调用首页接口开始");*/
//		System.out.println("调用首页接口开始");
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}

		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
		}
//		System.out.println(reqs);
		if(null==reqs){
			return RES_DATA_NOTGET;
		}
		//调用manager方法获取返回数据
		String res = resourceManager.userIndex(reqs,uid);
//		System.out.println(res);
		/*long end = System.currentTimeMillis();
		System.out.println("====================调用首页接口结束，耗时："+(end - begin)+"====================");
		LOG.info("====================调用首页接口结束，耗时："+(end - begin)+"====================");*/
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res.replace("\r\n", " ");
	}
//	/**
//	 * 运营首页
//	 * @param request
//	 * @param response
//	 * @param res 客户端传过来的json数据
//	 * @return
//	 */
//	@RequestMapping(value="/clientview/index",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
//	@ResponseBody
//	public String index(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		String reqs = "";
//		long uid=16;
//		long begin = System.currentTimeMillis();
//		LOG.info("调用首页接口开始");
////		System.out.println("调用首页接口开始");
//		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
//		
//		//获取客户端json数据
//		try {
//			reqs = request.getParameter("req");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET);
//			return RES_DATA_NOTGET;
//		}
////		System.out.println(reqs);
//		
//		//调用manager方法获取返回数据
//		String res = resourceManager.index(reqs,uid);
////		System.out.println(res);
//		long end = System.currentTimeMillis();
////		System.out.println("调用首页接口结束，耗时："+(end - begin));
//		LOG.info("调用首页接口结束，耗时："+(end - begin));
//		
//		return res;
//	}
	/**
	 * 猜你喜欢
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/guess_you_like",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String guessYouLike(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16;
		/*long begin = System.currentTimeMillis();
		LOG.info("调用首页接口开始");*/
//		System.out.println("调用首页接口开始");
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.guessYouLike(reqs,uid);
//		System.out.println(res);
		//long end = System.currentTimeMillis();
//		System.out.println("调用首页接口结束，耗时："+(end - begin));
		//LOG.info("调用首页接口结束，耗时："+(end - begin));
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 朋友圈
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/circle_of_friends",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String circleOfFriends(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16;
		/*long begin = System.currentTimeMillis();
		LOG.info("调用首页接口开始");*/
//		System.out.println("调用首页接口开始");
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
		}
		if(null==reqs){
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.circleOfFriends(reqs,uid);
//		System.out.println(res);
	/*	long end = System.currentTimeMillis();
		System.out.println("===================调用朋友圈接口结束，耗时："+(end - begin)+"========================");
		LOG.info("===================调用朋友圈接口结束，耗时："+(end - begin)+"========================");*/
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res.replace("\r\n", "");
	}
	/**
	 * 首页展示连载
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
//	@RequestMapping(value="/clientview/index_serialize",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
//	@ResponseBody
//	public String indexSerialize(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		String reqs = "";
//		long uid=0;
//		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//	LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
//		//获取客户端json数据
//		try {
//			reqs = request.getParameter("req");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET);
//			return RES_DATA_NOTGET;
//		}
////		System.out.println(reqs);
//		
//		//调用manager方法获取返回数据
//		String res = resourceManager.indexSerialize(reqs,uid);
////		System.out.println(res);
//		
//		return res;
//	}
	/**
	 * 首页展示书相关
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
//	@RequestMapping(value="/clientview/index_book",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
//	@ResponseBody
//	public String indexBook(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		String reqs = "";
//		long uid=0;
//		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//	LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
//		//获取客户端json数据
//		try {
//			reqs = request.getParameter("req");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET);
//			return RES_DATA_NOTGET;
//		}
////		System.out.println(reqs);
//		
//		//调用manager方法获取返回数据
//		String res = resourceManager.indexBook(reqs,uid);
////		System.out.println(res);
//		
//		return res;
//	}
	/**
	 * 首页展示电影相关
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
//	@RequestMapping(value="/clientview/index_movie",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
//	@ResponseBody
//	public String indexMovie(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		String reqs = "";
//		long uid=0;
//		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//	LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
//		//获取客户端json数据
//		try {
//			reqs = request.getParameter("req");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET);
//			return RES_DATA_NOTGET;
//		}
////		System.out.println(reqs);
//		
//		//调用manager方法获取返回数据
//		String res = resourceManager.indexMovie(reqs,uid);
////		System.out.println(res);
//		
//		return res;
//	}
	/**
	 * 我的页面@模块
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_actuser_list",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String actUserList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.actUserList(reqs,uid);
//		System.out.println(res);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
//	/**
//	 * 首页清单
//	 * @param request
//	 * @param response
//	 * @param res 客户端传过来的json数据
//	 * @return
//	 */
//	@RequestMapping(value="/clientview/detailed_list",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
//	@ResponseBody
//	public String detailedList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		
//		
//		//获取客户端json数据
//		try {
//			reqs = request.getParameter("req");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET);
//			return RES_DATA_NOTGET;
//		}
//		System.out.println(reqs);
//		
//		//调用manager方法获取返回数据
//		String res = resourceManager.detailedList(reqs);
//		System.out.println(res);
//		
//		return res;
//	}
//	/**
//	 * 首页帖子
//	 * @param request
//	 * @param response
//	 * @param res 客户端传过来的json数据
//	 * @return
//	 */
//	@RequestMapping(value="/clientview/index_post_list",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
//	@ResponseBody
//	public String indexPostList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		
//		
//		//获取客户端json数据
//		try {
//			reqs = request.getParameter("req");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(CommentUtils.ERROR_DATANOTGET);
//			return RES_DATA_NOTGET;
//		}
//		System.out.println(reqs);
//		
//		//调用manager方法获取返回数据
//		String res = resourceManager.indexPostList(reqs);
//		System.out.println(res);
//		
//		return res;
//	}
	/**
	 * 单条内容
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/one_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneResource(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.oneResource(reqs,uid);
//		System.out.println(res);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 删除资源
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/clientaction/del_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delResource(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.delResource(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 朋友动态
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/friend_dynamic",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String  friendDynamic(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
		}
		if(null==reqs){
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.friendDynamic(reqs,uid);
//		System.out.println(res);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 评论列表
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/comment_list",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String commentList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.commentList(reqs,uid);
//		System.out.println(res);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 赞、取消赞
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/praise_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String praise(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//long begin = System.currentTimeMillis();
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.praise(reqs,uid);
//		System.out.println(res);
		/*long end = System.currentTimeMillis();
		System.out.println("====================赞资源耗时："+(end-begin)+"=====================");*/
		return res;
	}
	/**
	 * 评论资源
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/comment_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String comment(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//long begin = System.currentTimeMillis();
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.comment(reqs,uid);
//		System.out.println(res);
		/*long end = System.currentTimeMillis();
		System.out.println("========================评论资源耗时:"+(end-begin)+"===================");*/
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 转发资源
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/repeat_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String repeat(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.repeat(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 转发资源
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/index_talentzone",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String indexTalentZone(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.indexTalentZone(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	
	/**
	 * 
	 * <p>Title: searchByLocation</p> 
	 * <p>Description: 根据位置搜索附近的资源</p> 
	 * @author :changjiang
	 * date 2015-6-16 下午4:35:39
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/search_location",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String searchByLocation(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceManager.searchByLocation(reqs, uid);
				//circleOfFriends(reqs,uid);
		
		return res;
	}
	
	/**
	 * 各种测试使用
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/clear_jedis/{type}",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response,@PathVariable String type) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			//e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET+e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		int flag = resourceManager.clearResourceFromJedis(type);
		if(ResultUtils.SUCCESS == flag){
			return "成功了";
		}else{
			return "失败了";
			
		}
		//List<UserTagJedis> list = jedisManager.takeUserTag(uid, reqs);
		
//		EasemobUser easemobUser = easemobUserManager.createNewIMUserSingle(uid);
		
	}
	/**
	 * 清空memcache
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/clearMemcache/{type}/{id}/{caseType}/{sort}/{page}",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String clearMemcache(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String type,@PathVariable String id,@PathVariable String caseType,
			@PathVariable String sort,@PathVariable String page) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16l;
		String result = "";
		if("0".equals(sort)){
			sort = "";
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		try {
			if("0".equals(type)){
//				if(ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.BOOK_MOVIE_LISTS)
//						|| ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.BOOKS)
//						|| ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.MOVIES)
//						|| ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.RESOURCES)){
//							
//				}
				if("0".equals(id)){
					
					String[] array1 = ResourceLinkManager.BOOK_MOVIE_LISTS;
					for (int i = 0; i < array1.length; i++) {
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_addTime"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_reviewNum"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_score"+"_"+page, 0, resourceInfos);
					}
					array1 = ResourceLinkManager.BOOKS;
					for (int i = 0; i < array1.length; i++) {
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_addTime"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_reviewNum"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_score"+"_"+page, 0, resourceInfos);
					}
					array1 = ResourceLinkManager.MOVIES;
					for (int i = 0; i < array1.length; i++) {
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_addTime"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_reviewNum"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_score"+"_"+page, 0, resourceInfos);
					}
					array1 = ResourceLinkManager.RESOURCES;
					for (int i = 0; i < array1.length; i++) {
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_addTime"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_reviewNum"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array1[i]+"_"+0+"_score"+"_"+page, 0, resourceInfos);
					}
				}else{
					String[] array2 = ResourceLinkManager.RESOURCE_BY_ID;
					for (int i = 0; i < array2.length; i++) {
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array2[i]+"_"+id+"_"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array2[i]+"_"+id+"_addTime"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array2[i]+"_"+id+"_reviewNum"+"_"+page, 0, resourceInfos);
						operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+array2[i]+"_"+id+"_score"+"_"+page, 0, resourceInfos);
					}
				}
			}else if("1".equals(type)){
				operationMemcachedClient.set(MemcacheResourceLinkConstant.MOVIE_BOOK_LIST_TYPE+id+"_"+caseType,0,resourceInfos);
			}
			result = "成功了";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			result = e.getMessage();
		}
		
		
		return result;
	}
	/**
	 * 测试
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/test",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		
		//updateJedisManager.sendMessage();
		
		
		//jedisManager.getResourcesByFriends(null, null);
		return "成功了";
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}
	public void setEasemobUserManager(EasemobUserManager easemobUserManager) {
		this.easemobUserManager = easemobUserManager;
	}
	public void setOperationMemcachedClient(MemcachedClient operationMemcachedClient) {
		this.operationMemcachedClient = operationMemcachedClient;
	}
	public void setUpdateJedisManager(UpdateJedisManager updateJedisManager) {
		this.updateJedisManager = updateJedisManager;
	}
}
