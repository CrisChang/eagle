package com.poison.eagle.manager.web; 

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.keel.utils.UKeyWorker;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.manager.UserJedisManager;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MyBk;
import com.poison.store.client.BkFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.NetBook;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserStatistics;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class BookWebManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(BookWebManager.class);
	
	/**
	 * 记录的日志信息
	 */
	private static final  Log BUSINESS_LOG = LogFactory.getLog("COMMON.BUSINESS");
	
	private int flagint;
	
	
	private BkFacade bkFacade;
	private BkCommentFacade bkCommentFacade;
	private NetBookFacade netBookFacade;
	private UcenterFacade ucenterFacade;
	private GetResourceInfoFacade getResourceInfoFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private ActFacade actFacade;
	private TopicFacade topicFacade;
	//private UKeyWorker reskeyWork;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	
	private UserJedisManager userJedisManager;
	private ResourceManager resourceManager;
	
	
	/**
	 * 写书评
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeBookComment(HttpServletRequest request,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		int id = 0;
		String content = request.getParameter("content");
		String picUrlStr = request.getParameter("picUrl");
		String picUrl[]=null;
		if(picUrlStr!=null && picUrlStr.length()>0){
			picUrl = picUrlStr.split(",");
		}
		String score = "";
		long commentId = 0;
		int isDB = 0;
		String name = "";
		String type = "";
		Long bookListId = 0l;
		String scan = "";
		String beforeScore = "";
		String lon = "";//经度
		String lat = "";//维度
		String locationName="";//地点描述
		String locationCity="";//地点城市
		String locationArea="";//地点地区
		Long vid = 0l;//虚拟主键
		String title = "";//标题
		String cover = "";//封面
		String resourceType = CommentUtils.TYPE_BOOK_COMMENT;//当前的资源类型
		//转化成可读类型
		try {
			score = request.getParameter("score");
			String ob = request.getParameter("commentId");
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
			
			id = Integer.valueOf(idstr);
			
			lon = CheckParams.objectToStr(request.getParameter("lon"));
			lat = CheckParams.objectToStr(request.getParameter("lat"));
			locationName = CheckParams.objectToStr(request.getParameter("locationName"));
			locationCity = CheckParams.objectToStr(request.getParameter("locationCity"));
			locationArea = CheckParams.objectToStr(request.getParameter("locationArea"));
			
			//常江修改
			//是否扫描 0为扫描1为不是扫描
			scan = request.getParameter("scan");
			//System.out.println("是否扫描"+scan);
			if(null==scan||"".equals(scan)){
				scan = "1";
			}
			if(null==ob||"".equals(ob)){
				ob="0";
			}
			commentId = Long.valueOf(ob);
			if(commentId == UNID){
			}else{
				beforeScore = request.getParameter("beforeScore");
				if(null==beforeScore){
					beforeScore = "0";
				}
			}
			try {
				type = request.getParameter("type");
			} catch (Exception e) {
				type = "";
			}
			try {
				bookListId = Long.valueOf(request.getParameter("bookListId"));
				if(0l == bookListId){
					bookListId = null;
				}

			} catch (Exception e) {
				bookListId = null;
			}
			if(title!=null && title.length()>0){
				resourceType = CommentUtils.TYPE_ARTICLE_BOOK;//有标题则为长书评
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
		
		StringBuffer html = new StringBuffer("");
		html.append("<div>" + content +"</div>").append("<br/><!--我是分隔符woshifengefu-->");
		if(picUrl!=null && picUrl.length>0){
			for(int i=0;i<picUrl.length;i++){
				html.append("<img src=\"" + picUrl[i] + "\"/>").append("<br/><!--我是分隔符woshifengefu-->");
			}
		}
		String resultContent = html.toString();
		if(commentId == UNID){
			//判断是否是库中的书
			//库中的书先添加评论，在存到默认书单中
//				bkComment = bkCommentFacade.addOneBkCommentByType(uid, id, WebUtils.putDataToHTML5(commentList), score, level+"", type);//(uid, id, WebUtils.putDataToHTML5(commentList), score, TRUE, isDB);
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
		}else{
			//==============================start===================================
			//web端发表和编辑影评、书评、文字的时候不能上传音乐和视频，所以在编辑的时候需要保存之前的音频和视频
			BkComment oldbkComment = bkCommentFacade.findCommentIsExistById(commentId);
			if(oldbkComment!=null){
				//音频常量
				String AUDIO_BEGIN = "<audio src=\"";
				String AUDIO_END = "\" controls=\"controls\"></audio>";
				//视频常量
				String VIDEO_BEGIN = "<video src=\"";
				String VIDEO_END  ="\" controls=\"contro" +
						"ls\"></video>";
				List<Map<String, String>> list = WebUtils.putHTMLToData(oldbkComment.getComment());
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, String> map = list.get(i);
						String type2 = map.get("type");
						String data = map.get("data");
						String length = map.get("length");
						if("2".equals(type2)){
							if(length!=null && length.length()>0){
								data = data.substring(0,data.lastIndexOf("."))+"_"+length+data.substring(data.lastIndexOf("."));
							}
							html.append(AUDIO_BEGIN + data + AUDIO_END).append("<br/><!--我是分隔符woshifengefu-->");
						}else if("3".equals(type2)){
							html.append(VIDEO_BEGIN + data + VIDEO_END).append("<br/><!--我是分隔符woshifengefu-->");
						}
					}
					
					resultContent = html.toString();
				}
			}
			//=======================================end===================================================
			bkComment = bkCommentFacade.updateMyCommentByBook(commentId, score,resultContent,title,cover);
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
		//如果有@消息，将此消息存入表中，并推送给相关的人
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			//将修改后的书评放入到缓存中
			resourceManager.setResourceToJedis(bkComment, uid,uid,vid);
			
			//将附加信息加入缓存中
			resourceManager.updateInListByJedis((long)bkComment.getBookId(), uid, CommentUtils.TYPE_BOOK, "0");
			
			//更新用户的最后更新时间
			ucenterFacade.saveUserLatestInfo(uid, bkComment.getId(), CommentUtils.TYPE_BOOK_COMMENT);
		}
		
		
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
	
	/**
	 * 查询一个书评的详细信息
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getOneBkComment(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;
		Map<String,Object> datas = new HashMap<String,Object>();
		String commentIdStr = request.getParameter("commentId");
		Long commentId = null;
		if(StringUtils.isInteger(commentIdStr)){
			commentId = Long.valueOf(commentIdStr);
		}
		if(commentId==null || commentId<=0){
			String error = "书评id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			String resString = getResponseData(datas);
			return resString;
		}
		BkComment bkComment = bkCommentFacade.findCommentIsExistById(commentId);
		if(bkComment!=null && bkComment.getId()>0){
			ResourceInfo resourceInfo = fileUtils.putObjectToResource(bkComment, ucenterFacade,  bkFacade);
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
	 * 删除书评
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delBookComment(HttpServletRequest request,Long uid){
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		long id = 0;
		long bookId = 0;
		
		String idstr = request.getParameter("commentId");
		//String bookIdStr = request.getParameter("bookId");
		
		if(idstr==null || !StringUtils.isInteger(idstr)){
			error = "评论id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		//bookId = Long.valueOf(bookIdStr);
		id = Long.valueOf(idstr);
		
		BkComment bkComment = bkCommentFacade.deleteMyCommentById(id);
		String beforeScore = bkComment.getScore();
		bookId = bkComment.getBookId();
		flagint = bkComment.getFlag();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			bkCommentFacade.deleteBkAvgMark((int) bookId, Float.valueOf("0.0"), Float.valueOf(beforeScore));
		}
	
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
	 * 查看书评列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBookCommentList(HttpServletRequest request,Long uid){
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		List<BkComment> bkcommentList = bkCommentFacade.findMyBkCommentListByTime(uid, 0L, System.currentTimeMillis());
		
		if(bkcommentList!=null && bkcommentList.size()>0){
			flagint = bkcommentList.get(0).getFlag();
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
	
	/**
	 * 修改长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map searchBookMovie(HttpServletRequest request,Long uid){
		Map map = new HashMap();
//		String flag = CommentUtils.RES_FLAG_ERROR;
		String error = "";
		String json = "";
		
		String name = request.getParameter("name");
		int num = Integer.valueOf(request.getParameter("num"));
		String type = request.getParameter("type");
		
		List<BookInfo> bookList = new ArrayList<BookInfo>();
		if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
			//库中的书
//			List<BkInfo> bkList = bkFacade.findBkInfoByName(name);
//			bookList = bookManager.getBKResponseList(bkList, null, bookList);
			
			json = getJsonFromUrl(CommentUtils.WEB_SEARCH_BOOK,name);
			
		}else{
			//库中电影
			json = getJsonFromUrl(CommentUtils.WEB_SEARCH_MOVIE,name);
		}
		
//		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("list", bookList);
//		}else{
//			flag = CommentUtils.RES_FLAG_ERROR;
//			error = MessageUtils.getResultMessage(flagint);
//			LOG.error(flagint);
//			datas.put("error", error);
//		}
//		datas.put("flag", flag);
//		map.put("flag", flag);
//		//处理返回数据
//		String resString = getResponseData(datas);
		json = "{"+json;
		
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		int flag = 0;//0：成功、1：失败
		
		List<Map<String, Object>> bm = new ArrayList<Map<String,Object>>();
		//去掉空格
		json = json.trim();
		
		//转化成可读类型
		try {
			dataq = getObjectMapper().readValue(json,  new TypeReference<Map<String, Object>>(){});
//			req = (Map<String, Object>) req.get("req");
//			dataq = (Map<String, Object>) req.get("data");
			
			bm = (List<Map<String, Object>>) dataq.get("list");
			flag = Integer.valueOf(dataq.get("flag").toString());
			error = (String) dataq.get("error");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Map<String, Object>> resList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : bm) {
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("id", map2.get("id"));
			resMap.put("name", map2.get("name"));
			resMap.put("pagePic", map2.get("pagePic"));
			resMap.put("authorName", map2.get("authorName"));
			resList.add(resMap);
		}
		
		
		datas = new HashMap<String, Object>();
//		if(flagint == ResultUtils.SUCCESS||flagint == UNID){
//			flag = CommentUtils.RES_FLAG_SUCCESS;
//		}else{
//			flag = CommentUtils.RES_FLAG_ERROR;
//			LOG.error(flagint);
//			error = MessageUtils.getResultMessage(flagint);
//			datas.put("error", error);
//		}
		datas.put("flag", flag);
		datas.put("error", error);
		datas.put("list", resList);
		//处理返回数据
		resString = getResponseData(datas);
		
		map.put("json", resString);
		
		
		
		return map;
	}
	
	public String getJsonFromUrl(String path,String name){
		URL url = null;
//		path = path+name;
		StringBuilder tempStr = new StringBuilder(); 
		InputStream inStrm = null;
		OutputStream outStrm = null;
		HttpURLConnection httpUrlConnection = null;
		URLConnection rulConnection = null;
		BufferedReader rd = null;
		ObjectOutputStream objOutputStrm = null;
		String parm = "";
		try {
			url = new URL(path); 
			rulConnection = url.openConnection();// 此处的urlConnection对象实际上是根据URL的 
			// 请求协议(此处是http)生成的URLConnection类 
			// 的子类HttpURLConnection,故此处最好将其转化 
			// 为HttpURLConnection类型的对象,以便用到 
			// HttpURLConnection更多的API.如下: 
			httpUrlConnection = (HttpURLConnection) rulConnection; 
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在 
			// http正文内，因此需要设为true, 默认情况下是false; 
			httpUrlConnection.setDoOutput(true); 
			// 设置是否从httpUrlConnection读入，默认情况下是true; 
			httpUrlConnection.setDoInput(true); 
			// Post 请求不能使用缓存 
			httpUrlConnection.setUseCaches(false); 
			// 设定传送的内容类型是可序列化的java对象 
			// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException) 
//			httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object"); 
			httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpUrlConnection.setRequestProperty("Charset", "utf-8");
			// 设定请求的方法为"POST"，默认是GET 
			httpUrlConnection.setRequestMethod("POST"); 
			httpUrlConnection.setRequestProperty("q", name);
			// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成， 
			httpUrlConnection.connect(); 
			// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法， 
			// 所以在开发中不调用上述的connect()也可以)。 
			outStrm = httpUrlConnection.getOutputStream(); 
			// 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。 
			objOutputStrm = new ObjectOutputStream(outStrm); 
			// 向对象输出流写出数据，这些数据将存到内存缓冲区中 
			parm = "?q="+name;
			objOutputStrm.writeObject(parm.getBytes()); 
//			 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream） 
			objOutputStrm.flush(); 
			// 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中, 
			// 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器 
			objOutputStrm.close(); 
			// 调用HttpURLConnection连接对象的getInputStream()函数, 
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。 
			inStrm = httpUrlConnection.getInputStream(); // <===注意，实际发送请求的代码段就在这里 
			// 上边的httpConn.getInputStream()方法已调用,本次HTTP请求已结束,下边向对象输出流的输出已无意义， 
			// 既使对象输出流没有调用close()方法，下边的操作也不会向对象输出流写入任何数据. 
			// 因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据、 
			// 重新发送数据(至于是否不用重新这些操作需要再研究) 
			rd = new BufferedReader(new InputStreamReader(inStrm,"UTF-8"));  
			while (rd.read() != -1) {  
				tempStr.append(rd.readLine());  
			}  
//			System.out.println(tempStr.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
//				objOutputStrm.close();
				rd.close();
				inStrm.close();
//				outStrm.close();
			} catch (Exception e2) {
			}
		}
		
		return tempStr.toString();
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
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
}
