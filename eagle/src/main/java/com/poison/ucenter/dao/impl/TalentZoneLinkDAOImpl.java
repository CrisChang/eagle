package com.poison.ucenter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.TalentZoneLinkDAO;
import com.poison.ucenter.model.TalentZoneLink;

public class TalentZoneLinkDAOImpl extends SqlMapClientDaoSupport implements TalentZoneLinkDAO{

	private static final  Log LOG = LogFactory.getLog(TalentZoneLinkDAOImpl.class);
	/**
	 * 查询达人圈信息列表
	 */
	@Override
	public List<TalentZoneLink> findTalentZoneLinkList(long talentZoneId) {
		List<TalentZoneLink> talentZoneLinkList = new ArrayList<TalentZoneLink>();
		try{
			talentZoneLinkList = getSqlMapClientTemplate().queryForList("findTalentZoneLinkList",talentZoneId);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			talentZoneLinkList = new ArrayList<TalentZoneLink>();
			TalentZoneLink talentZoneLink = new TalentZoneLink();
			talentZoneLink.setFlag(ResultUtils.QUERY_ERROR);
			talentZoneLinkList.add(talentZoneLink);
		}
		return talentZoneLinkList;
	}

}
