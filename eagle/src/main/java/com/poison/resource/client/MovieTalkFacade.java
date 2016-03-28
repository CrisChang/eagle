package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.MovieList;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.MyMovie;

/**
 * 
 * 类的作用：
 * 作者:温晓宁
 * 创建时间:2014-8-9下午3:42:59
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface MovieTalkFacade {

	/**
	 * 
	 * 方法的描述 :创建一条电影讨论
	 * @param mv
	 * @return
	 */
	public long addMovieTalk(int movieId , long uid , String content);
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
