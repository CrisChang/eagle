package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActUseful;
import com.poison.eagle.entity.CommentInfo;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.eagle.utils.story.StoryUtils;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.model.Diary;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.model.TopicLink;
import com.poison.resource.service.ResStatisticService;
import com.poison.ucenter.client.StoryUserFacade;
import com.poison.ucenter.client.UcenterFacade;

/**
 * 小说资源管理类
 * @author songdan
 * @date 2016年3月4日
 * @version V1.0
 */
public class StoryResourceManager extends BaseManager{
	private static final Log LOG = LogFactory.getLog(StoryResourceManager.class);

	/**资源统计service*/
	private ResStatisticService resStatisticService;
	
	private ActFacade actFacade;
	
	private FileUtils fileUtils = FileUtils.getInstance();

	private StoryUserFacade storyUserFacade;
	
	private TopicFacade topicFacade;
	
	private RocketProducer eagleProducer;
	
	private RelationToUserAndResManager relationToUserAndResManager;
	
	private ResourceJedisManager resourceJedisManager;
	
	
	
	public void setRelationToUserAndResManager(RelationToUserAndResManager relationToUserAndResManager) {
		this.relationToUserAndResManager = relationToUserAndResManager;
	}




	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}




	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}


	/**
	 * 用户的相关缓存
	 */
	private UserJedisManager userJedisManager;
	
	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}




	public void setStoryUserFacade(StoryUserFacade storyUserFacade) {
		this.storyUserFacade = storyUserFacade;
	}




	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
	
	


	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}




	/**
	 * 小说评论的评论列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String commentList(String reqs,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		Long lastId = 0l;
		Long id = 0l;
		String type = "";//1：评论列表，2：列表
		String sort= "";//1是热门
		int totalNumber=0;
		int page = 0;
		String resourceType= "";//资源类型
		reqs = reqs.trim();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Long.valueOf(dataq.get("id").toString());

			sort = CheckParams.objectToStr((String)dataq.get("sort"));

			page = CheckParams.objectToInt((String)dataq.get("page"));

			resourceType = CheckParams.objectToStr((String)dataq.get("resourceType"));
			try {
				type = (String) dataq.get("type");
				if(type == null){
					type = "1";
				}
			} catch (Exception e) {
				type = "1";
			}

			try {
				lastId = Long.valueOf(dataq.get("lastId").toString());
				if(lastId == 0l){
					lastId = null;
				}
			} catch (Exception e) {
				lastId = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<CommentInfo> commentInfos = new ArrayList<CommentInfo>();
		datas = new HashMap<String, Object>();
		try {
			if("1".equals(type)){

				List<ActComment> comList = new ArrayList<ActComment>();
				if("1".equals(sort)){//热门评论
					//查看多少热门评论
					Map<String,Object> map = resStatisticService.findResStatisticByLinkIdAndType(id, CommentUtils.TYPE_COMMENT);
					int count = (Integer)map.get("count");
					if(count>=10){
						List<ResStatistic> resStatisticList = resStatisticService.findResStatisticRankByUsefulAndType(CommentUtils.TYPE_COMMENT, id, resourceType, 0, (page-1) * 10,10);
						Iterator<ResStatistic> resStatisticListIt = resStatisticList.iterator();
						long resId = 0l;
						while(resStatisticListIt.hasNext()){
							ResStatistic resStatistic = resStatisticListIt.next();
							resId = resStatistic.getResId();
							ActComment actComment = actFacade.findCmtById(resId);
							comList.add(actComment);
						}

						commentInfos = getCommentList(comList,uid, commentInfos);


						totalNumber = count;
					}
				}else{
					comList = actFacade.findResCommentList(null, id, lastId);//(null, id);
					commentInfos = getCommentList(comList,uid, commentInfos);

					totalNumber = actFacade.findCommentCount(null, id);
				}
			}else if("2".equals(type)){
				List<ActPraise> actPraises = actFacade.findPraiseListByResIdAndType(id, lastId);
				commentInfos = getCommentList(actPraises,uid, commentInfos);
				totalNumber = actFacade.findPraiseCount(null, id);
			}else if("3".equals(type)){
				List<ActUseful> usefulList =actFacade.findUsefulListByResIdAndType(id, lastId);
				commentInfos = getCommentList(usefulList,uid, commentInfos);
				Map<String, Object> usefulMap = actFacade.findUsefulCount(id);
				totalNumber = (Integer) usefulMap.get("usefulCount");
			}
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//对资源进行倒序排列
			Collections.sort(commentInfos);
			datas.put("list", commentInfos);
			datas.put("totalNumber", totalNumber+"");
		} catch (Exception e) {
			error = MessageUtils.getResultMessage(Integer.parseInt(CommentUtils.RES_FLAG_ERROR));
			LOG.error("错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 * 将评论列表格式化
	 * @param list
	 * @param type
	 * @return
	 */
	private List getCommentList(List reqList , Long uid, List<CommentInfo> resList){
		CommentInfo ci = null;
		if(reqList.size()>0){
			for (Object obj : reqList) {
				//未注册的话用指定ID
				ci = fileUtils.putObjToStoryComment(obj, storyUserFacade,actFacade,uid);
				if(ci.getId() != 0){
					resList.add(ci);
				}
			}
		}else if(reqList.size() == 0){
		}
		return resList;
	}
	
	/**
	 * 评论资源
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String comment(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		List<Map<String, Object>> oList = new ArrayList<Map<String,Object>>();//话题列表等信息
		//List commentList = null;
//		uid=500000007L;
		//去掉空格
		reqs = reqs.trim();

		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);


		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");

		Long id = Long.valueOf(dataq.get("id").toString());
		String type = (String) dataq.get("type");
		final String content = (String) dataq.get("content");

		Long commentUid = 0l;
		try {
			commentUid = Long.valueOf(dataq.get("toUid").toString());
			if(commentUid == null){
				commentUid = 0l;
			}
		} catch (Exception e) {
			commentUid = 0l;
		}
		Long commentId = 0l;
		try {
			commentId = Long.valueOf(dataq.get("commentId").toString());
			if(commentId == null){
				commentId = 0l;
			}
		} catch (Exception e) {
			commentId = 0l;
		}
		Long res_userid = 0l;
		try {
			res_userid = Long.valueOf(dataq.get("res_userid").toString());
			if(res_userid == null){
				res_userid = 0l;
			}
		} catch (Exception e) {
			res_userid = 0l;
		}
		//ActPraise act = null;

		//content = getFilterContent(content, uid, id, type);
//		oList = WebUtils.checkIsAt(content, oList, storyUserFacade);
		oList.add(new HashMap(){
			{
				put("type","0");
				put("content",content);
			}
		});
		String resultStr = StoryUtils.putData2Str(oList);
		ActComment actComment = new ActComment();
		actComment = actFacade.doOneComment(null, uid, id, type, resultStr,commentUid,commentId,res_userid);
		int flagint = actComment.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			if(res_userid.longValue()!=uid.longValue()){
				//添加评论人的消息数量
				userJedisManager.incrOneUserInfo(res_userid, JedisConstant.USER_HASH_COMMENT_NOTICE);
			}
			long commentUserId = actComment.getCommentUserId();
			if(0l!=commentUserId&&commentUserId!=res_userid){//如果回复的话添加回复人的红点提示
				userJedisManager.incrOneUserInfo(commentUserId, JedisConstant.USER_HASH_COMMENT_NOTICE);
			}
			//TODO: 将评论后的附加信息放入缓存中
			try {
				int cNum = actFacade.findCommentCount(null, id);
				updateResourceByJedis(id, uid, JedisConstant.RELATION_COMMENT_NUM, cNum+"");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			/*try{
				//如果话题和评论的资源有关联，则更新话题和资源的关联表的更新时间
				TopicLink topicLink = new TopicLink();
				topicLink.setResid(id);
				topicLink.setLatestRevisionDate(System.currentTimeMillis());
				topicFacade.updateTopicLinkLatestrevisiondateByResid(topicLink);
			}catch(Exception e){
				e.printStackTrace();
			}*/
			/*if(null!=oList&&oList.size()>0){
				long rid = actComment.getId();
				String resType = CommentUtils.TYPE_COMMENT;
				Iterator<Map<String, Object>> oListIt = oList.iterator();
				String topicName = "";
				String oListType = "";
				Long toUid = 0l;
				while(oListIt.hasNext()){
					Map<String, Object> oneListmap = oListIt.next();
					topicName = (String) oneListmap.get("name");
					oListType = (String) oneListmap.get("type");
					if(oListType.equals(CommentUtils.TYPE_USER)){
						//添加@推送消息
						try{
							JSONObject json = new JSONObject();
							toUid = Long.valueOf(oneListmap.get("id"));
							//插入@信息
							userJedisManager.incrOneUserInfo(toUid, JedisConstant.USER_HASH_AT_NOTICE);
							json.put("uid", uid);
							json.put("toUid", toUid);
							json.put("rid", rid);
							json.put("type", CommentUtils.TYPE_COMMENT);
							json.put("pushType", PushManager.PUSH_AT_TYPE);
							json.put("context", resultStr.replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
							json.toString();
							eagleProducer.send("pushMessage", "toBody", "", json.toString());
							actFacade.insertintoActAt(uid, rid, uid, CommentUtils.TYPE_COMMENT, toUid,id,type);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}*/

			//推送消息
			/*long begin = System.currentTimeMillis();
			try {
				String pushType ="";
				if(commentId == 0){
					pushType = PushManager.PUSH_COMMENT_TYPE;
				}else{
					pushType = PushManager.PUSH_COMMENT_TO_TYPE;
				}
				JSONObject json = new JSONObject();
				json.put("uid", uid);
				json.put("toUid", commentUid);
				json.put("rid", id);
				json.put("type", type);
				json.put("pushType", pushType);
				json.put("context", actComment.getCommentContext().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				json.toString();
				eagleProducer.send("pushMessage", "toBody", "", json.toString());
				//pushManager.pushResourceMSG(uid, commentUid, id, type, pushType);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			long end = System.currentTimeMillis();
			System.out.println("异步评论耗时："+(end-begin));*/
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
	 * 修改资源是修改缓存中的信息(isPraise,isCollect,zNum,cNum,,)
	 * @param rid 资源id
	 * @param uid 用户id
	 * @param key 信息key
	 * @param value 信息
	 */
	private void updateResourceByJedis(Long rid,Long uid,String key,String value){
		long begin = System.currentTimeMillis();
		try {
			String result = relationToUserAndResManager.saveOneRelationToRes(rid, key, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		long end = System.currentTimeMillis();
		System.out.println("添加["+key+"]信息到资源缓存的耗时："+(end-begin));

	}
	
	/**
	 * 删除资源
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delResource(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		//去掉空格
		reqs = reqs.trim();
		long id = 0;
		String type = "";

		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Long.valueOf(dataq.get("id").toString());
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);


		int flagint = 0;
		ActComment actComment = actFacade.deleteComment(null, id);
		flagint = actComment.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			//System.out.println("是否执行删除缓存操作"+flagint);
			if(!CommentUtils.TYPE_COMMENT.equals(type)){
				delResourceFromJedis(uid, id, type);
				//删除话题相关
//				topicFacade.deleteTopicLinkByResid(id, uid);
			}
		}


		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
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




	private int delResourceFromJedis(Long uid, long id, String type) {
		int resultFlag = ResultUtils.ERROR;
		try{
			resultFlag = resourceJedisManager.delOneResource(id);
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
		return resultFlag;
	}
}
