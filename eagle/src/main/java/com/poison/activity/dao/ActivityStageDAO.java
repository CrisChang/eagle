package com.poison.activity.dao;

import com.poison.activity.model.ActivityStage;

public interface ActivityStageDAO {
	/**
	 * 查找最新的有效活动阶段信息
	 * @return
	 */
	public ActivityStage findLatestActivityStage(String type);
	/**
	 * 根据状态查询最新的活动阶段信息
	 */
	public ActivityStage findLatestActivityStageByState(String type,int state);
}
