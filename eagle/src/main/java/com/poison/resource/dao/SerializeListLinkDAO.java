package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.SerializeListLink;

public interface SerializeListLinkDAO {

	/**
	 * 
	 * <p>Title: insertserializelistlink</p> 
	 * <p>Description: 插入连载清单关系</p> 
	 * @author :changjiang
	 * date 2014-9-13 下午6:15:58
	 * @param serializeListLink
	 * @return
	 */
	public int insertSerializeListLink(SerializeListLink serializeListLink);
	
	/**
	 * 
	 * <p>Title: findSerializeListLinkById</p> 
	 * <p>Description: 根据id查询连载关系</p> 
	 * @author :changjiang
	 * date 2014-9-13 下午6:28:26
	 * @param serializeListLink
	 * @return
	 */
	public SerializeListLink findSerializeListLinkById(long id);

	/**
	 * 更新连载最后的时间
	 * @param id
	 * @return
	 */
	public int updateLastViewSerlizeLinkEndDate(long id);

	/**
	 * 查询用户是否看过这个章节
	 * @param userId
	 * @param chpaterId
	 * @return
	 */
	public SerializeListLink findSerializeListLinkByUidAndChapterId(long userId,long chpaterId);

	/**
	 * 根据用户id和小说id看下一页
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	public SerializeListLink findSerializeListLinkByUidAndSerializeId(long userId,long serializeId);
	
	/**
	 * 
	 * <p>Title: findSerializeListLinkByListId</p> 
	 * <p>Description: 根据连载清单id查询关系表</p> 
	 * @author :changjiang
	 * date 2014-9-19 上午11:15:29
	 * @param id
	 * @return
	 */
	public List<SerializeListLink> findSerializeListLinkByListId(long id);

	/**
	 * 查询看过的小说列表
	 * @param userId
	 * @param endDate
	 * @return
	 */
	public List<SerializeListLink> findViewedSerializeLinkList(long userId,Long endDate);

	/**
	 * 根据uid和serializeid删除看过的小说
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	public int delSerializeListLinkByUidAndSerializeId(long userId,long serializeId);
}
