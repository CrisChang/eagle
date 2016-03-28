package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActCommentDAO;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActTransmit;
import com.poison.eagle.utils.ResultUtils;

public class ActCommentDAOImpl extends SqlMapClientDaoSupport implements ActCommentDAO{

	private static final  Log LOG = LogFactory.getLog(ActCommentDAOImpl.class);
	/**
	 * 插入评论信息
	 */
	@Override
	public int insertComment(ActComment actComment) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertintoActComment", actComment);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
		return ResultUtils.SUCCESS;
	}

	/**
	 * 查找评论列表
	 */
	@Override
	public List<ActComment> findComment(long resourceId,Long id) {
		List<ActComment> commentList = new ArrayList<ActComment>();
		ActComment actComment = new ActComment();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceId", resourceId);
			map.put("id", id);
			commentList = getSqlMapClientTemplate().queryForList("findResComment", map);
			if(null==commentList){
				commentList = new ArrayList<ActComment>();
				actComment.setFlag(ResultUtils.DATAISNULL);
				commentList.add(actComment);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<ActComment>();
			actComment.setFlag(ResultUtils.ERROR);
			commentList.add(actComment);
		}
		return commentList;
	}

	/**
	 * 查询评论总数
	 */
	@Override
	public int findCommentCount(long resourceId) {
		int i = ResultUtils.ERROR;
		try{
			i = (Integer) getSqlMapClientTemplate().queryForObject("findResCommentCount",resourceId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.QUERY_ERROR;
		}
		return i;
	}

	/**
	 * 查询评论信息
	 */
	@Override
	public ActComment findCommentById(ActComment actComment) {
		ActComment resCommen = new ActComment();
		try{
			resCommen = (ActComment) getSqlMapClientTemplate().queryForObject("findComment", actComment);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			resCommen.setFlag(ResultUtils.QUERY_ERROR);
		}
		return resCommen;
	}

	/**
	 * 更新评论信息
	 */
	@Override
	public int updateComment(ActComment actComment) {
		int i = ResultUtils.ERROR;
		try{
			i=getSqlMapClientTemplate().update("updateActComment", actComment);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.UPDATE_ERROR;
		}
		return i;
	}

	/**
	 * 查询用户的评论中心
	 */
	@Override
	public List<ActComment> findUserCommentCenter(long userId, Long id) {
		List<ActComment> commentList = new ArrayList<ActComment>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("id", id);
			commentList = getSqlMapClientTemplate().queryForList("findUserCommentCenter", map);
			if(null==commentList||commentList.size()==0){
				commentList = new ArrayList<ActComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<ActComment>();
		}
		return commentList;
	}

	/**
	 * 根据id查询评论
	 */
	@Override
	public ActComment findCmtById(long id) {
		ActComment actComment = new ActComment();
		try{
			actComment = (ActComment) getSqlMapClientTemplate().queryForObject("findCmtById",id);
			if(null==actComment){
				actComment = new ActComment();
				actComment.setFlag(ResultUtils.DATAISNULL);
				return actComment;
			}
			actComment.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			actComment = new ActComment();
			actComment.setFlag(ResultUtils.ERROR);
		}
		return actComment;
	}

	/**
	 * 删除根据id评论
	 */
	@Override
	public int delCommentById(long id) {
		int flag = ResultUtils.ERROR;
		long systime = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("latestRevisionDate", systime);
		try{
			getSqlMapClientTemplate().update("delCommentById",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

}
