package com.poison.store.service.impl;

import com.poison.store.domain.repository.MvStillsDomainRepository;
import com.poison.store.model.MovieStills;
import com.poison.store.service.MvStillsService;

public class MvStillsServiceImpl implements MvStillsService{

	private MvStillsDomainRepository mvStillsDomainRepository;
	
	public void setMvStillsDomainRepository(
			MvStillsDomainRepository mvStillsDomainRepository) {
		this.mvStillsDomainRepository = mvStillsDomainRepository;
	}

	/**
	 * 插入电影剧照
	 */
	@Override
	public int insertintoMvOlineStills(MovieStills movieStills) {
		return mvStillsDomainRepository.insertintoMvOlineStills(movieStills);
	}

	/**
	 * 根据电影id查询剧照
	 */
	@Override
	public MovieStills findMvOlineStillsByBkId(long mvId) {
		return mvStillsDomainRepository.findMvOlineStillsByBkId(mvId);
	}

	/**
	 * 更新电影剧照
	 */
	@Override
	public int updateMvStills(MovieStills movieStills) {
		return mvStillsDomainRepository.updateMvStills(movieStills);
	}

	/**
	 * 更新电影片花
	 */
	@Override
	public int updateMvOther(MovieStills movieStills) {
		return mvStillsDomainRepository.updateMvOther(movieStills);
	}

	/**
	 * 更新电影的二维码
	 */
	@Override
	public MovieStills updateMvTwoDimensionCode(MovieStills movieStills) {
		return mvStillsDomainRepository.updateMvTwoDimensionCode(movieStills);
	}

}
