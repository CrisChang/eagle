package com.poison.store.service.impl;

import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.domain.repository.MvActorDomainRepository;
import com.poison.store.model.Actor;
import com.poison.store.model.ActorStills;
import com.poison.store.model.MvActor;
import com.poison.store.service.MvActorService;

/**
 * @Todo:TODO
 * @author :zhangqi
 * @time:2015-5-9上午10:29:15
 * @return
 */
public class MvActorServiceImpl implements MvActorService {

	private MvActorDomainRepository mvActorDomainRepository;

	public void setMvActorDomainRepository(MvActorDomainRepository mvActorDomainRepository) {
		this.mvActorDomainRepository = mvActorDomainRepository;
	}

	public Actor findActorById(long id) {
		return mvActorDomainRepository.findActorById(id);
	}

	@Override
	public Actor findActorWithStillById(long id) {
		Actor actor = mvActorDomainRepository.findActorById(id);
		if (actor.getFlag() == ResultUtils.SUCCESS) {
			ActorStills actorStills = mvActorDomainRepository.findActorStillsByActorId(actor.getId());
			if (actorStills.getFlag() == ResultUtils.SUCCESS) {
				actor.setActorStills(actorStills);
			}
		}
		return actor;
	}

	@Override
	public List<MvActor> findListMvActorByMvId(long mvId) {
		return mvActorDomainRepository.findListMvActorByMvId(mvId);
	}

	public List<MvActor> findListMvActorByActorId(long actorId, int start, int pageSize) {
		return mvActorDomainRepository.findListMvActorByActorId(actorId, start, pageSize);
	}
	
	public List<MvActor> findListMvActorByActorIdDictinctMv(long actorId, int start, int pageSize) {
		return mvActorDomainRepository.findListMvActorByActorIdDictinctMv(actorId, start, pageSize);
	}

	public List<MvActor> findListMvActorByActorIdDictinctMvUrl(long actorId, int start, int pageSize) {
		return mvActorDomainRepository.findListMvActorByActorIdDictinctMvUrl(actorId, start, pageSize);
	}

	public List<MvActor> findListMvActorByMvId(long mvId, String type) {
		return mvActorDomainRepository.findListMvActorByMvId(mvId, type);
	}

}
