package com.poison.eagle.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.util.PromptTagManager;

@Controller
@RequestMapping("/prompt")
public class SearchPromptController {
	private static final  Log LOG = LogFactory.getLog(SearchPromptController.class);
	
	private PromptTagManager promptTagManager;

	public void setPromptTagManager(PromptTagManager promptTagManager) {
		this.promptTagManager = promptTagManager;
	}
	
	@RequestMapping(value = "/tag", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String tagPrompt(HttpServletRequest request, HttpServletResponse response){
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;

		String msg = null;
		if(StringUtils.isBlank(keyWord)){
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"0\"}";
		}
		
		String ret = this.promptTagManager.prefixMatch(keyWord, PromptTagManager.TagType.MOVIELIST);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
	
	@RequestMapping(value = "/tag/bklist", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String tagBkListPrompt(HttpServletRequest request, HttpServletResponse response){
		String keyWord = request.getParameterValues("q") != null ? request.getParameterValues("q")[0] : null;

		String msg = null;
		if(StringUtils.isBlank(keyWord)){
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"0\"}";
		}
		
		String ret = this.promptTagManager.prefixMatch(keyWord, PromptTagManager.TagType.BOOKLIST);
		if(StringUtils.isBlank(ret)){
			msg = "inner error!";
			return "{\"count\":0,\"start\":0,\"total\":0,\"subjects\":[],\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}

		return ret;
	}
}
