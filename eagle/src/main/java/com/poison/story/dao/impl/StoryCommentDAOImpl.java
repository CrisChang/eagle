package com.poison.story.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.story.dao.IStoryCommentDAO;
import com.poison.story.model.StoryComment;

public class StoryCommentDAOImpl extends SqlMapClientDaoSupport implements IStoryCommentDAO{

	@Override
	public int insertStoryComment(StoryComment storyComment) {
		int result = ResultUtils.SUCCESS;
		try {
			getSqlMapClientTemplate().insert("insertStoryComment", storyComment);
		} catch (Exception e) {
			result=ResultUtils.ERROR;
		}
		return result;
	}

	@Override
	public List<StoryComment> findStoryCommentList(StoryComment queryStoryComment) {
		List<StoryComment>  storyComments = new ArrayList<>();
		try {
			storyComments=getSqlMapClientTemplate().queryForList("queryStoryCommentList", queryStoryComment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return storyComments;
	}

	@Override
	public StoryComment queryStoryCommentById(long resid) {
		StoryComment storyComment = new StoryComment();
		try{
			storyComment = (StoryComment) getSqlMapClientTemplate().queryForObject("selectStoryCommentById", resid);
		}catch(Exception e){
			e.printStackTrace();
		}
		return storyComment;
	}

}
