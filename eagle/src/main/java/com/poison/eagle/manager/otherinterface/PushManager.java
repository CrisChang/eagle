package com.poison.eagle.manager.otherinterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.poison.eagle.manager.ActManager;
import com.poison.otherinterface.umeng_java_sdk.push.Demo;
import com.poison.otherinterface.umeng_java_sdk.push.ios.IOSGroupcast;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserStatistics;

public class PushManager{

	/**
	 * 关注信息
	 */
	public final static String PUSH_ATTENTION_MSG = " 在毒药关注了你";
	/**
	 * 朋友圈发新消息提示
	 */
	public final static String PUSH_MOMENT_MSG = " 在毒药发送了新消息";
	
	/**
	 * 友盟“毒药”  线上环境的key
	 */
	//private static final String appStoreKey = "55028a47fd98c5d930000789";
	/**
	 * 打赏信息通知
	 */
	public final static String PUSH_GIVE_MSG = " 在毒药打赏了你";
	/**
	 * 评论信息通知
	 */
	public final static String PUSH_COMMENT_MSG = " 评论你:";
	/**
	 * 回复信息通知
	 */
	public final static String PUSH_COMMENT_TO_MSG = " 回复你:";
	/**
	 * 打赏信息通知
	 */
	public final static String PUSH_PARISE_MSG = " 在毒药赞了你";
	/**
	 * @信息通知
	 */
	public final static String PUSH_AT_MSG = " @了你：";
	/**
	 * 推送关注的通知
	 */
	public final static String PUSH_ATTENTION_TYPE = "50";
	/*
	 * 推送朋友圈的通知
	 */
	public final static String PUSH_MOMENTS_TYPE = "40";
	/**
	 * 推送打赏的通知
	 */
	public final static String PUSH_GIVE_TYPE = "60";
	/**
	 * 推送评论的通知
	 */
	public final static String PUSH_COMMENT_TYPE = "70";
	/**
	 * 推送回复的通知
	 */
	public final static String PUSH_COMMENT_TO_TYPE = "71";
	/**
	 * 推送赞的通知
	 */
	public final static String PUSH_PARISE_TYPE = "72";
	/**
	 * 推送@的通知
	 */
	public final static String PUSH_AT_TYPE = "80";
	/**
	 * 推送一本小说
	 */
	public final static String PUSH_STORY_TYPE = "210";
	/**
	 * 推送一个更新的章节
	 */
	public final static String PUSH_UPDATE_CHAPTER = "200";
	private static final  Log LOG = LogFactory.getLog(PushManager.class);
	/**
	 * 友盟“毒药”线上环境的appMasterSecret
	 */
	private static final String appStoreMasterSecret = "";
	/**
	 * 友盟“毒药”appkey
	 */
	private  String appkey;
	/**
	 * 友盟“毒药”appMasterSecret
	 */
	private  String appMasterSecret;
	/**
	 * 小说推送的key
	 */
	private String storyappkey;
	/**
	 * 小说推送的mastersecret
	 */
	private String storyappMasterSecret;
	private   String timestamp = null;
	private UcenterFacade ucenterFacade;
	private UserStatisticsFacade userStatisticsFacade;

	/**
	 *
	 * <p>Title: buildPushObject_all_all_alert</p>
	 * <p>Description: 极光推送消息体</p>
	 * @author :changjiang
	 * date 2015-3-26 下午7:03:39
	 * @param userId
	 * @param type
	 * @param msg
	 * @return
	 */
	public static PushPayload buildPushObject_all_all_alert(long userId,String type,String msg,Long resId,String resType) {
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("res_id", resId+"");
		extras.put("res_type", resType);
		extras.put("type", type);
		extras.put("url","www.baidu.com");
		//Message message = Message.newBuilder().addExtra("id", "142797933568458752").addExtra("type", "1");
		return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(userId+""))
			//.alias(userId+""))
                .setNotification(Notification.newBuilder()
						.addPlatformNotification(IosNotification.newBuilder()
								.setAlert(msg)
								.setBadge(1)
								.addExtras(extras)
								.build())
						.addPlatformNotification(AndroidNotification.newBuilder()
								.setAlert(msg)
								.setBuilderId(1)
								.addExtras(extras)
								.build())
						.build())
               /* .setMessage(Message.newBuilder()
                		.setMsgContent("你好啊")
                		.addExtra("id", "142797933568458752")
                		.addExtra("type", "1")
                		.build())*/
                .setOptions(Options.newBuilder()
						.setApnsProduction(true)
						.build())
                .build();
    }

	public void setStoryappkey(String storyappkey) {
		this.storyappkey = storyappkey;
	}
	
	public void setStoryappMasterSecret(String storyappMasterSecret) {
		this.storyappMasterSecret = storyappMasterSecret;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	
	public void setAppMasterSecret(String appMasterSecret) {
		this.appMasterSecret = appMasterSecret;
	}

	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}

	/*public PushManager(String key, String secret) {
		try {
			appkey = key;
			appMasterSecret = secret;
			timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}*/
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	
	/**
	 *
	 * <p>Title: pushAttentionMSG</p>
	 * <p>Description: 推送关注消息</p>
	 * @author :changjiang
	 * date 2015-2-6 下午12:24:36
	 * @param userId 当前用户
	 * @param userAttentionId 被关注的人用户ID
	 */
	public  void pushAttentionMSG(long userId,long userAttentionId){
		IOSGroupcast groupcast = new IOSGroupcast();
		timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		/*System.out.println(userId);
		System.out.println(userAttentionId);*/
		try {
			groupcast.setAppMasterSecret(appMasterSecret);
			groupcast.setPredefinedKeyValue("appkey", appkey);
			groupcast.setPredefinedKeyValue("timestamp", timestamp);
			/*  TODO
			 *  Construct the filter condition:
			 *  "where":
			 *	{
	    	 *		"and":
	    	 *		[
	      	 *			{"tag":"iostest"}
	    	 *		]
			 *	}*/

			JSONObject filterJson = new JSONObject();
			JSONObject whereJson = new JSONObject();
			JSONArray tagArray = new JSONArray();
			JSONObject testTag = new JSONObject();
			testTag.put("tag", userAttentionId+"");
			tagArray.put(testTag);
			whereJson.put("or", tagArray);
			filterJson.put("where", whereJson);
			System.out.println(filterJson.toString());

			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
			String userName = userAllInfo.getName();
			// Set filter condition into rootJson
			groupcast.setPredefinedKeyValue("filter", filterJson);
			groupcast.setPredefinedKeyValue("alert", userName+PUSH_ATTENTION_MSG);
			groupcast.setPredefinedKeyValue("badge", 1);
			groupcast.setPredefinedKeyValue("sound", "chime");
			// TODO set 'production_mode' to 'true' if your app is under production mode
			groupcast.setPredefinedKeyValue("production_mode", "true");

			groupcast.setCustomizedField("test", "pushAttentionMSG");
			//groupcast.setCustomizedField("id", "142797933568458752");
			groupcast.setCustomizedField("type", PUSH_ATTENTION_TYPE);
			groupcast.send();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		/*UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
		String userName = userAllInfo.getName();
		System.out.println(userName);
		String pushMSG = userName + PUSH_ATTENTION_MSG;
		String attentionId = userAttentionId+"";
		Demo demo = new Demo(appkey, appMasterSecret,pushMSG,attentionId,PUSH_ATTENTION_TYPE);
		try{
			demo.sendIOSGroupcast();
		}catch (Exception e) {
			e.printStackTrace();
		}*/

	}
	
	/**
	 *
	 * <p>Title: pushMomentMSG</p>
	 * <p>Description: 发送朋友圈的消息推送,一次只能推送50个</p>
	 * @author :changjiang
	 * date 2015-2-9 下午5:19:29
	 * @param userId   当前用户id
	 * @param resId
	 * @param resType
	 */
	public  void pushMomentMSG(long userId,List<Long> userAttentionIdList,Long resId,String resType){
		IOSGroupcast groupcast = new IOSGroupcast();
		timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		/*System.out.println(userId);
		System.out.println(userAttentionId);*/
		try {
			groupcast.setAppMasterSecret(appMasterSecret);
			groupcast.setPredefinedKeyValue("appkey", appkey);
			groupcast.setPredefinedKeyValue("timestamp", timestamp);
			/*  TODO
			 *  Construct the filter condition:
			 *  "where":
			 *	{
	    	 *		"and":
	    	 *		[
	      	 *			{"tag":"iostest"}
	    	 *		]
			 *	}*/

			JSONObject filterJson = new JSONObject();
			JSONObject whereJson = new JSONObject();
			JSONArray tagArray = new JSONArray();
			JSONObject testTag = new JSONObject();
			if(null!=userAttentionIdList&&userAttentionIdList.size()>0){
				StringBuffer sbs = new StringBuffer("[");
				StringBuffer sb = new StringBuffer();
				Iterator<Long> it = userAttentionIdList.iterator();
				while(it.hasNext()){
					sb.append("{\"tag\":\""+it.next()+"\"},");
				}
				if(sb.length()>0){
					sbs.append(sb.substring(0,sb.length()-1));
				}
				sbs.append("]");
				tagArray= new JSONArray(sbs.toString());
			}
			/*testTag.put("tag", userAttentionId+"");
			tagArray.put(testTag);*/
			whereJson.put("or", tagArray);
			filterJson.put("where", whereJson);
			System.out.println(filterJson.toString());

			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
			String userName = userAllInfo.getName();
			// Set filter condition into rootJson
			groupcast.setPredefinedKeyValue("filter", filterJson);
			groupcast.setPredefinedKeyValue("alert", userName+PUSH_MOMENT_MSG);
			groupcast.setPredefinedKeyValue("badge", 1);
			groupcast.setPredefinedKeyValue("sound", "chime");
			// TODO set 'production_mode' to 'true' if your app is under production mode
			groupcast.setPredefinedKeyValue("production_mode", "true");

			groupcast.setCustomizedField("test", "pushMomentMSG");
			groupcast.setCustomizedField("res_id", resId+"");
			groupcast.setCustomizedField("res_type", resType);
			groupcast.setCustomizedField("type", PUSH_MOMENTS_TYPE);
			groupcast.send();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		/*UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
		String userName = userAllInfo.getName();
		System.out.println(userName);
		String pushMSG = userName + PUSH_ATTENTION_MSG;
		String attentionId = userAttentionId+"";
		Demo demo = new Demo(appkey, appMasterSecret,pushMSG,attentionId,PUSH_ATTENTION_TYPE);
		try{
			demo.sendIOSGroupcast();
		}catch (Exception e) {
			e.printStackTrace();
		}*/

	}

	/**
	 *
	 * <p>Title: pushGiveMSG</p>
	 * <p>Description: 推送打赏信息</p>
	 * @author :changjiang
	 * date 2015-2-11 下午7:13:02
	 * @param userId
	 * @param userAttentionId
	 * @param resId
	 * @param resType
	 */
	public  void pushGiveMSG(long userId,long userAttentionId,Long resId,String resType,double rewardAmt){
		IOSGroupcast groupcast = new IOSGroupcast();
		timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		/*System.out.println(userId);
		System.out.println(userAttentionId);*/
		try {
			groupcast.setAppMasterSecret(appMasterSecret);
			groupcast.setPredefinedKeyValue("appkey", appkey);
			groupcast.setPredefinedKeyValue("timestamp", timestamp);
			/*  TODO
			 *  Construct the filter condition:
			 *  "where":
			 *	{
	    	 *		"and":
	    	 *		[
	      	 *			{"tag":"iostest"}
	    	 *		]
			 *	}*/

			JSONObject filterJson = new JSONObject();
			JSONObject whereJson = new JSONObject();
			JSONArray tagArray = new JSONArray();
			JSONObject testTag = new JSONObject();
			testTag.put("tag", userAttentionId+"");
			tagArray.put(testTag);
			whereJson.put("or", tagArray);
			filterJson.put("where", whereJson);
			System.out.println(filterJson.toString());

			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
			String userName = userAllInfo.getName();
			// Set filter condition into rootJson
			groupcast.setPredefinedKeyValue("filter", filterJson);
			groupcast.setPredefinedKeyValue("alert", userName+PUSH_GIVE_MSG+rewardAmt+"元");
			groupcast.setPredefinedKeyValue("badge", 1);
			groupcast.setPredefinedKeyValue("sound", "chime");
			// TODO set 'production_mode' to 'true' if your app is under production mode
			groupcast.setPredefinedKeyValue("production_mode", "true");

			groupcast.setCustomizedField("test", "pushGiveMSG");
			//groupcast.setCustomizedField("id", "142797933568458752");
			groupcast.setCustomizedField("res_id", resId+"");
			groupcast.setCustomizedField("res_type", resType);
			groupcast.setCustomizedField("type", PUSH_GIVE_TYPE);
			groupcast.send();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}
	
	/**
	 *
	 * <p>Title: pushResourceMSG</p>
	 * <p>Description: 推送评论赞信息</p>
	 * @author :温晓宁
	 * date 2015-2-11 下午7:13:02
	 * @param userId
	 * @param resId
	 * @param resType
	 */
	public  void pushResourceMSG(long userId,long toUid,Long resId,String resType,String type,String pushContext){
		long begin = System.currentTimeMillis();
		IOSGroupcast groupcast = new IOSGroupcast();
		timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		/*System.out.println(userId);
		System.out.println(userAttentionId);*/
		try {
			groupcast.setAppMasterSecret(appMasterSecret);
			groupcast.setPredefinedKeyValue("appkey", appkey);
			groupcast.setPredefinedKeyValue("timestamp", timestamp);
			/*  TODO
			 *  Construct the filter condition:
			 *  "where":
			 *	{
			 *		"and":
			 *		[
			 *			{"tag":"iostest"}
			 *		]
			 *	}*/

			JSONObject filterJson = new JSONObject();
			JSONObject whereJson = new JSONObject();
			JSONArray tagArray = new JSONArray();
			JSONObject testTag = new JSONObject();
			testTag.put("tag", toUid+"");
			tagArray.put(testTag);
			whereJson.put("or", tagArray);
			filterJson.put("where", whereJson);
			System.out.println(filterJson.toString());

			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
			String userName = userAllInfo.getName();
			// Set filter condition into rootJson
			groupcast.setPredefinedKeyValue("filter", filterJson);
			String alert = "";
			if(PUSH_COMMENT_TYPE.equals(type)){
				alert = userName + PUSH_COMMENT_MSG+pushContext;
			}else if(PUSH_PARISE_TYPE.equals(type)){
				alert = userName + PUSH_PARISE_MSG;
			}else if(PUSH_COMMENT_TO_TYPE.equals(type)){
				alert = userName + PUSH_COMMENT_TO_MSG+pushContext;
			}
			groupcast.setPredefinedKeyValue("alert", alert);
			groupcast.setPredefinedKeyValue("badge", 1);
			groupcast.setPredefinedKeyValue("sound", "chime");
			// TODO set 'production_mode' to 'true' if your app is under production mode
			groupcast.setPredefinedKeyValue("production_mode", "true");
			groupcast.setCustomizedField("test", "pushGiveMSG");
			//groupcast.setCustomizedField("id", "142797933568458752");
			groupcast.setCustomizedField("res_id", resId+"");
			groupcast.setCustomizedField("res_type", resType);
			groupcast.setCustomizedField("type", type);
			groupcast.send();
			long end = System.currentTimeMillis();
			System.out.println("推送评论耗时："+(end-begin));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}

	/**
	 *
	 * <p>Title: jpushResourceMSG</p>
	 * <p>Description: </p>
	 * @author :changjiang
	 * date 2015-3-26 下午6:59:02
	 * @param userId
	 * @param toUid
	 * @param resId
	 * @param resType
	 * @param type
	 * @param pushContext
	 */
	public void jpushResourceMSG(long userId,long toUid,Long resId,String resType,String type,String pushContext){
		JPushClient jpushClient = new JPushClient(appMasterSecret, appkey,3);
		// For push, all you need do is to build PushPayload object.
        try {
        	UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
        	UserStatistics userStatistics = userStatisticsFacade.findUserStatisticsByUid(toUid);
        	String userName = userAllInfo.getName();
        	String alert = "";
        	boolean isPush = false;
        	System.out.println("推送的用户评论开关为"+userStatistics.getCommentSwitch()+"推送的用户的@开关为"+userStatistics.getAtSwitch());
			if(PUSH_COMMENT_TYPE.equals(type)&&userStatistics.getCommentSwitch()==0){
				alert = userName + PUSH_COMMENT_MSG+pushContext;
				isPush = true;
			}else if(PUSH_PARISE_TYPE.equals(type)){
				alert = userName + PUSH_PARISE_MSG;
			}else if(PUSH_COMMENT_TO_TYPE.equals(type)&&userStatistics.getCommentSwitch()==0){
				alert = userName + PUSH_COMMENT_TO_MSG+pushContext;
				isPush = true;
			}else if(PUSH_GIVE_TYPE.equals(type)&&userStatistics.getGiveSwitch()==0){
				alert = userName + PUSH_GIVE_MSG +pushContext;
				isPush = true;
			}else if(PUSH_AT_TYPE.equals(type)&&userStatistics.getAtSwitch()==0){
				alert = userName + PUSH_AT_MSG +pushContext;
				isPush = true;
			}else{
				isPush = false;
			}

			if(isPush){
				PushPayload payload = buildPushObject_all_all_alert(toUid,type,alert,resId,resType);

				PushResult result = jpushClient.sendPush(payload);
				LOG.info("Got result - " + result);
			}

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            LOG.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            LOG.error("Should review the error, and fix the request", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
        }
	}
	
	public void jpushStoryMSG(long toUid,Long resId,String resType,String type,String pushContext){
		JPushClient jpushClient = new JPushClient(storyappMasterSecret, storyappkey,3);
		// For push, all you need do is to build PushPayload object.
		try {
//			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
//			UserStatistics userStatistics = userStatisticsFacade.findUserStatisticsByUid(toUid);
//			String userName = userAllInfo.getName();
//			String alert = "";
//			boolean isPush = false;
//			System.out.println("推送的用户评论开关为"+userStatistics.getCommentSwitch()+"推送的用户的@开关为"+userStatistics.getAtSwitch());
//			if(PUSH_COMMENT_TYPE.equals(type)&&userStatistics.getCommentSwitch()==0){
//				alert = userName + PUSH_COMMENT_MSG+pushContext;
//				isPush = true;
//			}else if(PUSH_PARISE_TYPE.equals(type)){
//				alert = userName + PUSH_PARISE_MSG;
//			}else if(PUSH_COMMENT_TO_TYPE.equals(type)&&userStatistics.getCommentSwitch()==0){
//				alert = userName + PUSH_COMMENT_TO_MSG+pushContext;
//				isPush = true;
//			}else if(PUSH_GIVE_TYPE.equals(type)&&userStatistics.getGiveSwitch()==0){
//				alert = userName + PUSH_GIVE_MSG +pushContext;
//				isPush = true;
//			}else if(PUSH_AT_TYPE.equals(type)&&userStatistics.getAtSwitch()==0){
//				alert = userName + PUSH_AT_MSG +pushContext;
//				isPush = true;
//			}else{
//				isPush = false;
//			}
			String alert = pushContext;

			if(PUSH_UPDATE_CHAPTER.equals(type)){//小说章节更新
				PushPayload payload = buildPushObject_all_all_alert(toUid,type,alert,resId,resType);
				PushResult result = jpushClient.sendPush(payload);
				LOG.info("Got result - " + result);
			}

//			if(isPush){
//				PushPayload payload = buildPushObject_all_all_alert(toUid,type,alert,resId,resType);
//
//				PushResult result = jpushClient.sendPush(payload);
//				LOG.info("Got result - " + result);
//			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	public static void main(String[] args) {
		JPushClient jpushClient = new JPushClient("846ac235af1468fd197852b4", "8eefe8299294ba578a0760f1",3);
		PushPayload payload = buildPushObject_all_all_alert(500000085,"220","今日毒药|韩东的少年生涯",416130589561856000l,"43");

		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
	}

}
