package com.poison.eagle.manager;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.entity.VersionInfo;

public class VersionsUpdateManager extends SqlMapClientDaoSupport{

	public VersionInfo findLatestVersion(){
		VersionInfo versionInfo = new VersionInfo();
		try{
			versionInfo = (VersionInfo) getSqlMapClientTemplate().queryForObject("findIsUpdateVersion");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return versionInfo;
	}
}
