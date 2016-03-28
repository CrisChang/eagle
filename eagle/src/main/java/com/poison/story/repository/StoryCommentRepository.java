package com.poison.story.repository;

import java.util.Date;
import java.util.List;

import com.keel.utils.UKeyWorker;
import com.poison.story.dao.IStoryCommentDAO;
import com.poison.story.dao.impl.StroyDAOImpl;
import com.poison.story.model.StoryComment;

public class StoryCommentRepository {
	
	private IStoryCommentDAO storyCommentDAO;
	
	private UKeyWorker reskeyWork;

	public void setStoryCommentDAO(IStoryCommentDAO storyCommentDAO) {
		this.storyCommentDAO = storyCommentDAO;
	}


	public void setReskeyWork(UKeyWorker resKeyWork) {
		this.reskeyWork = resKeyWork;
	}


	public int saveStoryComment(StoryComment storyComment) {
		initStoryComment(storyComment);
		return storyCommentDAO.insertStoryComment(storyComment);
	}
	/**初始化小说评论*/
	private void initStoryComment(StoryComment storyComment) {
		storyComment.setId(reskeyWork.nextId());
		Date date = new Date();
		storyComment.setCreateDate(date);
		storyComment.setLastVersionDate(date);
		storyComment.setIsDelete(false);
	}


	public List<StoryComment> getStoryCommentList(StoryComment queryStoryComment) {
		return storyCommentDAO.findStoryCommentList(queryStoryComment);
	}


	public StoryComment getStoryCommentById(long resid) {
		return storyCommentDAO.queryStoryCommentById(resid);
	}
	
}
