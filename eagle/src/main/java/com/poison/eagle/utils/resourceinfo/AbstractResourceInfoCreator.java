package com.poison.eagle.utils.resourceinfo;

import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public abstract class AbstractResourceInfoCreator implements ResourceInfoCreator{

	/**
	 * 将相应的用户类转换成UserEntity
	 * @param userInfo
	 * @param type 0:添加个性签名、1：没有
	 * @return
	 */
	protected UserEntity copyUserInfo(Object userInfo , int type){
		UserEntity userEntity = new UserEntity();
		if(userInfo.getClass().getName().equals(UserAllInfo.class.getName())){
			UserAllInfo ui = (UserAllInfo) userInfo;
			userEntity.setId(ui.getUserId());
			userEntity.setNickName(ui.getName());
			userEntity.setFace_url(ui.getFaceAddress());
			userEntity.setSign(ui.getSign());
			userEntity.setIdentification(ui.getIdentification());
			int isBinding = 0;
			if(WebUtils.isPhone(ui.getMobilePhone())){
				userEntity.setIsBinding(1);
			}else {
				userEntity.setIsBinding(0);
			}
			userEntity.setSort(ui.getLastestRevisionDate());
			if(type == CommentUtils.UN_ID){
				userEntity.setSex(ui.getSex());
				userEntity.setInterest(ui.getInterest());
				userEntity.setIntroduction(ui.getIntroduction());
				userEntity.setIdentification(ui.getIdentification());
			}
			int level = ui.getLevel();
			String isOperation = ui.getIsOperation();
			//用户等级
//			if(level == CommentUtils.USER_LEVEL_NORMAL){
//				level = 0;
//			}else if(level == CommentUtils.USER_LEVEL_TALENT){
//				level = 1;
//			}
			userEntity.setType(level);
			userEntity.setIsOperation(isOperation);
			/*userEntity.setAge(ui.getAge());
			userEntity.setConstellation(ui.getConstellation());*/
		}else if(userInfo.getClass().getName().equals(UserInfo.class.getName())){
			UserInfo ui = (UserInfo) userInfo;
			userEntity.setId(ui.getUserId());
			userEntity.setNickName(ui.getName());
			userEntity.setFace_url(ui.getFaceAddress());
			userEntity.setSex(null);
			int level = ui.getLevel();
			userEntity.setSort(ui.getLastestRevisionDate());
			//用户等级
//			if(level == CommentUtils.USER_LEVEL_NORMAL){
//				level = 0;
//			}else if(level == CommentUtils.USER_LEVEL_TALENT){
//				level = 1;
//			}
			userEntity.setType(level);
		}
		return userEntity;
	}

}
