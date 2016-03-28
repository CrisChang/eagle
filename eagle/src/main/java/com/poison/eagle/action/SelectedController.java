package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poison.eagle.manager.SensitiveManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.RankingManager;
import com.poison.eagle.manager.SelectedManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

/**
 * 精选
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class SelectedController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(SelectedController.class);
//	private String reqs = "";
//	private long uid = 15l;
	private SelectedManager selectedManager;

	private SensitiveManager sensitiveManager;

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}
	/**
	 * 查询精选
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
	@RequestMapping(value = "/clientview/view_selected", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewSelected(HttpServletRequest request,
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
		String res =selectedManager.getSelected(reqs, uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}

		return res;
	}
	/**
	 * 查询精选
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
	@RequestMapping(value = "/clientview/view_selected_new", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewSelectedNew(HttpServletRequest request,
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
		String res =selectedManager.getSelectedNew(reqs, uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 查询精选
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
	@RequestMapping(value = "/clientview/view_selecteds", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewSelecteds(HttpServletRequest request,
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
		String res =selectedManager.getSelecteds(reqs, uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	/**
	 * 查询推荐用户列表
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
	@RequestMapping(value = "/clientview/view_selectedusers", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewSelectedUsers(HttpServletRequest request,
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
		String res =selectedManager.getSelectedUsers(reqs, uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}


	public void setSelectedManager(SelectedManager selectedManager) {
		this.selectedManager = selectedManager;
	}
}
