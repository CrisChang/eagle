package com.poison.resource.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ResCollectNumDAO;
import com.poison.resource.model.ResCollectNum;

public class ResCollectNumDAOImpl extends SqlMapClientDaoSupport implements ResCollectNumDAO{

	private static final  Log LOG = LogFactory.getLog(ResCollectNumDAOImpl.class);
	
	/**
	 * 插入资源收藏
	 */
	@Override
	public int insertResCollectNum(ResCollectNum ResCollectNum) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertRescollectnum",ResCollectNum);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 根据id查询资源收藏
	 */
	@Override
	public ResCollectNum findResCollectNumById(ResCollectNum ResCollectNum) {
		ResCollectNum resCollectNum = new ResCollectNum();
		try{
			resCollectNum = (ResCollectNum) getSqlMapClientTemplate().queryForObject("findRescollectnumById",ResCollectNum);
			if(null==resCollectNum){
				resCollectNum = new ResCollectNum();
				resCollectNum.setFlag(ResultUtils.DATAISNULL);
				return resCollectNum;
			}
			resCollectNum.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			resCollectNum = new ResCollectNum();
			resCollectNum.setFlag(ResultUtils.ERROR);
		}
		return resCollectNum;
	}

	/**
	 * 更新收藏
	 */
	@Override
	public int updateResCollectNum(ResCollectNum resCollectNum) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateRescollectnum",resCollectNum);
			flag  = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

}
