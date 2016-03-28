package com.poison.resource.client.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.manager.SensitiveManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.Article;
import com.poison.resource.model.ArticleDraft;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.ArticleService;
import com.poison.resource.service.ResStatisticService;

public class ArticleFacadeImpl implements ArticleFacade {
	private UKeyWorker reskeyWork;
	private ArticleService articleService;
	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	private SensitiveManager sensitiveManager;
	
	
	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}
	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	/**
	 * 此方法的作用是新建帖子
	 */
	@Override
	public Article addArticle(String type,String name,String cover,String content,long uid,String summary,int atype) {
		Article article=new Article();
		long articleId = reskeyWork.getId();
		article.setId(articleId);
		article.setBeginDate(new Date().getTime());
		article.setEndDate(new Date().getTime());
		//content = sensitiveManager.checkSensitive(content, uid, articleId, CommentUtils.TYPE_POST);
		article.setContent(content);
		article.setName(name);
		article.setIsDel(0);
		article.setUid(uid);
		article.setType(type);
		article.setReadingCount(0);
		article.setSummary(summary);
		article.setCover(cover);
		article.setAtype(atype);
		return articleService.addArticle(article);
	}
	/**
	 * 此方法的作用是修改标题
	 */
	@Override
	public Article updateByIdArticle(long id, String name,String cover,long uid,String content,String summary,int atype) {
		Article article=new Article();
		article.setId(id);
		article.setName(name);
		article.setUid(uid);
		article.setContent(content);
		article.setEndDate(new Date().getTime());
		article.setSummary(summary);
		article.setCover(cover);
		article.setAtype(atype);
		return articleService.updateByIdArticle(article);
	}
	/**
	 * 删除帖子操作
	 */
	@Override
	public Article deleteByIdArticle(long id) {
		return articleService.deleteByIdArticle(id);
	}
	/**
	 * 根据用户id查询帖子
	 */
	@Override
	public List<Article> queryUidArticle(long uid) {
		Article article=new Article();
		article.setUid(uid);
		return articleService.queryUidArticle(article);
	}
	/**
	 * 修改帖子内容
	 */
	@Override
	public Article  updateByIdContent(long uid, String content) {
		Article article=new Article();
		article.setEndDate(new Date().getTime());
		article.setUid(uid);
		article.setContent(content);
		return articleService.updateByIdContent(article);
	}
	/**
	 * 查询帖子信息 返回实体
	 */
	@Override
	public Article queryArticleById(final long id) {
		resourceVisitClient.execute(new JedisWorker<Object>(){
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.ARTICLE_STATISTIC_MARK+id+ResStatisticConstant.ARTICLE_STATISTIC_TYPE;
				String beforeDate = jedis.hget(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE);
				//当没有数据时，置0
				if(null==beforeDate||"".equals(beforeDate)){
					beforeDate = "0";
				}
				long sysdate = System.currentTimeMillis();
				jedis.hset(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE, sysdate+"");
				long falseVisit = jedis.hincrBy(key, ResStatisticConstant.STATISTIC_FALSE_VISIT, ResRandomUtils.RandomInt());
				long visitNumber = jedis.hincrBy(key, ResStatisticConstant.RESOURCE_STATISTIC_VISIT, 1);
				//当大于等于十分钟时，更新数据库
				if(sysdate-Long.valueOf(beforeDate)>=ResStatisticConstant.STATISTIC_TIME_INTERVALS){
					ResStatistic resStatistic = new ResStatistic();
					resStatistic.setResId(id);
					resStatistic.setType(CommentUtils.TYPE_POST);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatistic.setResLinkType("");
					resStatistic.setResLinkId(0);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});
		return articleService.queryArticleById(id);
	}
	/**
	 * 此方法的作用是根据类别查询
	 */
	@Override
	public List<Article> queryByTypeArticle(String type,Long id) {
		return articleService.queryByTypeArticle(type,id);
	}
	/**
	 * 此方法的作用是查询当前用户的类别
	 */
	@Override
	public List<Article> queryByTypeUid(String type, long uid,long start,int pageSize) {
		Article article=new Article();
		article.setUid(uid);
		article.setType(type);
		return articleService.queryByTypeUid(type, uid,start,pageSize);
	}
	
	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Article> findArticleListById(long id) {
		return articleService.findArticleListById(id);
	}
	
	/**
	 * 根据ID查询帖子详情
	 */
	@Override
	public List<Article> findArticleListByUsersId(List<Long> userIdList, Long resId,String type) {
		return articleService.findArticleListByUsersId(userIdList, resId,type);
	}
	
	/**
	 * 更新阅读量
	 */
	@Override
	public Map<String,Object> updateArticleReadingCount(long id,Long readuid) {
		Map<String,Object> map = new HashMap<String,Object>(2);
		Article article = new Article();
		article.setId(id);
		//article.setEndDate(System.currentTimeMillis());
		int falsereading = ResRandomUtils.RandomIntWithChance();
		if(readuid!=null && readuid==46){
			falsereading = ResRandomUtils.BigRandomIntValue();
		}
		article.setFalsereading(falsereading);
		int flag = articleService.updateArticleReadingCount(article);
		map.put("flag", flag);
		map.put("num", falsereading);
		return map;
	}
	
	/**
	 * 查询帖子的总数
	 */
	@Override
	public Map<String, Object> findArticleCount(long userId) {
		return articleService.findArticleCount(userId);
	}
	/**
	 * 
	 */
	@Override
	public Map<String, Object> findArticleCount(long userId,String type){
		return articleService.findArticleCount(userId, type);
	}
	/**
	 * 根据标题模糊查询，包含文章开始时间和结束时间
	 */
	public List<Article> searchArticleByTitle(String type, long uid,String title,Long starttime,Long endtime,long start,int pageSize){
		return articleService.searchArticleByTitle(type, uid, title, starttime, endtime, start, pageSize);
	}
	/**
	 * 根据模糊查询条件查询帖子的数量
	 */
	public Map<String, Object> findArticleCountByLike(String type, long uid,String title,Long starttime,Long endtime){
		return articleService.findArticleCountByLike(type, uid, title, starttime, endtime);
	}
	
	
	@Override
	public Article publicArticle(long did, long uid) {
		return articleService.publicArticle(did, uid);
	}
	
	/**
	 * 根据用户id查询一个时间段的文章id和时间信息
	 */
	@Override
	public List<Article> findUserArticleTime(Long userId,Long starttime,Long endtime){
		return articleService.findUserArticleTime(userId, starttime, endtime);
	}
	/**
	 * 根据用户id查询一个时间段的文章信息列表
	 */
	@Override
	public List<Article> findUserArticlesByTime(Long userId,Long starttime,Long endtime){
		return articleService.findUserArticlesByTime(userId, starttime, endtime);
	}
	
	@Override
	public ArticleDraft addArticleDraft(String type, String name, String cover,
			String content, long uid, String summary, int atype,long aid) {
		ArticleDraft articleDraft=new ArticleDraft();
		long articleId = reskeyWork.getId();
		articleDraft.setId(articleId);
		articleDraft.setBeginDate(new Date().getTime());
		articleDraft.setEndDate(new Date().getTime());
		articleDraft.setContent(content);
		articleDraft.setName(name);
		articleDraft.setIsDel(0);
		articleDraft.setUid(uid);
		articleDraft.setType(type);
		articleDraft.setReadingCount(0);
		articleDraft.setSummary(summary);
		articleDraft.setCover(cover);
		articleDraft.setAtype(atype);
		articleDraft.setAid(aid);
		return articleService.addArticleDraft(articleDraft);
	}
	@Override
	public ArticleDraft updateByIdArticleDraft(long id, String name,
			String cover, long uid, String content, String summary, int atype) {
		ArticleDraft articleDraft=new ArticleDraft();
		articleDraft.setId(id);
		articleDraft.setName(name);
		articleDraft.setUid(uid);
		articleDraft.setContent(content);
		articleDraft.setEndDate(new Date().getTime());
		articleDraft.setSummary(summary);
		articleDraft.setCover(cover);
		articleDraft.setAtype(atype);
		return articleService.updateByIdArticleDraft(articleDraft);
	}
	@Override
	public ArticleDraft deleteByIdArticleDraft(long id) {
		return articleService.deleteByIdArticleDraft(id);
	}
	@Override
	public List<ArticleDraft> queryUidArticleDraft(long uid) {
		ArticleDraft articleDraft=new ArticleDraft();
		articleDraft.setUid(uid);
		return articleService.queryUidArticleDraft(articleDraft);
	}
	@Override
	public ArticleDraft queryArticleDraftById(long id) {
		return articleService.queryArticleDraftById(id);
	}
	@Override
	public List<ArticleDraft> queryByTypeArticleDraft(String type, Long id) {
		return articleService.queryByTypeArticleDraft(type, id);
	}
	@Override
	public List<ArticleDraft> queryArticleDraftByTypeUid(String type, long uid,
			long start, int pageSize) {
		return articleService.queryArticleDraftByTypeUid(type, uid, start, pageSize);
	}
	@Override
	public List<ArticleDraft> findArticleDraftListById(long id) {
		return articleService.findArticleDraftListById(id);
	}
	@Override
	public int updateArticleDraftReadingCount(long id) {
		return articleService.updateArticleDraftReadingCount(id);
	}
	@Override
	public Map<String, Object> findArticleDraftCount(long userId) {
		return articleService.findArticleDraftCount(userId);
	}
	@Override
	public List<ArticleDraft> searchArticleDraftByTitle(String type, long uid,
			String title, Long starttime, Long endtime, long start, int pageSize) {
		return articleService.searchArticleDraftByTitle(type, uid, title, starttime, endtime, start, pageSize);
	}
	@Override
	public Map<String, Object> findArticleDraftCountByLike(String type,
			long uid, String title, Long starttime, Long endtime) {
		return articleService.findArticleDraftCountByLike(type, uid, title, starttime, endtime);
	}
	@Override
	public ArticleDraft deleteArticleDraftByAid(long aid) {
		return articleService.deleteArticleDraftByAid(aid);
	}
	@Override
	public ArticleDraft queryArticleDraftByAid(long aid) {
		return articleService.queryArticleDraftByAid(aid);
	}
	
	/**
	 * 根据用户id查询新长文章详情
	 */
	@Override
	public List<Article> findArticleListByUserId(Long userId, Long resId,
			String type) {
		return articleService.findArticleListByUserId(userId, resId, type);
	}
	/**
	 * 根据文章id集合查询文章集合
	 */
	@Override
	public List<Article> findArticlesByIds(List<Long> aids){
		return articleService.findArticlesByIds(aids);
	}
}