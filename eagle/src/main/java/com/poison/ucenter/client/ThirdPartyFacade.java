package com.poison.ucenter.client;

import com.poison.ucenter.model.ThirdPartyLogin;

public interface ThirdPartyFacade {

	/**
	 * 
	 * <p>Title: insertThirdParty</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-12-17 下午2:07:19
	 * @param openid 第三方登录唯一号
	 * @param nickname 昵称
	 * @param gender 性别
	 * @param location 住址
	 * @param country 国家
	 * @param headimgurl 头像
	 * @param description 描述
	 * @param other 其他信息
	 * @param loginSource 来源
	 * @param pushToken 设备号
	 * @param phoneModel 手机型号
	 * @return
	 */
	public ThirdPartyLogin insertThirdParty(String openid,String nickname,int gender,String location,String country,String headimgurl,
			String description,String other,String loginSource,String type,String pushToken,String phoneModel,long uid);
	
	public ThirdPartyLogin findThirdPartyByOpenIdAndLoginResource(
			String openId, String loginResource);
}
