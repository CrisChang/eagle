package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.parser.Entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

import ch.qos.logback.classic.pattern.EnsureExceptionHandling;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.common.lang.BaseDO;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.entity.VersionInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeServiceUtil;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;

/**
 * 点击书籍电影manager
 * @author Administrator
 * 
 */
public class JedisManager extends BaseManager {
	private static final Log LOG = LogFactory
			.getLog(JedisManager.class);
	private final static String JEDIS_ID_LIST = "idList";
	public final static String JEDIS_RESOURCE_LIST = "resourceList";
	
	private JedisSimpleClient resourceVisitClient;
	private JedisSimpleClient userTagClient;
	private JedisSimpleClient resourceClient;
	
	private static final String USERTAG = "userid#";
	private static final String UID_LIST = "UIDLIST#";
	private static final String INDEXCOUNT = "INDEXCOUNT#";
	public static final String ARTICLE_INDEX = "ARTICLE_INDEX";
	public static final String BOOKLIST_INDEX = "BOOKLIST_INDEX";
	public static final String MOVIELIST_INDEX = "MOVIELIST_INDEX";
	public static final String BKCOMMENT_INDEX = "BKCOMMENT_INDEX";
	public static final String MVCOMMENT_INDEX = "MVCOMMENT_INDEX";
	public static final String RESOURCE_INDEX = "RESOURCE_INDEX";
	//新的缓存机制key
	private static final String RESOURCE_ALL = "RESOURCE_ALL";
	
	//新朋友圈的缓存机制key+uid
	private static final String CIRCLE_OF_FRIENDS = "CIRCLEOFFRIENDS_";
	
	/**
	 * 获取书电影书单影单被收藏的数量
	 * @param id
	 * @param type
	 * @return
	 */
	public int getResourceCollectCount(Long id , String type){
		final Long rid = id;
		final String aType = type;
		Map<String, String> map = resourceVisitClient.execute(new JedisWorker<Map<String,String>>() {
			public Map<String,String> work(Jedis jedis) {
				String key = "";
				if(CommentUtils.TYPE_BOOK.equals(aType)){
					key = ResStatisticConstant.BOOK_COLLECTED_MARK+rid+ResStatisticConstant.BOOK_COLLECTED_TYPE;
				}else if(CommentUtils.TYPE_MOVIE.equals(aType)){
					key = ResStatisticConstant.MOVIE_COLLECTED_MARK+rid+ResStatisticConstant.MOVIE_COLLECTED_TYPE;
				}else if(CommentUtils.TYPE_BOOKLIST.equals(aType)){
					key = ResStatisticConstant.BOOKLIST_COLLECTED_MARK+rid+ResStatisticConstant.BKLIST_STATISTIC_TYPE;
				}else if(CommentUtils.TYPE_MOVIELIST.equals(aType)){
					key = ResStatisticConstant.MOVIELIST_COLLECTED_MARK+rid+ResStatisticConstant.MVLIST_STATISTIC_TYPE;

				}
				return jedis.hgetAll(key);
			}
		});

		
		int falseCount = 0;
		int count = 0;
		
		String falseCountStr = map.get(ResStatisticConstant.COLLECTED_FALSE_NUM);
		if(falseCountStr != null && !"".equals(falseCountStr)){
			falseCount = Integer.valueOf(falseCountStr);
		}
		
		String countStr = map.get(ResStatisticConstant.RESOURCE_COLLECTED_NUM);
		if(countStr != null && !"".equals(countStr)){
			count = Integer.valueOf(countStr);
		}
		
		int total = count + falseCount;
		
		return total;
	}
	
	
	/**
	 * 获取用户标签列表
	 * @param uid
	 * @param tag
	 * @return
	 */
	public List<UserTagJedis> takeUserTag(Long uid,String tag){
		final Long fuid = uid;
		final String ftag = tag;
		List<UserTagJedis> list = userTagClient.execute(new JedisWorker<List<UserTagJedis>>(){

			@Override
			public List<UserTagJedis> work(Jedis jedis) {
				String key = USERTAG + fuid;
				int count = 0;
				if(ftag != null && !"".equals(ftag)){
					String strCount = jedis.hget(key, ftag);
					//获取该标签在缓存使用的次数
					if(null == strCount || "".equals(strCount)){
						count = 0;
					}else{
						count = Integer.valueOf(strCount);
					}
					
					if(count >0){
						jedis.hincrBy(key, ftag, 1);
					}else{
						jedis.hset(key, ftag, "1");
					}
					
				}
				
				//生成用户id列表
				String idKey = UID_LIST;
				jedis.hset(idKey, fuid+"", "1");
				
				jedis.hdel(key, "");
				
				//将标签遍历成list返回
				Map<String, String> map = jedis.hgetAll(key);
				Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
				List<UserTagJedis> userTagJedis = new ArrayList<JedisManager.UserTagJedis>();
				while(iter.hasNext()){
					Map.Entry<String, String> entry = iter.next();
					UserTagJedis userTagJedis2 = new UserTagJedis();
					String tag = entry.getKey();
					String tagCount = entry.getValue();
					userTagJedis2.setCount(tagCount);
					userTagJedis2.setTag(tag);
					userTagJedis.add(userTagJedis2);
				}
				Collections.sort(userTagJedis);
				LOG.info("用户"+fuid+"的标签组是："+userTagJedis.toString());
				
				
				Iterator<UserTagJedis> iterator = userTagJedis.iterator();

				StringBuffer sb = new StringBuffer();
				
				while(iterator.hasNext()){
					sb.append(iterator.next().getTag()+",");
					
				}
				sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",")+",".length(), "");
				
				return userTagJedis;
			}
			
		});
		
		return list;
	}
	/**
	 * 获取用户标签列表
	 * @param uid
	 * @param tag
	 * @return
	 */
	public Map<String, String> takeIndexCount(Long uid,Integer articleCount,Integer bookListCount , Integer movieListCount){
		final Long fuid = uid;
		final Integer artCount = articleCount;
		final Integer bCount = bookListCount;
		final Integer mCount = movieListCount;
		Map<String, String> map = userTagClient.execute(new JedisWorker<Map<String, String>>(){
			
			@Override
			public Map<String, String> work(Jedis jedis) {
				String key = INDEXCOUNT + fuid;
				int count = 0;
				
				//长文章数量
				String articleCount = jedis.hget(key, ARTICLE_INDEX);
				if(null == articleCount || "".equals(articleCount)){
					count = 0;
				}else{
					count = Integer.valueOf(articleCount);
				}
				
				if(count >0){
					if(artCount != 0){
						jedis.hincrBy(key, ARTICLE_INDEX, artCount);
					}
				}else{
					jedis.hset(key, ARTICLE_INDEX, artCount+"");
				}
				//影单数量
				String movieCount = jedis.hget(key, MOVIELIST_INDEX);
				if(null == movieCount || "".equals(movieCount)){
					count = 0;
				}else{
					count = Integer.valueOf(movieCount);
				}
				
				if(count >0){
					if(mCount != 0){
						
						jedis.hincrBy(key, MOVIELIST_INDEX, mCount);
					}
				}else{
					jedis.hset(key, MOVIELIST_INDEX, mCount+"");
				}
				//书单数量
				String bookCount = jedis.hget(key, BOOKLIST_INDEX);
				if(null == bookCount || "".equals(bookCount)){
					count = 0;
				}else{
					count = Integer.valueOf(bookCount);
				}
				
				if(count >0){
					if(bCount != 0){
						
						jedis.hincrBy(key, BOOKLIST_INDEX, bCount);
					}
				}else{
					jedis.hset(key, BOOKLIST_INDEX, bCount+"");
				}
					
				
				jedis.hdel(key, "");
				
				//将标签遍历成list返回
				Map<String, String> map = jedis.hgetAll(key);
//				Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
				List<UserTagJedis> userTagJedis = new ArrayList<JedisManager.UserTagJedis>();
//				while(iter.hasNext()){
//					Map.Entry<String, String> entry = iter.next();
//					UserTagJedis userTagJedis2 = new UserTagJedis();
//					String tag = entry.getKey();
//					String tagCount = entry.getValue();
//					userTagJedis2.setCount(tagCount);
//					userTagJedis2.setTag(tag);
//					userTagJedis.add(userTagJedis2);
//				}
//				Collections.sort(userTagJedis);
				
				
//				Iterator<UserTagJedis> iter = list.iterator();
//
//				StringBuffer sb = new StringBuffer();
//				
//				while(iter.hasNext()){
//					sb.append(iter.next().getTag()+",");
//					
//				}
//				sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",")+",".length(), "");
				return map;
			}
			
		});
		LOG.info("向用户推荐数据的索引位置："+map.toString());
//		LOG.info("向用户推荐数据的索引位置：[长文章:"+artIndex+",书单："+bookListIndex+",影单："+movieListIndex+"]");
		System.out.println("向用户推荐数据的索引位置："+map.toString());
		return map;
	}
	/**
	 * 用户某些索引位置清零
	 * @param uid
	 * @param tag
	 * @return
	 */
	public Map<String, String> clearSomeIndexCount(Long uid,String type){
		final Long fuid = uid;
		final String fType = type;
		Map<String, String> map = userTagClient.execute(new JedisWorker<Map<String, String>>(){
			
			@Override
			public Map<String, String> work(Jedis jedis) {
				
					try {
						String key = INDEXCOUNT + fuid;
						if(ARTICLE_INDEX.equals(fType)){
							//长文章数量清零
							jedis.hset(key, ARTICLE_INDEX, "0");
						}else if(MOVIELIST_INDEX.equals(fType)){
							//影单数量清零
							jedis.hset(key, MOVIELIST_INDEX, "0");
						}else if(BOOKLIST_INDEX.equals(fType)){
							//书单数量清零
							jedis.hset(key, BOOKLIST_INDEX, "0");
						}
						jedis.hdel(key, "");
					} catch (Exception e) {
						LOG.error("清空缓存索引数据出错"+e.getMessage(), e.fillInStackTrace());
					}
				
				//将标签遍历成list返回
//				Map<String, String> map = jedis.hgetAll(key);
//				List<UserTagJedis> userTagJedis = new ArrayList<JedisManager.UserTagJedis>();
				Map<String, String> map = new HashMap<String, String>();
				return map;
			}
			
		});
//		LOG.info("清空用户推荐数据的索引位置："+map.toString());
//		System.out.println("清空用户推荐数据的索引位置："+map.toString());
		return map;
	}
	/**
	 * 获取用户猜你喜欢的索引位置
	 * @param uid
	 * @param tag
	 * @return
	 */
	public Map<String, String> likeIndexCount(Long uid,Integer count,String type){
		final Long fuid = uid;
		final Integer fCount = count;
		final String fType = type;
		Map<String, String> map = userTagClient.execute(new JedisWorker<Map<String, String>>(){
			
			@Override
			public Map<String, String> work(Jedis jedis) {
				String key = INDEXCOUNT + fuid;
				int count = 0;
				
				if(BKCOMMENT_INDEX.equals(fType)){
					
					//书评数量
					String bCount = jedis.hget(key, BKCOMMENT_INDEX);
					if(null == bCount || "".equals(bCount)){
						count = 0;
					}else{
						count = Integer.valueOf(bCount);
					}
					
					if(count >0){
						if(fCount != 0){
							jedis.hincrBy(key, BKCOMMENT_INDEX, fCount);
						}
					}else{
						jedis.hset(key, BKCOMMENT_INDEX, fCount+"");
					}
				}else if(MVCOMMENT_INDEX.equals(fType)){
					//影评数量
					String movieCount = jedis.hget(key, MVCOMMENT_INDEX);
					if(null == movieCount || "".equals(movieCount)){
						count = 0;
					}else{
						count = Integer.valueOf(movieCount);
					}
					
					if(count >0){
						if(fCount != 0){
							
							jedis.hincrBy(key, MVCOMMENT_INDEX, fCount);
						}
					}else{
						jedis.hset(key, MVCOMMENT_INDEX, fCount+"");
					}
				}
				
				jedis.hdel(key, "");
				
				//将标签遍历成list返回
				Map<String, String> map = jedis.hgetAll(key);
//				Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
				List<UserTagJedis> userTagJedis = new ArrayList<JedisManager.UserTagJedis>();
				return map;
			}
			
		});
		LOG.info("猜你喜欢数据的索引位置："+map.toString());
//		LOG.info("向用户推荐数据的索引位置：[长文章:"+artIndex+",书单："+bookListIndex+",影单："+movieListIndex+"]");
//		System.out.println("猜你喜欢数据的索引位置："+map.toString());
		return map;
	}
	/**
	 * 用户猜你喜欢索引位置清零
	 * @param uid
	 * @param tag
	 * @return
	 */
	public Map<String, String> clearLikeIndexCount(Long uid,String type){
		final Long fuid = uid;
		final String fType = type;
		Map<String, String> map = userTagClient.execute(new JedisWorker<Map<String, String>>(){
			
			@Override
			public Map<String, String> work(Jedis jedis) {
				
				try {
					String key = INDEXCOUNT + fuid;
					if(BKCOMMENT_INDEX.equals(fType)){
						//书评数量清零
						jedis.hset(key, BKCOMMENT_INDEX, "0");
					}else if(MVCOMMENT_INDEX.equals(fType)){
						//影评数量清零
						jedis.hset(key, MVCOMMENT_INDEX, "0");
					}
					jedis.hdel(key, "");
				} catch (Exception e) {
					LOG.error("清空缓存索引数据出错"+e.getMessage(), e.fillInStackTrace());
				}
				
				//将标签遍历成list返回
//				Map<String, String> map = jedis.hgetAll(key);
//				List<UserTagJedis> userTagJedis = new ArrayList<JedisManager.UserTagJedis>();
				Map<String, String> map = new HashMap<String, String>();
				return map;
			}
			
		});
//		LOG.info("清空用户推荐数据的索引位置："+map.toString());
//		System.out.println("清空用户推荐数据的索引位置："+map.toString());
		return map;
	}
	/**
	 * 用户索引位置清零
	 * @param uid
	 * @param tag
	 * @return
	 */
	public Map<String, String> clearIndexCount(){
		Map<String, String> map = userTagClient.execute(new JedisWorker<Map<String, String>>(){
			
			@Override
			public Map<String, String> work(Jedis jedis) {
				Set<String> set = jedis.hkeys(UID_LIST);
//				System.out.println("缓存中用户id集合："+set.toString());
				Iterator<String> iter = set.iterator();
				
				while(iter.hasNext()){
					try {
						String uidStr = iter.next();
						if(uidStr != null && !"".equals(uidStr)){
							long uid = Long.valueOf(uidStr);
							String key = INDEXCOUNT + uid;
							//长文章数量清零
							jedis.hset(key, ARTICLE_INDEX, "0");
							//影单数量清零
							jedis.hset(key, MOVIELIST_INDEX, "0");
							//书单数量清零
							jedis.hset(key, BOOKLIST_INDEX, "0");
							jedis.hdel(key, "");
						}
					} catch (Exception e) {
						LOG.error("清空缓存索引数据出错"+e.getMessage(), e.fillInStackTrace());
						continue;
					}
				}
				
				//将标签遍历成list返回
//				Map<String, String> map = jedis.hgetAll(key);
//				List<UserTagJedis> userTagJedis = new ArrayList<JedisManager.UserTagJedis>();
				Map<String, String> map = new HashMap<String, String>();
				return map;
			}
			
		});
//		LOG.info("清空用户推荐数据的索引位置："+map.toString());
//		System.out.println("清空用户推荐数据的索引位置："+map.toString());
		return map;
	}
	
	public class UserTagJedis extends BaseDO implements Comparable<UserTagJedis>{
		int count;
		String tag;
		public int getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = Integer.valueOf(count);
		}
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		@Override
		public int compareTo(UserTagJedis o) {
			// TODO Auto-generated method stub
			if(o.count>=this.count){
				return 1;
			}
			return -1;
		}
		
	}
	

	/**
	 * 获取资源阅读量
	 * @param id
	 * @param type
	 * @return
	 */
	public int getResourceReadCount(Long id,String type){
		final long rid = id;
		final String t = type;
		Map<String, String> list = resourceVisitClient.execute(new JedisWorker<Map<String,String>>() {
			public Map<String,String> work(Jedis jedis) {
				String key = "";
				
				if(CommentUtils.TYPE_DIARY.equals(t)){
					key = ResStatisticConstant.DIARY_STATISTIC_MARK+rid+ResStatisticConstant.DIARY_STATISTIC_TYPE;
				}else if(CommentUtils.TYPE_ARTICLE.equals(t)){
					key = ResStatisticConstant.POST_STATISTIC_MARK+rid+ResStatisticConstant.POST_STATISTIC_TYPE;

				}else if(CommentUtils.TYPE_NEWARTICLE.equals(t)){
					key = ResStatisticConstant.ARTICLE_STATISTIC_MARK+rid+ResStatisticConstant.ARTICLE_STATISTIC_TYPE;

				}
				return jedis.hgetAll(key);
			}
		});
		
		int count = 0;
		int falseCount = 0;
		
		try {
			if(list != null && list.size()>0){
				count = Integer.valueOf(list.get(ResStatisticConstant.RESOURCE_STATISTIC_VISIT));
				falseCount = Integer.valueOf(list.get(ResStatisticConstant.STATISTIC_FALSE_VISIT));
			}
		} catch (Exception e) {
			count = 0;
			falseCount = 0;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		count = count +falseCount;
		
		return count;
	}
	
	/**
	 * 从缓存中获取全部resourceinfo
	 * @param jedis
	 * @return
	 */
	private List<ResourceInfo> getAllResourceListFromJedis(Jedis jedis){
		long begin = System.currentTimeMillis();
		
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		
		try {
			byte[] resourceByte = jedis.get(JEDIS_RESOURCE_LIST.getBytes());
			resourceInfos = (List<ResourceInfo>) SerializeServiceUtil.unserialize(resourceByte);
		} catch (Exception e) {
			resourceInfos = new ArrayList<ResourceInfo>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		long end = System.currentTimeMillis();
//		System.out.println("从缓存中获取全部resourceinfo所需时间:"+(end - begin));
		LOG.info("从缓存中获取全部resourceinfo所需时间:"+(end - begin));
		return resourceInfos;
	}
	/**
	 * 将全部resourceinfo放到缓存中
	 * @param jedis
	 * @return
	 */
	private void setAllResourceListToJedis(Jedis jedis,List<ResourceInfo> resourceInfos){
		long begin = System.currentTimeMillis();
		
		
		try {
			byte[] resourceByte = SerializeServiceUtil.serialize(resourceInfos);
			jedis.set(JEDIS_RESOURCE_LIST.getBytes(), resourceByte);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return;
		}
		
		
		
		long end = System.currentTimeMillis();
//		System.out.println("将全部resourceinfo放到缓存中所需时间:"+(end - begin));
		LOG.info("将全部resourceinfo放到缓存中所需时间:"+(end - begin));
	}
	
	/**
	 * 冲缓存中获取资源列表
	 * @return
	 */
	public List<ResourceInfo> getJedisResourceList(Long id){
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		long begin = System.currentTimeMillis();
		final Long rid = id;
		
		try {
			resourceInfos = resourceClient.execute(new JedisWorker<List<ResourceInfo>>(){
				
				@Override
				public List<ResourceInfo> work(Jedis jedis) {
					List<ResourceInfo> resourceInfos = getAllResourceListFromJedis(jedis);
					
					if(resourceInfos == null){
						resourceInfos = new ArrayList<ResourceInfo>();
					}else{
						resourceInfos = pageResourcesById(resourceInfos, rid);
					}
					
					
					
					
					return resourceInfos;
				}
				
			});
		} catch (Exception e) {
			resourceInfos = new ArrayList<ResourceInfo>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		long end = System.currentTimeMillis();
//		System.out.println("首页获取缓存时间："+(end - begin));
		LOG.info("首页获取缓存时间："+(end - begin));
		
		return resourceInfos;
	}
	
	/**
	 * 根据资源id分页
	 * @param resourceInfos
	 * @param rid
	 * @return
	 */
	private List<ResourceInfo> pageResourcesById(List<ResourceInfo> resourceInfos,Long rid){
		//对资源进行倒序排列
		Collections.sort(resourceInfos);
		
		
		if(rid == null){
			if(resourceInfos.size()>CommentUtils.RESOURCE_PAGE_SIZE){
				resourceInfos = resourceInfos.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
			}
		}else{
			int index = 0;
			long begin = System.currentTimeMillis();
			Iterator<ResourceInfo> iter = resourceInfos.iterator();
			while(iter.hasNext()){
				ResourceInfo resourceInfo = iter.next();
				
				if(resourceInfo.getRid() == rid){
					index = resourceInfos.indexOf(resourceInfo);
					if(index <0){
						index = 0;
					}
				}
				
			}
			long end = System.currentTimeMillis();
			System.out.println("缓存中比较id的时间:"+(end-begin));
			if(index>0){
				//向下翻页的时候截取的数据
				if(resourceInfos.size()>CommentUtils.RESOURCE_PAGE_SIZE+index){
					resourceInfos = resourceInfos.subList(index+1, CommentUtils.RESOURCE_PAGE_SIZE+index+1);
				}else{
					resourceInfos = resourceInfos.subList(index+1, resourceInfos.size());
				}
			}else{
				resourceInfos = new ArrayList<ResourceInfo>();
			}
			
		} 
		
		return resourceInfos;
	}
	/**
	 * 冲缓存中添加资源
	 * @return
	 */
	public int setJedisResourceList(ResourceInfo resourceInfo){
		long begin = System.currentTimeMillis();
		int flagint = ResultUtils.ERROR;
		if(resourceInfo == null || resourceInfo.getRid() == 0){
			return flagint;
		}
		
		final ResourceInfo ri = resourceInfo;
		try {
			flagint = resourceClient.execute(new JedisWorker<Integer>(){
				
				@Override
				public Integer work(Jedis jedis) {
					int flagint = ResultUtils.ERROR;
					try {
//						long bb = System.currentTimeMillis();
						List<ResourceInfo> resourceInfos = getAllResourceListFromJedis(jedis);
//						long ee = System.currentTimeMillis();
//						System.out.println("1"+(ee - bb));
						
						//移除
						resourceInfos = remaveOneResourceFromList(resourceInfos, ri.getRid());
						
						resourceInfos.add(ri);
						
						
						setAllResourceListToJedis(jedis, resourceInfos);
						flagint = ResultUtils.SUCCESS;
					} catch (Exception e) {
						flagint = ResultUtils.ERROR;
						LOG.error(e.getMessage(), e.fillInStackTrace());
					}
					
					
					return flagint;
				}
				
				
			});
		} catch (Exception e) {
			flagint = ResultUtils.ERROR;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		long end = System.currentTimeMillis();
//		System.out.println("添加缓存所需时间："+(end - begin));
		LOG.info("添加缓存所需时间："+(end - begin));
		return flagint;
	}
	/**
	 * 删除冲缓存中资源
	 * @return
	 */
	public int delJedisResourceList(long id){
		int flagint = ResultUtils.ERROR;
		
		final long rid = id;
		try {
			
			flagint = resourceClient.execute(new JedisWorker<Integer>(){
				
				@Override
				public Integer work(Jedis jedis) {
					int flagint = ResultUtils.ERROR;
					try {
						List<ResourceInfo> resourceInfos = getAllResourceListFromJedis(jedis);
						Iterator<ResourceInfo> iter = resourceInfos.iterator();
						while(iter.hasNext()){
							ResourceInfo resourceInfo = iter.next();
							if(resourceInfo.getRid() == rid){
								resourceInfos.remove(resourceInfo);
								break;
							}
						}
						
						setAllResourceListToJedis(jedis, resourceInfos);
						flagint = ResultUtils.SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
						
						flagint = ResultUtils.ERROR;
					}
					
					
					return flagint;
				}
				
				
			});
		} catch (Exception e) {
			flagint = ResultUtils.ERROR;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		return flagint;
	}
	
	/*****************************************************************************************
	 * 
	 * 用户生成资源缓存新方法
	 *
	 *****************************************************************************************/
	//TODO 用户生成资源缓存新方法
	/**
	 * 冲缓存中获取资源列表
	 * @return
	 */
	public List<ResourceInfo> getJedisResources(List<Long> uids,Long uid,Long id,int type){
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		List<byte[]> resourceBytes = new ArrayList<byte[]>();
		long begin = System.currentTimeMillis();
		final Long rid = id;
		final long fuid = uid;
		final List<Long> fuids = uids;
		final int fType = type;
		try {
			resourceBytes = resourceClient.execute(new JedisWorker<List<byte[]>>(){
				
				@Override
				public List<byte[]> work(Jedis jedis) {
//					List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
					List<byte[]> resourceBytes = new ArrayList<byte[]>();
					//用户id列表为空则返回全部信息
					if(fuids == null){
						List<byte[]> bs = jedis.hvals(RESOURCE_ALL.getBytes());
						
					}else{
						if(fType == 0){
							fuids.add(fuid);
						}
						//用户id列表不为空就返回指定用户的资源
						long begin = System.currentTimeMillis();
						Iterator<Long> iter = fuids.iterator();
						while(iter.hasNext()){
							Long uid = iter.next();
							byte[] userResourceBytes = jedis.hget(RESOURCE_ALL.getBytes(), SerializeServiceUtil.serialize(uid));
							if(userResourceBytes != null){
								//List<ResourceInfo> infos = (List<ResourceInfo>) SerializeServiceUtil.unserialize(userResourceBytes);
								
//								System.out.println("用户id为["+uid+"]的动态缓存数量为:"+infos.size());
								resourceBytes.add(userResourceBytes);
							}
						}
//						for(Long uid:fuids){
//						}
//						if(fType == 0){
//							begin = System.currentTimeMillis();
//							//用户自己发的信息
//							byte[] userResourceBytes = jedis.hget(RESOURCE_ALL.getBytes(), SerializeServiceUtil.serialize(fuid));
//							if(userResourceBytes != null){
//								List<ResourceInfo> infos = (List<ResourceInfo>) SerializeServiceUtil.unserialize(userResourceBytes);
//								//resourceInfos = remaveOneResourceFromList(resourceInfos, infos.get);
//								resourceInfos.addAll(infos);
//							}
//							long end = System.currentTimeMillis();
//							System.out.println("缓存获取自己信息的时间:"+(end-begin));
//						}
						long end = System.currentTimeMillis();
						System.out.println("缓存获取的时间:"+(end-begin));
					}
					
					
					
					return resourceBytes;
				}
				
			});
		} catch (Exception e) {
			resourceInfos = new ArrayList<ResourceInfo>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		long end = System.currentTimeMillis();
		System.out.println("缓存总的时间:"+(end-begin));
		begin = System.currentTimeMillis();
		if(resourceBytes != null && resourceBytes.size()>0){
			Iterator<byte[]> iter = resourceBytes.iterator();
			while(iter.hasNext()){
				byte[] b = iter.next();
				if(b != null){
					List<ResourceInfo> infos  = (List<ResourceInfo>) SerializeServiceUtil.unserialize(b);
					resourceInfos.addAll(infos);
				}
			}
		}
		end = System.currentTimeMillis();
		System.out.println("反序列化的时间:"+(end-begin));
		
		
		if(resourceInfos == null){
			resourceInfos = new ArrayList<ResourceInfo>();
		}
		
		//根据资源id获取分页数据
//		long begin = System.currentTimeMillis();
		resourceInfos  = pageResourcesById(resourceInfos, rid);
//		long end = System.currentTimeMillis();
//		System.out.println("缓存分页的时间:"+(end-begin));
		
		end = System.currentTimeMillis();
//		System.out.println("耗时多的地方："+(end - begin));
		LOG.info("首页获取缓存时间："+(end - begin));
		
		return resourceInfos;
	}
	
	/**
	 * 冲缓存中添加资源
	 * @return
	 */
	public int setJedisResources(ResourceInfo resourceInfo){
		long begin = System.currentTimeMillis();
		int flagint = ResultUtils.ERROR;
		if(resourceInfo == null || resourceInfo.getRid() == 0){
			return flagint;
		}
		
		final ResourceInfo ri = resourceInfo;
		try {
			flagint = resourceClient.execute(new JedisWorker<Integer>(){
				
				@Override
				public Integer work(Jedis jedis) {
					int flagint = ResultUtils.ERROR;
					try {
						UserEntity userEntity = ri.getUserEntity();
						if(userEntity != null){
							Long uid = userEntity.getId();
							if(uid != 0){
								byte[] userResourceBytes = jedis.hget(RESOURCE_ALL.getBytes(), serialize(uid));
//								Object object = unSerialize(userResourceBytes);
//								if(null==object){
//									return ResultUtils.ERROR;
//								}
								List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
								if(userResourceBytes != null){
									resourceInfos = (List<ResourceInfo>) unSerialize(userResourceBytes);
								}
								
								resourceInfos = remaveOneResourceFromList(resourceInfos, ri.getRid());
								resourceInfos.add(ri);
								
								jedis.hset(RESOURCE_ALL.getBytes(), serialize(uid), serialize(resourceInfos));
								flagint = ResultUtils.SUCCESS;
								//userResourceBytes = jedis.hget(RESOURCE_ALL.getBytes(), serialize(uid));
							}
						}
						
					} catch (Exception e) {
						flagint = ResultUtils.ERROR;
						LOG.error(e.getMessage(), e.fillInStackTrace());
					}
					
					
					return flagint;
				}
				
				
			});
		} catch (Exception e) {
			flagint = ResultUtils.ERROR;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		long end = System.currentTimeMillis();
//		System.out.println("添加缓存所需时间："+(end - begin));
		LOG.info("添加缓存所需时间："+(end - begin));
		return flagint;
	}
	
	
	/**
	 * 删除冲缓存中资源
	 * @return
	 */
	public int delJedisResources(long uid,long id){
		int flagint = ResultUtils.ERROR;
		
		final long rid = id;
		final long fuid = uid;
		try {
			
			flagint = resourceClient.execute(new JedisWorker<Integer>(){
				
				@Override
				public Integer work(Jedis jedis) {
					int flagint = ResultUtils.ERROR;
					try {
						byte[] b = jedis.hget(RESOURCE_ALL.getBytes(), serialize(fuid));
						List<ResourceInfo> resourceInfos = (List<ResourceInfo>) unSerialize(b);
//						System.out.println("资源删除前的数量："+resourceInfos.size());
						resourceInfos = remaveOneResourceFromList(resourceInfos, rid);
//						System.out.println("资源删除后的数量："+resourceInfos.size());
						
						jedis.hset(RESOURCE_ALL.getBytes(), serialize(fuid), serialize(resourceInfos));
						
						flagint = ResultUtils.SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
						
						flagint = ResultUtils.ERROR;
					}
					
					
					return flagint;
				}
				
				
			});
		} catch (Exception e) {
			flagint = ResultUtils.ERROR;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		return flagint;
	}
	
	//TODO 朋友圈新的缓存机制
	/*****************************************************************************************
	 * 
	 * 朋友圈新的缓存机制
	 * 
	 *****************************************************************************************/
	
	/**
	 * 冲缓存中获取资源列表
	 * @return
	 */
	public List<ResourceInfo> getResourcesByFriends(Long uid,Long id){
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		List<byte[]> resourceBytes = new ArrayList<byte[]>();
		long begin = System.currentTimeMillis();
		final Long rid = id;
		final Long fuid = uid;
		try {
			resourceBytes = resourceClient.execute(new JedisWorker<List<byte[]>>(){
				
				@Override
				public List<byte[]> work(Jedis jedis) {
					List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
					String key = CIRCLE_OF_FRIENDS + "111";
					List<byte[]> resourceBytes = new ArrayList<byte[]>();//jedis.hvals(key.getBytes());
//					jedis.lpush(key.getBytes(), "11".getBytes());
//					jedis.lpush(key.getBytes(), "22".getBytes());
					
					
					jedis.del("user:66", "user:55", "user:33", "user:22", "user:11",  
			                "userlist");  
			        jedis.lpush("userlist", "33");  
			        jedis.lpush("userlist", "22");  
			        jedis.lpush("userlist", "55");  
			        jedis.lpush("userlist", "11");  
			  
			        jedis.hset("user:66", "name", "66");  
			        jedis.hset("user:55", "name", "55");  
			        jedis.hset("user:33", "name", "33");  
			        jedis.hset("user:22", "name", "79");  
			        jedis.hset("user:11", "name", "24");  
			        jedis.hset("user:11", "add", "beijing");  
			        jedis.hset("user:22", "add", "shanghai");  
			        jedis.hset("user:33", "add", "guangzhou");  
			        jedis.hset("user:55", "add", "chongqing");  
			        jedis.hset("user:66", "add", "xi'an");  
			  
			        SortingParams sortingParameters = new SortingParams();  
			        // 符号 "->" 用于分割哈希表的键名(key name)和索引域(hash field)，格式为 "key->field" 。  
			        sortingParameters.get("user:*->name");  
			        sortingParameters.get("user:*->add");  
//			      sortingParameters.by("user:*->name");  
//			      sortingParameters.get("#");  
			        List<String> result = jedis.lrange("userlist", 0, -1);// jedis.sort("userlist", sortingParameters);  
			        for (String item : result) {  
			            System.out.println("item...." + item);  
			        }  
					
					
					jedis.hset("uid".getBytes(), "rid".getBytes(), "resourceinfo".getBytes());
					jedis.hset("uid".getBytes(), "rid1".getBytes(), "resourceinfo1".getBytes());
					jedis.hset("uid".getBytes(), "rid2".getBytes(), "resourceinfo2".getBytes());
					jedis.hset("uid".getBytes(), "rid3".getBytes(), "resourceinfo3".getBytes());
					jedis.hset(key.getBytes(), "2".getBytes(), "2222".getBytes());
					jedis.hset(key.getBytes(), "3".getBytes(), "3333".getBytes());
					
					List<byte[]> values = jedis.hvals(key.getBytes());
					List<byte[]> keys = (List<byte[]>) jedis.hkeys(key.getBytes());
					
					Iterator<byte[]> iter = keys.iterator();
					while(iter.hasNext()){
						byte[] b = iter.next();
						if(b != null){
							String value = (String) SerializeServiceUtil.unserialize(b);
							System.out.println("key:"+value);
						}
					}
					iter = values.iterator();
					while(iter.hasNext()){
						byte[] b = iter.next();
						if(b != null){
							String value = (String) SerializeServiceUtil.unserialize(b);
							System.out.println("value:"+value);
						}
					}
					
					
					jedis.hdel(key.getBytes(), "2".getBytes());
					values = jedis.hvals(key.getBytes());
					keys = (List<byte[]>) jedis.hkeys(key.getBytes());
					
					iter = keys.iterator();
					while(iter.hasNext()){
						byte[] b = iter.next();
						if(b != null){
							String value = (String) SerializeServiceUtil.unserialize(b);
							System.out.println("key:"+value);
						}
					}
					iter = values.iterator();
					while(iter.hasNext()){
						byte[] b = iter.next();
						if(b != null){
							String value = (String) SerializeServiceUtil.unserialize(b);
							System.out.println("value:"+value);
						}
					}
					
					return resourceBytes;
				}
				
			});
		} catch (Exception e) {
			resourceBytes = new ArrayList<byte[]>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		Iterator<byte[]> iter = resourceBytes.iterator();
		while(iter.hasNext()){
			byte[] b = iter.next();
			if(b != null){
				ResourceInfo resourceInfo = (ResourceInfo) unSerialize(b);
				resourceInfos.add(resourceInfo);
			}
		}
		
		if(resourceInfos == null){
			resourceInfos = new ArrayList<ResourceInfo>();
		}
		
		//根据资源id获取分页数据
//		long begin = System.currentTimeMillis();
		resourceInfos  = pageResourcesById(resourceInfos, rid);
//		long end = System.currentTimeMillis();
//		System.out.println("缓存分页的时间:"+(end-begin));
		
		long end = System.currentTimeMillis();
//		System.out.println("耗时多的地方："+(end - begin));
		LOG.info("首页获取缓存时间："+(end - begin));
		
		return resourceInfos;
	}
	
	/**
	 * 新朋友圈往缓存中添加资源
	 * @return
	 */
	public int setResourceToFriends(ResourceInfo resourceInfo){
		long begin = System.currentTimeMillis();
		int flagint = ResultUtils.ERROR;
		if(resourceInfo == null || resourceInfo.getRid() == 0){
			return flagint;
		}
		
		final ResourceInfo ri = resourceInfo;
		try {
			flagint = resourceClient.execute(new JedisWorker<Integer>(){
				
				@Override
				public Integer work(Jedis jedis) {
					int flagint = ResultUtils.ERROR;
					try {
						UserEntity userEntity = ri.getUserEntity();
						if(userEntity != null){
							Long uid = userEntity.getId();
							if(uid != 0){
								
								
								
								byte[] userResourceBytes = jedis.hget(RESOURCE_ALL.getBytes(), serialize(uid));
//								Object object = unSerialize(userResourceBytes);
//								if(null==object){
//									return ResultUtils.ERROR;
//								}
								List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
								if(userResourceBytes != null){
									resourceInfos = (List<ResourceInfo>) unSerialize(userResourceBytes);
								}
								
								resourceInfos = remaveOneResourceFromList(resourceInfos, ri.getRid());
								resourceInfos.add(ri);
								
								jedis.hset(RESOURCE_ALL.getBytes(), serialize(uid), serialize(resourceInfos));
								flagint = ResultUtils.SUCCESS;
								//userResourceBytes = jedis.hget(RESOURCE_ALL.getBytes(), serialize(uid));
							}
						}
						
					} catch (Exception e) {
						flagint = ResultUtils.ERROR;
						LOG.error(e.getMessage(), e.fillInStackTrace());
					}
					
					
					return flagint;
				}
				
				
			});
		} catch (Exception e) {
			flagint = ResultUtils.ERROR;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		long end = System.currentTimeMillis();
//		System.out.println("添加缓存所需时间："+(end - begin));
		LOG.info("添加缓存所需时间："+(end - begin));
		return flagint;
	}
	
	
	
	/*****************************************************************************************
	 * 
	 * 清空缓存
	 * 
	 *****************************************************************************************/
	
	/**
	 * 清空冲缓存中资源
	 * @return
	 */
	public int clearJedisResource(String type){
		int flagint = ResultUtils.ERROR;
		
		final String fType = type;
		try {
			
			flagint = resourceClient.execute(new JedisWorker<Integer>(){
				
				@Override
				public Integer work(Jedis jedis) {
					int flagint = ResultUtils.ERROR;
					try {
						String keyid = JedisConstant.USER_MOMENT_ID+16;
						jedis.del(keyid);
						if(JEDIS_RESOURCE_LIST.equals(fType)){
							jedis.del(JEDIS_RESOURCE_LIST.getBytes());
						}else if(RESOURCE_ALL.equals(fType)){
							List<byte[]> bs = jedis.hvals(RESOURCE_ALL.getBytes());
							Iterator<byte[]> iter = bs.iterator();
							while(iter.hasNext()){
								byte[] key = iter.next();
								jedis.hdel(RESOURCE_ALL.getBytes(), key);
							}
						}
						
						flagint = ResultUtils.SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
						
						flagint = ResultUtils.ERROR;
					}
					
					
					return flagint;
				}
				
				
			});
		} catch (Exception e) {
			flagint = ResultUtils.ERROR;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		return flagint;
	}
	
	public void test(){
		resourceClient.execute(new JedisWorker<Integer>(){

			@Override
			public Integer work(Jedis jedis) {
				
				String key = "user";
				String key1 = "uid";
				
//				jedis.hset(key.getBytes(), key1.getBytes(), value)
				
				
				return null;
			}
			
		});
	}
	
	
	public static void main(String[] args) {
		
		
		long begin = System.currentTimeMillis();
		int i = 10000;
		
		while(i>0){
			byte[] b = SerializeServiceUtil.serialize(i);
//			System.out.println(b);
			i--;
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println(end-begin);
		
		Set<Integer> set = new HashSet<Integer>();
		
		set.add(1);
		set.add(2);
		set.add(3);
		set.add(4);
		
		System.out.println(set.contains(5));
		
		System.out.println(set.toString());
		
		
		
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		list.add(11);
		list.add(12);
		list.add(13);
		list.add(14);
		list.add(15);
		list.add(16);
		list.add(17);
		list.add(18);
		list.add(19);
		list.add(20);
		list.add(21);
		list.add(22);
		
		
		//对资源进行倒序排列
		Collections.sort(list);
		
		int id = 33;
		if(id == 0){
			if(list.size()>CommentUtils.RESOURCE_PAGE_SIZE){
				list = list.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
			}
		}else{
			int index = 0;
			Iterator<Integer> iter = list.iterator();
			while(iter.hasNext()){
//				int a = iter.next();
//				
//				if(a == id){
//					index = list.indexOf(id);
//					if(index <0){
//						index = 0;
//					}
//				}
				
			}
			
			//向下翻页的时候截取的数据
			if(list.size()>CommentUtils.RESOURCE_PAGE_SIZE+index){
				list = list.subList(index, CommentUtils.RESOURCE_PAGE_SIZE+index);
			}else{
				list = list.subList(index, list.size());
			}
		}
		
//		System.out.println(list);
		
		JedisManager jedisManager = new JedisManager();
//		int i  = jedisManager.takeUserTag(16l,"文学");
	}
	
	
	
	/********************************************************************
	 * 
	 *首页推广数据技术
	 * 
	 ********************************************************************/
	/**
	 * 获取首页推广数据index
	 * @param count
	 * @return
	 */
	public int getIndexPopularizeCount(int count){
		final int fcount = count;
		
		Integer num = userTagClient.execute(new JedisWorker<Integer>(){

			@Override
			public Integer work(Jedis jedis) {
				if(fcount == 0){
					jedis.hset(INDEXCOUNT, RESOURCE_INDEX, "1");
				}else{
					jedis.hincrBy(INDEXCOUNT, RESOURCE_INDEX, 1);
				}
				String value = jedis.hget(INDEXCOUNT, RESOURCE_INDEX);
				if(value == null || "".equals(value)){
					value = "1";
				}
//				System.out.println("首页推广数据index:"+value);
				LOG.info("首页推广数据index:"+value);
				return Integer.valueOf(value);
			}});
		
		return num;
	}
	
	
	/**
	 * 序列化
	 * @param obj
	 * @return
	 */
	private byte[] serialize(Object obj){
		if(obj != null){
			return SerializeServiceUtil.serialize(obj);
		}else{
			return new byte[1];
		}
	}
	
	/**
	 * 反序列化
	 * @param b
	 * @return
	 */
	private Object unSerialize(byte[] b){
		if(b != null){
			return SerializeServiceUtil.unserialize(b);
		}else{
			return new Object();
		}
	}
	
	/**
	 * 移除资源list中的一条
	 * @param resourceInfos
	 * @param ri
	 * @return
	 */
	private List<ResourceInfo> remaveOneResourceFromList(List<ResourceInfo> resourceInfos ,Long id){
		if(resourceInfos !=null && resourceInfos.size()>0){
			Iterator<ResourceInfo> iter = resourceInfos.iterator();
			while(iter.hasNext()){
				ResourceInfo resourceInfo = iter.next();
//				long rid = resourceInfo.getRid();
//				if(CommentUtils.TYPE_BOOKLIST.equals(resourceInfo.getType())){
//					rid = resourceInfo.getBookListId();
//				}else if(CommentUtils.TYPE_MOVIELIST.equals(resourceInfo.getType())){
//					rid = resourceInfo.getMovieListId();
//				}
				if(resourceInfo.getRid() == id){
					resourceInfos.remove(resourceInfo);
					break;
				}
				
			}
		}else{
			resourceInfos = new ArrayList<ResourceInfo>();
		}
		
		return resourceInfos;
	}
	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}
	public void setUserTagClient(JedisSimpleClient userTagClient) {
		this.userTagClient = userTagClient;
	}


	public void setResourceClient(JedisSimpleClient resourceClient) {
		this.resourceClient = resourceClient;
	}

}

