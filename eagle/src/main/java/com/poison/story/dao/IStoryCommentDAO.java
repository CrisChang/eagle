package com.poison.story.dao;

import java.util.List;

import com.poison.story.model.StoryComment;

/**
 * 小说评论DAO层接口
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public interface IStoryCommentDAO {
	/**
	 * 插入一条小说评论
	 * @param storyComment
	 * @return
	 */
	int insertStoryComment(StoryComment storyComment);
	/**
	 * 查询小说评论列表
	 * @param queryStoryComment
	 * @return
	 */
	List<StoryComment> findStoryCommentList(StoryComment queryStoryComment);
	/**
	 * 查询小说评论
	 * @param resid
	 * @return
	 */
	StoryComment queryStoryCommentById(long resid);

}
