package com.poison.resource.dao;

import com.poison.resource.model.Big;

public interface BigDAO {

	/**
	 * 
	 * <p>Title: findBig</p> 
	 * <p>Description: 根据输入值获取big详情</p> 
	 * @author :changjiang
	 * date 2014-9-26 下午2:37:50
	 * @param attribute
	 * @param branch
	 * @param value
	 * @return
	 */
	public Big findBig(String attribute,String branch,String value);
	
}
