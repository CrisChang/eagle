package com.poison.act.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.ActWeixinCommentDAO;
import com.poison.act.model.ActWeixinComment;
import com.poison.eagle.utils.ResultUtils;

public class ActWeixinCommentDAOImpl extends SqlMapClientDaoSupport implements ActWeixinCommentDAO{

	private static final  Log LOG = LogFactory.getLog(ActWeixinCommentDAOImpl.class);
	/**
	 * 插入评论信息
	 */
	@Override
	public int insertWeixinComment(ActWeixinComment actWeixinComment) {
		Object object = new Object();
		try{
			object = getSqlMapClientTemplate().insert("insertWeixinComment", actWeixinComment);
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
	public List<ActWeixinComment> findWeixinComment(String sopenid) {
		List<ActWeixinComment> commentList = new ArrayList<ActWeixinComment>();
		//ActWeixinComment actWeixinComment = new ActWeixinComment();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sopenid", sopenid);
			commentList = getSqlMapClientTemplate().queryForList("findWeixinComment", map);
			if(null==commentList){
				commentList = new ArrayList<ActWeixinComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<ActWeixinComment>();
		}
		return commentList;
	}
	
	/**
	 * 查找评论列表
	 */
	@Override
	public int existUserComment(String openid,String sopenid,String comment) {
		int i = -1;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("openid", openid);
			map.put("sopenid", sopenid);
			map.put("commentcontext", comment);
			i = (Integer) getSqlMapClientTemplate().queryForObject("existUserComment", map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}
}
