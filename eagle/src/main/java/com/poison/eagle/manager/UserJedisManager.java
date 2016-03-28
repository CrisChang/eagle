package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;

public class UserJedisManager {
	
	private JedisSimpleClient userInfoHashClient;
	private UcenterFacade ucenterFacade;

	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setUserInfoHashClient(JedisSimpleClient userInfoHashClient) {
		this.userInfoHashClient = userInfoHashClient;
	}
	
	/**
	 * 
	 * <p>Title: saveOneUserInfo</p> 
	 * <p>Description: 增加一条用户信息</p> 
	 * @author :changjiang
	 * date 2015-3-6 下午5:09:35
	 * @param userId
	 * @param key
	 * @param value
	 * @return
	 */
	public String saveOneUserInfo(final long userId,final String key,final String value){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId +JedisConstant.USER_HASH_INFO;
				jedis.hset(relationkey, key, value);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	
	/**
	 * 
	 * <p>Title: saveOneUserInfo</p> 
	 * <p>Description: 增加一条用户资源数量信息</p> 
	 * @author :changjiang
	 * date 2015-3-6 下午5:09:35
	 * @param userId
	 * @param key
	 * @param value
	 * @return
	 */
	public String saveOneUserResourceCount(final long userId,final String key,final String value){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId +JedisConstant.USER_RESOURCE_COUNT;
				jedis.hset(relationkey, key, value);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: getUserInfo</p> 
	 * <p>Description: 得到用户资源的数量信息</p> 
	 * @author :changjiang
	 * date 2015-3-6 下午5:12:29
	 * @param userId
	 * @return
	 */
	public Map<String, String> getUserResourceCount(final long userId){
		Map<String, String> userAndResInfo = userInfoHashClient.execute(new JedisWorker<Map<String, String>>(){
			@Override
			public Map<String, String> work(Jedis jedis) {
				String relationkey = userId +JedisConstant.USER_RESOURCE_COUNT;
				return jedis.hgetAll(relationkey);
			}
		});
		return userAndResInfo;
	}
	
	/**
	 * 
	 * <p>Title: incrOneUserInfo</p> 
	 * <p>Description: 增加一条用户的信息</p> 
	 * @author :changjiang
	 * date 2015-3-17 下午4:19:36
	 * @param userId
	 * @param key
	 * @return
	 */
	public String incrOneUserInfo(final long userId,final String key){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId +JedisConstant.USER_HASH_INFO;
				//jedis.hset(relationkey, key, value);
				jedis.hincrBy(relationkey, key, 1);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: subtractOneUserInfo</p> 
	 * <p>Description: 相关信息减一</p> 
	 * @author :changjiang
	 * date 2015-6-25 下午6:25:04
	 * @param userId
	 * @param key
	 * @return
	 */
	public String subtractOneUserInfo(final long userId,final String key){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String relationkey = userId +JedisConstant.USER_HASH_INFO;
				//jedis.hset(relationkey, key, value);
				jedis.hincrBy(relationkey, key, -1);
				return jedis.hget(relationkey, key);
			}
		});
		return relationValue;
	}
	
	
	/**
	 * 
	 * <p>Title: getUserInfo</p> 
	 * <p>Description: 得到用户的基本信息</p> 
	 * @author :changjiang
	 * date 2015-3-6 下午5:12:29
	 * @param userId
	 * @return
	 */
	public Map<String, String> getUserInfo(final long userId){
		Map<String, String> userAndResInfo = userInfoHashClient.execute(new JedisWorker<Map<String, String>>(){
			@Override
			public Map<String, String> work(Jedis jedis) {
				String relationkey = userId +JedisConstant.USER_HASH_INFO;
				return jedis.hgetAll(relationkey);
			}
		});
		return userAndResInfo;
	}
	
	/**
	 * 
	 * <p>Title: saveOneUserAttention</p> 
	 * <p>Description: 增加一条关注信息</p> 
	 * @author :changjiang
	 * date 2015-3-23 上午11:06:05
	 * @param userId
	 * @return
	 */
	public String saveOneUserAttention(final long userId,final long attentionDate,final long attentionId){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String attentionkey = userId +JedisConstant.USER_ATTENTION_LIST;
				//增加用户最新关注的一个人的userid
				jedis.zadd(attentionkey, -attentionDate,attentionId+"");
				//获取关注人列表的长度
				long end = jedis.zcard(attentionkey);
				if(end>1000){//列表维护在1000条
					jedis.zremrangeByRank(attentionkey, end-1, end-1);
				}
				//返回插入的这个用户id
				Set<String> set = jedis.zrange(attentionkey, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: getUserAttentionLength</p> 
	 * <p>Description: 得到用户关注的列表长度</p> 
	 * @author :changjiang
	 * date 2015-3-27 下午3:04:48
	 * @param userId
	 * @return
	 */
	public long getUserAttentionLength(final long userId){
		long userAttentionLength = userInfoHashClient.execute(new JedisWorker<Long>(){
			@Override
			public Long work(Jedis jedis) {
				String attentionkey = userId +JedisConstant.USER_ATTENTION_LIST;
				return jedis.zcard(attentionkey);
			}
		});
		return userAttentionLength;
	}
	
	/**
	 * 
	 * <p>Title: saveOneUserFans</p> 
	 * <p>Description: 增加粉丝信息</p> 
	 * @author :changjiang
	 * date 2015-3-25 上午11:16:33
	 * @param userId
	 * @param attentionDate
	 * @return
	 */
	public String saveOneUserFans(final long userId,final long attentionDate,final long fansId){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String fanskey = fansId +JedisConstant.USER_FANS_LIST;
				//增加用户最新关注的一个人的userid
				jedis.zadd(fanskey, -attentionDate,userId+"");
				//获取关注人列表的长度
				long end = jedis.zcard(fanskey);
				if(end>1000){//列表维护在1000条
					jedis.zremrangeByRank(fanskey, end-1, end-1);
				}
				//返回插入的这个用户id
				Set<String> set = jedis.zrange(fanskey, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return relationValue;
	}
	
	/**
	 * 
	 * <p>Title: getUserFansLength</p> 
	 * <p>Description: 得到用户粉丝长度</p> 
	 * @author :changjiang
	 * date 2015-3-27 下午3:10:42
	 * @param userId
	 * @return
	 */
	public long getUserFansLength(final long userId){
		long userFansLength = userInfoHashClient.execute(new JedisWorker<Long>(){
			@Override
			public Long work(Jedis jedis) {
				String fanskey = userId +JedisConstant.USER_FANS_LIST;
				return jedis.zcard(fanskey);
			}
		});
		return userFansLength;
	}
	
	//删除一个关注信息
	public String delOneUserAttention(final long userId,final long attentionId){
		
		userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String attentionkey = userId +JedisConstant.USER_ATTENTION_LIST;
				jedis.zrem(attentionkey, attentionId+"");
				return null;
			}
			
		});
		return null;
	}
	
	/**
	 * 
	 * <p>Title: delOneUserFans</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-3-25 上午11:38:21
	 * @param userId 当前用户
	 * @param attentionId	关注人用户
	 * @return
	 */
	public String delOneUserFans(final long userId,final long attentionId){
		userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String fansListkey = attentionId +JedisConstant.USER_FANS_LIST;
				jedis.zrem(fansListkey, userId+"");
				return null;
			}
		});
		return null;
	}
	
	/**
	 * 
	 * <p>Title: saveRelationAttentionInfo</p> 
	 * <p>Description: 存储用户的关系信息</p> 
	 * @author :changjiang
	 * date 2015-3-23 下午3:58:42
	 * @param userId
	 * @param attentionId
	 * @return
	 */
	public String saveRelationUserAttentionInfo(final long userId,final long attentionId,final String field,final String value){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String attentionkey = userId +JedisConstant.RELATION_USER_ATTENTION+attentionId;
				//增加用户和用户的关系
				jedis.hset(attentionkey, field, value);
				return jedis.hget(attentionkey, field);
			}
		});
		return relationValue;
	}


	/**
	 * 得到用户的关注关系
	 * @param userId
	 * @param attentionId
	 * @param field
	 * @return
	 */
	public String getRelationUserAttentionInfo(final long userId,final long attentionId,final String field){
		//map.get("");
		String relationValue = "";
		relationValue = userInfoHashClient.execute(new JedisWorker<String>(){
			@Override
			public String work(Jedis jedis) {
				String attentionkey = userId +JedisConstant.RELATION_USER_ATTENTION+attentionId;
				//增加用户和用户的关系
				//jedis.hset(attentionkey, field, value);
				return jedis.hget(attentionkey, field);
			}
		});
		return relationValue;
	}
	
	
	/**
	 * 
	 * <p>Title: getRelationUserAttentionInfo</p> 
	 * <p>Description: 得到用户关注的信息列表</p> 
	 * @author :changjiang
	 * date 2015-3-23 下午5:07:37
	 * @param userId
	 * @param attentionId
	 * @return
	 */
	public Map<String, String> getRelationUserAttentionInfo(final long userId,final long attentionId){
		//map.get("");
		Map<String, String> relationUserAttentionInfoMap = userInfoHashClient.execute(new JedisWorker<Map<String, String>>(){
			@Override
			public Map<String, String> work(Jedis jedis) {
				String attentionkey = userId +JedisConstant.RELATION_USER_ATTENTION+attentionId;
				//测试清数据，正式需要删掉
				//jedis.del(attentionkey);
				return jedis.hgetAll(attentionkey);
			}
		});
		return relationUserAttentionInfoMap;
	}
	
	/**
	 * 
	 * <p>Title: getOneUserAttentionList</p> 
	 * <p>Description: 得到用户的关注列表</p> 
	 * @author :changjiang
	 * date 2015-3-23 下午12:10:55
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getOneUserAttentionList(final long userId,final Long lastUserId,final long muid){
		 final List<Map<String, Object>> userAttentionList = userInfoHashClient.execute(new JedisWorker<List<Map<String, Object>>>(){

			@Override
			public List<Map<String, Object>> work(Jedis jedis) {
				String userAttentionListId = userId+JedisConstant.USER_ATTENTION_LIST;
				//用户关注列表长度
				long end = jedis.zcard(userAttentionListId);
				//一页显示二十条关注人信息
				int size = 20;
				//最终返回的列表
				 List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
				 Set<String> tempSet = new HashSet<String>();
				 if(end==0){//当没有关注列表的时候 直接返回
					 return resList;
				 }
				 
				 if(null==lastUserId){//当传值为空时，查询30条信息
					 if(end>size){//当缓存中得数量超过分页的数量时
						 tempSet = jedis.zrange(userAttentionListId,0, size);
					}else{//当缓存数量小于分页数量时
						tempSet = jedis.zrange(userAttentionListId,0, end-1);
					}
				 }else if(-1==lastUserId.longValue()){//取出全部
					 tempSet = jedis.zrange(userAttentionListId, 0, end-1);
				 }else{
					 //查找最后一条用户所在的位置
					 long index = jedis.zrank(userAttentionListId, lastUserId+"");
					//得到分页的set列表
					 long endIndex = index+size;
					 if(endIndex>end){//当分页大于长度的时候
						tempSet = jedis.zrange(userAttentionListId,index+1, end);
					}else {//当分页小于最后的长度的时候
						tempSet = jedis.zrange(userAttentionListId,index+1, endIndex);
					}
				 }
				 
				 //复制关注人的列表
				 final Set<String> set = new LinkedHashSet<String>(tempSet);
				 
				 //根据关注人列表查询用户的关注人详情
				 resList = userInfoHashClient.execute(new JedisWorker<List<Map<String, Object>>>(){
					@Override
					public List<Map<String, Object>> work(Jedis jedis) {
						List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
						Map<String, Object> resMap = null;
						for(String uidStr:set){//循环遍历关注人的id
							final String uidKey = uidStr;
							if("0".equals(uidKey)||null==uidKey||"".equals(uidKey)){//当关注人id为0或者为空时
								continue;
							}
							Map<String, String> userMap = userInfoHashClient.execute(new JedisWorker<Map<String, String>>(){
								@Override
								public Map<String, String> work(Jedis jedis) {
									String userInfoKey = uidKey + JedisConstant.USER_HASH_INFO;
									Map<String, String> userTempMap = jedis.hgetAll(userInfoKey);
									if(null==userTempMap||userTempMap.isEmpty()||null==userTempMap.get(JedisConstant.USER_HASH_ID)||null==userTempMap.get(JedisConstant.USER_HASH_TYPE)||"0".equals((String)userTempMap.get(JedisConstant.USER_HASH_TYPE))){
										//从数据库中获取用户信息并加入缓存
										UserAllInfo userAllInfo  = ucenterFacade.findUserInfo(null, Long.valueOf(uidKey));
										if(userAllInfo.getFlag()==ResultUtils.SUCCESS){//当查询信息
											jedis.hset(userInfoKey, JedisConstant.USER_HASH_ID, CheckParams.objectToStr(userAllInfo.getUserId()+""));
											jedis.hset(userInfoKey, JedisConstant.USER_HASH_NAME, CheckParams.objectToStr(userAllInfo.getName()));
											jedis.hset(userInfoKey, JedisConstant.USER_HASH_FACE, CheckParams.objectToStr(userAllInfo.getFaceAddress()));
											jedis.hset(userInfoKey, JedisConstant.USER_HASH_SIGN, CheckParams.objectToStr(userAllInfo.getSign()));
											jedis.hset(userInfoKey, JedisConstant.USER_HASH_TYPE, CheckParams.objectToStr(userAllInfo.getLevel()+""));
											jedis.hset(userInfoKey, JedisConstant.USER_HASH_SEX, CheckParams.objectToStr(userAllInfo.getSex()));
										}
									}
									return jedis.hgetAll(userInfoKey);
								}
							});
							
							//获取关注信息
							Map<String,String> relationUserAttentionInfoMap = userInfoHashClient.execute(new JedisWorker<Map<String, String>>(){
								@Override
								public Map<String, String> work(Jedis jedis) {
									String userAttentionInfoKey = muid + JedisConstant.RELATION_USER_ATTENTION + uidKey;
									Map<String, String> userAttentionMap = jedis.hgetAll(userAttentionInfoKey);
									if(null==userAttentionMap||userAttentionMap.isEmpty()){
										//查询相互关注的状态并放入缓存
										UserAttention  userAttention  = ucenterFacade.findUserAttentionIsExist(muid,Long.valueOf(uidKey));
										Long attentionId = userAttention.getAttentionId();
										UserAttention  uAttention  = ucenterFacade.findUserAttentionIsExist(Long.valueOf(uidKey),muid);
										Long uattentionId = uAttention.getAttentionId();
										int isAttention = userAttention.getIsAttention();
										int uisAttention = uAttention.getIsAttention();
										String attentionkey = uidKey +JedisConstant.RELATION_USER_ATTENTION+muid;
										if(0!=attentionId.longValue()){//当我关注了这个人时
											if(1==isAttention){//我已经关注了这个人
												if(0!=uattentionId.longValue()){//如果这个人也关注了我
													if(1==uisAttention){//如果这个人也关注了我
														//userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "0");
														//增加用户和用户的关系
														jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "0");
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "0");
													}else{//如果我关注了这个人，这个人没有关注我
														jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "1");
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "2");
													}
												}
											}else{//当我没有关注这个人时
												jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "2");
												if(0!=uattentionId.longValue()){//如果这个人也关注了我
													if(1==uisAttention){//如果这个人也关注了我
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "1");
													}
												}
											}
										}else{//当我没有关注这个人时
												jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "2");
												if(0!=uattentionId.longValue()){//如果这个人也关注了我
													if(1==uisAttention){//如果这个人也关注了我
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "1");
													}
												}
										}
									}
									return jedis.hgetAll(userAttentionInfoKey);
								}
							});
							//插入用户的缓存信息
							if(null!=userMap&&!userMap.isEmpty()){
								if(null!=relationUserAttentionInfoMap&&!relationUserAttentionInfoMap.isEmpty()){
									userMap.put("isInterest", relationUserAttentionInfoMap.get(JedisConstant.RELATION_USER_ISINTEREST));
									resMap = new HashMap<String, Object>();
									resMap.put("userEntity", userMap);
									resultList.add(resMap);
								}
							}
						}
						//测试用，正式要注掉
						//jedis.del(userId+JedisConstant.USER_ATTENTION_LIST);
						return resultList;
					}
				 });
				return resList;
			}
			
		});
		return userAttentionList;
	}
	
	/**
	 * 
	 * <p>Title: getOneUserFANSList</p> 
	 * <p>Description: 得到用户的粉丝列表</p> 
	 * @author :changjiang
	 * date 2015-3-25 上午11:28:22
	 * @param userId
	 * @param lastUserId
	 * @param muid
	 * @return
	 */
	public List<Map<String, Object>> getOneUserFANSList(final long userId,final Long lastUserId,final long muid){
		 final List<Map<String, Object>> userFansList = userInfoHashClient.execute(new JedisWorker<List<Map<String, Object>>>(){

			@Override
			public List<Map<String, Object>> work(Jedis jedis) {
				String userFansListId = userId+JedisConstant.USER_FANS_LIST;
				//用户关注列表长度
				long end = jedis.zcard(userFansListId);
				//一页显示二十条关注人信息
				int size = 20;
				//最终返回的列表
				 List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
				 Set<String> tempSet = new HashSet<String>();
				 if(end==0){//当没有关注列表的时候 直接返回
					 return resList;
				 }
				 
				 if(null==lastUserId){//当传值为空时，查询30条信息
					 if(end>size){//当缓存中得数量超过分页的数量时
						 tempSet = jedis.zrange(userFansListId,0, size);
					}else{//当缓存数量小于分页数量时
						tempSet = jedis.zrange(userFansListId,0, end-1);
					}
				 }else{
					 //查找最后一条用户所在的位置
					 long index = jedis.zrank(userFansListId, lastUserId+"");
					//得到分页的set列表
					 long endIndex = index+size;
					 if(endIndex>end){//当分页大于长度的时候
						tempSet = jedis.zrange(userFansListId,index+1, end);
					}else {//当分页小于最后的长度的时候
						tempSet = jedis.zrange(userFansListId,index+1, endIndex);
					}
				 }
				 
				 //复制粉丝的列表
				 final Set<String> set = new LinkedHashSet<String>(tempSet);
				 //System.out.println("============第一次查询粉丝的个数为"+set.size());
				 //根据关注人列表查询用户的关注人详情
				 resList = userInfoHashClient.execute(new JedisWorker<List<Map<String, Object>>>(){
					@Override
					public List<Map<String, Object>> work(Jedis jedis) {
						List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
						for(String uidStr:set){//循环遍历关注人的id
							Map<String, Object> resMap = new HashMap<String, Object>();
							final String uidKey = uidStr;
							if("0".equals(uidKey)||null==uidKey||"".equals(uidKey)){//当关注人id为0或者为空时
								continue;
							}
							Map<String, String> userMap = userInfoHashClient.execute(new JedisWorker<Map<String, String>>(){
								@Override
								public Map<String, String> work(Jedis jedis) {
									String userInfoKey = uidKey + JedisConstant.USER_HASH_INFO;
									Map<String, String> userTempMap = jedis.hgetAll(userInfoKey);
									//System.out.println("循环遍历缓存查出来的用户id为"+userTempMap.get(JedisConstant.USER_HASH_ID));
									if(null==userTempMap||userTempMap.isEmpty()||null==userTempMap.get(JedisConstant.USER_HASH_ID)||null==userTempMap.get(JedisConstant.USER_HASH_TYPE)||"0".equals((String)userTempMap.get(JedisConstant.USER_HASH_TYPE))){
										//从数据库中获取用户信息并加入缓存
										UserAllInfo userAllInfo  = ucenterFacade.findUserInfo(null, Long.valueOf(uidKey));
										jedis.hset(userInfoKey, JedisConstant.USER_HASH_ID, CheckParams.objectToStr(userAllInfo.getUserId()+""));
										jedis.hset(userInfoKey, JedisConstant.USER_HASH_NAME, CheckParams.objectToStr(userAllInfo.getName()));
										jedis.hset(userInfoKey, JedisConstant.USER_HASH_FACE, CheckParams.objectToStr(userAllInfo.getFaceAddress()));
										jedis.hset(userInfoKey, JedisConstant.USER_HASH_SIGN, CheckParams.objectToStr(userAllInfo.getSign()));
										jedis.hset(userInfoKey, JedisConstant.USER_HASH_TYPE, CheckParams.objectToStr(userAllInfo.getLevel()+""));
										jedis.hset(userInfoKey, JedisConstant.USER_HASH_SEX, CheckParams.objectToStr(userAllInfo.getSex()));
									}
									return jedis.hgetAll(userInfoKey);
								}
							});
							
							//获取关注信息
							Map<String,String> relationUserAttentionInfoMap = userInfoHashClient.execute(new JedisWorker<Map<String, String>>(){
								@Override
								public Map<String, String> work(Jedis jedis) {
									String userAttentionInfoKey = muid + JedisConstant.RELATION_USER_ATTENTION + uidKey;
									Map<String, String> userAttentionMap = jedis.hgetAll(userAttentionInfoKey);
									if(null==userAttentionMap||userAttentionMap.isEmpty()){
										//查询相互关注的状态并放入缓存
										UserAttention  userAttention  = ucenterFacade.findUserAttentionIsExist(muid,Long.valueOf(uidKey));
										Long attentionId = userAttention.getAttentionId();
										UserAttention  uAttention  = ucenterFacade.findUserAttentionIsExist(Long.valueOf(uidKey),muid);
										Long uattentionId = uAttention.getAttentionId();
										int isAttention = userAttention.getIsAttention();
										int uisAttention = uAttention.getIsAttention();
										String attentionkey = uidKey +JedisConstant.RELATION_USER_ATTENTION+muid;
										if(0!=attentionId.longValue()){//当我关注了这个人时
											if(1==isAttention){//我已经关注了这个人
												if(0!=uattentionId.longValue()){//如果这个人也关注了我
													if(1==uisAttention){//如果这个人也关注了我
														//userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "0");
														//增加用户和用户的关系
														jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "0");
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "0");
													}else{//如果我关注了这个人，这个人没有关注我
														jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "1");
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "2");
													}
												}
											}else{//当我没有关注这个人时
												jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "2");
												if(0!=uattentionId.longValue()){//如果这个人也关注了我
													if(1==uisAttention){//如果这个人也关注了我
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "1");
													}
												}
											}
										}else{//当我没有关注这个人时
												jedis.hset(userAttentionInfoKey, JedisConstant.RELATION_USER_ISINTEREST, "2");
												if(0!=uattentionId.longValue()){//如果这个人也关注了我
													if(1==uisAttention){//如果这个人也关注了我
														jedis.hset(attentionkey, JedisConstant.RELATION_USER_ISINTEREST, "1");
													}
												}
										}
									}
									return jedis.hgetAll(userAttentionInfoKey);
								}
							});
							
							//System.out.println("用户对");
							//插入用户的缓存信息
							if(null!=userMap&&!userMap.isEmpty()){
								if(null!=relationUserAttentionInfoMap&&!relationUserAttentionInfoMap.isEmpty()){
									userMap.put("isInterest", relationUserAttentionInfoMap.get(JedisConstant.RELATION_USER_ISINTEREST));
									resMap.put("userEntity", userMap);
									resultList.add(resMap);
								}
							}
						}
						//System.out.println("缓存查出来的用户粉丝列表长度为"+resultList.size());
						//测试用，正式要注掉
						//jedis.del(userId+JedisConstant.USER_ATTENTION_LIST);
						return resultList;
					}
					 
				 });
				return resList;
			}
			
		});
		return userFansList;
	}

}
