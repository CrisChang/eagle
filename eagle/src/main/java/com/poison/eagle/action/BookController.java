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

import com.poison.eagle.manager.*;
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
public class BookController extends BaseController {
	private static final Log LOG = LogFactory.getLog(BookController.class);
	private BookManager bookManager;
	private SensitiveManager sensitiveManager;

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}

	// private String reqs = "";
	// private long uid;
	/**
	 * 展示书的信息
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_book", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBook(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.viewBook(reqs, uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 展示书的试读
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_bookread", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBookRead(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.viewBookRead(reqs);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 查找书的信息
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/search_book", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String searchBook(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.searchBook(reqs, uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 查找书的信息
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_book_comment_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBookCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.viewBookCommentList(reqs, uid);
		// LOG.info(res);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 
	 * <p>
	 * Title: viewBookHotCommentList
	 * </p>
	 * <p>
	 * Description: 获取热门的书评列表
	 * </p>
	 * 
	 * @author :changjiang date 2015-6-30 下午6:08:02
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_book_hotcomment_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBookHotCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.viewBookCommentPraiseList(reqs, uid);
		// viewBookCommentList(reqs,uid);
		// LOG.info(res);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		};
		return res;
	}
	
	/**
	 * 查询某本书的书摘列表（新的书摘资源）
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_book_digest_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBookDigestList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.viewBookDigestList(reqs, uid);
		// viewBookCommentList(reqs,uid);
		// LOG.info(res);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		};
		return res;
	}
	
	
	
	/**
	 * 写书评
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/write_book_comment", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeBookComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.writeBookComment(reqs, uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 删除书评
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clientaction/del_book_comment", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delBookComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.delBookComment(reqs, uid);

		return res;
	}

	/**
	 * 保存豆瓣的书
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/save_value", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveValue(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// //获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.saveValue(reqs);

		return res;
	}

	/**
	 * 
	 * <p>
	 * Title: saveValueByUser
	 * </p>
	 * <p>
	 * Description: 添加用户自己添加书籍
	 * </p>
	 * 
	 * @author :changjiang date 2015-4-20 下午6:29:52
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientaction/save_user_book", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveValueByUser(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// //获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.saveBookByUser(reqs, uid);

		return res;
	}

	/**
	 * 些书摘
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/write_booktalk", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeBookTalk(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// //获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.writeBookTalk(reqs, uid);

		return res;
	}

	/**
	 * 书摘列表
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_booktalk_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBookTalkList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// //获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.viewBookTalkList(reqs);

		return res;
	}

	/**
	 * 
	 * <p>
	 * Title: viewUserBookTalkList
	 * </p>
	 * <p>
	 * Description: 查询书摘列表
	 * </p>
	 * 
	 * @author :changjiang date 2015-4-10 下午5:39:41
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_user_booktalk_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUserBookTalkList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// //获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		// 调用manager方法获取返回数据
		String res = bookManager.viewBookTalkListByUserId(reqs, uid);
		return res;
	}

	/**
	 * 删除书摘
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/del_booktalk", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delBookTalk(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// //获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = bookManager.delBookTalk(reqs);

		return res;
	}

	public void setBookManager(BookManager bookManager) {
		this.bookManager = bookManager;
	}

}
