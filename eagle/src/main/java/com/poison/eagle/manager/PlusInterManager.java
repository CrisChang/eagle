package com.poison.eagle.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;

/**
 * 点击书籍电影manager
 * @author Administrator
 * 
 */
public class PlusInterManager extends BaseManager {
	private static final Log LOG = LogFactory
			.getLog(PlusInterManager.class);
//	private Map<String, Object> req ;
//	private Map<String, Object> dataq;
//	private Map<String, Object> datas ;
//	private String resString;//返回数据
//	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//	private String error;
//	private long uid;
//	private String type;//0:加关注、1：取消关注
	
	private int flagint;
	private UcenterFacade ucenterFacade;
	private PushManager pushManager;
	private UserJedisManager userJedisManager;

	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	/**
	 * 关注取消关注
	 * @return
	 */
	public String plusInter(String reqs,long muid) {

//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String type = "";
		long uid = 0;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = (String) dataq.get("type");
			uid = Long.valueOf(dataq.get("uid").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		LOG.info(req);
		
		//调用client方法
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		int level = userAllInfo.getLevel();

		UserAttention ua = new UserAttention();
		if(CommentUtils.REQ_ISON_TRUE.equals(type)){
			//调用加关注方法
			LOG.info("加关注");
			
			ua = ucenterFacade.doAttention(null, muid, uid, String.valueOf(level));
			if(ResultUtils.SUCCESS==ua.getFlag()){
				//添加用户的粉丝数量
				userJedisManager.incrOneUserInfo(uid, JedisConstant.USER_HASH_FANS_NOTICE);
				Map<String, String> userHashMap = userJedisManager.getUserInfo(uid);
				String fansTime = userHashMap.get(JedisConstant.USER_HASH_FANS_TIME);
				String fansNoticeNum = userHashMap.get(JedisConstant.USER_HASH_FANS_NOTICE);
				if(null==fansTime){//当粉丝的更新时间为空的时候
					userJedisManager.saveOneUserInfo(uid, JedisConstant.USER_HASH_FANS_TIME, System.currentTimeMillis()+"");
				}
			}
			//添加用户的粉丝列表
			userJedisManager.saveOneUserFans(ua.getUserId(), ua.getLatestRevisionDate(), ua.getUserAttentionId());
			
			//添加用户的关注列表
			userJedisManager.saveOneUserAttention(ua.getUserId(), ua.getLatestRevisionDate(), ua.getUserAttentionId());
			//添加用户关注的关系列表信息
			/*Map<String, String> userAttentionMap = userJedisManager.getRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId());
			if(null==userAttentionMap||userAttentionMap.isEmpty()||null==userAttentionMap.get(JedisConstant.RELATION_USER_ISINTEREST)){//当用户的关注关系为空时
				userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "1");
			}*/
			//当关注关系列表不为空时
			/*Map<String, String> attentionUserMap = userJedisManager.getRelationUserAttentionInfo(ua.getUserAttentionId(),ua.getUserId());
			if(null!=attentionUserMap&&!attentionUserMap.isEmpty()&&null!=attentionUserMap.get(JedisConstant.RELATION_USER_ISINTEREST)){*/
			/*	userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "0");
			}else{//查询数据库这个人是否已经关注了我
*/				UserAttention  userAttention  = ucenterFacade.findUserAttentionIsExist(ua.getUserAttentionId(),ua.getUserId());
				Long attentionId = userAttention.getAttentionId();
				if(0!=attentionId.longValue()){//当这个人关注我时
					int isAttention = userAttention.getIsAttention();
					if(1==isAttention){
						userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "0");
						userJedisManager.saveRelationUserAttentionInfo(ua.getUserAttentionId(), ua.getUserId(), JedisConstant.RELATION_USER_ISINTEREST, "0");
					}else if(0==isAttention){
						userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "1");
						userJedisManager.saveRelationUserAttentionInfo(ua.getUserAttentionId(), ua.getUserId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
					}
				}else{//当这个人没有关注我时
					userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "1");
					userJedisManager.saveRelationUserAttentionInfo(ua.getUserAttentionId(), ua.getUserId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
				}
			//}
			
			//Map<String, String> userAttentionMap = userJedisManager.getRelationUserAttentionInfo(muid, uid);
			/*try {
				//pushManager.pushAttentionMSG(muid, uid);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}*/
		}else{
			//调用取消关注方法
			UserAttention  uAttention  = ucenterFacade.findUserAttentionIsExist(muid,uid);
			LOG.info("取消关注");
			ua = ucenterFacade.cancelAttention(null, muid, uid);
			//用户关注列表减一
			userJedisManager.delOneUserAttention(muid, uid);
			//用户粉丝列表减一
			userJedisManager.delOneUserFans(muid, uid);
			Map<String, String> userHashMap = userJedisManager.getUserInfo(uid);
			String fansTime = "0";
			if(null!=userHashMap&&!userHashMap.isEmpty()){
				fansTime = userHashMap.get(JedisConstant.USER_HASH_FANS_TIME);
				if(null==fansTime){
					fansTime = "0";
				}
			}
			String fansNoticeNum = userHashMap.get(JedisConstant.USER_HASH_FANS_NOTICE);
			long lastestTime = uAttention.getLatestRevisionDate();
			if(lastestTime>Long.valueOf(fansTime)){
				String fansNoticNum = userJedisManager.subtractOneUserInfo(uid, JedisConstant.USER_HASH_FANS_NOTICE);
				if(Long.valueOf(fansNoticNum)<0){
					userJedisManager.saveOneUserInfo(uid, JedisConstant.USER_HASH_FANS_NOTICE, "0");
				}
			}
			
			//获取用户关注的关系列表信息
			UserAttention  userAttention  = ucenterFacade.findUserAttentionIsExist(ua.getUserAttentionId(),ua.getUserId());
			Long attentionId = userAttention.getAttentionId();
			if(0!=attentionId.longValue()){//当这个人关注我时
				int isAttention = userAttention.getIsAttention();
				if(1==isAttention){//当这个人关注我时
					userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
					userJedisManager.saveRelationUserAttentionInfo(ua.getUserAttentionId(), ua.getUserId(), JedisConstant.RELATION_USER_ISINTEREST, "1");
				}else if(0==isAttention){
					userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
					userJedisManager.saveRelationUserAttentionInfo(ua.getUserAttentionId(), ua.getUserId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
				}
			}else{//当这个人没有关注我时
				userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
				userJedisManager.saveRelationUserAttentionInfo(ua.getUserAttentionId(), ua.getUserId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
			}
			/*Map<String, String> userAttentionMap = userJedisManager.getRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId());
			String isInterest = "";
			if(null!=userAttentionMap&&!userAttentionMap.isEmpty()&&null!=userAttentionMap.get(JedisConstant.RELATION_USER_ISINTEREST)){//当用户的关注关系为空时
				userJedisManager.saveRelationUserAttentionInfo(ua.getUserId(), ua.getUserAttentionId(), JedisConstant.RELATION_USER_ISINTEREST, "2");
			}
		}*/
		}
		
		datas = new HashMap<String, Object>();
		flagint = ua.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		//拼接数据
		datas.put("flag", flag);

		//处理返回数据
		resString = getResponseData(datas);

		return resString;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setPushManager(PushManager pushManager) {
		this.pushManager = pushManager;
	}
	
	
}
