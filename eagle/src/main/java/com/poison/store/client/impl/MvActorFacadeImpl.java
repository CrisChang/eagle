package com.poison.store.client.impl;

import java.util.List;
import java.util.Map;

import com.poison.store.client.MvActorFacade;
import com.poison.store.model.Actor;
import com.poison.store.model.MvActor;
import com.poison.store.service.MvActorService;

/**
 * @Todo:TODO
 * @author :zhangqi
 * @time:2015-5-9上午10:29:15
 * @return
 */
public class MvActorFacadeImpl implements MvActorFacade {

	private MvActorService mvActorService;

	public void setMvActorService(MvActorService mvActorService) {
		this.mvActorService = mvActorService;
	}

	public List<MvActor> findListMvActorByMvId(long mvId) {
		return mvActorService.findListMvActorByMvId(mvId);
	}

	public Actor findActorById(long id) {
		return mvActorService.findActorById(id);
	}

	public Actor findActorWithStillById(long id) {
		return mvActorService.findActorWithStillById(id);
	}

	public List<MvActor> findListMvActorByActorId(long actorId, int size, int page) {
		return mvActorService.findListMvActorByActorId(actorId, size, page);
	}

	public List<MvActor> findListMvActorByActorIdDictinctMv(long actorId, int size, int page) {
		return mvActorService.findListMvActorByActorIdDictinctMv(actorId, size, page);
	}

	public List<MvActor> findListMvActorByActorIdDictinctMvUrl(long actorId, int size, int page) {
		return mvActorService.findListMvActorByActorIdDictinctMvUrl(actorId, size, page);
	}

	public List<MvActor> findListMvActorByMvId(long mvId, String actorType) {
		return mvActorService.findListMvActorByMvId(mvId, actorType);
	}

}
