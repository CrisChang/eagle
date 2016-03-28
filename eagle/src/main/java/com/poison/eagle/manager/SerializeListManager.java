package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.*;

import com.poison.resource.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class SerializeListManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(SerializeListManager.class);
	private Map<String, Object> req ;
	private Map<String, Object> dataq;
	private Map<String, Object> res ;
	private Map<String, Object> datas ;
	private String resString;//返回数据
	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
	private int flagint;
	private String error;
	
	private List<Map<String, String>> reasonList;
	
	private List<ResourceInfo> resourceList;
	
	private SerializeFacade serializeFacade;
	private SerializeListFacade serializeListFacade;
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	private FileUtils fileUtils = FileUtils.getInstance();
	private SerializeUtils serializeUtils = SerializeUtils.getInstance();
	/**
	 * 创建连载单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String createSerializeList(String reqs,Long uid){
		LOG.info("客户端json数据："+reqs);
		String title = "";
		String reason = "";
		String tag = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			title = (String) dataq.get("title");
			reason = (String) dataq.get("reason");
			tag = (String) dataq.get("tag");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SerializeList serializeList = serializeListFacade.addSerializeList(title, reason, CommentUtils.type_list2, tag, uid, 0);
		
		flagint = serializeList.getFlag();

		
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
	 * 连载单中添加连载
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeSerializeList(String reqs,Long uid){
		LOG.info("客户端json数据："+reqs);
		long serializeListId = 0;
		long serializeId = 0;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			serializeListId = Long.valueOf(dataq.get("serializeListId").toString());
			serializeId = Long.valueOf(dataq.get("serializeId").toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SerializeListLink serializeListLink = null;//serializeListFacade.addSerializeListLink(serializeListId, serializeId);
		
		flagint = serializeListLink.getFlag();
		
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
	 *	显示公共连载单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewPublicSerializeList(String reqs,Long uid){
		LOG.info("客户端json数据："+reqs);
		long serializeListId = 0;
		long serializeId = 0;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			serializeListId = Long.valueOf(dataq.get("serializeListId").toString());
			serializeId = Long.valueOf(dataq.get("serializeId").toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		serializeListFacade.findSerializeListLinkByListId(id)
		
		SerializeListLink serializeListLink = null;//serializeListFacade.addSerializeListLink(serializeListId, serializeId);
		
		flagint = serializeListLink.getFlag();
		
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
	 * 查看某一个连载单
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewOneSerializeList(String reqs,Long uid){
		LOG.info("客户端json数据："+reqs);
		long serializeListId = 0;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			serializeListId = Long.valueOf(dataq.get("serializeListId").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<SerializeListLink> serializeListLinks = serializeListFacade.findSerializeListLinkByListId(serializeListId);
		List<SerializeInfo> serializeInfos = new ArrayList<SerializeInfo>();
		
		serializeInfos = getSerializeLists(serializeListLinks, serializeInfos);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", serializeInfos);
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

	public String delSerializeListLink(String reqs,long uid){
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
				serializeListFacade.delSerializeListLink(uid,Long.valueOf(idStr));
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
	 * 查看看过的小说列表
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewReadedSerializeList(String reqs,Long uid){
		Long serializeListId = null;
		String serializeListStr = "";
		//去掉空格
		reqs = reqs.trim();

		Map<String, Object> req =new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =null;

		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			//最后的更新id
			serializeListStr =(String)dataq.get("lastId");

		} catch (Exception e) {
			e.printStackTrace();
		}

		Long endDate = null;
		if(null!=serializeListStr&&!"".equals(serializeListStr)&&!"0".equals(serializeListStr)){
			//查询当前用户看过的最后的连载时间
			serializeListId = Long.valueOf(serializeListStr);
			SerializeListLink lastSerializeListLink = serializeListFacade.findSerializeListLinkByUidAndSerializeId(uid,serializeListId);
			endDate = lastSerializeListLink.getLatestRevisionDate();
		}

		List<SerializeListLink> serializeListLinks = serializeListFacade.findViewedSerializeLinkList(uid,endDate);
		List<SerializeInfo> serializeInfos = new ArrayList<SerializeInfo>();

		serializeInfos = getSerializeLists(serializeListLinks, serializeInfos);
		int flagint = ResultUtils.SUCCESS;
		String flag = CommentUtils.RES_FLAG_ERROR;
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", serializeInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);

		return resString;
	}



	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型
	 * @param list
	 * @param type
	 * @return
	 */
	public List<SerializeInfo> getSerializeLists(List reqList  , List<SerializeInfo> resList){
		SerializeInfo si = null;
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			String objName = reqList.get(0).getClass().getName();
			long id = 0;
			if(SerializeListLink.class.getName().equals(objName)){
				SerializeListLink object = (SerializeListLink) reqList.get(0);
				id = object.getId();
			}
			
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (Object object : reqList) {
					if(SerializeListLink.class.getName().equals(objName)){
						si = serializeUtils.putLinkToSerialize(object, actFacade, ucenterFacade, serializeFacade);

						Serialize serialize = serializeFacade.seleceSerializeByid(si.getId());
						if(null==serialize){
							continue;
						}
						List<ChapterSummary> chapterList = serializeFacade.findChapterSummary(si.getId());
						List<ResourceInfo> resourceList  = new ArrayList<ResourceInfo>();
						resourceList = getResponseList(chapterList, null, resourceList);
						si.setChapterList(resourceList);
					}
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
	public void setSerializeListFacade(SerializeListFacade serializeListFacade) {
		this.serializeListFacade = serializeListFacade;
	} 
}
