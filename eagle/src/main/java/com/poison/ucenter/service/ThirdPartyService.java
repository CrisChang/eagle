package com.poison.ucenter.service;

import com.poison.ucenter.model.ThirdPartyLogin;

public interface ThirdPartyService {

	/**
	 * 
	 * <p>Title: insertThirdParty</p> 
	 * <p>Description: 插入第三方信息</p> 
	 * @author :changjiang
	 * date 2014-12-17 下午1:38:24
	 * @param thirdParty
	 * @return
	 */
	public ThirdPartyLogin insertThirdParty(ThirdPartyLogin thirdParty,long uid);
	
	public ThirdPartyLogin findThirdPartyByOpenIdAndLoginResource(
			String openId, String loginResource);
}
