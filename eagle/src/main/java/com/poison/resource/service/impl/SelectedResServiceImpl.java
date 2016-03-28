package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.SelectedResDomainRepository;
import com.poison.resource.model.SelectedRes;
import com.poison.resource.service.SelectedResService;

public class SelectedResServiceImpl implements SelectedResService{

	private SelectedResDomainRepository selectedResDomainRepository;
	
	public void setSelectedResDomainRepository(
			SelectedResDomainRepository selectedResDomainRepository) {
		this.selectedResDomainRepository = selectedResDomainRepository;
	}
	/**
	 * 根据精选值排序 查询精选资源列表
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResByScore(String type,String restype,Long score,Integer pageSize){
		return selectedResDomainRepository.findSelectedResByScore(type,restype, score, pageSize);
	}
	/**
	 * 根据精选值排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResByScoreWithoutTopshow(String type,String restype,Long score,Integer pageSize){
		return selectedResDomainRepository.findSelectedResByScoreWithoutTopshow(type,restype, score, pageSize);
	}
	/**
	 * 根据精选id排序 查询精选资源列表
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResById(String type,String restype,Long id,Integer pageSize){
		return selectedResDomainRepository.findSelectedResById(type,restype, id, pageSize);
	}
	
	/**
	 * 根据精选id排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<SelectedRes> findSelectedResByIdWithoutTopshow(String type,String restype,Long id,Integer pageSize){
		return selectedResDomainRepository.findSelectedResByIdWithoutTopshow(type,restype, id, pageSize);
	}
	
	/**
	 * 根据置顶值排序 查询精选资源列表 (只是置顶的资源)
	 */
	@Override
	public List<SelectedRes> findSelectedResByTopshow(String type,String restype){
		return selectedResDomainRepository.findSelectedResByTopshow(type,restype);
	}
	
	/**
	 * 查询精选资源的数量根据资源类型
	 * 
	 */
	@Override
	public long getSelectedResCountByType(String type,String restype){
		return selectedResDomainRepository.getSelectedResCountByType(type,restype);
	}
	
	/**
	 * 根据资源id和资源类型查询
	 */
	@Override
	public SelectedRes findSelectedResByResidAndType(long resid,String restype,String type){
		return selectedResDomainRepository.findSelectedResByResidAndType(resid,restype, type);
	}
}
