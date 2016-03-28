package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;

public interface SerializeListFacade {

	/**
	 * 
	 * <p>Title: addSerializeList</p> 
	 * <p>Description: 增加一个连载清单</p> 
	 * @author :changjiang
	 * date 2014-9-13 下午4:12:39
	 * @param serializeListName
	 * @param reason
	 * @param tag
	 * @param uId
	 * @param isPublishing
	 * @return
	 */
	public SerializeList addSerializeList(String serializeListName,String reason,int type,String tag,long uId,int isPublishing);
	
	/**
	 * 
	 * <p>Title: addSerializeListLink</p> 
	 * <p>Description: 增加连载</p> 
	 * @author :changjiang
	 * date 2014-9-14 下午4:55:36
	 * @param id
	 * @param serializeListId
	 * @param serializeId
	 * @param isDel
	 * @return
	 */
	public SerializeListLink addAlreadyViewSerializeListLink(long serializeListId,long serializeId,long chapterId,long userId);

	/**
	 * 修改连载最后更新时间
	 * @param id
	 * @return
	 */
	public SerializeListLink updateSerlizeLinkEndDate(long serializeListId,long serializeId,long chapterId,long userId);

	/**
	 * 
	 * <p>Title: findSerializeListLinkByListId</p> 
	 * <p>Description: 根据清单id查询清单关系列表</p> 
	 * @author :changjiang
	 * date 2014-9-19 上午11:37:50
	 * @param id
	 * @return
	 */
	public List<SerializeListLink> findSerializeListLinkByListId(long id);
	
	/**
	 * 
	 * <p>Title: publishSerializeList</p> 
	 * <p>Description: 发布连载清单</p> 
	 * @author :changjiang
	 * date 2014-9-19 下午3:05:56
	 * @param serializeList
	 * @return
	 */
	public SerializeList publishSerializeList(long id);

	/**
	 * 查找看过的小说列表
	 * @param userId
	 * @param endDate
	 * @return
	 */
	public List<SerializeListLink> findViewedSerializeLinkList(long userId, Long endDate);


	public SerializeListLink findSerializeListLinkByUidAndSerializeId(long userId, long serializeId);

	/**
	 * 删除看过的小说
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	public int delSerializeListLink(long userId,long serializeId);
}
