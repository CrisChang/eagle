package com.poison.resource.client.impl;

import java.util.Date;
import java.util.List;

import com.keel.utils.UKeyWorker;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.ChapterSummary;
import com.poison.resource.model.Serialize;
import com.poison.resource.service.SerializeService;

public class SerialzeFacadeImpl implements SerializeFacade {
	private SerializeService serializeService;
	private UKeyWorker reskeyWork;

	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setSerializeService(SerializeService serializeService) {
		this.serializeService = serializeService;
	}

	//private Serialize ser = new Serialize();
	//private Chapter ch = new Chapter();
	/**\
	 * 此方法的作用是创建连载、章节
	 */
	@Override
	public int addSerializeChapter(long uid, String SerializeName,
			String introduce, String chapterName, String content,String type,String author,String url) {

		Serialize ser = new Serialize();
		/*
		 * 这是关于连载相关的信息
		 */
		ser.setId(reskeyWork.getId());
		ser.setBeginDate(new Date().getTime());
		ser.setEndDate(new Date().getTime());
		ser.setIntroduce(introduce);
		ser.setIsDel(0);//0表示没被删除
		ser.setName(SerializeName);
		ser.setUrl(url);
		ser.setType(type);
		ser.setUid(uid);
		ser.setAuthor(author);
		/**
		 * 这是关于创建章节的相关信息
		 */
		Chapter ch = new Chapter();
		ch.setId(reskeyWork.getId());
		ch.setBeginDate(new Date().getTime());
		ch.setUpdateDate(new Date().getTime());
		ch.setContent(content);
		ch.setIsDel(0);
		ch.setParentId(ser.getId());
		ch.setName(chapterName);
		ch.setUid(uid);
		ch.setUpdateDate(new Date().getTime());
		return serializeService.addSerializeChapter(ser, ch);
	}
	
	/**
	 * 此方法的作用续写
	 */
	@Override
	public Chapter addOldChapter(long parentId, String chapterName, String content,
			long uid) {
		Chapter ch = new Chapter();
		long id = reskeyWork.getId();
		ch.setId(id);
		ch.setBeginDate(new Date().getTime());
		ch.setUpdateDate(new Date().getTime());
		ch.setContent(content);
		ch.setParentId(parentId);
		ch.setIsDel(0);
		ch.setName(chapterName);
		ch.setUid(uid);
		
		Serialize ser = serializeService.seleceSerializeByid(parentId);
		ser.setEndDate(new Date().getTime());
		serializeService.updateSerialize(ser, parentId);
		serializeService.addOldChapter(ch);
		
		ch = serializeService.findChapterContent(id);
		return ch;
	} 
	
	/**\
	 * 此方法的作用是修改连载名称
	 */
	@Override
	public int updateSerialize(long id, String name,  long uid) {
		Serialize ser = new Serialize();
		ser.setId(id);
		ser.setEndDate(new Date().getTime());
		ser.setName(name);
		ser.setUid(uid);
		return serializeService.updateSerialize(ser,id);
	}
	/**
	 * 此方法的作用是修改连载类型
	 */
	@Override
	public int updateSerializeType(long id, String type, long uid) {
		Serialize ser = new Serialize();
		ser.setId(id);
		ser.setEndDate(new Date().getTime());
		ser.setType(type);
		ser.setUid(uid);
		return serializeService.updateSerializeType(ser,id);
	}
	/**
	 * 此方法的作用是修改章节名称
	 */
	@Override
	public int updateChapter(long id, String name, long uid) {
		Chapter ch = new Chapter();
		ch.setId(id);
		ch.setUpdateDate(new Date().getTime());
		ch.setName(name);
		ch.setUid(uid);
		return serializeService.updateChapter(ch, id);
	}
	/**
	 * 此方法的作用是修改连载简介
	 */
	@Override
	public int updateSerializeIntroduce(long id, String introduce,
			 long uid) {
		Serialize ser = new Serialize();
		ser.setId(id);
		ser.setEndDate(new Date().getTime());
		ser.setIntroduce(introduce);
		ser.setUid(uid);
		return serializeService.updateSerializeIntroduce(ser, id);
	}
	/**
	 * 此方法的作用是修改章节内容
	 */
	@Override
	public int updateChapterContent(long id, String content, 
			long uid) {
		Chapter ch = new Chapter();
		ch.setId(id);
		ch.setUpdateDate(new Date().getTime());
		ch.setContent(content);
		ch.setUid(uid);
		return serializeService.updateChapterContent(ch, id);
	}
	/**
	 * 此方法的作用是删除章节
	 */
	@Override
	public int deleteChapter(long id) {
		Chapter ch = new Chapter();
		long sysdate = System.currentTimeMillis();
		ch.setId(id);
		ch.setUpdateDate(sysdate);
		return serializeService.deleteChapter(ch, id);
	}
	/**
	 * 此方法的作用是删除连载
	 */
	@Override
	public int deleteSerialize(long id) {
		Serialize ser = new Serialize();
		ser.setId(id);
		ser.setEndDate(System.currentTimeMillis());
		return serializeService.deleteSerialize(ser, id);
	}
	/**
	 * 此方法的作用和是查询连载相关的信息 
	 */
	@Override
	public List<Serialize> findAllSerialize(long id) {
//		ser.setUid(id);
		return serializeService.findAllSerialize(id);
	}
	/**
	 * 此方法的作用是查询类别信息（当前用户）
	 */
	@Override
	public List<Serialize> findSerialize(String type) {
		return serializeService.findSerialize(type);
	}
	/**
	 * 此方法的作用是根据外键id
	 */
	@Override
	public List<Chapter> findChapter(long id) {
		return serializeService.findChapter(id);
	}

	@Override
	public List<ChapterSummary> findChapterSummary(long id) {
		return serializeService.findChapterSummary(id);
	}

	/**
	 * 此方法的作用是根据主键id查询
	 */
	@Override
	public Chapter findChapterContent(long id) {
		return serializeService.findChapterContent(id);
	}
	/**
	 * 此方法的作用查询所有连载信息
	 */
	@Override
	public List<Serialize> findAllSerialize() {
		return serializeService.findAllSerialize();
	}
	/**
	 * 此方法的作用是查询所有章节信息
	 */
	/*@Override
	public List<Chapter> findAllChapter() {
		return serializeService.findAllChapter();
	}*/
	/**
	 * 此方法的作用是根据用户名查询
	 */
	@Override
	public List<Serialize> findSerializeByUser(long uid) {
		return serializeService.findSerializeByUser(uid);
	}
	/**
	 * 查询当前用户目录
	 */
	@Override
	public List<Serialize> findSerializeUserId(List<Long> userList, Long rId) {
		return serializeService.findSerializeUserId(userList, rId);
	}
	/**
	 * 此方法的作用是添加单个连载
	 */
	@Override
	public Serialize addSerialize(String type, String name, String author,
			String introduce, String url, long uid) {
		Serialize ser = new Serialize();
		ser.setBeginDate(new Date().getTime());
		ser.setEndDate(new Date().getTime());
		ser.setId(reskeyWork.getId());
		ser.setAuthor(author);
		ser.setType(type);
		ser.setName(name);
		ser.setUid(uid);
		ser.setUrl(url);
		ser.setIntroduce(introduce);
		ser.setIsDel(0);
		return serializeService.addSerialize(ser);
	}
	/**
	 * 分页查询连载
	 */
	public List<Serialize> findSerializePageByUserId(Long uid, int beginNo,
			int pageSize) {
		return serializeService.findSerializePageByUserId(uid, beginNo, pageSize);
	}

	//根据用户id查询该用户的连载数量
	public int findSerializeTotalCountByUserId(Long uid) {
		return serializeService.findSerializeTotalCountByUserId(uid);
	}

	//根据连载id查询该连载详细信息
	public Serialize seleceSerializeByid(Long id) {
		return serializeService.seleceSerializeByid(id);
	}

	//按章节查询章节
	public Chapter findChapterToPage(Long uid, Long serId, int beginNo) {
		return serializeService.findChapterToPage(uid,serId,beginNo);
	}

	//根据连载id查询该用户的章节数量
	public int findChapterTotalCountByUserId(Long serId) {
		return serializeService.findChapterTotalCountByUserId(serId);
	}

	/**
	 * 更新连载URL
	 */
	@Override
	public Serialize updateSerializeUrl(long id,String url) {
		Serialize serialize = new Serialize();
		long sysdata = System.currentTimeMillis();
		serialize.setId(id);
		serialize.setUrl(url);
		serialize.setEndDate(sysdata);
		return serializeService.updateSerializeUrl(serialize);
	}

}
