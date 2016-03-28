package com.poison.eagle.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActPublish;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.BookTalkInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.Post;
import com.poison.store.client.BkFacade;
import com.poison.store.model.BkInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;

public class PostUtils {
	private static final  Log LOG = LogFactory.getLog(PostUtils.class);
	private static PostUtils bookUtils;

	public PostUtils() {
	}

	public static PostUtils getInstance() {
		if (bookUtils == null) {
			return new PostUtils();
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
	public List<Post> putJsonListToPostList(List<Map<String, Object>> json){
		List<Post> posts = new ArrayList<Post>();
		
		Iterator<Map<String, Object>> iter = json.iterator();
		while(iter.hasNext()){
			Map<String, Object> p = iter.next();
			Post post = new Post();
			post = putJsonToPost(p);
			if(post != null && post.getId() != 0){
				posts.add(post);
			}
		}
		
		
		return posts;
	}
	
	/**
	 * 将根据用户标签查询的运营书单长文章json转换为长文章
	 * @param json
	 * @return
	 */
	public Post putJsonToPost(Map<String, Object> json){
		Post post = new Post();
		try {
			if(json.containsKey("id")){
				Long id = Long.valueOf(json.get("id").toString());
				post.setId(id);
			}
			if(json.containsKey("u_id")){
				Long uid = Long.valueOf(json.get("u_id").toString());
				post.setUid(uid);
			}
			if(json.containsKey("count")){
				Integer count = Integer.valueOf(json.get("count").toString());
				post.setReadingCount(count);
			}
			if(json.containsKey("type")){
				String type = (String) json.get("type");
				post.setType(type);
			}
			if(json.containsKey("name")){
				
				String name = (String) json.get("name");
				post.setName(name);
			}
			if(json.containsKey("summary")){
				
				String summary = (String) json.get("summary");
				post.setSummary(summary);
			}
			if(json.containsKey("content")){
				String content = (String) json.get("content");
				post.setContent(content);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			post = new Post();
		}
		
		return post;
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
