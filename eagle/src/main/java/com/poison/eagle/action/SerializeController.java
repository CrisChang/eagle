package com.poison.eagle.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poison.eagle.manager.SensitiveManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.JacksonObjectMapperFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.SerializeManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

/**
 * 连载
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class SerializeController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(SerializeController.class);
//	private String reqs = "";
//	private long uid=0;
	private SerializeManager serializeManager;

	private SensitiveManager sensitiveManager;

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}
	/**
	 * 写连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/write_serialize", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		//调用manager方法获取返回数据
		String res =serializeManager.writeSerialize(reqs,uid);
//		System.out.println(res);

		return res;
	}
	/**
	 * 写连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/write_chapter", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeChapter(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		//调用manager方法获取返回数据
		String res =serializeManager.writeChapter(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 修改连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/update_serialize", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String updateSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		//调用manager方法获取返回数据
		String res =serializeManager.updateSerialize(reqs,uid);
		
		
		return res;
	}
	/**
	 * 删除连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/del_novel", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid=0;
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =serializeManager.delSerialize(reqs);
		
//		System.out.println(res);
		return res;
	}
	/**
	 * 连载目录
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value ="/clientview/view_novel_list", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid=0;
		
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =serializeManager.viewSerializeList(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
//		System.out.println(res);
		return res;
	}
	/**
	 * 章节目录
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_chapter_list", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewChapterList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid=0;
		//获取客户端json数据
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =serializeManager.viewChapterList(reqs,uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 章节目录
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_chapter", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewChapter(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid=0;
		//获取客户端json数据
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =serializeManager.viewChapter(reqs,uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		
		return res;
	}

	@RequestMapping(value = "/clientaction/add_readed_noviellist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String addReadedNovellist(HttpServletRequest request,
							  HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid=0;
		//获取客户端json数据
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		//调用manager方法获取返回数据
		String res =serializeManager.addNovelList(reqs,uid);


		return res;
	}

	public void setSerializeManager(SerializeManager serializeManager) {
		this.serializeManager = serializeManager;
	}



	
	
}
