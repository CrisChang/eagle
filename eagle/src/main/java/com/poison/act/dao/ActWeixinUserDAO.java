package com.poison.act.dao;

import com.poison.act.model.ActWeixinUser;

public interface ActWeixinUserDAO {

	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入微信用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertUser(ActWeixinUser actWeixinUser);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findCountByScore(int score);
	
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findUserCount();
	
	/**
	 * 
	 * <p>Title: findCommentById</p> 
	 * <p>Description: 查询微信用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActWeixinUser findUserById(String openid);
	
	/**
	 * 
	 * <p>Title: updateComment</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午9:53:26
	 * @return
	 */
	public int updateUser(ActWeixinUser actWeixinUser);
}
