package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Diary;
import com.poison.resource.model.ResReport;
import com.poison.resource.model.Serialize;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserFeedBackFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserFeedBack;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class ReportManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ReportManager.class);
	
	private int flagint;
	
	private MyBkFacade myBkFacade;
	private UserFeedBackFacade userFeedBackFacade;
	
	/**
	 * 写举报
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeReport(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		List reasonList = null;
		Long id = null;
		String type = "";
		String reason = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			type = dataq.get("type").toString();
			reason = dataq.get("reason").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		
		ResReport resReport = myBkFacade.insertResReport(uid, id, type, reason);
		
		flagint = resReport.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
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
	 * 写反馈
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writeFeedback(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		List reasonList = null;
		Long id = null;
		String type = "";
		String reason = "";
		List list = new ArrayList();
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			list = (List) dataq.get("list");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		System.out.println(req);
		
		UserFeedBack userFeedBack = userFeedBackFacade.insertintoUserfeedback(uid, 0l, "", WebUtils.putDataToHTML5(list), "");
		
		//ResReport resReport = myBkFacade.insertResReport(uid, id, type, reason);
		
		flagint = userFeedBack.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
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
//					ri = fileUtils.putObjectToResource(obj, ucenterFacade,actFacade);
					resList.add(ri);
				}
			}
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		
		
		return resList;
	}
	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}
	public void setUserFeedBackFacade(UserFeedBackFacade userFeedBackFacade) {
		this.userFeedBackFacade = userFeedBackFacade;
	}
	
}
