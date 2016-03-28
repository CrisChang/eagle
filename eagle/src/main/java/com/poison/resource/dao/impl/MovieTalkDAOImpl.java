package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MovieTalkDao;
import com.poison.resource.dao.MyMovieDAO;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyMovie;

@SuppressWarnings("deprecation")
public class MovieTalkDAOImpl extends SqlMapClientDaoSupport implements MovieTalkDao{
	
	private static final  Log LOG = LogFactory.getLog(MovieTalkDAOImpl.class);
	
	@Override
	public long addMovieTalk(MovieTalk movieTalk) {
		long id = ResultUtils.INSERT_ERROR;
		try {
			id = (Long)getSqlMapClientTemplate().insert("addMovieTalk",movieTalk);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			id =  ResultUtils.INSERT_ERROR;
		}
		return id;
	}

	@Override
	public List<MovieTalk> findMovieTalkList(int movieId,Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("movieId", movieId);
		map.put("id", id);
		List<MovieTalk> mlist=new ArrayList<MovieTalk>();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findMovieTalkList",map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			MovieTalk movieTalk = new MovieTalk();
			mlist=new ArrayList<MovieTalk>();
			movieTalk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(movieTalk);
		}
		return mlist;
	}

	@Override
	public int delMovieTalk(Long id) {
		int r = 0;
		try {
			r = getSqlMapClientTemplate().update("delMovieTalk", id);
			if(r > 0){
				return ResultUtils.SUCCESS;
			}else{
				return ResultUtils.DELETE_ERROR;
			}
		} catch (Exception e) {
			return ResultUtils.DELETE_ERROR;
		}
	}

}
