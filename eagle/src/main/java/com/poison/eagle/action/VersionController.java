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

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.PlusInterManager;
import com.poison.eagle.manager.ScanAddressManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.manager.VersionManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
/**
 * 点击书单影单controller
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class VersionController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(VersionController.class);
	private String reqs = "";
	private String res;
	private long uid;
	private VersionManager versionManager;
	/**
	 * 点击书单方法 
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/check_version",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getVersion(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		
		//获取客户端json数据
		reqs = getParameter(request, "req");
		
		//判断是否能正常获取数据
		if(CommentUtils.ERROR_CODE_GETDATAERROR.equals(reqs)){
			return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		res = versionManager.getVersion(reqs);
		

		return res;
	}
	public void setVersionManager(VersionManager versionManager) {
		this.versionManager = versionManager;
	}
	
	
	
}
