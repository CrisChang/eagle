package com.poison.resource.client;

import java.util.List;

import com.poison.resource.model.Chapter;
import com.poison.resource.model.ChapterSummary;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.Serialize;

/**
 * 
 * 类的作用:此方法的作用是完成创建连载、章节
 * 作者:闫前刚
 * 创建时间:2014-7-27下午7:22:16
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface SerializeFacade {
	/**
	 * 
	 * 方法的描述 :此方法的作用创建连载、章节
	 * @param uid 用户名
	 * @param SerializeName 连载名称
	 * @param introduce 核心内容介绍
	 * @param content   章节内容简介
	 * @param chapterName 章节名称
	 * @return
	 */
	public int addSerializeChapter(long uid,String SerializeName,String introduce,String chapterName,String content,String type,String author,String url);
	/**
	 * 
	 * 方法的描述 :此方法的作用是续写
	 * @param parentId  父id
	 * @param name 章节名称
	 * @param content 内容
	 * @param uid 用户名
	 * @return
	 */
	public Chapter addOldChapter(long parentId,String chapterName,String content,long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改连载名
	 * @param id主键id
	 * @param name 连载名称
	 * @param endDate 修改时间
	 * @param uid 用户名
	 * @return
	 */
	
	public int updateSerialize(long id,String name,long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改连载类型
	 * @param id主键id
	 * @param type 连载类型
	 * @param endDate 修改时间
	 * @param uid 用户名
	 * @return
	 */
	
	public int updateSerializeType(long id, String type,  long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是创建单个连载
	 * @param type 类别
	 * @param name 名称
	 * @param author 作者
	 * @param introduce 简介
	 * @param url 图片路径
	 * @param uid 用户名
	 * @return
	 */
	public Serialize addSerialize(String type,String name,String author,String introduce,String url,long uid);
	/**
	 * 
	 * 方法的描述 :此方法、的作用是修改章节名称
	 * @param id 主键id
	 * @param name 章节名称
	 * @param endDate 跟新时间
	 * @param uid 用户名
	 * @return
	 */
	public int updateChapter(long id,String name,long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改连载简介
	 * @param id 主键id
	 * @param introduce 简介
	 * @param endDate 更新时间
	 * @param uid 用户名
	 * @return
	 */
	public int updateSerializeIntroduce(long id,String introduce,long uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改章节表的内容
	 * @param id主键id
	 * @param content 内容
	 * @param endDate 更新时间
	 * @param uid 用户名
	 * @return
	 */
	public int updateChapterContent(long id,String content,long uid);
	/**
	 * 
	 * 方法的描述 :此方法作用是实现删除章节操作
	 * @param id 根据id
	 * @param isDel 判断条件 
	 * @return
	 */
	public int deleteChapter(long id);
	/**
	 * 
	 * 方法的描述 :此方法作用是实现连载删除操作
	 * @param id
	 * @param isDel
	 * @return
	 */
	public int deleteSerialize(long id);


	/**
	 * 
	 * 方法的描述 :此方法的作用是查询连载信息（当前用户）
	 * @param ser
	 * @return
	 */
	public List<Serialize> findAllSerialize(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询类别信息（当前用户）
	 * @param ser
	 * @return
	 */
	public List<Serialize> findSerialize(String type);
	/**\
	 * 
	 * 方法的描述 :此方法的作用是查询章节目录
	 * @param ch
	 * @param id根据父id
	 * @return
	 */
	public List<ChapterSummary> findChapterSummary(long id);

	public List<Chapter> findChapter(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询章节、内容目录
	 * @param ch
	 * @param id根据主键id
	 * @return
	 */
	public Chapter findChapterContent(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所有连载信息
	 * @return
	 */
	public List<Serialize> findAllSerialize();
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所有章节信息
	 * @return
	 */
	//public List<Chapter> findAllChapter();
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据用户名查询
	 * @param uid 用户名
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
	public List<Serialize> findSerializePageByUserId(Long uid, int beginNo,	int pageSize);
	
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
	 * @param serId 
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
	 * <p>Description: 更新连载的URL</p> 
	 * @author :changjiang
	 * date 2014-10-16 下午3:03:55
	 * @param ser
	 * @return
	 */
	public Serialize updateSerializeUrl(long id,String url);
}
