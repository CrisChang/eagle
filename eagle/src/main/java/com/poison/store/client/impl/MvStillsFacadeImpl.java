package com.poison.store.client.impl;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.store.client.MvStillsFacade;
import com.poison.store.model.MovieStills;
import com.poison.store.model.OnlineRead;
import com.poison.store.service.MvStillsService;

public class MvStillsFacadeImpl implements MvStillsFacade{

	private MvStillsService mvStillsService;
	
	
	
	public void setMvStillsService(MvStillsService mvStillsService) {
		this.mvStillsService = mvStillsService;
	}

	/**
	 * 插入剧照信息
	 */
	/*@Override
	public int insertintoMvOlineStills(long mvId,String movieStills) {
		MovieStills mvStills = new MovieStills();
		mvStills = mvStillsService.findMvOlineStillsByBkId(mvId);
		int flag = ResultUtils.FILMSTILLS_IS_ALREADY_EXIST;
		if(ResultUtils.DATAISNULL==mvStills.getFlag()){
			UKeyWorker uk=new UKeyWorker(0,1);
			long sysdate = System.currentTimeMillis();
			mvStills.setId(uk.getId());
			mvStills.setMvId(mvId);
			mvStills.setMovieStills(movieStills);
			mvStills.setIsDelete(0);
			mvStills.setOther("");
			mvStills.setCreateDate(sysdate);
			mvStills.setLatestRevisionDate(sysdate);
			flag = mvStillsService.insertintoMvOlineStills(mvStills);
		}
		return flag;
	}*/

	/**
	 * 根据电影id查询剧照
	 */
	@Override
	public MovieStills findMvOlineStillsByBkId(long mvId) {
		MovieStills mvStills = mvStillsService.findMvOlineStillsByBkId(mvId);
		if(ResultUtils.DATAISNULL==mvStills.getFlag()){
			MovieStills stills = new MovieStills();
			UKeyWorker uk=new UKeyWorker(0,1);
			long sysdate = System.currentTimeMillis();
			stills.setId(uk.getId());
			stills.setMvId(mvId);
			stills.setMovieStills("");
			stills.setIsDelete(0);
			stills.setOther("");
			stills.setCreateDate(sysdate);
			stills.setLatestRevisionDate(sysdate);
			stills.setTwoDimensionCode("");
			mvStillsService.insertintoMvOlineStills(stills);
			mvStills = mvStillsService.findMvOlineStillsByBkId(mvId);
		}
		return mvStills;
	}

	/**
	 * 更新剧照
	 */
	@Override
	public MovieStills updateMvStills(String movieStills, long mvId) {
		MovieStills mvStills = new MovieStills();
		long sysdate = System.currentTimeMillis();
		mvStills.setMvId(mvId);
		mvStills.setMovieStills(movieStills);
		mvStills.setLatestRevisionDate(sysdate);
		int flag = mvStillsService.updateMvStills(mvStills);
		MovieStills stills = mvStillsService.findMvOlineStillsByBkId(mvId);
		return stills;
	}

	/**
	 * 更新片花
	 */
	@Override
	public MovieStills updateMvOther(String other, long mvId) {
		MovieStills mvStills = new MovieStills();
		long sysdate = System.currentTimeMillis();
		mvStills.setMvId(mvId);
		mvStills.setOther(other);
		mvStills.setLatestRevisionDate(sysdate);
		int flag = mvStillsService.updateMvOther(mvStills);
		MovieStills stills = mvStillsService.findMvOlineStillsByBkId(mvId);
		return stills;
	}

	/**
	 * 添加电影的二维码
	 */
	@Override
	public MovieStills addMvTwoDimensionCode(long mvId, String twoDimensionCode) {
		MovieStills movieStills = new MovieStills();
		UKeyWorker uk=new UKeyWorker(0,1);
		long sysdate = System.currentTimeMillis();
		movieStills.setId(uk.getId());
		movieStills.setMvId(mvId);
		movieStills.setMovieStills("");
		movieStills.setOther("");
		movieStills.setTwoDimensionCode(twoDimensionCode);
		movieStills.setCreateDate(sysdate);
		movieStills.setLatestRevisionDate(sysdate);
		return mvStillsService.updateMvTwoDimensionCode(movieStills);
	}

}
