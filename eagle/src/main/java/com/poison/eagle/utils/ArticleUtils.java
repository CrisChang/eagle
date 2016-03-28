package com.poison.eagle.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.eagle.entity.BookTalkInfo;
import com.poison.resource.model.Article;

public class ArticleUtils {
	private static final  Log LOG = LogFactory.getLog(ArticleUtils.class);
	private static ArticleUtils bookUtils;

	public ArticleUtils() {
	}

	public static ArticleUtils getInstance() {
		if (bookUtils == null) {
			return new ArticleUtils();
		} else {
			return bookUtils;
		}
	}

	private FileUtils fileUtils = FileUtils.getInstance();

	/**
	 * 将根据用户标签查询的运营书单json转换为书单列表
	 * @param json
	 * @return
	 */
	public List<Article> putJsonListToArticleList(List<Map<String, Object>> json){
		List<Article> articles = new ArrayList<Article>();
		
		Iterator<Map<String, Object>> iter = json.iterator();
		while(iter.hasNext()){
			Map<String, Object> p = iter.next();
			Article article = new Article();
			article = putJsonToArticle(p);
			if(article != null && article.getId() != 0){
				articles.add(article);
			}
		}
		
		
		return articles;
	}
	
	/**
	 * 将根据用户标签查询的运营书单长文章json转换为长文章
	 * @param json
	 * @return
	 */
	public Article putJsonToArticle(Map<String, Object> json){
		Article article = new Article();
		try {
			if(json.containsKey("id")){
				Long id = Long.valueOf(json.get("id").toString());
				article.setId(id);
			}
			if(json.containsKey("u_id")){
				Long uid = Long.valueOf(json.get("u_id").toString());
				article.setUid(uid);
			}
			if(json.containsKey("count")){
				Integer count = Integer.valueOf(json.get("count").toString());
				article.setReadingCount(count);
			}
			if(json.containsKey("type")){
				String type = (String) json.get("type");
				article.setType(type);
			}
			if(json.containsKey("name")){
				
				String name = (String) json.get("name");
				article.setName(name);
			}
			if(json.containsKey("summary")){
				
				String summary = (String) json.get("summary");
				article.setSummary(summary);
			}
			if(json.containsKey("content")){
				String content = (String) json.get("content");
				article.setContent(content);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			article = new Article();
		}
		
		return article;
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		BookTalkInfo info = new BookTalkInfo();

		for (Integer i : list) {
			if (info.getPage() != i) {
				info.setPage(i);
				info.getBookTalk().add(i);
			}
		}

	}

}
