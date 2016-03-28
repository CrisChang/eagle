package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.Article;
import com.poison.resource.model.ArticleDraft;

/**
 * 
 * 类的作用:此类作用是处理业务相关的内容
 * 作者:闫前刚
 * 创建时间:2014-8-1上午10:57:00
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface ArticleService {
	/**
	 * 
	 * 方法的描述 :此方法的作用是新建帖子
	 * @param article
	 * @return
	 */
	public Article addArticle(Article article);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子信息
	 * @param article
	 * @return
	 */
	public Article updateByIdArticle(Article article);
	/**
	 * 
	 * 方法的描述 :删除帖子信息
	 * @param id
	 * @return
	 */
	public Article deleteByIdArticle(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子相关的内容
	 * @param article
	 * @return
	 */
	public List<Article> queryUidArticle(Article article);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子内容
	 * @param article
	 * @return
	 */
	public Article updateByIdContent(Article article);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所需帖子内容
	 * @param article
	 * @return
	 */
	public Article queryArticleById(long id);
	/**
	 * 
	 * 方法的描述 :根据用户名查询
	 * @param article
	 * @return
	 */
	public List<Article> queryByTypeArticle(String type,Long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户类别
	 * @param type 类别
	 * @param uid 用户id
	 * @return
	 */
	public List<Article> queryByTypeUid(String type, long uid,long start,int pageSize);
	
	/**
	 * 
	 * <p>Title: findDiaryListById</p> 
	 * <p>Description: 根据ID查询日记</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:26:06
	 * @return
	 */
	public List<Article> findArticleListById(long id);
	
	/**
	 * 
	 * <p>Title: findArticleListByUsersId</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午4:59:56
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Article> findArticleListByUsersId(List<Long> userIdList,Long resId,String type);
	
	/**
	 * 
	 * <p>Title: updateArticleReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:23:26
	 * @param id
	 * @return
	 */
	public int updateArticleReadingCount(Article article);
	
	/**
	 * 
	 * <p>Title: findArticleCount</p> 
	 * <p>Description: 查询帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:26:32
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findArticleCount(long userId);
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
	 * 发布草稿为正式文章
	 * @Title: publicArticle 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-5 下午4:39:40
	 * @param @param did
	 * @param @return
	 * @return Article
	 * @throws
	 */
	public Article publicArticle(long did,long uid);
	
	/**
	 * 根据用户id查询一个时间段的文章id和时间信息
	 */
	public List<Article> findUserArticleTime(Long userId,Long starttime,Long endtime);
	/**
	 * 根据用户id查询一个时间段的文章信息列表
	 */
	public List<Article> findUserArticlesByTime(Long userId,Long starttime,Long endtime);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是保存草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft addArticleDraft(ArticleDraft articleDraft);
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft updateByIdArticleDraft(ArticleDraft articleDraft);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除草稿
	 * @param id
	 * @return
	 */
	public ArticleDraft deleteByIdArticleDraft(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据所属文章id删除草稿
	 * @param id
	 * @return
	 */
	public ArticleDraft deleteArticleDraftByAid(long aid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询草稿相关信息
	 * @param article
	 * @return
	 */
	public List<ArticleDraft> queryUidArticleDraft(ArticleDraft articleDraft);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所需要的草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft queryArticleDraftById(long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据所属文章id查询草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft queryArticleDraftByAid(long aid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据类别查询草稿
	 * @param article
	 * @return
	 */
	public List<ArticleDraft> queryByTypeArticleDraft(String type,Long id);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户的草稿列表
	 * @param type 类别
	 * @param uid  用户id
	 * @return
	 */
	public List<ArticleDraft> queryArticleDraftByTypeUid(String type, long uid,long start,int pageSize);
	
	/**
	 * 
	 * <p>Title: findArticleListById</p> 
	 * <p>Description: 根据ID查询草稿</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:24:37
	 * @param id
	 * @return
	 */
	public List<ArticleDraft> findArticleDraftListById(long id);
	
	/**
	 * 
	 * <p>Title: updateArticleReadingCount</p> 
	 * <p>Description: 更新草稿阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:22:24
	 * @param id
	 * @return
	 */
	public int updateArticleDraftReadingCount(long id);
	
	/**
	 * 
	 * <p>Title: findArticleCount</p> 
	 * <p>Description: 查找草稿总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:24:16
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findArticleDraftCount(long userId);
	
	/**
	 * 根据标题模糊查询，包含草稿开始时间和结束时间
	 */
	public List<ArticleDraft> searchArticleDraftByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize);
	/**
	 * 根据模糊查询条件查询草稿的数量
	 */
	public Map<String, Object> findArticleDraftCountByLike(String type, long uid,String title,Long starttime,Long endtime);
	
	/**
	 * 
	 * <p>Title: findArticleListByUserId</p> 
	 * <p>Description: 根据用户id查询新长文章详情</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午4:26:12
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public List<Article> findArticleListByUserId(Long userId, Long resId,
			String type);
	/**
	 * 根据文章id集合查询文章集合
	 */
	public List<Article> findArticlesByIds(List<Long> aids);
}
