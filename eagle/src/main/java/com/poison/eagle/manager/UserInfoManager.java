package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.eagle.easemobmanager.EasemobUserManager;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.MovieListInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserAlbumInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.BookUtils;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.TalentUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.UserTag;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.ShenrenApplyFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class UserInfoManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(UserInfoManager.class);
	
	private int flagint;
	private UcenterFacade ucenterFacade;
	private BigFacade bigFacade;
	private ActFacade actFacade;
	private DiaryFacade diaryFacade;
	private PostFacade postFacade;
	private ArticleFacade articleFacade;
	private BkCommentFacade bkCommentFacade;
	private MvCommentFacade mvCommentFacade;
	private MyBkFacade myBkFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private PaycenterFacade paycenterFacade;
	private ShenrenApplyFacade shenrenApplyFacade;
	private MyMovieFacade myMovieFacade;
	private MvFacade mvFacade;
	private GetResourceInfoFacade getResourceInfoFacade;
	private BkFacade bkFacade;
	private NetBookFacade netBookFacade;
	private BookUtils bookUtils = BookUtils.getInstance();
	
	private BookListManager bookListManager;
	private MovieListManager movieListManager;
	private TagManager tagManager;
	private ResourceManager resourceManager;
	private ActManager actManager;
	private EasemobUserManager easemobUserManager;
	private SearchApiManager searchApiManager; 
	private UserJedisManager userJedisManager;
	private RankingManager rankingManager;
	private ResStatJedisManager resStatJedisManager;
	//	private long id;
//	private String uname;//昵称
	private FileUtils fileUtils = FileUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	private TalentUtils talentUtils = TalentUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();

	public void setUserJedisManager(UserJedisManager userJedisManager) {
			this.userJedisManager = userJedisManager;
		}
	
	/**
	 * 用户信息页面
	 * @return
	 */
	public String userInfo(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		int fansNum = 0;
		int plusNum = 0;
		//去掉空格
		reqs = reqs.trim();
		Long id = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		//用户书单
		long begin = System.currentTimeMillis();
//		List<BookListInfo> bookLists = bookListManager.getMyBookList(id);
		List<BookListInfo> bookListInfos = new ArrayList<BookListInfo>();
//		if(bookLists.size()>3){
//			for (int i = 0; i < 3; i++) {
//				int index = (int)(Math.random()*bookLists.size());
//				bookListInfos.add(bookLists.get(index));
//				bookLists.remove(index);
//			}
//		}else{
//			bookListInfos.addAll(bookLists);
//		}
		
//		System.out.println("我的书单耗时："+(end-begin));
		//用户影单
//		begin = System.currentTimeMillis();
//		List<MovieListInfo> movieLists = movieListManager.getMyMovieList(id);
		List<MovieListInfo> movieListInfos = new ArrayList<MovieListInfo>();
//		if(movieLists.size()>3){
//			for (int i = 0; i < 3; i++) {
//				int index = (int)(Math.random()*movieLists.size());
//				movieListInfos.add(movieLists.get(index));
//				movieLists.remove(index);
//			}
//		}else{
//			movieListInfos.addAll(movieLists);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("我的影单耗时："+(end-begin));
		
		//用户常用标签
		//long start1 = System.currentTimeMillis();
		List<String> userTagInfos = tagManager.getUserTagList(id);
		/*long end1 = System.currentTimeMillis();
		System.out.println("用户常用标签耗时："+(end1-start1)+"毫秒");*/
		
		//查询用户的基本信息
		//long start2 = System.currentTimeMillis();
		UserAllInfo userAllInfo =  ucenterFacade.findUserInfo(null, id);
		
		UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, TRUE);
		
		userUtils.putIsInterestToUserEntity(userEntity,uid, ucenterFacade,userJedisManager);
		
		//读取用户缓存信息
		Map<String, String> userInfoMap = userJedisManager.getUserInfo(id);
		//当用户的缓存信息为空时
		if(null==userInfoMap||userInfoMap.isEmpty()||null==userInfoMap.get(JedisConstant.USER_HASH_FACE)||"".equals(userInfoMap.get(JedisConstant.USER_HASH_FACE))){
			
			userJedisManager.saveOneUserInfo(id, JedisConstant.USER_HASH_ID, CheckParams.objectToStr(userAllInfo.getUserId()+""));
			userJedisManager.saveOneUserInfo(id, JedisConstant.USER_HASH_NAME, CheckParams.objectToStr(userAllInfo.getName()));
			userJedisManager.saveOneUserInfo(id, JedisConstant.USER_HASH_FACE, CheckParams.objectToStr(userAllInfo.getFaceAddress()));
			//System.out.println("用户的头像为"+CheckParams.objectToStr(userAllInfo.getFaceAddress()));
			userJedisManager.saveOneUserInfo(id, JedisConstant.USER_HASH_SIGN, CheckParams.objectToStr(userAllInfo.getSign()));
			userJedisManager.saveOneUserInfo(id, JedisConstant.USER_HASH_TYPE, CheckParams.objectToStr(userAllInfo.getLevel()+""));
			userJedisManager.saveOneUserInfo(id, JedisConstant.USER_HASH_SEX, CheckParams.objectToStr(userAllInfo.getSex()));
		}
		
		/*long end2 = System.currentTimeMillis();
		System.out.println("用户常用标签耗时："+(end2-start2)+"毫秒");*/
		//添加逼格
//		userEntity = userUtils.putBigToUserEntity(userEntity, ucenterFacade, bigFacade);
		
		//动态数量
		//long start3 = System.currentTimeMillis();
//		System.out.println("我的影单耗时："+(end-begin));
		//长文章数量
		int postCount=0;
		Map<String, Object> postMap = postFacade.findPostCount(id);
		int flagint = (Integer) postMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			postCount = (Integer) postMap.get("count");
		}
		//新的长文章数量
		Map<String, Object> articleMap = articleFacade.findArticleCount(id,CommentUtils.TYPE_NEWARTICLE);
		flagint = (Integer) articleMap.get("flag");
		int articleCount = 0;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			articleCount = (Integer) articleMap.get("count");
		}
		//日志数量
		Map<String, Object> diaryMap = diaryFacade.findDiaryCount(id);
		flagint = (Integer) diaryMap.get("flag");
		int diaryCount = 0;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			diaryCount = (Integer) diaryMap.get("count");
		}
		//书评数量
		Map<String, Object> bkMap = bkCommentFacade.findBkCommentCount(id);
		flagint = (Integer) bkMap.get("flag");
		int bkCount = 0;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			bkCount = (Integer) bkMap.get("count");
		}
		//影评数量
		Map<String, Object> mvMap = mvCommentFacade.findMvCommentCountByUid(id);
		flagint = (Integer) mvMap.get("flag");
		int mvCount = 0;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			mvCount = (Integer) mvMap.get("count");
		}
		//推送数量
		Map<String, Object> pMap = actFacade.findPublishCount(id);
		flagint = (Integer) pMap.get("flag");
		int pulishCount = 0;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			pulishCount = (Integer) pMap.get("count");
		}
		//推送数量
		Map<String, Object> tMap = actFacade.findTransmitCountByUid(id);
		flagint = (Integer) tMap.get("flag");
		int transmitCount = 0;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			transmitCount = (Integer) tMap.get("count");
		}
				
		int resourceCount = postCount + articleCount + diaryCount + bkCount + mvCount + pulishCount + transmitCount;
		 //getUserDynamicCount(id);
		/*long end3 = System.currentTimeMillis();
		System.out.println("用户动态数耗时："+(end3-start3)+"毫秒");*/
		//long end = System.currentTimeMillis();
		//end = System.currentTimeMillis();
		//System.out.println("我的动态数量耗时："+(end-begin));
		//粉丝数量
		//begin = System.currentTimeMillis();
		//long start4 = System.currentTimeMillis();

		fansNum = ucenterFacade.findUserFensCount(null, id);


		String isOperation = userAllInfo.getIsOperation();
		if("".equals(isOperation)){
			isOperation = "0";
		}
		int isOperationInt = Integer.valueOf(isOperation);
		if(isOperationInt>=3){
			fansNum = fansNum+1000*isOperationInt;
		}
		userEntity.setIsOperation("0");
		//isOperation = "0";
//		if(id==514){//侯总 粉丝为11W+
//			fansNum = fansNum+100000;
//		} else if(id==724){//刘晓庆 粉丝为7W+
//			fansNum = fansNum+60000;
//		} else if(id==4147){//只是寒暄 粉丝为6W+
//			fansNum = fansNum+60000;
//		} else if(id ==94107){//戴军 粉丝为5W+
//			fansNum = fansNum+50000;
//		} else if(){//静静
//
//		}
		/*long end4 = System.currentTimeMillis();
		System.out.println("用户粉丝数数耗时："+(end4-start4)+"毫秒");*/
		
		//long start5 = System.currentTimeMillis();
		plusNum = ucenterFacade.findUserAttentionCount(null, id);
		/*long end5 = System.currentTimeMillis();
		System.out.println("用户关注数数耗时："+(end5-start5)+"毫秒");*/
		//end = System.currentTimeMillis();
		//System.out.println("粉丝数量耗时："+(end-begin));
		//收藏数量
		//long start6 = System.currentTimeMillis();
		int collectCount = 0;
		Map<String, Object> userCollectCount = new HashMap<String, Object>();
		try {
			/*List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
			resourceInfos = actManager.getCollects(uid, resourceInfos);
			collectCount = resourceInfos.size();*/
			userCollectCount = actFacade.findUserCollectCount(uid);
			collectCount = (Integer) userCollectCount.get("count");
		} catch (Exception e) {
			collectCount = 0;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		/*long end6 = System.currentTimeMillis();
		System.out.println("用户收藏数耗时："+(end6-start6)+"毫秒");*/
		
		//打赏总金额
		//long start7 = System.currentTimeMillis();
		int total = 0;
		try {
			total = paycenterFacade.getAccAmt(id);
			flagint = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			total = 0;
		}
		float ftotal = 0f;
		if(total >0){
			ftotal = (float)total /100;
		}
		String totalAmount = ftotal+"";
		/*long end7 = System.currentTimeMillis();
		System.out.println("用户打赏数耗时："+(end7-start7)+"毫秒");*/
		
		if(fansNum >=0 && plusNum >=0){
			flagint = ResultUtils.SUCCESS;
		}
		datas = new HashMap<String, Object>();
		//long start8 = System.currentTimeMillis();
		if(flagint == ResultUtils.SUCCESS){
			//添加用户数据数量
			try {
				userStatisticsFacade.insertUserStatistics(userEntity.getId());
			} catch (Exception e) {
				LOG.error("添加用户产生数据数量出错:"+e.getMessage(), e.fillInStackTrace());
			}
			flag = CommentUtils.RES_FLAG_SUCCESS;
			/*String DATEFORMAT = "yyyy-MM-dd";
			SimpleDateFormat sf = new SimpleDateFormat(DATEFORMAT);
			Date date = new Date(userAllInfo.getBirthday());*/
			String formatDateStr = DateUtil.format(userAllInfo.getBirthday(), "yyyy-MM-dd");
			
			datas.put("articleNum", articleCount);
			datas.put("fansNum", fansNum);
			datas.put("plusNum", plusNum);
			datas.put("diaryNum", diaryCount);
			datas.put("mvCommentNum", mvCount);
			datas.put("bkCommentNum", bkCount);
			datas.put("resourceNum", resourceCount);
			datas.put("collectNum", collectCount);
			datas.put("affective", userAllInfo.getAffectiveStates());//个人感情状态
			datas.put("birthday", formatDateStr);//生日
//			System.out.println("转换后的日期"+formatDateStr);
			datas.put("residence", userAllInfo.getResidence());//居住地
			datas.put("profession", userAllInfo.getProfession());//职业
			datas.put("age", userAllInfo.getAge());//年龄
			datas.put("constellation", userAllInfo.getConstellation());//星座
			datas.put("userEntity", userEntity);
			//用户书单影单（3个，随机）
			datas.put("bookListInfos", bookListInfos);
			datas.put("movieListInfos", movieListInfos);
			//用户常用标签
			datas.put("userTagInfos", userTagInfos);
			//用户总余额
			datas.put("totalAmount", totalAmount);
			//end = System.currentTimeMillis();
			//System.out.println("添加用户其他信息打赏等耗时："+(end-begin));
			//聊天
//			try {
//				EasemobUser easemobUser = easemobUserManager.createNewIMUserSingle(id);
//				String easeId = easemobUser.getEasemobId();
//				if(easeId != null){
//					datas.put("easeId", easeId);
//				}
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		/*long end8 = System.currentTimeMillis();
		System.out.println("处理用户数据耗时："+(end8-start8)+"毫秒");*/
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		long end = System.currentTimeMillis();
		//System.out.println("用户耗时："+(end-begin));
//		System.out.println(resString);
		return resString;
	}
	/**
	 * 获取用户动态数量
	 * @param uid
	 * @return
	 */
	public int getUserDynamicCount(Long uid){
		int resourceNum = 0;
		int postCount = 0;
		int articleCount = 0;
		int diaryCount = 0;
		int bkCount = 0;
		int mvCount = 0;
		int pulishCount = 0;
		int transmitCount = 0;
		//长文章数量
		Map<String, Object> postMap = postFacade.findPostCount(uid);
		int flagint = (Integer) postMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			postCount = (Integer) postMap.get("count");
		}
		//新的长文章数量
		Map<String, Object> articleMap = articleFacade.findArticleCount(uid);
		flagint = (Integer) articleMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			articleCount = (Integer) articleMap.get("count");
		}
		//日志数量
		Map<String, Object> diaryMap = diaryFacade.findDiaryCount(uid);
		flagint = (Integer) diaryMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			diaryCount = (Integer) diaryMap.get("count");
		}
		//书评数量
		Map<String, Object> bkMap = bkCommentFacade.findBkCommentCount(uid);
		flagint = (Integer) bkMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			bkCount = (Integer) bkMap.get("count");
		}
		//影评数量
		Map<String, Object> mvMap = mvCommentFacade.findMvCommentCountByUid(uid);
		flagint = (Integer) mvMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			mvCount = (Integer) mvMap.get("count");
		}
		//推送数量
		Map<String, Object> pMap = actFacade.findPublishCount(uid);
		flagint = (Integer) pMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			pulishCount = (Integer) pMap.get("count");
		}
		//推送数量
		Map<String, Object> tMap = actFacade.findTransmitCountByUid(uid);
		flagint = (Integer) tMap.get("flag");
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			transmitCount = (Integer) tMap.get("count");
		}
		
		resourceNum = postCount + articleCount + diaryCount + bkCount + mvCount + pulishCount + transmitCount;
		
		return resourceNum;
	}
	
	/*public static void main(String[] args) {
		System.out.println((int)(Math.random()*5));
	}*/
	/**
	 * 展示相册
	 * @return
	 */
	public String viewAlbum(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据

		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		long start = System.currentTimeMillis();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		List<UserAlbum> userAlbums  = ucenterFacade.findUserAlbumByUid(id);
		List<UserAlbumInfo> userAlbumInfos = new ArrayList<UserAlbumInfo>();
		
		userAlbumInfos= getUserAlbumList(userAlbums, userAlbumInfos);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", userAlbumInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		long end = System.currentTimeMillis();
		//System.out.println("用户相册耗时："+(end-start));
		return resString;
	}
	/**
	 * 添加相册中的照片
	 * @return
	 */
	public String addAlbum(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		long id = 0;
		String url = "";
		List<String> urls = new ArrayList<String>();
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			try {
				url = dataq.get("url").toString();//已废弃
			} catch (Exception e) {
				url = "";
			}
			urls = (List<String>) dataq.get("urls");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		List<String> photos = new ArrayList<String>();
		if(url == null || "".equals(url)){
			photos.addAll(urls);
		}else{
			photos.add(url);
		}
		
		UserAlbum userAlbum = ucenterFacade.insertintoUserAlbum(uid, photos, CommentUtils.TYPE_ALBUM);
		
		List<Map<String, String>> list = userUtils.getListFromAlbumJsonByIdOrUrl(userAlbum.getContent(), null, photos);
		Map<String, String> map = userUtils.getMapFromAlbumJsonByIdOrUrl(userAlbum.getContent(), null, photos.get(0));
		Long imageId = Long.valueOf(map.get("id"));
		
		flagint = userAlbum.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", list);
			datas.put("id", imageId);
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
	 * 删除相册中的照片
	 * @return
	 */
	public String delAlbum(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		List<String> ids = null;
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			ids = (List<String>) dataq.get("ids");
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		UserAlbum userAlbum = ucenterFacade.deleteUserPicture(id, ids);
		
		flagint = userAlbum.getFlag();
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
	 * 编辑用户信息
	 * @return
	 */
	public String editUser(long uid,String reqs){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		String face_url = "";// 头像地址
		String sex = "0";// 性别
		String sign = "";// 个性签名
		String interest = "";// 用户兴趣
		String introduction = "";// 个人说明
		String uname = "";
		String affective="";//个人感情状态
		String residence="";//居住地
		String profession="";//职业
		String birthday = "" ;
		String age = "";//年龄
		String constellation = "";//星座
		long birthdayDate = 0;
		List<String> userTagsInfos = new ArrayList<String>();
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			uname = (String) dataq.get("uname");
			face_url = (String) dataq.get("face_url");
			try{
				sex = (String) dataq.get("sex");
			}catch (Exception e) {
				sex = "0";
			}
			sign = CheckParams.objectToStr((String) dataq.get("sign"));
			interest = "";//(String) dataq.get("interest");
			introduction = CheckParams.objectToStr((String) dataq.get("introduction"));
			affective = CheckParams.objectToStr((String) dataq.get("affective"));
			residence = CheckParams.objectToStr((String) dataq.get("residence"));
			profession = CheckParams.objectToStr((String) dataq.get("profession"));
			try {
				userTagsInfos = (List<String>) dataq.get("userTagsInfos");
			} catch (Exception e) {
				userTagsInfos = new ArrayList<String>();
			}
			
			birthday = (String) dataq.get("birthday");
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if("".equals(birthday)){
				birthday = "2015-00-00";
			}
			birthdayDate = DateUtil.formatLong(birthday, "yyyy-MM-dd");
			
			try{
				constellation = (String) dataq.get("constellation");
				if(null==constellation){
					constellation = "";
				}
			}catch (Exception e) {
				constellation = "";
			}
			try{
				age = (String) dataq.get("age");
				if(null==age){
					age = "";
				}
			}catch (Exception e) {
				age = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flagint = ResultUtils.ERROR;
		}
//		System.out.println(req);
		
		if(userTagsInfos == null){
			userTagsInfos = new ArrayList<String>();
		}
		List<UserTag> userTags = myBkFacade.insertUserTag(uid, CheckParams.putListToString(userTagsInfos), CommentUtils.TAG_USER);
		
		
		
		//修改用户信息方法
		UserAllInfo uai = ucenterFacade.editUserInfo(uid, face_url, uname, sex, sign, 
				interest, introduction, affective, residence, profession,birthdayDate,constellation,age);//(uid, face_url, uname, sex, sign, interest, introduction, affective, residence, profession,brithday);
		
		flagint = uai.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			//将用户修改的标签放入到缓存中
			resourceManager.setUserTagToJedis(uid, userTagsInfos);
			//将用户的信息放入缓存中
			userJedisManager.saveOneUserInfo(uai.getUserId(), JedisConstant.USER_HASH_ID, uai.getUserId()+"");
			userJedisManager.saveOneUserInfo(uai.getUserId(), JedisConstant.USER_HASH_NAME, uai.getName());
			userJedisManager.saveOneUserInfo(uai.getUserId(), JedisConstant.USER_HASH_FACE, uai.getFaceAddress());
			userJedisManager.saveOneUserInfo(uai.getUserId(), JedisConstant.USER_HASH_SIGN, uai.getSign());
			userJedisManager.saveOneUserInfo(uai.getUserId(), JedisConstant.USER_HASH_TYPE, uai.getLevel()+"");
			userJedisManager.saveOneUserInfo(uai.getUserId(), JedisConstant.USER_HASH_SEX, uai.getSex());
			
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
	 * 检查用户昵称
	 * @return
	 */
	public String checkNickname(String reqs){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String uname = "";
		Long id = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			uname = (String) dataq.get("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		UserInfo userInfo = ucenterFacade.userNameIsExist(id, uname);
		flagint = userInfo.getFlag();
		
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			if(userInfo.getUserId() != 0){
				datas.put("exist", TRUE);
			}else{
				datas.put("exist", FALSE);
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
	 * 搜索用户
	 * @return
	 */
	public String searchUser(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String uname = "";
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			//type = (String) dataq.get("type");
			uname = (String) dataq.get("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
//		List<user> userAllInfos = new ArrayList<UserAllInfo>();
//		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		
		UserInfo userInfo = ucenterFacade.findUserInfoByNameOrMobilePhone(uname);
		
		UserEntity userEntity = new UserEntity();
		if(userInfo.getUserId() !=0){
			userEntity  = fileUtils.copyUserInfo(userInfo, 1);
			userUtils.putIsInterestToUserEntity(userEntity,uid, ucenterFacade,userJedisManager);
			flagint = ResultUtils.SUCCESS;
		}else{
			flagint = ResultUtils.ERROR;
		}
		
//		userEntities = getUserEntityList(userAllInfos, userEntities);
		
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("userEntity", userEntity);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			//error = MessageUtils.getResultMessage(flagint);
			//LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", "没有找到该用户");
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 模糊搜索用户
	 * @return
	 */
	public String matchUser(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String uname = "";
		Integer page = null;
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			uname = (String) dataq.get("name");
			page = (Integer.valueOf((String)dataq.get("page"))) - 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<UserEntity> entities = this.searchApiManager.userSearchByName(uname, page * 15, 15);

		if(entities != null){
			for (UserEntity entity : entities) {
				userUtils.putIsInterestToUserEntity(entity, uid, ucenterFacade,userJedisManager);
			}
			flagint = ResultUtils.SUCCESS;
		}else{
			flagint = ResultUtils.ERROR;
		}

//		userEntities = getUserEntityList(userAllInfos, userEntities);
		
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("userList", entities);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			//error = MessageUtils.getResultMessage(flagint);
			//LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", "没有搜索到用户");
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: searchUserDynamic</p> 
	 * <p>Description: 搜索个人动态</p> 
	 * @author :changjiang
	 * date 2015-6-1 上午10:39:18
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String searchUserDynamic(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		Long id = null;
		String keyWord = "";
		Integer page = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			id = Long.valueOf(CheckParams.stringToLong((String) dataq.get("id")));
			keyWord = (String)dataq.get("keyWord");
			page = (Integer.valueOf((String)dataq.get("page"))) - 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> map = searchApiManager.eventSearchByUserId(id, keyWord, page*10, 10);
		String total = (String)map.get("total").toString();
		flag = (String)map.get("flag");
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("subjects");
		Iterator<Map<String, Object>> itList = list.iterator();
		Map<String, Object> resMap = new HashMap<String, Object>();
		Long resId = 0l;
		String resType = "";
		String resInfoStr = "";
		List<String> resinfoList = new ArrayList<String>();
		while(itList.hasNext()){
			resMap = itList.next();
			resId = Long.valueOf(CheckParams.stringToLong((String)resMap.get("id")));
			resType = (String)resMap.get("type");
			resInfoStr = resourceManager.getResourceInfoStr(resId, id, resType,uid);
			if(null!=resInfoStr){
				resinfoList.add(resInfoStr);
			}
		}
//		userEntities = getUserEntityList(userAllInfos, userEntities);
		
		
		//修改用户信息方法
		/*datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("userList", entities);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			//error = MessageUtils.getResultMessage(flagint);
			//LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", "没有搜索到用户");
		}
		datas.put("flag", flag);*/
		//处理返回数据
		resString = "{\"res\":{\"data\":{\"flag\":\""+flag+"\",\"total\":\""+total+"\",\"list\":"+resinfoList.toString()+"}}}";
				//getResponseData(datas);
		
		return resString;
	}
	
	
	/**
	 * 
	 * <p>Title: editPassword</p> 
	 * <p>Description: 修改密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午12:03:16
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String editPassword(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =new HashMap<String, Object>();
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String mobilePhone="";//电话号码
		String oldPassword="";//之前的密码
		String newPassword="";//新的密码
		String verifyPassword="";//确认的密码
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//mobilePhone = dataq.get("mobilePhone").toString();
			oldPassword = dataq.get("oldPassword").toString();
			newPassword = dataq.get("newPassword").toString();
			verifyPassword = dataq.get("verifyPassword").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		userEntities = getUserEntityList(userAllInfos, userEntities);
		//查询用户的信息
		UserInfo userInfo = ucenterFacade.editPassword(uid, oldPassword, newPassword, verifyPassword);
		int resultflag = userInfo.getFlag();
		datas = new HashMap<String, Object>();
		
		if(ResultUtils.SUCCESS==resultflag||resultflag==UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", MessageUtils.getResultMessage(resultflag));
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: forgetPassword</p> 
	 * <p>Description: 忘记密码</p> 
	 * @author :changjiang
	 * date 2015-4-9 下午8:23:44
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String forgetPassword(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =new HashMap<String, Object>();
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String mobilePhone="";//电话号码
		String newPassword="";//新的密码
		String verifyPassword="";//确认的密码
		Long userId = 0l;//用户id
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			mobilePhone = dataq.get("mobilePhone").toString();
			newPassword = dataq.get("newPassword").toString();
			verifyPassword = dataq.get("verifyPassword").toString();
			userId = Long.valueOf(CheckParams.objectToStr(dataq.get("userId")));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		userEntities = getUserEntityList(userAllInfos, userEntities);
	/*	System.out.println();*/
		//查询用户的信息
		UserInfo userInfo = new UserInfo();
		userInfo = ucenterFacade.forgetPassword(mobilePhone, newPassword, verifyPassword);

		int resultflag = userInfo.getFlag();
		datas = new HashMap<String, Object>();
		
		if(ResultUtils.SUCCESS==resultflag||resultflag==UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", MessageUtils.getResultMessage(resultflag));
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}

	/**
	 * 绑定手机号
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String bindingMobile(String reqs,Long uid){
		Map<String, Object> req =new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =new HashMap<String, Object>();
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String mobilePhone="";//电话号码
		String newPassword="";//新的密码
		String verifyPassword="";//确认的密码
		//去掉空格
		reqs = reqs.trim();

		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			mobilePhone = dataq.get("mobilePhone").toString();
			newPassword = dataq.get("newPassword").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		userEntities = getUserEntityList(userAllInfos, userEntities);
	/*	System.out.println();*/
		//查询用户的信息
		UserInfo userInfo = new UserInfo();
		userInfo = ucenterFacade.bindingMobile(uid,mobilePhone,newPassword);

		int resultflag = userInfo.getFlag();
		datas = new HashMap<String, Object>();

		if(ResultUtils.SUCCESS==resultflag||resultflag==UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", MessageUtils.getResultMessage(resultflag));
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 日历形式的看过多少本书和电影
	 */
	public String getBMCalendar(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		Calendar ca=Calendar.getInstance();
		final int thisyear = ca.get(Calendar.YEAR);//今年
		final int thismonth = ca.get(Calendar.MONTH)+1;//这个月
		int year = thisyear;//默认今年
		int month = thismonth;//默认这个月
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String yearStr = dataq.get("year")+"";
			String monthStr = dataq.get("month")+"";
			
			if(StringUtils.isInteger(yearStr)){
				year = Integer.valueOf(yearStr);
			}
			if(year<1){
				year = thisyear;
			}
			if(StringUtils.isInteger(monthStr)){
				month = Integer.valueOf(monthStr);
			}
			if(month<1 || month >12){
				month = thismonth;//默认这个月
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//先初始化数据集合，默认值为没有更新和即将上映的电影
		int days = DateUtil.getDaysByYearMonth(year, month);
		Map<Integer,Map<String,Object>> maps = new HashMap<Integer,Map<String,Object>>(days);
		Map<Integer,Integer> dynamicmap = new HashMap<Integer,Integer>(days);
		Map<Integer,Integer> releasemap = new HashMap<Integer,Integer>(days);
		for(int i=0;i<days;i++){
			Map<String,Object> onemap = new HashMap<String,Object>(3);
			onemap.put("day", i+1);
			onemap.put("dynamic", 0);
			onemap.put("release", 0);
			maps.put(i, onemap);
			dynamicmap.put(i, 0);
			releasemap.put(i, 0);
		}
		
		int bkcommentcount = 0;//要查询的月份的书评数量
		int mvcommentcount = 0;//要查询的月份的影评数量
		int diarycount = 0;//要查询的月份的图文数量
		
		long starttime =  DateUtil.getTheMonthStartTime(year, month);
		long endtime = DateUtil.getTheMonthEndTime(year, month);
		int thisday = DateUtil.getTodayDay(null);//今天的号
		//只有查询时间是当前月或者早于当前月，才会有用户的评论信息
		if(year<thisyear || (year==thisyear && month<=thismonth)){
			List<BkComment> bkcomments = bkCommentFacade.findMyBkCommentListByTime(uid, starttime, endtime);
			List<MvComment> mvcomments = mvCommentFacade.findUserMvCommentsByTime(uid, starttime, endtime);
			List<Diary> diarys = diaryFacade.findUserDiaryTime(uid, starttime, endtime);
			//List<Article> articles = articleFacade.findUserArticlesByTime(uid, starttime, endtime);
			if(bkcomments!=null && bkcomments.size()>0){
				for(int i=0;i<bkcomments.size();i++){
					BkComment bkComment = bkcomments.get(i);
					if(bkComment.getId()>0){
						long createtime = bkComment.getCreateDate();
						//计算出是这个月的第几天
						int day = (int) ((createtime - starttime)/(24*60*60*1000L)+1);
						int value = 0;
						if(dynamicmap.get(day-1)!=null){
							value = dynamicmap.get(day-1);
						}
						dynamicmap.put(day-1, value+1);
						bkcommentcount++;
					}
				}
			}
			if(mvcomments!=null && mvcomments.size()>0){
				for(int i=0;i<mvcomments.size();i++){
					MvComment mvcomment = mvcomments.get(i);
					if(mvcomment.getId()>0){
						long createtime = mvcomment.getCreateDate();
						//计算出是这个月的第几天
						int day = (int) ((createtime - starttime)/(24*60*60*1000L)+1);
						int value = 0;
						if(dynamicmap.get(day-1)!=null){
							value = dynamicmap.get(day-1);
						}
						dynamicmap.put(day-1, value+1);
						mvcommentcount++;
					}
				}
			}
			if(diarys!=null && diarys.size()>0){
				for(int i=0;i<diarys.size();i++){
					Diary diary = diarys.get(i);
					if(diary.getId()>0){
						long createtime = diary.getBeginDate();
						//计算出是这个月的第几天
						int day = (int) ((createtime - starttime)/(24*60*60*1000L)+1);
						int value = 0;
						if(dynamicmap.get(day-1)!=null){
							value = dynamicmap.get(day-1);
						}
						dynamicmap.put(day-1, value+1);
						diarycount++;
					}
				}
			}
		}
		//如果查询时间是当前月或者晚于当前月，才会有即将上映的电影
		if(year>thisyear || (year==thisyear && month>=thismonth)){
			long filmListId = 133306509953204224L;
			List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(filmListId, null, null);
			int size = 0;
			if(mvListLinks!=null && mvListLinks.size()>0 && mvListLinks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				size = mvListLinks.size();
			}
			long ids[]=new long[size];
			if(mvListLinks!=null && mvListLinks.size()>0 && mvListLinks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				for(int i=0;i<mvListLinks.size();i++){
					ids[i]=mvListLinks.get(i).getMovieId();
				}
			}
			List<MvInfo> mvInfos = null;
			if(size>0){
				mvInfos = mvFacade.findMvInfosByIds(ids);
			}
			
			if(mvInfos!=null && mvInfos.size()>0){
				for(int i=0;i<mvInfos.size();i++){
					MvInfo mvInfo = mvInfos.get(i);
					if(mvInfo.getId()>0){
						long releaseDate = mvInfo.getReleaseDateSort();//形如:20120513
						//需要判断是否属于当前月上映的电影
						int yearmonth = (int) (releaseDate/100);
						int year1 = yearmonth/100;
						int month1 = yearmonth%100;
						if(year1==year && month1==month){
							//需要计算是否大于等于当天
							//计算出是这个月的第几天
							int day = (int) (releaseDate%100);
							if(day>=thisday){
								int value = 0;
								if(releasemap.get(day-1)!=null){
									value = releasemap.get(day-1);
								}
								releasemap.put(day-1, value+1);
							}
						}
					}
				}
			}
		}
		
		//
		for(int i=0;i<days;i++){
			Map<String,Object> onemap = maps.get(i);
			onemap.put("dynamic", dynamicmap.get(i));
			onemap.put("release", releasemap.get(i));
			maps.put(i, onemap);
		}
		List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>(days);
		for(int i=0;i<days;i++){
			lists.add(maps.get(i));
		}
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", lists);
			datas.put("today", thisday);
			datas.put("thisyear", thisyear);
			datas.put("thismonth", thismonth);
			datas.put("year", year);
			datas.put("month", month);
			datas.put("bkcomment", bkcommentcount);
			datas.put("mvcomment", mvcommentcount);
			datas.put("diarycount", diarycount);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "查询失败");
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	
	/**
	 * 点击日历中的某一天查看动态
	 */
	public String getDynamicByDay(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		Calendar ca=Calendar.getInstance();
		final int thisyear = ca.get(Calendar.YEAR);//今年
		final int thismonth = ca.get(Calendar.MONTH)+1;//这个月
		final int thisday = DateUtil.getTodayDay(null);//今天的号
		int year = thisyear;//默认今年
		int month = thismonth;//默认这个月
		int day = thisday;
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String yearStr = dataq.get("year")+"";
			String monthStr = dataq.get("month")+"";
			String dayStr = dataq.get("day")+"";
			
			if(StringUtils.isInteger(yearStr)){
				year = Integer.valueOf(yearStr);
			}
			if(year<1){
				year = thisyear;
			}
			if(StringUtils.isInteger(monthStr)){
				month = Integer.valueOf(monthStr);
			}
			if(month<1 || month >12){
				month = thismonth;//默认这个月
			}
			if(StringUtils.isInteger(dayStr)){
				day = Integer.valueOf(dayStr);
			}
			if(day<1 || day>31){
				day = thisday;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//
		int days = DateUtil.getDaysByYearMonth(year, month);
		if(day>days){
			day = days;
		}
		String monthstr = month+"";
		if(month<10){
			monthstr = "0"+month;
		}
		String daystr = day+"";
		if(day<10){
			daystr = "0"+day;
		}
		String datestr = year+"-"+monthstr+"-"+daystr;
		String starttimestr = datestr+" 00:00:00";
		String endtimestr = datestr+" 59:59:59";
		long starttime =  DateUtil.formatLong(starttimestr, DateUtil.timeformat);
		long endtime = DateUtil.formatLong(endtimestr, DateUtil.timeformat);
		
		List<ResourceInfo> mcresourceInfos = new ArrayList<ResourceInfo>(0);
		List<ResourceInfo> bcresourceInfos = new ArrayList<ResourceInfo>(0);
		List<ResourceInfo> dresourceInfos = new ArrayList<ResourceInfo>(0);
		List<MovieInfo> upcomings = new ArrayList<MovieInfo>(0);
		//只有查询时间是当前月或者早于当前月，才会有用户的评论信息
		if(year<thisyear || (year==thisyear && month<=thismonth)){
			List<BkComment> bkcomments = bkCommentFacade.findMyBkCommentListByTime(uid, starttime, endtime);
			List<MvComment> mvcomments = mvCommentFacade.findUserMvCommentsByTime(uid, starttime, endtime);
			List<Diary> diarys = diaryFacade.findUserDiarysByTime(uid, starttime, endtime);
			if(bkcomments!=null && bkcomments.size()>0){
				bcresourceInfos = new ArrayList<ResourceInfo>(bkcomments.size());
				for(int i=0;i<bkcomments.size();i++){
					BkComment bkComment = bkcomments.get(i);
					if(bkComment.getId()>0){
						ResourceInfo resourceInfo = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,netBookFacade,uid);
						bcresourceInfos.add(resourceInfo);
					}
				}
			}
			if(mvcomments!=null && mvcomments.size()>0){
				mcresourceInfos = new ArrayList<ResourceInfo>(mvcomments.size());
				for(int i=0;i<mvcomments.size();i++){
					MvComment mvcomment = mvcomments.get(i);
					if(mvcomment.getId()>0){
						ResourceInfo resourceInfo = movieUtils.putMVCommentToResource(mvcomment, ucenterFacade, actFacade, mvFacade,myMovieFacade,uid);
						mcresourceInfos.add(resourceInfo);
					}
				}
			}
			if(diarys!=null && diarys.size()>0){
				dresourceInfos = new ArrayList<ResourceInfo>(diarys.size());
				for(int i=0;i<diarys.size();i++){
					Diary diary = diarys.get(i);
					if(diary.getId()>0){
						ResourceInfo resourceInfo = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
						dresourceInfos.add(resourceInfo);
					}
				}
			}
		}
		//如果查询时间是当前月或者晚于当前月，才会有即将上映的电影
		if(year>thisyear || (year==thisyear && month>=thismonth)){
			long filmListId = 133306509953204224L;
			List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(filmListId, null, null);
			int size = 0;
			if(mvListLinks!=null && mvListLinks.size()>0 && mvListLinks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				size = mvListLinks.size();
			}
			long ids[]=new long[size];
			if(mvListLinks!=null && mvListLinks.size()>0 && mvListLinks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				for(int i=0;i<mvListLinks.size();i++){
					ids[i]=mvListLinks.get(i).getMovieId();
				}
			}
			List<MvInfo> mvInfos = null;
			if(size>0){
				mvInfos = mvFacade.findMvInfosByIds(ids);
			}
			
			if(mvInfos!=null && mvInfos.size()>0){
				for(int i=0;i<mvInfos.size();i++){
					MvInfo mvInfo = mvInfos.get(i);
					if(mvInfo.getId()>0){
						long releaseDate = mvInfo.getReleaseDateSort();//形如:20120513
						//需要判断是否属于当前月上映的电影
						int yearmonth = (int) (releaseDate/100);
						int year1 = yearmonth/100;
						int month1 = yearmonth%100;
						//需要计算是否大于等于当天
						//计算出是这个月的第几天，需要判断是否是查询的那天的电影
						int day1 = (int) (releaseDate%100);
						if(year1==year && month1==month && day1==day && day>=thisday){
							//是要查询的当天的上映的电影
							MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, FALSE);
							//需要赋值搜索量
							int totalSearchNum = resStatJedisManager.getTotalSearchNum(movieInfo.getId(), CommentUtils.TYPE_MOVIE);
							long searchNum = rankingManager.getSearchNum(movieInfo.getId(), totalSearchNum);
							movieInfo.setSearchNum(searchNum+"");
							MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(movieInfo.getId());
							if(mvAvgMark!=null && mvAvgMark.getId()>0){
								movieInfo.setScore(mvAvgMark.getMvAvgMark()+"");
								movieInfo.setTalentScore(mvAvgMark.getExpertsAvgMark()+"");
							}
							upcomings.add(movieInfo);
						}
					}
				}
			}
		}
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("today", thisday);
			datas.put("thisyear", thisyear);
			datas.put("thismonth", thismonth);
			datas.put("year", year);
			datas.put("month", month);
			datas.put("day", day);
			datas.put("mvcomments", mcresourceInfos);
			datas.put("bkcomments", bcresourceInfos);
			datas.put("diarys", dresourceInfos);
			datas.put("mvs", upcomings);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "查询失败");
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 查看某个月的书评、影评、图文
	 */
	public String getDynamicByMonth(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		Calendar ca=Calendar.getInstance();
		final int thisyear = ca.get(Calendar.YEAR);//今年
		final int thismonth = ca.get(Calendar.MONTH)+1;//这个月
		int year = thisyear;//默认今年
		int month = thismonth;//默认这个月
		String type = "";//查看的资源类型（书评、影评、图文）
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String yearStr = dataq.get("year")+"";
			String monthStr = dataq.get("month")+"";
			type = dataq.get("type")+"";
			
			if(StringUtils.isInteger(yearStr)){
				year = Integer.valueOf(yearStr);
			}
			if(year<1){
				year = thisyear;
			}
			if(StringUtils.isInteger(monthStr)){
				month = Integer.valueOf(monthStr);
			}
			if(month<1 || month >12){
				month = thismonth;//默认这个月
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long starttime =  DateUtil.getTheMonthStartTime(year, month);
		long endtime = DateUtil.getTheMonthEndTime(year, month);
		
		List<ResourceInfo> mcresourceInfos = new ArrayList<ResourceInfo>(0);
		List<ResourceInfo> bcresourceInfos = new ArrayList<ResourceInfo>(0);
		List<ResourceInfo> dresourceInfos = new ArrayList<ResourceInfo>(0);
		//只有查询时间是当前月或者早于当前月，才会有用户的评论信息
		if(year<thisyear || (year==thisyear && month<=thismonth)){
			if("1".equals(type)){
				//影评
				List<MvComment> mvcomments = mvCommentFacade.findUserMvCommentsByTime(uid, starttime, endtime);
				if(mvcomments!=null && mvcomments.size()>0){
					mcresourceInfos = new ArrayList<ResourceInfo>(mvcomments.size());
					for(int i=0;i<mvcomments.size();i++){
						MvComment mvcomment = mvcomments.get(i);
						if(mvcomment.getId()>0){
							ResourceInfo resourceInfo = movieUtils.putMVCommentToResource(mvcomment, ucenterFacade, actFacade, mvFacade,myMovieFacade,uid);
							mcresourceInfos.add(resourceInfo);
						}
					}
				}
			}else if("2".equals(type)){
				//书评
				List<BkComment> bkcomments = bkCommentFacade.findMyBkCommentListByTime(uid, starttime, endtime);
				if(bkcomments!=null && bkcomments.size()>0){
					bcresourceInfos = new ArrayList<ResourceInfo>(bkcomments.size());
					for(int i=0;i<bkcomments.size();i++){
						BkComment bkComment = bkcomments.get(i);
						if(bkComment.getId()>0){
							ResourceInfo resourceInfo = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,myBkFacade,getResourceInfoFacade,netBookFacade,uid);
							bcresourceInfos.add(resourceInfo);
						}
					}
				}
			}else if("3".equals(type)){
				//图文
				List<Diary> diarys = diaryFacade.findUserDiarysByTime(uid, starttime, endtime);
				if(diarys!=null && diarys.size()>0){
					dresourceInfos = new ArrayList<ResourceInfo>(diarys.size());
					for(int i=0;i<diarys.size();i++){
						Diary diary = diarys.get(i);
						if(diary.getId()>0){
							ResourceInfo resourceInfo = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
							dresourceInfos.add(resourceInfo);
						}
					}
				}
			}
		}
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("thisyear", thisyear);
			datas.put("thismonth", thismonth);
			datas.put("year", year);
			datas.put("month", month);
			datas.put("mvcomments", mcresourceInfos);
			datas.put("bkcomments", bcresourceInfos);
			datas.put("diarys", dresourceInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "查询失败");
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 查询“我看过的电影”的电影列表和“我读过的书”的书列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewMvAndBkList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		long movieListId = 0;
		long bookListId = 0;
		String movieListName = "我看过的电影";
		String bookListName = "我读过的书";
		Integer pagesize = 10;
		int mvsize = 0;
		int bksize = 0;
		List<ResourceInfo> mvresourceInfos = new ArrayList<ResourceInfo>();
		MovieListInfo movieListInfo = new MovieListInfo();
		//影单的详情
		MovieList dmovieList = new MovieList();
		dmovieList.setUid(uid);
		dmovieList.setFilmListName(movieListName);
		MovieList movieList = myMovieFacade.queryUserMvListByName(dmovieList);
		if(movieList!=null){
			movieListId = movieList.getId();
		}
		movieListInfo = movieUtils.putMVListToMovieList(movieList, myMovieFacade, mvFacade, ucenterFacade,actFacade,myBkFacade,uid);
		movieUtils.putMoneyToMovieList(movieListInfo, paycenterFacade);
		//获取影单中的电影
		List<MvListLink> mvListLinks = new ArrayList<MvListLink>();
		if(movieListId>0){
			mvListLinks = myMovieFacade.findMovieListInfo(movieListId,null, pagesize);
		}
		if(mvListLinks != null){
			mvsize = mvListLinks.size();
		}
		int type = movieList.getType();
		//将电影转换为显示类型
		mvresourceInfos = getNewMovieInfoResponseList(mvListLinks,uid,mvresourceInfos,type,movieList);
		flagint = ResultUtils.SUCCESS;
		
		
		
		BookListInfo bookListInfo = new BookListInfo();
		List<BookListLink> bookListLinks = new ArrayList<BookListLink>();
		List<ResourceInfo> bkresourceList = new ArrayList<ResourceInfo>();
		
		BookList dbookList = new BookList();
		dbookList.setBookListName(bookListName);
		dbookList.setuId(uid);
		BookList bookList =getResourceInfoFacade.queryUserBookListByName(dbookList);
		if(bookList!=null){
			bookListId = bookList.getId();
		}
		bookListInfo =fileUtils.putObjToBookListInfo(bookList,actFacade, getResourceInfoFacade, bkFacade, ucenterFacade,myBkFacade,netBookFacade,uid);
		fileUtils.putMoneyToBookList(bookListInfo, paycenterFacade);
		if(bookListId>0){
			bookListLinks = getResourceInfoFacade.findBookListInfo(bookListId, null,CommentUtils.BOOK_MOVIE_LIST_SIZE);
		}
		if(bookListLinks != null){
			bksize = bookListLinks.size();
		}
		int type2 = bookList.getType();
		bkresourceList = getOneBkListResponseList(bookListLinks,uid, bkresourceList,type2,bookList);
		flagint = ResultUtils.SUCCESS;
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("mvlist", mvresourceInfos);
			datas.put("mvlistsize", mvsize);
			datas.put("movieListInfo", movieListInfo);
			
			datas.put("bklist", bkresourceList);
			datas.put("booklistsize", bksize);
			datas.put("bookListInfo", bookListInfo);
			
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		//System.out.println(resString);
		return resString;
	}
	
	/**
	 * 神人申请
	 * @return
	 */
	public String shenrenApply(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		int flagint = 0;
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String realname = "";//真实姓名
		String content = "";//认证内容
		String mobileno = "";//手机号码
		String sid = "";//身份证
		String proof = "";//证明材料
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			realname = (String) dataq.get("realname");
			content = (String)dataq.get("content");
			mobileno = (String)dataq.get("mobileno");
			sid = (String)dataq.get("sid");
			proof = (String)dataq.get("proof");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(realname==null || realname.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "真实姓名不能为空");
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		}
		
		if(content==null || content.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "认证内容不能为空");
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		}
		
		if(mobileno==null || mobileno.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "手机号码不能为空");
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		}
		if(sid==null || sid.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "身份证不能为空");
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		}
		if(proof==null || proof.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "证明材料不能为空");
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		}
		boolean result = shenrenApplyFacade.insertintoShenrenApply(uid, realname, content, mobileno, sid, proof);
		if(result){
			flagint = ResultUtils.SUCCESS;
		}else{
			flagint = ResultUtils.ERROR;
		}
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "神人申请失败");
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 将用户列表格式化
	 * @param reqList
	 * @param resList
	 * @return
	 */
	private List<UserEntity> getUserEntityList(List<UserInfo> reqList,List<UserEntity> resList){
		UserEntity ui = null;
		
		if(reqList.size()>0){
			if(reqList.get(0).getUserId() != 0){
				flagint = ResultUtils.SUCCESS;
				
				for (UserInfo ua : reqList) {
					ui = fileUtils.copyUserInfo(ua,1);
					resList.add(ui);
				}
			}
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	private List<UserAlbumInfo> getUserAlbumList(List<UserAlbum> reqList,List<UserAlbumInfo> resList){
		UserAlbumInfo ui = null;
		
		if(reqList.size()>0){
			if(reqList.get(0).getId() != 0){
				flagint = ResultUtils.SUCCESS;
				
				for (UserAlbum ua : reqList) {
					ui = userUtils.putUserAlbumToInfo(ua);
					resList.add(ui);
				}
			}
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	
	/**
	 * 将影单内电影目录转换为可读类{xin}
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getNewMovieInfoResponseList(List<MvListLink> reqList , Long uid,  List<ResourceInfo> resList,int type,MovieList movieList){
		ResourceInfo ri = null;
		if(reqList.size()>0){
			if(reqList.get(0).getId() != 0){
				//flagint = ResultUtils.SUCCESS;
				for (MvListLink mv : reqList) {
					ri = movieUtils.putMVLinkToResource(mv, uid, ucenterFacade, actFacade, mvCommentFacade, mvFacade, myMovieFacade);
					if(ri != null && ri.getRid() != 0){
						resList.add(ri);
					}
				}
			}
		}else{
			//flagint = ResultUtils.SUCCESS;
		}
		
		return resList;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getOneBkListResponseList(List<BookListLink> reqList ,Long uid, List<ResourceInfo> resList,int type,BookList bookList){
		ResourceInfo ri = null;
		if(reqList.size()>0){
			if(reqList.get(0).getId() != 0){
				//flagint = ResultUtils.SUCCESS;
				for (BookListLink obj : reqList) {
					ri = bookUtils.putBkListLinkToResource(obj, ucenterFacade, actFacade, bkFacade, bkCommentFacade,getResourceInfoFacade,netBookFacade,myBkFacade,bookList,uid);
					if(ri.getRid() != 0){
						resList.add(ri);
					}
				}
			}else{
				//flagint = ResultUtils.ERROR;
			}
		}else if(reqList.size() == 0){
			//flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setBigFacade(BigFacade bigFacade) {
		this.bigFacade = bigFacade;
	}
	public void setBookListManager(BookListManager bookListManager) {
		this.bookListManager = bookListManager;
	}
	public void setMovieListManager(MovieListManager movieListManager) {
		this.movieListManager = movieListManager;
	}
	public void setTagManager(TagManager tagManager) {
		this.tagManager = tagManager;
	}
	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
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

	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}
	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setActManager(ActManager actManager) {
		this.actManager = actManager;
	}
	public void setEasemobUserManager(EasemobUserManager easemobUserManager) {
		this.easemobUserManager = easemobUserManager;
	}
	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}
	public void setSearchApiManager(SearchApiManager searchApiManager) {
		this.searchApiManager = searchApiManager;
	}
	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}
	public void setShenrenApplyFacade(ShenrenApplyFacade shenrenApplyFacade) {
		this.shenrenApplyFacade = shenrenApplyFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}
	public void setRankingManager(RankingManager rankingManager) {
		this.rankingManager = rankingManager;
	}
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}
}
