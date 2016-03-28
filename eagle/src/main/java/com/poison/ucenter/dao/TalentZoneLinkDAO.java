package com.poison.ucenter.dao;

import java.util.List;

import com.poison.ucenter.model.TalentZoneLink;

public interface TalentZoneLinkDAO {

	/**
	 * 
	 * <p>Title: findTalentZoneLinkList</p> 
	 * <p>Description: 查询达人圈人的信息</p> 
	 * @author :changjiang
	 * date 2014-9-12 上午12:33:15
	 * @param talentZoneId
	 * @return
	 */
	public List<TalentZoneLink> findTalentZoneLinkList(long talentZoneId);
}
