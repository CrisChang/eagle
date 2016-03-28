package com.poison.eagle.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActPublish;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.BookTalkInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MyBk;
import com.poison.store.client.BkFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.NetBook;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;

public class BookUtils {
	private static final  Log LOG = LogFactory.getLog(BookUtils.class);
	private static BookUtils bookUtils;

	public BookUtils() {
	}

	public static BookUtils getInstance() {
		if (bookUtils == null) {
			return new BookUtils();
		} else {
			return bookUtils;
		}
	}

	private FileUtils fileUtils = FileUtils.getInstance();

	/**
	 * 将根据用户标签查询的运营书单json转换为书单列表
	 * @param json
	 * @return
	 */
	public List<BookList> putJsonListToBookLists(List<Map<String, Object>> json){
		List<BookList> bookLists = new ArrayList<BookList>();
		
		Iterator<Map<String, Object>> iter = json.iterator();
		while(iter.hasNext()){
			Map<String, Object> bl = iter.next();
			BookList bookList = new BookList();
			bookList = putJsonToBookList(bl);
			if(bookList != null && bookList.getId() != 0){
				bookLists.add(bookList);
			}
		}
		
		
		return bookLists;
	}
	
	/**
	 * 将根据用户标签查询的运营书单json转换为书单
	 * @param json
	 * @return
	 */
	public BookList putJsonToBookList(Map<String, Object> json){
		BookList bookList = new BookList();
		try {
			if(json.containsKey("id")){
				Long id = Long.valueOf(json.get("id").toString());
				bookList.setId(id);
			}
			if(json.containsKey("u_id")){
				Long uid = Long.valueOf(json.get("u_id").toString());
				bookList.setuId(uid);
			}
			if(json.containsKey("cover")){
				String cover = (String) json.get("cover");
				bookList.setCover(cover);
			}
			if(json.containsKey("booklist_name")){
				String booklist_name = (String) json.get("booklist_name");
				bookList.setBookListName(booklist_name);
			}
			if(json.containsKey("reason")){
				String reason = (String) json.get("reason");
				bookList.setReason(reason);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			bookList = new BookList();
		}
//		List<String> tags = (List<String>) json.get("tags_a");
		
		
		return bookList;
	}
	/**
	 * 将根据用户标签查询的运营书单json转换为书评列表
	 * @param json
	 * @return
	 */
	public List<BkComment> putJsonListToBkComments(List<Map<String, Object>> json){
		List<BkComment> bkComments = new ArrayList<BkComment>();
		
		Iterator<Map<String, Object>> iter = json.iterator();
		while(iter.hasNext()){
			Map<String, Object> bl = iter.next();
			BkComment bkComment = new BkComment();
			bkComment = putJsonToBkComment(bl);
			bkComments.add(bkComment);
		}
		
		
		return bkComments;
	}
	
	/**
	 * 将根据用户标签查询的运营书单json转换为书单
	 * @param json
	 * @return
	 */
	public BkComment putJsonToBkComment(Map<String, Object> json){
		BkComment bkComment = new BkComment();// = new BookList();
		
		Long id = Long.valueOf(json.get("id").toString());
		Long uid = Long.valueOf(json.get("user_id").toString());
		int bookId = Integer.valueOf(json.get("book_id").toString());
		String score = (String) json.get("score");
		String comment = (String) json.get("comment");
//		List<String> tags = (List<String>) json.get("tags_a");
		
		bkComment.setId(id);
		bkComment.setUserId(uid);
		bkComment.setBookId(bookId);
		bkComment.setScore(score);
		bkComment.setComment(comment);
		
		return bkComment;
	}
	/**
	 * 将书摘转化为可读类
	 * 
	 * @param bookTalks
	 * @return
	 */
	public List<BookTalkInfo> putBookTalkToBookTalkInfo(List<BookTalk> bookTalks,UcenterFacade ucenterFacade) {
		List<BookTalkInfo> bookTalkInfos = new ArrayList<BookTalkInfo>();
		BookTalkInfo bookTalkInfo = new BookTalkInfo();
		//根据page正序排列
//		Collections.reverse(bookTalks);
		int count = 0;
		if (bookTalks.size() == 0) {
			return bookTalkInfos;
		} else {
			BookTalk bookTalk = bookTalks.get(0);
			if (bookTalk.getId() != 0) {
				// 可以执行转换操作
				for (int i = 0; i < bookTalks.size(); i++) {
					BookTalk bookTalk2 = bookTalks.get(i);
					if (bookTalkInfo.getPage() == bookTalk2.getPage()) {
						count++;
						// 如果page相同则继续添加
						bookTalkInfo.getBookTalk().add(
								putBookTalkToBook(bookTalk2,ucenterFacade));
					} else {
						// 否则重新初始化
						bookTalkInfo = new BookTalkInfo();
						bookTalkInfos.add(bookTalkInfo);
						bookTalkInfo.setPage(bookTalk2.getPage());
						bookTalkInfo.getBookTalk().add(
								putBookTalkToBook(bookTalk2,ucenterFacade));
						
					}
				}

			}

		}
		return bookTalkInfos;
	}

	public Map<String, Object> putBookTalkToBook(BookTalk bookTalk,UcenterFacade ucenterFacade) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", bookTalk.getId());
		map.put("list", WebUtils.putHTMLToData(bookTalk.getContent()));

		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, bookTalk.getUid());
		UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, 1);
		map.put("userEntity", userEntity);
		return map;
	}
	/**
	 * 将书的表类型转换为资源类型
	 * @param bkInfo
	 * @return
	 */
	public ResourceInfo putBookInfoToResource(BookInfo bookInfo){
		ResourceInfo ri = new ResourceInfo();
		
		if(bookInfo == null || bookInfo.getId() == 0){
			return ri;
		}
		
//		BookInfo bookInfo = putBKToBookInfo(bkInfo, 0);
		
		ri.setBookInfo(bookInfo);
		ri.setType(CommentUtils.TYPE_BOOK);
		ri.setRid(bookInfo.getId());
		ri.setImageUrl(bookInfo.getPagePic());
		return ri;
	}
	/**
	 * 将书的表类型转换为资源类型
	 * @param bkInfo
	 * @return
	 */
	public ResourceInfo putBkInfoToResource(BkInfo bkInfo){
		ResourceInfo ri = new ResourceInfo();
		
		if(bkInfo == null || bkInfo.getId() == 0){
			return ri;
		}
		
		BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, 0);
		
		ri = putBookInfoToResource(bookInfo);
		return ri;
	}
	
	/**
	 * 将推送的书单格式化
	 * @param actPublish
	 * @param getResourceInfoFacade
	 * @param ucenterFacade
	 * @param actFacade
	 * @param bkFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActPublishBookListToResource(ActPublish actPublish,GetResourceInfoFacade getResourceInfoFacade,UcenterFacade ucenterFacade,ActFacade actFacade,
			BkFacade bkFacade,long uid) {
		ResourceInfo ri = new ResourceInfo();
		
		long bookListId = actPublish.getResourceId();
		
		BookList bookList = getResourceInfoFacade.queryByIdBookList(bookListId);
		
		//已废弃
//		ri = putBookListToResource(bookList, getResourceInfoFacade, ucenterFacade, actFacade, bkFacade, uid);
		ri = fileUtils.putObjectToResource(bookList, ucenterFacade, actFacade);
		ri.setRid(actPublish.getId());
		ri.setBtime(actPublish.getPublishDate());
		return ri;
	}

	/**
	 * 将书单转换成资源类(已废弃)
	 * 
	 * @param bookList
	 * @param ri
	 * @param getResourceInfoFacade
	 * @param bkFacade
	 * @return
	 */
	public ResourceInfo putBookListToResource(BookList bookList,
			GetResourceInfoFacade getResourceInfoFacade,UcenterFacade ucenterFacade,ActFacade actFacade,
			BkFacade bkFacade,long uid) {
		ResourceInfo ri = new ResourceInfo();

//		ri.setTitle(bookList.getBookListName());
//		ri.setBookListId(bookList.getId());
		
		ri = fileUtils.putObjectToResource(bookList, ucenterFacade, actFacade);
		
		
		
		List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(bookList.getId(), null,null);

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// 将书单中第一本书放入资源类中
		if (bookListLinks.size() > 0) {
			BookListLink bookListLink = bookListLinks.get(0);
			BookInfo bookInfo = new BookInfo();
			
			BookListLink bookListLink2 = getResourceInfoFacade.findUserIsCollectBook(uid, bookInfo.getId(), bookListLink.getResType());//(uid, bookInfo.getId());
			if(bookListLink2.getId() != 0){
				bookInfo.setInList(0);
			}

			BkInfo bkInfo = bkFacade.findBkInfo(bookListLink.getBookId());
			bookInfo = fileUtils.putBKToBookInfo(bkInfo, 0);
			ri.setBookInfo(bookInfo);
			//废弃
			/*String imageUrl = ri.getImageUrl();
			if(imageUrl == null || "".equals(imageUrl)){
				ri.setImageUrl(bookInfo.getPagePic());
			}*/
		}
		return ri;
	}

//	/**
//	 * 将图书转化为客户端可以显示的类型
//	 * 
//	 * @param bkInfo
//	 *            实例类
//	 * @param isList
//	 *            0：列表、1：单本书
//	 * @return
//	 */
//	public BookInfo putBKToBookInfo(Object obj, int isList) {
//		String objName = obj.getClass().getName();
//		BookInfo bookInfo = new BookInfo();
//		
//		if(BkInfo.class.getName().equals(objName)){
//			BkInfo bkInfo = (BkInfo) obj;
//			
//			bookInfo.setId(bkInfo.getId());
//			bookInfo.setName(bkInfo.getName());
//			bookInfo.setAuthorName(bkInfo.getAuthorName());
//			//bookInfo.setIsDB(TRUE);
//			bookInfo.setPagePic(bkInfo.getBookPic());
//			bookInfo.setType(bkInfo.getResType());
//			
//			if(isList == 1){
//				bookInfo.setPress(bkInfo.getPress());
//				bookInfo.setScore(bkInfo.getScore());
//				bookInfo.setIntroduction(bkInfo.getContent());
//				bookInfo.setPublishTime(bkInfo.getPublishingTime());
//				bookInfo.setSales(bkInfo.getSalesVolume());
//				bookInfo.setRank(bkInfo.getRankingList());
//				
//				//出版信息
//				bookInfo.setPages(bkInfo.getNumber());
//				bookInfo.setBinding(bkInfo.getBinding());
//				bookInfo.setCatalog(bkInfo.getCatalog());
//				bookInfo.setIsbn13(bkInfo.getIsbn());
//				bookInfo.setPrice(bkInfo.getPrice());
//			}
//		}else if(NetBook.class.getName().equals(objName)){
//			NetBook netBook = (NetBook) obj;
//			
//			bookInfo.setId((int)netBook.getId());
//			bookInfo.setName(netBook.getName());
//			bookInfo.setAuthorName(netBook.getAuthorName());
//			bookInfo.setPagePic(netBook.getPagePicUrl());
//			bookInfo.setType(netBook.getResType());
//			if(isList == 1){
//				bookInfo.setIntroduction(netBook.getIntroduction());
//			}
//		}
//		return bookInfo;
//	}
	/**
	 * 将图书转化为客户端可以显示的类型(我的书单我的评论)
	 * @param obj
	 * @param isList  是否是列表查询。0：标示列表查询，不携带多余信息、1：表示图书详情需要将全部信息格式化
	 * @param uid 当前用户id
	 * @param bkCommentFacade
	 * @param ucenterFacade
	 * @return
	 */
	public  BookInfo putBKToBookInfo(Object obj , int isList, long uid,
			BkCommentFacade bkCommentFacade,UcenterFacade ucenterFacade){
		String objName = obj.getClass().getName();
		BookInfo bookInfo = new BookInfo();
		
		bookInfo = fileUtils.putBKToBookInfo(obj, isList);
		
		
		List<BkComment> bkComments = bkCommentFacade.findMyBkCommentList(uid, bookInfo.getId(), null, bookInfo.getType());//(uid,bookInfo.getId(), null);
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		if(bkComments.size() > 0){
			ResourceInfo resourceInfo = fileUtils.putObjectToResource(bkComments.get(0), ucenterFacade);
			bookInfo.setResourceInfo(resourceInfo);
		}
		
		return bookInfo;
	}
	
	/**
	 * 将图书转化为客户端可以显示的类型(首页)(去除我的评论)
	 * @param obj
	 * @param isList  是否是列表查询。0：标示列表查询，不携带多余信息、1：表示图书详情需要将全部信息格式化
	 * @param bkCommentFacade
	 * @param ucenterFacade
	 * @return
	 */
	public  BookInfo putBKToBookInfoIndex(Object obj , int isList, Long uid,
			BkCommentFacade bkCommentFacade,UcenterFacade ucenterFacade){
		String objName = obj.getClass().getName();
		BookInfo bookInfo = new BookInfo();
		
		bookInfo = fileUtils.putBKToBookInfo(obj, isList);
		
		
		
//		//图书全部评论
//		List<BkComment> bkComments = bkCommentFacade.findAllBkComment(bookInfo.getId(), null,bookInfo.getType());
//		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
//		//我的评论列表
//		List<BkComment> myComments = bkCommentFacade.findMyBkCommentList(uid, bookInfo.getId(), null,bookInfo.getType());
//		bkComments.removeAll(myComments);
//		
//		ResourceInfo resourceInfo =null;
//		//将我的评论单独放进去
//		if(myComments.size()>0){
//			resourceInfo = fileUtils.putObjectToResource(myComments.get(0), ucenterFacade);
//			bookInfo.setResourceInfo(resourceInfo);
//		}
//		if(bkComments.size()>0){
//			for (BkComment bkComment : bkComments) {
//				if(bkComment != null && bkComment.getId() != 0){
//					resourceInfo = fileUtils.putObjectToResource(bkComment, ucenterFacade);
//					resourceInfos.add(resourceInfo);
//				}
//			}
//			bookInfo.setCommentList(resourceInfos);
//		}
//		else if(bkComments.size() == 1){
//			BkComment bkComment = bkComments.get(0);
//			if(bkComment.getId() != 0 ){
//				resourceInfo = fileUtils.putObjectToResource(bkComment, ucenterFacade);
//				resourceInfos.add(resourceInfo);
//			}
//		}
		
//		bookInfo.setCommentList(resourceInfos);
		
		//添加书的平均分和评论数
		BkAvgMark bkAvgMark = bkCommentFacade.findBkAvgMarkByBkId(bookInfo.getId());
		if(bkAvgMark.getFlag() == ResultUtils.SUCCESS || bkAvgMark.getFlag() == 0){
			bookInfo.setScore(String.valueOf(bkAvgMark.getBkAvgMark()));
			bookInfo.setcNum(bkAvgMark.getBkTotalNum());
			bookInfo.setTalentScore(String.valueOf(bkAvgMark.getExpertsAvgMark()));
			bookInfo.setTalentCommentNum(bkAvgMark.getExpertsTotalNum());
		}
//		//书评数目
//		int cNum = bkCommentFacade.findCommentCount(bookInfo.getId());
//		bookInfo.setcNum(cNum);
		
		return bookInfo;
	}

	/**
	 * 将书评类转换为首页resoureinfo实例类
	 * 
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param bkFacade
	 * @return
	 */
	public ResourceInfo putBkCommentToResource(BkComment bkComment,
			UcenterFacade ucenterFacade, ActFacade actFacade, BkFacade bkFacade,NetBookFacade netBookFacade) {
		ResourceInfo ri = new ResourceInfo();
		if(bkComment == null){
			return ri;
		}
		// 调用本类公用方法
		ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade);

		BookInfo bookInfo = new BookInfo();

		if(CommentUtils.TYPE_NETBOOK.equals(bkComment.getResType())){
			NetBook netBook = netBookFacade.findNetBookInfoById(bkComment.getBookId());
			bookInfo = fileUtils.putBKToBookInfo(netBook, 1);
		}else{
			BkInfo bkInfo = bkFacade.findBkInfo(bkComment.getBookId());
			bookInfo = fileUtils.putBKToBookInfo(bkInfo, 1);
		}

		ri.setBookInfo(bookInfo);
		return ri;
	}

	/**
	 * 将书单中的资源转化为资源类
	 * 
	 * @param bookListLink
	 * @param ucenterFacade
	 * @param actFacade
	 * @param bkFacade
	 * @param bkCommentFacade
	 * @return
	 */
	public ResourceInfo putBkListLinkToResource(BookListLink bookListLink,
			UcenterFacade ucenterFacade, ActFacade actFacade,
			BkFacade bkFacade, BkCommentFacade bkCommentFacade,GetResourceInfoFacade getResourceInfoFacade,
			NetBookFacade netBookFacade,MyBkFacade myBkFacade,BookList bookList ,Long uid) {
		
		ResourceInfo ri = new ResourceInfo();
		if(bookListLink == null || bookListLink.getId() == 0){
			return ri;
		}
		BookInfo bookInfo = new BookInfo();
		if(CommentUtils.TYPE_NETBOOK.equals(bookListLink.getResType())){
			NetBook netBook = netBookFacade.findNetBookInfoById(bookListLink.getBookId());
			bookInfo = fileUtils.putBKToBookInfo(netBook, 0);
		}else{
			BkInfo bkInfo = bkFacade.findBkInfo(bookListLink.getBookId());
			bookInfo = fileUtils.putBKToBookInfo(bkInfo, 0);
		}
		if(bookInfo == null || bookInfo.getId() ==0){
			return ri;
		}
		
//		List<BkComment> bkComments = bkCommentFacade.findAllBkComment(bookListLink.getBookId(), null);
//		BookList bookList = getResourceInfoFacade.queryByIdBookList(bookListLink.getBookListId());
//		
		List<BkComment> bkComments = bkCommentFacade.findMyBkCommentList(bookList.getuId(), bookListLink.getBookId(), null, bookListLink.getResType());
//		List<BkComment> bkComments = bkCommentFacade.findMyBkCommentList(bookList.getuId(), bookListLink.getBookId(), null);
		if(bkComments.size()>0 && bkComments.get(0).getId() != 0){
			//ri = putBkCommentToResource(bkComments.get(0), ucenterFacade, actFacade, bkFacade);
			ri = fileUtils.putObjectToResource(bkComments.get(0), ucenterFacade, actFacade, bkFacade,myBkFacade, getResourceInfoFacade, netBookFacade, uid);
		}else{
			ri = fileUtils.putObjectToResource(bookInfo, ucenterFacade, actFacade);
			BkAvgMark bkAvgMark = bkCommentFacade.findBkAvgMarkByBkId(bookInfo.getId());
			ri.setScore(String.valueOf(bkAvgMark.getBkAvgMark()));
			ri.setContList(WebUtils.putHTMLToData(bookListLink.getDescription()));
			ri.setType(CommentUtils.TYPE_BOOK_COMMENT);
	//		ri.setScore(bookInfo.getScore());
			ri.setBookInfo(bookInfo);
		}
		
		if("0".equals(ri.getScore()) || "0.0".equals(ri.getScore())){
			ri.setScore(bookInfo.getScore());
		}
		
//		ri.setTitle(bookListLink.getDescription());
//		ri.setRid(bookListLink.getId());
//		ri.setFriendName(bookListLink.getFriendinfo());
//		ri.setAddress(bookListLink.getAddress());
//		ri.setRemark(bookListLink.getDescription());
		ri.setLinkId(bookListLink.getId());
		
		return ri;
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		BookTalkInfo info = new BookTalkInfo();
//		book/98/s9855156.jpg
		for (Integer i : list) {
			if (info.getPage() != i) {
				info.setPage(i);
				info.getBookTalk().add(i);
			}
		}

	}

}
