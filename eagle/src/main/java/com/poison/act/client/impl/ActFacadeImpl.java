package com.poison.act.client.impl;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.framework.runtime.ProductContext;
import com.keel.utils.UKeyWorker;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActAt;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActHot;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActSubscribe;
import com.poison.act.model.ActTransmit;
import com.poison.act.model.ActUseful;
import com.poison.act.model.ActWeixinComment;
import com.poison.act.model.ActWeixinUser;
import com.poison.act.service.ActService;
import com.poison.eagle.manager.SensitiveManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.ResCollectNum;
import com.poison.resource.service.BookListService;
import com.poison.resource.service.MovieListService;
import com.poison.resource.service.ResStatisticService;

public class ActFacadeImpl implements ActFacade{

	private ActService actService;
	private UKeyWorker reskeyWork;
	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	private SensitiveManager sensitiveManager;
	private MovieListService movieListService;
	private BookListService bookListService;
	
	
	public void setBookListService(BookListService bookListService) {
		this.bookListService = bookListService;
	}

	public void setMovieListService(MovieListService movieListService) {
		this.movieListService = movieListService;
	}

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

	public void setActService(ActService actService) {
		this.actService = actService;
	}

	/**
	 * 用户转发
	 */
	@Override
	public ActTransmit doActTransmit(ProductContext productContext,
			long userId,long resourceId,String type,String transmitContext) {
		ActTransmit actTransmit = new ActTransmit();
		long sysdate = System.currentTimeMillis();
		long id = reskeyWork.getId();
		actTransmit.setId(id);
		actTransmit.setUserId(userId);
		actTransmit.setResourceId(resourceId);
		//去除敏感词库
		//transmitContext = sensitiveManager.checkSensitive(transmitContext, userId, id, CommentUtils.COMMENT_ACT);
		actTransmit.setTransmitContext(transmitContext);
		actTransmit.setTransmitDate(sysdate);
		actTransmit.setType(type);
		actTransmit.setLatestRevisionDate(sysdate);
		int i =  actService.doActTransmit(productContext, actTransmit);
		return actService.findOneTransmit(id);
	}

	/**
	 * 查询用户转发列表
	 */
	@Override
	public List<ActTransmit> findTransmitList(ProductContext productContext,
			long userId) {
		return actService.findTransmitList(productContext, userId);
	}

	/**
	 * 查询转发总数
	 */
	@Override
	public int findTransmitCount(ProductContext productContext, long resourceId) {
		return actService.findTransmitCount(productContext, resourceId);
	}

	/**
	 * 查询评论总数
	 */
	@Override
	public int findCommentCount(ProductContext productContext, long resourceId) {
		return actService.findCommentCount(productContext, resourceId);
	}

	/**
	 * 查询点赞的总数
	 */
	@Override
	public int findPraiseCount(ProductContext productContext, long resourceId) {
		return actService.findPraiseCount(productContext, resourceId);
	}

	/**
	 * 根据用户ID查询转发信息
	 */
	@Override
	public List<ActTransmit> findTransmitListByUsersId(
			ProductContext productContext, List<Long> usersIdList) {
		return actService.findTransmitListByUsersId(productContext, usersIdList);
	}

	/**
	 * 删除评论信息
	 */
	@Override
	public ActComment deleteComment(ProductContext productContext,
			long commentId) {
		long sysdate = System.currentTimeMillis();
		ActComment comment = new ActComment();
		comment.setId(commentId);
		comment.setIsDelete(1);
		comment.setLatestRevisionDate(sysdate);
		return actService.deleteComment(productContext, comment);
	}

	/**
	 * 点赞
	 */
	@Override
	public ActPraise doPraise(ProductContext productContext,long userId,long resourceId,String type,long resUserId) {
		ActPraise actPraise = new ActPraise();
		long sysdate = System.currentTimeMillis();
		actPraise.setId(reskeyWork.getId());
		actPraise.setUserId(userId);
		actPraise.setResourceId(resourceId);
		actPraise.setPraiseDate(sysdate);
		actPraise.setLatestRevisionDate(sysdate);
		actPraise.setIsPraise(1);
		actPraise.setType(type);
		actPraise.setIsLow(0);
		actPraise.setResUserId(resUserId);
		return actService.doPraise(productContext, actPraise);
	}

	/**
	 * 点low
	 */
	@Override
	public ActPraise doLow(ProductContext productContext, long userId,
			long resourceId, String type) {
		ActPraise actPraise = new ActPraise();
		long sysdate = System.currentTimeMillis();
		actPraise.setId(reskeyWork.getId());
		actPraise.setUserId(userId);
		actPraise.setResourceId(resourceId);
		actPraise.setPraiseDate(sysdate);
		actPraise.setLatestRevisionDate(sysdate);
		actPraise.setIsPraise(0);
		actPraise.setType(type);
		actPraise.setIsLow(1);
		return actService.doLow(productContext, actPraise);
	}
	
	/**
	 * 取消赞
	 */
	@Override
	public ActPraise cancelPraise(ProductContext productContext,
			long userId,long resourceId,String type) {
		ActPraise actPraise = new ActPraise();
		long sysdate = System.currentTimeMillis();
		actPraise.setUserId(userId);
		actPraise.setResourceId(resourceId);
		actPraise.setPraiseDate(sysdate);
		actPraise.setLatestRevisionDate(sysdate);
		actPraise.setIsPraise(0);
		actPraise.setType(type);
		return actService.cancelPraise(productContext, actPraise);
	}

	/**
	 * 查询资源的评论列表
	 */
	@Override
	public List<ActComment> findResCommentList(ProductContext productContext,
			long resourceId,Long id) {
		return actService.findResCommentList(productContext, resourceId,id);
	}

	/**
	 * 插入一条评论信息
	 */
	@Override
	public ActComment doOneComment(ProductContext productContext, long userId,
			long resourceId, String type,String context,long commentUserId,long commentId,long resUserId) {
		long sysdate = System.currentTimeMillis();
		ActComment actComment = new ActComment();
		actComment.setId(reskeyWork.getId());
		actComment.setUserId(userId);
		actComment.setResourceId(resourceId);
		actComment.setType(type);
		actComment.setCommentDate(sysdate);
		//去除敏感词库
		//context = sensitiveManager.checkSensitive(context, userId, resourceId, CommentUtils.COMMENT_PL);
		actComment.setCommentContext(context);
		actComment.setCommentUserId(commentUserId);
		actComment.setCommentId(commentId);
		actComment.setLatestRevisionDate(sysdate);
		actComment.setResUserId(resUserId);
		if(CommentUtils.TYPE_MOVIELIST==type){//加入影单
			movieListService.addMvListCommentCount(resourceId, sysdate);
		}else if(CommentUtils.TYPE_BOOKLIST==type){//添加书单的评论数
			bookListService.addBookListCommentCount(resourceId, sysdate);
		}
		return actService.doOneComment(productContext, actComment);
	}

	/**
	 * 查询用户的收藏列表
	 */
	@Override
	public List<ActCollect> findUserCollectList(ProductContext productContext,
			long userId,String type) {
		return actService.findUserCollectList(productContext, userId,type);
	}

	/**
	 * 增加一条推荐信息
	 */
	@Override
	public ActPublish addOnePublishInfo(long userId,long resId,String type) {
		ActPublish publish = new ActPublish();
		long sysdate = System.currentTimeMillis();
		long id = reskeyWork.getId();
		publish.setId(id);
		publish.setUserId(userId);
		publish.setResourceId(resId);
		publish.setType(type);
		publish.setPublishContext("");
		publish.setPublishDate(sysdate);
		publish.setLatestRevisionDate(sysdate);
		
		List<ActPublish>  actPublish = actService.findPublishById(resId);
		if(null!=actPublish&&actPublish.size()>0){
			publish.setIsDelete(1);
			actService.updatePublishById(publish);
		}
		publish.setIsDelete(0);
		actService.addOnePublishInfo(publish);
		return actService.findOnePublish(id);
	}

	/**
	 * 查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishList(Long resId) {
		return actService.findPublishIdList(resId);
	}

	/**
	 * 查询转发的所有列表
	 */
	@Override
	public List<ActTransmit> findAllTransmitList(Long resId) {
		return actService.findAllTransmitList(resId);
	}

	/**
	 * 查询一条发布信息的详情
	 */
	@Override
	public ActPublish findOnePublish(long id) {
		return actService.findOnePublish(id);
	}

	/**
	 * 查询一条转发的详情
	 */
	@Override
	public ActTransmit findOneTransmit(long id) {
		return actService.findOneTransmit(id);
	}

	/**
	 * 根据用户ID查询该用户的发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUid(long userId, Long resId) {
		return actService.findPublishListByUid(userId, resId);
	}

	/**
	 * 查询一条点赞的信息
	 */
	@Override
	public ActPraise findActPraise(long userId,long resId) {
		ActPraise actPraise = new ActPraise();
		actPraise.setUserId(userId);
		actPraise.setResourceId(resId);
		return actService.findActPraise(actPraise);
	}

	/**
	 * 根据type查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByType(String type, Long resId) {
		return actService.findPublishListByType(type, resId);
	}

	/**
	 * 插入一条收藏信息
	 */
	@Override
	public ActCollect doCollect(long userId, final long resId,final String type) {
		if(CommentUtils.TYPE_BOOKLIST.equals(type)||CommentUtils.TYPE_MOVIELIST.equals(type)){
			resourceVisitClient.execute(new JedisWorker<Object>(){
				@Override
				public Object work(Jedis jedis) {
					String key = "";
					if(CommentUtils.TYPE_BOOKLIST.equals(type)){
						key = ResStatisticConstant.BOOKLIST_COLLECTED_MARK+resId+ResStatisticConstant.BKLIST_STATISTIC_TYPE;
					}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){
						key = ResStatisticConstant.MOVIELIST_COLLECTED_MARK+resId+ResStatisticConstant.MVLIST_STATISTIC_TYPE;
					}
					String beforeDate = jedis.hget(key, ResStatisticConstant.RESOURCE_COLLECTED_DATE);
					//当没有数据时，置0
					if(null==beforeDate||"".equals(beforeDate)){
						beforeDate = "0";
					}
					long sysdate = System.currentTimeMillis();
					jedis.hset(key, ResStatisticConstant.RESOURCE_COLLECTED_DATE, sysdate+"");
					long falseCollected = jedis.hincrBy(key, ResStatisticConstant.COLLECTED_FALSE_NUM, ResRandomUtils.RandomInt());
					long collectedNumber = jedis.hincrBy(key, ResStatisticConstant.RESOURCE_COLLECTED_NUM, 1);
					//当大于等于十分钟时，更新数据库
					if(sysdate-Long.valueOf(beforeDate)>=ResStatisticConstant.STATISTIC_TIME_INTERVALS){
						ResCollectNum resCollectNum = new ResCollectNum();
						resCollectNum.setResId(resId);
						resCollectNum.setType(type);
						resCollectNum.setFalseCollectNum(falseCollected);
						resCollectNum.setIsCollectedNum(collectedNumber);
						resCollectNum.setLatestRevisionDate(sysdate);
						resStatisticService.insertResCollectNum(resCollectNum);
					}
					return jedis.hgetAll(key);
				}
			});
		}
		ActCollect actCollect = new ActCollect();
		long sysdate = System.currentTimeMillis();
		long id = reskeyWork.getId();
		actCollect.setId(id);
		actCollect.setUserId(userId);
		actCollect.setResourceId(resId);
		actCollect.setIsCollect(1);
		actCollect.setType(type);
		actCollect.setCollectDate(sysdate);
		actCollect.setLatestRevisionDate(sysdate);
		return actService.doCollect(null, actCollect);
	}

	/**
	 * 取消收藏
	 */
	@Override
	public ActCollect cancelCollect(long userId, long resId) {
		ActCollect actCollect = new ActCollect();
		long sysdate = System.currentTimeMillis();
		actCollect.setUserId(userId);
		actCollect.setResourceId(resId);
		actCollect.setIsCollect(0);
		actCollect.setLatestRevisionDate(sysdate);
		return actService.cancelCollect(null, actCollect);
	}

	/**
	 * 查询收藏总数
	 */
	@Override
	public int findCollectCount(long resourceId) {
		return actService.findCollectCount(resourceId);
	}

	/**
	 * 插入订阅信息
	 */
	@Override
	public ActSubscribe doSubscribe(long userId, long resId, String type) {
		ActSubscribe actSubscribe = new ActSubscribe();
		long sysdate = System.currentTimeMillis();
		actSubscribe.setId(reskeyWork.getId());
		actSubscribe.setUserId(userId);
		actSubscribe.setResourceId(resId);
		actSubscribe.setType(type);
		actSubscribe.setIsSubscribe(1);
		actSubscribe.setSubscribeDate(sysdate);
		actSubscribe.setLatestRevisionDate(sysdate);
		return actService.doSubscribe(actSubscribe);
	}

	/**
	 * 取消订阅
	 */
	@Override
	public ActSubscribe cancelSubscribe(long userId, long resId) {
		ActSubscribe actSubscribe = new ActSubscribe();
		long sysdate = System.currentTimeMillis();
		actSubscribe.setUserId(userId);
		actSubscribe.setResourceId(resId);
		actSubscribe.setIsSubscribe(0);
		actSubscribe.setSubscribeDate(sysdate);
		actSubscribe.setLatestRevisionDate(sysdate);
		return actService.cancelSubscribe(actSubscribe);
	}

	/**
	 * 根据resId查询
	 */
	@Override
	public List<ActSubscribe> findSubscribeListByResId(long resId) {
		return actService.findSubscribeListByResId(resId);
	}

	/**
	 * 查询收藏信息是否存在
	 */
	@Override
	public ActCollect findCollectIsExist(long userId, long resId) {
		return actService.findCollectIsExist(userId, resId);
	}

	/**
	 * 查询订阅信息
	 */
	@Override
	public ActSubscribe findSubscribeIsExist(long userId, long resId) {
		return actService.findSubscribeIsExist(userId, resId);
	}

	/**
	 * 查询订阅列表
	 */
	@Override
	public List<ActSubscribe> findSubscribeList(long userId, String type) {
		return actService.findSubscribeList(userId, type);
	}

	/**
	 * 查询点low的总数
	 */
	@Override
	public int findLowCount(long resourceId) {
		return actService.findLowCount(resourceId);
	}

	/**
	 * 根据用户id查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUsersId(String type, Long resId,
			List<Long> usersId) {
		return actService.findPublishListByUsersId(type, resId, usersId);
	}

	/**
	 * 根据type和uid查询转发列表
	 */
	@Override
	public List<ActTransmit> findTransmitListByTypeAndUsersId(String type,
			Long resId, List<Long> usersId) {
		return actService.findTransmitListByTypeAndUsersId(type, resId, usersId);
	}

	/**
	 * 根据推荐类型查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByRecommendType(String recommendType) {
		return actService.findPublishListByRecommendType(recommendType);
	}

	/**
	 * 查询发布的总数
	 */
	@Override
	public Map<String, Object> findPublishCount(long userId) {
		return actService.findPublishCount(userId);
	}

	/**
	 * 根据uid查询转发的总数
	 */
	@Override
	public Map<String, Object> findTransmitCountByUid(long userId) {
		return actService.findTransmitCountByUid(userId);
	}


	/**
	 * 取消发布
	 */
	@Override
	public ActPublish cancelPublish(long id) {
		return actService.cancelPublish(id);
	}

	/**
	 * 根据资源id和资源type查询点赞列表
	 */
	@Override
	public List<ActPraise> findPraiseListByResIdAndType(long resId,
			 Long id) {
		return actService.findPraiseListByResIdAndType(resId, id);
	}

	/**
	 * 查找用户的评论中心
	 */
	@Override
	public List<ActComment> findUserCommentCenter(long userId, Long id) {
		return actService.findUserCommentCenter(userId, id);
	}

	/**
	 * 根据id查询评论信息
	 */
	@Override
	public ActComment findCmtById(long id) {
		return actService.findCmtById(id);
	}

	/**
	 * 根据用户id查询点赞列表
	 */
	@Override
	public List<ActPraise> findPraiseListByResUid(long resUid, Long id) {
		return actService.findPraiseListByResUid(resUid, id);
	}
	
	//以下是微信逼格相关功能=====================
	/**
	 * 保存对一个微信用户的评论
	 */
	@Override
	public int insertWeixinComment(ActWeixinComment actWeixinComment) {
		if(actWeixinComment!=null){
			long id = reskeyWork.getId();
			actWeixinComment.setId(id);
			return actService.insertWeixinComment(actWeixinComment);
		}else{
			return -1;
		}
	}

	/**
	 * 查询对一个微信用户的逼格评论信息列表
	 */
	@Override
	public List<ActWeixinComment> findWeixinComment(String sopendid) {
		return actService.findWeixinComment(sopendid);
	}

	/**
	 * 保存一个微信用户信息
	 */
	@Override
	public int insertUser(ActWeixinUser actWeixinUser) {
		if(actWeixinUser!=null){
			long id = reskeyWork.getId();
			actWeixinUser.setId(id);
			return actService.insertUser(actWeixinUser);
		}else{
			return -1;
		}
	}

	/**
	 * 根据分数查询小于该分数的人数
	 */
	@Override
	public long findCountByScore(int score) {
		return actService.findCountByScore(score);
	}

	/**
	 * 查询微信用户人数
	 */
	@Override
	public long findUserCount() {
		return actService.findUserCount();
	}

	/**
	 * 根据openid查询一个微信用户信息
	 */
	@Override
	public ActWeixinUser findUserById(String openid) {
		return actService.findUserById(openid);
	}

	/**
	 * 更新一个微信用户信息
	 */
	@Override
	public int updateUser(ActWeixinUser actWeixinUser) {
		return actService.updateUser(actWeixinUser);
	}
	
	/**
	 * 查询某个微信用户对另一个微信用户的评价是否存在
	 */
	@Override
	public int existUserComment(String openid, String sopenid, String comment) {
		return actService.existUserComment(openid, sopenid, comment);
	}

	/**
	 * 查询用户的收藏总数
	 */
	@Override
	public Map<String, Object> findUserCollectCount(long userId) {
		return actService.findUserCollectCount(userId);
	}

	/**
	 * 查询用户的收藏列表
	 */
	@Override
	public List<ActCollect> findUserCollectedList(long userId, Long resId) {
		return actService.findUserCollectedList(userId, resId);
	}

	@Override
	public ActAt insertintoActAt(long userid, long resourceid, long resUserid,
			String type, long atUserid,long resid,String restype) {
		ActAt actAt = new ActAt();
		long id = reskeyWork.getId();
		actAt.setId(id);
		actAt.setAtUserid(atUserid);
		actAt.setCreateDate(System.currentTimeMillis());
		actAt.setIsDelete(0);
		actAt.setLatestRevisionDate(System.currentTimeMillis());
		actAt.setResourceid(resourceid);
		actAt.setResUserid(resUserid);
		actAt.setType(type);
		actAt.setUserid(userid);
		actAt.setResid(resid);
		actAt.setRestype(restype);
		return actService.insertintoActAt(actAt);
	}

	@Override
	public List<ActAt> findResAt(long resourceid, Long id) {
		return actService.findResAt(resourceid, id);
	}

	@Override
	public List<ActAt> findUserAt(long userid, Long id) {
		return actService.findUserAt(userid, id);
	}

	@Override
	public List<ActAt> findAtUser(long atUserid, Long id) {
		return actService.findAtUser(atUserid, id);
	}

	@Override
	public int findResAtCount(long resourceid) {
		return actService.findResAtCount(resourceid);
	}

	@Override
	public int findUserAtCount(long userid) {
		return actService.findUserAtCount(userid);
	}

	@Override
	public int findAtUserCount(long atUserid) {
		return actService.findAtUserCount(atUserid);
	}

	@Override
	public ActAt findAtById(long id) {
		return actService.findAtById(id);
	}

	@Override
	public ActAt deleteActAt(long id) {
		ActAt actAt = new ActAt();
		actAt.setId(id);
		actAt.setLatestRevisionDate(System.currentTimeMillis());
		return actService.deleteActAt(actAt);
	}

	/**
	 * 根据用户id查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUserId(String type, Long resId,
			Long userId) {
		return actService.findPublishListByUserId(type, resId, userId);
	}

	/**
	 * 是否有用
	 */
	@Override
	public ActUseful doUseful(long resId, String resType, long resUserid,
			int status, long userid) {
		return actService.doUseful(resId, resType, resUserid, status, userid);
	}

	/**
	 * 查询有用的总数
	 */
	@Override
	public Map<String, Object> findUsefulCount(long resId) {
		return actService.findUsefulCount(resId);
	}

	/**
	 * 查询没用的总数
	 */
	@Override
	public Map<String, Object> findUselessCount(long resId) {
		return actService.findUselessCount(resId);
	}

	/**
	 * 根据id删除评论
	 */
	@Override
	public int delCommentById(long id) {
		return actService.delCommentById(id);
	}

	/**
	 * 根据资源id和用户id查询这条有用信息
	 */
	@Override
	public ActUseful findUsefulByResidAndUserid(long resId, long userId) {
		return actService.findUsefulByResidAndUserid(resId, userId);
	}

	/**
	 * 根据用户的id查询收到的有用
	 */
	@Override
	public List<ActUseful> findUsefulListByResUid(long userid, Long lastId) {
		return actService.findUsefulListByResUid(userid, lastId);
	}

	/**
	 * 根据资源id查询有用列表
	 */
	@Override
	public List<ActUseful> findUsefulListByResIdAndType(long resId, Long id) {
		return actService.findUsefulListByResIdAndType(resId, id);
	}

	/**
	 * 点击加热
	 */
	@Override
	public ActHot doHot(long userId, long resourceId, String type,String ipAddress) {
		return actService.doHot(userId, resourceId, type, ipAddress);
	}

	/**
	 * 查询加热总数
	 */
	@Override
	public Map<String, Object> findHotCount(long resourceId, String type) {
		return actService.findHotCount(resourceId, type);
	}
}
