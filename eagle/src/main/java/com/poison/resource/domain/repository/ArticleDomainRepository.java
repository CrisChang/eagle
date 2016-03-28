package com.poison.resource.domain.repository;

import java.util.List;
import java.util.Map;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.ArticleDAO;
import com.poison.resource.dao.ArticleDraftDAO;
import com.poison.resource.model.Article;
import com.poison.resource.model.ArticleDraft;

/**
 * 
 * 类的作用:此类的作用是封装dao层中的方法
 * 作者:闫前刚
 * 创建时间:2014-8-1上午10:51:25
 * email :1486488968@qq.com
 * version: 1.0
 */
public class ArticleDomainRepository {
	private ArticleDAO articleDAO;
	
	private ArticleDraftDAO articleDraftDAO;
	
	private UKeyWorker reskeyWork;
	
	public void setArticleDAO(ArticleDAO articleDAO) {
		this.articleDAO = articleDAO;
	}
	public void setArticleDraftDAO(ArticleDraftDAO articleDraftDAO) {
		this.articleDraftDAO = articleDraftDAO;
	}
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是新建帖子
	 * @param article
	 * @return
	 */
	public Article addArticle(Article article){
		Article pt = new Article();
		int flag = articleDAO.addArticle(article);
		pt.setFlag(flag);
		if(ResultUtils.SUCCESS==flag){
			pt = articleDAO.queryArticleById(article.getId());
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子
	 * @param article
	 * @return
	 */
	public Article updateByIdArticle(Article article){
		Article pt = new Article();
		int flag = articleDAO.updateByIdArticle(article);
		pt.setFlag(flag);
		if(ResultUtils.SUCCESS==flag){
			pt = articleDAO.queryArticleById(article.getId());
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除帖子
	 * @param id
	 * @return
	 */
	public Article deleteByIdArticle(long id){
		Article article = new Article();
		article.setId(id);
		Article pt = articleDAO.queryArticleById(article.getId());
		int flag = pt.getFlag();
		if(ResultUtils.SUCCESS==flag||0 == flag){
			flag = articleDAO.deleteByIdArticle(id);
			pt.setFlag(flag);
			pt.setIsDel(1);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询帖子相关信息
	 * @param article
	 * @return
	 */
	public List<Article> queryUidArticle(Article article){
		return articleDAO.queryUidArticle(article);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改帖子内容
	 * @param article
	 * @return
	 */
	public Article updateByIdContent(Article article){
		int flag = articleDAO.updateByIdContent(article);
		Article pt = new Article();
		if(ResultUtils.SUCCESS==flag){
			pt = articleDAO.queryArticleById(article.getId());
			pt.setFlag(flag);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所需要的帖子
	 * @param article
	 * @return
	 */
	public Article queryArticleById(long id){
		return articleDAO.queryArticleById(id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据类别查询
	 * @param article
	 * @return
	 */
	public List<Article> queryByTypeArticle(String type,Long id){
		return articleDAO.queryByTypeArticle(type,id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户的类别列表
	 * @param type 类别
	 * @param uid  用户id
	 * @return
	 */
	public List<Article> queryByTypeUid(String type, long uid,long start,int pageSize){
		return articleDAO.queryByTypeUid(type, uid,start,pageSize);
	}
	
	/**
	 * 
	 * <p>Title: findArticleListById</p> 
	 * <p>Description: 根据ID查询</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:24:37
	 * @param id
	 * @return
	 */
	public List<Article> findArticleListById(long id){
		return articleDAO.findArticleListById(id);
	}
	
	/**
	 * 
	 * <p>Title: findArticleListByUsersId</p> 
	 * <p>Description: 根据ID查询帖子列表</p> 
	 * @author :changjiang
	 * date 2014-8-2 下午4:58:56
	 * @param userIdList
	 * @param resId
	 * @return
	 */
	public List<Article> findArticleListByUsersId(List<Long> userIdList,Long resId,String type){
		return articleDAO.findArticleListByUsersId(userIdList, resId,type);
	}
	
	/**
	 * 
	 * <p>Title: updateArticleReadingCount</p> 
	 * <p>Description: 更新阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:22:24
	 * @param id
	 * @return
	 */
	public int updateArticleReadingCount(Article article){
		return articleDAO.updateArticleReadingCount(article);
	}
	
	/**
	 * 
	 * <p>Title: findArticleCount</p> 
	 * <p>Description: 查找帖子总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:24:16
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findArticleCount(long userId){
		return articleDAO.findArticleCount(userId,null);
	}
	/**
	 * 
	 * @return
	 */
	public Map<String, Object> findArticleCount(long userId,String type){
		return articleDAO.findArticleCount(userId,type);
	}
	/**
	 * 根据标题模糊查询，包含文章开始时间和结束时间
	 */
	public List<Article> searchArticleByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize){
		return articleDAO.searchArticleByTitle(type, uid, title, starttime, endtime, start, pageSize);
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	public Map<String, Object> findArticleCountByLike(String type, long uid,String title,Long starttime,Long endtime){
		return articleDAO.findArticleCountByLike(type, uid, title, starttime, endtime);
	}
	
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
	public Article publicArticle(long did,long uid){
		ArticleDraft articleDraft = articleDraftDAO.queryArticleDraftById(did);
		int flag = articleDraft.getFlag();
		if(flag==ResultUtils.SUCCESS){
			if(articleDraft.getAid()>0){
				//存在原文章，进行更新
				Article  article = new Article();
				article.setId(articleDraft.getAid());
				article.setUid(uid);
				article.setEndDate(System.currentTimeMillis());
				article.setName(articleDraft.getName());
				article.setCover(articleDraft.getCover());
				article.setContent(articleDraft.getContent());
				article.setSummary(articleDraft.getSummary());
				article.setAtype(articleDraft.getAtype());
				flag = articleDAO.updateByIdArticle(article);
				if(flag==ResultUtils.SUCCESS){
					//删除草稿
					articleDraftDAO.deleteByIdArticleDraft(did);
					return articleDAO.queryArticleById(articleDraft.getAid());
				}else{
					//更新正式文章失败
					article = new Article();
					article.setFlag(flag);
					return article;
				}
			}else{
				//不存在原文章，进行创建
				Article  article = new Article();
				article.setId(reskeyWork.getId());
				article.setUid(uid);
				article.setEndDate(System.currentTimeMillis());
				article.setName(articleDraft.getName());
				article.setCover(articleDraft.getCover());
				article.setContent(articleDraft.getContent());
				article.setSummary(articleDraft.getSummary());
				article.setAtype(articleDraft.getAtype());
				article.setBeginDate(System.currentTimeMillis());
				article.setReadingCount(0);
				article.setFalsereading(0);
				article.setType(articleDraft.getType());
				flag = articleDAO.addArticle(article);
				if(flag==ResultUtils.SUCCESS){
					//删除草稿
					articleDraftDAO.deleteByIdArticleDraft(did);
					return articleDAO.queryArticleById(article.getId());
				}else{
					//保存失败
					article = new Article();
					article.setFlag(flag);
					return article;
				}
			}
		}else{
			Article article = new Article();
			article.setFlag(flag);
			return article;
		}
	}
	
	/**
	 * 根据用户id查询一个时间段的文章id和时间信息
	 */
	public List<Article> findUserArticleTime(Long userId,Long starttime,Long endtime){
		return articleDAO.findUserArticleTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文章信息列表
	 */
	public List<Article> findUserArticlesByTime(Long userId,Long starttime,Long endtime){
		return articleDAO.findUserArticlesByTime(userId, starttime, endtime);
	}
	
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是保存草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft addArticleDraft(ArticleDraft articleDraft){
		ArticleDraft pt = new ArticleDraft();
		int flag = articleDraftDAO.addArticleDraft(articleDraft);
		pt.setFlag(flag);
		if(ResultUtils.SUCCESS==flag){
			pt = articleDraftDAO.queryArticleDraftById(articleDraft.getId());
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft updateByIdArticleDraft(ArticleDraft articleDraft){
		ArticleDraft pt = new ArticleDraft();
		int flag = articleDraftDAO.updateByIdArticleDraft(articleDraft);
		pt.setFlag(flag);
		if(ResultUtils.SUCCESS==flag){
			pt = articleDraftDAO.queryArticleDraftById(articleDraft.getId());
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除草稿
	 * @param id
	 * @return
	 */
	public ArticleDraft deleteByIdArticleDraft(long id){
		ArticleDraft articleDraft = new ArticleDraft();
		articleDraft.setId(id);
		ArticleDraft pt = articleDraftDAO.queryArticleDraftById(articleDraft.getId());
		int flag = pt.getFlag();
		if(ResultUtils.SUCCESS==flag||0 == flag){
			flag = articleDraftDAO.deleteByIdArticleDraft(id);
			pt.setFlag(flag);
			pt.setIsDel(1);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据所属文章id删除草稿
	 * @param id
	 * @return
	 */
	public ArticleDraft deleteArticleDraftByAid(long aid){
		ArticleDraft pt = articleDraftDAO.queryArticleDraftById(aid);
		int flag = pt.getFlag();
		if(ResultUtils.SUCCESS==flag||0 == flag){
			flag = articleDraftDAO.deleteByIdArticleDraft(aid);
			pt.setFlag(flag);
			pt.setIsDel(1);
		}
		return pt;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询草稿相关信息
	 * @param article
	 * @return
	 */
	public List<ArticleDraft> queryUidArticleDraft(ArticleDraft articleDraft){
		return articleDraftDAO.queryUidArticleDraft(articleDraft);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所需要的草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft queryArticleDraftById(long id){
		return articleDraftDAO.queryArticleDraftById(id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据所属文章id查询草稿
	 * @param article
	 * @return
	 */
	public ArticleDraft queryArticleDraftByAid(long aid){
		return articleDraftDAO.queryArticleDraftByAid(aid);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据类别查询草稿
	 * @param article
	 * @return
	 */
	public List<ArticleDraft> queryByTypeArticleDraft(String type,Long id){
		return articleDraftDAO.queryByTypeArticleDraft(type,id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户的草稿列表
	 * @param type 类别
	 * @param uid  用户id
	 * @return
	 */
	public List<ArticleDraft> queryArticleDraftByTypeUid(String type, long uid,long start,int pageSize){
		return articleDraftDAO.queryByTypeUid(type, uid,start,pageSize);
	}
	
	/**
	 * 
	 * <p>Title: findArticleListById</p> 
	 * <p>Description: 根据ID查询草稿</p> 
	 * @author :changjiang
	 * date 2014-8-2 上午2:24:37
	 * @param id
	 * @return
	 */
	public List<ArticleDraft> findArticleDraftListById(long id){
		return articleDraftDAO.findArticleDraftListById(id);
	}
	
	/**
	 * 
	 * <p>Title: updateArticleReadingCount</p> 
	 * <p>Description: 更新草稿阅读量</p> 
	 * @author :changjiang
	 * date 2014-11-12 下午7:22:24
	 * @param id
	 * @return
	 */
	public int updateArticleDraftReadingCount(long id){
		return articleDraftDAO.updateArticleDraftReadingCount(id);
	}
	
	/**
	 * 
	 * <p>Title: findArticleCount</p> 
	 * <p>Description: 查找草稿总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:24:16
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findArticleDraftCount(long userId){
		return articleDraftDAO.findArticleDraftCount(userId);
	}
	
	/**
	 * 根据标题模糊查询，包含草稿开始时间和结束时间
	 */
	public List<ArticleDraft> searchArticleDraftByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize){
		return articleDraftDAO.searchArticleDraftByTitle(type, uid, title, starttime, endtime, start, pageSize);
	}
	/**
	 * 根据模糊查询条件查询草稿的数量
	 */
	public Map<String, Object> findArticleDraftCountByLike(String type, long uid,String title,Long starttime,Long endtime){
		return articleDraftDAO.findArticleDraftCountByLike(type, uid, title, starttime, endtime);
	}
	
	/**
	 * 
	 * <p>Title: findArticleListByUserId</p> 
	 * <p>Description: 根据用户id查询新长文章列表</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午4:24:46
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public List<Article> findArticleListByUserId(Long userId, Long resId,
			String type){
		return articleDAO.findArticleListByUserId(userId, resId, type);
	}
	/**
	 * 根据文章id集合查询文章集合
	 */
	public List<Article> findArticlesByIds(List<Long> aids){
		return articleDAO.findArticlesByIds(aids);
	}
}
