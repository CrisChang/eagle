package com.poison.resource.service;

import java.util.List;

import com.poison.resource.model.MvComment;
import com.poison.resource.model.MyMovie;

public interface MyMovieService {
	/**
	 * 
	 * 方法的描述 :此方法的作用是添加一部电影
	 * @param mv
	 * @return
	 */
	public int addMyMovie(MyMovie mv);
	/**
	 * 
	 * 方法的描述 :此方法的作用是模糊、精准查询
	 * @param name
	 * @return
	 */
	public List<MyMovie> findMyMovieList(String name);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询已经添加的书籍
	 * @param mv
	 * @return
	 */
	public MyMovie findMyMovieIsExist(MyMovie mv);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id查询自建表信息
	 * @param id
	 * @return
	 */
	public MyMovie findMyMovieInfo(long id);
	
}
