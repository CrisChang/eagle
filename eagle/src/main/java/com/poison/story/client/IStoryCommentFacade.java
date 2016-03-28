package com.poison.story.client;

import java.util.List;

import com.poison.story.model.StoryComment;

/**
 * 小说评论门面接口
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public interface IStoryCommentFacade {
	/**
	 * 保存小说评论
	 * @param storyComment
	 * @return
	 */
	int saveStoryComment(StoryComment storyComment);
	
	/**获取小说评论列表*/
	List<StoryComment> findStoryCommentList(StoryComment queryStoryComment);
	
	/**
	 * 根据小说评论id获取评论
	 * @param resid
	 * @return
	 */
	StoryComment findStoryCommentById(long resid);

}
