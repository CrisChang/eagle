package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.swing.Box.Filler;

import com.poison.eagle.utils.*;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPublish;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.CommentInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.ext.constant.MemcacheResourceLinkConstant;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.Serialize;
import com.poison.store.client.BkFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.NetBook;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
import com.poison.ucenter.model.UserStatistics;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class BookListManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(BookListManager.class);
	private int flagint;
	
	
	private BkFacade bkFacade;
	private BkCommentFacade bkCommentFacade;
	private UcenterFacade ucenterFacade;
	private GetResourceInfoFacade getResourceInfoFacade;
	private MyBkFacade myBkFacade;
	private ActFacade actFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private PaycenterFacade paycenterFacade;
	private NetBookFacade netBookFacade;
	
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private BookUtils bookUtils = BookUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	
	private ResourceManager resourceManager;
	
	private MemcachedClient operationMemcachedClient;

	private UserJedisManager userJedisManager;
	
//	private long uid = CommentUtils.UID_YINNAN;
	
	
	/**
	 * 热搜书
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewHotsearchBook(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
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
//		System.out.println(req);
		
		List<BookInfo> resourceList = new ArrayList<BookInfo>();
		
//		//热搜书
		resourceList = getHotBookList();
		
//		//即将上映
//		bookListLinks = getResourceInfoFacade.findBookListInfo(39933854080958464l, null);
//		resourceList = getBookListResponseList(bookListLinks,null, resourceList);
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceList);
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
	 * 获取热搜书单的书
	 * @return
	 */
	public List<BookInfo> getHotBookList(){
		List<BookListLink> bookListLinks = new ArrayList<BookListLink>();
		List<BookInfo> resourceList = new ArrayList<BookInfo>();
		bookListLinks = getResourceInfoFacade.findBookListInfo(CommentUtils.WEB_PUBLIC_HOT_BOOKLIST_ID, null,null);
		resourceList = getBookListResponseList(bookListLinks,null, resourceList);
		return resourceList;
	}
	/**
	 * 广场书单列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewPublicBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String tag ="";
		Long lastId = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			tag = (String) dataq.get("tag");
			lastId = Long.valueOf(dataq.get("lastId").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		if(lastId == UNID){
			lastId = null;
		}
		
		List<BookList> bookLists = getResourceInfoFacade.findServerBookLists(tag, lastId);
		
		List<BookListInfo> bookListInfos = new ArrayList<BookListInfo>();
		
		//书单列表
		bookListInfos = getResponseList(bookLists,uid, bookListInfos);
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", bookListInfos);
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
	 * 我的书单列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		long startTime = System.currentTimeMillis();
		List<BookListInfo> bookListInfos = new ArrayList<BookListInfo>();
		
		bookListInfos = getMyBookList(id);
		long endTime = System.currentTimeMillis();
		System.out.println("个人书单所用时间为"+(endTime-startTime));
		
		/*List<ResourceInfo> collects = new ArrayList<ResourceInfo>();
		//收藏的书单
		List<ActCollect> actCollects = actFacade.findUserCollectList(null, id, CommentUtils.TYPE_BOOKLIST);
		long endTime1 = System.currentTimeMillis();
		System.out.println("调用收藏书单用时"+(endTime1-endTime));
		
		collects = resourceManager.getResponseList(actCollects, uid, collects);
		long endTime2 = System.currentTimeMillis();
		System.out.println("组装收藏书单用时"+(endTime2-endTime1));
		Collections.sort(collects);
		long finalTime = System.currentTimeMillis();
		System.out.println("排序用时时间为"+(finalTime-endTime2));*/
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", bookListInfos);
			//datas.put("collects", collects);
			
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
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
	 * 
	 * <p>Title: viewMyCollectedBookList</p> 
	 * <p>Description: 查看我收藏的书单</p> 
	 * @author :changjiang
	 * date 2015-4-22 下午2:09:52
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewMyCollectedBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		/*long startTime = System.currentTimeMillis();
		List<BookListInfo> bookListInfos = new ArrayList<BookListInfo>();
		
		bookListInfos = getMyBookList(id);
		System.out.println("个人书单所用时间为"+(endTime-startTime));*/
		long startTime = System.currentTimeMillis();
		List<ResourceInfo> collects = new ArrayList<ResourceInfo>();
		//收藏的书单
		List<ActCollect> actCollects = actFacade.findUserCollectList(null, id, CommentUtils.TYPE_BOOKLIST);
		/*long endTime1 = System.currentTimeMillis();
		System.out.println("调用收藏书单用时"+(endTime1-endTime));*/
		
		collects = resourceManager.getResponseList(actCollects, uid, collects);
		/*long endTime2 = System.currentTimeMillis();
		System.out.println("组装收藏书单用时"+(endTime2-endTime1));*/
		Collections.sort(collects);
		long endTime = System.currentTimeMillis();
		System.out.println("调用个人收藏的书单用时"+(endTime-startTime));
	/*	long finalTime = System.currentTimeMillis();
		System.out.println("排序用时时间为"+(finalTime-endTime2));*/
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("list", bookListInfos);
			datas.put("collects", collects);
			
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
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
	 * 获取我的书单列表公共方法
	 * @param id
	 * @return
	 */
	public List<BookListInfo> getMyBookList(long id){
		List<BookListInfo> bookListInfos = new ArrayList<BookListInfo>();
		List<BookList> bookLists = getResourceInfoFacade.findBookList(null, id);
		//书单列表
		bookListInfos = getMyBookListToListInfos(bookLists, id, bookListInfos);
				//getResponseList(bookLists,id, bookListInfos);
		
		if(bookListInfos == null ){
			bookListInfos = new ArrayList<BookListInfo>();
		}
//		//收藏的书单
//		List<ActCollect> actCollects = actFacade.findUserCollectList(null, id, CommentUtils.TYPE_BOOKLIST);
//		List<BookListInfo> bookListInfos2 = new ArrayList<BookListInfo>();
//		bookListInfos2 = getBookList(actCollects,id,bookListInfos2);
//		
//		
//		bookListInfos.addAll(bookListInfos2);
		if(bookListInfos == null ){
			bookListInfos = new ArrayList<BookListInfo>();
		}
		//按数量倒序
		Collections.sort(bookListInfos);
		return bookListInfos;
	}
	/**
	 * 单个书单的内容
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewOneBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		String type = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			type = dataq.get("type").toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		
		
		List<BookListLink> bookListLinks = new ArrayList<BookListLink>();
		List<BookInfo> resourceList = new ArrayList<BookInfo>();
		
		if("".equals(type)){
			bookListLinks = getResourceInfoFacade.findBookListInfo(id, null,null);
		}else{
			bookListLinks = getResourceInfoFacade.findBookListByType(id, type);
		}
		
		Collections.reverse(bookListLinks);//正序
		resourceList = getBookListResponseList(bookListLinks,uid, resourceList);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceList);
			
			//从缓存中获取书单被收藏的数量
			int count =0;
			try {
				count = resourceManager.getResourceCollectCountFromJedis((long)id, CommentUtils.TYPE_MOVIE);
			} catch (Exception e) {
				count =0;
				LOG.error("获取电影被收藏数量出错，id为："+id+e.getMessage(), e.fillInStackTrace());
			}
			datas.put("collectCount", count);
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
	 * 单个书单的内容{xin}
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewNewOneBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		Long lastId = 0l;
		Integer page = 0;
		int size = 0;
		String web = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			web = dataq.get("web")+"";//判断是否是web分享页查询,如果是，则查询书单中所有的书
			try {
				lastId = Long.valueOf(dataq.get("lastId").toString());
				if(lastId == 0){
					lastId =null;
				}
			} catch (Exception e) {
				lastId = null;
		 }
			try {
				page = Integer.valueOf(dataq.get("page").toString());
				if(page == null){
					page =0;
				}
			} catch (Exception e) {
				page = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);

		BookListInfo bookListInfo = new BookListInfo();
		List<BookListLink> bookListLinks = new ArrayList<BookListLink>();
		List<ResourceInfo> resourceList = new ArrayList<ResourceInfo>();
		
//		Map<String, Object> memcacheMap = new HashMap<String, Object>();

		long begin = System.currentTimeMillis();
		try {
			//System.out.println("页码："+page);
			//书单详情
			BookList bookList =getResourceInfoFacade.queryByIdBookList(id);
//			System.out.println("书单"+bookList);
			bookListInfo =fileUtils.putObjToBookListInfo(bookList,actFacade, getResourceInfoFacade, bkFacade, ucenterFacade,myBkFacade,netBookFacade,uid);
			fileUtils.putMoneyToBookList(bookListInfo, paycenterFacade);
//			memcacheMap = operationMemcachedClient.get(MemcacheResourceLinkConstant.MOVIE_BOOK_LIST_TYPE+id+"_"+CommentUtils.TYPE_BOOKLIST);
			resourceList = null;//operationMemcachedClient.get(MemcacheResourceLinkConstant.MOVIE_BOOK_LIST_TYPE+id+"_"+CommentUtils.TYPE_BOOKLIST+"_"+page);
			if(null == resourceList  || resourceList.size() == 0){
				//System.out.println("缓冲没有数据");
				resourceList = new ArrayList<ResourceInfo>();
				//书单下书的列表
				if("web".equals(web)){
					//web分享页面查询所有的
					bookListLinks = getResourceInfoFacade.findBookListInfo(id, null,null);
				}else{
					bookListLinks = getResourceInfoFacade.findBookListInfo(id, lastId,CommentUtils.BOOK_MOVIE_LIST_SIZE);
				}
				if(bookListLinks != null){
					size = bookListLinks.size();
					//System.out.println("书单的书的数量："+size);
				}
				//分页
				//bookListLinks = CheckParams.getListByPage(bookListLinks, page, CommentUtils.BOOK_MOVIE_LIST_SIZE);
				//System.out.println("["+page+"]页的数量"+bookListLinks.size());
//		Collections.reverse(bookListLinks);//正序
				int type = bookList.getType();
				resourceList = getOneBkListResponseList(bookListLinks,uid, resourceList,type,bookList);
				//判断是否加入缓存
				//memcacheMap = new HashMap<String, Object>();
				//memcacheMap.put("bookListInfo", bookListInfo);
				//memcacheMap.put("resourceList", resourceList);
				if(!"web".equals(web)){
					if(type == 2){//要修改
						operationMemcachedClient.set(MemcacheResourceLinkConstant.MOVIE_BOOK_LIST_TYPE+id+"_"+CommentUtils.TYPE_BOOKLIST+"_"+page,MemcacheResourceLinkConstant.TIME_INTERVALS_60_60,resourceList);
					}
				}
			}
			flagint = ResultUtils.SUCCESS;
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		//long end = System.currentTimeMillis();
		//System.out.println("书单详情耗时："+(end-begin));
		//LOG.info("书单详情耗时："+(end-begin));
		
		
		
		
		
//		String tags = "小说、文学、随笔、散文、诗歌、杂文、名著、漫画、推理、青春、悬疑、科幻、武侠、网络小说、言情、历史、心理、哲学、传记、社会、文化、电影、宗教、艺术、音乐、爱情、旅行、生活、励志、女性、摄影、两性、经济、管理、金融、商业、投资、广告股票、理财、创业、互联网、编程、科学";
//		String[] tagss = tags.split("、");
//		List<String> resTags = new ArrayList<String>();
//		
//		for (ResourceInfo ri : resourceList) {
//			String resTag = ri.getBookInfo().getTags();
//			for (String t : tagss) {
//				if(resTag.indexOf(t)>0){
//					resTags.add(t);
//				}
//			}
//		}
//		
//		for (int i = 0; i < resTags.size() - 1; i++) {
//			for (int j = resTags.size() - 1; j > i; j--) {
//				if (resTags.get(j).equals(resTags.get(i))) {
//					resTags.remove(j);
//				}
//			}
//		}
		
//		System.out.println(resTags);
		
		
		//bookListInfo = (BookListInfo) memcacheMap.get("bookListInfo");
		
		datas = new HashMap<String, Object>();
		//加入有用没用的数量

		Map<String, Object> usefulMap = new HashMap<String, Object>();
		usefulMap = actFacade.findUsefulCount(id);
		int usefulCount = (Integer) usefulMap.get("usefulCount");
		Map<String, Object> uselessMap = new HashMap<String, Object>();
		uselessMap = actFacade.findUselessCount(id);
		int uselessCount = (Integer) uselessMap.get("uselessCount");

		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceList);
			datas.put("size", size);
			bookListInfo.setUsefulCount(usefulCount);
			bookListInfo.setUselessCount(uselessCount);
			//判断对当前用户是否关注
			long userId = bookListInfo.getUserEntity().getId();
			String inList = userJedisManager.getRelationUserAttentionInfo(uid,userId, JedisConstant.RELATION_USER_ISINTEREST);
			if(null==inList){
				inList = "2";
			}
			bookListInfo.getUserEntity().setIsInterest(Integer.valueOf(inList));
//			datas.put("usefulCount",usefulCount);
//			datas.put("uselessCount",uselessCount);
			if(page == 0 || page == 1){
				datas.put("bookListInfo", bookListInfo);
			}
			
			//从缓存中获取电影被收藏的数量
			int count =0;
			try {
				count = resourceManager.getResourceCollectCountFromJedis((long)bookListInfo.getId(), CommentUtils.TYPE_BOOKLIST);
			} catch (Exception e) {
				count =0;
				LOG.error("获取电影被收藏数量出错，id为："+bookListInfo.getId()+e.getMessage(), e.fillInStackTrace());
			}
			datas.put("collectCount", count);
			//datas.put("tagList", resTags);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
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
	 * 单个书的评论
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewOneBookComment(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int bookId = 0;
//		int isDB = 0;
		String type = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			bookId = Integer.valueOf(dataq.get("bookId").toString());
			//isDB = Integer.valueOf(dataq.get("isDB").toString());
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
		
		String resourceType = CommentUtils.TYPE_BOOK_COMMENT;
		List<BkComment> bkComments = new ArrayList<BkComment>();
			bkComments = bkCommentFacade.findAllBkComment(bookId, UNID, type,resourceType);//(bookId, UNID);
		
		List<ResourceInfo> resourceList = new ArrayList<ResourceInfo>();
		resourceList = getCommentResponseList(bkComments, null, resourceList);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceList);
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
	 * 创建书单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String createBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String reason = "";
		long id = 0;
		String name;
		List<String> tags = new ArrayList<String>();
		String bookListPic = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			name = (String) dataq.get("name");
			reason = (String) dataq.get("reason");
			tags = (List<String>) dataq.get("tags");
			bookListPic = (String) dataq.get("bookListPic");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		if(id == UNID){
			flagint = getResourceInfoFacade.addNewBookList(null, name, reason, uid, "");
		}else{
			flagint = getResourceInfoFacade.updateBookList(id, name, reason, bookListPic, CheckParams.putListToString(tags));
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
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
	 * 创建书单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String createPublicBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String name="";
		String reason="";
		//去掉空格
		reqs = reqs.trim();
		String tag = "";
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			name = (String) dataq.get("name");
			reason = (String) dataq.get("reason");
			tag = (String) dataq.get("tag");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		
		flagint = getResourceInfoFacade.addServerBookList(name, reason, tag);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
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
	 * 书单中添加内容
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int isDB = 0;
		int bookId = 0;
		String name= "";
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		String scan = "";
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			//isDB = Integer.valueOf(dataq.get("isDB").toString());
			bookId = Integer.valueOf(dataq.get("bookId").toString());
			scan = (String) dataq.get("scan");
			if(null==scan||"".equals(scan)){
				scan = "1";
			}
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
		
		if(type == null || "".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		
		long bookListId = Long.valueOf(dataq.get("bookListId").toString());
		
		BookListLink bookListLink = getResourceInfoFacade.moveOneBook(bookId, bookListId, uid, type,scan);//(bookId, bookListId, uid);
		flagint = bookListLink.getFlag();
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将附加信息加入缓存中
			resourceManager.updateInListByJedis((long)bookId, uid, CommentUtils.TYPE_BOOK, "0");
			
			//修改书单封面
			try {
				String bkPic = "";
				//区分图书和网络小说
				if(CommentUtils.TYPE_BOOK.equals(type)){
					BkInfo bkInfo = bkFacade.findBkInfo(bookId);
					bkPic= bkInfo.getBookPic();
				}else if(CommentUtils.TYPE_NETBOOK.equals(type)){
					NetBook netBook = netBookFacade.findNetBookInfoById(bookId);
					bkPic = netBook.getPagePicUrl();
				}
				BookList bookList = getResourceInfoFacade.queryByIdBookList(bookListId);
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
	 * 修改书单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String updateBookList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		String name;
		String reason = "";
		List<String> tags = new ArrayList<String>();
		String bookListPic = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			name = (String) dataq.get("name");
			id = Long.valueOf(dataq.get("id").toString());
			reason = (String) dataq.get("reason");
			bookListPic = (String) dataq.get("bookListPic");
			tags = (List<String>) dataq.get("tags");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
	
		flagint = getResourceInfoFacade.updateBookList(id, name, reason, bookListPic, CheckParams.putListToString(tags));
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
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
	 * 修改书单中书的属性
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String updateBookListLinkAttribute(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		List<String> tags = new ArrayList<String>();
		String friendName = "";
		String address = "";
		String remark = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			friendName = (String) dataq.get("friendName");
			id = Long.valueOf(dataq.get("id").toString());
			address = (String) dataq.get("address");
			remark = (String) dataq.get("summary");
			tags = (List<String>) dataq.get("tags");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		BookListLink bookListLink = getResourceInfoFacade.updateBookListLinkRemark(friendName, address, CheckParams.putListToString(tags), id, remark);//(friendName, address, CheckParams.putListToString(tags), id);
		
		flagint = bookListLink.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS||flagint == UNID){
			//将用户修改的标签放入到缓存中
			resourceManager.setUserTagToJedis(uid, tags);
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
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
	 * 显示书单中书的属性
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBookListLinkAttribute(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		Map<String, Object> remark = new HashMap<String, Object>();
		List<String> tags = new ArrayList<String>();
		
		BookListLink bookListLink =  getResourceInfoFacade.findBookLinkIsExistById(id);
		remark.put("id", id);
		remark.put("friendName", bookListLink.getFriendinfo());
		remark.put("tags", CheckParams.putStringToList(bookListLink.getTags()));
		remark.put("address", bookListLink.getAddress());
		remark.put("description", bookListLink.getDescription());
		
		
		flagint = bookListLink.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS||flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("remarkInfo", remark);
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
	 * 删除书单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delBookList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		
		flagint = getResourceInfoFacade.deleteBookList(id);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
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
	 * 推书单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String publishBookList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		long id = 0;
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
//			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		
		ActPublish actPublish = actFacade.addOnePublishInfo(uid, id, CommentUtils.TYPE_BOOKLIST);
		flagint = actPublish.getFlag();

		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flagint = getResourceInfoFacade.publishBookList(id);
		}
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将生成的推送书单放入到缓存中
			resourceManager.setResourceToJedis(actPublish, uid,uid,0l);
			
			//增加用户推书单的数量
			try {
				UserStatistics userStatistics =userStatisticsFacade.updateBklistCount(uid);
			} catch (Exception e) {
				LOG.error("增加用户推书单数量失败:"+e.getMessage(), e.fillInStackTrace());
			}
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
	 * 移动书单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String moveBookList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int isDB = 0;
		int bookId = 0;
		long bookListId = 0;
		long nowBookListId = 0;
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			nowBookListId = Long.valueOf(dataq.get("nowBookListId").toString());
			bookListId = Long.valueOf(dataq.get("bookListId").toString());
			bookId = Integer.valueOf(dataq.get("bookId").toString());
			//isDB = Integer.valueOf(dataq.get("isDB").toString());
			try {
				type  = (String) dataq.get("type");
				
			} catch (Exception e) {
				type = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		if(type == null || "".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}

		
		flagint = getResourceInfoFacade.addBookToList(nowBookListId, bookId, 0, type);//(nowBookListId, bookId, isDB);
		if(flagint==ResultUtils.SUCCESS){
			flagint = getResourceInfoFacade.deleteBookListLink(bookListId, bookId, type);//(bookListId, bookId);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
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
	 * 删除书单内容
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delOneBook(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int bookId = 0;
		long bookListId = 0;
		String type = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			bookListId = Long.valueOf(dataq.get("bookListId").toString());
			bookId = Integer.valueOf(dataq.get("bookId").toString());
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
		
		if(type == null || "".equals(type)){
			type = CommentUtils.TYPE_BOOK;
		}
		
		
		flagint = getResourceInfoFacade.deleteBookListLink(bookListId, bookId, type);//(bookListId, bookId);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将附加信息加入缓存中
			resourceManager.updateInListByJedis((long)bookId, uid, CommentUtils.TYPE_BOOK, "1");
			
			//书单中删除一本书的时候，对封面进行修改
			try {
				String bkPic = "";
				//区分图书和网络小说
				if(CommentUtils.TYPE_BOOK.equals(type)){
					BkInfo bkInfo = bkFacade.findBkInfo(bookId);
					bkPic= bkInfo.getBookPic();
				}else if(CommentUtils.TYPE_NETBOOK.equals(type)){
					NetBook netBook = netBookFacade.findNetBookInfoById(bookId);
					bkPic = netBook.getPagePicUrl();
				}
				BookList bookList = getResourceInfoFacade.queryByIdBookList(bookListId);
				String bookListPic = bookList.getCover();
				if(bkPic == null){
					bkPic = "";
				}
				if(bookListPic.equals(bkPic) || "".equals(bookListPic)){
					List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(bookListId, null,null);
					if(bookListLinks != null && bookListLinks.size()>0){
						//区分图书和网络小说
						try {
							if(CommentUtils.TYPE_BOOK.equals(bookListLinks.get(0).getResType())){
								BkInfo bkInfo = bkFacade.findBkInfo(bookListLinks.get(0).getBookId());
								bkPic= bkInfo.getBookPic();
							}else if(CommentUtils.TYPE_NETBOOK.equals(bookListLinks.get(0).getResType())){
								NetBook netBook = netBookFacade.findNetBookInfoById(bookListLinks.get(0).getBookId());
								bkPic = netBook.getPagePicUrl();
							}
						} catch (Exception e) {
							bkPic = "";
							LOG.error(e.getMessage(), e.fillInStackTrace());
						}
						getResourceInfoFacade.updateBookListPic(bookListId, bkPic);
					}else {
						getResourceInfoFacade.updateBookListPic(bookListId, "");
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
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
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<BookListInfo> getResponseList(List<BookList> reqList ,Long uid, List<BookListInfo> resList){
		BookListInfo bookListInfo = null;
		if(reqList.size()>0){
			BookList object = reqList.get(0);
			if(object.getId() != UNID){
				flagint = ResultUtils.SUCCESS;
				for (BookList obj : reqList) {
					//新方法
					bookListInfo = fileUtils.putObjToBookListInfo(obj,actFacade,getResourceInfoFacade,bkFacade,ucenterFacade,myBkFacade,netBookFacade,uid);
					if(bookListInfo.getId() != 0){
						resList.add(bookListInfo);
					}
				}
			}else{
				flagint = ResultUtils.DATAISNULL;
			}
			
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	
	/**
	 * 
	 * <p>Title: getMyBookListToListInfos</p> 
	 * <p>Description: 将我的书单转换为resourceinfos</p> 
	 * @author :changjiang
	 * date 2015-5-12 下午12:42:19
	 * @param reqList
	 * @param uid
	 * @param resList
	 * @return
	 */
	public List<BookListInfo> getMyBookListToListInfos(List<BookList> reqList ,Long uid, List<BookListInfo> resList){
		BookListInfo bookListInfo = null;
		if(null!=reqList&&reqList.size()>0){
			UserEntity userEntity = new UserEntity();
			if(ucenterFacade != null){
				UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
				userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);
			}
			Iterator<BookList> bookListIt = reqList.iterator();
			flagint = ResultUtils.SUCCESS;
			while(bookListIt.hasNext()){
				BookList BookList = bookListIt.next();
				//新方法
				bookListInfo = fileUtils.putBookListToBookListInfo(BookList, actFacade, getResourceInfoFacade, bkFacade, ucenterFacade, myBkFacade, netBookFacade, uid);
						//putObjToBookListInfo(obj,actFacade,getResourceInfoFacade,bkFacade,ucenterFacade,myBkFacade,netBookFacade,uid);
				if(bookListInfo.getId() != 0){
					bookListInfo.setUserEntity(userEntity);
					resList.add(bookListInfo);
				}
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<BookListInfo> getBookList(List<ActCollect> reqList ,Long uid, List<BookListInfo> resList){
		BookListInfo bookInfo = null;
		if(reqList.size()>0){
			ActCollect object = reqList.get(0);
			if(object.getId() != UNID){
				flagint = ResultUtils.SUCCESS;
				for (ActCollect obj : reqList) {
					bookInfo = actUtils.putCollectBookListToInfo(obj, ucenterFacade, actFacade, getResourceInfoFacade, bkFacade,myBkFacade,netBookFacade,uid);
					resList.add(bookInfo);
				}
			}else{
				flagint = ResultUtils.DATAISNULL;
			}
			
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
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
		BkInfo bkInfo = new BkInfo();
		if(reqList.size()>0){
			if(reqList.get(0).getId() != 0){
				flagint = ResultUtils.SUCCESS;
				for (BookListLink obj : reqList) {
					/*if(CommentUtils.type_list2 == type){
						ri = bookUtils.putBkListLinkToResource(obj, ucenterFacade, actFacade, bkFacade, bkCommentFacade,getResourceInfoFacade,netBookFacade);
						BkAvgMark bkAvgMark = bkCommentFacade.findBkAvgMarkByBkId(obj.getBookId());
						ri.setScore(String.valueOf(bkAvgMark.getBkAvgMark()));
						resList.add(ri);
					}*///if(CommentUtils.type_list2 == type){
//						List<BkComment> bkComments = bkCommentFacade.findAllBkComment(obj.getBookId(), null);
//						List<BkComment> bkComments = bkCommentFacade.findMyBkCommentList(bookList.getuId(), obj.getBookId(), null, obj.getResType());
//						if(bkComments.size()>0 && bkComments.get(0).getId() != 0){
//							ri = fileUtils.putObjectToResource(bkComments.get(0), ucenterFacade, actFacade, bkFacade,myBkFacade, getResourceInfoFacade, netBookFacade, uid);
//							resList.add(ri);
//						}else{
							ri = bookUtils.putBkListLinkToResource(obj, ucenterFacade, actFacade, bkFacade, bkCommentFacade,getResourceInfoFacade,netBookFacade,myBkFacade,bookList,uid);
//							BkAvgMark bkAvgMark = bkCommentFacade.findBkAvgMarkByBkId(obj.getBookId());
//							ri.setScore(String.valueOf(bkAvgMark.getBkAvgMark()));
							if(ri.getRid() != 0){
								resList.add(ri);
							}
//						}
					//}
//				BookList bookList = getResourceInfoFacade.queryByIdBookList(obj.getBookListId());
					
					
				}
			}else{
				flagint = ResultUtils.ERROR;
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<BookInfo> getBookListResponseList(List<BookListLink> reqList ,Long uid, List<BookInfo> resList){
		BookInfo object = null;
		BkInfo bkInfo = new BkInfo();
		if(reqList.size()>0){
			BookListLink bookListLink = reqList.get(0);
			long id = bookListLink.getId();
			if(id != 0){
				flagint = ResultUtils.SUCCESS;
				for (BookListLink obj : reqList) {
	//				isDB = obj.getIsDb();
					BookList bookList = getResourceInfoFacade.queryByIdBookList(obj.getBookListId());
	//				if(isDB == TRUE){
						if(CommentUtils.TYPE_BOOK.equals(obj.getResType())){
							bkInfo = bkFacade.findBkInfo(obj.getBookId());
							object = bookUtils.putBKToBookInfo(bkInfo, TRUE,bookList.getuId(),bkCommentFacade , ucenterFacade);
						}else if(CommentUtils.TYPE_NETBOOK.equals(obj.getResType())){
							NetBook netBook = netBookFacade.findNetBookInfoById(obj.getBookId());
							object = bookUtils.putBKToBookInfo(netBook, TRUE,bookList.getuId(),bkCommentFacade , ucenterFacade);
						}
						
	//				}else{
	//					//不是库中的方法
	//					MyBk myBk = myBkFacade.findMyBkInfo(obj.getBookId());
	//					object = fileUtils.putBKToBookInfo(myBk, FALSE,bookList.getuId(),bkCommentFacade , ucenterFacade);
	//				}
					if(object.getId() != 0){
//						object.setForeignKeyId(obj.getId());
						resList.add(object);
					}
					
				}
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
//		else{
//			BookListLink obj = reqList.get(0);
//			flagint = obj.getFlag();
//			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
//				long id = obj.getId();
//				if(id != 0){
////					isDB = obj.getIsDb();
//					BookList bookList = getResourceInfoFacade.queryByIdBookList(obj.getBookListId());
////					if(isDB == TRUE){
//						bkInfo = bkFacade.findBkInfo(obj.getBookId());
//						object = bookUtils.putBKToBookInfo(bkInfo, FALSE,bookList.getuId(),bkCommentFacade , ucenterFacade);
//						
////					}else{
////						//不是库中的方法
////						MyBk myBk = myBkFacade.findMyBkInfo(obj.getBookId());
////						object = fileUtils.putBKToBookInfo(myBk, FALSE,bookList.getuId(),bkCommentFacade , ucenterFacade);
////					}
//					resList.add(object);
//				}
//			}
//		}
		return resList;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getCommentResponseList(List<BkComment> reqList , String type , List<ResourceInfo> resList){
		ResourceInfo commentInfo = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (BkComment obj : reqList) {
				commentInfo = fileUtils.putObjectToResource(obj, ucenterFacade,  bkFacade);
				resList.add(commentInfo);
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
					resList.add(commentInfo);
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
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}
	public void setOperationMemcachedClient(MemcachedClient operationMemcachedClient) {
		this.operationMemcachedClient = operationMemcachedClient;
	}
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
}
