package com.poison.resource.service;

import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.model.Topic;
import com.poison.resource.model.TopicLink;

public interface TopicService {

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
	public Topic insertTopic(Topic topic);
	
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
	public Topic deleteTopic(Long id,Long userId);
	
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
	public Topic updateTopic(String cover,String tags,String description,long id,long userid);
	/**
	 *  根据话题id查询话题记录
	 */
	public Topic findTopicByID(long id);
	/**
	 * 增加阅读量
	 */
	public Topic addTopicReadcount(Topic topic);
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
	public List<Topic> findTopicsByTopicIds(List<Long> topicids);
	/**
	 * 根据讨论数查询话题排行榜
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
	public List<Topic> findTopicRanking(long starttime,long endtime,long start);
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
	public List<Topic> findTopicRankingAllTime(long start,int pagesize);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是关联一个话题与资源
	 * @param bookLink
	 * @return
	 */
	public TopicLink addTopicLink(TopicLink topicLink);
	/**
	 * 根据话题id查询话题关联列表
	 * @Title: findTopicLinkInfoByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-15 下午6:03:46
	 * @param @param topicid
	 * @param @param userid
	 * @param @param pageSize
	 * @param @return
	 * @return List<TopicLink>
	 * @throws
	 */
	public List<TopicLink> findTopicLinkInfoByTopicid(long topicid,Integer isOperation,Long resId,Integer pageSize);
	/**
	 * 
	 * 方法的描述 :根据话题id和资源id查询记录
	 * @param topicLink
	 * @return
	 */
	public TopicLink findTopicIsExist(TopicLink topicLink);
	/**
	 * 
	 * 方法的描述 :删除一个话题关联
	 * @param mvLink
	 * @return
	 */
	public TopicLink deleteTopicLink(TopicLink topicLink);
	/**
	 * 根据资源id和用户id删除一个话题关联
	 * @Title: deleteTopicLinkByResid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-22 下午4:50:04
	 * @param @param topicLink
	 * @param @return
	 * @return int
	 * @throws
	 */
	public boolean deleteTopicLinkByResid(TopicLink topicLink);
	/**
	 * 
	 * 方法的描述 :增加赞的数量
	 * @param topicLink
	 * @return
	 */
	public TopicLink addTopicLinkPraisecount(TopicLink topicLink);
	/**
	 * 
	 * 方法的描述 :增加评论的数量
	 * @param topicLink
	 * @return
	 */
	public TopicLink addTopicLinkCommentcount(TopicLink topicLink);
	/**
	 * 
	 * 方法的描述 :查询单个关联信息根据id
	 * @param id
	 * @return
	 */
	public TopicLink findTopicLinkById(long id);
	/**
	 * 根据话题id查询话题相关联的数量
	 * topicid
	 */
	public long getTopicLinkCountByTopicId(long topicid);
	/**
	 * 根据话题id查询某个话题关联列表按赞的数量排序
	 * @Title: findTopicLinkInfoByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-15 下午6:03:46
	 * @param @param topicid
	 * @param @param userid
	 * @param @param pageSize
	 * @param @return
	 * @return List<TopicLink>
	 * @throws
	 */
	public List<TopicLink> findTopicLinkByTopicidOrderbyPraisecount(long topicid,Integer pageSize);
	/**
	 * 根据话题id查询某个话题关联列表按评论的数量排序
	 * @Title: findTopicLinkInfoByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-15 下午6:03:46
	 * @param @param topicid
	 * @param @param userid
	 * @param @param pageSize
	 * @param @return
	 * @return List<TopicLink>
	 * @throws
	 */
	public List<TopicLink> findTopicLinkByTopicidOrderbyCommentcount(long topicid,Integer pageSize);
	
	/**
	 * 保存话题或话题关联
	 * @Title: saveTopicOrLink 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-16 下午2:23:01
	 * @param @param topic
	 * @param @param topicLink
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public Topic saveTopicOrLink(Topic topic,TopicLink topicLink);
	/**
	 * 根据资源id更新最新更新时间
	 * @param topicLink
	 * @return
	 */
	public int updateTopicLinkLatestrevisiondateByResid(TopicLink topicLink);
}
