package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.resource.dao.GraphicFilmDAO;
import com.poison.resource.model.GraphicFilm;

public class GraphicFilmDomainRepository {

	private GraphicFilmDAO graphicFilmDAO;

	public void setGraphicFilmDAO(GraphicFilmDAO graphicFilmDAO) {
		this.graphicFilmDAO = graphicFilmDAO;
	}
	
	/**
	 * 
	 * <p>Title: insertGraphicFilm</p> 
	 * <p>Description: 插入图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午8:00:55
	 * @param graphicFilm
	 * @return
	 */
	public GraphicFilm insertGraphicFilm(GraphicFilm graphicFilm){
		int flag = graphicFilmDAO.insertGraphicFilm(graphicFilm);
		long id = graphicFilm.getId();
		graphicFilm= graphicFilmDAO.findGraphicFilmById(id);
		return graphicFilm;
	}
	
	/**
	 * 
	 * <p>Title: findGraphicFilmById</p> 
	 * <p>Description: 根据id查询图解信息</p> 
	 * @author :changjiang
	 * date 2015-1-20 下午8:11:34
	 * @param id
	 * @return
	 */
	public GraphicFilm findGraphicFilmById(long id){
		return graphicFilmDAO.findGraphicFilmById(id);
	}
	
	/**
	 * 
	 * <p>Title: findGraphicFilmByUid</p> 
	 * <p>Description: 根据uid查询图解电影</p> 
	 * @author :changjiang
	 * date 2015-1-22 下午6:35:06
	 * @param uid
	 * @return
	 */
	public List<GraphicFilm> findGraphicFilmByUid(long uid) {
		return graphicFilmDAO.findGraphicFilmByUid(uid);
	}
}
