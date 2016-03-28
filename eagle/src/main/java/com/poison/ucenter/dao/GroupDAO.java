package com.poison.ucenter.dao;

import java.util.List;

import com.poison.ucenter.model.Group;

public interface GroupDAO {

	/**
	 * 
	 * <p>Title: insertintoGroup</p> 
	 * <p>Description: 插入群组</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param group
	 * @return
	 */
	public int insertintoGroup(Group group);
	
	/**
	 * 
	 * <p>Title: findGroup</p> 
	 * <p>Description: 查询群组信息根据群组id</p> 
	 * @author :Administrator
	 * date 2015-4-29 
	 * @param groupid
	 * @return
	 */
	public Group findGroup(String groupid);
	/**
	 * 更新群组
	 * @Title: updateGroup 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-29 下午2:33:38
	 * @param @param group
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int updateGroup(Group group);
	/**
	 * 删除一个群组
	 * @Title: deleteGroup 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-29 下午2:34:26
	 * @param @param group
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int deleteGroup(Group group);
	/**
	 * 根据用户id查询组列表
	 * @Title: findGroupsByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-4 下午2:56:56
	 * @param @param uid
	 * @param @return
	 * @return List<Group>
	 * @throws
	 */
	public List<Group> findGroupsByUserid(Long uid);
	/**
	 * 根据多个群组id查询
	 * @Title: findGroupsByIds 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-4 下午3:02:17
	 * @param @param groupidList
	 * @param @return
	 * @return List<Group>
	 * @throws
	 */
	public List<Group> findGroupsByIds(List<String> groupidList);
}