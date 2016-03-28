package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BkCommentDAO;
import com.poison.resource.model.BkComment;

public class BkCommentDAOImpl extends SqlMapClientDaoSupport implements BkCommentDAO{

	private static final  Log LOG = LogFactory.getLog(BkCommentDAOImpl.class);
	/**
	 * 插入一条书评
	 */
	@Override
	public int insertBkComment(BkComment bkComment) {
		int i = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertBkComment", bkComment);
			i = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = ResultUtils.ERROR;
		}
		return i;
	}

	/**
	 * 查询所有书的评论
	 */
	@Override
	public List<BkComment> findAllBkComment(int bookId,Long resId,String resType,String resourceType) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		BkComment comment = new BkComment();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookId", bookId);
		map.put("resId", resId);
		map.put("resType", resType);
		map.put("resourceType", resourceType);
		if(resourceType.equals(CommentUtils.TYPE_ARTICLE_BOOK) || resourceType.equals(CommentUtils.TYPE_BOOK_DIGEST)){
			map.put("title", "");
		}else{
			map.put("title", null);
		}
		try{
			commentList = getSqlMapClientTemplate().queryForList("findBkCommentList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<BkComment>();
			comment.setFlag(ResultUtils.ERROR);
			commentList.add(comment);
		}
		return commentList;
	}
	
	/**
	 * 查询所有书的评论
	 */
	@Override
	public List<BkComment> findBkCommentListForOld(int bookId,Long resId,String resType,String resourceType) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		BkComment comment = new BkComment();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookId", bookId);
		map.put("resId", resId);
		map.put("resType", resType);
		map.put("resourceType", resourceType);
		if(resourceType.equals(CommentUtils.TYPE_ARTICLE_BOOK) || resourceType.equals(CommentUtils.TYPE_BOOK_DIGEST)){
			map.put("title", "");
		}else{
			map.put("title", null);
		}
		try{
			commentList = getSqlMapClientTemplate().queryForList("findBkCommentListForOld",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<BkComment>();
			comment.setFlag(ResultUtils.ERROR);
			commentList.add(comment);
		}
		return commentList;
	}

	/**
	 * 查询一本书的所有反对评论
	 */
	@Override
	public List<BkComment> findAllOpposiationComment(int bookId,Long resId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询所有书评
	 */
	@Override
	public List<BkComment> findAllComment(Long resId) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		BkComment comment = new BkComment();
		try{
			commentList = getSqlMapClientTemplate().queryForList("findAllBkCommentList",resId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<BkComment>();
			comment.setFlag(ResultUtils.ERROR);
			commentList.add(comment);
		}
		return commentList;
	}
	/**
	 *  根据id集合查询
	 */
	@Override
	public List<BkComment> findBkCommentListByIds(List<Long> ids) {
		List<BkComment> commentList = null;
		if(ids==null || ids.size()==0){
			return commentList;
		}
		BkComment comment = new BkComment();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("commentids", ids);
			commentList = getSqlMapClientTemplate().queryForList("findBkCommentListByIds",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<BkComment>(1);
			comment.setFlag(ResultUtils.ERROR);
			commentList.add(comment);
		}
		return commentList;
	}
	
	/**
	 * 查询自己对一本书的所有评论
	 */
	@Override
	public List<BkComment> findMyBkCommentList(long userId, int bookId,
			Long resId,String resType) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("bookId", bookId);
		map.put("resId", resId);
		try{
			commentList = getSqlMapClientTemplate().queryForList("findMyBkCommentList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			commentList = new ArrayList<BkComment>();
		}
		return commentList;
	}

	/**
	 * 查询一本书的评论总数
	 */
	@Override
	public int findCommentCount(int bookId,String resType) {
		int count = 0;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bookId", bookId);
			map.put("resType", resType);
			count = (Integer)getSqlMapClientTemplate().queryForObject("findCommentCount",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = ResultUtils.QUERY_ERROR;
		}
		return count;
	}

	
	
	/**
	 * 更新我对一本书的评论
	 */
	@Override
	public int updateMyCommentByBook(BkComment bkComment) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateMyCommentByBook",bkComment);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 根据ID查询一条评论是否存在
	 */
	@Override
	public BkComment findCommentIsExistById(long id) {
		
		BkComment bkComment = new BkComment();
		try{
			bkComment = (BkComment) getSqlMapClientTemplate().queryForObject("findCommentIsExistById",id);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkComment = new BkComment();
			bkComment.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bkComment;
	}

	/**
	 * 根据用户ID查询该用户的评论列表
	 */
	@Override
	public List<BkComment> findCommentListByUserId(long userId, Long resId) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			commentList = getSqlMapClientTemplate().queryForList("findCommentListByUserId",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<BkComment>();
			BkComment bkComment = new BkComment();
			bkComment.setFlag(ResultUtils.QUERY_ERROR);
			commentList.add(bkComment);
		}
		return commentList;
	}

	/**
	 * 删除一个书评
	 */
	@Override
	public int deleteMyCommentById(long id) {
		int flag =ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("deleteMyCommentById",id);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag =ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 根据type查询书评列表
	 */
	@Override
	public List<BkComment> findAllBkCommentListByType(Integer userId, String type,
			Long resId) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("type", type);
			map.put("resId", resId);
			commentList = getSqlMapClientTemplate().queryForList("findBkCommentListByType",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<BkComment>();
			BkComment bkComment = new BkComment();
			bkComment.setFlag(ResultUtils.QUERY_ERROR);
			commentList.add(bkComment);
		}
		return commentList;
	}

	/**
	 * 更新书评的逼格值
	 */
	@Override
	public int updateBkCommentBigValue(long id, float bigValue) {
		BkComment bkComment = new BkComment();
		Map<String, Object> map = new HashMap<String, Object>();
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			map.put("id", id);
			map.put("bigValue", bigValue);
			getSqlMapClientTemplate().update("updateCommentBigValue",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 查询在这个id之前的书评
	 */
	@Override
	public List<BkComment> findAboveComment(long resId) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		BkComment comment = new BkComment();
		try{
			commentList = getSqlMapClientTemplate().queryForList("findAboveComment",resId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<BkComment>();
			comment.setFlag(ResultUtils.ERROR);
			commentList.add(comment);
		}
		return commentList;
	}

	/**
	 * 查询用户书评的总数
	 */
	@Override
	public Map<String, Object> findBkCommentCount(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findBkCommentCount",userId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}

	/**
	 * 根据用户id查询长书评
	 */
	@Override
	public List<BkComment> findUserLongBkCommentListByUserId(long userId,
			Long resId) {
		List<BkComment> bkCommentList = new ArrayList<BkComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			bkCommentList = getSqlMapClientTemplate().queryForList("findUserLongBkCommentListByUserId",map);
			if(null==bkCommentList||bkCommentList.size()==0){
				bkCommentList = new ArrayList<BkComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkCommentList = new ArrayList<BkComment>();
		}
		return bkCommentList;
	}
	
	/**
	 * 根据时间段查询用户的书评信息
	 */
	@Override
	public List<BkComment> findMyBkCommentListByTime(long userId,Long starttime,Long endtime) {
		List<BkComment> commentList = new ArrayList<BkComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try{
			commentList = getSqlMapClientTemplate().queryForList("findMyBkCommentListByTime",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			commentList = new ArrayList<BkComment>();
		}
		return commentList;
	}
	/**
	 * 查询是否存在某个用户的书评记录
	 */
	@Override
	public long findBkCommentRecord(long userid){
		Long id = 0L;
		try{
			id = (Long) getSqlMapClientTemplate().queryForObject("findBkCommentRecord",userid);
			if(id==null){
				id = 0L;
			}
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
			id = -1L;
		}
		return id;
	}
}
