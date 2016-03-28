package com.poison.store.domain.repository;

import java.util.List;
import java.util.Map;

import com.poison.store.dao.MvActorDAO;
import com.poison.store.model.Actor;
import com.poison.store.model.ActorStills;
import com.poison.store.model.MvActor;

/**
 * 电影角色操作 Repository
 * 
 * @author zhangqi
 * 
 */
public class MvActorDomainRepository {

	private MvActorDAO mvActorDAO;

	public void setMvActorDAO(MvActorDAO mvActorDAO) {
		this.mvActorDAO = mvActorDAO;
	}

	// ==========MvActor============
	/**
	 * 查询电影角色ById
	 * 
	 * @param id
	 *            c_db_mv_actor 主键ID
	 * @return
	 */
	public MvActor findMvActorById(long id) {
		return mvActorDAO.findMvActorById(id);
	}

	public List<MvActor> findListMvActorByActorId(long actorId, int start, int pageSize) {
		return mvActorDAO.findListMvActorByActorId(actorId, start, pageSize);
	}

	public List<MvActor> findListMvActorByActorIdDictinctMv(long actorId, int start, int pageSize) {
		return mvActorDAO.findListMvActorByActorIdDictinctMv(actorId, start, pageSize);
	}

	public List<MvActor> findListMvActorByActorIdDictinctMvUrl(long actorId, int start, int pageSize) {
		return mvActorDAO.findListMvActorByActorIdDictinctMvUrl(actorId, start, pageSize);
	}

	/**
	 * 查找单个电影角色
	 * 
	 * @param input
	 * @return
	 */
	public MvActor findMvActor(Map<String, Object> input) {
		return mvActorDAO.findMvActor(input);
	}

	/**
	 * 查询电影角色列表
	 * 
	 * @param input
	 * @return
	 */
	public List<MvActor> findListMvActor(Map<String, Object> input) {
		return mvActorDAO.findListMvActor(input);
	}

	public List<MvActor> findListMvActorByMvId(long mvId) {
		return mvActorDAO.findListMvActorByMvId(mvId);
	}

	public List<MvActor> findListMvActorByMvUrl(String mvUrl) {
		return mvActorDAO.findListMvActorByMvUrl(mvUrl);
	}

	public List<MvActor> findListMvActorByMvId(long mvId, String type) {
		return mvActorDAO.findListMvActorByMvId(mvId, type);
	}

	// ==========Actor============
	/**
	 * 查询角色 ById
	 * 
	 * @param id
	 *            角色ID 主键
	 * @return
	 */
	public Actor findActorById(long id) {
		return mvActorDAO.findActorById(id);
	}

	/**
	 * 查询角色 ByActorUrl
	 * 
	 * @param actorUrl
	 *            角色 url
	 * @return
	 */
	public Actor findActorByActorUrl(String actorUrl) {
		return mvActorDAO.findActorByActorUrl(actorUrl);
	}

	/**
	 * 查询单个角色
	 * 
	 * @param input
	 * @return
	 */
	public Actor findActor(Map<String, Object> input) {
		return mvActorDAO.findActor(input);
	}

	/**
	 * 查询角色 列表
	 * 
	 * @param input
	 * @return
	 */
	public List<Actor> findListActor(Map<String, Object> input) {
		return mvActorDAO.findListActor(input);
	}

	// ==========ActorStills============
	/**
	 * 查询角色剧照 byId
	 * 
	 * @param id
	 *            剧照ID 主键
	 * @return
	 */
	public ActorStills findActorStillsById(long id) {
		return mvActorDAO.findActorStillsById(id);
	}

	/**
	 * 查询角色剧照
	 * 
	 * @param actorId
	 *            角色ID
	 * @return
	 */
	public ActorStills findActorStillsByActorId(long actorId) {
		return mvActorDAO.findActorStillsByActorId(actorId);
	}

	/**
	 * 查询角色剧照
	 * 
	 * @param actorUrl
	 *            角色URL
	 * @return
	 */
	public ActorStills findActorStillsByActorUrl(String actorUrl) {
		return mvActorDAO.findActorStillsByActorUrl(actorUrl);
	}

	/**
	 * 查询单个角色剧照
	 * 
	 * @param input
	 * @return
	 */
	public ActorStills findActorStills(Map<String, Object> input) {
		return mvActorDAO.findActorStills(input);
	}

	/**
	 * 查询角色列表
	 * 
	 * @param input
	 * @return
	 */
	public List<ActorStills> findListActorStills(Map<String, Object> input) {
		return mvActorDAO.findListActorStills(input);
	}

}
