package com.poison.resource.client.impl;

import java.util.List;

import com.poison.resource.model.OperationSelected;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.resource.client.SelectedFacade;
import com.poison.resource.model.Selected;
import com.poison.resource.service.SelectedService;

public class SelectedFacadeImpl implements SelectedFacade{
	private static final  Log LOG = LogFactory.getLog(SelectedFacadeImpl.class);
	private SelectedService selectedService;
	/**
	 * 根据精选值排序查询 精选列表
	 * @Title: findTopicLinkInfoByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-15 下午6:03:46
	 * @param @param topicid
	 * @param @param userid
	 * @param @param pageSize
	 * @param @return
	 * @return List<TopicLink>
	 * @throws
	 */
	@Override
	public List<Selected> findSelectedByScore(Long score,Integer pageSize){
		return selectedService.findSelectedByScore(score, pageSize);
	}
	/**
	 * 根据精选值排序 查询精选列表 (不包含用户和置顶的资源)
	 * @Title: findSelectedByScoreWithoutTopshow 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-7-8 下午3:41:07
	 * @param @param score
	 * @param @param pageSize
	 * @param @return
	 * @return List<Selected>
	 * @throws
	 */
	@Override
	public List<Selected> findSelectedByScoreWithoutTopshow(Long score,Integer pageSize,Long starttime,Long endtime){
		return selectedService.findSelectedByScoreWithoutTopshow(score, pageSize,starttime,endtime);
	}
	/**
	 * 随机查询精选列表 (不包含用户和置顶的资源)
	 */
	@Override
	public List<Selected> findSelectedRandomWithoutTopshow(Long starttime,Long endtime,Integer pageSize){
		return selectedService.findSelectedRandomWithoutTopshow(starttime,endtime,pageSize);
	}
	/**
	 * 根据置顶值排序 查询精选列表 (只是置顶的资源)
	 * @Title: findSelectedByScoreWithoutTopshow 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-7-8 下午3:41:07
	 * @param @param score
	 * @param @param pageSize
	 * @param @return
	 * @return List<Selected>
	 * @throws
	 */
	@Override
	public List<Selected> findSelectedByTopshow(){
		return selectedService.findSelectedByTopshow();
	}
	/**
	 * 查询精选列表的数量
	 * 
	 */
	@Override
	public long getSelectedCount(){
		return selectedService.getSelectedCount();
	}
	/**
	 * 查询推荐用户
	 */
	@Override
	public List<Selected> findSelectedUserByScore(Long score,Integer pageSize){
		return selectedService.findSelectedUserByScore(score, pageSize);
	}
	/**
	 * 根据推荐用户id查询所关联的资源
	 */
	@Override
	public List<Selected> findSelectedUserResource(List<Long> userids){
		return selectedService.findSelectedUserResource(userids);
	}
	/**
	 * 根据资源id和资源类型查询
	 */
	@Override
	public Selected findSelectedByResidAndType(long resid,String type){
		return selectedService.findSelectedByResidAndType(resid, type);
	}
	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}
	
	/**
	 * 查询最新的十条信息
	 */
	@Override
	public List<Selected> findSelectedByIdOrderDesc() {
		return selectedService.findSelectedByIdOrderDesc();
	}
	
	/**
	 * 查询中间的信息
	 */
	@Override
	public List<Selected> findSelectedByMiddle(int firstIndex, int secondIndex,int pageSize,long timeSeparation) {
		return selectedService.findSelectedByMiddle(firstIndex, secondIndex,pageSize,timeSeparation);
	}
	
	/**
	 * 查询大于id的信息
	 */
	@Override
	public List<Selected> findSelectedOrderId(int bigIndex) {
		return selectedService.findSelectedOrderId(bigIndex);
	}

	@Override
	public List<OperationSelected> findOperationSelectedByMiddle(int firstIndex, int secondIndex, int pageSize, long timeSeparation) {
		return selectedService.findOperationSelectedByMiddle(firstIndex, secondIndex, pageSize, timeSeparation);
	}

	@Override
	public List<OperationSelected> findOperationSelectedOrderId(int bigIndex) {
		return selectedService.findOperationSelectedOrderId(bigIndex);
	}

	/**
	 * 查询最新的运营添加
	 * @return
	 */
	@Override
	public List<OperationSelected> findOperationSelectedByIdOrderDesc() {
		return selectedService.findOperationSelectedByIdOrderDesc();
	}
}
