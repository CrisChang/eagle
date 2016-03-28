package com.poison.eagle.manager.web; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPublish;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.manager.JedisManager;
import com.poison.eagle.manager.ResourceJedisManager;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.manager.UserJedisManager;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Serialize;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class DiaryWebManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(DiaryWebManager.class);
	
	private int flagint;
	
	private DiaryFacade diaryFacade;
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private TopicFacade topicFacade;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	
	private JedisManager jedisManager;
	private ResourceManager resourceManager;
	private UserJedisManager userJedisManager;
	private ResourceJedisManager resourceJedisManager;
	
	private RocketProducer eagleProducer;
	/**
	 * 写日记
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeDiary(HttpServletRequest request,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String lon = "";//经度
		String lat = "";//维度
		String locationName="";//地点描述
		String locationCity="";//地点城市
		String locationArea="";//地点地区
		String title = "";// 标题
		String cover = "";// 封面
		
		String content = request.getParameter("content");
		String picUrlStr = request.getParameter("picUrl");
		
		String picUrl[]=null;
		if(picUrlStr!=null && picUrlStr.length()>0){
			picUrl = picUrlStr.split(",");
		}
		
		StringBuffer html = new StringBuffer("");
		html.append("<div>" + content +"</div>").append("<br/><!--我是分隔符woshifengefu-->");
		if(picUrl!=null && picUrl.length>0){
			for(int i=0;i<picUrl.length;i++){
				html.append("<img src=\"" + picUrl[i] + "\"/>").append("<br/><!--我是分隔符woshifengefu-->");
			}
		}
		
		String resultContent = html.toString();
		
		Diary diary = diaryFacade.addDiary(CommentUtils.TYPE_DIARY,resultContent , uid,lon,lat,locationName,locationCity,locationArea,title,cover);
		//用户的相册提取出来
//		List<String> photoList = WebUtils.getPhotoList();
//		UserAlbum userAlbum = ucenterFacade.insertintoUserAlbum(uid, photoList, CommentUtils.TYPE_ALBUM);
		
		flagint = diary.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将日志放入到缓存中
			resourceManager.setResourceToJedis(diary, uid,uid,0l);
			
			//更新用户的最后更新时间
			ucenterFacade.saveUserLatestInfo(uid, diary.getId(), CommentUtils.TYPE_DIARY);
			
			//增加用户日记数量
			try {
				userStatisticsFacade.updateDiaryCount(uid);
			} catch (Exception e) {
				LOG.error("增加用户日记数量失败:"+e.getMessage(), e.fillInStackTrace());
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
	 * 模糊查询图文列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String searchDiaryList(HttpServletRequest request,Long uid){
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
		List<Diary> diarys =diaryFacade.searchDiaryByLike(uid, keyword, starttime, endtime, start, pagesize);
		if(!(diarys!=null && diarys.size()==1 && diarys.get(0).getFlag()==ResultUtils.QUERY_ERROR)){
			Map<String,Object> amountmap = diaryFacade.findDiaryCountByLike(uid, keyword, starttime, endtime);
			long amount = 0;
			if(amountmap!=null){
				amount = (Integer) amountmap.get("count");
			}
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(); 
			resourceInfos = getResponseList(diarys, null, resourceInfos);
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
	 * 日记列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewDiaryList(Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		List<Diary> diaryList = new ArrayList<Diary>();
		List<ResourceInfo> resourceList = new ArrayList<ResourceInfo>();
		
		diaryList = diaryFacade.queryType(uid, CommentUtils.TYPE_DIARY);
		

		resourceList = getResponseList(diaryList, null, resourceList);
		
		//对资源进行倒序排列
		Collections.sort(resourceList);
		if(resourceList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
			resourceList = resourceList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
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
	 * 删除图文
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delOneDiary(HttpServletRequest request,Long uid){
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		long id = 0;
		
		String idstr = request.getParameter("id");
		if(idstr==null || !StringUtils.isInteger(idstr)){
			error = "短文id不合法";
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		
		id = Long.valueOf(idstr);	

		Diary diary = diaryFacade.deleteById(id);
		flagint = diary.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			delResourceFromJedis(uid, id, CommentUtils.TYPE_DIARY);
			//删除话题相关
			topicFacade.deleteTopicLinkByResid(id, uid);
		}
		
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
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List getResponseList(List<Diary> reqList , String type , List<ResourceInfo> resList){
		ResourceInfo ri = null;
		
		if(reqList.size()>0){
			if(reqList.get(0).getId() != 0){
				flagint = ResultUtils.SUCCESS;
				for (Diary obj : reqList) {
					ri = fileUtils.putObjectToResource(obj, ucenterFacade,actFacade);
					resList.add(ri);
				}
			}
		}else{
			flagint = ResultUtils.SUCCESS;
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
	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
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
	public void setResourceJedisManager(ResourceJedisManager resourceJedisManager) {
		this.resourceJedisManager = resourceJedisManager;
	}
}
