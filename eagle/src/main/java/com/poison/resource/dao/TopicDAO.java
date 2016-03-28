package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Topic;

public interface TopicDAO {

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
	public int insertTopic(Topic topic);
	
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
	public Topic findTopic(String title);
	
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
	public List<Topic> findTopicsByUserId(Long userId);
	
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
	public int deleteTopic(Long id,Long userId);
	
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
	public int updateTopic(Topic topic);
	/**
	 *  根据话题id查询话题记录
	 */
	public Topic findTopicByID(long id);
	/**
	 * 增加阅读量
	 */
	public int addTopicReadcount(Topic topic);
	/**
	 * 根据讨论数查询话题排行(只查询出了话题id和讨论数)
	 * @Title: findTopicRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 上午11:34:02
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	public List<Topic> findTopicRanking(long starttime,long endtime,long start);
	/**
	 * 排除一些话题id根据讨论数查询话题排行(只查询出了话题id和讨论数)
	 * @Title: findTopicRankingExceptTopicids 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-6-26 上午11:23:33
	 * @param @param topicids
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	public List<Topic> findTopicRankingExceptTopicids(List<Long> topicids,long starttime,long endtime,long start,int pagesize);
	/**
	 * 根据话题id集合查询话题列表 
	 */
	public List<Topic> findTopicsByIds(List<Long> topicids);
	/**
	 * 根据排行分值查询话题列表
	 */
	public List<Topic> findTopicsOrderbyScore(long start,int pageSize);
	/**
	 * 根据排行分值查询话题列表--全部
	 */
	public List<Topic> findTopicsOrderbyAllScore(long start,int pageSize);
	/**
	 * 根据话题集合查询讨论数
	 */
	public List<Topic> findTopicTalkCount(List<Long> topicids);
	/**
	 * 根据总讨论数查询话题排行，排除一些话题id的
	 */
	public List<Topic> findTopicRankingByAllTalkCount(List<Long> topicids,long start,int pageSize);
	/**
	 * 查询出规定时间内有讨论的话题的id
	 */
	public List<Topic> findTopicRankingAll(long starttime,long endtime);
	/**
	 * 查询话题有排行分值的话题id集合
	 */
	public List<Topic> findScoreTopicIds();
	/**
	 * 查询话题有排行分值的话题id集合--全部话题的
	 */
	public List<Topic> findAllScoreTopicIds();
	/**
	 * 查询数量根据某个时间段讨论
	 */
	public Map<String, Object> findTopicRankingCount(long starttime,long endtime);
}
