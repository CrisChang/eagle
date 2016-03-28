package com.poison.act.service.impl;

import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.poison.act.domain.repository.ActDomainRepository;
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

public class ActServiceImpl implements ActService{

	private ActDomainRepository actDomainRepository;

	public void setActDomainRepository(ActDomainRepository actDomainRepository) {
		this.actDomainRepository = actDomainRepository;
	}


	/**
	 * 插入转评信息
	 */
	@Override
	public int doActTransmit(ProductContext productContext,
			ActTransmit actTransmit) {
		return actDomainRepository.doActTransmit(productContext, actTransmit);
	}


	/**
	 * 查询转发列表
	 */
	@Override
	public List<ActTransmit> findTransmitList(ProductContext productContext,
			long userId) {
		return actDomainRepository.findTransmitList(productContext, userId);
	}

	/**
	 * 查询转发总数
	 */
	@Override
	public int findTransmitCount(ProductContext productContext, long resourceId) {
		return actDomainRepository.findTransmitCount(resourceId);
	}

	/**
	 * 查询评论总数
	 */
	@Override
	public int findCommentCount(ProductContext productContext, long resourceId) {
		return actDomainRepository.findCommentCount(resourceId);
	}
	
	/**
	 * 查询点赞的总数
	 */
	@Override
	public int findPraiseCount(ProductContext productContext, long resourceId) {
		return actDomainRepository.findPraiseCount(resourceId);
	}
	
	/**
	 * 根据用户ID查询转发信息
	 */
	@Override
	public List<ActTransmit> findTransmitListByUsersId(
			ProductContext productContext, List<Long> usersIdList) {
		return actDomainRepository.findTransmitListByUsersId(usersIdList);
	}

	/**
	 * 删除评论信息
	 */
	@Override
	public ActComment deleteComment(ProductContext productContext,
			ActComment actComment) {
		return actDomainRepository.deleteComment(actComment);
	}

	/**
	 * 点赞
	 */
	@Override
	public ActPraise doPraise(ProductContext productContext, ActPraise actPraise) {
		return actDomainRepository.doPraise(actPraise);
	}


	/**
	 * 	取消赞
	 */
	@Override
	public ActPraise cancelPraise(ProductContext productContext, ActPraise actPraise) {
		return actDomainRepository.cancelPraise(actPraise);
	}

	/**
	 * 查询一条资源的评论详情
	 */
	@Override
	public List<ActComment> findResCommentList(ProductContext productContext,
			long resourceId,Long id) {
		return actDomainRepository.findCommentList(productContext, resourceId,id);
	}

	/**
	 * 插入一条评论信息
	 */
	@Override
	public ActComment doOneComment(ProductContext productContext, ActComment actComment) {
		return actDomainRepository.doComment(productContext, actComment);
	}

	/**
	 * 查询用户的收藏列表
	 */
	@Override
	public List<ActCollect> findUserCollectList(ProductContext productContext,
			long userId,String type) {
		return actDomainRepository.findUserCollectList(userId,type);
	}

	/**
	 * 用户点击收藏
	 */
	@Override
	public ActCollect doCollect(ProductContext productContext, ActCollect actCollect) {
		return actDomainRepository.doCollect(actCollect);
	}

	/**
	 * 增加一条推荐信息
	 */
	@Override
	public int addOnePublishInfo(ActPublish publish) {
		return actDomainRepository.addOnePublishInfo(publish);
	}

	/**
	 * 查询推荐列表
	 */
	@Override
	public List<ActPublish> findPublishIdList(Long resId) {
		return actDomainRepository.findPublishIdList(resId);
	}

	/**
	 * 根据ID查询一条推荐信息
	 */
	@Override
	public List<ActPublish> findPublishById(long resId) {
		return actDomainRepository.findPublishById(resId);
	}

	/**
	 * 根据ID修改一条发布信息
	 */
	@Override
	public int updatePublishById(ActPublish publish) {
		return actDomainRepository.updatePublishById(publish);
	}

	/**
	 * 查询所有的转发详情
	 */
	@Override
	public List<ActTransmit> findAllTransmitList(Long resId) {
		return actDomainRepository.findAllTransmitList(resId);
	}

	/**
	 * 查询一条发布的详情
	 */
	@Override
	public ActPublish findOnePublish(long id) {
		return actDomainRepository.findOnePublish(id);
	}

	/**
	 * 查询一条转发的详情
	 */
	@Override
	public ActTransmit findOneTransmit(long id) {
		return actDomainRepository.findOneTransmit(id);
	}

	/**
	 * 根据用户ID查询该用户的发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUid(long userId, Long resId) {
		return actDomainRepository.findPublishListByUid(userId, resId);
	}

	/**
	 * 查询一条点赞信息
	 */
	@Override
	public ActPraise findActPraise(ActPraise actPraise) {
		return actDomainRepository.findActPraise(actPraise);
	}

	/**
	 * 根据type查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByType(String type, Long resId) {
		return actDomainRepository.findPublishListByType(type, resId);
	}

	/**
	 * 取消收藏
	 */
	@Override
	public ActCollect cancelCollect(ProductContext productContext,
			ActCollect actCollect) {
		return actDomainRepository.cancelCollect(actCollect);
	}

	/**
	 * 查询资源的收藏总数
	 */
	@Override
	public int findCollectCount(long resourceId) {
		return actDomainRepository.findCollectCount(resourceId);
	}

	/**
	 * 插入订阅信息
	 */
	@Override
	public ActSubscribe doSubscribe(ActSubscribe actSubscribe) {
		return actDomainRepository.doSubscribe(actSubscribe);
	}

	/**
	 * 取消订阅
	 */
	@Override
	public ActSubscribe cancelSubscribe(ActSubscribe actSubscribe) {
		return actDomainRepository.cancelSubscribe(actSubscribe);
	}

	/**
	 * 根据resid查询订阅列表
	 */
	@Override
	public List<ActSubscribe> findSubscribeListByResId(long resId) {
		return actDomainRepository.findSubscribeListByResId(resId);
	}

	/**
	 * 查询该收藏信息是否存在
	 */
	@Override
	public ActCollect findCollectIsExist(long userId, long resId) {
		return actDomainRepository.findCollectIsExist(userId, resId);
	}

	/**
	 * 查询订阅信息
	 */
	@Override
	public ActSubscribe findSubscribeIsExist(long userId, long resId) {
		return actDomainRepository.findSubscribeIsExist(userId, resId);
	}

	/**
	 * 查询订阅列表
	 */
	@Override
	public List<ActSubscribe> findSubscribeList(long userId, String type) {
		return actDomainRepository.findSubscribeList(userId, type);
	}

	/**
	 * 查询点low的总数
	 */
	@Override
	public int findLowCount(long resourceId) {
		return actDomainRepository.findLowCount(resourceId);
	}

	/**
	 * 点low
	 */
	@Override
	public ActPraise doLow(ProductContext productContext, ActPraise actPraise) {
		return actDomainRepository.doLow(actPraise);
	}

	/**
	 * 根据用户id查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUsersId(String type, Long resId,
			List<Long> usersId) {
		return actDomainRepository.findPublishListByUsersId(type, resId, usersId);
	}

	/**
	 * 根据type和uid查询转发信息
	 */
	@Override
	public List<ActTransmit> findTransmitListByTypeAndUsersId(String type,
			Long resId, List<Long> usersId) {
		return actDomainRepository.findTransmitListByTypeAndUsersId(type, resId, usersId);
	}

	/**
	 * 根据推荐类型查询推荐列表
	 */
	@Override
	public List<ActPublish> findPublishListByRecommendType(String recommendType) {
		return actDomainRepository.findPublishListByRecommendType(recommendType);
	}

	/**
	 * 查询发布总数
	 */
	@Override
	public Map<String, Object> findPublishCount(long userId) {
		return actDomainRepository.findPublishCount(userId);
	}

	/**
	 * 根据uid查询转发总数
	 */
	@Override
	public Map<String, Object> findTransmitCountByUid(long userId) {
		return actDomainRepository.findTransmitCountByUid(userId);
	}

	/**
	 * 取消发布
	 */
	@Override
	public ActPublish cancelPublish(long id) {
		return actDomainRepository.cancelPublish(id);
	}

	/**
	 * 根据资源id查询点赞列表
	 */
	@Override
	public List<ActPraise> findPraiseListByResIdAndType(long resId, Long id) {
		return actDomainRepository.findPraiseListByResIdAndType(resId, id);
	}

	/**
	 * 查询用户的评论宗信
	 */
	@Override
	public List<ActComment> findUserCommentCenter(long userId, Long id) {
		return actDomainRepository.findUserCommentCenter(userId, id);
	}

	/**
	 * 根据id查询评论信息
	 */
	@Override
	public ActComment findCmtById(long id) {
		return actDomainRepository.findCmtById(id);
	}

	/**
	 * 根据用户id查询点赞列表
	 */
	@Override
	public List<ActPraise> findPraiseListByResUid(long resUid, Long id) {
		return actDomainRepository.findPraiseListByResUid(resUid, id);
	}

	//以下是微信逼格相关功能=====================
	/**
	 * 保存对一个微信用户的评论
	 */
	@Override
	public int insertWeixinComment(ActWeixinComment actWeixinComment) {
		return actDomainRepository.insertWeixinComment(actWeixinComment);
	}

	/**
	 * 查询对一个微信用户的逼格评论信息列表
	 */
	@Override
	public List<ActWeixinComment> findWeixinComment(String sopendid) {
		return actDomainRepository.findWeixinComment(sopendid);
	}

	/**
	 * 保存一个微信用户信息
	 */
	@Override
	public int insertUser(ActWeixinUser actWeixinUser) {
		return actDomainRepository.insertUser(actWeixinUser);
	}

	/**
	 * 根据分数查询小于该分数的人数
	 */
	@Override
	public long findCountByScore(int score) {
		return actDomainRepository.findCountByScore(score);
	}

	/**
	 * 查询微信用户人数
	 */
	@Override
	public long findUserCount() {
		return actDomainRepository.findUserCount();
	}

	/**
	 * 根据openid查询一个微信用户信息
	 */
	@Override
	public ActWeixinUser findUserById(String openid) {
		return actDomainRepository.findUserById(openid);
	}

	/**
	 * 更新一个微信用户信息
	 */
	@Override
	public int updateUser(ActWeixinUser actWeixinUser) {
		return actDomainRepository.updateUser(actWeixinUser);
	}

	/**
	 * 查询某个微信用户对另一个微信用户的评价是否存在
	 */
	@Override
	public int existUserComment(String openid, String sopenid, String comment) {
		return actDomainRepository.existUserComment(openid, sopenid, comment);
	}

	/**
	 * 查询用户的收藏总数
	 */
	@Override
	public Map<String, Object> findUserCollectCount(long userId) {
		return actDomainRepository.findUserCollectCount(userId);
	}

	/**
	 * 查询用户的收藏列表
	 */
	@Override
	public List<ActCollect> findUserCollectedList(long userId, Long resId) {
		return actDomainRepository.findUserCollectedList(userId, resId);
	}

	/**
	 * 插入at信息
	 */
	@Override
	public ActAt insertintoActAt(ActAt actAt) {
		return actDomainRepository.insertintoActAt(actAt);
	}

	/**
	 * 查找某个资源相关的at列表
	 */
	@Override
	public List<ActAt> findResAt(long resourceid, Long id) {
		return actDomainRepository.findResAt(resourceid, id);
	}

	/**
	 * 查找某个人at别人的列表
	 */
	@Override
	public List<ActAt> findUserAt(long userid, Long id) {
		return actDomainRepository.findUserAt(userid, id);
	}

	/**
	 * 查找某个人被at的列表
	 */
	@Override
	public List<ActAt> findAtUser(long atUserid, Long id) {
		return actDomainRepository.findAtUser(atUserid, id);
	}

	/**
	 * 查询某个资源相关的at的总数
	 */
	@Override
	public int findResAtCount(long resourceid) {
		return actDomainRepository.findResAtCount(resourceid);
	}

	/**
	 * 查询某个人at别人的总数
	 */
	@Override
	public int findUserAtCount(long userid) {
		return actDomainRepository.findUserAtCount(userid);
	}

	/**
	 * 查询某个人被at的总数
	 */
	@Override
	public int findAtUserCount(long atUserid) {
		return actDomainRepository.findAtUserCount(atUserid);
	}

	/**
	 * 查询at信息根据id
	 */
	@Override
	public ActAt findAtById(long id) {
		return actDomainRepository.findAtById(id);
	}

	/**
	 * 删除一个at信息
	 */
	@Override
	public ActAt deleteActAt(ActAt actAt) {
		return actDomainRepository.deleteActAt(actAt);
	}

	/**
	 * 根据用户id查询发布列表
	 */
	@Override
	public List<ActPublish> findPublishListByUserId(String type, Long resId,
			Long userId) {
		return actDomainRepository.findPublishListByUserId(type, resId, userId);
	}

	/**
	 * 是否有用
	 */
	@Override
	public ActUseful doUseful(long resId, String resType, long resUserid,
			int status, long userid) {
		return actDomainRepository.doUseful(resId, resType, resUserid, status, userid);
	}

	/**
	 * 查询有用的总数
	 */
	@Override
	public Map<String, Object> findUsefulCount(long resId) {
		return actDomainRepository.findUsefulCount(resId);
	}


	@Override
	public Map<String, Object> findUselessCount(long resId) {
		return actDomainRepository.findUselessCount(resId);
	}

	/**
	 * 根据id删除评论
	 */
	@Override
	public int delCommentById(long id) {
		return actDomainRepository.delCommentById(id);
	}

	/**
	 * 根据资源id和用户id查询这条有用信息
	 */
	@Override
	public ActUseful findUsefulByResidAndUserid(long resId, long userId) {
		return actDomainRepository.findUsefulByResidAndUserid(resId, userId);
	}

	/**
	 * 根据用户id查询收到的有用
	 */
	@Override
	public List<ActUseful> findUsefulListByResUid(long userid, Long lastId) {
		return actDomainRepository.findUsefulListByResUid(userid, lastId);
	}

	/**
	 * 根据资源id查找
	 */
	@Override
	public List<ActUseful> findUsefulListByResIdAndType(long resId, Long id) {
		return actDomainRepository.findUsefulListByResIdAndType(resId, id);
	}

	/**
	 * 点击加热
	 */
	@Override
	public ActHot doHot(long userId, long resourceId, String type,String ipAddress) {
		return actDomainRepository.doHot(userId, resourceId, type,ipAddress);
	}

	/**
	 * 查找加热总数
	 */
	@Override
	public Map<String, Object> findHotCount(long resourceId, String type) {
		return actDomainRepository.findHotCount(resourceId, type);
	}
}