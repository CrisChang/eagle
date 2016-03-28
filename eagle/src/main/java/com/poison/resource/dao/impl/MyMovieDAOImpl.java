package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MyMovieDAO;
import com.poison.resource.model.MyMovie;

@SuppressWarnings("deprecation")
public class MyMovieDAOImpl extends SqlMapClientDaoSupport implements MyMovieDAO{

	private static final  Log LOG = LogFactory.getLog(MyMovieDAOImpl.class);
	
	@Override
	public int addMyMovie(MyMovie mv) {
		try {
			getSqlMapClientTemplate().insert("addMyMovie",mv);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return ResultUtils.INSERT_ERROR;
		}
	}

	@Override
	public MyMovie findMyMovieIsExist(MyMovie mv) {
		MyMovie movie=new MyMovie();
		try {
			movie=(MyMovie)getSqlMapClientTemplate().queryForObject("findMyMovieIsExist",mv);
			if(null==movie){
				movie=new MyMovie();
				movie.setFlag(ResultUtils.DATAISNULL);
			}else{
				movie.setFlag(ResultUtils.SUCCESS);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			movie=new MyMovie();
			movie.setFlag(ResultUtils.QUERY_ERROR);
			return movie;
		}
		return movie;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyMovie> findMyMovieList(String name) {
		List<MyMovie> mlist=new ArrayList<MyMovie>();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findMyMovieList",name);
			if(mlist==null){
				MyMovie mv=new MyMovie();
				mlist=new ArrayList<MyMovie>();
				mv.setFlag(ResultUtils.DATAISNULL);
				mlist.add(mv);
			}else{
				MyMovie mv=new MyMovie();
				mlist=new ArrayList<MyMovie>();
				mv.setFlag(ResultUtils.SUCCESS);
				mlist.add(mv);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			MyMovie mv=new MyMovie();
			mlist=new ArrayList<MyMovie>();
			mv.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(mv);
			return mlist;
		}
		return mlist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyMovie> findMyMovieLikeList(String name) {
		List<MyMovie> mlist=new ArrayList<MyMovie>();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findMyMovieLikeList",name);
			if(mlist==null){
				MyMovie mv=new MyMovie();
				mlist=new ArrayList<MyMovie>();
				mv.setFlag(ResultUtils.DATAISNULL);
				mlist.add(mv);
			}else{
				MyMovie mv=new MyMovie();
				mlist=new ArrayList<MyMovie>();
				mv.setFlag(ResultUtils.SUCCESS);
				mlist.add(mv);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			MyMovie mv=new MyMovie();
			mlist=new ArrayList<MyMovie>();
			mv.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(mv);
		}
		return mlist;
	}

	@Override
	public MyMovie findMyMovieInfo(long id) {
		MyMovie movie=new MyMovie();
		try {
			movie=(MyMovie)getSqlMapClientTemplate().queryForObject("findMyMovieInfo",id);
			if(movie==null){
				movie=new MyMovie();
				movie.setFlag(ResultUtils.DATAISNULL);
			}else{
				movie.setFlag(ResultUtils.SUCCESS);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			movie=new MyMovie();
			movie.setFlag(ResultUtils.QUERY_ERROR);
			return movie;
		}
		return movie;
	}

}
