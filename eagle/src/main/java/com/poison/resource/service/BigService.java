package com.poison.resource.service;

import com.poison.resource.model.Big;

public interface BigService {

	/**
	 * 
	 * <p>Title: findBig</p> 
	 * <p>Description: 查询BIG详情</p> 
	 * @author :changjiang
	 * date 2014-9-26 下午3:10:56
	 * @param attribute
	 * @param branch
	 * @param value
	 * @return
	 */
	public Big findBig(String attribute, String branch, String value);
}
