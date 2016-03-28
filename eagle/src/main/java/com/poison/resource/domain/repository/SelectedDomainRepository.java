package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.OperationSelectedDAO;
import com.poison.resource.dao.SelectedDAO;
import com.poison.resource.model.OperationSelected;
import com.poison.resource.model.Selected;

public class SelectedDomainRepository {

	private SelectedDAO selectedDAO;

	private OperationSelectedDAO operationSelectedDAO;

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
	public List<Selected> findSelectedByScore(Long score,Integer pageSize){
		return selectedDAO.findSelectedByScore(score, pageSize);
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
	public List<Selected> findSelectedByScoreWithoutTopshow(Long score,Integer pageSize,Long starttime,Long endtime){
		return selectedDAO.findSelectedByScoreWithoutTopshow(score, pageSize, starttime, endtime);
	}
	/**
	 * 随机查询精选列表 (不包含用户和置顶的资源)
	 * @Title: findSelectedRandomWithoutTopshow 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-7-8 下午3:41:07
	 * @param @param score
	 * @param @param pageSize
	 * @param @return
	 * @return List<Selected>
	 * @throws
	 */
	public List<Selected> findSelectedRandomWithoutTopshow(Long starttime,Long endtime,Integer pageSize){
		return selectedDAO.findSelectedRandomWithoutTopshow(starttime, endtime, pageSize);
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
	public List<Selected> findSelectedByTopshow(){
		return selectedDAO.findSelectedByTopshow();
	}
	/**
	 * 查询精选列表的数量
	 * 
	 */
	public long getSelectedCount(){
		return selectedDAO.getSelectedCount();
	}
	/**
	 * 查询推荐用户
	 */
	public List<Selected> findSelectedUserByScore(Long score,Integer pageSize){
		return selectedDAO.findSelectedUserByScore(score, pageSize);
	}
	/**
	 * 根据推荐用户id查询所关联的资源
	 */
	public List<Selected> findSelectedUserResource(List<Long> userids){
		return selectedDAO.findSelectedUserResource(userids);
	}
	/**
	 * 根据资源id和资源类型查询
	 */
	public Selected findSelectedByResidAndType(long resid,String type){
		return selectedDAO.findSelectedByResidAndType(resid, type);
	}
	
	/**
	 * 
	 * <p>Title: findSelectedByIdOrderDesc</p> 
	 * <p>Description: 查询最新的十条信息</p> 
	 * @author :changjiang
	 * date 2015-8-7 下午5:45:54
	 * @return
	 */
	public List<Selected> findSelectedByIdOrderDesc(){
		return selectedDAO.findSelectedByIdOrderDesc();
	}
	
	/**
	 * 
	 * <p>Title: findSelectedByMiddle</p> 
	 * <p>Description: 查询中间的信息</p> 
	 * @author :changjiang
	 * date 2015-8-10 下午6:26:19
	 * @param firstIndex
	 * @param secondIndex
	 * @return
	 */
	public List<Selected> findSelectedByMiddle(int firstIndex, int secondIndex,int pageSize,long timeSeparation){
		return selectedDAO.findSelectedByMiddle(firstIndex, secondIndex, pageSize, timeSeparation);
	}
	
	/**
	 * 
	 * <p>Title: findSelectedOrderId</p> 
	 * <p>Description: 查询大于id的信息</p> 
	 * @author :changjiang
	 * date 2015-8-10 下午6:27:52
	 * @param bigIndex
	 * @return
	 */
	public List<Selected> findSelectedOrderId(int bigIndex){
		return selectedDAO.findSelectedOrderId(bigIndex);
	}

	/**
	 * 查询运营精选中间id
	 * @param firstIndex
	 * @param secondIndex
	 * @param pageSize
	 * @param timeSeparation
	 * @return
	 */
	public List<OperationSelected> findOperationSelectedByMiddle(int firstIndex,int secondIndex,int pageSize,long timeSeparation){
		return operationSelectedDAO.findOperationSelectedByMiddle(firstIndex, secondIndex, pageSize, timeSeparation);
		}

	/**
	 * 查询大于运营精选的id
	 * @param bigIndex
	 * @return
	 */
	public List<OperationSelected> findOperationSelectedOrderId(int bigIndex) {
		return operationSelectedDAO.findOperationSelectedOrderId(bigIndex);
	}

	/**
	 * 查看最新的运营
	 * @return
	 */
	public List<OperationSelected> findOperationSelectedByIdOrderDesc(){
		return operationSelectedDAO.findOperationSelectedByIdOrderDesc();
	}


	public void setSelectedDAO(SelectedDAO selectedDAO) {
		this.selectedDAO = selectedDAO;
	}

	public void setOperationSelectedDAO(OperationSelectedDAO operationSelectedDAO) {
		this.operationSelectedDAO = operationSelectedDAO;
	}
}