package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import com.poison.eagle.utils.*;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPublish;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.CommentInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.MovieListInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.craw.AnalyzerUtils;
import com.poison.eagle.utils.craw.CrawlUtils;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.ext.constant.MemcacheResourceLinkConstant;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.Serialize;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class MovieListManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(MovieListManager.class);
	
	private int flagint;
	private MvFacade mvFacade;
	private MvCommentFacade mvCommentFacade;
	private MyMovieFacade myMovieFacade;
	private ActFacade actFacade;
	private UcenterFacade ucenterFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private PaycenterFacade paycenterFacade;
	private MyBkFacade myBkFacade;
	
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	private FileUtils fileUtils = FileUtils.getInstance();
	
	private ResourceManager resourceManager;
	
	private MemcachedClient operationMemcachedClient;
	private UserJedisManager userJedisManager;
	
	/**
	 * 热搜影单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewHotsearchMovie(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		
		String type = "";
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = (String) dataq.get("type");
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		List<MvListLink> mvListLinks = new ArrayList<MvListLink>();
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		
		movieInfos = getHotMovieList(type);
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", movieInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 获取热搜或即将上映影单
	 * @param type
	 * @return
	 */
	public List<MovieInfo> getHotMovieList(String type){
		List<MvListLink> mvListLinks = new ArrayList<MvListLink>();
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		
		if(CommentUtils.TYPE_MOVIELIST.equals(type)){
			//热搜
			mvListLinks = myMovieFacade.findMovieListInfo(CommentUtils.WEB_PUBLIC_HOT_MOVIELIST_ID, null,null);
			movieInfos = getMovieInfoResponseList(mvListLinks,null,movieInfos);
		}else if(CommentUtils.TYPE_MOVIE_UPCOMING.equals(type)){
			//即将上映
			mvListLinks = myMovieFacade.findMovieListInfo(39933854080958464l, null,null);
			movieInfos = getMovieInfoResponseList(mvListLinks,null,movieInfos);
		}
		return movieInfos;
	}
	/**
	 * 创建公共影单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String createPublicMovieList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String name = "";
		String reason = "";
		String tag = "";
		
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			name = (String) dataq.get("name");
			reason = (String) dataq.get("reason");
			tag = (String) dataq.get("tag");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		flagint = myMovieFacade.addServerMvList(name, reason, tag);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 公共影单列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewPublicMovieList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String tag = "";
		Long lastId = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			tag = (String) dataq.get("tag");
			lastId = Long.valueOf(dataq.get("lastId").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		if(lastId == 0){
			lastId = null;
		}
		
		List<MovieList> movieLists =myMovieFacade.queryServerMvList(tag, lastId);

		List<MovieListInfo> movieListInfos = new ArrayList<MovieListInfo>(); 
		movieListInfos = getResponseList(movieLists,uid, movieListInfos);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", movieListInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 创建影单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String createMovieList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String name = "";
		String reason = "";
		long id = 0;
		List<String> tags = new ArrayList<String>();
		String movieListPic = "";
		
		
		//去掉空格
		reqs = reqs.trim();

		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			name = (String) dataq.get("name");
			reason = (String) dataq.get("reason");
			tags = (List<String>) dataq.get("tags");
			movieListPic = (String) dataq.get("movieListPic");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		if(id == UNID){
			flagint = myMovieFacade.addNewMvList(uid, name, reason, "");
		}else{
			flagint = myMovieFacade.updateMovieList(id, name, reason, CheckParams.putListToString(tags), movieListPic);
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 我的影单目录
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewMovieList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String name = "";
		Long id = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		List<MovieListInfo> movieListInfos = new ArrayList<MovieListInfo>(); 
		long startTime = System.currentTimeMillis();
		movieListInfos = getMyMovieList(id);
		long endTime = System.currentTimeMillis();
		System.out.println("查询个人影单耗时"+(endTime-startTime));
		//收藏的影单
		/*List<ResourceInfo> collects = new ArrayList<ResourceInfo>();
		List<ActCollect> actCollects = actFacade.findUserCollectList(null, id, CommentUtils.TYPE_MOVIELIST);
		
		collects = resourceManager.getResponseList(actCollects, uid, collects);
		Collections.sort(collects);*/
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", movieListInfos);
			//datas.put("collects", collects);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: viewMyCollectedMovieList</p> 
	 * <p>Description: 查询收藏的列表</p> 
	 * @author :changjiang
	 * date 2015-4-22 下午2:59:36
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewMyCollectedMovieList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String name = "";
		Long id = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		/*List<MovieListInfo> movieListInfos = new ArrayList<MovieListInfo>(); 
		
		movieListInfos = getMyMovieList(id);*/
		long startTime = System.currentTimeMillis();
		//收藏的影单
		List<ResourceInfo> collects = new ArrayList<ResourceInfo>();
		List<ActCollect> actCollects = actFacade.findUserCollectList(null, id, CommentUtils.TYPE_MOVIELIST);
		
		collects = resourceManager.getResponseList(actCollects, uid, collects);
		Collections.sort(collects);
		long endTime = System.currentTimeMillis();
		System.out.println("个人收藏影单耗时"+(endTime -startTime ));
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("list", movieListInfos);
			datas.put("collects", collects);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	
	public List<MovieListInfo> getMyMovieList(Long uid){
		List<MovieListInfo> movieListInfos = new ArrayList<MovieListInfo>(); 
		List<MovieList> movieLists = myMovieFacade.queryFilmListByUid(uid, null);
		
		movieListInfos = getMyMovieListToListInfos(movieLists, uid, movieListInfos);
				//getResponseList(movieLists,uid, movieListInfos);
		if(movieListInfos == null){
			movieListInfos = new ArrayList<MovieListInfo>();
		}
//		//收藏的影单
//		List<ActCollect> actCollects = actFacade.findUserCollectList(null, uid, CommentUtils.TYPE_MOVIELIST);
//		List<MovieListInfo> movieListInfos2 = new ArrayList<MovieListInfo>();
//		movieListInfos2 = getMovieList(actCollects,uid,movieListInfos2);
//		
//		movieListInfos.addAll(movieListInfos2);
		if(movieListInfos == null){
			movieListInfos = new ArrayList<MovieListInfo>();
		}
		//按数量倒序
		Collections.sort(movieListInfos);
		return movieListInfos;
	}
	/**
	 * 影单中添加电影
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeMovieList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		int movieId = 0;
		long movieListId = 0;
		int isDB =0;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			movieId = Integer.valueOf(dataq.get("movieId").toString());
			movieListId = Long.valueOf(dataq.get("movieListId").toString());
			//isDB = Integer.valueOf(dataq.get("isDB").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
//		flagint = myMovieFacade.addMovieListLink(movieListId, movieId, isDB);
		
		MvListLink mvListLink = myMovieFacade.moveOneMovie(movieId, movieListId, uid);
		
		flagint = mvListLink.getFlag();
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将附加信息加入缓存中
			resourceManager.updateInListByJedis((long)movieId, uid, CommentUtils.TYPE_MOVIE, "0");
			//修改影单的封面
			try {
				MvInfo mvInfo = mvFacade.queryById(movieId);
				MovieList movieList = myMovieFacade.findMovieListById(movieListId);
				
				String movieListPic = movieList.getCover();
				String mvInfoPic = mvInfo.getMoviePic();
				if(mvInfoPic == null){
					mvInfoPic = "";
				}
				if("".equals(movieListPic)){
					//修改影单封面
					myMovieFacade.updateMovieListPic(movieListId, mvInfoPic);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 一个影单的内容
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewOneMovieList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long movieListId = 0;
//		long lastId = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			movieListId = Long.valueOf(dataq.get("movieListId").toString());
//			lastId = Long.valueOf(dataq.get("lastId").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		//获取影单中的电影
		List<MvListLink> mvListLinks = new ArrayList<MvListLink>();
//		if(lastId  == UNID){
			mvListLinks = myMovieFacade.findMovieListInfo(movieListId, null,null);
//		}else{
//			mvListLinks = myMovieFacade.findMovieListInfo(movieListId, lastId);
//		}
		
		//将电影转换为显示类型
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		
		movieInfos = getMovieInfoResponseList(mvListLinks,uid,movieInfos);
//		s
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", movieInfos);
			
			//从缓存中获取影单被收藏的数量
			int count =0;
			try {
				count = resourceManager.getResourceCollectCountFromJedis((long)movieListId, CommentUtils.TYPE_MOVIELIST);
			} catch (Exception e) {
				count =0;
				LOG.error("获取电影被收藏数量出错，id为："+movieListId+e.getMessage(), e.fillInStackTrace());
			}
			datas.put("collectCount", count);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 一个影单的内容{xin}
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewNewOneMovieList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long movieListId = 0;
		Long lastId = null;
		Integer page = 0;
		int size = 0;
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			movieListId = Long.valueOf(dataq.get("id").toString());
			//System.out.println("影单ID为"+movieListId);
//			try {
//				lastId = Long.valueOf(dataq.get("lastId").toString());
//				if(lastId == 0){
//					lastId = null;
//				}
//			} catch (Exception e) {
//				lastId = null;
//			}
			try {
				page = Integer.valueOf(dataq.get("page").toString());
				if(page == null){
					page =0;
				}
			} catch (Exception e) {
				page = 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		MovieListInfo movieListInfo = new MovieListInfo();
		
//		Map<String, Object> memcacheMap = new HashMap<String, Object>();

		long begin = System.currentTimeMillis();
		try {
			//影单的详情
			MovieList movieList = myMovieFacade.findMovieListById(movieListId);
			//long endtime11 = System.currentTimeMillis();
			//System.out.println("查询这个影单的详情用时为"+(endtime11-begin));
			movieListInfo = movieUtils.putMVListToMovieList(movieList, myMovieFacade, mvFacade, ucenterFacade,actFacade,myBkFacade,uid);

			//long endtime12 = System.currentTimeMillis();
			//System.out.println("添加影单各种附加信息用时为"+(endtime12-endtime11));
			movieUtils.putMoneyToMovieList(movieListInfo, paycenterFacade);
			//long endTime = System.currentTimeMillis();
			//System.out.println("查询出来的影单的信息用时为"+(endTime-begin));
			resourceInfos = null;//operationMemcachedClient.get(MemcacheResourceLinkConstant.MOVIE_BOOK_LIST_TYPE+movieListId+"_"+CommentUtils.TYPE_MOVIELIST+"_"+page);
			if( null== resourceInfos || resourceInfos.size() == 0){
				resourceInfos = new ArrayList<ResourceInfo>();
				
				//获取影单中的电影
				List<MvListLink> mvListLinks = new ArrayList<MvListLink>();
				mvListLinks = myMovieFacade.findMovieListInfo(movieListId,null, null);
				if(mvListLinks != null){
					size = mvListLinks.size();
				}
				//分页
				mvListLinks = CheckParams.getListByPage(mvListLinks, page, CommentUtils.BOOK_MOVIE_LIST_SIZE);
				int type = movieList.getType();
				//long endtime1 = System.currentTimeMillis();
				//System.out.println("显示一个一影单的详细信息用时"+(endtime1-endTime));
				//将电影转换为显示类型
				resourceInfos = getNewMovieInfoResponseList(mvListLinks,uid,resourceInfos,type,movieList);
				
				//long endtime2 = System.currentTimeMillis();
				//System.out.println("将一个影单详情转化为显示类型用时"+(endtime2-endtime1));
				
//				memcacheMap = new HashMap<String, Object>();
				//memcacheMap.put("movieListInfo", movieListInfo);
//				memcacheMap.put("resourceInfos", resourceInfos);
				if(type == 2){
					//System.out.println("调用电影缓存");
					operationMemcachedClient.set(MemcacheResourceLinkConstant.MOVIE_BOOK_LIST_TYPE+movieListId+"_"+CommentUtils.TYPE_MOVIELIST+"_"+page,MemcacheResourceLinkConstant.TIME_INTERVALS_60_60,resourceInfos);
				}
			}
			flagint = ResultUtils.SUCCESS;
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		//long end = System.currentTimeMillis();
		//System.out.println("影单详情耗时："+(end-begin));
		//LOG.info("影单详情耗时："+(end-begin));
		//加入有用没用的数量

		Map<String, Object> usefulMap = new HashMap<String, Object>();
		usefulMap = actFacade.findUsefulCount(movieListId);
		int usefulCount = (Integer) usefulMap.get("usefulCount");
		Map<String, Object> uselessMap = new HashMap<String, Object>();
		uselessMap = actFacade.findUselessCount(movieListId);
		int uselessCount = (Integer) uselessMap.get("uselessCount");
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
			datas.put("size", size);
			movieListInfo.setUsefulCount(usefulCount);
			movieListInfo.setUselessCount(uselessCount);
//			datas.put("usefulCount",usefulCount);
//			datas.put("uselessCount",uselessCount);
			//判断对当前用户是否关注
			long userId = movieListInfo.getUserEntity().getId();
			String inList = userJedisManager.getRelationUserAttentionInfo(uid,userId, JedisConstant.RELATION_USER_ISINTEREST);
			if(null==inList){
				inList = "2";
			}
			movieListInfo.getUserEntity().setIsInterest(Integer.valueOf(inList));

			if(page == 0 || page == 1){
				datas.put("movieListInfo", movieListInfo);
			}
			
			//从缓存中获取影单被收藏的数量
			int count =0;
			try {
				count = resourceManager.getResourceCollectCountFromJedis((long)movieListId, CommentUtils.TYPE_MOVIELIST);
			} catch (Exception e) {
				count =0;
				LOG.error("获取电影被收藏数量出错，id为："+movieListId+e.getMessage(), e.fillInStackTrace());
			}
			datas.put("collectCount", count);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		//System.out.println(resString);
		return resString;
	}
	/**
	 * 删除影单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delMovieList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		MovieList movieList = myMovieFacade.deleteMovieList(id);
		flagint = movieList.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 删除影单中的电影
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delOneMovie(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		int movieId = 0;
		long movieListId = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			movieId = Integer.valueOf(dataq.get("movieId").toString());
			movieListId = Long.valueOf(dataq.get("movieListId").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		flagint = myMovieFacade.deleteMovieListLink(movieListId, movieId);
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将附加信息加入缓存中
			resourceManager.updateInListByJedis((long)movieId, uid, CommentUtils.TYPE_MOVIE, "1");
			//修改影单的封面
			try {
				MvInfo mvInfo = mvFacade.queryById(movieId);
				MovieList movieList = myMovieFacade.findMovieListById(movieListId);
				
				String movieListPic = movieList.getCover();
				String mvInfoPic = mvInfo.getMoviePic();
				if(mvInfoPic == null){
					mvInfoPic = "";
				}
				if(movieListPic.equals(mvInfoPic) || "".equals(movieListPic)){
					//修改影单封面
					List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(movieListId, null,3);
					if(mvListLinks != null && mvListLinks.size()>0){
						//xiugai
						mvInfo = mvFacade.queryById(mvListLinks.get(0).getMovieId());
						if(mvInfo.getId() != 0){
							mvInfoPic = mvInfo.getMoviePic();
							if(mvInfoPic == null){
								mvInfoPic = "";
							}
						}else{
							mvInfoPic = "";
						}
						myMovieFacade.updateMovieListPic(movieListId, mvInfoPic);
					}else{
						myMovieFacade.updateMovieListPic(movieListId, "");
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 移动影单中的电影
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String moveMovieList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		int movieId = 0;
		long movieListId = 0;
		long nowMovieListId = 0;
		int isDB = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			movieId = Integer.valueOf(dataq.get("movieId").toString());
			isDB = Integer.valueOf(dataq.get("isDB").toString());
			movieListId = Long.valueOf(dataq.get("movieListId").toString());
			nowMovieListId = Long.valueOf(dataq.get("nowMovieListId").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		
		flagint = myMovieFacade.addMovieListLink(nowMovieListId, movieId, isDB);
		if(flagint == ResultUtils.SUCCESS){
			flagint = myMovieFacade.deleteMovieListLink(movieListId, movieId);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 修改影单中电影的属性
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String updateMovieListAttribute(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long id = 0;
		String name = "";
		String remark = "";
		String friendName = "";
		String address = "";
		List<String> tags = new ArrayList<String>();
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			friendName = (String) dataq.get("friendName");
			id = Long.valueOf(dataq.get("id").toString());
			address = (String) dataq.get("address");
			remark = (String) dataq.get("summary");
			tags = (List<String>) dataq.get("tags");
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		
		MvListLink mvListLink = myMovieFacade.updateMvListLinkRemark(friendName, address, CheckParams.putListToString(tags), id, remark);//(friendName, address, CheckParams.putListToString(tags), id);
		flagint = mvListLink.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			//将用户修改的标签放入到缓存中
			resourceManager.setUserTagToJedis(uid, tags);
			
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 显示影单中电影的属性
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewMovieRemark(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long id = 0;
		String description = "";
		String friendName = "";
		String address = "";
		List<String> tags = new ArrayList<String>();
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		Map<String, Object> remark = new HashMap<String, Object>();
		
		MvListLink mvListLink = myMovieFacade.findMovieLinkIsExistById(id);
		
		remark.put("id", id);
		remark.put("friendName", mvListLink.getFriendInfo());
		remark.put("description", mvListLink.getDescription());
		remark.put("address", mvListLink.getAddress());
		remark.put("tags", CheckParams.putStringToList(mvListLink.getTags()));
		
		flagint = mvListLink.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("remarkInfo", remark);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 修改影单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String updateMovieList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long id = 0;
		String name = "";
		List reason = new ArrayList();
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			name = (String) dataq.get("name");
			reason = (List) dataq.get("reason");
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
//		flagint = myMovieFacade.updateMovieList(id, name, WebUtils.putDataToHTML5(reason));
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 推影单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String publishMovieList(String reqs,long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		ActPublish actPublish = actFacade.addOnePublishInfo(uid, id, CommentUtils.TYPE_MOVIELIST);
		flagint = actPublish.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flagint = myMovieFacade.publishMovieList(id);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将生成的推送影单放入到缓存中
			resourceManager.setResourceToJedis(actPublish, uid,uid,0l);
			
			//记录用户推送影单数量
			try {
				userStatisticsFacade.updateMvlistCount(uid);
			} catch (Exception e) {
				LOG.error("用户推送影单数量记录失败:"+e.getMessage(), e.fillInStackTrace());
			}
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 将硬单转为手机端可读类
	 * @param list
	 * @param type
	 * @return
	 */
	public List<MovieListInfo> getResponseList(List<MovieList> reqList ,Long uid,  List<MovieListInfo> resList){
		MovieListInfo movieListInfo = null;
		if(reqList.size()>0){
			MovieList object = reqList.get(0);
			if(object.getId() != UNID){
				flagint = ResultUtils.SUCCESS;
				for (MovieList obj : reqList) {
					
					movieListInfo = movieUtils.putMVListToMovieList(obj, myMovieFacade, mvFacade,ucenterFacade,actFacade,myBkFacade,uid);
					if(movieListInfo.getId() != 0){
						resList.add(movieListInfo);
					}
				}
			}else{
				flagint = ResultUtils.DATAISNULL;
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	
	
	public List<MovieListInfo> getMyMovieListToListInfos(List<MovieList> reqList ,Long uid, List<MovieListInfo> resList){
		if(null!=reqList&&reqList.size()>0){
			UserEntity userEntity = new UserEntity();
			if(ucenterFacade != null){
				UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
				userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);
			}
			Iterator<MovieList> movieListIt = reqList.iterator();
			flagint = ResultUtils.SUCCESS;
			while(movieListIt.hasNext()){
				MovieList movieList = movieListIt.next();
				//新方法
				MovieListInfo movieListInfo = new MovieListInfo();
				
				movieListInfo.setId(movieList.getId());
				movieListInfo.setName(movieList.getFilmListName());
				movieListInfo.setReason(movieList.getReason());
				movieListInfo.setIsDefault(movieList.getType());
				movieListInfo.setFirstMoviePic(movieList.getCover());
				movieListInfo.setTags(movieList.getTag());
				
				Map<String, Object> map = myMovieFacade.findMovieLinkCount(movieList.getId());
				int count = (Integer) map.get("count");
				movieListInfo.setSize(count);
				
				
				//movieListInfo = fileUtils.putBookListToBookListInfo(BookList, actFacade, getResourceInfoFacade, bkFacade, ucenterFacade, myBkFacade, netBookFacade, uid);
						//putObjToBookListInfo(obj,actFacade,getResourceInfoFacade,bkFacade,ucenterFacade,myBkFacade,netBookFacade,uid);
				if(movieListInfo.getId() != 0){
					movieListInfo.setUserEntity(userEntity);
					resList.add(movieListInfo);
				}
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	
	/**
	 * 将硬单转为手机端可读类
	 * @param list
	 * @param type
	 * @return
	 */
	public List<MovieListInfo> getMovieList(List<ActCollect> reqList , Long uid, List<MovieListInfo> resList){
		MovieListInfo movieListInfo = null;
		if(reqList.size()>0){
			ActCollect object = reqList.get(0);
			if(object.getId() != UNID){
				flagint = ResultUtils.SUCCESS;
				for (ActCollect obj : reqList) {
					
					movieListInfo = actUtils.putCollectMovieListToInfo(obj, ucenterFacade, actFacade, myMovieFacade, mvFacade,myBkFacade,uid);
					if(movieListInfo.getId() != 0){
						resList.add(movieListInfo);
					}
				}
			}else{
				flagint = ResultUtils.DATAISNULL;
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	/**
	 * 将影单内电影目录转换为可读类
	 * @param list
	 * @param type
	 * @return
	 */
	public List<MovieInfo> getMovieInfoResponseList(List reqList , Long uid,  List<MovieInfo> resList){
		MovieInfo movieInfo = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (Object obj : reqList) {
				String objName = obj.getClass().getName();
				if(MvListLink.class.getName().equals(objName)){
					movieInfo = movieUtils.putMVLinkToMovieInfo(obj, uid, mvCommentFacade, mvFacade);
				}
				if( movieInfo != null && movieInfo.getId() != 0){
					resList.add(movieInfo);
				}
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			Object obj = reqList.get(0);
			String objName = obj.getClass().getName();
			long id = 0;
			if(MvListLink.class.getName().equals(objName)){
				MvListLink object = (MvListLink) obj;
				id = object.getId();
			}
			
			if(id != 0){
				movieInfo = movieUtils.putMVLinkToMovieInfo(obj, uid, mvCommentFacade, mvFacade);
			}
			resList.add(movieInfo);
		}
		return resList;
	}
	/**
	 * 将影单内电影目录转换为可读类{xin}
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getNewMovieInfoResponseList(List<MvListLink> reqList , Long uid,  List<ResourceInfo> resList,int type,MovieList movieList){
		ResourceInfo ri = null;
		
		if(reqList.size()>0){
			if(reqList.get(0).getId() != 0){
				flagint = ResultUtils.SUCCESS;
				for (MvListLink mv : reqList) {
					/*if(CommentUtils.type_list2 == type){
						ri = movieUtils.putMVLinkToResource(mv, uid, ucenterFacade, actFacade, mvCommentFacade, mvFacade, myMovieFacade);
						MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(mv.getMovieId());
						ri.setScore(String.valueOf(mvAvgMark.getExpertsAvgMark()));
						resList.add(ri);
					}else{*/
//						UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, movieList.getUid());
//						//long i= mv.getMovieId();
//						List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByType((int)movieList.getUid(), userAllInfo.getLevel()+"", mv.getId());
//						if(mvComments != null && mvComments.size()>0){
//							ri = movieUtils.putMVCommentToResource(mvComments.get(0), ucenterFacade, actFacade, mvFacade, myMovieFacade, uid);
//							resList.add(ri);
//						}else{
							ri = movieUtils.putMVLinkToResource(mv, uid, ucenterFacade, actFacade, mvCommentFacade, mvFacade, myMovieFacade);
//							MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(mv.getMovieId());
//							ri.setScore(String.valueOf(mvAvgMark.getExpertsAvgMark()));
							if(ri != null && ri.getRid() != 0){
								resList.add(ri);
							}
//						}
					//}
				}
			}
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		
		return resList;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}

	public void setOperationMemcachedClient(MemcachedClient operationMemcachedClient) {
		this.operationMemcachedClient = operationMemcachedClient;
	}

	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
}
