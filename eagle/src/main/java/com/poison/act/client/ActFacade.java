package com.poison.act.client;

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

public interface ActFacade {

	/**
	 * 
	 * <p>Title: insertActTransmit</p> 
	 * <p>Description: 增加转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-24 上午11:39:32
	 * @return
	 */
	public ActTransmit doActTransmit(ProductContext productContext,
			long userId,long resourceId,String type,String transmitContext);
	
	/**
	 * 
	 * <p>Title: findTransmitList</p> 
	 * <p>Description: 查询转发列表</p> 
	 * @author :changjiang
	 * date 2014-7-26 下午4:21:34
	 * @return
	 */
	public List<ActTransmit> findTransmitList(ProductContext productContext,long userId);
	
	/**
	 * 
	 * <p>Title: findTransmitCount</p> 
	 * <p>Description: 查询转发总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:33:29
	 * @return
	 */
	public int findTransmitCount(ProductContext productContext,long resourceId);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询评论总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:02:48
	 * @return
	 */
	public int findCommentCount(ProductContext productContext,long resourceId);
	
	/**
	 * 
	 * <p>Title: findPraiseCount</p> 
	 * <p>Description: 查询点赞的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:29:20
	 * @return
	 */
	public int findPraiseCount(ProductContext productContext,long resourceId);
	
	/**
	 * 
	 * <p>Title: findLowCount</p> 
	 * <p>Description: 查询点low的总数</p> 
	 * @author :changjiang
	 * date 2014-10-11 上午12:09:35
	 * @param resourceId
	 * @return
	 */
	public int findLowCount(long resourceId);
	
	
	/**
	 * 
	 * <p>Title: findTransmitListByUsersId</p> 
	 * <p>Description: 根据用户ID查询转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午9:27:40
	 * @param productContext
	 * @param usersIdList
	 * @return
	 */
	public List<ActTransmit> findTransmitListByUsersId(ProductContext productContext,List<Long> usersIdList);
	
	/**
	 * 
	 * <p>Title: deleteComment</p> 
	 * <p>Description: 删除评论信息</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午10:49:25
	 * @return
	 */
	public ActComment deleteComment(ProductContext productContext,long commentId);
	
	/**
	 * 
	 * <p>Title: doPraise</p> 
	 * <p>Description: 点击赞</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午2:23:53
	 * @return
	 */
	public ActPraise doPraise(ProductContext productContext,long userId,long resourceId,String type,long resUserId);
	
	/**
	 * 
	 * <p>Title: doLow</p> 
	 * <p>Description: 点击low</p> 
	 * @author :changjiang
	 * date 2014-10-10 下午11:48:41
	 * @param productContext
	 * @param userId
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public ActPraise doLow(ProductContext productContext,long userId,long resourceId,String type);
	
	/**
	 * 
	 * <p>Title: cancelPraise</p> 
	 * <p>Description: 取消赞</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午2:26:31
	 * @param productContext
	 * @param actPraise
	 * @return
	 */
	public ActPraise cancelPraise(ProductContext productContext,long userId,long resourceId,String type);
	
	/**
	 * 
	 * <p>Title: findResCommentList</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午5:35:36
	 * @return
	 */
	public List<ActComment> findResCommentList(ProductContext productContext,long resourceId,Long id);
	
	/**
	 * 
	 * <p>Title: doOneComment</p> 
	 * <p>Description: 插入一条评论信息</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午5:54:11
	 * @return
	 */
	public ActComment doOneComment(ProductContext productContext,long userId,long resourceId,String type,String context,long commentUserId,long commentId,long resUserId);
	
	/**
	 * 
	 * <p>Title: findUserCollectList</p> 
	 * <p>Description: 查询用户的收藏列表</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午8:52:59
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public List<ActCollect> findUserCollectList(ProductContext productContext,long userId,String type);
	
	/**
	 * 
	 * <p>Title: findUserCollectedList</p> 
	 * <p>Description: 查询用户的收藏列表</p> 
	 * @author :changjiang
	 * date 2015-4-7 上午10:54:55
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<ActCollect> findUserCollectedList(long userId,Long resId);
	
	/**
	 * 
	 * <p>Title: doCollect</p> 
	 * <p>Description: 添加收藏信息</p> 
	 * @author :changjiang
	 * date 2014-9-23 上午11:33:37
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActCollect doCollect(long userId,long resId,String type);
	
	/**
	 * 
	 * <p>Title: cancelCollect</p> 
	 * <p>Description: 取消收藏</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午2:02:10
	 * @param id
	 * @return
	 */
	public ActCollect cancelCollect(long userId, long resId);
	
	/**
	 * 
	 * <p>Title: findCollectIsExist</p> 
	 * <p>Description: 根据uid，和resid查询收藏信息是否存在</p> 
	 * @author :changjiang
	 * date 2014-9-24 上午11:17:52
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActCollect findCollectIsExist(long userId, long resId);
	
	/**
	 * 
	 * <p>Title: findCollectCount</p> 
	 * <p>Description: 收藏总数</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午4:34:35
	 * @param resourceId
	 * @return
	 */
	public int findCollectCount(long resourceId);
	
	/**
	 * 
	 * <p>Title: addOnePublishInfo</p> 
	 * <p>Description: 增加一条推荐信息</p> 
	 * @author :changjiang
	 * date 2014-8-10 下午4:46:04
	 * @param publish
	 * @return
	 */
	public ActPublish addOnePublishInfo(long userId,long resId,String type);
	
	
	/**
	 * 
	 * <p>Title: findPublishIdList</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-8-10 下午6:29:06
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishList(Long resId);
	
	/**
	 * 
	 * <p>Title: findAllTransmitList</p> 
	 * <p>Description: 查询转发的所有信息</p> 
	 * @author :changjiang
	 * date 2014-8-18 上午1:06:20
	 * @param resId
	 * @return
	 */
	public List<ActTransmit> findAllTransmitList(Long resId);
	
	/**
	 * 
	 * <p>Title: findOnePublish</p> 
	 * <p>Description: 查询一条发布的详情</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午2:39:52
	 * @param id
	 * @return
	 */
	public ActPublish findOnePublish(long id);
	
	/**
	 * 
	 * <p>Title: findOneTransmit</p> 
	 * <p>Description: 查询一条转发的详情</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午3:06:19
	 * @param id
	 * @return
	 */
	public ActTransmit findOneTransmit(long id);
	
	/**
	 * 
	 * <p>Title: findPublishListByUid</p> 
	 * <p>Description: 根据用户ID查询该用户的发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午7:17:51
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishListByUid(long userId, Long resId);
	
	/**
	 * 
	 * <p>Title: findActPraise</p> 
	 * <p>Description: 查询点赞信息</p> 
	 * @author :changjiang
	 * date 2014-8-20 下午2:11:32
	 * @param actPraise
	 * @return
	 */
	public ActPraise findActPraise(long userId,long resId);
	
	/**
	 * 
	 * <p>Title: findPublishListByType</p> 
	 * <p>Description: 根据type查询发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-27 上午12:41:27
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishListByType(String type, Long resId);
	
	/**
	 * 
	 * <p>Title: doSubscribe</p> 
	 * <p>Description: 插入一条订阅信息</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午5:11:37
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public ActSubscribe doSubscribe(long userId,long resId,String type);
	
	/**
	 * 
	 * <p>Title: cancelSubscribe</p> 
	 * <p>Description: 取消订阅</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午9:21:47
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActSubscribe cancelSubscribe(long userId,long resId);
	
	/**
	 * 
	 * <p>Title: findSubscribeListByResId</p> 
	 * <p>Description: 根据resId查询订阅列表</p> 
	 * @author :changjiang
	 * date 2014-9-24 上午10:44:24
	 * @param resId
	 * @return
	 */
	public List<ActSubscribe> findSubscribeListByResId(long resId);
	
	/**
	 * 
	 * <p>Title: findSubscribeIsExist</p> 
	 * <p>Description: 查询订阅信息</p> 
	 * @author :changjiang
	 * date 2014-9-24 下午4:11:06
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActSubscribe findSubscribeIsExist(long userId, long resId);
	
	/**
	 * 
	 * <p>Title: findSubscribeList</p> 
	 * <p>Description: 查询订阅列表</p> 
	 * @author :changjiang
	 * date 2014-9-24 下午4:27:33
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<ActSubscribe> findSubscribeList(long userId, String type);
	
	/**
	 * 
	 * <p>Title: findPublishListByUsersId</p> 
	 * <p>Description: 根据用户ID查询发布列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 上午11:35:55
	 * @param type
	 * @param resId
	 * @param usersId
	 * @return
	 */
	public List<ActPublish> findPublishListByUsersId(String type, Long resId,List<Long> usersId);
	
	/**
	 * 
	 * <p>Title: findTransmitListByTypeAndUsersId</p> 
	 * <p>Description: 根据type和uid查询转发列表</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午5:19:50
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
	 * date 2014-11-25 下午7:02:07
	 * @param recommendType
	 * @return
	 */
	public List<ActPublish> findPublishListByRecommendType(String recommendType);
	
	/**
	 * 
	 * <p>Title: findPublishCount</p> 
	 * <p>Description: 查询发布的总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:46:55
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPublishCount(long userId);
	
	/**
	 * 
	 * <p>Title: findTransmitCountByUid</p> 
	 * <p>Description: 根据uid查询转发总数</p> 
	 * @author :changjiang
	 * date 2014-12-10 下午3:29:31
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findTransmitCountByUid(long userId);
	
	/**
	 * 
	 * <p>Title: cancelPublish</p> 
	 * <p>Description: 取消发布</p> 
	 * @author :changjiang
	 * date 2015-2-5 下午1:48:18
	 * @param id
	 * @return
	 */
	public ActPublish cancelPublish(long id);
	
	/**
	 * 
	 * <p>Title: findPraiseListByResIdAndType</p> 
	 * <p>Description: 查询一条资源的点赞列表</p> 
	 * @author :changjiang
	 * date 2015-2-25 下午4:50:03
	 * @param resId
	 * @param resType
	 * @param id
	 * @return
	 */
	public List<ActPraise> findPraiseListByResIdAndType(long resId,Long id);
	
	/**
	 * 
	 * <p>Title: findUserCommentCenter</p> 
	 * <p>Description: 查找用户的评论中心</p> 
	 * @author :changjiang
	 * date 2015-3-9 下午7:48:36
	 * @param userId
	 * @param id
	 * @return
	 */
	public List<ActComment> findUserCommentCenter(long userId, Long id);
	
	/**
	 * 
	 * <p>Title: findCmtById</p> 
	 * <p>Description: 根据id查询评论</p> 
	 * @author :changjiang
	 * date 2015-3-10 上午10:35:39
	 * @param id
	 * @return
	 */
	public ActComment findCmtById(long id);
	
	/**
	 * 
	 * <p>Title: findPraiseListByResUid</p> 
	 * <p>Description: 根据用户id查询用户的点赞列表</p> 
	 * @author :changjiang
	 * date 2015-3-11 下午8:26:29
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
	 * @author :changjiang
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertWeixinComment(ActWeixinComment actWeixinComment);
	
	/**
	 * 
	 * <p>Title: findComment</p> 
	 * <p>Description: 查找评论列表</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午12:29:00
	 * @param userId
	 * @return
	 */
	public List<ActWeixinComment> findWeixinComment(String sopendid);
	
	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入微信用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertUser(ActWeixinUser actWeixinUser);
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findCountByScore(int score);
	
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findUserCount();
	
	/**
	 * 
	 * <p>Title: findCommentById</p> 
	 * <p>Description: 查询微信用户信息</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActWeixinUser findUserById(String openid);
	
	/**
	 * 
	 * <p>Title: updateComment</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
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
	 * date 2015-3-31 下午3:01:18
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findUserCollectCount(long userId);
	
	
	/**
	 * 
	 * <p>Title: insertintoActAt</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public ActAt insertintoActAt(long userid,long resourceid,long resUserid,String type,long atUserid,long resid,String restype);
	
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
	public ActAt deleteActAt(long id);
	
	/**
	 * 
	 * <p>Title: findPublishListByUserId</p> 
	 * <p>Description: 根据用户id查询发布列表</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午3:19:38
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
	 * date 2015-6-9 下午6:30:08
	 * @param resId
	 * @param resType
	 * @param resUserid
	 * @param status
	 * @param userid
	 * @return
	 */
	public ActUseful doUseful(long resId, String resType, long resUserid,
			int status, long userid);
	
	/**
	 * 
	 * <p>Title: findUsefulCount</p> 
	 * <p>Description: 查询有用的总数</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:53:47
	 * @param resId
	 * @return
	 */
	public Map<String, Object> findUsefulCount(long resId);
	
	/**
	 * 
	 * <p>Title: findUselessCount</p> 
	 * <p>Description: 查询没用的总数</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:54:11
	 * @param resId
	 * @return
	 */
	public Map<String, Object> findUselessCount(long resId);
	
	/**
	 * 
	 * <p>Title: delCommentById</p> 
	 * <p>Description: 根据id删除评论</p> 
	 * @author :changjiang
	 * date 2015-6-12 上午11:18:03
	 * @param id
	 * @return
	 */
	public int delCommentById(long id);
	
	/**
	 * 
	 * <p>Title: findUsefulByResidAndUserid</p> 
	 * <p>Description: 根据资源id和用户id查询这条有用信息</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午12:30:21
	 * @param resId
	 * @param userId
	 * @return
	 */
	public ActUseful findUsefulByResidAndUserid(long resId, long userId);
	
	/**
	 * 
	 * <p>Title: findUsefulListByResUid</p> 
	 * <p>Description: 根据用户的id查询收到的有用</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午6:00:38
	 * @param userid
	 * @param lastId
	 * @return
	 */
	public List<ActUseful> findUsefulListByResUid(long userid, Long lastId);
	
	/**
	 * 
	 * <p>Title: findUsefulListByResIdAndType</p> 
	 * <p>Description: 查询资源的有用列表</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午7:48:37
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
	 * date 2015-7-9 上午11:37:01
	 * @param userId
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public ActHot doHot(long userId, long resourceId, String type,String ipAddress);
	
	/**
	 * 
	 * <p>Title: findHotCount</p> 
	 * <p>Description: 查找加热总数</p> 
	 * @author :changjiang
	 * date 2015-7-9 下午3:44:03
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public Map<String, Object> findHotCount(long resourceId, String type);
}
