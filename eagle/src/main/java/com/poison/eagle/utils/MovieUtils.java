package com.poison.eagle.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.MovieListInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.MovieManager;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.paycenter.model.RewardStatistical;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.Tag;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.MvStillsFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MovieStills;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class MovieUtils {
	private static final  Log LOG = LogFactory.getLog(MovieUtils.class);

	/**
	 * 简介开始标签
	 */
	private static final String DESCRIBE_START = "<div style=\"width:100%;font-size:13px;color:#616170;line-height:18px;\">";

	/**
	 * 图片开始标签
	 */
	private static final String DESCRIBE_IMG_START = "<img src='";

	/**
	 * 图片结束标签
	 */
	private static final String DESCRIBE_IMG_END = "' width=\"90px\" height=\"130px\" style=\"float:left;margin-right:10px;\">";

	/**
	 * 文字开始标签
	 */
	private static final String DESCRIBE_CONTENT_START = "<p style=\"margin:0;\"><b style=\"color:rgb(130,130,130);font-size:13px;\">电影简介:</b>";

	/**
	 * 文字结束标签
	 */
	private static final String DESCRIBE_CONTENT_END = "</p></div>";
	private static final String FGF = ",";
	private static MovieUtils movieUtils;
	private final int TRUE = 0;
	private final int FALSE = 1;
	private FileUtils fileUtils = FileUtils.getInstance();
	private PaycenterFacade paycenterFacade;
	private int TIMEOUT = 5000;
	private  ObjectMapper objectMapper;
	private String HTML_MOVIE_NAME = "<span property=\"v:itemreviewed\">四大名捕大结局</span>";//电影名
	
	private MovieUtils(){}

	public static MovieUtils getInstance(){
		if(movieUtils == null){
			return new MovieUtils();
		}else{
			return movieUtils;
		}
	}

	public static void main(String[] args) {
//		MovieUtils movieUtils = MovieUtils.getInstance();
//		MvInfo mvinfo = new MvInfo();
//		mvinfo.setId(2l);
//		mvinfo.setName("name");
//		mvinfo.setMoviePic("18646//application/image/movie/18646/p1864635725.jpg");
//		mvinfo.setScore("score");
//		mvinfo.setDirector("director");
//		mvinfo.setActor("actor");
//		mvinfo.setTags("tags");
//		mvinfo.setLanguage("language");
//		mvinfo.setProCountries("proeountries");
//		mvinfo.setReleaseDate("releaseDate");
//		mvinfo.setFilmTime("filmtime");
//		mvinfo.setAlias("alias");
//		mvinfo.setImdbLink("http://www.imdb.com/title/tt0460649");
//		mvinfo.setDescription("description");
//
//
//		MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvinfo);
//		System.out.println(movieInfo.toString());
		try {
			JSONObject json = new JSONObject("{\"list\":[{\"sss\":\"sss\",\"wefa\":\"sss\"},{\"sss\":\"sss\",\"wefa\":\"sss\"}]}");
			JSONArray list = json.getJSONArray("list");
//			System.out.println(list.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将根据用户标签查询的运营影单json转换为影单列表
	 * @param json
	 * @return
	 */
	public List<MovieList> putJsonListToMovieLists(List<Map<String, Object>> json){
		List<MovieList> movieLists = new ArrayList<MovieList>();

		Iterator<Map<String, Object>> iter = json.iterator();
		while(iter.hasNext()){
			Map<String, Object> ml = iter.next();
			MovieList movieList = new MovieList();
			movieList = putJsonToMovieList(ml);
			if(movieList != null && movieList.getId() != 0){
				movieLists.add(movieList);
			}
		}

		return movieLists;
	}
	
//	/**
//	 * 将根据用户标签查询的运营影评json转换为MvComment列表
//	 * @param json
//	 * @return
//	 */
//	public List<MvComment> putJsonListToMvCommentList(List<Map<String, Object>> json){
//		List<MvComment> mvComments = new ArrayList<MvComment>();
//		
//		Iterator<Map<String, Object>> iter = json.iterator();
//		while(iter.hasNext()){
//			Map<String, Object> mv = iter.next();
//			MvComment mvComment = new MvComment();
//			mvComment = putJsonToMvComment(mv);
//			mvComments.add(mvComment);
//		}
//		
//		return mvComments;
//	}
	
//	/**
//	 * 将根据用户标签查询的运营影评json转换为MvComment
//	 * @param json
//	 * @return
//	 */
//	public MvComment putJsonToMvComment(Map<String, Object> json){
//		MvComment mvComment = new MvComment();
//		
//		Long id = Long.valueOf(json.get("id").toString());
//		Long uid = Long.valueOf(json.get("user_id").toString());
//		Long mid = Long.valueOf(json.get("movie_id").toString());
//		String score = (String) json.get("score");
//		String description = (String) json.get("description");
//		String comment = (String) json.get("comment");
//		
//		mvComment.setId(id);
//		mvComment.setUserId(uid);
//		mvComment.setMovieId(mid);
//		mvComment.setScore(score);
//		mvComment.setDescription(description);
//		mvComment.setContent(comment);
//		
//		return mvComment;
//	}

	/**
	 * 将根据用户标签查询的运营影单json转换为影单
	 * @param json
	 * @return
	 */
	public MovieList putJsonToMovieList(Map<String, Object> json){
		MovieList movieList = new MovieList();
		try {
			if(json.containsKey("id")){
				Long id = Long.valueOf(json.get("id").toString());
				movieList.setId(id);
			}
			if(json.containsKey("u_id")){
				Long uid = Long.valueOf(json.get("u_id").toString());
				movieList.setUid(uid);
			}
			if(json.containsKey("cover")){

				String cover = (String) json.get("cover");
				movieList.setCover(cover);
			}
			if(json.containsKey("filmlist_name")){
				String filmlist_name = (String) json.get("filmlist_name");
				movieList.setFilmListName(filmlist_name);
			}
			if(json.containsKey("reason")){
				String reason = (String) json.get("reason");
				movieList.setReason(reason);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			movieList = new MovieList();
		}

		return movieList;
	}
	
	/**
	 * 将根据用户标签查询的运营影单json转换为电影列表
	 * @param json
	 * @return
	 */
	public List<MvComment> putJsonListToMvComments(List<Map<String, Object>> json){
		List<MvComment> mvComments = new ArrayList<MvComment>();

		Iterator<Map<String, Object>> iter = json.iterator();
		while(iter.hasNext()){
			Map<String, Object> ml = iter.next();
			MvComment mvComment = new MvComment();
			mvComment = putJsonToMvComment(ml);
			mvComments.add(mvComment);
		}

		return mvComments;
	}
	
	/**
	 * 将根据用户标签查询的运营影单json转换为影评
	 * @param json
	 * @return
	 */
	public MvComment putJsonToMvComment(Map<String, Object> json){
		MvComment mvComment = new MvComment();// = new MovieList();

		Long id = Long.valueOf(json.get("id").toString());
		Long uid = Long.valueOf(json.get("user_id").toString());
		Long movieId = Long.valueOf(json.get("movie_id").toString());
		String comment = (String) json.get("comment");
		if(json.containsKey("score")){

			String score = (String) json.get("score");
			mvComment.setScore(score);
		}
		if(json.containsKey("type")){
			String type = (String) json.get("type");
			mvComment.setType(type);
		}

		mvComment.setId(id);
		mvComment.setUserId(uid);
		mvComment.setMovieId(movieId);
		mvComment.setContent(comment);

		return mvComment;
	}
	
	/**
	 * 将豆瓣获取的json串转换为保存类
	 * @param data
	 */
	public MvInfo putJsonToMovie(Map<String, Object> data){
		String movieUrl =  (String) data.get("alt");//地址
		String moviePic = (String) data.get("image");//海报
		String name = (String) data.get("title");//名称
		String score = (String) data.get("average");//平均分
		List<String> directors =  (List<String>) data.get("director");//导演列表
		StringBuffer director = new StringBuffer();
		for (int i = 0; i < directors.size(); i++) {
			director.append(directors.get(i));
			if(i != directors.size()-1 && !"".equals(directors.get(i))){
				director.append(FGF);
			}
		}
		List<String> casts = (List<String>) data.get("cast");//演员列表
		StringBuffer cast = new StringBuffer();
		for (int i = 0; i < casts.size(); i++) {
			cast.append(casts.get(i));
			if(i != casts.size()-1 && !"".equals(casts.get(i))){
				cast.append(FGF);
			}
		}
		List<String> genres = (List<String>) data.get("tags");//标签
		StringBuffer genre = new StringBuffer();
		try{
			for (int i = 0; i < genres.size(); i++) {
				genre.append(genres.get(i));
				if(i != genres.size()-1 && !"".equals(genres.get(i))){
					genre.append(FGF);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String summary = (String) data.get("summary");//内容概要
		String akas = (String) data.get("alt_title");//别名
		List<String> countries = (List<String>) data.get("country");//制作国家
		StringBuffer countrie = new StringBuffer();
		for (int i = 0; i < countries.size(); i++) {
			countrie.append(countries.get(i));
			if(i != countries.size()-1 && !"".equals(countries.get(i))){
				countrie.append(FGF);
			}
		}
		List<String> languages = (List<String>) data.get("language");//语言
		StringBuffer language = new StringBuffer();
		for (int i = 0; i < languages.size(); i++) {
			language.append(languages.get(i));
			if(i != languages.size()-1 && !"".equals(languages.get(i))){
				language.append(FGF);
			}
		}
		List<String> pubdates = (List<String>) data.get("pubdate");//上映日期
		StringBuffer pubdate = new StringBuffer();
		for (int i = 0; i < pubdates.size(); i++) {
			pubdate.append(pubdates.get(i));
			if(i != pubdates.size()-1 && !"".equals(pubdates.get(i))){
				pubdate.append(FGF);
			}
		}
		List<String> writers = (List<String>) data.get("writer");//编剧
		StringBuffer writer = new StringBuffer();
		for (int i = 0; i < writers.size(); i++) {
			writer.append(writers.get(i));
			if(i != writers.size()-1 && !"".equals(writers.get(i))){
				writer.append(FGF);
			}
		}
		List<String> movie_durations = (List<String>) data.get("movie_duration");//时长
		StringBuffer movie_duration = new StringBuffer();
		for (int i = 0; i < movie_durations.size(); i++) {
			movie_duration.append(movie_durations.get(i));
			if(i != movie_durations.size()-1 && !"".equals(movie_durations.get(i))){
				movie_duration.append(FGF);
			}
		}
		List<String> movie_types = (List<String>) data.get("movie_type");//类型
		StringBuffer movie_type = new StringBuffer();
		for (int i = 0; i < movie_types.size(); i++) {
			movie_type.append(movie_types.get(i));
			if(i != movie_types.size()-1 && !"".equals(movie_types.get(i))){
				movie_type.append(FGF);
			}
		}


		MvInfo mvInfo = new MvInfo();
		mvInfo.setMovieUrl(movieUrl);
		mvInfo.setMoviePic(moviePic);
		mvInfo.setName(name);
		mvInfo.setScore(score);
		mvInfo.setDirector(director.toString());
		mvInfo.setScreenWriter(writer.toString());
		mvInfo.setActor(cast.toString());
		mvInfo.setTags(movie_type.toString());
		mvInfo.setUserTags(genre.toString());
		mvInfo.setProCountries(countrie.toString());
		mvInfo.setLanguage(language.toString());
		mvInfo.setReleaseDate(pubdate.toString());
		mvInfo.setFilmTime(movie_duration.toString());
		mvInfo.setAlias(akas);
		mvInfo.setDescription(CheckParams.replaceN(summary));
		mvInfo.setCollTime((int) (Calendar.getInstance().getTimeInMillis()/1000));
		mvInfo.setWeekBoxOffice("");
		mvInfo.setRankingList("");
		mvInfo.setBoxOffice("");


		return mvInfo;

	}

	/**
	 * 将爬虫下来的数据转换为mvinfo
	 * @param data
	 * @return
	 */
	public MvInfo putAnalyseToMvInfo(Map<String, Object> data){
		MvInfo mvInfo = new MvInfo();


		String moviePic = (String) data.get("image");//海报
		String name = (String) data.get("title");//名称
		String score = (String) data.get("average");//平均分
		String director =  (String) data.get("director");//导演列表
		String casts = (String) data.get("cast");//演员列表
		String summary = (String) data.get("summary");//内容概要
		String akas = (String) data.get("alt_title");//别名
		String countries = (String) data.get("country");//制作国家
		String languages = (String) data.get("language");//语言
		String pubdates = (String) data.get("pubdate");//上映日期
		String writers = (String) data.get("writer");//编剧
		String movie_durations = (String) data.get("movie_duration");//时长
		String movie_types = (String) data.get("movie_type");//类型


		mvInfo.setMoviePic(moviePic);
		mvInfo.setName(name);
		mvInfo.setScore(score);
		mvInfo.setDirector(director);
		mvInfo.setScreenWriter(writers);
		mvInfo.setActor(casts);
		mvInfo.setTags(movie_types);
		mvInfo.setProCountries(countries);
		mvInfo.setLanguage(languages);
		mvInfo.setReleaseDate(pubdates);
		mvInfo.setFilmTime(movie_durations);
		mvInfo.setAlias(akas);
		mvInfo.setDescription(CheckParams.replaceN(summary));
		mvInfo.setCollTime((int) (Calendar.getInstance().getTimeInMillis()/1000));

		return mvInfo;
	}

	/**
	 * 将影评类转换为的resoureinfo实例类
	 * @param obj
	 * @param ucenterFacade
	 * @param mvFacade
	 * @return
	 */
	public  ResourceInfo putMVCommentToResource(Object obj , UcenterFacade ucenterFacade ,MvFacade mvFacade){
		ResourceInfo ri = new ResourceInfo();
		if(obj == null){
			return ri;
		}
		//调用本类公用方法
		ri = fileUtils.putObjectToResource(obj, ucenterFacade);
		String objName = obj.getClass().getName();
		if(mvFacade != null){
			if(MvComment.class.getName().equals(objName)){
				MvComment object = (MvComment) obj;
				MvInfo mvInfo = mvFacade.queryById(object.getMovieId());
				MovieInfo movieInfo = putMVToMovieInfo(mvInfo,TRUE);
				ri.setMovieInfo(movieInfo);
			}
		}


		return ri;
	}
	
	/**
	 * 将影评类转换为的resoureinfo实例类
	 * @param mvComment
	 * @param ucenterFacade
	 * @param mvFacade
	 * @return
	 */
	public  ResourceInfo putMVCommentToResource(MvComment mvComment , UcenterFacade ucenterFacade ,MvFacade mvFacade,int isList){
		ResourceInfo ri = new ResourceInfo();
		if(mvComment == null){
			return ri;
		}
		//调用本类公用方法
		ri = fileUtils.putObjectToResource(mvComment, ucenterFacade);
		if(mvFacade != null){
			MvInfo mvInfo = mvFacade.queryById(mvComment.getMovieId());
			MovieInfo movieInfo = putMVToMovieInfo(mvInfo,isList);
			ri.setMovieInfo(movieInfo);
		}

		return ri;
	}
	
	/**
	 * 将影评类转换为的resoureinfo实例类
	 * @param obj
	 * @param ucenterFacade
	 * @param mvFacade
	 * @return
	 */
	public  ResourceInfo putMVCommentToResource(Object obj , UcenterFacade ucenterFacade ,ActFacade actFacade ,MvFacade mvFacade,MyMovieFacade myMovieFacade,long uid){
		ResourceInfo ri = new ResourceInfo();
		if(obj == null){
			return ri;
		}
		//调用本类公用方法
		ri = fileUtils.putObjectToResource(obj, ucenterFacade,actFacade);
		String objName = obj.getClass().getName();
		if(mvFacade != null){
			if(MvComment.class.getName().equals(objName)){
				MvComment object = (MvComment) obj;
				MvInfo mvInfo = mvFacade.queryById(object.getMovieId());
				MovieInfo movieInfo = putMVToMovieInfo(mvInfo,TRUE);

				//是否在我影单中
				MvListLink mvListLink = myMovieFacade.findUserIsCollectMovie(uid, movieInfo.getId());
				if(mvListLink.getId() != TRUE){
					movieInfo.setInList(TRUE);
				}else{
					movieInfo.setInList(FALSE);
				}
				ri.setMovieInfo(movieInfo);
				//需要判断长影评是否有封面，没有封面则查询电影的封面
				if(ri.getImageUrl()==null || ri.getImageUrl().trim().length()==0){
					if(mvInfo!=null){
						ri.setImageUrl(mvInfo.getMoviePic());
					}
				}
			}
		}

		return ri;
	}
	
//	/**
//	 * 
//	 * @param data
//	 * @return
//	 */
//	public static Map<String, String> putNameListToString(List<Map<String, Object>> data){
//		 for (Map<String, Object> map : data) {
//			Map<String, String> image = (Map<String, String>) map.get("avatars");
//			String name = (String) map.get("name");
//			
//		}
//	}
	
	/**
	 * 将电影讨论转换为资源类
	 * @param obj
	 * @param ucenterFacade
	 * @return
	 */
	public  ResourceInfo putMovieTalkToResource(Object obj , UcenterFacade ucenterFacade ){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		ri = fileUtils.putObjectToResource(obj, ucenterFacade);
		return ri;
	}

	/**
	 * 将推到首页的影单转换为资源类
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param myMovieFacade
	 * @param mvFacade
	 * @return
	 */
	public ResourceInfo putActPublishMovieListToResource(Object obj,UcenterFacade ucenterFacade,ActFacade actFacade,MyMovieFacade myMovieFacade,MvFacade mvFacade,Long uid){
		ResourceInfo ri  =new ResourceInfo();
		ActPublish object = (ActPublish) obj;
		Long movieListId = object.getResourceId();

		//将movielist转换为资源类
		MovieList movieList = myMovieFacade.findMovieListById(movieListId);
		//已废弃
//		ri = putMovieListToResource(movieList, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
		ri = fileUtils.putObjectToResource(movieList, ucenterFacade, actFacade);

		ri.setRid(object.getId());
		ri.setBtime(object.getPublishDate());

		return ri;
	}
	
	/**
	 * 将movielist转换为资源类(已废弃)
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param myMovieFacade
	 * @param mvFacade
	 * @return
	 */
	public ResourceInfo putMovieListToResource(Object obj,UcenterFacade ucenterFacade,ActFacade actFacade,MyMovieFacade myMovieFacade,MvFacade mvFacade,long uid){
		ResourceInfo ri = new ResourceInfo();
		ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade);

		//在影单中添加第一本书
		List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(ri.getMovieListId(), null,null);
		if(mvListLinks.size()>0 && mvListLinks.get(0).getId() != 0){
			MvInfo mvInfo = mvFacade.queryById(mvListLinks.get(0).getMovieId());
			MovieInfo movieInfo = putMVToMovieInfo(mvInfo,TRUE);

			MvListLink mvListLink = myMovieFacade.findUserIsCollectMovie(uid, movieInfo.getId());
			if(mvListLink.getId() != TRUE){
				movieInfo.setInList(TRUE);
			}

			ri.setMovieInfo(movieInfo);
			String imageUrl = ri.getImageUrl();
			if(imageUrl == null || "".equals(imageUrl)){
				ri.setImageUrl(movieInfo.getMoviePic());
			}
		}
		return ri;
	}
	
	public  ObjectMapper getObjectMapper() {
		if(objectMapper == null){
			objectMapper  = new ObjectMapper();

			objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
//			objectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
//			objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
			return objectMapper;
		}else{
			return objectMapper;
		}
	}

	public List<Header> getHeaders() {
		//现成沉睡1秒
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error("沉睡出错误");
		}
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2"));
		headers.add(new Header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		headers.add(new Header("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"));
		headers.add(new Header("Host","http://www.douban.com"));
		headers.add(new Header("Referer", "http://www.douban.com/"));
		return headers;
	}
	
	/**
	 * httpclient读取页面信息
	 * @param headers
	 * @param encoding
	 * @param tryCount
	 * @return
	 */
	public String getDate(List<Header> headers,String path,String encoding,int tryCount) {
		HttpClient httpClient = null;
		InputStream is = null;
		GetMethod httpget = null;
		String returnStr = "";
		try {
			// 创建默认的httpClient实例.
			httpClient = new HttpClient();
			// 创建httpget
			httpget = new GetMethod(path);
			// 增加请求头
			httpClient.getHostConfiguration().getParams().setParameter(
					"http.default-headers", headers);
//			httpClient.getHostConfiguration().setProxy("101.228.195.49",9999);
			//httpClient.getHostConfiguration().setProxy("116.236.250.58",8080);

			//设置请求超时
			httpClient.getParams().setSoTimeout(TIMEOUT);
			// 执行get请求.
			int statusCode = httpClient.executeMethod(httpget);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("Method failed: " + httpget.getStatusLine());
			}
			// 获取响应实体
			// 返回的太大放弃
			// byte[] responseBody = httpget.getResponseBody();
			is = httpget.getResponseBodyAsStream();
			//log.info("is处理前bytes长度:" + is.available());
				returnStr = IOUtils.toString(is,encoding);

			//log.info("is处理后字符串长度:" + returnStr.length());
		} catch (Exception e) {
			LOG.error("获取数据流异常:" + e.getMessage());
			tryCount--;
			LOG.error("重试次数:" + tryCount);
			if(tryCount > 0){
				return this.getDate(headers,path, encoding,tryCount);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("关闭数据流异常:" + e.getMessage());

				}
			}
			if (httpget != null) {
				httpget.releaseConnection();
				// 关闭连接
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
		return returnStr;
	}

	/**
	 * 将mvinfo转换为movieInfo
	 * @param mvInfo
	 * @return
	 */
	public MovieInfo putMVToMovieInfo(MvInfo mvInfo,int isList){
		MovieInfo movieInfo = new MovieInfo();

		movieInfo.setId(mvInfo.getId());
		movieInfo.setName(mvInfo.getName());
		movieInfo.setMoviePic(CheckParams.matcherMoviePic(mvInfo.getMoviePic()));
		movieInfo.setScore(mvInfo.getScore());
		movieInfo.setDirector(mvInfo.getDirector());
		//演员
		movieInfo.setActor(mvInfo.getActor());
		movieInfo.setYear(mvInfo.getReleaseDate());
		if (!StringUtils.isEmpty(mvInfo.getMoviePic2())) {
			movieInfo.setMovieHPic(mvInfo.getMoviePic2());
		} else {
			if (!StringUtils.isEmpty(mvInfo.getExtendField1()) && !StringUtils.equals(mvInfo.getExtendField1().trim(), "0")) {
				movieInfo.setMovieHPic(mvInfo.getExtendField1());
			}
		}
		if (StringUtils.isEmpty(movieInfo.getMovieHPic())) {
			movieInfo.setMovieHPic(movieInfo.getMoviePic());
		}
		//System.out.println("是否在影单中"+isList);
		if(isList == FALSE){
			if (StringUtils.isEmpty(mvInfo.getBoxOffice())) {
				movieInfo.setTotalTicket("暂无");
			}else{
				movieInfo.setTotalTicket(mvInfo.getBoxOffice());
			}
			movieInfo.setWeeklyTicket(mvInfo.getWeekBoxOffice());
			movieInfo.setRank(mvInfo.getRankingList());
			movieInfo.setUrl(mvInfo.getMovieUrl());
			/*String name = movieInfo.getName();
		String director = WebUtils.takeOutPinYin(name, mvInfo.getDirector());*/
			//String actor = WebUtils.takeOutPinYin(name, mvInfo.getActor());
			movieInfo.setActor(mvInfo.getActor());
			movieInfo.setTags(mvInfo.getTags());
			movieInfo.setLanguage(mvInfo.getLanguage());
//			String writer = WebUtils.takeOutPinYin(name, mvInfo.getScreenWriter());
			movieInfo.setWriter( mvInfo.getScreenWriter());
			movieInfo.setCountries(mvInfo.getProCountries());
			movieInfo.setFilmLength(mvInfo.getFilmTime());
			movieInfo.setAlias(mvInfo.getAlias());
			movieInfo.setImdb(mvInfo.getImdbLink());
//			movieInfo.setIsDB(TRUE);
			movieInfo.setDescribe(generateNewmMovieDescribe(mvInfo.getMoviePic(),mvInfo.getDescription()));
		}

		return movieInfo;
	}

	/**
	 * 将mvinfo转换为movieInfo(带有自己评论)
	 * @param mvInfo
	 * @return
	 */
	public MovieInfo putMVToMovieInfo(MvInfo mvInfo , Long uid , MvCommentFacade mvCommentFacade,MvStillsFacade mvStillsFacade,int isList){
		MovieInfo movieInfo = new MovieInfo();

		//获得电影实体类
		movieInfo  = putMVToMovieInfo(mvInfo,isList);
		//System.out.println("是否在影单中"+isList);
		if(isList == 1){

			//剧照和预告片
			if(mvStillsFacade != null){
				MovieStills movieStills = mvStillsFacade.findMvOlineStillsByBkId(mvInfo.getId());
				try {
					String photos = movieStills.getMovieStills();
					if(photos != null && !"".equals(photos)){
						photos = CheckParams.replaceKG(photos);
						photos = CheckParams.matcherMoviePhoto(photos);
						List list = getObjectMapper().readValue(photos, new TypeReference<List>(){});
						movieInfo.setPhotos(list);
					}
					String trailers = movieStills.getOther();
					if(trailers != null && !"".equals(trailers)){
						trailers = CheckParams.replaceKG(trailers);
						List list = getObjectMapper().readValue(trailers, new TypeReference<List>(){});
						movieInfo.setTrailers(list);
					}
				} catch (Exception  e) {
					e.printStackTrace();
				}
			}


			if(mvCommentFacade == null || uid == null || uid == 0){
				return movieInfo;
			}
			List<MvComment> mvComments = mvCommentFacade.findUserMvComment(uid, (int) movieInfo.getId());
			if(mvComments.size() > 0 && mvComments.get(0).getMovieId() != TRUE){
				ResourceInfo resourceInfo = putMVCommentToResource(mvComments.get(0), null, null);
				//将我的评论放到电影中
				movieInfo.setResourceInfo(resourceInfo);
			}

			//将电影的评论数和平均分放进去(专家，正常人)
			MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(movieInfo.getId());
			if(mvAvgMark.getFlag() == ResultUtils.SUCCESS || mvAvgMark.getFlag() == 0){
				movieInfo.setScore(String.valueOf(mvAvgMark.getMvAvgMark()));
				movieInfo.setcNum(mvAvgMark.getMvTotalNum());
				movieInfo.setTalentScore(String.valueOf(mvAvgMark.getExpertsAvgMark()));
				movieInfo.setTalentCommentNum(mvAvgMark.getExpertsTotalNum());
			}
		}
//		//评论数
//		int cNum = mvCommentFacade.findMvCommentCount(movieInfo.getId());
//		if(cNum != ResultUtils.SUCCESS){
//			movieInfo.setcNum(cNum);
//		}

		return movieInfo;
	}

	/**
	 * 将mvinfo转换为movieInfo(带有自己评论)
	 * @param mvInfo
	 * @return
	 */
	public MovieInfo putMVToMovieInfo(MvInfo mvInfo , Long uid , MvCommentFacade mvCommentFacade,MvStillsFacade mvStillsFacade,int isList,long stageid){
		MovieInfo movieInfo = new MovieInfo();

		//获得电影实体类
		movieInfo  = putMVToMovieInfo(mvInfo,isList);
		//System.out.println("是否在影单中"+isList);
		if(isList == 1){

			//剧照和预告片
			if(mvStillsFacade != null){
				MovieStills movieStills = mvStillsFacade.findMvOlineStillsByBkId(mvInfo.getId());
				try {
					String photos = movieStills.getMovieStills();
					if(photos != null && !"".equals(photos)){
						photos = CheckParams.replaceKG(photos);
						photos = CheckParams.matcherMoviePhoto(photos);
						List list = getObjectMapper().readValue(photos, new TypeReference<List>(){});
						movieInfo.setPhotos(list);
					}
					String trailers = movieStills.getOther();
					if(trailers != null && !"".equals(trailers)){
						trailers = CheckParams.replaceKG(trailers);
						List list = getObjectMapper().readValue(trailers, new TypeReference<List>(){});
						movieInfo.setTrailers(list);
					}
				} catch (Exception  e) {
					e.printStackTrace();
				}
			}


			if(mvCommentFacade == null || uid == null || uid == 0){
				return movieInfo;
			}
			List<MvComment> mvComments = mvCommentFacade.findUserMvComment(uid, (int) movieInfo.getId(),stageid);
			if(mvComments.size() > 0 && mvComments.get(0).getMovieId() != TRUE){
				ResourceInfo resourceInfo = putMVCommentToResource(mvComments.get(0), null, null);
				//将我的评论放到电影中
				movieInfo.setResourceInfo(resourceInfo);
			}

			//将电影的评论数和平均分放进去(专家，正常人)
			MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(movieInfo.getId());
			if(mvAvgMark.getFlag() == ResultUtils.SUCCESS || mvAvgMark.getFlag() == 0){
				movieInfo.setScore(String.valueOf(mvAvgMark.getMvAvgMark()));
				movieInfo.setcNum(mvAvgMark.getMvTotalNum());
				movieInfo.setTalentScore(String.valueOf(mvAvgMark.getExpertsAvgMark()));
				movieInfo.setTalentCommentNum(mvAvgMark.getExpertsTotalNum());
			}
		}
//		//评论数
//		int cNum = mvCommentFacade.findMvCommentCount(movieInfo.getId());
//		if(cNum != ResultUtils.SUCCESS){
//			movieInfo.setcNum(cNum);
//		}

		return movieInfo;
	}

	/**
	 * 将mvinfo转换为movieInfo(带有自己评论)
	 * @param mvInfo
	 * @return
	 */
	public MovieInfo putMVLinkToMovieInfo(Object obj , Long uid , MvCommentFacade mvCommentFacade , MvFacade mvFacade){
		MovieInfo movieInfo = new MovieInfo();
		if(obj == null){
			return movieInfo;
		}
		MvListLink object = (MvListLink) obj;

		MvInfo mvInfo = mvFacade.queryById(object.getMovieId());

		//调用本地方法
		movieInfo = putMVToMovieInfo(mvInfo, uid, mvCommentFacade,null,TRUE);
//		movieInfo.setForeignKeyId(object.getId());
		return movieInfo;
	}

	/**
	 * 将mvLink转换为资源类
	 * @param mvInfo
	 * @return
	 */
	public ResourceInfo putMVLinkToResource(MvListLink mvListLink , Long uid ,UcenterFacade ucenterFacade,ActFacade actFacade, MvCommentFacade mvCommentFacade , MvFacade mvFacade,MyMovieFacade myMovieFacade){
		ResourceInfo ri = new ResourceInfo();
		MovieInfo movieInfo = new MovieInfo();
		if(mvListLink == null){
			return ri;
		}
		MvInfo mvInfo = mvFacade.queryById(mvListLink.getMovieId());
		if(mvInfo == null || mvInfo.getId() == 0){
			return ri;
		}
		movieInfo = putMVToMovieInfo(mvInfo, TRUE);

		MovieList movieList = myMovieFacade.findMovieListById(mvListLink.getFilmListId());

		List<MvComment> mvComments = mvCommentFacade.findUserMvComment(movieList.getUid(), (int)mvListLink.getMovieId());
		if(mvComments.size()>0){
			ri = putMVCommentToResource(mvComments.get(0), ucenterFacade, actFacade, mvFacade, myMovieFacade, uid);
		}else{
//			if(mvInfo != null && mvInfo.getId() != 0){
				//调用本地方法
				//已废弃
//			movieInfo = putMVToMovieInfo(mvInfo, movieList.getUid(), mvCommentFacade,null,TRUE);
				ri = fileUtils.putObjectToResource(movieInfo, ucenterFacade, actFacade);
				ri.setType(CommentUtils.TYPE_MOVIE_COMMENT);
//			ri.setScore(movieInfo.getScore());
				MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(movieInfo.getId());
				ri.setScore(String.valueOf(mvAvgMark.getExpertsAvgMark()));
				if("0".equals(ri.getScore()) || "0.0".equals(ri.getScore())){
					ri.setScore(String.valueOf(mvAvgMark.getMvAvgMark()));
				}
				ri.setMovieInfo(movieInfo);
				ri.setContList(WebUtils.putHTMLToData(mvListLink.getDescription()));
//			}else{
//				ri = new ResourceInfo();
//			}
		}

		if("0".equals(ri.getScore()) || "0.0".equals(ri.getScore())){
			ri.setScore(movieInfo.getScore());
		}
//		ri.setTitle(mvListLink.get);
		ri.setLinkId(mvListLink.getId());
		return ri;
	}

	/**
	 * 将表中的电影转化为资源类
	 * @param mvInfo
	 * @return
	 */
	public ResourceInfo putMovieInfoToResource(MovieInfo movieInfo){
		ResourceInfo ri = new ResourceInfo();

		if(movieInfo == null || movieInfo.getId() == 0){
			return ri;
		}
//		MovieInfo movieInfo = putMVToMovieInfo(mvInfo, TRUE);
		ri.setMovieInfo(movieInfo);
		ri.setType(CommentUtils.TYPE_MOVIE);
		ri.setRid(movieInfo.getId());
		ri.setImageUrl(movieInfo.getMoviePic());
		return ri;
	}
	
	/**
	 * 将表中的电影转化为资源类
	 * @param mvInfo
	 * @return
	 */
	public ResourceInfo putMVInfoToResource(MvInfo mvInfo){
		ResourceInfo ri = new ResourceInfo();

		if(mvInfo == null || mvInfo.getId() == 0){
			return ri;
		}
		MovieInfo movieInfo = putMVToMovieInfo(mvInfo, FALSE);
		ri = putMovieInfoToResource(movieInfo);
		return ri;
	}
	
	/**
	 * 将影单转换为手机端显示的影单类型
	 * @param movieList
	 * @return
	 */
	public MovieListInfo putMVListToMovieList(MovieList movieList,MyMovieFacade myMovieFacade,MvFacade mvFacade,UcenterFacade ucenterFacade,ActFacade actFacade,MyBkFacade myBkFacade,Long uid){
		MovieListInfo movieListInfo = new MovieListInfo();
		movieListInfo.setType(CommentUtils.TYPE_MOVIELIST);
		movieListInfo.setId(movieList.getId());
		movieListInfo.setName(movieList.getFilmListName());
		movieListInfo.setReason(movieList.getReason());
		movieListInfo.setTags(movieList.getTag());

//		//带有分类的标签列表
//		List<String> tags = movieListInfo.getTags();
//		Map<String, String> tagMap = new HashMap<String, String>();
//		for (String string : tags) {
//			Tag tag = myBkFacade.findTaggroupByTagName(string, CommentUtils.TYPE_MOVIELIST);
//			String type = "";
//			if(tag == null || tag.getId() == 0){
//				type = "";
//			}else{
//				type = tag.getTagGroup();
//			}
//			try {
//				tagMap.put(string,type);
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
//		}
//		movieListInfo.setTagInfo(tagMap);


		movieListInfo.setIsDefault(movieList.getType());
		//评论数量
		int cNum= actFacade.findCommentCount(null, movieList.getId());
		movieListInfo.setcNum(cNum);
		//转发数量
		/*int rNum = actFacade.findTransmitCount(null, movieList.getId());
		movieListInfo.setrNum(rNum);*/
		//是否收藏过
		ActCollect actCollect = actFacade.findCollectIsExist(uid, movieList.getId());
		if(actCollect.getId() != 0){
			movieListInfo.setIsCollect(CheckParams.changeIntForTrueFalse(actCollect.getIsCollect()));
		}
		//是否赞过
		ActPraise actPraise = actFacade.findActPraise(uid, movieList.getId());
		if(actPraise != null && actPraise.getFlag() == ResultUtils.SUCCESS){
			movieListInfo.setIsPraise(fileUtils.changeIntForTrueFalse(actPraise.getIsPraise()));
		}
		//赞的数量
		int zNum= actFacade.findPraiseCount(null, movieListInfo.getId());
		movieListInfo.setzNum(zNum);


		if(ucenterFacade != null){
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, movieList.getUid());
			UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);
			//添加是否关注

			movieListInfo.setUserEntity(userEntity);
		}

		//影单封面
		String movieListCover = movieList.getCover();
		if(!"".equals(movieListCover)){
			movieListInfo.setFirstMoviePic(movieListCover);
		}else{
			//将影单中电影数目及首个封面
			List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(movieListInfo.getId(), null,null);
			if(mvListLinks.size() > 0){
				if(mvListLinks.get(0).getId() != 0){
					//暂时留用
					MovieInfo movieInfo1 = putMVLinkToMovieInfo(mvListLinks.get(0), null, null, mvFacade);
					movieListInfo.setFirstMoviePic(movieInfo1.getMoviePic());
					//影单中电影的数量
					/*int count = 0;
				Iterator<MvListLink> iter = mvListLinks.iterator();
				while(iter.hasNext()){
					MvListLink mvListLink = iter.next();
					MvInfo mvInfo = mvFacade.queryById(mvListLink.getMovieId());
					if(mvInfo != null && mvInfo.getId() != 0){
						count++;
					}
				}*/
				}
			}

		}

		Map<String, Object> map = myMovieFacade.findMovieLinkCount(movieList.getId());
		int count = (Integer) map.get("count");
		movieListInfo.setSize(count);

//				int listSize = mvListLinks.size();

				//将影单中的电影都添加到影单中
//				List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
//				for (MvListLink mvListLink : mvListLinks) {
//					MovieInfo movieInfo2 = putMVLinkToMovieInfo(mvListLink, null, null, mvFacade);
//					movieInfos.add(movieInfo2);
//				}
//				movieListInfo.setList(movieInfos);


				//前五个封面
//				int size = 5;
//				if(listSize<size){
//					size = listSize;
//				}
//				List<String> picUrl = new ArrayList<String>();
//				for (int i = 0; i < size; i++) {
//					MovieInfo movieInfo = putMVLinkToMovieInfo(mvListLinks.get(i), null, null, mvFacade);
//					picUrl.add(movieInfo.getMoviePic());
//				}
//				movieListInfo.setMoviePicList(picUrl);
		/*	}
		}

		if(movieList != null && !"".equals(movieList.getCover())){
			movieListInfo.setFirstMoviePic(movieList.getCover());
		}*/

		//putMoneyToMovieList(movieListInfo, paycenterFacade);
		return movieListInfo;
	}

	/**
	 * 将每条资源的得到的金钱数放入到影单类中
	 * @param ri
	 * @param uid
	 * @param paycenterFacade
	 * @return
	 */
	public void putMoneyToMovieList(MovieListInfo mi ,PaycenterFacade paycenterFacade){
		if(mi == null || mi.getId() == 0 || paycenterFacade == null){
			return;
		}
		RewardStatistical rewardStatistical = paycenterFacade.getMoneyBySourceId(mi.getId());
		if(rewardStatistical.getId() != 0){
			mi.setMoney(rewardStatistical.getTotalAmt());
		}

	}
	
	/**
	 * 生成最新的电影简介
	 * @param cover
	 * @param describe
	 * @return
	 */
	public String generateNewmMovieDescribe(String cover,String describe){
		return DESCRIBE_START+DESCRIBE_IMG_START+CheckParams.matcherMoviePhoto(cover)+DESCRIBE_IMG_END+DESCRIBE_CONTENT_START+describe+DESCRIBE_CONTENT_END;
	}

	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}
	
}
