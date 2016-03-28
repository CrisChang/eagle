package com.poison.eagle.utils.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.poison.eagle.action.StoryCommentController;
import com.poison.eagle.utils.CommentUtils;

/**
 * 解析request请求的interceptor
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public class RequestParseInterceptor implements HandlerInterceptor{

	private Log LOG = LogFactory.getLog(RequestParseInterceptor.class);
	
	private Pattern p = Pattern.compile("\\{\"req\":\\{\"data\":(\\{.+\\})\\}\\}");
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			String req = request.getParameter("req");
			req.replaceAll("[\\s]+", "");
			LOG.info("请求参数是：==============");
			LOG.info(req);
			if(StringUtils.isEmpty(req)){
				throw new RuntimeException("请求参数不能为空");
			}else{
				req=req.replaceAll("\\s+", "");
				Matcher matcher = p.matcher(req);
				if(!matcher.find()){
					throw new RuntimeException("请求参数:"+req+"--->格式错误");
				}
				return true;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			LOG.error(CommentUtils.ERROR_DATANOTGET);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_DATANOTGET+CommentUtils.RES_ERROR_END);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

	
	public static void main(String[] args) {
		Gson gson = new Gson();
		gson.fromJson("{\"req\":{\"data\":{\"caseType\":\"5\",\"page\":\"1\"}}}", RequestParam.class);
		System.out.println("{\"req\":{\"data\":{\"caseType\":\"5\",\"page\":\"1\"}}}");
		Matcher matcher = Pattern.compile("\\{\"req\":\\{\"data\":(\\{.+\\})\\}\\}").matcher("{\"req\":{\"data\":{\"nickname\":\"刘德华\",\"score\":9,\"commentBody\":\"刘德华，好帅啊\"}}}");
		while(matcher.find()){
			System.out.println(matcher.group(1));
		}
	}
}
