package com.poison.resource.client.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.utils.UKeyWorker;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.Topic;
import com.poison.resource.model.TopicLink;
import com.poison.resource.service.TopicService;

public class TopicFacadeImpl implements TopicFacade{
	private static final  Log LOG = LogFactory.getLog(TopicFacadeImpl.class);
	private TopicService topicService;
	private UKeyWorker reskeyWork;
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	
	/**
	 * 保存一个话题
	 * @Title: insertTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:18:27
	 * @param @param topic
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public Topic insertTopic(long userId,String title,String cover,String tags,String description) {
		Topic topic = new Topic();
		topic.setUserid(userId);
		topic.setTitle(title.trim());
		topic.setCover(cover);
		topic.setTags(tags);
		topic.setDescription(description);
		topic.setCreateDate(System.currentTimeMillis());
		topic.setLatestRevisionDate(System.currentTimeMillis());
		topic.setIsDelete(0);
		long id = reskeyWork.getId();
		topic.setId(id);
		return topicService.insertTopic(topic);
	}
	
	/**
	 * 根据话题名称查询话题记录
	 * @Title: findTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:18:39
	 * @param @param title
	 * @param @return
	 * @return Topic
	 * @throws
	 */
	@Override
	public Topic findTopic(String title) {
		return topicService.findTopic(title);
	}
	
	/**
	 * 根据用户id查询话题列表
	 * @Title: findTopicSByUserId 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:20:11
	 * @param @param userId
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	@Override
	public List<Topic> findTopicsByUserId(Long userId) {
		return topicService.findTopicsByUserId(userId);
	}
	
	/**
	 * 根据活动id和用户id删除一个话题
	 * @Title: deleteTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:22:49
	 * @param @param id
	 * @param @param userId
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public Topic deleteTopic(Long id, Long userId) {
		return topicService.deleteTopic(id, userId);
	}
	
	/**
	 * 编辑一个话题
	 * @Title: updateTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:23:47
	 * @param @param topic
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public Topic updateTopic(String cover,String tags,String description,long id,long userid) {
		return topicService.updateTopic(cover,tags,description,id,userid);
	}
	/**
	 *  根据话题id查询话题记录
	 */
	@Override
	public Topic findTopicByID(long id){
		return topicService.findTopicByID(id);
	}
	@Override
	public Topic addTopicReadcount(long id,Long readuid) {
		Topic topic = new Topic();
		topic.setId(id);
		topic.setLatestRevisionDate(System.currentTimeMillis());
		int falsereading = ResRandomUtils.RandomIntWithChance();
		if(readuid!=null && readuid==46){
			falsereading = ResRandomUtils.BigRandomIntValue();
		}
		topic.setFalsereading(falsereading);
		return topicService.addTopicReadcount(topic);
	}
	
	@Override
	public List<TopicLink> findTopicLinkInfoByTopicid(long topicid,Long resId,
			Integer pageSize) {
		return topicService.findTopicLinkInfoByTopicid(topicid,null,resId, pageSize);
	}
	@Override
	public List<TopicLink> findTopicLinkInfoByTopicid(long topicid,Integer isOperation,Long resId,
			Integer pageSize) {
		return topicService.findTopicLinkInfoByTopicid(topicid,isOperation,resId, pageSize);
	}
	@Override
	public TopicLink findTopicIsExist(long topicid, long resid) {
		TopicLink topicLink = new TopicLink();
		topicLink.setTopicid(topicid);
		topicLink.setResid(resid);
		return topicService.findTopicIsExist(topicLink);
	}
	@Override
	public TopicLink deleteTopicLink(long linkid) {
		TopicLink topicLink = new TopicLink();
		topicLink.setId(linkid);
		topicLink.setLatestRevisionDate(System.currentTimeMillis());
		return topicService.deleteTopicLink(topicLink);
	}
	@Override
	public TopicLink addTopicLinkPraisecount(long linkid) {
		TopicLink topicLink = new TopicLink();
		topicLink.setId(linkid);
		topicLink.setLatestRevisionDate(System.currentTimeMillis());
		return topicService.addTopicLinkPraisecount(topicLink);
	}
	@Override
	public TopicLink addTopicLinkCommentcount(long linkid) {
		TopicLink topicLink = new TopicLink();
		topicLink.setId(linkid);
		topicLink.setLatestRevisionDate(System.currentTimeMillis());
		return topicService.addTopicLinkCommentcount(topicLink);
	}
	@Override
	public TopicLink findTopicLinkById(long id) {
		return topicService.findTopicLinkById(id);
	}
	@Override
	public long getTopicLinkCountByTopicId(long topicid) {
		return topicService.getTopicLinkCountByTopicId(topicid);
	}
	@Override
	public List<TopicLink> findTopicLinkByTopicidOrderbyPraisecount(
			long topicid, Integer pageSize) {
		return topicService.findTopicLinkByTopicidOrderbyPraisecount(topicid, pageSize);
	}
	@Override
	public List<TopicLink> findTopicLinkByTopicidOrderbyCommentcount(
			long topicid, Integer pageSize) {
		return topicService.findTopicLinkByTopicidOrderbyCommentcount(topicid, pageSize);
	}
	@Override
	public Topic saveTopicOrLink(long userId, String title, String type,
			String cover, String tags, String description, long resid,
			String restype) {
		//创建一个话题对象
		Topic topic = new Topic();
		topic.setCover(cover);
		topic.setCreateDate(System.currentTimeMillis());
		topic.setDescription(description);
		topic.setLatestRevisionDate(System.currentTimeMillis());
		topic.setTags(tags);
		topic.setTitle(title.trim());
		topic.setUserid(userId);
		topic.setIsDelete(0);
		long id = reskeyWork.getId();
		topic.setId(id);
		//创建一个话题关联对象
		TopicLink topicLink = null;
		if(resid>0){
			topicLink = new TopicLink();
			topicLink.setCreateDate(System.currentTimeMillis());
			topicLink.setIsDel(0);
			topicLink.setLatestRevisionDate(System.currentTimeMillis());
			topicLink.setResid(resid);
			topicLink.setRestype(restype);
			topicLink.setType(type);
			topicLink.setUserid(userId);
			topicLink.setCommentcount(0);
			topicLink.setPraisecount(0);
			long linkid = reskeyWork.getId();
			topicLink.setId(linkid);
		}
		return topicService.saveTopicOrLink(topic, topicLink);
	}
	@Override
	public boolean deleteTopicLinkByResid(long resid, long userid) {
		TopicLink topicLink = new TopicLink();
		topicLink.setUserid(userid);
		topicLink.setResid(resid);
		topicLink.setLatestRevisionDate(System.currentTimeMillis());
		return topicService.deleteTopicLinkByResid(topicLink);
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
		return topicService.findTopicsByTopicIds(topicids);
	}
	@Override
	public List<Topic> findTopicRanking(long starttime, long endtime, long start) {
		return topicService.findTopicRanking(starttime, endtime, start);
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
		return topicService.findTopicRankingAllTime(start, pagesize);
	}
	/**
	 * 根据资源id更新最新更新时间
	 * @param topicLink
	 * @return
	 */
	@Override
	public int updateTopicLinkLatestrevisiondateByResid(TopicLink topicLink){
		return topicService.updateTopicLinkLatestrevisiondateByResid(topicLink);
	}
}
