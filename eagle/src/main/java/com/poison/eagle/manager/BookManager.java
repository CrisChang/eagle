package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.keel.common.event.rocketmq.RocketProducer;
import com.keel.utils.UKeyWorker;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookTalkInfo;
import com.poison.eagle.entity.BookTalkListInfo;
//import com.poison.eagle.entity.QuestInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.BigUtils;
import com.poison.eagle.utils.BookUtils;
import com.poison.eagle.utils.BussinessLogUtils;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HtmlUtil;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.msg.client.MsgFacade;
//import com.poison.quest.client.QuestFacade;
//import com.poison.quest.model.Quest;
//import com.poison.quest.model.QuestProgress;
//import com.poison.quest.model.QuestReward;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.BookTalkFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.BkFacade;
import com.poison.store.client.BkOnlineReadFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.NetBook;
import com.poison.store.model.OnlineRead;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserStatistics;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class BookManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(BookManager.class);
	
	/**
	 * 记录的日志信息
	 */
	private static final  Log BUSINESS_LOG = LogFactory.getLog("COMMON.BUSINESS");
	
	private int flagint;
	
	
	private BkFacade bkFacade;
	private BkCommentFacade bkCommentFacade;
	private UcenterFacade ucenterFacade;
	private GetResourceInfoFacade getResourceInfoFacade;
	private MyBkFacade myBkFacade;
	private BookTalkFacade bookTalkFacade;
	private BkOnlineReadFacade bkOnlineReadFacade;
	private MsgFacade msgFacade;
	private BigFacade bigFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private NetBookFacade netBookFacade;
	private ActFacade actFacade;
	private TopicFacade topicFacade;
	private ResStatisticService resStatisticService;
	
	//private UKeyWorker reskeyWork;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private BookUtils bookUtils = BookUtils.getInstance();
	private BigUtils bigUtils = BigUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	
	private UserJedisManager userJedisManager;
	private ResourceManager resourceManager;
	//private PaycenterManager paycenterManager;
	
	private ResStatJedisManager resStatJedisManager;
	
	private RocketProducer eagleProducer;
	
	//private QuestFacade questFacade;

	/*public void setQuestFacade(QuestFacade questFacade) {
		this.questFacade = questFacade;
	}
	public void setPaycenterManager(PaycenterManager paycenterManager) {
		this.paycenterManager = paycenterManager;
	}
	 */
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}
	/**
	 * 单本图书详情
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBook(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		String type = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Integer.valueOf(dataq.get("id").toString());
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = "";
			}
//			isDB = Integer.valueOf(dataq.get("isDB").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		if(type == null ||"".equals(type) || "0".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		
		BookInfo bookInfo = new BookInfo();
//		if(isDB == TRUE){
		if(CommentUtils.TYPE_BOOK.equals(type)){
			
			BkInfo bkInfo = bkFacade.findBkInfo(id);
			flagint = bkInfo.getFlag();
			bookInfo = bookUtils.putBKToBookInfoIndex(bkInfo, FALSE,uid,bkCommentFacade,ucenterFacade);
		}else if(CommentUtils.TYPE_NETBOOK.equals(type)){
			NetBook netBook = netBookFacade.findNetBookInfoById(id);
			flagint = netBook.getFlag();
			bookInfo = bookUtils.putBKToBookInfoIndex(netBook, FALSE,uid,bkCommentFacade,ucenterFacade);
		}
		
		
//		//图书全部评论
//		List<BkComment> bkComments = bkCommentFacade.findAllBkComment(bookInfo.getId(), null,bookInfo.getType());
//		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		//我的评论列表
		List<BkComment> myComments = bkCommentFacade.findMyBkCommentList(uid, bookInfo.getId(), null,bookInfo.getType());
//		bkComments.removeAll(myComments);
		
		ResourceInfo resourceInfo =null;
		//将我的评论单独放进去
		if(myComments.size()>0){
			resourceInfo = fileUtils.putObjectToResource(myComments.get(0), ucenterFacade);
			try {
				actUtils.putIsCollectToResoure(resourceInfo, uid, actFacade,1);
			} catch (Exception e) {
				LOG.error("添加附加内容时出错，资源类型["+resourceInfo.getType()+"]id为:"+resourceInfo.getRid()+e.getMessage(), e.fillInStackTrace());
			}
			if(resourceInfo != null && resourceInfo.getRid() != 0){
				bookInfo.setResourceInfo(resourceInfo);
			}
		}
//		if(bkComments.size()>0){
//			for (BkComment bkComment : bkComments) {
//				if(bkComment != null && bkComment.getId() != 0){
//					resourceInfo = fileUtils.putObjectToResource(bkComment, ucenterFacade);
//					try {
//						actUtils.putIsCollectToResoure(resourceInfo, uid, actFacade);
//					} catch (Exception e) {
//						LOG.error("添加附加内容时出错，资源类型["+resourceInfo.getType()+"]id为:"+resourceInfo.getRid()+e.getMessage(), e.fillInStackTrace());
//					}
//					if(resourceInfo != null && resourceInfo.getRid() != 0){
//						resourceInfos.add(resourceInfo);
//					}
//				}
//			}
//			bookInfo.setCommentList(resourceInfos);
//		}

		
			
			BookListLink bookListLink = getResourceInfoFacade.findUserIsCollectBook(uid, bookInfo.getId(), type);//(uid, bookInfo.getId());
			if(bookListLink.getId() != 0){
				bookInfo.setInList(0);
			}
			
			
			List<String> bookTalks = new ArrayList<String>();
			List<BookTalk> bookTalkList = bookTalkFacade.findSomeBookTalkListByBookId(id,type);
			//List<BookTalkInfo> list = bookUtils.putBookTalkToBookTalkInfo(bookTalkList);
			BookTalk bookTalk = new BookTalk();
			String content = "";
			if(null!=bookTalkList||bookTalkList.size()!=0){
				Iterator<BookTalk> bookTalkIt = bookTalkList.iterator();
				while(bookTalkIt.hasNext()){
					bookTalk = bookTalkIt.next();
					content = bookTalk.getContent();
					if(content == null){
						content = "";
					}
					bookTalks.add(content.replace("<img src=\"", "").replace("\"/><br/><!--我是分隔符woshifengefu-->", ""));
				}
			}
		
		if(bookInfo != null && bookInfo.getId() != UNID){
			flagint =  ResultUtils.SUCCESS;
		}
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("bookInfo", bookInfo);
			datas.put("bookTalkList", bookTalks);
			
			//添加热搜书的日志打印
			BussinessLogUtils.searchBookLog(bookInfo.getId(), bookInfo.getName(), bookInfo.getType(), uid);
			/*Map<String, Object> bussinessMap = new HashMap<String, Object>();
			bussinessMap.put("method", "");
			BUSINESS_LOG.info(message);*/
			try{
				//记录阅读数量
				resStatJedisManager.addReadNum(bookInfo.getId(), type, 0, "", 0);
			}catch(Exception e){
				LOG.error("增加书的阅读数量出错，id为：" + bookInfo.getId() + e.getMessage(), e.fillInStackTrace());
			}
			//从缓存中获取书被收藏的数量
			int count =0;
			try {
				count = resourceManager.getResourceCollectCountFromJedis((long)bookInfo.getId(), CommentUtils.TYPE_BOOK);
			} catch (Exception e) {
				count =0;
				LOG.error("获取书被收藏数量出错，id为："+bookInfo.getId()+e.getMessage(), e.fillInStackTrace());
			}
			datas.put("collectCount", count);
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
	 * 图书试读
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBookRead(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		String type = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Integer.valueOf(dataq.get("bookId").toString());
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		if(type == null ||"".equals(type) || "0".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		
		
		String read = "";
		OnlineRead onlineRead = bkOnlineReadFacade.findOnlineReadByBkId(id, type);//(id);
		//书摘信息
		//List<BookTalk> bookTalksList = bookTalkFacade.findSomeBookTalkListByBookId(id);
		
		flagint = onlineRead.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			read = onlineRead.getOnlineRead();
		}
		//数据不存在属于正常情况，也要返回正确
		if(flagint == ResultUtils.DATAISNULL){
			flagint = ResultUtils.SUCCESS;
		}
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("read", read);
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
	 * 查找书
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String searchBook(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String name = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			name = (String) dataq.get("name");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		

		//库中的书
		List<BkInfo> bkList = bkFacade.findBkInfoByName(name);
		
		List<BookInfo> bookList = new ArrayList<BookInfo>();
		
		bookList = getBKResponseList(bkList, null, bookList);
		//不是库中的书
		List<MyBk> myBks = myBkFacade.findMyBkList(uid, name);
		bookList = getBKResponseList(myBks, null, bookList);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", bookList);
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
	 * 查看书评列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBookCommentList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		long lastCommentId = 0;
		String type = "";
		String commentType = "";
		String resourceType = CommentUtils.TYPE_BOOK_COMMENT;
		String version = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Integer.valueOf(dataq.get("id").toString());
			lastCommentId = Long.valueOf(dataq.get("lastId").toString());
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = CommentUtils.TYPE_BOOK;
			}
			
			//是长评还是短评列表
			resourceType = CheckParams.objectToStr((String) dataq.get("resourceType"));
			if(null==resourceType||"".equals(resourceType)){//当没有类型时 默认普通短评
				resourceType = CommentUtils.TYPE_BOOK_COMMENT;
			}
			version = (String) dataq.get("version");
			/*commentType =  (String) dataq.get("commentType");
			if(null==commentType){
				//eggg
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		if(type == null || "0".equals(type) || "".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		List<BkComment> bkcommentList = null;
		if("1".equals(version)){
			//获取评论
			if(lastCommentId == 0){
				bkcommentList = bkCommentFacade.findAllBkComment(id, null, type,resourceType);//(id, null);
			}else{
				bkcommentList = bkCommentFacade.findAllBkComment(id, lastCommentId,type,resourceType);
			}
		}else{
			if(lastCommentId == 0){
				bkcommentList = bkCommentFacade.findBkCommentListForOld(id, null, type,resourceType);//(id, null);
			}else{
				bkcommentList = bkCommentFacade.findBkCommentListForOld(id, lastCommentId,type,resourceType);
			}
		}
		
		List<ResourceInfo> commentList = new ArrayList<ResourceInfo>();
		
		commentList = getCommentResponseList(bkcommentList, null,uid, commentList);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", commentList);
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
	 * <p>Title: viewBookCommentPraiseList</p> 
	 * <p>Description: 查询点赞数的列表</p> 
	 * @author :changjiang
	 * date 2015-6-30 下午5:35:36
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewBookCommentPraiseList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		String type = "";
		String commentType = "";
		String resourceType = "";
		String sort = "";
		//去掉空格
		reqs = reqs.trim();
		int page = 0;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Integer.valueOf(dataq.get("id").toString());
			try{
				page = Integer.valueOf((String)dataq.get("page"));
			}catch (Exception e) {
				page = 1;
			}
			type = (String) dataq.get("type");
			
			resourceType = CheckParams.objectToStr((String) dataq.get("resourceType"));
			
			//添加排序字段 0为时间，1为热度 (按时间排序的是另一个接口，这个接口只是按热度的)
			sort = CheckParams.objectToStr((String) dataq.get("sort"));
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		if(type == null || "0".equals(type) || "".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		List<ResStatistic> ResStatisticList = new ArrayList<ResStatistic>();
		if("1".equals(sort)){//按照有用最多排序
			ResStatisticList = resStatisticService.findResStatisticRankByUsefulAndType(resourceType,id, type,0, (page - 1) * 5, page * 5);
		}else{
			ResStatisticList = resStatisticService.findResStatisticRankByUseful(id, type,0, (page-1)*5, page*5);
			//findResStatisticRankByPraise(id, type, (page-1)*5, page*5);
		}
		
		List<String> resInfoStrList = new ArrayList<String>();
		if(null!=ResStatisticList&&ResStatisticList.size()>0){
			Iterator<ResStatistic> ResStatisticListIt = ResStatisticList.iterator();
			while(ResStatisticListIt.hasNext()){
				ResStatistic resStatistic = ResStatisticListIt.next();
				if(null!=resStatistic){
					String resStr = resourceManager.getResourceInfoStr(resStatistic.getResId(), 0, type,uid);
					resInfoStrList.add(resStr);
				}
			}
		}
		
		resString = "{\"res\":{\"data\":{\"flag\":\"0\",\"list\":"+resInfoStrList.toString()+"}}}";
		/*List<ResourceInfo> commentList = new ArrayList<ResourceInfo>();
		
		commentList = getCommentResponseList(bkcommentList, null,uid, commentList);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", commentList);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);*/
		
		return resString;
	}
	
	
	/**
	 * 查看书摘列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBookDigestList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		long lastCommentId = 0;
		String type = CommentUtils.TYPE_BOOK;
		String commentType = "";
		String resourceType = CommentUtils.TYPE_BOOK_DIGEST;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Integer.valueOf(dataq.get("id").toString());
			lastCommentId = Long.valueOf(dataq.get("lastId").toString());
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = CommentUtils.TYPE_BOOK;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		if(type == null || "0".equals(type) || "".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		List<BkComment> bkcommentList = null;
		int flagint = 0;
		//获取评论
		if(lastCommentId == 0){
			bkcommentList = bkCommentFacade.findAllBkComment(id, null, type,resourceType);//(id, null);
		}else{
			bkcommentList = bkCommentFacade.findAllBkComment(id, lastCommentId,type,resourceType);
		}
		int size = 0;
		if(bkcommentList!=null && bkcommentList.size()>0){
			flagint = bkcommentList.get(0).getFlag();
			size = bkcommentList.size();
		}
		
		List<ResourceInfo> commentList = new ArrayList<ResourceInfo>(size);
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			commentList = getCommentResponseList(bkcommentList, null,uid, commentList);
			if(commentList!=null && commentList.size()>0){
				flagint = commentList.get(0).getFlag();
			}
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", commentList);
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
	 * <p>Title: viewLongBookCommentList</p> 
	 * <p>Description: 查看长书评列表</p> 
	 * @author :changjiang
	 * date 2015-6-26 下午4:31:13
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewLongBookCommentList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		Long lastCommentId = null;
		String type = "";
		String commentType = "";
		String resourceType = CommentUtils.TYPE_BOOK_COMMENT;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String lastId = (String) dataq.get("lastId");
			if(null==lastId||lastId.equals("")){
				lastCommentId = null;
			}else{
				lastCommentId = Long.valueOf(lastId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		List<BkComment> bkcommentList = bkCommentFacade.findUserLongBkCommentListByUserId(uid, lastCommentId);
		
		List<ResourceInfo> commentList = new ArrayList<ResourceInfo>();
		
		commentList = getCommentResponseList(bkcommentList, null,uid, commentList);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", commentList);
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
	 * 写书评
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeBookComment(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		List commentList = null;
		String score = "";
		long commentId = 0;
		int isDB = 0;
		String name = "";
		String type = "";
		Long bookListId = 0l;
		String scan = "";
		//去掉空格
		reqs = reqs.trim();
		String beforeScore = "";
		String lon = "";//经度
		String lat = "";//维度
		String locationName="";//地点描述
		String locationCity="";//地点城市
		String locationArea="";//地点地区
		List<Map<String, String>> oList = new ArrayList<Map<String,String>>();//话题列表等信息
		Long vid = 0l;//虚拟主键
		String title = "";//标题
		String cover = "";//封面
		String resourceType = "";//当前的资源类型

		//是否只打分的标识
		boolean isScore = true;
		
		boolean add = false;//是否发表新的书评或书摘
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			commentList = (List) dataq.get("list");
			if(null==commentList||commentList.isEmpty()){
				isScore = false;
			}
			score = (String) dataq.get("score");
			String ob = (String) dataq.get("commentId");
			
			lon = CheckParams.objectToStr((String) dataq.get("lon"));
			lat = CheckParams.objectToStr((String) dataq.get("lat"));
			locationName = CheckParams.objectToStr((String) dataq.get("locationName"));
			locationCity = CheckParams.objectToStr((String) dataq.get("locationCity"));
			locationArea = CheckParams.objectToStr((String) dataq.get("locationArea"));
			
			//标题
			title = CheckParams.objectToStr((String) dataq.get("title"));
			//封面
			cover = CheckParams.objectToStr((String) dataq.get("cover"));
			//资源类型
			resourceType = CheckParams.objectToStr((String) dataq.get("resourceType"));
			if("".equals(resourceType)){
				resourceType = CommentUtils.TYPE_BOOK_COMMENT;
			}
			
			//常江修改
			//是否扫描 0为扫描1为不是扫描
			scan = (String) dataq.get("scan");
			//System.out.println("是否扫描"+scan);
			if(null==scan||"".equals(scan)){
				scan = "1";
			}
			if(null==ob||"".equals(ob)){
				ob="0";
			}
			commentId = Long.valueOf(ob);
			id = Integer.valueOf(CheckParams.objectToStr(dataq.get("id")).toString());
//			List<BkComment> bkCommentList = bkCommentFacade.findMyBkCommentList(uid, id, null, CommentUtils.TYPE_BOOK);
//			if(null!=bkCommentList&&!bkCommentList.isEmpty()){//当查询出来的不为空并且不报错
//				commentId = bkCommentList.get(0).getId();
//			}

			if(commentId == UNID){
			}else{
				beforeScore = (String) dataq.get("beforeScore");
				if(null==beforeScore){
					beforeScore = "0";
				}
			}
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = "";
			}
			try {
				bookListId = Long.valueOf(dataq.get("bookListId").toString());
				if(0l == bookListId){
					bookListId = null;
				}

			} catch (Exception e) {
				bookListId = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		if(type == null || "".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		//bookListId = 142097387517775872l;
				
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		int level= userAllInfo.getLevel();
		if(level!=50){
			level = 0;
		}
		
		BkComment bkComment = new BkComment();
		
		
		Map<String, Object> map = WebUtils.putDataToMap(commentList,ucenterFacade);
		String resultContent = (String) map.get("resultContent");

		//长书评需要对文字处理
		if(resourceType.equals(CommentUtils.TYPE_ARTICLE_BOOK)){
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

				resultContent = resultContent.replace(tempStr, "</p><p><a class=\"imagescroll\"><img originalSrc=\"" + tempStr + "\" _originalSrc=\""+tempStr+"\"  width=\""+w+"\" height=\""+h+"\" /></a></p><p>");
			}

			resultContent = resultContent.replaceAll("\r\n","</p><p>").replaceAll("\n", "</p><p>");
			resultContent = "<p>"+resultContent+"</p>";
		}

		String linkType = (String) map.get("linkType");
		String linkName = (String) map.get("linkName");
		
		if(commentId == UNID){
			add = true;
			//判断是否是库中的书
			//库中的书先添加评论，在存到默认书单中
//			bkComment = bkCommentFacade.addOneBkCommentByType(uid, id, WebUtils.putDataToHTML5(commentList), score, level+"", type);//(uid, id, WebUtils.putDataToHTML5(commentList), score, TRUE, isDB);
			bkComment = bkCommentFacade.addOneBkComment(uid, id, resultContent, score, 0, 0, type, bookListId,scan,lon,lat,locationName,locationCity,locationArea,level+"",title,cover,resourceType);
			//System.out.println(bkComment);
			//算平均分
			
			/*if(level == CommentUtils.USER_LEVEL_TALENT){
				BkAvgMark avgMark = bkCommentFacade.addExpertsBkAvgMark(id, Float.valueOf(score));
						//addExpertsAvgMark(id, Float.valueOf(score));
			}else{*/
				BkAvgMark avgMark =bkCommentFacade.addBkAvgMark(id,type, Float.valueOf(score));
				//MvAvgMark avgMark = mvCommentFacade.addMvAvgMark((int)id, Float.valueOf(score));
			//}
			
			//增加评论数量
			if(bkComment.getId() != 0){
				try {
					UserStatistics userStatistics = userStatisticsFacade.updateBkcommentCount(uid);
					if(userStatistics!=null && userStatistics.getId()>0){
						userJedisManager.saveOneUserResourceCount(uid, JedisConstant.BKCOMMENT_COUNT, userStatistics.getBkcommentCount()+"");
					}
				} catch (Exception e) {
					LOG.error("增加评论数量出错:"+e.getMessage(), e.fillInStackTrace());
				}
			}
			//用户的书评任务操作,字数需要大于20字
			/*if(bkComment.getFlag()==ResultUtils.SUCCESS || bkComment.getFlag() == UNID){
				if(bkComment.getComment()!=null && bkComment.getComment().length()>=20){
					addQuestBkComment(uid);
				}
			}*/
		}else{
			bkComment = bkCommentFacade.updateMyCommentByBook(commentId, score, WebUtils.putDataToHTML5(commentList),title,cover);
			if(null!=bookListId && bookListId != 0){
				long userId = bkComment.getUserId();
				long bookId = bkComment.getBookId();
				getResourceInfoFacade.moveOneBook(bookId, bookListId, userId, type,scan);
				//修改书单封面
				try {
					String bkPic = "";
					//区分图书和网络小说
					if(CommentUtils.TYPE_BOOK.equals(type)){
						BkInfo bkInfo = bkFacade.findBkInfo((int)bookId);
						bkPic= bkInfo.getBookPic();
					}else if(CommentUtils.TYPE_NETBOOK.equals(type)){
						NetBook netBook = netBookFacade.findNetBookInfoById(bookId);
						bkPic = netBook.getPagePicUrl();
					}
					BookList bookList  =getResourceInfoFacade.queryByIdBookList(bookListId);
					String bookListPic = bookList.getCover();
					if(bkPic == null){
						bkPic = "";
					}
					if("".equals(bookListPic)){
						getResourceInfoFacade.updateBookListPic(bookListId, bkPic);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
			//修改平均分
		/*	if(level == CommentUtils.USER_LEVEL_TALENT){
				BkAvgMark avgMark = bkCommentFacade.updateBkExpertsAvgMark(id, Float.valueOf(score), Float.valueOf(beforeScore));
						//updateBkExpertsAvgMark(id, Float.valueOf(score), Float.valueOf(beforeScore));//(id, Float.valueOf(score));
			}else{*/
				//MvAvgMark avgMark = mvCommentFacade.updateMvAvgMark((int)id, Float.valueOf(score), Float.valueOf(beforeScore));
				BkAvgMark avgMark = bkCommentFacade.updateBkAvgMark(id, Float.valueOf(score), Float.valueOf(beforeScore));
			//}
			//最后修改时间
			long latestRevisionDate = bkComment.getLatestRevisionDate();
			vid =  ((latestRevisionDate - UKeyWorker.getTwepoch() << UKeyWorker.getTimestampshift()));
			/*System.out.println("书评修改后的虚拟id为"+vid);
			System.out.println("书评的id为"+bkComment.getId());
			System.out.println("当前的主键id为"+reskeyWork.getId());*/
			
		}
		flagint = bkComment.getFlag();
		//常江修改
		//如果是扫描的书，直接同时也加入收藏书单
		if("0".equals(scan)){
			getResourceInfoFacade.addBookToActivityList(CommentUtils.ACTIVITY_COLLECTED_BOOKLIST, uid, id, type,scan);
		}
		//添加逼格
		/*BkInfo bkInfo = bkFacade.findBkInfo(id);
		float big = bigFacade.getBookBigValue(bkInfo.getAuthorName(), bkInfo.getTags());
		BkComment bkComment2 = bkCommentFacade.addBkCommentBigValue(bkComment.getId(), big);
		
		UserBigInfo userBigInfo = bigUtils.getUserNowLevel(uid, big,TRUE, ucenterFacade, bigFacade);
		
		UserBigValue userBigValue = ucenterFacade.updateUserBigValue(uid, big, userBigInfo.getLevel());
		
		flagint = userBigValue.getFlag();*/
		//如果有@消息，将此消息存入表中，并推送给相关的人
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){

			if(isScore){
				//将修改后的书评放入到缓存中
				resourceManager.setResourceToJedis(bkComment, uid,uid,vid);

				//将附加信息加入缓存中
				resourceManager.updateInListByJedis((long)bkComment.getBookId(), uid, CommentUtils.TYPE_BOOK, "0");

				//更新用户的最后更新时间
				ucenterFacade.saveUserLatestInfo(uid, bkComment.getId(), CommentUtils.TYPE_BOOK_COMMENT);

				//插入话题
				oList = (List<Map<String, String>>) map.get("oList");
				if(null!=oList&&oList.size()>0){
					long rid = bkComment.getId();
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
								//添加@的数量
								userJedisManager.incrOneUserInfo(toUid, JedisConstant.USER_HASH_AT_NOTICE);
								json.put("uid", uid);
								json.put("toUid", toUid);
								json.put("rid", rid);
								json.put("type", CommentUtils.TYPE_BOOK_COMMENT);
								json.put("pushType", PushManager.PUSH_AT_TYPE);
								json.put("context", resultContent.replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
								json.toString();
								eagleProducer.send("pushMessage", "toBody", "", json.toString());
								//插入at信息
								actFacade.insertintoActAt(uid, rid, uid, CommentUtils.TYPE_BOOK_COMMENT, toUid,rid,CommentUtils.TYPE_BOOK_COMMENT);
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				//发表长书评，则增加对应书的阅读量
				if(add && CommentUtils.TYPE_ARTICLE_BOOK.equals(resourceType)){
					try{
						resStatJedisManager.addReadNum(id, type,0,"",0,50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}

			
			/*userAllInfo = ucenterFacade.findUserInfo(null, uid);
			String uname = userAllInfo.getName();
			
			//收集被act人的名字并推送消息
			List<String> actNames = WebUtils.getActNameList();
			for (String string : actNames) {
//				System.out.println("成功了:"+string);
				UserInfo userInfo = ucenterFacade.findUserInfoByName(string);
				String token = userInfo.getPushToken();
				
				MsgAt msgAt = msgFacade.doAct(null, uid, userInfo.getUserId(), bkComment.getId(), CommentUtils.TYPE_BOOK_COMMENT);
				
				flagint = msgAt.getFlag();
				if(flagint == ResultUtils.SUCCESS){
					MainSend mainSend = new MainSend();
					mainSend.sendMsgToStore(uname+CommentUtils.ACT_MESSAGE, token);
				}
			}*/
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("userBig", userBigInfo);
			
			
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

	//书评任务--新增书评的操作
	/*private void addQuestBkComment(Long uid){
		try{
			//需要判断是否是首次书评，是否达到每日任务，是否达到累计任务,需要加入小红点
			boolean questfinish = false;//是否有任务完成
			boolean dayquestfinish = false;//是否有每日任务完成
			//long bcid = bkCommentFacade.findBkCommentRecord(uid);
			QuestProgress dayquestProgress = questFacade.getQuestProgressByUseridAndType(uid, QuestInfo.type_day_bk);//每日的任务进程
			QuestProgress questProgress = questFacade.getQuestProgressByUseridAndType(uid, QuestInfo.type_bk);//累计任务进程
			
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
			//if(bcid==0){
				//是首次评论，需要打赏
				//Quest quest = questFacade.getQuest(QuestInfo.type_first_bk);
				//if(quest!=null && quest.getId()>0){
					//int totalFee = quest.getReward();//需要查询首次评论任务的打赏金额
					//paycenterManager.rewardUser(rewardUserId, rewardUserName, sourceId, sourceName, sourceType, totalFee);
					//questReward.setReward(questReward.getReward()+quest.getReward());
					//dayquestReward.setReward(dayquestReward.getReward()+quest.getReward());
				//}
			//}
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
				Quest quest = questFacade.getQuest(QuestInfo.type_day_bk);
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
				dayquestProgress.setType(QuestInfo.type_day_bk);
				dayquestProgress.setUpdatetime(System.currentTimeMillis());
				dayquestProgress.setUserid(uid);
				questFacade.insertQuestProgress(dayquestProgress);
			}
			
			if(questProgress!=null && questProgress.getId()>0){
				//存在累计任务进度，需要判断是否达到累计任务的最大值
				questProgress.setProgress(questProgress.getProgress()+1);
				//需要查询累计任务的信息（最大进度值，打赏金额）
				Quest quest = questFacade.getQuest(QuestInfo.type_bk);
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
				questProgress.setType(QuestInfo.type_bk);
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
	 * 删除书评
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delBookComment(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		long id = 0;
		long bookId = 0;
		String beforeScore = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			bookId = Long.valueOf(dataq.get("bookId").toString());
			beforeScore = (String) dataq.get("beforeScore");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		BkComment bkComment = bkCommentFacade.deleteMyCommentById(id);
		bkCommentFacade.deleteBkAvgMark((int) bookId, Float.valueOf("0.0"), Float.valueOf(beforeScore));
		
		flagint = bkComment.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			
			resourceManager.delResourceFromJedis(uid, bkComment.getId(), null);
			//话题相关删除
			topicFacade.deleteTopicLinkByResid(id, uid);
//			datas.put("id", bkComment.getId());
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
	 * 保存豆瓣书
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String saveValue(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
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
			return RES_DATA_NOTGET;
		}
		BkInfo bkInfo = fileUtils.putDataToBKInfo(dataq);
		
//		System.out.println(bkInfo);
		BkInfo bkInfo2 = new BkInfo();
		bkInfo2 = bkFacade.insertBkInfo(bkInfo);
		flagint = bkInfo2.getFlag();
		id = bkInfo2.getId();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", id);
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
	 * <p>Title: saveBookByUser</p> 
	 * <p>Description: 存储用户自己建立的书</p> 
	 * @author :changjiang
	 * date 2015-4-20 下午6:19:07
	 * @param reqs
	 * @return
	 */
	public String saveBookByUser(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
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
			return RES_DATA_NOTGET;
		}
		
		String isbn13 = CheckParams.objectToStr((String) dataq.get("isbn13"));
		String isbn10 = CheckParams.objectToStr((String) dataq.get("isbn10"));
		String image = CheckParams.objectToStr((String) dataq.get("image"));
		String title = CheckParams.objectToStr((String) dataq.get("title"));
		String publisher = CheckParams.objectToStr((String) dataq.get("publisher"));
		String pubdate = CheckParams.objectToStr((String) dataq.get("pubdate"));
		String pageStr = CheckParams.objectToStr(dataq.get("pages").toString().replaceAll("[^0-9]", ""));
		String binding = CheckParams.objectToStr((String) dataq.get("binding"));
		String summary = CheckParams.objectToStr((String) dataq.get("summary"));
		String author = CheckParams.objectToStr(dataq.get("author"));
		String catalog = CheckParams.objectToStr((String) dataq.get("catalog"));
		String price = CheckParams.objectToStr((String) dataq.get("price"));
		
		MyBk myBk = new MyBk();
		myBk.setAuthorName(author);
		
		myBkFacade.addMyBkInfo(title, pageStr, catalog, isbn13, image, binding, pubdate, summary, publisher, price, author, isbn10, uid);
		/*BkInfo bkInfo = new BkInfo();
		bkInfo = fileUtils.putDataToBKInfo(dataq);*/
		
		//MyBk myBk = null;
		//用户自己建立的书插入库中
		
//		System.out.println(bkInfo);
		/*BkInfo bkInfo2 = new BkInfo();
		bkInfo2 = bkFacade.insertBkInfo(bkInfo);*/
		//flagint = 0;//myBkFacade.addMyBkInfo(bkInfo.getName(), bkInfo.getContent(), uid);
		//id = bkInfo2.getId();
		datas = new HashMap<String, Object>();
		//if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", id);
		/*}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}*/
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 写书摘
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeBookTalk(String reqs,long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		int bookId = 0;
		List content = new ArrayList<Map<String,String>>();
		int page = 0;
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			bookId = Integer.valueOf(dataq.get("bookId").toString());
			page = Integer.valueOf(dataq.get("page").toString());
			content = (List) dataq.get("list");
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = CommentUtils.TYPE_BOOK;
			}
			if(type == null || "".equals(type)){
				type = CommentUtils.TYPE_BOOK;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		long id = bookTalkFacade.addBookTalk(bookId, uid, page, WebUtils.putDataToHTML5(content),type);
		
		if(id == ResultUtils.ERROR){
			flagint = ResultUtils.ERROR;
		}
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", id);
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
	 * 书摘列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBookTalkList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String type = "";
		Long id = null;
		
		int bookId = 0;
		int page = 0;
		Long userId = null;
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			bookId = Integer.valueOf(dataq.get("bookId").toString());
			page = Integer.valueOf(dataq.get("page").toString());
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
				type = CommentUtils.TYPE_BOOK;
			}
			if(type == null || "".equals(type)){
				type = CommentUtils.TYPE_BOOK;
			}
			try {
				id = Long.valueOf(dataq.get("id").toString());
				if(id == 0){
					id = null;
				}
			} catch (Exception e) {
				id = null;
			}
			
			try{
				userId = Long.valueOf(dataq.get("userId").toString());
				if(0==userId.longValue()){
					userId = null;
				}
			}catch (Exception e) {
				userId = null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		List<BookTalk> bookTalks = new ArrayList<BookTalk>();
		if(page == UNID){
//			bookTalks = bookTalkFacade.findBookTalkList(bookId, null,type);
			bookTalks = bookTalkFacade.findBookTalkListByPageAndId(bookId, null, null, type,userId);
		}else{
//			if(1==page){//常江修改
//				page = 0;
//			}
//			bookTalks = bookTalkFacade.findBookTalkList(bookId, page,type);
			bookTalks = bookTalkFacade.findBookTalkListByPageAndId(bookId, page, id, type,userId);
		}
		
		List<BookTalkInfo> bookTalkInfos = new ArrayList<BookTalkInfo>();
		
		if(bookTalks != null && bookTalks.size() >15){
			bookTalks.subList(0, 15);
		}
		int size =0;
		if(bookTalks != null){
			size = bookTalks.size();
		}
		bookTalkInfos = bookUtils.putBookTalkToBookTalkInfo(bookTalks,ucenterFacade);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", bookTalkInfos);
			datas.put("size", size);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: viewBookTalkListByUserId</p> 
	 * <p>Description: 查询用户自己的书摘列表</p> 
	 * @author :changjiang
	 * date 2015-4-10 下午5:27:33
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewBookTalkListByUserId(String reqs,long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq= new HashMap<String, Object>();
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		Long lastId = 0l;
		//去掉空格
		reqs = reqs.trim();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		try{
			lastId = Long.valueOf(dataq.get("lastId").toString());
			if(0==lastId.longValue()){
				lastId = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			lastId=null;
		}
		List<BookTalkListInfo> bkTalkList = new ArrayList<BookTalkListInfo>();
		//用户的书摘列表
		List<BookTalk> bookTalkList = bookTalkFacade.findBookTalkListByUserId(uid, lastId);
		Iterator<BookTalk> bookTalkIt = bookTalkList.iterator();
		BkInfo bkInfo = new BkInfo();
		NetBook netBook = new NetBook();
		String resType = "";
		BookTalk bookTalk = new BookTalk();
		while(bookTalkIt.hasNext()){
			bookTalk= bookTalkIt.next();
			resType = bookTalk.getResType();
			if(CommentUtils.TYPE_BOOK.equals(resType)){
				bkInfo = bkFacade.findBkInfo(bookTalk.getBookId());
				if(bkInfo.getId()==0){
					continue;
				}
				BookTalkListInfo bkTalkListInfo = new BookTalkListInfo();
				bkTalkListInfo.setBookTalkId(bookTalk.getId());
				bkTalkListInfo.setBookId(bkInfo.getId());
				bkTalkListInfo.setBookName(bkInfo.getName());
				bkTalkListInfo.setBookPic(bkInfo.getBookPic());
				bkTalkListInfo.setAuthorName(bkInfo.getAuthorName());
				bkTalkListInfo.setPress(bkInfo.getPress());
				bkTalkListInfo.setScore(bkInfo.getScore());
				bkTalkListInfo.setPublishTime(bkInfo.getPublishingTime());
				bkTalkListInfo.setType(bookTalk.getResType());
				Map<String, Object> countMap = bookTalkFacade.findUserOneBookTalkCount(uid, bkInfo.getId());
				int countFlag = (Integer) countMap.get("flag");
				if(countFlag==ResultUtils.SUCCESS){
					int count = (Integer) countMap.get("count");
					bkTalkListInfo.setPages(count+"");
				}
				//bkTalkListInfo.setPages(pages);
				bkTalkList.add(bkTalkListInfo);
			}else if(CommentUtils.TYPE_NETBOOK.equals(resType)){
				netBook = netBookFacade.findNetBookInfoById(bookTalk.getBookId());
				if(netBook.getId()==0){
					continue;
				}
				BookTalkListInfo bkTalkListInfo = new BookTalkListInfo();
				bkTalkListInfo.setBookTalkId(bookTalk.getId());
				bkTalkListInfo.setBookId(netBook.getId());
				bkTalkListInfo.setBookPic(netBook.getPagePic());
				bkTalkListInfo.setAuthorName(netBook.getAuthorName());
				bkTalkListInfo.setType(bookTalk.getResType());
				Map<String, Object> countMap = bookTalkFacade.findUserOneBookTalkCount(uid, bkInfo.getId());
				int countFlag = (Integer) countMap.get("flag");
				if(countFlag==ResultUtils.SUCCESS){
					int count = (Integer) countMap.get("count");
					bkTalkListInfo.setPages(count+"");
				}
				//bkTalkListInfo.setPages(pages);
				bkTalkList.add(bkTalkListInfo);
			}
		}
		datas = new HashMap<String, Object>();
		if(null!=bkTalkList){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", bkTalkList);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	public String delBookTalk(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		String bookTalkIdArray = "";
		//去掉空格
		reqs = reqs.trim();
		
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			bookTalkIdArray = dataq.get("bookTalkIdArray").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		try {
			List<Long> list =  getObjectMapper().readValue(bookTalkIdArray, List.class);
			for(int i=0;i<list.size();i++){
				Long bookTalkStr = list.get(i);
				if(null!=bookTalkStr&&0!=bookTalkStr.longValue()){
					bookTalkFacade.delBookTalk(bookTalkStr);
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		datas = new HashMap<String, Object>();
	/*	if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}*/
		datas.put("flag", CommentUtils.RES_FLAG_SUCCESS);
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
	public List<BookInfo> getBKResponseList(List reqList , String type , List<BookInfo> resList){
		BookInfo bookInfo = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (Object obj : reqList) {
				bookInfo = fileUtils.putBKToBookInfo(obj, TRUE);
				resList.add(bookInfo);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			String objName = reqList.get(0).getClass().getName();
			if(BkInfo.class.getName().equals(objName)){
				//库中
				BkInfo obj = (BkInfo) reqList.get(0);
				flagint = obj.getFlag();
				if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
					long id = obj.getId();
					if(id != 0){
						bookInfo = fileUtils.putBKToBookInfo(obj, TRUE);
						resList.add(bookInfo);
					}
				}
			}else if(MyBk.class.getName().equals(objName)){
				//我的库中
				MyBk obj = (MyBk) reqList.get(0);
				flagint = obj.getFlag();
				if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
					long id = obj.getId();
					if(id != 0){
						bookInfo = fileUtils.putBKToBookInfo(obj, TRUE);
						resList.add(bookInfo);
					}
				}
				
			}
		}
		return resList;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getCommentResponseList(List<BkComment> reqList , String type ,Long uid, List<ResourceInfo> resList){
		ResourceInfo commentInfo = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (BkComment obj : reqList) {
				commentInfo = fileUtils.putObjectToResource(obj, ucenterFacade,  bkFacade);
				try {
					actUtils.putIsCollectToResoure(commentInfo, uid, actFacade,0);
				} catch (Exception e) {
					LOG.error("添加附加内容时出错，资源类型["+commentInfo.getType()+"]id为:"+commentInfo.getRid()+e.getMessage(), e.fillInStackTrace());
				}
				if(commentInfo != null && commentInfo.getRid() != 0){
					resList.add(commentInfo);
				}
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			BkComment obj = reqList.get(0);
			flagint = obj.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				long id = obj.getId();
				if(id != 0){
					commentInfo = fileUtils.putObjectToResource(obj, ucenterFacade, bkFacade);
					try {
						actUtils.putIsCollectToResoure(commentInfo, uid, actFacade,0);
					} catch (Exception e) {
						LOG.error("添加附加内容时出错，资源类型["+commentInfo.getType()+"]id为:"+commentInfo.getRid()+e.getMessage(), e.fillInStackTrace());
					}
					if(commentInfo != null && commentInfo.getRid() != 0){
						resList.add(commentInfo);
					}
				}
			}
		}
		return resList;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}
	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}
	public void setBookTalkFacade(BookTalkFacade bookTalkFacade) {
		this.bookTalkFacade = bookTalkFacade;
	}
	public void setBkOnlineReadFacade(BkOnlineReadFacade bkOnlineReadFacade) {
		this.bkOnlineReadFacade = bkOnlineReadFacade;
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
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
	/*public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}*/
	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
	
	
}
