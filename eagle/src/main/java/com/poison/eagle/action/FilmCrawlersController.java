package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.keel.utils.web.HttpBodyUtils;
import com.poison.eagle.manager.CrawlersManager;

@Controller
@RequestMapping("/filmcrawlers")
public class FilmCrawlersController {
	private static final  Log LOG = LogFactory.getLog(FilmCrawlersController.class);
	
	private CrawlersManager crawlersManager;

	@Autowired
	public void setCrawlersManager(CrawlersManager crawlersManager) {
		this.crawlersManager = crawlersManager;
	}
	
	/**1 搜索电影*/
	@RequestMapping(value = "/db/so", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String doubanSo(HttpServletRequest request, HttpServletResponse response){
		String[] keys = request.getParameterValues("q");
		if (null == keys){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		if (StringUtils.isBlank(keys[0])){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		URLCodec encode = new URLCodec("UTF_8");
		String encodeQ;
		try {
			encodeQ = encode.encode(keys[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "{\"flag\":1,\"error\":\"encode error!\"}";
		}
		String outUrl = crawlersManager.getDoubanFilmSoUrl(encodeQ);
		
	    return "{\"flag\":0,\"error\":\"success\",\"outUrl\":\"" + outUrl + "\"}";
	}

	
	/**2 提交搜索结果，返回结构化数据*/
	@RequestMapping(value = "/db/so/analyse", produces = { "text/html;charset=utf-8" }, method=RequestMethod.POST)
	@ResponseBody
	public String doubanSoAnalyse(HttpServletRequest request, HttpServletResponse response){
		//page_str.indexOf("success")
		/*
        pattern = re.compile(r'var delImfor={([\s\S]*?)};')
        delImfor_str = pattern.findall(result)[0]
        delImfor_str = delImfor_str.replace('\n', '')
        delImfor_str = delImfor_str.replace('http:', '')
        delImfor_str = re.sub(r"(,?)(\w+?)\s*?:", r"\1'\2':", delImfor_str);
        delImfor_str = delImfor_str.replace("'", "\"");
        */

		String body = HttpBodyUtils.getBodyString(request);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		Pattern pattern = Pattern.compile("<a class=\"nbg\" href=\"(.*?)\".*?title=\"(.*?)\">" +
				"\\s*<img src=\"(.*?)\" alt=\"(.*?)\"[\\s\\S]*?" + 
				"<div class=\"pl2\">[\\s\\S]*?<a href=\"(.*?)\"[\\s\\S]*?>[\\s]*?(.*?)[\\s]*?</a>[\\s\\S]*?" +
				"<p class=\"pl\">(.*?)</p>");
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			String tmp = matcher.group();
			//System.out.println("结果 :"+tmp);
			Map<String, String> item = new HashMap<String, String>();
			//for (int i =1 ; i<= matcher.groupCount(); i++) {
			//	System.out.println(matcher.group(i));
			//}
			item.put("url", matcher.group(1));
			item.put("name", matcher.group(2));
			item.put("picUrl", matcher.group(3));
				//alt
				//url
				//name
			item.put("other", matcher.group(7));
			result.add(item);
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String json = "";
        try {
			json = objectMaper.writeValueAsString(result);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "{\"flag\":1,\"error\":\"json error!\"}";
		}

	    return "{\"flag\":0,\"error\":\"success\",\"list\":" + json + "}";
	}
	
	/**3 请求电影信息，返回请求地址*/
	
	
	/**4 提交电影信息，返回结构化数据*/
	@RequestMapping(value = "/db/film/analyse", produces = { "text/html;charset=utf-8" }, method=RequestMethod.POST)
	@ResponseBody
	public String doubanFilmAnalyse(HttpServletRequest request, HttpServletResponse response){
		String body = HttpBodyUtils.getBodyString(request);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		
		Map<String, String> result = new HashMap<String, String>();
		
		Pattern pattern = Pattern.compile("<span property=\"v:itemreviewed\">(.*)?</span>[\\s\\S]+?" +  //名称
				"<span class=\"year\">\\(([\\d]+?)\\)</span>[\\s\\S]+?" +           //年代
				"<div id=\"mainpic\"[\\s\\S]+?src=\"(.+?)\"[ /].*?>[\\s\\S]+?" +           //主图地址
				"<span><span class='pl'>导演.+?(<a .*?</a>)</span><br/>[\\s\\S]+?" +           //导演
				"<span><span class='pl'>编剧.+?(<a .*?</a>)</span><br/>[\\s\\S]+?" +           //编剧
				"<span><span class='pl'>主演.+?(<a .*?</a>)</span><br/>[\\s\\S]+?" +           //主演
				"<span class=\"pl\">类型.+?(<span .*?</span>)<br/>[\\s\\S]+?" +           //类型
				"<span class=\"pl\">制片国家/地区.+?</span>[ ]*?(.*?)<br/>[\\s\\S]+?" +           //制片国家/地区
				"<span class=\"pl\">语言.+?</span>[ ]*?(.*?)<br/>[\\s\\S]+?" +           //语言
				"<span class=\"pl\">上映日期.+?content=\"(.*?)\"[\\s\\S]+?" +           //上映日期
				"<span class=\"pl\">片长.+?content=\"(.*?)\"[\\s\\S]+?" +           //片长
				"<span class=\"pl\">又名.+?</span>[ ]*?(.*?)<br/>[\\s\\S]+?" +           //又名
				"class=\"ll rating_num\".*?>([\\d\\.]*?)<[\\s\\S]+?" +           //分值
				"id=\"related-pic\"[\\s\\S]+?<a href=\"(.*?)\">预告片[\\d]*?</a>[\\s\\S]+?" +           //预告片地址
				"<a href=\"(.*?)\">图片[\\d]*?</a>[\\s\\S]+?" +           //图片地址
				""
		);
		

		Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			result.put("title", StringUtils.trimToEmpty(matcher.group(1)));
			result.put("year", StringUtils.trimToEmpty(matcher.group(2)));
			result.put("image", StringUtils.trimToEmpty(matcher.group(3)));
			result.put("director", StringUtils.trimToEmpty(matcher.group(4).replaceAll("<.+?>", "")));
			result.put("writer", StringUtils.trimToEmpty(matcher.group(5).replaceAll("<.+?>", "")));
			result.put("cast", StringUtils.trimToEmpty(matcher.group(6).replaceAll("<.+?>", "")));
			result.put("movie_type", StringUtils.trimToEmpty(matcher.group(7).replaceAll("<.+?>", "")));
			result.put("country", StringUtils.trimToEmpty(matcher.group(8)));
			result.put("language", StringUtils.trimToEmpty(matcher.group(9)));
			result.put("pubdate", StringUtils.trimToEmpty(matcher.group(10)));
			result.put("movie_duration", StringUtils.trimToEmpty(matcher.group(11)));
			result.put("alt_title", StringUtils.trimToEmpty(matcher.group(12)));
			result.put("score", StringUtils.trimToEmpty(matcher.group(13)));
			result.put("trailer_urls", StringUtils.trimToEmpty(matcher.group(14)));
			result.put("photos", StringUtils.trimToEmpty(matcher.group(15)));
		}
		
		//简介
		pattern = Pattern.compile("property=\"v:summary\".*?>[\\s]*?(.*?)[\\s]*?</span>");
		matcher = pattern.matcher(body);
		if (matcher.find()) {
			result.put("summary", StringUtils.trimToEmpty(matcher.group(1)));
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String json = "";
        try {
			json = objectMaper.writeValueAsString(result);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "{\"flag\":1,\"error\":\"json error!\"}";
		}

	    return "{\"flag\":0,\"error\":\"success\",\"ret\":" + json + "}";
	}

	/**5 提交电影预告片页信息，返回结构化数据*/
	@RequestMapping(value = "/db/film/trailer/analyse/{mvId}", produces = { "text/html;charset=utf-8" }, method=RequestMethod.POST)
	@ResponseBody
	public String doubanFilmTrailerAnalyse(HttpServletRequest request, HttpServletResponse response,@PathVariable long mvId){
		String body = HttpBodyUtils.getBodyString(request);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		Pattern pattern = Pattern.compile("<div class=\"mod\">(.+?<h2>预告片.+?)<div class=\"mod\">");
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()){
			body = matcher.group(1);
		} else {
			return "{\"flag\":1,\"error\":\"content error!\"}";
		}
		
		pattern = Pattern.compile("<a class=\"pr-video\" href=\"(.*?)\"[\\s\\S]*?" +            //预告片地址
				"<img src=\"(.*?)\"[\\s\\S]*?" +                                                        //预告片图片地址
				"<em>(.*?)</em>[\\s\\S]*?" +                                                            //片长
				"</a>[\\s]*<p>[\\s]*<a href=\".*?\">(.*?)[\\s]*</a>[\\s]*</p>[\\s\\S]+?" +              //预告片题目
				"</li>");
		

		matcher = pattern.matcher(body);
		while (matcher.find()) {
			String tmp = matcher.group();
			Map<String, String> item = new HashMap<String, String>();

			item.put("trailerVideoUrl", StringUtils.trimToEmpty(matcher.group(1)));
			item.put("trailerPicUrl", StringUtils.trimToEmpty(matcher.group(2)));
			item.put("trailerLength", StringUtils.trimToEmpty(matcher.group(3)));
			item.put("trailerName", StringUtils.trimToEmpty(matcher.group(4)));
			
			list.add(item);
	    }
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String json = "";
        try {
			json = objectMaper.writeValueAsString(list);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "{\"flag\":1,\"error\":\"json error!\"}";
		}

        //插入库中
        crawlersManager.updateMvOther(json, mvId);
        
	    return "{\"flag\":0,\"error\":\"success\",\"list\":" + json + "}";
	}
	
	/**6 提交电影图片页信息，返回结构化数据*/
	@RequestMapping(value = "/db/film/photo/analyse/{mvId}", produces = { "text/html;charset=utf-8" }, method=RequestMethod.POST)
	@ResponseBody
	public String doubanFilmPhotoAnalyse(HttpServletRequest request, HttpServletResponse response,@PathVariable long mvId){
		String body = HttpBodyUtils.getBodyString(request);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}

		LinkedList<String>  list1 = new LinkedList<String>();
		String photo1Context = null;
		Pattern pattern = Pattern.compile("<div class=\"mod\">(.+?<h2>[\\s]*剧照.+?)<div class=\"mod\">");
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()){
			photo1Context = matcher.group(1);
		} else {
			return "{\"flag\":1,\"error\":\"content error!\"}";
		}
		
		pattern = Pattern.compile("<img src=\"(.+?)\"");
		matcher = pattern.matcher(photo1Context);
		String url= "";
		String errorUrl = "http://img3.douban.com/pics/morepic.png";
		while (matcher.find()) {
			url = StringUtils.trimToEmpty(matcher.group(1)).replace("albumicon", "photo");
			if(!errorUrl.equals(url)){
				list1.add(url);
			}
	    }
		//有改动
		if(null!=list1&&list1.size()!=0){
			list1.removeLast();
		}
		LinkedList<String>  list2 = new LinkedList<String>();
		String photo2Context = null;
		pattern = Pattern.compile("<div class=\"mod\">(.+?<h2>[\\s]*海报.+?)<div class=\"mod\">");
		matcher = pattern.matcher(body);

		if (matcher.find()){
			photo2Context = matcher.group(1);
		} else {
			return "{\"flag\":1,\"error\":\"content error!\"}";
		}
		
		pattern = Pattern.compile("<img src=\"(.+?)\"");
		matcher = pattern.matcher(photo2Context);
		while (matcher.find()) {
/*<<<<<<< .mine
			list.add(StringUtils.trimToEmpty(matcher.group(1)).replace("albumicon", "photo"));
=======*/
			list2.add(StringUtils.trimToEmpty(matcher.group(1)).replace("albumicon", "photo"));
	    }
		if(null!=list2&&list2.size()>0){
			list2.removeLast();
		}
		
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		ret.put("juzhao", list1);
		ret.put("haibao", list2);
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String json = "";
        try {
			json = objectMaper.writeValueAsString(ret);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "{\"flag\":1,\"error\":\"json error!\"}";
		}

        //插入库中
        crawlersManager.updateMvStills(json, mvId);
        
	    return "{\"flag\":0,\"error\":\"success\",\"list\":" + json + "}";
	}
	
	static public void  main(String [] argv){

	}
}
