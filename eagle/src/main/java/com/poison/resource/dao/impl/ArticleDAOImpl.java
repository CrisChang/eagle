package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ArticleDAO;
import com.poison.resource.model.Article;

@SuppressWarnings("deprecation")
public class ArticleDAOImpl extends SqlMapClientDaoSupport implements ArticleDAO {

	private static final  Log LOG = LogFactory.getLog(ArticleDAOImpl.class);
	
	@Override
	public int addArticle(Article article) {
		try {
			getSqlMapClientTemplate().insert("addArticle", article);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
	}

	@Override
	public int updateByIdArticle(Article article) {
		try {
			getSqlMapClientTemplate().update("updateByIdArticle", article);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public int deleteByIdArticle(long id) {
		try {
			getSqlMapClientTemplate().update("deleteByIdArticle", id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.DELETE_ERROR;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> queryUidArticle(Article article) {
		List<Article> list = new ArrayList<Article>();
		try {
			list = getSqlMapClientTemplate().queryForList("queryByUidArticle",
					article);
			if (null == list) {
				list = new ArrayList<Article>();
				//article.setFlag(ResultUtils.SUCCESS);
				//list.add(article);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<Article>();
			article.setFlag(ResultUtils.QUERY_ERROR);
			list.add(article);
			return list;
		}
		return list;
	}

	@Override
	public int updateByIdContent(Article article) {
		try {
			getSqlMapClientTemplate().update("updateArticleContentByID", article);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public Article queryArticleById(long id) {
		Article article = null;
		try {
			article = (Article) getSqlMapClientTemplate().queryForObject(
					"queryArticleById", id);
			if (null == article) {
				article = new Article();
				article.setFlag(ResultUtils.DATAISNULL);
				return article;
			}
			article.setFlag(ResultUtils.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			article = new Article();
			article.setFlag(ResultUtils.QUERY_ERROR);
			return article;
		}
		return article;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> queryByTypeArticle(String type,Long id) {
		List<Article> list = new ArrayList<Article>();
		Map<String, Object> map = new HashMap<String, Object>();
		Article article = new Article();
		try {
			map.put("type", type);
			map.put("id", id);
			list = getSqlMapClientTemplate().queryForList("queryByTypeArticle",
					map);
			if (null == list) {
				list = new ArrayList<Article>();
				//article.setFlag(ResultUtils.SUCCESS);
				//list.add(article);
			}
		} catch (Exception e) {
			list = new ArrayList<Article>();
			article.setFlag(ResultUtils.QUERY_ERROR);
			list.add(article);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> queryByTypeUid(String type, long uid,long start,int pageSize) {
		Map<Object,Object> map=new HashMap<Object, Object>();
		List<Article> list=new ArrayList<Article>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("start", start);
		map.put("pageSize", pageSize);
		try {
			list=getSqlMapClientTemplate().queryForList("queryArticleByTypeUid",map);
			if(null==list){
				list=new ArrayList<Article>();
				//Article article=new Article();
				//article.setFlag(ResultUtils.SUCCESS);
				//list.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list=new ArrayList<Article>();
			Article article=new Article();
			article.setFlag(ResultUtils.QUERY_ERROR);
			list.add(article);
			return list;
		}
		return list;
	}

	/**
	 * 根据ID查询帖子信息
	 */
	@Override
	public List<Article> findArticleListById(long id) {
		List<Article> articleList = new ArrayList<Article>();
		Article article = new Article();
		try{
			if(id==0){
				articleList = getSqlMapClientTemplate().queryForList("findArticleListByDate");
			}else{
				articleList = getSqlMapClientTemplate().queryForList("findArticleListById",id);
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			article.setFlag(ResultUtils.QUERY_ERROR);
			articleList.add(article);
		}
		return articleList;
	}

	/**
	 * 根据ID查询帖子信息
	 */
	@Override
	public List<Article> findArticleListByUsersId(List<Long> userIdList, Long resId,String type) {
		List<Article> articleList = new ArrayList<Article>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		Article article = new Article();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("usersIdList", userIdList);
		map.put("resId", resId);
		map.put("type", type);
		try {
			if (null == userIdList || userIdList.size() == 0) {
				return articleList;
			}
			articleList = getSqlMapClientTemplate().queryForList("findArticleListByUsersId", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleList = new ArrayList<Article>();
			article.setFlag(ResultUtils.ERROR);
			articleList.add(article);
		}
		return articleList;
	}

	/**
	 * 更新阅读量
	 */
	@Override
	public int updateArticleReadingCount(Article article) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateArticleReadingCount",article);
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
	public Map<String, Object> findArticleCount(long userId,String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			map.put("userId", userId);
			map.put("type", type);
			count = (Integer) getSqlMapClientTemplate().queryForObject("findArticleCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			count = 0;
			flag = ResultUtils.ERROR;
		}
		map.clear();
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}
	/**
	 * 根据标题模糊查询，包含文章开始时间和结束时间
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Article> searchArticleByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize) {
		Map<Object,Object> map=new HashMap<Object, Object>();
		List<Article> list=new ArrayList<Article>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("name", title);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			list=getSqlMapClientTemplate().queryForList("searchArticleByTitle",map);
			if(null==list){
				list=new ArrayList<Article>();
				Article article=new Article();
				article.setFlag(ResultUtils.SUCCESS);
				list.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list=new ArrayList<Article>();
			Article article=new Article();
			article.setFlag(ResultUtils.QUERY_ERROR);
			list.add(article);
			return list;
		}
		return list;
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	@Override
	public Map<String, Object> findArticleCountByLike(String type, long uid,String title,Long starttime,Long endtime) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("type", type);
		map.put("uid", uid);
		map.put("name", title);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		int count = 0;
		int flag = ResultUtils.ERROR;
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findArticleCountByLike",map);
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
	 * 根据用户id查询新长文章的详情
	 */
	@Override
	public List<Article> findArticleListByUserId(Long userId, Long resId,
			String type) {
		List<Article> articleList = new ArrayList<Article>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("resId", resId);
		map.put("type", type);
		try {
			articleList = getSqlMapClientTemplate().queryForList("findArticleListByUserId", map);
			if(null==articleList){
				articleList = new ArrayList<Article>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleList = new ArrayList<Article>();
		}
		return articleList;
	}
	
	/**
	 * 根据用户id查询一个时间段的文章id和时间信息
	 */
	@Override
	public List<Article> findUserArticleTime(Long userId,Long starttime,Long endtime) {
		List<Article> articleList = new ArrayList<Article>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("uid", userId);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			articleList = getSqlMapClientTemplate().queryForList("findUserArticleTime", map);
			if(null==articleList){
				articleList = new ArrayList<Article>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleList = new ArrayList<Article>();
		}
		return articleList;
	}
	/**
	 * 根据用户id查询一个时间段的文章信息列表
	 */
	@Override
	public List<Article> findUserArticlesByTime(Long userId,Long starttime,Long endtime) {
		List<Article> articleList = new ArrayList<Article>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("uid", userId);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			articleList = getSqlMapClientTemplate().queryForList("findUserArticlesByTime", map);
			if(null==articleList){
				articleList = new ArrayList<Article>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleList = new ArrayList<Article>();
		}
		return articleList;
	}
	/**
	 * 根据文章id集合查询文章集合
	 */
	@Override
	public List<Article> findArticlesByIds(List<Long> aids) {
		List<Article> articleList = null;
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("aids", aids);
		try {
			articleList = getSqlMapClientTemplate().queryForList("findArticlesByIds", map);
			if(null==articleList){
				articleList = new ArrayList<Article>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			articleList = new ArrayList<Article>();
		}
		return articleList;
	}
}
