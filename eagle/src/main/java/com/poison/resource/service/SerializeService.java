package com.poison.resource.service;

import java.util.List;

import com.poison.resource.model.Chapter;
import com.poison.resource.model.ChapterSummary;
import com.poison.resource.model.Serialize;

/**
 * 
 * 类的作用:此方法的作用是完成业务相关的操作
 * 作者:闫前刚
 * 创建时间:2014-7-27下午7:14:40
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface SerializeService {
	/**
	 * 
	 * 方法的描述 :此方法的作用创建连载、章节
	 * @param ser
	 * @param ch
	 * @return
	 */
	public int addSerializeChapter(Serialize ser,Chapter ch);
	/**
	 * 
	 * 方法的描述 :此方法的作用是创建连载（分别）
	 * @param ser
	 * @return
	 */
	public Serialize addSerialize(Serialize ser);
	/**
	 * 
	 * 方法的描述 :此方法的作用是续写
	 * @param ch
	 * @return
	 */
	public int addOldChapter(Chapter ch);
	/**
	 * 
	 * 方法的描述 :修改连载名称
	 * @param ser
	 * @return
	 */
	public int updateSerialize(Serialize ser,long id);
	
	/**
	 * 修改连载类型
	 */
	public int updateSerializeType(Serialize ser,long id);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改章节名称
	 * @param ch
	 * @param id 根据id
	 * @return
	 */
	public int updateChapter(Chapter ch,long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用的是修改章节内容
	 * @param ch
	 * @param id根据修改
	 * @return
	 */
	public int updateChapterContent(Chapter ch,long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是连载简介
	 * @param ser
	 * @param id根据id修改
	 * @return
	 */
	public int updateSerializeIntroduce(Serialize ser,long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除章节
	 * @param ch
	 * @param id根据id
	 * @return
	 */
	public int deleteChapter(Chapter ch,long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除连载
	 * @param ser
	 * @param id根据id
	 * @return
	 */
	public int deleteSerialize(Serialize ser,long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询连载信息
	 * @param ser
	 * @return
	 */
	public List<Serialize> findAllSerialize(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询类别
	 * @param type
	 * @return
	 */
	public List<Serialize> findSerialize(String type);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查选章节目录
	 * @param parentId
	 * @return
	 */
	public List<ChapterSummary> findChapterSummary(long parentId);

	public List<Chapter> findChapter(long parentId);
	/**
	 * 
	 * 方法的描述 :此方法的作用和查询内容、章节目录
	 * @param id 主键id
	 * @return
	 */
	public Chapter findChapterContent(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所有的连载信息
	 * @return
	 */
	//public List<Chapter> findAllChapter();
	/**
	 * 
	 * 方法的描述 :查询所有的章节信息
	 * @return
	 */
	public List<Serialize>findAllSerialize();
	/**
	 * 
	 * 方法的描述 :查询当前用户名的连载信息
	 * @param uid
	 * @return
	 */
	public List<Serialize> findSerializeByUser(long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户列表
	 * @param userList
	 * @param uid 用户id
	 * @return
	 */
	public List<Serialize> findSerializeUserId(List<Long> userList, Long uid);
	
	/**
	 * 分页查询连载
	 * @param uid 用户id
	 * @param beginNo 开始
	 * @param pageSize 每页显示数
	 * @return
	 */
	public List<Serialize> findSerializePageByUserId(Long uid, int beginNo,
			int pageSize);
	
	/**
	 * 根据用户id查询该用户的连载数量
	 * @param uid 用户id
	 * @return 连载数量
	 */
	public int findSerializeTotalCountByUserId(Long uid);
	
	/**
	 * 根据连载id查询该连载详细信息
	 * @param id 连载id
	 * @return 连载信息
	 */
	public Serialize seleceSerializeByid(Long id);
	
	/**
	 * 按章节查询章节
	 * @param uid 用户id
	 * @param beginNo 开始
	 * @return 章节信息
	 */
	public Chapter findChapterToPage(Long uid, Long serId, int beginNo);
	
	/**
	 * 根据连载id查询该用户的章节数量
	 * @param serId 连载id
	 * @return 章节数量
	 */ 
	public int findChapterTotalCountByUserId(Long serId);
	
	/**
	 * 
	 * <p>Title: updateSerializeUrl</p> 
	 * <p>Description: 更新连载URL</p> 
	 * @author :changjiang
	 * date 2014-10-16 下午3:01:51
	 * @param ser
	 * @return
	 */
	public Serialize updateSerializeUrl(Serialize ser);
}
