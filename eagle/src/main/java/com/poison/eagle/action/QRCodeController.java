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
import com.poison.eagle.manager.QRCodeManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
/**
 * 用户编辑信息manager
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class QRCodeController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(QRCodeController.class);
	
//	private String reqs = "";
//	private long uid=0;
	
	private QRCodeManager qrCodeManager;
	
	/**显示用户二维码
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_qrcode",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUserQRCode(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
//		LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
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
		String res = qrCodeManager.viewUserQRCode(reqs,uid);
//		System.out.println(res);

		return res;
	}
	/**
		扫描二维码
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/scanning_qrcode/{type}/{id}",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String scanningQrcode(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0;
//		LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
//			return RES_USER_NOTLOGIN;
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
		String res = qrCodeManager.scanningQrcode(reqs,uid);
//		System.out.println(res);
		
		return res;
	}
	/**显示书或影的二维码
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_bm_qrcode",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBMQRcode(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
		String res = qrCodeManager.viewBMQRcode(reqs);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 通过二维码关注人
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/plus_user_byqrcode/{id}",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String plusUserByQRCode(HttpServletRequest request, HttpServletResponse response, @PathVariable Long param) throws UnsupportedEncodingException{
		
		
//		
//		//调用manager方法获取返回数据
		String res = qrCodeManager.plusUserByQRCode(param,16l);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 通过二维码关注收藏书、电影
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/comment_bm_byqrcode/{type}/{id}/{status}",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String commentBMByQRCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type, @PathVariable Long id,@PathVariable String status) throws UnsupportedEncodingException{
		
		if(CommentUtils.REQUEST_FROM_WEB.equals(status)){
			return "";
		}
		return null;
	}

	public void setQrCodeManager(QRCodeManager qrCodeManager) {
		this.qrCodeManager = qrCodeManager;
	}
}
