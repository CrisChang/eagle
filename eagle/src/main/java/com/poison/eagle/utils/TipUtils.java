package com.poison.eagle.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TipEntity;
import com.poison.eagle.entity.UserAlbumInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.paycenter.model.RewardDetail;
import com.poison.paycenter.model.RewardPesonStatistical;
import com.poison.paycenter.model.RewardStatistical;
import com.poison.resource.client.BigFacade;
import com.poison.resource.model.BigLevelValue;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;

public class TipUtils {
	private static TipUtils userUtils;
	public TipUtils(){}
	public static TipUtils getInstance(){
		if(userUtils == null){
			return new TipUtils();
		}else{
			return userUtils;
		}
	}
	
	public static final String TYPE_COLL = "COLL";
	public static final String TYPE_PAY = "PAY";
	
	private FileUtils fileUtils = FileUtils.getInstance();
	
	/**
	 * 将RewardStatistical格式化
	 * @param rewardDetail
	 * @return
	 */
	public TipEntity putRewardStatisticalToTip(RewardStatistical rewardStatistical,ResourceInfo resourceInfo){
		TipEntity tipEntity = new TipEntity();
		if(rewardStatistical == null || rewardStatistical.getId() == 0){
			return tipEntity;
		}
		
		tipEntity.setId(rewardStatistical.getId());
		tipEntity.setAmount(rewardStatistical.getTotalAmt());
		tipEntity.setCount(rewardStatistical.getTotalCount());
		
		tipEntity.setResourceInfo(resourceInfo);
		
		return tipEntity;
	}
	/**
	 * 将RewardDetail格式化
	 * @param rewardDetail
	 * @return
	 */
	public TipEntity putRewardDetailToTip(RewardDetail rewardDetail,ResourceInfo resourceInfo,UcenterFacade ucenterFacade){
		TipEntity tipEntity = new TipEntity();
		if(rewardDetail == null || rewardDetail.getId() == 0){
			return tipEntity;
		}
		
		tipEntity.setId(rewardDetail.getId());
		tipEntity.setRidStr(rewardDetail.getId()+"");
		tipEntity.setAmount(rewardDetail.getSendAmt());
		tipEntity.setCreateDate(rewardDetail.getSendTime());
		tipEntity.setContent(rewardDetail.getPostscript());
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, rewardDetail.getSendUserId());
		UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, 0);
		tipEntity.setUserEntity(userEntity);
		tipEntity.setResourceInfo(resourceInfo);
		
		return tipEntity;
	}
	/**
	 * 
	 * @param rewardPesonStatistical
	 * @param ucenterFacade
	 * @param type COLL or PAY
	 * @return
	 */
	public TipEntity putRewardPesonStatisticalToTip(RewardPesonStatistical rewardPesonStatistical,
			UcenterFacade ucenterFacade,String type){
		TipEntity tipEntity = new TipEntity();
		if(rewardPesonStatistical == null || rewardPesonStatistical.getId() == 0){
			return tipEntity;
		}
		
		tipEntity.setId(rewardPesonStatistical.getId());
		if(TYPE_COLL.equals(type)){
			
			tipEntity.setAmount(rewardPesonStatistical.getTotalCollAmt());
			tipEntity.setCount(rewardPesonStatistical.getTotalCollCount());
		}else if(TYPE_PAY.equals(type)){
			tipEntity.setAmount(rewardPesonStatistical.getTotalPayAmt());
			tipEntity.setCount(rewardPesonStatistical.getTotalPayCount());
		}
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, rewardPesonStatistical.getUserId());
		UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, 0);
		tipEntity.setUserEntity(userEntity);
		
		return tipEntity;
	}
	
}
