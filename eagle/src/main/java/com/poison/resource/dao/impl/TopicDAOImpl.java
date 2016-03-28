package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.TopicDAO;
import com.poison.resource.model.Topic;

public class TopicDAOImpl extends SqlMapClientDaoSupport implements TopicDAO{

	private static final  Log LOG = LogFactory.getLog(TopicDAOImpl.class);
	/**
	 *  保存一个话题
	 */
	@Override
	public int insertTopic(Topic topic){
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertTopic", topic);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.ERROR;
		}
		return i;
	}

	/**
	 *  根据话题名称查询话题记录
	 */
	@Override
	public Topic findTopic(String title){
		Topic topic = new Topic();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title);
		try{
			topic = (Topic) getSqlMapClientTemplate().queryForObject("findTopic",map);
			if(topic!=null){
				topic.setFlag(ResultUtils.SUCCESS);
			}else{
				topic = new Topic();
				//topic.setFlag(ResultUtils.ERROR);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topic = new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
		}
		return topic;
	}
	/**
	 *  根据话题id查询话题记录
	 */
	@Override
	public Topic findTopicByID(long id){
		Topic topic = new Topic();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try{
			topic = (Topic) getSqlMapClientTemplate().queryForObject("findTopicByID",map);
			if(topic!=null){
				topic.setFlag(ResultUtils.SUCCESS);
			}else{
				topic = new Topic();
				//topic.setFlag(ResultUtils.ERROR);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topic = new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
		}
		return topic;
	}
	
	/**
	 * 根据用户id查询话题列表
	 */
	@Override
	public List<Topic> findTopicsByUserId(Long userid) {
		List<Topic> topicList = new ArrayList<Topic>();
		Topic topic = new Topic();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		try{
			topicList = getSqlMapClientTemplate().queryForList("findTopicsByUserId",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			topic.setFlag(ResultUtils.ERROR);
			topicList.add(topic);
		}
		return topicList;
	}


	/**
	 * 根据活动id和用户id删除一个话题
	 */
	@Override
	public int deleteTopic(Long id,Long userid) {
		int flag =ResultUtils.UPDATE_ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("userid", userid);
		try{
			getSqlMapClientTemplate().update("deleteTopic",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag =ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}	
	/**
	 * 编辑一个话题
	 */
	@Override
	public int updateTopic(Topic topic) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateTopic",topic);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	@Override
	public int addTopicReadcount(Topic topic) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("addTopicReadcount",topic);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}
	
	
	@Override
	public List<Topic> findTopicRanking(long starttime,long endtime,long start) {
		List<Topic> topics=new ArrayList<Topic>();
		Topic topic=new Topic();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("start", start);
		try {
			topics=getSqlMapClientTemplate().queryForList("findTopicRanking",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topics=new ArrayList<Topic>();
			topic=new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topics.add(topic);
		}
		return topics;
	}
	
	@Override
	public List<Topic> findTopicRankingExceptTopicids(List<Long> topicids,long starttime,long endtime,long start,int pagesize) {
		List<Topic> topics=new ArrayList<Topic>();
		Topic topic=new Topic();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("start", start);
		map.put("pagesize", pagesize);
		if(topicids!=null && topicids.size()>0){
			map.put("topicids", topicids);
		}
		try {
			topics=getSqlMapClientTemplate().queryForList("findTopicRankingExceptTopicids",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topics=new ArrayList<Topic>();
			topic=new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topics.add(topic);
		}
		return topics;
	}
	
	/**
	 * 根据话题id集合查询话题列表 
	 */
	@Override
	public List<Topic> findTopicsByIds(List<Long> topicids) {
		List<Topic> topicList = new ArrayList<Topic>();
		Topic topic = new Topic();
		if(topicids==null || topicids.size()==0){
			return topicList;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("topicids", topicids);
		try{
			topicList = getSqlMapClientTemplate().queryForList("findTopicsByIds",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topicList.add(topic);
		}
		return topicList;
	}
	
	/**
	 * 根据排行分值查询话题列表
	 */
	@Override
	public List<Topic> findTopicsOrderbyScore(long start,int pageSize) {
		List<Topic> topicList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("pageSize", pageSize);
		try{
			topicList = getSqlMapClientTemplate().queryForList("findTopicsOrderbyScore",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			Topic topic = new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topicList.add(topic);
		}
		return topicList;
	}
	
	/**
	 * 根据排行分值查询话题列表--全部
	 */
	@Override
	public List<Topic> findTopicsOrderbyAllScore(long start,int pageSize) {
		List<Topic> topicList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("pageSize", pageSize);
		try{
			topicList = getSqlMapClientTemplate().queryForList("findTopicsOrderbyAllScore",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			Topic topic = new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topicList.add(topic);
		}
		return topicList;
	}
	
	/**
	 * 根据话题集合查询讨论数
	 */
	@Override
	public List<Topic> findTopicTalkCount(List<Long> topicids) {
		List<Topic> topics=new ArrayList<Topic>();
		Topic topic=new Topic();
		if(topicids==null || topicids.size()==0){
			return topics;
		}
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("topicids", topicids);
		try {
			topics=getSqlMapClientTemplate().queryForList("findTopicTalkCount",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topics=new ArrayList<Topic>();
			topic=new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topics.add(topic);
		}
		return topics;
	}
	
	/**
	 * 根据总讨论数查询话题排行，排除一些话题id的
	 */
	@Override
	public List<Topic> findTopicRankingByAllTalkCount(List<Long> topicids,long start,int pageSize) {
		List<Topic> topicList = new ArrayList<Topic>();
		Topic topic = new Topic();
		Map<String, Object> map = new HashMap<String, Object>();
		if(topicids!=null && topicids.size()>0){
			map.put("topicids", topicids);
		}
		map.put("start", start);
		map.put("pageSize", pageSize);
		try{
			topicList = getSqlMapClientTemplate().queryForList("findTopicRankingByAllTalkCount",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topicList.add(topic);
		}
		return topicList;
	}
	
	
	/**
	 * 查询出规定时间内有讨论的话题的id
	 */
	@Override
	public List<Topic> findTopicRankingAll(long starttime,long endtime) {
		List<Topic> topicList = new ArrayList<Topic>();
		Topic topic = new Topic();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try{
			topicList = getSqlMapClientTemplate().queryForList("findTopicRankingAll",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topicList.add(topic);
		}
		return topicList;
	}
	

	/**
	 * 查询话题有排行分值的话题id集合--热门话题
	 */
	@Override
	public List<Topic> findScoreTopicIds() {
		List<Topic> topicList = null;
		try{
			topicList = getSqlMapClientTemplate().queryForList("findScoreTopicIds");
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			Topic topic = new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topicList.add(topic);
		}
		return topicList;
	}
	
	/**
	 * 查询话题有排行分值的话题id集合--全部话题的
	 */
	@Override
	public List<Topic> findAllScoreTopicIds() {
		List<Topic> topicList = null;
		try{
			topicList = getSqlMapClientTemplate().queryForList("findAllScoreTopicIds");
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicList = new ArrayList<Topic>();
			Topic topic = new Topic();
			topic.setFlag(ResultUtils.QUERY_ERROR);
			topicList.add(topic);
		}
		return topicList;
	}
	
	/**
	 * 查询数量根据某个时间段讨论
	 */
	@Override
	public Map<String, Object> findTopicRankingCount(long starttime,long endtime) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		long count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Long) getSqlMapClientTemplate().queryForObject("findTopicRankingCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}
}
