package com.poison.resource.dao;

import com.poison.resource.model.SerializeList;

public interface SerializeListDAO {

	/**
	 * 
	 * <p>Title: addSerializeList</p> 
	 * <p>Description: 添加一个连载清单</p> 
	 * @author :changjiang
	 * date 2014-9-12 下午2:13:52
	 * @param SerializeList
	 * @return
	 */
	public int addSerializeList(SerializeList SerializeList);
	
	/**
	 * 
	 * <p>Title: findSerializeListById</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-9-13 下午3:47:52
	 * @param id
	 * @return
	 */
	public SerializeList findSerializeListById(long id);
	
	/**
	 * 
	 * <p>Title: updateSerializeList</p> 
	 * <p>Description: 更新连载清单</p> 
	 * @author :changjiang
	 * date 2014-9-19 下午2:40:22
	 * @param serializeList
	 * @return
	 */
	public int updateSerializeList(SerializeList serializeList);
	
	
}
