package com.poison.eagle.action.web;

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

import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.SerializeManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.manager.web.SerializeWebManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.ucenter.model.UserAllInfo;

/**
 * 连载
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class SerializeWebController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(SerializeWebController.class);
	private SerializeWebManager serializeWebManager;
	private Map resMap;
	private String flag;
	
	private long uid = 65l;
	private String title;
	private String describe;
	private String author;
	private String url;
	private String tag;
	private long id;
	private String content;
	private int isPublish;
	
	private HttpUtils httpUtils = HttpUtils.getInstance();
	/**
	 * 上传封面
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/upload_serialize_img", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String uploadSerializeImg(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
//		String img  = request.getParameter("serializeImg");
//		long uid = Long.valueOf(request.getParameter("uid"));
		
		//调用manager方法获取返回数据
		resMap =serializeWebManager.uploadSerializeImg(request);
		System.out.println(resMap.toString());
		
		flag = (String) resMap.get("flag");
		if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
//			request.setAttribute("serialize.id", id);
//			request.setAttribute("serialize.title", title);
		}else{
			String error = (String) resMap.get("error");
		}
		try {
			request.getRequestDispatcher("/webview/view_serialize_list?currPage=1").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return resMap.toString();
		return null;
	}
	/**
	 * 写连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/write_serialize", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		title = request.getParameter("title");
		describe = request.getParameter("describe");
		author = request.getParameter("author");
		url = request.getParameter("url");
		tag = request.getParameter("tag");
		Long sid = Long.valueOf(request.getParameter("id"));
		
		//调用manager方法获取返回数据
		resMap =serializeWebManager.writeSerialize(sid,title,describe,author,url,tag,uid);
		System.out.println(resMap.toString());

		flag = (String) resMap.get("flag");
		/*if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
			Long id = Long.valueOf(resMap.get("id").toString());
			request.setAttribute("serialize.id", id);
			request.setAttribute("serialize.title", title);
		}else{
			String error = (String) resMap.get("error");
		}
		try {
			request.getRequestDispatcher("/web_eagle/addChapter.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//return resMap.toString();
		return null;
	}
	/**
	 * ajax写连载
	 *
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/ajax_write_serialize", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String ajaxWriteSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		title = request.getParameter("title");
		describe = request.getParameter("describe");
		author = request.getParameter("author");
		url = request.getParameter("url");
		tag = request.getParameter("tag");
		Long sid = Long.valueOf(request.getParameter("id"));
		//调用manager方法获取返回数据
		resMap =serializeWebManager.writeSerialize(sid,title,describe,author,url,tag,uid);
		System.out.println(resMap.toString());
		
		flag = (String) resMap.get("flag");
		JSONObject json = new JSONObject();
		if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
			String id = resMap.get("id").toString();
			System.out.println(id+"----------------");
			try {
				json.put("success", "success");
				json.put("id", id);
				json.put("title", title);
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
		}
		return null;
	}
	/**
	 * 写章节
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/write_chapter", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeChapter(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		id = Long.valueOf(request.getParameter("id"));
		title = request.getParameter("title");
		content = request.getParameter("content");
		Long cid = Long.valueOf(request.getParameter("chatperId"));
		isPublish = Integer.valueOf(request.getParameter("isPublish"));
		
		//调用manager方法获取返回数据
		resMap =serializeWebManager.writeChapter(cid,id, title , content , isPublish,uid);
		System.out.println(resMap.toString());
		
		try {
			String toUrl = "/webview/view_serialize_list?currPage=1";
			request.setAttribute("toUrl", toUrl);
			request.getRequestDispatcher("/web_eagle/success.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		//return resMap.toString();
	}
	/**
	 * 写章节
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/ajax_write_chapter", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String ajaxWriteChapter(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		id = Long.valueOf(request.getParameter("id"));
		title = request.getParameter("title");
		content = request.getParameter("content");
		Long cid = Long.valueOf(request.getParameter("chatperId"));
		
		isPublish = 1;
		
		//调用manager方法获取返回数据
		resMap =serializeWebManager.writeChapter(cid,id, title , content , isPublish,uid);
		System.out.println(resMap.toString());
		
		flag = (String) resMap.get("flag");
		JSONObject json = new JSONObject();
		if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
			String chatperId = resMap.get("chatperId").toString();
			try {
				json.put("success", "success");
				json.put("chatperId", chatperId);
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
		}
		
		
		return null;
		//return resMap.toString();
	}
	
	/**
	 * 续写连载章节
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/serialize_chapter", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String serializeChapter(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String flag = serializeWebManager.seleceSerializeByid(request, response);
		
		try {
			if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
				request.getRequestDispatcher("/web_eagle/addChapter.jsp").forward(request, response);
			}else{
				request.setAttribute("toUrl", WEB_SERIALIZE_LIST_PATH);
				request.setAttribute("error", "没有该连载！");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		//return resMap.toString();
	}
	/**
	 * 修改连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/update_serialize", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String updateSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		//获取用户id
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//			return RES_USER_NOTLOGIN;
//		}		
		Long id =Long.valueOf(request.getParameter("id"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String type = request.getParameter("type");
		Long uid = getUserId(request);
		if(uid == null){
			request.setAttribute("toUrl", WEB_INDEX_PATH);
			request.setAttribute("error", "您还没有登录呢，请先去登陆！");
			try {
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//调用manager方法获取返回数据
		String flag  =serializeWebManager.updateSerialize(id, title, content, type, uid, request);
		
		try {
			if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
				
				if (CommentUtils.REQ_ISON_TRUE.equals(type)) {
					//修改连载后跳转到连载列表页面
					request.setAttribute("toUrl", WEB_SERIALIZE_LIST_PATH);
				} else{
					//修改章节后，跳转到章节列表页面
					request.setAttribute("toUrl", WEB_SERIALIZE_LIST_PATH);
				}
				
				request.getRequestDispatcher(WEB_SUCCESS_PATH).forward(request, response);
			}else{
				//错误后跳转页面
				if (CommentUtils.REQ_ISON_TRUE.equals(type)) {
					//修改连载错误后，跳转到连载列表页面
					request.setAttribute("toUrl", WEB_SERIALIZE_LIST_PATH);
				} else{
					//修改章节错误后，跳转到章节列表页面
					request.setAttribute("toUrl", "/webview/view_chapter_list");
				}
				
				request.setAttribute("error", "修改失败");
				request.getRequestDispatcher(WEB_SUCCESS_PATH).forward(request, response);
			}
			
		} catch (Exception e) {
		}
		
		return null;
	}
	/**
	 * 删除连载
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/del_serialize", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		
		//调用manager方法获取返回数据
		flag =serializeWebManager.delSerialize(request);
		
		try {
			if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
				LOG.debug("删除连载成功");
				request.getRequestDispatcher(WEB_SERIALIZE_LIST_PATH).forward(request, response);
			}else{
				LOG.debug("删除连载失败");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				
			}
		} catch (Exception e) {
		}
		
		return null;
	}
	/**
	 * 连载目录
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value ="/webview/view_serialize_list", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewSerialize(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
//		if(checkUserIsLogin()){
//			uid = getUserId();
//		}else{
//			uid = 0;
//			return RES_USER_NOTLOGIN;
//		}	
		if(session != null){
			//如果用户没登陆
			if (session.getAttribute("userInfo") == null) {
				try {
					request.setAttribute("toUrl", WEB_INDEX_PATH);
					request.setAttribute("error", "您还没有登录呢，请先去登陆！");
					request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
					//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			uid = ((UserEntity)session.getAttribute("userInfo")).getId();
		}else{
			try {
				response.sendRedirect(request.getContextPath()+WEB_INDEX_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//调用manager方法获取返回数据
		flag =serializeWebManager.viewSerializeList(uid,request,response);
		
		try {
			if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
				request.getRequestDispatcher(WEB_SERIALIZE_PATH).forward(request, response);
			}else{
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 章节目录
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webview/view_chapter_list", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewChapterList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String type = request.getParameter("type");
		if (CommentUtils.REQ_ISON_TRUE.equals(type)) {
			//跳转到修改连载页面
			try {
				String flag = serializeWebManager.seleceSerializeByid(request, response);
				if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
					request.getRequestDispatcher("/web_eagle/updateContinued.jsp").forward(request, response);
				}else{
					request.setAttribute("toUrl", WEB_SERIALIZE_LIST_PATH);
					request.setAttribute("error", "没有该连载！");
					request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			//跳转到查询章节目录页面
			//调用manager方法获取返回数据
			flag =serializeWebManager.viewChapterList(request);

			try {
				if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
					request.getRequestDispatcher("/web_eagle/chapter.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	/**
	 * 查询章节
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webview/view_chapter", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewChapter(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		
		HttpSession session = request.getSession(false);
		if(session != null){
			//如果用户没登陆
			if (session.getAttribute("userInfo") == null) {
				try {
					request.setAttribute("toUrl", WEB_INDEX_PATH);
					request.setAttribute("error", "您还没有登录呢，请先去登陆！");
					request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
					//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			uid = ((UserEntity)session.getAttribute("userInfo")).getId();
		}else{
			try {
				response.sendRedirect(request.getContextPath()+WEB_INDEX_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//调用manager方法获取返回数据
		
		try {
			String type = request.getParameter("type");
			String flag2 = serializeWebManager.seleceSerializeByid(request, response);
			if(CommentUtils.RES_FLAG_SUCCESS.equals(flag2)){
				
				if (CommentUtils.REQ_ISON_TRUE.equals(type)) {
					String flag =serializeWebManager.updateChapter(uid ,request, response);
					//跳转到修改章节页面
					try {
						if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
							request.getRequestDispatcher("/web_eagle/updateChapter.jsp").forward(request, response);
						}else{
							request.setAttribute("toUrl", WEB_SERIALIZE_LIST_PATH);
							request.setAttribute("error", "没有该连载！");
							request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					//跳转到显示章节内容
					String flag =serializeWebManager.viewChapter(uid ,request, response);
					
					if(CommentUtils.RES_FLAG_SUCCESS.equals(flag)){
						request.getRequestDispatcher(WEB_VIEWCHAPTER_PATH).forward(request, response);
					}else{
						request.setAttribute("toUrl", WEB_SERIALIZE_LIST_PATH);
						request.setAttribute("error", "没有该连载！");
						request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
					}
				}
				
			}else{
				request.setAttribute("toUrl", WEB_VIEWCHAPTER_PATH);
				request.setAttribute("error", "查询失败");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public void setSerializeWebManager(SerializeWebManager serializeWebManager) {
		this.serializeWebManager = serializeWebManager;
	}
	
}
