package com.poison.eagle.action.web;

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

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.SerializeManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.manager.web.ArticleWebManager;
import com.poison.eagle.manager.web.GraphicFilmWebManager;
import com.poison.eagle.manager.web.SerializeWebManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.Uploader;
import com.poison.eagle.utils.WebUtils;
import com.poison.ucenter.model.UserAllInfo;

/**
 * 连载
 * 
 * @author Administrator
 * 
 */
@Controller
//@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class GraphicFilmWebController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(GraphicFilmWebController.class);
	private GraphicFilmWebManager graphicFilmWebManager;
	private File upload;  
    private String uploadContentType;  
    private String uploadFileName;  
	
	
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	private HttpUtils httpUtils = HttpUtils.getInstance();
	
	
	
	/**
	 * 批量上传图片
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/upload_image_for_film", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String uploadImageForFilm(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
//		String img  = request.getParameter("serializeImg");
//		long uid = Long.valueOf(request.getParameter("uid"));
		
		//调用manager方法获取返回数据
		Map<String, Object> map = graphicFilmWebManager.uploadImageForFilm(request,response);
		
		List<String> images = (List<String>) map.get("images");
		String url = (String) map.get("image");
//		Uploader up = (Uploader) map.get("uploader");
		
//		request.setCharacterEncoding("utf-8");
//		response.setCharacterEncoding("utf-8");
	    

//	    String callback = request.getParameter("callback");
//
//	    String result = "{\"name\":\""+ up.getFileName() +"\", \"originalName\": \""+ up.getOriginalName() +"\", \"size\": "+ up.getSize() +", \"state\": \""+ up.getState() +"\", \"type\": \""+ up.getType() +"\", \"url\": \""+ up.getUrl() +"\"}";
//
//	    result = result.replaceAll( "\\\\", "\\\\" );
//
		response.setCharacterEncoding("UTF-8");

		try {
			PrintWriter out = response.getWriter();
//	    if( callback == null ){
//				response.getWriter().print( result );
//	    }else{
			out.println("<script type=\"text/javascript\">");
			out.println("window.opener.addImage('" +url + "')"); 
			out.println("</script>");
//	    }	
			out.flush();
			out.close();
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		return null;
	}
	/**
	 * 某个人的长文章列表
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/view_graphic_film_list", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewGraphicFilmList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		LOG.info("sessionId"+session.getId()+"-----"+session.isNew());
		Long uid = null;
		LOG.info("productContext"+ProductContextHolder.getProductContext().toString());
		
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			try {
				request.setAttribute("toUrl", WEB_INDEX_PATH);
				request.setAttribute("error", "您还没有登录呢，请先去登陆！");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		//调用manager方法获取返回数据
		Map map = graphicFilmWebManager.viewGraphicFilmList(request,uid);
		
//		List<ResourceInfo> resourceInfos = (List<ResourceInfo>) map.get("list");
		
		try {
			request.getRequestDispatcher("/web_eagle/graphic_film/list.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 写图解电影
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/write_graphic_film", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeGraphicFilm(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			try {
				request.setAttribute("toUrl", WEB_INDEX_PATH);
				request.setAttribute("error", "您还没有登录呢，请先去登陆！");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		//调用manager方法获取返回数据
		Map map = graphicFilmWebManager.writeGraphicFilm(request,uid);
		
		int flagint = (Integer) map.get("flag");
		
		if(flagint == ResultUtils.SUCCESS  || flagint == 0){
			try {
				response.sendRedirect("/w/webview/view_graphic_film_list");
//				request.getRequestDispatcher("/webview/view_article_list").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				request.setAttribute("toUrl", WEB_ARTICLE_LIST);
				request.setAttribute("error", MessageUtils.getResultMessage(flagint));
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	/**
	 * 显示修改图解电影页面
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/view_update_grahic_film", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUpdateGrahicFilm(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			try {
				request.setAttribute("toUrl", WEB_INDEX_PATH);
				request.setAttribute("error", "您还没有登录呢，请先去登陆！");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		//调用manager方法获取返回数据
		Map map = graphicFilmWebManager.viewUpdateGrahicFilm(request,uid);
		
		int flagint = (Integer) map.get("flag");
		
		if(flagint == ResultUtils.SUCCESS  || flagint == 0){
			try {
				request.getRequestDispatcher("/web_eagle/graphic_film/edit.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				request.setAttribute("toUrl", CommentUtils.REQUEST_FROM_WEB+"/webview/view_graphic_film_list");
				request.setAttribute("error", MessageUtils.getResultMessage(flagint));
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	/**
	 * 修改长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/update_artle", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String updateArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			try {
				request.setAttribute("toUrl", WEB_INDEX_PATH);
				request.setAttribute("error", "您还没有登录呢，请先去登陆！");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
//		if(session != null){
//			//如果用户没登陆
//			if (session.getAttribute("userInfo") == null) {
//				try {
//					request.setAttribute("toUrl", WEB_INDEX_PATH);
//					request.setAttribute("error", "您还没有登录呢，请先去登陆！");
//					request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
//					//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			uid = ((UserEntity)session.getAttribute("userInfo")).getId();
//		}else{
//			try {
//				response.sendRedirect(request.getContextPath()+WEB_INDEX_PATH);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		//调用manager方法获取返回数据
		Map map = graphicFilmWebManager.updateArticle(request,uid);
		
		int flagint = (Integer) map.get("flag");
		
		if(flagint == ResultUtils.SUCCESS  || flagint == 0){
			try {
				response.sendRedirect("/w/webview/view_article_list");
//				request.getRequestDispatcher("/webview/view_article_list").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				request.setAttribute("toUrl", WEB_ARTICLE_LIST);
				request.setAttribute("error", MessageUtils.getResultMessage(flagint));
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	/**
	 * 删除长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/del_ticle", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			try {
				request.setAttribute("toUrl", WEB_INDEX_PATH);
				request.setAttribute("error", "您还没有登录呢，请先去登陆！");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		//调用manager方法获取返回数据
		Map map = graphicFilmWebManager.delArticle(request,uid);
		
		int flagint = (Integer) map.get("flag");
		
		if(flagint == ResultUtils.SUCCESS||flagint == 0){
			try {
				request.getRequestDispatcher("/w/webview/view_article_list").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				request.setAttribute("toUrl", WEB_ARTICLE_LIST);
				request.setAttribute("error", MessageUtils.getResultMessage(flagint));
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	/**
	 * 显示图解电影
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/view_graphic_film/{id}", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewGraphicFilm(HttpServletRequest request,
			HttpServletResponse response,@PathVariable String id) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			try {
				request.setAttribute("toUrl", WEB_INDEX_PATH);
				request.setAttribute("error", "您还没有登录呢，请先去登陆！");
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		long rid = Long.valueOf(id);
		//调用manager方法获取返回数据
		Map map = graphicFilmWebManager.viewGraphicFilm(request,uid,rid);
		
		int flagint = (Integer) map.get("flag");
		
		if(flagint == ResultUtils.SUCCESS||flagint == 0){
			try {
				request.getRequestDispatcher("/web_eagle/graphic_film/view.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				request.setAttribute("toUrl", WEB_ARTICLE_LIST);
				request.setAttribute("error", MessageUtils.getResultMessage(flagint));
				request.getRequestDispatcher(WEB_ERROR_PATH).forward(request, response);
				//response.sendRedirect(request.getContextPath()+WEB_ERROR_PATH);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	public void setGraphicFilmWebManager(GraphicFilmWebManager graphicFilmWebManager) {
		this.graphicFilmWebManager = graphicFilmWebManager;
	}
	
	
}
