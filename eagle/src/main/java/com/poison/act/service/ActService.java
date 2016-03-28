package com.poison.act.service;

import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
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
import com.poison.eagle.utils.ResultUtils;

public interface ActService {

	/**
	 * 
	 * <p>Title: insertActTransmit</p> 
	 * <p>Description: 插入转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-24 上午11:24:46
	 * @param productContext
	 * @param actTransmit
	 * @return
	 */
	public int doActTransmit(ProductContext productContext,ActTransmit actTransmit);
	
	/**
	 * 
	 * <p>Title: findTransmitList</p> 
	 * <p>Description: 查询用户转发列表</p> 
	 * @author :changjiang
	 * date 2014-7-26 下午4:17:17
	 * @return
	 */
	public List<ActTransmit> findTransmitList(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: findTransmitCount</p> 
	 * <p>Description: 查找查询总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:30:32
	 * @return
	 */
	public int findTransmitCount(ProductContext productContext,long resourceId);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询评论总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:01:19
	 * @return
	 */
	public int findCommentCount(ProductContext productContext,long resourceId);
	
	/**
	 * 
	 * <p>Title: findResCommentList</p> 
	 * <p>Description: 查询一条资源的评论详情</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午5:31:13
	 * @param productContext
	 * @param resourceId
	 * @return
	 */
	public List<ActComment> findResCommentList(ProductContext productContext,long resourceId,Long id);
	
	/**
	 * 
	 * <p>Title: doOneComment</p> 
	 * <p>Description: 插入一条评论信息</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午5:52:09
	 * @return
	 */
	public ActComment doOneComment(ProductContext productContext,ActComment actComment);
	
	/**
	 * 
	 * <p>Title: findPraiseCount</p> 
	 * <p>Description: 查询点赞的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:25:51
	 * @return
	 */
	public int findPraiseCount(ProductContext productContext,long resourceId);
	
	/**
	 * 
	 * <p>Title: findLowCount</p> 
	 * <p>Description: 查询点low的总数</p> 
	 * @author :changjiang
	 * date 2014-10-11 上午12:07:54
	 * @param resourceId
	 * @return
	 */
	public int findLowCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: findTransmitListByUsersId</p> 
	 * <p>Description: 根据ID查询转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午9:21:30
	 * @param productContext
	 * @param usersIdList
	 * @return
	 */
	public List<ActTransmit> findTransmitListByUsersId(ProductContext productContext,List<Long> usersIdList);
	
	/**
	 * 
	 * <p>Title: updateComment</p> 
	 * <p>Description: 删除评论信息</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午10:44:58
	 * @param productContext
	 * @param actComment
	 * @return
	 */
	public ActComment deleteComment(ProductContext productContext,ActComment actComment);
	
	/**
	 * 
	 * <p>Title: doPraise</p> 
	 * <p>Description: 点赞</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午11:40:53
	 * @return
	 */
	public ActPraise doPraise(ProductContext productContext,ActPraise actPraise);
	
	/**
	 * 
	 * <p>Title: cancelPraise</p> 
	 * <p>Description: 取消赞</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午11:42:01
	 * @param productContext
	 * @param actPraise
	 * @return
	 */
	public ActPraise cancelPraise(ProductContext productContext,ActPraise actPraise);
	
	/**
	 * 
	 * <p>Title: findUserCollectList</p> 
	 * <p>Description: 查找用户收藏的列表信息</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午8:46:07
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public List<ActCollect> findUserCollectList(ProductContext productContext,long userId,String type);
	
	/**
	 * 
	 * <p>Title: doCollect</p> 
	 * <p>Description: 用户点击收藏</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午3:43:52
	 * @return
	 */
	public ActCollect doCollect(ProductContext productContext,ActCollect actCollect);
	
	/**
	 * 
	 * <p>Title: cancelCollect</p> 
	 * <p>Description: 取消收藏</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午2:00:12
	 * @param productContext
	 * @param actCollect
	 * @return
	 */
	public ActCollect cancelCollect(ProductContext productContext,ActCollect actCollect);
	
	/**
	 * 
	 * <p>Title: findCollectIsExist</p> 
	 * <p>Description: 查询该收藏信息是否存在</p> 
	 * @author :changjiang
	 * date 2014-9-24 上午11:15:52
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActCollect findCollectIsExist(long userId,long resId);
	
	/**
	 * 
	 * <p>Title: findCollectCount</p> 
	 * <p>Description: 查询收藏总数</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午4:31:53
	 * @param resourceId
	 * @return
	 */
	public int findCollectCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: addOnePublishInfo</p> 
	 * <p>Description: 增加一条推荐理由</p> 
	 * @author :changjiang
	 * date 2014-8-10 下午4:44:40
	 * @param publish
	 * @return
	 */
	public int addOnePublishInfo(ActPublish publish);
	
	/**
	 * 
	 * <p>Title: findPublishIdList</p> 
	 * <p>Description: 查询推荐列表</p> 
	 * @author :changjiang
	 * date 2014-8-10 下午6:27:36
	 * @param resId
	 * @return
	 */
	public List<ActPublish>  findPublishIdList(Long resId);
	
	/**
	 * 
	 * <p>Title: findPublishById</p> 
	 * <p>Description: 根据ID查询一条发布信息</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午3:10:50
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishById(long resId);
	
	/**
	 * 
	 * <p>Title: updatePublishById</p> 
	 * <p>Description: 更新一条发布信息</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午3:52:53
	 * @param publish
	 * @return
	 */
	public int updatePublishById(ActPublish publish);
	
	/**
	 * 
	 * <p>Title: findAllTransmitList</p> 
	 * <p>Description: 查询转发所有列表</p> 
	 * @author :changjiang
	 * date 2014-8-18 上午1:04:38
	 * @param resId
	 * @return
	 */
	public List<ActTransmit> findAllTransmitList(Long resId); 
	
	/**
	 * 
	 * <p>Title: findOnePublish</p> 
	 * <p>Description: 查询一条发布的信息</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午2:38:35
	 * @param id
	 * @return
	 */
	public ActPublish findOnePublish(long id);
	
	/**
	 * 
	 * <p>Title: findOneTransmit</p> 
	 * <p>Description: 查询一条转发的详情</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午3:05:09
	 * @param id
	 * @return
	 */
	public ActTransmit findOneTransmit(long id);
	
	/**
	 * 
	 * <p>Title: findPublishListByUid</p> 
	 * <p>Description: 根据用户ID查询该用户的发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午7:15:29
	 * @param userId
	 * @param resId
	 * @return
	 */
	public  List<ActPublish> findPublishListByUid(long userId, Long resId);
	
	/**
	 * 
	 * <p>Title: findActPraise</p> 
	 * <p>Description: 查询赞的信息</p> 
	 * @author :changjiang
	 * date 2014-8-20 下午2:09:05
	 * @param actPraise
	 * @return
	 */
	public ActPraise findActPraise(ActPraise actPraise);
	
	/**
	 * 
	 * <p>Title: findPublishListByType</p> 
	 * <p>Description: 根据type查询发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-27 上午12:38:31
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishListByType(String type, Long resId);
	
	/**
	 * 
	 * <p>Title: doSubscribe</p> 
	 * <p>Description: 插入订阅信息</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午5:09:37
	 * @param actSubscribe
	 * @return
	 */
	public ActSubscribe doSubscribe(ActSubscribe actSubscribe);
	
	/**
	 * 
	 * <p>Title: cancelSubscribe</p> 
	 * <p>Description: 取消订阅</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午9:17:43
	 * @param actSubscribe
	 * @return
	 */
	public ActSubscribe cancelSubscribe(ActSubscribe actSubscribe);
	
	/**
	 * 
	 * <p>Title: findSubscribeListByResId</p> 
	 * <p>Description: 根据resid查询订阅列表</p> 
	 * @author :changjiang
	 * date 2014-9-24 上午10:41:35
	 * @param resId
	 * @return
	 */
	public List<ActSubscribe> findSubscribeListByResId(long resId);
	
	/**
	 * 
	 * <p>Title: findSubscribeIsExist</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-9-24 下午4:09:37
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActSubscribe findSubscribeIsExist(long userId,long resId);
	
	/**
	 * 
	 * <p>Title: findSubscribeList</p> 
	 * <p>Description: 查询订阅列表</p> 
	 * @author :changjiang
	 * date 2014-9-24 下午4:26:23
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<ActSubscribe> findSubscribeList(long userId,String type);
	
	/**
	 * 
	 * <p>Title: doPraise</p> 
	 * <p>Description: 点low</p> 
	 * @author :changjiang
	 * date 2014-10-12 下午6:02:30
	 * @param productContext
	 * @param actPraise
	 * @return
	 */
	public ActPraise doLow(ProductContext productContext, ActPraise actPraise);
	
	/**
	 * 
	 * <p>Title: findPublishListByUsersId</p> 
	 * <p>Description: 根据用户id查询发布列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午12:10:16
	 * @param type
	 * @param resId
	 * @param usersId
	 * @return
	 */
	public List<ActPublish> findPublishListByUsersId(String type, Long resId,
			List<Long> usersId);
	
	/**
	 * 
	 * <p>Title: findTransmitListByTypeAndUsersId</p> 
	 * <p>Description: 根据type和uid查询转发列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午5:18:10
	 * @param type
	 * @param resId
	 * @param usersId
	 * @return
	 */
	public List<ActTransmit> findTransmitListByTypeAndUsersId(String type,
			Long resId, List<Long> usersId);
	
	/**
	 * 
	 * <p>Title: findPublishListByRecommendType</p> 
	 * <p>Description: 根据推荐类型查询推荐列表</p> 
	 * @author :changjiang
	 * date 2014-11-25 下午6:59:57
	 * @param recommendType
	 * @return
	 */
	public List<ActPublish> findPublishListByRecommendType(String recommendType);
	
	/**
	 * 
	 * <p>Title: findPublishCount</p> 
	 * <p>Description: 查询发布总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:43:49
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPublishCount(long userId);
	
	/**
	 * 
	 * <p>Title: findTransmitCountByUid</p> 
	 * <p>Description: 根据uid查询转发总数</p> 
	 * @author :changjiang
	 * date 2014-12-10 下午3:25:33
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findTransmitCountByUid(long userId);
	
	/**
	 * 
	 * <p>Title: cancelPublish</p> 
	 * <p>Description: 取消发布</p> 
	 * @author :changjiang
	 * date 2015-2-5 下午1:46:48
	 * @param id
	 * @return
	 */
	public ActPublish cancelPublish(long id);
	
	/**
	 * 
	 * <p>Title: findPraiseListByResIdAndType</p> 
	 * <p>Description: 根据资源查询点赞列表</p> 
	 * @author :changjiang
	 * date 2015-2-25 下午5:04:52
	 * @param resId
	 * @param id
	 * @return
	 */
	public List<ActPraise> findPraiseListByResIdAndType(long resId,Long id);
	
	/**
	 * 
	 * <p>Title: findUserCommentCenter</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-3-9 下午7:43:43
	 * @param userId
	 * @param id
	 * @return
	 */
	public List<ActComment> findUserCommentCenter(long userId, Long id);
	
	/**
	 * 
	 * <p>Title: findCmtById</p> 
	 * <p>Description: 根据id查询评论信息</p> 
	 * @author :changjiang
	 * date 2015-3-10 上午10:30:54
	 * @param id
	 * @return
	 */
	public ActComment findCmtById(long id);
	
	/**
	 * 
	 * <p>Title: findPraiseListByResUid</p> 
	 * <p>Description: 根据uid查询用户的点赞列表</p> 
	 * @author :changjiang
	 * date 2015-3-11 下午8:25:16
	 * @param resUid
	 * @param id
	 * @return
	 */
	public List<ActPraise> findPraiseListByResUid(long resUid, Long id);
	
	//以下是微信逼格相关功能=====================
	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入评论信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertWeixinComment(ActWeixinComment actWeixinComment);
	
	/**
	 * 
	 * <p>Title: findComment</p> 
	 * <p>Description: 查找评论列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param userId
	 * @return
	 */
	public List<ActWeixinComment> findWeixinComment(String sopendid);
	
	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入微信用户信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertUser(ActWeixinUser actWeixinUser);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findCountByScore(int score);
	
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findUserCount();
	
	/**
	 * 
	 * <p>Title: findCommentById</p> 
	 * <p>Description: 查询微信用户信息</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActWeixinUser findUserById(String openid);
	
	/**
	 * 
	 * <p>Title: updateComment</p> 
	 * <p>Description: 示例类</p> 
	 * @author :weizhensong
	 * date 2014-7-30 上午9:53:26
	 * @return
	 */
	public int updateUser(ActWeixinUser actWeixinUser);
	
	/**
	 * 
	 * @Title: existUserComment 
	 * @Description: 查询是否存在一个微信用户的某个评价信息
	 * @author weizhensong
	 * @date 2015-3-23 下午6:03:48
	 * @param @param openid
	 * @param @param comment
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int existUserComment(String openid, String sopenid,String comment);
	
	/**
	 * 
	 * <p>Title: findUserCollectCount</p> 
	 * <p>Description: 查询用户的收藏总数</p> 
	 * @author :changjiang
	 * date 2015-3-31 下午3:00:19
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findUserCollectCount(long userId);
	
	/**
	 * 
	 * <p>Title: findUserCollectedList</p> 
	 * <p>Description: 查询用户的收藏列表</p> 
	 * @author :changjiang
	 * date 2015-4-7 上午11:08:51
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<ActCollect> findUserCollectedList(long userId, Long resId);
	
	
	/**
	 * 
	 * <p>Title: insertintoActAt</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public ActAt insertintoActAt(ActAt actAt);
	
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个资源相关的at列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param resourceid
	 * @return
	 */
	public List<ActAt> findResAt(long resourceid,Long id);
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个人at别人的列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param userid
	 * @return
	 */
	public List<ActAt> findUserAt(long userid,Long id);
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个人被at的列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param atUserid
	 * @return
	 */
	public List<ActAt> findAtUser(long atUserid,Long id);
	/**
	 * 
	 * <p>Title: findResAtCount</p> 
	 * <p>Description: 查询某个资源相关的at的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param resourceid
	 * @return
	 */
	public int findResAtCount(long resourceid);
	/**
	 * 
	 * <p>Title: findUserAtCount</p> 
	 * <p>Description: 查询某个人at别人的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param userid
	 * @return
	 */
	public int findUserAtCount(long userid);
	/**
	 * 
	 * <p>Title: findAtUserCount</p> 
	 * <p>Description: 查询某个人被at的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param atUserid
	 * @return
	 */
	public int findAtUserCount(long atUserid);
	
	/**
	 * 
	 * <p>Title: findAtById</p> 
	 * <p>Description: 查询at信息根据id</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActAt findAtById(long id);
	
	/**
	 * 
	 * <p>Title: deleteActAt</p> 
	 * <p>Description: 删除一个at信息</p> 
	 * @author :weizhensong
	 * date 2014-7-30 上午9:53:26
	 * @return
	 */
	public ActAt deleteActAt(ActAt actAt);
	
	/**
	 * 
	 * <p>Title: findPublishListByUserId</p> 
	 * <p>Description: 根据id查询用户的发布信息</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午3:14:17
	 * @param type
	 * @param resId
	 * @param userId
	 * @return
	 */
	public List<ActPublish> findPublishListByUserId(String type, Long resId,
			Long userId);
	
	/**
	 * 
	 * <p>Title: doUseful</p> 
	 * <p>Description: 是否有用</p> 
	 * @author :changjiang
	 * date 2015-6-9 下午6:27:51
	 * @param resId
	 * @param resType
	 * @param resUserid
	 * @param status
	 * @param userid
	 * @return
	 */
	public ActUseful doUseful(long resId,String resType,long resUserid,int status,long userid);
	
	/**
	 * 
	 * <p>Title: findUsefulCount</p> 
	 * <p>Description: 查询有用的总数</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:51:23
	 * @param resId
	 * @return
	 */
	public Map<String, Object> findUsefulCount(long resId);
	
	/**
	 * 
	 * <p>Title: findUselessCount</p> 
	 * <p>Description: 查询没用的总数</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:52:03
	 * @param resId
	 * @return
	 */
	public Map<String, Object> findUselessCount(long resId);
	
	/**
	 * 
	 * <p>Title: delCommentById</p> 
	 * <p>Description: 根据id删除评论</p> 
	 * @author :changjiang
	 * date 2015-6-12 上午11:16:51
	 * @param id
	 * @return
	 */
	public int delCommentById(long id);
	
	/**
	 * 
	 * <p>Title: findUsefulByResidAndUserid</p> 
	 * <p>Description: 根据资源id和用户id查询这条有用信息</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午12:28:50
	 * @param resId
	 * @param userId
	 * @return
	 */
	public ActUseful findUsefulByResidAndUserid(long resId,long userId);
	
	/**
	 * 
	 * <p>Title: findUsefulListByResUid</p> 
	 * <p>Description: 根据用户id查询收到的有用</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午5:57:05
	 * @param userid
	 * @param lastId
	 * @return
	 */
	public List<ActUseful> findUsefulListByResUid(long userid, Long lastId);
	
	/**
	 * 
	 * <p>Title: findUsefulListByResIdAndType</p> 
	 * <p>Description: 根据资源id查询有用列表</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午7:34:40
	 * @param resId
	 * @param id
	 * @return
	 */
	public List<ActUseful> findUsefulListByResIdAndType(long resId, Long id);
	
	/**
	 * 
	 * <p>Title: doHot</p> 
	 * <p>Description: 点击加热</p> 
	 * @author :changjiang
	 * date 2015-7-9 上午11:35:01
	 * @param userId
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public ActHot doHot(long userId,long resourceId,String type,String ipAddress);
	
	/**
	 * 
	 * <p>Title: findHotCount</p> 
	 * <p>Description: 查找加热总数</p> 
	 * @author :changjiang
	 * date 2015-7-9 下午3:42:50
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public Map<String, Object> findHotCount(long resourceId, String type);
}
