package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.GraphicFilmDAO;
import com.poison.resource.model.GraphicFilm;

public class GraphicFilmDAOImpl extends SqlMapClientDaoSupport implements GraphicFilmDAO{

	private static final  Log LOG = LogFactory.getLog(GraphicFilmDAOImpl.class);
	
	/**
	 *插入图解电影信息
	 */
	@Override
	public int insertGraphicFilm(GraphicFilm graphicFilm) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("insertintoGraphicFilm",graphicFilm);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据id查询图解电影
	 */
	@Override
	public GraphicFilm findGraphicFilmById(long id) {
		GraphicFilm graphicFilm = new GraphicFilm();
		try{
			graphicFilm =  (GraphicFilm) getSqlMapClientTemplate().queryForObject("findGraphicFilm",id);
			if(null==graphicFilm){
				graphicFilm = new GraphicFilm();
				graphicFilm.setFlag(ResultUtils.DATAISNULL);
				return graphicFilm;
			}
			graphicFilm.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			graphicFilm = new GraphicFilm();
			graphicFilm.setFlag(ResultUtils.ERROR);
		}
		return graphicFilm;
	}

	/**
	 * 根据uid查询图解电影
	 */
	@Override
	public List<GraphicFilm> findGraphicFilmByUid(long uid) {
		List<GraphicFilm> list = new ArrayList<GraphicFilm>();
		try{
			list = getSqlMapClientTemplate().queryForList("findGraphicFilmByUid",uid);
			if(null==list||list.size()==0){
				list = new ArrayList<GraphicFilm>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return list;
	}

}
