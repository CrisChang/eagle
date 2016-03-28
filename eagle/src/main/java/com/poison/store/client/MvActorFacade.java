package com.poison.store.client;

import java.util.List;

import com.poison.store.model.Actor;
import com.poison.store.model.MvActor;

/**
 * @Todo:TODO
 * @author :zhangqi
 * @time:2015-5-9上午10:26:43
 * @return
 */
public interface MvActorFacade {

	public List<MvActor> findListMvActorByMvId(long mvId);

	public Actor findActorById(long id);

	public Actor findActorWithStillById(long id);

	public List<MvActor> findListMvActorByActorId(long actorId, int start, int page);

	public List<MvActor> findListMvActorByActorIdDictinctMv(long actorId, int start, int page);

	public List<MvActor> findListMvActorByActorIdDictinctMvUrl(long actorId, int start, int page);

	public List<MvActor> findListMvActorByMvId(long mvId, String actorType);

}
