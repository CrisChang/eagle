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

import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.manager.TalentZoneManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.model.Serialize;
/**
 * 用户编辑信息manager
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class TalentZoneController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(TalentZoneController.class);
//	private String reqs = "";
//	private long uid=0;
	private TalentZoneManager talentZoneManager;
	/**
	 * 查询达人圈的方法
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_talentzone",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewTalentZone(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
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
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = talentZoneManager.viewTalentZone(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 查询某个达人圈的方法
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_one_talentzone",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewOneTalentZone(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
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
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = talentZoneManager.viewOneTalentZone(reqs,uid);
		System.out.println(res);
		
		return res;
	}
	/**
	 * 我关注的领袖
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_user_talentzone",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUserTalentZone(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
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
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = talentZoneManager.viewUserTalentZone(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 发现页首页
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_discovery",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewDiscovery(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
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
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = talentZoneManager.viewDiscovery(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 发现页推送精选资源
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/publish_resource",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String publishResource(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16;
		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//		LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
//		}
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = talentZoneManager.publishResource(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	
	
	public void setTalentZoneManager(TalentZoneManager talentZoneManager) {
		this.talentZoneManager = talentZoneManager;
	}
}
