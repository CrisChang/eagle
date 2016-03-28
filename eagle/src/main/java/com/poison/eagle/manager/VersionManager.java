package com.poison.eagle.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.entity.VersionInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;

/**
 * 点击书籍电影manager
 * @author Administrator
 * 
 */
public class VersionManager extends BaseManager {
	private static final Log LOG = LogFactory
			.getLog(VersionManager.class);
	private Map<String, Object> req ;
	private Map<String, Object> dataq;
	private Map<String, Object> res ;
	private Map<String, Object> datas ;
	private Map<String, Object> json;
	private String resString;//返回数据
	private String dataString;//从client层获取的数据
	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
	private int flagint;
	private String error;
	private UcenterFacade ucenterFacade;
	/**
	 * 扫描通讯录
	 * @return
	 */
	public String getVersion(String reqs) {

		LOG.info("客户端json数据："+reqs);
		//去掉空格
		reqs = reqs.trim();
		String os = "";//系统版本
		String channel= "";//系统渠道
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		try{
			os = (String) dataq.get("os");
			channel = (String) dataq.get("channel");
		}catch (Exception e) {
			e.printStackTrace();
			os=null;
			channel = null;
		}
		
		VersionInfo versionInfo = ucenterFacade.findLatestVersion();
		flagint = versionInfo.getFlag();
		String version = versionInfo.getVersion();
		if(null!=os&&null!=channel){
			if(os.equals("ios")){//如果为ios
				if(channel.equals("App Store")){//appstore版本
					version = versionInfo.getIosAppstoreVersion();
				}else if(channel.equals("Enterprise")){//fir版本
					version = versionInfo.getIosFirVersion();
				}
			}else if(os.equals("android")){//如果为安卓的话
				if(channel.equals("androidStore")){//安卓市场版本
					version = versionInfo.getAndroidStoreVersion();
				}else if(channel.equals("fir")){//安卓fir版本
					version = versionInfo.getAndroidFirVersion();
				}
			}
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("version", version);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		//拼接数据
		datas.put("flag", flag);

		//处理返回数据
		resString = getResponseData(datas);

		return resString;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	
}
