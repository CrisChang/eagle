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
import javax.servlet.http.Cookie;
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
public class ArticleWebController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(ArticleWebController.class);
	private ArticleWebManager articleWebManager;
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
	 * 上传图片(百度编辑器)
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/upload_image_by_ueditor", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String uploadImageByUeditor(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
//		String img  = request.getParameter("serializeImg");
//		long uid = Long.valueOf(request.getParameter("uid"));
		
		//调用manager方法获取返回数据
		Map<String, Object> map = articleWebManager.uploadImageByUeditor(request);
		Uploader up = (Uploader) map.get("uploader");
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	    

	    String callback = request.getParameter("callback");

	    String result = "{\"name\":\""+ up.getFileName() +"\", \"originalName\": \""+ up.getOriginalName() +"\", \"size\": "+ up.getSize() +", \"state\": \""+ up.getState() +"\", \"type\": \""+ up.getType() +"\", \"url\": \""+ up.getUrl() +"\"}";

	    result = result.replaceAll( "\\\\", "\\\\" );

	    try {
	    if( callback == null ){
				response.getWriter().print( result );
	    }else{
	        response.getWriter().print("<script>"+ callback +"(" + result + ")</script>");
	    }
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		return null;
	}
	/**
	 * 上传图片
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/upload_img", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String uploadSerializeImg(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
//		String img  = request.getParameter("serializeImg");
//		long uid = Long.valueOf(request.getParameter("uid"));
		
		//调用manager方法获取返回数据
		Map<String, String> map = articleWebManager.uploadImg(request);
		String callback =request.getParameter("CKEditorFuncNum"); 
		
		response.setCharacterEncoding("UTF-8");

		try {
			PrintWriter out = response.getWriter();
			
			if("0".equals(map.get("flag"))){
				out.println("<script type=\"text/javascript\">");
				out.println("window.parent.CKEDITOR.tools.callFunction("+ callback + ",'" +map.get("uploadPath") + "','')"); 
				out.println("</script>");
			}else{
				out.print("<font color=\"red\" size=\"2\">"+map.get("error")+"</font>");  
			}
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
//		try {
//			request.getRequestDispatcher("/webview/view_serialize_list?currPage=1").forward(request, response);
//		} catch (ServletException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//return resMap.toString();
		return null;
	}
	/**
	 * 上传封面
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/upload_article_pic", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String uploadArticlePic(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		
		String image = request.getParameter("picImage");
		//调用manager方法获取返回数据
		Map<String, String> map = articleWebManager.uploadImg(request);
		
		response.setCharacterEncoding("UTF-8");
		
		try {
			PrintWriter out = response.getWriter();
			
			if("0".equals(map.get("flag"))){
				out.println("<script type=\"text/javascript\">");
				out.println("window.parent.resultImage('" +map.get("uploadPath") + "','" +map.get("error") + "')"); 
				out.println("</script>");
//				out.print("<font  size=\"2\">上传成功</font>");  
			}else{
//				out.print("<font color=\"red\" size=\"2\" style=\"width:100%\">"+map.get("error")+"</font>");  
			}
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
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
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/view_article_list", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewArticleList(HttpServletRequest request,
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
		Map map = articleWebManager.viewArticleList(request,uid);
		
		List<ResourceInfo> resourceInfos = (List<ResourceInfo>) map.get("list");
		
		System.out.println(resourceInfos);
		try {
			request.getRequestDispatcher("/web_eagle/article/list.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 写长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/write_article", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeArticle(HttpServletRequest request,
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
		Map map = articleWebManager.writeArticle(request,uid);
		
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
	 * 显示修改长文章页面
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/view_update_article", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewUpdateArticle(HttpServletRequest request,
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
		Map map = articleWebManager.viewUpdateArticle(request,uid);
		
		int flagint = (Integer) map.get("flag");
		
		if(flagint == ResultUtils.SUCCESS  || flagint == 0){
			try {
				request.getRequestDispatcher("/web_eagle/article/edit.jsp").forward(request, response);
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
	 * 修改长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/update_article", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
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
		Map map = articleWebManager.updateArticle(request,uid);
		
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
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/del_article", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
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
		Map map = articleWebManager.delArticle(request,uid);
		
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
	
	public void setArticleWebManager(ArticleWebManager articleWebManager) {
		this.articleWebManager = articleWebManager;
	}
	
	
	//======================================以下是新的web相关的接口======================================
	//返回的需要登录的错误信息--web端接口使用
	String RES_USER_NOTLOGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\""+CommentUtils.ERROR_USERNOTLOGIN+"\",\"code\":1}}}";
	
	/**
	 * 某个人的长文章列表
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/get_article_list", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getArticleList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		/*Cookie cookies[]=request.getCookies();
		if(cookies!=null){
			for(int i=0;i<cookies.length;i++){
				System.out.println("======================"+i+":"+cookies[i].getName()+":"+cookies[i].getValue());
			}
		}else{
			System.out.println("=======================cookies为null");
		}*/
		
		Long uid = null;
		LOG.info("productContext"+ProductContextHolder.getProductContext().toString());
		
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.searchArticleList(request, uid);
		return result;
	}
	
	/**
	 * 写长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/save_article", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.saveArticle(request,uid);
		return result;
	}
	
	/**
	 * 删除长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/delete_article", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String deleteArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.deleteArticle(request, uid);
		return result;
	}
	
	/**
	 * 显示修改长文章页面
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/get_update_article", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getUpdateArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.getUpdateArticle(request,uid);
		return result;
	}
	
	/**
	 * 修改长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/save_update_article", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveUpdateArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.saveUpdateArticle(request,uid);
		
		return result;
	}
	
	/**
	 * 上传封面
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/upload_article_cover", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String uploadArticleCover(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		String image = request.getParameter("picImage");
		//调用manager方法获取返回数据
		String result = articleWebManager.uploadArticleImg(request);
		//result = result+ "<script>document.domain = \"duyao001.com\";</script>";
		response.setCharacterEncoding("UTF-8");		
		
		return result;
	}
	
	/**
	 * 查看文章详情
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/get_article_info",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getArticleInfo(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法		
		Long uid = null;
		LOG.info("productContext"+ProductContextHolder.getProductContext().toString());
		
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			/*LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;*/
		}
		//调用manager方法获取返回数据,未登录用户也可以查看
		String result = articleWebManager.getArticleInfo(request,uid);
		return result;
	}
	
	/**
	 * 将草稿发布为正式文章
	*/
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/public_article", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String publicArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.publicArticle(request, uid);
		
		return result;
	}
	/**
	 * 保存草稿，新增或更新
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/save_articledraft", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveArticleDraft(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.saveArticleDraft(request, uid);
		
		return result;
	}
	/**
	 * 删除草稿
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/delete_articledraft", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String deleteArticleDraft(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.deleteArticleDraft(request, uid);
		
		return result;
	}
	//某个人的草稿列表
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/get_articledraft_list", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getArticleDraftList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.getArticleDraftList(request, uid);
		
		return result;
	}
	// 查询草稿详情
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/get_articledraft_info", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getArticleDraftInfo(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		Long uid = null;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		//调用manager方法获取返回数据
		String result = articleWebManager.getArticleDraftInfo(request, uid);
		
		return result;
	}
	
	//设置允许跨域
	private void setAlloweCrossDomain(HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "http://m.duyao001.com");
		//response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");	
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
}
