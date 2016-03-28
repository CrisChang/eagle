package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.SelectedResDAO;
import com.poison.resource.model.SelectedRes;

public class SelectedResDomainRepository {

	private SelectedResDAO selectedResDAO;

	public void setSelectedResDAO(SelectedResDAO selectedResDAO) {
		this.selectedResDAO = selectedResDAO;
	}
	/**
	 * 根据精选值排序 查询精选资源列表
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResByScore(String type,String restype,Long score,Integer pageSize){
		return selectedResDAO.findSelectedResByScore(type,restype, score, pageSize);
	}
	/**
	 * 根据精选值排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResByScoreWithoutTopshow(String type,String restype,Long score,Integer pageSize){
		return selectedResDAO.findSelectedResByScoreWithoutTopshow(type,restype,score, pageSize);
	}
	/**
	 * 根据精选id排序 查询精选资源列表
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResById(String type,String restype,Long id,Integer pageSize){
		return selectedResDAO.findSelectedResById(type,restype,id, pageSize);
	}
	
	/**
	 * 根据精选id排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResByIdWithoutTopshow(String type,String restype,Long id,Integer pageSize){
		return selectedResDAO.findSelectedResByIdWithoutTopshow(type,restype,id, pageSize);
	}
	
	/**
	 * 根据置顶值排序 查询精选资源列表 (只是置顶的资源)
	 */
	public List<SelectedRes> findSelectedResByTopshow(String type,String restype){
		return selectedResDAO.findSelectedResByTopshow(type,restype);
	}
	
	/**
	 * 查询精选资源的数量根据资源类型
	 * 
	 */
	public long getSelectedResCountByType(String type,String restype){
		return selectedResDAO.getSelectedResCountByType(type,restype);
	}
	
	/**
	 * 根据资源id和资源类型查询
	 */
	public SelectedRes findSelectedResByResidAndType(long resid,String restype,String type){
		return selectedResDAO.findSelectedResByResidAndType(resid,restype,type);
	}
}