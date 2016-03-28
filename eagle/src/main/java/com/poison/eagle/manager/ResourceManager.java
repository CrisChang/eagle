package com.poison.eagle.manager; 

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.poison.eagle.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActTransmit;
import com.poison.act.model.ActUseful;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.CommentInfo;
import com.poison.eagle.entity.MovieInfo;
//import com.poison.eagle.entity.QuestInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SortEntity;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.JedisManager.UserTagJedis;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.msg.client.MsgFacade;
import com.poison.msg.model.MsgAt;
import com.poison.paycenter.client.PaycenterFacade;
//import com.poison.quest.client.QuestFacade;
//import com.poison.quest.model.Quest;
//import com.poison.quest.model.QuestProgress;
//import com.poison.quest.model.QuestReward;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GraphicFilmFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.Article;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.Diary;
import com.poison.resource.model.GraphicFilm;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.Post;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.model.ResourceLink;
import com.poison.resource.model.ResourceRanking;
import com.poison.resource.model.Serialize;
import com.poison.resource.model.Topic;
import com.poison.resource.model.TopicLink;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.TalentZoneLink;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class ResourceManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ResourceManager.class);
	
	private static int flagint;
	private GetResourceInfoFacadeImpl getResourceInfoFacade;
	private ActFacade actFacade;
	private UcenterFacade ucenterFacade;
	private DiaryFacade diaryFacade;
	private PostFacade postFacade;
	private ArticleFacade articleFacade;
	private BkFacade bkFacade;
	private BkCommentFacade bkCommentFacade;
	private MyBkFacade myBkFacade;
	private SerializeFacade serializeFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private GraphicFilmFacade graphicFilmFacade;
	//电影相关
	private MvCommentFacade mvCommentFacade;
	private MyMovieFacade myMovieFacade;
	private MvFacade mvFacade;
//	private ResourceInfo resourceInfo;
	private MsgFacade msgFacade;
	private PaycenterFacade paycenterFacade;
	private NetBookFacade netBookFacade;
	/**
	 * 资源关系
	 */
	private JedisSimpleClient relationToUserandresClient;
	
	private JedisSimpleClient jedisSimpleClient;

	/**
	 * 用户的相关缓存
	 */
	private UserJedisManager userJedisManager;

	private JedisSimpleClient resourceHashClient;

	private JedisManager jedisManager;
	private ResourceManager resourceManager;
	private SensitiveManager sensitiveManager;
	private SearchApiManager searchApiManager;
	private PushManager pushManager;
	private FansListManager fansListManager;
	private ResStatJedisManager resStatJedisManager;
	
	private MomentJedisManager momentJedisManager;
	private ResourceJedisManager resourceJedisManager;
	private RelationToUserAndResManager relationToUserAndResManager;
	
	private ResStatisticService resStatisticService;
	
	private TopicFacade topicFacade;
	
	private RocketProducer eagleProducer;
	
	//private PaycenterManager paycenterManager;
	
	//private QuestFacade questFacade;

	//public void setQuestFacade(QuestFacade questFacade) {
		//this.questFacade = questFacade;
	//}
	//public void setPaycenterManager(PaycenterManager paycenterManager) {
		//this.paycenterManager = paycenterManager;
	//}

	
	private FileUtils fileUtils = FileUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	private BookUtils bookUtils = BookUtils.getInstance();
	private PostUtils postUtils = PostUtils.getInstance();
	private ArticleUtils articleUtils = ArticleUtils.getInstance();
	//private ResStatJedisManager resStatJedisManager;
	
	public static void main(String[] args) {
		ResourceManager resourceManager = new ResourceManager();

		List<String> list = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rid", 171019451972587520l);
		map.put("type", "3");
		String string = "";
		try {
			string = resourceManager.getObjectMapper().writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		list.add(string);


		System.out.println(list.toString());
		list = resourceManager.getResourceListByJedis(list, 16l);
		System.out.println(list.toString());
	}
	
	/**
	 * 首页显示方法
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String userIndex(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		String userType = null;
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
		try{
			userType = (String)dataq.get("userType");
		}catch (Exception e) {
			//e.printStackTrace();
			userType = null;
		}

		final Long lastId = 0l;

		List<BookList> bookList = new ArrayList<BookList>();
		List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();

		if(id == UNID){
			id = null;
		}


		//long begin = System.currentTimeMillis();
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		//先从缓存中获取数据
		/*try {
			begin = System.currentTimeMillis();
//			resourceInfos = jedisManager.getJedisResources(null,uid,id,1);
			long end = System.currentTimeMillis();
//			System.out.println("调用首页所需时间"+(end - begin));
			begin = System.currentTimeMillis();
			//resourceInfos = jedisManager.getJedisResourceList(id);
			end = System.currentTimeMillis();
//			System.out.println("调用首页所需时间"+(end - begin));
//			//先截取15条
//			if(resourceInfos.size()>CommentUtils.RESOURCE_PAGE_SIZE){
//				resourceInfos = resourceInfos.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
//			}
//			resoureList = getResponseList(resourceInfos, uid, resoureList);

			LOG.info("从缓存中获取首页数据成功，数据为:"+resoureList);
		} catch (Exception e) {
			resoureList = new ArrayList<ResourceInfo>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}*/

		/*begin = System.currentTimeMillis();
		System.out.println("朋友圈的type类型为"+userType);*/
		//从缓存中取出朋友圈数据
		List<String> list =  new ArrayList<String>();
		if(null==userType){//全部广场
			list = momentJedisManager.getSquareMoment(uid, id,userType);
		}else{//用户类型广场
			list = momentJedisManager.getUserTypeSquareMoment(uid, id, userType);
		}

		if(list != null && list.size()>0){
			resString = RES_DATA_RIGHT_BEGIN+"\"list\":"+list.toString()+RES_DATA_RIGHT_END;
		}else{
		//如果查出来是空，进行查库
		//System.out.println("查出来的广场为空");
		if(null == resoureList  || resoureList.size()==0){
			//begin = System.currentTimeMillis();
			//查询全部信息
//			resoureList = getResoureListByPage(null,id,uid,resoureList);
			resoureList = getResoureListByPageNew(null,id,uid,resoureList,userType);
			long end = System.currentTimeMillis();
			//System.out.println("查询数据库总耗时："+(end-begin));
		}


		//对资源进行倒序排列
		Collections.sort(resoureList);

		if(resoureList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
			resoureList = resoureList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
		}
		//每条信息截取100字
		//begin = System.currentTimeMillis();
		actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
		//long end = System.currentTimeMillis();
		//System.out.println("截取100字总耗时："+(end-begin));
		//资源列表放入缓存中
		setResourcesToJedis(resoureList, uid);
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resoureList);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);

		//end = System.currentTimeMillis();
		//System.out.println("调用首页所需时间"+(end - begin));
//		LOG.info("调用首页所需时间"+(end - begin));
		}
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 将库中查出的资源列表放入缓存中
	 * @param resourceInfos
	 * @param uid
	 */
	public void setResourcesToJedis(List<ResourceInfo> resourceInfos,Long uid){
		//long begin = System.currentTimeMillis();
		Iterator<ResourceInfo> iter = resourceInfos.iterator();
		while(iter.hasNext()){
			ResourceInfo resourceInfo1 = iter.next();
			int flag = ResultUtils.ERROR;
			ResourceInfo resourceInfo = putObjectToResource(resourceInfo1, uid,1);
			if(resourceInfo != null && resourceInfo.getRid() != 0){
				//flag = jedisManager.setJedisResourceList(resourceInfo);
//				flag = jedisManager.setJedisResources(resourceInfo);
				long fuid = uid;
				long frid = resourceInfo.getRid();
				/*UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null, uid);
				int level = uInfo.getLevel();
				System.out.println("用户信息的等级为"+level);
				if(level==50){//添加神人广场
					momentJedisManager.saveOneShenRenMoment(frid,0l);
				}else{//添加普通人广场
					momentJedisManager.saveOneCommonMoment(frid,0l);
				}*/
//				System.out.println("插入前缓存："+frid);
				//String StrId = momentJedisManager.saveOneItem(fuid, frid,0l);
//				System.out.println("插入缓存："+StrId);
				String uidstr = frid+"";
//				if(uidstr.equals(StrId)){
				long ruid = 0;
				try {
					UserEntity userEntity = resourceInfo.getUserEntity();
					if(userEntity != null){
						ruid = userEntity.getId();
						/*if(userEntity.getType()==50){//添加神人广场
							momentJedisManager.saveOneShenRenMoment(frid,0l);
							System.out.println("添加神人广场");
						}else{//普通人添加普通人广场
							momentJedisManager.saveOneCommonMoment(frid,0l);
						}*/
						//添加全部广场
						momentJedisManager.saveOneItem(0,frid,0l);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}

			/*	if(uid == ruid){//个人动态
					String iUid = momentJedisManager.saveOneDynamic(fuid, frid,0l);
				}
			setResourceToJedis(resourceInfo, resourceInfo.getUserEntity().getId(),uid,0l);*/
			}
		}
		//long end = System.currentTimeMillis();
		//System.out.println("将库中查出的资源列表放入缓存中，耗时："+(end-begin));
	}
	
	/**
	 *
	 * <p>Title: setcircleOfFriendsResourcesToJedis</p>
	 * <p>Description: 将朋友圈的信息放入缓存</p>
	 * @author :changjiang
	 * date 2015-5-25 下午6:24:15
	 * @param resourceInfos
	 * @param uid
	 */
	public void setcircleOfFriendsResourcesToJedis(List<ResourceInfo> resourceInfos,Long uid){
		//long begin = System.currentTimeMillis();
		Iterator<ResourceInfo> iter = resourceInfos.iterator();
		while(iter.hasNext()){
			ResourceInfo resourceInfo1 = iter.next();
			int flag = ResultUtils.ERROR;
			ResourceInfo resourceInfo = putObjectToResource(resourceInfo1, uid,1);
			if(resourceInfo != null && resourceInfo.getRid() != 0){
				//flag = jedisManager.setJedisResourceList(resourceInfo);
//				flag = jedisManager.setJedisResources(resourceInfo);
				long fuid = uid;
				long frid = resourceInfo.getRid();
				/*UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null, uid);
				int level = uInfo.getLevel();
				System.out.println("用户信息的等级为"+level);
				if(level==50){//添加神人广场
					momentJedisManager.saveOneShenRenMoment(frid,0l);
				}else{//添加普通人广场
					momentJedisManager.saveOneCommonMoment(frid,0l);
				}*/
//				System.out.println("插入前缓存："+frid);
				String StrId = momentJedisManager.saveOneItem(fuid, frid,0l);
//				System.out.println("插入缓存："+StrId);
				/*String uidstr = frid+"";
//				if(uidstr.equals(StrId)){
				long ruid = 0;
				try {
					UserEntity userEntity = resourceInfo.getUserEntity();
					if(userEntity != null){
						ruid = userEntity.getId();
						if(userEntity.getType()==50){//添加神人广场
							momentJedisManager.saveOneShenRenMoment(frid,0l);
							System.out.println("添加神人广场");
						}else{//普通人添加普通人广场
							momentJedisManager.saveOneCommonMoment(frid,0l);
						}
						momentJedisManager.saveOneItem(0,frid,0l);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}

				if(uid == ruid){//个人动态
					String iUid = momentJedisManager.saveOneDynamic(fuid, frid,0l);
				}
			setResourceToJedis(resourceInfo, resourceInfo.getUserEntity().getId(),uid,0l);*/
			}
		}
		//long end = System.currentTimeMillis();
		//System.out.println("将库中查出的资源列表放入缓存中，耗时："+(end-begin));
	}
	
//	/**
//	 * 运营首页
//	 * @param req	从客户端获取到的json数据字符窜
//	 * @return
//	 */
//	public String index(String reqs,final Long uid){
////		LOG.info("客户端json数据："+reqs);
//		Map<String, Object> req =null;
//		Map<String, Object> dataq=null;
//		Map<String, Object> datas =null;
//		String resString="";//返回数据
//		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//		String error="";
//		String type = "";
//		int flag_int = 0;
//		//去掉空格
//		reqs = reqs.trim();
//		List<Map<String, String>> idList = new ArrayList<Map<String,String>>();
//		
//		//转化成可读类型
//		try {
//			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			req = (Map<String, Object>) req.get("req");
//			dataq = (Map<String, Object>) req.get("data");
//			
//			idList = (List<Map<String, String>>) dataq.get("idList");
//			try {
//				type = (String) dataq.get("type");
//			} catch (Exception e) {
//				type = CommentUtils.TYPE_ARTICLE;
//			}
//			if(type == null ){
//				type = CommentUtils.TYPE_ARTICLE;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		System.out.println(req);
//		
//		
//		
//		
//		List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();
//		
////		if(id == UNID){
////			id = null;
////		}
//		
//		
//		long begin = System.currentTimeMillis();
//		List<Object> objects = new ArrayList<Object>();
//		if(idList != null && idList.size()==0){
//			//获取推荐给用户的资源
//			objects = getListFromSearch(uid,type);
//		}else if(idList != null && idList.size()>0){
//			Iterator<Map<String, String>> iter = idList.iterator();
//			while(iter.hasNext()){
//				Map<String, String> map = iter.next();
//				Long id = Long.valueOf(map.get("id"));
//				String resType = map.get("type");
//				if(CommentUtils.TYPE_BOOKLIST.equals(resType)){
//					BookList bookList = getResourceInfoFacade.queryByIdBookList(id);
//					if(bookList.getId() != 0){
//						objects.add(bookList);
//					}
//				}else if(CommentUtils.TYPE_MOVIELIST.equals(resType)){
//					MovieList movieList = myMovieFacade.findMovieListById(id);
//					if(movieList.getId() != 0){
//						objects.add(movieList);
//					}
//				}else if(CommentUtils.TYPE_ARTICLE.equals(resType)){
//					Post post = postFacade.queryByIdName(id);
//					if(post.getId() != 0){
//						objects.add(post);
//					}
//				}
//			}
//		}
//		
//		
//		//如果查出来是空，进行查库
//		if(null == resoureList  || resoureList.size()==0){
//			
//			//查询全部信息
////			resoureList = getResoureListByPage(null,id,uid,resoureList);
//			
//			resoureList = getResponseList(objects, uid, resoureList);
//		}
//		
//		
//		//对资源进行倒序排列
////		Collections.sort(resoureList);
//		
//		if(resoureList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
//			resoureList = resoureList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
//		}
//		
//		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
//		if(resoureList.size()>0){
//			Iterator<ResourceInfo> iter = resoureList.iterator();
//			int size =1;
//			while(size >0){
//				size = resoureList.size();
//				int index = (int)(Math.random()*size);
//				ResourceInfo ri = resoureList.get(index);
//				if(ri != null && ri.getRid() != 0){
//					resourceInfos.add(ri);
//					resoureList.remove(ri);
//				}
//				size = resoureList.size();
////				if(size == 0){
////					break;
////				}
//			}
//		}
//		
//		//每条信息截取100字
//		actUtils.subStringResourceListContent(resourceInfos, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
//		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("list", resourceInfos);
//		}else{
//			flag = CommentUtils.RES_FLAG_ERROR;
//			error = MessageUtils.getResultMessage(flagint);
//			LOG.error("错误代号:"+flagint+",错误信息:"+error);
//			datas.put("error", error);
//		}
//		datas.put("flag", flag);
//		//处理返回数据
//		resString = getResponseData(datas);
////		System.out.println(resString);
//		
//		long end = System.currentTimeMillis();
////		System.out.println("调用首页所需时间"+(end - begin));
////		LOG.info("调用首页所需时间"+(end - begin));
//		return resString;
//	}
	
	/**
	 *
	 * <p>Title: setUserDynamicResourcesToJedis</p>
	 * <p>Description: 将信息同步到个人动态</p>
	 * @author :changjiang
	 * date 2015-5-26 下午4:37:21
	 * @param resourceInfos
	 * @param uid
	 */
	public void setUserDynamicResourcesToJedis(ResourceInfo resourceInfo,Long uid){
		//long begin = System.currentTimeMillis();
			if(resourceInfo != null && resourceInfo.getRid() != 0){
				long fuid = uid;
				long frid = resourceInfo.getRid();
				String StrId = momentJedisManager.saveOneDynamic(fuid, frid,0l);
						//saveOneItem(fuid, frid,0l);
			}
	}

	/**
	 * 获取推荐给用户的资源
	 * @param uid
	 * @return
	 */
	public List<Object> getListFromSearch(Long uid,String type){
		String[] tags = {};
		try {

			List<UserTagJedis> list = jedisManager.takeUserTag(uid, "段子");
			StringBuffer sb = new StringBuffer();
	    	if(list != null && list.size()>0){
	    		Iterator<UserTagJedis> iter = list.iterator();
	    		while(iter.hasNext()){
	    			sb.append(iter.next().getTag()+",");

	    		}
	    		sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",")+",".length(), "");
	    	}
			tags = sb.toString().split(",");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}


		int artCount = 10;// (int)(Math.random()*5 +1);
		int bCount = 10;//(int)(Math.random()*5+1);
		int mCount = 10;//10 - artCount - bCount;

		if(type == null){
			artCount = 4;
			bCount = 3;
			mCount = 3;
		}
		Map<String, String> map = jedisManager.takeIndexCount(uid, 0, 0, 0);

		int artIndex = Integer.valueOf(map.get(JedisManager.ARTICLE_INDEX));
		int bookListIndex = Integer.valueOf(map.get(JedisManager.BOOKLIST_INDEX));
		int movieListIndex = Integer.valueOf(map.get(JedisManager.MOVIELIST_INDEX));



		List<Object> objects = new ArrayList<Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		int count = 0;
		Long total = 0l;
		if(CommentUtils.TYPE_BOOKLIST.equals(type) || type == null){
			//获取书单的推荐数据
			List<BookList> bookLists = new ArrayList<BookList>();
			String[] bookType = {"booklist"};
			json = searchApiManager.userRecByTags(bookType, tags, bookListIndex, bCount);
//		Map<String, Object> json = searchApiManager.userRecByTags(bookType, tags, 0, bCount);

			if(json != null){
				count = (Integer) json.get("count");
				//清零
				total = (Long) json.get("total");
//			System.out.println("推荐书单的总数："+total);
				if(bookListIndex>=total){
					jedisManager.clearSomeIndexCount(uid, JedisManager.BOOKLIST_INDEX);
				}
				//转换格式
				if(count>0){
					bookLists = bookUtils.putJsonListToBookLists((List<Map<String, Object>>) json.get("list"));
					LOG.info("向用户推荐的书单个数："+bookLists.size());
				}
				objects.addAll(bookLists);
			}
			jedisManager.takeIndexCount(uid, 0, bCount, 0);
		}

		if(CommentUtils.TYPE_MOVIELIST.equals(type) || type == null){
			//获取影单的推荐数据
			List<MovieList> movieLists = new ArrayList<MovieList>();
			String[] movieType = {"movielist"};
			json = searchApiManager.userRecByTags(movieType, tags, movieListIndex, mCount);
//		json = searchApiManager.userRecByTags(movieType, tags, 0, mCount);
			if(json != null){
				count = (Integer) json.get("count");
				//清零
				total = (Long) json.get("total");
//			System.out.println("推荐影单的总数："+total);
				if(movieListIndex>=total){
					jedisManager.clearSomeIndexCount(uid, JedisManager.MOVIELIST_INDEX);
				}
				//转换格式
				if(count>0){
					movieLists = movieUtils.putJsonListToMovieLists((List<Map<String, Object>>) json.get("list"));
					LOG.info("向用户推荐的影单个数："+movieLists.size());
				}
				objects.addAll(movieLists);
			}
			jedisManager.takeIndexCount(uid, 0, 0, mCount);
		}

		if(CommentUtils.TYPE_ARTICLE.equals(type) || type == null){

			//获取长文章的推荐数据
			List<Post> posts = new ArrayList<Post>();
			String[] postType = {"post"};
//		json = searchApiManager.userRecByTags(postType, tags, 0, artCount);
			json = searchApiManager.userRecByTags(postType, tags, artIndex, artCount);
			if(json != null){
				count = (Integer) json.get("count");
				//清零
				total = (Long) json.get("total");
//			System.out.println("推荐长文章的总数："+total);
				if(artIndex>=total){
					jedisManager.clearSomeIndexCount(uid, JedisManager.ARTICLE_INDEX);
				}
				//转换格式
				if(count>0){
					posts = postUtils.putJsonListToPostList((List<Map<String, Object>>) json.get("list"));
					LOG.info("向用户推荐的长文章个数："+posts.size());

				}

				objects.addAll(posts);
			}
			jedisManager.takeIndexCount(uid, artCount, 0, 0);
		}

		/*if(CommentUtils.TYPE_NEWARTICLE.equals(type) || type == null){

			//获取长文章的推荐数据
			List<Article> articles = new ArrayList<Article>();
			String[] articleType = {"article"};
//		json = searchApiManager.userRecByTags(postType, tags, 0, artCount);
			json = searchApiManager.userRecByTags(articleType, tags, artIndex, artCount);
			if(json != null){
				count = (Integer) json.get("count");
				//清零
				total = (Long) json.get("total");
//			System.out.println("推荐长文章的总数："+total);
				if(artIndex>=total){
					jedisManager.clearSomeIndexCount(uid, JedisManager.ARTICLE_INDEX);
				}
				//转换格式
				if(count>0){
					articles = articleUtils.putJsonListToArticleList((List<Map<String, Object>>) json.get("list"));
					LOG.info("向用户推荐的长文章个数："+articles.size());

				}

				objects.addAll(articles);
			}
			jedisManager.takeIndexCount(uid, artCount, 0, 0);
		}*/

		return objects;
	}
	
	/**
	 * 猜你喜欢
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String guessYouLike(String reqs,final Long uid){
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

		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);


		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");

		String type = (String) dataq.get("type");

//		if(CommentUtils.TYPE_BOOK.equals(type)){
//			type = JedisManager.BKCOMMENT_INDEX;
//		}else if(CommentUtils.TYPE_MOVIE.equals(type)){
//			type = JedisManager.MVCOMMENT_INDEX;
//		}


		List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();


		//long begin = System.currentTimeMillis();
//		List<Object> objects = new ArrayList<Object>();
//			//获取推荐给用户的资源
//		objects = getListFromGuess(uid,type);


		List<Object> objList = new ArrayList<Object>();
//		Iterator<Object> iter = objects.iterator();
//		while(iter.hasNext()){
//			if(JedisManager.BKCOMMENT_INDEX.equals(type)){
//				BkComment comment = (BkComment) iter.next();
//				BkInfo bkInfo = bkFacade.findBkInfo(comment.getBookId());
//				BookInfo bookInfo = bookUtils.putBKToBookInfo(bkInfo, 0);
//				objList.add(bookInfo);
//			}else if(JedisManager.MVCOMMENT_INDEX.equals(type)){
//				MvComment mvComment = (MvComment) iter.next();
//				MvInfo mvInfo = mvFacade.queryById(mvComment.getMovieId());
//				MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, 0);
//				objList.add(movieInfo);
//			}
//		}

		if(CommentUtils.TYPE_BOOK.equals(type)){
			for (int i = 0; i < 4; i++) {
				BkInfo bkInfo = bkFacade.findBkInfo((int)(Math.random()*280000));
				if(bkInfo.getId() == 0){
					i--;
				}else{
					BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, 0);
					objList.add(bookInfo);
				}
			}
		}else if(CommentUtils.TYPE_MOVIE.equals(type)){
			for (int i = 0; i < 4; i++) {
				MvInfo mvInfo = mvFacade.queryById((int)(Math.random()*4100));
				if(mvInfo.getId() == 0){
					i--;
				}else{
					MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, 0);
					objList.add(movieInfo);
				}
			}
		}


		//如果查出来是空，进行查库
		if(resoureList != null && resoureList.size()==0){

			//查询全部信息

			resoureList = getResponseList(objList, uid, resoureList);
		}


		//对资源进行倒序排列
		Collections.sort(resoureList);

		if(resoureList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
			resoureList = resoureList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
		}
		//每条信息截取100字
		actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resoureList);
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

		long end = System.currentTimeMillis();
//		System.out.println("调用首页所需时间"+(end - begin));
//		LOG.info("调用首页所需时间"+(end - begin));
		return resString;
	}
	
	//TODO 朋友圈

	/**
	 * 获取推荐给用户的资源
	 * @param uid
	 * @return
	 */
	private List<Object> getListFromGuess(Long uid,String type){
		String[] tags = {};
		try {

			List<UserTagJedis> list = jedisManager.takeUserTag(uid, "");
			StringBuffer sb = new StringBuffer();
			if(list != null && list.size()>0){
				Iterator<UserTagJedis> iter = list.iterator();
				while(iter.hasNext()){
					sb.append(iter.next().getTag()+",");

				}
				sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",")+",".length(), "");
			}
			tags = sb.toString().split(",");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}



		Map<String, String> map = jedisManager.likeIndexCount(uid, 0, type);
		List<Object> objects = new ArrayList<Object>();

		int num = 4;
		Integer index = 0;
		int count = 0;
		Long total = 0l;
		Map<String, Object> json = new HashMap<String, Object>();
		if(JedisManager.BKCOMMENT_INDEX.equals(type)){
			index = Integer.valueOf(map.get(JedisManager.BKCOMMENT_INDEX));
			//获取书评的推荐数据
			List<BkComment> bkComments = new ArrayList<BkComment>();
			String[] types = {"bookcomment"};
			json = searchApiManager.userRecByTags(types, tags, index, num);
//		Map<String, Object> json = searchApiManager.userRecByTags(bookType, tags, 0, bCount);

			if(json != null){
				count = (Integer) json.get("count");
				//清零
				total = (Long) json.get("total");
//				System.out.println("推荐书单的总数："+total);
				if(index>=total){
					jedisManager.clearSomeIndexCount(uid, type);
				}
				//转换格式
				if(count>0){
					bkComments = bookUtils.putJsonListToBkComments((List<Map<String, Object>>) json.get("list"));
					LOG.info("向用户推荐的书评个数："+bkComments.size());
				}
				objects.addAll(bkComments);
			}
		}else if(JedisManager.MVCOMMENT_INDEX.equals(type)){
			index = Integer.valueOf(map.get(JedisManager.MVCOMMENT_INDEX));
			//获取影单的推荐数据
			List<MvComment> mvComments = new ArrayList<MvComment>();
			String[] types = {"moviecomment"};
			json = searchApiManager.userRecByTags(types, tags, index, num);
//		json = searchApiManager.userRecByTags(movieType, tags, 0, mCount);
			if(json != null){
				count = (Integer) json.get("count");
				//清零
				total = (Long) json.get("total");
//				System.out.println("推荐影评的总数："+total);
				if(index>=total){
					jedisManager.clearSomeIndexCount(uid, type);
				}
				//转换格式
				if(count>0){
					mvComments = movieUtils.putJsonListToMvComments((List<Map<String, Object>>) json.get("list"));
					LOG.info("向用户推荐的影单个数："+mvComments.size());
				}
				objects.addAll(mvComments);
			}
		}

		jedisManager.likeIndexCount(uid, num, type);

		return objects;
	}
	
	/**
	 * TODO 朋友圈
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	@SuppressWarnings("unused")
	public String circleOfFriends(String reqs,final Long uid){
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
		int theflag = 0;//区分是否显示长影评长书评
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
		String flagStr = (String) dataq.get("flag");
		if(StringUtils.isInteger(flagStr)){
			theflag = Integer.valueOf(flagStr);
		}
		List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();

		if(id == UNID){
			id = null;
		}


		//long begin = System.currentTimeMillis();
		//long end = System.currentTimeMillis();
//		System.out.println("缓存获取朋友圈的时间:"+(end-begin));
		userJedisManager.saveOneUserInfo(uid, JedisConstant.USER_HASH_MOMENT_NOTICE, "0");
		//从缓存中取出朋友圈数据
		List<String> list =  momentJedisManager.getOneUserMoment(uid,id,theflag);
		if(list != null && list.size()>0){
//			list = getResourceListByJedis(list,uid);
//			System.out.println("最终结果:"+list.toString());

			resString = RES_DATA_RIGHT_BEGIN+"\"list\":"+list.toString()+RES_DATA_RIGHT_END;
		}else{
			//begin = System.currentTimeMillis();
			//只查询朋友发的信息
			List<Long> idList = ucenterFacade.findUserAttentionList(uid, 0, CommentUtils.PAGE_SIZE);
			//如果查出来是空，进行查库
			if(resoureList != null && resoureList.size()==0){
				//只查询朋友发的信息
				idList.add(uid);
				resoureList = getFriendNewResoureListNew(idList, id,uid,resoureList);
				//查询全部信息
//			resoureList = getResoureListByPage(null,id,uid,resoureList);
			}
			/*end = System.currentTimeMillis();
			System.out.println("库中获取朋友圈的时间:"+(end-begin));*/



			//对资源进行倒序排列
//		begin = System.currentTimeMillis();
			Collections.sort(resoureList);
//		end = System.currentTimeMillis();
//		System.out.println("排序的时间:"+(end-begin));
			if(resoureList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
				resoureList = resoureList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
			}
			//每条信息截取100字
		//begin = System.currentTimeMillis();
			actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
		//end = System.currentTimeMillis();
		//System.out.println("截取100字的时间:"+(end-begin));
			//资源列表放入缓存中
			setcircleOfFriendsResourcesToJedis(resoureList, uid);
			//setResourcesToJedis(resoureList, uid);
			datas = new HashMap<String, Object>();
			if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				datas.put("list", resoureList);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
		}
//		System.out.println(resString);
		return resString;
	}
	/**
	 * 首页连载列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
//	public String indexSerialize(String reqs,Long uid){
////		LOG.info("客户端json数据："+reqs);
//		Map<String, Object> req =null;
//		Map<String, Object> dataq=null;
//		Map<String, Object> datas =null;
//		String resString="";//返回数据
//		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//		String error="";
//		int flag_int = 0;
//		//去掉空格
//		reqs = reqs.trim();
//		
//		//转化成可读类型
//		try {
//			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(req);
//		
//		
//		req = (Map<String, Object>) req.get("req");
//		dataq = (Map<String, Object>) req.get("data");
//		
//		Long id = Long.valueOf(dataq.get("id").toString());
//		List<BookList> bookList = new ArrayList<BookList>();
//		List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();
//		
//		
//		if(id == CommentUtils.UN_ID){
//			id = null;
//		}
//		resoureList = getResoureListByPage(CommentUtils.TYPE_PUSH,id,uid);
//		
//		//对资源进行倒序排列
//		Collections.sort(resoureList);
//		
//		if(resoureList.size()>15){
//			resoureList = resoureList.subList(0, RESOURCE_PAGE_SIZE);
//		}
//		
//		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("list", resoureList);
//		}else{
//			flag = CommentUtils.RES_FLAG_ERROR;
//			LOG.error(flagint);
//			error = MessageUtils.getResultMessage(flagint);
//			datas.put("error", error);
//		}
//		datas.put("flag", flag);
//		//处理返回数据
//		resString = getResponseData(datas);
//		
//		return resString;
//	}
	/**
	 * 首页书相关
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
//	public String indexBook(String reqs,Long uid){
////		LOG.info("客户端json数据："+reqs);
//		Map<String, Object> req =null;
//		Map<String, Object> dataq=null;
//		Map<String, Object> datas =null;
//		String resString="";//返回数据
//		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//		String error="";
//		int flag_int = 0;
//		//去掉空格
//		reqs = reqs.trim();
//		
//		//转化成可读类型
//		try {
//			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		System.out.println(req);/
//		
//		
//		req = (Map<String, Object>) req.get("req");
//		dataq = (Map<String, Object>) req.get("data");
//		
//		Long id = Long.valueOf(dataq.get("id").toString());
//		bookList = new ArrayList<BookList>();
//		resoureList = new ArrayList<ResourceInfo>();
//		if(id == CommentUtils.UN_ID){
//			id = null;
//		}
//		resoureList = getResoureListByPage(CommentUtils.TYPE_BOOKLIST,id,uid);
//		
//		//对资源进行倒序排列
//		Collections.sort(resoureList);
//		
//		if(resoureList.size()>15){
//			resoureList = resoureList.subList(0, RESOURCE_PAGE_SIZE);
//		}
//		
//		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("list", resoureList);
//		}else{
//			error = MessageUtils.getResultMessage(flagint);
//			datas.put("error", error);
//		}
//		datas.put("flag", flag);
//		//处理返回数据
//		resString = getResponseData(datas);
//		
//		return resString;
//	}
	/**
	 * 首页电影相关
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
//	public String indexMovie(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
//		int flag_int = 0;
//		//去掉空格
//		reqs = reqs.trim();
//		
//		//转化成可读类型
//		try {
//			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(req);
//		
//		
//		req = (Map<String, Object>) req.get("req");
//		dataq = (Map<String, Object>) req.get("data");
//		
//		id = Long.valueOf(dataq.get("id").toString());
//		resoureList = new ArrayList<ResourceInfo>();
//		if(id == CommentUtils.UN_ID){
//			id = null;
//		}
//		resoureList = getResoureListByPage(CommentUtils.TYPE_MOVIELIST,id,uid);
//		
//		//对资源进行倒序排列
//		Collections.sort(resoureList);
//		
//		if(resoureList.size()>15){
//			resoureList = resoureList.subList(0, RESOURCE_PAGE_SIZE);
//		}
//		
//		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("list", resoureList);
//		}else{
//			error = MessageUtils.getResultMessage(flagint);
//			datas.put("error", error);
//		}
//		datas.put("flag", flag);
//		//处理返回数据
//		resString = getResponseData(datas);
//		
//		return resString;
//	}

	/**
	 * 对从缓存中取出的资源列表遍历
	 * @param list
	 * @return
	 */
	public List<String> getResourceListByJedis(List<String> list,Long uid){
		long begin = System.currentTimeMillis();
		List<String> strings = new ArrayList<String>();

		Iterator<String> iter = list.iterator();
		while(iter.hasNext()){
			String item = iter.next();
			try {
				Map<String, Object> map = getObjectMapper().readValue(item,  new TypeReference<Map<String, Object>>(){});

				ResourceInfo ri = new ResourceInfo();
				Long rid = Long.valueOf(map.get("rid").toString());
				String mType = (String) map.get("type");
				Long mRid = rid;
				if(CommentUtils.TYPE_BOOKLIST.equals(mType)){
					mRid = Long.valueOf(map.get("bookListId").toString());
				}else if(CommentUtils.TYPE_MOVIELIST.equals(mType)){
					mRid = Long.valueOf(map.get("movieListId").toString());
				}
				ri = getResourceById(mRid, mType, uid,0);
//				ri = actUtils.putIsCollectToResoure(ri, uid, actFacade, 0);
				map.put("isCollect", ri.getIsCollect());
				map.put("isPraise", ri.getIsPraise());
				map.put("zNum", ri.getzNum());
				map.put("cNum", ri.getcNum());
				if(CommentUtils.TYPE_BOOK_COMMENT.equals(mType)){
					int inList = 1;
					if(ri.getRid() == 0){
						inList =1;
					}else{
						inList = ri.getBookInfo().getInList();

					}
					map.put("inList", inList);
				}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(mType)){
					int inList = 1;
					if(ri.getRid() == 0){
						inList =1;
					}else{
						inList = ri.getMovieInfo().getInList();

					}
					map.put("inList", inList);
				}
				String json = getObjectMapper().writeValueAsString(map);
				strings.add(json);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		/*long end = System.currentTimeMillis();
		System.out.println("将缓存信息添加附加信息的总时间"+(end-begin));*/
		return strings;
	}
//	/**
//	 * 首页清单
//	 * @param req	从客户端获取到的json数据字符窜
//	 * @return
//	 */
//	public String detailedList(String reqs){
//		LOG.info("客户端json数据："+reqs);
//		int flag_int = 0;
//		//去掉空格
//		reqs = reqs.trim();
//		
//		//转化成可读类型
//		try {
//			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(req);
//		
//		
//		req = (Map<String, Object>) req.get("req");
//		dataq = (Map<String, Object>) req.get("data");
//		
//		id = Long.valueOf(dataq.get("id").toString());
//		bookList = new ArrayList<BookList>();
//		resoureList = new ArrayList<ResourceInfo>();
//		if(id == CommentUtils.UN_ID){
//			//首页新内容
//			resoureList = getDetailedListNewResoureList();
//		}else{
//			//翻页
//			resoureList = getDetailedListResoureListByPage(id);
//		}
//		
//		
//		//对资源进行倒序排列
//		Collections.sort(resoureList);
//		
//		if(resoureList.size()>15){
//			resoureList = resoureList.subList(0, RESOURCE_PAGE_SIZE);
//		}
//		
//		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("list", resoureList);
//		}else{
//			error = MessageUtils.getResultMessage(flagint);
//			datas.put("error", error);
//		}
//		datas.put("flag", flag);
//		//处理返回数据
//		resString = getResponseData(datas);
//		
//		return resString;
//	}
//	/**
//	 * 首页帖子列表
//	 * @param req	从客户端获取到的json数据字符窜
//	 * @return
//	 */
//	public String indexPostList(String reqs){
//		LOG.info("客户端json数据："+reqs);
//		int flag_int = 0;
//		//去掉空格
//		reqs = reqs.trim();
//		
//		//转化成可读类型
//		try {
//			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(req);
//		
//		
//		req = (Map<String, Object>) req.get("req");
//		dataq = (Map<String, Object>) req.get("data");
//		
//		id = Long.valueOf(dataq.get("id").toString());
//		resoureList = new ArrayList<ResourceInfo>();
//		if(id == CommentUtils.UN_ID){
//			//首页新内容
//			resoureList = getIndexPostListNewResoureList();
//		}else{
//			//翻页
//			resoureList = getIndexPostListByPage(id);
//		}
//		
//		//对资源进行倒序排列
//		Collections.sort(resoureList);
//		
//		if(resoureList.size()>15){
//			resoureList = resoureList.subList(0, RESOURCE_PAGE_SIZE);
//		}
//		
//		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("list", resoureList);
//		}else{
//			error = MessageUtils.getResultMessage(flagint);
//			datas.put("error", error);
//		}
//		datas.put("flag", flag);
//		//处理返回数据
//		resString = getResponseData(datas);
//		
//		return resString;
//	}
//	/**
//	 * 更新资源信息
//	 * @return
//	 */
//	public List<ResourceInfo> getIndexPostListNewResoureList(){
//		//日记
//		diaryList = diaryFacade.findDiaryListById(UNID);
//		resoureList = getResponseList(diaryList, null, resoureList);
//		//帖子
//		postList = postFacade.findPostListById(UNID);
//		resoureList = getResponseList(postList, null, resoureList);
//		
//		
//		return resoureList;
//	}
//	/**
//	 * 更新资源分页信息
//	 * @return
//	 */
//	public List<ResourceInfo> getIndexPostListByPage(long id ){
//		//日记
//		diaryList = diaryFacade.findDiaryListById(id);
//		resoureList = getResponseList(diaryList, null, resoureList);
//		//帖子
//		postList = postFacade.findPostListById(id);
//		resoureList = getResponseList(postList, null, resoureList);
//		return resoureList;
//	}
//	/**
//	 * 更新资源信息
//	 * @return
//	 */
//	public List<ResourceInfo> getDetailedListNewResoureList(){
//		//书单
////		bookList = getResourceInfoFacade.findLatestBookListByDate();
////		resoureList = getResponseList(bookList, CommentUtils.TYPE_BOOKLIST, resoureList);
//////		resoureList = WebUtils.getResponseListByType(bookList, resoureList,flag_int, ucenterFacade, actFacade);
////		System.out.println(flagint);
////		//影单
////		filmList = filmListInfoFacade.findFilmListByDate();
////		resoureList = getResponseList(filmList, null, resoureList);
//		
//		
//		return resoureList;
//	}
//	/**
//	 * 更新资源分页信息
//	 * @return
//	 */
//	public List<ResourceInfo> getDetailedListResoureListByPage(long id ){
//		//书单
////		bookList = getResourceInfoFacade.findLatestBookListById(id);
//////		resoureList = WebUtils.getResponseListByType(bookList, resoureList, flag_int ,ucenterFacade, actFacade);
////		resoureList = getResponseList(bookList, CommentUtils.TYPE_BOOKLIST, resoureList);
////		//影单
////		filmList = filmListInfoFacade.findFilmListById(id);
////		resoureList = getResponseList(filmList, null, resoureList);
//		return resoureList;
//	}

	/**
	 * 我的页面@模块
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String actUserList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		Long lastId = null;
		//去掉空格
		reqs = reqs.trim();

		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			lastId = Long.valueOf(dataq.get("lastId").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);

		if(lastId == UNID){
			lastId = null;
		}

		List<MsgAt> msgAts = msgFacade.findUserAtList(uid, lastId);

		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();

		resourceInfos = getActResponseList(msgAts,uid, resourceInfos);

		//对资源进行倒序排列
//		Collections.sort(resoureList);

//		if(resoureList.size()>15){
//			resoureList = resoureList.subList(0, RESOURCE_PAGE_SIZE);
//		}

		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
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
	 * 单条数据
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String oneResource(String reqs,Long uid){
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
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);

		ResourceInfo resourceInfo = new ResourceInfo();

		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");

		id = Long.valueOf((String)dataq.get("id"));
		type = (String) dataq.get("type");



		//推送类型
//		if(CommentUtils.TYPE_BOOKLIST.equals(type)
//				|| CommentUtils.TYPE_PUSH.equals(type)
//				||CommentUtils.TYPE_PUSH.equals(type)){
//			ActPublish actPublish = actFacade.findOnePublish(id);
//			String pType = actPublish.getType();
//			//判断推送的类型
//			if(CommentUtils.TYPE_BOOKLIST.equals(pType)){
//				resourceInfo = bookUtils.putActPublishBookListToResource(actPublish, getResourceInfoFacade, ucenterFacade, actFacade, bkFacade, uid);
//			}else if(CommentUtils.TYPE_PUSH.equals(pType)){
//				resourceInfo = fileUtils.putActPulishToResource(actPublish, ucenterFacade, serializeFacade, actFacade);
//			}else if(CommentUtils.TYPE_MOVIELIST.equals(pType)){
//				resourceInfo = movieUtils.putActPublishMovieListToResource(actPublish, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
//			}
//		}

//		Object obj = new Object();
//		//转发的资源
//		if(CommentUtils.TYPE_ACT.equals(type)){
//			ActTransmit actTransmit = actFacade.findOneTransmit(id);
//			obj = actTransmit;
//		}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
//			//书评
//			BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
//			obj = bkComment;
////			resourceInfo= fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,uid);
//		}
//		else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
//			MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);
//			obj = mvComment;
////			resourceInfo = movieUtils.putMVCommentToResource(mvComment, ucenterFacade,actFacade, mvFacade,myMovieFacade,uid);
//		}else if(CommentUtils.TYPE_DIARY.equals(type)){
//			Diary diary = diaryFacade.queryByIdDiary(id);
//			obj = diary;
////			resourceInfo = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
//		}else if(CommentUtils.TYPE_ARTICLE.equals(type)){
//			Post post = postFacade.queryByIdName(id);
//			obj = post;
//		}


//		resourceInfo = putObjectToResource(obj, uid);
		resourceInfo = getResourceById(id, type,uid,1);
		if(resourceInfo != null && resourceInfo.getRid() != 0){
			flagint = ResultUtils.SUCCESS;
		}

//		actUtils.putIsCollectToResoure(resourceInfo, uid, actFacade);
//		try {
//			actUtils.putMoneyToResource(resourceInfo, paycenterFacade);
//		} catch (Exception e) {
//			LOG.error("添加资源获取打赏时出错，资源id为:"+resourceInfo.getRid()+e.getMessage(), e.fillInStackTrace());
//		}

		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//添加附加信息
			updateInListByJedis(id, uid, JedisConstant.RELATION_IS_INLIST, resourceInfo.getInList()+"");
			updateIsPraiseByJedis(id, uid, JedisConstant.RELATION_IS_PRAISE, resourceInfo.getIsPraise()+"");
			updateResourceByJedis(id, uid, JedisConstant.RELATION_PRAISE_NUM, resourceInfo.getzNum()+"");
			updateResourceByJedis(id, uid, JedisConstant.RELATION_COMMENT_NUM, resourceInfo.getcNum()+"");

			//增加用户是否看过这个资源
			relationToUserAndResManager.saveOneRelation(uid, id, JedisConstant.RELATION_IS_READ, "1");
			//添加阅读量，阅读量增加
			if(46==uid){
				resStatJedisManager.addReadNum(id, type,0,"",1);
			}else{
				resStatJedisManager.addReadNum(id, type,0,"",0);
			}
			//slong readNum=resStatJedisManager.addReadNum(id, type);
			//System.out.println("阅读数为"+readNum);
					//addReadNum(id, type);
			//resourceInfo.setReadingCount(readNum+"");
			datas.put("resourceInfo", resourceInfo);
			/*Map<String, Object> map = new HashMap<String, Object>();
			if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){
				map = bkCommentFacade.findBkCommentCount(uid);
				datas.put("readTotal", map.get("count")+"");
			}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){
				map = mvCommentFacade.findMvCommentCountByUid(uid);
				datas.put("readTotal", map.get("count")+"");
			}*/
			/*if(flagint == ResultUtils.SUCCESS){
				long readNum=resStatJedisManager.addReadNum(id, type);
				resourceInfo.setReadingCount((int)readNum);
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
	 * @Title: getResourceById
	 * @Description: 根据id和type查询各种资源详情
	 * @author 温晓宁
	 * @date 2015-3-10
	 * @param @param id 资源id
	 * @param @param type 资源type
	 * @param @param uid 当前用户id
	 * @param @param isList 当遍历查询资源时传：0，当查询单个资源详情的时候传：1
	 * @param @return
	 * @return ResourceInfo
	 * @throws
	 */
	public ResourceInfo getResourceById(long id,String type,long uid,int isList){
		Object obj = new Object();
		ResourceInfo resourceInfo = new ResourceInfo();
		if(id == 0 || "-1".equals(type)){
			return resourceInfo;
		}
		//转发的资源
		if(CommentUtils.TYPE_ACT.equals(type)){
			ActTransmit actTransmit = actFacade.findOneTransmit(id);
			obj = actTransmit;
		}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)||CommentUtils.TYPE_ARTICLE_BOOK.equals(type) || CommentUtils.TYPE_BOOK_DIGEST.equals(type)){
			//书评
			BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
			obj = bkComment;
			//加上查看书评的日志
			if(null!=bkComment){
				long bkCommentId = bkComment.getId();
				long bkUserId = bkComment.getUserId();
				int bookId = bkComment.getBookId();
				//图书还是小说
				String bookType = bkComment.getResType();
				//长评还是短评
				String resourceType = bkComment.getResourceType();
				BussinessLogUtils.viewBkCommentLog(bkCommentId,bookId,bkUserId,bookType,resourceType);
			}
//			resourceInfo= fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,uid);
		}
		else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)||CommentUtils.TYPE_ARTICLE_MOVIE.equals(type)){
			MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);
			obj = mvComment;
			//加上查看影评的日志
			if(null!=mvComment&&mvComment.getFlag()==ResultUtils.SUCCESS){
				long mvCommentId = mvComment.getId();
				long mvUserId = mvComment.getUserId();
				long movieId = mvComment.getMovieId();
				//长评还是短评
				String resourceType = mvComment.getResourceType();
				BussinessLogUtils.viewMvCommentLog(mvCommentId,movieId,mvUserId,resourceType);
			}
//			resourceInfo = movieUtils.putMVCommentToResource(mvComment, ucenterFacade,actFacade, mvFacade,myMovieFacade,uid);
		}else if(CommentUtils.TYPE_DIARY.equals(type)){
			Diary diary = diaryFacade.queryByIdDiary(id);
			obj = diary;
			//加上查看文字的日志
			if(null!=diary&&diary.getFlag()==ResultUtils.SUCCESS){
				long diaryId = diary.getId();
				long diaryUserId = diary.getUid();
				//长评还是短评
				String resourceType = diary.getType();
				BussinessLogUtils.viewDiaryLog(diaryId,diaryUserId,resourceType);
			}
//			resourceInfo = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
		}else if(CommentUtils.TYPE_ARTICLE.equals(type)){
			Post post = postFacade.queryByIdName(id);
			obj = post;
		}else if(CommentUtils.TYPE_NEWARTICLE.equals(type)){
			Article article = articleFacade.queryArticleById(id);
			obj = article;
		}else if(CommentUtils.TYPE_BOOKLIST.equals(type)){
			BookList bookList = getResourceInfoFacade.queryByIdBookList(id);
			obj = bookList;
		}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){
			MovieList movieList  = myMovieFacade.findMovieListById(id);
			obj = movieList;
		}


		resourceInfo = putObjectToResource(obj, uid,isList);

		if(null==resourceInfo||null==resourceInfo.getUserEntity()){
			resourceInfo = new ResourceInfo();
			return resourceInfo;
		}
		//添加当前用户对这个人是否关注
		long resUid = resourceInfo.getUserEntity().getId();
		if(0!=resUid){
			Map<String,String> userRelationMap =  userJedisManager.getRelationUserAttentionInfo(uid, resUid);
			if(null!=userRelationMap&&!userRelationMap.isEmpty()){
				String jedisIsInterest = userRelationMap.get(JedisConstant.RELATION_USER_ISINTEREST);
				if(null==jedisIsInterest||"".equals(jedisIsInterest)){
					jedisIsInterest = "2";
				}
				resourceInfo.getUserEntity().setIsInterest(Integer.valueOf(jedisIsInterest));
			}
		}
		return resourceInfo;
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


		if(CommentUtils.TYPE_DIARY.equals(type)){
			Diary diary = diaryFacade.deleteById(id);
			flagint = diary.getFlag();
		}else if(CommentUtils.TYPE_BOOKLIST.equals(type)
				|| CommentUtils.TYPE_MOVIELIST.equals(type)){
			ActPublish actPublish = actFacade.cancelPublish(id);
			flagint = actPublish.getFlag();
		}else if(CommentUtils.TYPE_COMMENT.equals(type)){
			ActComment actComment = actFacade.deleteComment(null, id);
			flagint = actComment.getFlag();
		}
		if(flagint == ResultUtils.SUCCESS){
			//System.out.println("是否执行删除缓存操作"+flagint);
			if(!CommentUtils.TYPE_COMMENT.equals(type)){
				delResourceFromJedis(uid, id, type);
				//删除话题相关
				topicFacade.deleteTopicLinkByResid(id, uid);
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
	
	/**
	 * 朋友动态
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String friendDynamic(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =new HashMap<String, Object>();
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
		}
//		System.out.println(req);

		long start = System.currentTimeMillis();
		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");

		Long friendId = Long.valueOf(dataq.get("friendId").toString());
		Long id = Long.valueOf(dataq.get("id").toString());
		List<BookList> bookList = new ArrayList<BookList>();
		String resType = (String)dataq.get("resType");
		if(null==resType){
			resType = "-1";
		}

		//消除朋友圈的提醒数量
		if(uid.equals(friendId)){
			userJedisManager.saveOneUserInfo(uid, JedisConstant.USER_HASH_MOMENT_NOTICE, "0");
		}

		List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();
		List<Long> idList = new ArrayList<Long>();
		if(friendId == UNID){
			idList = ucenterFacade.findUserAttentionList(uid, 0, CommentUtils.PAGE_SIZE);
		}else{
			idList.add(friendId);
		}

		if(id == CommentUtils.UN_ID){
			id = null;
		}

		//先从缓存中获取数据
		/*try {
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
//			resourceInfos = jedisManager.getJedisResources(idList,uid, id,1);
//			System.out.println("缓存中的数量:"+resourceInfos.size());
//			resoureList = getResponseList(resourceInfos, uid, resoureList);
//			System.out.println("转换后的数量:"+resoureList.size());

		} catch (Exception e) {
			resoureList = new ArrayList<ResourceInfo>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}*/


		//从缓存中取出朋友圈数据
		List<String> list = new ArrayList<String>();
		if("-1".equals(resType)){//获取全部信息
			if(uid.equals(friendId)){
				list = momentJedisManager.getOneUserDynamic(friendId, id);
			}else{
				list = momentJedisManager.getOtherUserDynamic(friendId, uid, id);
			}
		}

		//System.out.println("个人动态中获取的缓存动态数为"+list.size()+"拿到的个人动态类型为"+resType);
		if(list != null && list.size()>9){
//			begin = System.currentTimeMillis();
//			list = getResourceListByJedis(list,uid);
//			end = System.currentTimeMillis();
//			System.out.println("新缓存添加附加信息时间:"+(end-begin));
//					System.out.println("最终结果:"+list.toString());

			resString = RES_DATA_RIGHT_BEGIN+"\"list\":"+list.toString()+RES_DATA_RIGHT_END;
		}else{
//		System.out.println("id="+id);
			int size = 0;
			if(resoureList == null || resoureList.size()== 0){
				if("-1".equals(resType)){
					resoureList = getFriendNewResoureListNew(idList, id,uid,resoureList);
					//System.out.println("个人动态中数据库中获取的数量为"+resoureList.size());
					Iterator<ResourceInfo> iter = resoureList.iterator();
					while(iter.hasNext()){
						ResourceInfo resourceInfo = iter.next();
						//插入个人动态
						setUserDynamicResourcesToJedis(resourceInfo,friendId );
						//setResourceToJedis(resourceInfo, uid,uid,0l);
					}
				}else{
					/*long startTime = System.currentTimeMillis();
					Map<String, Object> sizeMap = new HashMap<String, Object>();
					if(resType.equals(CommentUtils.TYPE_DIARY)){//文字
						sizeMap = diaryFacade.findDiaryCount(friendId);
						size = (Integer) sizeMap.get("count");
					}else if(resType.equals(CommentUtils.TYPE_BOOK_COMMENT)){//书评
						sizeMap = bkCommentFacade.findBkCommentCount(friendId);
						size = (Integer) sizeMap.get("count");
					}else if(resType.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//影评
						sizeMap = mvCommentFacade.findMvCommentCountByUid(friendId);
						size = (Integer) sizeMap.get("count");
					}
					datas.put("size", size+"");*/
					long endTime = System.currentTimeMillis();
					//System.out.println("查询数量用时"+(endTime-startTime));
					//查询朋友的
					list = getUserNewResoureListNew(idList, id, uid, resoureList, resType,friendId);
					resString = RES_DATA_RIGHT_BEGIN+"\"list\":"+list.toString()+RES_DATA_RIGHT_END;
					//resoureList =
					/*long endTime1 = System.currentTimeMillis();
					System.out.println("查询详细信息用时"+(endTime1-endTime));*/
					return resString;
				}
						//getFriendNewResoureListNew(idList, id,uid,resoureList);
//			System.out.println("缓存为空，查出库中的数量:"+resoureList.size());
			}
			//对资源进行倒序排列
			Collections.sort(resoureList);
			if(resoureList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
				resoureList = resoureList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
			}
//		System.out.println("最后的的数量:"+resoureList.size());
			//actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
			//从库中查出后放入缓存中

			if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				datas.put("list", resoureList);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
		}
		//long end = System.currentTimeMillis();
		//end = System.currentTimeMillis();
		//System.out.println("朋友的动态数量耗时："+(end-start));

		return resString;
	}
	
	/**
	 *
	 * <p>Title: searchByLocation</p>
	 * <p>Description: 根据地点查询周围资源</p>
	 * @author :changjiang
	 * date 2015-6-16 下午4:16:28
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String searchByLocation(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =new HashMap<String, Object>();
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
		}
//		System.out.println(req);

		long start = System.currentTimeMillis();
		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");
		//获取经度维度
		String lon = (String) dataq.get("lon");
		String lat = (String)dataq.get("lat");

		//搜索半径
		int radius = 100000;
		int page = (Integer.valueOf((String)dataq.get("page"))) - 1;

		Map<String, Object> map = searchApiManager.eventSearchByLocation(lon, lat, radius, page*10, 10);
		String total = (String)map.get("total").toString();
		flag = (String)map.get("flag");
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("subjects");
		Iterator<Map<String, Object>> itList = list.iterator();
		Map<String, Object> resMap = new HashMap<String, Object>();
		Long resId = 0l;
		String resType = "";
		String resInfoStr = "";
		String distance = "";
		List<String> resinfoList = new ArrayList<String>();
		while(itList.hasNext()){
			resMap = itList.next();
			resId = Long.valueOf(CheckParams.stringToLong((String)resMap.get("id")));
			resType = (String)resMap.get("type");
			distance= (String)resMap.get("distance");
			resInfoStr = resourceManager.getResourceInfoStr(resId, uid, resType,uid);
			if(null!=resInfoStr){
				resInfoStr = resInfoStr.replaceAll("\"distance\": \"?[0-9]+\"", "\"distance\": \""+distance+"\"");
				resinfoList.add(resInfoStr);
			}
		}

		resString = "{\"res\":{\"data\":{\"flag\":\""+flag+"\",\"total\":\""+total+"\",\"list\":"+resinfoList.toString()+"}}}";

		return resString;
	}

	/**
	 * 获取朋友动态新内容
	 * @return
	 */
	public List<ResourceInfo> getFriendNewResoureList(List<Long> idList,Long id,Long uid,List<ResourceInfo> resoureList){
//			List<SortEntity> sortEntities = new ArrayList<SortEntity>();
//			SortEntity sortEntity = new SortEntity();
			//书评
			//long begin = System.currentTimeMillis();
			List<BkComment> bkcommentList = new ArrayList<BkComment>();
			for (long userId : idList) {
				List<BkComment> bkComments = bkCommentFacade.findCommentListByUserId(userId, id);
				if(bkComments != null && bkComments.size()>0){
					bkcommentList.addAll(bkComments);
				}
			}
//			for (BkComment bkComment : bkcommentList) {
//				sortEntity = new SortEntity();
//				sortEntity.setId(bkComment.getId());
//				sortEntity.setObject(bkComment);
//				sortEntities.add(sortEntity);
//			}
			resoureList = getResponseList(bkcommentList, uid, resoureList);
			/*long end = System.currentTimeMillis();
			System.out.println("书评耗时："+(end-begin));*/
//			System.out.println("书评数量:"+resoureList.size());
			//书单
			//begin = System.currentTimeMillis();
			List<ActPublish> actPublishs = actFacade.findPublishListByUsersId(CommentUtils.TYPE_BOOKLIST, id, idList);
//			for (ActPublish actPublish : actPublishs) {
//				sortEntity = new SortEntity();
//				sortEntity.setId(actPublish.getId());
//				sortEntity.setObject(actPublish);
//				sortEntities.add(sortEntity);
//			}
//			System.out.println("书单数量:"+actPublishs.size());
			resoureList = getResponseList(actPublishs, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("书单耗时："+(end-begin));
			//日记
			//begin = System.currentTimeMillis();
			List<Diary> diaryList = diaryFacade.findDiaryListByUsersId(idList, id);
//			for (Diary diary : diaryList) {
//				sortEntity = new SortEntity();
//				sortEntity.setId(diary.getId());
//				sortEntity.setObject(diary);
//				sortEntities.add(sortEntity);
//			}
//			System.out.println("日记数量:"+diaryList.size());
			resoureList = getResponseList(diaryList, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("日记耗时："+(end-begin));
			//影评
//			List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByUsersId(idList, null, id);
			//begin = System.currentTimeMillis();
			List<MvComment> mvCommentList = new ArrayList<MvComment>();
			for (long userId : idList) {
				List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByType(userId, null, id);
				if(mvComments != null && mvComments.size()>0){
					mvCommentList.addAll(mvComments);
				}
			}
			resoureList = getResponseList(mvCommentList, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("影评耗时："+(end-begin));
//			for (MvComment mvComment : mvCommentList) {
//				sortEntity = new SortEntity();
//				sortEntity.setId(mvComment.getId());
//				sortEntity.setObject(mvComment);
//				sortEntities.add(sortEntity);
//			}
//			System.out.println("影评数量:"+mvCommentList.size());
//			影单
			//begin = System.currentTimeMillis();
			List<ActPublish> mvLists = actFacade.findPublishListByUsersId(CommentUtils.TYPE_MOVIELIST, id, idList);
//			for (ActPublish actPublish : mvLists) {
//				sortEntity = new SortEntity();
//				sortEntity.setId(actPublish.getId());
//				sortEntity.setObject(actPublish);
//				sortEntities.add(sortEntity);
//			}
//			System.out.println("影单数量:"+mvLists.size());
			resoureList = getResponseList(mvLists, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("影单耗时："+(end-begin));
//			连载
//			List<ActPublish> serializes = actFacade.findPublishListByUsersId(CommentUtils.TYPE_PUSH, id, idList);
//			resoureList = getResponseList(serializes, uid, resoureList);
//			转发
//			List<ActTransmit> actTransmits = actFacade.findTransmitListByTypeAndUsersId(null, id, idList);
//			resoureList = getResponseList(actTransmits, uid, resoureList);
//			//长文章
			//begin = System.currentTimeMillis();
			List<Post> posts = postFacade.findPostListByUsersId(idList, id,null);
			resoureList = getResponseList(posts, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("长文章耗时："+(end-begin));
//			for (Post post : posts) {
//				sortEntity = new SortEntity();
//				sortEntity.setId(post.getId());
//				sortEntity.setObject(post);
//				sortEntities.add(sortEntity);
//			}

			//新的长文章
			//begin = System.currentTimeMillis();
			List<Article> articles = articleFacade.findArticleListByUsersId(idList, id,null);
			resoureList = getResponseList(articles, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("新的长文章耗时："+(end-begin));

//			Collections.sort(sortEntities);
//			if(sortEntities.size()>CommentUtils.RESOURCE_PAGE_SIZE){
//				sortEntities = sortEntities.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
//			}
//			List<Object> objects = new ArrayList<Object>();
//			for (SortEntity sortEntity2 : sortEntities) {
//				objects.add(sortEntity2.getObject());
//			}
//
////			System.out.println("长文章数量:"+posts.size());
//			resoureList = getResponseList(objects, uid, resoureList);
		return resoureList;
	}
	
	/**
	 * 获取朋友动态新内容
	 * @return
	 */
	public List<ResourceInfo> getFriendNewResoureListNew(List<Long> idList,Long id,Long uid,List<ResourceInfo> resoureList){
			List<SortEntity> sortEntities = new ArrayList<SortEntity>();
			SortEntity sortEntity = new SortEntity();
		//书评
		//long begin = System.currentTimeMillis();
		List<BkComment> bkcommentList = new ArrayList<BkComment>();
		for (long userId : idList) {
			List<BkComment> bkComments = bkCommentFacade.findCommentListByUserId(userId, id);
			//System.out.println("查询个人动态的书评信息的个数为"+bkComments.size()+"个人的uid列表为"+idList.toString()+"传过来的最后一个id为"+id);
			if(bkComments != null && bkComments.size()>0){
				bkcommentList.addAll(bkComments);
			}
		}
		for (BkComment bkComment : bkcommentList) {
			sortEntity = new SortEntity();
			sortEntity.setId(bkComment.getId());
			sortEntity.setObject(bkComment);
			if(sortEntity.getId()!=0){
				sortEntities.add(sortEntity);
			}
		}
		//long end = System.currentTimeMillis();
		//System.out.println("书评耗时："+(end-begin));
//			System.out.println("书评数量:"+resoureList.size());
		//书单
		//begin = System.currentTimeMillis();
		List<ActPublish> actPublishs = actFacade.findPublishListByUsersId(CommentUtils.TYPE_BOOKLIST, id, idList);
		//System.out.println("查询个人动态的书单信息的个数为"+actPublishs.size());
		for (ActPublish actPublish : actPublishs) {
			sortEntity = new SortEntity();
			sortEntity.setId(actPublish.getId());
			sortEntity.setObject(actPublish);
			if(sortEntity.getId()!=0){
				sortEntities.add(sortEntity);
			}
		}
//			System.out.println("书单数量:"+actPublishs.size());
		//end = System.currentTimeMillis();
		//System.out.println("书单耗时："+(end-begin));
		//日记
		//begin = System.currentTimeMillis();
		List<Diary> diaryList = diaryFacade.findDiaryListByUsersId(idList, id);
		//System.out.println("查询个人动态的日志信息的个数为"+diaryList.size());
		for (Diary diary : diaryList) {
			sortEntity = new SortEntity();
			sortEntity.setId(diary.getId());
			sortEntity.setObject(diary);
			if(sortEntity.getId()!=0){
				sortEntities.add(sortEntity);
			}
		}
//			System.out.println("日记数量:"+diaryList.size());
		//end = System.currentTimeMillis();
		//System.out.println("日记耗时："+(end-begin));
		//影评
//			List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByUsersId(idList, null, id);
		//begin = System.currentTimeMillis();
		List<MvComment> mvCommentList = new ArrayList<MvComment>();
		for (long userId : idList) {
			List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByType(userId, null, id);
			//System.out.println("查询个人动态的影评信息的个数为"+mvCommentList.size());
			if(mvComments != null && mvComments.size()>0){
				mvCommentList.addAll(mvComments);
			}
		}
		for (MvComment mvComment : mvCommentList) {
			sortEntity = new SortEntity();
			sortEntity.setId(mvComment.getId());
			sortEntity.setObject(mvComment);
			if(sortEntity.getId()!=0){
				sortEntities.add(sortEntity);
			}
		}
		//end = System.currentTimeMillis();
		//System.out.println("影评耗时："+(end-begin));
//			System.out.println("影评数量:"+mvCommentList.size());
//			影单
		//begin = System.currentTimeMillis();
		List<ActPublish> mvLists = actFacade.findPublishListByUsersId(CommentUtils.TYPE_MOVIELIST, id, idList);
		//System.out.println("查询个人动态的影单信息的个数为"+mvLists.size());
		for (ActPublish actPublish : mvLists) {
			sortEntity = new SortEntity();
			sortEntity.setId(actPublish.getId());
			sortEntity.setObject(actPublish);
			if(sortEntity.getId()!=0){
				sortEntities.add(sortEntity);
			}
		}
//			System.out.println("影单数量:"+mvLists.size());
		//end = System.currentTimeMillis();
		//System.out.println("影单耗时："+(end-begin));
//			连载
//			List<ActPublish> serializes = actFacade.findPublishListByUsersId(CommentUtils.TYPE_PUSH, id, idList);
//			resoureList = getResponseList(serializes, uid, resoureList);
//			转发
//			List<ActTransmit> actTransmits = actFacade.findTransmitListByTypeAndUsersId(null, id, idList);
//			resoureList = getResponseList(actTransmits, uid, resoureList);
//			//长文章
		//begin = System.currentTimeMillis();
		List<Post> posts = postFacade.findPostListByUsersId(idList, id,null);
		//System.out.println("查询个人动态的旧长文章的个数为"+posts.size());
		for (Post post : posts) {
			sortEntity = new SortEntity();
			sortEntity.setId(post.getId());
			sortEntity.setObject(post);
			if(sortEntity.getId()!=0){
				sortEntities.add(sortEntity);
			}
		}
		//end = System.currentTimeMillis();
		//System.out.println("长文章耗时："+(end-begin));

		//新的长文章
		//begin = System.currentTimeMillis();
		List<Article> articles = articleFacade.findArticleListByUsersId(idList, id,null);
		//System.out.println("查询个人动态的新长文章的个数为"+articles.size());
		for (Article article : articles) {
			article.setContent("");
			sortEntity = new SortEntity();
			sortEntity.setId(article.getId());
			sortEntity.setObject(article);
			if(sortEntity.getId()!=0){
				sortEntities.add(sortEntity);
			}
		}
		//end = System.currentTimeMillis();
		//System.out.println("新长文章耗时："+(end-begin));

		//System.out.println("得到的个人动态的长度为"+sortEntities.size());
		//排序截取
		Collections.sort(sortEntities);
		if(sortEntities.size()>CommentUtils.RESOURCE_PAGE_SIZE){
			sortEntities = sortEntities.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
		}
		//System.out.println("最后需要格式化的数量:"+sortEntities.size());
		List<Object> objects = new ArrayList<Object>();
		for (SortEntity sortEntity2 : sortEntities) {
			objects.add(sortEntity2.getObject());
		}

//			System.out.println("长文章数量:"+posts.size());
		//System.out.println("格式化 个人动态开始");
		//long begin = System.currentTimeMillis();
		resoureList = getResponseList(objects, uid, resoureList);
		/*long end = System.currentTimeMillis();
		System.out.println("格式化个人动态总耗时耗时："+(end-begin));*/
		return resoureList;
	}
	
	
	
//	/**
//	 * 更新资源信息
//	 * @return
//	 */
//	public List<ResourceInfo> getNewResoureList(String resouceType){
//		if(resouceType == null){
//			//书评
//			bkcommentList = bkCommentFacade.findAllComment(null);
//			resoureList = getResponseList(bkcommentList, null, resoureList);
//			//书单
//			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_BOOKLIST, null);
//			resoureList = getResponseList(actPublishs, null, resoureList);
//		}
//		else if(CommentUtils.TYPE_PUSH.equals(resouceType)){
//			//连载
//			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_PUSH, null);
//			resoureList = getResponseList(actPublishs, null, resoureList);
//		}
//		else if(CommentUtils.TYPE_BOOKLIST.equals(resouceType)){
//			//书评
//			bkcommentList = bkCommentFacade.findAllComment(null);
//			resoureList = getResponseList(bkcommentList, null, resoureList);
//			//书单
//			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_BOOKLIST,null);
//			resoureList = getResponseList(actPublishs, null, resoureList);
//		}else if(CommentUtils.TYPE_MOVIELIST.equals(resouceType)){
//			//影评
//			List<MvComment> mvComments = mvCommentFacade.findAllComment(null);
//			resoureList = getResponseList(mvComments, null, resoureList);
//			//影单
//			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_MOVIELIST, null);
//			resoureList = getResponseList(actPublishs, null, resoureList);
//			
//		}
//		return resoureList;
//	}

	/**
	 *
	 * <p>Title: getUserNewResoureListNew</p>
	 * <p>Description: 对一个人的动态筛选</p>
	 * @author :changjiang
	 * date 2015-5-26 下午5:27:12
	 * @param idList
	 * @param id
	 * @param uid
	 * @param resoureList
	 * @param resType
	 * @return
	 */
	public List<String> getUserNewResoureListNew(List<Long> idList,Long id,final Long uid,List<ResourceInfo> resoureList,String resType,Long friendId){
		List<ResourceInfo> resourList = new ArrayList<ResourceInfo>();
		/*List<SortEntity> sortEntities = new ArrayList<SortEntity>();
		SortEntity sortEntity = new SortEntity();*/
		List<String> resStrList = new ArrayList<String>();
		Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(friendId));

		if(resType.equals(CommentUtils.TYPE_DIARY)){//文字
			//日记
			//begin = System.currentTimeMillis();
			List<Diary> diaryList = diaryFacade.findDiaryListByUsersId(idList, id);
			for (Diary diary : diaryList) {

				String resStr = getResourceInfoStr(diary.getId(), friendId, resType,uid);
				if(null==resStr){
					ResourceInfo resourceInfo = putObjectToResource(diary, friendId,1);
					resStr = StringUtils.putObjectToJson(resourceInfo);
				}
				resStrList.add(resStr);
				/*ResourceInfo resourceInfo = putObjectToResource(diary, uid,1);
				resourList.add(resourceInfo);*/
				/*sortEntity = new SortEntity();
				sortEntity.setId(diary.getId());
				sortEntity.setObject(diary);
				sortEntities.add(sortEntity);*/
			}
		}else if(resType.equals(CommentUtils.TYPE_BOOK_COMMENT)){//书评
			List<BkComment> bkcommentList = new ArrayList<BkComment>();
			for (long userId : idList) {
				List<BkComment> bkComments = bkCommentFacade.findCommentListByUserId(userId, id);
				if(bkComments != null && bkComments.size()>0){
					bkcommentList.addAll(bkComments);
				}
			}
			for (BkComment bkComment : bkcommentList) {

				String resStr = getResourceInfoStr(bkComment.getId(), friendId, resType,uid);
				if(null==resStr){
					ResourceInfo resourceInfo = putObjectToResource(bkComment, friendId,1);
					resStr = StringUtils.putObjectToJson(resourceInfo);
				}
				resStrList.add(resStr);
				/*ResourceInfo resourceInfo = putObjectToResource(bkComment, uid,1);
				resourList.add(resourceInfo);*/
				/*sortEntity = new SortEntity();
				sortEntity.setId(bkComment.getId());
				sortEntity.setObject(bkComment);
				sortEntities.add(sortEntity);*/
			}
		}else if(resType.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//影评
			//影评
			//		List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByUsersId(idList, null, id);
				//begin = System.currentTimeMillis();
				List<MvComment> mvCommentList = new ArrayList<MvComment>();
				for (long userId : idList) {
					List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByType(userId, null, id);
					if(mvComments != null && mvComments.size()>0){
						mvCommentList.addAll(mvComments);
					}
				}
				for (MvComment mvComment : mvCommentList) {

					String resStr = getResourceInfoStr(mvComment.getId(), friendId, resType,uid);
					if(null==resStr){
						ResourceInfo resourceInfo = putObjectToResource(mvComment, friendId,1);
						resStr = StringUtils.putObjectToJson(resourceInfo);
					}
					resStrList.add(resStr);
					/*ResourceInfo resourceInfo = putObjectToResource(mvComment, uid,1);
					resourList.add(resourceInfo);*/
					/*sortEntity = new SortEntity();
					sortEntity.setId(mvComment.getId());
					sortEntity.setObject(mvComment);
					sortEntities.add(sortEntity);*/
				}
		}else if(resType.equals(CommentUtils.TYPE_NEWARTICLE)){//长文章
			List<Article> articleList = new ArrayList<Article>();
			for (long userId : idList) {
				List<Article> articles = articleFacade.findArticleListByUserId(userId, id,CommentUtils.TYPE_NEWARTICLE);
				if(articles != null && articles.size()>0){
					articleList.addAll(articles);
				}
			}
			for (Article article : articleList) {
				String resStr = getResourceInfoStr(article.getId(), friendId, resType,uid);
				if(null==resStr){
					ResourceInfo resourceInfo = putObjectToResource(article, friendId,1);
					resStr = StringUtils.putObjectToJson(resourceInfo);
				}
				resStrList.add(resStr);
				/*ResourceInfo resourceInfo = putObjectToResource(mvComment, uid,1);
				resourList.add(resourceInfo);*/
				/*sortEntity = new SortEntity();
				sortEntity.setId(mvComment.getId());
				sortEntity.setObject(mvComment);
				sortEntities.add(sortEntity);*/
			}
		}
		//书评
		//long begin = System.currentTimeMillis();
		//long end = System.currentTimeMillis();
		//System.out.println("书评耗时："+(end-begin));
	//		System.out.println("书评数量:"+resoureList.size());
		//书单
		//begin = System.currentTimeMillis();
		/*List<ActPublish> actPublishs = actFacade.findPublishListByUsersId(CommentUtils.TYPE_BOOKLIST, id, idList);
		for (ActPublish actPublish : actPublishs) {
			sortEntity = new SortEntity();
			sortEntity.setId(actPublish.getId());
			sortEntity.setObject(actPublish);
			sortEntities.add(sortEntity);
		}*/
	//		System.out.println("书单数量:"+actPublishs.size());
		//end = System.currentTimeMillis();
		//System.out.println("书单耗时："+(end-begin));

	//		System.out.println("日记数量:"+diaryList.size());
		//end = System.currentTimeMillis();
		//System.out.println("日记耗时："+(end-begin));

		//end = System.currentTimeMillis();
		//System.out.println("影评耗时："+(end-begin));
	//		System.out.println("影评数量:"+mvCommentList.size());
	//		影单
		//begin = System.currentTimeMillis();
		/*List<ActPublish> mvLists = actFacade.findPublishListByUsersId(CommentUtils.TYPE_MOVIELIST, id, idList);
		for (ActPublish actPublish : mvLists) {
			sortEntity = new SortEntity();
			sortEntity.setId(actPublish.getId());
			sortEntity.setObject(actPublish);
			sortEntities.add(sortEntity);
		}*/
	//		System.out.println("影单数量:"+mvLists.size());
		//end = System.currentTimeMillis();
		//System.out.println("影单耗时："+(end-begin));
	//		连载
	//		List<ActPublish> serializes = actFacade.findPublishListByUsersId(CommentUtils.TYPE_PUSH, id, idList);
	//		resoureList = getResponseList(serializes, uid, resoureList);
	//		转发
	//		List<ActTransmit> actTransmits = actFacade.findTransmitListByTypeAndUsersId(null, id, idList);
	//		resoureList = getResponseList(actTransmits, uid, resoureList);
	//		//长文章
		//begin = System.currentTimeMillis();
		/*List<Post> posts = postFacade.findPostListByUsersId(idList, id,null);
		for (Post post : posts) {
			sortEntity = new SortEntity();
			sortEntity.setId(post.getId());
			sortEntity.setObject(post);
			sortEntities.add(sortEntity);
		}*/
		//end = System.currentTimeMillis();
		//System.out.println("长文章耗时："+(end-begin));

		//新的长文章
		//begin = System.currentTimeMillis();
		/*List<Article> articles = articleFacade.findArticleListByUsersId(idList, id,null);
		for (Article article : articles) {
			article.setContent("");
			sortEntity = new SortEntity();
			sortEntity.setId(article.getId());
			sortEntity.setObject(article);
			sortEntities.add(sortEntity);
		}*/
		//end = System.currentTimeMillis();
		//System.out.println("新长文章耗时："+(end-begin));

		//排序截取
		/*Collections.sort(sortEntities);
		if(sortEntities.size()>CommentUtils.RESOURCE_PAGE_SIZE){
			sortEntities = sortEntities.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
		}
		//System.out.println("最后需要格式化的数量:"+sortEntities.size());
		List<Object> objects = new ArrayList<Object>();
		for (SortEntity sortEntity2 : sortEntities) {
			objects.add(sortEntity2.getObject());
		}

	//		System.out.println("长文章数量:"+posts.size());
		//begin = System.currentTimeMillis();
		resoureList = getResponseList(objects, uid, resoureList);*/
		//end = System.currentTimeMillis();
		//System.out.println("格式化总耗时耗时："+(end-begin));
		return resStrList;
	}
	
	/**
	 *
	 * <p>Title: getResourceInfoStr</p>
	 * <p>Description: 根据资源id和type来查询</p>
	 * @author :changjiang
	 * date 2015-5-28 下午5:49:31
	 * @param resId
	 * @param userId
	 * @param type
	 * @return
	 */
	public String getResourceInfoStr(long resId,final long userId,String type,final long uuid){
		String resStr = resourceJedisManager.getOneResource(resId);
		if(null==resStr){
			return null;
		}
		if(0!=userId){
			Map<String, String> userInfoMap = userJedisManager.getUserInfo(userId);
			if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
				//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
				resStr = resStr.substring(0,resStr.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+resStr.substring(resStr.indexOf("\"face_url\""));
				resStr = resStr.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
				/*System.out.println("返回用户的信息威"+userInfoMap);
			System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
			System.out.println("返回的资源信息为"+value);*/
			}
		}
		boolean isReBuild = false;
		Map<String,String> map = relationToUserAndResManager.getOneRelation(uuid, resId);
		if(null!=map&&!map.isEmpty()){
		//当关系不为空时

			/*inList = map.get(JedisConstant.RELATION_IS_INLIST);
			if(null!=inList){
				value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
			}*/
			String isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
			if(null!=isPraise&&!"".equals(isPraise)){
				resStr = resStr.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
			}
			String isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
			if(null!=isCollect&&!"".equals(isCollect)){
				resStr = resStr.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+isCollect+"");
			}
//			String isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
//			if(null!=isUseful&&!"".equals(isUseful)){
//				resStr = resStr.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
//			}
			if(!resStr.contains("isUseful")){//当这条资源不包含有用信息的话 重建
				isReBuild = true;
				resStr= resStr.substring(0,resStr.length()-1)+",\"isUseful\": 0,\"usefulCount\": \"0\",\"uselessCount\": \"0\"}";

			}
			String isUseful = map.get(JedisConstant.RLEATION_IS_USEFUL);
			if(null!=isUseful&&!"".equals(isUseful)){
				resStr = resStr.replaceAll("\"isUseful\": ?[0-9]+", "\"isUseful\": "+isUseful+"");
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
		Map<String, String> otherMap = relationToUserAndResManager.getOneRelationToRes(resId);
		//添加资源的附加信息 比如说评论数，点赞数
			if(null!=otherMap&&!otherMap.isEmpty()){
				String zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
				if(null!=zNum&&!"".equals(zNum)){
					resStr = resStr.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
				}
				String cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
				if(null!=cNum&&!"".equals(cNum)){
					resStr = resStr.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
				}
				String usefulCount = otherMap.get(JedisConstant.RELATION_USEFUL_NUM);
				if(null!=usefulCount&&!"".equals(usefulCount)){
					resStr = resStr.replaceAll("\"usefulCount\": ?\"[0-9]+\"", "\"usefulCount\": \""+usefulCount+"\"");
				}
				String uselessCount = otherMap.get(JedisConstant.RELATION_USELESS_NUM);
				if(null!=uselessCount&&!"".equals(uselessCount)){
					resStr = resStr.replaceAll("\"uselessCount\": ?\"[0-9]+\"", "\"uselessCount\": \""+usefulCount+"\"");
				}
			}
			//添加是否已经加入书单
			/*String type = resStr.substring(resStr.indexOf("\"type\":\"")+8, resStr.indexOf("\"type\":\"")+9);
			String inList = "";*/
			String inList = "";
			String restype = resStr.substring(resStr.indexOf("\"type\":\"")+8, resStr.indexOf("\"type\":\"")+9);
			//增加阅读数
			//int readCount = resStatJedisManager.getReadNum(resId, restype);
			//System.out.println("阅读数为："+readCount+"阅读的resid为"+rid);
			/*if(0!=readCount){
				resStr = resStr.replaceAll("\"readingCount\": ?\"[0-9]+\"", "\"readingCount\": \""+readCount+"\"");
			}*/
			if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
				String bookId = "";
				Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
				Matcher bookIdMatcher = bookIdPattern.matcher(resStr);
				if(bookIdMatcher.find()){
					bookId = bookIdMatcher.group(1);
				}
				if(!"".equals(bookId)){//当电影的id不为空时
					final String bkId = bookId;
					inList = relationToUserandresClient.execute(new JedisWorker<String>(){
						@Override
						public String work(Jedis jedis) {
							String relationkey = uuid +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
									return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
						}});
				}
			}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
				String movieId = "";
				Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
				Matcher movieIdMatcher = movieIdPattern.matcher(resStr);
				if(movieIdMatcher.find()){
					movieId = movieIdMatcher.group(1);
				}
				if(!"".equals(movieId)){//当电影的id不为空时
					final String mvId = movieId;
					inList = relationToUserandresClient.execute(new JedisWorker<String>(){
						@Override
						public String work(Jedis jedis) {
							String relationkey = uuid +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
									return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
						}});
				}
			}
			if(null!=inList&&!"".equals(inList)){
				resStr = resStr.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
			}else{
				resStr = resStr.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
			}

		if(isReBuild){
			resourceJedisManager.saveOneResourceStr(resId,resStr);
		}
		return resStr;
	}

	/**
	 * 更新资源分页信息
	 * @return
	 */
	public List<ResourceInfo> getResoureListByPage(String resouceType , Long id,Long uid ,List<ResourceInfo> resoureList){

		if(resouceType == null){
			//书评
			//long begin = System.currentTimeMillis();
			List<BkComment> bkcommentList = bkCommentFacade.findAllComment(id);
//			if(bkcommentList.size()>10){
//				bkcommentList = bkcommentList.subList(0, 10);
//			}
			resoureList = getResponseList(bkcommentList, uid, resoureList);
			//long end = System.currentTimeMillis();
			//System.out.println("书评耗时："+(end-begin));
			//LOG.info("书评耗时："+(end-begin));
			//书单
			//begin = System.currentTimeMillis();
			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_BOOKLIST,id);
//			if(actPublishs.size()>10){
//				actPublishs = actPublishs.subList(0, 10);
//			}
			resoureList = getResponseList(actPublishs, uid, resoureList);
			//end = System.currentTimeMillis();
			//LOG.info("书单耗时："+(end-begin));
			//System.out.println("书单耗时："+(end-begin));
//			//连载
//			begin = System.currentTimeMillis();
//			actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_PUSH, id);
//			if(actPublishs.size()>10){
//				actPublishs = actPublishs.subList(0, 10);
//			}
//			resoureList = getResponseList(actPublishs, uid, resoureList);
//			end = System.currentTimeMillis();
//			System.out.println("连载耗时："+(end-begin));
//			LOG.info("连载耗时："+(end-begin));
			//影评
			//begin = System.currentTimeMillis();
			List<MvComment> mvComments = mvCommentFacade.findAllComment(id);
//			if(mvComments.size()>10){
//				mvComments = mvComments.subList(0, 10);
//			}
			resoureList = getResponseList(mvComments, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("影评耗时："+(end-begin));
			//LOG.info("影评耗时："+(end-begin));
			//影单
			//begin = System.currentTimeMillis();
			actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_MOVIELIST, id);
//			if(actPublishs.size()>10){
//				actPublishs = actPublishs.subList(0, 10);
//			}
			resoureList = getResponseList(actPublishs, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("影单耗时："+(end-begin));
			//LOG.info("影单耗时："+(end-begin));
			//日志
			//begin = System.currentTimeMillis();
			List<Diary> diaries = diaryFacade.findDiaryList(id);
//			if(diaries.size()>10){
//				diaries = diaries.subList(0, 10);
//			}
			resoureList = getResponseList(diaries, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("日志耗时："+(end-begin));
			//LOG.info("日志耗时："+(end-begin));
			//转发
//			begin = System.currentTimeMillis();
//			List<ActTransmit> actTransmits = actFacade.findAllTransmitList(id);
////			if(actTransmits.size()>10){
////				actTransmits = actTransmits.subList(0, 10);
////			}
//			resoureList = getResponseList(actTransmits, uid, resoureList);
//			end = System.currentTimeMillis();
////			System.out.println("转发耗时："+(end-begin));
//			LOG.info("转发耗时："+(end-begin));
			//长文章
			//begin = System.currentTimeMillis();
			List<Post> posts = postFacade.queryByTypePost(null, id);
			resoureList = getResponseList(posts, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("长文章耗时："+(end-begin));
			//LOG.info("长文章耗时："+(end-begin));

			//新的长文章
			//begin = System.currentTimeMillis();
			List<Article> articles = articleFacade.queryByTypeArticle(null, uid);
			resoureList = getResponseList(articles, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("新的长文章耗时："+(end-begin));
			//LOG.info("新的长文章耗时："+(end-begin));

		}
		else if(CommentUtils.TYPE_PUSH.equals(resouceType)){
			//连载
			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_PUSH, id);
			resoureList = getResponseList(actPublishs, uid, resoureList);
		}
		else if(CommentUtils.TYPE_BOOKLIST.equals(resouceType)){
			//书单
			List<ActPublish> actPublishs = actFacade.findPublishList(id);
			resoureList = getResponseList(actPublishs, uid, resoureList);
		}else if(CommentUtils.TYPE_MOVIELIST.equals(resouceType)){
			//影单
			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_MOVIELIST, id);
			resoureList = getResponseList(actPublishs, uid, resoureList);
		}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(resouceType)){
			//书评
			List<BkComment> bkcommentList = bkCommentFacade.findAllComment(id);
			resoureList = getResponseList(bkcommentList, uid, resoureList);
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(resouceType)){
			//影评
			List<MvComment> mvComments = mvCommentFacade.findAllComment(id);
			resoureList = getResponseList(mvComments, uid, resoureList);
		}
		return resoureList;
	}

	/**
	 * 更新资源分页信息
	 * @return
	 */
	public List<ResourceInfo> getResoureListByPageNew(String resouceType , Long id,Long uid ,List<ResourceInfo> resoureList,String userType){

		if(resouceType == null){
			List<SortEntity> sortEntities = new ArrayList<SortEntity>();
			//书评
			//long begin = System.currentTimeMillis();
			List<BkComment> bkcommentList = bkCommentFacade.findAllComment(id);
			for (BkComment bkComment : bkcommentList) {
				SortEntity sortEntity = new SortEntity();
				if(null==userType){//所有人广场
					sortEntity.setId(bkComment.getId());
					sortEntity.setObject(bkComment);
					sortEntities.add(sortEntity);
				}else{
					long userId = bkComment.getUserId();
					UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null,userId);
					if(null==uInfo){//没有该用户时
						continue;
					}
					if(userType.equals("50")){//神人广场
						if(uInfo.getLevel()==50){
							sortEntity.setId(bkComment.getId());
							sortEntity.setObject(bkComment);
							sortEntities.add(sortEntity);
						}
					}else{
						if(uInfo.getLevel()!=50){
							sortEntity.setId(bkComment.getId());
							sortEntity.setObject(bkComment);
							sortEntities.add(sortEntity);
						}
					}
				}
			}
//			if(bkcommentList.size()>10){
//				bkcommentList = bkcommentList.subList(0, 10);
//			}
//			resoureList = getResponseList(bkcommentList, uid, resoureList);
			//long end = System.currentTimeMillis();
			//System.out.println("书评耗时："+(end-begin));
			//LOG.info("书评耗时："+(end-begin));
			//书单
			//begin = System.currentTimeMillis();
			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_BOOKLIST,id);
			for (ActPublish actPublish : actPublishs) {
				SortEntity sortEntity = new SortEntity();
				if(null==userType){//所有人广场
					sortEntity.setId(actPublish.getId());
					sortEntity.setObject(actPublish);
					sortEntities.add(sortEntity);
				}else{
					long userId = actPublish.getUserId();
					UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null,userId);
					if(null==uInfo){//没有该用户时
						continue;
					}
					if(userType.equals("50")){//神人广场
						if(uInfo.getLevel()==50){
							sortEntity.setId(actPublish.getId());
							sortEntity.setObject(actPublish);
							sortEntities.add(sortEntity);
						}
					}/*else{
						if(uInfo.getLevel()!=50){
							sortEntity.setId(actPublish.getId());
							sortEntity.setObject(actPublish);
							sortEntities.add(sortEntity);
						}
					}*/
				}
			}
//			if(actPublishs.size()>10){
//				actPublishs = actPublishs.subList(0, 10);
//			}
//			resoureList = getResponseList(actPublishs, uid, resoureList);
			//end = System.currentTimeMillis();
			//LOG.info("书单耗时："+(end-begin));
			//System.out.println("书单耗时："+(end-begin));
//			//连载
//			begin = System.currentTimeMillis();
//			actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_PUSH, id);
//			if(actPublishs.size()>10){
//				actPublishs = actPublishs.subList(0, 10);
//			}
//			resoureList = getResponseList(actPublishs, uid, resoureList);
//			end = System.currentTimeMillis();
//			System.out.println("连载耗时："+(end-begin));
//			LOG.info("连载耗时："+(end-begin));
			//影评
			//begin = System.currentTimeMillis();
			List<MvComment> mvComments = mvCommentFacade.findAllComment(id);
			for (MvComment mvComment : mvComments) {
				SortEntity sortEntity = new SortEntity();
				if(null==userType){//所有人广场
					sortEntity.setId(mvComment.getId());
					sortEntity.setObject(mvComment);
					sortEntities.add(sortEntity);
				}else{
					long userId = mvComment.getUserId();
					UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null,userId);
					if(null==uInfo){//没有该用户时
						continue;
					}
					if(userType.equals("50")){//神人广场
						if(uInfo.getLevel()==50){
							sortEntity.setId(mvComment.getId());
							sortEntity.setObject(mvComment);
							sortEntities.add(sortEntity);
						}
					}else{
						if(uInfo.getLevel()!=50){
							sortEntity.setId(mvComment.getId());
							sortEntity.setObject(mvComment);
							sortEntities.add(sortEntity);
						}
					}
				}
			}
//			if(mvComments.size()>10){
//				mvComments = mvComments.subList(0, 10);
//			}
//			resoureList = getResponseList(mvComments, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("影评耗时："+(end-begin));
			//LOG.info("影评耗时："+(end-begin));
			//影单
			//begin = System.currentTimeMillis();
			actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_MOVIELIST, id);
			for (ActPublish actPublish : actPublishs) {
				SortEntity sortEntity = new SortEntity();
				if(null==userType){//所有人广场
					sortEntity.setId(actPublish.getId());
					sortEntity.setObject(actPublish);
					sortEntities.add(sortEntity);
				}else{
					long userId = actPublish.getUserId();
					UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null,userId);
					if(null==uInfo){//没有该用户时
						continue;
					}
					if(userType.equals("50")){//神人广场
						if(uInfo.getLevel()==50){
							sortEntity.setId(actPublish.getId());
							sortEntity.setObject(actPublish);
							sortEntities.add(sortEntity);
						}
					}/*else{
						if(uInfo.getLevel()!=50){
							sortEntity.setId(actPublish.getId());
							sortEntity.setObject(actPublish);
							sortEntities.add(sortEntity);
						}
					}*/
				}
			}
//			if(actPublishs.size()>10){
//				actPublishs = actPublishs.subList(0, 10);
//			}
//			resoureList = getResponseList(actPublishs, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("影单耗时："+(end-begin));
			//LOG.info("影单耗时："+(end-begin));
			//日志
			//begin = System.currentTimeMillis();
			List<Diary> diaries = diaryFacade.findDiaryList(id);
			for (Diary diary : diaries) {
				SortEntity sortEntity = new SortEntity();
				if(null==userType){//所有人广场
					sortEntity.setId(diary.getId());
					sortEntity.setObject(diary);
					sortEntities.add(sortEntity);
				}else{
					long userId = diary.getUid();
					UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null,userId);
					if(null==uInfo){//没有该用户时
						continue;
					}
					if(userType.equals("50")){//神人广场
						if(uInfo.getLevel()==50){
							sortEntity.setId(diary.getId());
							sortEntity.setObject(diary);
							sortEntities.add(sortEntity);
						}
					}else{
						if(uInfo.getLevel()!=50){
							sortEntity.setId(diary.getId());
							sortEntity.setObject(diary);
							sortEntities.add(sortEntity);
						}
					}
				}

			}
//			if(diaries.size()>10){
//				diaries = diaries.subList(0, 10);
//			}
//			resoureList = getResponseList(diaries, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("日志耗时："+(end-begin));
			//LOG.info("日志耗时："+(end-begin));
			//转发
//			begin = System.currentTimeMillis();
//			List<ActTransmit> actTransmits = actFacade.findAllTransmitList(id);
////			if(actTransmits.size()>10){
////				actTransmits = actTransmits.subList(0, 10);
////			}
//			resoureList = getResponseList(actTransmits, uid, resoureList);
//			end = System.currentTimeMillis();
////			System.out.println("转发耗时："+(end-begin));
//			LOG.info("转发耗时："+(end-begin));
			//长文章
			//begin = System.currentTimeMillis();
			List<Post> posts = postFacade.queryByTypePost(null, id);
			for (Post post : posts) {
				SortEntity sortEntity = new SortEntity();
				if(null==userType){//所有人广场
					sortEntity.setId(post.getId());
					sortEntity.setObject(post);
					sortEntities.add(sortEntity);
				}else{
					long userId = post.getUid();
					UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null,userId);
					if(null==uInfo){//没有该用户时
						continue;
					}
					if(userType.equals("50")){//神人广场
						if(uInfo.getLevel()==50){
							sortEntity.setId(post.getId());
							sortEntity.setObject(post);
							sortEntities.add(sortEntity);
						}
					}/*else{
						if(uInfo.getLevel()!=50){
							sortEntity.setId(post.getId());
							sortEntity.setObject(post);
							sortEntities.add(sortEntity);
						}
					}*/
				}

			}
//			resoureList = getResponseList(posts, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("长文章耗时："+(end-begin));
			//LOG.info("长文章耗时："+(end-begin));


			//新的长文章
			//begin = System.currentTimeMillis();
			List<Article> articles = articleFacade.queryByTypeArticle(null, id);
			for (Article article : articles) {
				article.setContent("");
				SortEntity sortEntity = new SortEntity();
				if(null==userType){//所有人广场
					sortEntity.setId(article.getId());
					sortEntity.setObject(article);
					sortEntities.add(sortEntity);
				}else{
					long userId = article.getUid();
					UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null,userId);
					if(null==uInfo){//没有该用户时
						continue;
					}
					if(userType.equals("50")){//神人广场
						if(uInfo.getLevel()==50){
							sortEntity.setId(article.getId());
							sortEntity.setObject(article);
							sortEntities.add(sortEntity);
						}
					}/*else{
						if(uInfo.getLevel()!=50){
							sortEntity.setId(post.getId());
							sortEntity.setObject(post);
							sortEntities.add(sortEntity);
						}
					}*/
				}

			}
//			resoureList = getResponseList(posts, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("新长文章耗时："+(end-begin));
			//LOG.info("新长文章耗时："+(end-begin));



			//begin = System.currentTimeMillis();
			Collections.sort(sortEntities);
			if(sortEntities.size()>CommentUtils.RESOURCE_PAGE_SIZE){
				sortEntities = sortEntities.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
			}
			List list = new ArrayList();
			for (SortEntity sortEntity : sortEntities) {
				list.add(sortEntity.getObject());
			}
			resoureList = getResponseList(list, uid, resoureList);
			//end = System.currentTimeMillis();
			//System.out.println("格式化耗时："+(end-begin));
			//LOG.info("格式化耗时："+(end-begin));
		}
		else if(CommentUtils.TYPE_PUSH.equals(resouceType)){
			//连载
			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_PUSH, id);
			resoureList = getResponseList(actPublishs, uid, resoureList);
		}
		else if(CommentUtils.TYPE_BOOKLIST.equals(resouceType)){
			//书单
			List<ActPublish> actPublishs = actFacade.findPublishList(id);
			resoureList = getResponseList(actPublishs, uid, resoureList);
		}else if(CommentUtils.TYPE_MOVIELIST.equals(resouceType)){
			//影单
			List<ActPublish> actPublishs = actFacade.findPublishListByType(CommentUtils.TYPE_MOVIELIST, id);
			resoureList = getResponseList(actPublishs, uid, resoureList);
		}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(resouceType)){
			//书评
			List<BkComment> bkcommentList = bkCommentFacade.findAllComment(id);
			resoureList = getResponseList(bkcommentList, uid, resoureList);
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(resouceType)){
			//影评
			List<MvComment> mvComments = mvCommentFacade.findAllComment(id);
			resoureList = getResponseList(mvComments, uid, resoureList);
		}
		return resoureList;
	}
	
	/**
	 *	将资源转为首页
	 * @param list
	 * @param type
	 * @return
	 */
	public List getResponseList(List reqList  ,Long uid, List<ResourceInfo> resList){
		ResourceInfo ri = new ResourceInfo();
		if(reqList.size()>0){
			Object object = reqList.get(0);
//			ResourceInfo resourceInfo = fileUtils.putObjectToResource(object,ucenterFacade);
//			long id = resourceInfo.getRid();
//			if(id != 0){
				flagint = ResultUtils.SUCCESS;
				Iterator<Object> iter = reqList.iterator();

//				for (Object obj : reqList) {
				while(iter.hasNext()){
					Object obj = iter.next();
					//long begin = System.currentTimeMillis();
					//将各种资源格式化
					//long startTime = System.currentTimeMillis();
					//System.out.println("将各种资源格式化为resourceInfo"+obj.toString());
					ri = putObjectToResource(obj, uid,1);
							//putCollectObjectToResource(obj, uid,1);
					//long endTime = System.currentTimeMillis();
					//System.out.println("将一个书单转化为resource用到的时间为"+(endTime - startTime));
							//putObjectToResource(obj, uid,1);
					//long end = System.currentTimeMillis();
//					System.out.println("单条resource格式化时间:"+(end-begin));
					if(ri != null && ri.getRid() != UNID){
						resList.add(ri);
					}
				}
//				}
//			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	
	/**
	 * 将所有类型格式化
	 * @param obj
	 * @param uid
	 * @return
	 */
	public ResourceInfo putObjectToResource(Object obj,Long uid,int isList){
		//long begin = System.currentTimeMillis();
		ResourceInfo ri = new ResourceInfo();
		if(obj == null || uid == 0){
			return ri;
		}
		String objName = obj.getClass().getName();
		if(BkComment.class.getName().equals(objName)){
			try {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,netBookFacade,uid);
				BkComment bkcomment = (BkComment) obj;
				Map<String, Object> readTotalMap = bkCommentFacade.findBkCommentCount(bkcomment.getUserId());
				ri.setReadTotal(readTotalMap.get("count")+"");
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("书评资源显示出错，资源ID为："+((BkComment)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(ActPublish.class.getName().equals(objName)){
			ActPublish actPublish = (ActPublish) obj;
			String pType = actPublish.getType();
			try {
				//判断推送的类型
				if(CommentUtils.TYPE_BOOKLIST.equals(pType)){
					ri = bookUtils.putActPublishBookListToResource((ActPublish) obj, getResourceInfoFacade, ucenterFacade, actFacade, bkFacade, uid);
					ri.setType(CommentUtils.TYPE_BOOKLIST);
				}else if(CommentUtils.TYPE_PUSH.equals(pType)){
					ri = fileUtils.putActPulishToResource(obj, ucenterFacade, serializeFacade, actFacade);
				}else if(CommentUtils.TYPE_MOVIELIST.equals(pType)){
					ri = movieUtils.putActPublishMovieListToResource(obj, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
				}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(pType)){
					ri = actUtils.putActPublishBKCommentToResource(actPublish, ucenterFacade, actFacade, bkCommentFacade, bkFacade, getResourceInfoFacade,netBookFacade, uid);
				}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(pType)){
					ri = actUtils.putActPublishMVCommentToResource(actPublish, ucenterFacade, actFacade, mvCommentFacade, mvFacade, myMovieFacade, uid);
				}else if(CommentUtils.TYPE_ARTICLE.equals(pType)){
					ri = actUtils.putActPublishArticleToResource(actPublish, ucenterFacade, actFacade, postFacade, uid);
				}else if(CommentUtils.TYPE_NEWARTICLE.equals(pType)){
					ri = actUtils.putActPublishNEWArticleToResource(actPublish, ucenterFacade, actFacade, articleFacade, uid);
				}
			} catch (Exception e) {
				LOG.error("推送资源显示出错，资源类型["+actPublish.getType()+"]ID为："+actPublish.getId()+e.getMessage(), e.fillInStackTrace());
				ri = new ResourceInfo();
			}

			//fileUtils.putSomeThingToResource(actPublish, ri);
		}else if(ActTransmit.class.getName().equals(objName)){
			ActTransmit actTransmit = (ActTransmit) obj;
			try {
				ri = putActTransmitToResource(actTransmit, uid);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
//						System.out.println("转发显示出错，资源id为："+actTransmit.getId());
				LOG.error("转发显示出错，资源id为："+actTransmit.getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(MvComment.class.getName().equals(objName)){
			try {
				ri = movieUtils.putMVCommentToResource(obj, ucenterFacade, actFacade, mvFacade,myMovieFacade,uid);
				MvComment mvcomment = (MvComment) obj;
				Map<String, Object> readTotalMap = mvCommentFacade.findMvCommentCountByUid(mvcomment.getUserId());
				ri.setReadTotal(readTotalMap.get("count")+"");

				ResStatistic reStat = new ResStatistic();
				reStat.setResId(ri.getRid());
				reStat.setType(ri.getType());
				ResStatistic resStatic = resStatisticService.findResStatisticById(reStat);
				long hotCount = 0;
				if(resStatic!=null && resStatic.getId()>0){
					hotCount = resStatic.getHeatNumber();
				}
				ri.setVoteNum(hotCount+"");
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("影评资源显示出错，资源ID为："+((MvComment)obj).getId()+e.getMessage(),e.fillInStackTrace());
			}
		}else if(Diary.class.getName().equals(objName)){
			try {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("日志资源显示出错，资源ID为："+((Diary)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(Post.class.getName().equals(objName)){
			try {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("长文章资源显示出错，资源ID为："+((Post)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(Article.class.getName().equals(objName)){
			try {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("长文章资源显示出错，资源ID为："+((Article)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(BookInfo.class.getName().equals(objName)){
			try {
				ri = bookUtils.putBookInfoToResource((BookInfo) obj);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("书资源显示出错，资源ID为："+((BkInfo)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(BkInfo.class.getName().equals(objName)){
			try {
				ri = bookUtils.putBkInfoToResource((BkInfo) obj);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("书资源显示出错，资源ID为："+((BkInfo)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(MvInfo.class.getName().equals(objName)){
			try {
				ri = movieUtils.putMVInfoToResource((MvInfo) obj);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("电影资源显示出错，资源ID为："+((MvInfo)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}
		}else if(MovieInfo.class.getName().equals(objName)){
			try {
				ri = movieUtils.putMovieInfoToResource((MovieInfo) obj);
			} catch (Exception e) {
				e.printStackTrace();
				ri = new ResourceInfo();
				LOG.error("电影资源显示出错，资源ID为："+((MvInfo)obj).getId()+e.getMessage(), e.fillInStackTrace());
			}

		}else if(ResourceLink.class.getName().equals(objName)){
			ResourceLink rl = (ResourceLink) obj;
			try {
				//排除“外链”资源的情况
				String type = rl.getLinkType();
				long id = rl.getResLinkId();
				if(CommentUtils.TYPE_OUT_LINK.equals(type)){
					//外链资源类型
					ri.setRid(rl.getId());
					String content = rl.getDescription();//包含图片和链接地址
					String imageUrl = "";
					String url = "";
					if(content!=null && content.trim().length()>0){
						String contents[]=content.split("<!--separator-->");
						if(contents.length>0){
							imageUrl=contents[0];
						}
						if(contents.length>1){
							url=contents[1];
						}
					}
					ri.setContUrl(url);
					ri.setImageUrl(imageUrl);
					ri.setType(type);
					ri.setTitle("外链资源");
				}else{
					//根据资源id和type获取资源
					ri = getResourceByIdAndType(id, type, uid);
					actUtils.putLinkRemarkToResource(ri, rl);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("运营资源显示出错，资源类型["+rl.getLinkType()+"]ID为："+rl.getResLinkId()+e.getMessage(), e.fillInStackTrace());
				ri = new ResourceInfo();
			}
		}else if(ResourceInfo.class.getName().equals(objName)){
			ri = (ResourceInfo) obj;
			try {
				//begin = System.currentTimeMillis();
				ri = putInListToResource(ri,uid);
				//long end = System.currentTimeMillis();
//				System.out.println("书单影单是否添加list所需时间："+(end - begin));
			} catch (Exception e) {
				LOG.error("资源显示出错，资源类型["+ri.getType()+"]ID为："+ri.getRid()+e.getMessage(), e.fillInStackTrace());
				ri = new ResourceInfo();
			}
		}else if(BookList.class.getName().equals(objName)){
			//已废弃
//			ri = bookUtils.putBookListToResource((BookList) obj, getResourceInfoFacade, ucenterFacade, actFacade, bkFacade, uid);
			try {
				ri = fileUtils.putObjectToResource((BookList) obj, ucenterFacade, actFacade);
			} catch (Exception e) {
				LOG.error("书单资源限时出错，资源id为："+((BookList) obj).getId()+e.getMessage(), e.fillInStackTrace());
				ri = new ResourceInfo();
			}
		}else if(MovieList.class.getName().equals(objName)){
			//已废弃
//			ri = movieUtils.putMovieListToResource(obj, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
			try {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade, actFacade);
			} catch (Exception e) {
				LOG.error("影单资源限时出错，资源id为："+((MovieList) obj).getId()+e.getMessage(), e.fillInStackTrace());
				ri = new ResourceInfo();
			}
		}else if(GraphicFilm.class.getName().equals(objName)){
			try {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade);
			} catch (Exception e) {
				LOG.error("图解电影资源限时出错，资源id为："+((GraphicFilm) obj).getId()+e.getMessage(), e.fillInStackTrace());
				ri = new ResourceInfo();
			}
		}else if(ActCollect.class.getName().equals(objName)){
			ri = putActCollectToResource((ActCollect) obj, uid);
		}else if(Topic.class.getName().equals(objName)){
			try{
				ri = fileUtils.putObjectToResource(obj, ucenterFacade);
			}catch(Exception e){
				e.printStackTrace();
				LOG.error("话题资源出错，资源id为："+((Topic) obj).getId()+e.getMessage(), e.fillInStackTrace());
				ri = new ResourceInfo();
			}
		}
		try {
			//将朋友的朋友名字加进资源类中
//			userUtils.putFriendNameInResourceInfo(ri, uid, ucenterFacade);
			//将各种判定加入资源类
			actUtils.putIsCollectToResoure(ri, uid, actFacade,isList);
//			LOG.info("resource添加附加信息所需时间："+(end - begin));
//			long begin = System.currentTimeMillis();
//			if(ri != null && ri.getRid() != 0){
//				int readingCount = getResourceReadCount(ri.getRid(), ri.getType());
//				ri.setReadingCount(readingCount);
//			}
//			long end = System.currentTimeMillis();
//			System.out.println("resource添加阅读量所需时间："+(end - begin));
			//将更新各种附加数据的resource加入缓存中
//			jedisManager.setJedisResourceList(ri);
		} catch (Exception e) {
			LOG.error("添加附加内容时出错，资源类型["+ri.getType()+"]id为:"+ri.getRid()+e.getMessage(), e.fillInStackTrace());
			ri = new ResourceInfo();
		}

		//0：代表是返回列表，1：代表某个资源
		if(isList == 1){
			try {
				if(ri != null && ri.getRid() != 0 && !CommentUtils.TYPE_NEWARTICLE.equals(ri.getType()) && !CommentUtils.TYPE_TOPIC.equals(ri.getType())){
					int readingCount = getResourceReadCount(ri.getRid(), ri.getType());
					ri.setReadingCount(readingCount+"");
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			//将打赏总金额都放入到资源类中
			try {
				//begin = System.currentTimeMillis();
				actUtils.putMoneyToResource(ri, paycenterFacade);
				//long end = System.currentTimeMillis();
//				System.out.println("resource添加打赏数量所需时间："+(end - begin));
			} catch (Exception e) {
				LOG.error("添加资源获取打赏时出错，资源id为:"+ri.getRid()+e.getMessage(), e.fillInStackTrace());
			}
			//排除长文章的类型，长文章用的自己的阅读数
			if(!CommentUtils.TYPE_NEWARTICLE.equals(ri.getType()) && !CommentUtils.TYPE_TOPIC.equals(ri.getType())){
				int readCount = resStatJedisManager.getReadNum(ri.getRid(), ri.getType());
				ri.setReadingCount(readCount+"");
			}
			if(CommentUtils.TYPE_BOOKLIST.equals(ri.getType())){
				//从缓存中获取书单被收藏的数量
				int count =0;
				try {
					count = getResourceCollectCountFromJedis(ri.getRid(), CommentUtils.TYPE_BOOKLIST);
				} catch (Exception e) {
					count =0;
					//LOG.error("获取电影被收藏数量出错，id为："+bookListInfo.getId()+e.getMessage(), e.fillInStackTrace());
				}
				ri.setfNum(count);
			}else if(CommentUtils.TYPE_MOVIELIST.equals(ri.getType())){
				//从缓存中获取电影单被收藏的数量
				int count =0;
				try {
					count = getResourceCollectCountFromJedis(ri.getRid(), CommentUtils.TYPE_MOVIELIST);
				} catch (Exception e) {
					count =0;
					//LOG.error("获取电影被收藏数量出错，id为："+bookListInfo.getId()+e.getMessage(), e.fillInStackTrace());
				}
				ri.setfNum(count);
			}
		}

		return ri;
	}
	
	/**
	 *
	 * <p>Title: putCollectObjectToResource</p>
	 * <p>Description: 将收藏的资源转化为resourceInfo</p>
	 * @author :changjiang
	 * date 2015-5-13 下午12:25:45
	 * @param obj
	 * @param uid
	 * @param isList
	 * @return
	 */
	public ResourceInfo putCollectObjectToResource(Object obj,Long uid,int isList){
		long begin = System.currentTimeMillis();
		ResourceInfo ri = new ResourceInfo();
		if(obj == null || uid == 0){
			return ri;
		}
		String objName = obj.getClass().getName();
		if(ActCollect.class.getName().equals(objName)){
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
			UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);

			//ri = putActCollectToResource((ActCollect) obj, uid);
			ActCollect actCollect = (ActCollect) obj;
			ri.setRid(actCollect.getId());
			ri.setType(CommentUtils.TYPE_COLLECT);
			ri.setBtime(actCollect.getLatestRevisionDate());
			ri.setIsCollect(1);
			ri.setUserEntity(userEntity);
			ResourceInfo fri = new ResourceInfo();
			String type = actCollect.getType();
			if(type.equals(CommentUtils.TYPE_BOOKLIST)){//当为书单时
				BookList bList = getResourceInfoFacade.queryByIdBookList(actCollect.getResourceId());
				fri.setRid(bList.getId());
				fri.setBookListId(bList.getId());
				fri.setTitle(bList.getBookListName());
				fri.setImageUrl(bList.getCover());
				fri.setBtime(bList.getLatestRevisionDate());
				Map<String, Object> map = getResourceInfoFacade.findBookLinkCount(bList.getId());
				int count = (Integer) map.get("count");
				fri.setSize(count);
			}else if(type.equals(CommentUtils.TYPE_MOVIELIST)){//当为影单是
				MovieList movieList = myMovieFacade.findMovieListById(actCollect.getResourceId());
						//queryByIdBookList(actCollect.getResourceId());
				fri.setRid(movieList.getId());
				fri.setMovieListId(movieList.getId());
				fri.setTitle(movieList.getFilmListName());
				fri.setImageUrl(movieList.getCover());
				fri.setBtime(movieList.getLatestRevisionDate());
				Map<String, Object> map = myMovieFacade.findMovieLinkCount(movieList.getId());
				int count = (Integer) map.get("count");
				fri.setSize(count);
			}
			fri.setType(type);
			fri.setUserEntity(userEntity);
			ri.setResourceInfo(fri);
		}
		return ri;
	}
	
	private ResourceInfo putInListToResource(ResourceInfo ri,Long uid){
		if(ri == null || ri.getRid() == 0){
			return ri;
		}
		try {

			if(CommentUtils.TYPE_BOOK.equals(ri.getType())
					||CommentUtils.TYPE_BOOK_COMMENT.equals(ri.getType())
					||CommentUtils.TYPE_BOOKLIST.equals(ri.getType())){
				BookInfo bookInfo = ri.getBookInfo();
				if(bookInfo != null && bookInfo.getId() != 0){
					//是否在我的书单中
					BookListLink bookListLink = getResourceInfoFacade.findUserIsCollectBook(uid, bookInfo.getId(), bookInfo.getType());//(uid, bookInfo.getId());
					if(bookListLink.getId() != 0){
						ri.getBookInfo().setInList(0);
					}else{
						ri.getBookInfo().setInList(1);
					}
				}
			}else if(CommentUtils.TYPE_MOVIE.equals(ri.getType())
					||CommentUtils.TYPE_MOVIE_COMMENT.equals(ri.getType())
					|| CommentUtils.TYPE_MOVIELIST.equals(ri.getType())){
				MovieInfo movieInfo = ri.getMovieInfo();
				if(movieInfo  != null && movieInfo.getId() != 0){
					//是否在我影单中
					MvListLink mvListLink = myMovieFacade.findUserIsCollectMovie(uid, movieInfo.getId());
					if(mvListLink.getId() != TRUE){
						ri.getMovieInfo().setInList(TRUE);
					}else{
						ri.getMovieInfo().setInList(1);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}



		return ri;
	}
	
	/**
	 * 将转发的资源格式化
	 * @param actTransmit
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitToResource(ActTransmit actTransmit,Long uid){
		ResourceInfo ri = new ResourceInfo();

		if(actTransmit == null || actTransmit.getId() == 0){
			return ri;
		}
		String aType = actTransmit.getType();
		if(CommentUtils.TYPE_BOOKLIST.equals(aType)){
			ri = actUtils.putActTransmitBookListToResourceInfo(actTransmit, ucenterFacade, actFacade, bkFacade, getResourceInfoFacade,netBookFacade, uid);
		}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(aType)){
			ri = actUtils.putActTransmitBKCommentToResourceInfo(actTransmit, ucenterFacade, actFacade, bkCommentFacade, bkFacade, getResourceInfoFacade,netBookFacade, uid);
		}else if(CommentUtils.TYPE_MOVIELIST.equals(aType)){
			ri = actUtils.putActTransmitMovieListToResourceInfo(actTransmit, ucenterFacade, actFacade, mvCommentFacade, mvFacade, myMovieFacade, uid);
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(aType)){
			ri = actUtils.putActTransmitMVCommentToResourceInfo(actTransmit, ucenterFacade, actFacade, mvCommentFacade, mvFacade, myMovieFacade, uid);
		}else if(CommentUtils.TYPE_DIARY.equals(aType)){
			ri = actUtils.putActTransmitDiaryToResourceInfo(actTransmit, ucenterFacade, actFacade, diaryFacade, uid);
		}else if(CommentUtils.TYPE_ARTICLE.equals(aType)){
			ri = actUtils.putActTransmitPostToResourceInfo(actTransmit, ucenterFacade, actFacade, postFacade, uid);
		}else if(CommentUtils.TYPE_NEWARTICLE.equals(aType)){
			ri = actUtils.putActTransmitArticleToResourceInfo(actTransmit, ucenterFacade, actFacade, articleFacade, uid);
		}

		return ri;
	}

	/**
	 * 将收藏类型格式化
	 * @param actCollect
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActCollectToResource(ActCollect actCollect,long uid){
		ResourceInfo si = new ResourceInfo();
		if(actCollect == null || actCollect.getId() == 0 || uid == 0){
			return si;
		}
		try {
			String type = actCollect.getType();
			if(CommentUtils.TYPE_BOOKLIST.equals(type)){
				si = actUtils.putCollectBookListToResource(actCollect, ucenterFacade, actFacade, getResourceInfoFacade, bkFacade,netBookFacade,uid);
			}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){
				si = actUtils.putCollectMovieListToResource(actCollect, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
			}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
				si = actUtils.putCollectBkCommentToResource(actCollect, ucenterFacade, actFacade, bkFacade, bkCommentFacade,netBookFacade,getResourceInfoFacade,uid);
			}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
				si = actUtils.putCollectMvCommentToResource(actCollect, ucenterFacade, actFacade, mvFacade, mvCommentFacade,myMovieFacade,uid);
			}else if(CommentUtils.TYPE_ARTICLE.equals(type)){
				si = actUtils.putCollectPostToResource(actCollect, ucenterFacade, actFacade, postFacade, uid);
			}else if(CommentUtils.TYPE_NEWARTICLE.equals(type)){
				si = actUtils.putCollectArticleToResource(actCollect, ucenterFacade, actFacade, articleFacade, uid);
			}else if(CommentUtils.TYPE_DIARY.equals(type)){
				si = actUtils.putCollectDiaryToResource(actCollect, ucenterFacade, actFacade, diaryFacade, uid);
			}else if(CommentUtils.TYPE_ACT.equals(type)){
				ActTransmit actTransmit = actFacade.findOneTransmit(actCollect.getResourceId());
				si = resourceManager.putActTransmitToResource(actTransmit, uid);
				ResourceInfo fri = new ResourceInfo();
				fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
				fri.setResourceInfo(si);
				si = fri;
//				si = actUtils.putCollectToResource(si, (ActCollect)object, ucenterFacade);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("收藏内容显示出错，资源id为："+actCollect.getId(), e);
		}

		return si;
	}

	/**
	 * 评论列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String commentList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
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
		//int praiseSize=0;
		//去掉空格
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
//		System.out.println(req);



		List<CommentInfo> commentInfos = new ArrayList<CommentInfo>();
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
//			//评论的回复
//			Iterator<ActComment> iter = comList.iterator();
//			while(iter.hasNext()){
//				ActComment actComment = iter.next();
//				comList = actFacade.findResCommentList(null, actComment.getId(), null);//(null, id);
//			}
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

		//对资源进行倒序排列
		Collections.sort(commentInfos);


		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", commentInfos);
			datas.put("totalNumber", totalNumber+"");
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
	 * 将评论列表格式化
	 * @param list
	 * @param type
	 * @return
	 */
	public List getCommentList(List reqList , Long uid, List<CommentInfo> resList){
		CommentInfo ci = null;
		if(reqList.size()>0){
			flagint = ResultUtils.SUCCESS;
			for (Object obj : reqList) {
				//未注册的话用指定ID
				ci = fileUtils.putObjToComment(obj, ucenterFacade,actFacade,uid);
				if(ci.getId() != 0){
					resList.add(ci);
				}
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
//		else{
//			ActComment obj = reqList.get(0);
//			flagint = obj.getFlag();
//			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
//				long id = obj.getId();
//				if(id != 0){
//					//未注册的话用指定ID
//					ci = fileUtils.putObjToComment(obj, ucenterFacade,actFacade,uid);
//					resList.add(ci);
//				}
//			}
//		}
		return resList;
	}

	/**
	 * 将转发或@资源格式化
	 * @param list
	 * @param type
	 * @return
	 */
	public List getActResponseList(List<MsgAt> reqList ,Long uid, List<ResourceInfo> resList){
		ResourceInfo ri = new ResourceInfo();
		if(reqList.size()>0){
			Long id = reqList.get(0).getAtId();
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (MsgAt ma : reqList) {
					String type = ma.getType();
					if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
						ri = actUtils.putActMovieCommentToResourceInfo(ma, ucenterFacade, mvCommentFacade, mvFacade, actFacade,myMovieFacade,uid);
					}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
						ri = actUtils.putActBookCommentToResourceInfo(ma, ucenterFacade, bkCommentFacade, actFacade, bkFacade, myBkFacade,getResourceInfoFacade,netBookFacade,uid);
					}
					resList.add(ri);
				}
			}

		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}

	/**
	 * 赞资源
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String praise(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
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
		}
//		System.out.println(req);


		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");

		Long id = Long.valueOf(dataq.get("id").toString());
		String type = (String) dataq.get("type");
		String status = (String) dataq.get("status");
		Long toUid = 0l;
		try {
			toUid = Long.valueOf(dataq.get("toUid").toString());
			if(toUid == null){
				toUid = 0l;
			}
		} catch (Exception e) {
			toUid = 0l;
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

		ActPraise act = null;
		if(CommentUtils.COMMENT_PARISE.equals(status)){
			act = actFacade.doPraise(null, uid, id, type,res_userid);
			/*if(act.getIsPraise()==1&&act.getFlag()==ResultUtils.SUCCESS){
				resStatJedisManager.updateActPraiseNum(id, type, act);
			}*/
			//添加用户的点赞的数量缓存
			//对资源的评论点赞去除加一
			if(0l!=res_userid&&!CommentUtils.TYPE_COMMENT.equals(type)){
				userJedisManager.incrOneUserInfo(res_userid, JedisConstant.USER_HASH_PRAISE_NOTICE);
			}
		}else if(CommentUtils.COMMENT_NOTPARISE.equals(status)){
			act = actFacade.cancelPraise(null, uid, id, type);
			/*if(act.getIsPraise()==0&&act.getFlag()==ResultUtils.SUCCESS){
				resStatJedisManager.updateActPraiseNum(id, type, act);
			}*/
		}

		int num = 0;
		flagint = act.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			num = actFacade.findPraiseCount(null, id);
//			num = resStatJedisManager.getActPraiseNum(id, type);
		}
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){

			if(act.getType().equals(CommentUtils.TYPE_BOOK_COMMENT)){
				BkComment bkComment = bkCommentFacade.findCommentIsExistById(act.getResourceId());
				if(null!=bkComment&&bkComment.getId()!=0){
					//更细点赞的数量
					//System.out.println("书评点赞更新");

					resStatJedisManager.updateActPraiseNum(id, type, act,bkComment.getBookId(),bkComment.getResType());
				}
			}else if(act.getType().equals(CommentUtils.TYPE_MOVIE_COMMENT)){
				MvComment mvComment = mvCommentFacade.findMvCommentIsExist(act.getResourceId());
				if(null!=mvComment&&mvComment.getFlag()==ResultUtils.SUCCESS){
					//更细点赞的数量
					//System.out.println("影评点赞更新");
					resStatJedisManager.updateActPraiseNum(id, type, act,mvComment.getMovieId(),CommentUtils.TYPE_MOVIE);
				}
			}

			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("num", num);

			//TODO: 将评论后的附加信息放入缓存中
			try {
//				int cNum = actFacade.findCommentCount(null, id);
				updateIsPraiseByJedis(id, uid, JedisConstant.RELATION_IS_PRAISE, status);
				updateResourceByJedis(id, uid, JedisConstant.RELATION_PRAISE_NUM, num+"");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}

			//推送消息
			if(CommentUtils.COMMENT_PARISE.equals(status)){
				//推送消息
				long begin = System.currentTimeMillis();
				try {
					begin = System.currentTimeMillis();
					JSONObject json = new JSONObject();
					json.put("uid", uid);
					json.put("toUid", toUid);
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
				long end = System.currentTimeMillis();
				//System.out.println("异步赞耗时："+(end-begin));
			}
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
		List<Map<String, String>> oList = new ArrayList<Map<String,String>>();//话题列表等信息
		//List commentList = null;

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
		String content = (String) dataq.get("content");

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
		oList = WebUtils.checkIsAt(content, oList, ucenterFacade);
		String resultStr = WebUtils.putDataToNode(content, "0", oList);
		ActComment actComment = new ActComment();
		actComment = actFacade.doOneComment(null, uid, id, type, resultStr,commentUid,commentId,res_userid);
		flagint = actComment.getFlag();
//		//如果有@消息，将此消息存入表中，并推送给相关的人
//		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
//			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
//			String uname = userAllInfo.getName();
//
//			//收集被act人的名字并推送消息
//			List<String> actNames = WebUtils.actNameList;
//			for (String string : actNames) {
//				System.out.println("成功了:"+string);
//				UserInfo userInfo = ucenterFacade.findUserInfoByName(string);
//				String token = userInfo.getPushToken();
//
//				String msgType = "";
//				if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
//					msgType = CommentUtils.TYPE_RESOURCEINFO_MOVIE;
//				}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
//					msgType = CommentUtils.TYPE_RESOURCEINFO_BOOK;
//				}
//
//				MsgAt msgAt = msgFacade.doAct(null, uid, userInfo.getUserId(), actComment.getId(), msgType);
//
//				flagint = msgAt.getFlag();
//				if(flagint == ResultUtils.SUCCESS){
//					MainSend mainSend = new MainSend();
//					mainSend.sendMsgToStore(uname+CommentUtils.ACT_MESSAGE, token);
//				}
//			}
//		}
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
//			//评论后的资源放入到缓存中
//			if(!CommentUtils.TYPE_COMMENT.equals(type)){
//				ResourceInfo resourceInfo = getResourceByIdAndType(id, type, uid);
//				actUtils.putIsCollectToResoure(resourceInfo, uid, actFacade, 0);
//				resourceJedisManager.saveOneResource(id, resourceInfo);
//			}
			try{
				//如果话题和评论的资源有关联，则更新话题和资源的关联表的更新时间
				TopicLink topicLink = new TopicLink();
				topicLink.setResid(id);
				topicLink.setLatestRevisionDate(System.currentTimeMillis());
				topicFacade.updateTopicLinkLatestrevisiondateByResid(topicLink);
			}catch(Exception e){
				e.printStackTrace();
			}
			/*try{
				//用户的互动评论任务操作，字数需要大于20字
				if(actComment.getFlag()==ResultUtils.SUCCESS|| actComment.getFlag() == CommentUtils.UN_ID){
					if(actComment.getCommentContext()!=null && actComment.getCommentContext().length()>=20){
						addQuestComment(uid);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}*/
			if(null!=oList&&oList.size()>0){
				long rid = actComment.getId();
				String resType = CommentUtils.TYPE_COMMENT;
				Iterator<Map<String, String>> oListIt = oList.iterator();
				String topicName = "";
				String oListType = "";
				Long toUid = 0l;
				while(oListIt.hasNext()){
					Map<String, String> oneListmap = oListIt.next();
					topicName = oneListmap.get("name");
					oListType = oneListmap.get("type");
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
			}

			//推送消息
			long begin = System.currentTimeMillis();
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
			System.out.println("异步评论耗时："+(end-begin));
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
	//互动评论任务--新增互动评论的操作
	/*private void addQuestComment(Long uid){
		try{
			//是否达到每日任务，是否达到累计任务,需要加入小红点
			boolean questfinish = false;//是否有任务完成
			boolean dayquestfinish = false;//是否有每日任务完成
			QuestProgress dayquestProgress = questFacade.getQuestProgressByUseridAndType(uid, QuestInfo.type_day_comment);//每日的任务进程
			QuestProgress questProgress = questFacade.getQuestProgressByUseridAndType(uid, QuestInfo.type_comment);//累计任务进程
			
			String date = DateUtil.getDate(System.currentTimeMillis()-5*60*60*1000L);
			List<QuestReward> questRewards = questFacade.getQuestRewardByUseridAndType(uid, QuestReward.type_sum, null);//任务累计打赏
			List<QuestReward> dayquestRewards = questFacade.getQuestRewardByUseridAndType(uid, QuestReward.type_daily, date);//当日打赏
			QuestReward questReward = null;
			QuestReward dayquestReward = null;
			if(questRewards!=null && questRewards.size()>0){
				questReward = questRewards.get(0);
			}else{
				questReward = new QuestReward();
				questReward.setCreatetime(System.currentTimeMillis());
				questReward.setDate("");
				questReward.setReward(0);
				questReward.setType(QuestReward.type_sum);
				questReward.setUpdatetime(System.currentTimeMillis());
				questReward.setUserid(uid);
			}
			if(dayquestRewards!=null && dayquestRewards.size()>0){
				dayquestReward = dayquestRewards.get(0);
			}else{
				dayquestReward = new QuestReward();
				dayquestReward.setCreatetime(System.currentTimeMillis());
				dayquestReward.setDate(date);
				dayquestReward.setReward(0);
				dayquestReward.setType(QuestReward.type_daily);
				dayquestReward.setUpdatetime(System.currentTimeMillis());
				dayquestReward.setUserid(uid);
			}
			
			
			String rewardUserId = uid+"";
			String rewardUserName = "";
			String sourceId = uid+"";
			String sourceName = "";
			String sourceType = CommentUtils.TYPE_USER;
			if(dayquestProgress!=null && dayquestProgress.getId()>0){
				//存在每日任务进度，需要判断是否达到每日任务的最大值
				if(date.equals(dayquestProgress.getDate())){
					//是今天，进度加1
					dayquestProgress.setProgress(dayquestProgress.getProgress()+1);
				}else{
					//不是今天，进度置为今天的1，日期改为今天
					dayquestProgress.setProgress(1);
					dayquestProgress.setDate(date);
				}
				questFacade.updateQuestProgress(dayquestProgress);
				//需要查询每日任务的信息（最大进度值，打赏金额）
				Quest quest = questFacade.getQuest(QuestInfo.type_day_comment);
				if(quest!=null && quest.getId()>0){
					//只有进度值等于最大进度值的时候才打赏
					if(dayquestProgress.getProgress()==quest.getMaxProgress()){
						int totalFee = quest.getReward();
						paycenterManager.rewardUser(rewardUserId, rewardUserName, sourceId, sourceName, sourceType, totalFee);
						questReward.setReward(questReward.getReward()+quest.getReward());
						dayquestReward.setReward(dayquestReward.getReward()+quest.getReward());
						dayquestfinish=true;
					}
				}
			}else{
				//不存在每日任务进度，需要保存一个任务进度
				dayquestProgress = new QuestProgress();
				dayquestProgress.setCreatetime(System.currentTimeMillis());
				dayquestProgress.setDate(date);
				dayquestProgress.setProgress(1);
				dayquestProgress.setType(QuestInfo.type_day_comment);
				dayquestProgress.setUpdatetime(System.currentTimeMillis());
				dayquestProgress.setUserid(uid);
				questFacade.insertQuestProgress(dayquestProgress);
			}
			
			if(questProgress!=null && questProgress.getId()>0){
				//存在累计任务进度，需要判断是否达到累计任务的最大值
				questProgress.setProgress(questProgress.getProgress()+1);
				//需要查询累计任务的信息（最大进度值，打赏金额）
				Quest quest = questFacade.getQuest(QuestInfo.type_comment);
				if(quest!=null && quest.getId()>0){
					//需要判断是否达到了最大值
					if(questProgress.getProgress()==quest.getMaxProgress()){
						//达到最大值了，进行打赏和初始化累计进度
						//questProgress.setProgress(0);//===========================================重新循环累计任务的操作
						int totalFee = quest.getReward();
						paycenterManager.rewardUser(rewardUserId, rewardUserName, sourceId, sourceName, sourceType, totalFee);
						questReward.setReward(questReward.getReward()+quest.getReward());
						dayquestReward.setReward(dayquestReward.getReward()+quest.getReward());
						questfinish=true;
					}else if(questProgress.getProgress()>quest.getMaxProgress()){
						//超过了最大值，证明异常了，初始化
						//questProgress.setProgress(0);//===========================================重新循环累计任务的操作
					}
				}
				questFacade.updateQuestProgress(questProgress);
			}else{
				//不存在累计任务进度，需要保存一个任务进度
				questProgress = new QuestProgress();
				questProgress.setCreatetime(System.currentTimeMillis());
				questProgress.setDate("");
				questProgress.setProgress(1);
				questProgress.setType(QuestInfo.type_comment);
				questProgress.setUpdatetime(System.currentTimeMillis());
				questProgress.setUserid(uid);
				questFacade.insertQuestProgress(questProgress);
			}
			//有任务完成，增加小红点
			if(questfinish || dayquestfinish){
				//更新任务打赏金额记录
				if(questReward.getId()>0){
					//更新打赏金额
					questFacade.updateQuestReward(questReward);
				}else{
					//插入记录
					questFacade.insertQuestReward(questReward);
				}
				if(dayquestReward.getId()>0){
					//更新打赏金额
					questFacade.updateQuestReward(dayquestReward);
				}else{
					//插入记录
					questFacade.insertQuestReward(dayquestReward);
				}
				if(questfinish){
					//累计任务的完成数量增加
					userJedisManager.incrOneUserInfo(uid, JedisConstant.USER_QUEST_COUNT);
				}
				if(dayquestfinish){
					//每日任务完成
					userJedisManager.saveOneUserInfo(uid, JedisConstant.USER_DAY_QUEST_COUNT,DateUtil.getDate(System.currentTimeMillis()-5*60*60*1000L));
				}
			}
		}catch(Exception e){
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}*/
	/**
	 * 根据id和type获取资源实体类
	 * @param id
	 * @param type
	 * @return
	 */
	public ResourceInfo getResourceByIdAndType(long id,String type,Long uid){
		ResourceInfo ri = new ResourceInfo();
		if(CommentUtils.TYPE_BOOKLIST.equals(type)){
			BookList bookList = getResourceInfoFacade.queryByIdBookList(id);
			//已废弃
//			ri = bookUtils.putBookListToResource(bookList, getResourceInfoFacade, ucenterFacade, actFacade, bkFacade, uid);
			ri = fileUtils.putObjectToResource(bookList, ucenterFacade, actFacade);
		}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){
			MovieList movieList = myMovieFacade.findMovieListById(id);
			//已废弃
//			ri = movieUtils.putMovieListToResource(movieList, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
			ri = fileUtils.putObjectToResource(movieList, ucenterFacade, actFacade);
		}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
			BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
			ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,netBookFacade,uid);
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
			MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);
			ri = movieUtils.putMVCommentToResource(mvComment, ucenterFacade, actFacade, mvFacade,myMovieFacade,uid);
		}else if(CommentUtils.TYPE_DIARY.equals(type)){
			Diary diary = diaryFacade.queryByIdDiary(id);
			ri = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
		}else if(CommentUtils.TYPE_ARTICLE.equals(type)){
			Post post = postFacade.queryByIdName(id);
			ri = fileUtils.putObjectToResource(post, ucenterFacade, actFacade);
		}else if(CommentUtils.TYPE_NEWARTICLE.equals(type)){
			Article article = articleFacade.queryArticleById(id);
			ri = fileUtils.putObjectToResource(article, ucenterFacade, actFacade);
		}else if (CommentUtils.TYPE_BOOK.equals(type)){
			BkInfo bkInfo = bkFacade.findBkInfo((int)id);
			ri = bookUtils.putBkInfoToResource(bkInfo);
		}else if(CommentUtils.TYPE_MOVIE.equals(type)){
			MvInfo mvInfo = mvFacade.queryById(id);
			ri = movieUtils.putMVInfoToResource(mvInfo);
		}else if(CommentUtils.TYPE_GRAPHIC_FILM.equals(type)){
			GraphicFilm graphicFilm = graphicFilmFacade.findGraphicFilmById(id);
			ri = fileUtils.putObjectToResource(graphicFilm, ucenterFacade);
		}else if(CommentUtils.TYPE_TOPIC.equals(type)){
			//话题
			Topic topic = topicFacade.findTopicByID(id);
			ri = fileUtils.putObjectToResource(topic, ucenterFacade);
		}else if(CommentUtils.TYPE_ARTICLE_BOOK.equals(type)){
			//长书评
			BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
			ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,netBookFacade,uid);
		}else if(CommentUtils.TYPE_ARTICLE_MOVIE.equals(type)){
			//长影评
			MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);
			ri = movieUtils.putMVCommentToResource(mvComment, ucenterFacade, actFacade, mvFacade,myMovieFacade,uid);
		}else if(CommentUtils.TYPE_USER.equals(type)){
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null,id);
			ri = fileUtils.putUserInfoToResource(userAllInfo,ucenterFacade,uid);
		}else if(CommentUtils.TYPE_BOOK_DIGEST.equals(type)){
			//书摘(关于一本书的文字内容)
			BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
			ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,netBookFacade,uid);
		}

		//添加是否看过
		Map<String, String> relationInfo = relationToUserAndResManager.getOneRelation(uid, id);
		if(null!=relationInfo&&!relationInfo.isEmpty()){//当关系不为空时
			String isReadStr = relationInfo.get(JedisConstant.RELATION_IS_READ);
			if(null!=isReadStr&&!"".equals(isReadStr)){
				ri.setIsRead(1);
			}
		}


		return ri;
	}

	/**
	 * 转发资源 修改，去掉加入缓存
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String repeat(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		List content = null;
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
		content = (List) dataq.get("list");


		String cont = WebUtils.putDataToHTML5(content);
		//过滤敏感词汇
//		cont = getFilterContent(cont, uid, id, type);

		ActTransmit actTransmit = actFacade.doActTransmit(null, uid, id, type, cont);

		flagint = actTransmit.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将生成的转发信息存入到缓存中
			//resourceManager.setResourceToJedis(actTransmit, uid,uid,0l);


			//增加用户转发数量
			try {
				userStatisticsFacade.updateTransmitCount(uid);
			} catch (Exception e) {
				LOG.error("增加用户转发数量:"+e.getMessage(), e.fillInStackTrace());
			}
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
	 * 首页显示达人圈
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String indexTalentZone(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";

		//去掉空格
		reqs = reqs.trim();

		long talentZoneId = 0;
		Long lastId = null;
		String type = "";


		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			talentZoneId = Long.valueOf(dataq.get("talentZoneId").toString());
			lastId = Long.valueOf(dataq.get("lastId").toString());
			if(lastId == UNID){
				lastId = null;
			}
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);



		List<TalentZoneLink> talentZoneLinks = new ArrayList<TalentZoneLink>();
		List<BkComment> bkComments = new ArrayList<BkComment>();
		List<MvComment> mvComments = new ArrayList<MvComment>();
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();



		//达人圈内名人
		talentZoneLinks = ucenterFacade.findTalentZoneLinkList(talentZoneId);

		//达人圈内发布的书相关的
		if(talentZoneLinks.size()>0 && talentZoneLinks.get(0).getId() != 0){
			for (TalentZoneLink link : talentZoneLinks) {
				if(CommentUtils.TYPE_TALENTZONE_BOOK.equals(type)){
					if(bkComments.size() == 0){
						bkComments = bkCommentFacade.findAllBkCommentListByType(link.getUserId(), CommentUtils.TYPE_TALENTZONE_BOOK, lastId);
					}else{
						bkComments.addAll(bkCommentFacade.findAllBkCommentListByType(link.getUserId(), CommentUtils.TYPE_TALENTZONE_BOOK, lastId));
					}
				}else if(CommentUtils.TYPE_TALENTZONE_MOVIE.equals(type)){
					if(mvComments.size() == 0){
						mvComments = mvCommentFacade.findAllMvCommentListByType((long)link.getUserId(), CommentUtils.TYPE_TALENTZONE_MOVIE, lastId);
					}else{
						mvComments.addAll(mvCommentFacade.findAllMvCommentListByType((long)link.getUserId(), CommentUtils.TYPE_TALENTZONE_MOVIE, lastId));
					}
				}
			}
		}

		//将类型转换
		if(CommentUtils.TYPE_TALENTZONE_BOOK.equals(type)){
			resourceInfos = getResponseList(bkComments, uid, resourceInfos);
		}else if(CommentUtils.TYPE_TALENTZONE_MOVIE.equals(type)){
			resourceInfos = getResponseList(mvComments, uid, resourceInfos);
		}



		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
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
	 * 将各种资源放入到缓存中
	 * @param obj
	 * @param uid
	 */
	public int setResourceToJedis(Object obj,long uid,long jedisUid,Long vid){
		int flag = ResultUtils.ERROR;
		ResourceInfo resourceInfo = putObjectToResource(obj, uid,1);
		if(resourceInfo != null && resourceInfo.getRid() != 0){
			//flag = jedisManager.setJedisResourceList(resourceInfo);
//			flag = jedisManager.setJedisResources(resourceInfo);
			long fuid = jedisUid;
			long frid = resourceInfo.getRid();
			/*UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null, uid);
			int level = uInfo.getLevel();
			System.out.println("用户信息的等级为"+level);
			if(level==50){//添加神人广场
				momentJedisManager.saveOneShenRenMoment(frid);
			}else{//添加普通人广场
				momentJedisManager.saveOneCommonMoment(frid);
			}*/
//			System.out.println("插入前缓存："+frid);
			String resourceType = resourceInfo.getType();
			//个人朋友圈存一条
			//if(!resourceType.equals(CommentUtils.TYPE_ARTICLE_BOOK)&&!resourceType.equals(CommentUtils.TYPE_ARTICLE_MOVIE)){
				String StrId = momentJedisManager.saveOneItem(fuid, frid,vid);
			//}
//			System.out.println("插入缓存："+StrId);
			String uidstr = frid+"";
//			if(uidstr.equals(StrId)){
			long ruid = 0;
			try {
				UserEntity userEntity = resourceInfo.getUserEntity();
				if(userEntity != null){
					ruid = userEntity.getId();
					/*if(userEntity.getType()==50){//添加神人广场
						momentJedisManager.saveOneShenRenMoment(frid,vid);
						System.out.println("添加神人广场");
					}else{//普通人添加普通人广场
						momentJedisManager.saveOneCommonMoment(frid,vid);
					}*/
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			//添加广场
			if(!resourceType.equals(CommentUtils.TYPE_ARTICLE_BOOK)&&!resourceType.equals(CommentUtils.TYPE_ARTICLE_MOVIE)) {
				momentJedisManager.saveOneItem(0, frid, vid);
			}
			if(uid == ruid){//个人动态
				if(!resourceType.equals(CommentUtils.TYPE_ARTICLE_BOOK)&&!resourceType.equals(CommentUtils.TYPE_ARTICLE_MOVIE)) {
					String iUid = momentJedisManager.saveOneDynamic(fuid, frid,vid);
				}
			}
			String result = resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
//			}

			System.out.println("开始调用异步处理");
			try {
				//System.out.println("异步处理推送朋友圈和广场"+uid+"朋友的id"+frid);
				JSONObject json = new JSONObject();
				json.put("uid", uid);
				json.put("frid", frid);
				json.put("vid", vid);
				json.toString();
				//if(!resourceType.equals(CommentUtils.TYPE_ARTICLE_BOOK)&&!resourceType.equals(CommentUtils.TYPE_ARTICLE_MOVIE)) {
					eagleProducer.send("pushMoments", "toFans", uid + "", json + "");
				//}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}

			/*long id = resourceInfo.getRid();
			if(CommentUtils.TYPE_BOOKLIST.equals(resourceInfo.getType())){
				id = resourceInfo.getBookListId();
			}else if(CommentUtils.TYPE_MOVIELIST.equals(resourceInfo.getType())){
				id = resourceInfo.getMovieListId();
			}

			//推送小红点
			List<UserInfo> userInfos = fansListManager.getFansList(uid);
			List<Long> ids = new ArrayList<Long>();
			for (UserInfo userInfo : userInfos) {
				ids.add(userInfo.getUserId());
			}


			//推送小红点
				try {
					//pushManager.pushMomentMSG(uid, ids, id, resourceInfo.getType());


				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}*/
	//		}
		}
		return flag;
	}

	/**
	 * 将各种资源从缓存中删除
	 * @param obj
	 * @param uid
	 */
	public int delResourceFromJedis(long uid,long id,String type){
		int flag = ResultUtils.ERROR;
//		ResourceInfo resourceInfo = putObjectToResource(obj, uid);
//		if(resourceInfo != null && resourceInfo.getRid() != 0){
//			jedisManager.setJedisResourceList(resourceInfo);
//			flag = jedisManager.delJedisResources(uid, id);
			//flag = jedisManager.delJedisResourceList(id);
			flag = resourceJedisManager.delOneResource(id);
//		}
		return flag;
	}

	/**
	 * 将各种资源从缓存中删除
	 * @param obj
	 * @param uid
	 */
	public int clearResourceFromJedis(String type){
		int flag = ResultUtils.ERROR;
		flag = jedisManager.clearJedisResource(type);
		return flag;
	}

	/**
	 * 将各种资源阅读量
	 * @param obj
	 * @param uid
	 */
	public int getResourceReadCount(long id,String type){
		int count = 0;
		try {
			count = jedisManager.getResourceReadCount(id, type);
		} catch (Exception e) {
			count = 0;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return count;
	}

	/**
	 * 从缓存中获取资源被收藏的数量
	 * @param id
	 * @param type
	 * @return
	 */
	public int getResourceCollectCountFromJedis(Long id , String type){
		long begin = System.currentTimeMillis();
		int count = jedisManager.getResourceCollectCount(id, type);
		if(count == 0){
			count = count+ResRandomUtils.RandomInt();
		}
		long end = System.currentTimeMillis();
//		System.out.println("获取资源被收藏数量耗时"+(end - begin));
		return count;
	}
	
	/**
	 * 清空用户索引位置
	 * @param id
	 * @param type
	 * @return
	 */
	public void clearIndexCount(){
//		jedisManager.clearIndexCount();
	}
	
	/**
	 * 获取首页推广数据index
	 * @param count
	 * @return
	 */
	public int getIndexPopularizeCount(int count){
		return jedisManager.getIndexPopularizeCount(count);
	}
	
	/**
	 * 将用户修改过的标签放入到缓存中
	 * @param tags
	 */
	public void setUserTagToJedis(Long uid,List<String> tags){
		if(tags == null || tags.size()==0){
			return ;
		}

		try {
			Iterator<String> iter = tags.iterator();

			while(iter.hasNext()){
				String tag = iter.next();
				if(tag != null && !"".equals(tag)){
					jedisManager.takeUserTag(uid, tag);
				}
			}
		} catch (Exception e) {
			LOG.error("出错数据:"+tags.toString());
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}

	/**
	 * 修改资源是修改缓存中的信息(isPraise)
	 * @param rid 资源id
	 * @param uid 用户id
	 * @param key 信息key
	 * @param value 信息
	 */
	public void updateIsPraiseByJedis(Long rid,Long uid,String key,String value){
		//long begin = System.currentTimeMillis();
		try {
			String result = relationToUserAndResManager.saveOneRelation(uid, rid, key, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		/*long end = System.currentTimeMillis();
		System.out.println("添加["+key+"]信息到资源缓存的耗时："+(end-begin));*/

	}

	/**
	 * 修改资源是修改缓存中的信息(isPraise,isCollect,zNum,cNum,,)
	 * @param rid 资源id
	 * @param uid 用户id
	 * @param key 信息key
	 * @param value 信息
	 */
	public void updateResourceByJedis(Long rid,Long uid,String key,String value){
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
	 * 修改资源是修改缓存中的信息(inList)
	 * @param rid 资源id
	 * @param uid 用户id
	 * @param type 27:书，28:dianying
	 * @param value 信息
	 */
	public void updateInListByJedis(Long id,Long uid,String type,String value){
		long begin = System.currentTimeMillis();
		try {
			if(CommentUtils.TYPE_BOOK.equals(type)){
				relationToUserAndResManager.saveOneRelationUserAndBook(uid, id, JedisConstant.RELATION_IS_INLIST, value);
			}else{
				relationToUserAndResManager.saveOneRelationUserAndMovie(uid, id, JedisConstant.RELATION_IS_INLIST, value);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		long end = System.currentTimeMillis();
		System.out.println("添加["+JedisConstant.RELATION_IS_INLIST+"]信息到资源缓存的耗时："+(end-begin));

	}

	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}

	public void setGetResourceInfoFacade(
			GetResourceInfoFacadeImpl getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setDiaryFacade(DiaryFacade diaryFacade) {
		this.diaryFacade = diaryFacade;
	}

	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}

	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}

	public void setSerializeFacade(SerializeFacade serializeFacade) {
		this.serializeFacade = serializeFacade;
	}

	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setMsgFacade(MsgFacade msgFacade) {
		this.msgFacade = msgFacade;
	}

	public void setJedisSimpleClient(JedisSimpleClient jedisSimpleClient) {
		this.jedisSimpleClient = jedisSimpleClient;
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

	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}

	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}

	public void setSearchApiManager(SearchApiManager searchApiManager) {
		this.searchApiManager = searchApiManager;
	}

	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}

	public void setGraphicFilmFacade(GraphicFilmFacade graphicFilmFacade) {
		this.graphicFilmFacade = graphicFilmFacade;
	}

	public void setPushManager(PushManager pushManager) {
		this.pushManager = pushManager;
	}

	public void setFansListManager(FansListManager fansListManager) {
		this.fansListManager = fansListManager;
	}

	public void setMomentJedisManager(MomentJedisManager momentJedisManager) {
		this.momentJedisManager = momentJedisManager;
	}

	public void setResourceJedisManager(ResourceJedisManager resourceJedisManager) {
		this.resourceJedisManager = resourceJedisManager;
	}

	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}
	
	public void setRelationToUserAndResManager(
			RelationToUserAndResManager relationToUserAndResManager) {
		this.relationToUserAndResManager = relationToUserAndResManager;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}

	
	/*public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}*/

	public void setRelationToUserandresClient(
			JedisSimpleClient relationToUserandresClient) {
		this.relationToUserandresClient = relationToUserandresClient;
	}

	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}
	
	public void setResourceHashClient(JedisSimpleClient resourceHashClient) {
		this.resourceHashClient = resourceHashClient;
	}

	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
}
