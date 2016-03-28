package com.poison.eagle.action.web;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.eagle.manager.web.MovieWebManager;
import com.poison.eagle.utils.CommentUtils;

/**
 * 连载
 * 
 * @author Administrator
 * 
 */
@Controller
//@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class MovieWebController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(MovieWebController.class);
	private MovieWebManager movieWebManager;
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
	public void setMovieWebManager(MovieWebManager movieWebManager) {
		this.movieWebManager = movieWebManager;
	}




	//返回的需要登录的错误信息--web端接口使用
	String RES_USER_NOTLOGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\""+CommentUtils.ERROR_USERNOTLOGIN+"\",\"code\":1}}}";
	
	
	/**
	 * 写影评
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/write_movie_comment", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeMovieComment(HttpServletRequest request,
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
		String result = movieWebManager.writeMovieComment(request, uid);
		return result;
	}
	
	/**
	 * 某个人的影评列表
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/get_mvcomment_list",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getMvCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		
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
		String result = movieWebManager.searchMvCommentList(request, uid);
		return result;
	}
	
	/**
	 * 获取某个影评的详情
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/get_one_mvcomment",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getOneMvComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);//设置允许跨域访问该路径方法
		
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
		String result = movieWebManager.getOneMvComment(request, uid);
		return result;
	}
	
	/**
	 * 删除影评
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/del_movie_comment", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delMovieComment(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0;
		// // //获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}

		// 调用manager方法获取返回数据
		String res = movieWebManager.delMovieComment(request, uid);

		return res;
	}
	//设置允许跨域
	private void setAlloweCrossDomain(HttpServletResponse response){
		//response.addHeader("Access-Control-Allow-Origin", "http://m.duyao001.com");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");	
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
}
