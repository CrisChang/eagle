package com.poison.eagle.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActSubscribe;
import com.poison.eagle.entity.BigSelectInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserBigInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.MovieManager;
import com.poison.msg.model.MsgAt;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.model.BigLevelValue;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Serialize;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BigSelecting;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserBigValue;

public class BigUtils {
	private static final  Log LOG = LogFactory.getLog(BigUtils.class);
	private static BigUtils actUtils;

	public BigUtils() {
	}

	public static BigUtils getInstance() {
		if (actUtils == null) {
			return new BigUtils();
		} else {
			return actUtils;
		}
	}

	private FileUtils fileUtils = FileUtils.getInstance();

	/**
	 * 将逼格选择题格式化
	 * @param bigSelecting
	 * @return
	 */
	public BigSelectInfo putBigSelectToInfo(BigSelecting bigSelecting){
		BigSelectInfo bigSelectInfo = new BigSelectInfo();
		
		bigSelectInfo.setId(bigSelecting.getId());
		bigSelectInfo.setaItem(bigSelecting.getaItem());
		bigSelectInfo.setbItem(bigSelecting.getbItem());
		bigSelectInfo.setcItem(bigSelecting.getcItem());
		bigSelectInfo.setdItem(bigSelecting.getdItem());
		bigSelectInfo.setType(String.valueOf(bigSelecting.getType()));
		bigSelectInfo.setTitle(bigSelecting.getTitle());
		bigSelectInfo.setBtime(bigSelecting.getCreateDate());
		
		
		
		return bigSelectInfo;
	}
	
	/**
	 * 获取用户加完big后的等级
	 * @param uid
	 * @param addBig
	 * @param ucenterFacade
	 * @param bigFacade
	 * @return
	 */
	public UserBigInfo getUserNowLevel(Long uid,float addBig,int type,UcenterFacade ucenterFacade,BigFacade bigFacade){
		int level = 0;
		float nextBig = 0;
		float userBig= 0;
		int pk = 0;
		UserBigInfo userBigInfo = new UserBigInfo();
		UserBigValue userBigValue = ucenterFacade.findUserBigValueByUserId(uid);
		if(userBigValue.getUserId() != 0){
			level = userBigValue.getBigLevel();
			if(type ==0){//其他加分
				userBig = userBigValue.getBigValue()+userBigValue.getSelfTest()+addBig;
			}else{//自测题的加分
				userBig = userBigValue.getBigValue()+addBig;
			}
			try {
				BigLevelValue bigLevelValue = bigFacade.getLevelValue(level+1);
				float levelBig = Float.valueOf(bigLevelValue.getValue());
				if(levelBig<=userBig){
					level +=1;
					bigLevelValue = bigFacade.getLevelValue(level+1);
					
					nextBig = Float.valueOf(bigLevelValue.getValue()) - userBig;
				}else{
					nextBig = levelBig - userBig;
				}
			} catch (Exception e) {
				LOG.error("big_value数据表中数据不够，应该添加了："+e.getMessage(), e.fillInStackTrace());
				nextBig = 0;
			}
			
			//pk
			pk = ucenterFacade.findUserBeatPercent(addBig);
		}	
		
		userBigInfo.setId(uid);
		userBigInfo.setLevel(level);
		userBigInfo.setBig((int)addBig);
		userBigInfo.setTotalBig((int)userBig);
		userBigInfo.setNextBig((int)nextBig);
		userBigInfo.setPk(pk+"%");
		
		return userBigInfo;
	}

}
