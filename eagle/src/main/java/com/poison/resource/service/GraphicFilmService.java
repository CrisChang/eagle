package com.poison.resource.service;

import java.util.List;

import com.poison.resource.model.GraphicFilm;

public interface GraphicFilmService {

	/**
	 * 
	 * <p>Title: insertGraphicFilm</p> 
	 * <p>Description: 插入图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午8:05:46
	 * @param graphicFilm
	 * @return
	 */
	public GraphicFilm insertGraphicFilm(GraphicFilm graphicFilm);
	
	/**
	 * 
	 * <p>Title: findGraphicFilmById</p> 
	 * <p>Description: 根据id查询图解信息</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午8:12:03
	 * @param id
	 * @return
	 */
	public GraphicFilm findGraphicFilmById(long id);
	
	/**
	 * 
	 * <p>Title: findGraphicFilmByUid</p> 
	 * <p>Description: 根据uid查询图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-22 下午6:35:47
	 * @param uid
	 * @return
	 */
	public List<GraphicFilm> findGraphicFilmByUid(long uid);
}
