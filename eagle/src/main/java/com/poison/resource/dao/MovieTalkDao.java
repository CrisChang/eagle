package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyMovie;

public interface MovieTalkDao {
	/**
	 * 
	 * 方法的描述 :创建一条电影讨论
	 * @param mv
	 * @return
	 */
	public long addMovieTalk(MovieTalk movieTalk);
	/**
	 * 
	 * 方法的描述 :查询电影讨论列表
	 * @param mv
	 * @return
	 */
	public List<MovieTalk> findMovieTalkList(int movieId,Long id);
	/**
	 * 
	 * 方法的描述 :删除一条电影讨论
	 * @param mv
	 * @return
	 */
	public int delMovieTalk(Long id);
}
