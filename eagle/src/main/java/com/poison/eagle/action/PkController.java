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

import com.poison.eagle.manager.PkManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

/**
 * pk
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class PkController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(PkController.class);
//	private String reqs = "";
//	private long uid = 15l;
	private PkManager pkManager;
	
	
	/**
	 * 查询PK结果
	 * @Title: viewTopicRanking
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-17 下午2:48:29
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws UnsupportedEncodingException
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/clientview/view_pk_result", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewPkResult(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
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
			//return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =pkManager.getResPkResult(reqs, uid);
		
		return res;
	}


	public void setPkManager(PkManager pkManager) {
		this.pkManager = pkManager;
	}
}
