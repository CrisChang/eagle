package com.poison.eagle.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.poison.eagle.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import com.keel.common.event.rocketmq.RocketProducer;
import com.keel.utils.UKeyWorker;
import com.poison.act.client.ActFacade;
import com.poison.activity.client.ActivityFacade;
import com.poison.activity.model.ActivityStage;
import com.poison.activity.model.ActivityState;
import com.poison.eagle.entity.ActorInfo;
import com.poison.eagle.entity.ArticleInfo;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.CommentInfo;
import com.poison.eagle.entity.DiaryInfo;
import com.poison.eagle.entity.DuyaoerInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.MvActorInfo;
//import com.poison.eagle.entity.QuestInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.entity.UserBigInfo;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.manager.util.MovieAutoUtil;
import com.poison.eagle.utils.craw.AnalyzerUtils;
import com.poison.eagle.utils.craw.CrawlUtils;
import com.poison.msg.client.MsgFacade;
import com.poison.msg.model.MsgAt;
//import com.poison.quest.client.QuestFacade;
//import com.poison.quest.model.Quest;
//import com.poison.quest.model.QuestProgress;
//import com.poison.quest.model.QuestReward;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.GraphicFilmFacade;
import com.poison.resource.client.MovieTalkFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.ResourceLinkFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Diary;
import com.poison.resource.model.GraphicFilm;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.model.ResourceLink;
import com.poison.resource.model.Serialize;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvActorFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.MvStillsFacade;
import com.poison.store.dao.impl.MvActorDAOImpl;
import com.poison.store.model.Actor;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MovieStills;
import com.poison.store.model.MvActor;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;
import com.poison.ucenter.model.UserStatistics;

/**
 * 用户写书单影单manager
 * 
 * @author Administrator
 * 
 */
public class MovieManager extends BaseManager {
	private static final Log LOG = LogFactory.getLog(MovieManager.class);
	// private Map<String, Object> req ;
	// private Map<String, Object> dataq;
	// private Map<String, Object> datas ;
	// private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
	// private String error;

	// private List<Map<String,String>> commentList;

	private int flagint;

	private MvFacade mvFacade;
	private MvCommentFacade mvCommentFacade;
	private MovieTalkFacade movieTalkFacade;
	private UcenterFacade ucenterFacade;
	private MvStillsFacade mvStillsFacade;
	private MyMovieFacade myMovieFacade;
	private MsgFacade msgFacade;
	private BigFacade bigFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private ActFacade actFacade;
	private TopicFacade topicFacade;
	private ResStatisticService resStatisticService;
	private GraphicFilmFacade graphicFilmFacade;
	private ActivityFacade activityFacade;
	

	private ActUtils actUtils = ActUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private AnalyzerUtils analyzerUtils = new AnalyzerUtils();
	private BigUtils bigUtils = BigUtils.getInstance();

	private ResourceManager resourceManager;

	private RocketProducer eagleProducer;

	private UserJedisManager userJedisManager;
	
	private PaycenterManager paycenterManager;

	private MvActorFacade mvActorFacade;

	private MovieAutoUtil movieAutoUtil;

	private ResourceLinkFacade resourceLinkFacade;
	
	private ResStatJedisManager resStatJedisManager;
	
	/*private QuestFacade questFacade;

	public void setQuestFacade(QuestFacade questFacade) {
		this.questFacade = questFacade;
	}*/
	
	public void setPaycenterManager(PaycenterManager paycenterManager) {
		this.paycenterManager = paycenterManager;
	}
	
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}

	/**
	 * 保存豆瓣书
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveMovie(String reqs) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		// String result = movieUtils.getDate(movieUtils.getHeaders(), path,
		// "UTF-8", 5);
		// System.out.println(result);
		//
		// CrawlUtils crawlUtils = new CrawlUtils();
		//
		//
		// MvInfo mvInfo = crawlUtils.movie(path, result, 2353023l);
		// System.out.println(mvInfo.toString());

		MvInfo mvInfo = movieUtils.putJsonToMovie(dataq);
		// System.out.println("保存前："+mvInfo);
		LOG.info("保存前：" + mvInfo);
		mvInfo = mvFacade.insertMvInfo(mvInfo);
		LOG.info("保存后：" + mvInfo);
		// System.out.println("保存后："+mvInfo);

		long id = mvInfo.getId();
		int flagint;
		flagint = (int) mvInfo.getFlag();
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", id);
		} else {
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 通过爬虫保存豆瓣电影
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveMovieAnalyse(String reqs) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		MvInfo mvInfo = movieUtils.putAnalyseToMvInfo(dataq);
		mvInfo = mvFacade.insertMvInfo(mvInfo);

		long id = mvInfo.getId();
		int flagint;
		flagint = (int) mvInfo.getFlag();
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", id);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 写/修改影评
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeMovieComment(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		long id = 0;
		String score = null;
		List content = null;
		int isDB = 0;
		Long commentId = null;
		String beforeScore = "0";
		Long movieListId = null;
		String lon = "";// 经度
		String lat = "";// 维度
		String locationDescription = "";// 地点描述
		String locationCity = "";// 地点城市
		String locationArea = "";// 地点地区
		List<Map<String, String>> oList = new ArrayList<Map<String, String>>();// 话题列表等信息
		Long vid = 0l;// 虚拟主键
		String title = "";// 标题
		String cover = "";// 封面
		String resourceType = CommentUtils.TYPE_MOVIE_COMMENT;// 资源类型 默认为7影评
		// 去掉空格
		reqs = reqs.trim();

		//是否只打分的标识
		boolean isScore = true;
		
		boolean add = false;//是否发表新的影评
		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			score = (String) dataq.get("score");
			content = (List) dataq.get("list");
			if(null==content||content.isEmpty()){
				isScore = false;
			}
			try {
				commentId = Long.valueOf(dataq.get("commentId").toString());
			} catch (Exception e) {
				commentId = 0l;
			}
			id = Long.valueOf(dataq.get("id").toString());



			lon = CheckParams.objectToStr((String) dataq.get("lon"));
			lat = CheckParams.objectToStr((String) dataq.get("lat"));
			locationDescription = CheckParams.objectToStr((String) dataq.get("locationName"));
			if (commentId == UNID) {
				// isDB = Integer.valueOf(dataq.get("isDB").toString());
			} else {
				beforeScore = (String) dataq.get("beforeScore");
				if (null == beforeScore) {
					beforeScore = "0";
				}
			}
			//只修改评分
			List<MvComment> mvCommentList = mvCommentFacade.findUserMvComment(uid, (int) id, 0);
			if(null!=mvCommentList&&mvCommentList.size()>0){//当查询出来的不为空并且不报错
				beforeScore = mvCommentList.get(0).getScore();
			}else{
				beforeScore = "0";
			}

			try {
				movieListId = Long.valueOf(dataq.get("movieListId").toString());
				if (movieListId == 0) {
					movieListId = null;
				}
			} catch (Exception e) {
				movieListId = null;
			}
			// 标题
			title = CheckParams.objectToStr((String) dataq.get("title"));
			// 封面
			cover = CheckParams.objectToStr((String) dataq.get("cover"));
			// 资源类型
			resourceType = CheckParams.objectToStr((String) dataq.get("resourceType"));
			if (null == resourceType || "".equals(resourceType)) {
				resourceType = CommentUtils.TYPE_MOVIE_COMMENT;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		// 获取用户等级
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		int level = userAllInfo.getLevel();
		if (level != 50) {
			level = 0;
		}

		MvComment mvComment = new MvComment();

		Map<String, Object> map = WebUtils.putDataToMap(content, ucenterFacade);
		String resultContent = (String) map.get("resultContent");
		//长影评的话图片需要处理
//		if(resourceType.equals(CommentUtils.TYPE_MOVIE_COMMENT)){
//			resultContent = (String) map.get("resultContent");
//		}else
		if(resourceType.equals(CommentUtils.TYPE_ARTICLE_MOVIE)){
			//长影评的类型的话对文字做处理
			String imgRegex = "http://p[0-2]{1}\\.duyao001\\.com/[^\\.]+\\.(jpg|bmp|eps|gif|mif|miff|png|tif|tiff|svg|wmf|jpe|jpeg|dib|ico|tga|cut|pic)";

			resultContent = HtmlUtil.delHTMLTag(resultContent);
			Pattern pattern = Pattern.compile(imgRegex);
			Matcher matcher = pattern.matcher(resultContent);
			String tempStr = "";
			String imagePath = "";
			String w = "";
			String h = "";
			while (matcher.find()){
				tempStr = matcher.group();
				if(tempStr.indexOf("x")>0 && tempStr.indexOf("_")>0){
					imagePath = tempStr;
					imagePath = imagePath.substring(imagePath.indexOf("_")+1, imagePath.lastIndexOf("."));
					w = imagePath.substring(0,imagePath.indexOf("x"));
					h = imagePath.substring(imagePath.indexOf("x")+1);
				}
				resultContent = resultContent.replace(tempStr, "</p><p><a class=\"imagescroll\"><img originalSrc=\"" + tempStr + "\" _originalSrc=\""+tempStr+"\" width=\""+w+"\" height=\""+h+"\"  /></a></p><p>");
			}

			resultContent = resultContent.replace("\r\n","\r\n</p><p>").replace("\n", "\n</p><p>");
			resultContent = "<p>"+resultContent+"</p>";
		}
		String linkType = (String) map.get("linkType");
		String linkName = (String) map.get("linkName");
		// WebUtils webUtils = new WebUtils();
		if (commentId == UNID) {// 插入评论
			add = true;
			//需要判断用户是否参赛状态并且处于一个活动阶段，如果是参赛用户且处于参赛活动阶段，则直接走另一个逻辑，直接保存影评信息，如果是需要修改影评，则需要判断是修改的参赛前的影评还是参赛的影评再分逻辑处理
			//(影评所有增删改查的逻辑都需要更改)
			boolean match = false;//是否参赛
			ActivityStage activityStage = null;
			if(isScore){//排除只打分没有影评内容的情况
				ActivityState activityState = activityFacade.findActivityStateByUserid(uid);
				if(activityState!=null && activityState.getState()==ActivityState.state_match){
					activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
					if(activityStage!=null && activityStage.getId()>0){
						//判断用户是否处于当前活动阶段
						if(activityStage.getId()==activityState.getStageid()){
							match = true;
						}
					}
				}
			}
			
			if(match){
				mvComment = mvCommentFacade.addActivityMvComment(uid, id, resultContent, score, TRUE, 0, String.valueOf(level), movieListId, lon, lat, locationDescription, locationCity, locationArea, title,
						cover, resourceType,activityStage.getId());
				//需要增加有用的数量（默认为0），用于按有用数排行
				if(mvComment!=null && mvComment.getId()>0){
					try{
						//resStatJedisManager.doUsefulNum(commentId, CommentUtils.TYPE_MOVIE_COMMENT, mvComment.getMovieId(), CommentUtils.TYPE_MOVIE,0,activityStage.getId());
						resStatJedisManager.doHeatNum(commentId, CommentUtils.TYPE_MOVIE_COMMENT, mvComment.getMovieId(), CommentUtils.TYPE_MOVIE, 0, activityStage.getId());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{
				mvComment = mvCommentFacade.addMvComment(uid, id, resultContent, score, TRUE, 0, String.valueOf(level), movieListId, lon, lat, locationDescription, locationCity, locationArea, title,
						cover, resourceType);// (uid, id,
												// WebUtils.putDataToHTML5(content),
												// score, TRUE, 0,
												// String.valueOf(level));
				// mvComment = mvCommentFacade.addMvComment(uid, id,
				// WebUtils.putDataToHTML5(content), score, TRUE, isDB);


				// 添加用户影评数量
				if (mvComment.getId() != 0) {
					try {
						UserStatistics userStatistics = userStatisticsFacade.updateMvcommentCount(uid);
						if (userStatistics != null && userStatistics.getId() > 0) {
							userJedisManager.saveOneUserResourceCount(uid, JedisConstant.MVCOMMENT_COUNT, userStatistics.getMvcommentCount() + "");
						}
					} catch (Exception e) {
						LOG.error("增加用户影评数量失败:" + e.getMessage(), e.fillInStackTrace());
					}
				}
			}
			//用户的影评任务的操作,字数需要大于20字
			if(mvComment.getFlag()==ResultUtils.SUCCESS || mvComment.getFlag() == UNID){
				if(mvComment.getContent()!=null && mvComment.getContent().length()>=20){
					//addQuestMvComment(uid);
				}
			}
		} else {
			// 修改评论
			//需要判断是否是修改的参赛的影评信息
			mvComment = mvCommentFacade.findMvCommentIsExist(commentId);
			if(mvComment!=null && mvComment.getStageid()>0){
				//参赛影评，只需修改内容，无需修改电影平均分等信息
				mvComment = mvCommentFacade.updateMyMvComment(commentId, WebUtils.putDataToHTML5(content), score,title,cover);
				/*if(mvComment==null){
					mvComment = new MvComment();
				}*/
				//需求需改，参赛影评不允许修改
				//mvComment.setFlag(ResultUtils.MATCH_MV_COMMENT_NOT_ALLOW_UPDATE);
			}else{
				// 常江修改
				mvComment = mvCommentFacade.updateMyMvComment(commentId, WebUtils.putDataToHTML5(content), score,title,cover);
				if (null != movieListId && movieListId != 0) {
					long userId = mvComment.getUserId();
					long movieId = mvComment.getMovieId();
					myMovieFacade.moveOneMovie(movieId, movieListId, userId);
					// 修改影单的封面
					try {
						MovieList movieList = myMovieFacade.findMovieListById(movieListId);
						MvInfo mvInfo = mvFacade.queryById(movieId);
						String movieListPic = movieList.getCover();
						String mvInfoPic = mvInfo.getMoviePic();
						if (mvInfoPic == null) {
							mvInfoPic = "";
						}
						if ("".equals(movieListPic)) {
							// 修改影单封面
							myMovieFacade.updateMovieListPic(movieList.getId(), mvInfoPic);
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(), e.fillInStackTrace());
					}

					// mvComment = mvCommentFacade.addMvComment(userId, movieId,
					// content, score, isOpposition, isDb, type, resId);
				}


				// 最后修改时间
				long latestRevisionDate = mvComment.getLatestRevisionDate();
				vid = ((latestRevisionDate - UKeyWorker.getTwepoch() << UKeyWorker.getTimestampshift()));
			}
		}

		//修改评分
		if("0".equals(beforeScore)){
			if (level == CommentUtils.USER_LEVEL_TALENT) {
				MvAvgMark avgMark = mvCommentFacade.addExpertsAvgMark(id, Float.valueOf(score));
			} else {
				MvAvgMark avgMark = mvCommentFacade.addMvAvgMark((int) id, Float.valueOf(score));
			}
		}else{
			if (level == CommentUtils.USER_LEVEL_TALENT) {
				MvAvgMark avgMark = mvCommentFacade.updateExpertsAvgMark(id, Float.valueOf(score), Float.valueOf(beforeScore));// (id,
				// Float.valueOf(score));
			} else {
				MvAvgMark avgMark = mvCommentFacade.updateMvAvgMark((int) id, Float.valueOf(score), Float.valueOf(beforeScore));
			}
		}


		int flagint;
		flagint = mvComment.getFlag();

		// 添加逼格
		// MvInfo mvInfo = mvFacade.queryById(id);
		// float bigValue = bigFacade.getMovieBigValue(mvInfo.getReleaseDate(),
		// mvInfo.getProCountries(), mvInfo.getTags(), mvInfo.getDirector());
		// MvComment mvComment2 =
		// mvCommentFacade.addMvCommentBigValue(mvComment.getId(), bigValue);
		//
		//
		// UserBigInfo userBigInfo = bigUtils.getUserNowLevel(uid,
		// bigValue,TRUE, ucenterFacade, bigFacade);
		//
		// UserBigValue userBigValue = ucenterFacade.updateUserBigValue(uid,
		// bigValue, userBigInfo.getLevel());
		//
		// flagint = userBigValue.getFlag();

		// 如果有@消息，将此消息存入表中，并推送给相关的人
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {

			if(isScore){
				// 将生成的影评放入到缓存中
				resourceManager.setResourceToJedis(mvComment, uid, uid, vid);
				// 将附加信息加入缓存中
				resourceManager.updateInListByJedis(mvComment.getMovieId(), uid, CommentUtils.TYPE_MOVIE, "0");
				// 更新用户的最后更新时间
				ucenterFacade.saveUserLatestInfo(uid, mvComment.getId(), CommentUtils.TYPE_MOVIE_COMMENT);
				// 插入话题列表中
				oList = (List<Map<String, String>>) map.get("oList");
				if (null != oList && oList.size() > 0) {
					long rid = mvComment.getId();
					String resType = CommentUtils.TYPE_DIARY;
					Iterator<Map<String, String>> oListIt = oList.iterator();
					String topicName = "";
					String oListType = "";
					Long toUid = 0l;
					while (oListIt.hasNext()) {
						Map<String, String> oneListmap = oListIt.next();
						topicName = oneListmap.get("name");
						oListType = oneListmap.get("type");
						if (oListType.equals(CommentUtils.TYPE_TOPIC)) {
							// 插入一个话题
							topicFacade.saveTopicOrLink(uid, topicName, CommentUtils.TYPE_TOPIC, "", "", "", rid, resType);
						} else if (oListType.equals(CommentUtils.TYPE_USER)) {
							// 添加@推送消息
							try {
								JSONObject json = new JSONObject();
								toUid = Long.valueOf(oneListmap.get("id"));
								// 插入@信息
								userJedisManager.incrOneUserInfo(toUid, JedisConstant.USER_HASH_AT_NOTICE);
								json.put("uid", uid);
								json.put("toUid", toUid);
								json.put("rid", rid);
								json.put("type", CommentUtils.TYPE_MOVIE_COMMENT);
								json.put("pushType", PushManager.PUSH_AT_TYPE);
								json.put("context", resultContent.replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
								json.toString();
								eagleProducer.send("pushMessage", "toBody", "", json.toString());
								// 插入at信息
								actFacade.insertintoActAt(uid, rid, uid, CommentUtils.TYPE_MOVIE_COMMENT, toUid, rid, CommentUtils.TYPE_MOVIE_COMMENT);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				//发表长影评，则增加对应电影的阅读量
				if(add && CommentUtils.TYPE_ARTICLE_MOVIE.equals(resourceType)){
					try{
						resStatJedisManager.addReadNum(id, CommentUtils.TYPE_MOVIE,0,"",0,50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}


			/*
			 * userAllInfo = ucenterFacade.findUserInfo(null, uid); String uname
			 * = userAllInfo.getName();
			 * 
			 * //收集被act人的名字并推送消息 List<String> actNames =
			 * WebUtils.getActNameList(); for (String string : actNames) { //
			 * System.out.println("成功了:"+string); UserInfo userInfo =
			 * ucenterFacade.findUserInfoByName(string); String token =
			 * userInfo.getPushToken();
			 * 
			 * MsgAt msgAt = msgFacade.doAct(null, uid, userInfo.getUserId(),
			 * mvComment.getId(), CommentUtils.TYPE_MOVIE_COMMENT);
			 * 
			 * flagint = msgAt.getFlag(); if(flagint == ResultUtils.SUCCESS){
			 * MainSend mainSend = new MainSend();
			 * mainSend.sendMsgToStore(uname+CommentUtils.ACT_MESSAGE, token); }
			 * }
			 */
		}

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			// datas.put("userBig", userBigInfo);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}
	
	//影评任务--新增影评的操作
	/*private void addQuestMvComment(Long uid){
		try{
			//需要判断是否是首次影评，是否达到每日任务，是否达到累计任务,需要加入小红点
			boolean questfinish = false;//是否有任务完成
			boolean dayquestfinish = false;//是否有每日任务完成
			//long mcid = mvCommentFacade.findMvCommentRecord(uid);
			QuestProgress dayquestProgress = questFacade.getQuestProgressByUseridAndType(uid, QuestInfo.type_day_mv);//每日的任务进程
			QuestProgress questProgress = questFacade.getQuestProgressByUseridAndType(uid, QuestInfo.type_mv);//累计任务进程
			
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
			if(mcid==0){
				//是首次评论，需要打赏
				Quest quest = questFacade.getQuest(QuestInfo.type_first_mv);
				if(quest!=null && quest.getId()>0){
					int totalFee = quest.getReward();//需要查询首次评论任务的打赏金额
					paycenterManager.rewardUser(rewardUserId, rewardUserName, sourceId, sourceName, sourceType, totalFee);
					questReward.setReward(questReward.getReward()+quest.getReward());
					dayquestReward.setReward(dayquestReward.getReward()+quest.getReward());
				}
			}
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
				Quest quest = questFacade.getQuest(QuestInfo.type_day_mv);
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
				dayquestProgress.setType(QuestInfo.type_day_mv);
				dayquestProgress.setUpdatetime(System.currentTimeMillis());
				dayquestProgress.setUserid(uid);
				questFacade.insertQuestProgress(dayquestProgress);
			}
			
			if(questProgress!=null && questProgress.getId()>0){
				//存在累计任务进度，需要判断是否达到累计任务的最大值
				questProgress.setProgress(questProgress.getProgress()+1);
				//需要查询累计任务的信息（最大进度值，打赏金额）
				Quest quest = questFacade.getQuest(QuestInfo.type_mv);
				if(quest!=null && quest.getId()>0){
					//需要判断是否达到了最大值
					if(questProgress.getProgress()==quest.getMaxProgress()){
						//达到最大值了，进行打赏和初始化累计进程
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
				questProgress.setType(QuestInfo.type_mv);
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
	 * 删除影评
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delMovieComment(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		long id = 0;
		long movieId = 0;
		String beforeScore = "";
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Long.valueOf(dataq.get("id").toString());
			movieId = Long.valueOf(dataq.get("movieId").toString());
			beforeScore = (String) dataq.get("beforeScore");

		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		MvComment mvComment = mvCommentFacade.deleteMvComment(id);
		int flagint;
		flagint = mvComment.getFlag();
		//需要判断删除的是否是参赛影评
		if(mvComment!=null && mvComment.getStageid()==0){
			//普通的影评才需要删除计算的平均分
			if(flagint == ResultUtils.SUCCESS || flagint == UNID){
				UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
				int level = userAllInfo.getLevel();
				if (level == CommentUtils.USER_LEVEL_TALENT) {
					mvCommentFacade.deleteExpertsAvgMark((int) movieId, Float.valueOf("0.0"), Float.valueOf(beforeScore));
				} else {
					mvCommentFacade.deleteMvAvgMark((int) movieId, Float.valueOf("0.0"), Float.valueOf(beforeScore));
				}
			}
		}

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;

			// 删除影评缓存
			resourceManager.delResourceFromJedis(uid, mvComment.getId(), null);
			// 删除话题相关
			topicFacade.deleteTopicLinkByResid(id, uid);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 影评列表
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewMovieCommentList(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		Long id = null;
		long movieId = 0;
		String type = null;
		// 去掉空格
		reqs = reqs.trim();
		String resourceType = CommentUtils.TYPE_MOVIE_COMMENT;
		String version = "";
		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Long.valueOf(dataq.get("lastId").toString());
			movieId = Long.valueOf(dataq.get("movieId").toString());
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = null;
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			// 是长评还是短评列表
			resourceType = CheckParams.objectToStr((String) dataq.get("resourceType"));
			if ("".equals(resourceType)) {
				resourceType = CommentUtils.TYPE_MOVIE_COMMENT;
			}
			version = (String) dataq.get("version");
			// System.out.println("得到的resType类型为"+resourceType+"得到的最后lastid为"+id+"得到的电影id为"+movieId+"得到的类型为type"+type);

		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		List<MvComment> friendmvComments = new ArrayList<MvComment>();
		List<ResourceInfo> friends = new ArrayList<ResourceInfo>();
		// 朋友对电影的评论
		// friendmvComments = mvCommentFacade.findUserFriendMvComment(uid,
		// movieId);
		// friends = getResponseList(friendmvComments, friends);

		// 小编评论
		// List<MvComment> writeComments =
		// mvCommentFacade.findUserMvComment(CommentUtils.UID_HANDONG,
		// (int)movieId);
		// MvComment mvComment = null;
		// ResourceInfo resourceInfo = null;
		// if(writeComments.size()>0){
		// mvComment = writeComments.get(0);
		// resourceInfo = movieUtils.putMVCommentToResource(mvComment,
		// ucenterFacade, mvFacade);
		// }

		// //全部评论
		// List<MvComment> mvComments = new ArrayList<MvComment>();
		// //除去我朋友的全部评论
		// if(id == UNID){
		// mvComments = mvCommentFacade.findAllMvComment(movieId, null,type);
		// }else{
		// mvComments = mvCommentFacade.findAllMvComment(movieId, id,type);
		// }

		// mvComments.removeAll(friendmvComments);
		// if(mvComment != null){
		// mvComments.remove(mvComment);
		// }

		// List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		// resourceInfos = getResponseList(mvComments, resourceInfos);

		// 专家评论或正常用户评论
		List<MvComment> mvComments = new ArrayList<MvComment>();
		// 除去我朋友的全部评论
		if("1".equals(version)){
			//支持长影评的版本
			if (id == UNID) {
				mvComments = mvCommentFacade.findAllMvComment(movieId, null, type, resourceType);
			} else {
				mvComments = mvCommentFacade.findAllMvComment(movieId, id, type, resourceType);
			}
		}else{
			//不支持长影评的版本，查询短影评是这个接口，是没有标题的短影评
			if (id == UNID) {
				mvComments = mvCommentFacade.findAllMvCommentForOld(movieId, null, type, resourceType,null,null);
			} else {
				mvComments = mvCommentFacade.findAllMvCommentForOld(movieId, id, type, resourceType,null,null);
			}
		}
		

		MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(movieId);
		int size = 0;
		if (String.valueOf(CommentUtils.USER_LEVEL_TALENT).equals(type)) {
			size = mvAvgMark.getExpertsTotalNum();
		} else if (String.valueOf(CommentUtils.USER_LEVEL_NORMAL).equals(type)) {
			size = mvAvgMark.getMvTotalNum();
		}

		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		resourceInfos = getResponseList(mvComments, uid, resourceInfos);
		
		datas = new HashMap<String, Object>();
		
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
			datas.put("size", size);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 
	 * <p>
	 * Title: viewMovieCommentPraiseList
	 * </p>
	 * <p>
	 * Description: 查询热门影评列表
	 * </p>
	 * 
	 * @author :changjiang date 2015-7-1 下午2:42:12
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewMovieCommentPraiseList(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req;
		Map<String, Object> dataq;
		Map<String, Object> res;
		Map<String, Object> datas;
		String resString;// 返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error;
		int id = 0;
		String type = null;
		String commentType = "";
		String resourceType = "";
		String sort = "";
		String version = "";//区分新旧版app，用来判断显示长影评
		// 去掉空格
		reqs = reqs.trim();
		int page = 1;

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Integer.valueOf(dataq.get("id").toString());
			try {
				page = Integer.valueOf((String) dataq.get("page"));
			} catch (Exception e) {
				page = 1;
			}
			type = (String) dataq.get("type");
			resourceType = CheckParams.objectToStr((String) dataq.get("resourceType"));

			//添加排序字段 0为时间，1为热度
			sort = CheckParams.objectToStr((String) dataq.get("sort"));
			
			version = (String) dataq.get("version");

		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		// System.out.println(req);

		if (type == null || "0".equals(type) || "".equals(type)) {
			type = CommentUtils.TYPE_MOVIE;
		}

		if(resourceType.equals(CommentUtils.TYPE_ARTICLE_MOVIE)&&"0".equals(sort)){//当为长影评时
			List<MvComment> mvList = null;
			//if("1".equals(version)){
				mvList =  mvCommentFacade.findOneMvCommentListByResTypeAndPage(id,resourceType,(page - 1) * 10,page * 10);
			//}else{
				//mvList =  mvCommentFacade.findAllMvCommentForOld(id, null, null, resourceType, (page - 1) * 10, page * 10);
			//}
			
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
			resourceInfos = getResponseList(mvList, uid, resourceInfos);
			datas = new HashMap<String, Object>();
			datas.put("list", resourceInfos);
			datas.put("flag", "0");
			resString = getResponseData(datas);
			return resString;
		}


		List<ResStatistic> ResStatisticList = new ArrayList<ResStatistic>();
		if("1".equals(sort)){//按照有用最多排序，根据resourceType区分长影评和短影评
			ResStatisticList = resStatisticService.findResStatisticRankByUsefulAndType(resourceType,id, type,0, (page - 1) * 5, page * 5);
		}else{
			ResStatisticList = resStatisticService.findResStatisticRankByUseful(id, type,0, (page - 1) * 5, page * 5);
		}
		// findResStatisticRankByPraise(id, type, (page - 1) * 5, page * 5);

		List<String> resInfoStrList = new ArrayList<String>();
		if (null != ResStatisticList && ResStatisticList.size() > 0) {
			Iterator<ResStatistic> ResStatisticListIt = ResStatisticList.iterator();
			while (ResStatisticListIt.hasNext()) {
				ResStatistic resStatistic = ResStatisticListIt.next();
				if (null != resStatistic) {
					String resStr = resourceManager.getResourceInfoStr(resStatistic.getResId(), 0, type,uid);
					resInfoStrList.add(resStr);
				}
			}
		}

		resString = "{\"res\":{\"data\":{\"flag\":\"0\",\"list\":" + resInfoStrList.toString() + "}}}";
		/*
		 * List<ResourceInfo> commentList = new ArrayList<ResourceInfo>();
		 * 
		 * commentList = getCommentResponseList(bkcommentList, null,uid,
		 * commentList);
		 * 
		 * 
		 * datas = new HashMap<String, Object>(); if(flagint ==
		 * ResultUtils.SUCCESS || flagint == UNID){ flag =
		 * CommentUtils.RES_FLAG_SUCCESS; datas.put("list", commentList); }else{
		 * flag = CommentUtils.RES_FLAG_ERROR; error =
		 * MessageUtils.getResultMessage(flagint);
		 * LOG.error("错误代号:"+flagint+",错误信息:"+error); datas.put("error", error);
		 * } datas.put("flag", flag); //处理返回数据 resString =
		 * getResponseData(datas);
		 */

		return resString;
	}

	/**
	 * 
	 * <p>
	 * Title: viewUserLongMovieCommentList
	 * </p>
	 * <p>
	 * Description: 查询个人的长影评列表
	 * </p>
	 * 
	 * @author :changjiang date 2015-6-24 下午6:46:36
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewUserLongMovieCommentList(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		Long id = null;
		long movieId = 0;
		String type = "";
		// 去掉空格
		reqs = reqs.trim();
		String resourceType = CommentUtils.TYPE_MOVIE_COMMENT;

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			String lastId = (String) dataq.get("lastId");
			if (null == lastId || lastId.equals("")) {
				id = null;
			} else {
				id = Long.valueOf(lastId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		List<MvComment> friendmvComments = new ArrayList<MvComment>();
		List<ResourceInfo> friends = new ArrayList<ResourceInfo>();

		// 查找长影评列表
		List<MvComment> mvComments = new ArrayList<MvComment>();
		mvComments = mvCommentFacade.findUserLongMvCommentListByUserId(uid, id);

		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		resourceInfos = getResponseList(mvComments, uid, resourceInfos);

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 单个电影的详情
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewOneMovie(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		Long id = null;
		int isDB = 0;
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Long.valueOf(dataq.get("id").toString());
			// isDB = Integer.valueOf(dataq.get("isDB").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		MovieInfo movieInfo = new MovieInfo();

		MvInfo mvInfo = mvFacade.queryById(id);
		
		//==================================是否参加了影评大赛的判断=================================
		//boolean match = false;//是否参赛
		long stageid = 0L;
		ActivityStage activityStage = null;
		ActivityState activityState = activityFacade.findActivityStateByUserid(uid);
		if(activityState!=null && activityState.getState()==ActivityState.state_match){
			activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
			if(activityStage!=null && activityStage.getId()>0){
				//判断用户是否处于当前活动阶段
				if(activityStage.getId()==activityState.getStageid()){
					//match = true;
					stageid = activityStage.getId();
				}
			}
		}
		//=================================================================================
		// 将电影实体类格式化
		movieInfo = movieUtils.putMVToMovieInfo(mvInfo, uid, mvCommentFacade, mvStillsFacade, FALSE,stageid);
		
		// 是否在我影单中
		MvListLink mvListLink = myMovieFacade.findUserIsCollectMovie(uid, movieInfo.getId());
		if (mvListLink.getId() != TRUE) {
			movieInfo.setInList(TRUE);
		}
		// mvCommentFacade.findAllMvCommentListByUsersId(usersId, type, resId)

		// System.out.println(movieInfo.toString());
		int flagint=ResultUtils.QUERY_ERROR;
		if (movieInfo != null && movieInfo.getId() != UNID) {
			flagint = ResultUtils.SUCCESS;
		}
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("movieInfo", movieInfo);

			/**
			 * 打印热搜电影的日志
			 */
			BussinessLogUtils.searchMovieLog(mvInfo.getId(), mvInfo.getName(), mvInfo.getType(), uid);
			try{
				//记录阅读数量
				resStatJedisManager.addReadNum(mvInfo.getId(), CommentUtils.TYPE_MOVIE, 0, "", 0);
			}catch(Exception e){
				LOG.error("增加电影的阅读数量出错，id为：" + movieInfo.getId() + e.getMessage(), e.fillInStackTrace());
			}
			// 从缓存中获取电影被收藏的数量
			int count = 0;
			try {
				count = resourceManager.getResourceCollectCountFromJedis((long) movieInfo.getId(), CommentUtils.TYPE_MOVIE);
			} catch (Exception e) {
				count = 0;
				LOG.error("获取电影被收藏数量出错，id为：" + movieInfo.getId() + e.getMessage(), e.fillInStackTrace());
			}
			datas.put("collectCount", count);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 添加一条讨论信息
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeMovieTalk(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		int movieId = 0;
		List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			movieId = Integer.valueOf(dataq.get("movieId").toString());
			content = (List<Map<String, Object>>) dataq.get("list");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		long id = movieTalkFacade.addMovieTalk(movieId, uid, WebUtils.putDataToHTML5(content));
		int flagint=ResultUtils.QUERY_ERROR;
		if (id != ResultUtils.INSERT_ERROR) {
			flagint = ResultUtils.SUCCESS;
		}

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", id);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 展示讨论信息
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewMovieTalkList(String reqs) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		long id = 0;
		int movieId = 0;
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			// id = Long.valueOf(dataq.get("lastId").toString());
			movieId = Integer.valueOf(dataq.get("movieId").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		List<MovieTalk> movieTalks = movieTalkFacade.findMovieTalkList(movieId, null);
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();

		resourceInfos = getResponseList(movieTalks, null, resourceInfos);

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 插入剧照
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveMoviePhoto(String reqs) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		Long movieId = null;
		JSONArray list = new JSONArray();
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			JSONObject json = new JSONObject(reqs);
			json = json.getJSONObject("req");
			json = json.getJSONObject("data");

			movieId = json.getLong("movieId");
			list = json.getJSONArray("list");
			// System.out.println(list.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		MovieStills movieStills = mvStillsFacade.findMvOlineStillsByBkId(movieId);

		int flagint = movieStills.getFlag();
		if (flagint == ResultUtils.SUCCESS) {
			movieStills = mvStillsFacade.updateMvStills(list.toString(), movieId);
			flagint = movieStills.getFlag();
		}

		// List lists = new ArrayList();
		// try {
		// lists = getObjectMapper().readValue(list.toString(), new
		// TypeReference<List>(){});
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 插入预告片
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveMovieTrailer(String reqs) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		Long movieId = null;
		JSONArray list = new JSONArray();
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			JSONObject json = new JSONObject(reqs);
			json = json.getJSONObject("req");
			json = json.getJSONObject("data");

			movieId = json.getLong("movieId");
			list = json.getJSONArray("list");
			// System.out.println(list.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		MovieStills movieStills = mvStillsFacade.findMvOlineStillsByBkId(movieId);

		int flagint = movieStills.getFlag();
		if (flagint == ResultUtils.SUCCESS) {
			movieStills = mvStillsFacade.updateMvOther(list.toString(), movieId);
			flagint = movieStills.getFlag();
		}

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 展示图解电影
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewGraphicFilm(String reqs) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		Long movieId = null;
		JSONArray list = new JSONArray();
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			movieId = Long.valueOf(dataq.get("id").toString());

		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
        //图解提供人信息
		DuyaoerInfo duyaoerInfo = null;
		// 获取方法
		List<String> resourceInfos = new ArrayList<String>();
		List graphicFilmList = null;
		List<ResourceLink> resLinkList = resourceLinkFacade.findResListByResIdAndType(movieId, CommentUtils.CASETYPE_MOVIE_GRAPHICFILM);
		if (resLinkList.size() > 0) {
			Iterator<ResourceLink> resLinkIt = resLinkList.iterator();
			while (resLinkIt.hasNext()) {
				ResourceLink resourceLink = resLinkIt.next();
				long graphicFilmId = resourceLink.getResLinkId();
				GraphicFilm graphicFilm = graphicFilmFacade.findGraphicFilmById(graphicFilmId);
				if (graphicFilm.getFlag() == ResultUtils.SUCCESS) {
					try {
						graphicFilmList = getObjectMapper().readValue(graphicFilm.getContent(), new TypeReference<List>() {
						});
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// resourceInfos.add(graphicFilm.getContent());
					UserInfo userInfo = ucenterFacade.findUserInfoByUserId(null, graphicFilm.getUid());
					if(userInfo.getFlag() == ResultUtils.SUCCESS){
						duyaoerInfo = new DuyaoerInfo();
						duyaoerInfo.setFaceAddress(userInfo.getFaceAddress());
						duyaoerInfo.setName(userInfo.getName());
					}
				}
			}
		}

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", graphicFilmList);
			datas.put("duyaoerInfo", duyaoerInfo);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 获取电影所有角色信息
	 * 
	 * @param reqs
	 * @return
	 */
	public String viewMvActors(String reqs) {
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		Long movieId = null;
		// 去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			movieId = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		List<ActorInfo> actorInfoDirector = movieAutoUtil.getActorInfos(movieId, MvActorDAOImpl.actorTypeDirector, false);
		List<ActorInfo> actorInfoStarring = movieAutoUtil.getActorInfos(movieId, MvActorDAOImpl.actorTypeStarring, false);

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("actorInfoDirector", actorInfoDirector);
			datas.put("actorInfoStarring", actorInfoStarring);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		String resString = getResponseData(datas);
		return resString;
	}

	/**
	 * 获取电影单个角色信息详情
	 * 
	 * @param reqs
	 * @return
	 */
	public String viewActorDetail(String reqs) {
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		Long id = null;
		// 去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		// 角色基本信息
		ActorInfo actorInfo = movieAutoUtil.getActorInfoById(id);
		// 角色参与的电影
		List<MvActorInfo> mvActorInfos = movieAutoUtil.getMvActorInfoByActorI3(id, 0, 3);
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("actorInfo", actorInfo);
			datas.put("mvActorInfos", mvActorInfos);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		String resString = getResponseData(datas);
		return resString;
	}

	/**
	 * 获取电影单个角色所参与的所有电影
	 * 
	 * @param reqs
	 * @return
	 */
	public String viewActorMovies(String reqs) {
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		Long id = null;
		int page = 1;
		int start = 0;
		// 去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			id = Long.valueOf(dataq.get("id").toString());
			page = Integer.valueOf(dataq.get("page").toString());
			start = (page - 1) * 10;
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		boolean newQuery = false;
		try {
			newQuery = Integer.valueOf(dataq.get("newQuery").toString()) == 0 ? true : false;
		} catch (Exception e) {
			LOG.debug("test");
		}
		// 角色参与的电影
		List<MvActorInfo> mvActorInfos = new ArrayList<MvActorInfo>();
		if (!newQuery) {
			mvActorInfos = movieAutoUtil.getMvActorInfoByActorI3(id, start, 10);
		} else {
			mvActorInfos = movieAutoUtil.getMvActorInfoByActorId(id, start, 10);
		}

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("mvActorInfos", mvActorInfos);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		String resString = getResponseData(datas);
		return resString;
	}

	/**
	 * 单个电影的详情(新接口)
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewOneMovieDetail(String reqs, Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		Long id = null;
		int isDB = 0;
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Long.valueOf(dataq.get("id").toString());
			// isDB = Integer.valueOf(dataq.get("isDB").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		MovieInfo movieInfo = new MovieInfo();
		
		MvInfo mvInfo = mvFacade.queryById(id);
		
		// 将电影实体类格式化
		movieInfo = movieUtils.putMVToMovieInfo(mvInfo, uid, mvCommentFacade, mvStillsFacade, FALSE);
		
//		if (!StringUtils.isEmpty(mvInfo.getMoviePic2())) {
//			movieInfo.setMovieHPic(mvInfo.getMoviePic2());
//		} else {
//			if (!StringUtils.isEmpty(mvInfo.getExtendField1()) && !StringUtils.equals(mvInfo.getExtendField1().trim(), "0")) {
//				movieInfo.setMovieHPic(mvInfo.getExtendField1());
//			}
//		}
		ActorInfo actorInfo = null;
		List<ActorInfo> directorActorInfos = movieAutoUtil.getActorInfos(id, MvActorDAOImpl.actorTypeDirector, false);
		if (directorActorInfos.size() > 0) {
			actorInfo = directorActorInfos.get(0);
		}
		List<ActorInfo> actorInfos = movieAutoUtil.getActorInfos(id, MvActorDAOImpl.actorTypeStarring, false);
		if (actorInfo != null) {
			actorInfos.add(0, actorInfo);
		}
		List<ArticleInfo> articleInfos = movieAutoUtil.findResListByResIdAndType(id, CommentUtils.TYPE_MV_MESSAGE, -1);// CommentUtils.TYPE_ARTICLE
		// TYPE_NEWARTICLE CommentUtils.TYPE_MV_MESSAGE

		// 图解电影
		List<ResourceLink> resLinks = movieAutoUtil.listResListByResIdAndType(id, CommentUtils.CASETYPE_MOVIE_SCHEME);
		int chartMovie = resLinks.size() > 0 ? 1 : 0;
		movieInfo.setChartMovie(String.valueOf(chartMovie));

		// 是否在我影单中
		MvListLink mvListLink = myMovieFacade.findUserIsCollectMovie(uid, movieInfo.getId());
		if (mvListLink.getId() != TRUE) {
			movieInfo.setInList(TRUE);
		}
		int flagint=ResultUtils.QUERY_ERROR;
		if (movieInfo != null && movieInfo.getId() != UNID) {
			flagint = ResultUtils.SUCCESS;
		}
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("movieInfo", movieInfo);
			datas.put("actorInfos", actorInfos);
			datas.put("articleInfos", articleInfos);
			/**
			 * 打印热搜电影的日志
			 */
			BussinessLogUtils.searchMovieLog(mvInfo.getId(), mvInfo.getName(), mvInfo.getType(), uid);
			try{
				//记录阅读数量
				resStatJedisManager.addReadNum(mvInfo.getId(), CommentUtils.TYPE_MOVIE, 0, "", 0);
			}catch(Exception e){
				LOG.error("detail增加电影的阅读数量出错，id为：" + movieInfo.getId() + e.getMessage(), e.fillInStackTrace());
			}
			// 从缓存中获取电影被收藏的数量
			int count = 0;
			try {
				count = resourceManager.getResourceCollectCountFromJedis((long) movieInfo.getId(), CommentUtils.TYPE_MOVIE);
			} catch (Exception e) {
				count = 0;
				LOG.error("获取电影被收藏数量出错，id为：" + movieInfo.getId() + e.getMessage(), e.fillInStackTrace());
			}
			datas.put("collectCount", count);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		// 处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 获取电影资讯
	 * 
	 * @param reqs
	 * @return
	 */
	public String viewMovieMessage(String reqs) {
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		Long id = null;
		// 去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		List<ArticleInfo> articleInfos = movieAutoUtil.findResListByResIdAndType(id, CommentUtils.TYPE_MV_MESSAGE, 0);// CommentUtils.TYPE_ARTICLE

		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("articleInfos", articleInfos);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		String resString = getResponseData(datas);
		return resString;
	}

	/**
	 * 获取电影图解
	 * 
	 * @param reqs
	 * @author zhangqi
	 * @return
	 */
	public String viewMovieScheme(String reqs) {
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		Long id = null;
		// 去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		List<ResourceLink> resLinks = movieAutoUtil.listResListByResIdAndType(id, CommentUtils.CASETYPE_MOVIE_SCHEME);
		int movieScheme = resLinks.size() > 0 ? 1 : 0;
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("movieScheme", movieScheme);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		String resString = getResponseData(datas);
		return resString;
	}

	/**
	 * 获取电影花絮
	 * 
	 * @param reqs
	 * @author zhangqi
	 * @return
	 */
	public String viewMovieScoop(String reqs) {
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		Long id = null;
		// 去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		// 角色参与的电影
		List<DiaryInfo> diaryInfos = movieAutoUtil.findResListByResIdAndType(id, CommentUtils.TYPE_MOVIE_SCOOPY);// CommentUtils.TYPE_DIARY
		datas = new HashMap<String, Object>();
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("diaryInfos", diaryInfos);
		} else {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		String resString = getResponseData(datas);
		return resString;
	}

	// /**
	// * 分组list
	// * @param list
	// * @param type
	// * @return
	// */
	// public List<BookInfo> getBKResponseList(List reqList , String type ,
	// List<BookInfo> resList){
	// BookInfo bookInfo = null;
	// if(reqList.size()>1){
	// flagint = ResultUtils.SUCCESS;
	// for (Object obj : reqList) {
	// bookInfo = FileUtils.putBKToBookInfo(obj, TRUE);
	// resList.add(bookInfo);
	// }
	// }else if(reqList.size() == 0){
	// flagint = ResultUtils.SUCCESS;
	// }else{
	// String objName = reqList.get(0).getClass().getName();
	// if(BkInfo.class.getName().equals(objName)){
	// //库中
	// BkInfo obj = (BkInfo) reqList.get(0);
	// flagint = obj.getFlag();
	// if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
	// long id = obj.getId();
	// if(id != 0){
	// bookInfo = FileUtils.putBKToBookInfo(obj, TRUE);
	// resList.add(bookInfo);
	// }
	// }
	// }else if(MyBk.class.getName().equals(objName)){
	// //我的库中
	// MyBk obj = (MyBk) reqList.get(0);
	// flagint = obj.getFlag();
	// if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
	// long id = obj.getId();
	// if(id != 0){
	// bookInfo = FileUtils.putBKToBookInfo(obj, TRUE);
	// resList.add(bookInfo);
	// }
	// }
	//
	// }
	// }
	// return resList;
	// }
	/**
	 * 分组list
	 * 
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getResponseList(List reqList, Long uid, List<ResourceInfo> resList) {
		ResourceInfo ri = null;
		if (reqList.size() > 1) {
			flagint = ResultUtils.SUCCESS;
			for (Object obj : reqList) {
				String objName = obj.getClass().getName();
				if (MvComment.class.getName().equals(objName)) {
					ri = movieUtils.putMVCommentToResource(obj, ucenterFacade, mvFacade);
					try {
						actUtils.putIsCollectToResoure(ri, uid, actFacade, 0);
					} catch (Exception e) {
						LOG.error("添加附加内容时出错，资源类型[" + ri.getType() + "]id为:" + ri.getRid() + e.getMessage(), e.fillInStackTrace());
					}
					// 将各种判定加入资源类
				} else if (MovieTalk.class.getName().equals(objName)) {
					ri = movieUtils.putMovieTalkToResource(obj, ucenterFacade);
				}
				resList.add(ri);
			}
		} else if (reqList.size() == 0) {
			flagint = ResultUtils.SUCCESS;
		} else {
			Object obj = reqList.get(0);
			String objName = obj.getClass().getName();
			long id = 0;
			if (MvComment.class.getName().equals(objName)) {
				MvComment object = (MvComment) obj;
				id = object.getId();
			}

			if (id != 0) {
				ri = movieUtils.putMVCommentToResource(obj, ucenterFacade, mvFacade);
				try {
					actUtils.putIsCollectToResoure(ri, uid, actFacade, 0);
				} catch (Exception e) {
					LOG.error("添加附加内容时出错，资源类型[" + ri.getType() + "]id为:" + ri.getRid() + e.getMessage(), e.fillInStackTrace());
				}
			} else if (MovieTalk.class.getName().equals(objName)) {
				ri = movieUtils.putMovieTalkToResource(obj, ucenterFacade);
			}
			resList.add(ri);
		}
		return resList;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}

	public void setMovieTalkFacade(MovieTalkFacade movieTalkFacade) {
		this.movieTalkFacade = movieTalkFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setMvStillsFacade(MvStillsFacade mvStillsFacade) {
		this.mvStillsFacade = mvStillsFacade;
	}

	public void setMsgFacade(MsgFacade msgFacade) {
		this.msgFacade = msgFacade;
	}

	public void setBigFacade(BigFacade bigFacade) {
		this.bigFacade = bigFacade;
	}

	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
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

	public void setMvActorFacade(MvActorFacade mvActorFacade) {
		this.mvActorFacade = mvActorFacade;
	}

	public void setMovieAutoUtil(MovieAutoUtil movieAutoUtil) {
		this.movieAutoUtil = movieAutoUtil;
	}

	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}

	public void setGraphicFilmFacade(GraphicFilmFacade graphicFilmFacade) {
		this.graphicFilmFacade = graphicFilmFacade;
	}

	public void setResourceLinkFacade(ResourceLinkFacade resourceLinkFacade) {
		this.resourceLinkFacade = resourceLinkFacade;
	}
	public void setActivityFacade(ActivityFacade activityFacade) {
		this.activityFacade = activityFacade;
	}
}
