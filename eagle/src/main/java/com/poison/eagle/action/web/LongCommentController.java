package com.poison.eagle.action.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.BookManager;
import com.poison.eagle.manager.MovieManager;
import com.poison.eagle.utils.CommentUtils;

@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class LongCommentController extends BaseController{

	private static final Log LOG = LogFactory
			.getLog(LongCommentController.class);
	
	private MovieManager movieManager;
	private BookManager bookManager;
	
	@RequestMapping(value = "/webaction/write_long_mvcomment", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeLongMvComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
		HttpSession session = request.getSession(false);
		Long uid = 89l;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			return RES_USER_NOTLOGIN;
		}	
		
		String reqs = request.getParameter("req");
		//调用manager方法获取返回数据
		//Map map = bookWebManager.searchBookMovie(request,uid);
		String res = movieManager.writeMovieComment(reqs, uid);

		//String json = (String) map.get("json");
		
		/*response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			
				out.println("<script type=\"text/javascript\">");
				out.println("window.parent.searchBookMovieResult('"+ json + "','" + request.getParameter("type") + "')");
				out.println("</script>");
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		//response.setHeader("Access-Control-Allow-Origin", "*");
		return res;
	}
	
	
	//设置允许跨域
	private void setAlloweCrossDomain(HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "http://dev.duyao001.com");
		//response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");	
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
		
	@RequestMapping(value = "/webaction/write_long_bkcomment", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeLongBkComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
		HttpSession session = request.getSession(false);
		Long uid = 89l;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			return RES_USER_NOTLOGIN;
		}	
		String reqs = request.getParameter("req");
		//调用manager方法获取返回数据
		//Map map = bookWebManager.searchBookMovie(request,uid);
		String res = bookManager.writeBookComment(reqs, uid);
				//movieManager.writeMovieComment(reqs, uid);

		//String json = (String) map.get("json");
		
		/*response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			
				out.println("<script type=\"text/javascript\">");
				out.println("window.parent.searchBookMovieResult('"+ json + "','" + request.getParameter("type") + "')");
				out.println("</script>");
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		//response.setHeader("Access-Control-Allow-Origin", "*");
		return res;
	}
	
	/**
	 * 
	 * <p>Title: viewOneMovie</p> 
	 * <p>Description: 查看一个电影</p> 
	 * @author :changjiang
	 * date 2015-6-24 下午3:52:38
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/webview/view_one_movie", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewOneMovie(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
		String reqs = "";
		long uid = 0;
////		//获取用户id
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
		String res =movieManager.viewOneMovie(reqs,uid);
		
		
		return res;
	}
	
	/**
	 * 
	 * <p>Title: viewBook</p> 
	 * <p>Description: 查询一个书的详情</p> 
	 * @author :changjiang
	 * date 2015-6-24 下午4:02:49
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/webview/view_one_book", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBook(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
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
			return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =bookManager.viewBook(reqs,uid);
		

		return res;
	}
	
	/**
	 * 
	 * <p>Title: viewLongMvCommentList</p> 
	 * <p>Description: 查询长影评列表</p> 
	 * @author :changjiang
	 * date 2015-6-25 下午12:03:39
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/webview/view_long_mvcommentlist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewLongMvCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
		String reqs = "";
		long uid = 0;
////		//获取用户id
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
		String res =movieManager.viewUserLongMovieCommentList(reqs, uid);
		
		return res;
	}
	
	@RequestMapping(value = "/webview/view_long_bkcommentlist", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewLongBkCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		setAlloweCrossDomain(response);
		String reqs = "";
		long uid = 0;
////		//获取用户id
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
		String res =bookManager.viewLongBookCommentList(reqs, uid);
		
		return res;
	}

	public void setMovieManager(MovieManager movieManager) {
		this.movieManager = movieManager;
	}

	public void setBookManager(BookManager bookManager) {
		this.bookManager = bookManager;
	}
	
	
}
