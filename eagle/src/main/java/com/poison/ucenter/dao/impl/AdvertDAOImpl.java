package com.poison.ucenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.ucenter.dao.AdvertDAO;
import com.poison.ucenter.model.Advert;

public class AdvertDAOImpl extends SqlMapClientDaoSupport implements AdvertDAO{

	private static final  Log LOG = LogFactory.getLog(AdvertDAOImpl.class);

	/**
	 * 查询广告信息
	 */
	@Override
	public Advert getAdvertInfo(){
		Advert advert = new Advert();
		try{
			advert = (Advert) getSqlMapClientTemplate().queryForObject("getAdvertInfo");
			if(null==advert){
				return null;
			}
			advert.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			advert = new Advert();
			advert.setFlag(ResultUtils.ERROR);
		}
		return advert;
	}
}
