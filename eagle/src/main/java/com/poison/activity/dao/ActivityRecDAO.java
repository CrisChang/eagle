package com.poison.activity.dao;

import java.util.List;

import com.poison.activity.model.ActivityRec;

public interface ActivityRecDAO {
	/**
	 * 查询推荐列表
	 * @param userid
	 * @return
	 */
	public List<ActivityRec> findActivityRec(long stageid,String restype,long start,int pagesize);
}
