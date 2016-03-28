package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.TopicDomainRepository;
import com.poison.resource.model.Topic;
import com.poison.resource.model.TopicLink;
import com.poison.resource.service.TopicService;

public class TopicServiceImpl implements TopicService{

	private TopicDomainRepository topicDomainRepository;

	public void setTopicDomainRepository(TopicDomainRepository topicDomainRepository) {
		this.topicDomainRepository = topicDomainRepository;
	}

	@Override
	public Topic insertTopic(Topic topic) {
		return topicDomainRepository.insertTopic(topic);
	}
	@Override
	public Topic findTopic(String title) {
		return topicDomainRepository.findTopic(title);
	}

	@Override
	public List<Topic> findTopicsByUserId(Long userId) {
		return topicDomainRepository.findTopicsByUserId(userId);
	}

	@Override
	public Topic deleteTopic(Long id, Long userId) {
		return topicDomainRepository.deleteTopic(id, userId);
	}

	@Override
	public Topic updateTopic(String cover,String tags,String description,long id,long userid) {
		return topicDomainRepository.updateTopic(cover,tags,description,id,userid);
	}
	@Override
	public Topic findTopicByID(long id){
		return topicDomainRepository.findTopicByID(id);
	}
	@Override
	public Topic addTopicReadcount(Topic topic) {
		return topicDomainRepository.addTopicReadcount(topic);
	}
	
	@Override
	public TopicLink addTopicLink(TopicLink topicLink) {
		return topicDomainRepository.addTopicLink(topicLink);
	}

	@Override
	public List<TopicLink> findTopicLinkInfoByTopicid(long topicid,Integer isOperation,Long resId,
			Integer pageSize) {
		return topicDomainRepository.findTopicLinkInfoByTopicid(topicid,isOperation,resId, pageSize);
	}

	@Override
	public TopicLink findTopicIsExist(TopicLink topicLink) {
		return topicDomainRepository.findTopicIsExist(topicLink);
	}

	@Override
	public TopicLink deleteTopicLink(TopicLink topicLink) {
		return topicDomainRepository.deleteTopicLink(topicLink);
	}

	@Override
	public TopicLink addTopicLinkPraisecount(TopicLink topicLink) {
		return topicDomainRepository.addTopicLinkPraisecount(topicLink);
	}

	@Override
	public TopicLink addTopicLinkCommentcount(TopicLink topicLink) {
		return topicDomainRepository.addTopicLinkCommentcount(topicLink);
	}

	@Override
	public TopicLink findTopicLinkById(long id) {
		return topicDomainRepository.findTopicLinkById(id);
	}

	@Override
	public long getTopicLinkCountByTopicId(long topicid) {
		return topicDomainRepository.getTopicLinkCountByTopicId(topicid);
	}

	@Override
	public List<TopicLink> findTopicLinkByTopicidOrderbyPraisecount(
			long topicid, Integer pageSize) {
		return topicDomainRepository.findTopicLinkByTopicidOrderbyPraisecount(topicid, pageSize);
	}

	@Override
	public List<TopicLink> findTopicLinkByTopicidOrderbyCommentcount(
			long topicid, Integer pageSize) {
		return topicDomainRepository.findTopicLinkByTopicidOrderbyCommentcount(topicid, pageSize);
	}

	@Override
	public Topic saveTopicOrLink(Topic topic, TopicLink topicLink) {
		return topicDomainRepository.saveTopicOrLink(topic, topicLink);
	}

	@Override
	public boolean deleteTopicLinkByResid(TopicLink topicLink) {
		return topicDomainRepository.deleteTopicLinkByResid(topicLink);
	}
	/**
	 * 根据id集合查询话题（包含讨论数）
	 * @Title: findTopicRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午12:05:14
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	@Override
	public List<Topic> findTopicsByTopicIds(List<Long> topicids){
		return topicDomainRepository.findTopicsByTopicIds(topicids);
	}
	@Override
	public List<Topic> findTopicRanking(long starttime, long endtime, long start) {
		return topicDomainRepository.findTopicRanking(starttime, endtime, start);
	}
	/**
	 * 根据讨论数查询话题排行榜---所有时间的
	 * @Title: findTopicRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午12:05:14
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	@Override
	public List<Topic> findTopicRankingAllTime(long start,int pagesize){
		return topicDomainRepository.findTopicRankingAllTime(start, pagesize);
	}
	/**
	 * 根据资源id更新最新更新时间
	 * @param topicLink
	 * @return
	 */
	@Override
	public int updateTopicLinkLatestrevisiondateByResid(TopicLink topicLink){
		return topicDomainRepository.updateTopicLinkLatestrevisiondateByResid(topicLink);
	}
}
