package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserInfo;

/**
 * 点击书籍电影manager
 * @author Administrator
 * 
 */
public class ScanAddressManager extends BaseManager {
	private static final Log LOG = LogFactory
			.getLog(ScanAddressManager.class);
//	private Map<String, Object> req ;
//	private Map<String, Object> dataq;
//	private Map<String, Object> datas ;
//	private String resString;//返回数据
//	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//	private String error;
	
	private int flagint;
//	private int id;
//	private List<Map> ulist;
//	private List<String> pList;
//	private List<Map> list = new ArrayList<Map>();//返回的用户列表
//	private List<UserInfo> userList = new ArrayList<UserInfo>();

	
	private UcenterFacade ucenterFacade;

	/**
	 * 扫描通讯录
	 * @return
	 */
	public String scanAddress(String reqs,Long uid) {

//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");
		List<Map> ulist = (List<Map>) dataq.get("ulist");
		List<String> pList = new ArrayList<String>();
		for (Map pnum : ulist) {
			pList.add((String) pnum.get("pnum"));
		}
		//调用client方法
		//用户关注
		List<UserInfo> userList = ucenterFacade.findAttentionByMobile(null, pList, uid, 0, CommentUtils.PAGE_SIZE);
		List<Map> list = new ArrayList<Map>();
		
		list = getResponseList(userList, CommentUtils.RES_ATTENTION, list);
		//用户未关注
		clearObject(userList);//清理数据
		if(pList != null && pList.size()!=0){
			userList = ucenterFacade.findUnAttentionByMobile(null, pList, uid, 0, CommentUtils.PAGE_SIZE);
			list = getResponseList(userList, CommentUtils.RES_REGIST, list);
		}
//		System.out.println("list---------------"+list.size());
		clearObject(userList);//清理数据
		//未注册
		if(pList != null && pList.size()!=0){
			userList = ucenterFacade.findUnRegistListByMobileList(null, pList);
			list = getResponseList(userList, CommentUtils.RES_UNREGIST, list);
		}
//		System.out.println("list---------------"+list.size());
		
		
		
		//拼接数据
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", list);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);

//		clearObject(userList,list);
//		userList = new ArrayList<UserInfo>();
//		list = new ArrayList<Map>();
		return resString;
	}
	
	/**
	 * 将返回的电话号码分组处理
	 * @param list
	 * @param type
	 * @return
	 */
	public List getResponseList(List<UserInfo> reqList , String type , List<Map> resList){
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(reqList.size()>1){
			for (UserInfo userInfo : reqList) {
				flagint = ResultUtils.SUCCESS;
				resMap = new HashMap<String, Object>();
				//未注册的话用指定ID
				if(CommentUtils.RES_UNREGIST.equals(type)){
					resMap.put("uid", CommentUtils.UN_ID);
				}else{
					resMap.put("uid", userInfo.getUserId());
				}
				resMap.put("pnum", userInfo.getMobilePhone());
				resMap.put("type", type);
				resList.add(resMap);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			UserInfo ui = reqList.get(0);
			flagint = ui.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				String mp = ui.getMobilePhone();
				if(mp != null){
					resMap = new HashMap<String, Object>();
					//未注册的话用指定ID
					if(CommentUtils.RES_UNREGIST.equals(type)){
						resMap.put("uid", CommentUtils.UN_ID);
					}else{
						resMap.put("uid", ui.getUserId());
					}
					resMap.put("pnum", ui.getMobilePhone());
					resMap.put("type", type);
					resList.add(resMap);
				}
			}
		}
		return resList;
	}
	
	

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	
	
}
