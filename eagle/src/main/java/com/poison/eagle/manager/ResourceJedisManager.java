package com.poison.eagle.manager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.JsonGenerationException;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.common.event.rocketmq.MessageRecv;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.ResultUtils;

public class ResourceJedisManager {

	private JedisSimpleClient resourceHashClient;

	public void setResourceHashClient(JedisSimpleClient resourceHashClient) {
		this.resourceHashClient = resourceHashClient;
	}
	
	/**
	 * 
	 * <p>Title: saveOneResource</p> 
	 * <p>Description: 存储一条资源信息</p> 
	 * @author :changjiang
	 * date 2015-2-28 下午12:39:17
	 * @param resId
	 * @param resourceInfo
	 * @return
	 */
	public String saveOneResource(final long resId,ResourceInfo resourceInfo){
		String itemStr="";
		if(null==resourceInfo){
			return "";
		}
		//需要区分书、网络小说和电影，因为书和电影都是自增id，id会重复，所以需要加上type类型用于区分
		String restype = resourceInfo.getType();//资源类型
		String resIdStr = resId+"";
		if(CommentUtils.TYPE_BOOK.equals(restype) || CommentUtils.TYPE_MOVIE.equals(restype) || CommentUtils.TYPE_NETBOOK.equals(restype)){
			resIdStr = resId + "_"+restype;
		}
		final String resIdKey = resIdStr;
		//========================================
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false) ;
			
			itemStr = objectMapper.writeValueAsString(resourceInfo);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//存入最终条目
		final String saveItem = itemStr;
		itemStr = resourceHashClient.execute(new JedisWorker<String>(){
			String resHashKey = JedisConstant.RESOURCE_HASH_ID+resIdKey;
			String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
			@Override
			public String work(Jedis jedis) {
				//System.out.println(resHashKey);
				jedis.hset(resHashKey, resHashValue, saveItem);
				//jedis.hs
				//System.out.println(jedis.hget(resHashKey, resHashValue));
				return jedis.hget(resHashKey, resHashValue);
			}
		});
		return itemStr;
	}


	/**
	 * 存入一条资源的缓存
	 * @param resId
	 * @param resourceInfoStr
	 * @return
	 */
	public String saveOneResourceStr(final long resId,final String resourceInfoStr){
		String itemStr="";
		if(null==resourceInfoStr){
			return "";
		}
		final String resIdKey = resId+"";
		//存入最终条目
		itemStr = resourceHashClient.execute(new JedisWorker<String>(){
			String resHashKey = JedisConstant.RESOURCE_HASH_ID+resIdKey;
			String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
			@Override
			public String work(Jedis jedis) {
				//System.out.println(resHashKey);
				jedis.hset(resHashKey, resHashValue, resourceInfoStr);
				//jedis.hs
				//System.out.println(jedis.hget(resHashKey, resHashValue));
				return jedis.hget(resHashKey, resHashValue);
			}
		});
		return itemStr;
	}
	
	/**
	 * 
	 * <p>Title: delOneResource</p> 
	 * <p>Description: 删除一条资源信息</p> 
	 * @author :changjiang
	 * date 2015-2-28 下午12:39:38
	 * @param rid
	 * @return
	 */
	public int delOneResource(final long rid){
		int flag =ResultUtils.ERROR;
		resourceHashClient.execute(new JedisWorker<Integer>(){
			String resHashKey = JedisConstant.RESOURCE_HASH_ID+rid;
			String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
			@Override
			public Integer work(Jedis jedis) {
				//System.out.println(jedis.hdel(resHashKey, resHashValue));
				int status = ResultUtils.DATAISNULL;
				long index = jedis.hdel(resHashKey, resHashValue);
				if(index>0){
					status = ResultUtils.SUCCESS;
				}
				return status;
			}
		});
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: getOneResource</p> 
	 * <p>Description: 得到一条资源的信息</p> 
	 * @author :changjiang
	 * date 2015-4-17 下午5:27:36
	 * @param rid
	 * @return
	 */
	public String getOneResource(final long rid){
		String resultStr = resourceHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String resHashKey = JedisConstant.RESOURCE_HASH_ID+rid;
				String resHashField = JedisConstant.RESOURCE_HASH_INFO;
				String resHashValue = jedis.hget(resHashKey, resHashField);
				return resHashValue;
			}
		});
		return resultStr;
	}
	
	
	/**
	 * 
	 * <p>Title: getOneResource</p> 
	 * <p>Description: 得到一条资源的信息根据判断资源类型</p> 
	 * @author :changjiang
	 * date 2015-4-17 下午5:27:36
	 * @param rid
	 * @return
	 */
	public String getOneResourceWithType(final long rid,String restype){
		//需要区分书、网络小说和电影，因为书和电影都是自增id，id会重复，所以需要加上type类型用于区分
		String ridStr = rid+"";
		if(CommentUtils.TYPE_BOOK.equals(restype) || CommentUtils.TYPE_MOVIE.equals(restype) || CommentUtils.TYPE_NETBOOK.equals(restype)){
			ridStr = rid + "_"+restype;
		}
		final String ridkey = ridStr;
		//========================================
		
		String resultStr = resourceHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String resHashKey = JedisConstant.RESOURCE_HASH_ID+ridkey;
				String resHashField = JedisConstant.RESOURCE_HASH_INFO;
				String resHashValue = jedis.hget(resHashKey, resHashField);
				return resHashValue;
			}
		});
		return resultStr;
	}
}
