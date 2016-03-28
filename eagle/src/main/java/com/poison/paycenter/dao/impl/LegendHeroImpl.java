package com.poison.paycenter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.LegendHeroDao;
import com.poison.paycenter.model.LegendHero;

/**
 * 封神榜 impl
 * 
 * @author :zhangqi
 * @time:2015-4-21下午6:07:34
 * @return
 */
public class LegendHeroImpl extends SqlMapClientDaoSupport implements
		LegendHeroDao {

	private static final Log LOG = LogFactory.getLog(LegendHeroImpl.class);

	@Override
	public int insertIntoLegendHero(LegendHero hero) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().insert("insertIntoLegendHero", hero);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.INSERT_ERROR;
		}
		return flag;
	}

	@Override
	public List<LegendHero> findAllLegendHero() {
		List<LegendHero> list = new ArrayList<LegendHero>();
		try {
			list = getSqlMapClientTemplate().queryForList("findAllLegendHero");
			if (null == list || list.size() == 0) {
				list = new ArrayList<LegendHero>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = new ArrayList<LegendHero>();
		}
		return list;
	}

	public LegendHero findLegendHeroById(long id) {
		LegendHero hero = new LegendHero();
		int flag = ResultUtils.ERROR;
		try {
			hero = (LegendHero) getSqlMapClientTemplate().queryForObject(
					"findLegendHeroById", id);
			if (hero == null) {
				hero = new LegendHero();
				flag = ResultUtils.SUCCESS;
			} else {
				flag = ResultUtils.DATAISNULL;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.QUERY_ERROR;
			hero = new LegendHero();
		}
		hero.setFlag(flag);
		return hero;
	}

	public LegendHero findLegendHeroByUserId(long userId) {
		LegendHero hero = new LegendHero();
		int flag = ResultUtils.ERROR;
		try {
			hero = (LegendHero) getSqlMapClientTemplate().queryForObject(
					"findLegendHeroByUserId", userId);
			if (hero == null) {
				flag = ResultUtils.SUCCESS;
				hero = new LegendHero();
			} else {
				flag = ResultUtils.DATAISNULL;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.QUERY_ERROR;
			hero = new LegendHero();
		}
		hero.setFlag(flag);
		return hero;
	}

	public int updateLegendHeroById(LegendHero hero) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().update("updateLegendHeroById", hero);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	public int updateLegendHeroByUserId(LegendHero hero) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().update("updateLegendHeroByUserId", hero);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.UPDATE_ERROR;
		}
		return flag;
	}

	public int deleteLegendHeroById(long id) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate().delete("deleteLegendHeroById", id);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.DELETE_ERROR;
		}
		return flag;
	}

	public int deleteLegendHeroByUserId(long userId) {
		int flag = ResultUtils.ERROR;
		try {
			getSqlMapClientTemplate()
					.delete("deleteLegendHeroByUserId", userId);
			flag = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flag = ResultUtils.DELETE_ERROR;
		}
		return flag;
	}

}
