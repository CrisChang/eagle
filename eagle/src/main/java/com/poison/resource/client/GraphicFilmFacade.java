package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.GraphicFilm;

public interface GraphicFilmFacade {

	/**
	 * 
	 * <p>Title: insertGraphicFilm</p> 
	 * <p>Description: 插入图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午8:09:35
	 * @param graphicFilm
	 * @return
	 */
	public GraphicFilm insertGraphicFilm(long uid,String title,String content,String type,String description,String cover);
	
	/**
	 * 
	 * <p>Title: findGraphicFilmById</p> 
	 * <p>Description: 根据id查询图解信息</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午8:13:16
	 * @param id
	 * @return
	 */
	public GraphicFilm findGraphicFilmById(long id);
	
	/**
	 * 
	 * <p>Title: findGraphicFilmByUid</p> 
	 * <p>Description: 查询某个人的图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-22 下午6:23:14
	 * @param uid
	 * @return
	 */
	public List<GraphicFilm> findGraphicFilmByUid(long uid);
}
