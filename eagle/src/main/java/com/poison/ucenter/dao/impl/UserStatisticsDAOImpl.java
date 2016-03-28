package com.poison.ucenter.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.UserStatisticsDAO;
import com.poison.ucenter.model.UserStatistics;

public class UserStatisticsDAOImpl extends SqlMapClientDaoSupport implements UserStatisticsDAO{

	private static final  Log LOG = LogFactory.getLog(UserStatisticsDAOImpl.class);
	/**
	 * 插入用户的统计信息
	 */
	@Override
	public int insertUserStatistics(long userId) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertUserStatistics",userId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据uid查询用户的统计信息
	 */
	@Override
	public UserStatistics findUserStatisticsByUid(long userId) {
		UserStatistics userStatistics = new UserStatistics();
		try{
			userStatistics = (UserStatistics) getSqlMapClientTemplate().queryForObject("findUserStatisticsByUid",userId);
			if(null==userStatistics){
				userStatistics = new UserStatistics();
				userStatistics.setFlag(ResultUtils.DATAISNULL);
				return userStatistics;
			}
			userStatistics.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			userStatistics = new UserStatistics();
			userStatistics.setFlag(ResultUtils.ERROR);
		}
		return userStatistics;
	}

	/**
	 * 修改书评总数
	 */
	@Override
	public int updateBkcommentCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateBkcommentCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: updateBkcommentCount</p> 
	 * <p>Description: 更新书评总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:35:44
	 * @param userId
	 * @return
	 */
	@Override
	public int updateBkcommentNewCount(long userId,long bkcommentCount){
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("bkcommentCount", bkcommentCount);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateBkcommentNewCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 修改影评总数
	 */
	@Override
	public int updateMvcommentCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateMvcommentCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: updateMvcommentCount</p> 
	 * <p>Description: 更新影评总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:36:28
	 * @param userId
	 * @return
	 */
	@Override
	public int updateMvcommentNewCount(long userId,long mvcommentCount){
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("mvcommentCount", mvcommentCount);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateMvcommentNewCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 修改书单总数
	 */
	@Override
	public int updateBklistCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateBklistCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 修改影单总数
	 */
	@Override
	public int updateMvlistCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateMvlistCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 修改转发总数
	 */
	@Override
	public int updateTransmitCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateTransmitCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 修改日记总数
	 */
	@Override
	public int updateDiaryCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateDiaryCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: updateDiaryCount</p> 
	 * <p>Description: 修改日记总数-赋值新值</p> 
	 * @author :changjiang
	 * date 2014-12-18 下午5:40:04
	 * @param userId
	 * @return
	 */
	@Override
	public int updateDiaryNewCount(long userId,long diaryCount){
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("diaryCount", diaryCount);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateDiaryNewCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 修改帖子总数
	 */
	@Override
	public int updatePostCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updatePostCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
	
	/**
	 * 修改新长文章总数
	 */
	@Override
	public int updateArticleCount(long userId) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("userId", userId);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateArticleCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
	
	/**
	 * 更新评论的消息提醒
	 */
	@Override
	public int updateCommentSwitch(long userId, int commentSwitch) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		long systime = System.currentTimeMillis();
		try{
			map.put("userId", userId);
			map.put("commentSwitch", commentSwitch);
			map.put("latestRevisionDate", systime);
			getSqlMapClientTemplate().update("updateCommentSwitch",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 更新打赏的消息提醒
	 */
	@Override
	public int updateGiveSwitch(long userId, int giveSwitch) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		long systime = System.currentTimeMillis();
		try{
			map.put("userId", userId);
			map.put("giveSwitch", giveSwitch);
			map.put("latestRevisionDate", systime);
			getSqlMapClientTemplate().update("updateGiveSwitch",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 更新@的消息提醒
	 */
	@Override
	public int updateAtSwitch(long userId, int atSwitch) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		long systime = System.currentTimeMillis();
		try{
			map.put("userId", userId);
			map.put("atSwitch", atSwitch);
			map.put("latestRevisionDate", systime);
			getSqlMapClientTemplate().update("updateAtSwitch",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
	
	/**
	 * 查询总条数
	 */
	@Override
	public Map<String, Object> findTotalNum(){
		Map<String, Object> map = new HashMap<String, Object>();
		long count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Long) getSqlMapClientTemplate().queryForObject("findTotalNum");
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
	 * 查询小于书评数量的条数
	 */
	@Override
	public Map<String, Object> findNumByLessBkcommentCount(long bkcommentCount){
		Map<String, Object> map = new HashMap<String, Object>();
		long count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Long) getSqlMapClientTemplate().queryForObject("findNumByLessBkcommentCount",bkcommentCount);
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
	 * 查询小于影评数量的条数
	 */
	@Override
	public Map<String, Object> findNumByLessMvcommentCount(long mvcommentCount){
		Map<String, Object> map = new HashMap<String, Object>();
		long count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Long) getSqlMapClientTemplate().queryForObject("findNumByLessMvcommentCount",mvcommentCount);
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
}
