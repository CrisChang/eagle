package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.GraphicFilmDomainRepository;
import com.poison.resource.model.GraphicFilm;
import com.poison.resource.service.GraphicFilmService;

public class GraphicFilmServiceImpl implements GraphicFilmService{

	
	private GraphicFilmDomainRepository graphicFilmDomainRepository;
	
	
	public void setGraphicFilmDomainRepository(
			GraphicFilmDomainRepository graphicFilmDomainRepository) {
		this.graphicFilmDomainRepository = graphicFilmDomainRepository;
	}


	/**
	 * 插入图解电影
	 */
	@Override
	public GraphicFilm insertGraphicFilm(GraphicFilm graphicFilm) {
		return graphicFilmDomainRepository.insertGraphicFilm(graphicFilm);
	}


	/**
	 * 根据id查询图解信息
	 */
	@Override
	public GraphicFilm findGraphicFilmById(long id) {
		return graphicFilmDomainRepository.findGraphicFilmById(id);
	}


	/**
	 * 根据uid查询图解电影
	 */
	@Override
	public List<GraphicFilm> findGraphicFilmByUid(long uid) {
		return graphicFilmDomainRepository.findGraphicFilmByUid(uid);
	}

}
