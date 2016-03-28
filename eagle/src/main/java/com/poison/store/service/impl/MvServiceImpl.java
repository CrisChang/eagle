package com.poison.store.service.impl;

import java.util.List;

import com.poison.store.domain.repository.MvDomainRepository;
import com.poison.store.model.MvInfo;
import com.poison.store.service.MvService;

public class MvServiceImpl implements MvService{
	private MvDomainRepository mvDomainRepository;
	
	public void setMvDomainRepository(MvDomainRepository mvDomainRepository) {
		this.mvDomainRepository = mvDomainRepository;
	}
	/**
	 * 根据id查询电影信息
	 */
	@Override
	public MvInfo queryById(long id) {
		return mvDomainRepository.queryById(id);
	}
	/**
	 * 根据名称精准、模糊查询
	 */
	@Override
	public MvInfo findMvInfoByName(String name,String releaseDate) {
		MvInfo info = new MvInfo();
		info.setName(name);
		info.setReleaseDate(releaseDate);
		return mvDomainRepository.findMvInfoByName(info);
	}
	
	/**
	 * 插入电影信息
	 */
	@Override
	public long insertMvInfo(MvInfo mvInfo) {
		return mvDomainRepository.insertMvInfo(mvInfo);
	}
	/**
	 * 根据movieUrl查询电影信息
	 */
	@Override
	public MvInfo queryByMvURL(String movieUrl) {
		return mvDomainRepository.queryByMvURL(movieUrl);
	}
	
	/**
	 * 更新电影的简介
	 */
	@Override
	public void updateMvInfoDescribe(long id, String describe) {
		mvDomainRepository.updateMvInfoDescribe(id, describe);
	}
	
	public void updateMvInfoReleaseDateSort(long id, long releaseDateSort){
		mvDomainRepository.updateMvInfoReleaseDateSort(id, releaseDateSort);
	}
	
	/**
	 * 更新电影的演员
	 */
	@Override
	public void updateMvInfoActor(long id, String actor) {
		mvDomainRepository.updateMvInfoActor(id, actor);
	}
	
	/**
	 * 根据id集合查询电影信息集合
	 */
	public List<MvInfo> findMvInfosByIds(long ids[]){
		return mvDomainRepository.findMvInfosByIds(ids);
	}

}
