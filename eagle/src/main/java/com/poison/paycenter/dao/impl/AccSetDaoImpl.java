package com.poison.paycenter.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.paycenter.dao.AccSetDao;
import com.poison.paycenter.model.AccSet;

/**
 * @author weizhensong
 *
 */
public class AccSetDaoImpl extends SqlMapClientDaoSupport implements AccSetDao {
	private static final Log LOG = LogFactory.getLog(AccSetDaoImpl.class);
	@Override
	public AccSet getAccSet() {
		AccSet set = new AccSet();
		try{
			set = (AccSet) getSqlMapClientTemplate().queryForObject("getAccSet");
			if(null==set){
				set = new AccSet();
				set.setFlag(ResultUtils.DATAISNULL);
				return set;
			}
			set.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			set = new AccSet();
			set.setFlag(ResultUtils.QUERY_ERROR);
		}
		return set;
	}
}
