package com.poison.resource.client.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.resource.client.SelectedResFacade;
import com.poison.resource.model.SelectedRes;
import com.poison.resource.service.SelectedResService;

public class SelectedResFacadeImpl implements SelectedResFacade{
	private static final  Log LOG = LogFactory.getLog(SelectedResFacadeImpl.class);
	private SelectedResService selectedResService;
	
	public void setSelectedResService(SelectedResService selectedResService) {
		this.selectedResService = selectedResService;
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
		return selectedResService.findSelectedResByScore(type,restype,score, pageSize);
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
		return selectedResService.findSelectedResByScoreWithoutTopshow(type,restype, score, pageSize);
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
		return selectedResService.findSelectedResById(type,restype, id, pageSize);
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
		return selectedResService.findSelectedResByIdWithoutTopshow(type,restype, id, pageSize);
	}
	
	/**
	 * 根据置顶值排序 查询精选资源列表 (只是置顶的资源)
	 */
	@Override
	public List<SelectedRes> findSelectedResByTopshow(String type,String restype){
		return selectedResService.findSelectedResByTopshow(type,restype);
	}
	
	/**
	 * 查询精选资源的数量根据资源类型
	 * 
	 */
	@Override
	public long getSelectedResCountByType(String type,String restype){
		return selectedResService.getSelectedResCountByType(type,restype);
	}
	
	/**
	 * 根据资源id和资源类型查询
	 */
	@Override
	public SelectedRes findSelectedResByResidAndType(long resid,String restype,String type){
		return selectedResService.findSelectedResByResidAndType(resid,restype, type);
	}
}
