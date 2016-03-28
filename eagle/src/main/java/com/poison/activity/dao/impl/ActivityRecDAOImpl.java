package com.poison.activity.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.activity.dao.ActivityRecDAO;
import com.poison.activity.model.ActivityRec;

public class ActivityRecDAOImpl extends SqlMapClientDaoSupport implements ActivityRecDAO{

	private static final  Log LOG = LogFactory.getLog(ActivityRecDAOImpl.class);
	/**
	 * 查询推荐列表
	 * @param userid
	 * @return
	 */
	@Override
	public List<ActivityRec> findActivityRec(long stageid,String restype,long start,int pagesize){
		List<ActivityRec> activityRecs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stageid", stageid);
		map.put("restype", restype);
		map.put("start", start);
		map.put("pagesize", pagesize);
		try{
			activityRecs = getSqlMapClientTemplate().queryForList("findActivityRec",map);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			activityRecs = new ArrayList<ActivityRec>();
		}
		return activityRecs;
	}
}
