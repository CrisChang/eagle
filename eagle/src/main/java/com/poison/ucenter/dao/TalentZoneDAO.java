package com.poison.ucenter.dao;

import java.util.List;

import com.poison.ucenter.model.TalentZone;

public interface TalentZoneDAO {

	/**
	 * 
	 * <p>Title: findTalentZoneInfo</p> 
	 * <p>Description: 查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-11 下午10:34:15
	 * @param id
	 * @return
	 */
	public TalentZone findTalentZoneInfo(long id);
	
	/**
	 * 
	 * <p>Title: findTalentZoneInfoByType</p> 
	 * <p>Description: 根据type查询达人圈信息</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午12:04:24
	 * @param type
	 * @return
	 */
	public TalentZone findTalentZoneInfoByType(String type);
	
	/**
	 * 
	 * <p>Title: findAllTalentZone</p> 
	 * <p>Description: 查询所有的达人圈</p> 
	 * @author :changjiang
	 * date 2014-9-28 下午9:35:30
	 * @return
	 */
	public List<TalentZone> findAllTalentZone();
}
