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

import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.ScanAddressManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
/**
 * 点击书单影单controller
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class ScanAddressController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(ScanAddressController.class);
//	private String reqs = "";
//	private long uid=0;
	private ScanAddressManager scanAddressManager;
	/**
	 * 点击书单方法 
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/scanaddress",method=RequestMethod.POST ,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String scanAddress(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
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
		String res = scanAddressManager.scanAddress(reqs,uid);
		
//		System.out.println("扫描通讯录结果"+res);
		return res;
	}
	public void setScanAddressManager(ScanAddressManager scanAddressManager) {
		this.scanAddressManager = scanAddressManager;
	}
	
	

	
	
	
}
