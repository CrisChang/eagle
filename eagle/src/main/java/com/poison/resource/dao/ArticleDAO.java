package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Article;

/**
 * 
 * 类的作用:此类的作用是作用与数据库进行持久化操作
 * 作者:闫前刚
 * 创建时间:2014-8-1上午10:35:38
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface ArticleDAO  {
	/**
	 * 
	 * 方法的描述 :此方法的作用和是添加帖子
	 * @param psot
	 * @return
	 */
	public int addArticle(Article psot);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子
	 * @param article
	 * @return
	 */
	public int updateByIdArticle(Article article);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id删除帖子
	 * @param id
	 * @return
	 */
	public int deleteByIdArticle(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子信息
	 * @param article
	 * @return
	 */
	public List<Article> queryUidArticle(Article article);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改内容
	 * @param article
	 * @return
	 */
	public int updateByIdContent(Article article);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所用的帖子
	 * @param article
	 * @return
	 */
	public Article queryArticleById(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询类别
	 * @param article
	 * @return
	 */
	public List<Article> queryByTypeArticle(String type,Long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子目录
	 * @param type 类别
	 * @param uid 用户
	 * @return
	 */
	public List<Article> queryByTypeUid(String type,long uid,long start,int pageSize);
	
	/**
	 * 
	 * <p>Title: findArticleListById</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:20:27
	 * @param id
	 * @return
	 */
	public List<Article> findArticleListById(long id);
	
	/**
	 * 
	 * <p>Title: findArticleListByUsersId</p> 
	 * <p>Description: 根据ID查询帖子信息</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午4:27:05
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Article> findArticleListByUsersId(List<Long> userIdList,Long resId,String type);
	
	/**
	 * 
	 * <p>Title: findArticleListByUserId</p> 
	 * <p>Description: 根据用户id查询新长文章详情</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午4:16:04
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public List<Article> findArticleListByUserId(Long userId,Long resId,String type);
	
	/**
	 * 
	 * <p>Title: updateArticleReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:18:12
	 * @return
	 */
	public int updateArticleReadingCount(Article article);
	
	/**
	 * 
	 */
	public Map<String, Object> findArticleCount(long userId,String type);
	/**
	 * 根据标题模糊查询，包含文章开始时间和结束时间
	 */
	public List<Article> searchArticleByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize);
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	public Map<String, Object> findArticleCountByLike(String type, long uid,String title,Long starttime,Long endtime);
	/**
	 * 根据用户id查询一个时间段的文章id和时间信息
	 */
	public List<Article> findUserArticleTime(Long userId,Long starttime,Long endtime);
	/**
	 * 根据用户id查询一个时间段的文章信息列表
	 */
	public List<Article> findUserArticlesByTime(Long userId,Long starttime,Long endtime);
	/**
	 * 根据文章id集合查询文章集合
	 * @param aids
	 * @return
	 */
	public List<Article> findArticlesByIds(List<Long> aids);
}
