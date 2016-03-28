package com.poison.resource.client.impl;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.framework.runtime.ProductContext;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeServiceUtil;
import com.poison.eagle.utils.StringUtils;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.ResCollectNum;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.BookListService;
import com.poison.resource.service.MyBkService;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.BkFacade;
import com.poison.store.model.BkInfo;
import com.poison.ucenter.client.impl.UcenterFacadeImpl;
import com.poison.ucenter.model.TalentZone;

public class GetResourceInfoFacadeImpl implements GetResourceInfoFacade {
	//private BookList booklist=new BookList();
	private static final  Log LOG = LogFactory.getLog(UcenterFacadeImpl.class);
	
	private BookListService bookListService;
	private MyBkService myBkService;
	private BkFacade bkFacade;
	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	
	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	private UKeyWorker reskeyWork;

	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	public void setMyBkService(MyBkService myBkService) {
		this.myBkService = myBkService;
	}
	public void setBookListService(BookListService bookListService) {
		this.bookListService = bookListService;
	}

	private int flag = ResultUtils.ERROR;
	
	/*
	 * 插入单本书(库中无)
	 */
	/*@Override
	public int addNoBook(ProductContext productContext,long uid,String name, String reason) {
		return bookListService.singleBookList(productContext, uid, name, reason);
	}*/
	/**
	 * 新建书单
	 */
	/*@Override
	public int addNewBookList(ProductContext productContext,long uid,String name, String reason, String content,
			List<Map> list) {
		    UKeyWorker rId=new UKeyWorker(0, 1);
		    long rid=rId.getId();
			flag=bookListService.addBookList(productContext, rid, uid, name, reason, content);
			if(flag==ResultUtils.SUCCESS){
				for (int i = 0; i <list.size(); i++) {
					Map map=list.get(i);
					String isDB=(String) map.get("isDB");
					String bname = (String) map.get("name");
					if(CommentUtils.REQ_ISON_TRUE.equals(isDB)){
						long bookId = Long.valueOf(map.get("id").toString());
						flag =bookListService.addBook(productContext, bookId,uid, rid, bname, "", content);
					}else{
					    flag = bookListService.singleList(productContext,uid,rid, bname, "");
					}
				}
			}
		return flag;
	}*/
	/**
	 * 在老书单中添加一本书书
	 */
	/*@Override
	public int addOldBookList(ProductContext productContext,long uid,long id, String name, String reason, String content,
			List<Map> list) {
			for (int i = 0; i < list.size(); i++) {
		 		Map map = list.get(i);
				String isDB = (String) map.get("isDB");
				String bname = (String) map.get("name");
				if (CommentUtils.REQ_ISON_TRUE.equals(isDB)) {
					//添加单本书的方法
					long bookId = Long.valueOf(map.get("id").toString());
					flag = bookListService.addBook(productContext, bookId, uid, id, bname, "", content);
				}else{
					flag = bookListService.singleList(productContext, uid, id, bname, "");
				}
			}
		return flag;
	}*/
	/**
	 * 该方法的作用主要根据name模糊
	 */
/*	@Override
	public List<Book> findBook(ProductContext productContext,String name) {
		return bookListService.findBook(productContext,name);
	}*/
	/**
	 * 插入单本书(库中有)
	 */
/*	@Override
	public int addHaveBook(ProductContext productContext, long uid, long id,
			long rid, String name, String reason, String content) {
		return bookListService.addBook(productContext, uid, id, rid, name, reason, content);
	}*/
	/**
	 * 根据用户id查询当前用户书单信息
	 */
	@Override
	public List<BookList> findBookList(ProductContext productContext, long uid) {
		List<BookList> bookList = new ArrayList<BookList>();
		//List<BookList> bookList = bookListService.findBookList(productContext, uid);
		List<BookList> defaultBookList = bookListService.findDefaultBookList(uid);
		if(null==defaultBookList||defaultBookList.size()<5){
			//我看过的书
			addDefaultBookList(productContext,uid);
			//我想看的书
			addWantReadBookList(productContext,uid);
			//我正在看的书
			addIsReadingBookList(productContext,uid);
			//我的书架藏书
			addIsCollectedBookList(productContext,uid);
			//我不喜欢看的书
			addNoLikeBookList(productContext, uid);
		}
		bookList = bookListService.findBookList(productContext, uid);
		return bookList;
	}
	/**
	 * 此方法的作用是修改书单
	 */
	@Override
	public int updateBookList(long id, String name,String reason,String cover,String tag) {
		BookList bookList = bookListService.queryByIdBookList(id);
		if(null==bookList){
			return ResultUtils.DATAISNULL;
		}
		long sysdate = System.currentTimeMillis();
		bookList.setId(id);
		bookList.setBookListName(name);
		bookList.setReason(reason);
		bookList.setCover(cover);
		bookList.setTag(tag);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.updateBookList(bookList);
	}
	
	/**
	 * 此方法的作用是删除书单
	 */
	@Override
	public int deleteBookList(long id) {
		BookList bookList = bookListService.queryByIdBookList(id);
		if(null==bookList){
			return ResultUtils.DATAISNULL;
		}
		if(bookList.getIsDel()==1){
			return ResultUtils.BOOKLIST_IS_ALREADY_DELETE;
		}
		long sysdate = System.currentTimeMillis();
		bookList.setIsDel(1);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.updateBookList(bookList);
	}
	
	/**
	 * 发布书单
	 */
	@Override
	public int publishBookList(long id) {
		BookList bookList = bookListService.queryByIdBookList(id);
		if(null==bookList){
			return ResultUtils.DATAISNULL;
		}
		//已经发布
		if(bookList.getIsPublishing()==0){
			return ResultUtils.BOOKLIST_IS_ALREADY_PUBLISH;
		}
		long sysdate = System.currentTimeMillis();
		bookList.setIsPublishing(1);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.updateBookList(bookList);
	}
	
	/**
	 * 根据时间查询书单最新信息
	 */
	@Override
	public List<BookList> findLatestBookList(Long resId) {
		return bookListService.findLatestBookList(resId);
	}
	
	/**
	 * 根据ID查询书单最新信息
	 */
	/*@Override
	public List<BookList> findLatestBookListById(long resourceId) {
		return bookListService.findLatestBookListById(resourceId);
	}*/

	
	/**
	 * 根据用户ID查询书单列表
	 */
	/*@Override
	public List<BookList> findBookListByUsersId(List<Long> usersIdList,Long resId) {
		return bookListService.findBookListByUsersId(usersIdList,resId);
	}*/

	/**
	 * 此方法作用是根据id查询一个书单
	 */
	@Override
	public BookList queryByIdBookList(final long id) {
		BookList bookList = bookListService.queryByIdBookList(id);
		/*resourceVisitClient.execute(new JedisWorker<Object>(){
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.BKLIST_STATISTIC_MARK+id+ResStatisticConstant.BKLIST_STATISTIC_TYPE;
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
					resStatistic.setType(CommentUtils.TYPE_BOOK);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});*/
		if(null==bookList){
			bookList = new BookList();
			bookList.setFlag(ResultUtils.DATAISNULL);
		}
		return bookList;
	}
	
	/**
	 * 增加一个新书单
	 */
	@Override
	public int addNewBookList(ProductContext productContext,
			String bookListName, String reason, long uId,String tag) {
		BookList bookList = new BookList();
		long sysdate = System.currentTimeMillis();
		bookList.setId(reskeyWork.getId());
		bookList.setBookListName(bookListName);
		bookList.setReason(reason);
		bookList.setType(1);
		bookList.setTag(tag);
		bookList.setuId(uId);
		bookList.setIsDel(0);
		bookList.setIsPublishing(1);
		bookList.setCreateDate(sysdate);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.addBookList(productContext, bookList);
	}
	
	/**
	 * 增加一个默认书单
	 */
	@Override
	public int addDefaultBookList(ProductContext productContext, long uId) {
		BookList bookList = new BookList();
		long sysdate = System.currentTimeMillis();
		bookList.setId(reskeyWork.getId());
		bookList.setBookListName("我读过的书");
		bookList.setReason("");
		bookList.setType(0);
		bookList.setTag("");
		bookList.setuId(uId);
		bookList.setIsDel(0);
		bookList.setIsPublishing(1);
		bookList.setCreateDate(sysdate);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.addBookList(productContext, bookList);
	}

	/**
	 * 向一个书单中添加书
	 */
	@Override
	public int addBookToList(long bookListId, final int bookId,int isDb,final String resType) {
		resourceVisitClient.execute(new JedisWorker<Object>(){

			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.BOOK_COLLECTED_MARK+bookId+"#"+resType+ResStatisticConstant.BOOK_COLLECTED_TYPE;
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
					resCollectNum.setResId(bookId);
					resCollectNum.setType(CommentUtils.TYPE_BOOK);
					resCollectNum.setFalseCollectNum(falseCollected);
					resCollectNum.setIsCollectedNum(collectedNumber);
					resCollectNum.setLatestRevisionDate(sysdate);
					resStatisticService.insertResCollectNum(resCollectNum);
				}
				return jedis.hgetAll(key);
			}
			
		});
		BookListLink bookListLink = new BookListLink();
		long sysdate =System.currentTimeMillis();
		bookListLink.setId(reskeyWork.getId());
		bookListLink.setBookListId(bookListId);
		bookListLink.setBookId(bookId);
		bookListLink.setIsDel(0);
		bookListLink.setIsDb(isDb);
		bookListLink.setResType(resType);
		bookListLink.setCreateDate(sysdate);
		bookListLink.setLatestRevisionDate(sysdate);
		BookListLink bookLink = bookListService.findBookLinkIsExist(bookListLink);
		if(null==bookLink){
			return bookListService.addBookToList(bookListLink);
		}
		//BkInfo bkInfo = bkFacade.findBkInfo(bookId);
		return ResultUtils.BOOK_IS_ALREADY_EXIST;
	}

	/**
	 * 查询书单详情
	 */
	@Override
	public List<BookListLink> findBookListInfo(long bookListId, Long resId,Integer pageSize) {
		return bookListService.findBookListInfo(bookListId, resId,pageSize);
	}
	
	/**
	 * 一个书单中删除一本书的
	 */
	@Override
	public int deleteBookListLink(long bookListId,long bookId,String resType) {
		BookListLink bookLink = bookListService.findBookLinkById(bookListId,bookId,resType);
		//没有这条数据
		if(null==bookLink){
			return ResultUtils.DATAISNULL;
		}
		//已经删除该本书
		if(bookLink.getIsDel()==1){
			return ResultUtils.BOOK_DELETE_FROM_BOOKLIST;
		}
		long sysdate = System.currentTimeMillis();
		bookLink.setIsDel(1);
		bookLink.setLatestRevisionDate(sysdate);
		return bookListService.updateBookListLink(bookLink);
	}
	
	/**
	 * 书单中添加自己建的书
	 */
	@Override
	public int addMyBookToList(String name,long bookListId,long userId,String resType) {
		MyBk myBk = new MyBk();
		long sysdate = System.currentTimeMillis();
		myBk.setBookUrl("");
		myBk.setCreateId(userId);
		myBk.setBookPic("");
		myBk.setName(name);
		myBk.setScore("");
		myBk.setAuthorName("");
		myBk.setTranslator("");
		myBk.setPress("");
		myBk.setOriginalName("");
		myBk.setSubtitle("");
		myBk.setPublishingTime("");
		myBk.setNumber(0);
		myBk.setPrice("");
		myBk.setBinding("");
		myBk.setSeriesName("");
		myBk.setTags("");
		myBk.setContent("");
		myBk.setAuthorInfo("");
		myBk.setCatalog("");
		myBk.setSeriesInfo("");
		myBk.setIsbn("");
		myBk.setCollTime(sysdate);
		MyBk bk = myBkService.findMyBkIsExist(myBk);
		if(null!=bk){
			return ResultUtils.BOOK_IS_ALREADY_EXIST;
		}
		int id = myBkService.addMyBkInfo(myBk);
		return addBookToList(bookListId,id,1,resType);
	}
	
	/**
	 * 查询用户的默认书单
	 */
	@Override
	public List<BookList> findDefaultBookList(long userId) {
		return bookListService.findDefaultBookList(userId);
	}
	
	/**
	 * 添加系统推荐的书
	 */
	@Override
	public int addServerBookList(String bookListName, String reason,String tag) {
		BookList bookList = new BookList();
		long sysdate = System.currentTimeMillis();
		bookList.setId(reskeyWork.getId());
		bookList.setBookListName(bookListName);
		bookList.setReason(reason);
		bookList.setType(2);
		bookList.setTag(tag);
		bookList.setuId(0);
		bookList.setIsDel(0);
		bookList.setIsPublishing(1);
		bookList.setCreateDate(sysdate);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.addBookList(null, bookList);
	}
	
	/**
	 * 查询系统推荐的书单
	 */
	@Override
	public List<BookList> findServerBookLists(final String tags, final Long resId) {
		List<BookList> bookList = new ArrayList<BookList>();
		/*try{
			bookList = jedisSimpleClient.execute(new JedisWorker<List<BookList>>(){
				@Override
				public List<BookList> work(Jedis jedis) {
					byte[] bookListByte = jedis.get(("ServerBookLists"+tags).getBytes());
					List<BookList> bookListRds = (List<BookList>) SerializeServiceUtil.unserialize(bookListByte);
					if(null==bookListRds||bookListRds.size()==0){
						bookListRds = new ArrayList<BookList>();
						bookListRds = bookListService.findServerBookLists(tags, resId);
						jedis.set(("ServerBookLists"+tags).getBytes(), SerializeServiceUtil.serialize(bookListRds));
					}
					return bookListRds;
				}
				
			});
		}catch (Exception e) {
			LOG.error("client,GetResourceInfoFacadeImpl,findServerBookLists,error:"+tags+":"+e.getMessage());*/
			bookList = new ArrayList<BookList>();
			bookList = bookListService.findServerBookLists(tags, resId);
		//}
		return bookList;
	}
	
	/**
	 * 添加想读的书单
	 */
	@Override
	public int addWantReadBookList(ProductContext productContext, long uId) {
		BookList bookList = new BookList();
	
		long sysdate = System.currentTimeMillis();
		bookList.setId(reskeyWork.getId());
		bookList.setBookListName("我想看的书");
		bookList.setReason("");
		bookList.setType(0);
		bookList.setTag("");
		bookList.setuId(uId);
		bookList.setIsDel(0);
		bookList.setIsPublishing(1);
		bookList.setCreateDate(sysdate);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.addBookList(productContext, bookList);
	}
	
	/**
	 * 添加正在读的书单
	 */
	@Override
	public int addIsReadingBookList(ProductContext productContext, long uId) {
		BookList bookList = new BookList();
		long sysdate = System.currentTimeMillis();
		bookList.setId(reskeyWork.getId());
		bookList.setBookListName("我正在看的书");
		bookList.setReason("");
		bookList.setType(0);
		bookList.setTag("");
		bookList.setuId(uId);
		bookList.setIsDel(0);
		bookList.setIsPublishing(1);
		bookList.setCreateDate(sysdate);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.addBookList(productContext, bookList);
	}
	
	/**
	 * 查询用户是否收藏了这本书
	 */
	@Override
	public BookListLink findUserIsCollectBook(long uId, long bookId,String resType) {
		BookListLink bkListLink = new BookListLink();
		//查询用户的书单列表
		List<BookList> bkList = bookListService.findBookList(null, uId);
		BookList bk = new BookList();
		if(null==bkList||bkList.size()==0){
			bkListLink.setFlag(ResultUtils.BKLIST_IS_NOT_EXISTED);
			return bkListLink;
		}
		bkListLink.setFlag(ResultUtils.DATAISNULL);
		//根据书单查询这本书是否存在书单中
		Iterator<BookList> bkListIt = bkList.iterator();
		while(bkListIt.hasNext()){
			bkListLink.setBookId((int)bookId);
			bk = bkListIt.next();
			long bkListId = bk.getId();
			bkListLink.setBookListId(bkListId);
			bkListLink.setResType(resType);
			bkListLink = bookListService.findBookLinkIsExist(bkListLink);
			//当这个书在书单中是
			if(null!=bkListLink&&bkListLink.getId()!=0){
				return bkListLink;
			}
				bkListLink = new BookListLink();
			
		}
		return bkListLink;
	}
	
	/**
	 * 移动一本书
	 */
	@Override
	public BookListLink moveOneBook(long bookId, long bookListId,long userId,String resType,String scan) {
		BookList bookList = bookListService.queryByIdBookList(bookListId);
		BookListLink bookListLink = new BookListLink();
		int flag = ResultUtils.ERROR;
		//书单为空
		if(null==bookList){
			bookListLink.setFlag(ResultUtils.BKLIST_IS_NOT_EXISTED);
			return bookListLink;
		}
		//查询用户的默认书单
		/*List<BookList> list = findDefaultBookList(userId);
		Iterator<BookList> listIt = list.iterator();
		List<BookListLink> bkLinkList = new ArrayList<BookListLink>();
		BookListLink bkListLink = new BookListLink();
		//当要移动的为默认书单时删除
		if(0==bookList.getType()){
			while(listIt.hasNext()){
				BookList bkListIt = listIt.next();
				long bkListId = bkListIt.getId();
				bkLinkList = bookListService.findBookListInfo(bkListId, null);
				Iterator<BookListLink> linkListIt = bkLinkList.iterator();
				while(linkListIt.hasNext()){
					bkListLink = linkListIt.next();
					long bListId = bkListLink.getBookListId();
					long bkId = bkListLink.getBookId();
					if(bkListLink.getBookId()==bookId){
						deleteBookListLink(bListId,bkId,resType);
					}
				}
			}
		}*/
		//当插入的这个书单为活动书单时，并且为不为扫描的书时
		/*String booklistName = bookList.getBookListName();
		if(booklistName.equals(CommentUtils.ACTIVITY_COLLECTED_BOOKLIST)&&"1".equals(scan)){
			bookListLink.setFlag(ResultUtils.BOOK_IS_COLLECTED_BY_SCAN);
			return bookListLink;
		}*/
		//书单中添加这本书
		flag = addBookToList(bookListId, (int)bookId, 0,resType);
		bookListLink = bookListService.findBookLinkById(bookListId, bookId,resType);
		/*if(booklistName.equals(CommentUtils.ACTIVITY_COLLECTED_BOOKLIST)&&"0".equals(scan)){
			//书单中添加这本书
			flag = addBookToList(bookListId, (int)bookId, 0,resType);
			bookListLink = bookListService.findBookLinkById(bookListId, bookId,resType);
		}*/
		bookListLink.setFlag(flag);
		return bookListLink;
	}
	
	/**
	 * 根据书单id和type的内容筛选
	 */
	@Override
	public List<BookListLink> findBookListByType(long bookListId, String type) {
		List<BookListLink> bookLinkList = bookListService.findBookListInfo(bookListId, null,null);
		Iterator<BookListLink> linkIt = bookLinkList.iterator();
		BookListLink bookListLink = new BookListLink();
		BkInfo book = new BkInfo();
		List<BookListLink> bkInfoList = new ArrayList<BookListLink>();
		int bookId = 0;
		String tags = "";
		while(linkIt.hasNext()){
			bookListLink = linkIt.next();
			bookId = bookListLink.getBookId();
			book = bkFacade.findBkInfo(bookId);
			tags = book.getTags();
			if(StringUtils.isType(tags,type)){
				bkInfoList.add(bookListLink);
			}
		}
		return bkInfoList;
	}
	
	/**
	 * 更新书的备注
	 */
	@Override
	public BookListLink updateBookListLinkRemark(String friendinfo,
			String address, String tags, long id,String description) {
		return bookListService.updateBookListLinkRemark(friendinfo, address, tags, id,description);
	}
	
	/**
	 * 根据id查询图书是否存在
	 */
	@Override
	public BookListLink findBookLinkIsExistById(long id) {
		return bookListService.findBookLinkIsExistById(id);
	}
	
	/**
	 * 更新书单封面
	 */
	@Override
	public BookList updateBookListPic(long id, String cover) {
		return bookListService.updateBookListPic(id, cover);
	}
	
	/**
	 * 添加我的书架藏书
	 */
	@Override
	public int addIsCollectedBookList(ProductContext productContext, long uId) {
		BookList bookList = new BookList();
		long sysdate = System.currentTimeMillis();
		bookList.setId(reskeyWork.getId());
		bookList.setBookListName("我的书架藏书");
		bookList.setReason("");
		bookList.setType(0);
		bookList.setTag("");
		bookList.setuId(uId);
		bookList.setIsDel(0);
		bookList.setIsPublishing(1);
		bookList.setCreateDate(sysdate);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.addBookList(productContext, bookList);
	}
	
	@Override
	public int addNoLikeBookList(ProductContext productContext, long uId) {
		BookList bookList = new BookList();
		long sysdate = System.currentTimeMillis();
		bookList.setId(reskeyWork.getId());
		bookList.setBookListName("我不喜欢的书");
		bookList.setReason("");
		bookList.setType(0);
		bookList.setTag("");
		bookList.setuId(uId);
		bookList.setIsDel(0);
		bookList.setIsPublishing(1);
		bookList.setCreateDate(sysdate);
		bookList.setLatestRevisionDate(sysdate);
		return bookListService.addBookList(productContext, bookList);
	}
	
	/**
	 * 增加图书至活动书单
	 */
	@Override
	public BookListLink addBookToActivityList(String activityListName,long userId,
			int bookId, String resType,String scan) {
		BookList bkList = new BookList();
		bkList.setuId(userId);
		bkList.setBookListName(activityListName);
		BookList bookList = bookListService.queryUserBookListByName(bkList);
		int flag = bookList.getFlag();
		//不存在这个书单的时候,新建该书单
		if(ResultUtils.DATAISNULL==flag){
			addIsCollectedBookList(null, userId);
			bookList = bookListService.queryUserBookListByName(bkList);
		}
		long bookListId = bookList.getId();
		BookListLink BookListLink = moveOneBook(bookId, bookListId,userId,resType,scan);
		return BookListLink;
	}
	@Override
	public long getBookCountByListId(long listid) {
		return bookListService.getBookCountByListId(listid);
	}
	@Override
	public List<BookListLink> getBookListLinkByStartIndex(long listid,
			long start, int pageSize) {
		return bookListService.getBookListLinkByStartIndex(listid, start, pageSize);
	}
	@Override
	public BookList queryUserBookListByName(BookList bookList) {
		return bookListService.queryUserBookListByName(bookList);
	}
	
	/**
	 * 查询书单有多少本书
	 */
	@Override
	public Map<String, Object> findBookLinkCount(long bookListId) {
		return bookListService.findBookLinkCount(bookListId);
	}
	/**
	 * 根据书单id集合查询书单列表
	 * @param bklistids
	 * @return
	 */
	@Override
	public List<BookList> findBookListsByIds(List<Long> bklistids){
		return bookListService.findBookListsByIds(bklistids);
	}
}
