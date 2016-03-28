package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.SwitchManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class SwitchController extends BaseController{

	private static final Log LOG = LogFactory
			.getLog(SwitchController.class);
	
	private SwitchManager switchManager;
	
	@RequestMapping(value = "/clientaction/save_setting", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveNoticeSetting(HttpServletRequest request,
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
		String res =switchManager.operateNoticeSwitch(reqs, uid);
//		System.out.println(res);

		return res;
	}
	
	@RequestMapping(value = "/clientview/view_setting", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewSetting(HttpServletRequest request,
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
		String res =switchManager.viewSetting(reqs, uid);
//		System.out.println(res);

		return res;
	}

	public void setSwitchManager(SwitchManager switchManager) {
		this.switchManager = switchManager;
	}
	
	
}
