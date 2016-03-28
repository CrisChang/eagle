package com.poison.paycenter.dao;

import com.poison.paycenter.model.ControlUserStatus;

/**
 * @author Administrator
 *
 */
public interface ControlUserStatusDao {

	/**
	 * 查询用户操作的次数
	 * @param userId
	 * @return
	 */
	public ControlUserStatus selectUserStatus(long userId);

	/**
	 * 生成一条用户状态信息
	 * @param controlUserStatusNew
	 * @return
	 * @throws Exception 
	 */
	public int insertControlUserStatus(ControlUserStatus controlUserStatusNew) throws Exception;

	/**
	 * @param userId 
	 * @param count
	 * @param i
	 * @return
	 * @throws Exception
	 */
	public int updateControlUserStatus(long userId, int count, long i) throws Exception;

}
