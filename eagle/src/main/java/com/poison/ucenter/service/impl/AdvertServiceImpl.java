package com.poison.ucenter.service.impl;

import com.poison.ucenter.domain.repository.AdvertRepository;
import com.poison.ucenter.model.Advert;
import com.poison.ucenter.service.AdvertService;

public class AdvertServiceImpl implements AdvertService {

	private AdvertRepository advertRepository;
	
	public void setAdvertRepository(AdvertRepository advertRepository) {
		this.advertRepository = advertRepository;
	}

	@Override
	public Advert getAdvertInfo() {
		return advertRepository.getAdvertInfo();
	}
}
