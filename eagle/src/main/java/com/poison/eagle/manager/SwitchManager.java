package com.poison.eagle.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserStatistics;

public class SwitchManager extends BaseManager{
	
	private static final  Log LOG = LogFactory.getLog(SwitchManager.class);
	
	private UserStatisticsFacade userStatisticsFacade;
	private PushManager pushManager;
	private UcenterFacade ucenterFacade;

	public String operateNoticeSwitch(String reqs,Long uid){
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =new HashMap<String, Object>();
		String type = "";
		String value = "";
		String error="";
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		reqs = reqs.trim();
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = (String) dataq.get("type");
			value = (String) dataq.get("value");
			if(null==value||value.equals("")){
				value = "0";
			}
			int valueInt = Integer.valueOf(value);
			
			UserStatistics userStatistics = new UserStatistics();
			if(type.equals(pushManager.PUSH_COMMENT_TYPE)){//评论的提醒
				userStatistics = userStatisticsFacade.updateCommentSwitch(uid, valueInt);
			}else if(type.equals(pushManager.PUSH_GIVE_TYPE)){//打赏的提醒
				userStatistics = userStatisticsFacade.updateGiveSwitch(uid, valueInt);
			}else if(type.equals(pushManager.PUSH_AT_TYPE)){//@的提醒
				userStatistics = userStatisticsFacade.updateAtSwitch(uid, valueInt);
			}
			int flagint = userStatistics.getFlag();
			if(flagint == ResultUtils.SUCCESS){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				//datas.put("list", resourceInfos);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: viewSetting</p> 
	 * <p>Description: 查看个人的设置</p> 
	 * @author :changjiang
	 * date 2015-4-29 下午8:16:43
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewSetting(String reqs,Long uid){
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();
		Map<String, Object> datas =new HashMap<String, Object>();
		String type = "";
		String value = "";
		String error="";
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		reqs = reqs.trim();
		try {
			
			UserStatistics userStatistics = userStatisticsFacade.findUserStatisticsByUid(uid);
			
			int isSearch = ucenterFacade.findSearchDuYao();
			int flagint = userStatistics.getFlag();
			
			if(flagint == ResultUtils.SUCCESS){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				datas.put("atSwitch", userStatistics.getAtSwitch());
				datas.put("commentSwitch", userStatistics.getCommentSwitch());
				datas.put("giveSwitch", userStatistics.getGiveSwitch());
				datas.put("searchDuYao", isSearch);
				//datas.put("list", resourceInfos);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resString;
	}
	
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setPushManager(PushManager pushManager) {
		this.pushManager = pushManager;
	}

	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
}
