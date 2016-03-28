package com.poison.resource.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ResReportDAO;
import com.poison.resource.model.ResReport;

public class ResReportDAOImpl extends SqlMapClientDaoSupport implements ResReportDAO{

	private static final  Log LOG = LogFactory.getLog(ResReportDAOImpl.class);
	
	/**
	 * 插入举报信息
	 */
	@Override
	public int insertResReport(ResReport report) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertReport",report);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 根据id查询举报信息
	 */
	@Override
	public ResReport findResReportById(long id) {
		ResReport resReport = new ResReport();
		try{
			resReport = (ResReport) getSqlMapClientTemplate().queryForObject("queryReportById",id);
			if(null==resReport){
				resReport = new ResReport();
				resReport.setFlag(ResultUtils.DATAISNULL);
				return resReport;
			}
			resReport.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			resReport = new ResReport();
			resReport.setFlag(ResultUtils.QUERY_ERROR);
		}
		return resReport;
	}

	/**
	 * 查询用户举报信息是否存在
	 */
	@Override
	public ResReport findResReportIsExist(long userId, long resId) {
		ResReport resReport = new ResReport();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("resId", resId);
			resReport  = (ResReport) getSqlMapClientTemplate().queryForObject("findResReportIsExist",map);
			if(null == resReport){
				resReport = new ResReport();
				resReport.setFlag(ResultUtils.DATAISNULL);
				return resReport;
			}
			resReport.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			resReport = new ResReport();
			resReport.setFlag(ResultUtils.QUERY_ERROR);
		}
		return resReport;
	}

}
