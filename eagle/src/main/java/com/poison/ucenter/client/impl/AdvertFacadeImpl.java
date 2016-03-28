package com.poison.ucenter.client.impl;

import com.poison.ucenter.client.AdvertFacade;
import com.poison.ucenter.model.Advert;
import com.poison.ucenter.service.AdvertService;

public class AdvertFacadeImpl implements AdvertFacade{

	private AdvertService advertService;
	
	public void setAdvertService(AdvertService advertService) {
		this.advertService = advertService;
	}
	@Override
	public Advert getAdvertInfo() {
		return advertService.getAdvertInfo();
	}
}