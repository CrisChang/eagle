package com.poison.eagle.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.ucenter.client.AdvertFacade;
import com.poison.ucenter.model.Advert;

public class AdvertManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(AdvertManager.class);
	
	//private int flagint;
	private AdvertFacade advertFacade;
	
	public void setAdvertFacade(AdvertFacade advertFacade) {
		this.advertFacade = advertFacade;
	}

	/**
	 * 查询广告信息
	 * @return
	 */
	public String getAdvertInfo(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		long period = 3*60*60*1000;//显示广告时间间隔
		long lasttime = 0;//上次浏览广告时间
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			String lasttimeStr = dataq.get("lasttime")+"";
			if(StringUtils.isInteger(lasttimeStr)){
				lasttime = Long.parseLong(lasttimeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		
		//需要判断是否超过了时间间隔，如果超过了则显示广告信息
		long nowtime = System.currentTimeMillis();
		if(nowtime-lasttime<period){
			//未超过时间间隔，不需要显示广告信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("flag", flag);
			Advert advert = new Advert();
			advert.setShow(0);
			datas.put("map",advert);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		}
		int flagint = ResultUtils.ERROR;
		
		Advert advert = advertFacade.getAdvertInfo();
		if(advert!=null){
			flagint = advert.getFlag();
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			advert.setShow(1);
			datas.put("map",advert);
			datas.put("lasttime", nowtime+"");
			datas.put("disappear", "3");//客户端自动消失广告时间,单位秒
		}else{
			if(advert==null){
				//没有数据
				flag = CommentUtils.RES_FLAG_SUCCESS;
				advert = new Advert();
				advert.setShow(0);
				datas.put("map",advert);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
}
