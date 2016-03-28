package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ArticleDraftDAO;
import com.poison.resource.model.ArticleDraft;

@SuppressWarnings("deprecation")
public class ArticleDraftDAOImpl extends SqlMapClientDaoSupport implements ArticleDraftDAO {

	private static final  Log LOG = LogFactory.getLog(ArticleDraftDAOImpl.class);
	
	@Override
	public int addArticleDraft(ArticleDraft articleDraft) {
		try {
			getSqlMapClientTemplate().insert("addArticleDraft", articleDraft);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
	}

	@Override
	public int updateByIdArticleDraft(ArticleDraft articleDraft) {
		try {
			getSqlMapClientTemplate().update("updateByIdArticleDraft", articleDraft);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public int deleteByIdArticleDraft(long id) {
		try {
			getSqlMapClientTemplate().update("deleteByIdArticleDraft", id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DELETE_ERROR;
		}
	}
	
	@Override
	public int deleteArticleDraftByAid(long aid) {
		try {
			getSqlMapClientTemplate().update("deleteArticleDraftByAid", aid);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DELETE_ERROR;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleDraft> queryUidArticleDraft(ArticleDraft articleDraft) {
		List<ArticleDraft> list = new ArrayList<ArticleDraft>();
		try {
			list = getSqlMapClientTemplate().queryForList("queryByUidArticleDraft",
					articleDraft);
			if (null == list) {
				list = new ArrayList<ArticleDraft>();
				articleDraft.setFlag(ResultUtils.SUCCESS);
				list.add(articleDraft);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<ArticleDraft>();
			articleDraft.setFlag(ResultUtils.QUERY_ERROR);
			list.add(articleDraft);
			return list;
		}
		return list;
	}

	@Override
	public ArticleDraft queryArticleDraftById(long id) {
		ArticleDraft articleDraft = null;
		try {
			articleDraft = (ArticleDraft) getSqlMapClientTemplate().queryForObject(
					"queryArticleDraftById", id);
			if (null == articleDraft) {
				articleDraft = new ArticleDraft();
				articleDraft.setFlag(ResultUtils.DATAISNULL);
				return articleDraft;
			}
			articleDraft.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleDraft = new ArticleDraft();
			articleDraft.setFlag(ResultUtils.QUERY_ERROR);
			return articleDraft;
		}
		return articleDraft;
	}
	
	@Override
	public ArticleDraft queryArticleDraftByAid(long aid) {
		ArticleDraft articleDraft = null;
		try {
			articleDraft = (ArticleDraft) getSqlMapClientTemplate().queryForObject(
					"queryArticleDraftByAid", aid);
			if (null == articleDraft) {
				articleDraft = new ArticleDraft();
				articleDraft.setFlag(ResultUtils.DATAISNULL);
				return articleDraft;
			}
			articleDraft.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleDraft = new ArticleDraft();
			articleDraft.setFlag(ResultUtils.QUERY_ERROR);
			return articleDraft;
		}
		return articleDraft;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleDraft> queryByTypeArticleDraft(String type,Long id) {
		List<ArticleDraft> list = new ArrayList<ArticleDraft>();
		Map<String, Object> map = new HashMap<String, Object>();
		ArticleDraft articleDraft = new ArticleDraft();
		try {
			map.put("type", type);
			map.put("id", id);
			list = getSqlMapClientTemplate().queryForList("queryByTypeArticleDraft",
					map);
			if (null == list) {
				list = new ArrayList<ArticleDraft>();
				articleDraft.setFlag(ResultUtils.SUCCESS);
				list.add(articleDraft);
			}
		} catch (Exception e) {
			list = new ArrayList<ArticleDraft>();
			articleDraft.setFlag(ResultUtils.QUERY_ERROR);
			list.add(articleDraft);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleDraft> queryByTypeUid(String type, long uid,long start,int pageSize) {
		Map<Object,Object> map=new HashMap<Object, Object>();
		List<ArticleDraft> list=new ArrayList<ArticleDraft>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("start", start);
		map.put("pageSize", pageSize);
		try {
			list=getSqlMapClientTemplate().queryForList("queryArticleDraftByTypeUid",map);
			if(null==list){
				list=new ArrayList<ArticleDraft>();
				ArticleDraft articleDraft=new ArticleDraft();
				articleDraft.setFlag(ResultUtils.SUCCESS);
				list.add(articleDraft);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list=new ArrayList<ArticleDraft>();
			ArticleDraft articleDraft=new ArticleDraft();
			articleDraft.setFlag(ResultUtils.QUERY_ERROR);
			list.add(articleDraft);
			return list;
		}
		return list;
	}

	/**
	 * 根据ID查询帖子信息
	 */
	@Override
	public List<ArticleDraft> findArticleDraftListById(long id) {
		List<ArticleDraft> articleDraftList = new ArrayList<ArticleDraft>();
		ArticleDraft articleDraft = new ArticleDraft();
		try{
			if(id==0){
				articleDraftList = getSqlMapClientTemplate().queryForList("findArticleDraftListByDate");
			}else{
				articleDraftList = getSqlMapClientTemplate().queryForList("findArticleDraftListById",id);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleDraft.setFlag(ResultUtils.QUERY_ERROR);
			articleDraftList.add(articleDraft);
		}
		return articleDraftList;
	}

	/**
	 * 更新阅读量
	 */
	@Override
	public int updateArticleDraftReadingCount(long id) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateArticleDraftReadingCount",id);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据uid查询帖子的总数
	 */
	@Override
	public Map<String, Object> findArticleDraftCount(long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findArticleDraftCount",userId);
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
	 * 根据标题模糊查询，包含文章开始时间和结束时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleDraft> searchArticleDraftByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize) {
		Map<Object,Object> map=new HashMap<Object, Object>();
		List<ArticleDraft> list=new ArrayList<ArticleDraft>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("name", title);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			list=getSqlMapClientTemplate().queryForList("searchArticleDraftByTitle",map);
			if(null==list){
				list=new ArrayList<ArticleDraft>();
				ArticleDraft articleDraft=new ArticleDraft();
				articleDraft.setFlag(ResultUtils.SUCCESS);
				list.add(articleDraft);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list=new ArrayList<ArticleDraft>();
			ArticleDraft articleDraft=new ArticleDraft();
			articleDraft.setFlag(ResultUtils.QUERY_ERROR);
			list.add(articleDraft);
			return list;
		}
		return list;
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	@Override
	public Map<String, Object> findArticleDraftCountByLike(String type, long uid,String title,Long starttime,Long endtime) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("name", title);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findArticleDraftCountByLike",map);
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
