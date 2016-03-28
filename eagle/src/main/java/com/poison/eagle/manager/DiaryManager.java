package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Diary;
import com.poison.resource.model.Serialize;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class DiaryManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(DiaryManager.class);
	
	private int flagint;
	
	private DiaryFacade diaryFacade;
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private TopicFacade topicFacade;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	
	private JedisManager jedisManager;
	private ResourceManager resourceManager;
	private UserJedisManager userJedisManager;
	
	private RocketProducer eagleProducer;
	/**
	 * 写日记
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeDiary(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =new HashMap<String, Object>();
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		List reasonList = null;
		String lon = "";//经度
		String lat = "";//维度
		String locationName="";//地点描述
		String locationCity="";//地点城市
		String locationArea="";//地点地区
		String title = "";// 标题
		String cover = "";// 封面
		List<Map<String, String>> oList = new ArrayList<Map<String,String>>();//话题列表等信息
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			reasonList = new ArrayList();
			reasonList = (List) dataq.get("list");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		lon = CheckParams.objectToStr((String) dataq.get("lon"));
		lat = CheckParams.objectToStr((String) dataq.get("lat"));
		locationName = CheckParams.objectToStr((String) dataq.get("locationName"));
		locationCity = CheckParams.objectToStr((String) dataq.get("locationCity"));
		locationArea = CheckParams.objectToStr((String) dataq.get("locationArea"));
		
		Map<String, Object> map = WebUtils.putDataToMap(reasonList,ucenterFacade);
		String resultContent = (String) map.get("resultContent");
		String linkType = (String) map.get("linkType");
		String linkName = (String) map.get("linkName");
		
		//标题
		title = CheckParams.objectToStr((String) dataq.get("title"));
		//封面
		cover = CheckParams.objectToStr((String) dataq.get("cover"));
		
		
		Diary diary = diaryFacade.addDiary(CommentUtils.TYPE_DIARY,resultContent , uid,lon,lat,locationName,locationCity,locationArea,title,cover);
		//用户的相册提取出来
//		List<String> photoList = WebUtils.getPhotoList();
//		UserAlbum userAlbum = ucenterFacade.insertintoUserAlbum(uid, photoList, CommentUtils.TYPE_ALBUM);
		
		flagint = diary.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将日志放入到缓存中
			resourceManager.setResourceToJedis(diary, uid,uid,0l);
			
			//更新用户的最后更新时间
			ucenterFacade.saveUserLatestInfo(uid, diary.getId(), CommentUtils.TYPE_DIARY);
			
			//日志成功时 判断是否有话题 存入话题相关表中
			oList = (List<Map<String, String>>) map.get("oList");
			if(null!=oList&&oList.size()>0){
				long rid = diary.getId();
				String resType = CommentUtils.TYPE_DIARY;
				Iterator<Map<String, String>> oListIt = oList.iterator();
				String topicName = "";
				String oListType = "";
				Long toUid = 0l;
				while(oListIt.hasNext()){
					Map<String, String> oneListmap = oListIt.next();
					topicName = oneListmap.get("name");
					oListType = oneListmap.get("type");
					if(oListType.equals(CommentUtils.TYPE_TOPIC)){
						//插入一个话题
						topicFacade.saveTopicOrLink(uid, topicName, CommentUtils.TYPE_TOPIC, "", "", "", rid, resType);
					}else if(oListType.equals(CommentUtils.TYPE_USER)){
						//添加@推送消息
						try{
							JSONObject json = new JSONObject();
							toUid = Long.valueOf(oneListmap.get("id"));
							//插入@信息
							userJedisManager.incrOneUserInfo(toUid, JedisConstant.USER_HASH_AT_NOTICE);
							json.put("uid", uid);
							json.put("toUid", toUid);
							json.put("rid", rid);
							json.put("type", CommentUtils.TYPE_DIARY);
							json.put("pushType", PushManager.PUSH_AT_TYPE);
							json.put("context", resultContent.replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
							json.toString();
							eagleProducer.send("pushMessage", "toBody", "", json.toString());
							actFacade.insertintoActAt(uid, rid, uid, CommentUtils.TYPE_DIARY, toUid,rid,CommentUtils.TYPE_DIARY);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			//增加用户日记数量
			try {
				userStatisticsFacade.updateDiaryCount(uid);
			} catch (Exception e) {
				LOG.error("增加用户日记数量失败:"+e.getMessage(), e.fillInStackTrace());
			}
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);

		return resString;
	}
	/**
	 * 日记列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewDiaryList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");
		
		
		long userId = Long.valueOf(dataq.get("id").toString());
		
		List<Diary> diaryList = new ArrayList<Diary>();
		List<ResourceInfo> resourceList = new ArrayList<ResourceInfo>();
		
		diaryList = diaryFacade.queryType(userId, CommentUtils.TYPE_DIARY);
		

		resourceList = getResponseList(diaryList, null, resourceList);
		
		//对资源进行倒序排列
		Collections.sort(resourceList);
		if(resourceList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
			resourceList = resourceList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceList);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List getResponseList(List<Diary> reqList , String type , List<ResourceInfo> resList){
		ResourceInfo ri = null;
		
		if(reqList.size()>0){
			if(reqList.get(0).getId() != 0){
				flagint = ResultUtils.SUCCESS;
				for (Diary obj : reqList) {
					ri = fileUtils.putObjectToResource(obj, ucenterFacade,actFacade);
					resList.add(ri);
				}
			}
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		
		
		return resList;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setDiaryFacade(DiaryFacade diaryFacade) {
		this.diaryFacade = diaryFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	
}
