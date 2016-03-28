package com.poison.eagle.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.common.event.rocketmq.MessageRecv;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;

public class MomentJedisManager implements MessageRecv{

	private static final  Log LOG = LogFactory.getLog(MomentJedisManager.class);
	
	/**
	 * 朋友圈的缓存
	 */
	private JedisSimpleClient momentsClient;
	
	/**
	 * 资源的hash缓存
	 */
	private JedisSimpleClient resourceHashClient;
	
	/**
	 * 资源关系
	 */
	private JedisSimpleClient relationToUserandresClient;
	
	/**
	 * 用户的资源信息
	 */
	private JedisSimpleClient userInfoHashClient;
	
	private UcenterFacade ucenterFacade;
	
	/**
	 * 用户的缓存
	 */
	private UserJedisManager userJedisManager;
	
	/**
	 * 资源的各种相关数量
	 */
	private ResStatJedisManager resStatJedisManager;
	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	public void setUserInfoHashClient(JedisSimpleClient userInfoHashClient) {
		this.userInfoHashClient = userInfoHashClient;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	
	public void setRelationToUserandresClient(
			JedisSimpleClient relationToUserandresClient) {
		this.relationToUserandresClient = relationToUserandresClient;
	}


	public void setResourceHashClient(JedisSimpleClient resourceHashClient) {
		this.resourceHashClient = resourceHashClient;
	}

	public void setMomentsClient(JedisSimpleClient momentsClient) {
		this.momentsClient = momentsClient;
	}
	
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}

	/**
	 * 
	 * <p>Title: saveOneItem</p> 
	 * <p>Description: 存储朋友圈的一个条目</p> 
	 * @author :changjiang
	 * date 2015-2-27 下午12:08:16
	 * @param userId
	 * @param object
	 * @return
	 */
	public String saveOneItem(long uid,long rId,Long vId){
		final long userId = uid;
		final long rid = rId;
		if(vId==null){
			vId =0l;
		}
		final long vid = vId;
		String itemStr="";
//		System.out.println("存储缓存的用户id和资源id为"+userId+";"+rid);
		//存入一条信息
		itemStr = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String userMomentId = JedisConstant.USER_MOMENT_ID+userId;
				//String squareId =  JedisConstant.USER_MOMENT_ID+0;
//				jedis.del(userMomentId);
//				jedis.del(JedisConstant.USER_MOMENT_ID+0);
//				jedis.del(JedisConstant.USER_DYNAMEIC_ID+userId);
//				jedis.del(JedisConstant.RESOURCE_HASH_ID+userId);
				//long index = jedis.lpush(userMomentId, rid+"");
				//System.out.println(jedis.zcard(userMomentId));
				if(vid==0){
					jedis.zadd(userMomentId, -rid, rid+"");
				}else{
					jedis.zadd(userMomentId, -vid, rid+"");
				}
				//设置过期1个月
				jedis.expire(userMomentId, 2592000);
				//jedis.zadd(squareId, -rid, rid+"");
				long end = jedis.zcard(userMomentId);
				//维护数量在1000个
				if(end>1000){
					//jedis.zremrangeByScore(userMomentId, end-1, end-1);
					jedis.zremrangeByRank(userMomentId, end-1, end-1);
				}
				Set<String> set = jedis.zrange(userMomentId, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
//				System.out.println("数量"+end);
				//jedis.zscan(userMomentId, 0);
				//排分从高到低
				//Set<String> set1 = jedis.zrevrange(userMomentId, 0, -1);
						//jedis.zrevrangeByScore(userMomentId, -1, 0);
				
				//System.out.println(jedis.zrange(userMomentId, 2, 2));
				//System.out.println(jedis.zscan(userMomentId, rid+""));
				/*Set<String> set = jedis.zrange(userMomentId, 0, 1);
				System.out.println(set);
				System.out.println("排序中得成员"+jedis.zrevrank(userMomentId, rid+""));
				Set<String> set1 =jedis.zrevrangeByScore(userMomentId, rid, 0);*/
				//当超出长度的时候维护长度
				/*if(index>500){
					jedis.rpop(userMomentId);
				}*/
				//String item = jedis.lindex(userMomentId, index-1);
				//System.out.println(item);
				return result;
			}
		});
//		System.out.println(itemStr);
		return itemStr;
	}
	
	/**
	 * 
	 * <p>Title: getUserMomentIsExist</p> 
	 * <p>Description: 查询个人朋友圈是否存在</p> 
	 * @author :changjiang
	 * date 2015-8-13 下午5:06:44
	 * @param userId
	 * @return
	 */
	public boolean getUserMomentIsExist(final long userId){
		boolean flag = false;
		flag = momentsClient.execute(new JedisWorker<Boolean>(){
			@Override
			public Boolean work(Jedis jedis) {
				String userMomentId = JedisConstant.USER_MOMENT_ID+userId;
				return jedis.exists(userMomentId);
			}
		});
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: saveOneDynamic</p> 
	 * <p>Description: 添加一个人的个人动态</p> 
	 * @author :changjiang
	 * date 2015-3-2 下午5:20:01
	 * @param userId
	 * @param rid
	 * @return
	 */
	public String saveOneDynamic(final long userId,final long rid,Long vId){
		String itemStr="";
		if(vId==null){
			vId =0l;
		}
		final long vid = vId;
//		System.out.println("存储缓存的用户id和资源id为"+userId+";"+rid);
		//存入一条信息
		itemStr = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String userMomentId = JedisConstant.USER_DYNAMEIC_ID+userId;
				if(vid==0){
					jedis.zadd(userMomentId, -rid, rid+"");
				}else{
					jedis.zadd(userMomentId, -vid, rid+"");
				}
				//设置过期1个月
				jedis.expire(userMomentId, 2592000);
				long end = jedis.zcard(userMomentId);
				//维护数量在500个
				if(end>1000){
					jedis.zremrangeByRank(userMomentId, end-1, end-1);
				}
				Set<String> set = jedis.zrange(userMomentId, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return itemStr;
	}
	
	/**
	 * 
	 * <p>Title: saveOneShenRenDynamic</p> 
	 * <p>Description: 存入神人广场</p> 
	 * @author :changjiang
	 * date 2015-4-13 下午5:34:09
	 * @param userId
	 * @param rid
	 * @return
	 */
	public String saveOneShenRenMoment(final long rid,Long vId){
		String itemStr="";
		if(vId==null){
			vId =0l;
		}
		final long vid = vId;
//		System.out.println("存储缓存的用户id和资源id为"+userId+";"+rid);
		//存入一条信息
		itemStr = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String shenRenMomentId = JedisConstant.SHENREN_MOMENT_ID;
				if(vid==0){
					jedis.zadd(shenRenMomentId, -rid, rid+"");
				}else{
					jedis.zadd(shenRenMomentId, -vid, rid+"");
				}
				//设置过期1个月
				jedis.expire(shenRenMomentId, 2592000);
				long end = jedis.zcard(shenRenMomentId);
				//维护数量在500个
				if(end>1000){
					jedis.zremrangeByRank(shenRenMomentId, end-1, end-1);
				}
				Set<String> set = jedis.zrange(shenRenMomentId, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return itemStr;
	}
	
	/**
	 * 
	 * <p>Title: saveOneCommonMoment</p> 
	 * <p>Description: 存储一条普通人的广场</p> 
	 * @author :changjiang
	 * date 2015-4-13 下午5:48:56
	 * @param rid
	 * @return
	 */
	public String saveOneCommonMoment(final long rid,Long vId){
		String itemStr="";
		if(vId==null){
			vId =0l;
		}
		final long vid = vId;
//		System.out.println("存储缓存的用户id和资源id为"+userId+";"+rid);
		//存入一条信息
		itemStr = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String commonMomentId = JedisConstant.COMMON_MOMENT_ID;
				if(vid==0){
					jedis.zadd(commonMomentId, -rid, rid+"");
				}else{
					jedis.zadd(commonMomentId, -vid, rid+"");
				}
				//设置过期1个月
				jedis.expire(commonMomentId, 2592000);
				long end = jedis.zcard(commonMomentId);
				//维护数量在500个
				if(end>1000){
					jedis.zremrangeByRank(commonMomentId, end-1, end-1);
				}
				Set<String> set = jedis.zrange(commonMomentId, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return itemStr;
	}
	
	/**
	 * 
	 * <p>Title: saveOneCollected</p> 
	 * <p>Description: 存储用户的一条收藏信息</p> 
	 * @author :changjiang
	 * date 2015-4-3 下午3:51:46
	 * @param userId
	 * @param rid
	 * @return
	 */
	public String saveOneCollected(final long userId,final long collectDate,final long rid){
		String itemStr="";
//		System.out.println("存储缓存的用户id和资源id为"+userId+";"+rid);
		//存入一条信息
		itemStr = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String userCollectedtId = JedisConstant.USER_COLLECTED_ID+userId;
				jedis.zadd(userCollectedtId, -collectDate, rid+"");
				long end = jedis.zcard(userCollectedtId);
				//维护数量在500个
				if(end>1000){
					jedis.zremrangeByRank(userCollectedtId, end-1, end-1);
				}
				//设置过期1个月
				jedis.expire(userCollectedtId, 2592000);
				Set<String> set = jedis.zrange(userCollectedtId, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return itemStr;
	}
	
	
	/**
	 * 
	 * <p>Title: saveOneCommonMoment</p> 
	 * <p>Description: 存储一条精选的广场</p> 
	 * @author :changjiang
	 * date 2015-4-13 下午5:48:56
	 * @param rid
	 * @return
	 */
	public String saveOneSelectedMoment(final long rid,Long vId,String restype){
		String itemStr="";
		if(vId==null){
			vId =0l;
		}
		
		//需要区分书、网络小说和电影，因为书和电影都是自增id，id会重复，所以需要加上type类型用于区分
		String ridStr = rid+"";
		if("27".equals(restype) || "28".equals(restype) || "29".equals(restype)){
			ridStr = rid + "_"+restype;
		}
		final String ridvalue = ridStr;
		//========================================
		
		final long vid = vId;
//		System.out.println("存储缓存的用户id和资源id为"+userId+";"+rid);
		//存入一条信息
		itemStr = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String selectedMomentId = JedisConstant.SELECTED_MOMENT_ID;
				if(vid==0){
					jedis.zadd(selectedMomentId, -rid, ridvalue);
				}else{
					jedis.zadd(selectedMomentId, -vid, ridvalue);
				}
				//设置过期1个月 
				jedis.expire(selectedMomentId, 2592000);
				long end = jedis.zcard(selectedMomentId);
				//维护数量在500个
				if(end>1000){
					jedis.zremrangeByRank(selectedMomentId, end-1, end-1);
				}
				Set<String> set = jedis.zrange(selectedMomentId, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return itemStr;
	}
	
	/**
	 * 
	 * <p>Title: saveUserSelectedItem</p> 
	 * <p>Description: 插入一条精选item</p> 
	 * @author :changjiang
	 * date 2015-8-11 下午4:08:15
	 * @param item
	 * @param userId
	 * @return
	 */
	public String saveUserSelectedItem(final String item,final long userId){
		String saveValue = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String userSelectedId = JedisConstant.SELECTED_MOMENT_DYNAMEIC_ID+userId;
				long inputIndex = jedis.lpush(userSelectedId, item);
				//判断队列的长度 如果超过20则去除尾部的一个元素
				long listLength = jedis.llen(userSelectedId);
				if(listLength>20){
					jedis.rpop(userSelectedId);
				}
				//设置过期1个月 
				jedis.expire(userSelectedId, 2592000);
				String inputValue = jedis.lindex(userSelectedId, inputIndex);
				return inputValue;
			}
		});
		return saveValue;
	}

	/**
	 * 存储一条运营精选的
	 * @param item
	 * @param userId
	 * @return
	 */
	public String saveUserOperationSelectedItem(final String item,final long userId){
		String saveValue = momentsClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String userSelectedId = JedisConstant.OPERATION_SELECTED_DYNAMEIC_ID+userId;
				long inputIndex = jedis.lpush(userSelectedId, item);
				//判断队列的长度 如果超过20则去除尾部的一个元素
				long listLength = jedis.llen(userSelectedId);
				if(listLength>20){
					jedis.rpop(userSelectedId);
				}
				//设置过期1个月
				jedis.expire(userSelectedId, 2592000);
				String inputValue = jedis.lindex(userSelectedId, inputIndex);
				return inputValue;
			}
		});
		return saveValue;
	}
	
	/**
	 * 
	 * <p>Title: delUserSelectedItem</p> 
	 * <p>Description: 删除一个精选item</p> 
	 * @author :changjiang
	 * date 2015-8-11 下午4:09:36
	 * @param userId
	 * @param index
	 * @param value
	 * @return
	 */
	public long delUserSelectedItem(final long userId,final int index,final String value){
		long delSize = momentsClient.execute(new JedisWorker<Long>(){
			@Override
			public Long work(Jedis jedis) {
				String userSelectedId = JedisConstant.SELECTED_MOMENT_DYNAMEIC_ID+userId;
				return jedis.lrem(userSelectedId, index, value);
			}
		});
		return delSize;
	}

	/**
	 * 删除运营精选的用户
	 * @param userId
	 * @param index
	 * @param value
	 * @return
	 */
	public long delUserOperationSelectedItem(final long userId,final int index,final String value){
		long delSize = momentsClient.execute(new JedisWorker<Long>(){
			@Override
			public Long work(Jedis jedis) {
				String userSelectedId = JedisConstant.OPERATION_SELECTED_DYNAMEIC_ID+userId;
				return jedis.lrem(userSelectedId, index, value);
			}
		});
		return delSize;
	}
	
	/**
	 * 
	 * <p>Title: getUserSelectedList</p> 
	 * <p>Description: 查询用户的精选列表</p> 
	 * @author :changjiang
	 * date 2015-8-7 下午12:36:01
	 * @param userId
	 * @return
	 */
	public List<String> getUserSelectedList(final long userId){
		List<String> userSelectedList = momentsClient.execute(new JedisWorker<List<String>>(){

			@Override
			public List<String> work(Jedis jedis) {
				String userSelectedId = JedisConstant.SELECTED_MOMENT_DYNAMEIC_ID+userId;
				long listLength = jedis.llen(userSelectedId);
				List<String> userSelectedList = new ArrayList<String>();
				if(listLength>0){
					userSelectedList = jedis.lrange(userSelectedId, 0, listLength-1);
				}
				return userSelectedList;
			}
		});
		return userSelectedList;
	}

	/**
	 * 查询运营精选的个人列表
	 * @param userId
	 * @return
	 */
	public List<String> getUserOperationSelectedList(final long userId){
		List<String> userSelectedList = momentsClient.execute(new JedisWorker<List<String>>(){

			@Override
			public List<String> work(Jedis jedis) {
				String userSelectedId = JedisConstant.OPERATION_SELECTED_DYNAMEIC_ID+userId;
				long listLength = jedis.llen(userSelectedId);
				List<String> userSelectedList = new ArrayList<String>();
				if(listLength>0){
					userSelectedList = jedis.lrange(userSelectedId, 0, listLength-1);
				}
				return userSelectedList;
			}
		});
		return userSelectedList;
	}
	
	/**
	 * 
	 * <p>Title: getUserSelectedLength</p> 
	 * <p>Description: 获取每个人的精选列表长度</p>
	 * @author :changjiang
	 * date 2015-8-10 上午11:22:12
	 * @param userId
	 * @return
	 */
	public long getUserSelectedLength(final long userId){
		long length = momentsClient.execute(new JedisWorker<Long>(){
			@Override
			public Long work(Jedis jedis) {
				String userSelectedId = JedisConstant.SELECTED_MOMENT_DYNAMEIC_ID+userId;
				return  jedis.llen(userSelectedId);
			}
		});
		return length;
	}

	/**
	 * 获取每个人的运营精选列表的长度
	 * @param userId
	 * @return
	 */
	public long getUserOperationSelectedLength(final long userId){
		long length = momentsClient.execute(new JedisWorker<Long>(){
			@Override
			public Long work(Jedis jedis) {
				String userSelectedId = JedisConstant.OPERATION_SELECTED_DYNAMEIC_ID+userId;
				return  jedis.llen(userSelectedId);
			}
		});
		return length;
	}
	
	//faffd
	
	
	/**
	 * 
	 * <p>Title: getOneUserMoment</p> 
	 * <p>Description: 得到一个用户的朋友圈</p> 
	 * @author :changjiang
	 * date 2015-2-27 下午12:47:47
	 * @param userId
	 * @return
	 */
	public List<String> getOneUserMoment(final long userId,final Long rid){
		return getOneUserMoment(userId, rid, 0);
	}
	/**
	 * 
	 * <p>Title: getOneUserMoment</p> 
	 * <p>Description: 得到一个用户的朋友圈</p> 
	 * @author :changjiang
	 * date 2015-2-27 下午12:47:47
	 * @param userId
	 * @return
	 */
	public List<String> getOneUserMoment(final long userId,final Long rid,final int flag){
		List<String> userMoments = new ArrayList<String>();
		userMoments = momentsClient.execute(new JedisWorker<List<String>>(){
			//得到一个用户的朋友圈
			@Override
			public List<String> work(Jedis jedis) {
				String userMomentId = JedisConstant.USER_MOMENT_ID+userId;
				//long end = jedis.zcard(userMomentId);
						//jedis.llen(userMomentId);
				//System.out.println(end);
				// = new ArrayList<String>();
				List<String> resList = new ArrayList<String>(0);
				List<String> tempList = new ArrayList<String>(0);
				Set<String> tempSet = new HashSet<String>();
				long end = jedis.zcard(userMomentId);
				int size = 10;
				int realsize = 0;//第一次真实查询出的条数
				final int rightsize[] = {0};//经过排除过后的条数
				final Long lastid[]={null};
				if(end==0){
					return resList;
				}
				if(null==rid){//当传过来的值为空的时候，只取20条
					//tempList = jedis.lrange(userMomentId,end-20, end-1);
					if(end>size){//当缓存中得数量超过分页的数量时
						tempSet = jedis.zrange(userMomentId,0, size);
					}else{
						tempSet = jedis.zrange(userMomentId,0, end-1);
					}
					//jedis.zremrangeByScore(userMomentId, -171224570307678208L, -171224570307678208L);
					//System.out.println(tempSet.toString());
					//tempSet =jedis.zrevrangeByScore(userMomentId, rid, 0);
				}else{
					//查找列表中的value值得位置
					long index = jedis.zrank(userMomentId, rid+"");
					//得到分页的set列表
					long endIndex = index+size;
					if(endIndex>end){
						tempSet = jedis.zrange(userMomentId,index+1, end);
					}else {
						tempSet = jedis.zrange(userMomentId,index+1, endIndex);
					}
					//List<String> allList  = jedis.lrange(userMomentId,0, -1);
					//List<String> list  = jedis.lrange(userMomentId,0, -1);
					//if(null!=allList&&allList.size()>0){
						/*String listValue = "";
						long j = 0;
						for(int i=0;i<end;i++){
							listValue = allList.get(i);
							if(listValue.equals(rid+"")){
								j = i;
								break;
							}
						}
						tempList = jedis.lrange(userMomentId,j, 20);*/
					//}
				}
				if(tempSet!=null && tempSet.size()>0){
					realsize = tempSet.size();
				}
				
				resList = getResourceStrs(userId,tempSet, lastid, rightsize, flag);
				
				if(flag==0){
					//需要判断是否查够了，如果没有查够,则继续查询
					if(realsize == size){
						if(rightsize[0]<realsize){
							//说明排除了一些不能显示的，需要继续查询缓存
							//查找列表中的value值得位置
							boolean nomore = false;
							boolean circle = true;
							do{
								int size2 = size - rightsize[0];
								if(lastid[0]!=null){
									Set<String> tempSet2 = new HashSet<String>();
									long index = jedis.zrank(userMomentId, lastid[0]+"");
									//得到分页的set列表
									long endIndex = index+size2;
									if(endIndex>end){
										nomore = true;//没有更多了
										tempSet2 = jedis.zrange(userMomentId,index+1, end);
									}else {
										tempSet2 = jedis.zrange(userMomentId,index+1, endIndex);
									}
									List<String> resList2 = getResourceStrs(userId,tempSet2, lastid, rightsize, flag);
									resList.addAll(resList2);
									if(nomore){
										circle = false;
										break;
									}
								}else{
									circle = false;
									break;
								}
								if(rightsize[0]>=size){
									circle = false;
									break;
								}
							}while(circle);
						}
					}else if(realsize < size){
						//说明第一次查询时候缓存剩下的数据不够10条了，无需再继续查询
						
					}
				}
//				 System.out.println(resList);
				return resList;
			}
		});
		//通过朋友圈的rid获取资源列表
		/*resourceHashClient.execute(new JedisWorker<List<String>>(){

			@Override
			public List<String> work(Jedis jedis) {
				// TODO Auto-generated method stub
				Pipeline  pipeline = jedis.pipelined();
				List<Response<String>> resultList = new ArrayList<Response<String>>(userMoments.size());
				return null;
			}
			
		});*/
		
		return userMoments;
	}

	
	private List<String> getResourceStrs(final long userId,Set<String> tempSet,final Long lastid[],final int rightsize[],final int flag){
		List<String> resList = new ArrayList<String>(0);
		List<String> tempList = new ArrayList<String>(0);
		//朋友圈的20条信息
		//final List<String> list = new ArrayList<String>(tempList);
		final Set<String> set = new LinkedHashSet<String>(tempSet);
		resList = resourceHashClient.execute(new JedisWorker<List<String>>(){
			@Override
			public List<String> work(Jedis jedis) {
				//Pipeline  pipeline = jedis.pipelined();
				//List<Response<String>> resultList = new ArrayList<Response<String>>(list.size());
				List<String> resultLists = new LinkedList<String>();
				String key = "";
				String value = "";
				String resHashKey = JedisConstant.RESOURCE_HASH_ID;
				String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
				// pipeline.sync();
				String inList = "";
				String isPraise = "";
				String isUseful = "";
				String isCollect = "";
				String zNum = "";
				String cNum = "";
				String usefulCount = "";
				String uselessCount = "";
				for(String uidStr:set){
					try{
						lastid[0]=Long.valueOf(uidStr);//获取最后一条资源id
					}catch(Exception e){
						e.printStackTrace();
					}
					final String uidKey = uidStr;
					key = resHashKey+uidKey;
					value = jedis.hget(key, resHashValue);
					boolean isReBuild = false;
//					System.out.println(value);
					long begin = System.currentTimeMillis();
					if(null!=value){//查找用户对这个资源的关系,当前只有是否赞过
						
						if(flag==0){
							//当flag为0代表旧版的APP，不显示长书评，长影评
							String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+10);
							if(type!=null && type.contains("\"")){
								type = type.replace("\"", "");
							}
							if(CommentUtils.TYPE_ARTICLE_BOOK.equals(type) || CommentUtils.TYPE_ARTICLE_MOVIE.equals(type)){
								//是长书评或者长影评资源，不显示
								continue;//继续下一个循环
							}
						}
						//是需要的资源，数量加1
						rightsize[0] = rightsize[0]++;
						//获取用户信息的id
						final String uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
						if(!uid.equals("0")){//用户id不为0时
							Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
							if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
								//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
								value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
								value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
								value = value.replaceAll("\"level\": ?[0-9]+", "\"sex\":"+userInfoMap.get(JedisConstant.USER_HASH_SEX)+"");
								/*System.out.println("返回用户的信息威"+userInfoMap);
								System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
								System.out.println("返回的资源信息为"+value);*/
							}
									//new HashMap<String, String>();
						}
						Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
							@Override
							public Map<String,String> work(Jedis jedis) {
								//改动
								String relationKey = userId+JedisConstant.RELATION_TO_USER_AND_RES+uidKey;
								return jedis.hgetAll(relationKey);
							}
						} );
						if(null!=map&&!map.isEmpty()){//当关系不为空时
							/*inList = map.get(JedisConstant.RELATION_IS_INLIST);
							if(null!=inList){
								value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
							}*/
							isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
							if(null!=isPraise&&!"".equals(isPraise)){
								value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
							}
							isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
							if(null!=isCollect&&!"".equals(isCollect)){
								value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
							}
							if(!value.contains("isUseful")){//当这条资源不包含有用信息的话 重建
								isReBuild = true;
								value= value.substring(0,value.length()-1)+",\"isUseful\": 0,\"usefulCount\": \"0\",\"uselessCount\": \"0\"}";

							}
							isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
							if(null!=isUseful&&!"".equals(isUseful)){
								value = value.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
							}
						}
						//添加资源的附加信息 比如说评论数，点赞数
						final String rid = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
						if(!rid.equals("0")){
							Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
								//查询资源的附加信息
								@Override
								public Map<String, String> work(
										Jedis jedis) {
									String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
									return jedis.hgetAll(otherInfo);
								}
							});
							if(null!=otherMap&&!otherMap.isEmpty()){
								zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
								if(null!=zNum&&!"".equals(zNum)){
									value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
								}
								cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
								if(null!=cNum&&!"".equals(cNum)){
									value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
								}
								usefulCount = otherMap.get(JedisConstant.RELATION_USEFUL_NUM);
								if(null!=usefulCount&&!"".equals(usefulCount)){
									value = value.replaceAll("\"usefulCount\": ?\"[0-9]+\"", "\"usefulCount\": \""+usefulCount+"\"");
								}
								uselessCount = otherMap.get(JedisConstant.RELATION_USELESS_NUM);
								if(null!=uselessCount&&!"".equals(uselessCount)){
									value = value.replaceAll("\"uselessCount\": ?\"[0-9]+\"", "\"uselessCount\": \""+usefulCount+"\"");
								}
							}
							
							
						}
						//添加是否已经加入书单
						String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+10);
						if(type!=null && type.contains("\"")){
							type = type.replace("\"", "");
						}
						if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
							String bookId = "";
							Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
							Matcher bookIdMatcher = bookIdPattern.matcher(value);
							if(bookIdMatcher.find()){
								bookId = bookIdMatcher.group(1);
							}
							if(!"".equals(bookId)){//当电影的id不为空时
								final String bkId = bookId;
								inList = relationToUserandresClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
												return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
									}});
							}
						}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
							String movieId = "";
							Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
							Matcher movieIdMatcher = movieIdPattern.matcher(value);
							if(movieIdMatcher.find()){
								movieId = movieIdMatcher.group(1);
							}
							if(!"".equals(movieId)){//当电影的id不为空时
								final String mvId = movieId;
								inList = relationToUserandresClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
												return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
									}});
							}
						}
//						System.out.println(value);
						if(null!=inList&&!"".equals(inList)){
							value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
						}else{
							value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
						}
//						System.out.println(value);
						resultLists.add(value);
						//resultLists.add(value);
						//如果有新加入的资源 重新构建resource
						if(isReBuild){
							jedis.hset(key,resHashValue,value);
						}
						/*long end = System.currentTimeMillis();
						System.out.println("缓存中遍历附加信息的耗时："+(end-begin));*/
					}else{
						//当没有这个资源时，删除朋友圈的资源id
						momentsClient.execute(new JedisWorker<String>(){
							@Override
							public String work(Jedis jedis) {
								String userMomentId = JedisConstant.USER_MOMENT_ID+userId;
								long userScore = Long.valueOf(uidKey);
								jedis.zremrangeByScore(userMomentId, -userScore, -userScore);
								return null;
							}
						} );
					}
					//resultList.add(pipeline.get(key));
					//System.out.println(resultList.get(0).get());
					/*System.out.println(pipeline.get(key).toString());
					resultLists.add(pipeline.get(key).get());*/
				}
//				System.out.println(resultLists.toString());
				return resultLists;
			}
		});
		return resList;
	}
	
	
	/**
	 * 
	 * <p>Title: getOneUserDynamic</p> 
	 * <p>Description: 获取一个人的个人动态</p> 
	 * @author :changjiang
	 * date 2015-3-2 下午5:35:09
	 * @param userId
	 * @param rid
	 * @return
	 */
	public List<String> getOneUserDynamic(final long userId,final Long rid){
		List<String> userMoments = new ArrayList<String>();
		userMoments = momentsClient.execute(new JedisWorker<List<String>>(){
			//得到一个用户的朋友圈
			@Override
			public List<String> work(Jedis jedis) {
				String userDynamicId = JedisConstant.USER_DYNAMEIC_ID+userId;
				//long end = jedis.zcard(userMomentId);
						//jedis.llen(userMomentId);
				//System.out.println(end);
				// = new ArrayList<String>();
				 List<String> resList = new ArrayList<String>();
				List<String> tempList = new ArrayList<String>();
				Set<String> tempSet = new HashSet<String>();
				long end = jedis.zcard(userDynamicId);
				int size = 10;
				if(end==0){
					return resList;
				}
				if(null==rid){//当传过来的值为空的时候，只取20条
					if(end>size){
						tempSet = jedis.zrange(userDynamicId,0, size);
					}else{
						tempSet = jedis.zrange(userDynamicId,0, end-1);
					}
				}else{
					//查找列表中的value值得位置
					long index = jedis.zrank(userDynamicId, rid+"");
					//得到分页的set列表
					long endIndex = index+size;
					if(endIndex>end){
						tempSet = jedis.zrange(userDynamicId,index+1, end);
					}else {
						tempSet = jedis.zrange(userDynamicId,index+1, endIndex);
					}
				}
				//个人动态的20条信息
				final Set<String> set = new LinkedHashSet<String>(tempSet);
				resList = resourceHashClient.execute(new JedisWorker<List<String>>(){
					@Override
					public List<String> work(Jedis jedis) {
						List<String> resultLists = new LinkedList<String>();
						String key = "";
						String value = "";
						String resHashKey = JedisConstant.RESOURCE_HASH_ID;
						String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
						
						String inList = "";
						String isPraise = "";
						String isCollect = "";
						String isUseful = "";
						String zNum = "";
						String cNum = "";
						String usefulCount = "";
						String uselessCount = "";
						
						for(String uidStr:set){
							final String uidKey = uidStr;
							key = resHashKey+uidKey;
							value = jedis.hget(key, resHashValue);
							boolean isReBuild = false;
							if(null!=value){
								
								//获取用户信息的id
								final String uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
								if(!uid.equals("0")){//用户id不为0时
									Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
									if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
										//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
										value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
										value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
										value = value.replaceAll("\"level\": ?[0-9]+", "\"sex\":"+userInfoMap.get(JedisConstant.USER_HASH_SEX)+"");
										/*System.out.println("返回用户的信息威"+userInfoMap);
										System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
										System.out.println("返回的资源信息为"+value);*/
									}
								}
								
								Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
									@Override
									public Map<String,String> work(Jedis jedis) {
										//改动
										String relationKey = userId+JedisConstant.RELATION_TO_USER_AND_RES+uidKey;
										return jedis.hgetAll(relationKey);
									}
								} );
								if(null!=map&&!map.isEmpty()){//当关系不为空时
									isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
									if(null!=isPraise&&!"".equals(isPraise)){
										value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
									}
									isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
									if(null!=isCollect&&!"".equals(isCollect)){
										value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
									}
									if(!value.contains("isUseful")){//当这条资源不包含有用信息的话 重建
										isReBuild = true;
										value= value.substring(0,value.length()-1)+",\"isUseful\": 0,\"usefulCount\": \"0\",\"uselessCount\": \"0\"}";

									}
									isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
									if(null!=isUseful&&!"".equals(isUseful)){
										value = value.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
									}
								}
								//添加资源的附加信息 比如说评论数，点赞数
								final String rid = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
								if(!rid.equals("0")){
									Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
										//查询资源的附加信息
										@Override
										public Map<String, String> work(
												Jedis jedis) {
											String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
											return jedis.hgetAll(otherInfo);
										}
										
									});
									if(null!=otherMap&&!otherMap.isEmpty()){
										zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
										if(null!=zNum&&!"".equals(zNum)){
											value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
										}
										cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
										if(null!=cNum&&!"".equals(cNum)){
											value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
										}
										usefulCount = otherMap.get(JedisConstant.RELATION_USEFUL_NUM);
										if(null!=usefulCount&&!"".equals(usefulCount)){
											value = value.replaceAll("\"usefulCount\": ?\"[0-9]+\"", "\"usefulCount\": \""+usefulCount+"\"");
										}
										uselessCount = otherMap.get(JedisConstant.RELATION_USELESS_NUM);
										if(null!=uselessCount&&!"".equals(uselessCount)){
											value = value.replaceAll("\"uselessCount\": ?\"[0-9]+\"", "\"uselessCount\": \""+usefulCount+"\"");
										}
									}
									
								}
								//添加是否已经加入书单
								String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+10);
								if(type!=null && type.contains("\"")){
									type = type.replace("\"", "");
								}
								if(!CommentUtils.TYPE_NEWARTICLE.equals(type)){
									//增加阅读数
									int readCount = resStatJedisManager.getReadNum(Long.valueOf(rid), type);
									//System.out.println("阅读数为："+readCount+"阅读的resid为"+rid);
									if(0!=readCount){
										value = value.replaceAll("\"readingCount\": ?\"[0-9]+\"", "\"readingCount\": \""+readCount+"\"");
									}
								}
								if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
									String bookId = "";
									Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
									Matcher bookIdMatcher = bookIdPattern.matcher(value);
									if(bookIdMatcher.find()){
										bookId = bookIdMatcher.group(1);
									}
									if(!"".equals(bookId)){//当电影的id不为空时
										final String bkId = bookId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
									String movieId = "";
									Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
									Matcher movieIdMatcher = movieIdPattern.matcher(value);
									if(movieIdMatcher.find()){
										movieId = movieIdMatcher.group(1);
									}
									if(!"".equals(movieId)){//当电影的id不为空时
										final String mvId = movieId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}
								if(null!=inList&&!"".equals(inList)){
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
								}else{
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
								}
								resultLists.add(value);
								//如果有新加入的资源 重新构建resource
								if(isReBuild){
									jedis.hset(key,resHashValue,value);
								}
							}else{
								//当没有这个资源时，删除朋友圈的资源id
								momentsClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										String userMomentId = JedisConstant.USER_DYNAMEIC_ID+userId;
										long userScore = Long.valueOf(uidKey);
										jedis.zremrangeByScore(userMomentId, -userScore, -userScore);
										return null;
									}
								} );
							}
						}
						return resultLists;
					}
				});
				return resList;
			}
		});
		return userMoments;
	}
	
	/**
	 * 
	 * <p>Title: getOneUserCollected</p> 
	 * <p>Description: 获取用户的收藏列表</p> 
	 * @author :changjiang
	 * date 2015-4-3 下午4:12:58
	 * @param userId
	 * @param rid
	 * @return
	 */
	public List<String> getOneUserCollected(final long userId,final Long rid){
		List<String> userMoments = new ArrayList<String>();
		userMoments = momentsClient.execute(new JedisWorker<List<String>>(){
			//得到一个用户的朋友圈
			@Override
			public List<String> work(Jedis jedis) {
				String userCollectedId = JedisConstant.USER_COLLECTED_ID+userId;
				//long end = jedis.zcard(userMomentId);
						//jedis.llen(userMomentId);
				//System.out.println(end);
				// = new ArrayList<String>();
				 List<String> resList = new ArrayList<String>();
				List<String> tempList = new ArrayList<String>();
				Set<String> tempSet = new HashSet<String>();
				long end = jedis.zcard(userCollectedId);
				int size = 10;
				if(end==0){
					return resList;
				}
				if(null==rid){//当传过来的值为空的时候，只取20条
					if(end>size){
						tempSet = jedis.zrange(userCollectedId,0, size);
					}else{
						tempSet = jedis.zrange(userCollectedId,0, end-1);
					}
				}else{
					//查找列表中的value值得位置
					long index = jedis.zrank(userCollectedId, rid+"");
					//得到分页的set列表
					long endIndex = index+size;
					if(endIndex>end){
						tempSet = jedis.zrange(userCollectedId,index+1, end);
					}else {
						tempSet = jedis.zrange(userCollectedId,index+1, endIndex);
					}
				}
				//个人收藏的20条信息
				final Set<String> set = new LinkedHashSet<String>(tempSet);
				resList = resourceHashClient.execute(new JedisWorker<List<String>>(){
					@Override
					public List<String> work(Jedis jedis) {
						List<String> resultLists = new LinkedList<String>();
						String key = "";
						String value = "";
						String resHashKey = JedisConstant.RESOURCE_HASH_ID;
						String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
						
						String inList = "";
						String isPraise = "";
						String isCollect = "";
						String isUseful = "";
						String zNum = "";
						String cNum = "";
						
						for(String uidStr:set){
							final String uidKey = uidStr;
							key = resHashKey+uidKey;
							value = jedis.hget(key, resHashValue);
							if(null!=value){
								
								//获取用户信息的id
								final String uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
								if(!uid.equals("0")){//用户id不为0时
									Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
									if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
										//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
										value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
										value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
										value = value.replaceAll("\"level\": ?[0-9]+", "\"sex\":"+userInfoMap.get(JedisConstant.USER_HASH_SEX)+"");
										/*System.out.println("返回用户的信息威"+userInfoMap);
										System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
										System.out.println("返回的资源信息为"+value);*/
									}
								}
								
								Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
									@Override
									public Map<String,String> work(Jedis jedis) {
										//改动
										String relationKey = userId+JedisConstant.RELATION_TO_USER_AND_RES+uidKey;
										return jedis.hgetAll(relationKey);
									}
								} );
								if(null!=map&&!map.isEmpty()){//当关系不为空时
									/*inList = map.get(JedisConstant.RELATION_IS_INLIST);
									if(null!=inList){
										value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
									}*/
									isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
									if(null!=isPraise&&!"".equals(isPraise)){
										value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
									}
									isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
									if(null!=isCollect&&!"".equals(isCollect)){
										value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
									}
									isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
									if(null!=isUseful&&!"".equals(isUseful)){
										value = value.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
									}
									/*zNum = map.get(JedisConstant.RELATION_PRAISE_NUM);
									if(null!=zNum&&!"".equals(zNum)){
										value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
									}
									cNum = map.get(JedisConstant.RELATION_COMMENT_NUM);
									if(null!=cNum&&!"".equals(cNum)){
										value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
									}*/
								}
								//添加资源的附加信息 比如说评论数，点赞数
								final String rid = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
								if(!rid.equals("0")){
									Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
										//查询资源的附加信息
										@Override
										public Map<String, String> work(
												Jedis jedis) {
											String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
											return jedis.hgetAll(otherInfo);
										}
										
									});
									if(null!=otherMap&&!otherMap.isEmpty()){
										zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
										if(null!=zNum&&!"".equals(zNum)){
											value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
										}
										cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
										if(null!=cNum&&!"".equals(cNum)){
											value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
										}
									}
								}
								//添加是否已经加入书单
								String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+10);
								if(type!=null && type.contains("\"")){
									type = type.replace("\"", "");
								}
								if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
									String bookId = "";
									Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
									Matcher bookIdMatcher = bookIdPattern.matcher(value);
									if(bookIdMatcher.find()){
										bookId = bookIdMatcher.group(1);
									}
									if(!"".equals(bookId)){//当电影的id不为空时
										final String bkId = bookId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
									String movieId = "";
									Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
									Matcher movieIdMatcher = movieIdPattern.matcher(value);
									if(movieIdMatcher.find()){
										movieId = movieIdMatcher.group(1);
									}
									if(!"".equals(movieId)){//当电影的id不为空时
										final String mvId = movieId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}
								if(null!=inList&&!"".equals(inList)){
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
								}else{
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
								}
								resultLists.add(value);
							}else{
								//当没有这个资源时，删除朋友圈的资源id
								momentsClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										String userMomentId = JedisConstant.USER_COLLECTED_ID+userId;
										long userScore = Long.valueOf(uidKey);
										//jedis.zremrangeByScore(userMomentId, -userScore, -userScore);
										jedis.zrem(userMomentId, userScore+"");
										return null;
									}
								} );
							}
						}
						return resultLists;
					}
				});
				return resList;
			}
		});
		return userMoments;
	}
	
	/**
	 * 
	 * <p>Title: getOtherUserDynamic</p> 
	 * <p>Description: 看他人的动态</p> 
	 * @author :changjiang
	 * date 2015-3-4 下午6:35:23
	 * @param momentUserId
	 * @param rid
	 * @return
	 */
	public List<String> getOtherUserDynamic(final long userId,final long momentUserId,final Long rid){
		List<String> userMoments = new ArrayList<String>();
		userMoments = momentsClient.execute(new JedisWorker<List<String>>(){
			//得到他看的用户的个人动态
			@Override
			public List<String> work(Jedis jedis) {
				String userDynamicId = JedisConstant.USER_DYNAMEIC_ID+userId;
				//long end = jedis.zcard(userMomentId);
						//jedis.llen(userMomentId);
				//System.out.println(end);
				// = new ArrayList<String>();
				 List<String> resList = new ArrayList<String>();
				List<String> tempList = new ArrayList<String>();
				Set<String> tempSet = new HashSet<String>();
				long end = jedis.zcard(userDynamicId);
				int size = 10;
				if(end==0){
					return resList;
				}
				if(null==rid){//当传过来的值为空的时候，只取20条
					if(end>size){
						tempSet = jedis.zrange(userDynamicId,0, size);
					}else{
						tempSet = jedis.zrange(userDynamicId,0, end-1);
					}
				}else{
					//查找列表中的value值得位置
					long index = jedis.zrank(userDynamicId, rid+"");
					//得到分页的set列表
					long endIndex = index+size;
					if(endIndex>end){
						tempSet = jedis.zrange(userDynamicId,index+1, end);
					}else {
						tempSet = jedis.zrange(userDynamicId,index+1, endIndex);
					}
				}
				//个人动态的20条信息
				final Set<String> set = new LinkedHashSet<String>(tempSet);
				resList = resourceHashClient.execute(new JedisWorker<List<String>>(){
					@Override
					public List<String> work(Jedis jedis) {
						List<String> resultLists = new LinkedList<String>();
						String key = "";
						String value = "";
						String resHashKey = JedisConstant.RESOURCE_HASH_ID;
						String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
						
						String inList = "";
						String isPraise = "";
						String isCollect = "";
						String isUseful = "";
						String zNum = "";
						String cNum = "";
						String usefulCount = "";
						String uselessCount = "";

						for(String uidStr:set){
							final String uidKey = uidStr;
							key = resHashKey+uidKey;
							value = jedis.hget(key, resHashValue);
							boolean isReBuild = false;
							if(null!=value){//当资源不为空时，获取当前用户对这个资源的关系
								//获取用户信息的id
								final String uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
								if(!uid.equals("0")){//用户id不为0时
									Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
									if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
										//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
										value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
										value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
										value = value.replaceAll("\"level\": ?[0-9]+", "\"sex\":"+userInfoMap.get(JedisConstant.USER_HASH_SEX)+"");
										/*System.out.println("返回用户的信息威"+userInfoMap);
										System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
										System.out.println("返回的资源信息为"+value);*/
									}
								}
								
								
								Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
									@Override
									public Map<String,String> work(Jedis jedis) {
										//改动
										String relationKey = momentUserId+JedisConstant.RELATION_TO_USER_AND_RES+uidKey;
										return jedis.hgetAll(relationKey);
									}
								} );
								if(null!=map&&!map.isEmpty()){//当关系不为空时
									/*inList = map.get(JedisConstant.RELATION_IS_INLIST);
									if(null!=inList){
										value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
									}*/
									isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
									if(null!=isPraise&&!"".equals(isPraise)){
										value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
									}
									isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
									if(null!=isCollect&&!"".equals(isCollect)){
										value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
									}
									if(!value.contains("isUseful")){//当这条资源不包含有用信息的话 重建
										isReBuild = true;
										value= value.substring(0,value.length()-1)+",\"isUseful\": 0,\"usefulCount\": \"0\",\"uselessCount\": \"0\"}";

									}
									isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
									if(null!=isUseful&&!"".equals(isUseful)){
										value = value.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
									}
									/*zNum = map.get(JedisConstant.RELATION_PRAISE_NUM);
									if(null!=zNum&&!"".equals(zNum)){
										value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
									}
									cNum = map.get(JedisConstant.RELATION_COMMENT_NUM);
									if(null!=cNum&&!"".equals(cNum)){
										value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
									}*/
								}
								//添加资源的附加信息 比如说评论数，点赞数
								final String rid = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
								if(!rid.equals("0")){
									Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
										//查询资源的附加信息
										@Override
										public Map<String, String> work(
												Jedis jedis) {
											String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
											return jedis.hgetAll(otherInfo);
										}
										
									});
									if(null!=otherMap&&!otherMap.isEmpty()){
										zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
										if(null!=zNum&&!"".equals(zNum)){
											value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
										}
										cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
										if(null!=cNum&&!"".equals(cNum)){
											value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
										}
										usefulCount = otherMap.get(JedisConstant.RELATION_USEFUL_NUM);
										if(null!=usefulCount&&!"".equals(usefulCount)){
											value = value.replaceAll("\"usefulCount\": ?\"[0-9]+\"", "\"usefulCount\": \""+usefulCount+"\"");
										}
										uselessCount = otherMap.get(JedisConstant.RELATION_USELESS_NUM);
										if(null!=uselessCount&&!"".equals(uselessCount)){
											value = value.replaceAll("\"uselessCount\": ?\"[0-9]+\"", "\"uselessCount\": \""+usefulCount+"\"");
										}
									}
								}
								//添加是否已经加入书单
								String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+10);
								if(type!=null && type.contains("\"")){
									type = type.replace("\"", "");
								}
								//增加阅读数
								if(!CommentUtils.TYPE_NEWARTICLE.equals(type)){
									int readCount = resStatJedisManager.getReadNum(Long.valueOf(rid), type);
									//System.out.println("阅读数为："+readCount+"阅读的resid为"+rid);
									if(0!=readCount){
										value = value.replaceAll("\"readingCount\": ?\"[0-9]+\"", "\"readingCount\": \""+readCount+"\"");
									}
								}
								if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
									String bookId = "";
									Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
									Matcher bookIdMatcher = bookIdPattern.matcher(value);
									if(bookIdMatcher.find()){
										bookId = bookIdMatcher.group(1);
									}
									if(!"".equals(bookId)){//当电影的id不为空时
										final String bkId = bookId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
									String movieId = "";
									Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
									Matcher movieIdMatcher = movieIdPattern.matcher(value);
									if(movieIdMatcher.find()){
										movieId = movieIdMatcher.group(1);
									}
									if(!"".equals(movieId)){//当电影的id不为空时
										final String mvId = movieId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}
								if(null!=inList&&!"".equals(inList)){
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
								}else{
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
								}
		
								resultLists.add(value);
								//如果有新加入的资源 重新构建resource
								if(isReBuild){
									jedis.hset(key,resHashValue,value);
								}
							}else{
								//当没有这个资源时，删除朋友圈的资源id
								momentsClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										String userMomentId = JedisConstant.USER_DYNAMEIC_ID+userId;
										long userScore = Long.valueOf(uidKey);
										jedis.zremrangeByScore(userMomentId, -userScore, -userScore);
										return null;
									}
								} );
							}
						}
						return resultLists;
					}
				});
				return resList;
			}
		});
		return userMoments;
	}
	
	/**
	 * 
	 * <p>Title: getSquareMoment</p> 
	 * <p>Description: 查找广场的信息</p> 
	 * @author :changjiang
	 * date 2015-3-3 上午11:43:26
	 * @param momentUserId	当前用户的id
	 * @param rid
	 * @return
	 */
	public List<String> getSquareMoment(final long momentUserId,final Long rid,final String userType){
		List<String> userMoments = new ArrayList<String>();
	/*	if(userType.equals("50")){//神人
			userMomentKey = JedisConstant.SHENREN_MOMENT_ID;
		}else{//普通人
			userMomentKey = JedisConstant.COMMON_MOMENT_ID;
		}*/
		final String userMomentId =  JedisConstant.USER_MOMENT_ID+0;
		
		userMoments = momentsClient.execute(new JedisWorker<List<String>>(){
			//得到用户id为0的广场的信息
			@Override
			public List<String> work(Jedis jedis) {
				//String userMomentId = JedisConstant.USER_MOMENT_ID+0;
				 List<String> resList = new ArrayList<String>();
				List<String> tempList = new ArrayList<String>();
				Set<String> tempSet = new HashSet<String>();
				long end = jedis.zcard(userMomentId);
				int size = 10;
				if(end==0){
					return resList;
				}
				if(null==rid){//当传过来的值为空的时候，只取20条
					//tempList = jedis.lrange(userMomentId,end-20, end-1);
					if(end>size){
						tempSet = jedis.zrange(userMomentId,0, size);
					}else{
						tempSet = jedis.zrange(userMomentId,0, end-1);
					}
				}else{
					//查找列表中的value值得位置
					long index = jedis.zrank(userMomentId, rid+"");
					//得到分页的set列表
					long endIndex = index+size;
					if(endIndex>end){
						tempSet = jedis.zrange(userMomentId,index+1, end);
					}else {
						tempSet = jedis.zrange(userMomentId,index+1, endIndex);
					}
				}
				//朋友圈的20条信息
				//final List<String> list = new ArrayList<String>(tempList);
				final Set<String> set = new LinkedHashSet<String>(tempSet);
				resList = resourceHashClient.execute(new JedisWorker<List<String>>(){
					@Override
					public List<String> work(Jedis jedis) {
						//Pipeline  pipeline = jedis.pipelined();
						//List<Response<String>> resultList = new ArrayList<Response<String>>(list.size());
						List<String> resultLists = new LinkedList<String>();
						String key = "";
						String value = "";
						String resHashKey = JedisConstant.RESOURCE_HASH_ID;
						String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
						// pipeline.sync();
						String inList = "";
						String isPraise = "";
						String isCollect = "";
						String isUseful = "";
						String zNum = "";
						String cNum = "";
						String usefulCount = "";
						String uselessCount = "";
						for(String uidStr:set){
							final String uidKey = uidStr;
							key = resHashKey+uidKey;
							value = jedis.hget(key, resHashValue);
							//是否重建缓存资源
							boolean isReBuild = false;
//							System.out.println(value);
							if(null!=value){
								//获取用户信息的id
								final String uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
								if(!uid.equals("0")){//用户id不为0时
									Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
									if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
										//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
										value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
										value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
										value = value.replaceAll("\"level\": ?[0-9]+", "\"sex\":"+userInfoMap.get(JedisConstant.USER_HASH_SEX)+"");
										/*System.out.println("返回用户的信息威"+userInfoMap);
										System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
										System.out.println("返回的资源信息为"+value);*/
									}
								}
								
								Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
									@Override
									public Map<String,String> work(Jedis jedis) {
										//改动
										String relationKey = momentUserId+JedisConstant.RELATION_TO_USER_AND_RES+uidKey;
										return jedis.hgetAll(relationKey);
									}
								} );
								if(null!=map&&!map.isEmpty()){//当关系不为空时
									isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
									if(null!=isPraise&&!"".equals(isPraise)){
										value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
									}
									isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
									if(null!=isCollect&&!"".equals(isCollect)){
										value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
									}
									if(!value.contains("isUseful")){//当这条资源不包含有用信息的话 重建
										isReBuild = true;
										value= value.substring(0,value.length()-1)+",\"isUseful\": 0,\"usefulCount\": \"0\",\"uselessCount\": \"0\"}";

									}
									isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
									if(null!=isUseful&&!"".equals(isUseful)){
										value = value.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
									}

									/*zNum = map.get(JedisConstant.RELATION_PRAISE_NUM);
									if(null!=zNum&&!"".equals(zNum)){
										value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
									}
									cNum = map.get(JedisConstant.RELATION_COMMENT_NUM);
									if(null!=cNum&&!"".equals(cNum)){
										value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
									}*/
								}
								//添加资源的附加信息 比如说评论数，点赞数
								final String rid = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
								if(!rid.equals("0")){
									Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
										//查询资源的附加信息
										@Override
										public Map<String, String> work(
												Jedis jedis) {
											String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
											return jedis.hgetAll(otherInfo);
										}
										
									});
									if(null!=otherMap&&!otherMap.isEmpty()){
										zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
										if(null!=zNum&&!"".equals(zNum)){
											value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
										}
										cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
										if(null!=cNum&&!"".equals(cNum)){
											value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
										}
										usefulCount = otherMap.get(JedisConstant.RELATION_USEFUL_NUM);
//										System.out.println("资源id为"+rid+"得到的有用总数为"+usefulCount);
//										System.out.println("这条资源的信息为"+value);
										if(null!=usefulCount&&!"".equals(usefulCount)){
											value = value.replaceAll("\"usefulCount\": ?\"[0-9]+\"", "\"usefulCount\": \""+usefulCount+"\"");
										}
										uselessCount = otherMap.get(JedisConstant.RELATION_USELESS_NUM);
										if(null!=uselessCount&&!"".equals(uselessCount)){
											value = value.replaceAll("\"uselessCount\": ?\"[0-9]+\"", "\"uselessCount\": \""+usefulCount+"\"");
										}

									}
								}
								//添加是否已经加入书单
								String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+10);
								if(type!=null && type.contains("\"")){
									type = type.replace("\"", "");
								}
								if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
									String bookId = "";
									Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
									Matcher bookIdMatcher = bookIdPattern.matcher(value);
									if(bookIdMatcher.find()){
										bookId = bookIdMatcher.group(1);
									}
									if(!"".equals(bookId)){//当图书的id不为空时
										final String bkId = bookId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
									String movieId = "";
									Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
									Matcher movieIdMatcher = movieIdPattern.matcher(value);
									if(movieIdMatcher.find()){
										movieId = movieIdMatcher.group(1);
									}
									if(!"".equals(movieId)){//当电影的id不为空时
										final String mvId = movieId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}
								if(null!=inList&&!"".equals(inList)){
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
								}else{
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
								}		
								resultLists.add(value);
								//如果有新加入的资源 重新构建resource
								if(isReBuild){
									jedis.hset(key,resHashValue,value);
								}

							}else{
								//当没有这个资源时，删除广场的资源id
								momentsClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										String userMomentId = JedisConstant.USER_MOMENT_ID+0;
										long userScore = Long.valueOf(uidKey);
										jedis.zremrangeByScore(userMomentId, -userScore, -userScore);
										return null;
									}
								} );
							}
						}
//						System.out.println(resultLists.toString());
						return resultLists;
					}
				});
//				 System.out.println(resList);
				return resList;
			}
		});
		return userMoments;
	}
	
	/**
	 * 
	 * <p>Title: getUserTypeSquareMoment</p> 
	 * <p>Description: 查询用户的朋友圈</p> 
	 * @author :changjiang
	 * date 2015-4-13 下午8:30:34
	 * @param momentUserId
	 * @param rid
	 * @param userType
	 * @return
	 */
	public List<String> getUserTypeSquareMoment(final long momentUserId,final Long rid,final String userType){
		List<String> userMoments = new ArrayList<String>();
		String userMomentKey = "";//JedisConstant.USER_MOMENT_ID+0;
		if(userType.equals("50")){//神人
			userMomentKey = JedisConstant.SHENREN_MOMENT_ID;
		}else{//普通人
			userMomentKey = JedisConstant.COMMON_MOMENT_ID;
		}
		final String userMomentId = userMomentKey;
		
		userMoments = momentsClient.execute(new JedisWorker<List<String>>(){
			//得到用户id为0的广场的信息
			@Override
			public List<String> work(Jedis jedis) {
				//String userMomentId = JedisConstant.USER_MOMENT_ID+0;
				 List<String> resList = new ArrayList<String>();
				List<String> tempList = new ArrayList<String>();
				Set<String> tempSet = new HashSet<String>();
				long end = jedis.zcard(userMomentId);
				int size = 10;
				if(end==0){
					return resList;
				}
				if(null==rid){//当传过来的值为空的时候，只取20条
					//tempList = jedis.lrange(userMomentId,end-20, end-1);
					if(end>size){
						tempSet = jedis.zrange(userMomentId,0, size);
					}else{
						tempSet = jedis.zrange(userMomentId,0, end-1);
					}
				}else{
					//查找列表中的value值得位置
					long index = jedis.zrank(userMomentId, rid+"");
					//得到分页的set列表
					long endIndex = index+size;
					if(endIndex>end){
						tempSet = jedis.zrange(userMomentId,index+1, end);
					}else {
						tempSet = jedis.zrange(userMomentId,index+1, endIndex);
					}
				}
				//朋友圈的20条信息
				//final List<String> list = new ArrayList<String>(tempList);
				final Set<String> set = new LinkedHashSet<String>(tempSet);
				resList = resourceHashClient.execute(new JedisWorker<List<String>>(){
					@Override
					public List<String> work(Jedis jedis) {
						//Pipeline  pipeline = jedis.pipelined();
						//List<Response<String>> resultList = new ArrayList<Response<String>>(list.size());
						List<String> resultLists = new LinkedList<String>();
						String key = "";
						String value = "";
						String resHashKey = JedisConstant.RESOURCE_HASH_ID;
						String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
						// pipeline.sync();
						String inList = "";
						String isPraise = "";
						String isCollect = "";
						String isUseful= "";
						String zNum = "";
						String cNum = "";
						String usefulCount = "";
						String uselessCount = "";
						for(String uidStr:set){
							final String uidKey = uidStr;
							key = resHashKey+uidKey;
							//System.out.println("获取的key为"+key);
							value = jedis.hget(key, resHashValue);
							boolean isReBuild = false;
//							System.out.println(value);
							if(null!=value){
								//获取用户信息的id
								String uid = "0";
								if(value.contains("\"userEntity\":{\"id\":")&&value.contains(",\"nickName\"")){
									uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
								}
								if(!uid.equals("0")){//用户id不为0时
									Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
									if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
										//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
										value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
										value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
										value = value.replaceAll("\"level\": ?[0-9]+", "\"sex\":"+userInfoMap.get(JedisConstant.USER_HASH_SEX)+"");
										/*System.out.println("返回用户的信息威"+userInfoMap);
										System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
										System.out.println("返回的资源信息为"+value);*/
									}
								}
								
								Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
									@Override
									public Map<String,String> work(Jedis jedis) {
										//改动
										String relationKey = momentUserId+JedisConstant.RELATION_TO_USER_AND_RES+uidKey;
										return jedis.hgetAll(relationKey);
									}
								} );
								if(null!=map&&!map.isEmpty()){//当关系不为空时
									isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
									if(null!=isPraise&&!"".equals(isPraise)){
										value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
									}
									isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
									if(null!=isCollect&&!"".equals(isCollect)){
										value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
									}
									if(!value.contains("isUseful")){//当这条资源不包含有用信息的话 重建
										isReBuild = true;
										value= value.substring(0,value.length()-1)+",\"isUseful\": 0,\"usefulCount\": \"0\",\"uselessCount\": \"0\"}";

									}
									isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
									if(null!=isUseful&&!"".equals(isUseful)){
										value = value.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
									}
									/*zNum = map.get(JedisConstant.RELATION_PRAISE_NUM);
									if(null!=zNum&&!"".equals(zNum)){
										value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
									}
									cNum = map.get(JedisConstant.RELATION_COMMENT_NUM);
									if(null!=cNum&&!"".equals(cNum)){
										value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
									}*/
								}
								//添加资源的附加信息 比如说评论数，点赞数
								String ridStr = "0";
								//if(value.contains("\"rid\":\"")){
									ridStr = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
								//}
								final String rid = ridStr;
								if(!rid.equals("0")){
									Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
										//查询资源的附加信息
										@Override
										public Map<String, String> work(
												Jedis jedis) {
											String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
											return jedis.hgetAll(otherInfo);
										}
										
									});
									if(null!=otherMap&&!otherMap.isEmpty()){
										zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
										if(null!=zNum&&!"".equals(zNum)){
											value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
										}
										cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
										if(null!=cNum&&!"".equals(cNum)){
											value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
										}
										usefulCount = otherMap.get(JedisConstant.RELATION_USEFUL_NUM);
										//System.out.println("资源id为"+rid+"得到的有用总数为"+usefulCount);
										if(null!=usefulCount&&!"".equals(usefulCount)){
											value = value.replaceAll("\"usefulCount\": ?\"[0-9]+\"", "\"usefulCount\": \""+usefulCount+"\"");
										}
										uselessCount = otherMap.get(JedisConstant.RELATION_USELESS_NUM);
										if(null!=uselessCount&&!"".equals(uselessCount)){
											value = value.replaceAll("\"uselessCount\": ?\"[0-9]+\"", "\"uselessCount\": \""+usefulCount+"\"");
										}
									}
								}
								//添加是否已经加入书单
								String type = "0";
								if(value.contains("\"type\":\"")){
									type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+9);
								}
								if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
									String bookId = "";
									Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
									Matcher bookIdMatcher = bookIdPattern.matcher(value);
									if(bookIdMatcher.find()){
										bookId = bookIdMatcher.group(1);
									}
									if(!"".equals(bookId)){//当图书的id不为空时
										final String bkId = bookId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
									String movieId = "";
									Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
									Matcher movieIdMatcher = movieIdPattern.matcher(value);
									if(movieIdMatcher.find()){
										movieId = movieIdMatcher.group(1);
									}
									if(!"".equals(movieId)){//当电影的id不为空时
										final String mvId = movieId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}
								if(null!=inList&&!"".equals(inList)){
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
								}else{
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
								}		
								resultLists.add(value);
								//如果有新加入的资源 重新构建resource
								if(isReBuild){
									jedis.hset(key,resHashValue,value);
								}
							}else{
								//当没有这个资源时，删除广场的资源id
								momentsClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										//String userMomentId = userMomentId;
										long userScore = Long.valueOf(uidKey);
										jedis.zremrangeByScore(userMomentId, -userScore, -userScore);
										return null;
									}
								} );
							}
						}
//						System.out.println(resultLists.toString());
						return resultLists;
					}
				});
//				 System.out.println(resList);
				return resList;
			}
		});
		return userMoments;
	}
	
	
	/**
	 * 
	 * <p>Title: getUserTypeSquareMoment</p> 
	 * <p>Description: 查询精选列表</p> 
	 * @author :changjiang
	 * date 2015-4-13 下午8:30:34
	 * @param momentUserId
	 * @param rid
	 * @param userType
	 * @return
	 */
	public List<String> getSelectedSquareMoment(final long momentUserId,final Long score,final Long[] newscore){
		List<String> userMoments = new ArrayList<String>();
		String userMomentKey = JedisConstant.SELECTED_MOMENT_ID;
		final String userMomentId = userMomentKey;
		
		userMoments = momentsClient.execute(new JedisWorker<List<String>>(){
			//得到用户id为0的广场的信息
			@Override
			public List<String> work(Jedis jedis) {
				//String userMomentId = JedisConstant.USER_MOMENT_ID+0;
				 List<String> resList = new ArrayList<String>();
				List<String> tempList = new ArrayList<String>();
				Set<String> tempSet = new HashSet<String>();
				long end = jedis.zcard(userMomentId);
				int size = 10;
				if(end==0){
					return resList;
				}
				if(null==score){//当传过来的值为空的时候，只取20条
					//tempList = jedis.lrange(userMomentId,end-20, end-1);
					if(end>size){
						tempSet = jedis.zrange(userMomentId,0, size);
					}else{
						tempSet = jedis.zrange(userMomentId,0, end-1);
					}
				}else{
					//查找列表中的value值得位置
					long index = jedis.zrank(userMomentId, score+"");
					//得到分页的set列表
					long endIndex = index+size;
					if(endIndex>end){
						tempSet = jedis.zrange(userMomentId,index+1, end);
					}else {
						tempSet = jedis.zrange(userMomentId,index+1, endIndex);
					}
				}
				try{
					//需要查询出score，用于与客户端进行交换使用
					if(newscore!=null && newscore.length>0){
						newscore[0] = 0l;
						if(tempSet!=null && tempSet.size()>0){
							newscore[0] = -(jedis.zscore(userMomentId, (String) tempSet.toArray()[tempSet.size()-1]).longValue());
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				//朋友圈的20条信息
				//final List<String> list = new ArrayList<String>(tempList);
				final Set<String> set = new LinkedHashSet<String>(tempSet);
				resList = resourceHashClient.execute(new JedisWorker<List<String>>(){
					@Override
					public List<String> work(Jedis jedis) {
						//Pipeline  pipeline = jedis.pipelined();
						//List<Response<String>> resultList = new ArrayList<Response<String>>(list.size());
						List<String> resultLists = new LinkedList<String>();
						String key = "";
						String value = "";
						String resHashKey = JedisConstant.RESOURCE_HASH_ID;
						String resHashValue = JedisConstant.RESOURCE_HASH_INFO;
						// pipeline.sync();
						String inList = "";
						String isPraise = "";
						String isCollect = "";
						String isUseful = "";
						String zNum = "";
						String cNum = "";
						for(String uidStr:set){
							final String uidKey = uidStr;
							key = resHashKey+uidKey;
							//System.out.println("获取的key为"+key);
							value = jedis.hget(key, resHashValue);
//							System.out.println(value);
							if(null!=value){
								//获取用户信息的id
								String uid = "0";
								if(value.contains("\"userEntity\":{\"id\":")&&value.contains(",\"nickName\"")){
									uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
								}
								if(!uid.equals("0")){//用户id不为0时
									Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
									if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
										//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
										value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
										value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
										value = value.replaceAll("\"level\": ?[0-9]+", "\"sex\":"+userInfoMap.get(JedisConstant.USER_HASH_SEX)+"");
										/*System.out.println("返回用户的信息威"+userInfoMap);
										System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
										System.out.println("返回的资源信息为"+value);*/
									}
								}
								
								Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
									@Override
									public Map<String,String> work(Jedis jedis) {
										//改动
										String relationKey = momentUserId+JedisConstant.RELATION_TO_USER_AND_RES+uidKey;
										return jedis.hgetAll(relationKey);
									}
								} );
								if(null!=map&&!map.isEmpty()){//当关系不为空时
									isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
									if(null!=isPraise&&!"".equals(isPraise)){
										value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
									}
									isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
									if(null!=isCollect&&!"".equals(isCollect)){
										value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
									}
									isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
									if(null!=isUseful&&!"".equals(isUseful)){
										value = value.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
									}
									/*zNum = map.get(JedisConstant.RELATION_PRAISE_NUM);
									if(null!=zNum&&!"".equals(zNum)){
										value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
									}
									cNum = map.get(JedisConstant.RELATION_COMMENT_NUM);
									if(null!=cNum&&!"".equals(cNum)){
										value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
									}*/
								}
								//添加资源的附加信息 比如说评论数，点赞数
								String ridStr = "0";
								//if(value.contains("\"rid\":\"")){
									ridStr = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
								//}
								final String rid = ridStr;
								if(!rid.equals("0")){
									Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
										//查询资源的附加信息
										@Override
										public Map<String, String> work(
												Jedis jedis) {
											String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
											return jedis.hgetAll(otherInfo);
										}
										
									});
									if(null!=otherMap&&!otherMap.isEmpty()){
										zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
										if(null!=zNum&&!"".equals(zNum)){
											value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
										}
										cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
										if(null!=cNum&&!"".equals(cNum)){
											value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
										}
									}
								}
								//添加是否已经加入书单
								String type = "0";
								if(value.contains("\"type\":\"")){
									type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+9);
								}
								if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
									String bookId = "";
									Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
									Matcher bookIdMatcher = bookIdPattern.matcher(value);
									if(bookIdMatcher.find()){
										bookId = bookIdMatcher.group(1);
									}
									if(!"".equals(bookId)){//当图书的id不为空时
										final String bkId = bookId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
									String movieId = "";
									Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
									Matcher movieIdMatcher = movieIdPattern.matcher(value);
									if(movieIdMatcher.find()){
										movieId = movieIdMatcher.group(1);
									}
									if(!"".equals(movieId)){//当电影的id不为空时
										final String mvId = movieId;
										inList = relationToUserandresClient.execute(new JedisWorker<String>(){
											@Override
											public String work(Jedis jedis) {
												String relationkey = momentUserId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
														return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
											}});
									}
								}
								if(null!=inList&&!"".equals(inList)){
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
								}else{
									value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
								}		
								resultLists.add(value);
							}else{
								//当没有这个资源时，删除广场的资源id
								momentsClient.execute(new JedisWorker<String>(){
									@Override
									public String work(Jedis jedis) {
										//String userMomentId = userMomentId;
										long userScore = Long.valueOf(uidKey);
										jedis.zremrangeByScore(userMomentId, -userScore, -userScore);
										return null;
									}
								} );
							}
						}
//						System.out.println(resultLists.toString());
						return resultLists;
					}
				});
//				 System.out.println(resList);
				return resList;
			}
		});
		return userMoments;
	}
	
	/**
	 * 遍历朋友圈，插入资源id
	 */
	@Override
	public ConsumeStatus recv(String topic, String tags, String key, String body) {
		// TODO Auto-generated method stub
		LOG.info("topic: " + topic + " tags:" + tags + " key:" + key + " body:" + body);
		System.out.println("topic: " + topic + " tags:" + tags + " key:" + key + " body:" + body);
		long userId = Long.valueOf(key);
		Long rid =0l;
		Long vid = 0l;
		try{
			JSONObject json = new JSONObject(body);
			rid = json.getLong("frid");
			vid = json.getLong("vid");
//			System.out.println("到加广场这里了");
			/*UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null, userId);
			int level = uInfo.getLevel();
			if(level==50){//神人
				saveOneShenRenMoment(rid);
			}else{//普通人
				saveOneCommonMoment(rid);
			}*/
			//saveOneItem(0,rid,vid);
			/*UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null, userId);
			if(null!=uInfo){//当用户不为空时
				int level = uInfo.getLevel();
				if(level==50){//神人
					saveOneShenRenMoment(rid);
					System.out.println("推送添加神人广场");
				}else{//普通人
					saveOneCommonMoment(rid);
				}
			}*/
			//发送朋友圈的个数新版为100000，稍后需要调整
			List<UserAttention> list = ucenterFacade.findAttentionUserList(null, userId, 2, 0, CommentUtils.PAGE_NEW_SIZE);
			//findUserFens(null, userId, 2, 0, CommentUtils.PAGE_SIZE);
//			System.out.println(list);
			Iterator<UserAttention> itList = list.iterator();
			UserAttention userInfo = new UserAttention();
			long uid = 0;
			while(itList.hasNext()){
				userInfo = itList.next();
				uid = userInfo.getUserId();
				if(getUserMomentIsExist(uid)){
					saveOneItem(uid,rid,vid);
					//添加朋友圈的提醒通知
					userJedisManager.incrOneUserInfo(uid, JedisConstant.USER_HASH_MOMENT_NOTICE);
				}
				//getOneUserMoment(uid,rid);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//getOneUserMoment(0,rid);
		return MessageRecv.ConsumeStatus.CONSUME_SUCCESS;
	} 
}
