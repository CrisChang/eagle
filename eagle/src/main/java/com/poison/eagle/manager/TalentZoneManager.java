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

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActTransmit;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.CommentInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TalentZoneInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MainSend;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.TalentUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.msg.client.MsgFacade;
import com.poison.msg.model.MsgAt;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Post;
import com.poison.resource.model.Serialize;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.TalentZone;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class TalentZoneManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(TalentZoneManager.class);
	
	
	private int flagint;
	
//	private List<Map<String, String>> reasonList;
	private GetResourceInfoFacadeImpl getResourceInfoFacade;
	private ActFacade actFacade;
	private UcenterFacade ucenterFacade;
	private DiaryFacade diaryFacade;
	private PostFacade postFacade;
	private BkFacade bkFacade;
	private BkCommentFacade bkCommentFacade;
	private MyBkFacade myBkFacade;
	private SerializeFacade serializeFacade;
	//电影相关
	private MvCommentFacade mvCommentFacade;
	private MyMovieFacade myMovieFacade;
	private MvFacade mvFacade;
//	private ResourceInfo resourceInfo;
	private MsgFacade msgFacade;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	private TalentUtils talentUtils = TalentUtils.getInstance();
	
	private ResourceManager resourceManager;
	private BookListManager bookListManager;
	private MovieListManager movieListManager;
	
	/**
	 * 查询达人圈
	 * @return
	 */
	public String viewTalentZone(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
//		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
//			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		List<UserAttention> userAttentions = ucenterFacade.findUserAttentionTalentedPersons(uid, String.valueOf(CommentUtils.USER_LEVEL_TALENT));

		//关注的领袖
		userEntities = getUserEntityResponseList(userAttentions, userEntities);
		
		TalentZone talentZone18 = ucenterFacade.findTalentZoneInfoByType(CommentUtils.TYPE_TALENTZONE18);
		TalentZone talentZone19 = ucenterFacade.findTalentZoneInfoByType(CommentUtils.TYPE_TALENTZONE19);
		
		//推荐领袖
		TalentZoneInfo talentZoneInfo18 = talentUtils.putTaletZoneToInfo(talentZone18,ucenterFacade,0);
		//领袖推荐
		TalentZoneInfo talentZoneInfo19 = talentUtils.putTaletZoneToInfo(talentZone19,ucenterFacade,0);
		
		
		List<TalentZoneInfo> talentZoneInfos = new ArrayList<TalentZoneInfo>();
		List<TalentZone> talentZones = ucenterFacade.findAllTalentZone();
		
		Iterator<TalentZone> iter = talentZones.iterator();
		while(iter.hasNext()){
			TalentZone talentZone = iter.next();
			if("18".equals(talentZone.getType())|| "19".equals(talentZone.getType())){
				iter.remove();
			}
		}
		
		//领袖圈
		talentZoneInfos = getTalentResponseList(talentZones, talentZoneInfos);
		
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("userList", userEntities);
			datas.put("recommendTalent", talentZoneInfo18);
			datas.put("recommendTalents", talentZoneInfo19);
			datas.put("talents", talentZoneInfos);
			
//			datas.put("talentZoneInfo", talentZoneInfo);
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
	 * 查询某个达人圈
	 * @return
	 */
	public String viewOneTalentZone(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		Long id = null;
		Long lastId = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			lastId = Long.valueOf(dataq.get("lastId").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		TalentZone talentZone = ucenterFacade.findTalentZoneInfo(id);
		
		TalentZoneInfo talentZoneInfo = talentUtils.putTaletZoneToInfo(talentZone,ucenterFacade,0);
		
		//将全内人的uid收集起来
		List<Long> uids = new ArrayList<Long>();
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		if(talentZoneInfo.getUserList().size()>0){
			for (UserEntity userEntity : talentZoneInfo.getUserList()) {
				uids.add(userEntity.getId());
			}
		}
		if(talentZoneInfo.getLeader().getId() != 0){
			uids.add(talentZoneInfo.getLeader().getId());
		}
		
		if(uids.size()>0){
			if(lastId == UNID){
				lastId = null;
			}
			resourceInfos = resourceManager.getFriendNewResoureList(uids, lastId, uid, resourceInfos);
		}
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("talent", talentZoneInfo);
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
	 * 查询关注的领袖
	 * @return
	 */
	public String viewUserTalentZone(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
//		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
//			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("talentZoneInfo", talentZoneInfo);
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
	 * 发现页首页
	 * @return
	 */
	public String viewDiscovery(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
//		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
//			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		//精选滚动页
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		List<ActPublish> actPublishs =  actFacade.findPublishListByRecommendType(CommentUtils.TYPE_DISCOVERY);
		if(actPublishs != null && actPublishs.size()>=3){
			actPublishs = actPublishs.subList(0, 3);
		}
		
		resourceInfos = resourceManager.getResponseList(actPublishs, uid, resourceInfos);
		
//		resourceInfos.get(0).setImageUrl("http://112.126.68.72/image/common/42b196e0fb91fe97a39c6a9f5e913625.png");
//		resourceInfos.get(1).setImageUrl("http://112.126.68.72/image/common/ce03b900951d70cbae05423a0a2f954c.png");
//		resourceInfos.get(2).setImageUrl("http://112.126.68.72/image/common/4db5910804652f1b6fb9a94231c79434.png");
		
		//推荐领袖
		TalentZone talentZone19 = ucenterFacade.findTalentZoneInfoByType(CommentUtils.TYPE_TALENTZONE19);
		TalentZoneInfo talentZoneInfo19 = talentUtils.putTaletZoneToInfo(talentZone19,ucenterFacade,0);
		
		//推荐影评
//		resourceInfos = new ArrayList<ResourceInfo>();
		List<ResourceInfo> movieComments = new ArrayList<ResourceInfo>();// resourceManager.getResoureListByPage(CommentUtils.TYPE_MOVIE_COMMENT, null, uid, resourceInfos);
		List<MvComment> mvComments = mvCommentFacade.findAllMvCommentListByType(null, CommentUtils.TYPE_DISCOVERY, null);
		movieComments = resourceManager.getResponseList(mvComments, uid, movieComments);
		if(movieComments.size()>6){
			movieComments = movieComments.subList(0, 6);
		}
		
		actUtils.subStringResourceListContent(movieComments,CommentUtils.RESOURCE_CONTENT_SIZE_DISCOVERY);
		
		
		//推荐电影
		List<MovieInfo> movieInfos = movieListManager.getHotMovieList(CommentUtils.TYPE_MOVIELIST);
		if(movieInfos.size()>6){
			movieInfos = movieInfos.subList(0, 6);
		}
		
		//推荐书评
//		resourceInfos = new ArrayList<ResourceInfo>();
		List<ResourceInfo> bookComments = new ArrayList<ResourceInfo>();// resourceManager.getResoureListByPage(CommentUtils.TYPE_MOVIE_COMMENT, null, uid, resourceInfos);
		List<BkComment> bkComments = bkCommentFacade.findAllBkCommentListByType(null, CommentUtils.TYPE_DISCOVERY, null);
		bookComments = resourceManager.getResponseList(bkComments, uid, bookComments);
		if(bookComments.size()>6){
			bookComments = bookComments.subList(0, 6);
		}
		actUtils.subStringResourceListContent(bookComments,CommentUtils.RESOURCE_CONTENT_SIZE_DISCOVERY);
		
		//推荐书单
		List<BookInfo> bookInfos = bookListManager.getHotBookList();
		if(bookInfos.size()>6){
			bookInfos = bookInfos.subList(0, 6);
		}
		
		
		
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("rollingList", resourceInfos);
			datas.put("recommendTalents", talentZoneInfo19);
			datas.put("movieComments", movieComments);
			datas.put("movieList", movieInfos);
			datas.put("bookComments", bookComments);
			datas.put("bookList", bookInfos);
			datas.put("movieListId", CommentUtils.WEB_PUBLIC_HOT_MOVIELIST_ID);
			datas.put("bookListId", CommentUtils.WEB_PUBLIC_HOT_BOOKLIST_ID);
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
		return resString;
	}
	
	/**
	 * 发现页推送精选资源
	 * @return
	 */
	public String publishResource(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String type = "";
		Long id = null;
		//去掉空格
		reqs = reqs.trim();
		
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
		
		ActPublish actPublish = actFacade.addOnePublishInfo(uid, id, type);
		flagint = actPublish.getFlag();
		
		//修改用户信息方法
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
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
//		System.out.println(resString);
		return resString;
	}
	
	
	
	/**
	 * 将领袖圈格式化
	 * @param list
	 * @param type
	 * @return
	 */
	public List getTalentResponseList(List<TalentZone> reqList , List<TalentZoneInfo> resList){
		TalentZoneInfo ri = new TalentZoneInfo();
		if(reqList.size()>0){
			Long id = reqList.get(0).getId();
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (TalentZone ma : reqList) {
					ri = talentUtils.putTaletZoneToInfo(ma, ucenterFacade, 1);
					resList.add(ri);
				}
			}
			
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	/**
	 * 将关注人格式化
	 * @param list
	 * @param type
	 * @return
	 */
	public List getUserEntityResponseList(List<UserAttention> reqList , List<UserEntity> resList){
		UserEntity ri = new UserEntity();
		if(reqList.size()>0){
			Long id = reqList.get(0).getAttentionId();
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (UserAttention ma : reqList) {
					ri = userUtils.putUserAttentionToEntity(ma, ucenterFacade);
					resList.add(ri);
				}
			}
			
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
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
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setBookListManager(BookListManager bookListManager) {
		this.bookListManager = bookListManager;
	}
	public void setMovieListManager(MovieListManager movieListManager) {
		this.movieListManager = movieListManager;
	}
	
}
