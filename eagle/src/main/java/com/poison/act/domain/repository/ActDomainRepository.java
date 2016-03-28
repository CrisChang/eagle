package com.poison.act.domain.repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.keel.utils.UKeyWorker;
import com.poison.act.dao.ActAtDAO;
import com.poison.act.dao.ActCollectDAO;
import com.poison.act.dao.ActCommentDAO;
import com.poison.act.dao.ActHotDAO;
import com.poison.act.dao.ActPraiseDAO;
import com.poison.act.dao.ActPublishDAO;
import com.poison.act.dao.ActSubscribeDAO;
import com.poison.act.dao.ActTransmitDAO;
import com.poison.act.dao.ActUsefulDAO;
import com.poison.act.dao.ActWeixinCommentDAO;
import com.poison.act.dao.ActWeixinUserDAO;
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
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.ResultUtils;

public class ActDomainRepository {

	private ActTransmitDAO actTransmitDAO;
	
	private ActCommentDAO actCommentDAO;
	
	private ActPraiseDAO actPraiseDAO;
	
	private ActCollectDAO actCollectDAO;
	
	private ActPublishDAO ActPublishDAO;
	
	private ActSubscribeDAO actSubscribeDAO;
	
	private ActWeixinCommentDAO actWeixinCommentDAO;
	
	private ActWeixinUserDAO actWeixinUserDAO;
	
	private ActAtDAO actAtDAO;
	
	private ActUsefulDAO actUsefulDAO;
	
	private ActHotDAO actHotDAO;
	
	private UKeyWorker reskeyWork;
	
	/**
	 * 加热间隔为24个小时
	 */
	private final long HOT_INTERVAL = 1000*60*60*24;

	
	public void setActHotDAO(ActHotDAO actHotDAO) {
		this.actHotDAO = actHotDAO;
	}

	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setActTransmitDAO(ActTransmitDAO actTransmitDAO) {
		this.actTransmitDAO = actTransmitDAO;
	}

	public void setActCommentDAO(ActCommentDAO actCommentDAO) {
		this.actCommentDAO = actCommentDAO;
	}

	public void setActPraiseDAO(ActPraiseDAO actPraiseDAO) {
		this.actPraiseDAO = actPraiseDAO;
	}

	public void setActCollectDAO(ActCollectDAO actCollectDAO) {
		this.actCollectDAO = actCollectDAO;
	}

	public void setActPublishDAO(ActPublishDAO actPublishDAO) {
		ActPublishDAO = actPublishDAO;
	}

	public void setActSubscribeDAO(ActSubscribeDAO actSubscribeDAO) {
		this.actSubscribeDAO = actSubscribeDAO;
	}
	
	public void setActWeixinCommentDAO(ActWeixinCommentDAO actWeixinCommentDAO) {
		this.actWeixinCommentDAO = actWeixinCommentDAO;
	}

	public void setActWeixinUserDAO(ActWeixinUserDAO actWeixinUserDAO) {
		this.actWeixinUserDAO = actWeixinUserDAO;
	}

	public void setActAtDAO(ActAtDAO actAtDAO) {
		this.actAtDAO = actAtDAO;
	}

	public void setActUsefulDAO(ActUsefulDAO actUsefulDAO) {
		this.actUsefulDAO = actUsefulDAO;
	}

	/**
	 * 
	 * <p>Title: insertActTransmit</p> 
	 * <p>Description: 插入转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-24 上午11:13:18
	 * @return
	 */
	public int doActTransmit(ProductContext productContext,ActTransmit actTransmit){
		return actTransmitDAO.insertTransimit(actTransmit);
	}
	
	/**
	 * 
	 * <p>Title: findTransmitCount</p> 
	 * <p>Description: 查询转发资源的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:28:06
	 * @param resourceId
	 * @return
	 */
	public int findTransmitCount(long resourceId){
		return actTransmitDAO.findTransmitCount(resourceId);
	}
	/**
	 * 
	 * <p>Title: findTransmitList</p> 
	 * <p>Description: 查询用户转发列表</p> 
	 * @author :changjiang
	 * date 2014-7-26 下午4:14:21
	 * @param productContext
	 * @param userId
	 * @return
	 */
	public List<ActTransmit> findTransmitList(ProductContext productContext,long userId){
		return actTransmitDAO.findTransmit(userId);
	}
	
	/**
	 * 
	 * <p>Title: doComment</p> 
	 * <p>Description: 插入评论内容</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午12:45:18
	 * @return
	 */
	public ActComment doComment(ProductContext productContext,ActComment actComment){
		int flag =  actCommentDAO.insertComment(actComment);
		ActComment comment = new ActComment();
		if(ResultUtils.SUCCESS==flag){
			long id = actComment.getId();
			comment = actCommentDAO.findCommentById(actComment);
			comment.setFlag(ResultUtils.SUCCESS);
			return comment;
		}
		comment.setFlag(ResultUtils.ERROR);
		return comment;
	}
	
	/**
	 * 
	 * <p>Title: findCommentList</p> 
	 * <p>Description: 查询</p> 
	 * @author :changjiang
	 * date 2014-7-27 上午12:54:56
	 * @return
	 */
	public List<ActComment> findCommentList(ProductContext productContext,long resourceId,Long id){
		return actCommentDAO.findComment(resourceId,id);
	}
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询评论总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午4:59:22
	 * @return
	 */
	public int findCommentCount(long resourceId){
		return actCommentDAO.findCommentCount(resourceId);
	}
	
	/**
	 * 
	 * <p>Title: findPraiseCount</p> 
	 * <p>Description: 查询点赞的总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:23:38
	 * @return
	 */
	public int findPraiseCount(long resourceId){
		return actPraiseDAO.findPraiseCount(resourceId);
	}
	
	/**
	 * 
	 * <p>Title: findLowCount</p> 
	 * <p>Description: 查询点low的总数</p> 
	 * @author :changjiang
	 * date 2014-10-11 上午12:06:47
	 * @param resourceId
	 * @return
	 */
	public int findLowCount(long resourceId){
		return actPraiseDAO.findLowCount(resourceId);
	}
	
	/**
	 * 
	 * <p>Title: findCollectCount</p> 
	 * <p>Description: 查询收藏总数</p> 
	 * @author :changjiang
	 * date 2014-7-28 下午5:51:16
	 * @return
	 */
	public int findCollectCount(long resourceId){
		return actCollectDAO.findCollectCount(resourceId);
	}
	
	/**
	 * 
	 * <p>Title: findUserCollectList</p> 
	 * <p>Description: 用户收藏的列表</p> 
	 * @author :changjiang
	 * date 2014-7-30 下午8:43:16
	 * @param userId
	 * @return
	 */
	public List<ActCollect> findUserCollectList(long userId,String type){
		return actCollectDAO.findCollectList(userId,type);
	}
	
	/**
	 * 
	 * <p>Title: doCollect</p> 
	 * <p>Description: 点击收藏</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午3:25:54
	 * @param actCollect
	 * @return
	 */
	public ActCollect doCollect(ActCollect actCollect){
		long userId = actCollect.getUserId();
		long resourceId = actCollect.getResourceId();
		//查询收藏信息是否存在
		ActCollect collect = actCollectDAO.findCollectIsExist(userId, resourceId);
		int flag = collect.getFlag();
		//不存在信息时插入一条收藏信息
		if(ResultUtils.DATAISNULL==flag){
			actCollectDAO.doCollect(actCollect);
			collect = actCollectDAO.findCollectIsExist(userId, resourceId);
		}else if(ResultUtils.SUCCESS==flag){
			//已经收藏过该信息
			int isCollect = collect.getIsCollect();
			if(1==isCollect){
				collect.setFlag(ResultUtils.IS_COLLECTED);
			}else{
				actCollectDAO.updateCollect(actCollect);
				collect = actCollectDAO.findCollectIsExist(userId, resourceId);
			}
		}
		return collect;
	}
	
	/**
	 * 
	 * <p>Title: cancelCollect</p> 
	 * <p>Description: 取消收藏</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午1:28:52
	 * @param actCollect
	 * @return
	 */
	public ActCollect cancelCollect(ActCollect actCollect){
		long userId = actCollect.getUserId();
		long resourceId = actCollect.getResourceId();
		ActCollect collect = actCollectDAO.findCollectIsExist(userId,resourceId);
		int flag = collect.getFlag();
		if(ResultUtils.DATAISNULL==flag){
			return collect;
		}
		int isCollect = collect.getIsCollect();
		if(0==isCollect){
			collect.setFlag(ResultUtils.IS_CANCELED_COLLECT);
		}else{
			actCollect.setUserId(collect.getUserId());
			actCollect.setResourceId(collect.getResourceId());
			actCollectDAO.updateCollect(actCollect);
			collect = actCollectDAO.findCollectIsExist(userId,resourceId);
		}
		return collect;
	}
	
	/**
	 * 
	 * <p>Title: findCollectIsExist</p> 
	 * <p>Description: 查询收藏信息是否存在</p> 
	 * @author :changjiang
	 * date 2014-9-24 上午11:13:04
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActCollect findCollectIsExist(long userId,long resId){
		return actCollectDAO.findCollectIsExist(userId, resId);
	}
	
	/**
	 * 
	 * <p>Title: findTransmitListByUsersId</p> 
	 * <p>Description: 根据用户ID查询转发信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午9:19:47
	 * @param usersIdList
	 * @return
	 */
	public List<ActTransmit> findTransmitListByUsersId(List<Long> usersIdList){
		return actTransmitDAO.findTransmitListByUserId(usersIdList);
	}
	
	/**
	 * 
	 * <p>Title: deleteComment</p> 
	 * <p>Description: 删除评论信息</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午10:05:14
	 * @return
	 */
	public ActComment deleteComment(ActComment actComment){
		ActComment comment = new ActComment();
		long commentId = actComment.getId();
		comment.setId(actComment.getId());
		comment.setIsDelete(0);
		comment = actCommentDAO.findCommentById(comment);
		if(null==comment||comment.getId()==0){
			comment = new ActComment();
			comment.setFlag(ResultUtils.DATAISNULL);
			return comment;
		}
		int flag = actCommentDAO.updateComment(actComment);
		comment = actCommentDAO.findCommentById(actComment);
		
		comment.setFlag(flag);
		return comment;
	}
	
	/**
	 * 
	 * <p>Title: doPraise</p> 
	 * <p>Description: 点赞</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午11:35:36
	 * @param actPraise
	 * @return
	 */
	public ActPraise doPraise(ActPraise actPraise){
		ActPraise praise = new ActPraise();
		praise = actPraiseDAO.findActPraise(actPraise);
		if(0==praise.getId()){
			actPraiseDAO.insertPraise(actPraise);
			praise = new ActPraise();
			praise = actPraiseDAO.findActPraise(actPraise);
			praise.setFlag(ResultUtils.SUCCESS);
			return praise;
		}
		if(praise.getIsPraise()==1){
			praise.setFlag(ResultUtils.EXISTED_PRAISE);
			return praise;
		}
		actPraiseDAO.updatePraise(actPraise);
		praise = actPraiseDAO.findActPraise(actPraise);
		praise.setFlag(ResultUtils.SUCCESS);
		return praise;
	}
	
	/**
	 * 
	 * <p>Title: doLow</p> 
	 * <p>Description: 点Low</p> 
	 * @author :changjiang
	 * date 2014-10-12 下午6:00:41
	 * @param actPraise
	 * @return
	 */
	public ActPraise doLow(ActPraise actPraise){
		ActPraise praise = new ActPraise();
		praise = actPraiseDAO.findActPraise(actPraise);
		if(0==praise.getId()){
			actPraiseDAO.insertPraise(actPraise);
			praise = new ActPraise();
			praise = actPraiseDAO.findActPraise(actPraise);
			praise.setFlag(ResultUtils.SUCCESS);
			return praise;
		}
		if(praise.getIsLow()==1){
			praise.setFlag(ResultUtils.EXISTED_PRAISE);
			return praise;
		}
		actPraiseDAO.updatePraise(actPraise);
		praise = actPraiseDAO.findActPraise(actPraise);
		praise.setFlag(ResultUtils.SUCCESS);
		return praise;
	}
	/**
	 * 
	 * <p>Title: cancelPraise</p> 
	 * <p>Description: 取消赞</p> 
	 * @author :changjiang
	 * date 2014-7-30 上午11:36:04
	 * @return
	 */
	public ActPraise cancelPraise(ActPraise actPraise){
		ActPraise praise = new ActPraise();
		praise = actPraiseDAO.findActPraise(actPraise);
		if(0==praise.getId()){
			praise = new ActPraise();
			praise.setFlag(ResultUtils.DATAISNULL);
			return praise;
		}
		if(praise.getIsPraise()==0){
			praise.setFlag(ResultUtils.EXISTED_CANCEL_PRAISE);
			return praise;
		}
		actPraiseDAO.updatePraise(actPraise);
		praise = actPraiseDAO.findActPraise(actPraise);
		praise.setFlag(ResultUtils.SUCCESS);
		return praise;
	}
	
	/**
	 * 
	 * <p>Title: addOnePublishInfo</p> 
	 * <p>Description: 增加一条发布信息</p> 
	 * @author :changjiang
	 * date 2014-8-10 下午4:40:41
	 * @return
	 */
	public int addOnePublishInfo(ActPublish publish){
		return ActPublishDAO.insertPublishInfo(publish);
	}
	
	public List<ActPublish> findPublishIdList(Long resId){
		return ActPublishDAO.findPublishList(resId);
	}
	
	/**
	 * 
	 * <p>Title: findPublishById</p> 
	 * <p>Description: 根据ID查询一条发布信息</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午3:09:05
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishById(long resId){
		return ActPublishDAO.findPublishById(resId);
	}
	
	/**
	 * 
	 * <p>Title: updatePublishById</p> 
	 * <p>Description: 更新一条发布信息</p> 
	 * @author :changjiang
	 * date 2014-8-15 下午3:51:56
	 * @param publish
	 * @return
	 */
	public int updatePublishById(ActPublish publish){
		return ActPublishDAO.updatePublishById(publish);
	}
	
	/**
	 * 
	 * <p>Title: findAllTransmitList</p> 
	 * <p>Description: 查询所有转发信息</p> 
	 * @author :changjiang
	 * date 2014-8-18 上午1:03:00
	 * @param resId
	 * @return
	 */
	public List<ActTransmit> findAllTransmitList(Long resId) {
		return actTransmitDAO.findAllTransmitList(resId);
	}
	
	/**
	 * 
	 * <p>Title: findOnePublish</p> 
	 * <p>Description: 查询一条发布详情</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午2:37:33
	 * @param id
	 * @return
	 */
	public ActPublish findOnePublish(long id){
		return ActPublishDAO.findOnePublish(id);
	} 
	
	/**
	 * 
	 * <p>Title: findOneTransmit</p> 
	 * <p>Description: 查询一条转发详情</p> 
	 * @author :changjiang
	 * date 2014-8-18 下午3:04:03
	 * @param id
	 * @return
	 */
	public ActTransmit findOneTransmit(long id){
		return actTransmitDAO.findOneTransmit(id);
	}
	
	/**
	 * 
	 * <p>Title: findPublishListByUid</p> 
	 * <p>Description: 根据用户ID查询该用户发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-19 下午7:14:55
	 * @param userId
	 * @param resId
	 * @return
	 */
	public  List<ActPublish> findPublishListByUid(long userId, Long resId){
		return ActPublishDAO.findPublishListByUid(userId, resId);
	}
	
	/**
	 * 
	 * <p>Title: findActPraise</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-8-20 下午2:06:06
	 * @param actPraise
	 * @return
	 */
	public ActPraise findActPraise(ActPraise actPraise){
		return actPraiseDAO.findActPraise(actPraise);
	}
	
	/**
	 * 
	 * <p>Title: findPublishListByType</p> 
	 * <p>Description: 根据type查询发布列表</p> 
	 * @author :changjiang
	 * date 2014-8-27 上午12:36:46
	 * @param type
	 * @param resId
	 * @return
	 */
	public List<ActPublish> findPublishListByType(String type, Long resId){
		return ActPublishDAO.findPublishListByType(type, resId);
	}
	
	/**
	 * 
	 * <p>Title: doSubscribe</p> 
	 * <p>Description: 添加订阅信息</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午3:17:31
	 * @param actSubscribe
	 * @return
	 */
	public ActSubscribe doSubscribe(ActSubscribe actSubscribe){
		long userId = actSubscribe.getUserId();
		long resourceId = actSubscribe.getResourceId();
		//查询订阅信息是否存在
		ActSubscribe subscribe = actSubscribeDAO.findSubscribeIsExist(userId, resourceId);
		int flag = subscribe.getFlag();
		//不存在信息时插入一条订阅信息
		if(ResultUtils.DATAISNULL==flag){
			actSubscribeDAO.insertSubscribe(actSubscribe);
			subscribe = actSubscribeDAO.findSubscribeIsExist(userId, resourceId);
		}else if(ResultUtils.SUCCESS==flag){
			//已经订阅过该信息
			int isSubscribe = subscribe.getIsSubscribe();
			if(1==isSubscribe){
				subscribe.setFlag(ResultUtils.IS_SUBSCRIBED);
			}else{
				actSubscribeDAO.updateSubscribe(actSubscribe);
				subscribe = actSubscribeDAO.findSubscribeIsExist(userId, resourceId);
			}
		}
		return subscribe;
	}
	
	/**
	 * 
	 * <p>Title: cancelSubscribe</p> 
	 * <p>Description: 取消订阅</p> 
	 * @author :changjiang
	 * date 2014-9-23 下午8:31:49
	 * @param actSubscribe
	 * @return
	 */
	public ActSubscribe cancelSubscribe(ActSubscribe actSubscribe){
		long userId = actSubscribe.getUserId();
		long resourceId = actSubscribe.getResourceId();
		ActSubscribe subscribe = actSubscribeDAO.findSubscribeIsExist(userId, resourceId);
		int flag = subscribe.getFlag();
		if(ResultUtils.DATAISNULL==flag){
			return subscribe;
		}
		int isSubscribe = subscribe.getIsSubscribe();
		if(0==isSubscribe){
			subscribe.setFlag(ResultUtils.IS_CANCELED_SUBSCRIBE);
		}else{
			subscribe.setUserId(subscribe.getUserId());
			subscribe.setResourceId(subscribe.getResourceId());
			actSubscribeDAO.updateSubscribe(actSubscribe);
			subscribe = actSubscribeDAO.findSubscribeIsExist(userId, resourceId);
		}
		return subscribe;
	}
	
	/**
	 * 
	 * <p>Title: findSubscribeListByResId</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-9-24 上午10:37:04
	 * @param resId
	 * @return
	 */
	public List<ActSubscribe> findSubscribeListByResId(long resId){
		return actSubscribeDAO.findSubscribeListByResId(resId);
	}
	
	/**
	 * 
	 * <p>Title: findSubscribeIsExist</p> 
	 * <p>Description: 查询订阅信息</p> 
	 * @author :changjiang
	 * date 2014-9-24 下午4:08:59
	 * @param userId
	 * @param resId
	 * @return
	 */
	public ActSubscribe findSubscribeIsExist(long userId,long resId){
		return actSubscribeDAO.findSubscribeIsExist(userId, resId);
	}
	
	/**
	 * 
	 * <p>Title: findSubscribeList</p> 
	 * <p>Description: 查询订阅列表</p> 
	 * @author :changjiang
	 * date 2014-9-24 下午4:25:35
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<ActSubscribe> findSubscribeList(long userId,String type){
		return actSubscribeDAO.findSubscribeList(userId, type);
	}
	
	/**
	 * 
	 * <p>Title: findPublishListByUsersId</p> 
	 * <p>Description: 根据用户id查询发布信息</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午12:08:55
	 * @param type
	 * @param resId
	 * @param usersId
	 * @return
	 */
	public List<ActPublish> findPublishListByUsersId(String type, Long resId,
			List<Long> usersId){
		return ActPublishDAO.findPublishListByUsersId(type, resId, usersId);
	}
	
	/**
	 * 
	 * <p>Title: findTransmitListByTypeAndUsersId</p> 
	 * <p>Description: 根据type和uid查询</p> 
	 * @author :changjiang
	 * date 2014-10-27 下午5:05:25
	 * @param type
	 * @param resId
	 * @param usersId
	 * @return
	 */
	public List<ActTransmit> findTransmitListByTypeAndUsersId(String type,
			Long resId, List<Long> usersId){
		return actTransmitDAO.findTransmitListByTypeAndUsersId(type, resId, usersId);
	}
	
	/**
	 * 
	 * <p>Title: findPublishListByRecommendType</p> 
	 * <p>Description: 查询精品类型</p> 
	 * @author :changjiang
	 * date 2014-11-25 下午6:58:10
	 * @param recommendType
	 * @return
	 */
	public List<ActPublish> findPublishListByRecommendType(String recommendType){
		return ActPublishDAO.findPublishListByRecommendType(recommendType);
	}
	
	/**
	 * 
	 * <p>Title: findPublishCount</p> 
	 * <p>Description: 查询发布的总数</p> 
	 * @author :changjiang
	 * date 2014-12-9 下午8:43:02
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findPublishCount(long userId){
		return ActPublishDAO.findPublishCount(userId);
	}
	
	/**
	 * 
	 * <p>Title: findTransmitCountByUid</p> 
	 * <p>Description: 查询转发总数</p> 
	 * @author :changjiang
	 * date 2014-12-10 下午3:24:29
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findTransmitCountByUid(long userId){
		return actTransmitDAO.findTransmitCountByUid(userId);
	}
	
	/**
	 * 
	 * <p>Title: cancelPublish</p> 
	 * <p>Description: 取消发布的资源</p> 
	 * @author :changjiang
	 * date 2015-2-5 下午12:55:13
	 * @param userId
	 * @param resId
	 * @param type
	 * @return
	 */
	public ActPublish cancelPublish(long id) {
		ActPublish actPublish = ActPublishDAO.findOnePublish(id);
				//findPublishByUidAndResIdAndType(userId, resId, type);
		int flag =ResultUtils.ERROR;
		//当存在这条发布信息的时候
		if(null!=actPublish){
			actPublish.setIsDelete(1);
			flag = ActPublishDAO.updatePublishById(actPublish);
		}
		actPublish = ActPublishDAO.findOnePublish(id);
		if(1==actPublish.getIsDelete()){
			actPublish.setFlag(ResultUtils.SUCCESS);
		}else{
			actPublish.setFlag(ResultUtils.ERROR);
		}
		return actPublish;
	}
	
	/**
	 * 
	 * <p>Title: findPraiseListByResIdAndType</p> 
	 * <p>Description: 根据资源的id和类型查询列表</p> 
	 * @author :changjiang
	 * date 2015-2-25 下午5:03:13
	 * @param resId
	 * @param id
	 * @return
	 */
	public List<ActPraise> findPraiseListByResIdAndType(long resId,Long id){
		return actPraiseDAO.findPraiseListByResIdAndType(resId, id);
	}
	
	/**
	 * 
	 * <p>Title: findUserCommentCenter</p> 
	 * <p>Description: 查询当前用户的评论中心</p> 
	 * @author :changjiang
	 * date 2015-3-9 下午7:37:56
	 * @param userId
	 * @param id
	 * @return
	 */
	public List<ActComment> findUserCommentCenter(long userId, Long id){
		return actCommentDAO.findUserCommentCenter(userId, id);
	}
	
	/**
	 * 
	 * <p>Title: findCmtById</p> 
	 * <p>Description: 根据id查询评论</p> 
	 * @author :changjiang
	 * date 2015-3-10 上午10:30:21
	 * @param id
	 * @return
	 */
	public ActComment findCmtById(long id){
		return actCommentDAO.findCmtById(id);
	}
	
	/**
	 * 
	 * <p>Title: findPraiseListByResUid</p> 
	 * <p>Description: 根据用户id查询用户的点赞列表</p> 
	 * @author :changjiang
	 * date 2015-3-11 下午8:17:22
	 * @param resUid
	 * @param id
	 * @return
	 */
	public List<ActPraise> findPraiseListByResUid(long resUid, Long id){
		return actPraiseDAO.findPraiseListByResUid(resUid, id);
	}
	
	
	//以下是微信用户逼格相关功能代码==========================
	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入评论信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertWeixinComment(ActWeixinComment actWeixinComment){
		return actWeixinCommentDAO.insertWeixinComment(actWeixinComment);
	}
	
	/**
	 * 
	 * <p>Title: findComment</p> 
	 * <p>Description: 查找评论列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param userId
	 * @return
	 */
	public List<ActWeixinComment> findWeixinComment(String sopendid){
		return actWeixinCommentDAO.findWeixinComment(sopendid);
	}
	
	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 插入微信用户信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public int insertUser(ActWeixinUser actWeixinUser){
		return actWeixinUserDAO.insertUser(actWeixinUser);
	}
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findCountByScore(int score){
		return actWeixinUserDAO.findCountByScore(score);
	}
	
	
	/**
	 * 
	 * <p>Title: findCommentCount</p> 
	 * <p>Description: 查询总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param resourceId
	 * @return
	 */
	public long findUserCount(){
		return actWeixinUserDAO.findUserCount();
	}
	
	/**
	 * 
	 * <p>Title: findCommentById</p> 
	 * <p>Description: 查询微信用户信息</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActWeixinUser findUserById(String openid){
		return actWeixinUserDAO.findUserById(openid);
	}
	
	/**
	 * 
	 * <p>Title: updateComment</p> 
	 * <p>Description: 示例类</p> 
	 * @author :weizhensong
	 * date 2014-7-30 上午9:53:26
	 * @return
	 */
	public int updateUser(ActWeixinUser actWeixinUser){
		return actWeixinUserDAO.updateUser(actWeixinUser);
	}
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
	public int existUserComment(String openid, String sopenid,String comment){
		return actWeixinCommentDAO.existUserComment(openid, sopenid, comment);
	}
	
	/**
	 * 
	 * <p>Title: findUserCollectCount</p> 
	 * <p>Description: 查询用户的收藏总数</p> 
	 * @author :changjiang
	 * date 2015-3-31 下午2:59:39
	 * @param userId
	 * @return
	 */
	public Map<String, Object> findUserCollectCount(long userId){
		return actCollectDAO.findUserCollectCount(userId);
	}
	
	/**
	 * 
	 * <p>Title: findUserCollectedList</p> 
	 * <p>Description: 查询用户的收藏列表</p> 
	 * @author :changjiang
	 * date 2015-4-7 上午11:08:19
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<ActCollect> findUserCollectedList(long userId, Long resId){
		return actCollectDAO.findUserCollectedList(userId, resId);
	}
	
	
	/**
	 * 
	 * <p>Title: insertintoActAt</p> 
	 * <p>Description: 插入at信息</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:20:19
	 * @return
	 */
	public ActAt insertintoActAt(ActAt actAt){
		int flag = actAtDAO.insertintoActAt(actAt);
		if(flag == ResultUtils.SUCCESS){
			return actAtDAO.findAtById(actAt.getId());
		}else{
			actAt = new ActAt();
			actAt.setFlag(flag);
			return actAt;
		}
	}
	
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个资源相关的at列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param resourceid
	 * @return
	 */
	public List<ActAt> findResAt(long resourceid,Long id){
		return actAtDAO.findResAt(resourceid, id);
	}
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个人at别人的列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param userid
	 * @return
	 */
	public List<ActAt> findUserAt(long userid,Long id){
		return actAtDAO.findUserAt(userid, id);
	}
	/**
	 * 
	 * <p>Title: findResAt</p> 
	 * <p>Description: 查找某个人被at的列表</p> 
	 * @author :weizhensong
	 * date 2014-7-27 上午12:29:00
	 * @param atUserid
	 * @return
	 */
	public List<ActAt> findAtUser(long atUserid,Long id){
		return actAtDAO.findAtUser(atUserid, id);
	}
	/**
	 * 
	 * <p>Title: findResAtCount</p> 
	 * <p>Description: 查询某个资源相关的at的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param resourceid
	 * @return
	 */
	public int findResAtCount(long resourceid){
		return actAtDAO.findResAtCount(resourceid);
	}
	/**
	 * 
	 * <p>Title: findUserAtCount</p> 
	 * <p>Description: 查询某个人at别人的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param userid
	 * @return
	 */
	public int findUserAtCount(long userid){
		return actAtDAO.findUserAtCount(userid);
	}
	/**
	 * 
	 * <p>Title: findAtUserCount</p> 
	 * <p>Description: 查询某个人被at的总数</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午4:50:07
	 * @param atUserid
	 * @return
	 */
	public int findAtUserCount(long atUserid){
		return actAtDAO.findAtUserCount(atUserid);
	}
	
	/**
	 * 
	 * <p>Title: findAtById</p> 
	 * <p>Description: 查询at信息根据id</p> 
	 * @author :weizhensong
	 * date 2014-7-28 下午8:30:16
	 * @return
	 */
	public ActAt findAtById(long id){
		return actAtDAO.findAtById(id);
	}
	
	/**
	 * 
	 * <p>Title: deleteActAt</p> 
	 * <p>Description: 示例类</p> 
	 * @author :weizhensong
	 * date 2014-7-30 上午9:53:26
	 * @return
	 */
	public ActAt deleteActAt(ActAt actAt){
		ActAt at = actAtDAO.findAtById(actAt.getId());
		int flag = actAtDAO.deleteActAt(actAt);
		if(flag == ResultUtils.SUCCESS){
			at.setFlag(flag);
			at.setIsDelete(1);
			return at;
		}
		actAt.setFlag(flag);
		return actAt;
	}
	
	/**
	 * 
	 * <p>Title: findPublishListByUserId</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-6-5 下午2:56:33
	 * @param type
	 * @param resId
	 * @param userId
	 * @return
	 */
	public List<ActPublish> findPublishListByUserId(String type, Long resId,
			Long userId){
		return ActPublishDAO.findPublishListByUserId(type, resId, userId);
	}
	
	/**
	 * 
	 * <p>Title: doUseful</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2015-6-9 下午5:36:32
	 * @param resId
	 * @param resType
	 * @param resUserid
	 * @param status
	 * @param userid
	 * @return
	 */
	public ActUseful doUseful(long resId,String resType,long resUserid,int status,long userid){
		//查询这个用户是否已经点过有用或者没用
		ActUseful actUseful = actUsefulDAO.findUsefulByResidAndUserid(resId, userid);
		int flag = actUseful.getFlag();
		long id = actUseful.getId();
		long systime = System.currentTimeMillis();
		//如果没有这条数据插入这条数据
		if(ResultUtils.DATAISNULL==flag){
			actUseful = new ActUseful();
			id = reskeyWork.getId();
			actUseful.setId(id);
			actUseful.setUserId(userid);
			actUseful.setResourceId(resId);
			actUseful.setIsUseful(status);
			actUseful.setResUserId(resUserid);
			actUseful.setResType(resType);
			actUseful.setCreateDate(systime);
			actUseful.setLatestRevisionDate(systime);
			actUsefulDAO.insertUseful(actUseful);
		}else if(ResultUtils.SUCCESS==flag) {//有这条资源更新状态
			actUsefulDAO.updateUseful(id,status,systime);
		}
		//如果有这条数据更新这条数据
		//返回这条数据的详情
		actUseful = actUsefulDAO.findUserfulById(id);
		return actUseful;
	}
	
	/**
	 * 
	 * <p>Title: findUsefulCount</p> 
	 * <p>Description: 查询有用的数量</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:49:17
	 * @param resId
	 * @return
	 */
	public Map<String, Object> findUsefulCount(long resId){
		return actUsefulDAO.findUsefulCount(resId);
	}
	
	/**
	 * 
	 * <p>Title: findUselessCount</p> 
	 * <p>Description: 查询没用的总数</p> 
	 * @author :changjiang
	 * date 2015-6-10 下午4:50:01
	 * @param resId
	 * @return
	 */
	public Map<String, Object> findUselessCount(long resId){
		return actUsefulDAO.findUselessCount(resId);
	}
	
	/**
	 * 
	 * <p>Title: delCommentById</p> 
	 * <p>Description: 根据id删除评论</p> 
	 * @author :changjiang
	 * date 2015-6-12 上午11:16:21
	 * @param id
	 * @return
	 */
	public int delCommentById(long id){
		return actCommentDAO.delCommentById(id);
	}
	
	/**
	 * 
	 * <p>Title: findUsefulByResidAndUserid</p> 
	 * <p>Description: 根据用户id和资源id查询是否有用</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午12:26:57
	 * @param resId
	 * @param userId
	 * @return
	 */
	public ActUseful findUsefulByResidAndUserid(long resId,long userId){
		return actUsefulDAO.findUsefulByResidAndUserid(resId, userId);
	}
	
	/**
	 * 
	 * <p>Title: findUsefulListByResUid</p> 
	 * <p>Description: 根据用户的id和查找收到的有用</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午5:56:03
	 * @param userid
	 * @param lastId
	 * @return
	 */
	public List<ActUseful> findUsefulListByResUid(long userid, Long lastId){
		return actUsefulDAO.findUsefulListByResUid(userid, lastId);
	}
	
	/**
	 * 
	 * <p>Title: findUsefulListByResIdAndType</p> 
	 * <p>Description: 查询有用的列表</p> 
	 * @author :changjiang
	 * date 2015-6-15 下午7:33:27
	 * @param resId
	 * @param id
	 * @return
	 */
	public List<ActUseful> findUsefulListByResIdAndType(long resId, Long id){
		return actUsefulDAO.findUsefulListByResIdAndType(resId, id);
	}
	
	/**
	 * 
	 * <p>Title: doHot</p> 
	 * <p>Description: 加热</p> 
	 * @author :changjiang
	 * date 2015-7-9 上午11:20:10
	 * @param userId
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public ActHot doHot(long userId,long resourceId,String type,String ipAddress){
		//改动 根据ip查询
		ActHot actHot = actHotDAO.findIsHotByUserIdAndResIdType(userId,resourceId,type);
				//findIsHotByIpAddressAndResIdType(ipAddress,resourceId,type);
		int flag = actHot.getFlag();
		if(flag==ResultUtils.SUCCESS){
			//上次时间
			long latestTime = actHot.getLatestRevisionDate();
			//当前时间
			long systime = System.currentTimeMillis();
//			if(systime-latestTime>HOT_INTERVAL){//当前时间大于两个小时时可以点赞
//				flag = ResultUtils.DATAISNULL;
//			}else{
//				flag = ResultUtils.EXISTED_IS_VOTE;
//				actHot.setFlag(flag);
//			}

			if(DateUtil.compareDateNow(latestTime,systime)){//当时间相同时不能再投票
				flag = ResultUtils.EXISTED_IS_VOTE;
				actHot.setFlag(flag);
			}else{
				flag = ResultUtils.DATAISNULL;
			}
		}
		
		if(flag == ResultUtils.DATAISNULL){//插入一条资源
			ActHot insertActHot = new ActHot();
			long systime = System.currentTimeMillis();
			long id = reskeyWork.getId();
			insertActHot.setId(id);
			insertActHot.setUserId(userId);
			insertActHot.setResourceId(resourceId);
			insertActHot.setType(type);
			insertActHot.setIsHot(1);
			insertActHot.setCreateDate(systime);
			insertActHot.setLatestRevisionDate(systime);
			insertActHot.setIpAddress(ipAddress);
			actHotDAO.insertHot(insertActHot);
			actHot = actHotDAO.findActHotById(id);
		}
		return actHot;
	}
	
	/**
	 * 
	 * <p>Title: findHotCount</p> 
	 * <p>Description: 查找热度总数</p> 
	 * @author :changjiang
	 * date 2015-7-9 下午3:41:52
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public Map<String, Object> findHotCount(long resourceId, String type){
		return actHotDAO.findHotCount(resourceId, type);
	}


//	public static void main(String[] args) {
//		long time = 1444708943581l;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date dt = new Date(time);
//		Date dt1 = new Date(System.currentTimeMillis());
//		//dt.getDay();
////		Calendar cal = Calendar.getInstance();
////		int i = cal.get(Calendar.DAY_OF_YEAR);
////		System.out.println(i);
//		String str = sdf.format(dt);
//		String str1 = sdf.format(dt1);
//		System.out.println(str);
//		System.out.println(str1);
//		System.out.println(str.equals(str1));
//	}
}
