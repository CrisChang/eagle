package com.poison.resource.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.manager.BookManager;
import com.poison.eagle.manager.SensitiveManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.BkCommentService;
import com.poison.resource.service.BookListService;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.BkFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.NetBook;

public class BkCommentFacadeImpl implements BkCommentFacade{
	private static final  Log LOG = LogFactory.getLog(BkCommentFacadeImpl.class);
	private BkCommentService bkCommentService;
	private BookListService bookListService;
	private UKeyWorker reskeyWork;
	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	private SensitiveManager sensitiveManager;
	private GetResourceInfoFacade getResourceInfoFacade;
	private NetBookFacade netBookFacade;
	private BkFacade bkFacade;
	
	
	public void setSensitiveManager(SensitiveManager sensitiveManager) {
		this.sensitiveManager = sensitiveManager;
	}

	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}

	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}

	public void setBookListService(BookListService bookListService) {
		this.bookListService = bookListService;
	}

	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setBkCommentService(BkCommentService bkCommentService) {
		this.bkCommentService = bkCommentService;
	}

	@Override
	public BkComment addOneBkComment(long userId,int bookId,String comment,String score,int isOpposition,int isDb,String resType,Long resId,String scan,String lon,String lat,String locationName,String locationCity,String locationArea,String level,String title,String cover,String resourceType) {
		
		long sysdate = System.currentTimeMillis();
		List<BookList> bookList = new ArrayList<BookList>();
		BookList list = new BookList();
		//获取默认书单
		if(null==resId){
			bookList = bookListService.findDefaultBookList(userId);
			list.setBookListName("我读过的书");
			list.setuId(userId);
			if(null==bookList||bookList.size()==0){
				//bookListService.
				list.setId(reskeyWork.getId());
				list.setReason("");
				list.setType(0);
				list.setIsDel(0);
				list.setTag("");
				list.setIsPublishing(1);
				list.setCreateDate(sysdate);
				list.setLatestRevisionDate(sysdate);
				bookListService.addBookList(null, list);
				bookList = bookListService.findDefaultBookList(userId);
			}else{
				list = bookList.get(0);
			}
		}else{//获取指定影单
			list = bookListService.queryByIdBookList(resId);
			String booklistName = list.getBookListName();
			//当为收藏的书单并且不是扫描的书时
			/*if(booklistName.equals(CommentUtils.ACTIVITY_COLLECTED_BOOKLIST)&&"0".equals(scan)){
				BkComment bkcomment = new BkComment();
				bkcomment.setFlag(ResultUtils.BOOK_IS_COLLECTED_BY_SCAN);
				return bkcomment;
			}*/
		}
		//查询书单名称
		BookList bkList = list;// bookListService.queryUserBookListByName(list);
		long bookListId = bkList.getId();
		//向默认书单中添加一本书
		BookListLink bookListLink = new BookListLink();
		bookListLink.setId(reskeyWork.getId());
		bookListLink.setBookListId(bookListId);
		bookListLink.setBookId(bookId);
		bookListLink.setIsDel(0);
		bookListLink.setIsDb(isDb);
		bookListLink.setResType(resType);
		bookListLink.setCreateDate(sysdate);
		bookListLink.setLatestRevisionDate(sysdate);
		BookListLink bookLink = null;//bookListService.findBookLinkIsExist(bookListLink);
		//当书单中不存在这本书时插入书单
		if(null==bookLink){
			bookListService.addBookToList(bookListLink);
			//修改书单封面
			try {
				String bkPic = "";
				//区分图书和网络小说
				if(CommentUtils.TYPE_BOOK.equals(resType)){
					BkInfo bkInfo = bkFacade.findBkInfo(bookId);
					bkPic= bkInfo.getBookPic();
				}else if(CommentUtils.TYPE_NETBOOK.equals(resType)){
					NetBook netBook = netBookFacade.findNetBookInfoById(bookId);
					bkPic = netBook.getPagePicUrl();
				}
				String bookListPic = bkList.getCover();
				if(bkPic == null){
					bkPic = "";
				}
				if("".equals(bookListPic)){
					getResourceInfoFacade.updateBookListPic(bookListId, bkPic);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			
			
			
		}
		//插入一条评论
		BkComment bkComment = new BkComment();
		long bkCommentId = reskeyWork.getId();
		bkComment.setId(bkCommentId);
		bkComment.setUserId(userId);
		bkComment.setBookId(bookId);
		//去除敏感词汇
		//comment = sensitiveManager.checkSensitive(comment, userId, bkCommentId, CommentUtils.TYPE_BOOK_COMMENT);
		bkComment.setComment(comment);
		bkComment.setScore(score);
		bkComment.setIsOpposition(isOpposition);
		bkComment.setCreateDate(sysdate);
		bkComment.setResType(resType);
		bkComment.setLatestRevisionDate(sysdate);
		bkComment.setIsDb(isDb);
		bkComment.setType(level);
		bkComment.setLon(lon);
		bkComment.setLat(lat);
		bkComment.setLocationName(locationName);
		bkComment.setLocationCity(locationCity);
		bkComment.setLocationArea(locationArea);
		bkComment.setTitle(title);
		bkComment.setCover(cover);
		bkComment.setResourceType(resourceType);
		return bkCommentService.addOneBkComment(bkComment);
	}

	/**
	 * 查询一本书的所有评论
	 */
	@Override
	public List<BkComment> findAllBkComment(int bookId, Long resId,String resType,String resourceType) {
		return bkCommentService.findAllBkComment(bookId, resId,resType,resourceType);
	}
	/**
	 * 查询所有书的评论
	 */
	@Override
	public List<BkComment> findBkCommentListForOld(int bookId,Long resId,String resType,String resourceType){
		return bkCommentService.findBkCommentListForOld(bookId, resId, resType, resourceType);
	}
	/**
	 *  根据id集合查询
	 */
	@Override
	public List<BkComment> findBkCommentListByIds(List<Long> ids){
		return bkCommentService.findBkCommentListByIds(ids);
	}
	/**
	 * 查询所有评论
	 */
	@Override
	public List<BkComment> findAllComment(Long resId) {
		return bkCommentService.findAllComment(resId);
	}

	/**
	 * 查询自己对一本书的所有评论
	 */
	@Override
	public List<BkComment> findMyBkCommentList(long userId, int bookId,
			Long resId,String resType) {
		return bkCommentService.findMyBkCommentList(userId, bookId, resId,resType);
	}

	/**
	 * 查询一本书的所有评论
	 */
	@Override
	public int findCommentCount(int bookId,String resType) {
		return bkCommentService.findCommentCount(bookId,resType);
	}

	/**
	 * 更新一本书的评论
	 */
	@Override
	public BkComment updateMyCommentByBook(long id, String score,String comment,String title,String cover) {
		long sysdate = System.currentTimeMillis();
		BkComment bkComment = new BkComment();
		bkComment.setId(id);
		bkComment.setScore(score);
		bkComment.setComment(comment);
		bkComment.setLatestRevisionDate(sysdate);
		bkComment.setTitle(title);
		bkComment.setCover(cover);
		return bkCommentService.updateMyCommentByBook(bkComment);
	}

	/**
	 * 查询一条评论详情
	 */
	@Override
	public BkComment findCommentIsExistById(final long id) {
		/*resourceVisitClient.execute(new JedisWorker<Object>(){
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.BKCOMMENT_STATISTIC_MARK+id+ResStatisticConstant.BKCOMMENT_STATISTIC_TYPE;
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
					resStatistic.setType(CommentUtils.TYPE_BOOK_COMMENT);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});*/
		return bkCommentService.findCommentIsExistById(id);
	}

	/**
	 * 根据用户ID查询该用户的评论列表
	 */
	@Override
	public List<BkComment> findCommentListByUserId(long userId, Long resId) {
		return bkCommentService.findCommentListByUserId(userId, resId);
	}

	/**
	 * 增加一个评分
	 */
	@Override
	public BkAvgMark addBkAvgMark(int bkId,String type, float bkAvgMark) {
		BkAvgMark avgMark = new BkAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setId(reskeyWork.getId());
		avgMark.setBkId(bkId);
		if(type==null){
			type = CommentUtils.TYPE_BOOK;
		}
		avgMark.setResType(type);
		avgMark.setBkAvgMark(bkAvgMark);
		avgMark.setBkTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return bkCommentService.addBkAvgMark(avgMark);
	}

	/**
	 * 修改一个评分
	 */
	@Override
	public BkAvgMark updateBkAvgMark(int bkId, float bkAvgMark,
			float beforeScore) {
		BkAvgMark avgMark = new BkAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setBkId(bkId);
		avgMark.setBkAvgMark(bkAvgMark);
		avgMark.setBkTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return bkCommentService.updateBkAvgMark(avgMark, beforeScore);
	}

	/**
	 * 查询一本书的评分
	 */
	@Override
	public BkAvgMark findBkAvgMarkByBkId(int bkId) {
		return bkCommentService.findBkAvgMarkByBkId(bkId);
	}
	/**
	 * 根据书的id集合查询和书的类型查询评分信息
	 */
	@Override
	public List<BkAvgMark> findBkAvgMarkByBkIds(List<Long> bkids,String type){
		return bkCommentService.findBkAvgMarkByBkIds(bkids, type);
	}
	/**
	 * 删除一条评论
	 */
	@Override
	public BkComment deleteMyCommentById(long id) {
		return bkCommentService.deleteMyCommentById(id);
	}

	/**
	 * 根据type查询书评列表
	 */
	@Override
	public List<BkComment> findAllBkCommentListByType(Integer userId, String type,
			Long resId) {
		return bkCommentService.findAllBkCommentListByType(userId, type, resId);
	}

	/**
	 * 增加一个类型的书评
	 */
	@Override
	public BkComment addOneBkCommentByType(long userId, int bookId,
			String comment, String score, String type,String resType) {
		long sysdate = System.currentTimeMillis();
		//获取默认书单
		List<BookList> bookList = bookListService.findDefaultBookList(userId);
		BookList list = new BookList();
		list.setBookListName("我读过的书");
		list.setuId(userId);
		if(null==bookList||bookList.size()==0){
			list.setId(reskeyWork.getId());
			list.setReason("");
			list.setType(0);
			list.setIsDel(0);
			list.setTag("");
			list.setIsPublishing(1);
			list.setCreateDate(sysdate);
			list.setLatestRevisionDate(sysdate);
			bookListService.addBookList(null, list);
			bookList = bookListService.findDefaultBookList(userId);
		}
		//查询书单名称
		BookList bkList = bookListService.queryUserBookListByName(list);
		long bookListId = bkList.getId();
		//向默认书单中添加一本书
		BookListLink bookListLink = new BookListLink();
		bookListLink.setId(reskeyWork.getId());
		bookListLink.setBookListId(bookListId);
		bookListLink.setBookId(bookId);
		bookListLink.setIsDel(0);
		bookListLink.setIsDb(0);
		bookListLink.setResType(resType);
		bookListLink.setCreateDate(sysdate);
		bookListLink.setLatestRevisionDate(sysdate);
		BookListLink bookLink = bookListService.findBookLinkIsExist(bookListLink);
		//当书单中不存在这本书时插入书单
		if(null==bookLink){
			bookListService.addBookToList(bookListLink);
		}
		BkComment bkComment = new BkComment();
		long bkCommentId = reskeyWork.getId();
		bkComment.setId(bkCommentId);
		bkComment.setUserId(userId);
		bkComment.setBookId(bookId);
		//去除敏感词汇
		//comment = sensitiveManager.checkSensitive(comment, userId, bkCommentId, CommentUtils.TYPE_BOOK_COMMENT);
		bkComment.setComment(comment);
		bkComment.setScore(score);
		bkComment.setType(type);
		bkComment.setResType(resType);
		bkComment.setIsOpposition(0);
		bkComment.setCreateDate(sysdate);
		bkComment.setLatestRevisionDate(sysdate);
		bkComment.setIsDb(0);
		return bkCommentService.addOneBkComment(bkComment);
	}

	/**
	 * 删除一本书的评分
	 */
	@Override
	public BkAvgMark deleteBkAvgMark(int bkId, float bkAvgMark,
			float beforeScore) {
		BkAvgMark avgMark = new BkAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setBkId(bkId);
		avgMark.setBkAvgMark(bkAvgMark);
		avgMark.setBkTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return bkCommentService.deleteBkAvgMark(avgMark, beforeScore);
	}

	/**
	 * 增加书评的逼格值
	 */
	@Override
	public BkComment addBkCommentBigValue(long id, float bigValue) {
		return bkCommentService.updateBkCommentBigValue(id, bigValue);
	}

	/**
	 * 根据userid查询评论总数
	 */
	@Override
	public Map<String, Object> findBkCommentCount(long userId) {
		return bkCommentService.findBkCommentCount(userId);
	}

	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	/**
	 * 增加专家的评分
	 */
	@Override
	public BkAvgMark addExpertsBkAvgMark(int bkId, float expertsAvgMark) {
		BkAvgMark avgMark = new BkAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setId(reskeyWork.getId());
		avgMark.setBkId(bkId);
		avgMark.setExpertsAvgMark(expertsAvgMark);
		avgMark.setExpertsTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return bkCommentService.addExpertsBkAvgMark(avgMark);
	}

	/**
	 * 修改神人的书评分
	 */
	@Override
	public BkAvgMark updateBkExpertsAvgMark(int bkId, float expertsAvgMark,
			float beforeScore) {
		BkAvgMark avgMark = new BkAvgMark();
		long sysdate = System.currentTimeMillis();
		avgMark.setBkId(bkId);
		avgMark.setExpertsAvgMark(expertsAvgMark);
		avgMark.setExpertsTotalNum(1);
		avgMark.setIsDelete(0);
		avgMark.setLatestRevisionDate(sysdate);
		return bkCommentService.updateBkExpertsAvgMark(avgMark, beforeScore);
	}

	/**
	 * 根据用户id查询用户长书评
	 */
	@Override
	public List<BkComment> findUserLongBkCommentListByUserId(long userId,
			Long resId) {
		return bkCommentService.findUserLongBkCommentListByUserId(userId, resId);
	}
	/**
	 * 根据时间段查询用户的书评信息
	 */
	@Override
	public List<BkComment> findMyBkCommentListByTime(long userId,Long starttime,Long endtime){
		return bkCommentService.findMyBkCommentListByTime(userId, starttime, endtime);
	}
	/**
	 * 查询是否存在某个用户的书评记录
	 */
	@Override
	public long findBkCommentRecord(long userid){
		return bkCommentService.findBkCommentRecord(userid);
	}
}
