package com.poison.store.dao;

import java.util.List;
import java.util.Map;

import com.poison.store.model.Actor;
import com.poison.store.model.ActorStills;
import com.poison.store.model.MvActor;

/**
 * 角色DAO
 * 
 * @author :zhangqi
 * @time:2015-5-8下午12:41:51
 * @return
 */
public interface MvActorDAO {

	// ==========MvActor============

	public MvActor findMvActorById(long id);

	public MvActor findMvActor(Map<String, Object> input);

	public List<MvActor> findListMvActorByActorId(long actorId, int start, int pageSize);
	
	public List<MvActor> findListMvActorByActorIdDictinctMv(long actorId, int start, int pageSize);
	
	public List<MvActor> findListMvActorByActorIdDictinctMvUrl(long actorId, int start, int pageSize);

	public List<MvActor> findListMvActorByMvId(long mvId);

	public List<MvActor> findListMvActorByMvUrl(String mvUrl);

	public List<MvActor> findListMvActor(Map<String, Object> input);

	public List<MvActor> findListMvActorByMvId(long mvId, String type);

	// ==========Actor============
	public Actor findActor(Map<String, Object> input);

	public Actor findActorById(long id);

	public Actor findActorByActorUrl(String actorUrl);

	public List<Actor> findListActor(Map<String, Object> input);

	// ==========ActorStills============
	public ActorStills findActorStills(Map<String, Object> input);

	public ActorStills findActorStillsById(long id);

	public ActorStills findActorStillsByActorId(long actorId);

	public ActorStills findActorStillsByActorUrl(String actorUrl);

	public List<ActorStills> findListActorStills(Map<String, Object> input);

}
