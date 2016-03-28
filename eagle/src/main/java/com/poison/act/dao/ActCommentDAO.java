package com.poison.act.dao;

import java.util.List;

import com.poison.act.model.ActComment;

public interface ActCommentDAO {

	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入评论信息</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertComment(ActComment actComment);
	
	/**
	 * 
	 * <p>Title: findComment</p> 
	 * <p>Description: 查找评论列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午12:29:00
	 * @param userId
	 * @return
	 */
	public List<ActComment> findComment(long resourceId,Long id);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询评论总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public int findCommentCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: findCommentById</p> 
	 * <p>Description: 查询评论信息</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActComment findCommentById(ActComment actComment);
	
	/**
	 * 
	 * <p>Title: findCmtById</p> 
	 * <p>Description: 根据id查询</p> 
	 * @author :changjiang
	 * date 2015-3-10 上午10:22:26
	 * @param id
	 * @return
	 */
	public ActComment findCmtById(long id);
	
	/**
	 * 
	 * <p>Title: updateComment</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午9:53:26
	 * @return
	 */
	public int updateComment(ActComment actComment);
	
	/**
	 * 
	 * <p>Title: findUserCommentCenter</p> 
	 * <p>Description: 查询用户的评论中心</p> 
	 * @author :changjiang
	 * date 2015-3-9 下午7:33:07
	 * @param userId
	 * @param id
	 * @return
	 */
	public List<ActComment> findUserCommentCenter(long userId,Long id);
	
	/**
	 * 
	 * <p>Title: delCommentById</p> 
	 * <p>Description: 删除评论</p> 
	 * @author :changjiang
	 * date 2015-6-12 上午11:09:59
	 * @param id
	 * @return
	 */
	public int delCommentById(long id);
}
