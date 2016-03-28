package com.poison.eagle.utils;

import java.awt.print.Book;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActSubscribe;
import com.poison.act.model.ActTransmit;
import com.poison.act.model.ActUseful;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.MovieListInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.MovieManager;
import com.poison.msg.model.MsgAt;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.paycenter.model.RewardStatistical;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.Post;
import com.poison.resource.model.ResourceLink;
import com.poison.resource.model.Serialize;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.store.model.NetBook;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;

public class ActUtils {
	private static ActUtils actUtils;

	public ActUtils() {
	}

	public static ActUtils getInstance() {
		if (actUtils == null) {
			return new ActUtils();
		} else {
			return actUtils;
		}
	}
	private static final  Log LOG = LogFactory.getLog(ActUtils.class);
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private FileUtils fileUtils = FileUtils.getInstance();
	private BookUtils bookUtils = BookUtils.getInstance();
	private SerializeUtils serializeUtils = SerializeUtils.getInstance();
	

	/**
	 * 将act信息中影评的资源转化为resourceInfo
	 * @param obj
	 * @param ucenterFacade
	 * @param mvCommentFacade
	 * @param mvFacade
	 * @param actFacade
	 * @return
	 */
	public ResourceInfo putActMovieCommentToResourceInfo(Object obj,
			UcenterFacade ucenterFacade, MvCommentFacade mvCommentFacade,
			MvFacade mvFacade, ActFacade actFacade,MyMovieFacade myMovieFacade,long uid) {
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fir = putActToResourceInfo(obj, ucenterFacade);

		MsgAt msgAt = (MsgAt) obj;

		MvComment mvComment = mvCommentFacade.findMvCommentIsExist(msgAt
				.getResourceId());

		ri = movieUtils.putMVCommentToResource(mvComment, ucenterFacade,
				actFacade, mvFacade,myMovieFacade,uid);
		
		fir.setResourceInfo(ri);

		return fir;
	}
	/**
	 * 将act信息中书评的资源转化为resourceInfo
	 * @param obj
	 * @param ucenterFacade
	 * @param mvCommentFacade
	 * @param mvFacade
	 * @param actFacade
	 * @return
	 */
	public ResourceInfo putActBookCommentToResourceInfo(Object obj,
			UcenterFacade ucenterFacade, BkCommentFacade bkCommentFacade,
			ActFacade actFacade,BkFacade bkFacade,MyBkFacade myBkFacade,GetResourceInfoFacade getResourceInfoFacade,NetBookFacade netBookFacade,Long uid) {
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fir = putActToResourceInfo(obj, ucenterFacade);
		
		MsgAt msgAt = (MsgAt) obj;
		
		BkComment bkComment = bkCommentFacade.findCommentIsExistById(msgAt.getResourceId());
		
		ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade, myBkFacade,getResourceInfoFacade,netBookFacade,uid);
		
		fir.setResourceInfo(ri);
		
		return fir;
	}
	
	/**
	 * 将@的父类转化为固定资源类
	 * @param obj
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putActToResourceInfo(Object obj,UcenterFacade ucenterFacade){
		ResourceInfo ri = new ResourceInfo();
		if(obj == null){
			return ri;
		}
		MsgAt msgAt = (MsgAt) obj;

		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, msgAt.getUserId());
		UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, 1);
		ri.setRid(msgAt.getAtId());
		ri.setUserEntity(userEntity);
		ri.setType(CommentUtils.TYPE_ACT);
		ri.setIsRead(CheckParams.changeIntForTrueFalse(msgAt.getIsRead()));
		ri.setBtime(msgAt.getAtDate());
		
		return ri;
	}
	
	
	/**
	 * 将收藏类格式化
	 * @param ri
	 * @param actCollect
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectToResource(ResourceInfo ri , ActCollect actCollect , UcenterFacade ucenterFacade){
		if(ri == null){
			ri = new ResourceInfo();
		}
		ri.setRid(actCollect.getId());
		ri.setBtime(actCollect.getCollectDate());
		
//		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, actCollect.getUserId());
//		
//		UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, 1);
//
//		ri.setUserEntity(userEntity);
		
		return ri;
	}
	/**
	 * 将订阅类格式化
	 * @param ri
	 * @param actCollect
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putSubscribeToResource(ResourceInfo ri , ActSubscribe actSubscribe , UcenterFacade ucenterFacade){
		if(ri == null){
			ri = new ResourceInfo();
		}
		
		ri.setRid(actSubscribe.getId());
		ri.setBtime(actSubscribe.getSubscribeDate());
		
//		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, actSubscribe.getUserId());
//		
//		UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, 1);
//		
//		ri.setUserEntity(userEntity);
		
		return ri;
	}
	
	/**
	 * 将收藏的影单格式化(转为转发格式)
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectMovieListToResource(ActCollect actCollect ,UcenterFacade ucenterFacade , ActFacade actFacade,MyMovieFacade myMovieFacade,MvFacade mvFacade,Long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
		
		MovieList movieList = myMovieFacade.findMovieListById(actCollect.getResourceId());
		
		if(movieList == null || movieList.getIsDel() == CommentUtils.FALSE || movieList.getId() == 0){
			return new ResourceInfo();
//			ri.setIsDel(1);
//			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
//			ri.setType(actCollect.getType());
//			try {
//				ri.setRid(movieList.getId());
//				ri.setMovieListId(movieList.getId());
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			ri = movieUtils.putMovieListToResource(movieList, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
			//影单中电影的数量
			int count = 0;
			List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(actCollect.getResourceId(), null,null);
			Iterator<MvListLink> iter = mvListLinks.iterator();
			while(iter.hasNext()){
				MvListLink mvListLink = iter.next();
				MvInfo mvInfo = mvFacade.queryById(mvListLink.getMovieId());
				if(mvInfo != null && mvInfo.getId() != 0){
					count++;
				}
			}
			ri.setSize(count);
		
		}
		
		fri.setResourceInfo(ri);
		
		
		
//		MovieList movieList = myMovieFacade.findMovieListById(actCollect.getResourceId());	
//		if(movieList == null || movieList.getId() == 0 ){
//			return ri;
//		}
//		ri = movieUtils.putMovieListToResource(movieList, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
//		
//		ri = putCollectToResource(ri, actCollect, ucenterFacade);
		
		return fri;
	}
	/**
	 * 将收藏的影单格式化
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public MovieListInfo putCollectMovieListToInfo(ActCollect actCollect ,UcenterFacade ucenterFacade , ActFacade actFacade,MyMovieFacade myMovieFacade,MvFacade mvFacade,MyBkFacade myBkFacade,Long uid){
		MovieListInfo ri = new MovieListInfo();
		MovieList movieList = myMovieFacade.findMovieListById(actCollect.getResourceId());	
		if(movieList == null || movieList.getId() == 0 ){
			return ri;
		}
		//收藏的默认影单修改名字
		if(movieList.getType() == 0){
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, movieList.getUid());
			if(userAllInfo.getUserId() != 0){
				String movieListName = movieList.getFilmListName();
				movieListName = movieListName.substring(1);
				movieList.setFilmListName(userAllInfo.getName()+movieListName);
			}
		}
		ri = movieUtils.putMVListToMovieList(movieList, myMovieFacade, mvFacade, ucenterFacade,actFacade,myBkFacade,uid);
		ri.setIsDefault(3);
		return ri;
	}
	/**
	 * 将收藏的书单格式化(变为转发结构)
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectBookListToResource(ActCollect actCollect ,UcenterFacade ucenterFacade , 
			ActFacade actFacade, GetResourceInfoFacade getResourceInfoFacade,BkFacade bkFacade,NetBookFacade netBookFacade,long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
		
		BookList bList = getResourceInfoFacade.queryByIdBookList(actCollect.getResourceId());
		
		if(bList == null || bList.getIsDel() == CommentUtils.FALSE || bList.getId() == 0){
			return new ResourceInfo();
//			ri.setIsDel(1);
//			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
//			ri.setType(actCollect.getType());
//			try {
//				ri.setRid(bList.getId());
//				ri.setBookListId(bList.getId());
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			ri = fileUtils.putBookListToResource(bList, ucenterFacade, bkFacade, getResourceInfoFacade,netBookFacade, uid);
			//将书单中书的数量放入进去
			int count = 0;
			List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(actCollect.getResourceId(), null,null);
			ri.setSize(bookListLinks.size());
			/*Iterator<BookListLink> iter = bookListLinks.iterator();
			while(iter.hasNext()){
				BookListLink bookListLink = iter.next();
				if(CommentUtils.TYPE_NETBOOK.equals(bookListLink.getResType())){
					NetBook netBook = netBookFacade.findNetBookInfoById(bookListLink.getBookId());
					if(netBook != null && netBook.getId() != 0){
						count++;
					}
				}else{
					BkInfo bkInfo = bkFacade.findBkInfo(bookListLink.getBookId());
					if(bkInfo != null && bkInfo.getId() != 0){
						count++;
					}
				}
			}
			ri.setSize(count);
		}*/
		}
		fri.setResourceInfo(ri);
		
//		BookList bookList = getResourceInfoFacade.queryByIdBookList(actCollect.getResourceId());
//		if(bookList == null || bookList.getId() ==0 ){
//			return ri;
//		}
//		ri = bookUtils.putBookListToResource(bookList,  getResourceInfoFacade,ucenterFacade,actFacade, bkFacade,uid);
//		
//		ri = putCollectToResource(ri, actCollect, ucenterFacade);
//		System.out.println("ri====="+ri);
		return fri;
	}
	/**
	 * 将收藏的书单格式化
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public BookListInfo putCollectBookListToInfo(ActCollect actCollect ,UcenterFacade ucenterFacade , 
			ActFacade actFacade, GetResourceInfoFacade getResourceInfoFacade,BkFacade bkFacade,
			MyBkFacade myBkFacade,NetBookFacade netBookFacade,Long uid){
		BookListInfo ri = new BookListInfo();
		BookList bookList = getResourceInfoFacade.queryByIdBookList(actCollect.getResourceId());
		
		if(bookList == null || bookList.getId() ==0 ){
			return ri;
		}
		//如果是默认书单修改书单名
		if(bookList.getType() == 0){
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, bookList.getuId());
			if(userAllInfo.getUserId() != 0){
				String bookListName = bookList.getBookListName();
				bookListName = bookListName.substring(1);
				bookList.setBookListName(userAllInfo.getName()+bookListName);
			}
		}
		ri = fileUtils.putObjToBookListInfo(bookList,actFacade, getResourceInfoFacade, bkFacade, ucenterFacade,myBkFacade,netBookFacade,uid);
		
		ri.setIsDefault(3);
		return ri;
	}
	/**
	 * 将收藏的书评格式化(变为转发格式)
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectBkCommentToResource(ActCollect actCollect ,UcenterFacade ucenterFacade , 
			ActFacade actFacade,BkFacade bkFacade,BkCommentFacade bkCommentFacade,NetBookFacade netBookFacade,
			GetResourceInfoFacade getResourceInfoFacade,long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
		
		BkComment bkComment = bkCommentFacade.findCommentIsExistById(actCollect.getResourceId());
		
		if(bkComment == null || bkComment.getIsDelete() == CommentUtils.FALSE || bkComment.getId() ==0){
			return new ResourceInfo();
//			ri.setIsDel(1);
//			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
//			ri.setType(actCollect.getType());
//			try {
//				if(bkComment != null){
//					ri.setRid(actCollect.getId());
//				}
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,null,getResourceInfoFacade,netBookFacade,uid);
		}
		fri.setResourceInfo(ri);
		
		
		
		
		
//		ResourceInfo fri = new ResourceInfo();
//		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade);
//		
//		BkComment bkComment = bkCommentFacade.findCommentIsExistById(actCollect.getResourceId());
//		if(bkComment == null || bkComment.getId() == 0){
//			return ri;
//		}
//		ri = bookUtils.putBkCommentToResource(bkComment, ucenterFacade, actFacade, bkFacade,netBookFacade);
//		fri.setResourceInfo(ri);
//		ri = putCollectToResource(ri, actCollect, ucenterFacade);
		
		return fri;
	}
	/**
	 * 将收藏的影评格式化
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectMvCommentToResource(ActCollect actCollect ,UcenterFacade ucenterFacade , ActFacade actFacade,MvFacade mvFacade,MvCommentFacade mvCommentFacade,MyMovieFacade myMovieFacade ,long uid){
		ResourceInfo ri = new ResourceInfo();
		
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
		
		MvComment mvComment = mvCommentFacade.findMvCommentIsExist(actCollect.getResourceId());

		if(mvComment == null || mvComment.getIsDel() == CommentUtils.FALSE || mvComment.getId() == 0){
			return new ResourceInfo();
//			ri.setIsDel(1);
//			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
//			ri.setType(actCollect.getType());
//			try {
//				ri.setRid(mvComment.getId());
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			ri = movieUtils.putMVCommentToResource(mvComment, ucenterFacade,
					actFacade, mvFacade,myMovieFacade,uid);
		}
		fri.setResourceInfo(ri);
		
		
//		MvComment mvComment  = mvCommentFacade.findMvCommentIsExist(actCollect.getResourceId());
//		if(mvComment == null || mvComment.getId() == 0){
//			return ri;
//		}
//		ri = movieUtils.putMVCommentToResource(mvComment, ucenterFacade, actFacade, mvFacade,myMovieFacade,uid);
//		
//		ri = putCollectToResource(ri, actCollect, ucenterFacade);
		
		return fri;
	}
	/**
	 * 将收藏的长文章格式化
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectPostToResource(ActCollect actCollect ,UcenterFacade ucenterFacade , ActFacade actFacade,PostFacade postFacade ,long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
		
		Post post = postFacade.queryByIdName(actCollect.getResourceId());
		if(post == null || post.getIsDel() == CommentUtils.FALSE || post.getId() == 0){
			return new ResourceInfo();
//			ri.setIsDel(1);
//			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
//			ri.setType(actCollect.getType());
//			try {
//				ri.setRid(post.getId());
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			ri = fileUtils.putObjectToResource(post, ucenterFacade, actFacade);
		}
		
		
		fri.setResourceInfo(ri);
		
		
		
//		Post post  = postFacade.queryByIdName(actCollect.getResourceId());
//		if(post == null || post.getId() == 0){
//			return ri;
//		}
//		
//		ri = fileUtils.putObjectToResource(post, ucenterFacade, actFacade);
//		ri = putCollectToResource(ri, actCollect, ucenterFacade);
		
		return fri;
	}
	/**
	 * 将收藏的新的长文章格式化
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectArticleToResource(ActCollect actCollect ,UcenterFacade ucenterFacade , ActFacade actFacade,ArticleFacade articleFacade ,long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
		
		Article article = articleFacade.queryArticleById(actCollect.getResourceId());
		if(article == null || article.getIsDel() == CommentUtils.FALSE || article.getId() == 0){
			return new ResourceInfo();
//			ri.setIsDel(1);
//			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
//			ri.setType(actCollect.getType());
//			try {
//				ri.setRid(post.getId());
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			ri = fileUtils.putObjectToResource(article, ucenterFacade, actFacade);
		}
		
		
		fri.setResourceInfo(ri);
		
		
		
//		Post post  = postFacade.queryByIdName(actCollect.getResourceId());
//		if(post == null || post.getId() == 0){
//			return ri;
//		}
//		
//		ri = fileUtils.putObjectToResource(post, ucenterFacade, actFacade);
//		ri = putCollectToResource(ri, actCollect, ucenterFacade);
		
		return fri;
	}
	/**
	 * 将收藏的日志格式化
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putCollectDiaryToResource(ActCollect actCollect ,UcenterFacade ucenterFacade , ActFacade actFacade,DiaryFacade diaryFacade ,long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actCollect, ucenterFacade, actFacade);
		
		Diary diary = diaryFacade.queryByIdDiary(actCollect.getResourceId());
		if(diary == null || diary.getIsDel() == CommentUtils.FALSE || diary.getId() == 0){
			return new ResourceInfo();
//			ri.setIsDel(1);
//			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
//			ri.setType(actCollect.getType());
//			try {
//				ri.setRid(diary.getId());
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
		}else{
			ri = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
		}
		
		
		fri.setResourceInfo(ri);
		
		
		
//		Diary diary  = diaryFacade.queryByIdDiary(actCollect.getResourceId());
//		if(diary == null || diary.getId() == 0){
//			return ri;
//		}
//		
//		ri = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
//		ri = putCollectToResource(ri, actCollect, ucenterFacade);
		
		return fri;
	}
	
	
	
	
	
	
	
	/**
	 * 将订阅的连载格式化
	 * @param object
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putSubscribeSerializeToResource(ActSubscribe actSubscribe ,UcenterFacade ucenterFacade , ActFacade actFacade,SerializeFacade serializeFacade){
		ResourceInfo ri = new ResourceInfo();
		
		Serialize serialize = serializeFacade.seleceSerializeByid(actSubscribe.getResourceId());
		if(serialize.getId() == 0){
			return ri;
		}
		ri = fileUtils.putObjectToResource(serialize, ucenterFacade, actFacade, serializeFacade);
		
		ri = putSubscribeToResource(ri, actSubscribe, ucenterFacade);
		
		return ri;
	}
	
	
	/**
	 * 是否赞过/是否收藏过等方法
	 * @param ri
	 * @param uid
	 * @param actFacade
	 */
	public ResourceInfo putIsCollectToResoure(ResourceInfo ri ,Long uid,ActFacade actFacade,int isList){
		if(ri == null || ri.getRid() == 0){
			return ri;
		}
		long rid = ri.getRid();
		String type = ri.getType();
		if(CommentUtils.TYPE_BOOKLIST.equals(ri.getType())){
			rid = ri.getBookListId();
		}else if(CommentUtils.TYPE_MOVIELIST.equals(ri.getType())){
			rid = ri.getMovieListId();
		}
		
		if(uid != null && uid != 0){
			if(isList == 1){
				//收藏数量
				int fNum = actFacade.findCollectCount(rid);
				ri.setfNum(fNum);
				//是否收藏过
				ActCollect actCollect = actFacade.findCollectIsExist(uid, rid);
				if(actCollect.getId() != 0){
					ri.setIsCollect(CheckParams.changeIntForTrueFalse(actCollect.getIsCollect()));
				}
			}
//			//是否赞过
//			ActPraise actPraise = actFacade.findActPraise(uid, rid);
//			if(actPraise != null && actPraise.getFlag() == ResultUtils.SUCCESS){
//				ri.setIsPraise(CheckParams.changeIntForTrueFalse(actPraise.getIsPraise()));
//			}
			
			//是否订阅
//			if(CommentUtils.TYPE_PUSH.equals(type)){
//				ActSubscribe actSubscribe = actFacade.findSubscribeIsExist(uid, ri.getSerializeInfo().getId());
//				ri.getSerializeInfo().setIsSubscribe(CheckParams.changeIntForTrueFalse(actSubscribe.getIsSubscribe()));
//			}
		}
		
		//各种数量
		
		if(CommentUtils.TYPE_BOOKLIST.equals(ri.getType())){
			rid = ri.getBookListId();
		}else if(CommentUtils.TYPE_MOVIE_TALK.equals(ri.getType())){
			rid = ri.getMovieListId();
		}
//		int rNum= actFacade.findTransmitCount(null, rid);
		int cNum = actFacade.findCommentCount(null, rid);
		int zNum= actFacade.findPraiseCount(null, rid);
		int lNum = actFacade.findLowCount(rid);
//		ri.setrNum(rNum);
		ri.setcNum(cNum);
		ri.setzNum(zNum);
		ri.setlNum(lNum);
		
		//添加有用没用的数量
		Map<String, Object> usefulMap = actFacade.findUsefulCount(rid);
		int usefulCount = (Integer) usefulMap.get("usefulCount");
		Map<String, Object> uselessMap = actFacade.findUselessCount(rid);
		int uselessCount = (Integer) uselessMap.get("uselessCount");
		ri.setUsefulCount(usefulCount+"");
		ri.setUselessCount(uselessCount+"");
		//是否点过有用没用
		ActUseful actUseful = actFacade.findUsefulByResidAndUserid(rid, uid);
		ri.setIsUseful(actUseful.getIsUseful());
		//是否赞过
//		Long uid = getUserId();
		if(uid != null && uid != 0){
			ActPraise actPraise = actFacade.findActPraise(uid, rid);
			if(actPraise != null && actPraise.getFlag() == ResultUtils.SUCCESS){
				ri.setIsPraise(fileUtils.changeIntForTrueFalse(actPraise.getIsPraise()));
				ri.setIsLow(fileUtils.changeIntForTrueFalse(actPraise.getIsLow()));
			}
		}
		return ri;
	}
	
	
	/**
	 * 将转发的书评格式化
	 * @param actTransmit
	 * @param ucenterFacade
	 * @param actFacade
	 * @param bkCommentFacade
	 * @param bkFacade
	 * @param getResourceInfoFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitBKCommentToResourceInfo(ActTransmit actTransmit,UcenterFacade ucenterFacade,ActFacade actFacade,BkCommentFacade bkCommentFacade,BkFacade bkFacade,GetResourceInfoFacade getResourceInfoFacade,NetBookFacade netBookFacade,Long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actTransmit, ucenterFacade, actFacade);
		
		BkComment bkComment = bkCommentFacade.findCommentIsExistById(actTransmit.getResourceId());
		
		if(bkComment == null || bkComment.getIsDelete() == CommentUtils.FALSE || bkComment.getId() ==0){
			ri.setIsDel(1);
			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
		}else{
			ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,null,getResourceInfoFacade,netBookFacade,uid);
		}
		fri.setResourceInfo(ri);
		return fri;
	}
	
	/**
	 * 将转发的书单格式化
	 * @param actTransmit
	 * @param ucenterFacade
	 * @param actFacade
	 * @param bkFacade
	 * @param getResourceInfoFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitBookListToResourceInfo(ActTransmit actTransmit,UcenterFacade ucenterFacade,ActFacade actFacade,BkFacade bkFacade,GetResourceInfoFacade getResourceInfoFacade,NetBookFacade netBookFacade,Long uid){
		ResourceInfo fri = new ResourceInfo();
		ResourceInfo ri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actTransmit, ucenterFacade, actFacade);
		
		BookList bList = getResourceInfoFacade.queryByIdBookList(actTransmit.getResourceId());
		
		if(bList == null || bList.getIsDel() == CommentUtils.FALSE || bList.getId() == 0){
			ri.setIsDel(1);
			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
		}else{
			ri = fileUtils.putBookListToResource(bList, ucenterFacade, bkFacade, getResourceInfoFacade,netBookFacade, uid);
		}
		fri.setResourceInfo(ri);
		return fri;
	}
	
	/**
	 * 将转发的影评格式化
	 * @param actTransmit
	 * @param ucenterFacade
	 * @param actFacade
	 * @param mvCommentFacade
	 * @param mvFacade
	 * @param myMovieFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitMVCommentToResourceInfo(ActTransmit actTransmit,UcenterFacade ucenterFacade,ActFacade actFacade,MvCommentFacade mvCommentFacade,MvFacade mvFacade,MyMovieFacade myMovieFacade, Long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actTransmit, ucenterFacade, actFacade);
		
		MvComment mvComment = mvCommentFacade.findMvCommentIsExist(actTransmit
				.getResourceId());

		if(mvComment == null || mvComment.getIsDel() == CommentUtils.FALSE || mvComment.getId() == 0){
			ri.setIsDel(1);
			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
		}else{
			ri = movieUtils.putMVCommentToResource(mvComment, ucenterFacade,
					actFacade, mvFacade,myMovieFacade,uid);
		}
		fri.setResourceInfo(ri);
		return fri;
	}
	
	/**
	 * 将转发的影单格式化
	 * @param actTransmit
	 * @param ucenterFacade
	 * @param actFacade
	 * @param mvCommentFacade
	 * @param mvFacade
	 * @param myMovieFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitMovieListToResourceInfo(ActTransmit actTransmit,UcenterFacade ucenterFacade,ActFacade actFacade,MvCommentFacade mvCommentFacade,MvFacade mvFacade,MyMovieFacade myMovieFacade, Long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actTransmit, ucenterFacade, actFacade);
		
		MovieList movieList = myMovieFacade.findMovieListById(actTransmit.getResourceId());
		
		if(movieList == null || movieList.getIsDel() == CommentUtils.FALSE || movieList.getId() == 0){
			ri.setIsDel(1);
			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
		}else{
			ri = movieUtils.putMovieListToResource(movieList, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
		}
		
		fri.setResourceInfo(ri);
		return fri;
	}
	
	/**
	 * 将转发的日志格式化
	 * @param actTransmit
	 * @param ucenterFacade
	 * @param actFacade
	 * @param diaryFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitDiaryToResourceInfo(ActTransmit actTransmit,UcenterFacade ucenterFacade,ActFacade actFacade,DiaryFacade diaryFacade, Long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actTransmit, ucenterFacade, actFacade);
		
		Diary diary = diaryFacade.queryByIdDiary(actTransmit.getResourceId());
		if(diary == null || diary.getIsDel() == CommentUtils.FALSE || diary.getId() == 0){
			ri.setIsDel(1);
			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
		}else{
			ri = fileUtils.putObjectToResource(diary, ucenterFacade, actFacade);
		}
		
		
		fri.setResourceInfo(ri);
		return fri;
	}
	/**
	 * 将转发的长文章格式化
	 * @param actTransmit
	 * @param ucenterFacade
	 * @param actFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitPostToResourceInfo(ActTransmit actTransmit,UcenterFacade ucenterFacade,ActFacade actFacade,PostFacade postFacade, Long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actTransmit, ucenterFacade, actFacade);
		
		Post post = postFacade.queryByIdName(actTransmit.getResourceId());
		if(post == null || post.getIsDel() == CommentUtils.FALSE || post.getId() == 0){
			ri.setIsDel(1);
			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
		}else{
			ri = fileUtils.putObjectToResource(post, ucenterFacade, actFacade);
		}
		
		
		fri.setResourceInfo(ri);
		return fri;
	}
	
	/**
	 * 将转发的新的长文章格式化
	 * @param actTransmit
	 * @param ucenterFacade
	 * @param actFacade
	 * @param uid
	 * @return
	 */
	public ResourceInfo putActTransmitArticleToResourceInfo(ActTransmit actTransmit,UcenterFacade ucenterFacade,ActFacade actFacade,ArticleFacade articleFacade, Long uid){
		ResourceInfo ri = new ResourceInfo();
		ResourceInfo fri = new ResourceInfo();
		
		fri = fileUtils.putObjectToResource(actTransmit, ucenterFacade, actFacade);
		
		Article article = articleFacade.queryArticleById(actTransmit.getResourceId());
		if(article == null || article.getIsDel() == CommentUtils.FALSE || article.getId() == 0){
			ri.setIsDel(1);
			ri.setTitle(CommentUtils.CONTENT_IS_DEL);
		}else{
			ri = fileUtils.putObjectToResource(article, ucenterFacade, actFacade);
		}
		
		
		fri.setResourceInfo(ri);
		return fri;
	}
	
	/**
	 * 将推到首页的影评转换为资源类
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param myMovieFacade
	 * @param mvFacade
	 * @return
	 */
	public ResourceInfo putActPublishMVCommentToResource(ActPublish actPublish,UcenterFacade ucenterFacade,ActFacade actFacade,MvCommentFacade mvCommentFacade,MvFacade mvFacade,MyMovieFacade myMovieFacade,Long uid){
		ResourceInfo ri  =new ResourceInfo();
		Long id = actPublish.getResourceId();
		
		//将movielist转换为资源类
		MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);
//		MovieList movieList = myMovieFacade.findMovieListById(movieListId);
//		ri = putMovieListToResource(movieList, ucenterFacade, actFacade, myMovieFacade, mvFacade,uid);
		ri = movieUtils.putMVCommentToResource(mvComment, ucenterFacade,
				actFacade, mvFacade,myMovieFacade,uid);
		
//		ri.setRid();
		ri.setBtime(actPublish.getPublishDate());
		
		return ri;
	}
	/**
	 * 将推到首页的影评转换为资源类
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param myMovieFacade
	 * @param mvFacade
	 * @return
	 */
	public ResourceInfo putActPublishBKCommentToResource(ActPublish actPublish,UcenterFacade ucenterFacade,ActFacade actFacade,BkCommentFacade bkCommentFacade,BkFacade bkFacade,GetResourceInfoFacade getResourceInfoFacade,NetBookFacade netBookFacade,Long uid){
		ResourceInfo ri  =new ResourceInfo();
		Long id = actPublish.getResourceId();
		
		BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
		//将movielist转换为资源类
		ri = fileUtils.putObjectToResource(bkComment, ucenterFacade, actFacade, bkFacade,null,getResourceInfoFacade,netBookFacade,uid);
		
//		ri.setRid();
		ri.setBtime(actPublish.getPublishDate());
		
		return ri;
	}
	/**
	 * 将推到首页的长文章转换为资源类
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param myMovieFacade
	 * @param mvFacade
	 * @return
	 */
	public ResourceInfo putActPublishArticleToResource(ActPublish actPublish,UcenterFacade ucenterFacade,ActFacade actFacade,PostFacade postFacade,Long uid){
		ResourceInfo ri  =new ResourceInfo();
		Long id = actPublish.getResourceId();
		
		Post post = postFacade.queryByIdName(id);
		
		//将movielist转换为资源类
		ri = fileUtils.putObjectToResource(post, ucenterFacade, actFacade);
		
//		ri.setRid();
		ri.setBtime(actPublish.getPublishDate());
		
		return ri;
	}
	/**
	 * 将推到首页的长文章转换为资源类
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param myMovieFacade
	 * @param mvFacade
	 * @return
	 */
	public ResourceInfo putActPublishNEWArticleToResource(ActPublish actPublish,UcenterFacade ucenterFacade,ActFacade actFacade,ArticleFacade articleFacade,Long uid){
		ResourceInfo ri  =new ResourceInfo();
		Long id = actPublish.getResourceId();
		
		Article article = articleFacade.queryArticleById(id);
		
		//将movielist转换为资源类
		ri = fileUtils.putObjectToResource(article, ucenterFacade, actFacade);
		
//		ri.setRid();
		ri.setBtime(actPublish.getPublishDate());
		
		return ri;
	}
	/**
	 * 将运营封面等信息加入到resource中
	 * @param ri
	 * @param rl
	 */
	public void putLinkRemarkToResource(ResourceInfo ri , ResourceLink rl){
		if(ri == null || rl == null){
			return ;
		}
		
		String publicImage = "";
		try {
			publicImage  = rl.getDescription();
		} catch (Exception e) {
			publicImage = "";
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		if((CommentUtils.CASETYPE_BK_BANNER.equals(rl.getType()) || CommentUtils.CASETYPE_INDEX.equals(rl.getType())) && rl.getDescription()!=null && rl.getDescription().trim().length()>0){
			//banner里面的资源封面可能被替换了
			String contents[]=rl.getDescription().split("<!--separator-->");
			if(contents!=null && contents.length>0){
				ri.setImageUrl(contents[0]);
			}
		}
		if(ri.getImageUrl()==null || "".equals(ri.getImageUrl())){
			ri.setImageUrl(rl.getDescription());
		}
		
	}
	
	/**
	 * 截取资源类中的评论
	 * @param resourceInfos
	 */
	public void subStringResourceListContent(List<ResourceInfo> resourceInfos,int size){
		for (ResourceInfo ri : resourceInfos) {
			List<Map<String, String>> map = ri.getContList();
			if(map != null && map.size()>0){
				for (int i = 0; i < map.size(); i++) {
					if(WebUtils.TYPE_DIV.equals(map.get(i).get(WebUtils.TYPE))){
						String content = (String) ((Map<String, String>)ri.getContList().get(i)).get(WebUtils.DATA);
						if(content.length()>size){
							content = content.substring(0, size);
							((Map<String, String>)ri.getContList().get(i)).put(WebUtils.DATA, content);
//						((Map<String, String>)ri.getContList().get(i)).get(WebUtils.DATA).substring(0, 50);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 将每条资源的得到的金钱数放入到资源类中
	 * @param ri
	 * @param uid
	 * @param paycenterFacade
	 * @return
	 */
	public void putMoneyToResource(ResourceInfo ri ,PaycenterFacade paycenterFacade){
		if(ri == null || ri.getRid() == 0 || paycenterFacade == null){
			return;
		}
		
		if(CommentUtils.TYPE_ACT.equals(ri.getType())){
			ri = ri.getResourceInfo();
		}
		long rid = ri.getRid();
		if(CommentUtils.TYPE_BOOKLIST.equals(ri.getType())){
			rid = ri.getBookListId();
		}else if(CommentUtils.TYPE_MOVIELIST.equals(ri.getType())){
			rid = ri.getMovieListId();
		}
		
		RewardStatistical rewardStatistical = paycenterFacade.getMoneyBySourceId(ri.getRid());
		if(rewardStatistical.getId() != 0){
			ri.setMoney(rewardStatistical.getTotalAmt());
		}
		
	}
	
	
	
	
	public static void main(String[] args) {
		int a = 10;
		BookInfo bookInfo = new BookInfo();
		System.out.println(bookInfo);
		test(bookInfo);
		System.out.println(bookInfo);
	}
	
	
	public static void test(BookInfo bookInfo){
		bookInfo.setId(100);
	}

}
