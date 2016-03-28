package com.poison.eagle.manager.web; 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.manager.ResStatJedisManager;
import com.poison.eagle.manager.ResourceJedisManager;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HtmlUtil;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.Uploader;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.ArticleDraft;
import com.poison.resource.model.Post;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class ArticleWebManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ArticleWebManager.class);
	
//	private long id;
	
	private UcenterFacade ucenterFacade;
	private PostFacade postFacade;
	private ActFacade actFacade;
	private String savePath;
	private ResourceManager resourceManager;
	private ResourceJedisManager resourceJedisManager;
	private BkFacade bkFacade;
	private MvFacade mvFacade;
	private ArticleFacade articleFacade;
	
	private ResStatJedisManager resStatJedisManager;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private SerializeUtils serializeUtils = SerializeUtils.getInstance();
	private HttpUtils httpUtils = HttpUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();
	
	
	/**
	 * 上传图片(百度编辑器)
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map<String, Object> uploadImageByUeditor(HttpServletRequest request){
		String uploadPath = "";//上传到服务器的图片地址
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Uploader up = new Uploader(request);
	    up.setSavePath(savePath);
	    String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
	    up.setAllowFiles(fileType);
	    up.setMaxSize(10000); //单位KB
	    try {
			up.upload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    map.put("uploader", up);
		
		
		return map;
	}
	/**
	 * 上传图片
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map<String, String> uploadImg(HttpServletRequest request){
		String uploadPath = "";//上传到服务器的图片地址
		
		
		Map<String, String> map = httpUtils.uploadImage(request,savePath);
		String savePath = map.get("savePath");
		if("0".equals(map.get("flag"))){
			uploadPath = httpUtils.uploadFile(savePath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_ARTICLE);
			map.put("uploadPath", uploadPath);
			map.put("error", "上传成功");
		}
		
		
		
		
		return map;
	}
	/**
	 * 某个人的长文章列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map viewArticleList(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		
		
		
		List<Post> posts = postFacade.queryByTypeUid(null, uid);
		
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(); 
		resourceInfos = getPostResponseList(posts, uid, resourceInfos);
		Collections.sort(resourceInfos);
		request.setAttribute("list", resourceInfos);
		map.put("list", resourceInfos);
		
		
		return map;
	}
	/**
	 * 写长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map writeArticle(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("picUrl");
		String content = request.getParameter("content");
		long id = 0;
		String name = "";
		String pic = "";
		String score = "";
		String author = "";
		String type = request.getParameter("type");
		String ahref = "";
		String summary = request.getParameter("summary");
		if(summary != null && !"".equals(summary)){
			summary = summary.replace("\t", "");
			summary = summary.replaceAll("\\t", "");
		}
		if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
			id = Long.valueOf(request.getParameter("searchId"));
			BkInfo bkInfo = bkFacade.findBkInfo((int)id);
			BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, 1);
			name = bookInfo.getName();
			author = bookInfo.getAuthorName();
			pic = bookInfo.getPagePic();
			if("".equals(pic)){
				pic = CommentUtils.WEB_PUBLIC_BOOKPIC;
			}
			score = request.getParameter("score");
			ahref = "book="+id;
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
			id = Long.valueOf(request.getParameter("searchId"));
			MvInfo mvInfo = mvFacade.queryById(id);
			MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, 1);
			name = movieInfo.getName();
			List<String> list = movieInfo.getDirector();
			if(list != null && list.size()>0){
				author = list.get(0);
			}else{
				author = "";
			}
			pic = movieInfo.getMoviePic();
			if("".equals(pic)){
				pic = CommentUtils.WEB_PUBLIC_BOOKPIC;
			}
			score = request.getParameter("score");
			ahref = "movie="+id;
		}
		
		//0id的情况
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
		
		content = WebUtils.replaceWHFromImg(content);
		
		String html = WebUtils.HTML_CSS_FOR_ARTICLE_BEGIN+ "<div class=\"title\">"+title
				+"</div><div class=\"author\"><span style=\"float: left;\">" +
				"发布者:"+userAllInfo.getName()+"&nbsp;&nbsp;"+
				sf.format(data)+
				"</span><span style=\"float: right;\">" +
				"阅读量:<span id=\"readNum\"></span>" +
				"</span></div><br/>"
				+hasImage(imageUrl) + "<hr SIZE=1 color=\"#f0f0f0\"  />";
		if(id != 0){
			html = html+WebUtils.HTML_CSS_FOR_ARTICLE_BOOK_MOVIE +
					WebUtils.HTML_DIV_FOR_ARTICLE_BOOK_MOVIE0+ahref+
					WebUtils.HTML_DIV_FOR_ARTICLE_BOOK_MOVIE1+pic+WebUtils.HTML_DIV_FOR_ARTICLE_BOOK_MOVIE2+name+
					WebUtils.HTML_DIV_FOR_ARTICLE_BOOK_MOVIE3+author+WebUtils.HTML_DIV_FOR_ARTICLE_BOOK_MOVIE4+score+
					WebUtils.HTML_DIV_FOR_ARTICLE_BOOK_MOVIE5;
		}
		html = html+"<div class=\"content content_position\">"+content+"</div>"+WebUtils.HTML_CSS_FOR_ARTICLE_END;
		if("".equals(imageUrl)){
			imageUrl = pic;
		}
		title = WebUtils.putStringImageVideoAudioToHTML5(title, imageUrl, null, null);
		int flagint = 0;
		String filePath = savePath+UUID.randomUUID()+".html";
		flagint = fileUtils.writeFile(filePath, html);
		Post post = new Post();
		if(flagint == ResultUtils.SUCCESS){
			String url = httpUtils.uploadFile(filePath, HttpUtils.HTTPUTIL_UPLOAD_FILE_URL_FOR_ARTICLE);
			
			post = postFacade.addPost(type, title, url, uid, summary);//(type, title, url, uid);	
		}
		flagint = post.getFlag();
		
		//写长文章成功后添加到缓存
		if(post != null && post.getId() != 0){
			resourceManager.setResourceToJedis(post, uid,uid,0l);
		}
		
		map.put("flag", flagint);
		
		
		return map;
	}
	/**
	 * 显示修改长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map viewUpdateArticle(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		Long id = Long.valueOf(request.getParameter("id"));
		
		
		Post post = postFacade.queryByIdName(id);
		String title = "";
		String imageUrl = "";
		String content = "";
		List<Map<String, String>> list = WebUtils.putHTMLToData(post.getName());
		for (Map<String, String> map2 : list) {
			if(WebUtils.TYPE_DIV.equals(map2.get(WebUtils.TYPE))){
				title = map2.get(WebUtils.DATA);
			}else if(WebUtils.TYPE_IMG.equals(map2.get(WebUtils.TYPE))){
				imageUrl = map2.get(WebUtils.DATA);
			}
		}
		
		content = CheckParams.getContentFromHtmlByArticle(fileUtils.readHTMl(post.getContent()));
		
		
		int flagint = post.getFlag();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			request.setAttribute("id", id);
			request.setAttribute("title", title);
			request.setAttribute("imageUrl", imageUrl);
			request.setAttribute("content", content);
			
		}
		
		map.put("flag", flagint);
		
		
		return map;
	}
	/**
	 * 修改长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map updateArticle(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		Long id = Long.valueOf(request.getParameter("id"));
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("picUrl");
		String content = request.getParameter("content");
		String summary = request.getParameter("summary");
		if(summary != null && !"".equals(summary)){
			summary = summary.replace("\t", "");
			summary = summary.replaceAll("\\t", "");
		}
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
		
		content = WebUtils.replaceWHFromImg(content);
		content = WebUtils.HTML_CSS_FOR_ARTICLE_BEGIN+ "<div class=\"title\">"+title
				+"</div><div class=\"author\"><span style=\"float: left;\">" +
				"作者:"+userAllInfo.getName()+"&nbsp;&nbsp;"+
				sf.format(data)+
				"</span><span style=\"float: right;\">" +
				"阅读量:<span id=\"readNum\"></span>" +
				"</span></div><br/>"
				+hasImage(imageUrl)
				+"<hr SIZE=1 color=\"#f0f0f0\"  />" +
				"<div class=\"content content_position\">"+content+"</div>"+WebUtils.HTML_CSS_FOR_ARTICLE_END;
		title = WebUtils.putStringImageVideoAudioToHTML5(title, imageUrl, null, null);
		int flagint = 0;
		String filePath = savePath+UUID.randomUUID()+".html";
		flagint = fileUtils.writeFile(filePath, content);
		Post post = new Post();
		if(flagint == ResultUtils.SUCCESS){
			String url = httpUtils.uploadFile(filePath, HttpUtils.HTTPUTIL_UPLOAD_FILE_URL_FOR_ARTICLE);
			
			post = postFacade.updateByIdPost(id, title, uid, url, summary);//(id, title, uid, url);
		}
		flagint = post.getFlag();
		
		map.put("flag", flagint);
		//修改长文章成功后添加进缓存
		if(post != null && post.getId() != 0){
			resourceManager.setResourceToJedis(post, uid,uid,0l);
		}
		
		return map;
	}
	
	private String hasImage(String imageUrl){
		if(imageUrl == null || "".equals(imageUrl)){
			return "";
		}else{
			return "<img id=\"articlePic\" src=\""+imageUrl+"\" />" ;
		}
	}
	/**
	 * 删除长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map delArticle(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		Long id = Long.valueOf(request.getParameter("id"));
		
		int flagint = 0;
		Post post = postFacade.deleteByIdPost(id);
		flagint = post.getFlag();
		
		map.put("flag", flagint);
		
		//修改长文章成功后添加进缓存
		if(post != null && post.getId() != 0){
			resourceManager.delResourceFromJedis(uid, id, null);//ResourceToJedis(post, uid);
		}
		
		return map;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	
	//==============================以下是新的web相关的接口====================================
	
	public void setResourceJedisManager(ResourceJedisManager resourceJedisManager) {
		this.resourceJedisManager = resourceJedisManager;
	}
	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}
	
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}


	private static final String articletype = "34";//新长文章类型值
	
	/**
	 * 某个人的长文章列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getArticleList(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		String pagenumStr = request.getParameter("pagenum");
		int pagenum = 1;
		if(StringUtils.isInteger(pagenumStr)){
			pagenum = Integer.parseInt(pagenumStr);
		}
		if(pagenum<1){
			pagenum = 1;
		}
		int pagesize = 10;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		Map<String,Object> datas = new HashMap<String,Object>();
		List<Article> articles = articleFacade.queryByTypeUid(articletype, uid,start,pagesize);
		if(!(articles!=null && articles.size()==1 && articles.get(0).getFlag()==ResultUtils.QUERY_ERROR)){
			Map<String,Object> amountmap = articleFacade.findArticleCount(uid);
			long amount = 0;
			if(amountmap!=null){
				amount = (Integer) amountmap.get("count");
			}
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(); 
			resourceInfos = getResponseList(articles, uid, resourceInfos);
			Collections.sort(resourceInfos);
			request.setAttribute("list", resourceInfos);
			datas.put("list", resourceInfos);
			datas.put("amount", amount);
			datas.put("pagesize", pagesize);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 写长文章或更新长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveArticle(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		Long did = 0L;
		String didstr = request.getParameter("did");
		if(StringUtils.isInteger(didstr)){
			did = Long.valueOf(didstr);
		}
		
		Long id = 0L;
		String idstr = request.getParameter("id");
		if(StringUtils.isInteger(idstr)){
			id = Long.valueOf(idstr);
		}
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("picUrl");
		String content = request.getParameter("content");
		String type = request.getParameter("type");
		String summary = request.getParameter("summary");
		String atypeStr = request.getParameter("atype");
		if(summary != null && !"".equals(summary)){
			summary = summary.replace("\t", "");
			summary = summary.replaceAll("\\t", "");
		}else if(summary==null){
			summary = "";
		}
		if(type==null){
			type = articletype;
		}
		int atype = 0;
		if(StringUtils.isInteger(atypeStr)){
			atype = Integer.valueOf(atypeStr);
		}
		
		String resString = null;
		if(title==null || title.trim().length()==0){
			String error = "文章标题不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		if(imageUrl==null || imageUrl.trim().length()==0){
			String error = "文章封面不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		if(content==null || content.trim().length()==0){
			String error = "文章内容不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		//0id的情况
		content = WebUtils.replaceWHFromImg(content);
		Article article = new Article();
		if(id>0){
			//更新 文章
			article = articleFacade.updateByIdArticle(id, title,imageUrl,uid, content, summary,atype);
		}else{
			//写 新文章
			article = articleFacade.addArticle(type, title, imageUrl, content, uid, summary,atype);
		}
		
		//写长文章成功后添加到缓存
		if(article != null && article.getId() != 0){
			summary = HtmlUtil.getTextFromHtml(article.getContent());
			article.setContent("");
			if(summary!=null){
				summary = summary.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
			}
			if(summary!=null && summary.length()>50){
				summary = summary.substring(0,50);
			}
			article.setSummary(summary);
			if(article.getName()!=null){
				String name = article.getName().replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
				article.setName(name);
			}
			resourceManager.setResourceToJedis(article, uid,uid,null);
			//需要判断是否存在草稿，如果存在则删除
			articleFacade.deleteByIdArticleDraft(did);
			articleFacade.deleteArticleDraftByAid(article.getId());
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(article.getFlag());
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 删除长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String deleteArticle(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		
		Article article = articleFacade.deleteByIdArticle(id);
		int flagint = article.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		
		//修改长文章成功后添加进缓存
		if(article != null && article.getId() != 0){
			resourceManager.delResourceFromJedis(uid, id, null);//ResourceToJedis(post, uid);
		}
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 显示修改长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getUpdateArticle(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		
		Article article = articleFacade.queryArticleById(id);	
		
		int flagint = article.getFlag();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			datas.put("map", article);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 修改长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveUpdateArticle(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		Long id = 0L;
		String idstr = request.getParameter("id");
		if(StringUtils.isInteger(idstr)){
			id = Long.valueOf(idstr);
		}
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("picUrl");
		String content = request.getParameter("content");
		String summary = request.getParameter("summary");
		String atypeStr = request.getParameter("atype");
		if(summary != null && !"".equals(summary)){
			summary = summary.replace("\t", "");
			summary = summary.replaceAll("\\t", "");
		}else if(summary==null){
			summary = "";
		}
		int atype = 0;
		if(StringUtils.isInteger(atypeStr)){
			atype = Integer.valueOf(atypeStr);
		}
		String resString = null;
		if(id<=0){
			String error = "文章id不正确";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		if(title==null || title.trim().length()==0){
			String error = "文章标题不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		if(imageUrl==null || imageUrl.trim().length()==0){
			String error = "文章封面不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		if(content==null || content.trim().length()==0){
			String error = "文章内容不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		content = WebUtils.replaceWHFromImg(content);
		Article article = new Article();
		article = articleFacade.updateByIdArticle(id, title,imageUrl,uid, content, summary,atype);//(id, title, uid, url);
		int flagint = article.getFlag();
		
		//修改长文章成功后添加进缓存
		if(article != null && article.getId() != 0){
			summary = HtmlUtil.getTextFromHtml(article.getContent());
			article.setContent("");
			if(summary!=null){
				summary = summary.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
			}
			if(summary!=null && summary.length()>50){
				summary = summary.substring(0,50);
			}
			article.setSummary(summary);
			if(article.getName()!=null){
				String name = article.getName().replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
				article.setName(name);
			}
			resourceManager.setResourceToJedis(article, uid,uid,null);
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error",error);
		}
		datas.put("flag", flag);
		
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 上传图片
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String uploadArticleImg(HttpServletRequest request){
		Map<String,Object> datas = new HashMap<String,Object>();
		
		String uploadPath = "";//上传到服务器的图片地址
		
		
		Map<String, String> map = httpUtils.uploadImage(request,savePath);
		String savePath = map.get("savePath");
		String flag = map.get("flag");
		String error = map.get("error");
		
		//datas.put("savePath", savePath);
		datas.put("flag", flag);
		
		if("0".equals(flag)){
			uploadPath = httpUtils.uploadFile(savePath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_ARTICLE);
			
			Map<String,Object> datamap = new HashMap<String,Object>();
			
			datamap.put("uploadPath", uploadPath);
			//datamap.put("error", "上传成功");
			
			datas.put("map", datamap);
		}else{
			datas.put("error", error);
		}
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 查询文章详情
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getArticleInfo(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		String articleidStr = request.getParameter("id");
		long articleid = 0;
		if(StringUtils.isInteger(articleidStr)){
			articleid = Long.valueOf(articleidStr);
		}
		Map<String,Object> datas = new HashMap<String,Object>();
		String resString = null;
		if(articleid<=0){
			String error = "文章id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		Article article = articleFacade.queryArticleById(articleid);
		if(!(article!=null && ResultUtils.QUERY_ERROR==article.getFlag())){
			//更新阅读量
			Map<String,Object> map = articleFacade.updateArticleReadingCount(articleid,uid);
			Integer flagint = (Integer) map.get("flag");
			if(flagint!=null && flagint==ResultUtils.SUCCESS){
				try{
					article.setReadingCount(article.getReadingCount()+1);
					article.setFalsereading(article.getFalsereading()+(Integer)map.get("num"));
					resStatJedisManager.setReadNum(articleid, CommentUtils.TYPE_NEWARTICLE,0,"",0,article.getReadingCount(),article.getFalsereading());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			ResourceInfo resourceInfo = new ResourceInfo(); 
			try {
				resourceInfo = fileUtils.putObjectToResource(article, ucenterFacade, actFacade);
				//重新放入缓存
				String result = resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
			} catch (Exception e) {
				e.printStackTrace();
				resourceInfo = new ResourceInfo();
			}
			//resourceInfo = resourceManager.putObjectToResource(article, uid, 1);
			datas.put("map", resourceInfo);
			//需要查询是否存在草稿，如果有草稿则返回存在信息
			ArticleDraft  articleDraft = articleFacade.queryArticleDraftByAid(articleid);
			if(articleDraft!=null && articleDraft.getId()>0){
				datas.put("did", articleDraft.getId());//草稿id
			}
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		
		return resString;
	}
	/**
	 * 根据标题和时间模糊查询文章列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String searchArticleList(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		String title = request.getParameter("title");
		String starttimestr = request.getParameter("starttime");
		String endtimestr = request.getParameter("endtime");
		Long starttime = null;
		Long endtime = null;
		String format = "yyyy-MM-dd HH:mm:ss";
		if(starttimestr!=null && endtimestr!=null && starttimestr.trim().length()>0 && endtimestr.trim().length()>0){
			starttime = DateUtil.formatLong(starttimestr+" 00:00:00", format);
			endtime = DateUtil.formatLong(endtimestr+" 23:59:59", format);
		}else{
			if(starttimestr!=null && starttimestr.trim().length()>0){
				starttime = DateUtil.formatLong(starttimestr+" 00:00:00", format);
				endtime = DateUtil.formatLong(starttimestr+" 23:59:59", format);
			}else if(endtimestr!=null && endtimestr.trim().length()>0){
				starttime = DateUtil.formatLong(endtimestr+" 00:00:00", format);
				endtime = DateUtil.formatLong(endtimestr+" 23:59:59", format);
			}
		}
		String pagenumStr = request.getParameter("pagenum");
		int pagenum = 1;
		if(StringUtils.isInteger(pagenumStr)){
			pagenum = Integer.parseInt(pagenumStr);
		}
		if(pagenum<1){
			pagenum = 1;
		}
		int pagesize = 10;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		Map<String,Object> datas = new HashMap<String,Object>();
		List<Article> articles = articleFacade.searchArticleByTitle(null, uid, title, starttime, endtime, start, pagesize);
		if(!(articles!=null && articles.size()==1 && articles.get(0).getFlag()==ResultUtils.QUERY_ERROR)){
			Map<String,Object> amountmap = articleFacade.findArticleCountByLike(null, uid, title, starttime, endtime);
			long amount = 0;
			if(amountmap!=null){
				amount = (Integer) amountmap.get("count");
			}
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(); 
			resourceInfos = getResponseList(articles, uid, resourceInfos);
			//Collections.sort(resourceInfos);
			request.setAttribute("list", resourceInfos);
			datas.put("list", resourceInfos);
			datas.put("amount", amount);
			datas.put("pagesize", pagesize);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 将草稿发布为正式文章
	 * @Title: publicArticle 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-6 上午10:59:06
	 * @param @param request
	 * @param @param uid
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String publicArticle(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		String didStr = request.getParameter("id");
		long did = 0;
		if(StringUtils.isInteger(didStr)){
			did = Long.valueOf(didStr);
		}
		Map<String,Object> datas = new HashMap<String,Object>();
		String resString = null;
		if(did<=0){
			String error = "文章id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		Article article = articleFacade.publicArticle(did, uid);
		if(article.getFlag()==ResultUtils.SUCCESS){
			//保存到缓存中
			String summary = HtmlUtil.getTextFromHtml(article.getContent());
			article.setContent("");
			if(summary!=null){
				summary = summary.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
			}
			if(summary!=null && summary.length()>50){
				summary = summary.substring(0,50);
			}
			article.setSummary(summary);
			if(article.getName()!=null){
				String name = article.getName().replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
				article.setName(name);
			}
			resourceManager.setResourceToJedis(article, uid,uid,null);
			//ResourceInfo resourceInfo = new ResourceInfo();
			//resourceInfo = resourceManager.putObjectToResource(article, uid, 1);
			//datas.put("map", resourceInfo);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(article.getFlag());
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		
		return resString;
	}
	
	/**
	 * 保存草稿，新增或更新
	 * @Title: saveArticle 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-6 上午11:28:22
	 * @param @param request
	 * @param @param uid
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String saveArticleDraft(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		Long id = 0L;
		String idstr = request.getParameter("id");
		if(StringUtils.isInteger(idstr)){
			id = Long.valueOf(idstr);
		}
		Long aid = 0L;
		String aidstr = request.getParameter("aid");
		if(StringUtils.isInteger(aidstr)){
			aid = Long.valueOf(aidstr);
		}
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("picUrl");
		String content = request.getParameter("content");
		String type = request.getParameter("type");
		String summary = request.getParameter("summary");
		String atypeStr = request.getParameter("atype");
		if(summary != null && !"".equals(summary)){
			summary = summary.replace("\t", "");
			summary = summary.replaceAll("\\t", "");
		}else if(summary==null){
			summary = "";
		}
		if(type==null){
			type = articletype;
		}
		int atype = 0;
		if(StringUtils.isInteger(atypeStr)){
			atype = Integer.valueOf(atypeStr);
		}
		
		String resString = null;
		if(title==null || title.trim().length()==0){
			String error = "文章标题不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		/*if(imageUrl==null || imageUrl.trim().length()==0){
			String error = "文章封面不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}*/
		
		if(content==null || content.trim().length()==0){
			String error = "文章内容不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		//0id的情况
		content = WebUtils.replaceWHFromImg(content);
		ArticleDraft articleDraft = null;
		if(id>0){
			//更新 文章
			articleDraft = articleFacade.updateByIdArticleDraft(id, title,imageUrl,uid, content, summary,atype);
		}else{
			//写 新文章
			articleDraft = articleFacade.addArticleDraft(type, title, imageUrl, content, uid, summary,atype,aid);
		}
		
		//保存草稿成功后需要返回给前端
		if(articleDraft != null && articleDraft.getId() != 0){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("did", articleDraft.getId());
			datas.put("map", map);
		}else{
			String error = MessageUtils.getResultMessage(articleDraft.getFlag());
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 删除草稿
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String deleteArticleDraft(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		
		ArticleDraft articleDraft = articleFacade.deleteByIdArticleDraft(id);
		int flagint = articleDraft.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 某个人的草稿列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getArticleDraftList(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		String pagenumStr = request.getParameter("pagenum");
		int pagenum = 1;
		if(StringUtils.isInteger(pagenumStr)){
			pagenum = Integer.parseInt(pagenumStr);
		}
		if(pagenum<1){
			pagenum = 1;
		}
		int pagesize = 10;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		Map<String,Object> datas = new HashMap<String,Object>();
		List<ArticleDraft> articleDrafts = articleFacade.queryArticleDraftByTypeUid(null, uid, start, pagesize);
		if(!(articleDrafts!=null && articleDrafts.size()==1 && articleDrafts.get(0).getFlag()==ResultUtils.QUERY_ERROR)){
			Map<String,Object> amountmap = articleFacade.findArticleDraftCount(uid);
			long amount = 0;
			if(amountmap!=null){
				amount = (Integer) amountmap.get("count");
			}
			datas.put("list", articleDrafts);
			datas.put("amount", amount);
			datas.put("pagesize", pagesize);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 查询草稿详情
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getArticleDraftInfo(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		String didStr = request.getParameter("id");
		long did = 0;
		if(StringUtils.isInteger(didStr)){
			did = Long.valueOf(didStr);
		}
		Map<String,Object> datas = new HashMap<String,Object>();
		String resString = null;
		if(did<=0){
			String error = "文章id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		ArticleDraft articleDraft = articleFacade.queryArticleDraftById(did);
		if(articleDraft.getFlag()==ResultUtils.SUCCESS){
			datas.put("map", articleDraft);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		
		return resString;
	}
	
	//保存正式文章的时候，需要判断是否有草稿，有草稿则删除
	//查询文章详情的时候，需要查询是否 存在草稿，为了编辑时候提示用户是否使用该草稿
	
	
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getResponseList(List<Article> reqList , Long uid , List<ResourceInfo> resList){
		ResourceInfo ri = null;
		
		if(reqList!=null && reqList.size()>0){
			if(reqList.get(0).getId()!= 0){
				//flagint = ResultUtils.SUCCESS;
				for (Article obj : reqList) {
					try {
						ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade);
						resList.add(ri);
					} catch (Exception e) {
						e.printStackTrace();
						//ri = new ResourceInfo();
						//LOG.error("长文章资源显示出错，资源ID为："+((Article)obj).getId()+e.getMessage(), e.fillInStackTrace());
					}
				}
			}
		}else{
			//flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getPostResponseList(List<Post> reqList , Long uid , List<ResourceInfo> resList){
		ResourceInfo ri = null;
		
		if(reqList!=null && reqList.size()>0){
			if(reqList.get(0).getId()!= 0){
				//flagint = ResultUtils.SUCCESS;
				for (Post obj : reqList) {
					try {
						ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade);
						resList.add(ri);
					} catch (Exception e) {
						e.printStackTrace();
						//ri = new ResourceInfo();
						//LOG.error("长文章资源显示出错，资源ID为："+((Article)obj).getId()+e.getMessage(), e.fillInStackTrace());
					}
				}
			}
		}else{
			//flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
}
