package com.poison.eagle.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.entity.StringProperties;
import com.poison.eagle.manager.ResStatJedisManager;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.utils.BaseController;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.GraphicFilmFacade;
import com.poison.resource.client.ResourceLinkFacade;

/**
 * app页面统一调用地址
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(CommentUtils.REQUEST_FROM_WEB)
public class AppWapController extends BaseController {
	private static final Log LOG = LogFactory
			.getLog(AppWapController.class);
	
	private ResourceManager resourceManager;

	public void setStringProperties(StringProperties stringProperties) {
		this.stringProperties = stringProperties;
	}

	private GraphicFilmFacade graphicFilmFacade;
	private ResourceLinkFacade resourceLinkFacade;
	private ArticleFacade articleFacade;
	private StringProperties stringProperties;
	
	private ResStatJedisManager resStatJedisManager;


	public void setGraphicFilmFacade(GraphicFilmFacade graphicFilmFacade) {
		this.graphicFilmFacade = graphicFilmFacade;
	}
	public void setResourceLinkFacade(ResourceLinkFacade resourceLinkFacade) {
		this.resourceLinkFacade = resourceLinkFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}
	/**
	 * app页面统一地址
	 * 
	 * @param request
	 * @param response
	 * @param res  客户端传过来的json数据
	 * @return
	 */
	@RequestMapping(value = "/wap/{id}/{type}/{uid}", method = RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String viewShareList(HttpServletRequest request,
			HttpServletResponse response,@PathVariable Long id,@PathVariable String type,@PathVariable Long uid) throws UnsupportedEncodingException {
		//System.out.println(id+"==="+type+"===="+uid);
		
		request.setAttribute("id", id);
		request.setAttribute("type", type);
		request.setAttribute("uid", uid);
		String platform = request.getParameter("platform");
		
		try {
			if(CommentUtils.TYPE_NEWARTICLE.equals(type)){//长文章
				try{
					if(uid!=null && uid==46){
						Map<String,Object> map = articleFacade.updateArticleReadingCount(id, uid);
						Integer flagint = (Integer) map.get("flag");
						if(flagint!=null && flagint==ResultUtils.SUCCESS){
							try{
								resStatJedisManager.addReadNum(id, CommentUtils.TYPE_NEWARTICLE,0,"",0,(Integer)map.get("num"));
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				if(null!=platform){
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/article.html?id="+id+"&origin="+platform);
				}else{
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/article.html?id="+id);
				}
			} else if(type.equals(CommentUtils.TYPE_ARTICLE_MOVIE)){
				if(null!=platform){
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-movie-comment.html?id="+id+"&origin="+platform);
				}else{
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-movie-comment.html?id="+id);
				}
			} else if(type.equals(CommentUtils.TYPE_ARTICLE_BOOK)){
				if(null!=platform){
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-book-comment.html?id="+id+"&origin="+platform);
				}else{
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-book-comment.html?id="+id);
				}
			} else if(type.equals(CommentUtils.TYPE_BOOK_DIGEST)){
				if(null!=platform){
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-bookl-comment.html?id="+id+"&origin="+platform);
				}else{
					response.sendRedirect(stringProperties.getWebServer()+"/sy/0.1.15/detail-bookl-comment.html?id="+id);
				}
			}
			/*else if(CommentUtils.CASETYPE_MOVIE_GRAPHICFILM.endsWith(type)){
				List<ResourceLink> resLinkList = resourceLinkFacade.findResListByResIdAndType(id,CommentUtils.CASETYPE_MOVIE_GRAPHICFILM);
				System.out.println("关联表中的长度为"+resLinkList.size());
				if(resLinkList.size()>0){
					ResourceLink resourceLink = resLinkList.get(0);
					long graphicFilmId = resourceLink.getResLinkId();
					System.out.println("图解电影的id为"+graphicFilmId);
					response.sendRedirect("http://m.duyao001.com/sy/0.1.9/article.html?id="+graphicFilmId+"&origin="+platform);
					Iterator<ResourceLink> resLinkIt = resLinkList.iterator();
					while(resLinkIt.hasNext()){
						ResourceLink resourceLink = resLinkIt.next();
						long graphicFilmId = resourceLink.getResLinkId();
						GraphicFilm graphicFilm = graphicFilmFacade.findGraphicFilmById(graphicFilmId);
						resourceInfos.add(graphicFilm.getContent());
					}
				}
				
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
