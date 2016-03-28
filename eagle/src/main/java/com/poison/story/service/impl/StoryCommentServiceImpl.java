package com.poison.story.service.impl;

import java.util.List;

import com.poison.story.model.StoryComment;
import com.poison.story.repository.StoryCommentRepository;
import com.poison.story.service.IStoryCommentService;

/**
 * 小说评论接口实现类
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public class StoryCommentServiceImpl implements IStoryCommentService{

	private StoryCommentRepository storyCommentRepository;

	public void setStoryCommentRepository(
			StoryCommentRepository storyCommentRepository) {
		this.storyCommentRepository = storyCommentRepository;
	}

	@Override
	public int saveStoryComment(StoryComment storyComment) {
		return storyCommentRepository.saveStoryComment(storyComment);
	}

	@Override
	public List<StoryComment> getStoryCommentList(StoryComment queryStoryComment) {
		return storyCommentRepository.getStoryCommentList(queryStoryComment);
	}

	@Override
	public StoryComment getStoryCommentById(long resid) {
		return storyCommentRepository.getStoryCommentById(resid);
	}
	
	
}
