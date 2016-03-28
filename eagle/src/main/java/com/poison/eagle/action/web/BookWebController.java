package com.poison.eagle.action.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

import org.apache.commons.httpclient.HttpClient;
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

import org.omg.CORBA.portable.ResponseHandler;
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
import com.poison.eagle.manager.web.BookWebManager;
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
@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class BookWebController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(BookWebController.class);
	private BookWebManager bookWebManager;
	
	/**
	 * 修改长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webview/search_book_and_movie", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String searchBookMovie(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		Long uid = null;
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
		Map map = bookWebManager.searchBookMovie(request,uid);
		
//		String string = bookWebManager.getJsonFromUrl("http://112.126.68.72:8080/search/book/10/1?q="+request.getParameter("name"));
		
		
//		HttpClient httpclient = new DefaultHttpClient();  
//        try {  
//            HttpGet httpget = new HttpGet("http://www.google.com/");  
//  
//            System.out.println("executing request " + httpget.getURI());  
//  
//            // Create a response handler  
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();  
//            String responseBody = httpclient.execute(httpget, responseHandler);  
//            System.out.println("----------------------------------------");  
//            System.out.println(responseBody);  
//            System.out.println("----------------------------------------");  
//  
//        } finally {  
//            // When HttpClient instance is no longer needed,  
//            // shut down the connection manager to ensure  
//            // immediate deallocation of all system resources  
//            httpclient.getConnectionManager().shutdown();  
//        }  
//    }  
		
		
		
//		
//		URL url = null;
//		HttpURLConnection url_con = null;
//		StringBuilder tempStr = null;
//		try {  
//            url = new URL("http://112.126.68.72:8080/search/book/10/1?q="+request.getParameter("name"));  
//            url_con = (HttpURLConnection) url.openConnection();  
//            url_con.setRequestMethod("GET");  
//            url_con.setDoOutput(true);  
//  
////            url_con.getOutputStream().write("".getBytes());  
//            url_con.getOutputStream().flush();  
//            url_con.getOutputStream().close();  
//            InputStream in = url_con.getInputStream();  
//            BufferedReader rd = new BufferedReader(new InputStreamReader(in));  
//            tempStr = new StringBuilder();  
//            while (rd.read() != -1) {  
//                tempStr.append(rd.readLine());  
//            }  
//  
//        }catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {  
//            if (url_con != null)  
//                url_con.disconnect();  
//        }  
//		System.out.println(tempStr.toString());
		String json = (String) map.get("json");
		
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			
				out.println("<script type=\"text/javascript\">");
				out.println("window.parent.searchBookMovieResult('"+ json + "','" + request.getParameter("type") + "')");
				out.println("</script>");
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 写书评
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/write_book_comment", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeBookComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		String result = bookWebManager.writeBookComment(request, uid);
		return result;
	}
	/**
	 * 获取某个书评的详情
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webview/get_one_bkcomment",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getOneBkComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		String result = bookWebManager.getOneBkComment(request, uid);
		return result;
	}
	/**
	 * 删除书评
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webaction/del_book_comment", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String delBookComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		long uid = 0;
		// 获取用户id
		if (checkUserIsLogin()) {
			uid = getUserId();
		} else {
			uid = 0;
			LOG.error(CommentUtils.ERROR_USERNOTLOGIN);
			return RES_USER_NOTLOGIN;
		}
		// 调用manager方法获取返回数据
		String res = bookWebManager.delBookComment(request, uid);

		return res;
	}
	/**
	 * 获取用户的书评列表
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/webview/get_bkcomment_list",produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getBkCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
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
		String result = bookWebManager.viewBookCommentList(request, uid);
		return result;
	}
	public void setBookWebManager(BookWebManager bookWebManager) {
		this.bookWebManager = bookWebManager;
	}
}
