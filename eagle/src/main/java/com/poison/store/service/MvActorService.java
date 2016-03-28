package com.poison.store.service;

import java.util.List;

import com.poison.store.model.Actor;
import com.poison.store.model.MvActor;

/**
 * @Todo:TODO
 * @author :zhangqi
 * @time:2015-5-9上午10:26:43
 * @return
 */
public interface MvActorService {

	public List<MvActor> findListMvActorByMvId(long mvId);

	public Actor findActorWithStillById(long id);

	public Actor findActorById(long id);

	public List<MvActor> findListMvActorByActorId(long actorId, int start, int pageSize);
	
	public List<MvActor> findListMvActorByActorIdDictinctMv(long actorId, int start, int pageSize);

	public List<MvActor> findListMvActorByActorIdDictinctMvUrl(long actorId, int size, int page);

	public List<MvActor> findListMvActorByMvId(long mvId, String type);

	// public Actor findActorByActorUrl(String actorUrl);
	// public ActorStills findActorStillsById(long id);
	// public ActorStills findActorStillsByActorId(long actorId);
	// public ActorStills findActorStillsByActorUrl(String actorUrl);
	// public MvActor findMvActorById(long id);

}
