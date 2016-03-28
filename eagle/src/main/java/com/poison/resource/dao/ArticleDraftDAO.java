package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.ArticleDraft;

/**
 * 
 * 类的作用:此类的作用是作用与数据库进行持久化操作
 * 作者:闫前刚
 * 创建时间:2014-8-1上午10:35:38
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface ArticleDraftDAO  {
	/**
	 * 
	 * 方法的描述 :此方法的作用和是添加帖子
	 * @param psot
	 * @return
	 */
	public int addArticleDraft(ArticleDraft psot);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子
	 * @param articleDraft
	 * @return
	 */
	public int updateByIdArticleDraft(ArticleDraft articleDraft);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id删除帖子
	 * @param id
	 * @return
	 */
	public int deleteByIdArticleDraft(long id);
	/**
	 * 根据所属文章id删除草稿
	 * @Title: deleteArticleDraftByAid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-6 下午2:33:11
	 * @param @param aid
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int deleteArticleDraftByAid(long aid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子信息
	 * @param articleDraft
	 * @return
	 */
	public List<ArticleDraft> queryUidArticleDraft(ArticleDraft articleDraft);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询草稿根据id
	 * @param articleDraft
	 * @return
	 */
	public ArticleDraft queryArticleDraftById(long id);
	/**
	 * 查询草稿根据所属文章id
	 * @Title: queryArticleDraftByAid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-6 下午2:37:34
	 * @param @param aid
	 * @param @return
	 * @return ArticleDraft
	 * @throws
	 */
	public ArticleDraft queryArticleDraftByAid(long aid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询类别
	 * @param articleDraft
	 * @return
	 */
	public List<ArticleDraft> queryByTypeArticleDraft(String type,Long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子目录
	 * @param type 类别
	 * @param uid 用户
	 * @return
	 */
	public List<ArticleDraft> queryByTypeUid(String type,long uid,long start,int pageSize);
	
	/**
	 * 
	 * <p>Title: findArticleDraftListById</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:20:27
	 * @param id
	 * @return
	 */
	public List<ArticleDraft> findArticleDraftListById(long id);
	
	/**
	 * 
	 * <p>Title: updateArticleDraftReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:18:12
	 * @return
	 */
	public int updateArticleDraftReadingCount(long id);
	
	/**
	 * 
	 * <p>Title: findArticleDraftCount</p> 
	 * <p>Description: 查询帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午7:35:46
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findArticleDraftCount(long userId);
	/**
	 * 根据标题模糊查询，包含文章开始时间和结束时间
	 */
	public List<ArticleDraft> searchArticleDraftByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize);
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	public Map<String, Object> findArticleDraftCountByLike(String type, long uid,String title,Long starttime,Long endtime);
}
