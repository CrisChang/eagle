package com.poison.resource.client.impl;

import java.util.List;

import com.keel.utils.UKeyWorker;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;
import com.poison.resource.service.SerializeListService;

public class SerializeListFacadeImpl implements SerializeListFacade{

	private SerializeListService serializeListService;
	private UKeyWorker reskeyWork;
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setSerializeListService(SerializeListService serializeListService) {
		this.serializeListService = serializeListService;
	}

	/**
	 * 增加一个连载清单
	 */
	@Override
	public SerializeList addSerializeList(String serializeListName,
			String reason, int type,String tag, long uId, int isPublishing) {
		SerializeList serializeList = new SerializeList();
		long sysdate = System.currentTimeMillis();
		serializeList.setId(reskeyWork.getId());
		serializeList.setSerializeListName(serializeListName);
		serializeList.setReason(reason);
		serializeList.setIsDel(0);
		serializeList.setType(type);
		serializeList.setTag(tag);
		serializeList.setuId(uId);
		serializeList.setIsPublishing(isPublishing);
		serializeList.setCreateDate(sysdate);
		serializeList.setLatestRevisionDate(sysdate);
		return serializeListService.addSerializeList(serializeList);
	}

	/**
	 * 增加一个连载链接
	 */
	@Override
	public SerializeListLink addAlreadyViewSerializeListLink(long serializeListId,
			long serializeId,long chapterId,long userId) {
		SerializeListLink serializeListLink = new SerializeListLink();
		long sysdate = System.currentTimeMillis();
		serializeListLink.setId(reskeyWork.getId());
		serializeListLink.setSerializeListId(serializeListId);
		serializeListLink.setSerializeId(serializeId);
		serializeListLink.setChapterId(chapterId);
		serializeListLink.setUserId(userId);
		serializeListLink.setIsDel(0);
		serializeListLink.setCreateDate(sysdate);
		serializeListLink.setLatestRevisionDate(sysdate);
		return serializeListService.addSerializeListLink(serializeListLink);
	}

	/**
	 * 修改最后时间
	 * @param id
	 * @return
	 */
	@Override
	public SerializeListLink updateSerlizeLinkEndDate(long serializeListId,long serializeId,long chapterId,long userId) {
		SerializeListLink serializeListLink = new SerializeListLink();
		long sysdate = System.currentTimeMillis();
		serializeListLink.setId(reskeyWork.getId());
		serializeListLink.setSerializeListId(serializeListId);
		serializeListLink.setSerializeId(serializeId);
		serializeListLink.setChapterId(chapterId);
		serializeListLink.setUserId(userId);
		serializeListLink.setIsDel(0);
		serializeListLink.setCreateDate(sysdate);
		serializeListLink.setLatestRevisionDate(sysdate);
		return serializeListService.updateLastViewSerlizeLinkEndDate(serializeListLink);
	}

	/**
	 * 根据清单id查询连载关系列表
	 */
	@Override
	public List<SerializeListLink> findSerializeListLinkByListId(long id) {
		return serializeListService.findSerializeListLinkByListId(id);
	}

	/**
	 * 发布连载清单
	 */
	@Override
	public SerializeList publishSerializeList(long id) {
		return serializeListService.updateSerializeList(id);
	}

	@Override
	public List<SerializeListLink> findViewedSerializeLinkList(long userId, Long endDate) {
		return serializeListService.findViewedSerializeLinkList(userId, endDate);
	}

	@Override
	public SerializeListLink findSerializeListLinkByUidAndSerializeId(long userId, long serializeId) {
		return serializeListService.findSerializeListLinkByUidAndSerializeId(userId, serializeId);
	}

	/**
	 * 删除看过的小说
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	@Override
	public int delSerializeListLink(long userId, long serializeId) {
		return serializeListService.delSerializeListLink(userId, serializeId);
	}
}
