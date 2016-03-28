package com.poison.story.service;

import java.util.List;

import com.poison.story.model.StoryComment;

/**
 * 销售评论service接口
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public interface IStoryCommentService {
	
	/**
	 * 保存小说评论
	 * @param storyComment
	 * @return
	 */
	int saveStoryComment(StoryComment storyComment);
	/**
	 * 获取小说评论列表	
	 * @param queryStoryComment
	 * @return
	 */
	List<StoryComment> getStoryCommentList(StoryComment queryStoryComment);
	/**
	 * 获取小说评论
	 */
	StoryComment getStoryCommentById(long resid);

}
