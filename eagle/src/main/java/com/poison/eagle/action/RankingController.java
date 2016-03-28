package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poison.eagle.manager.SensitiveManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.RankingManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;

/**
 * 排行
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class RankingController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(RankingController.class);
//	private String reqs = "";
//	private long uid = 15l;
	private RankingManager rankingManager;

	private SensitiveManager sensitiveManager;

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}
	
	
	/**
	 * 查询排行榜
	 * @Title: viewTopicRanking
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-17 下午2:48:29
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws UnsupportedEncodingException
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/clientview/view_ranking", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewTopicRanking(HttpServletRequest request,
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
			//return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =rankingManager.ViewRanking(reqs, uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	
	/**
	 * 查询图书热搜榜
	 * @Title: viewTopicRanking
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-17 下午2:48:29
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws UnsupportedEncodingException
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/clientview/view_book_search_ranking", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewBookSearchRanking(HttpServletRequest request,
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
			//return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =rankingManager.ViewBookSearchRanking(reqs, uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}

	/**
	 * 查询图书热搜榜
	 * @Title: viewTopicRanking
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-17 下午2:48:29
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws UnsupportedEncodingException
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/clientview/view_movie_search_ranking", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewMovieSearchRanking(HttpServletRequest request,
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
			//return RES_DATA_NOTGET;
		}
		
		//调用manager方法获取返回数据
		String res =rankingManager.ViewMovieSearchRanking(reqs, uid);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	
	/**
	 * 根据评论数量查询资源排行
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_resource_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewResourceRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.viewResourceRanking(reqs, uid);
//		System.out.println(res);
		//去除敏感字
		if(null!=sensitiveManager){
			res = sensitiveManager.checkSensitive(res);
		}
		return res;
	}
	
	
	/**
	 * 最新电影推荐榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/latest_movies_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String latestMoviesRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.latestMoviesRanking(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 最新剧集推荐榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/latest_dramas_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String latestDramasRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.latestDramasRanking(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	
	/**
	 * 每周电影热搜榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/week_movies_search_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String weekMoviesSearchRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.weekMoviesSearchRanking(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	//每周剧集热搜榜
	@RequestMapping(value="/clientview/week_dramas_search_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String weekDramasSearchRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.weekDramasSearchRanking(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 每周热门影单榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/week_hot_movielist",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String weekHotMovielist(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.weekHotMovielist(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 每周图书热搜榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/week_books_search_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String weekBooksSearchRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.weekBooksSearchRanking(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 最新图书推荐榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/latest_books_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String latestBooksRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.latestBooksRanking(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 每周热门书单榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/week_hot_booklist",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String weekHotBooklist(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.weekHotBooklist(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 根据阅读数查询图书排行
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/books_ranking_by_readnum",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String BooksRankingByReadnum(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.bookRankingByReadNum(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 根据阅读数查询影视排行
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/movies_ranking_by_readnum",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String MoviesRankingByReadnum(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.movieRankingByReadNum(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	/**
	 * 根据阅读数查询毒药排行
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/duyao_ranking",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String duYaoRanking(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.duYaoRanking(reqs, uid);
//		System.out.println(res);
		
		return res;
	}
	
	/**
	 * 原创榜数据获取
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientview/view_rankingList",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewRankingList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
		String res = rankingManager.viewRankingList(reqs, uid);
		return res;
	}
	
	/**
	 * 根据阅读数查询资源排行榜
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/clientview/resource_ranking_by_readnum",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String resourceRankingByReadNum(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0;
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
//		System.out.println(reqs);
		
		//调用manager方法获取返回数据
		String res = rankingManager.resourceRankingByReadNum(reqs, uid);
//		System.out.println(res);
		
		return res;
	}

	public void setRankingManager(RankingManager rankingManager) {
		this.rankingManager = rankingManager;
	}
}
