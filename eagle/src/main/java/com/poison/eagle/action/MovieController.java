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
import org.json.JSONArray;
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
public class MovieController extends BaseController {
	private static final Log LOG = LogFactory.getLog(MovieController.class);
	// private String reqs = "";
	// private long uid = 15l;

	private MovieManager movieManager;
	private SensitiveManager sensitiveManager;

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
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
	@RequestMapping(value = "/clientaction/save_movie", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveMovie(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// // //获取用户id
		String reqs = "";
		long uid = 0;
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
		String res = movieManager.saveMovie(reqs);

		return res;
	}

	/**
	 * 通过爬虫保存豆瓣的电影
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/savemovie_analyse", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveMovieAnalyse(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.saveMovieAnalyse(reqs);

		return res;
	}

	/**
	 * 写影评
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/write_movie_comment", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeMovieComment(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		//long startTime = System.currentTimeMillis();
		long uid = 0;
		// // //获取用户id
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
		String res = movieManager.writeMovieComment(reqs, uid);
		//long endTime = System.currentTimeMillis();
		//response.setDateHeader("Expires", 1000*60*2);
//		try {
//			Thread.sleep(1000*2);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
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
	@RequestMapping(value = "/clientaction/del_movie_comment", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.delMovieComment(reqs, uid);

		return res;
	}

	/**
	 * 影评列表
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_movie_comment_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewMovieCommentList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.viewMovieCommentList(reqs, uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 
	 * <p>
	 * Title: viewMovieHotCommentList
	 * </p>
	 * <p>
	 * Description: 查询热门影评列表
	 * </p>
	 * 
	 * @author :changjiang date 2015-7-1 下午2:44:21
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_movie_hotcomment_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewMovieHotCommentList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String res = movieManager.viewMovieCommentPraiseList(reqs, uid);
		// viewBookCommentList(reqs,uid);
		// LOG.info(res);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 单个电影的详情
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_movie", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewOneMovie(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.viewOneMovie(reqs, uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 单个电影的详情(新接口)
	 * 
	 * @param request
	 * @param response
	 * @author zhangqi
	 * @return res 客户端传过来的json数据
	 */
	@RequestMapping(value = "/clientview/view_moviedetail", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewOneMovieDetail(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}
		// 调用manager方法获取返回数据
		String res = movieManager.viewOneMovieDetail(reqs, uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 添加一条讨论信息
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/write_movietalk", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String writeMovieTalk(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.writeMovieTalk(reqs, uid);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 展示讨论信息列表
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_movietalk_list", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewMovieTalkList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.viewMovieTalkList(reqs);

		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 插入剧照
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/save_moviephoto", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveMoviePhoto(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.saveMoviePhoto(reqs);

		return res;
	}

	/**
	 * 插入预告片
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/save_movietrailer", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String saveMovieTrailer(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.saveMovieTrailer(reqs);

		return res;
	}

	/**
	 * 展示图解电影
	 * 
	 * @param request
	 * @param response
	 * @param res
	 *            客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientview/view_graphic_film", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewGraphicFilm(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		// 获取客户端json数据
		try {
			reqs = request.getParameter("req");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			return RES_DATA_NOTGET;
		}

		// 调用manager方法获取返回数据
		String res = movieManager.viewGraphicFilm(reqs);

		return res;
	}

	/**
	 * 获取电影角色信息所有 无角色剧照
	 * 
	 * @param request
	 * @param response
	 * @author zhangqi
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_mvactors", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewMvActors(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String res = movieManager.viewMvActors(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 获取单个角色详情
	 * 
	 * @param request
	 * @param response
	 * @author zhangqi
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_actordetail", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewActorDetail(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String res = movieManager.viewActorDetail(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 获取角色所关联的电影信息
	 * 
	 * @param request
	 * @param response
	 * @author zhangqi
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_actormovies", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewActorMovies(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String res = movieManager.viewActorMovies(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 获取电影资讯
	 * 
	 * @param request
	 * @param response
	 * @author zhangqi
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_moviemessage", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewMovieMessage(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String res = movieManager.viewMovieMessage(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 获取电影花絮
	 * 
	 * @param request
	 * @param response
	 * @author zhangqi
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/clientview/view_moviescoop", method = RequestMethod.POST, produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewMovieScoop(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String res = movieManager.viewMovieScoop(reqs);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 获取电影图解
	 * 
	 * @param request
	 * @param response
	 * @author zhangqi type = 110 res_id = movie.id
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	 /*@RequestMapping(value = "/clientview/view_moviescheme", method =RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	 public String viewMovieScheme(HttpServletRequest request,
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
	 return RES_DATA_NOTGET;
	 }
	 //调用manager方法获取返回数据
	 String res =movieManager.viewMovieScheme(reqs);
	 return res;
	 }*/

	public void setMovieManager(MovieManager movieManager) {
		this.movieManager = movieManager;
	}
}
