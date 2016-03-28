package com.poison.eagle.manager.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.keel.utils.UKeyWorker;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.manager.UserJedisManager;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.store.client.MvFacade;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserStatistics;

/**
 * 用户写书单影单manager
 * 
 * @author Administrator
 * 
 */
public class MovieWebManager extends BaseManager {
	private static final Log LOG = LogFactory.getLog(MovieWebManager.class);
	// private Map<String, Object> req ;
	// private Map<String, Object> dataq;
	// private Map<String, Object> datas ;
	// private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
	// private String error;

	// private List<Map<String,String>> commentList;

	private int flagint;

	private MvFacade mvFacade;
	private MvCommentFacade mvCommentFacade;
	private MyMovieFacade myMovieFacade;
	private UcenterFacade ucenterFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private ActFacade actFacade;
	private TopicFacade topicFacade;
	private ActUtils actUtils = ActUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();

	private ResourceManager resourceManager;


	private UserJedisManager userJedisManager;
	/**
	 * 写/修改影评
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeMovieComment(HttpServletRequest request,Long uid) {
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		Map<String, Object> datas = new HashMap<String, Object>();
		String error = "";

		long id = 0;
		String score = null;
		String content = request.getParameter("content");
		String picUrlStr = request.getParameter("picUrl");
		String picUrl[]=null;
		if(picUrlStr!=null && picUrlStr.length()>0){
			picUrl = picUrlStr.split(",");
		}
		Long commentId = null;
		String beforeScore = "";
		Long movieListId = null;
		String lon = "";// 经度
		String lat = "";// 维度
		String locationDescription = "";// 地点描述
		String locationCity = "";// 地点城市
		String locationArea = "";// 地点地区
		Long vid = 0l;// 虚拟主键
		String title = "";// 标题
		String cover = "";// 封面
		String resourceType = CommentUtils.TYPE_MOVIE_COMMENT;// 资源类型 默认为7影评
		String resString = null;
		try {
			score = request.getParameter("score");
			String idstr = request.getParameter("id");
			// 标题
			title = CheckParams.objectToStr(request.getParameter("title"));
			// 封面
			cover = CheckParams.objectToStr(request.getParameter("cover"));
			String str = "score:"+score+"-id:"+idstr+"-title:"+title+"-content:"+content+"-picUrl:"+picUrlStr;
			if(idstr==null || !StringUtils.isInteger(idstr)){
				error = "电影id不合法";
				datas.put("error", error+"-"+str);
				datas.put("flag", flag);
				resString = getResponseData(datas);
				return resString;
			}
			if(score==null || score.length()==0){
				error = "评分不能为空";
				datas.put("error", error+"-"+str);
				datas.put("flag", flag);
				resString = getResponseData(datas);
				return resString;
			}
			if(content==null || content.length()==0){
				error = "内容不能为空";
				datas.put("error", error+"-"+str);
				datas.put("flag", flag);
				resString = getResponseData(datas);
				return resString;
			}
			try {
				commentId = Long.valueOf(request.getParameter("commentId")+"");
			} catch (Exception e) {
				commentId = 0l;
			}
			
			id = Long.valueOf(idstr);
			lon = CheckParams.objectToStr(request.getParameter("lon"));
			lat = CheckParams.objectToStr(request.getParameter("lat"));
			locationDescription = CheckParams.objectToStr(request.getParameter("locationName"));
			if (commentId == UNID) {
				// isDB = Integer.valueOf(dataq.get("isDB").toString());
			} else {
				beforeScore = request.getParameter("beforeScore");
				if (null == beforeScore) {
					beforeScore = "0";
				}
			}
			try {
				movieListId = Long.valueOf(request.getParameter("movieListId"));
				if (movieListId == 0) {
					movieListId = null;
				}
			} catch (Exception e) {
				movieListId = null;
			}
			if(title!=null && title.length()>0){
				resourceType = CommentUtils.TYPE_ARTICLE_MOVIE;//有标题则为长影评
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		// 获取用户等级
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		int level = userAllInfo.getLevel();
		if(level!=50){
			level = 0;
		}

		MvComment mvComment = new MvComment();

		
		StringBuffer html = new StringBuffer("");
		html.append("<div>" + content +"</div>").append("<br/><!--我是分隔符woshifengefu-->");
		if(picUrl!=null && picUrl.length>0){
			for(int i=0;i<picUrl.length;i++){
				html.append("<img src=\"" + picUrl[i] + "\"/>").append("<br/><!--我是分隔符woshifengefu-->");
			}
		}
		String resultContent = html.toString();
		if (commentId == UNID) {// 插入评论
			mvComment = mvCommentFacade.addMvComment(uid, id, resultContent, score, TRUE, 0, String.valueOf(level), movieListId, lon, lat, locationDescription, locationCity, locationArea, title,
					cover, resourceType);// (uid, id,
											// WebUtils.putDataToHTML5(content),
											// score, TRUE, 0,
											// String.valueOf(level));
			// mvComment = mvCommentFacade.addMvComment(uid, id,
			// WebUtils.putDataToHTML5(content), score, TRUE, isDB);
			if (level == CommentUtils.USER_LEVEL_TALENT) {
				MvAvgMark avgMark = mvCommentFacade.addExpertsAvgMark(id, Float.valueOf(score));
			} else {
				MvAvgMark avgMark = mvCommentFacade.addMvAvgMark((int) id, Float.valueOf(score));
			}

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
		} else {// 修改评论
			// 常江修改
			
			//==============================start===================================
			//web端发表和编辑影评、书评、文字的时候不能上传音乐和视频，所以在编辑的时候需要保存之前的音频和视频
			MvComment oldmvComment = mvCommentFacade.findMvCommentIsExist(commentId);
			if(oldmvComment!=null){
				//音频常量
				String AUDIO_BEGIN = "<audio src=\"";
				String AUDIO_END = "\" controls=\"controls\"></audio>";
				//视频常量
				String VIDEO_BEGIN = "<video src=\"";
				String VIDEO_END  ="\" controls=\"contro" +
						"ls\"></video>";
				List<Map<String, String>> list = WebUtils.putHTMLToData(oldmvComment.getContent());
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, String> map = list.get(i);
						String type = map.get("type");
						String data = map.get("data");
						String length = map.get("length");
						if("2".equals(type)){
							if(length!=null && length.length()>0){
								data = data.substring(0,data.lastIndexOf("."))+"_"+length+data.substring(data.lastIndexOf("."));
							}
							html.append(AUDIO_BEGIN + data + AUDIO_END).append("<br/><!--我是分隔符woshifengefu-->");
						}else if("3".equals(type)){
							html.append(VIDEO_BEGIN + data + VIDEO_END).append("<br/><!--我是分隔符woshifengefu-->");
						}
					}
					resultContent = html.toString();
				}
			}
			//=======================================end===================================================
			
			mvComment = mvCommentFacade.updateMyMvComment(commentId, resultContent, score,title,cover);
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
			if (level == CommentUtils.USER_LEVEL_TALENT) {
				MvAvgMark avgMark = mvCommentFacade.updateExpertsAvgMark(id, Float.valueOf(score), Float.valueOf(beforeScore));// (id,
																																// Float.valueOf(score));
			} else {
				MvAvgMark avgMark = mvCommentFacade.updateMvAvgMark((int) id, Float.valueOf(score), Float.valueOf(beforeScore));
			}

			// 最后修改时间
			long latestRevisionDate = mvComment.getLatestRevisionDate();
			vid = ((latestRevisionDate - UKeyWorker.getTwepoch() << UKeyWorker.getTimestampshift()));
			
		}

		flagint = mvComment.getFlag();

		// 如果有@消息，将此消息存入表中，并推送给相关的人
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			// 将生成的影评放入到缓存中
			resourceManager.setResourceToJedis(mvComment, uid, uid, vid);
			// 将附加信息加入缓存中
			resourceManager.updateInListByJedis(mvComment.getMovieId(), uid, CommentUtils.TYPE_MOVIE, "0");
			// 更新用户的最后更新时间
			ucenterFacade.saveUserLatestInfo(uid, mvComment.getId(), CommentUtils.TYPE_MOVIE_COMMENT);
		}

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
		resString = getResponseData(datas);

		return resString;
	}

	
	/**
	 * 模糊查询影评
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String searchMvCommentList(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		String keyword = request.getParameter("keyword");
		String starttimestr = request.getParameter("starttime");
		String endtimestr = request.getParameter("endtime");
		Long starttime = null;
		Long endtime = null;
		String format = "yyyy-MM-dd HH:mm:ss";
		if(starttimestr!=null && endtimestr!=null && starttimestr.trim().length()>0 && endtimestr.trim().length()>0){
			starttime = DateUtil.formatLong(starttimestr+" 00:00:00", format);
			endtime = DateUtil.formatLong(endtimestr+" 23:59:59", format);
		}else{
			if(starttimestr!=null && starttimestr.trim().length()>0){
				starttime = DateUtil.formatLong(starttimestr+" 00:00:00", format);
				endtime = DateUtil.formatLong(starttimestr+" 23:59:59", format);
			}else if(endtimestr!=null && endtimestr.trim().length()>0){
				starttime = DateUtil.formatLong(endtimestr+" 00:00:00", format);
				endtime = DateUtil.formatLong(endtimestr+" 23:59:59", format);
			}
		}
		String pagenumStr = request.getParameter("pagenum");
		int pagenum = 1;
		if(StringUtils.isInteger(pagenumStr)){
			pagenum = Integer.parseInt(pagenumStr);
		}
		if(pagenum<1){
			pagenum = 1;
		}
		int pagesize = 10;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		Map<String,Object> datas = new HashMap<String,Object>();
		List<MvComment> mvComments = mvCommentFacade.searchMvCommentByLike(uid, keyword, starttime, endtime,start, pagesize);
		if(!(mvComments!=null && mvComments.size()==1 && mvComments.get(0).getFlag()==ResultUtils.QUERY_ERROR)){
			Map<String,Object> amountmap = mvCommentFacade.findMvCommentCountByLike(uid, keyword, starttime, endtime);
			long amount = 0;
			if(amountmap!=null){
				amount = (Integer) amountmap.get("count");
			}
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(); 
			resourceInfos = getResponseList(mvComments, uid, resourceInfos);
			//Collections.sort(resourceInfos);
			request.setAttribute("list", resourceInfos);
			datas.put("list", resourceInfos);
			datas.put("amount", amount);
			datas.put("pagesize", pagesize);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	
	
	/**
	 * 查询一个影评的详细信息
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getOneMvComment(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		String commentIdStr = request.getParameter("commentId");
		Long commentId = null;
		if(StringUtils.isInteger(commentIdStr)){
			commentId = Long.valueOf(commentIdStr);
		}
		if(commentId==null || commentId<=0){
			String error = "影评id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			String resString = getResponseData(datas);
			return resString;
		}
		MvComment mvComment = mvCommentFacade.findMvCommentIsExist(commentId);
		if(mvComment!=null && mvComment.getId()>0){
			ResourceInfo resourceInfo = movieUtils.putMVCommentToResource(mvComment, ucenterFacade, mvFacade,FALSE);
			datas.put("map", resourceInfo);
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			String error = MessageUtils.getResultMessage(ResultUtils.QUERY_ERROR);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 删除影评
	 * 
	 * @param req
	 *            从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delMovieComment(HttpServletRequest request, Long uid) {
		Map<String, Object> datas = new HashMap<String, Object>();
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		String resString = null;
		long id = 0;
		long movieId = 0;
		//String beforeScore = request.getParameter("beforeScore");
		String idstr = request.getParameter("commentId");
		//String movieIdStr = request.getParameter("movieId");
		if(idstr==null || !StringUtils.isInteger(idstr)){
			error = "评论id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		id = Long.valueOf(idstr);
		//movieId = Long.valueOf(movieIdStr);

		MvComment mvComment = mvCommentFacade.deleteMvComment(id);

		String beforeScore = mvComment.getScore();
		movieId = mvComment.getMovieId();
		
		flagint = mvComment.getFlag();
		
		if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
			int level = userAllInfo.getLevel();
			if (level == CommentUtils.USER_LEVEL_TALENT) {
				mvCommentFacade.deleteExpertsAvgMark((int) movieId, Float.valueOf("0.0"), Float.valueOf(beforeScore));
			} else {
				mvCommentFacade.deleteMvAvgMark((int) movieId, Float.valueOf("0.0"), Float.valueOf(beforeScore));
			}
		}
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
		resString = getResponseData(datas);

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

			id = Long.valueOf(dataq.get("lastId").toString());
			movieId = Long.valueOf(dataq.get("movieId").toString());
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = null;
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}

			// 是长评还是短评列表
			resourceType = CheckParams.objectToStr((String)dataq.get("resourceType"));
			if ("".equals(resourceType)) {
				resourceType = CommentUtils.TYPE_MOVIE_COMMENT;
			}
			
			//System.out.println("得到的resType类型为"+resourceType+"得到的最后lastid为"+id+"得到的电影id为"+movieId+"得到的类型为type"+type);

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
		if (id == UNID) {
			mvComments = mvCommentFacade.findAllMvComment(movieId, null, type, resourceType);
		} else {
			mvComments = mvCommentFacade.findAllMvComment(movieId, id, type, resourceType);
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

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
}
