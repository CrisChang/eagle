package com.poison.story.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.keel.utils.UKeyWorker;
import com.poison.story.model.StoryPromote;
import com.poison.story.repository.StoryPromoteRepository;
import com.poison.story.service.IStoryPromoteService;

public class StoryPromoteServiceImpl implements IStoryPromoteService {
	
	/**小说运营数据层操作类*/
	@Autowired
	private StoryPromoteRepository storyPromoteRepository;
	
	/**
	 * 主键生成器
	 */
	private UKeyWorker reskeyWork;
	
	
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	@Override
	public int saveStoryPromote(StoryPromote storyPromote) {
		initStoryPromote(storyPromote);
		return storyPromoteRepository.saveStoryPromote(storyPromote);
	}
	/**
	 * 创建一条运营信息，初始化
	 * @param storyPromote
	 */
	private void initStoryPromote(StoryPromote storyPromote) {
		storyPromote.setId(reskeyWork.getId());
		storyPromote.setDeleteFlag(0);
		Date time = new Date();
		storyPromote.setCreateTime(time);
		storyPromote.setUpdateTime(time);
	}

	@Override
	public List<StoryPromote> findStoryPromoteByCondition(StoryPromote promote, long recordStart, int pageSize) {
		return storyPromoteRepository.getStoryPromoteByCondition(promote,recordStart,pageSize);
	}

}
