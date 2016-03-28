package com.poison.ucenter.dao;

import com.poison.ucenter.model.UserLatest;

public interface UserLatestDAO {
	
	/**
	 * 插入一条信息 
	 */
	public int insertUserlatest(UserLatest userlatest);
	/**
	 * 修改信息
	 * @Title: updateUserlatest 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-6-17 下午5:09:55
	 * @param @param userlatest
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int updateUserlatest(UserLatest userlatest);
	/**
	 * 根据userid查询
	 */
	public UserLatest findUserlatestByUserid(long userid);
}
