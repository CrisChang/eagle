package com.poison.ucenter.domain.repository;

import com.poison.ucenter.dao.AdvertDAO;
import com.poison.ucenter.model.Advert;

public class AdvertRepository {
	
	private AdvertDAO advertDAO;

	public void setAdvertDAO(AdvertDAO advertDAO) {
		this.advertDAO = advertDAO;
	}
	/**
	 * 
	 * <p>Title: getAdvertInfo</p> 
	 * <p>Description: 查询广告信息</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public Advert getAdvertInfo(){
		return advertDAO.getAdvertInfo();
	}
}
