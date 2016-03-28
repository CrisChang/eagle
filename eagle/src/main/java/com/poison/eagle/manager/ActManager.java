package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poison.act.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Serialize;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class ActManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ActManager.class);
//	private Map<String, Object> req ;
//	private Map<String, Object> dataq;
//	private Map<String, Object> datas ;
//	private String resString;//返回数据
//	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//	private String error;
	
	private int flagint;
	private SerializeFacade serializeFacade;
	private SerializeListFacade serializeListFacade;
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	private GetResourceInfoFacade getResourceInfoFacade;
	private BkFacade bkFacade;
	private MyMovieFacade myMovieFacade;
	private MvFacade mvFacade;
	private BkCommentFacade bkCommentFacade;
	private MvCommentFacade mvCommentFacade;
	private PostFacade postFacade;
	private DiaryFacade diaryFacade;
	private NetBookFacade netBookFacade;
	
	private MomentJedisManager momentJedisManager;
	
	private ResourceManager resourceManager;
	
	private RelationToUserAndResManager relationToUserAndResManager;
	
	private UserJedisManager userJedisManager;
	
	private ResStatJedisManager resStatJedisManager;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	/**
	 * 收藏
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String collectResource(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		long id = 0;
		String type = "";
		int status = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			type = dataq.get("type").toString();
			status = Integer.valueOf(dataq.get("status").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ActCollect actCollect = new ActCollect();
		
		if(status == FALSE){
			actCollect = actFacade.cancelCollect(uid, id);
		}else{
			actCollect = actFacade.doCollect(uid, id, type);
		}
		
		
		flagint = actCollect.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			//收藏数量
			int fNum = actFacade.findCollectCount(actCollect.getResourceId());
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("num", fNum);
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
	 * 删除收藏
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delCollect(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		long id = 0;
		String type = "";
		int status = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ActCollect actCollect = new ActCollect();
		
		
		if(status == FALSE){
			actCollect = actFacade.cancelCollect(uid, id);
		}else{
			actCollect = actFacade.doCollect(uid, id, type);
		}
		
		
		flagint = actCollect.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			//收藏数量
			int fNum = actFacade.findCollectCount(actCollect.getResourceId());
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("num", fNum);
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
	 * 显示收藏列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewCollectList(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq = null;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		String type = "";
		Long lastId = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = dataq.get("type").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			lastId = Long.valueOf(dataq.get("lastId").toString());
			if(0l==lastId.longValue()){
				lastId = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			lastId = null;
		}
		
		if(CommentUtils.TYPE_ALL.equals(type)){
			type = null;
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		
		
		resourceInfos = getCollects(uid, resourceInfos,lastId);
		
		//倒序
		Collections.sort(resourceInfos);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
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
	 * 获取收藏列表
	 * @param uid
	 * @param resourceInfos
	 * @return
	 */
	public List<ResourceInfo> getCollects(Long uid,List<ResourceInfo> resourceInfos,Long lastId){
		
		
		List<ActCollect> actCollects = actFacade.findUserCollectedList(uid, lastId);
				//actFacade.findUserCollectList(null, uid, CommentUtils.TYPE_MOVIE_COMMENT);
		/*List<ActCollect> actCollects = actFacade.findUserCollectList(null, uid, CommentUtils.TYPE_BOOK_COMMENT);
		List<ActCollect> actCollects2 = actFacade.findUserCollectList(null, uid, CommentUtils.TYPE_ARTICLE);
		List<ActCollect> actCollects3 = actFacade.findUserCollectList(null, uid, CommentUtils.TYPE_DIARY);
		List<ActCollect> actCollects4 = actFacade.findUserCollectList(null, uid, CommentUtils.TYPE_ACT);
		actCollects.addAll(collects);
		actCollects.addAll(actCollects2);
		actCollects.addAll(actCollects3);
		actCollects.addAll(actCollects4);*/
//		System.out.println("用户收藏数:"+actCollects.size());
		
		resourceInfos = getResourceLists(actCollects,uid, resourceInfos);
		
		Iterator<ResourceInfo> iterator = resourceInfos.iterator();
		String btimeStr = "";
		while(iterator.hasNext()){
			ResourceInfo temp = iterator.next();
		    if (temp.getRid() == 0) {
		        iterator.remove();
		    }
		  /*  btimeStr = temp.getBtime();
		    //System.out.println();
		    if(null!=btimeStr&&!btimeStr.equals("")){
		    	long tempBtime = DateUtil.formatLong(btimeStr, "yyyy-MM-dd HH:mm:ss");
		    	System.out.println(tempBtime);
		    	//加上缓存来存放个人的收藏sas
		    	momentJedisManager.saveOneCollected(uid, -tempBtime, temp.getRid());
		    }*/
		}
		
		return resourceInfos;
	}
	
	
	
	
	/**
	 * 显示订阅列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewSubscribeList(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		String type = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = dataq.get("type").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CommentUtils.TYPE_ALL.equals(type)){
			type = null;
		}
		
		List<ActSubscribe> actSubscribes = actFacade.findSubscribeList(uid, type);
		
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		resourceInfos = getSubscribeResourceLists(actSubscribes,uid, resourceInfos);
		
		Iterator<ResourceInfo> iterator = resourceInfos.iterator();
		while(iterator.hasNext()){
			ResourceInfo temp = iterator.next();
		    if (temp.getRid() == 0) {
		        iterator.remove();
		    }

		}
		//倒序
		Collections.sort(resourceInfos);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
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
	 * 订阅连载
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String subscribeSerialize(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		String type = "";
		long id = 0;
		int status = 1;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = dataq.get("type").toString();
			id = Long.valueOf(dataq.get("id").toString());
			status = Integer.valueOf(dataq.get("status").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ActSubscribe actSubscribe = new ActSubscribe();
		if(TRUE == status){
			actSubscribe = actFacade.doSubscribe(uid, id, type);
		}else{
			actSubscribe = actFacade.cancelSubscribe(uid, id);
		}
		
		flagint = actSubscribe.getFlag();
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
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
	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getResourceLists(List reqList  , Long uid, List<ResourceInfo> resList){
		ResourceInfo si = null;
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			String objName = reqList.get(0).getClass().getName();
			long id = 0;
			if(ActCollect.class.getName().equals(objName)){
				ActCollect object = (ActCollect) reqList.get(0);
				id = object.getId();
			}
			
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (Object object : reqList) {
					if(ActCollect.class.getName().equals(objName)){
						si = resourceManager.putObjectToResource(object, uid,0);
//						try {
//							String type = ((ActCollect)object).getType();
//							if(CommentUtils.TYPE_BOOKLIST.equals(type)){
//								si = actUtils.putCollectBookListToResource((ActCollect)object, ucenterFacade, actFacade, getResourceInfoFacade, bkFacade,netBookFacade,uid);
//							}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){
//								si = actUtils.putCollectMovieListToResource((ActCollect)object, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
//							}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
//								si = actUtils.putCollectBkCommentToResource((ActCollect)object, ucenterFacade, actFacade, bkFacade, bkCommentFacade,netBookFacade,getResourceInfoFacade,uid);
//							}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
//								si = actUtils.putCollectMvCommentToResource((ActCollect)object, ucenterFacade, actFacade, mvFacade, mvCommentFacade,myMovieFacade,uid);
//							}else if(CommentUtils.TYPE_ARTICLE.equals(type)){
//								si = actUtils.putCollectPostToResource((ActCollect)object, ucenterFacade, actFacade, postFacade, uid);
//							}else if(CommentUtils.TYPE_DIARY.equals(type)){
//								si = actUtils.putCollectDiaryToResource((ActCollect)object, ucenterFacade, actFacade, diaryFacade, uid);
//							}else if(CommentUtils.TYPE_ACT.equals(type)){
//								ActTransmit actTransmit = actFacade.findOneTransmit(((ActCollect)object).getResourceId());
//								si = resourceManager.putActTransmitToResource(actTransmit, uid);
//								ResourceInfo fri = new ResourceInfo();
//								fri = fileUtils.putObjectToResource(object, ucenterFacade, actFacade);
//								fri.setResourceInfo(si);
////								si = actUtils.putCollectToResource(si, (ActCollect)object, ucenterFacade);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//							LOG.error("收藏内容显示出错，资源id为："+((ActCollect)object).getId(), e);
//						}
					}
					
					//将各种显示加上
//					actUtils.putIsCollectToResoure(si, uid, actFacade);
					if(si.getRid() != 0){
						resList.add(si);
					}
				}
			}
		}
		return resList;
	}
	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getSubscribeResourceLists(List reqList  , Long uid, List<ResourceInfo> resList){
		ResourceInfo si = null;
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			String objName = reqList.get(0).getClass().getName();
			long id = 0;
			if(ActSubscribe.class.getName().equals(objName)){
				ActSubscribe object = (ActSubscribe) reqList.get(0);
				id = object.getId();
			}
			
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (Object object : reqList) {
					if(ActSubscribe.class.getName().equals(objName)){
						String type = ((ActSubscribe)object).getType();
						if(CommentUtils.TYPE_PUSH.equals(type)){
							si = actUtils.putSubscribeSerializeToResource((ActSubscribe)object, ucenterFacade, actFacade, serializeFacade);
						}
					}
					
					//将各种显示加上
					actUtils.putIsCollectToResoure(si, uid, actFacade,0);
					resList.add(si);
				}
			}
		}
		return resList;
	}
	
	/**
	 * 
	 * <p>Title: choseUseable</p> 
	 * <p>Description: 判断是否有用</p> 
	 * @author :changjiang
	 * date 2015-6-8 下午7:55:22
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String chooseUseful(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq = null;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		Long lastId = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Long id = Long.valueOf(dataq.get("id").toString());
		String type = (String) dataq.get("type");
		String status = (String) dataq.get("status");
		Long res_userid = 0l;
		try {
			res_userid = Long.valueOf(dataq.get("resUserid").toString());
			if(res_userid == null){
				res_userid = 0l;
			}
		} catch (Exception e) {
			res_userid = 0l;
		}
		
		ActUseful actUseful = new ActUseful();
		
		//有用没用或者取消选择
		if(CommentUtils.COMMENT_USEFUL.equals(status)||CommentUtils.COMMENT_USELESS.equals(status)||CommentUtils.COMMENT_NOCHOOSEUSE.equals(status)){
			actUseful = actFacade.doUseful(id, type, res_userid, Integer.valueOf(status), uid);
		}
		int flagint = actUseful.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			

			//有用的个数加一
			if(0l!=res_userid&&CommentUtils.COMMENT_USEFUL.equals(status)){
				userJedisManager.incrOneUserInfo(res_userid, JedisConstant.USER_HASH_USEFUL_NOTICE);
			}
			//用户对资源是否有用这里状态更新
			relationToUserAndResManager.saveOneRelation(uid, id, JedisConstant.RLEATION_IS_USEFUL, actUseful.getIsUseful()+"");
			//资源的有用没用数更新
			Map<String, Object> usefulMap = new HashMap<String, Object>();
			usefulMap = actFacade.findUsefulCount(id);
			int usefulCount = (Integer) usefulMap.get("usefulCount");
			Map<String, Object> uselessMap = new HashMap<String, Object>();
			uselessMap = actFacade.findUselessCount(id);
			int uselessCount = (Integer) uselessMap.get("uselessCount");
			relationToUserAndResManager.saveOneRelationToRes(id, JedisConstant.RELATION_USEFUL_NUM, usefulCount+"");
			relationToUserAndResManager.saveOneRelationToRes(id, JedisConstant.RELATION_USELESS_NUM, uselessCount+"");
			datas.put("usefulCount", usefulCount);
			datas.put("uselessCount", uselessCount);



			if(actUseful.getResType().equals(CommentUtils.TYPE_BOOK_COMMENT)||CommentUtils.TYPE_ARTICLE_BOOK.equals(actUseful.getResType())){
				BkComment bkComment = bkCommentFacade.findCommentIsExistById(actUseful.getResourceId());
				if(null!=bkComment&&bkComment.getId()!=0){
					//更细点赞的数量
					//System.out.println("书评点赞更新");
					resStatJedisManager.doUsefulNum(id, type, bkComment.getBookId(), bkComment.getResType(),usefulCount,0);
					//resStatJedisManager.doNousefulNum(resourceId, type);
					//resStatJedisManager.updateActPraiseNum(id, type, act,bkComment.getBookId(),bkComment.getResType());
				}
			}else if(actUseful.getResType().equals(CommentUtils.TYPE_MOVIE_COMMENT)||CommentUtils.TYPE_ARTICLE_MOVIE.equals(actUseful.getResType())){
				MvComment mvComment = mvCommentFacade.findMvCommentIsExist(actUseful.getResourceId());
				if(null!=mvComment&&mvComment.getFlag()==ResultUtils.SUCCESS){
					//更细点赞的数量
					//System.out.println("影评点赞更新");
					//resStatJedisManager.doNousefulNum(resourceId, type);
					resStatJedisManager.doUsefulNum(id, type, mvComment.getMovieId(), CommentUtils.TYPE_MOVIE,usefulCount,mvComment.getStageid());
					//resStatJedisManager.updateActPraiseNum(id, type, act,mvComment.getMovieId(),CommentUtils.TYPE_MOVIE);
				}
			}else if(CommentUtils.TYPE_COMMENT.equals(actUseful.getResType())){
				ActComment actComment = actFacade.findCmtById(id);
				if(null!=actComment&&actComment.getFlag()==ResultUtils.SUCCESS){
					resStatJedisManager.doUsefulNum(id, type, actComment.getResourceId(), actComment.getType(),usefulCount,0);
				}
			}
			//推送消息
			/*if(CommentUtils.COMMENT_USEFUL.equals(status)){
				//推送消息
				long begin = System.currentTimeMillis();
				try {
					begin = System.currentTimeMillis();
					JSONObject json = new JSONObject();
					json.put("uid", uid);
					json.put("toUid", res_userid);
					json.put("rid", id);
					json.put("type", type);
					json.put("pushType", PushManager.PUSH_PARISE_TYPE);
					json.toString();
					long end = System.currentTimeMillis();
					//System.out.println("json包装耗时："+(end-begin));
					//eagleProducer.send("pushMessage", "toBody", "", json.toString());
					//pushManager.pushResourceMSG(uid, commentUid, id, type, pushType);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}*/
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
	 * 
	 * <p>Title: doHot</p> 
	 * <p>Description: 对资源加热</p> 
	 * @author :changjiang
	 * date 2015-7-9 上午11:53:27
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String doHot(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq = null;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		Long lastId = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Long id = Long.valueOf(dataq.get("id").toString());
		String type = (String) dataq.get("type");
		/*Long userid = 0l;
		try {
			userid = Long.valueOf(dataq.get("resUserid").toString());
			if(userid == null){
				userid = 0l;
			}
		} catch (Exception e) {
			userid = 0l;
		}*/

		//ActHot actHot = actFacade.doHot(uid, id, type,"");
		int flagint = ResultUtils.ERROR;//actHot.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", "初赛已结束，敬请关注复赛");
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	public void setSerializeFacade(SerializeFacade serializeFacade) {
		this.serializeFacade = serializeFacade;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setSerializeListFacade(SerializeListFacade serializeListFacade) {
		this.serializeListFacade = serializeListFacade;
	}
	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}
	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}
	public void setDiaryFacade(DiaryFacade diaryFacade) {
		this.diaryFacade = diaryFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}
	public void setMomentJedisManager(MomentJedisManager momentJedisManager) {
		this.momentJedisManager = momentJedisManager;
	}
	public void setRelationToUserAndResManager(
			RelationToUserAndResManager relationToUserAndResManager) {
		this.relationToUserAndResManager = relationToUserAndResManager;
	}
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	} 
	
}
