package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.SelectedRes;

public interface SelectedResFacade {

	/**
	 * 根据精选值排序 查询精选资源列表
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResByScore(String type,String restype,Long score,Integer pageSize);
	/**
	 * 根据精选值排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param score
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResByScoreWithoutTopshow(String type,String restype,Long score,Integer pageSize);
	/**
	 * 根据精选id排序 查询精选资源列表
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResById(String type,String restype,Long id,Integer pageSize);
	
	/**
	 * 根据精选id排序 查询精选列表 不包含置顶的
	 * @param type
	 * @param id
	 * @param pageSize
	 * @return
	 */
	public List<SelectedRes> findSelectedResByIdWithoutTopshow(String type,String restype,Long id,Integer pageSize);
	
	/**
	 * 根据置顶值排序 查询精选资源列表 (只是置顶的资源)
	 */
	public List<SelectedRes> findSelectedResByTopshow(String type,String restype);
	
	/**
	 * 查询精选资源的数量根据资源类型
	 * 
	 */
	public long getSelectedResCountByType(String type,String restype);
	
	/**
	 * 根据资源id和资源类型查询
	 */
	public SelectedRes findSelectedResByResidAndType(long resid,String restype,String type);
}
