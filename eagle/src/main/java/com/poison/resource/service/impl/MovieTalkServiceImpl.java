package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.dao.MovieTalkDao;
import com.poison.resource.domain.repository.MovieTalkDomainRepository;
import com.poison.resource.domain.repository.MyMovieDomainRepository;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyMovie;
import com.poison.resource.service.MovieTalkService;
import com.poison.resource.service.MyMovieService;

public class MovieTalkServiceImpl implements MovieTalkService {
	private MovieTalkDomainRepository movieTalkDomainRepository;
	@Override
	public long addMovieTalk(MovieTalk movieTalk) {
		return movieTalkDomainRepository.addMovieTalk(movieTalk);
	}

	@Override
	public List<MovieTalk> findMovieTalkList(int movieId,Long id) {
		return movieTalkDomainRepository.findMovieTalkList(movieId,id);
	}

	@Override
	public int delMovieTalk(Long id) {
		return movieTalkDomainRepository.delMovieTalk(id);
	}

	public void setMovieTalkDomainRepository(
			MovieTalkDomainRepository movieTalkDomainRepository) {
		this.movieTalkDomainRepository = movieTalkDomainRepository;
	}
	
}
