package com.poison.resource.service;

import java.util.List;

import com.poison.resource.domain.repository.SerializeListDomainRepository;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;

public interface SerializeListService {

	
	
	/**
	 * 
	 * <p>Title: addSerializeList</p> 
	 * <p>Description: 插入一个连载清单</p> 
	 * @author :changjiang
	 * date 2014-9-13 下午4:05:39
	 * @param serializeList
	 * @return
	 */
	public SerializeList addSerializeList(SerializeList serializeList);
	
	/**
	 * 
	 * <p>Title: addSerializeListLink</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2014-9-14 下午12:12:46
	 * @param serializeListLink
	 * @return
	 */
	public SerializeListLink addSerializeListLink(SerializeListLink serializeListLink);
	
	/**
	 * 
	 * <p>Title: findSerializeListLinkByListId</p> 
	 * <p>Description: 根据清单id查询清单关系表</p> 
	 * @author :changjiang
	 * date 2014-9-19 上午11:27:57
	 * @param id
	 * @return
	 */
	public List<SerializeListLink> findSerializeListLinkByListId(long id);
	
	/**
	 * 
	 * <p>Title: updateSerializeList</p> 
	 * <p>Description: 更新连载列表</p> 
	 * @author :changjiang
	 * date 2014-9-19 下午3:01:31
	 * @param serializeList
	 * @return
	 */
	public SerializeList updateSerializeList(long id);

	/**
	 * 更新最后看过的章节时间
	 * @param serializeListLink
	 * @return
	 */
	public SerializeListLink updateLastViewSerlizeLinkEndDate(SerializeListLink serializeListLink);

	/**
	 * 查询用户看过的小说列表
	 * @param userId
	 * @param endDate
	 * @return
	 */
	public List<SerializeListLink> findViewedSerializeLinkList(long userId, Long endDate);

	/**
	 * 根据用户id和小说id
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	public SerializeListLink findSerializeListLinkByUidAndSerializeId(long userId, long serializeId);

	/**
	 * 删除看过的小说
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	public int delSerializeListLink(long userId,long serializeId);
}
