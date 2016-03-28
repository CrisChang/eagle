package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.domain.repository.ArticleDomainRepository;
import com.poison.resource.model.Article;
import com.poison.resource.model.ArticleDraft;
import com.poison.resource.service.ArticleService;

public class ArticleServiceImpl implements ArticleService {
	private ArticleDomainRepository articleDomainRepository;

	public void setArticleDomainRepository(
			ArticleDomainRepository articleDomainRepository) {
		this.articleDomainRepository = articleDomainRepository;
	}

	@Override
	public Article addArticle(Article article) {
		return articleDomainRepository.addArticle(article);
	}

	@Override
	public Article updateByIdArticle(Article article) {
		return articleDomainRepository.updateByIdArticle(article);
	}

	@Override
	public Article deleteByIdArticle(long id) {
		return articleDomainRepository.deleteByIdArticle(id);
	}

	@Override
	public List<Article> queryUidArticle(Article article) {
		return articleDomainRepository.queryUidArticle(article);
	}

	@Override
	public Article updateByIdContent(Article article) {
		return articleDomainRepository.updateByIdContent(article);
	}

	@Override
	public Article queryArticleById(long id) {
		return articleDomainRepository.queryArticleById(id);
	}

	@Override
	public List<Article> queryByTypeArticle(String type,Long id) {
		return articleDomainRepository.queryByTypeArticle(type,id);
	}

	@Override
	public List<Article> queryByTypeUid(String type, long uid,long start,int pageSize) {
		return articleDomainRepository.queryByTypeUid(type, uid,start,pageSize);
	}

	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Article> findArticleListById(long id) {
		return articleDomainRepository.findArticleListById(id);
	}

	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Article> findArticleListByUsersId(List<Long> userIdList, Long resId,String type) {
		return articleDomainRepository.findArticleListByUsersId(userIdList, resId,type);
	}

	/**
	 * 更新阅读量
	 */
	@Override
	public int updateArticleReadingCount(Article article) {
		return articleDomainRepository.updateArticleReadingCount(article);
	}

	/**
	 * 查询帖子总数
	 */
	@Override
	public Map<String, Object> findArticleCount(long userId) {
		return articleDomainRepository.findArticleCount(userId);
	}
	/**
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> findArticleCount(long userId,String type){
		return articleDomainRepository.findArticleCount(userId, type);
	}
	/**
	 * 根据标题模糊查询，包含文章开始时间和结束时间
	 */
	public List<Article> searchArticleByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize){
		return articleDomainRepository.searchArticleByTitle(type, uid, title, starttime, endtime, start, pageSize);
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	public Map<String, Object> findArticleCountByLike(String type, long uid,String title,Long starttime,Long endtime){
		return articleDomainRepository.findArticleCountByLike(type, uid, title, starttime, endtime);
	}

	@Override
	public Article publicArticle(long did, long uid) {
		return articleDomainRepository.publicArticle(did, uid);
	}
	
	/**
	 * 根据用户id查询一个时间段的文章id和时间信息
	 */
	@Override
	public List<Article> findUserArticleTime(Long userId,Long starttime,Long endtime){
		return articleDomainRepository.findUserArticleTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文章信息列表
	 */
	@Override
	public List<Article> findUserArticlesByTime(Long userId,Long starttime,Long endtime){
		return articleDomainRepository.findUserArticlesByTime(userId, starttime, endtime);
	}

	@Override
	public ArticleDraft addArticleDraft(ArticleDraft articleDraft) {
		return articleDomainRepository.addArticleDraft(articleDraft);
	}

	@Override
	public ArticleDraft updateByIdArticleDraft(ArticleDraft articleDraft) {
		return articleDomainRepository.updateByIdArticleDraft(articleDraft);
	}

	@Override
	public ArticleDraft deleteByIdArticleDraft(long id) {
		return articleDomainRepository.deleteByIdArticleDraft(id);
	}

	@Override
	public List<ArticleDraft> queryUidArticleDraft(ArticleDraft articleDraft) {
		return articleDomainRepository.queryUidArticleDraft(articleDraft);
	}

	@Override
	public ArticleDraft queryArticleDraftById(long id) {
		return articleDomainRepository.queryArticleDraftById(id);
	}

	@Override
	public List<ArticleDraft> queryByTypeArticleDraft(String type, Long id) {
		return articleDomainRepository.queryByTypeArticleDraft(type, id);
	}

	@Override
	public List<ArticleDraft> queryArticleDraftByTypeUid(String type, long uid,
			long start, int pageSize) {
		return articleDomainRepository.queryArticleDraftByTypeUid(type, uid, start, pageSize);
	}

	@Override
	public List<ArticleDraft> findArticleDraftListById(long id) {
		return articleDomainRepository.findArticleDraftListById(id);
	}

	@Override
	public int updateArticleDraftReadingCount(long id) {
		return articleDomainRepository.updateArticleDraftReadingCount(id);
	}

	@Override
	public Map<String, Object> findArticleDraftCount(long userId) {
		return articleDomainRepository.findArticleDraftCount(userId);
	}

	@Override
	public List<ArticleDraft> searchArticleDraftByTitle(String type, long uid,
			String title, Long starttime, Long endtime, long start, int pageSize) {
		return articleDomainRepository.searchArticleDraftByTitle(type, uid, title, starttime, endtime, start, pageSize);
	}

	@Override
	public Map<String, Object> findArticleDraftCountByLike(String type,
			long uid, String title, Long starttime, Long endtime) {
		return articleDomainRepository.findArticleCountByLike(type, uid, title, starttime, endtime);
	}

	@Override
	public ArticleDraft deleteArticleDraftByAid(long aid) {
		return articleDomainRepository.deleteArticleDraftByAid(aid);
	}

	@Override
	public ArticleDraft queryArticleDraftByAid(long aid) {
		return articleDomainRepository.queryArticleDraftByAid(aid);
	}

	/**
	 * 根据用户id查询新长文章详情
	 */
	@Override
	public List<Article> findArticleListByUserId(Long userId, Long resId,
			String type) {
		return articleDomainRepository.findArticleListByUserId(userId, resId, type);
	}
	/**
	 * 根据文章id集合查询文章集合
	 */
	@Override
	public List<Article> findArticlesByIds(List<Long> aids){
		return articleDomainRepository.findArticlesByIds(aids);
	}
}
