package com.poison.ucenter.client.impl;

import com.poison.ucenter.client.ThirdPartyFacade;
import com.poison.ucenter.model.ThirdPartyLogin;
import com.poison.ucenter.service.ThirdPartyService;


public class ThirdPartyFacadeImpl implements ThirdPartyFacade{

	private ThirdPartyService thirdPartyService;
	
	public void setThirdPartyService(ThirdPartyService thirdPartyService) {
		this.thirdPartyService = thirdPartyService;
	}


	@Override
	public ThirdPartyLogin insertThirdParty(String openid, String nickname,
			int gender, String location, String country, String headimgurl,
			String description, String other, String loginSource, String type,
			String pushToken, String phoneModel,long uid) {
			ThirdPartyLogin thirdPartyLogin = new ThirdPartyLogin();
			long sysdate = System.currentTimeMillis();
			thirdPartyLogin.setOpenId(openid);
			thirdPartyLogin.setNickName(nickname);
			thirdPartyLogin.setGender(gender);
			thirdPartyLogin.setLocation(location);
			thirdPartyLogin.setCountry(country);
			thirdPartyLogin.setHeadImgUrl(headimgurl);
			thirdPartyLogin.setDescription(description);
			thirdPartyLogin.setOther(other);
			thirdPartyLogin.setLoginSource(loginSource);
			thirdPartyLogin.setType(type);
			thirdPartyLogin.setCreateDate(sysdate);
			thirdPartyLogin.setPushToken(pushToken);
			thirdPartyLogin.setPhoneModel(phoneModel);
			return thirdPartyService.insertThirdParty(thirdPartyLogin,uid);
	}


	@Override
	public ThirdPartyLogin findThirdPartyByOpenIdAndLoginResource(
			String openId, String loginResource) {
		return thirdPartyService.findThirdPartyByOpenIdAndLoginResource(openId, loginResource);
	}
}
