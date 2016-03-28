package com.poison.story.dao;

import java.util.List;

import com.poison.story.model.StoryPromote;

/**
 * 小说运营DAO接口
 * @author songdan
 * @date 2016年2月26日 
 * @time 上午10:11:41
 * @version 1.0
 */
public interface IStoryPromoteDAO{
	/**
	 * 保存一条小说运营记录
	 * @param storyPromote 小说运营记录
	 * @return 操作结果成功标识
	 */
	int saveStoryPromote(StoryPromote storyPromote);

	List<StoryPromote> getStoryPromoteByCondition(StoryPromote promote, long recordStart, int pageSize);
}
