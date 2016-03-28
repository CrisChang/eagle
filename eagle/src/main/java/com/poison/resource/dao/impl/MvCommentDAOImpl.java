package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MvCommentDAO;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvComment;

@SuppressWarnings("deprecation")
public class MvCommentDAOImpl extends SqlMapClientDaoSupport implements  MvCommentDAO {

	private static final  Log LOG = LogFactory.getLog(MvCommentDAOImpl.class);
	@Override
	public int addMvComment(MvComment comment) {
		try {
			getSqlMapClientTemplate().insert("addMvComment",comment);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
	}

	@Override
	public List<MvComment> findAllMvComment(long movieId, Long id,String type,String resourceType) {
		List<MvComment> mlist=new ArrayList<MvComment>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		MvComment mk=new MvComment();
		map.put("movieId", movieId);
		map.put("id", id);
		map.put("type", type);
		map.put("resourceType", resourceType);
		//临时变动，长书评长影评按照title判断
		if(resourceType.equals("24")){
			map.put("title", "");
		}else{
			map.put("title", null);
		}
		//map.put("resourceType", resourceType);
		try {
			mlist=getSqlMapClientTemplate().queryForList("findAllMvComment",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mlist=new ArrayList<MvComment>();
			mk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(mk);
		}
		return mlist;
	}
	
	@Override
	public List<MvComment> findAllMvCommentForOld(long movieId, Long id,String type,String resourceType,Integer pageIndex, Integer pageSize) {
		List<MvComment> mlist=new ArrayList<MvComment>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		MvComment mk=new MvComment();
		map.put("movieId", movieId);
		map.put("id", id);
		map.put("type", type);
		map.put("resourceType", resourceType);
		map.put("pageIndex", pageIndex);
		map.put("pageSize",pageSize);
		//临时变动，长书评长影评按照title判断
		if(resourceType.equals("24")){
			map.put("title", "");
		}else{
			map.put("title", null);
		}
		//map.put("resourceType", resourceType);
		try {
			mlist=getSqlMapClientTemplate().queryForList("findAllMvCommentForOld",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mlist=new ArrayList<MvComment>();
			mk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(mk);
		}
		return mlist;
	}

	/**
	 *
	 * @param movieId
	 * @param resourceType
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<MvComment> findOneMvCommentListByResTypeAndPage(long movieId, String resourceType, int pageIndex, int pageSize) {
		List<MvComment> mlist=new ArrayList<MvComment>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("movieId", movieId);
		map.put("pageIndex", pageIndex);
		map.put("pageSize",pageSize);
		map.put("resourceType", resourceType);
		//map.put("resourceType", resourceType);
		try {
			mlist=getSqlMapClientTemplate().queryForList("findOneMvCommentListByResTypeAndPage",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mlist=new ArrayList<MvComment>();
		}
		return mlist;
	}

	@Override
	public List<MvComment> findAllOpposiationComment(int movieId, Long id) {
		List<MvComment> mlist=new ArrayList<MvComment>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		MvComment mk=new MvComment();
		map.put("movieId", movieId);
		map.put("id", id);
		try {
			mlist=getSqlMapClientTemplate().queryForList("findAllOpposiationComment",map);
			if(mlist==null || mlist.size()==0){
				mlist=new ArrayList<MvComment>();
				mk=new MvComment();
				mk.setFlag(ResultUtils.DATAISNULL);
				mlist.add(mk);
				return mlist;
			}else{
				mlist=new ArrayList<MvComment>();
				mk=new MvComment();
				mk.setFlag(ResultUtils.SUCCESS);
				mlist.add(mk);
				return mlist;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mlist=new ArrayList<MvComment>();
			mk=new MvComment();
			mk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(mk);
			return mlist;
		}
	}

	@Override
	public List<MvComment> findAllComment(Long id) {
		List<MvComment> mlist=new ArrayList<MvComment>();
		MvComment mk=new MvComment();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findAllComment",id);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mlist=new ArrayList<MvComment>();
			mk=new MvComment();
			mk.setFlag(ResultUtils.SUCCESS);
			mlist.add(mk);
		}
		return mlist;
	}

	/**
	 * 查询用户对一部电影的评论
	 */
	@Override
	public List<MvComment> findUserMvComment(MvComment mvComment) {
		List<MvComment> commentList = new ArrayList<MvComment>();
		MvComment comment = new MvComment();
		try{
			commentList = getSqlMapClientTemplate().queryForList("findUserMvComment",mvComment);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<MvComment>();
			comment.setFlag(ResultUtils.QUERY_ERROR);
			commentList.add(comment);
		}
		return commentList;
	}

	/**
	 * 更新我的影评
	 */
	@Override
	public int updateMyMvComment(MvComment mvComment) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateMyCommentByMovie",mvComment);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 查询电影的评论总数
	 */
	@Override
	public int findMvCommentCount(long movieId) {
		int flag = ResultUtils.ERROR;
		try{
			flag = (Integer) getSqlMapClientTemplate().queryForObject("findMvCommentCount",movieId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.QUERY_ERROR;
		}
		return flag;
	}

	/**
	 * 查询这个影评是否存在
	 */
	@Override
	public MvComment findMvCommentIsExist(long id) {
		MvComment mvComment = new MvComment();
		try{
			mvComment = (MvComment) getSqlMapClientTemplate().queryForObject("findMvCommentIsExist",id);
			if(null==mvComment){
				mvComment = new MvComment();
				mvComment.setFlag(ResultUtils.DATAISNULL);
				return mvComment;
			}
			mvComment.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvComment.setFlag(ResultUtils.QUERY_ERROR);
		}
		return mvComment;
	}

	/**
	 * 根据type查询电影评论
	 */
	@Override
	public List<MvComment> findAllMvCommentListByType(Long userId, String type,
			Long resId) {
		List<MvComment> commentList = new ArrayList<MvComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("type", type);
			map.put("resId", resId);
			commentList = getSqlMapClientTemplate().queryForList("findMvCommentListByType",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			commentList = new ArrayList<MvComment>();
			MvComment mvComment = new MvComment();
			mvComment.setFlag(ResultUtils.QUERY_ERROR);
			commentList.add(mvComment);
		}
		return commentList;
	}

	/**
	 * 更新影评的逼格值
	 */
	@Override
	public int updateMvCommentBigValue(long id, float bigValue) {
		int flag = ResultUtils.UPDATE_ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("id", id);
			map.put("bigValue", bigValue);
			getSqlMapClientTemplate().update("updateMvCommentBigValue",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	/**
	 * 根据userid查询影评
	 */
	@Override
	public List<MvComment> findAllMvCommentListByUsersId(List<Long> usersId,
			String type, Long resId) {
		List<MvComment> mvCommentList = new ArrayList<MvComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		MvComment mvComment = new MvComment();
		try{
			map.put("usersId", usersId);
			map.put("type", type);
			map.put("resId", resId);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentListByUserId",map);
			if(null==mvCommentList||mvCommentList.size()==0){
				mvCommentList = new ArrayList<MvComment>();
				mvComment.setFlag(ResultUtils.DATAISNULL);
				mvCommentList.add(mvComment);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
			mvComment.setFlag(ResultUtils.QUERY_ERROR);
			mvCommentList.add(mvComment);
		}
		return mvCommentList;
	}

	/**
	 * 根据uid查询电影评论数量
	 */
	@Override
	public Map<String, Object> findMvCommentCountByUid(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findMvCommentCountByUid",userId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map.put("count", count);
		map.put("flag", flag);
		return map;
	}

	/**
	 * 根据用户id查询用户的长影评
	 */
	@Override
	public List<MvComment> findUserLongMvCommentListByUserId(long userId,
			Long resId) {
		List<MvComment> mvCommentList = new ArrayList<MvComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			mvCommentList = getSqlMapClientTemplate().queryForList("findUserLongMvCommentListByUserId",map);
			if(null==mvCommentList||mvCommentList.size()==0){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	
	/**
	 * 查询某个用户某个时间段的影评
	 */
	@Override
	public List<MvComment> findUserMvCommentsByTime(long userId,Long starttime,Long endtime) {
		List<MvComment> mvCommentList = new ArrayList<MvComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("starttime", starttime);
			map.put("endtime", endtime);
			mvCommentList = getSqlMapClientTemplate().queryForList("findUserMvCommentsByTime",map);
			if(null==mvCommentList||mvCommentList.size()==0){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	
	/**
	 * 根据标题和内容模糊查询，查询某个时间段的
	 */
	@Override
	public List<MvComment> searchMvCommentByLike(long userId,String keyword,Long starttime,Long endtime,long start,int pageSize) {
		List<MvComment> mvCommentList = new ArrayList<MvComment>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("keyword", keyword);
			map.put("starttime", starttime);
			map.put("endtime", endtime);
			map.put("start", start);
			map.put("pageSize", pageSize);
			mvCommentList = getSqlMapClientTemplate().queryForList("searchMvCommentByLike",map);
			if(null==mvCommentList||mvCommentList.size()==0){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	@Override
	public Map<String, Object> findMvCommentCountByLike(long userId,String keyword,Long starttime,Long endtime) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("keyword", keyword);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findMvCommentCountByLike",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map=new HashMap<String, Object>();
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}
	
	/**
	 * 根据阶段id查询影评列表根据评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByStageidOrderbyPoint(long stageid,long start,int pagesize){
		List<MvComment> mvCommentList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("stageid", stageid);
			map.put("start", start);
			map.put("pagesize", pagesize);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentsByStageidOrderbyPoint",map);
			if(null==mvCommentList){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	/**
	 * 根据阶段id查询影评列表根据评论时间排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByStageidOrderbyId(long stageid,long start,int pagesize){
		List<MvComment> mvCommentList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("stageid", stageid);
			map.put("start", start);
			map.put("pagesize", pagesize);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentsByStageidOrderbyId",map);
			if(null==mvCommentList){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	/**
	 * 根据阶段id查询用户影评得分排行榜按评委评分排序 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentUserRankByStageid(long stageid,long start,int pagesize){
		List<MvComment> mvCommentList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("stageid", stageid);
			map.put("start", start);
			map.put("pagesize", pagesize);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentUserRankByStageid",map);
			if(null==mvCommentList){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	
	/**
	 * 根据阶段id查询某个人的影评列表根据评委评分排序
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByUseridAndStageid(long userId,long stageid,long start,int pagesize){
		List<MvComment> mvCommentList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("stageid", stageid);
			map.put("start", start);
			map.put("pagesize", pagesize);
			map.put("userId", userId);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentsByUseridAndStageid",map);
			if(null==mvCommentList){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	/**
	 * 根据阶段id查询某个电影的影评列表
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByMovieidAndStageid(long movieId,long stageid,Long resId,int pagesize){
		List<MvComment> mvCommentList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("stageid", stageid);
			map.put("resId", resId);
			map.put("pagesize", pagesize);
			map.put("movieId", movieId);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentsByMovieidAndStageid",map);
			if(null==mvCommentList){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByIdsAndStageid(long stageid,List<Long> commentids){
		List<MvComment> mvCommentList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("stageid", stageid);
			map.put("commentids", commentids);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentsByIdsAndStageid",map);
			if(null==mvCommentList){
				mvCommentList = new ArrayList<MvComment>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>();
		}
		return mvCommentList;
	}
	/**
	 * 根据影评id集合查询影评列表 
	 * @param stageid
	 * @param start
	 * @param pagesize
	 * @return
	 */
	@Override
	public List<MvComment> findMvCommentsByIds(List<Long> commentids){
		List<MvComment> mvCommentList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("commentids", commentids);
			mvCommentList = getSqlMapClientTemplate().queryForList("findMvCommentsByIds",map);
			if(null==mvCommentList){
				mvCommentList = new ArrayList<MvComment>(0);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvCommentList = new ArrayList<MvComment>(0);
		}
		return mvCommentList;
	}
	/**
	 * 查询是否存在某个用户的影评记录
	 */
	@Override
	public long findMvCommentRecord(long userid){
		Long id = 0L;
		try{
			id = (Long) getSqlMapClientTemplate().queryForObject("findMvCommentRecord",userid);
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
