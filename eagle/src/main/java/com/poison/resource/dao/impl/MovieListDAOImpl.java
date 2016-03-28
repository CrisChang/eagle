package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MovieListDAO;
import com.poison.resource.model.MovieList;

public class MovieListDAOImpl extends SqlMapClientDaoSupport implements MovieListDAO {

	private static final  Log LOG = LogFactory.getLog(MovieListDAOImpl.class);
	
	@Override
	public List<MovieList> queryDefaultFilmList(long userId) {
		List<MovieList> flist = new ArrayList<MovieList>();
		try {
			flist=getSqlMapClientTemplate().queryForList("queryDefaultFilmList",userId);
			if(null==flist||flist.size()==0){
				flist = new ArrayList<MovieList>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flist = new ArrayList<MovieList>();
			MovieList movieList = new MovieList();
			movieList.setFlag(ResultUtils.QUERY_ERROR);
			flist.add(movieList);
		}
		return flist;
	}

	@Override
	public int addNewFilmList(MovieList filmlist) {
		try {
			getSqlMapClientTemplate().insert("newFilmlist",filmlist);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.ERROR;
		}
	}

	/**
	 * 查询用户的影单列表
	 */
	@Override
	public List<MovieList> queryFilmListByUid(long userId, Long resId) {
		List<MovieList> list = new ArrayList<MovieList>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			list = getSqlMapClientTemplate().queryForList("queryFilmListByUid",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			MovieList movie = new MovieList();
			movie.setFlag(ResultUtils.QUERY_ERROR);
			list.add(movie);
		}
		return list;
	}

	/**
	 * 更新影单信息
	 */
	@Override
	public int updateFilmList(MovieList movieList) {
		int flag = ResultUtils.UPDATE_ERROR;
		try{
			getSqlMapClientTemplate().update("updateMovieList",movieList);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据ID查询一个影单
	 */
	@Override
	public MovieList findMovieListById(long id) {
		MovieList movieList = new MovieList();
		try{
			movieList = (MovieList) getSqlMapClientTemplate().queryForObject("findMovieListById",id);
			if(null==movieList){
				movieList = new MovieList();
				movieList.setFlag(ResultUtils.DATAISNULL);
				return movieList;
			}
			movieList.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			movieList = new MovieList();
			movieList.setFlag(ResultUtils.QUERY_ERROR);
		}
		return movieList;
	}

	/**
	 * 查询发布的影单列表
	 */
	@Override
	public List<MovieList> findPublishMvList(Long id) {
		List<MovieList> mvList = new ArrayList<MovieList>();
		try{
			mvList = getSqlMapClientTemplate().queryForList("findPublishMvList",id);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvList = new ArrayList<MovieList>();
			MovieList list = new MovieList();
			list.setFlag(ResultUtils.QUERY_ERROR);
			mvList.add(list);
		}
		return mvList;
	}

	/**
	 * 根据名称查询用户影单信息
	 */
	@Override
	public MovieList queryUserMvListByName(MovieList mvList) {
		MovieList movieList = new MovieList();
		try{
			movieList = (MovieList) getSqlMapClientTemplate().queryForObject("queryUserMvListByName",mvList);
			if(null==movieList){
				movieList = new MovieList();
				movieList.setFlag(ResultUtils.DATAISNULL);
				return movieList;
			}
			movieList.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			movieList = new MovieList();
			movieList.setFlag(ResultUtils.QUERY_ERROR);
		}
		return movieList;
	}

	/**
	 * 查询系统推荐影单
	 */
	@Override
	public List<MovieList> queryServerMvList(String tags, Long resId) {
		List<MovieList> mvList = new ArrayList<MovieList>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tags", tags);
		map.put("resId", resId);
		try{
			mvList = getSqlMapClientTemplate().queryForList("queryServerMvList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			mvList = new ArrayList<MovieList>();
			MovieList list = new MovieList();
			list.setFlag(ResultUtils.QUERY_ERROR);
			mvList.add(list);
		}
		return mvList;
	}

	/**
	 * 更新影单
	 */
	@Override
	public int updateMovieListPic(long id, String cover) {
		int flag = ResultUtils.ERROR;
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("id", id);
			map.put("cover", cover);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateMovieListPic",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 增加影单的评论数
	 */
	@Override
	public int addMvListCommentCount(long id, long latestRevisionDate) {
		int flag = ResultUtils.ERROR;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("latestRevisionDate", latestRevisionDate);
			getSqlMapClientTemplate().update("updateMovieListCommentCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			flag = ResultUtils.ERROR;
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
	
	@Override
	public List<MovieList> findMvListsByIds(List<Long> mvlistids) {
		List<MovieList> flist = new ArrayList<MovieList>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mvlistids", mvlistids);
			flist=getSqlMapClientTemplate().queryForList("findMvListsByIds",map);
			if(null==flist){
				flist = new ArrayList<MovieList>();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flist = new ArrayList<MovieList>();
			MovieList movieList = new MovieList();
			movieList.setFlag(ResultUtils.QUERY_ERROR);
			flist.add(movieList);
		}
		return flist;
	}

}
