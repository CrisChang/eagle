package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.SelectedDomainRepository;
import com.poison.resource.model.OperationSelected;
import com.poison.resource.model.Selected;
import com.poison.resource.service.SelectedService;

public class SelectedServiceImpl implements SelectedService{

	private SelectedDomainRepository selectedDomainRepository;

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
		return selectedDomainRepository.findSelectedByScore(score, pageSize);
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
		return selectedDomainRepository.findSelectedByScoreWithoutTopshow(score, pageSize,starttime,endtime);
	}
	/**
	 * 随机查询精选列表 (不包含用户和置顶的资源)
	 */
	@Override
	public List<Selected> findSelectedRandomWithoutTopshow(Long starttime,Long endtime,Integer pageSize){
		return selectedDomainRepository.findSelectedRandomWithoutTopshow(starttime,endtime,pageSize);
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
		return selectedDomainRepository.findSelectedByTopshow();
	}
	/**
	 * 查询精选列表的数量
	 * 
	 */
	@Override
	public long getSelectedCount(){
		return selectedDomainRepository.getSelectedCount();
	}
	/**
	 * 查询推荐用户
	 */
	@Override
	public List<Selected> findSelectedUserByScore(Long score,Integer pageSize){
		return selectedDomainRepository.findSelectedUserByScore(score, pageSize);
	}
	/**
	 * 根据推荐用户id查询所关联的资源
	 */
	@Override
	public List<Selected> findSelectedUserResource(List<Long> userids){
		return selectedDomainRepository.findSelectedUserResource(userids);
	}
	/**
	 * 根据资源id和资源类型查询
	 */
	@Override
	public Selected findSelectedByResidAndType(long resid,String type){
		return selectedDomainRepository.findSelectedByResidAndType(resid, type);
	}
	public void setSelectedDomainRepository(
			SelectedDomainRepository selectedDomainRepository) {
		this.selectedDomainRepository = selectedDomainRepository;
	}
	
	/**
	 * 查询最新的十条信息
	 */
	@Override
	public List<Selected> findSelectedByIdOrderDesc() {
		return selectedDomainRepository.findSelectedByIdOrderDesc();
	}
	
	/**
	 * 查看中间的精选信息
	 */
	@Override
	public List<Selected> findSelectedByMiddle(int firstIndex, int secondIndex,int pageSize,long timeSeparation) {
		return selectedDomainRepository.findSelectedByMiddle(firstIndex, secondIndex,pageSize,timeSeparation);
	}
	
	/**
	 * 查看超过id的信息
	 */
	@Override
	public List<Selected> findSelectedOrderId(int bigIndex) {
		return selectedDomainRepository.findSelectedOrderId(bigIndex);
	}

	/**
	 * 查看运营之间的精选页
	 * @param firstIndex
	 * @param secondIndex
	 * @param pageSize
	 * @param timeSeparation
	 * @return
	 */
	@Override
	public List<OperationSelected> findOperationSelectedByMiddle(int firstIndex, int secondIndex, int pageSize, long timeSeparation) {
		return selectedDomainRepository.findOperationSelectedByMiddle(firstIndex, secondIndex, pageSize, timeSeparation);
	}

	/**
	 * 查询大于运营的id
	 * @param bigIndex
	 * @return
	 */
	@Override
	public List<OperationSelected> findOperationSelectedOrderId(int bigIndex) {
		return selectedDomainRepository.findOperationSelectedOrderId(bigIndex);
	}

	/**
	 * 查询最新的运营添加
	 * @return
	 */
	@Override
	public List<OperationSelected> findOperationSelectedByIdOrderDesc() {
		return selectedDomainRepository.findOperationSelectedByIdOrderDesc();
	}
}
