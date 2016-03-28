package com.poison.eagle.action;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.keel.utils.web.HttpBodyUtils;
import com.poison.eagle.manager.CrawlersManager;

@Controller
@RequestMapping("/bookcrawlers/qd")
public class QidianCrawlerController {
	private static final  Log LOG = LogFactory.getLog(QidianCrawlerController.class);
	
	private CrawlersManager crawlersManager;
	
	@Autowired
	public void setCrawlersManager(CrawlersManager crawlersManager) {
		this.crawlersManager = crawlersManager;
	}
	
	/**通过起点搜索，搜书名和作者相关关键字，返回搜索地址*/
	@RequestMapping(value = "/so/key/{keyword}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String qidianSoByKeyword(HttpServletRequest request, HttpServletResponse response, @PathVariable String keyword){
		if (StringUtils.isBlank(keyword)) {
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		
		return null;
	}
	
	/**解析搜索结果，返回解析信息*/
	@RequestMapping(value = "/so/analyse", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String qidianSoAnalyse(HttpServletRequest request, HttpServletResponse response){
		String body = HttpBodyUtils.getBodyString(request);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		
		
		
		return null;
	}
	
	/**
	 * http://top.qidian.com/Book/TopDetail.aspx?TopType=1&Time=3&PageIndex=19000
	 * */
	/**解析起点图书页，返回结构化结果*/
	@RequestMapping(value = "/book/analyse", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String qidianBookAnalyse(HttpServletRequest request, HttpServletResponse response){
		String body = HttpBodyUtils.getBodyString(request);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		Map<String, String> result = new HashMap<String, String>();
		Pattern pattern = Pattern.compile("BookId: \"(.+?)\",[\\s\\S]+?" +  //书id
				"BookName: \"(.+?)\",[\\s\\S]+?" +                          //书名
				"AuthorId: \"(.+?)\",[\\s\\S]+?" +                          //作者id
				"AuthorName: \"(.+?)\",[\\s\\S]+?" +                        //作者名
				"CategoryName: \"(.*?)\",[\\s\\S]+?" +                      //分类名
				"SubCategoryName: \"(.*?)\",[\\s\\S]+?" +                   //子分类名	
				"AbsoluteUrl: \"(.*?)\",[\\s\\S]+?" +                        //书地址
				"<img itemprop=\"image\" src=\"(.*?)\"[\\s\\S]+?" +           //封皮地址
				""
		);
		
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			result.put("book_id", StringUtils.trimToEmpty(matcher.group(1)));
			result.put("name", StringUtils.trimToEmpty(matcher.group(2)));
			result.put("author_id", StringUtils.trimToEmpty(matcher.group(3)));
			result.put("authorName", StringUtils.trimToEmpty(matcher.group(4)));
			result.put("type", StringUtils.trimToEmpty(matcher.group(5)));
			result.put("tags", StringUtils.trimToEmpty(matcher.group(6)));
			result.put("book_url", StringUtils.trimToEmpty(matcher.group(7).replace("\" + \"", "")));
			//图片地址可能失败，为404，
			result.put("pagePic", StringUtils.trimToEmpty(matcher.group(8)));
		}
		
		pattern = Pattern.compile("<span itemprop=\"description\">[\\s]*?(.*?)[\\s]*?</span>");
		matcher = pattern.matcher(body);
		if (matcher.find()) {
			result.put("introduction", StringUtils.trimToEmpty(matcher.group(1)));
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
	
	/**else*/
}
