package com.poison.resource.domain.repository;

import java.util.ArrayList;
import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MyMovieDAO;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MyMovie;
/**
 * 
 * 类的作用:此类的作用对dao层的一个封装
 * 作者:闫前刚
 * 创建时间:2014-8-9上午11:45:10
 * email :1486488968@qq.com
 * version: 1.0
 */
public class MyMovieDomainRepository {
	private MyMovieDAO myMovieDAO;

	public void setMyMovieDAO(MyMovieDAO myMovieDAO) {
		this.myMovieDAO = myMovieDAO;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是添加一部电影
	 * @param mv
	 * @return
	 */
	public int addMyMovie(MyMovie mv){
		return myMovieDAO.addMyMovie(mv);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是模糊、精准查询
	 * @param name
	 * @return
	 */
	public List<MyMovie> findMyMovieList(String name){
		List<MyMovie> mlist=new ArrayList<MyMovie>();
		//精准查询
		mlist=myMovieDAO.findMyMovieList(name);
		if(mlist==null || mlist.size()==0){
			//模糊查询
			mlist=myMovieDAO.findMyMovieLikeList(name);
		}
		return mlist;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询已经添加的书籍
	 * @param mv
	 * @return
	 */
	public MyMovie findMyMovieIsExist(MyMovie mv){
		return myMovieDAO.findMyMovieIsExist(mv);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id查询自建表信息
	 * @param id
	 * @return
	 */
	public MyMovie findMyMovieInfo(long id){
		return myMovieDAO.findMyMovieInfo(id);
	}
	
}
