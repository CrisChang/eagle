package com.poison.eagle.utils;

import java.util.HashMap;
import java.util.Map;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActSubscribe;
import com.poison.eagle.entity.BigSelectInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserBigInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.msg.model.MsgAt;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.model.BigLevelValue;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Serialize;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BigSelecting;
import com.poison.store.model.BkInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserBigValue;

public class JedisApiUtils {
	private static JedisApiUtils actUtils;

	public JedisApiUtils() {
	}

	public static JedisApiUtils getInstance() {
		if (actUtils == null) {
			return new JedisApiUtils();
		} else {
			return actUtils;
		}
	}

	private FileUtils fileUtils = FileUtils.getInstance();

	/**
	 * 将调用set缓存的json转换为bkcomment
	 * @param data
	 * @return
	 */
	public BkComment putJedisJsonToBkComment(Map<String, Object> data){
		BkComment bkComment = new BkComment();
		
		Long id = Long.valueOf(String.valueOf(data.get("id")));
		long user_id = Long.valueOf(String.valueOf(data.get("user_id")));
		long book_id = Long.valueOf(String.valueOf(data.get("book_id")));
		String comment = (String) data.get("comment");
		String description = (String) data.get("description");
		Float big_value = Float.valueOf(String.valueOf(data.get("big_value")));
		String score = (String) data.get("score");
		String type = (String) data.get("type");
		int is_db = Integer.valueOf(String.valueOf(data.get("is_db")));
		int is_opposition = Integer.valueOf(String.valueOf(data.get("is_opposition")));
		int is_delete = Integer.valueOf(String.valueOf(data.get("is_delete")));
		Long create_date = Long.valueOf(String.valueOf(data.get("create_date")));
		Long latest_revision_date = Long.valueOf(String.valueOf(data.get("latest_revision_date")));
		String title = (String) data.get("title");
		
		bkComment.setId(id);
		bkComment.setUserId(user_id);
		bkComment.setBookId((int)book_id);
		bkComment.setComment(comment);
		bkComment.setDescription(description);
		bkComment.setBigValue(big_value);
		bkComment.setScore(score);
		bkComment.setType(type);
		bkComment.setIsDb(is_db);
		bkComment.setIsOpposition(is_opposition);
		bkComment.setCreateDate(create_date);
		bkComment.setLatestRevisionDate(latest_revision_date);
		bkComment.setTitle(title);
		
		return bkComment;
	}
	/**
	 * 将调用set缓存的json转换为MvComment
	 * @param data
	 * @return
	 */
	public MvComment putJedisJsonToMvComment(Map<String, Object> data){
		MvComment mvComment = new MvComment();
		
		Long id = Long.valueOf(String.valueOf(data.get("id")));
		Long user_id = Long.valueOf(String.valueOf(data.get("user_id")));
		int movie_id = (Integer) data.get("movie_id");
		String content = (String) data.get("content");
		String description = (String) data.get("description");
		Float big_value = Float.valueOf(String.valueOf(data.get("big_value")));
		String score = (String) data.get("score");
		String type = (String) data.get("type");
		int is_db = Integer.valueOf(String.valueOf(data.get("is_db")));
		int is_opposition = Integer.valueOf(String.valueOf(data.get("is_opposition")));
		int is_delete = Integer.valueOf(String.valueOf(data.get("is_delete")));
		Long create_date = Long.valueOf(String.valueOf(data.get("create_date")));
		Long latest_revision_date = Long.valueOf(String.valueOf(data.get("latest_revision_date")));
		String title = (String) data.get("title");

		mvComment.setId(id);
		mvComment.setUserId(user_id);
		mvComment.setMovieId(movie_id);
		mvComment.setContent(content);
		mvComment.setDescription(description);
		mvComment.setBigValue(big_value);
		mvComment.setScore(score);
		mvComment.setType(type);
		mvComment.setIsDB(is_db);
		mvComment.setIsOpposition(is_opposition);
		mvComment.setCreateDate(create_date);
		mvComment.setLatestRevisionDate(latest_revision_date);
		mvComment.setTitle(title);
		
		return mvComment;
	}
	/**
	 * 将调用set缓存的json转换为MvComment
	 * @param data
	 * @return
	 */
	public ActPublish putJedisJsonToActPublish(Map<String, Object> data){
		ActPublish actPublish = new ActPublish();
		
		Long id = Long.valueOf(String.valueOf(data.get("id")));
		Long user_id = Long.valueOf(String.valueOf(data.get("user_id")));
		Long resource_id = Long.valueOf(String.valueOf(data.get("resource_id")));
		String type = (String) data.get("type");
		String recommend_type = (String) data.get("recommend_type");
		int is_delete = Integer.valueOf(String.valueOf(data.get("is_delete")));
		String publish_context = (String) data.get("publish_context");
		Long publish_date = Long.valueOf(String.valueOf(data.get("publish_date")));
		Long latest_revision_date = Long.valueOf(String.valueOf(data.get("latest_revision_date")));
		
		actPublish.setId(id);
		actPublish.setUserId(user_id);
		actPublish.setResourceId(resource_id);
		actPublish.setType(type);
		actPublish.setRecommendType(recommend_type);
		actPublish.setIsDelete(is_delete);
		actPublish.setPublishContext(publish_context);
		actPublish.setPublishDate(publish_date);
		actPublish.setLatestRevisionDate(latest_revision_date);
		
		return actPublish;
	}
	
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 199999999999l);
		map.put("user_id", 123l);
		map.put("resource_id", 199999999999l);
		map.put("recommend_type", "recommend_type");
		map.put("type", "type");
		map.put("is_delete", 0);
		map.put("publish_context", "publish_context");
		map.put("publish_date", 199999999999l);
		map.put("latest_revision_date", 199999999999l);
		
		JedisApiUtils jedisApiUtils = JedisApiUtils.getInstance();
//		BkComment bkComment = jedisApiUtils.putJedisJsonToBkComment(map);
//		MvComment mvComment = jedisApiUtils.putJedisJsonToMvComment(map);
		ActPublish actPublish = jedisApiUtils.putJedisJsonToActPublish(map);
		System.out.println(actPublish);
	}

}
