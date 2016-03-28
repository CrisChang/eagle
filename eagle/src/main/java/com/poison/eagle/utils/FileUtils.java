package com.poison.eagle.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poison.resource.model.*;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.keel.framework.runtime.ProductContextHolder;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPraise;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActTransmit;
import com.poison.act.model.ActUseful;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.CommentInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.MovieListInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.resourceinfo.ResourceInfoCreator;
import com.poison.eagle.utils.story.StoryUtils;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.paycenter.model.RewardStatistical;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.store.client.BkFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.NetBook;
import com.poison.ucenter.client.StoryUserFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;


public class FileUtils {
	private static final Log LOG = LogFactory.getLog(FileUtils.class);
	private static FileUtils fileUtils;
	private  final int TRUE= 0;
	private  final int FALSE= 1;
	private  final long UNID = 0;
	//豆瓣返回字符串的标示位
	private  final String BOOK_ID="id";
	private  final String BOOK_ALT="alt";
	private  final String BOOK_IMAGE = "image";
	private  final String BOOK_TITLE = "title";
	private  final String BOOK_AVERAGE = "average";
	private  final String BOOK_AUTHOR = "author";
	private  final String BOOK_TRANSLATOR = "translator";
	private  final String BOOK_PUBLISHER = "publisher";
	private  final String BOOK_ORIGIN_TITLE = "origin_title";
	private  final String BOOK_SUBTITLE = "subtitle";
	private  final String BOOK_PUBDATE = "pubdate";
//	/**
//	 * 将推送的书单类转换为首页resoureinfo实例类
//	 * @param obj
//	 * @param ucenterFacade
//	 * @param bkFacade
//	 * @param getResourceInfoFacade
//	 * @param myBkFacade
//	 * @return
//	 */
//	public  ResourceInfo putActPulishToResource(Object obj , UcenterFacade ucenterFacade ,
//			BkFacade bkFacade,GetResourceInfoFacade getResourceInfoFacade,ActFacade actFacade,long uid){
//		ResourceInfo ri = new ResourceInfo();
//		//调用本类公用方法
//		ri = putObjectToResource(obj, ucenterFacade,actFacade);
//		String objName = obj.getClass().getName();
//		ActPublish object = (ActPublish) obj;
//		//查询某个书单详情
//		BookList bList = getResourceInfoFacade.queryByIdBookList(object.getResourceId());
//		ri.setTitle(bList.getBookListName());
//		ri.setBookListId(bList.getId());
//		ri.setReason(bList.getReason());
//		List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(bList.getId(), null);
//		
//		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
//		// 将书单中第一本书放入资源类中
//		if(bookListLinks.size() >0){
//			BookListLink bookListLink = bookListLinks.get(0);
//			BookInfo bookInfo = new BookInfo();
//			
//			BkInfo bkInfo = bkFacade.findBkInfo(bookListLink.getBookId());
//			bookInfo = putBKToBookInfo(bkInfo, TRUE);
//			
//			BookListLink bookListLink2 = getResourceInfoFacade.findUserIsCollectBook(uid, bookInfo.getId());
//			if(bookListLink2.getId() != 0){
//				bookInfo.setInList(TRUE);
//			}
//			
//			ri.setBookInfo(bookInfo);
//		}
//		return ri;
//	}
	private  final String BOOK_PAGES = "pages";
	private  final String BOOK_PRICE = "price";
	private  final String BOOK_BINDING = "binding";
	private  final String BOOK_TAGS = "tags";
//	/**
//	 * 将图书转化为客户端可以显示的类型(首页)(去除我的评论)
//	 * @param obj
//	 * @param isList
//	 * @param bkCommentFacade
//	 * @param ucenterFacade
//	 * @return
//	 */
//	public  BookInfo putBKToBookInfoIndex(Object obj , int isList, Long uid,
//			BkCommentFacade bkCommentFacade,UcenterFacade ucenterFacade){
//		String objName = obj.getClass().getName();
//		BookInfo bookInfo = new BookInfo();
//		
//		bookInfo = putBKToBookInfo(obj, isList);
//		
//		
//		
//		//图书全部评论
//		List<BkComment> bkComments = bkCommentFacade.findAllBkComment(bookInfo.getId(), null);
//		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
//		//我的评论列表
//		List<BkComment> myComments = bkCommentFacade.findMyBkCommentList(uid, bookInfo.getId(), null);
//		bkComments.removeAll(myComments);
//		
//		ResourceInfo resourceInfo =null;
//		//将我的评论单独放进去
//		if(myComments.size()>0){
//			resourceInfo = putObjectToResource(myComments.get(0), ucenterFacade);
//			bookInfo.setResourceInfo(resourceInfo);
//		}
//		if(bkComments.size()>1){
//			for (BkComment bkComment : bkComments) {
//				resourceInfo = putObjectToResource(bkComment, ucenterFacade);
//				resourceInfos.add(resourceInfo);
//			}
//			bookInfo.setCommentList(resourceInfos);
//		}else if(bkComments.size() == 1){
//			BkComment bkComment = bkComments.get(0);
//			if(bkComment.getId() != 0 ){
//				resourceInfo = putObjectToResource(bkComment, ucenterFacade);
//				resourceInfos.add(resourceInfo);
//			}
//		}
//		
//		bookInfo.setCommentList(resourceInfos);
//		
//		//添加书的平均分和评论数
//		BkAvgMark bkAvgMark = bkCommentFacade.findBkAvgMarkByBkId(bookInfo.getId());
//		if(bkAvgMark.getFlag() == ResultUtils.SUCCESS || bkAvgMark.getFlag() == UNID){
//			bookInfo.setScore(String.valueOf(bkAvgMark.getBkAvgMark()));
//			bookInfo.setcNum(bkAvgMark.getBkTotalNum());
//		}
////		//书评数目
////		int cNum = bkCommentFacade.findCommentCount(bookInfo.getId());
////		bookInfo.setcNum(cNum);
//		
//		return bookInfo;
//	}
//	/**
//	 * 将图书转化为客户端可以显示的类型(我的书单我的评论)
//	 * @param obj
//	 * @param isList
//	 * @param uid
//	 * @param bkCommentFacade
//	 * @param ucenterFacade
//	 * @return
//	 */
//	public  BookInfo putBKToBookInfo(Object obj , int isList, long uid,
//			BkCommentFacade bkCommentFacade,UcenterFacade ucenterFacade){
//		String objName = obj.getClass().getName();
//		BookInfo bookInfo = new BookInfo();
//		
//		bookInfo = putBKToBookInfo(obj, isList);
//		
//		
//		List<BkComment> bkComments = bkCommentFacade.findMyBkCommentList(uid,bookInfo.getId(), null);
//		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
//		if(bkComments.size() > 0){
//			ResourceInfo resourceInfo = putObjectToResource(bkComments.get(0), ucenterFacade);
//			bookInfo.setResourceInfo(resourceInfo);
//		}
//		
//		return bookInfo;
//	}
	private  final String BOOK_SUMMARY = "summary";
	private  final String BOOK_AUTHOR_INTRO = "author_intro";
	private  final String BOOK_CATALOG = "catalog";
	private  final String BOOK_ISBN13 = "isbn13";
	private  final String BOOK_ISBN10 = "isbn10";
	private PaycenterFacade paycenterFacade;
	private FileUtils(){}

	public static FileUtils getInstance(){
		if(fileUtils == null){
			return new FileUtils();
		}else{
			return fileUtils;
		}
	}

	/**
	 * 读取html文件
	 * @param path
	 * @return
	 */
	public static String readHTMl(String path){
		URL url;
		String data = "";
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			InputStream inputStream = conn.getInputStream();   //通过输入流获得网站数据
			byte[] getData = readInputStream(inputStream);     //获得网站的二进制数据
			data = new String(getData, "utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return data;
	}

	private static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        bos.close();
        return bos.toByteArray();
    }

	 public static void main(String[] args) {

		 System.out.println(readHTMl("http://112.126.68.72/image/common/351de8b273f6d48a03474e9765c0788e.html?num=0"));
		 System.out.println(CheckParams.getContentFromHtmlByArticle(readHTMl("http://112.126.68.72/image/common/351de8b273f6d48a03474e9765c0788e.html?num=0")));
//		  FileOutputStream fop = null;
//		  File file;
//		  String content = "This is the text content";
//
//		  try {
//
//		   file = new File("e:/newfile.htm");
//		   fop = new FileOutputStream(file);
//
//		   // if file doesnt exists, then create it
//		   if (!file.exists()) {
//		    file.createNewFile();
//		   }
//
//		   // get the content in bytes
//		   byte[] contentInBytes = content.getBytes();
//
//		   fop.write(contentInBytes);
//		   fop.flush();
//		   fop.close();
//
//		   System.out.println("Done");
//
//		  } catch (IOException e) {
//		   e.printStackTrace();
//		  } finally {
//		   try {
//		    if (fop != null) {
//		     fop.close();
//		    }
//		   } catch (IOException e) {
//		    e.printStackTrace();
//		   }
//		  }
	}

	/**
	 * 将相应的用户类转换成UserEntity
	 * @param userInfo
	 * @param type 0:添加个性签名、1：没有
	 * @return
	 */
	public  UserEntity copyUserInfo(Object userInfo , int type){
		UserEntity userEntity = new UserEntity();
		if(userInfo.getClass().getName().equals(UserAllInfo.class.getName())){
			UserAllInfo ui = (UserAllInfo) userInfo;
			userEntity.setId(ui.getUserId());
			userEntity.setNickName(ui.getName());
			userEntity.setFace_url(ui.getFaceAddress());
			userEntity.setSign(ui.getSign());
			userEntity.setIdentification(ui.getIdentification());
			int isBinding = 0;
			if(WebUtils.isPhone(ui.getMobilePhone())){
				userEntity.setIsBinding(1);
			}else {
				userEntity.setIsBinding(0);
			}
			userEntity.setSort(ui.getLastestRevisionDate());
			if(type == CommentUtils.UN_ID){
				userEntity.setSex(ui.getSex());
				userEntity.setInterest(ui.getInterest());
				userEntity.setIntroduction(ui.getIntroduction());
				userEntity.setIdentification(ui.getIdentification());
			}
			int level = ui.getLevel();
			String isOperation = ui.getIsOperation();
			//用户等级
//			if(level == CommentUtils.USER_LEVEL_NORMAL){
//				level = 0;
//			}else if(level == CommentUtils.USER_LEVEL_TALENT){
//				level = 1;
//			}
			userEntity.setType(level);
			userEntity.setIsOperation(isOperation);
			/*userEntity.setAge(ui.getAge());
			userEntity.setConstellation(ui.getConstellation());*/
		}else if(userInfo.getClass().getName().equals(UserInfo.class.getName())){
			UserInfo ui = (UserInfo) userInfo;
			userEntity.setId(ui.getUserId());
			userEntity.setNickName(ui.getName());
			userEntity.setFace_url(ui.getFaceAddress());
			userEntity.setSex(null);
			int level = ui.getLevel();
			userEntity.setSort(ui.getLastestRevisionDate());
			//用户等级
//			if(level == CommentUtils.USER_LEVEL_NORMAL){
//				level = 0;
//			}else if(level == CommentUtils.USER_LEVEL_TALENT){
//				level = 1;
//			}
			userEntity.setType(level);
		}
		return userEntity;
	}

	/**
	 * 将资源类转换为首页带有评论转发赞的resoureinfo实例类(新添有连载类型)
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @return
	 */
	public  ResourceInfo putObjectToResource(Object obj , UcenterFacade ucenterFacade , ActFacade actFacade){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		ri = putObjectToResource(obj,ucenterFacade);
		String objName = obj.getClass().getName();
		if(Serialize.class.getName().equals(objName)){
			Serialize object = (Serialize) obj;
			ri.setRid(object.getId());
			ri.setType(CommentUtils.TYPE_PUSH);
			ri.setFlag(object.getFlag());
			ri.setBtime(object.getEndDate());

			SerializeInfo serializeInfo = putSerializeToInfo(object, actFacade, ucenterFacade);
			ri.setSerializeInfo(serializeInfo);

			UserAllInfo ui = ucenterFacade.findUserInfo(null, object.getUid());
			ri.setUserEntity(copyUserInfo(ui, FALSE));
		}
		//如果是书单或影单，提取书单id或影单id
		long r_id = ri.getRid();
		if(CommentUtils.TYPE_BOOKLIST.equals(ri.getType())){
			r_id = ri.getBookListId();
		}else if(CommentUtils.TYPE_MOVIE_TALK.equals(ri.getType())){
			r_id = ri.getMovieListId();
		}
		int rNum= actFacade.findTransmitCount(null, r_id);
		int cNum = actFacade.findCommentCount(null, r_id);
		int zNum= actFacade.findPraiseCount(null, r_id);
		int lNum = actFacade.findLowCount(r_id);
		ri.setrNum(rNum);
		ri.setcNum(cNum);
		ri.setzNum(zNum);
		ri.setlNum(lNum);

		//是否赞过
		Long uid = getUserId();
		if(uid != null && uid != 0){
			ActPraise actPraise = actFacade.findActPraise(uid, r_id);
			if(actPraise != null && actPraise.getFlag() == ResultUtils.SUCCESS){
				ri.setIsPraise(changeIntForTrueFalse(actPraise.getIsPraise()));
				ri.setIsLow(changeIntForTrueFalse(actPraise.getIsLow()));
			}
		}


		return ri;
	}

	/**
	 * 将资源类转换为首页带有评论转发赞的resoureinfo实例类(新添有连载类型)
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @return
	 */
	public  ResourceInfo putObjectToResource(Object obj , UcenterFacade ucenterFacade , ActFacade actFacade,SerializeFacade serializeFacade){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		ri = putObjectToResource(obj,ucenterFacade);
		String objName = obj.getClass().getName();
		if(Serialize.class.getName().equals(objName)){
			Serialize object = (Serialize) obj;
			ri.setRid(object.getId());
			ri.setType(CommentUtils.TYPE_PUSH);
			ri.setFlag(object.getFlag());
			//shouyelianzaixiugai
			SerializeInfo serializeInfo = putSerializeToInfo(object, actFacade, ucenterFacade, serializeFacade);
			ri.setSerializeInfo(serializeInfo);

			UserAllInfo ui = ucenterFacade.findUserInfo(null, object.getUid());
			ri.setUserEntity(copyUserInfo(ui, FALSE));
		}
		long r_id = ri.getRid();
		int rNum= actFacade.findTransmitCount(null, r_id);
		int cNum = actFacade.findCommentCount(null, r_id);
		int zNum= actFacade.findPraiseCount(null, r_id);
		ri.setrNum(rNum);
		ri.setcNum(cNum);
		ri.setzNum(zNum);
		try {

			long uid = getUserId();
			//是否赞过
			ActPraise actPraise = actFacade.findActPraise(uid, r_id);
			if(actPraise != null && actPraise.getFlag() == ResultUtils.SUCCESS){
				ri.setIsPraise(changeIntForTrueFalse(actPraise.getIsPraise()));
			}
		} catch (Exception e) {
		}


		return ri;
	}

	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型
	 * @param serialize
	 * @param actFacade
	 * @param ucenterFacade
	 * @return
	 */
	public  SerializeInfo putSerializeToInfo(Serialize serialize , ActFacade actFacade, UcenterFacade ucenterFacade){
		SerializeInfo serializeInfo = new SerializeInfo();
		//跟用户id查出用户实例
		UserAllInfo userAllInfo = new UserAllInfo();
		UserEntity userEntity = new UserEntity();
		if(ucenterFacade != null){
			userAllInfo = ucenterFacade.findUserInfo(null, serialize.getUid());
			userEntity = copyUserInfo(userAllInfo,1);

		}

		serializeInfo.setAuthor(serialize.getAuthor());
		serializeInfo.setTitle(serialize.getName());
		serializeInfo.setId(serialize.getId());
		serializeInfo.setDescribe(serialize.getIntroduce());
		serializeInfo.setUrl(serialize.getUrl());
		serializeInfo.setType(serialize.getType());
		serializeInfo.setBtime(serialize.getBeginDate());
		serializeInfo.setUtime(serialize.getEndDate());
		serializeInfo.setUserEntity(userEntity);

		long r_id = serialize.getId();
		int rNum= actFacade.findTransmitCount(null, r_id);
		int cNum = actFacade.findCommentCount(null, r_id);
		int zNum= actFacade.findPraiseCount(null, r_id);
		serializeInfo.setrNum(rNum);
		serializeInfo.setcNum(cNum);
		serializeInfo.setzNum(zNum);

		return serializeInfo;
	}

	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型(加章节题目)
	 * @param serialize
	 * @param actFacade
	 * @param ucenterFacade
	 * @param serializeFacade
	 * @return
	 */
	public  SerializeInfo putSerializeToInfo(Serialize serialize , ActFacade actFacade, UcenterFacade ucenterFacade , SerializeFacade serializeFacade){
		SerializeInfo serializeInfo = new SerializeInfo();

		serializeInfo = putSerializeToInfo(serialize, actFacade, ucenterFacade);

		List<Chapter> chapters = serializeFacade.findChapter(serializeInfo.getId());

		if(chapters.size() >0){
			Chapter c = chapters.get(chapters.size()-1);
			serializeInfo.setChapterTitle(c.getName());
		}

		return serializeInfo;
	}

	/**
	 * 将书评类转换为图书详情中的resoureinfo实例类
	 * @param obj
	 * @param ucenterFacade
	 * @param bkFacade
	 * @return
	 */
	public  ResourceInfo putObjectToResource(Object obj , UcenterFacade ucenterFacade ,BkFacade bkFacade){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		ri = putObjectToResource(obj, ucenterFacade);
		String objName = obj.getClass().getName();
		if(BkComment.class.getName().equals(objName)){
			BkComment object = (BkComment) obj;
			BkInfo bkInfo = bkFacade.findBkInfo(object.getBookId());
			BookInfo bookInfo = putBKToBookInfo(bkInfo, FALSE);

			ri.setBookInfo(bookInfo);
		}

		return ri;
	}

	/**
	 * 将书评类转换为首页resoureinfo实例类
	 * @param obj
	 * @param ucenterFacade
	 * @param actFacade
	 * @param bkFacade
	 * @return
	 */
	public  ResourceInfo putObjectToResource(Object obj , UcenterFacade ucenterFacade ,
			ActFacade actFacade,BkFacade bkFacade,MyBkFacade myBkFacade,GetResourceInfoFacade getResourceInfoFacade,NetBookFacade netBookFacade,Long uid){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		ri = putObjectToResource(obj, ucenterFacade, actFacade);
		String objName = obj.getClass().getName();
		if(BkComment.class.getName().equals(objName)){
			BkComment object = (BkComment) obj;
			BookInfo bookInfo = new BookInfo();
			if(CommentUtils.TYPE_NETBOOK.equals(object.getResType())){
				NetBook netBook = netBookFacade.findNetBookInfoById(object.getBookId());
				bookInfo = putBKToBookInfo(netBook, TRUE);
			}else{
				BkInfo bkInfo = bkFacade.findBkInfo(object.getBookId());
				bookInfo = putBKToBookInfo(bkInfo, TRUE);
			}

			//是否在我的书单中
			BookListLink bookListLink = getResourceInfoFacade.findUserIsCollectBook(uid, bookInfo.getId(),object.getResType());
			if(bookListLink.getId() != 0){
				bookInfo.setInList(0);
				ri.setInList(0);
			}else{
				ri.setInList(1);
				bookInfo.setInList(1);
			}

			ri.setBookInfo(bookInfo);
			//如果没有封面，则使用书的封面
			if(ri.getImageUrl()==null || ri.getImageUrl().trim().length()==0){
				ri.setImageUrl(bookInfo.getPagePic());
			}
		}

		return ri;
	}


	/**
	 * 将用户转换为resource类型
	 * @param obj
	 * @param ucenterFacade
	 * @param uid
	 * @return
	 */
	public  ResourceInfo putUserInfoToResource(Object obj , UcenterFacade ucenterFacade ,
											 Long uid){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		UserEntity userEntity = copyUserInfo(obj, 0);
		ri.setUserEntity(userEntity);
		return ri;
	}

	/**
	 * 将书单类转换为首页resoureinfo实例类
	 * @param obj
	 * @param ucenterFacade
	 * @param bkFacade
	 * @param getResourceInfoFacade
	 * @return
	 */
	public  ResourceInfo putBookListToResource(BookList obj , UcenterFacade ucenterFacade ,
			BkFacade bkFacade,GetResourceInfoFacade getResourceInfoFacade,NetBookFacade netBookFacade,Long uid){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		ri = putObjectToResource(obj, ucenterFacade);
		String objName = obj.getClass().getName();

		List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(ri.getRid(), null,null);

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		if(bookListLinks.size() >0){
			BookListLink bookListLink = bookListLinks.get(0);
			BookInfo bookInfo = new BookInfo();


			if(CommentUtils.TYPE_NETBOOK.equals(bookListLink.getResType())){
				NetBook netBook = netBookFacade.findNetBookInfoById(bookListLink.getBookId());
				bookInfo = putBKToBookInfo(netBook, 0);
			}else{
				BkInfo bkInfo = bkFacade.findBkInfo(bookListLink.getBookId());
				bookInfo = putBKToBookInfo(bkInfo, 0);
			}

			BookListLink bookListLink2 = getResourceInfoFacade.findUserIsCollectBook(uid, bookInfo.getId(),bookListLink.getResType());
			if(bookListLink2.getId() != 0){
				bookInfo.setInList(TRUE);
			}
			ri.setBookInfo(bookInfo);
		}
		return ri;
	}

	/**
	 * 将推送的连载类转换为首页resoureinfo实例类
	 * @param obj
	 * @param ucenterFacade
	 * @param serializeFacade
	 * @param actFacade
	 * @return
	 */
	public  ResourceInfo putActPulishToResource(Object obj , UcenterFacade ucenterFacade ,
			SerializeFacade serializeFacade,ActFacade actFacade){
		ResourceInfo ri = new ResourceInfo();
		//调用本类公用方法
		String objName = obj.getClass().getName();
		ActPublish object = (ActPublish) obj;


		List<Serialize> serializes = serializeFacade.findAllSerialize(object.getResourceId());

		if(serializes.size()>0){
			Serialize serialize = serializes.get(0);
			ri = putObjectToResource(serialize, ucenterFacade, actFacade, serializeFacade);
			//首页连载修改
		}
		ri.setRid(object.getId());
		ri.setBtime(object.getPublishDate());

		return ri;
	}

	/**
	 * 重载com.poison.eagle.utils.FileUtils.putObjectToResource(Object, UcenterFacade)方法，
	 * 传入ResourceInfoCreator的不同实现类,策略模式的体现
	 * @param obj
	 * @param infoCreator
	 * @param ucenterFacade
	 * @return
	 */
	public ResourceInfo putObjectToResource(Object obj,ResourceInfoCreator infoCreator){
		ResourceInfo resourceInfo = infoCreator.create(obj);
		return resourceInfo;
	}
	
	/**
	 * 将object类转换为resoureinfo实例类
	 * @param obj
	 * @param ucenterFacade
	 * @return
	 */
	public  ResourceInfo putObjectToResource(Object obj ,UcenterFacade ucenterFacade){
		ResourceInfo ri = new ResourceInfo();
		String objName = "";
		if(obj != null){
			objName = obj.getClass().getName();
		}
		UserAllInfo ui = new UserAllInfo();
		BookInfo bookInfo = new BookInfo();
		Long uid = null;
		if(BookList.class.getName().equals(objName)){
			BookList object = (BookList) obj;
			ri.setBtime(object.getLatestRevisionDate());
			ri.setRid(object.getId());
			ri.setBookListId(object.getId());
//			ri.setReason(object.getReason());
//			ri.setContList(WebUtils.putHTMLToData(object.getReason()));
			ri.setType(CommentUtils.TYPE_BOOKLIST);
			ri.setTitle(object.getBookListName());
			ri.setImageUrl(CheckParams.matcherBookPic(object.getCover()));
			ri.setSummary(object.getReason());
			ri.setFlag(object.getFlag());

			uid = object.getuId();
		}
		else if(Chapter.class.getName().equals(objName)){
			//章节需要修改
			Chapter object = (Chapter) obj;
			ri.setBtime(object.getBeginDate());
			ri.setRid(object.getId());
			ri.setTitle(object.getName());
			ri.setContList(WebUtils.putChapterToData(object.getContent()));
			ri.setType(CommentUtils.TYPE_CHAPTER);
			ri.setFlag(object.getFlag());

			uid = object.getUid();
		}else if(ChapterSummary.class.getName().equals(objName)){
			ChapterSummary object = (ChapterSummary)obj;
			ri.setBtime(object.getUpdateDate());
			ri.setRid(object.getId());
			ri.setTitle(object.getName());
			ri.setType(CommentUtils.TYPE_CHAPTER);

			uid = object.getUid();
		}else if(Diary.class.getName().equals(objName)){
			Diary object = (Diary) obj;
			long begin = System.currentTimeMillis();
			if(CommentUtils.TYPE_DIARY.equals(object.getType())){
				ri.setType(CommentUtils.TYPE_DIARY);
			}else if(CommentUtils.TYPE_SHARE.equals(object.getType())){
				ri.setType(CommentUtils.TYPE_SHARE);
			}
			ri.setBtime(object.getBeginDate());
			ri.setRid(object.getId());
			ri.setTitle(null);
			ri.setContList(WebUtils.putHTMLToData(object.getContent()));
			ri.setLocationName(object.getLocationName());
			ri.setLocationArea(object.getLocationArea());
			ri.setLocationCity(object.getLocationCity());
			ri.setLon(object.getLon());
			ri.setLat(object.getLat());
			ri.setFlag(object.getFlag());
			ri.setTitle(object.getTitle());
			ri.setTags(object.getTag());
			long end = System.currentTimeMillis();
//			System.out.println("解析日志耗时:"+(end-begin));
			uid = object.getUid();
		}else if(Post.class.getName().equals(objName)){
			Post object = (Post) obj;
			ri.setBtime(object.getBeginDate());
			ri.setRid(object.getId());
//			long begin = System.currentTimeMillis();
			List<Map<String, String>> list = WebUtils.putHTMLToData(object.getName());
			for (Map<String, String> map : list) {
				String type = map.get(WebUtils.TYPE);
				if(WebUtils.TYPE_DIV.equals(type)){
					ri.setTitle(map.get(WebUtils.DATA));
				}else if(WebUtils.TYPE_IMG.equals(type)){
					ri.setImageUrl(map.get(WebUtils.DATA));
				}
			}
//			long end = System.currentTimeMillis();
//			System.out.println("解析文章题目耗时："+(end - begin));

//			ri.setContList(WebUtils.putHTMLToData(object.getContent()));
			ri.setContUrl(object.getContent());
			ri.setSummary(object.getSummary());
			ri.setType(CommentUtils.TYPE_ARTICLE);
			ri.setFlag(object.getFlag());
			ri.setReadingCount(object.getReadingCount() + "");

			uid = object.getUid();
		}else if(Article.class.getName().equals(objName)){
			Article object = (Article) obj;
			ri.setBtime(object.getBeginDate());
			ri.setRid(object.getId());
//			long begin = System.currentTimeMillis();
			ri.setTitle(object.getName());
			ri.setImageUrl(object.getCover());
//			long end = System.currentTimeMillis();
//			System.out.println("解析文章题目耗时："+(end - begin));
			String summary = HtmlUtil.getTextFromHtml(object.getContent());
			if(summary!=null){
				summary = summary.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
			}
			if(summary!=null && summary.length()>50){
				summary = summary.substring(0,50);
			}
//			ri.setContList(WebUtils.putHTMLToData(object.getContent()));
			ri.setContUrl(object.getContent());
			ri.setSummary(summary);
			ri.setType(CommentUtils.TYPE_NEWARTICLE);
			ri.setTags(object.getTag());
			ri.setFlag(object.getFlag());
			ri.setReadingCount((object.getReadingCount() + object.getFalsereading()) + "");
			ri.setAtype(object.getAtype());
			uid = object.getUid();
		}else if(BkComment.class.getName().equals(objName)){
			BkComment object = (BkComment) obj;
			ri.setBtime(object.getLatestRevisionDate());
			ri.setRid(object.getId());
			ri.setContList(WebUtils.putHTMLToData(object.getComment()));
			String resourceType = object.getResourceType();
			if(null==resourceType||"".equals(resourceType)){
				resourceType = CommentUtils.TYPE_BOOK_COMMENT;
			}
			ri.setType(resourceType);
			ri.setFlag(object.getFlag());
			ri.setScore(object.getScore());
			ri.setTitle(object.getTitle());
			ri.setBkCommentId(object.getId());
			ri.setLocationName(object.getLocationName());
			ri.setLocationCity(object.getLocationCity());
			ri.setLocationArea(object.getLocationArea());
			ri.setLon(object.getLon());
			ri.setLat(object.getLat());
			ri.setTags(object.getTag());
			ri.setImageUrl(object.getCover());
			uid = object.getUserId();
		}else if(ActPublish.class.getName().equals(objName)){
			ActPublish object = (ActPublish) obj;
			ri.setRid(object.getId());
			ri.setBtime(object.getPublishDate());
			ri.setFlag(object.getFlag());
//			ri.setType(CommentUtils.TYPE_BOOK);

			putSomeThingToResource(object, ri);

			uid = object.getUserId();
		}else if(MvComment.class.getName().equals(objName)){
			MvComment object = (MvComment) obj;
			ri.setBtime(object.getLatestRevisionDate());
			ri.setRid(object.getId());
			ri.setContList(WebUtils.putHTMLToData(object.getContent()));
			String resourceType = object.getResourceType();
			if(null==resourceType||"".equals(resourceType)){
				resourceType = CommentUtils.TYPE_MOVIE_COMMENT;
			}
			ri.setType(resourceType);
			ri.setFlag(object.getFlag());
			ri.setScore(object.getScore());
			ri.setTitle(object.getTitle());
			ri.setTags(object.getTag());
			ri.setMvCommentId(object.getId());
			ri.setLocationName(object.getLocationName());
			ri.setLocationArea(object.getLocationArea());
			ri.setLocationCity(object.getLocationCity());
			ri.setLon(object.getLon());
			ri.setLat(object.getLat());
			ri.setPoint(object.getPoint()+"");
			ri.setStageid(object.getStageid());
			ri.setTags(object.getTag());
			ri.setImageUrl(object.getCover());
			uid = object.getUserId();
		}else if(MovieTalk.class.getName().equals(objName)){
			MovieTalk object = (MovieTalk) obj;
			ri.setBtime(object.getCreateTime());
			ri.setRid(object.getId());
			ri.setContList(WebUtils.putHTMLToData(object.getContent()));
			ri.setType(CommentUtils.TYPE_MOVIE_TALK);
			ri.setFlag(object.getFlag());


			uid = object.getUid();
		}else if(MovieList.class.getName().equals(objName)){
			MovieList object = (MovieList) obj;
			ri.setBtime(object.getCreateDate());
			ri.setRid(object.getId());
//			ri.setContList(WebUtils.putHTMLToData(object.getContent()));
			ri.setType(CommentUtils.TYPE_MOVIELIST);
			ri.setTitle(object.getFilmListName());
//			ri.setReason(object.getReason());
			ri.setSummary(object.getReason());
			ri.setImageUrl(CheckParams.matcherMoviePic(object.getCover()));
			ri.setMovieListId(object.getId());
			ri.setFlag(object.getFlag());


			uid = object.getUid();
		}else if(ActCollect.class.getName().equals(objName)){
			ActCollect object = (ActCollect) obj;

			ri.setRid(object.getId());
			ri.setBtime(object.getCollectDate());
			ri.setType(CommentUtils.TYPE_COLLECT);

			uid = object.getUserId();
		}else if(ActTransmit.class.getName().equals(objName)){
			ActTransmit object = (ActTransmit) obj;

			ri.setRid(object.getId());
			ri.setBtime(object.getTransmitDate());
			ri.setType(CommentUtils.TYPE_ACT);
//			ri.setTitle(object.getTransmitContext());
			ri.setContList(WebUtils.putHTMLToData(object.getTransmitContext()));

			uid = object.getUserId();
		}else if(ResourceLink.class.getName().equals(objName)){
			ResourceLink object = (ResourceLink) obj;
			ri.setRid(object.getId());
		}else if(BookInfo.class.getName().equals(objName)){
			BookInfo object = (BookInfo) obj;
			ri.setRid(object.getId());
		}else if(MovieInfo.class.getName().equals(objName)){
			MovieInfo object = (MovieInfo) obj;
			ri.setRid(object.getId());
		}else if(ResourceInfo.class.getName().equals(objName)){
			ri = (ResourceInfo) obj;
		}else if(GraphicFilm.class.getName().equals(objName)){
			GraphicFilm object = (GraphicFilm) obj;
			ri.setRid(object.getId());
			ri.setTitle(object.getTitle());
			ri.setImageUrl(object.getCover());
			ri.setImages(object.getContent());
			ri.setBtime(object.getCreateDate());
			ri.setSummary(object.getDescription());
			ri.setType(CommentUtils.TYPE_GRAPHIC_FILM);

			uid = object.getUid();
		}else if(Topic.class.getName().equals(objName)){
			Topic topic = (Topic)obj;
			ri.setRid(topic.getId());
			ri.setTitle(topic.getTitle());
			ri.setImageUrl(topic.getCover());
			ri.setBtime(topic.getCreateDate());
			ri.setSummary(topic.getDescription());
			ri.setType(CommentUtils.TYPE_TOPIC);
			ri.setReadingCount((topic.getReadcount()+topic.getFalsereading())+"");
			uid = topic.getUserid();
		}

		if(uid != null && uid != 0 && ucenterFacade != null){
			ui = ucenterFacade.findUserInfo(null, uid);
			ri.setUserEntity(copyUserInfo(ui,FALSE));
		}
		return ri;
	}
	

	/**
	 * 将推送类型中的某些属性放入资源类中
	 * @param actPublish
	 * @param resourceInfo
	 */
	public void putSomeThingToResource(ActPublish actPublish,ResourceInfo resourceInfo){
		if(resourceInfo == null|| actPublish == null || resourceInfo.getRid() == 0){
			return ;
		}
		String html = actPublish.getPublishContext();
		List<Map<String, String>> map = WebUtils.putHTMLToData(html);
		String imageUrl = "";
		for (Map<String, String> map2 : map) {
			String type = map2.get(WebUtils.TYPE);
			if(WebUtils.TYPE_IMG.equals(type)){
				imageUrl = map2.get(WebUtils.DATA);
			}
		}
		resourceInfo.setImageUrl(imageUrl);
	}

	/**
	 * 将图书转化为客户端可以显示的类型
	 * @param isList 0：列表、1：单本书
	 * @return
	 */
	public  BookInfo putBKToBookInfo(Object obj , int isList){
		String objName = obj.getClass().getName();
		BookInfo bookInfo = new BookInfo();

		if(BkInfo.class.getName().equals(objName)){
			BkInfo bkInfo = (BkInfo) obj;

			bookInfo.setId(bkInfo.getId());
			bookInfo.setName(bkInfo.getName());
			bookInfo.setAuthorName(bkInfo.getAuthorName());
			//bookInfo.setIsDB(TRUE);
			bookInfo.setPagePic(bkInfo.getBookPic());
			bookInfo.setType(CommentUtils.TYPE_BOOK);
			bookInfo.setScore(bkInfo.getScore());
			bookInfo.setPublishTime(bkInfo.getPublishingTime());
			bookInfo.setPress(bkInfo.getPress());

			if(isList == FALSE){
				bookInfo.setPress(bkInfo.getPress());
				bookInfo.setIntroduction(bkInfo.getContent());
				bookInfo.setSales(bkInfo.getSalesVolume());
				bookInfo.setRank(bkInfo.getRankingList());

				//出版信息
				bookInfo.setPages(bkInfo.getNumber());
				bookInfo.setBinding(bkInfo.getBinding());
				bookInfo.setCatalog(bkInfo.getCatalog());
				bookInfo.setIsbn13(bkInfo.getIsbn());
				bookInfo.setPrice(bkInfo.getPrice());
			}
		}else if(NetBook.class.getName().equals(objName)){
			NetBook netBook = (NetBook) obj;

			bookInfo.setId((int)netBook.getId());
			bookInfo.setName(netBook.getName());
			bookInfo.setAuthorName(netBook.getAuthorName());
			bookInfo.setPagePic(netBook.getPagePicUrl());
			bookInfo.setType(CommentUtils.TYPE_NETBOOK);
			bookInfo.setSortId(netBook.getBookId());
			if(isList == FALSE){
				bookInfo.setIntroduction(netBook.getIntroduction());
			}
		}

		return bookInfo;
	}

	/**
	 * 将书单转换成客户端可以显示的类型
	 * @param obj
	 * @return
	 */
	public  BookListInfo putObjToBookListInfo(BookList obj,ActFacade actFacade,
			GetResourceInfoFacade getResourceInfoFacade,BkFacade bkFacade,UcenterFacade ucenterFacade,
			MyBkFacade myBkFacade,NetBookFacade netBookFacade,Long uid){
		BookListInfo bookListInfo = new BookListInfo();

		bookListInfo.setId(obj.getId());
		bookListInfo.setName(obj.getBookListName());
		bookListInfo.setReason(obj.getReason());
		bookListInfo.setIsDefault(obj.getType());
		bookListInfo.setFristBookPicUrl(obj.getCover());
		bookListInfo.setTags(obj.getTag());
//		//带有分类的标签列表
//		List<String> tags = bookListInfo.getTags();
//		Map<String, String> tagMap = new HashMap<String, String>();
//		for (String string : tags) {
//			Tag tag = myBkFacade.findTaggroupByTagName(string, CommentUtils.TYPE_MOVIELIST);
//			String type = "";
//			if(tag == null || tag.getId() == 0){
//				type = "";
//			}else{
//				type = tag.getTagGroup();
//			}
//			try {
//				tagMap.put(string,type);
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e.fillInStackTrace());
//			}
//		}
//		bookListInfo.setTagInfo(tagMap);

		//评论数量
		//现已注释掉
		int cNum= actFacade.findCommentCount(null, obj.getId());
		bookListInfo.setcNum(cNum);
		//转发数量
		//现已注释掉
		/*int rNum = actFacade.findTransmitCount(null, obj.getId());
		bookListInfo.setrNum(rNum);*/
		//是否收藏过
		ActCollect actCollect = actFacade.findCollectIsExist(uid, obj.getId());
		if(actCollect.getId() != 0){
			bookListInfo.setIsCollect(CheckParams.changeIntForTrueFalse(actCollect.getIsCollect()));
		}

		//是否赞过
		//现已注释掉
		ActPraise actPraise = actFacade.findActPraise(uid, bookListInfo.getId());
		if(actPraise != null && actPraise.getFlag() == ResultUtils.SUCCESS){
			bookListInfo.setIsPraise(changeIntForTrueFalse(actPraise.getIsPraise()));
		}
		//赞的数量
		//现已注释掉
		int zNum= actFacade.findPraiseCount(null, bookListInfo.getId());
		bookListInfo.setzNum(zNum);

		//将推荐书单的用户放入到书单中
		if(ucenterFacade != null){
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, obj.getuId());
			UserEntity userEntity = copyUserInfo(userAllInfo, FALSE);
			bookListInfo.setUserEntity(userEntity);
		}

		//将书单中书的数量和第一本书封面添加进去
		List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(obj.getId(), null,null);
		if(bookListLinks.size() > 0){
			if(bookListLinks.get(0).getId() != 0){

//				Collections.sort(bookListLinks);
//				Collections.reverse(bookListLinks);//正序
				int listSize = bookListLinks.size();
//				//暂时留用
				BkInfo bkInfo1 = bkFacade.findBkInfo(bookListLinks.get(0).getBookId());
				BookInfo bookInfo1 = putBKToBookInfo(bkInfo1, FALSE);
				bookListInfo.setFristBookPicUrl(bookInfo1.getPagePic());

//				MovieInfo movieInfo = putMVLinkToMovieInfo(mvListLinks.get(0), null, null, mvFacade);
//				movieListInfo.setFirstMoviePic(movieInfo.getMoviePic());
				//将书单中书的数量放入进去
				int count = 0;
				Iterator<BookListLink> iter = bookListLinks.iterator();
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
				bookListInfo.setSize(count);

				//将书单中的书都加入
//				List<BookInfo> bookInfos = new ArrayList<BookInfo>();
//				for (BookListLink blk : bookListLinks) {
//					BkInfo bkInfo2 = bkFacade.findBkInfo(blk.getBookId());
//					BookInfo bookInfo2 = putBKToBookInfo(bkInfo2, TRUE);
//					bookInfos.add(bookInfo2);
//				}
//				bookListInfo.setList(bookInfos);



				//前五个封面
//				int size = 5;
//				if(listSize<size){
//					size = listSize;
//				}
//				List<String> picUrl = new ArrayList<String>();
//				for (int i = 0; i < size; i++) {
//					BkInfo bkInfo = bkFacade.findBkInfo(bookListLinks.get(i).getBookId());
//					BookInfo bookInfo = putBKToBookInfo(bkInfo, FALSE);
//					picUrl.add(bookInfo.getPagePic());
//				}
//				bookListInfo.setBookPicList(picUrl);
			}
		}

		if(obj != null && !"".equals(obj.getCover())){
			bookListInfo.setFristBookPicUrl(obj.getCover());
		}

//		putMoneyToBookList(bookListInfo, paycenterFacade);

		return bookListInfo;
	}

	public BookListInfo putBookListToBookListInfo(BookList obj,ActFacade actFacade,
			GetResourceInfoFacade getResourceInfoFacade,BkFacade bkFacade,UcenterFacade ucenterFacade,
			MyBkFacade myBkFacade,NetBookFacade netBookFacade,Long uid){
		BookListInfo bookListInfo = new BookListInfo();

		bookListInfo.setId(obj.getId());
		bookListInfo.setName(obj.getBookListName());
		bookListInfo.setReason(obj.getReason());
		bookListInfo.setIsDefault(obj.getType());
		bookListInfo.setFristBookPicUrl(obj.getCover());
		bookListInfo.setTags(obj.getTag());

		Map<String, Object> map = getResourceInfoFacade.findBookLinkCount(obj.getId());
		int count = (Integer) map.get("count");
		bookListInfo.setSize(count);
		return bookListInfo;
	}
	
	/**
	 * 将每条资源的得到的金钱数放入到书单类中
	 * @param ri
	 * @param uid
	 * @param paycenterFacade
	 * @return
	 */
	public void putMoneyToBookList(BookListInfo bi ,PaycenterFacade paycenterFacade){
		if(bi == null || bi.getId() == 0 || paycenterFacade == null){
			return;
		}

		RewardStatistical rewardStatistical = paycenterFacade.getMoneyBySourceId(bi.getId());
		if(rewardStatistical.getId() != 0){
			bi.setMoney(rewardStatistical.getTotalAmt());
		}

	}

	/**
	 * 将评论实例转换为统一的客户端可以显示的类型
	 * @param obj 需要转换的评论实例类
	 * @param ucenterFacade
	 * @return
	 */
	public  CommentInfo putObjToComment(Object obj,UcenterFacade ucenterFacade,ActFacade actFacade,Long uid){
		CommentInfo commentInfo = new CommentInfo();
		String objName = obj.getClass().getName();
		UserAllInfo userAllInfo = new UserAllInfo();

		if(ActComment.class.getName().equals(objName)){
			ActComment object = (ActComment) obj;
			if(object != null && object.getId() != 0){
				commentInfo.setId(object.getId());
				commentInfo.setIdStr(object.getId()+"");
				commentInfo.setContent(object.getCommentContext().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				commentInfo.setBtime(object.getCommentDate());
				commentInfo.setContList(WebUtils.putHTMLToData(object.getCommentContext()));
				userAllInfo = ucenterFacade.findUserInfo(null, object.getUserId());
				//资源id
				Long commentId = object.getCommentId();
				if(commentId != null && commentId != 0){
					commentInfo.setCommentId(commentId);
					commentInfo.setCommentIdStr(commentId+"");
				}else{
					commentInfo.setCommentId(object.getId());
					commentInfo.setCommentIdStr(object.getId()+"");
				}
				//被评论人
				Long toUid  = object.getCommentUserId();
				if(toUid != null && toUid != 0 && commentId != null && commentId != 0){
					UserAllInfo toUserInfo = ucenterFacade.findUserInfo(null, object.getCommentUserId());
					commentInfo.setToUserEntity(copyUserInfo(toUserInfo, FALSE));
				}
				//评论数量
				int cNum = actFacade.findCommentCount(null, object.getId());
				commentInfo.setcNum(cNum);
				//赞的数量
				Map<String,Object> map= actFacade.findUsefulCount(object.getId());
				int zNum = (Integer)map.get("usefulCount");
				//actFacade.findPraiseCount(null, object.getId());
				commentInfo.setzNum(zNum);
				//是否赞过
				ActUseful actUseful = actFacade.findUsefulByResidAndUserid(object.getId(),uid);
				//ActPraise actPraise = actFacade.findActPraise(uid, object.getId());
				if(actUseful != null && actUseful.getFlag() == ResultUtils.SUCCESS){
					commentInfo.setIsPraise(CheckParams.changeIntForTrueFalse(actUseful.getIsUseful()));
					//commentInfo.setIsPraise(CheckParams.changeIntForTrueFalse(actUseful.getIsPraise()));
				}
			}
		}else if(ActPraise.class.getName().equals(objName)){
			ActPraise actPraise = (ActPraise) obj;
			if(actPraise != null && actPraise.getId() != 0){
				commentInfo.setId(actPraise.getId());
				commentInfo.setIdStr(actPraise.getId()+"");
				commentInfo.setBtime(actPraise.getLatestRevisionDate());

				userAllInfo = ucenterFacade.findUserInfo(null, actPraise.getUserId());
			}
		}else if(ActUseful.class.getName().equals(objName)){
			ActUseful actUseful = (ActUseful) obj;
			if(actUseful!=null&&actUseful.getId()!=0){
				commentInfo.setId(actUseful.getId());
				commentInfo.setIdStr(actUseful.getId()+"");
				commentInfo.setBtime(actUseful.getLatestRevisionDate());
				userAllInfo = ucenterFacade.findUserInfo(null, actUseful.getUserId());
			}
		}

		commentInfo.setUserEntity(copyUserInfo(userAllInfo, FALSE));

		return commentInfo;
	}
	
	/**
	 * 将豆瓣上获取到的json数据转换为bkinfo实例类
	 * @param data
	 * @return
	 */
	public  BkInfo putDataToBKInfo(Map<String, Object> data){
		BkInfo bkInfo = new BkInfo();
		String isbn13 = null;
		String isbn10 = null;
		int id = 0;
		try{
			id = Integer.valueOf(data.get(BOOK_ID).toString());
		}catch (Exception e) {
			id = 0;
		}
		String alt = CheckParams.objectToStr((String) data.get(BOOK_ALT));
		String image = CheckParams.objectToStr((String) data.get(BOOK_IMAGE));
		String title = CheckParams.objectToStr((String) data.get("title"));
		String average = CheckParams.objectToStr((String) data.get(BOOK_AVERAGE));
		String publisher = CheckParams.objectToStr((String) data.get(BOOK_PUBLISHER));
		String origin_title = CheckParams.objectToStr((String) data.get(BOOK_ORIGIN_TITLE));
		String subtitle = CheckParams.objectToStr((String) data.get(BOOK_SUBTITLE));
		String pubdate = CheckParams.objectToStr((String) data.get(BOOK_PUBDATE));
		String pageStr = CheckParams.objectToStr(data.get(BOOK_PAGES).toString().replaceAll("[^0-9]", ""));
		if(pageStr == null || "".equals(pageStr)){
			pageStr = "0";
		}
		int pages = Integer.valueOf(pageStr);
		String price = CheckParams.objectToStr((String) data.get(BOOK_PRICE));
		String binding = CheckParams.objectToStr((String) data.get(BOOK_BINDING));

		//作者
		StringBuffer authors = new StringBuffer();
		try{
			List<String> author = (List<String>) data.get(BOOK_AUTHOR);
			for (int i = 0; i < author.size(); i++) {
				authors.append(author.get(i));
				if(i != author.size()-1 && !"".equals(author.get(i))){
					authors.append(",");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		StringBuffer t = new StringBuffer();
		try{
			//翻译
			List<String> translator = (List<String>) data.get(BOOK_TRANSLATOR);
			for (int i = 0; i < translator.size(); i++) {
				t.append(translator.get(i));
				if(i != translator.size()-1 && !"".equals(translator.get(i))){
					t.append(",");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		StringBuffer tag = new StringBuffer();
		try{
			//标签
			List<String> tags = (List<String>) data.get(BOOK_TAGS);
			System.out.println(tags);
			try{
				for (int i = 0; i < tags.size(); i++) {
					tag.append(tags.get(i));
					if(i != tags.size()-1 && !"".equals(tags.get(i))){
						tag.append(",");
					}
				}

			}catch (Exception e) {
				e.printStackTrace();
			}

		}catch (Exception e) {
			// TODO: handle exception
		}
		String summary = CheckParams.objectToStr((String) data.get(BOOK_SUMMARY));
		String author_intro = CheckParams.objectToStr((String) data.get(BOOK_AUTHOR_INTRO));
		String catalog = CheckParams.objectToStr((String) data.get(BOOK_CATALOG));

		if(data.get(BOOK_ISBN13)!=null){
			 isbn13 = data.get(BOOK_ISBN13).toString();
		}else{
			 isbn13 = "";
		}
		if(data.get(BOOK_ISBN10)!=null){
		      isbn10 = data.get(BOOK_ISBN10).toString();
		}else{
			 isbn10= "";
		}

		bkInfo.setId(id);
		bkInfo.setBookUrl(alt);
		bkInfo.setBookPic(image);
		bkInfo.setName(title);
		bkInfo.setScore(average);
		bkInfo.setAuthorName(authors.toString());
		bkInfo.setTranslator(t.toString());
		bkInfo.setPress(publisher);
		bkInfo.setOriginalName(origin_title);
		bkInfo.setSubtitle(subtitle);
		bkInfo.setPublishingTime(pubdate);
		bkInfo.setNumber(pages);
		bkInfo.setPrice(price);
		bkInfo.setBinding(binding);
		bkInfo.setTags(tag.toString());
		bkInfo.setContent(CheckParams.replaceN(summary));
		bkInfo.setAuthorInfo(CheckParams.replaceN(author_intro));
		bkInfo.setCatalog(CheckParams.replaceN(catalog));
		bkInfo.setSeriesName("");
		bkInfo.setSeriesInfo("");
		bkInfo.setRankingList("");
		bkInfo.setSalesVolume("");

		if(isbn13 == null || "".equals(isbn13)){
			bkInfo.setIsbn(isbn10);
		}else{
			bkInfo.setIsbn(isbn13);
		}

		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmm");
		bkInfo.setCollTime((int) (Calendar.getInstance().getTimeInMillis()/1000));

		return bkInfo;
	}
	
	public  Long getUserId(){
		String uid = ProductContextHolder.getProductContext().getProductUser().getUserId();
		if(uid == null ||"".equals(uid)){
			return null;
		}
		return Long.valueOf(uid);
	}

	public  int changeIntForTrueFalse(int isTrue){
		int tmp = 0;
		if(isTrue == 0){
			tmp =  1;
		}else if(isTrue == 1){
			tmp =  0;
		}

		return tmp;
	}
	
	/**
	 * 写入文件
	 * @param path
	 * @param content
	 */
	public int writeFile(String path, String content) {
//		FileOutputStream fop = null;
		File file;
		Writer outTxt = null;
		int flag = ResultUtils.ERROR;
		try {

			file = new File(path);
//			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
//			byte[] contentInBytes = content.getBytes();
//			fop.write(contentInBytes);
//			fop.flush();
//			fop.close();

			outTxt = new OutputStreamWriter(new FileOutputStream(file,true), "UTF-8");

	        outTxt.write(content);
	        outTxt.flush();

			flag = ResultUtils.SUCCESS;
		} catch (IOException e) {
			e.printStackTrace();
			flag = ResultUtils.INSERT_ERROR;
		} finally {
			try {
				if (outTxt != null) {
					outTxt.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return flag;
	}

	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}
	
	/**
	 * 创建小说评论的评论
	 * @param obj
	 * @param storyUserFacade
	 * @param actFacade
	 * @param uid
	 * @return
	 */
	public CommentInfo putObjToStoryComment(Object obj, StoryUserFacade storyUserFacade, ActFacade actFacade,
			Long uid) {
		CommentInfo commentInfo = new CommentInfo();
		String objName = obj.getClass().getName();
		StoryUser userAllInfo = new StoryUser();

		if(ActComment.class.getName().equals(objName)){
			ActComment object = (ActComment) obj;
			if(object != null && object.getId() != 0){
				commentInfo.setId(object.getId());
				commentInfo.setIdStr(object.getId()+"");
				commentInfo.setContent(object.getCommentContext().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				commentInfo.setBtime(object.getCommentDate());
//				HashMap map = new HashMap(StoryUtils.str2Data(object.getCommentContext()));
				commentInfo.setContList(StoryUtils.str2DataNoInfer(object.getCommentContext()));
				userAllInfo = storyUserFacade.findStoryUserByUserid(object.getUserId());
				//资源id
				Long commentId = object.getCommentId();
				if(commentId != null && commentId != 0){
					commentInfo.setCommentId(commentId);
					commentInfo.setCommentIdStr(commentId+"");
				}else{
					commentInfo.setCommentId(object.getId());
					commentInfo.setCommentIdStr(object.getId()+"");
				}
				//被评论人
				Long toUid  = object.getCommentUserId();
				if(toUid != null && toUid != 0 && commentId != null && commentId != 0){
					StoryUser toUserInfo = storyUserFacade.findStoryUserByUserid(object.getCommentUserId());
					commentInfo.setToUserEntity(copyStoryUserInfo(toUserInfo));
				}
				//评论数量
				int cNum = actFacade.findCommentCount(null, object.getId());
				commentInfo.setcNum(cNum);
				//赞的数量
				Map<String,Object> map= actFacade.findUsefulCount(object.getId());
				int zNum = (Integer)map.get("usefulCount");
				//actFacade.findPraiseCount(null, object.getId());
				commentInfo.setzNum(zNum);
				//是否赞过
				ActUseful actUseful = actFacade.findUsefulByResidAndUserid(object.getId(),uid);
				//ActPraise actPraise = actFacade.findActPraise(uid, object.getId());
				if(actUseful != null && actUseful.getFlag() == ResultUtils.SUCCESS){
					commentInfo.setIsPraise(CheckParams.changeIntForTrueFalse(actUseful.getIsUseful()));
					//commentInfo.setIsPraise(CheckParams.changeIntForTrueFalse(actUseful.getIsPraise()));
				}
			}
		}else if(ActPraise.class.getName().equals(objName)){
			ActPraise actPraise = (ActPraise) obj;
			if(actPraise != null && actPraise.getId() != 0){
				commentInfo.setId(actPraise.getId());
				commentInfo.setIdStr(actPraise.getId()+"");
				commentInfo.setBtime(actPraise.getLatestRevisionDate());

				userAllInfo = storyUserFacade.findStoryUserByUserid(actPraise.getUserId());
			}
		}else if(ActUseful.class.getName().equals(objName)){
			ActUseful actUseful = (ActUseful) obj;
			if(actUseful!=null&&actUseful.getId()!=0){
				commentInfo.setId(actUseful.getId());
				commentInfo.setIdStr(actUseful.getId()+"");
				commentInfo.setBtime(actUseful.getLatestRevisionDate());
				userAllInfo = storyUserFacade.findStoryUserByUserid(actUseful.getUserId());
			}
		}

		commentInfo.setUserEntity(copyStoryUserInfo(userAllInfo));

		return commentInfo;
	}
	
	/**
	 * 转换小说用户到UserEntity
	 * @param userInfo
	 * @return
	 */
	private UserEntity copyStoryUserInfo(StoryUser userInfo) {
		UserEntity entity = new UserEntity();
		entity.setId(userInfo.getUserId());
		entity.setNickName(userInfo.getName());
		entity.setFace_url(userInfo.getFaceAddress());
		return entity;
	}
	 
	 
}
