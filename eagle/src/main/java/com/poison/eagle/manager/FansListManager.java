package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;

/**
 * 点击书籍电影manager
 * @author Administrator
 * 
 */
public class FansListManager extends BaseManager {
	private static final Log LOG = LogFactory
			.getLog(FansListManager.class);
//	private Map<String, Object> datas ;
//	private Map<String, Object> req ;
//	private Map<String, Object> dataq;
//	private String resString;//返回数据
//	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//	private String error;
//	private List<Map> ulist;
	
	private int flagint;
	private UcenterFacade ucenterFacade;
	private FileUtils fileUtils = FileUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	private UserJedisManager userJedisManager;
	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	/**
	 * 粉丝列表
	 * @return
	 */
	public String fansList(String reqs,Long uid) {
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> datas =null;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		Long id = null;
		Long lastId = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		if(id.longValue()==uid.longValue()){
			//清楚粉丝的消息提醒数
			userJedisManager.saveOneUserInfo(uid, JedisConstant.USER_HASH_FANS_NOTICE, "0");
			userJedisManager.saveOneUserInfo(uid, JedisConstant.USER_HASH_FANS_TIME, System.currentTimeMillis()+"");
		}
		try{
			lastId = Long.valueOf(dataq.get("lastId").toString());
			if(0==lastId.longValue()){
				lastId = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			lastId = null;
		}
		
		
		//调用粉丝列表
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		//System.out.println("查询缓存中的关注人列表");
		//long startlong = System.currentTimeMillis();
		//System.out.println("查询关注人的id为"+id);
		long resListLength = userJedisManager.getUserFansLength(id);
		System.out.println("=======关注人的id为"+id+"=====关注人的粉丝列表的缓存长度为"+resListLength+"=======传过来的最后的lastid为"+lastId);
		//resList = userJedisManager.getOneUserFANSList(id, lastId, uid);
		/*long endlong = System.currentTimeMillis();
		System.out.println("调用关注人用时"+(endlong -startlong) );*/
		
		if(resListLength<20){//如果缓存中没有这条数据时
		List<UserInfo> list = getFansList(id);
		resList = getResponseList(list, CommentUtils.REQ_ISON_TRUE,uid, resList);
		System.out.println("缓存中没有这条数据时刷数据库");
		}
		resList = userJedisManager.getOneUserFANSList(id, lastId, uid);
		System.out.println("=====查出来的数据长度为"+resList.size());
		//long finaloong = System.currentTimeMillis();
		//System.out.println("调用数据库拼装数据所用时间"+(finaloong-endlong));
		
		//拼接数据
		datas = new HashMap<String, Object>();
		//if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("ulist", resList);
		/*}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}*/
		datas.put("flag", flag);
		
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		
		return resString;
	}
	/**
	 * TODO 获取粉丝列表
	 * @param uid
	 * @return
	 */
	public List<UserInfo> getFansList(Long uid){
		//调用粉丝列表
		List<UserInfo> list = ucenterFacade.findUserFens(null, uid, 2, 0, CommentUtils.PAGE_SIZE);
		
		//调用互相关注人列表
		List<UserInfo> lists = ucenterFacade.findUserAttentionEachList(null, uid);
		if(lists != null && lists.size()>0){
			list.addAll(lists);
		}
		Collections.sort(list);
		
		Iterator<UserInfo> it = list.iterator();
		UserInfo userInfo = new UserInfo();
		while(it.hasNext()){
			userInfo = it.next();
			if(null==userInfo||userInfo.getUserId()==0){
				continue;
			}
			//插入用户的粉丝列表
			userJedisManager.saveOneUserFans(userInfo.getUserId(), userInfo.getAttentionDate(),uid);
		}
		
		return list;
	}
	/**
	 * 关注人列表
	 * @return
	 */
	public String plusList(String reqs,Long uid) {
		Map<String, Object> datas =null;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		Long id = null;
		Long lastId = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			lastId = Long.valueOf(dataq.get("lastId").toString());
			if(0==lastId.longValue()){
				lastId = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			lastId = null;
		}
		
		//调用关注人列表
		List<UserInfo> list = new ArrayList<UserInfo>();
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		//关注人列表从缓存中去取 如果没有直接调用数据库返回数据
		System.out.println("查询缓存中的关注人列表");
		long startlong = System.currentTimeMillis();
		//System.out.println("查询关注人的id为"+id);
		long resListLength = userJedisManager.getUserAttentionLength(id);
		//resList = userJedisManager.getOneUserAttentionList(id, lastId,uid);
		//long endlong = System.currentTimeMillis();
		//System.out.println("调用关注人用时"+(endlong -startlong) );
		if(resListLength<20){//当缓存中没有时从数据库中查询
			//System.out.println("从数据库中查询关注人列表");
			list = getPlusUsers(id);
			/*long startTime = System.currentTimeMillis();
			long endTime = System.currentTimeMillis();
			System.out.println("调用关注人列表耗时"+(endTime-startTime));
			long startResTime = System.currentTimeMillis();*/
			//修改的关注人列表------------------------------------------
			resList = getResponseList(list, CommentUtils.REQ_ISON_TRUE,uid, resList);
			//long endResTime = System.currentTimeMillis();
			//System.out.println("分组关注人列表耗时"+(endResTime-startResTime));
		}
		resList = userJedisManager.getOneUserAttentionList(id, lastId,uid);
		long finaloong = System.currentTimeMillis();
		System.out.println("调用数据库拼装数据所用时间"+(finaloong-startlong)+"发过来的最后id为"+lastId);
		//拼接数据
		datas = new HashMap<String, Object>();
		//if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("ulist", resList);
		/*}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}*/
		datas.put("flag", flag);
		
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		
		return resString;
	}
	/**
	 * 获取关注人列表
	 * @param id
	 * @return
	 */
	public List<UserInfo> getPlusUsers(Long id){
		//调用关注人列表
		List<UserInfo> list = ucenterFacade.findUserAttention(null, id, 0, 0, CommentUtils.PAGE_SIZE);
		
		
//				resList = getResponseList(list, CommentUtils.REQ_ISON_FALSE,uid, resList);
		//调用互相关注人列表
		List<UserInfo> lists = ucenterFacade.findUserAttentionEachList(null, id);
		if(lists != null && lists.size()>0){
			list.addAll(lists);
		}
		Collections.sort(list);
		Iterator<UserInfo> it = list.iterator();
		UserInfo userInfo = new UserInfo();
		while(it.hasNext()){
			userInfo = it.next();
			if(null==userInfo||userInfo.getUserId()==0){
				continue;
			}
			//插入用户的关注人列表
			userJedisManager.saveOneUserAttention(id, userInfo.getAttentionDate(), userInfo.getUserId());
		}
		return list;
	}
	/**
	 * 朋友关注人列表
	 * @return
	 */
	public String findFriends(String reqs,Long uid) {
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//调用client方法
		List<UserInfo> list = ucenterFacade.findFriendsUnAttentionList(null, uid, CommentUtils.PAGE_SIZE, 0, CommentUtils.PAGE_SIZE);
		
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		resList = getResponseList(list, CommentUtils.REQ_ISON_FALSE,uid, resList);
		
		list = ucenterFacade.findFriendsAttentionEachList(null, uid, CommentUtils.PAGE_SIZE, 0, CommentUtils.PAGE_SIZE);
		resList = getResponseList(list, CommentUtils.REQ_ISON_TRUE,uid, resList);
		
		
		//拼接数据
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("ulist", resList);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		
		return resString;
	}
	
	/**
	 * 将返回的电话号码分组处理
	 * @param list
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getResponseList(List<UserInfo> reqList , String type ,Long uid, List<Map<String, Object>> resList){
		Map<String, Object> map = null;
		UserEntity userEntity = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			Map<String, String> userEntityMap = null;
			//Map<String, String> userAttentionRelationMap = null;
			for (UserInfo userInfo : reqList) {
				//未注册的话用指定ID
				map = new HashMap<String, Object>();
				userEntityMap = new HashMap<String, String>();
				if(userInfo.getUserId()==0){
					continue;
				}
				//缓存中插入用户关注人列表
				//userJedisManager.saveOneUserAttention(uid, userInfo.getAttentionDate(), userInfo.getUserId());
				//userEntityMap = userJedisManager.getUserInfo(userInfo.getUserId());
				//System.out.println("缓存中的用户信息威"+userEntityMap);
				//if(null==userEntityMap||userEntityMap.isEmpty()||null==userEntityMap.get(JedisConstant.USER_HASH_ID)){//当用户的基本信息为空时，查找数据库
				UserAllInfo userAllInfo  = ucenterFacade.findUserInfo(null, userInfo.getUserId());
				userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);
					//缓存中插入用户的基本信息
					/*userJedisManager.saveOneUserInfo(userEntity.getId(), JedisConstant.USER_HASH_ID, CheckParams.objectToStr(userAllInfo.getUserId()+""));
					userJedisManager.saveOneUserInfo(userEntity.getId(),JedisConstant.USER_HASH_NAME,CheckParams.objectToStr(userAllInfo.getName()));
					userJedisManager.saveOneUserInfo(userEntity.getId(), JedisConstant.USER_HASH_FACE, CheckParams.objectToStr(userAllInfo.getFaceAddress()));
					userJedisManager.saveOneUserInfo(userEntity.getId(), JedisConstant.USER_HASH_SIGN, CheckParams.objectToStr(userAllInfo.getSign()));
					userJedisManager.saveOneUserInfo(userEntity.getId(), JedisConstant.USER_HASH_TYPE, CheckParams.objectToStr(userAllInfo.getLevel()+""));
					userJedisManager.saveOneUserInfo(userEntity.getId(), JedisConstant.USER_HASH_SEX, CheckParams.objectToStr(userAllInfo.getSex()));
				}else{
					userEntity = new UserEntity();
					userEntity.setId(Long.valueOf(userEntityMap.get(JedisConstant.USER_HASH_ID)));
					userEntity.setNickName(userEntityMap.get(JedisConstant.USER_HASH_NAME));
					userEntity.setFace_url(userEntityMap.get(JedisConstant.USER_HASH_FACE));
					userEntity.setSign(userEntityMap.get(JedisConstant.USER_HASH_SIGN));
					userEntity.setSex(userEntityMap.get(JedisConstant.USER_HASH_SEX));
					userEntity.setFansNum(0);
					userEntity.setPlusNum(0);
					userEntity.setBig(0);
					userEntity.setLevel(0);
					userEntity.setNextBig(0);
					//userEntity.setIsInterest();
					userEntity.setType(Integer.valueOf(userEntityMap.get(JedisConstant.USER_HASH_TYPE)));
					userEntity.setSort(0l);
				}*/
				
				//从缓存中获取用户对用户的关系信息不为空
			/*	String isInterest = "";
				if(null!=userAttentionRelationMap&&userAttentionRelationMap.isEmpty()){
					isInterest = userAttentionRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST);
					if(null==isInterest){
						//缓存中插入用户跟当前用户的关注关系*/						
				userUtils.putIsInterestToUserEntity(userEntity,uid, ucenterFacade,userJedisManager);
				//System.out.println("最后的排序后的用户信息为"+userEntity);
				/*		userJedisManager.saveRelationUserAttentionInfo(uid, userInfo.getUserId(), JedisConstant.RELATION_USER_ISINTEREST,userEntity.getIsInterest()+"");
					}
					userEntity.setIsInterest(Integer.valueOf(isInterest));
				}*/
				map.put("userEntity", userEntity);
				//map.put("type", type);
				resList.add(map);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			UserInfo ui = reqList.get(0);
			flagint = ui.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				long id = ui.getUserId();
				if(id != UNID){
					//未注册的话用指定ID
					map = new HashMap<String, Object>();
					UserAllInfo userAllInfo  = ucenterFacade.findUserInfo(null, ui.getUserId());
					userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);
					userUtils.putIsInterestToUserEntity(userEntity,uid, ucenterFacade,userJedisManager);
					map.put("userEntity", userEntity);
					//map.put("type", type);
					resList.add(map);
				}
			}/*else{
				map = new HashMap<String, Object>();
				map.put("userEntity", userEntity);
				resList.add(map);
			}*/
		}
		return resList;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
}
