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

import com.poison.eagle.entity.StringProperties;
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

import com.poison.act.client.ActFacade;
import com.poison.eagle.manager.ActManager;
import com.poison.eagle.manager.BigManager;
import com.poison.eagle.manager.BookListManager;
import com.poison.eagle.manager.BookManager;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.MovieListManager;
import com.poison.eagle.manager.MovieManager;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.manager.SerializeListManager;
import com.poison.eagle.manager.SerializeManager;
import com.poison.eagle.manager.ShareManager;
import com.poison.eagle.manager.TipManager;
import com.poison.eagle.manager.TopicManager;
import com.poison.eagle.manager.UserInfoManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.StringUtils;

/**
 * 分享页面统一调用地址
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class ShareAPiController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(ShareAPiController.class);
	private BookListManager bookListManager;
	private MovieListManager movieListManager;
	private MovieManager movieManager;
	private BookManager bookManager;
	private ResourceManager resourceManager;
	private TipManager tipManager;
	private TopicManager topicManager;
	private StringProperties stringProperties;

	public void setStringProperties(StringProperties stringProperties) {
		this.stringProperties = stringProperties;
	}

	public void setTipManager(TipManager tipManager) {
		this.tipManager = tipManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setMovieManager(MovieManager movieManager) {
		this.movieManager = movieManager;
	}
	public void setBookManager(BookManager bookManager) {
		this.bookManager = bookManager;
	}
	public void setMovieListManager(MovieListManager movieListManager) {
		this.movieListManager = movieListManager;
	}
	public void setTopicManager(TopicManager topicManager) {
		this.topicManager = topicManager;
	}
	/**
	 * 分享页面统一地址
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/share/{id}/{type}/{uid}", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewShareList(HttpServletRequest request,
			HttpServletResponse response,@PathVariable Long id,@PathVariable String type,@PathVariable Long uid) throws UnsupportedEncodingException {
		//System.out.println(id+"==="+type+"===="+uid);
		
		request.setAttribute("id", id);
		request.setAttribute("type", type);
		request.setAttribute("uid", uid);

		try {
			if(CommentUtils.TYPE_BOOKLIST.equals(type)){//书单
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-book-list.html?id="+id);
			}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){//影单
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-movie-list.html?id="+id);
			}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){//书评
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-book-comment.html?id="+id);
			}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){//影评
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-movie-comment.html?id="+id);
			}else if(CommentUtils.TYPE_BOOK.equals(type)){//一本书
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-book.html?id="+id+"&type=27");
			}else if(CommentUtils.TYPE_MOVIE.equals(type)){//一本电影
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-movie.html?id="+id);
			}else if(CommentUtils.TYPE_NETBOOK.equals(type)){//网络小说
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-book.html?id="+id+"&type=29");
			}else if(CommentUtils.TYPE_DIARY.equals(type)){//一个日志
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-text.html?id="+id+"&type=3");
			}else if(CommentUtils.TYPE_NEWARTICLE.equals(type)){
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/article.html?id="+id);
			}else if(CommentUtils.TYPE_TOPIC.equals(type)){
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/topic.html?id="+id);
			}else if(CommentUtils.TYPE_BOOK_DIGEST.equals(type)){
				response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-bookl-comment.html?id="+id);
			}else{

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分享页面统一地址
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/view_onebooklist.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneBookList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		//{id}/{page}/{uid}
		String id = request.getParameter("id");
		String page = request.getParameter("page");
		String uidStr = request.getParameter("uid");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("page", page);
			json.put("web", "web");
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		
		/*try {
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 *
	 * <p>Title: oneMovieList</p> 
	 * <p>Description: 一个影单详情</p>
	 * @author :changjiang
	 * date 2015-3-10 下午12:18:32
	 * @param request
	 * @param response
	 * @param page
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_onemovielist.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneMovieList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		
		String id = request.getParameter("id");
		String page = request.getParameter("page");
		String uidStr = request.getParameter("uid");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("page", page);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = movieListManager.viewNewOneMovieList(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		/*try {
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 
	 * <p>Title: oneMovie</p> 
	 * <p>Description: 一个电影的详情页</p> 
	 * @author :changjiang
	 * date 2015-3-10 下午12:19:08
	 * @param request
	 * @param response
	 * @param id
	 * @param page
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_onemovie.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneMovie(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String id = request.getParameter("id");
		String page = request.getParameter("page");
		String uidStr = request.getParameter("uid");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("page", page);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = movieManager.viewOneMovie(req.toString(), uid);
				//movieListManager.viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		/*try {
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 
	 * <p>Title: oneBook</p> 
	 * <p>Description: 一个图书的详情</p> 
	 * @author :changjiang
	 * date 2015-3-10 下午4:13:38
	 * @param request
	 * @param response
	 * @param id
	 * @param page
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_onebook.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneBook(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String uidStr = request.getParameter("uid");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("type", type);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = bookManager.viewBook(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		/*try {
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 
	 * <p>Title: oneBookComment</p> 
	 * <p>Description: 一个书评详情</p> 
	 * @author :changjiang
	 * date 2015-3-11 上午10:47:02
	 * @param request
	 * @param response
	 * @param id
	 * @param page
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_oneresourcecomment.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneResourceComment(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		//String uidStr = request.getParameter("uid");
		Long uid = 1l;
		/*if(null==uidStr||"".equals(uidStr)){
			uid = 1l;
		}*/
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("type", type);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = resourceManager.oneResource(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 
	 * <p>Title: oneCommentList</p> 
	 * <p>Description: 评论列表或者点赞列表</p> 
	 * @author :changjiang
	 * date 2015-3-13 下午4:50:51
	 * @param request
	 * @param response
	 * @param id
	 * @param type
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_onecommentlist.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String uidStr = request.getParameter("uid");
		String lastId = request.getParameter("lastId");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("type", type);
			json.put("lastId", lastId);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = resourceManager.commentList(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 
	 * <p>Title: oneGiveList</p> 
	 * <p>Description: 打赏列表</p> 
	 * @author :changjiang
	 * date 2015-4-28 下午8:01:08
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_onegivelist.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneGiveList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String uidStr = request.getParameter("uid");
		String lastId = request.getParameter("lastId");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("type", type);
			json.put("lastId", lastId);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = tipManager.viewTiplistByBesourceid(req.toString(), uid);
				//resourceManager.commentList(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	
	@RequestMapping(value = "/view_onebookdigest.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneBookDigest(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String bookId = request.getParameter("id");
		String page = request.getParameter("page");
		String type = request.getParameter("type");
		String lastId = request.getParameter("lastId");
		String uidStr = request.getParameter("uid");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("bookId", bookId);
			json.put("page", page);
			json.put("type", type);
			json.put("id", lastId);
			json.put("uid", uid);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = bookManager.viewBookTalkList(req.toString());
				//movieManager.viewMovieCommentList(req.toString(), uid);
				//bookManager.viewBookCommentList(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}

	
	/**
	 * 
	 * <p>Title: oneCommentList</p> 
	 * <p>Description: 书的评论列表</p> 
	 * @author :changjiang
	 * date 2015-3-12 下午3:52:31
	 * @param request
	 * @param response
	 * @param id
	 * @param page
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_onebookcommentlist.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneBookCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String lastId = request.getParameter("lastId");
		String uidStr = request.getParameter("uid");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("type", type);
			json.put("lastId", lastId);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = bookManager.viewBookCommentList(req.toString(), uid);
				//movieManager.viewMovieCommentList(req.toString(), uid);
				//bookManager.viewBookCommentList(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 
	 * <p>Title: oneMovieCommentList</p> 
	 * <p>Description: 电影的评论列表</p> 
	 * @author :changjiang
	 * date 2015-3-12 下午6:32:52
	 * @param request
	 * @param response
	 * @param id
	 * @param type
	 * @param lastId
	 * @param uid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/view_onemoviecommentlist.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneMovieCommentList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String lastId = request.getParameter("lastId");
		String uidStr = request.getParameter("uid");
		Long uid = 0l;
		if(null==uidStr||"".equals(uidStr)){
			uid = 0l;
		}
		try {
			//System.out.println(callback);
			json.put("movieId", id);
			json.put("type", type);
			json.put("lastId", lastId);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//String 
		String res = movieManager.viewMovieCommentList(req.toString(), uid);
				//movieManager.viewMovieCommentList(req.toString(), uid);
				//bookManager.viewBookCommentList(req.toString(), uid);
				//viewMovieList(req.toString(), uid);
				//bookListManager.viewNewOneBookList(req.toString(), uid);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 分享页面统一地址
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/view_topic.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneTopic(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		//{id}/{page}/{uid}
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		if(title!=null){
			title = URLDecoder.decode(title,"UTF-8");
			//title = new String(title.getBytes("ISO8859-1"), "UTF-8");
		}
		try {
			//System.out.println(callback);
			json.put("id", id);
			json.put("title", title);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//String 
		String res = null;
		if(id!=null && StringUtils.isInteger(id)){
			res = topicManager.viewTopicById(req.toString(),0L);
		}else{
			res = topicManager.viewTopic(req.toString(),0L);
		}
		if(null!=callback){
			res = callback+"("+res+")";
		}
		
		/*try {
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	/**
	 * 分享页面统一地址
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/view_topiclink.do", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String oneTopicLinks(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject req = new JSONObject();
		String callback = (String) request.getParameter("callback");
		//{id}/{page}/{uid}
		String topicid = request.getParameter("topicId");
		String resid = request.getParameter("lastId");
		try {
			//System.out.println(callback);
			json.put("topicid", topicid);
			json.put("resid", resid);
			data.put("data", json);
			req.put("req", data);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//String 
		String res = topicManager.viewTopicResources(req.toString(), 0);
		if(null!=callback){
			res = callback+"("+res+")";
		}
		
		/*try {
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return res.replace("\\r", "<p/>").replace("\\n", "<p/>");
	}
	
	
	public void setBookListManager(BookListManager bookListManager) {
		this.bookListManager = bookListManager;
	}
	
}
