package com.poison.ucenter.dao;

import com.poison.ucenter.model.ThirdPartyLogin;

public interface ThirdPartyDAO {

	/**
	 * 
	 * <p>Title: insertThirdParty</p> 
	 * <p>Description: 插入第三方信息</p> 
	 * @author :changjiang
	 * date 2014-12-16 下午6:29:59
	 * @param thirdParty
	 * @return
	 */
	public int insertThirdParty(ThirdPartyLogin thirdParty);
	
	/**
	 * 
	 * <p>Title: findThirdPartyByOpenIdAndLoginResource</p> 
	 * <p>Description: 根据openid和登录信息查询</p> 
	 * @author :changjiang
	 * date 2014-12-17 上午11:30:46
	 * @param openId
	 * @param loginResource
	 * @return
	 */
	public ThirdPartyLogin findThirdPartyByOpenIdAndLoginResource(String openId,String loginResource);
	
	/**
	 * 
	 * <p>Title: updateThirdParty</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-12-17 下午1:10:46
	 * @param thirdParty
	 * @return
	 */
	public int updateThirdParty(ThirdPartyLogin thirdParty);
}
