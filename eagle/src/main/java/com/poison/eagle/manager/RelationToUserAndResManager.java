package com.poison.eagle.manager;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.poison.eagle.utils.JedisConstant;

public class RelationToUserAndResManager {

	private static final  Log LOG = LogFactory.getLog(RelationToUserAndResManager.class);
	
	private JedisSimpleClient relationToUserandresClient;

	public void setRelationToUserandresClient(
			JedisSimpleClient relationToUserandresClient) {
		this.relationToUserandresClient = relationToUserandresClient;
	}
	
	/**
	 * 
	 * <p>Title: saveOneRelation</p> 
	 * <p>Description: 存储用户对资源的附加信息</p> 
	 * @author :changjiang
	 * date 2015-3-4 下午2:51:35
	 * @param userId
	 * @param resId
	 * @param key
	 * @param value
	 * @return
	 */
	public String saveOneRelation(final long userId,final long resId,final String key,final String value){
		//map.get("");
		String relationValue = "";
		relationValue = relationToUserandresClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_RES+resId;
				jedis.hset(relationkey, key, value);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: getOneRelation</p> 
	 * <p>Description: 获取用户对资源的附加信息</p> 
	 * @author :changjiang
	 * date 2015-3-4 下午3:33:46
	 * @param userId
	 * @param resId
	 * @return
	 */
	public Map<String, String> getOneRelation(final long userId,final long resId){
		Map<String, String> userAndResInfo = relationToUserandresClient.execute(new JedisWorker<Map<String, String>>(){
			@Override
			public Map<String, String> work(Jedis jedis) {
				String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_RES+resId;
				return jedis.hgetAll(relationkey);
			}
		});
		return userAndResInfo;
	}
	
	/**
	 * 
	 * <p>Title: saveOneRelation</p> 
	 * <p>Description: 存储资源的附加信息</p> 
	 * @author :changjiang
	 * date 2015-3-2 下午8:09:51
	 * @param userId
	 * @param resId
	 * @param key
	 * @param value
	 * @return
	 */
	public String saveOneRelationToRes(final long resId,final String key,final String value){
		//map.get("");
		String relationValue = "";
		relationValue = relationToUserandresClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = JedisConstant.RELATION_TO_RES_OTHERINFO+resId;
				jedis.hset(relationkey, key, value);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: getOneRelationToRes</p> 
	 * <p>Description: 获取资源的附加信息</p> 
	 * @author :changjiang
	 * date 2015-3-4 下午3:46:15
	 * @param resId
	 * @param key
	 * @return
	 */
	public Map<String, String> getOneRelationToRes(final long resId){
		Map<String, String> relationToRes = relationToUserandresClient.execute(new JedisWorker<Map<String, String>>(){
			@Override
			public Map<String, String> work(Jedis jedis) {
				String relationkey = JedisConstant.RELATION_TO_RES_OTHERINFO+resId;
				return jedis.hgetAll(relationkey);
			}
		});
		return relationToRes;
	}
	
	/**
	 * 
	 * <p>Title: saveOneRelationUserAndBook</p> 
	 * <p>Description: 增加用户对图书的关系</p> 
	 * @author :changjiang
	 * date 2015-3-3 下午4:01:24
	 * @param userId
	 * @param resId
	 * @param key
	 * @param value
	 * @return
	 */
	public String saveOneRelationUserAndBook(final long userId,final long resId,final String key,final String value){
		//map.get("");
		String relationValue = "";
		relationValue = relationToUserandresClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_BOOK+resId;
				jedis.hset(relationkey, key, value);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: saveOneRelationUserAndMovie</p> 
	 * <p>Description: 增加用户对电影的关系</p> 
	 * @author :changjiang
	 * date 2015-3-3 下午4:02:29
	 * @param userId
	 * @param resId
	 * @param key
	 * @param value
	 * @return
	 */
	public String saveOneRelationUserAndMovie(final long userId,final long resId,final String key,final String value){
		//map.get("");
		String relationValue = "";
		relationValue = relationToUserandresClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_MOVIE+resId;
				jedis.hset(relationkey, key, value);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: delOneRelation</p> 
	 * <p>Description: 删除一条资源关系</p> 
	 * @author :changjiang
	 * date 2015-3-3 上午10:49:29
	 * @param userId
	 * @param resId
	 * @return
	 */
	public String delOneRelation(final long userId,final long resId){
		relationToUserandresClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId + JedisConstant.RELATION_TO_USER_AND_RES+resId;
				jedis.del(relationkey);
				return null;
			}
		});
		return null;
	}
}
