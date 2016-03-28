package com.poison.resource.dao;

import java.util.List;

import com.poison.resource.model.Chapter;
import com.poison.resource.model.ChapterSummary;
import com.poison.resource.model.Serialize;

/**
 * 
 * 类的作用:此类的作用是完成对数据库进行持久化操作 
 * 作者:闫前刚 
 * 创建时间:2014-7-27下午6:40:25 
 * email:1486488968@qq.com 
 * version: 1.0
 */
public interface SerializeDAO {
	/**
	 * 
	 * 方法的描述 :此方法的作用完成新建连载
	 * @param ser
	 * @return
	 */
	public int addSerialize(Serialize ser);
    /**
     * 
     * 方法的描述 :此方法的作用是完成新建章节
     * @param ch
     * @return
     */
	public int addChapter(Chapter ch);
	/**
	 * 
	 * 方法的描述 :此方法的作用是续写
	 * @param ch
	 * @return
	 */
	public int addOldChapter(Chapter ch);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改連載
	 * @param ser
	 * @return
	 */
	public int updateSerialize(Serialize ser);
	
	/**
	 * 
	 * <p>Title: updateSerializeUrl</p> 
	 * <p>Description: 更新连载的url</p> 
	 * @author :changjiang
	 * date 2014-10-16 下午2:47:56
	 * @param ser
	 * @return
	 */
	public int updateSerializeUrl(Serialize ser);
	
	/**
	 * 修改连载类型
	 */
	public int updateSerializeType(Serialize ser);
	
	/**
	 * 
	 * 方法的描述 :此方法是查询连载表中是否存在该数据
	 * @param id
	 * @return
	 */
	public int findById(long id);
	
	/**
	 * 
	 * <p>Title: findSerializeById</p> 
	 * <p>Description: 查询</p> 
	 * @author :changjiang
	 * date 2014-8-15 上午11:36:31
	 * @param id
	 * @return
	 */
	public Serialize findSerializeById(long id);
	
	/**
	 * 
	 * 方法的描述 :此方作用是修改章节名
	 * @param name 章节名称
	 * @param id  主键id
	 * @param uid 用户名
	 * @return
	 */
	public int updateChapter(Chapter ch);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询章节表中的信息
	 * @param id 根据主键id查询
	 * @return
	 */
	public int queryById(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改章节内容
	 * @param ch
	 * @return
	 */
	public int updateChapterContent(Chapter ch);
	/**
	 * 
	 * 方法的描述 :此方法的作用修改连载简介
	 * @param ser
	 * @return
	 */
	public int updateSerializeIntroduce(Serialize ser);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除章节
	 * @param ch
	 * @return
	 */
	public int deleteChapter(Chapter ch);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除连载
	 * @param ser
	 * @return
	 */
	public int deleteSerialize(Serialize ser);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询连载的目录
	 * @param id 根据 uid查询
	 * @return
	 */
	public List<Serialize> findAllSerialize(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用查询类别信息
	 * @return
	 */
	public List<Serialize> findSerialize(String type);
	/**\
	 * 
	 * 方法的描述 :此方法的作用是查询章节
	 * @param parentId 根据外键查询
	 * @return
	 */
	public List<ChapterSummary> findChapterSummary(long parentId);

	public List<Chapter> findChapter(long parentId);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询章节内容
	 * @param id 根据主键查询
	 * @return
	 */
	public Chapter queryChapterContent(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所有连载所有目录
	 * @return
	 */
	public List<Serialize> findAllSerialize();
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所有章节目录
	 * @return
	 */
	//public List<Chapter> findAllChapter();
	/**
	 * 
	 * 方法的描述 :显示当前用户连载
	 * @param uid
	 * @return
	 */
	public List<Serialize> findSerializeByUser(long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用查询连载列表
	 * @param userList
	 * @param uid 用户id
	 * @return
	 */
	public List<Serialize> findSerializeUserId(List<Long> userList,Long uid);
	
	/**
	 * 
	 * <p>Title: findSerializeIsExisted</p> 
	 * <p>Description: 查询这个连载是否存在</p> 
	 * @author :changjiang
	 * date 2014-8-22 上午11:35:13
	 * @return
	 */
	public Serialize findSerializeIsExisted(long userId,String name);
	
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
	 * 按章节查询章节
	 * @param uid 用户id
	 * @param beginNo 开始
	 * @return 章节信息
	 */
	public Chapter findChapterToPage(Long uid, Long serId, int beginNo);
	
	/**
	 * 根据连载id查询该连载的章节数量
	 * @param serId 连载id
	 * @return 章节数量
	 */ 
	public int findChapterTotalCountByUserId(Long serId); 
	
}
