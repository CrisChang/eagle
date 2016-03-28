package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.TopicLinkDAO;
import com.poison.resource.model.TopicLink;

@SuppressWarnings("deprecation")
public class TopicLinkDAOImpl extends SqlMapClientDaoSupport implements TopicLinkDAO {

	private static final  Log LOG = LogFactory.getLog(TopicLinkDAOImpl.class);
	
	@Override
	public int addTopicLink(TopicLink topicLink) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().insert("addTopicLink",topicLink);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	@Override
	public int deleteTopicLink(TopicLink topicLink) {
		try {
			getSqlMapClientTemplate().update("deleteTopicLink",topicLink);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
	
	@Override
	public int deleteTopicLinkByResid(TopicLink topicLink) {
		try {
			getSqlMapClientTemplate().update("deleteTopicLinkByResid",topicLink);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}
	@Override
	public int  updateTopicLinkLatestrevisiondateByResid(TopicLink topicLink){
		try {
			getSqlMapClientTemplate().update("updateTopicLinkLatestrevisiondateByResid",topicLink);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public List<TopicLink> findTopicLinkInfoByTopicid(long topicid,Integer isOperation,Long resId,Integer pageSize){
		List<TopicLink> tlist=new ArrayList<TopicLink>();
		TopicLink topicLink=null;
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("topicid", topicid);
		map.put("isOperation", isOperation);
		map.put("resId", resId);
		map.put("pageSize", pageSize);
		try {
			tlist=getSqlMapClientTemplate().queryForList("findTopicLinkInfoByTopicid",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tlist=new ArrayList<TopicLink>();
			topicLink=new TopicLink();
			topicLink.setFlag(ResultUtils.QUERY_ERROR);
			tlist.add(topicLink);
		}
		return tlist;
	}

	@Override
	public TopicLink findTopicIsExist(TopicLink topicLink) {
		TopicLink tk=new TopicLink();
		try {
			tk=(TopicLink)getSqlMapClientTemplate().queryForObject("findTopicIsExist",topicLink);
			if(null==tk){
				tk=new TopicLink();
				tk.setFlag(ResultUtils.DATAISNULL);
				return tk;
			}else{
				tk.setFlag(ResultUtils.SUCCESS);
				return tk;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tk=new TopicLink();
			tk.setFlag(ResultUtils.QUERY_ERROR);
			return tk;
		}
	}

	/**
	 * 增加赞的数量
	 */
	@Override
	public int addTopicLinkPraisecount(TopicLink topicLink) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("addTopicLinkPraisecount",topicLink);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}
	/**
	 * 增加评论的数量
	 */
	@Override
	public int addTopicLinkCommentcount(TopicLink topicLink) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("addTopicLinkCommentcount",topicLink);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询单个关联信息根据id
	 */
	@Override
	public TopicLink findTopicLinkById(long id) {
		TopicLink topicLink = new TopicLink();
		try{
			topicLink = (TopicLink) getSqlMapClientTemplate().queryForObject("findTopicLinkById",id);
			if(null==topicLink){
				topicLink = new TopicLink();
				topicLink.setFlag(ResultUtils.DATAISNULL);
				return topicLink;
			}
			topicLink.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			topicLink = new TopicLink();
			topicLink.setFlag(ResultUtils.ERROR);
		}
		return topicLink;
	}

	/**
	 * 查询影单中的电影数量
	 */
	@Override
	public long getTopicLinkCountByTopicId(long topicid){
		long i = 0;
		try{
			Long total = (Long) getSqlMapClientTemplate().queryForObject("getTopicLinkCountByTopicId",topicid);
			if(total!=null){
				i = total;
			}
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}

	@Override
	public List<TopicLink> findTopicLinkByTopicidOrderbyPraisecount(
			long topicid, Integer pageSize) {
		List<TopicLink> tlist=new ArrayList<TopicLink>();
		TopicLink topicLink=new TopicLink();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("topicid", topicid);
		map.put("pageSize", pageSize);
		try {
			tlist=getSqlMapClientTemplate().queryForList("findTopicLinkByTopicidOrderbyPraisecount",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tlist=new ArrayList<TopicLink>();
			topicLink=new TopicLink();
			topicLink.setFlag(ResultUtils.QUERY_ERROR);
			tlist.add(topicLink);
		}
		return tlist;
	}

	@Override
	public List<TopicLink> findTopicLinkByTopicidOrderbyCommentcount(
			long topicid, Integer pageSize) {
		List<TopicLink> tlist=new ArrayList<TopicLink>();
		TopicLink topicLink=new TopicLink();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("topicid", topicid);
		map.put("pageSize", pageSize);
		try {
			tlist=getSqlMapClientTemplate().queryForList("findTopicLinkByTopicidOrderbyCommentcount",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			tlist=new ArrayList<TopicLink>();
			topicLink=new TopicLink();
			topicLink.setFlag(ResultUtils.QUERY_ERROR);
			tlist.add(topicLink);
		}
		return tlist;
	}
}
