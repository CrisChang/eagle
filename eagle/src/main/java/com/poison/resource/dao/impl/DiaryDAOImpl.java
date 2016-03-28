package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.DiaryDAO;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Diary;

@SuppressWarnings("deprecation")
public class DiaryDAOImpl extends SqlMapClientDaoSupport implements DiaryDAO{

	private static final  Log LOG = LogFactory.getLog(DiaryDAOImpl.class);
	
	@Override
	public int addDiary(Diary diary) {
		try {
			getSqlMapClientTemplate().insert("addDiary",diary);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			return ResultUtils.INSERT_ERROR;
		}
	}

	@Override
	public int deleteDiary(long id) {
		try {
			getSqlMapClientTemplate().update("deleteDiary",id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DELETE_ERROR;
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Diary> queryDiaryByUid(Diary diary) {
		List<Diary> list=new ArrayList<Diary>();
		try {
			list=getSqlMapClientTemplate().queryForList("queryDiaryByUid",diary);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list=new ArrayList<Diary>();
			diary.setFlag(ResultUtils.ERROR);
			list.add(diary);
			return list;
		}
		return list;
	}

	@Override
	public int updateDiary(Diary diary) {
		try {
			getSqlMapClientTemplate().update("updateDiary",diary);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public Diary queryByIdDiary(long id) {
		Diary diary=new Diary();
		try {
			diary=(Diary)getSqlMapClientTemplate().queryForObject("queryByIdDiary",id);
			if(null==diary){
				diary=new Diary();
				diary.setFlag(ResultUtils.DATAISNULL);
			}
			diary.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diary.setFlag(ResultUtils.QUERY_ERROR);
			return diary;
		}
		return diary;
	}

	@Override
	public Diary queryByIdDiaryWithoutDel(long id) {
		Diary diary=new Diary();
		try {
			diary=(Diary)getSqlMapClientTemplate().queryForObject("queryByIdDiaryWithoutDel",id);
			if(null==diary){
				diary=new Diary();
				diary.setFlag(ResultUtils.DATAISNULL);
			}
			diary.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diary.setFlag(ResultUtils.QUERY_ERROR);
			return diary;
		}
		return diary;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Diary> queryType(long uid,String type) {
		List<Diary> list=new ArrayList<Diary>();
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("uid", uid);
		map.put("type", type);
		try {
			list=getSqlMapClientTemplate().queryForList("queryType",map);
			if(null==list){
				Diary diary=new Diary();
				list=new ArrayList<Diary>();
				diary.setFlag(ResultUtils.SUCCESS);
				list.add(diary);	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list=new ArrayList<Diary>();
			Diary diary=new Diary();
			diary.setFlag(ResultUtils.QUERY_ERROR);
			list.add(diary);
			return list;
		}
		return list;
	}

	/**
	 * 根据ID查询日记详情
	 */
	@Override
	public List<Diary> findDiaryListById(long id) {
		List<Diary> diaryList = new ArrayList<Diary>();
		Diary diary = new Diary();
		try{
			if(id==0){
				diaryList = getSqlMapClientTemplate().queryForList("findDiaryListByDate");
			}else{
				diaryList = getSqlMapClientTemplate().queryForList("findDiaryListById",id);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diary.setFlag(ResultUtils.QUERY_ERROR);
			diaryList.add(diary);
		}
		return diaryList;
	}

	/**
	 * 根据ID查询日记详情
	 */
	@Override
	public List<Diary> findDiaryListByUsersId(List<Long> userIdList, Long resId) {
		List<Diary> diaryList = new ArrayList<Diary>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		Diary diary = new Diary();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("usersIdList", userIdList);
		map.put("resId", resId);
		try {
			if (null == userIdList || userIdList.size() == 0) {
				return diaryList;
			}
			diaryList = getSqlMapClientTemplate().queryForList("findDiaryListByUsersId", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diaryList = new ArrayList<Diary>();
			diary.setFlag(ResultUtils.ERROR);
			diaryList.add(diary);
		}
		return diaryList;
	}

	/**
	 * 根据TYPE查询所有的日记
	 */
	@Override
	public List<Diary> queryAllType(String type) {
		List<Diary> list = new ArrayList<Diary>();
		try{
			list = getSqlMapClientTemplate().queryForList("queryAllType",type);
			if(null==list){
				list = new ArrayList<Diary>();
				Diary diary = new Diary();
				diary.setFlag(ResultUtils.DATAISNULL);
				list.add(diary);
				return list;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<Diary>();
			Diary diary = new Diary();
			diary.setFlag(ResultUtils.QUERY_ERROR);
			list.add(diary);
		}
		return list;
	}

	/**
	 * 查询日记列表
	 */
	@Override
	public List<Diary> findDiaryList(Long id) {
		List<Diary> diaryList = new ArrayList<Diary>();
		Diary diary = new Diary();
		try{
			diaryList = getSqlMapClientTemplate().queryForList("findDiaryList",id);
			if(null==diaryList||diaryList.size()==0){
				diaryList = new ArrayList<Diary>();
				diary.setFlag(ResultUtils.QUERY_ERROR);
				diaryList.add(diary);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diaryList = new ArrayList<Diary>();
			diary.setFlag(ResultUtils.ERROR);
			diaryList.add(diary);
		}
		return diaryList;
	}

	/**
	 * 查询用户发布日记的总数
	 */
	@Override
	public Map<String, Object> findDiaryCount(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findDiaryCount",userId);
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
	 * 根据用户id查询日志详情
	 */
	@Override
	public List<Diary> findDiaryListByUserId(Long userId, Long resId) {
		List<Diary> diaryList = new ArrayList<Diary>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("resId", resId);
		try{
			diaryList = getSqlMapClientTemplate().queryForList("findDiaryListByUserId",map);
			if(null==diaryList){
				diaryList = new ArrayList<Diary>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diaryList = new ArrayList<Diary>();
		}
		return diaryList;
	}
	
	/**
	 * 根据用户id查询一个时间段的文字时间信息 
	 */
	@Override
	public List<Diary> findUserDiaryTime(Long userId, Long starttime,Long endtime) {
		List<Diary> diaryList = new ArrayList<Diary>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("uid", userId);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try{
			diaryList = getSqlMapClientTemplate().queryForList("findUserDiaryTime",map);
			if(null==diaryList){
				diaryList = new ArrayList<Diary>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diaryList = new ArrayList<Diary>();
		}
		return diaryList;
	}
	
	/**
	 * 根据用户id查询一个时间段的文字信息列表
	 */
	@Override
	public List<Diary> findUserDiarysByTime(Long userId, Long starttime,Long endtime) {
		List<Diary> diaryList = new ArrayList<Diary>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("uid", userId);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try{
			diaryList = getSqlMapClientTemplate().queryForList("findUserDiarysByTime",map);
			if(null==diaryList){
				diaryList = new ArrayList<Diary>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diaryList = new ArrayList<Diary>();
		}
		return diaryList;
	}
	/**
	 * 根据标题和内容模糊查询，查询某个时间段的
	 */
	@Override
	public List<Diary> searchDiaryByLike(Long userId,String keyword, Long starttime,Long endtime,long start,int pageSize) {
		List<Diary> diaryList = new ArrayList<Diary>();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		map.put("keyword",keyword);
		map.put("start", start);
		map.put("pageSize", pageSize);
		try{
			diaryList = getSqlMapClientTemplate().queryForList("searchDiaryByLike",map);
			if(null==diaryList){
				diaryList = new ArrayList<Diary>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			diaryList = new ArrayList<Diary>();
		}
		return diaryList;
	}
	
	/**
	 * 根据模糊查询的条件查询数量
	 */
	@Override
	public Map<String, Object> findDiaryCountByLike(long userId,String keyword,Long starttime,Long endtime) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("keyword", keyword);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findDiaryCountByLike",map);
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
}
