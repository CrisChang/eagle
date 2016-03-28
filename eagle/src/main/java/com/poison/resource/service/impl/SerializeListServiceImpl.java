package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.dao.SerializeListDAO;
import com.poison.resource.domain.repository.SerializeListDomainRepository;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;
import com.poison.resource.service.SerializeListService;

public class SerializeListServiceImpl implements SerializeListService{

	private SerializeListDomainRepository serializeListDomainRepository;

	public void setSerializeListDomainRepository(
			SerializeListDomainRepository serializeListDomainRepository) {
		this.serializeListDomainRepository = serializeListDomainRepository;
	}

	/**
	 * 新增一个连载清单
	 */
	@Override
	public SerializeList addSerializeList(SerializeList serializeList) {
		return serializeListDomainRepository.addSerializeList(serializeList);
	}

	/**
	 * 增加清单一条记录
	 */
	@Override
	public SerializeListLink addSerializeListLink(
			SerializeListLink serializeListLink) {
		return serializeListDomainRepository.addSerializeListLink(serializeListLink);
	}

	/**
	 * 根据清单id查询清单关系表
	 */
	@Override
	public List<SerializeListLink> findSerializeListLinkByListId(long id) {
		return serializeListDomainRepository.findSerializeListLinkByListId(id);
	}

	/**
	 * 更新连载列表
	 */
	@Override
	public SerializeList updateSerializeList(long id) {
		return serializeListDomainRepository.updateSerializeList(id);
	}

	/**
	 * 更新最后最后看过的章节
	 * @param serializeListLink
	 * @return
	 */
	@Override
	public SerializeListLink updateLastViewSerlizeLinkEndDate(SerializeListLink serializeListLink) {
		return serializeListDomainRepository.updateLastViewSerlizeLinkEndDate(serializeListLink);
	}

	/**
	 * 查找看过的小说列表
	 * @param userId
	 * @param endDate
	 * @return
	 */
	@Override
	public List<SerializeListLink> findViewedSerializeLinkList(long userId, Long endDate) {
		return serializeListDomainRepository.findViewedSerializeLinkList(userId, endDate);
	}

	@Override
	public SerializeListLink findSerializeListLinkByUidAndSerializeId(long userId, long serializeId) {
		return serializeListDomainRepository.findSerializeListLinkByUidAndSerializeId(userId, serializeId);
	}

	/**
	 * 删除看过的小说
	 * @param userId
	 * @param serializeId
	 * @return
	 */
	@Override
	public int delSerializeListLink(long userId, long serializeId) {
		return serializeListDomainRepository.delSerializeListLink(userId, serializeId);
	}
}
