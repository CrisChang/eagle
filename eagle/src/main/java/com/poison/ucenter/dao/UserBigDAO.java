package com.poison.ucenter.dao;

import com.poison.ucenter.model.UserBigValue;

public interface UserBigDAO {

	/**
	 * 
	 * <p>Title: insertintoUserBigValue</p> 
	 * <p>Description: 插入个人的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午4:01:29
	 * @param userBigValue
	 * @return
	 */
	public int insertintoUserBigValue(UserBigValue userBigValue);
	
	/**
	 * 
	 * <p>Title: updateUserBigValue</p> 
	 * <p>Description: 修改个人的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午4:03:47
	 * @return
	 */
	public int updateUserBigValue(UserBigValue userBigValue);
	
	/**
	 * 
	 * <p>Title: findUserBigValue</p> 
	 * <p>Description: 查找用户的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-29 下午4:17:53
	 * @return
	 */
	public UserBigValue findUserBigValue(long userId);
	
	/**
	 * 
	 * <p>Title: findUserBigBeyondCount</p> 
	 * <p>Description: 查询超出多少人</p> 
	 * @author :changjiang
	 * date 2014-10-11 下午12:52:20
	 * @param selfTest
	 * @return
	 */
	public float findUserBigBeyondCount(float selfTest);
	
	/**
	 * 
	 * <p>Title: findUserBigCount</p> 
	 * <p>Description: 查询逼格的总数</p> 
	 * @author :changjiang
	 * date 2014-10-11 下午12:53:41
	 * @return
	 */
	public float findUserBigCount();
}
