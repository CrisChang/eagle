package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keel.utils.web.HttpBodyUtils;
import com.poison.eagle.manager.CrawlersManager;

@Controller
@RequestMapping("/bookcrawlers")
public class BookCrawlersController {
	private static final  Log LOG = LogFactory.getLog(BookCrawlersController.class);
	
	private CrawlersManager crawlersManager;
	
	@Autowired
	public void setCrawlersManager(CrawlersManager crawlersManager) {
		this.crawlersManager = crawlersManager;
	}
	
	/**通过ISBN码获取dangdang图书信息，返回要访问dangdang的地址*/
	@RequestMapping(value = "/dd/so/isbn/{isbn}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String dangdangSoByISBN(HttpServletRequest request, HttpServletResponse response, @PathVariable String isbn){
		if (StringUtils.isBlank(isbn)) {
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		
		String outUrl = this.crawlersManager.getDangdangBookSoUrlByISBN(isbn);
		/*System.out.println(bkId);
		this.crawlersManager.setBkId(bkId);*/
		return "{\"flag\":0,\"error\":\"success\",\"outUrl\":\"" + outUrl + "\"}";
	}
	
	/**解析搜索结果，返回解析信息*/
	@RequestMapping(value = "/dd/so/analyse", produces = { "text/html;charset=utf-8" }, method=RequestMethod.POST )
	@ResponseBody
	public String dangdangSoAnalyse(HttpServletRequest request, HttpServletResponse response){
		String body = HttpBodyUtils.getBodyString(request);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		Pattern pattern = Pattern.compile("<li class=\"line[\\d]*\"[\\s\\S]+?href=\"(http://product.*?)\"");
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			String tmp = matcher.group();
			Map<String, String> item = new HashMap<String, String>();
			item.put("outUrl", matcher.group(1));
			
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
	
	/**解析dangdang图书页，返回试读内容*/
	@RequestMapping(value = "/dd/shidu/analyse/{bkId}", produces = { "text/html;charset=utf-8" }, method=RequestMethod.POST)
	@ResponseBody
	public String doubanFilmTrailerAnalyse(HttpServletRequest request, HttpServletResponse response,@PathVariable int bkId){
		String encode = request.getParameterValues("encode") != null ? request.getParameterValues("encode")[0] : "GBK";
		try {
			request.setCharacterEncoding(encode);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String body = HttpBodyUtils.getBodyString(request);
		//System.out.println(bkId);
		if (StringUtils.isBlank(body)){
			return "{\"flag\":1,\"error\":\"param error!\"}";
		}
		
		Map<String, String> result = new HashMap<String, String>();
		
		Pattern pattern = Pattern.compile(">在线试读部分章节</div>[\\s\\S]+?<textarea.*?>(.*?)</textarea>");
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			body = StringUtils.trimToEmpty(matcher.group(1));
		} else {
			return "{\"flag\":1,\"error\":\"content error!\"}";
		}
		
		pattern = Pattern.compile("(.*)");
		matcher = pattern.matcher(body);                                                                                                                            
		if (matcher.find()) {
			//result.put("shiduTitle", StringUtils.trimToEmpty(matcher.group(1)).replace("|", ""));
			result.put("shidu", StringUtils.trimToEmpty(matcher.group(1)));
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        objectMaper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMaper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
        /*objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;*/
        String json = "";
        //String resultJson = "";
       /* String flag = "1";
        String error = "json error!";*/
        //Map<String, String> resultMap = new HashMap<String, String>();
        result.put("flag", "0");
        result.put("error", "");
        result.put("success", "");
        try {
			json = objectMaper.writeValueAsString(result);
			//resultMap.put("ret", json);
			//resultJson = objectMaper.writeValueAsString(resultMap);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "{\"flag\":1,\"error\":\"json error!\"}";
		}
        
        //插入试读部分
        //System.out.println(bkId);
        /*int bkId = this.crawlersManager.getBkId();*/
        this.crawlersManager.insertBkOnlineRead(bkId, body);
       // System.out.println(json);
       // " + json + "
       // \"ret\":{\"shidu\":\"你好\"}}
       // "{\"flag\":0,\"error\":\"success\",\"ret\":{\"shidu\":\"你好\"}}";
	    return json;
	}
}
