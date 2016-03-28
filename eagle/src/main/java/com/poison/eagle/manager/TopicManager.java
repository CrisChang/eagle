package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TopicInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.model.Topic;
import com.poison.resource.model.TopicLink;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserInfo;
/**
 * 话题manager
 * @author Administrator
 *
 */
public class TopicManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(TopicManager.class);
	
	private int flagint;
	
	private TopicFacade topicFacade;
	
	private UcenterFacade ucenterFacade;
	
	private ResourceManager resourceManager;
	
	private ResourceJedisManager resourceJedisManager;
	
	/**
	 * 资源关系
	 */
	private JedisSimpleClient relationToUserandresClient;
	
	/**
	 * 用户的缓存
	 */
	private UserJedisManager userJedisManager;
	
	/**
	 * 查询一个话题详情
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTopic(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		
		String title = "";
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			title = (String) dataq.get("title");
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		if(title==null || title.trim().length()==0){
			error = "话题标题不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		Topic topic = topicFacade.findTopic(title);
		flagint = topic.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//增加阅读数量
			Topic topic2 = topicFacade.addTopicReadcount(topic.getId(),uid);
			if(topic2!=null && topic2.getFlag()==ResultUtils.SUCCESS){
				topic = topic2;
			}else{
				topic.setReadcount(topic.getReadcount()+1);
			}
			UserInfo userinfo = ucenterFacade.findUserInfoByUserId(null, topic.getUserid());
			long talkcount = topicFacade.getTopicLinkCountByTopicId(topic.getId());
			String nickname = "NONAME";
			if(userinfo!=null && userinfo.getName()!=null && userinfo.getName().trim().length()>0){
				nickname = userinfo.getName();
			}
			TopicInfo topicInfo = TopicInfo.copy(topic);
			topicInfo.setNickname(nickname);
			topicInfo.setTalkcount(talkcount);
			datas.put("map", topicInfo);
		}else if(flagint == UNID){
			flag = CommentUtils.RES_FLAG_ERROR;
			error = "话题不存在或已经被删除";
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 查询一个话题详情
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTopicById(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		
		Long id = null;
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String idstr = dataq.get("id")+"";
			if(StringUtils.isInteger(idstr)){
				id = Long.valueOf(idstr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		if(id==null || id<=0){
			error = "话题id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		Topic topic = topicFacade.findTopicByID(id);
		flagint = topic.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//增加阅读数量
			Topic topic2 = topicFacade.addTopicReadcount(topic.getId(),uid);
			if(topic2!=null && topic2.getFlag()==ResultUtils.SUCCESS){
				topic = topic2;
			}else{
				topic.setReadcount(topic.getReadcount()+1);
			}
			UserInfo userinfo = ucenterFacade.findUserInfoByUserId(null, topic.getUserid());
			long talkcount = topicFacade.getTopicLinkCountByTopicId(topic.getId());
			String nickname = "NONAME";
			if(userinfo!=null && userinfo.getName()!=null && userinfo.getName().trim().length()>0){
				nickname = userinfo.getName();
			}
			TopicInfo topicInfo = TopicInfo.copy(topic);
			topicInfo.setNickname(nickname);
			topicInfo.setTalkcount(talkcount);
			datas.put("map", topicInfo);
		}else if(flagint == UNID){
			flag = CommentUtils.RES_FLAG_ERROR;
			error = "话题不存在或已经被删除";
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 创建话题
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String createTopic(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String title = "";
		String cover = "";
		String tags = "";
		String description = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			title = (String) dataq.get("title");
			cover = (String) dataq.get("cover");
			tags = (String) dataq.get("tags");
			description = (String)dataq.get("description");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		datas = new HashMap<String, Object>();
		if(title==null || title.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			error = "话题标题不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			String resString = getResponseData(datas);
			return resString;
		}
		if(title.trim().length()>50){
			flag = CommentUtils.RES_FLAG_ERROR;
			error = "话题标题不能超过50个字";
			datas.put("error", error);
			datas.put("flag", flag);
			String resString = getResponseData(datas);
			return resString;
		}
		
		Topic topic = topicFacade.insertTopic(userId, title.trim(), cover, tags, description);
		flagint = topic.getFlag();
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("map", topic);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 设置话题
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String setTopic(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long topicid = 0;
		String cover = "";
		String tags = "";
		String description = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String topicidStr = dataq.get("topicid")+"";
			if(StringUtils.isInteger(topicidStr)){
				topicid = Long.parseLong(topicidStr);
			}
			cover = (String) dataq.get("cover");
			tags = (String) dataq.get("tags");
			description = (String)dataq.get("description");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		if(topicid<=0){
			error = "话题id不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		Topic topic = topicFacade.updateTopic(cover, tags, description, topicid, userId);
		flagint = topic.getFlag();
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("map", topic);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 查询一个话题相关的资源列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTopicResources(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		
		long topicid = 0;
		Long resid = null;
		String isoperation = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String topicidStr = dataq.get("topicid")+"";
			String residStr = dataq.get("resid")+"";
			isoperation = dataq.get("isoperation")+"";
			if(StringUtils.isInteger(topicidStr)){
				topicid = Long.parseLong(topicidStr);
			}
			if(StringUtils.isInteger(residStr)){
				resid = Long.parseLong(residStr);
				if(resid<=0){
					resid = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		if(topicid<=0){
			error = "话题id不能为空";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		//根据topicid查询关联资源列表
		int pageSize = 10;
		//List<ResourceInfo> infos = new ArrayList<ResourceInfo>();
		List<String> list = new ArrayList<String>(pageSize);
		List<Long> resourceids = new ArrayList<Long>(pageSize);
		List<TopicLink> topicLinks = null;
		List<TopicLink> essences = null;//精华的
		if("essence".equals(isoperation)){
			//精华帖
			topicLinks = topicFacade.findTopicLinkInfoByTopicid(topicid,TopicLink.ISOPERATION_ESSENCE,resid, pageSize);
		}else if("normal".equals(isoperation)){
			//先查询出一个精华
			essences = topicFacade.findTopicLinkInfoByTopicid(topicid,TopicLink.ISOPERATION_ESSENCE,null, 2);
			/*if(essences!=null && essences.size()>0 && essences.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				essence = essences.get(0);
			}*/
			TopicLink lasttopicLink = null;
			//普通帖，包含置顶
			if(resid==null){
				topicLinks = topicFacade.findTopicLinkInfoByTopicid(topicid,TopicLink.ISOPERATION_TOP,resid, pageSize+1);
			}else{
				lasttopicLink = topicFacade.findTopicIsExist(topicid, resid);
				if(lasttopicLink!=null && lasttopicLink.getId()>0 && lasttopicLink.getIsOperation()==TopicLink.ISOPERATION_TOP){
					//是置顶的
					topicLinks = topicFacade.findTopicLinkInfoByTopicid(topicid,TopicLink.ISOPERATION_TOP,resid, pageSize+1);
				}
			}
			int size = 0;
			if(topicLinks!=null && topicLinks.size()>0 && topicLinks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				size = topicLinks.size();
			}
			if(size<pageSize){
				if(size>0){
					//查询缺少的
					List<TopicLink> normaltopicLinks = topicFacade.findTopicLinkInfoByTopicid(topicid,TopicLink.ISOPERATION_NORMAL,null, pageSize-size);
					topicLinks.addAll(normaltopicLinks);
				}else{
					//List<TopicLink> lasttopicLinks = null;
					if(resid!=null && lasttopicLink==null){
						//如果没有置顶，则可能是上一页置顶刚刚查询完，也可能是普通的在翻页
						lasttopicLink = topicFacade.findTopicIsExist(topicid, resid);
					}
					//如果resid对应的记录是置顶的，则说明是刚开始查询普通的
					if(lasttopicLink!=null && lasttopicLink.getId()>0 && lasttopicLink.getIsOperation()==TopicLink.ISOPERATION_TOP){
						topicLinks=topicFacade.findTopicLinkInfoByTopicid(topicid,TopicLink.ISOPERATION_NORMAL,null, pageSize);
					}else{
						//如果resid对应的记录是普通的，则说明是在翻页查询普通的
						topicLinks=topicFacade.findTopicLinkInfoByTopicid(topicid,TopicLink.ISOPERATION_NORMAL,resid, pageSize);
					}
				}
			}else if(size>pageSize){
				//置顶的已经够数量了
				topicLinks = topicLinks.subList(0, pageSize);
			}else{
				//如果就剩下pageSize条置顶了，需要考虑往下如何翻页
				
			}
		}else{
			//旧版本的讨论列表
			topicLinks = topicFacade.findTopicLinkInfoByTopicid(topicid,resid, pageSize);
		}
		if(topicLinks!=null && topicLinks.size()>0){
			for(int i=0;i<topicLinks.size();i++){
				TopicLink topicLink = topicLinks.get(i);
				if(topicLink!=null && topicLink.getId()>0){
					String resourcestr = resourceJedisManager.getOneResource(topicLink.getResid());
					if(resourcestr!=null){
						//System.out.println("=======================缓存中查出的数据:"+resourcestr);
						//需要判断是否存在置顶帖
						if(topicLink.getIsOperation()==TopicLink.ISOPERATION_TOP){
							resourcestr = addTopFlagForResourceStr(resourcestr);
						}
						list.add(resourcestr);
						resourceids.add(topicLink.getResid());
						
					}/*else{
						ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(topicLink.getResid(), topicLink.getRestype(), userId);
						if(resourceInfo!=null && resourceInfo.getRid()>0){
							//infos.add(resourceInfo);
							try {
								resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
								list.add(resourcestr);
								resourceids.add(topicLink.getResid());
							} catch (JsonGenerationException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							int result = resourceManager.setResourceToJedis(resourceInfo, userId, userId,0l);
							//System.out.println("=======================放入缓存的结果:"+result);
						}
					}*/
				}
			}
			
			list = handleJsonValue(userId, list, resourceids);//处理 赞、评 等信息
			
		}
		List<String> essencelist = new ArrayList<String>(2);
		if(essences!=null && essences.size()>0 && essences.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
			for(int i=0;i<essences.size();i++){
				String essenceStr = resourceJedisManager.getOneResource(essences.get(i).getResid());
				essencelist.add(essenceStr);
			}
		}
		/*flagint = ResultUtils.SUCCESS;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", infos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);*/
		//处理返回数据
		//resString = getResponseData(datas);
		//resString = RES_DATA_RIGHT_BEGIN+"\"list\":"+list.toString()+RES_DATA_RIGHT_END;
		resString = RES_DATA_RIGHT_BEGIN+"\"essence\":"+essencelist.toString()+",\"list\":"+list.toString()+RES_DATA_RIGHT_END;
		return resString;
	}
	
	//为资源字符串增加置顶标志
	private String addTopFlagForResourceStr(String resourcestr){
		if(resourcestr!=null && resourcestr.length()>0){
			if(resourcestr.indexOf("\"type\":\"")>-1){
				resourcestr = resourcestr.replaceFirst("\"type\":\"","\"istop\":\""+1+"\",\"type\":\"");
			}else if(resourcestr.indexOf("\"type\": \"")>-1){
				resourcestr = resourcestr.replaceFirst("\"type\": \"","\"istop\":\""+1+"\",\"type\":\"");
			}
		}
		return resourcestr;
	}
	
	/**
	 * 查询话题排行根据讨论数
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String TopicRanking(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		long page = 1;
		long endtime = System.currentTimeMillis();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			String endtimeStr = dataq.get("endtime")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
			if(StringUtils.isInteger(endtimeStr)){
				endtime = Long.parseLong(endtimeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		long period = 7*24*60*60*1000L;//话题为7天内的
		//long endtime = System.currentTimeMillis();
		long starttime = endtime - period;
		
		List<Topic> topics = topicFacade.findTopicRanking(starttime, endtime, start);
		List<TopicInfo> topicInfos = new ArrayList<TopicInfo>();
		int size = 0;//当前返回数据集合的长度
		if(topics!=null && topics.size()==1 && topics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(topics!=null){
				size = topics.size();
			}
			//封装一次topics
			if(topics!=null && topics.size()>0){
				List<Long> userids=new ArrayList<Long>(size);
				for(int i=0;i<topics.size();i++){
					TopicInfo topicInfo = TopicInfo.copy(topics.get(i));
					topicInfos.add(topicInfo);
					userids.add(topicInfo.getUserid());
				}
				List<UserInfo> users = ucenterFacade.findUserListByUseridList(null, userids);
				if(users!=null && users.size()>0){
					for(int i=0;i<topicInfos.size();i++){
						for(int j=0;j<users.size();j++){
							if(topicInfos.get(i).getUserid()==users.get(j).getUserId()){
								topicInfos.get(i).setNickname(users.get(j).getName());
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", topicInfos);
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("endtime", endtime+"");
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	
	/**
	 * 查询话题排行根据讨论数--所有时间的
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String TopicRankingAllTime(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		long page = 1;
		//long endtime = System.currentTimeMillis();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			//String endtimeStr = dataq.get("endtime")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
			/*if(StringUtils.isInteger(endtimeStr)){
				endtime = Long.parseLong(endtimeStr);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Topic> topics = topicFacade.findTopicRankingAllTime(start,pagesize);
		List<TopicInfo> topicInfos = new ArrayList<TopicInfo>();
		int size = 0;//当前返回数据集合的长度
		if(topics!=null && topics.size()==1 && topics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(topics!=null){
				size = topics.size();
			}
			//封装一次topics
			if(topics!=null && topics.size()>0){
				List<Long> userids=new ArrayList<Long>(size);
				for(int i=0;i<topics.size();i++){
					TopicInfo topicInfo = TopicInfo.copy(topics.get(i));
					topicInfos.add(topicInfo);
					userids.add(topicInfo.getUserid());
				}
				List<UserInfo> users = ucenterFacade.findUserListByUseridList(null, userids);
				if(users!=null && users.size()>0){
					for(int i=0;i<topicInfos.size();i++){
						for(int j=0;j<users.size();j++){
							if(topicInfos.get(i).getUserid()==users.get(j).getUserId()){
								topicInfos.get(i).setNickname(users.get(j).getName());
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", topicInfos);
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			//datas.put("endtime", endtime+"");
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	
	public List<String> handleJsonValue(final long userId,List<String> values,List<Long> resourceids){
		if(values==null || values.size()==0){
			return values;
		}
		List<String> resultLists = new LinkedList<String>();
		// pipeline.sync();
		String inList = "";
		String isPraise = "";
		String isCollect = "";
		String zNum = "";
		String cNum = "";
		for(int i=0;i<values.size();i++){
			String value = values.get(i);
			if(null!=value){//查找用户对这个资源的关系,当前只有是否赞过
				//获取用户信息的id
				String uid = "0";
				try{
					uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
				}catch(Exception e){
					e.printStackTrace();
				}
				final Long resourceid = resourceids.get(i);
				if(!uid.equals("0")){//用户id不为0时
					Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
					if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
						//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
						value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
						value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
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
						String relationKey = userId+JedisConstant.RELATION_TO_USER_AND_RES+resourceid;
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
				String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+9);
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
//				System.out.println(value);
				if(null!=inList&&!"".equals(inList)){
					value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
				}else{
					value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
				}
//				System.out.println(value);
				resultLists.add(value);
				/*long end = System.currentTimeMillis();
				System.out.println("缓存中遍历附加信息的耗时："+(end-begin));*/
			}
		}
		
		return resultLists;
	}
	
	
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setResourceJedisManager(ResourceJedisManager resourceJedisManager) {
		this.resourceJedisManager = resourceJedisManager;
	}
	public void setRelationToUserandresClient(
			JedisSimpleClient relationToUserandresClient) {
		this.relationToUserandresClient = relationToUserandresClient;
	}
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
}
