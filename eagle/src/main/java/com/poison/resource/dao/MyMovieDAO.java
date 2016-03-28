package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.MyMovie;

public interface MyMovieDAO {
	/**
	 * 
	 * 方法的描述 :创建一部电影
	 * @param mv
	 * @return
	 */
	public int addMyMovie(MyMovie mv);
	/**
	 * 
	 * 方法的描述 :查询用户添加电影是否存在
	 * @param mv
	 * @return
	 */
	public MyMovie findMyMovieIsExist(MyMovie mv);
	/**
	 * 
	 * 方法的描述 :此方法的作用是精确查找
	 * @param name
	 * @return
	 */
	public List<MyMovie> findMyMovieList(String name);
	/**
	 * 
	 * 方法的描述 :此方法的作用是精确查找
	 * @param name
	 * @return
	 */
	public List<MyMovie> findMyMovieLikeList(String name);
	/**
	 * 
	 * 方法的描述 :根据id查询自建库中的信息
	 * @param id
	 * @return
	 */
	public MyMovie findMyMovieInfo(long id);
}
