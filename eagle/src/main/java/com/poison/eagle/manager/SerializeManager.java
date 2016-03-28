package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.*;

import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActPublish;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class SerializeManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(SerializeManager.class);
	
	private int flagint;
	
	private SerializeFacade serializeFacade;
	private SerializeListFacade serializeListFacade;
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	private FileUtils fileUtils = FileUtils.getInstance();
	private SerializeUtils serializeUtils = SerializeUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	
	private ResourceManager resourceManager;
	/**
	 * 写书单方法
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeSerialize(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String tag = "";
		String url = "";
		String author = "";
		String describe = "";
		String title = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			title = (String) dataq.get("title");
			describe = (String) dataq.get("describe");
			author = (String) dataq.get("author");
			url = (String) dataq.get("url");
			tag = (String) dataq.get("tag");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		Serialize serialize = serializeFacade.addSerialize(tag, title, author, describe, url, uid);

		long sid = serialize.getId();
		flagint = serialize.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", sid);
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
	 * 写章节
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeChapter(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		int isPublish=0;
		Long chapterId = null;
		Long id = null;
		List reasonList = null;
		String title = "";
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			
			chapterId = Long.valueOf(dataq.get("chapterId").toString());
			title = (String) dataq.get("title");
			reasonList = (List) dataq.get("list");
			if(chapterId == UNID){
				id = Long.valueOf(dataq.get("id").toString());
				isPublish = Integer.valueOf(dataq.get("isPublish").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Chapter chapter = new Chapter();
		ActPublish actPublish = new ActPublish();
		if(chapterId == UNID){
			//更新连载
			if(isPublish == TRUE){
				chapter = serializeFacade.addOldChapter(id, title, WebUtils.putDataToHTML5(reasonList), uid);
				flagint = chapter.getFlag();
				if(flagint == ResultUtils.SUCCESS){
					actPublish = actFacade.addOnePublishInfo(uid, id, CommentUtils.TYPE_PUSH);
					flagint = actPublish.getFlag();
				}
				
			}else{
				chapter = serializeFacade.addOldChapter(id, title, WebUtils.putDataToHTML5(reasonList), uid);
				flagint = chapter.getFlag();
			}
		}else{
			//修改章节
			flagint = serializeFacade.updateChapter(chapterId, title, uid);
			flagint = serializeFacade.updateChapterContent(chapterId, WebUtils.putDataToHTML5(reasonList), uid);
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == 0){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			
			//将生成的推送连载放入到缓存中
			//resourceManager.setResourceToJedis(actPublish, uid,uid,0l);
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
	 * 修改连载
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String updateSerialize(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		String type = "";
		String title = "";
		List reasonList = null;
		String describe = "";
		Long id = null;
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			describe = (String) dataq.get("describe");
			reasonList = (List) dataq.get("list");
			title = (String) dataq.get("title");
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		
		if(CommentUtils.REQ_ISON_TRUE.equals(type)){
			//修改连载
			flagint = serializeFacade.updateSerialize(id, title, uid);
			flagint = serializeFacade.updateSerializeIntroduce(id, describe, uid);
		}else{
			//修改章节
			flagint = serializeFacade.updateChapter(id, title, uid);
			flagint = serializeFacade.updateChapterContent(id, WebUtils.putDataToHTML5(reasonList), uid);
		}
		
		
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
	 * 删除连载
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delSerialize(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		String type = "";
		List idList = new ArrayList();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			idList = (List)dataq.get("idList");

		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		if(null!=idList&&idList.size()>0){
			Iterator<String> it = idList.iterator();
			String idStr = "";
			while(it.hasNext()){
				idStr = it.next();
				serializeFacade.deleteSerialize(Long.valueOf(idStr));
			}
		}
//		if(CommentUtils.REQ_ISON_TRUE.equals(type)){
//			//修改连载
//			flagint = serializeFacade.deleteSerialize(id);
//		}else{
//			//修改章节
//			flagint = serializeFacade.deleteChapter(id);
//		}
		int flagint = ResultUtils.SUCCESS;
		
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
	 * 连载目录
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewSerializeList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
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
		
		
//		type = (String) dataq.get("type");
		
		List<Serialize> sList = new ArrayList<Serialize>();
		List<SerializeInfo> resList = new ArrayList<SerializeInfo>();
		//用户全部连载目录
		sList = serializeFacade.findSerializeByUser(id);
		resList = getSerializeResponseLists(sList, resList);
			
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resList);
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
	 * 章节目录
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewChapterList(String reqs,long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		String type = "";
		Long id = null;
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
//		System.out.println(req);
		
		
		Serialize serialize = new Serialize();
		SerializeInfo serializeInfo = new SerializeInfo();
		
		List<ChapterSummary> cList = new ArrayList<ChapterSummary>();
		List<ResourceInfo> resourceList  = new ArrayList<ResourceInfo>();
		if(!"".equals(id)){
			serialize = serializeFacade.seleceSerializeByid(id);
			cList = serializeFacade.findChapterSummary(id);
		}
		//再加上小说详情
		resourceList = getResponseList(cList, null, resourceList);
		serializeInfo = serializeUtils.putSerializeToInfo(serialize, actFacade, ucenterFacade,serializeFacade);
		//查看是否加入书架
		SerializeListLink serializeListLink = serializeListFacade.findSerializeListLinkByUidAndSerializeId(uid,id);
		int flagSerializeLink = serializeListLink.getFlag();
		if(ResultUtils.SUCCESS==flagSerializeLink){//已经加入书架
			serializeInfo.setIsNovelshelf(1);
		}else{
			serializeInfo.setIsNovelshelf(0);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("serializeInfo",serializeInfo);
			datas.put("list", resourceList);
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
	 * 章节内容
	 * @param reqs	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewChapter(String reqs,long uid){
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
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		Chapter c = new Chapter();
		
		c = (Chapter) serializeFacade.findChapterContent(id);
		
		ResourceInfo resourceInfo = fileUtils.putObjectToResource(c, ucenterFacade, actFacade);
		

		datas = new HashMap<String, Object>();

//		Map<String, Object> chapterInfo = new HashMap<String, Object>();
//		chapterInfo.put("title",c.getName());
//		chapterInfo.put("content",c.getContent());
//		chapterInfo.put("id",c.getId());

		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			//添加已经阅读过的小说
			serializeListFacade.updateSerlizeLinkEndDate(0,c.getParentId(),c.getId(),uid);
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("resourceInfo", resourceInfo);
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


	public String addNovelList(String reqs,long uid){
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
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);

		//添加已经阅读过的小说
		SerializeListLink serializeListLink = serializeListFacade.updateSerlizeLinkEndDate(0, id, 0, uid);

		int flagint = serializeListLink.getFlag();
		datas = new HashMap<String, Object>();

//		Map<String, Object> chapterInfo = new HashMap<String, Object>();
//		chapterInfo.put("title",c.getName());
//		chapterInfo.put("content",c.getContent());
//		chapterInfo.put("id",c.getId());

		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){

			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("resourceInfo", resourceInfo);
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
	public List<SerializeInfo> getSerializeResponseLists(List<Serialize> reqList  , List<SerializeInfo> resList){
		SerializeInfo si = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (Serialize s : reqList) {
				si = serializeUtils.putSerializeToInfo(s, actFacade, ucenterFacade,serializeFacade);
				resList.add(si);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			Serialize s = reqList.get(0);
			flagint = (int) s.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				long id = s.getId();
				if(id != 0){
					UserAllInfo ui = ucenterFacade.findUserInfo(null, s.getUid());
					si = serializeUtils.putSerializeToInfo(s, actFacade, ucenterFacade,serializeFacade);
					resList.add(si);
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
	public List getResponseList(List reqList , String type , List<ResourceInfo> resList){
		ResourceInfo ri = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (Object obj : reqList) {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade,actFacade);
				resList.add(ri);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			Object object = reqList.get(0);
			ResourceInfo resourceInfo = fileUtils.putObjectToResource(object,ucenterFacade);
			flagint = resourceInfo.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				long id = resourceInfo.getRid();
				if(id != 0){
					ri = fileUtils.putObjectToResource(object, ucenterFacade, actFacade);
					resList.add(ri);
				}
			}
		}
		return resList;
	}
	public void setSerializeFacade(SerializeFacade serializeFacade) {
		this.serializeFacade = serializeFacade;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setSerializeListFacade(SerializeListFacade serializeListFacade) {
		this.serializeListFacade = serializeListFacade;
	}
}
