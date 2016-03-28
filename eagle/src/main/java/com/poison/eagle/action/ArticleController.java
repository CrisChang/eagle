package com.poison.eagle.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.manager.ArticleManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
/**
 * 长文章
 * @author Administrator
 *
 */
@Controller  
@RequestMapping(CommentUtils.REQUEST_FROM_MOBLIE)
public class ArticleController extends BaseController {
	
	private static final  Log LOG = LogFactory.getLog(ArticleController.class);
	
	private ArticleManager articleManager;
	
	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager = articleManager;
	}


	/**
	 * @param request
	 * @param response
	 * @param res 客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value="/clientaction/save_article",method=RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getGroup(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String reqs = "";
		long uid=0L;
		//获取用户id
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			//uid = 0;
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
		String res = articleManager.saveArticle(reqs, uid);
		return res;
	}
	
	/**
	 * 删除长文章
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/clientaction/delete_article", method = RequestMethod.POST,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String deleteArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String reqs = "";
		long uid = 0L;
		if(checkUserIsLogin()){
			uid = getUserId();
		}else{
			uid = 0l;
			//返回未登录的json数据
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
		String result = articleManager.deleteArticle(reqs, uid);
		return result;
	}
}
