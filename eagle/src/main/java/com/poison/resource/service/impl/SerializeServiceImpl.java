package com.poison.resource.service.impl;

import java.util.List;

import com.poison.resource.domain.repository.SerializeDomainRepository;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.ChapterSummary;
import com.poison.resource.model.Serialize;
import com.poison.resource.service.SerializeService;

public class SerializeServiceImpl implements SerializeService{
	private SerializeDomainRepository serializeDomainRepository; 
	public void setSerializeDomainRepository(
			SerializeDomainRepository serializeDomainRepository) {
		this.serializeDomainRepository = serializeDomainRepository;
	}
	/**
	 * 同时添加连载、章节
	 */
	@Override
	public int addSerializeChapter(Serialize ser, Chapter ch) {
		return serializeDomainRepository.addSerializeChapter(ser, ch);
	}
	/**
	 * 续写章节内容
	 */
	@Override
	public int addOldChapter(Chapter ch) {
		return serializeDomainRepository.addOldChapter(ch);
	}
	/**
	 * 修改连载名称
	 */
	@Override
	public int updateSerialize(Serialize ser,long id) {
		return serializeDomainRepository.updateSerialize(ser,id);
	}
	/**
	 * 修改连载类型
	 */
	@Override
	public int updateSerializeType(Serialize ser,long id) {
		return serializeDomainRepository.updateSerializeType(ser,id);
	}
	/**
	 * 修改章节名称
	 */
	@Override
	public int updateChapter(Chapter ch, long id) {
		return serializeDomainRepository.updateChapter(ch, id);
	}
	/**
	 * 修改章节内容
	 */
	@Override
	public int updateChapterContent(Chapter ch, long id) {
		return serializeDomainRepository.updateChapterContent(ch, id);
	}
	/**
	 * 修改连载简介
	 */
	@Override
	public int updateSerializeIntroduce(Serialize ser, long id) {
		return serializeDomainRepository.updateSerializeIntroduce(ser, id);
	}
	/**
	 * 根据id删除章节
	 */
	@Override
	public int deleteChapter(Chapter ch, long id) {
		return serializeDomainRepository.deleteChapter(ch, id);
	}
	/**
	 * 根据id删除连载
	 */
	@Override
	public int deleteSerialize(Serialize ser, long id) {
		return serializeDomainRepository.deleteSerialize(ser, id);
	}
	/**
	 * 根据id查找连载信息
	 */
	@Override
	public List<Serialize> findAllSerialize(long id) {
		return serializeDomainRepository.findAllSerialize(id);
	}
	/**
	 * 根据类别查找连载信息
	 */
	@Override
	public List<Serialize> findSerialize(String type) {
		return serializeDomainRepository.findSerialize(type);
	}
	/**
	 * 根据父id查询章节信息
	 */
	@Override
	public List<Chapter> findChapter(long parentId) {
		return serializeDomainRepository.findChapter(parentId);
	}

	@Override
	public List<ChapterSummary> findChapterSummary(long parentId) {
		return serializeDomainRepository.findChapterSummary(parentId);
	}

	/**
	 * 根据id查询章节目录、内容
	 */
	@Override
	public Chapter findChapterContent(long id) {
		return serializeDomainRepository.findChapterContent(id);
	}
	/**
	 * 此方法的作用是创建连载（分别）
	 */
	@Override
	public Serialize addSerialize(Serialize ser) {
		return serializeDomainRepository.addSerialize(ser);
	}
	/**
	 * 查询所有的连载信息
	 *//*
	@Override
	public List<Chapter> findAllChapter() {
		return serializeDomainRepository.findAllChapter();
	}*/
	/**
	 * 查询所有的章节信息
	 */
	@Override
	public List<Serialize> findAllSerialize() {
		return serializeDomainRepository.findAllSerialize();
	}
	/**
	 * 查询当前用户连载信息
	 */
	@Override
	public List<Serialize> findSerializeByUser(long uid) {
		return serializeDomainRepository.findSerializeByUser(uid);
	}
	/**
	 * 查询当前用户目录
	 */
	@Override
	public List<Serialize> findSerializeUserId(List<Long> userList, Long uid) {
		return serializeDomainRepository.findSerializeUserId(userList, uid);
	}
	/**
	 * 分页查询连载
	 */
	public List<Serialize> findSerializePageByUserId(Long uid, int beginNo,
			int pageSize) {
		return serializeDomainRepository.findSerializePageByUserId(uid, beginNo, pageSize);
	}

	// 根据用户id查询该用户的连载数量
	public int findSerializeTotalCountByUserId(Long uid) {
		return serializeDomainRepository.findSerializeTotalCountByUserId(uid);
	}

	//根据连载id查询该连载详细信息
	public Serialize seleceSerializeByid(Long id) {
		return serializeDomainRepository.seleceSerializeByid(id);
	}

	//按章节查询章节
	public Chapter findChapterToPage(Long uid, Long serId, int beginNo) {
		return serializeDomainRepository.findChapterToPage(uid, serId, beginNo);
	}
	
	//根据连载id查询该用户的章节数量
	public int findChapterTotalCountByUserId(Long serId) {
		return serializeDomainRepository.findChapterTotalCountByUserId(serId);
	}
	
	/**
	 * 更新连载URL
	 */
	@Override
	public Serialize updateSerializeUrl(Serialize ser) {
		return serializeDomainRepository.updateSerializeUrl(ser);
	}
}
