package com.poison.ucenter.dao;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.poison.ucenter.model.UserSummary;

public interface UserSummaryDAO {

	/**
	 * 
	 * <p>Title: insertUserSummary</p> 
	 * <p>Description: 插入用户的简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 上午10:54:48
	 * @param productContext
	 * @param userSummary
	 * @return
	 */
	public int insertUserSummary(ProductContext productContext,UserSummary userSummary);
	
	/**
	 * 
	 * <p>Title: updateUserSummary</p> 
	 * <p>Description: 修改用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 上午10:55:50
	 * @param productContext
	 * @param userSummary
	 * @return
	 */
	public int updateUserSummary(ProductContext productContext,UserSummary userSummary);
	
	/**
	 * 
	 * <p>Title: findUserSummary</p> 
	 * <p>Description: 查询用户简介</p> 
	 * @author :changjiang
	 * date 2014-7-22 上午10:56:40
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public UserSummary findUserSummaryById(ProductContext productContext,long userId);
	/**
	 * 根据用户id集合查询用户的简介信息 
	 */
	public List<UserSummary> findUserSummarysByUserids(ProductContext productContext,List<Long> userids);
}
