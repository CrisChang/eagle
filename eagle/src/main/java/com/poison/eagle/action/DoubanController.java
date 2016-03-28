package com.poison.eagle.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.events.Comment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.JacksonObjectMapperFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.DoubanManager;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.SerializeManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.manager.web.ArticleWebManager;
import com.poison.eagle.manager.web.SerializeWebManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.ucenter.model.UserAllInfo;

/**
 * 连载
 * 
 * @author Administrator
 * 
 */
@Controller
public class DoubanController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(DoubanController.class);
	private DoubanManager doubanManager;
	/**
	 * 写日记
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/save_douban", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveDouban(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid=0;
		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
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
		String res =doubanManager.saveDouban(reqs);
		

		return res;
	}
	
	/**
	 * 根据书名和数字控制从豆瓣上查询的条数
	 * @author 111
	 * @param request
	 * @param response
	 * @param name
	 * @param num
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientaction/controlBKNumbers", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String controlBKNumbers(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String name = request.getParameter("name");
		int num = Integer.valueOf(request.getParameter("num"));
		return doubanManager.controlBKNumbers(name,num);
	}
	
	/**
	 * 根据电影名和数字控制从豆瓣上查询的条数
	 * @author 111
	 * @param request
	 * @param response
	 * @param name
	 * @param num
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientaction/controlMVNumbers", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String controlMVNumbers(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String name = request.getParameter("name");
		int num = Integer.valueOf(request.getParameter("num"));
//		System.out.println("电影名称："+name+",返回"+num+"条数");
		return doubanManager.controlMVNumbers(name,num);
	}
	
	public void setDoubanManager(DoubanManager doubanManager) {
		this.doubanManager = doubanManager;
	}
	
}
