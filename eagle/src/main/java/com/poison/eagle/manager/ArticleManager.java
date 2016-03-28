package com.poison.eagle.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.HtmlUtil;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.model.Article;

public class ArticleManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ArticleManager.class);
	
	private ArticleFacade articleFacade;
	
	private ResourceManager resourceManager;

	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}



	/**
	 * 写长文章或更新长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveArticle(String reqs,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		Long did = 0L;
		Long id = 0L;
		String title = null;
		String imageUrl = null;
		//String content = null;
		List contentList = null;
		String type = null;
		String summary = null;
		int atype = 0;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String didstr = dataq.get("did")+"";
			if(StringUtils.isInteger(didstr)){
				did = Long.valueOf(didstr);
			}
			String idstr = dataq.get("id")+"";
			if(StringUtils.isInteger(idstr)){
				id = Long.valueOf(idstr);
			}
			title = dataq.get("title")+"";
			imageUrl = dataq.get("picUrl")+"";
			//content = dataq.get("content")+"";
			contentList = (List) dataq.get("list");
			type = (String) dataq.get("type");
			summary = dataq.get("summary")+"";
			String atypeStr = dataq.get("atype")+"";
			if(StringUtils.isInteger(atypeStr)){
				atype = Integer.valueOf(atypeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>(3);
		
		if(summary != null && !"".equals(summary)){
			summary = summary.replace("\t", "");
			summary = summary.replaceAll("\\t", "");
		}else if(summary==null){
			summary = "";
		}
		if(type==null || "".equals(type)){
			type = CommentUtils.TYPE_NEWARTICLE;
		}
		
		if(title==null || title.trim().length()==0){
			error = "文章标题不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		if(imageUrl==null || imageUrl.trim().length()==0){
			error = "文章封面不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		Map<String, Object> map = WebUtils.putDataToMap(contentList, null);
		String resultContent = (String) map.get("resultContent");
		
		if(resultContent==null || resultContent.trim().length()==0){
			error = "文章内容不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		//0id的情况
		resultContent = WebUtils.addHtmlToAppArticle(resultContent);
		Article article = new Article();
		if(id>0){
			//更新 文章
			article = articleFacade.updateByIdArticle(id, title,imageUrl,uid, resultContent, summary,atype);
		}else{
			//写 新文章
			article = articleFacade.addArticle(type, title, imageUrl, resultContent, uid, summary,atype);
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
			//articleFacade.deleteByIdArticleDraft(did);
			//articleFacade.deleteArticleDraftByAid(article.getId());
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			error = MessageUtils.getResultMessage(article.getFlag());
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
	public String deleteArticle(String reqs,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		Long id = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			String idstr = dataq.get("id")+"";
			if(StringUtils.isInteger(idstr)){
				id = Long.valueOf(idstr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>(3);
		Article article = articleFacade.deleteByIdArticle(id);
		int flagint = article.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		
		//缓存
		if(article != null && article.getId() != 0){
			resourceManager.delResourceFromJedis(uid, id, null);//ResourceToJedis(post, uid);
		}
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
}