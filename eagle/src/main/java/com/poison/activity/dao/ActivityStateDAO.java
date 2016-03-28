package com.poison.activity.dao;

import com.poison.activity.model.ActivityState;

public interface ActivityStateDAO {
	/**
	 * 根据userid查询参赛状态
	 * @param userid
	 * @return
	 */
	public ActivityState findActivityStateByUserid(long userid);
	/**
	 * 报名--插入报名状态信息
	 */
	public int addActivityState(ActivityState activityState);
	/**
	 * 修改报名状态信息
	 */
	public int updateActivityState(ActivityState activityState);
}
