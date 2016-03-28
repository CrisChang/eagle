package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.TopicLink;

public interface TopicLinkDAO {
	/**
	 * 
	 * 方法的描述 :此方法的作用是关联一个话题与资源
	 * @param bookLink
	 * @return
	 */
	public int addTopicLink(TopicLink topicLink);
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
	public int deleteTopicLink(TopicLink topicLink);
	/**
	 * 根据资源id更新最新更新时间
	 * @param topicLink
	 * @return
	 */
	public int  updateTopicLinkLatestrevisiondateByResid(TopicLink topicLink);
	/**
	 * 
	 * 方法的描述 :增加赞的数量
	 * @param topicLink
	 * @return
	 */
	public int addTopicLinkPraisecount(TopicLink topicLink);
	/**
	 * 
	 * 方法的描述 :增加评论的数量
	 * @param topicLink
	 * @return
	 */
	public int addTopicLinkCommentcount(TopicLink topicLink);
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
	public int deleteTopicLinkByResid(TopicLink topicLink);
}
