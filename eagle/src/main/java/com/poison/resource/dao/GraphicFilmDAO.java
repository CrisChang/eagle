package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.GraphicFilm;

public interface GraphicFilmDAO {

	/**
	 * 
	 * <p>Title: insertGraphicFilm</p> 
	 * <p>Description: 插入图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午7:49:05
	 * @param graphicFilm
	 * @return
	 */
	public int insertGraphicFilm(GraphicFilm graphicFilm);
	
	/**
	 * 
	 * <p>Title: findGraphicFilmById</p> 
	 * <p>Description: 根据id查找图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午7:50:03
	 * @param id
	 * @return
	 */
	public GraphicFilm findGraphicFilmById(long id);
	
	/**
	 * 
	 * <p>Title: findGraphicFilmByUid</p> 
	 * <p>Description: 根据uid查询图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-22 下午6:31:24
	 * @param uid
	 * @return
	 */
	public List<GraphicFilm> findGraphicFilmByUid(long uid);
}
