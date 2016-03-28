package com.poison.resource.client.impl;

import java.util.List;

import com.keel.utils.UKeyWorker;
import com.poison.resource.client.GraphicFilmFacade;
import com.poison.resource.model.GraphicFilm;
import com.poison.resource.service.GraphicFilmService;

public class GraphicFilmFacadeImpl implements GraphicFilmFacade{

	private GraphicFilmService graphicFilmService;
	private UKeyWorker reskeyWork;
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setGraphicFilmService(GraphicFilmService graphicFilmService) {
		this.graphicFilmService = graphicFilmService;
	}

	@Override
	public GraphicFilm insertGraphicFilm(long uid,String title,String content,String type,String description,String cover) {
		GraphicFilm graphicFilm = new GraphicFilm();
		graphicFilm.setId(reskeyWork.getId());
		graphicFilm.setTitle(title);
		graphicFilm.setUid(uid);
		graphicFilm.setContent(content);
		graphicFilm.setType(type);
		graphicFilm.setDescription(description);
		graphicFilm.setCover(cover);
		graphicFilm.setIsDel(0);
		long sysdate = System.currentTimeMillis();
		graphicFilm.setCreateDate(sysdate);
		graphicFilm.setLatestRevisionDate(sysdate);
		return graphicFilmService.insertGraphicFilm(graphicFilm);
	}

	/**
	 * 根据id查询图解电影
	 */
	@Override
	public GraphicFilm findGraphicFilmById(long id) {
		return graphicFilmService.findGraphicFilmById(id);
	}

	/**
	 * 查询某个人的图解电影
	 */
	@Override
	public List<GraphicFilm> findGraphicFilmByUid(long uid) {
		return graphicFilmService.findGraphicFilmByUid(uid);
	}
	
}
