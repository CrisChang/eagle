package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.store.dao.impl.BigSelectingDAOImpl;
import com.poison.ucenter.dao.TalentZoneDAO;
import com.poison.ucenter.model.TalentZone;

public class TalentZoneDAOImpl extends SqlMapClientDaoSupport implements TalentZoneDAO{

	private static final  Log LOG = LogFactory.getLog(TalentZoneDAOImpl.class);
	/**
	 * 查询达人圈信息
	 */
	@Override
	public TalentZone findTalentZoneInfo(long id) {
		TalentZone talentZone = new TalentZone();
		try{
			talentZone = (TalentZone) getSqlMapClientTemplate().queryForObject("findTalentZoneInfo",id);
			if(null==talentZone){
				talentZone = new TalentZone();
				talentZone.setFlag(ResultUtils.DATAISNULL);
				return talentZone;
			}
			talentZone.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			talentZone = new TalentZone();
			talentZone.setFlag(ResultUtils.QUERY_ERROR);
		}
		return talentZone;
	}

	/**
	 * 根据type查询达人圈信息
	 */
	@Override
	public TalentZone findTalentZoneInfoByType(String type) {
		TalentZone talentZone = new TalentZone();
		try{
			talentZone = (TalentZone) getSqlMapClientTemplate().queryForObject("findTalentZoneInfoByType",type);
			if(null==talentZone){
				talentZone = new TalentZone();
				talentZone.setFlag(ResultUtils.DATAISNULL);
				return talentZone;
			}
			talentZone.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			talentZone = new TalentZone();
			talentZone.setFlag(ResultUtils.QUERY_ERROR);
		}
		return talentZone;
	}

	/**
	 * 查询所有的达人圈
	 */
	@Override
	public List<TalentZone> findAllTalentZone() {
		List<TalentZone> list = new ArrayList<TalentZone>();
		TalentZone talentZone = new TalentZone();
		try{
			list = getSqlMapClientTemplate().queryForList("findAllTalentZone");
			if(null==list||list.size()==0){
				talentZone.setFlag(ResultUtils.DATAISNULL);
				list.add(talentZone);
				return list;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			talentZone.setFlag(ResultUtils.QUERY_ERROR);
			list.add(talentZone);
		}
		return list;
	}

}
