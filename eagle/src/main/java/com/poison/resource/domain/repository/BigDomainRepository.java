package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BigDAO;
import com.poison.resource.dao.BkAvgMarkDAO;
import com.poison.resource.dao.BkCommentDAO;
import com.poison.resource.model.Big;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;

public class BigDomainRepository {

	private BigDAO bigDAO;

	public void setBigDAO(BigDAO bigDAO) {
		this.bigDAO = bigDAO;
	}
	
	/**
	 * 
	 * <p>Title: findBig</p> 
	 * <p>Description: 查询big详情</p> 
	 * @author :changjiang
	 * date 2014-9-26 下午3:08:43
	 * @param attribute
	 * @param branch
	 * @param value
	 * @return
	 */
	public Big findBig(String attribute, String branch, String value){
		return bigDAO.findBig(attribute, branch, value);
	}
}
