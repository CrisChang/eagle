package com.poison.resource.service;

import java.util.List;

import com.poison.resource.model.OperationSelected;
import com.poison.resource.model.Selected;

public interface SelectedService {

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
	public List<Selected> findSelectedByScore(Long score,Integer pageSize);
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
	public List<Selected> findSelectedByScoreWithoutTopshow(Long score,Integer pageSize,Long starttime,Long endtime);
	/**
	 * 
	 * @Title: findSelectedRandomWithoutTopshow 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-7-17 上午11:23:04
	 * @param @param pageSize
	 * @param @return
	 * @return List<Selected>
	 * @throws
	 */
	public List<Selected> findSelectedRandomWithoutTopshow(Long starttime,Long endtime,Integer pageSize);
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
	public List<Selected> findSelectedByTopshow();
	/**
	 * 查询精选列表的数量
	 * 
	 */
	public long getSelectedCount();
	/**
	 * 查询推荐用户
	 */
	public List<Selected> findSelectedUserByScore(Long score,Integer pageSize);
	/**
	 * 根据推荐用户id查询所关联的资源
	 */
	public List<Selected> findSelectedUserResource(List<Long> userids);
	/**
	 * 根据资源id和资源类型查询
	 */
	public Selected findSelectedByResidAndType(long resid,String type);
	
	/**
	 * 
	 * <p>Title: findSelectedByIdOrderDesc</p> 
	 * <p>Description: 查询最新的十条信息</p> 
	 * @author :changjiang
	 * date 2015-8-7 下午5:46:48
	 * @return
	 */
	public List<Selected> findSelectedByIdOrderDesc();
	
	/**
	 * 
	 * <p>Title: findSelectedByMiddle</p> 
	 * <p>Description: 查看中间的id信息</p> 
	 * @author :changjiang
	 * date 2015-8-10 下午6:29:03
	 * @param firstIndex
	 * @param secondIndex
	 * @return
	 */
	public List<Selected> findSelectedByMiddle(int firstIndex, int secondIndex,int pageSize,long timeSeparation);
	
	/**
	 * 
	 * <p>Title: findSelectedOrderId</p> 
	 * <p>Description: 查看大于id的中间信息</p> 
	 * @author :changjiang
	 * date 2015-8-10 下午6:29:53
	 * @param bigIndex
	 * @return
	 */
	public List<Selected> findSelectedOrderId(int bigIndex);

	/**
	 * 查询运营精选中间的id
	 * @param firstIndex
	 * @param secondIndex
	 * @param pageSize
	 * @param timeSeparation
	 * @return
	 */
	public List<OperationSelected> findOperationSelectedByMiddle(int firstIndex,int secondIndex,int pageSize,long timeSeparation);

	/**
	 * 查询大于运营精选的id
	 * @param bigIndex
	 * @return
	 */
	public List<OperationSelected> findOperationSelectedOrderId(int bigIndex);

	/**
	 * 查询
	 * @return
	 */
	public List<OperationSelected> findOperationSelectedByIdOrderDesc();
}
