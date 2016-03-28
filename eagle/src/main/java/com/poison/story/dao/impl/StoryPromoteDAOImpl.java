package com.poison.story.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.story.dao.IStoryPromoteDAO;
import com.poison.story.model.StoryPromote;

@SuppressWarnings("deprecation")
public class StoryPromoteDAOImpl extends SqlMapClientDaoSupport implements IStoryPromoteDAO{

	@Override
	public int saveStoryPromote(StoryPromote storyPromote) {
		int result = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("saveStoryPromote", storyPromote);
			result = ResultUtils.SUCCESS;
		}catch(DataAccessException exception){
			result = ResultUtils.ERROR;
		}
		return result;
	}

	@Override
	public List<StoryPromote> getStoryPromoteByCondition(StoryPromote promote, long recordStart, int pageSize) {
		List<StoryPromote> results = new ArrayList<>();
		try {
			Map<String,Object> map = new HashMap<>();
			map.put("start", recordStart);
			map.put("pageSize", pageSize);
			map.put("promote", promote);
			results=getSqlMapClient().queryForList("getStoryPromoteByCondition", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

}
