package com.poison.story.client.impl;

import java.util.List;

import com.poison.story.client.IStoryCommentFacade;
import com.poison.story.model.StoryComment;
import com.poison.story.service.IStoryCommentService;
/**
 * 小说评论门面实现
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public class StoryCommentFacadeImpl implements IStoryCommentFacade{

	private IStoryCommentService storyCommentService;
	
	public void setStoryCommentService(IStoryCommentService storyCommentService) {
		this.storyCommentService = storyCommentService;
	}



	@Override
	public int saveStoryComment(StoryComment storyComment) {
		return storyCommentService.saveStoryComment(storyComment);
	}



	@Override
	public List<StoryComment> findStoryCommentList(StoryComment queryStoryComment) {
		return storyCommentService.getStoryCommentList(queryStoryComment);
	}



	@Override
	public StoryComment findStoryCommentById(long resid) {
		return storyCommentService.getStoryCommentById(resid);
	}
}
