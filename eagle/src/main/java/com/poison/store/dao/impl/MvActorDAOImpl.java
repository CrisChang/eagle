package com.poison.store.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.MvActorDAO;
import com.poison.store.model.Actor;
import com.poison.store.model.ActorStills;
import com.poison.store.model.MvActor;

/**
 * @Todo:TODO
 * @author :zhangqi
 * @time:2015-5-8下午12:44:43
 * @return
 */
@SuppressWarnings("deprecation")
public class MvActorDAOImpl extends SqlMapClientDaoSupport implements MvActorDAO {

	private static final Log LOG = LogFactory.getLog(MvActorDAOImpl.class);
	/** 导演 **/
	public static final String actorTypeDirector = "1";
	/** 编剧 **/
	public static final String actorTypeScriptwriter = "2";
	/** 主演 **/
	public static final String actorTypeStarring = "3";

	public MvActor findMvActor(Map<String, Object> input) {
		MvActor actor = new MvActor();
		try {
			actor = (MvActor) getSqlMapClientTemplate().queryForObject("findMvActor", input);
			if (actor != null) {
				actor.setFlag(ResultUtils.SUCCESS);
			} else {
				actor = new MvActor();
				actor.setFlag(ResultUtils.DATAISNULL);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			actor = new MvActor();
			actor.setFlag(ResultUtils.QUERY_ERROR);
			return actor;
		}
		return actor;
	}

	@Override
	public MvActor findMvActorById(long id) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("id", id);
		input.put("isDel", 0);
		return findMvActor(input);
	}

	@Override
	public List<MvActor> findListMvActorByActorId(long actorId, int start, int pageSize) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("actorId", actorId);
		input.put("start", start);
		input.put("pageSize", pageSize);
		return findListMvActorByPage(input);
	}

	public List<MvActor> findListMvActorByActorIdDictinctMv(long actorId, int start, int pageSize) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("actorId", actorId);
		input.put("start", start);
		input.put("pageSize", pageSize);
		return findListMvActorByActorIdDictinctMv(input);
	}

	public List<MvActor> findListMvActorByActorIdDictinctMvUrl(long actorId, int start, int pageSize) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("actorId", actorId);
		input.put("start", start);
		input.put("pageSize", pageSize);
		input.put("isDel", 0);
		List<MvActor> mvInfoList = new ArrayList<MvActor>();
		try {
			mvInfoList = getSqlMapClientTemplate().queryForList("findListMvActorByPageDistinctMvUrl", input);
		} catch (Exception e) {
			mvInfoList = new ArrayList<MvActor>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return mvInfoList;
	}

	@Override
	public List<MvActor> findListMvActorByMvId(long mvId) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("mvId", mvId);
		return findListMvActor(input);
	}

	public List<MvActor> findListMvActorByMvId(long mvId, String type) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("mvId", mvId);
		if (!StringUtils.isEmpty(type))
			input.put("actorType", type);
		return findListMvActor(input);
	}

	public List<MvActor> findListMvActorByMvUrl(String mvUrl) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("mvUrl", mvUrl);
		return findListMvActor(input);
	}

	public List<MvActor> findListMvActor(Map<String, Object> input) {
		List<MvActor> mvInfoList = new ArrayList<MvActor>();
		try {
			input.put("isDel", 0);
			mvInfoList = getSqlMapClientTemplate().queryForList("findListMvActor", input);
		} catch (Exception e) {
			mvInfoList = new ArrayList<MvActor>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return mvInfoList;
	}

	public List<MvActor> findListMvActorByPage(Map<String, Object> input) {
		List<MvActor> mvInfoList = new ArrayList<MvActor>();
		try {
			input.put("isDel", 0);
			mvInfoList = getSqlMapClientTemplate().queryForList("findListMvActorByPage", input);
		} catch (Exception e) {
			mvInfoList = new ArrayList<MvActor>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return mvInfoList;
	}

	public List<MvActor> findListMvActorByActorIdDictinctMv(Map<String, Object> input) {
		List<MvActor> mvInfoList = new ArrayList<MvActor>();
		try {
			input.put("isDel", 0);
			mvInfoList = getSqlMapClientTemplate().queryForList("findListMvActorByPageDictinctMv", input);
		} catch (Exception e) {
			mvInfoList = new ArrayList<MvActor>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return mvInfoList;
	}

	public Actor findActor(Map<String, Object> input) {
		Actor actor = new Actor();
		try {
			actor = (Actor) getSqlMapClientTemplate().queryForObject("findActor", input);
			if (actor != null) {
				actor.setFlag(ResultUtils.SUCCESS);
			} else {
				actor = new Actor();
				actor.setFlag(ResultUtils.DATAISNULL);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			actor = new Actor();
			actor.setFlag(ResultUtils.QUERY_ERROR);
			return actor;
		}
		return actor;
	}

	@Override
	public Actor findActorById(long id) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("id", id);
		return findActor(input);
	}

	@Override
	public Actor findActorByActorUrl(String actorUrl) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("actorUrl", actorUrl);
		return findActor(input);
	}

	public List<Actor> findListActor(Map<String, Object> input) {
		List<Actor> mvInfoList = new ArrayList<Actor>();
		try {
			mvInfoList = getSqlMapClientTemplate().queryForList("findListActor", input);
		} catch (Exception e) {
			mvInfoList = new ArrayList<Actor>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return mvInfoList;
	}

	public ActorStills findActorStills(Map<String, Object> input) {
		ActorStills po = new ActorStills();
		try {
			po = (ActorStills) getSqlMapClientTemplate().queryForObject("findActorStills", input);
			if (po != null) {
				po.setFlag(ResultUtils.SUCCESS);
			} else {
				po = new ActorStills();
				po.setFlag(ResultUtils.DATAISNULL);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			po = new ActorStills();
			po.setFlag(ResultUtils.QUERY_ERROR);
			return po;
		}
		return po;
	}

	@Override
	public ActorStills findActorStillsById(long id) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("id", id);
		return findActorStills(input);
	}

	public List<ActorStills> findListActorStills(Map<String, Object> input) {
		List<ActorStills> poList = new ArrayList<ActorStills>();
		try {
			poList = getSqlMapClientTemplate().queryForList("findListActorStills", input);
		} catch (Exception e) {
			poList = new ArrayList<ActorStills>();
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return poList;
	}

	@Override
	public ActorStills findActorStillsByActorId(long actorId) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("actorId", actorId);
		return findActorStills(input);
	}

	@Override
	public ActorStills findActorStillsByActorUrl(String actorUrl) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("actorUrl", actorUrl);
		return findActorStills(input);
	}

}
