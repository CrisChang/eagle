package com.poison.story.service;

import java.util.List;

import com.poison.story.model.StoryPromote;

/**
 * 小说运营service接口
 * @author songdan
 * @date 2016年2月26日 
 * @time 上午11:57:31
 * @version 1.0
 */
public interface IStoryPromoteService {
	
	/**
	 * 保存一条小说运营记录
	 * @param storyPromote
	 * @return 操作结果标识
	 */
	int saveStoryPromote(StoryPromote storyPromote);
	/**
	 * 通过条件查询运营记录
	 * @param promote 封装的条件
	 * @param recordStart 起始
	 * @param pageSize 页面大小
	 * @return
	 */
	List<StoryPromote> findStoryPromoteByCondition(StoryPromote promote, long recordStart, int pageSize);
}
