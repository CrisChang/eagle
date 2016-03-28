package com.poison.eagle.manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.util.ReflectionUtils;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActAt;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActUseful;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Post;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.story.client.IStoryCommentFacade;
import com.poison.story.client.StoryFacade;
import com.poison.story.client.impl.StoryCommentFacadeImpl;
import com.poison.story.model.Story;
import com.poison.story.model.StoryComment;
import com.poison.ucenter.client.StoryUserFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class MessageCenterManager extends BaseManager{

	private static final  Log LOG = LogFactory.getLog(MessageCenterManager.class);
	
	private ActFacade actFacade;
	private UcenterFacade ucenterFacade;
	private BkCommentFacade bkCommentFacade;
	private MvCommentFacade mvCommentFacade;
	private GetResourceInfoFacade getResourceInfoFacade;
	private MyMovieFacade myMovieFacade;
	private DiaryFacade diaryFacade;
	private BkFacade bkFacade;
	private MvFacade mvFacade;
	private PostFacade postFacade;
	private ArticleFacade articleFacade;
	/**
	 * 用户的相关缓存
	 */
	private UserJedisManager userJedisManager;
	
	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	public void setDiaryFacade(DiaryFacade diaryFacade) {
		this.diaryFacade = diaryFacade;
	}

	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}

	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}

	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}

	/**
	 * 
	 * <p>Title: viewUserCommentMSG</p> 
	 * <p>Description: 查看用户的评论中心列表</p> 
	 * @author :changjiang
	 * date 2015-3-9 下午8:22:44
	 * @param reqs
	 * @return
	 */
	public String viewUserCommentMSG(String reqs){
		String finalreq = "";
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Long userId = 0l;//用户id
		Long rid = 0l;
		//去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			userId =  Long.valueOf((String) dataq.get("userId"));
			try{
				rid = Long.valueOf(dataq.get("rid").toString());
			}catch (Exception e) {
				rid = 0l;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//清除用户评论的缓存数量
		userJedisManager.saveOneUserInfo(userId, JedisConstant.USER_HASH_COMMENT_NOTICE, "0");
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Map<String, Object> allMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;//new HashMap<String, Object>();
		Map<String, Object> userMap = null;//new HashMap<String, Object>();
		Map<String, Object> resMap = null;//new HashMap<String, Object>();
		Map<String, Object> recommentMap = null;//new HashMap<String, Object>();
		Map<String, Object> replyMap = null;
		Map<String, Object> rreplyMap = null;
		//评论
		long commentId = 0l;
		long resid = 0l;
		long commentUserId = 0l;
		String comment = "";
		String type = "";
		long bookId = 0l;
		long movieId = 0l;
		long ruid = 0l;
		long btime = 0l;
		long id = 0l;
		String publishTime = "";
		ActComment actComment = new ActComment();
		UserInfo userInfo = new UserInfo();
		
		if(0==rid){
			rid = null;
		}
		List<ActComment> commentList = actFacade.findUserCommentCenter(userId, rid);
		Iterator<ActComment> commentIt = commentList.iterator();
		
		BookList bookList = new BookList();
		MovieList movieList = new MovieList();
		Diary diary = new Diary();
		Post post = new Post();
		Article article = new Article();
		BkComment bkComment = new BkComment();
		MvComment mvComment = new MvComment();
		BkInfo bkInfo  = new BkInfo();
		MvInfo mvInfo = new MvInfo();
		ActComment actCmt = new ActComment();
		ActComment replyActCmt = new ActComment();
		while(commentIt.hasNext()){
			actComment = commentIt.next();
			id = actComment.getId();
			commentUserId = actComment.getUserId();
			comment = CheckParams.objectToStr(actComment.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", "");
			commentId = actComment.getCommentId();
			resid = actComment.getResourceId();
			type = actComment.getType();
			btime = actComment.getLatestRevisionDate();
			Date date = new Date(btime);
			publishTime = sf.format(date);
			//获取用户信息
			userInfo = ucenterFacade.findUserInfoByUserId(null, commentUserId);
					//findUserInfo(null,commentUserId);
			map = new HashMap<String, Object>();
			userMap = new HashMap<String, Object>();
			resMap = new HashMap<String, Object>();
			recommentMap = new HashMap<String, Object>();
			replyMap = new HashMap<String, Object>();
			rreplyMap = new HashMap<String, Object>();
			userMap.put("userId", userInfo.getUserId());
			userMap.put("name", userInfo.getName());
			userMap.put("faceAddress", userInfo.getFaceAddress());
			userMap.put("sex", userInfo.getSex());
			userMap.put("type", userInfo.getLevel());
			if(commentId==0){//当评论的id为0时，直接获取资源
				recommentMap.put("isRecomment", "0");//0为直接资源
			}else{
				recommentMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(commentId);//查询在这条评论的回复
				replyMap.put("replyId", actCmt.getId());
				replyMap.put("replyComment", CheckParams.objectToStr(actCmt.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				
				long replyCommentId = actCmt.getCommentId();
				if(replyCommentId!=0){//当为回复的回复的时候
					recommentMap.put("isRecomment", "2");//2为回复的回复资源
					long replyUserId = actCmt.getCommentUserId();
					replyActCmt = actFacade.findCmtById(commentId);//查询回复的回复
					UserInfo replyUserInfo = new UserInfo();
					replyUserInfo = ucenterFacade.findUserInfoByUserId(null, replyUserId);
					if(null==replyUserInfo){
						continue;
					}
							//findUserInfo(null,replyUserId);
					rreplyMap.put("userId", replyUserInfo.getUserId());
					rreplyMap.put("name", replyUserInfo.getName());
					rreplyMap.put("faceAddress", replyUserInfo.getFaceAddress());
					rreplyMap.put("sex", replyUserInfo.getSex());
					rreplyMap.put("type", replyUserInfo.getLevel());
					//加载回复的回复信息
					rreplyMap.put("rreplyId", replyActCmt.getId());
					rreplyMap.put("rreplyComment",  CheckParams.objectToStr(replyActCmt.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				}
			}
			recommentMap.put("id", id);
			recommentMap.put("comment", comment);
			recommentMap.put("publishTime", publishTime);
				if(type.equals(CommentUtils.TYPE_BOOKLIST)){//书单
					bookList = getResourceInfoFacade.queryByIdBookList(resid);
					//ruid = bookList.getuId();
					resMap.put("rid", bookList.getId());//书单id
					resMap.put("rcomment", bookList.getReason());//书单回复
					resMap.put("restype", type);//书单类型
					resMap.put("rpic", bookList.getCover());//书单封面
					resMap.put("rrname",bookList.getBookListName());//书单内容
					resMap.put("ruserid", bookList.getuId());//书单作者id
					UserInfo ruserInfo =ucenterFacade.findUserInfoByUserId(null, bookList.getuId());
					if(null==ruserInfo){
						continue;
					}
							//findUserInfo(null, bookList.getuId());
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书单作者id
					resMap.put("rauthor", "");//书单作者
					resMap.put("rusername", ruserInfo.getName());//书单作者
				}else if(type.equals(CommentUtils.TYPE_MOVIELIST)){//影单
					movieList = myMovieFacade.findMovieListById(resid);
					resMap.put("rid", movieList.getId());//影单id
					resMap.put("rcomment", movieList.getReason());//影单回复
					resMap.put("restype", type);//影单类型
					resMap.put("rpic", movieList.getCover());//影单封面
					resMap.put("rrname",movieList.getFilmListName());//影单名字
					resMap.put("ruserid", movieList.getUid());//影单作者id
					UserInfo ruserInfo =ucenterFacade.findUserInfoByUserId(null, movieList.getUid());
					if(null==ruserInfo){
						continue;
					}
							//findUserInfo(null, movieList.getUid());
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书单作者id
					resMap.put("rauthor", "");//书单作者
					resMap.put("rusername", ruserInfo.getName());//书单作者
				}else if(type.equals(CommentUtils.TYPE_DIARY)){//日志
					diary = diaryFacade.queryByIdDiary(resid);
					resMap.put("rid", diary.getId());//日志id
					resMap.put("rcomment", "");//日志内容
					resMap.put("restype", type);//日志类型
					resMap.put("rpic", "");//日志封面
					resMap.put("rrname", CheckParams.objectToStr(diary.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", diary.getUid());//日志作者id
					UserInfo ruserInfo =ucenterFacade.findUserInfoByUserId(null, diary.getUid());
					if(null==ruserInfo){
						continue;
					}
							//findUserInfo(null, diary.getUid());
					resMap.put("ruserface", ruserInfo.getFaceAddress());//日志作者id
					resMap.put("rauthor", "");//日志作者
					resMap.put("rusername", ruserInfo.getName());//日志作者
				}else if(type.equals(CommentUtils.TYPE_ARTICLE)){//长文章
					post = postFacade.queryByIdName(resid);
					resMap.put("rid", post.getId());//长文章id
					resMap.put("rcomment", post.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(post.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", post.getUid());//长文章作者id
					UserInfo ruserInfo =ucenterFacade.findUserInfoByUserId(null, post.getUid());
					if(null==ruserInfo){
						continue;
					}
							//findUserInfo(null, post.getUid());
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章作者id
					resMap.put("rauthor", "");//长文章作者
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_NEWARTICLE)){//新的长文章
					article = articleFacade.queryArticleById(resid);
					resMap.put("rid", article.getId());//长文章id
					resMap.put("rcomment", article.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(article.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", article.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, article.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章作者id
					resMap.put("rauthor", "");//长文章作者
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//书评
					bkComment = bkCommentFacade.findCommentIsExistById(resid);
					if(null==bkComment){
						bkComment = new BkComment();
					}
					String commentext = bkComment.getComment();
					if(null==commentext){
						commentext = "";
					}
					resMap.put("rid", bkComment.getId());//书评id
					resMap.put("rcomment", commentext.replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//书评内容
					resMap.put("restype", type);//书评类型
					bookId = bkComment.getBookId();
					bkInfo = bkFacade.findBkInfo((int)bookId);
					resMap.put("rpic", bkInfo.getBookPic());//图书封面
					resMap.put("rrname", bkInfo.getName());//图书名字
					resMap.put("rauthor", bkInfo.getAuthorName());//图书作者
					resMap.put("ruserid", bkComment.getUserId());//书评作者id
					UserInfo ruserInfo =ucenterFacade.findUserInfoByUserId(null, bkComment.getUserId());
					if(null==ruserInfo){
						continue;
					}
							//findUserInfo(null, bkComment.getUserId());
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评作者封面
					resMap.put("rusername", ruserInfo.getName());//书评作者
					//resMap.put("rauthor", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//影评
					mvComment = mvCommentFacade.findMvCommentIsExist(resid);
					resMap.put("rid", mvComment.getId());//影评id
					resMap.put("rcomment", CheckParams.objectToStr(mvComment.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//影评内容
					resMap.put("restype", type);//影评内容
					movieId = mvComment.getMovieId();
					mvInfo = mvFacade.queryById(movieId);
					resMap.put("rpic",mvInfo.getMoviePic());//电影封面
					resMap.put("rrname",mvInfo.getName());//电影名字
					resMap.put("rauthor",CheckParams.objectToStr(mvInfo.getDirector()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//电影导演
					resMap.put("ruserid", mvComment.getUserId());//影评作者id
					UserInfo ruserInfo =ucenterFacade.findUserInfoByUserId(null, mvComment.getUserId());
					if(null==ruserInfo){
						continue;
					}
							//findUserInfo(null, mvComment.getUserId());
					resMap.put("ruserface", CheckParams.objectToStr(ruserInfo.getFaceAddress()));//影评作者封面
					resMap.put("rusername", ruserInfo.getName());//影评作者
				}
				if(null!=resMap&&!resMap.isEmpty()){
					Long ridLong =(Long) resMap.get("rid");
					if(ridLong.longValue()!=0l){
						recommentMap.put("res", resMap);
						recommentMap.put("user", userMap);
						recommentMap.put("reply", replyMap);
						recommentMap.put("rreplyMap", rreplyMap);
						//map.put("res", resMap);
						map.put("recomment", recommentMap);
						list.add(map);
					}
				}
			//}
				/*else{//当评论的id不为0时，为回复资源
				recommentMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(commentId);
				recommentMap.put("recomid", actCmt.getId());
				recommentMap.put("recomment", actCmt.getCommentContext());
				resMap.put("", value);
				movieId = mvComment.getMovieId();
				mvInfo = mvFacade.queryById(movieId);
			}*/
		}
		//allMap.put("data", list);
		//allMap.put("", value);
		try {
			finalreq = getObjectMapper().writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return finalreq;
	}
	
	/**
	 * 
	 * <p>Title: viewUserPraiseMSG</p> 
	 * <p>Description: 查询用户的点赞列表</p> 
	 * @author :changjiang
	 * date 2015-3-11 下午8:28:58
	 * @param reqs
	 * @return
	 */
	public String viewUserPraiseMSG(String reqs){
		String finalreq = "";
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Long userId = 0l;//用户id
		Long rid = null;
		//去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			userId =  Long.valueOf((String) dataq.get("userId"));
			try{
				rid = Long.valueOf(dataq.get("rid").toString());
			}catch (Exception e) {
				rid = 0l;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Map<String, Object> allMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;//new HashMap<String, Object>();
		Map<String, Object> userMap = null;//new HashMap<String, Object>();
		Map<String, Object> resMap = null;//new HashMap<String, Object>();
		Map<String, Object> praiseMap = null;//new HashMap<String, Object>();
		Map<String, Object> replyMap = null;
		//评论
		long praiseId = 0l;
		long resid = 0l;
		long praiseUserId = 0l;
		String type = "";
		long bookId = 0l;
		long movieId = 0l;
		long ruid = 0l;
		long btime = 0l;
		String publishTime = "";
		ActPraise actPraise = new ActPraise();
		UserAllInfo userInfo = new UserAllInfo();
		if(0==rid){
			rid = null;
		}
		//用户的点赞提醒数清零
		userJedisManager.saveOneUserInfo(userId, JedisConstant.USER_HASH_PRAISE_NOTICE, "0");
		
		List<ActPraise> praiseList = actFacade.findPraiseListByResUid(userId, rid);
		//System.out.println("查询出来的点赞条数为========"+praiseList.size());
		Iterator<ActPraise> praiseIt = praiseList.iterator();
		
		BookList bookList = new BookList();
		MovieList movieList = new MovieList();
		Diary diary = new Diary();
		Post post = new Post();
		Article article = new Article();
		BkComment bkComment = new BkComment();
		MvComment mvComment = new MvComment();
		BkInfo bkInfo  = new BkInfo();
		MvInfo mvInfo = new MvInfo();
		ActComment actCmt = new ActComment();
		while(praiseIt.hasNext()){
			actPraise = praiseIt.next();
			praiseId = actPraise.getId();
			praiseUserId = actPraise.getUserId();
			resid = actPraise.getResourceId();
			type = actPraise.getType();
			btime = actPraise.getLatestRevisionDate();
			Date date = new Date(btime);
			publishTime = sf.format(date);
			//获取用户信息
			userInfo = ucenterFacade.findUserInfo(null,praiseUserId);
			map = new HashMap<String, Object>();
			userMap = new HashMap<String, Object>();
			resMap = new HashMap<String, Object>();
			praiseMap = new HashMap<String, Object>();
			replyMap = new HashMap<String, Object>();
			userMap.put("userId", userInfo.getUserId());
			userMap.put("name", userInfo.getName());
			userMap.put("faceAddress", userInfo.getFaceAddress());
			userMap.put("sex", userInfo.getSex());
			userMap.put("type", userInfo.getLevel());
			
			praiseMap.put("id", praiseId);
			//praiseMap.put("comment", comment);
			praiseMap.put("publishTime", publishTime);
			praiseMap.put("isRecomment", "0");//0为普通资源
			
			if(type.equals(CommentUtils.TYPE_COMMENT)){//评论
				praiseMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(resid);//查询在这条评论的回复
				replyMap.put("replyId", actCmt.getId());
				replyMap.put("replyComment", CheckParams.objectToStr(actCmt.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				type = actCmt.getType();
				resid = actCmt.getResourceId();
			}
				if(type.equals(CommentUtils.TYPE_BOOKLIST)){//书单
					bookList = getResourceInfoFacade.queryByIdBookList(resid);
					//ruid = bookList.getuId();
					resMap.put("rid", bookList.getId());//书单id
					resMap.put("rcomment", bookList.getReason());//书单回复
					resMap.put("restype", type);//书单类型
					resMap.put("rpic", bookList.getCover());//书单封面
					resMap.put("rrname",bookList.getBookListName());//书单内容
					resMap.put("ruserid", bookList.getuId());//书单作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, bookList.getuId());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书单作者id
					resMap.put("rauthor", ruserInfo.getName());//书单作者
					resMap.put("rusername", ruserInfo.getName());//书单作者
					resMap.put("rauthor", "");//书单作者
				}else if(type.equals(CommentUtils.TYPE_MOVIELIST)){//影单
					movieList = myMovieFacade.findMovieListById(resid);
					resMap.put("rid", movieList.getId());//影单id
					resMap.put("rcomment", movieList.getReason());//影单回复
					resMap.put("restype", type);//影单类型
					resMap.put("rpic", movieList.getCover());//影单封面
					resMap.put("rrname",movieList.getFilmListName());//影单名字
					resMap.put("ruserid", movieList.getUid());//影单作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, movieList.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//影单作者id
					resMap.put("rusername", ruserInfo.getName());//影单作者
					resMap.put("rauthor", "");//影单作者
				}else if(type.equals(CommentUtils.TYPE_DIARY)){//日志
					diary = diaryFacade.queryByIdDiary(resid);
					resMap.put("rid", diary.getId());//日志id
					resMap.put("rcomment", "");//日志内容
					resMap.put("restype", type);//日志类型
					resMap.put("rpic", "");//日志封面
					resMap.put("rrname", CheckParams.objectToStr(diary.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", diary.getUid());//日志作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, diary.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//日志作者头像
					resMap.put("rusername", ruserInfo.getName());//日志作者
					resMap.put("rauthor", "");//日志作者
				}else if(type.equals(CommentUtils.TYPE_ARTICLE)){//长文章
					post = postFacade.queryByIdName(resid);
					resMap.put("rid", post.getId());//长文章id
					resMap.put("rcomment", post.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(post.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章名字
					resMap.put("rauthor", "");//长文章作者
					resMap.put("ruserid", post.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, post.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章头像
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_NEWARTICLE)){//长文章
					article = articleFacade.queryArticleById(resid);
					resMap.put("rid", article.getId());//长文章id
					resMap.put("rcomment", article.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(article.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章名字
					resMap.put("rauthor", "");//长文章作者
					resMap.put("ruserid", article.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, article.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章头像
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//书评
					bkComment = bkCommentFacade.findCommentIsExistById(resid);
					if(null==bkComment){
						bkComment = new BkComment();
					}
					resMap.put("rid", bkComment.getId());//书评id
					resMap.put("rcomment", CheckParams.objectToStr(bkComment.getComment()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//书评内容
					resMap.put("restype", type);//书评类型
					bookId = bkComment.getBookId();
					bkInfo = bkFacade.findBkInfo((int)bookId);
					resMap.put("rpic", bkInfo.getBookPic());//图书封面
					resMap.put("rrname", bkInfo.getName());//图书名字
					resMap.put("rauthor", CheckParams.objectToStr(bkInfo.getAuthorName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//图书作者
					resMap.put("ruserid", bkComment.getUserId());//书评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, bkComment.getUserId());//
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评头像
					resMap.put("rusername", ruserInfo.getName());//书评作者
				}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//影评
					mvComment = mvCommentFacade.findMvCommentIsExist(resid);
					resMap.put("rid", mvComment.getId());//影评id
					resMap.put("rcomment", CheckParams.objectToStr(mvComment.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//影评内容
					resMap.put("restype", type);//影评内容
					movieId = mvComment.getMovieId();
					mvInfo = mvFacade.queryById(movieId);
					resMap.put("rpic",mvInfo.getMoviePic());//电影封面
					resMap.put("rrname",mvInfo.getName());//电影名字
					resMap.put("rauthor",CheckParams.objectToStr(mvInfo.getDirector()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//电影导演
					resMap.put("ruserid", mvComment.getUserId());//影评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, mvComment.getUserId());//
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评头像
					resMap.put("rusername", ruserInfo.getName());//书评作者
				}
				if(null!=resMap&&!resMap.isEmpty()){
					Long ridLong =(Long) resMap.get("rid");
					if(ridLong.longValue()!=0l){
						praiseMap.put("res", resMap);
						praiseMap.put("reply", replyMap);
						praiseMap.put("user", userMap);
						map.put("praiselist", praiseMap);
						list.add(map);
					}
				}
				/*System.out.println("传过来的type类型为====="+type);
				System.out.println("点赞数的列表为======="+list.size());
				System.out.println("点赞的一条resMap为"+resMap);*/
			//}
				/*else{//当评论的id不为0时，为回复资源
				recommentMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(commentId);
				recommentMap.put("recomid", actCmt.getId());
				recommentMap.put("recomment", actCmt.getCommentContext());
				resMap.put("", value);
				movieId = mvComment.getMovieId();
				mvInfo = mvFacade.queryById(movieId);
			}*/
			//map.put("res", resMap);
		}
		//allMap.put("data", list);
		//allMap.put("", value);
		try {
			finalreq = getObjectMapper().writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return finalreq;
	}
	
	/**
	 * 
	 * <p>Title: viewUserUsefulMSG</p> 
	 * <p>Description: 查看用户有用的信息列表</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午5:29:27
	 * @param reqs
	 * @return
	 */
	public String viewUserUsefulMSG(String reqs){
		String finalreq = "";
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Long userId = 0l;//用户id
		Long rid = null;
		//去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			userId =  Long.valueOf((String) dataq.get("userId"));
			try{
				rid = Long.valueOf(dataq.get("rid").toString());
			}catch (Exception e) {
				rid = 0l;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Map<String, Object> allMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;//new HashMap<String, Object>();
		Map<String, Object> userMap = null;//new HashMap<String, Object>();
		Map<String, Object> resMap = null;//new HashMap<String, Object>();
		Map<String, Object> usefulMap = null;//new HashMap<String, Object>();
		Map<String, Object> replyMap = null;
		//有用
		long usefulId = 0l;
		long resid = 0l;
		long usefulUserId = 0l;
		String type = "";
		long bookId = 0l;
		long movieId = 0l;
		long ruid = 0l;
		long btime = 0l;
		String publishTime = "";
		ActUseful actUseful = new ActUseful();
		UserAllInfo userInfo = new UserAllInfo();
		if(0==rid){
			rid = null;
		}
		//用户的有用数提醒数清零
		userJedisManager.saveOneUserInfo(userId, JedisConstant.USER_HASH_USEFUL_NOTICE, "0");
		
		List<ActUseful> usefulList = actFacade.findUsefulListByResUid(userId, rid);
				//findPraiseListByResUid(userId, rid);
		//System.out.println("查询出来的点赞条数为========"+praiseList.size());
		Iterator<ActUseful> usefulIt = usefulList.iterator();
		
		BookList bookList = new BookList();
		MovieList movieList = new MovieList();
		Diary diary = new Diary();
		Post post = new Post();
		Article article = new Article();
		BkComment bkComment = new BkComment();
		MvComment mvComment = new MvComment();
		BkInfo bkInfo  = new BkInfo();
		MvInfo mvInfo = new MvInfo();
		ActComment actCmt = new ActComment();
		while(usefulIt.hasNext()){
			actUseful = usefulIt.next();
			usefulId = actUseful.getId();
			usefulUserId = actUseful.getUserId();
			resid = actUseful.getResourceId();
			type = actUseful.getResType();
			btime = actUseful.getLatestRevisionDate();
			Date date = new Date(btime);
			publishTime = sf.format(date);
			//获取用户信息
			userInfo = ucenterFacade.findUserInfo(null,usefulUserId);
			map = new HashMap<String, Object>();
			userMap = new HashMap<String, Object>();
			resMap = new HashMap<String, Object>();
			usefulMap = new HashMap<String, Object>();
			replyMap = new HashMap<String, Object>();
			userMap.put("userId", userInfo.getUserId());
			userMap.put("name", userInfo.getName());
			userMap.put("faceAddress", userInfo.getFaceAddress());
			userMap.put("sex", userInfo.getSex());
			userMap.put("type", userInfo.getLevel());
			
			usefulMap.put("id", usefulId);
			//praiseMap.put("comment", comment);
			usefulMap.put("publishTime", publishTime);
			usefulMap.put("isRecomment", "0");//0为普通资源
			
			if(type.equals(CommentUtils.TYPE_COMMENT)){//评论
				usefulMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(resid);//查询在这条评论的回复
				replyMap.put("replyId", actCmt.getId());
				replyMap.put("replyComment", CheckParams.objectToStr(actCmt.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				type = actCmt.getType();
				resid = actCmt.getResourceId();
			}
				if(type.equals(CommentUtils.TYPE_BOOKLIST)){//书单
					bookList = getResourceInfoFacade.queryByIdBookList(resid);
					//ruid = bookList.getuId();
					resMap.put("rid", bookList.getId());//书单id
					resMap.put("rcomment", bookList.getReason());//书单回复
					resMap.put("restype", type);//书单类型
					resMap.put("rpic", bookList.getCover());//书单封面
					resMap.put("rrname",bookList.getBookListName());//书单内容
					resMap.put("ruserid", bookList.getuId());//书单作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, bookList.getuId());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书单作者id
					resMap.put("rauthor", ruserInfo.getName());//书单作者
					resMap.put("rusername", ruserInfo.getName());//书单作者
					resMap.put("rauthor", "");//书单作者
				}else if(type.equals(CommentUtils.TYPE_MOVIELIST)){//影单
					movieList = myMovieFacade.findMovieListById(resid);
					resMap.put("rid", movieList.getId());//影单id
					resMap.put("rcomment", movieList.getReason());//影单回复
					resMap.put("restype", type);//影单类型
					resMap.put("rpic", movieList.getCover());//影单封面
					resMap.put("rrname",movieList.getFilmListName());//影单名字
					resMap.put("ruserid", movieList.getUid());//影单作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, movieList.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//影单作者id
					resMap.put("rusername", ruserInfo.getName());//影单作者
					resMap.put("rauthor", "");//影单作者
				}else if(type.equals(CommentUtils.TYPE_DIARY)){//日志
					diary = diaryFacade.queryByIdDiary(resid);
					resMap.put("rid", diary.getId());//日志id
					resMap.put("rcomment", "");//日志内容
					resMap.put("restype", type);//日志类型
					resMap.put("rpic", "");//日志封面
					resMap.put("rrname", CheckParams.objectToStr(diary.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", diary.getUid());//日志作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, diary.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//日志作者头像
					resMap.put("rusername", ruserInfo.getName());//日志作者
					resMap.put("rauthor", "");//日志作者
				}else if(type.equals(CommentUtils.TYPE_ARTICLE)){//长文章
					post = postFacade.queryByIdName(resid);
					resMap.put("rid", post.getId());//长文章id
					resMap.put("rcomment", post.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(post.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章名字
					resMap.put("rauthor", "");//长文章作者
					resMap.put("ruserid", post.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, post.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章头像
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_NEWARTICLE)){//长文章
					article = articleFacade.queryArticleById(resid);
					resMap.put("rid", article.getId());//长文章id
					resMap.put("rcomment", article.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(article.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章名字
					resMap.put("rauthor", "");//长文章作者
					resMap.put("ruserid", article.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, article.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章头像
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//书评
					bkComment = bkCommentFacade.findCommentIsExistById(resid);
					if(null==bkComment){
						bkComment = new BkComment();
					}
					resMap.put("rid", bkComment.getId());//书评id
					resMap.put("rcomment", CheckParams.objectToStr(bkComment.getComment()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//书评内容
					resMap.put("restype", type);//书评类型
					bookId = bkComment.getBookId();
					bkInfo = bkFacade.findBkInfo((int)bookId);
					resMap.put("rpic", bkInfo.getBookPic());//图书封面
					resMap.put("rrname", bkInfo.getName());//图书名字
					resMap.put("rauthor", CheckParams.objectToStr(bkInfo.getAuthorName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//图书作者
					resMap.put("ruserid", bkComment.getUserId());//书评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, bkComment.getUserId());//
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评头像
					resMap.put("rusername", ruserInfo.getName());//书评作者
				}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//影评
					mvComment = mvCommentFacade.findMvCommentIsExist(resid);
					resMap.put("rid", mvComment.getId());//影评id
					resMap.put("rcomment", CheckParams.objectToStr(mvComment.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//影评内容
					resMap.put("restype", type);//影评内容
					movieId = mvComment.getMovieId();
					mvInfo = mvFacade.queryById(movieId);
					resMap.put("rpic",mvInfo.getMoviePic());//电影封面
					resMap.put("rrname",mvInfo.getName());//电影名字
					resMap.put("rauthor",CheckParams.objectToStr(mvInfo.getDirector()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//电影导演
					resMap.put("ruserid", mvComment.getUserId());//影评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, mvComment.getUserId());//
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评头像
					resMap.put("rusername", ruserInfo.getName());//书评作者
				}
				if(null!=resMap&&!resMap.isEmpty()){
					Long ridLong =(Long) resMap.get("rid");
					if(ridLong.longValue()!=0l){
						usefulMap.put("res", resMap);
						usefulMap.put("reply", replyMap);
						usefulMap.put("user", userMap);
						map.put("usefullist", usefulMap);
						list.add(map);
					}
				}
				/*System.out.println("传过来的type类型为====="+type);
				System.out.println("点赞数的列表为======="+list.size());
				System.out.println("点赞的一条resMap为"+resMap);*/
			//}
				/*else{//当评论的id不为0时，为回复资源
				recommentMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(commentId);
				recommentMap.put("recomid", actCmt.getId());
				recommentMap.put("recomment", actCmt.getCommentContext());
				resMap.put("", value);
				movieId = mvComment.getMovieId();
				mvInfo = mvFacade.queryById(movieId);
			}*/
			//map.put("res", resMap);
		}
		//allMap.put("data", list);
		//allMap.put("", value);
		try {
			finalreq = getObjectMapper().writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return finalreq;
	}
	
	/**
	 * 查看用户被at的列表
	 * @Title: viewAtUserMSG 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-25 下午12:15:22
	 * @param @param reqs
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String viewAtUserMSG(String reqs){
		String finalreq = "";
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Long atuserId = 0l;//用户id
		Long rid = 0l;
		//去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			atuserId =  Long.valueOf(dataq.get("userId")+"");
			try{
				rid = Long.valueOf(dataq.get("rid")+"");
			}catch (Exception e) {
				rid = 0l;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//清除用户被at的数量
		userJedisManager.saveOneUserInfo(atuserId, JedisConstant.USER_HASH_AT_NOTICE, "0");
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Map<String, Object> allMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;//new HashMap<String, Object>();
		Map<String, Object> userMap = null;//new HashMap<String, Object>();
		Map<String, Object> resMap = null;//new HashMap<String, Object>();
		Map<String, Object> atMap = null;//new HashMap<String, Object>();
		Map<String, Object> replyMap = null;
		Map<String, Object> rreplyMap = null;
		//评论
		long resid = 0l;
		long userId = 0l;
		String type = "";
		long bookId = 0l;
		long movieId = 0l;
		long ruid = 0l;
		long btime = 0l;
		long id = 0l;
		String creatTime = "";
		ActAt actAt = new ActAt();
		UserAllInfo userInfo = new UserAllInfo();
		if(0==rid){
			rid = null;
		}
		List<ActAt> atList = actFacade.findAtUser(atuserId, rid);
		Iterator<ActAt> atIt = atList.iterator();
		
		BookList bookList = new BookList();
		MovieList movieList = new MovieList();
		Diary diary = new Diary();
		Post post = new Post();
		Article article = new Article();
		BkComment bkComment = new BkComment();
		MvComment mvComment = new MvComment();
		BkInfo bkInfo  = new BkInfo();
		MvInfo mvInfo = new MvInfo();
		ActComment actCmt = new ActComment();
		//ActComment replyActCmt = new ActComment();
		while(atIt.hasNext()){
			actAt = atIt.next();
			id = actAt.getId();
			userId = actAt.getUserid();
			resid = actAt.getResid();
			type = actAt.getRestype();
			btime = actAt.getCreateDate();
			Date date = new Date(btime);
			creatTime = sf.format(date);
			//获取用户信息
			userInfo = ucenterFacade.findUserInfo(null,userId);
			map = new HashMap<String, Object>();
			userMap = new HashMap<String, Object>();
			resMap = new HashMap<String, Object>();
			atMap = new HashMap<String, Object>();
			replyMap = new HashMap<String, Object>();
			rreplyMap = new HashMap<String, Object>();
			userMap.put("userId", userInfo.getUserId());
			userMap.put("name", userInfo.getName());
			userMap.put("faceAddress", userInfo.getFaceAddress());
			userMap.put("sex", userInfo.getSex());
			userMap.put("type", userInfo.getLevel());
			if(!CommentUtils.TYPE_COMMENT.equals(actAt.getType())){//当不是评论类型时候，直接获取资源
				atMap.put("isRecomment", "0");//0为直接资源
			}else{
				atMap.put("isRecomment", "1");//1为回复资源
				
				actCmt = actFacade.findCmtById(actAt.getResourceid());//查询在这条评论的回复
				replyMap.put("replyId", actCmt.getId());
				replyMap.put("replyComment", CheckParams.objectToStr(actCmt.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				
				/*long replyCommentId = actCmt.getCommentId();
				if(replyCommentId!=0){//当为回复的回复的时候
					atMap.put("isRecomment", "2");//2为回复的回复资源
					long replyUserId = actCmt.getCommentUserId();
					replyActCmt = actFacade.findCmtById(replyCommentId);//查询回复的回复
					UserAllInfo replyUserInfo = new UserAllInfo();
					replyUserInfo = ucenterFacade.findUserInfo(null,replyUserId);
					rreplyMap.put("userId", replyUserInfo.getUserId());
					rreplyMap.put("name", replyUserInfo.getName());
					rreplyMap.put("faceAddress", replyUserInfo.getFaceAddress());
					rreplyMap.put("sex", replyUserInfo.getSex());
					rreplyMap.put("level", replyUserInfo.getLevel());
					//加载回复的回复信息
					rreplyMap.put("rreplyId", replyActCmt.getId());
					rreplyMap.put("rreplyComment",  CheckParams.objectToStr(replyActCmt.getCommentContext()).replaceAll("<[^>]*>", ""));
					
				}*/
			}
			atMap.put("id", id);
			atMap.put("creatTime", creatTime);
				if(type.equals(CommentUtils.TYPE_BOOKLIST)){//书单
					bookList = getResourceInfoFacade.queryByIdBookList(resid);
					//ruid = bookList.getuId();
					resMap.put("rid", bookList.getId());//书单id
					resMap.put("rcomment", bookList.getReason());//书单回复
					resMap.put("restype", type);//书单类型
					resMap.put("rpic", bookList.getCover());//书单封面
					resMap.put("rrname",bookList.getBookListName());//书单内容
					resMap.put("ruserid", bookList.getuId());//书单作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, bookList.getuId());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书单作者id
					resMap.put("rauthor", "");//书单作者
					resMap.put("rusername", ruserInfo.getName());//书单作者
				}else if(type.equals(CommentUtils.TYPE_MOVIELIST)){//影单
					movieList = myMovieFacade.findMovieListById(resid);
					resMap.put("rid", movieList.getId());//影单id
					resMap.put("rcomment", movieList.getReason());//影单回复
					resMap.put("restype", type);//影单类型
					resMap.put("rpic", movieList.getCover());//影单封面
					resMap.put("rrname",movieList.getFilmListName());//影单名字
					resMap.put("ruserid", movieList.getUid());//影单作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, movieList.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书单作者id
					resMap.put("rauthor", "");//书单作者
					resMap.put("rusername", ruserInfo.getName());//书单作者
				}else if(type.equals(CommentUtils.TYPE_DIARY)){//日志
					diary = diaryFacade.queryByIdDiary(resid);
					resMap.put("rid", diary.getId());//日志id
					resMap.put("rcomment", "");//日志内容
					resMap.put("restype", type);//日志类型
					resMap.put("rpic", "");//日志封面
					resMap.put("rrname", CheckParams.objectToStr(diary.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", diary.getUid());//日志作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, diary.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//日志作者id
					resMap.put("rauthor", "");//日志作者
					resMap.put("rusername", ruserInfo.getName());//日志作者
				}else if(type.equals(CommentUtils.TYPE_ARTICLE)){//长文章
					post = postFacade.queryByIdName(resid);
					resMap.put("rid", post.getId());//长文章id
					resMap.put("rcomment", post.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(post.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", post.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, post.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章作者id
					resMap.put("rauthor", "");//长文章作者
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_NEWARTICLE)){//新的长文章
					article = articleFacade.queryArticleById(resid);
					resMap.put("rid", article.getId());//长文章id
					resMap.put("rcomment", article.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(article.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", article.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, article.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章作者id
					resMap.put("rauthor", "");//长文章作者
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//书评
					bkComment = bkCommentFacade.findCommentIsExistById(resid);
					if(null==bkComment){
						bkComment = new BkComment();
					}
					String commentext = bkComment.getComment();
					if(null==commentext){
						commentext = "";
					}
					resMap.put("rid", bkComment.getId());//书评id
					resMap.put("rcomment", commentext.replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//书评内容
					resMap.put("restype", type);//书评类型
					bookId = bkComment.getBookId();
					bkInfo = bkFacade.findBkInfo((int)bookId);
					resMap.put("rpic", bkInfo.getBookPic());//图书封面
					resMap.put("rrname", bkInfo.getName());//图书名字
					resMap.put("rauthor", bkInfo.getAuthorName());//图书作者
					resMap.put("ruserid", bkComment.getUserId());//书评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, bkComment.getUserId());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评作者封面
					resMap.put("rusername", ruserInfo.getName());//书评作者
					//resMap.put("rauthor", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//影评
					mvComment = mvCommentFacade.findMvCommentIsExist(resid);
					resMap.put("rid", mvComment.getId());//影评id
					resMap.put("rcomment", CheckParams.objectToStr(mvComment.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//影评内容
					resMap.put("restype", type);//影评内容
					movieId = mvComment.getMovieId();
					mvInfo = mvFacade.queryById(movieId);
					resMap.put("rpic",mvInfo.getMoviePic());//电影封面
					resMap.put("rrname",mvInfo.getName());//电影名字
					resMap.put("rauthor",CheckParams.objectToStr(mvInfo.getDirector()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//电影导演
					resMap.put("ruserid", mvComment.getUserId());//影评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, mvComment.getUserId());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//影评作者封面
					resMap.put("rusername", ruserInfo.getName());//影评作者
				}
				if(null!=resMap&&!resMap.isEmpty()){
					Long ridLong =(Long) resMap.get("rid");
					if(ridLong.longValue()!=0l){
						atMap.put("res", resMap);
						atMap.put("user", userMap);
						atMap.put("reply", replyMap);
						atMap.put("rreplyMap", rreplyMap);
						//map.put("res", resMap);
						map.put("atinfo", atMap);
						list.add(map);
					}
				}
			//}
				/*else{//当评论的id不为0时，为回复资源
				recommentMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(commentId);
				recommentMap.put("recomid", actCmt.getId());
				recommentMap.put("recomment", actCmt.getCommentContext());
				resMap.put("", value);
				movieId = mvComment.getMovieId();
				mvInfo = mvFacade.queryById(movieId);
			}*/
		}
		//allMap.put("data", list);
		//allMap.put("", value);
		try {
			finalreq = getObjectMapper().writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return finalreq;
	}
	
	private StoryUserFacade storyUserFacade;
	
	
	
	public void setStoryUserFacade(StoryUserFacade storyUserFacade) {
		this.storyUserFacade = storyUserFacade;
	}

	private IStoryCommentFacade storyCommentFacade;
	
	public void setStoryCommentFacade(IStoryCommentFacade storyCommentFacade) {
		this.storyCommentFacade = storyCommentFacade;
	}

	private StoryFacade storyFacade;
	
	
	public void setStoryFacade(StoryFacade storyFacade) {
		this.storyFacade = storyFacade;
	}

	/**小说用户中心评论*/
	public String viewStoryUserCommentMSG(String reqs) {
		String finalreq = "";
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Long userId = 0l;//用户id
		Long rid = 0l;
		//去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			userId =  Long.valueOf((String) dataq.get("userId"));
			try{
				rid = Long.valueOf(dataq.get("rid").toString());
			}catch (Exception e) {
				rid = 0l;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//清除用户评论的缓存数量
		userJedisManager.saveOneUserInfo(userId, JedisConstant.USER_HASH_COMMENT_NOTICE, "0");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Map<String, Object> allMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;//new HashMap<String, Object>();
		//用户信息
		Map<String, Object> userMap = null;//new HashMap<String, Object>();
		//资源信息 小说
		Map<String, Object> resMap = null;//new HashMap<String, Object>();
		//资源信息 小说评
		Map<String, Object> recommentMap = null;//new HashMap<String, Object>();
		Map<String, Object> replyMap = null;
		Map<String, Object> rreplyMap = null;
		//评论
		long commentId = 0l;
		long resid = 0l;
		long commentUserId = 0l;
		String comment = "";
		String type = "";
		long storyId = 0l;
		long ruid = 0l;
		long btime = 0l;
		long id = 0l;
		String publishTime = "";
		ActComment actComment = new ActComment();
		StoryUser userInfo = new StoryUser();
		
		if(0==rid){
			rid = null;
		}
		//查找用户的评论中心
		List<ActComment> commentList = actFacade.findUserCommentCenter(userId, rid);
		Iterator<ActComment> commentIt = commentList.iterator();
		
		StoryComment storyComment = new StoryComment();
		Story story = new Story();
		ActComment actCmt = new ActComment();
		ActComment replyActCmt = new ActComment();
		while(commentIt.hasNext()){
			actComment = commentIt.next();
			id = actComment.getId();
			//获取当前回复的用户的id
			commentUserId = actComment.getUserId();
			comment = CheckParams.objectToStr(actComment.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", "");
			commentId = actComment.getCommentId();
			resid = actComment.getResourceId();
			type = actComment.getType();
			btime = actComment.getLatestRevisionDate();
			Date date = new Date(btime);
			publishTime = sf.format(date);
			//获取小说评论的回复的用户信息
			userInfo = storyUserFacade.findStoryUserByUserid(commentUserId);
			map = new HashMap<String, Object>();
			userMap = new HashMap<String, Object>();
			resMap = new HashMap<String, Object>();
			//1层回复 直接资源
			recommentMap = new HashMap<String, Object>();
			//2层回复
			replyMap = new HashMap<String, Object>();
			//3层回复
			rreplyMap = new HashMap<String, Object>();
			userMap.put("userId", userInfo.getUserId());
			userMap.put("name", userInfo.getName());
			userMap.put("faceAddress", userInfo.getFaceAddress());
			userMap.put("sex", userInfo.getSex());
			if(commentId==0){//当评论的id为0时，直接获取资源
				recommentMap.put("isRecomment", "0");//0为直接资源
			}else{
				recommentMap.put("isRecomment", "1");//1为回复资源
				//查询当前回复回复的是哪一条回复
				actCmt = actFacade.findCmtById(commentId);
				StoryUser replayUser = storyUserFacade.findStoryUserByUserid(actCmt.getUserId());
				replyMap.put("userId", replayUser.getUserId());
				replyMap.put("name", replayUser.getName());
				replyMap.put("faceAddress", replayUser.getFaceAddress());
				replyMap.put("sex", replayUser.getSex());
				//查询被回复的回复的用户id
				replyMap.put("replyId", actCmt.getId());
				//查询被回复的回复的评论内容
				replyMap.put("replyComment", CheckParams.objectToStr(actCmt.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				//查询被回复的回复的回复的是哪一条评论
				long replyCommentId = actCmt.getCommentId();
				if(replyCommentId!=0){//当为回复的回复的时候
					recommentMap.put("isRecomment", "2");//2为回复的回复资源
					//获取3层回复的发布人
					long replyUserId = actCmt.getCommentUserId();
					//查询第三层回复
					replyActCmt = actFacade.findCmtById(replyCommentId);
					StoryUser replyUserInfo = new StoryUser();
					replyUserInfo = storyUserFacade.findStoryUserByUserid(replyUserId);
					if(null==replyUserInfo){
						continue;
					}
					rreplyMap.put("userId", replyUserInfo.getUserId());
					rreplyMap.put("name", replyUserInfo.getName());
					rreplyMap.put("faceAddress", replyUserInfo.getFaceAddress());
					rreplyMap.put("sex", replyUserInfo.getSex());
					//加载回复的回复信息
					rreplyMap.put("rreplyId", replyActCmt.getId());
					rreplyMap.put("rreplyComment",  CheckParams.objectToStr(replyActCmt.getCommentContext()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				}
			}
			recommentMap.put("id", id);
			recommentMap.put("comment", comment);
			recommentMap.put("publishTime", publishTime);
			storyComment = storyCommentFacade.findStoryCommentById(resid);
			if(storyComment==null){
				//小说评论已找不到
				continue;
			}
			resMap.put("rid", storyComment.getId());//小说评id
			resMap.put("rcomment", CheckParams.objectToStr(storyComment.getCommentBody()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//小说评内容
			resMap.put("restype", type);//小说评评内容
			storyId = storyComment.getStoryId();
			story = storyFacade.findStoryById(storyId);
			if(story == null){
				continue;
			}
			resMap.put("rpic",story.getCover());//小说封面
			resMap.put("rrname",story.getName());//小说名字
			resMap.put("rauthor",CheckParams.objectToStr(story.getAuthor()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//小说作者
			resMap.put("ruserid", storyComment.getUserId());//小说评作者id
			StoryUser ruserInfo =storyUserFacade.findStoryUserByUserid(storyComment.getUserId());
			if(null==ruserInfo){
				continue;
			}
			resMap.put("ruserface", CheckParams.objectToStr(ruserInfo.getFaceAddress()));//小说评作者封面
			resMap.put("rusername", ruserInfo.getName());//小说评作者
			if(null!=resMap&&!resMap.isEmpty()){
				Long ridLong =(Long) resMap.get("rid");
				if(ridLong.longValue()!=0l){
					recommentMap.put("res", resMap);
					recommentMap.put("user", userMap);
					recommentMap.put("reply", replyMap);
					recommentMap.put("rreplyMap", rreplyMap);
					//map.put("res", resMap);
					map.put("recomment", recommentMap);
					list.add(map);
				}
			}
		}
		try {
			finalreq = getObjectMapper().writeValueAsString(list);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return finalreq;
	}
	
	/**
	 * 
	 * <p>Title: viewUserUsefulMSG</p> 
	 * <p>Description: 查看小说用户有用的信息列表</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午5:29:27
	 * @param reqs
	 * @return
	 */
	public String viewStoryUserUsefulMSG(String reqs){
		String finalreq = "";
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Long userId = 0l;//用户id
		Long rid = null;
		//去掉空格
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			userId =  Long.valueOf((String) dataq.get("userId"));
			try{
				rid = Long.valueOf(dataq.get("rid").toString());
			}catch (Exception e) {
				rid = 0l;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Map<String, Object> allMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;//new HashMap<String, Object>();
		Map<String, Object> userMap = null;//new HashMap<String, Object>();
		Map<String, Object> resMap = null;//new HashMap<String, Object>();
		Map<String, Object> usefulMap = null;//new HashMap<String, Object>();
		Map<String, Object> replyMap = null;
		//有用
		long usefulId = 0l;
		long resid = 0l;
		long usefulUserId = 0l;
		String type = "";
		long bookId = 0l;
		long movieId = 0l;
		long ruid = 0l;
		long btime = 0l;
		String publishTime = "";
		ActUseful actUseful = new ActUseful();
		StoryUser userInfo = new StoryUser();
		if(0==rid){
			rid = null;
		}
		//用户的有用数提醒数清零
		userJedisManager.saveOneUserInfo(userId, JedisConstant.USER_HASH_USEFUL_NOTICE, "0");
		
		List<ActUseful> usefulList = actFacade.findUsefulListByResUid(userId, rid);
				//findPraiseListByResUid(userId, rid);
		//System.out.println("查询出来的点赞条数为========"+praiseList.size());
		Iterator<ActUseful> usefulIt = usefulList.iterator();
		
		/*BookList bookList = new BookList();
		MovieList movieList = new MovieList();
		Diary diary = new Diary();
		Post post = new Post();
		Article article = new Article();
		BkComment bkComment = new BkComment();
		MvComment mvComment = new MvComment();
		BkInfo bkInfo  = new BkInfo();
		MvInfo mvInfo = new MvInfo();*/
		StoryComment storyComment = new StoryComment();
		while(usefulIt.hasNext()){
			actUseful = usefulIt.next();
			usefulId = actUseful.getId();
			usefulUserId = actUseful.getUserId();
			resid = actUseful.getResourceId();
			type = actUseful.getResType();
			btime = actUseful.getLatestRevisionDate();
			Date date = new Date(btime);
			publishTime = sf.format(date);
			//获取用户信息
//			userInfo = ucenterFacade.findUserInfo(null,usefulUserId);
			userInfo = storyUserFacade.findStoryUserByUserid(usefulUserId);
			map = new HashMap<String, Object>();
			userMap = new HashMap<String, Object>();
			resMap = new HashMap<String, Object>();
			usefulMap = new HashMap<String, Object>();
			replyMap = new HashMap<String, Object>();
			userMap.put("userId", userInfo.getUserId());
			userMap.put("name", userInfo.getName());
			userMap.put("faceAddress", userInfo.getFaceAddress());
			userMap.put("sex", userInfo.getSex());
//			userMap.put("type", userInfo.getLevel());
			
			usefulMap.put("id", usefulId);
			//praiseMap.put("comment", comment);
			usefulMap.put("publishTime", publishTime);
			usefulMap.put("isRecomment", "0");//0为普通资源
			
			if(type.equals(CommentUtils.TYPE_STORY_COMMENT)){//评论
				usefulMap.put("isRecomment", "1");//1为回复资源
				resMap.put("rid", resid);
				//查询在这条评论的回复
				storyComment = storyCommentFacade.findStoryCommentById(resid);
				if(storyComment==null){
					continue;
				}
				replyMap.put("replyId", storyComment.getId());
				replyMap.put("replyComment", CheckParams.objectToStr(storyComment.getCommentBody()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				resMap.put("restype", type);//影评内容
				long storyId = storyComment.getStoryId();
				Story storyInfo = storyFacade.findStoryById(storyId);
				resMap.put("rpic",storyInfo.getCover());//封面
				resMap.put("rrname",storyInfo.getName());//名字
				resMap.put("rauthor",CheckParams.objectToStr(storyInfo.getAuthor()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//电影导演
				resMap.put("ruserid", storyComment.getUserId());//影评作者id
				StoryUser ruserInfo =storyUserFacade.findStoryUserByUserid(storyComment.getUserId());//
				if(null==ruserInfo){
					continue;
				}
				resMap.put("ruserface", ruserInfo.getFaceAddress());//书评头像
				resMap.put("rusername", ruserInfo.getName());//书评作者
			}
				/*if(type.equals(CommentUtils.TYPE_BOOKLIST)){//书单
					bookList = getResourceInfoFacade.queryByIdBookList(resid);
					//ruid = bookList.getuId();
					resMap.put("rid", bookList.getId());//书单id
					resMap.put("rcomment", bookList.getReason());//书单回复
					resMap.put("restype", type);//书单类型
					resMap.put("rpic", bookList.getCover());//书单封面
					resMap.put("rrname",bookList.getBookListName());//书单内容
					resMap.put("ruserid", bookList.getuId());//书单作者id
					StoryUser ruserInfo =storyUserFacade.findStoryUserByUserid(bookList.getuId());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书单作者id
					resMap.put("rauthor", ruserInfo.getName());//书单作者
					resMap.put("rusername", ruserInfo.getName());//书单作者
					resMap.put("rauthor", "");//书单作者
				}else if(type.equals(CommentUtils.TYPE_MOVIELIST)){//影单
					movieList = myMovieFacade.findMovieListById(resid);
					resMap.put("rid", movieList.getId());//影单id
					resMap.put("rcomment", movieList.getReason());//影单回复
					resMap.put("restype", type);//影单类型
					resMap.put("rpic", movieList.getCover());//影单封面
					resMap.put("rrname",movieList.getFilmListName());//影单名字
					resMap.put("ruserid", movieList.getUid());//影单作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, movieList.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//影单作者id
					resMap.put("rusername", ruserInfo.getName());//影单作者
					resMap.put("rauthor", "");//影单作者
				}else if(type.equals(CommentUtils.TYPE_DIARY)){//日志
					diary = diaryFacade.queryByIdDiary(resid);
					resMap.put("rid", diary.getId());//日志id
					resMap.put("rcomment", "");//日志内容
					resMap.put("restype", type);//日志类型
					resMap.put("rpic", "");//日志封面
					resMap.put("rrname", CheckParams.objectToStr(diary.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//日志名字
					resMap.put("ruserid", diary.getUid());//日志作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, diary.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//日志作者头像
					resMap.put("rusername", ruserInfo.getName());//日志作者
					resMap.put("rauthor", "");//日志作者
				}else if(type.equals(CommentUtils.TYPE_ARTICLE)){//长文章
					post = postFacade.queryByIdName(resid);
					resMap.put("rid", post.getId());//长文章id
					resMap.put("rcomment", post.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(post.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章名字
					resMap.put("rauthor", "");//长文章作者
					resMap.put("ruserid", post.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, post.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章头像
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_NEWARTICLE)){//长文章
					article = articleFacade.queryArticleById(resid);
					resMap.put("rid", article.getId());//长文章id
					resMap.put("rcomment", article.getSummary().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章内容
					resMap.put("restype", type);//长文章类型
					resMap.put("rpic", "");//长文章封面
					resMap.put("rrname", CheckParams.objectToStr(article.getName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//长文章名字
					resMap.put("rauthor", "");//长文章作者
					resMap.put("ruserid", article.getUid());//长文章作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, article.getUid());
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//长文章头像
					resMap.put("rusername", ruserInfo.getName());//长文章作者
				}else if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//书评
					bkComment = bkCommentFacade.findCommentIsExistById(resid);
					if(null==bkComment){
						bkComment = new BkComment();
					}
					resMap.put("rid", bkComment.getId());//书评id
					resMap.put("rcomment", CheckParams.objectToStr(bkComment.getComment()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//书评内容
					resMap.put("restype", type);//书评类型
					bookId = bkComment.getBookId();
					bkInfo = bkFacade.findBkInfo((int)bookId);
					resMap.put("rpic", bkInfo.getBookPic());//图书封面
					resMap.put("rrname", bkInfo.getName());//图书名字
					resMap.put("rauthor", CheckParams.objectToStr(bkInfo.getAuthorName()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//图书作者
					resMap.put("ruserid", bkComment.getUserId());//书评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, bkComment.getUserId());//
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评头像
					resMap.put("rusername", ruserInfo.getName());//书评作者
				}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//影评
					mvComment = mvCommentFacade.findMvCommentIsExist(resid);
					resMap.put("rid", mvComment.getId());//影评id
					resMap.put("rcomment", CheckParams.objectToStr(mvComment.getContent()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//影评内容
					resMap.put("restype", type);//影评内容
					movieId = mvComment.getMovieId();
					mvInfo = mvFacade.queryById(movieId);
					resMap.put("rpic",mvInfo.getMoviePic());//电影封面
					resMap.put("rrname",mvInfo.getName());//电影名字
					resMap.put("rauthor",CheckParams.objectToStr(mvInfo.getDirector()).replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));//电影导演
					resMap.put("ruserid", mvComment.getUserId());//影评作者id
					UserAllInfo ruserInfo =ucenterFacade.findUserInfo(null, mvComment.getUserId());//
					if(null==ruserInfo){
						continue;
					}
					resMap.put("ruserface", ruserInfo.getFaceAddress());//书评头像
					resMap.put("rusername", ruserInfo.getName());//书评作者
				}*/
				if(null!=resMap&&!resMap.isEmpty()){
					Long ridLong =(Long) resMap.get("rid");
					if(ridLong.longValue()!=0l){
						usefulMap.put("res", resMap);
						usefulMap.put("reply", replyMap);
						usefulMap.put("user", userMap);
						map.put("usefullist", usefulMap);
						list.add(map);
					}
				}
				/*System.out.println("传过来的type类型为====="+type);
				System.out.println("点赞数的列表为======="+list.size());
				System.out.println("点赞的一条resMap为"+resMap);*/
			//}
				/*else{//当评论的id不为0时，为回复资源
				recommentMap.put("isRecomment", "1");//1为回复资源
				actCmt = actFacade.findCmtById(commentId);
				recommentMap.put("recomid", actCmt.getId());
				recommentMap.put("recomment", actCmt.getCommentContext());
				resMap.put("", value);
				movieId = mvComment.getMovieId();
				mvInfo = mvFacade.queryById(movieId);
			}*/
			//map.put("res", resMap);
		}
		//allMap.put("data", list);
		//allMap.put("", value);
		try {
			finalreq = getObjectMapper().writeValueAsString(list);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return finalreq;
	}
}
