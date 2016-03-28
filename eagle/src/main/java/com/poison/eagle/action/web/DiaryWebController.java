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

import com.poison.eagle.manager.web.ArticleWebManager;
import com.poison.eagle.manager.web.DiaryWebManager;
import com.poison.eagle.utils.CommentUtils;

/**
 * 连载
 * 
 * @author Administrator
 * 
 */
@Controller
//@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class DiaryWebController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(DiaryWebController.class);
	private ArticleWebManager articleWebManager;
	private DiaryWebManager diaryWebManager;
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
	
	public void setArticleWebManager(ArticleWebManager articleWebManager) {
		this.articleWebManager = articleWebManager;
	}
	public void setDiaryWebManager(DiaryWebManager diaryWebManager) {
		this.diaryWebManager = diaryWebManager;
	}
	
	
	//返回的需要登录的错误信息--web端接口使用
	String RES_USER_NOTLOGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\""+CommentUtils.ERROR_USERNOTLOGIN+"\",\"code\":1}}}";
	
	
	/**
	 * 写长图文
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/save_diary", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveDiary(HttpServletRequest request,
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
		String result = diaryWebManager.writeDiary(request, uid);
		return result;
	}
	
	// 查询图文列表
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webview/get_diary_list", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getDiaryList(HttpServletRequest request,
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
		String result = diaryWebManager.searchDiaryList(request, uid);
		
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
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/upload_diary_image", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
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
	 * 删除图文
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = CommentUtils.REQUEST_FROM_WEB+"/webaction/del_one_diary",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delOneDiary(HttpServletRequest request,
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
		String result = diaryWebManager.delOneDiary(request, uid);
		return result;
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
