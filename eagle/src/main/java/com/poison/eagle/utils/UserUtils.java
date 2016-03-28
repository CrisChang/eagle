package com.poison.eagle.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserAlbumInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.UserJedisManager;
import com.poison.resource.client.BigFacade;
import com.poison.resource.model.BigLevelValue;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;

public class UserUtils {
	private static UserUtils userUtils;
	public UserUtils(){}
	public static UserUtils getInstance(){
		if(userUtils == null){
			return new UserUtils();
		}else{
			return userUtils;
		}
	}
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private UserJedisManager userJedisManager;
	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	/**
	 * 在用户类中放入手机用户是否关注该人
	 * @param userEntity
	 * @param ucenterFacade
	 */
	public void putIsInterestToUserEntity(UserEntity userEntity ,Long uid,UcenterFacade ucenterFacade,UserJedisManager userJedisManager){
		if(userEntity == null){
			return;
		}
		List<Long> longs = ucenterFacade.findUserAttentionList(uid, 0, CommentUtils.PAGE_SIZE);
		Map<String, String> userAttentionRelationMap = new HashMap<String, String>();
		//得到的关注信息错误
		userAttentionRelationMap = userJedisManager.getRelationUserAttentionInfo(uid, userEntity.getId());
		if(null==userAttentionRelationMap||userAttentionRelationMap.isEmpty()||null==userAttentionRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST)){//当这个缓存为空时
			for (Long l : longs) {
				if(l == userEntity.getId()){
					userEntity.setIsInterest(1);
					break;
				}
			}
			
			List<UserInfo> userInfos = ucenterFacade.findUserAttentionEachList(null, uid);
			
			for (UserInfo userInfo : userInfos) {
				Long id = userInfo.getUserId();
				if(id == userEntity.getId()){
					userEntity.setIsInterest(0);
					break;
				}
			}
			//将这条信息插入缓存
			userJedisManager.saveRelationUserAttentionInfo(uid, userEntity.getId(), JedisConstant.RELATION_USER_ISINTEREST, CheckParams.objectToStr(userEntity.getIsInterest()+""));
		}else{
			//System.out.println(userAttentionRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST));
			userEntity.setIsInterest(Integer.valueOf(userAttentionRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST)));
		}
		
		
//		userInfos = ucenterFacade.findUserFens(null, uid, 2, 0, CommentUtils.PAGE_SIZE);
//		for (UserInfo userInfo : userInfos) {
//			Long id = userInfo.getUserId();
//			if(id == userEntity.getId()){
//				userEntity.setIsInterest(2);
//				break;
//			}
//		}
		
	}
	
	/**
	 * 查询当前登陆用户与其他人直接的关注关系
	 * @param 
	 * @param ucenterFacade
	 */
	public int getIsInterest(Long uid,Long seconduid,UcenterFacade ucenterFacade,UserJedisManager userJedisManager){
		Map<String, String> userAttentionRelationMap = new HashMap<String, String>();
		//得到的关注信息错误
		userAttentionRelationMap = userJedisManager.getRelationUserAttentionInfo(uid, seconduid);
		if(null==userAttentionRelationMap||userAttentionRelationMap.isEmpty()||null==userAttentionRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST)){//当这个缓存为空时
			int isInterest = 2;
			UserAttention userAttention = ucenterFacade.findUserAttentionIsExist(uid, seconduid);
			if(userAttention!=null && userAttention.getIsAttention()==1){
				isInterest = 1;
				UserAttention userAttention2 = ucenterFacade.findUserAttentionIsExist(seconduid,uid);
				if(userAttention2!=null && userAttention2.getIsAttention()==1){
					isInterest = 0;
				}
			}
			//将这条信息插入缓存
			userJedisManager.saveRelationUserAttentionInfo(uid, seconduid, JedisConstant.RELATION_USER_ISINTEREST, CheckParams.objectToStr(isInterest+""));
			return isInterest;
		}else{
			//System.out.println(userAttentionRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST));
			return Integer.valueOf(userAttentionRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST));
		}
	}
	
	/**
	 * 将朋友的朋友的名字放入资源类中
	 * @param ri
	 * @param uid
	 * @param ucenterFacade
	 */
	public void putFriendNameInResourceInfo(ResourceInfo ri ,Long uid, UcenterFacade ucenterFacade){
//		System.out.println("资源类："+ri.toString());
		if(ri == null || uid == null || ucenterFacade == null || ri.getRid() == 0){
			return;
		}
		
		if(ri.getUserEntity() == null || ri.getUserEntity().getId() == 0){
			return;
		}
		long ruid = ri.getUserEntity().getId();
		
		//如果是当前用户发的信息则不添加该字段
		if(ruid == uid){
			return;
		}
		
		//如果朋友关注的朋友我已关注，则不添加字段
		List<UserInfo> userInfos = ucenterFacade.findFriendsIsAttentionList(null, uid, CommentUtils.PAGE_SIZE, 0, CommentUtils.PAGE_SIZE);
		for (UserInfo userInfo : userInfos) {
			if(userInfo.getUserId() == ruid){
				return;
			}
		}
		
		//调用关注人列表
		List<UserInfo> list = ucenterFacade.findUserAttention(null, uid, 0, 0, CommentUtils.PAGE_SIZE);
		//调用互相关注人列表
		list.addAll(ucenterFacade.findUserAttentionEachList(null, uid));
		for (UserInfo userInfo : list) {
			//朋友的朋友（朋友间相互关注的）
			List<UserInfo> userInfos2 = ucenterFacade.findUserAttentionEachList(null, userInfo.getUserId());
			for (UserInfo userInfo2 : userInfos2) {
				if(userInfo2.getUserId() == ruid){
					ri.setFriendName(userInfo.getName());
					break;
				}
			}
		}
		
		
//		List<UserInfo> list = ucenterFacade.findFriendsUnAttentionList(null, uid, CommentUtils.PAGE_SIZE, 0, CommentUtils.PAGE_SIZE);
//		for (UserInfo userInfo : list) {
//			if(userInfo.getUserId()== ruid){
//				ri.setFriendName(userInfo.getName());
//				break;
//			}
//		}
//		list = ucenterFacade.findFriendsAttentionEachList(null, uid, CommentUtils.PAGE_SIZE, 0, CommentUtils.PAGE_SIZE);
//		for (UserInfo userInfo : list) {
//			if(userInfo.getUserId()== ruid){
//				ri.setFriendName(userInfo.getName());
//				break;
//			}
//		}
	}
	
	/**
	 * 把粉丝数和关注数放到用户中
	 * @param obj
	 * @param ucenterFacade
	 * @return
	 */
	public UserEntity putFansPlusToUser(Object obj , UcenterFacade ucenterFacade){
		UserEntity userEntity = new UserEntity();
		
		if(obj == null){
			return userEntity;
		}
		
		userEntity = fileUtils.copyUserInfo(obj, 0);
		
		int fansNum = ucenterFacade.findUserFensCount(null, userEntity.getId());
		int plusNum = ucenterFacade.findUserAttentionCount(null, userEntity.getId());
		
		if(fansNum != 0){
			userEntity.setFansNum(fansNum);
		}
		if(plusNum != 0){
			userEntity.setPlusNum(plusNum);
		}
		
		return userEntity;
		
	}
	
	
	/**
	 * 将用户关注类转换为用户
	 * @param userAttention
	 * @param ucenterFacade
	 * @return
	 */
	public UserEntity putUserAttentionToEntity(UserAttention userAttention , UcenterFacade ucenterFacade){
		UserEntity userEntity = new UserEntity();
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userAttention.getUserAttentionId());
		userEntity = fileUtils.copyUserInfo(userAllInfo, 0);
		
		return userEntity;
	}
	/**
	 * 将逼格值放入user中
	 * @param userEntity
	 * @param ucenterFacade
	 * @param bigFacade
	 * @return
	 */
	public UserEntity putBigToUserEntity(UserEntity userEntity , UcenterFacade ucenterFacade,BigFacade bigFacade){
		
		UserBigValue userBigValue = ucenterFacade.findUserBigValueByUserId(userEntity.getId());
		
		if(userBigValue.getUserId() != 0){
			int level = userBigValue.getBigLevel();
			float big = userBigValue.getBigValue()+userBigValue.getSelfTest();
			userEntity.setBig((int)big);
			userEntity.setLevel(level);
			
			BigLevelValue bigLevelValue = bigFacade.getLevelValue(level+1);
			try {
				if(bigLevelValue.getId() !=0){
					userEntity.setNextBig((int)(Float.valueOf(bigLevelValue.getValue()) - big));
				}
			} catch (Exception e) {
				e.printStackTrace();
				userEntity.setNextBig(0);
			}
		}
		
		return userEntity;
	}
	
	/**
	 * 将用户相册的json转换为list
	 * @param json
	 */
	public List<Map<String, Object>> putUserAlbumJsonToList(String json){
		List<Map<String, Object>> imgList = new ArrayList<Map<String, Object>>();
		Map<String, Object> imgMap = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			imgMap = objectMapper.readValue(json, Map.class);
			Iterator<Map.Entry<String, Object>> iterator  =imgMap.entrySet().iterator();
			
			while(iterator.hasNext()){
				Map.Entry<String, Object> map = iterator.next();
				Map<String, Object> resMap = new HashMap<String, Object>();
				resMap.put("id", Long.valueOf(map.getKey()));
				resMap.put("url", map.getValue());
				imgList.add(resMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return imgList;
	}
	
	/**
	 * 将相册类格式化
	 * @param userAlbum
	 * @return
	 */
	public UserAlbumInfo putUserAlbumToInfo(UserAlbum userAlbum){
		UserAlbumInfo userAlbumInfo = new UserAlbumInfo();
		
		userAlbumInfo.setId(userAlbum.getId());
		userAlbumInfo.setTitle(userAlbum.getTitle());
		userAlbumInfo.setUid(userAlbum.getUserId());
		userAlbumInfo.setType(userAlbum.getType());
		userAlbumInfo.setUrlList(userAlbum.getContent());
		
		
		return userAlbumInfo;
	}
	
	
	/**
	 * 将用户相册json解析出来(已废弃)
	 * @param json
	 * @param id default null
	 * @param url default null
	 * @return
	 */
	public Map<String, String> getMapFromAlbumJsonByIdOrUrl(String json,Long id,String url){
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> urlMap = new HashMap<String, String>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			urlMap = objectMapper.readValue(json, new TypeReference<Map<String, String>>(){});
			
			Iterator<Map.Entry<String, String>> iter = urlMap.entrySet().iterator();
			
			while(iter.hasNext()){
				Map.Entry<String, String> entry = iter.next();
				String idStr = entry.getKey();
				String urlStr = entry.getValue();
				if(id != null){
					if(id.toString().equals(idStr)){
						map.put("url", urlStr);
					}
				}
				if(url != null){
					if(url.equals(urlStr)){
						map.put("id", idStr);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	/**
	 * 将新添加的相片提取出来
	 * @param json
	 * @param ids
	 * @param urls
	 * @return
	 */
	public List<Map<String, String>> getListFromAlbumJsonByIdOrUrl(String json,List<Long> ids,List<String> urls){
		Map<String, String> urlMap = new HashMap<String, String>();
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			urlMap = objectMapper.readValue(json, new TypeReference<Map<String, String>>(){});
			
			Iterator<Map.Entry<String, String>> iter = urlMap.entrySet().iterator();
			
			while(iter.hasNext()){
				Map<String, String> map = new HashMap<String, String>();
				Map.Entry<String, String> entry = iter.next();
				String idStr = entry.getKey();
				String urlStr = entry.getValue();
				if(ids != null){
					if(ids.contains(Long.valueOf(idStr))){
						map.put("id", idStr);
						map.put("url", urlStr);
					}
				}
				if(urls != null){
					if(urls.contains(urlStr)){
						map.put("id", idStr);
						map.put("url", urlStr);
						list.add(map);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	
	
}
