package com.poison.eagle.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.SearchManager;

@Controller  
@RequestMapping("/search")
public class CommonSearchController {
	private static final  Log LOG = LogFactory.getLog(CommonSearchController.class);

	private SearchManager searchManager;

	@Autowired
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}
	
	@RequestMapping(value = "/book/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String commonSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		String keyWord = request.getParameterValues("q") [0];
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0) {
			msg = "invalid param!";
			return "{\"flag\":1,\"error\":\"" + msg + "\",\"list\":[]}";
		}
		
		String ret = this.searchManager.bookSearch(keyWord, page * num, num);

		return "{\"flag\":0,\"error\":\"success\",\"list\":" + ret + "}";

	}
	
	@RequestMapping(value = "/netBook/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String netBookSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		String keyWord = request.getParameterValues("q") [0];
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0) {
			msg = "invalid param!";
			return "{\"flag\":1,\"error\":\"" + msg + "\",\"list\":[]}";
		}
		
		String ret = this.searchManager.netBookSearch(keyWord, page * num, num);

		return "{\"flag\":0,\"error\":\"success\",\"list\":" + ret + "}";
	}
	
	@RequestMapping(value = "/film/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String filmSearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		String keyWord = request.getParameterValues("q") [0];
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0) {
			msg = "invalid param!";
			return "{\"flag\":1,\"error\":\"" + msg + "\",\"list\":[]}";
		}
		
		String ret = this.searchManager.filmSearch(keyWord, page * num, num, "0");

		return "{\"flag\":0,\"error\":\"success\",\"list\":" + ret + "}";
	}
	
	@RequestMapping(value = "/stagePlay /{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String stagePlaySearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		String keyWord = request.getParameterValues("q") [0];
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0) {
			msg = "invalid param!";
			return "{\"flag\":1,\"error\":\"" + msg + "\",\"list\":[]}";
		}
		
		String ret = this.searchManager.filmSearch(keyWord, page * num, num, "1");

		return "{\"flag\":0,\"error\":\"success\",\"list\":" + ret + "}";
	}
	
	@RequestMapping(value = "/musicPlay/{param1}/{param2}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String musicPlaySearch(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1, @PathVariable String param2){
		String keyWord = request.getParameterValues("q") [0];
		
		int num = NumberUtils.toInt(param1);
		int page = NumberUtils.toInt(param2) - 1;
		
		String msg = null;
		
		if ( num <= 0 || num > 50 || page < 0) {
			msg = "invalid param!";
			return "{\"flag\":1,\"error\":\"" + msg + "\",\"list\":[]}";
		}
		
		String ret = this.searchManager.filmSearch(keyWord, page * num, num, "2");

		return "{\"flag\":0,\"error\":\"success\",\"list\":" + ret + "}";
	}
}
