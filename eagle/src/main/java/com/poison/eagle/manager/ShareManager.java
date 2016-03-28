package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActSubscribe;
import com.poison.eagle.entity.BigSelectInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.BigUtils;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BigLevelValue;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.Serialize;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;
import com.poison.store.client.BigSelectingFacade;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BigSelecting;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class ShareManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ShareManager.class);
//	private Map<String, Object> req ;
//	private Map<String, Object> dataq;
//	private Map<String, Object> datas ;
//	private String resString;//返回数据
//	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//	private String error;
	
	private int flagint;
	private UcenterFacade ucenterFacade;
	private DiaryFacade diaryFacade;
	private ActFacade actFacade;
	private GetResourceInfoFacade getResourceInfoFacade;
	private MyMovieFacade myMovieFacade;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private HttpUtils httpUtils = HttpUtils.getInstance();
	
	private String savePath;
	/**
	 * 晒一晒列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewShareList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		Long id = null;
		
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
		
//		if(id == UNID){
//			id = null;
//		}
		
		
		List<Diary> diaries = diaryFacade.queryAllType(CommentUtils.TYPE_SHARE);
		
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		
		resourceInfos = getResourceLists(diaries, resourceInfos);
		
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
	 * 写晒一晒
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeShare(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		List list = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			list = (List) dataq.get("list");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		if(id == UNID){
//			id = null;
//		}
		
		Diary diary = diaryFacade.addDiary(CommentUtils.TYPE_SHARE, WebUtils.putDataToHTML5(list), uid,"","","","","","","");
		
		flagint = diary.getFlag();
		
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
		
		return resString;
	}
	/**
	 * 顶/low
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String praiseShare(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		Long id = null;
		String type = null;
		String status = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			type = (String) dataq.get("type");
			status = (String) dataq.get("status");
			
		} catch (Exception e) {
			e.printStackTrace();
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
		}else if(CommentUtils.COMMENT_NOTPARISE.equals(status)){
			act = actFacade.doLow(null, uid, id, type);
		}
		
		int zNum = 0;
		int lNum = 0;
		flagint = act.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			zNum = actFacade.findPraiseCount(null, id);
			lNum = actFacade.findLowCount(id);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("zNum", zNum);
			datas.put("lNum", lNum);
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
	
	private static final String HTML_SHARE_BEGIN = "<!DOCTYPE HTML><html><head><meta charset=\"UTF-8\">" +
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/></head><style type=\"text/css\">" +
			"@font-face {font-family: FZXH-GB18030;src: url('http://s.duyao001.com/static/article/zt/FZXH-GB18030.ttf') format('truetype');} " +
			"a{text-decoration:none;}" +
			"body{padding:0 auto;margin:0 auto;background:#f0f0f0;font-family:FZXH-GB18030;}" +
			".tishi{margin:0 0 0 12px;background:#60a9cb;color:#FFF;padding:3px;width:42px;font-size:10px;border-radius:0 0 3px 3px !important;}" +
			".div_radius{border-radius:5px !important;color:#FFF;border:#FFF solid 1px;margin:12px 15px 0 0;padding:10px;font-size:13px;}" +
			".body_font{position:fixed;bottom:0;left:0;background:#111111;width:100%;height:60px;filter:alpha(Opacity=80);-moz-opacity:0.9;opacity:0.9;z-index:100;}" +
			".color_white{background:#FFF}" +
			".title{margin:15px 10px 10px 10px;color:#3c3c3c;font-weight:bold}" +
			".title_{margin:0 10px 20px 10px;font-size:10px;}" +
			".reason{width:100%;background:#FFF !important;padding:1px 0 1px 0;margin-bottom:10px}" +
			".reason_{margin:15px 10px 10px 10px;font-size:13px;color:#3c3c3c}" +
			".body_font_{float:left; color:#FFF; padding-top:15px;}</style>" +
			"<script>" +
			"function is_weixn(){var ua = navigator.userAgent.toLowerCase();if(ua.match(/MicroMessenger/i)==\"micromessenger\"){return true;} else{return false;}}" +
			"function xiazai(){if(is_weixn()){document.getElementById('load').style.display = \"\";document.getElementById('bantouming').style.display = \"\";}else{window.location.href=\"https://itunes.apple.com/us/app/du-yao/id917583287?l=zh&ls=1&mt=8\";}}" +
			"function displayLoad(){document.getElementById('load').style.display = \"none\";document.getElementById('bantouming').style.display = \"none\";}" +
			"</script>" +
			"<body>";
	
	private static final String HTML_SHARE_TITLE1 = 
			"<div onClick=\"displayLoad()\" id=\"bantouming\" style=\"position: absolute;left:0;top:0;width:100%; height:100%; background:#000000;filter:alpha(Opacity=40);-moz-opacity:0.4;opacity:0.4;z-index:1; display:none\"></div>" +
			"<div id=\"load\" style=\"position:fixed;left:0;top:0; display:none;z-index:2;\">" +
			"	<img src=\"http://p1.duyao001.com/image/article/2f67eaf10c5bd078aa6e7409ad0583f5_750x470.png\" style=\"width:100%;\"/>" +
			"</div>" +
			"										<div style=\"width:100%;\"><div class=\"title\">";
	private static final String HTML_SHARE_TITLE2 = "</div><div class=\"title_\">来源:毒药APP";
	private static final String HTML_SHARE_TITLE3 = "&nbsp;&nbsp;<span style=\"color:#60a9cb\">";
	private static final String HTML_SHARE_TITLE4 = "</span></div></div>";
	
	private static final String HTML_SHARE_REASON1 = "<div class=\"color_white\"><div class=\"tishi\">影单介绍</div></div>" +
			"<div class=\"reason\"><div class=\"reason_\">&nbsp;&nbsp;&nbsp;&nbsp;";
	private static final String HTML_SHARE_REASON2 = "</div></div><div class=\"color_white\"><div class=\"tishi\">清单列表</div></div>";
	
	
	private static final String HTML_SHARE_IMAGE1 = "<img onClick=\"displayLoad()\" style=\"width:100% !important;\" src=\"";
	private static final String HTML_SHARE_IMAGE2 = "\" />";
	
	private static final String HTML_SHARE_END = "<div style=\"height:60px;\"></div>" +
			"<div class=\"body_font\"><div style=\"float:left\">" +
			"<img src=\"http://112.126.68.72/image/article/559bdb7d7cacec668c1015ae121e52ed_68x68.png\" width=\"35\" height=\"35\" style=\"margin:12px;\"/></div>" +
			"<div class=\"body_font_\"><div><img src=\"http://112.126.68.72/image/article/51db2bf2eb7c205cfeeb9eb6e0b1e70b_82x35.png\" width=\"41\" height=\"18\"/></div>" +
			"<div style=\"font-size:10px\">下载毒药APP立即提高逼格</div></div>" +
			"<a style=\"float: right\" href=\"javascript:void(0)\" onClick=\"xiazai()\"><div class=\"div_radius\">立即下载</div></a><div style=\"clear:both\"></div></div></body></html>";
	
	/**
	 * 生成第三方分享
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String weixinShare(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		Long id = null;
		String type = null;
		String imageUrl = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			type = (String) dataq.get("type");
			imageUrl = (String) dataq.get("imageUrl");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String url = "";
		String title = "";
		String name = "";
		String reason = "";
		if(CommentUtils.TYPE_BOOKLIST.equals(type)){
			BookList bookList = getResourceInfoFacade.queryByIdBookList(id);
			title = bookList.getBookListName();
			if(bookList.getuId() != 0){
				UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, bookList.getuId());
				name = userAllInfo.getName();
			}
			reason = bookList.getReason();
		}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){
			MovieList movieList = myMovieFacade.findMovieListById(id);
			title = movieList.getFilmListName();
			if(movieList.getUid() != 0){
				UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, movieList.getUid());
				name = userAllInfo.getName();
			}
			reason = movieList.getReason();
		}
		
		
		
		String content = HTML_SHARE_BEGIN+HTML_SHARE_TITLE1+title+HTML_SHARE_TITLE2+HTML_SHARE_TITLE3+name+
				HTML_SHARE_TITLE4+HTML_SHARE_REASON1+reason+HTML_SHARE_REASON2+HTML_SHARE_IMAGE1+imageUrl+
				HTML_SHARE_IMAGE2+HTML_SHARE_END;
//				"<img src=\""+imageUrl+"\">"+WebUtils.HTML_CSS_FOR_ARTICLE_END;
		String filePath = savePath+UUID.randomUUID()+".html";
		flagint = fileUtils.writeFile(filePath, content);
		if(flagint == ResultUtils.SUCCESS){
			url = httpUtils.uploadFile(filePath, HttpUtils.HTTPUTIL_UPLOAD_FILE_URL_FOR_ARTICLE);
			
		}
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("url", url);
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
	public List<ResourceInfo> getResourceLists(List<Diary> reqList , List<ResourceInfo> resList){
		ResourceInfo ri = null;
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			long id = reqList.get(0).getId();
			
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (Diary object : reqList) {
					ri = fileUtils.putObjectToResource(object, ucenterFacade, actFacade);
					resList.add(ri);
				}
			}
		}
		return resList;
	}
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setDiaryFacade(DiaryFacade diaryFacade) {
		this.diaryFacade = diaryFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
}
