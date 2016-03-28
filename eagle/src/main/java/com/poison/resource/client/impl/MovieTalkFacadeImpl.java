package com.poison.resource.client.impl;

import java.util.List;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.MovieTalkFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyBk;
import com.poison.resource.service.MovieTalkService;
import com.poison.resource.service.MyBkService;

public class MovieTalkFacadeImpl implements MovieTalkFacade{
	private MovieTalkService movieTalkService;
	private UKeyWorker reskeyWork;
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	@Override
	public long addMovieTalk(int movieId, long uid, String content) {
		MovieTalk movieTalk = new MovieTalk();
		long id = reskeyWork.getId();
		movieTalk.setId(id);
		movieTalk.setMovieId(movieId);
		movieTalk.setUid(uid);
		movieTalk.setContent(content);
		movieTalk.setIsDel(0);
		movieTalk.setType(0);
		movieTalk.setCreateTime(System.currentTimeMillis());
		if(movieTalkService.addMovieTalk(movieTalk) != ResultUtils.INSERT_ERROR){
			return id;
		}else{
			return ResultUtils.INSERT_ERROR;
		}
		
	}

	@Override
	public List<MovieTalk> findMovieTalkList(int movieId,Long id) {
		return movieTalkService.findMovieTalkList(movieId,id);
	}

	@Override
	public int delMovieTalk(Long id) {
		return movieTalkService.delMovieTalk(id);
	}

	public void setMovieTalkService(MovieTalkService movieTalkService) {
		this.movieTalkService = movieTalkService;
	}


}
