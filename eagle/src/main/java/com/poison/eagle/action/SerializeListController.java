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
import com.poison.eagle.manager.SerializeListManager;
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
public class SerializeListController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(SerializeListController.class);
	private String reqs = "";
	private SerializeListManager serializeListManager;
	private long uid;
	/**
	 * 创建连载单
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/create_public_serializelist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String createSerializeList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		//调用manager方法获取返回数据
		String res =serializeListManager.createSerializeList(reqs,35l);
//		System.out.println(res);

		return res;
	}
	/**
	 * 连载单中添加连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/write_serializelist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeSerializeList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		//调用manager方法获取返回数据
		String res =serializeListManager.writeSerializeList(reqs,35l);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 连载单中添加连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_public_serializelist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewPublicSerializeList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		//调用manager方法获取返回数据
		String res =serializeListManager.viewPublicSerializeList(reqs,35l);
//		System.out.println(res);
		
		return res;
	}


	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_readed_novellist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewReadedSerializeList(HttpServletRequest request,
										  HttpServletResponse response) throws UnsupportedEncodingException {
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
		LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		String reqs = "";
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		//调用manager方法获取返回数据
		String res =serializeListManager.viewReadedSerializeList(reqs,uid);
//		System.out.println(res);

		return res;
	}

	@RequestMapping(value = "/clientaction/del_readed_novellist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delReadedSerializeList(HttpServletRequest request,
										  HttpServletResponse response) throws UnsupportedEncodingException {
		long uid = 0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		String reqs = "";
		//获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		//调用manager方法获取返回数据
		String res =serializeListManager.delSerializeListLink(reqs,uid);
				//viewReadedSerializeList(reqs,uid);
//		System.out.println(res);

		return res;
	}

	/**
	 * 查看某一个连载单
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_oneserializelist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewOneSerializeList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		//调用manager方法获取返回数据
		String res =serializeListManager.viewOneSerializeList(reqs,35l);
//		System.out.println(res);
		
		return res;
	}
	public void setSerializeListManager(SerializeListManager serializeListManager) {
		this.serializeListManager = serializeListManager;
	}
	
}
