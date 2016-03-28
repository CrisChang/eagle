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

import com.poison.act.client.ActFacade;
import com.poison.eagle.manager.ActManager;
import com.poison.eagle.manager.BigManager;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.JedisApiManager;
import com.poison.eagle.manager.ResourceManager;
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
public class JedisApiController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(JedisApiController.class);
	private JedisApiManager jedisApiManager;
	/**
	 * 自测题列表
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/api/jedis/set", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String jedisSet(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
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
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =jedisApiManager.jedisSet(reqs);
//		System.out.println(res);

		return res;
	}
	/**
	 * 扫描名片二维码
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/app/download", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String download(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		
		
		try {
			response.sendRedirect("/qrcode/qrcode.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public void setJedisApiManager(JedisApiManager jedisApiManager) {
		this.jedisApiManager = jedisApiManager;
	}
}
