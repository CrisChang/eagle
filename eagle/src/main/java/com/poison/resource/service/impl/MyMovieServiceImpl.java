package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.MyMovieDomainRepository;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MyMovie;
import com.poison.resource.service.MyMovieService;

public class MyMovieServiceImpl implements MyMovieService {
	private MyMovieDomainRepository myMovieDomainRepository;
	
	public void setMyMovieDomainRepository(
			MyMovieDomainRepository myMovieDomainRepository) {
		this.myMovieDomainRepository = myMovieDomainRepository;
	}

	@Override
	public int addMyMovie(MyMovie mv) {
		return myMovieDomainRepository.addMyMovie(mv);
	}

	@Override
	public List<MyMovie> findMyMovieList(String name) {
		return myMovieDomainRepository.findMyMovieList(name);
	}

	@Override
	public MyMovie findMyMovieIsExist(MyMovie mv) {
		return myMovieDomainRepository.findMyMovieIsExist(mv);
	}

	@Override
	public MyMovie findMyMovieInfo(long id) {
		return myMovieDomainRepository.findMyMovieInfo(id);
	}


}
