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

import com.poison.eagle.manager.*;
import org.apache.commons.lang.StringUtils;
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
public class ResourceLinkController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(ResourceLinkController.class);
//	private String reqs = "";
//	private long uid=0;
	private ResourceLinkManager resourceLinkManager;

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
	@RequestMapping(value="/clientview/fuck_business",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String fuckBusiness(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16;
		long begin = System.currentTimeMillis();
		LOG.info("调用首页接口开始");
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
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceLinkManager.fuckBusiness(reqs,uid);
//		System.out.println(res);
		long end = System.currentTimeMillis();
		System.out.println("调用首页接口结束，耗时："+(end - begin));
		LOG.info("调用首页接口结束，耗时："+(end - begin));

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	
	/**补充搜索结果为空的内容*/
	@RequestMapping(value="/clientview/soso",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String sosoBusiness(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=16;
		long begin = System.currentTimeMillis();
		LOG.info("调用首页接口开始");
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
			String id = request.getParameterValues("id") != null ? request.getParameterValues("id")[0] : "0";
			String caseType = request.getParameterValues("caseType") != null ? request.getParameterValues("caseType")[0] : "0";
			String sort = request.getParameterValues("sort") != null ? request.getParameterValues("sort")[0] : "";
			if(StringUtils.equals("0", sort)){
				sort = "";
			}
			String page = request.getParameterValues("page") != null ? request.getParameterValues("page")[0] : "0";
			reqs="{\"req\":{\"data\":{\"id\":\"" + id +"\",\"caseType\":\"" + caseType + "\",\"sort\":\"" + sort + "\",\"page\":\"" + page +"\"}}}";
			//reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = resourceLinkManager.fuckBusiness(reqs,uid);
//		System.out.println(res);
		long end = System.currentTimeMillis();
//		System.out.println("调用首页接口结束，耗时："+(end - begin));
		LOG.info("调用首页接口结束，耗时："+(end - begin));

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	
	public void setResourceLinkManager(ResourceLinkManager resourceLinkManager) {
		this.resourceLinkManager = resourceLinkManager;
	}
}
