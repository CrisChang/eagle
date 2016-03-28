package com.poison.story.repository;

import java.util.List;

import org.springframework.transaction.support.TransactionTemplate;

import com.poison.story.dao.IStoryPromoteDAO;
import com.poison.story.model.StoryPromote;

/**
 * 小说运营数据库处理类
 * @author songdan
 * @date 2016年2月26日 
 * @time 上午11:01:31
 * @version 1.0
 */
public class StoryPromoteRepository {

	private IStoryPromoteDAO storyPromoteDAO;
	
	private TransactionTemplate transactionTemplate;

	public void setStoryPromoteDAO(IStoryPromoteDAO storyPromoteDAO) {
		this.storyPromoteDAO = storyPromoteDAO;
	}
	
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}



	public int saveStoryPromote(StoryPromote storyPromote){
		return storyPromoteDAO.saveStoryPromote(storyPromote);
	}


	public List<StoryPromote> getStoryPromoteByCondition(StoryPromote promote, long recordStart, int pageSize) {
		return storyPromoteDAO.getStoryPromoteByCondition(promote,recordStart,pageSize);
	}
}
