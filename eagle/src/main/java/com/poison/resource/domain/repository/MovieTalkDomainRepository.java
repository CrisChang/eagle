package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.MovieListDAO;
import com.poison.resource.dao.MovieTalkDao;
import com.poison.resource.dao.MvListLinkDAO;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MvListLink;

public class MovieTalkDomainRepository {
	private MovieTalkDao movieTalkDAO;
	
	public void setMovieTalkDAO(MovieTalkDao movieTalkDAO) {
		this.movieTalkDAO = movieTalkDAO;
	}
	/**
	 * 
	 * 方法的描述 :创建一条电影讨论
	 * @param mv
	 * @return
	 */
	public long addMovieTalk(MovieTalk movieTalk){
		return movieTalkDAO.addMovieTalk(movieTalk);
	};
	/**
	 * 
	 * 方法的描述 :查询电影讨论列表
	 * @param mv
	 * @return
	 */
	public List<MovieTalk> findMovieTalkList(int movieId,Long id){
		return movieTalkDAO.findMovieTalkList(movieId,id);
	}
	/**
	 * 
	 * 方法的描述 :删除一条电影讨论
	 * @param mv
	 * @return
	 */
	public int delMovieTalk(Long id){
		return movieTalkDAO.delMovieTalk(id);
	}
	
}
