package com.poison.eagle.action;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.manager.*;
import com.poison.eagle.utils.BaseManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.eagle.manager.util.SensitiveWordManager;


@Controller
@RequestMapping("/api/v2")
public class SearchApiController extends BaseManager{
	private static final  Log LOG = LogFactory.getLog(SearchApiController.class);
	
	private SearchApiManager searchApiManager;
	private UserPOIManager userPOIManager;
	private SensitiveWordManager sensitiveWordManager;
	private TagManager tagManager;
	private ResourceManager resourceManager;
	private StoryManager storyManager;

	@Autowired
	public void setStoryManager(StoryManager storyManager) {
		this.storyManager = storyManager;
	}

	@Autowired
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Autowired
	public void setSensitiveWordManager(
			SensitiveWordManager sensitiveWordManager) {
		this.sensitiveWordManager = sensitiveWordManager;
	}

	@Autowired
	public void setSearchApiManager(
			SearchApiManager searchApiManager) {
		this.searchApiManager = searchApiManager;
	}

	@Autowired
	public void setUserPOIManager(UserPOIManager userPOIManager) {
		this.userPOIManager = userPOIManager;
	}
	@Autowired
	public void setTagManager(TagManager tagManager) {
		this.tagManager = tagManager;
	}

	@RequestMapping(value = "/event/location/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String eventByLocation(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String lon = request.getParameterValues("lon") != null ? request.getParameterValues("lon")[0] : null;
		String lat = request.getParameterValues("lat") != null ? request.getParameterValues("lat") [0] : null;
		String radius = request.getParameterValues("radius") != null ? request.getParameterValues("radius") [0] : "100";
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.eventSearchByLocationOuter(lon, lat, NumberUtils.toInt(radius), page * num, num);
		
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/event/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String event(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String userId = request.getParameterValues("user_id") != null ? request.getParameterValues("user_id")[0] : null;
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		Long uid = null;
		if (StringUtils.isNotBlank(userId)){
			uid = NumberUtils.createLong(userId);
		} else {
			if(ProductContextHolder.getProductContext().isAuthenticated()){
				uid = NumberUtils.createLong(ProductContextHolder.getProductContext().getProductUser().getUserId());
			}
		}
		if (null == uid) {
			msg = "invalid userid!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.eventSearchByUserIdOuter(uid, keyWord, page * num, num);
		
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}

	@RequestMapping(value = "/ring/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String ring(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String lon = request.getParameterValues("lon") != null ? request.getParameterValues("lon")[0] : null;
		String lat = request.getParameterValues("lat") != null ? request.getParameterValues("lat") [0] : null;
		String userId = request.getParameterValues("id") != null ? request.getParameterValues("id") [0] : null;
		String radius = request.getParameterValues("radius") != null ? request.getParameterValues("radius") [0] : "100";
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		if ( StringUtils.isBlank(userId)) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = userPOIManager.findRing(NumberUtils.toLong(userId), lon, lat, NumberUtils.toInt(radius), page * num, num);
		
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/user/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String userSearchByFields(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.userSearchByCommonOuter(keyWord, fields, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/topic/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String topicSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		/*fun 2: tags*/
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.topicSearchByCommon(keyWord, fields, tags, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/book/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String bookSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		setAlloweCrossDomain(response);
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		String set = request.getParameterValues("set") != null ? request.getParameterValues("set")[0] : "all";
		/*fun 2: tags*/
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		/*
		if(StringUtils.isNotBlank(keyWord)){
			String sensitiveRet = this.sensitiveWordManager.checkSensitiveWord(keyWord);
			if(StringUtils.isNotBlank(sensitiveRet)){
				msg = sensitiveRet;
				return "{\"count\":0,\"start\":0,\"total\":0,\"books\":[],\"flag\":\"3\",\"msg\":\"" + msg + "\"}";
			}
		}*/
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"books\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.bookSearchByCommon(keyWord, fields, tags, set, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"books\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/movie/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String movieSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		setAlloweCrossDomain(response);
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		String set = request.getParameterValues("set") != null ? request.getParameterValues("set")[0] : "all";
		/*fun 2: tags*/
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;

		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		/*
		if(StringUtils.isNotBlank(keyWord)){
			String sensitiveRet = this.sensitiveWordManager.checkSensitiveWord(keyWord);
			if(StringUtils.isNotBlank(sensitiveRet)){
				msg = sensitiveRet;
				return "{\"count\":0,\"start\":0,\"total\":0,\"books\":[],\"flag\":\"3\",\"msg\":\"" + msg + "\"}";
			}
		}*/
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.movieSearchByCommon(keyWord, fields, tags, set, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		//response.setHeader("Access-Control-Allow-Origin", "*");
		return ret;
	}
	
	//设置允许跨域
	private void setAlloweCrossDomain(HttpServletResponse response){
		response.addHeader("Access-Control-Allow-Origin", "http://dev.duyao001.com");
		//response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");	
		response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
		response.addHeader("Access-Control-Allow-Credentials", "true");
	}
	
	@RequestMapping(value = "/book/exactSearch/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String bookExactSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 100 || page < 0 || StringUtils.isBlank(keyWord)) {
			msg = "invalid param!";
			return "{\"exact\":null,\"ps\":{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[]},\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.bookExactSearch(keyWord, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"exact\":null,\"ps\":{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[]},\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/movie/exactSearch/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String movieExactSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 100 || page < 0 || StringUtils.isBlank(keyWord) ) {
			msg = "invalid param!";
			return "{\"exact\":null,\"ps\":{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[]},\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.movieExactSearch(keyWord, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"exact\":null,\"ps\":{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[]},\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/booklist/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String bookListSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		/*fun 2: tags*/
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.bookListSearchByCommon(keyWord, fields, tags, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/movielist/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String movieListSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		/*fun 2: tags*/
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.movieListSearchByCommon(keyWord, fields, tags, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/bookcomment/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String bookCommentSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		String innerTypes = request.getParameterValues("resource_types") != null ? request.getParameterValues("resource_types")[0] : "23";

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(innerTypes))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.bookCommentSearchByCommonOuter(keyWord, fields, innerTypes, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/moviecomment/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String movieCommentSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		String innerTypes = request.getParameterValues("resource_types") != null ? request.getParameterValues("resource_types")[0] : "24";

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(innerTypes))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.movieCommentSearchByCommonOuter(keyWord, fields, innerTypes, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	/**
	 * 文章搜索
	 * */
	@RequestMapping(value = "/article/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String articleSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		/*fun 2: tags*/
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			//return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
			return "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\"查询出错\"}}}";
		}
		
		Map<String, Object> retJson = this.searchApiManager.articleSearchByCommon(keyWord, fields, tags, page * num, num);
		if(null == retJson){
			msg = "inner error!";
			//return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
			return "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\"查询出错\"}}}";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
        	ret = tagManager.getArticlesByIds(retJson);
			//ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			ret = "";
		}
        
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			//return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
			return "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\"查询出错\"}}}";
		}

		return ret;
	}
	
	/**
	 * http://127.0.0.1:8080/api/v2/movie/fieldsSearch/50/1?country=%E6%97%A5%E6%9C%AC&year=2004&tag=%E7%A7%91%E5%B9%BB&sort=score&desc=1
	 * */
	@RequestMapping(value = "/movie/fieldsSearch/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String movieSearchByFields(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		Map<String, String> fields = new HashMap<String, String>();
		/*fun 1: keyword by each field*/
		fields.put("country", request.getParameterValues("country") != null ? request.getParameterValues("country")[0] : null);
		fields.put("tag", request.getParameterValues("tag") != null ? request.getParameterValues("tag") [0] : null);
		fields.put("year", request.getParameterValues("year") != null ? request.getParameterValues("year")[0] : null);
		fields.put("topic", request.getParameterValues("topic") != null ? request.getParameterValues("topic")[0] : null);
		fields.put("type", request.getParameterValues("type") != null ? request.getParameterValues("type")[0] : null);
		fields.put("star", request.getParameterValues("star") != null ? request.getParameterValues("star")[0] : null);
		fields.put("scene", request.getParameterValues("scene") != null ? request.getParameterValues("scene")[0] : null);
		fields.put("mind", request.getParameterValues("mind") != null ? request.getParameterValues("mind")[0] : null);
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("sort", request.getParameterValues("sort") != null ? request.getParameterValues("sort")[0] : "score");
		sort.put("desc", request.getParameterValues("desc") != null ? request.getParameterValues("desc")[0] : "1");
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.movieSearchByFields(fields, sort, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/movielist/fieldsSearch/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String movieListSearchByFields(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		Map<String, String> fields = new HashMap<String, String>();
		/*fun 1: keyword by each field*/
		fields.put("country", request.getParameterValues("country") != null ? request.getParameterValues("country")[0] : null);
		fields.put("director", request.getParameterValues("director") != null ? request.getParameterValues("director")[0] : null);
		fields.put("year", request.getParameterValues("year") != null ? request.getParameterValues("year")[0] : null);
		fields.put("topic", request.getParameterValues("topic") != null ? request.getParameterValues("topic")[0] : null);
		fields.put("type", request.getParameterValues("type") != null ? request.getParameterValues("type")[0] : null);
		fields.put("star", request.getParameterValues("star") != null ? request.getParameterValues("star")[0] : null);
		fields.put("scene", request.getParameterValues("scene") != null ? request.getParameterValues("scene")[0] : null);
		fields.put("mind", request.getParameterValues("mind") != null ? request.getParameterValues("mind")[0] : null);
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("sort", request.getParameterValues("sort") != null ? request.getParameterValues("sort")[0] : "score");
		sort.put("desc", request.getParameterValues("desc") != null ? request.getParameterValues("desc")[0] : "1");
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.movieListSearchByFields(fields, sort, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/book/fieldsSearch/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String bookSearchByFields(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		Map<String, String> fields = new HashMap<String, String>();
		/*fun 1: keyword by each field*/
		fields.put("book", request.getParameterValues("book") != null ? request.getParameterValues("book")[0] : null);
		fields.put("netbook", request.getParameterValues("netbook") != null ? request.getParameterValues("netbook")[0] : null);
		fields.put("catoon", request.getParameterValues("catoon") != null ? request.getParameterValues("catoon")[0] : null);
		fields.put("sheke", request.getParameterValues("sheke") != null ? request.getParameterValues("sheke")[0] : null);
		fields.put("eco_enc", request.getParameterValues("eco_enc") != null ? request.getParameterValues("eco_enc")[0] : null);

		Map<String, String> sort = new HashMap<String, String>();
		sort.put("sort", request.getParameterValues("sort") != null ? request.getParameterValues("sort")[0] : "score");
		sort.put("desc", request.getParameterValues("desc") != null ? request.getParameterValues("desc")[0] : "1");
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.bookSearchByFields(fields, sort, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/booklist/fieldsSearch/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String bookListSearchByFields(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		Map<String, String> fields = new HashMap<String, String>();
		/*fun 1: keyword by each field*/
		fields.put("type", request.getParameterValues("type") != null ? request.getParameterValues("type")[0] : null);
		fields.put("topic", request.getParameterValues("topic") != null ? request.getParameterValues("topic")[0] : null);
		fields.put("star", request.getParameterValues("star") != null ? request.getParameterValues("star")[0] : null);
		fields.put("rank", request.getParameterValues("rank") != null ? request.getParameterValues("rank")[0] : null);
		fields.put("year", request.getParameterValues("year") != null ? request.getParameterValues("year")[0] : null);
		fields.put("recommend", request.getParameterValues("recommend") != null ? request.getParameterValues("recommend")[0] : null);
		
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("sort", request.getParameterValues("sort") != null ? request.getParameterValues("sort")[0] : "score");
		sort.put("desc", request.getParameterValues("desc") != null ? request.getParameterValues("desc")[0] : "1");
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.bookListSearchByFields(fields, sort, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/serialize/fieldsSearch/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String serializeSearchByFields(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		Map<String, String> fields = new HashMap<String, String>();
		/*fun 1: keyword by each field*/
		fields.put("tags", request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null);
		fields.put("type", request.getParameterValues("type") != null ? request.getParameterValues("type")[0] : null);

		Map<String, String> sort = new HashMap<String, String>();
		sort.put("sort", request.getParameterValues("sort") != null ? request.getParameterValues("sort")[0] : "score");
		sort.put("desc", request.getParameterValues("desc") != null ? request.getParameterValues("desc")[0] : "1");
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.serializeSearchByFields(fields, sort, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/tags/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String tagsSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String tagType = request.getParameterValues("type") != null ? request.getParameterValues("type") [0] : "400";

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.tagsSearchByCommon(keyWord, tagType, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	/**
	 * 通过标签搜索所有资源
	 * */
	@RequestMapping(value = "/resource/tag/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String resourceSearchByTag(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;
		String types = request.getParameterValues("types") != null ? request.getParameterValues("types")[0] : null;
		String flag = request.getParameter("flag");
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		String callback = request.getParameter("callback");
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			//return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
			String ret = "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
			if(callback!=null && callback.trim().length()>0){
				ret = callback+"("+ret+")";
			}
			return ret;
		}
		
		String ret = this.searchApiManager.resourceSearchByTagOuter(keyWord, tags, types, page * num, num,flag);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			//return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
			ret = "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		if(callback!=null && callback.trim().length()>0){
			ret = callback+"("+ret+")";
		}
		return ret;
	}
	
	/**
	 * 标准小说通用搜索
	 * */
	@RequestMapping(value = "/story/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String storySearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		String type = request.getParameterValues("type") != null ? request.getParameterValues("type") [0] : null;
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;
		String channel = request.getParameterValues("channel") != null ? request.getParameterValues("channel")[0] : null;
		/*fun 2: sort*/
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("sort", request.getParameterValues("sort") != null ? request.getParameterValues("sort")[0] : "");
		sort.put("desc", request.getParameterValues("desc") != null ? request.getParameterValues("desc")[0] : "1");

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags)&&StringUtils.isBlank(type))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.storySearchByCommonOuter(keyWord, fields, type, tags, channel, sort, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	/**
	 * 通过标签随机获取小说
	 * */
	@RequestMapping(value = "/story/search/tags_random/{param1}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String storyRandomSearchByTags(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1){
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;

		int num = NumberUtils.toInt(param1);
		
		String msg = null;
		
		if ( num <= 0 || (StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.storyRandomSearchByTagsOuter(tags, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}

	@RequestMapping(value = "/story/random/search/{param1}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String storyRandomSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1){
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;

		int num = NumberUtils.toInt(param1);

		String msg = null;

		if ( num <= 0 || (StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		String ret = storyManager.searchRandomStory(tags,num);

		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
//		String ret = this.searchApiManager.storySearchByCommonOuter(keyWord, fields, tags, page * num, num);
//		if(StringUtils.isBlank(ret)){
//			msg = "inner error!";
//			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
//		}

		return ret;
	}


	/**
	 * 搜索小说
	 * @param request
	 * @param response
	 * @param param1
	 * @param param2
	 * @return
	 */
	@RequestMapping(value = "/story/common/search/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String storyCommonSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		/*fun 1: keyword and fields*/
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String fields = request.getParameterValues("keyFields") != null ? request.getParameterValues("keyFields") [0] : null;
		/*fun 2: tags*/
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;
		String type = request.getParameterValues("type") != null ? request.getParameterValues("type") [0] : null;
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;

		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags)&&StringUtils.isBlank(type))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		String res = storyManager.searchStory(keyWord,fields,tags,type,page * num,num);
//		String ret = this.searchApiManager.storySearchByCommonOuter(keyWord, fields, tags, page * num, num);
//		if(StringUtils.isBlank(ret)){
//			msg = "inner error!";
//			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
//		}

		return res;
	}
	
	@RequestMapping(value = "/test/userRec/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String testUserRecByTags(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){

		String tags = request.getParameterValues("tags")[0];
		String types = request.getParameterValues("type")[0];
		String [] tag_list = StringUtils.split(tags, ',');
		String [] type_list = StringUtils.split(types, ',');
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;
		
		String sensitiveRet = this.sensitiveWordManager.checkSensitiveWord(tags);
		if(StringUtils.isNotBlank(sensitiveRet)){
			msg = sensitiveRet;
			return "{\"count\":0,\"start\":0,\"total\":0,\"books\":[],\"flag\":\"3\",\"msg\":\"" + msg + "\"}";
		}
		
		if ( num <= 0 || num > 50 || page < 0 ) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"books\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		String ret = this.searchApiManager.tempUserRecByTags(type_list, tag_list, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"books\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}


	@RequestMapping(value = "/resource/all/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String resourceAllSearchByKeyWord(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;
		String tags = request.getParameterValues("tags") != null ? request.getParameterValues("tags")[0] : null;
		String types = request.getParameterValues("types") != null ? request.getParameterValues("types")[0] : null;

		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;

		String msg = null;

		if ( num <= 0 || num > 50 || page < 0 || (StringUtils.isBlank(keyWord)&&StringUtils.isBlank(tags))) {
			msg = "invalid param!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		Map<String,Object> resultMap = new HashMap<String, Object>();
		long id = 0;
		String type = "";

		List<ResourceInfo> movieResourceInfos = new ArrayList<ResourceInfo>();
		//List<Map<String,Object>> subjects = new ArrayList<Map<String, Object>>();
		//影视
		Map<String,Object> movieMap = this.searchApiManager.resourceSearchByTag(keyWord,null,"movie",0,2,null);
		List<Map<String, Object>> movieSubjects = (ArrayList)movieMap.get("subjects");
		Iterator<Map<String,Object>> movieSubjectsIt = movieSubjects.iterator();
		while(movieSubjectsIt.hasNext()){
			Map<String,Object> map = movieSubjectsIt.next();
			id = Long.valueOf((String)map.get("id"));
			type = (String)map.get("type");
			//转换为resourceInfo类型
			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
			movieResourceInfos.add(resourceInfo);
		}
		resultMap.put("movie",movieResourceInfos);

		//图书
		List<ResourceInfo> bookResourceInfos = new ArrayList<ResourceInfo>();
		Map<String,Object> bookMap =this.searchApiManager.resourceSearchByTag(keyWord,null,"book",0,2,null);
		List<Map<String, Object>> bookSubjects = (ArrayList)bookMap.get("subjects");
		Iterator<Map<String,Object>> bookSubjectsIt = bookSubjects.iterator();
		while(bookSubjectsIt.hasNext()){
			Map<String,Object> map = bookSubjectsIt.next();
			id = Long.valueOf((String)map.get("id"));
			type = (String)map.get("type");
			//转换为resourceInfo类型
			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
			bookResourceInfos.add(resourceInfo);
		}
		resultMap.put("book",bookResourceInfos);
		//subjects.addAll(bookSubjects);

		//话题
		Map<String,Object> topicMap =this.searchApiManager.resourceSearchByTag(keyWord,null,"topic",0,2,null);
		List<Map<String, Object>> topicSubjects = (ArrayList)topicMap.get("subjects");
		List<ResourceInfo> topicResourceInfos = new ArrayList<ResourceInfo>();
		Iterator<Map<String,Object>> topicSubjectsIt = topicSubjects.iterator();
		while(topicSubjectsIt.hasNext()){
			Map<String,Object> map = topicSubjectsIt.next();
			id = Long.valueOf((String)map.get("id"));
			type = (String)map.get("type");
			//转换为resourceInfo类型
			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
			topicResourceInfos.add(resourceInfo);
		}
		resultMap.put("topic",topicResourceInfos);
		//subjects.addAll(topicSubjects);
		//找人
		Map<String,Object> userMap =this.searchApiManager.userSearchByCommon(keyWord, null, 0, 2);
		List<Map<String, Object>> userSubjects = (ArrayList)userMap.get("subjects");
		List<ResourceInfo> userResourceInfos = new ArrayList<ResourceInfo>();
		Iterator<Map<String,Object>> userSubjectsIt = userSubjects.iterator();
		while(userSubjectsIt.hasNext()){
			Map<String,Object> map = userSubjectsIt.next();
			id = Long.valueOf((String)map.get("id"));
			type = (String)map.get("type");
			//转换为resourceInfo类型
			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
			userResourceInfos.add(resourceInfo);
		}
		resultMap.put("user",userResourceInfos);
		//subjects.addAll(userSubjects);
		//影单
		Map<String,Object> movieListMap =this.searchApiManager.resourceSearchByTag(keyWord,null,"movielist",0,2,null);
		List<Map<String, Object>> movieListSubjects = (ArrayList)movieListMap.get("subjects");
		List<ResourceInfo> movieListResourceInfos = new ArrayList<ResourceInfo>();
		Iterator<Map<String,Object>> movieListSubjectsIt = movieListSubjects.iterator();
		while(movieListSubjectsIt.hasNext()){
			Map<String,Object> map = movieListSubjectsIt.next();
			id = Long.valueOf((String)map.get("id"));
			type = (String)map.get("type");
			//转换为resourceInfo类型
			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
			movieListResourceInfos.add(resourceInfo);
		}
		resultMap.put("movieList",movieListResourceInfos);
		//subjects.addAll(movieListSubjects);
		//书单
		Map<String,Object> bookListMap =this.searchApiManager.resourceSearchByTag(keyWord,null,"booklist",0,2,null);
		List<Map<String, Object>> bookListSubjects = (ArrayList)bookListMap.get("subjects");
		List<ResourceInfo> bookListResourceInfos = new ArrayList<ResourceInfo>();
		Iterator<Map<String,Object>> bookListSubjectsIt = bookListSubjects.iterator();
		while(bookListSubjectsIt.hasNext()){
			Map<String,Object> map = bookListSubjectsIt.next();
			id = Long.valueOf((String)map.get("id"));
			type = (String)map.get("type");
			//转换为resourceInfo类型
			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
			bookListResourceInfos.add(resourceInfo);
		}
		resultMap.put("bookList",bookListResourceInfos);
		//subjects.addAll(bookListSubjects);
		//文章
		Map<String,Object> articleMap =this.searchApiManager.resourceSearchByTag(keyWord,null,"article",0,2,null);
		List<Map<String, Object>> articleSubjects = (ArrayList)articleMap.get("subjects");
		List<ResourceInfo> articleResourceInfos = new ArrayList<ResourceInfo>();
		Iterator<Map<String,Object>> articleSubjectsIt = articleSubjects.iterator();
		while(articleSubjectsIt.hasNext()){
			Map<String,Object> map = articleSubjectsIt.next();
			id = Long.valueOf((String)map.get("id"));
			type = (String)map.get("type");
			//转换为resourceInfo类型
			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
			articleResourceInfos.add(resourceInfo);
		}
		resultMap.put("article",articleResourceInfos);
		//subjects.addAll(articleSubjects);

//		Iterator<Map<String,Object>> subjectsIt = subjects.iterator();
//		while(subjectsIt.hasNext()){
//			Map<String,Object> map = subjectsIt.next();
//			id = Long.valueOf((String)map.get("id"));
//			type = (String)map.get("type");
//			//转换为resourceInfo类型
//			ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(id,type,0l);
//			resourceInfos.add(resourceInfo);
//		}
		resultMap.put("flag","0");
		String ret =getResponseData(resultMap);
				//this.searchApiManager.resourceSearchByTagOuter(keyWord, tags, types, page * num, num);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
}
