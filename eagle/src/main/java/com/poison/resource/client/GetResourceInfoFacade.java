package com.poison.resource.client;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.store.model.BkInfo;

/**
 * 
 * 类的作用:该类的功能主要是完成与service层的操作
 * 作者:闫前刚
 * 创建时间:2014-7-15下午4:55:54
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface GetResourceInfoFacade {
	/**
	 * 
	 * 方法的描述 :插入单本书(库中有)
	 * @param book
	 */
	//public int addHaveBook(ProductContext productContext,long uid,long rid,long id,String name,String reason,String content);
	/**
	 * 
	 * 方法的描述 :插入单本书(库中无)
	 * @param book
	 */
	//public int addNoBook(ProductContext productContext,long uid,String name,String reason);
	
	/**
	 * 
	 * 方法的描述 :该方法主要是新建书单
	 * @param booklist
	 */
	public int addNewBookList(ProductContext productContext,String bookListName,String reason,long uId,String tag);
	
	/**
	 * 
	 * <p>Title: addServerBookList</p> 
	 * <p>Description: 添加系统推荐的书</p> 
	 * @author :changjiang
	 * date 2014-8-21 下午3:29:51
	 * @param bookListName
	 * @param reason
	 * @return
	 */
	public int addServerBookList(String bookListName,String reason,String tag);
	
	/**

	 */
	//public int addNewBookList(ProductContext productContext,long uid,String name,String reason,String content,List<Map> list);
	/**

	 * 
	 * <p>Title: addDefaultBookList</p> 
	 * <p>Description: 创建默认书单列表</p> 
	 * @author :changjiang
	 * date 2014-8-8 上午10:37:12
	 * @param productContext
	 * @param booklistName
	 * @param uId
	 * @return
	 */
	public int addDefaultBookList(ProductContext productContext,long uId);
	
	/**
	 * 
	 * <p>Title: findUserIsCollectBook</p> 
	 * <p>Description: 查询用户是否收藏了这本书</p> 
	 * @author :changjiang
	 * date 2014-10-17 下午5:39:23
	 * @param uId
	 * @param bookId
	 * @return
	 */
	public BookListLink findUserIsCollectBook(long uId,long bookId,String resType);
	
	/**
	 * 
	 * <p>Title: addWantReadBookList</p> 
	 * <p>Description: 添加想读的书单</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午10:03:56
	 * @param productContext
	 * @param uId
	 * @return
	 */
	public int addWantReadBookList(ProductContext productContext,long uId);
	
	/**
	 * 
	 * <p>Title: addIsReadingBookList</p> 
	 * <p>Description: 添加正在读的书单</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午10:04:38
	 * @param productContext
	 * @param uId
	 * @return
	 */
	public int addIsReadingBookList(ProductContext productContext,long uId);
	
	/**
	 * 
	 * <p>Title: addIsCollectedBookList</p> 
	 * <p>Description: 添加我的书架藏书</p> 
	 * @author :changjiang
	 * date 2015-2-3 下午5:31:42
	 * @param productContext
	 * @param uId
	 * @return
	 */
	public int addIsCollectedBookList(ProductContext productContext,long uId);
	
	/**
	 * 
	 * 方法的描述 :该方法主要是老书单中添加
	 * @param id 书单的ID
	 * @param name	书单的名称
	 * @param reason 推荐语
	 * @param content 评论
	 * @param list 书单中的书目
	 * @return
	 */
	//public int addOldBookList(ProductContext productContext,long uid,long id,String name,String reason,String content,List<Map> list);
	/**
	 * 
	 * 方法的描述 :查询图书表中是否存在该图书 模糊查询
	 * @param name
	 * @return
	 */
	//public List<Book> findBook(ProductContext productContext,String name);
	/*
	 * 
	 * 方法的描述 :根据用户名查询
	 * @param productContext
	 * @param uid 用户名
	 * @return
	 */
	public List<BookList> findBookList(ProductContext productContext,long uid);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改书单名称
	 * @param id
	 * name 书单名称
	 * @return
	 */
	public int updateBookList(long id,String name,String reason,String cover,String tag);
	
	/**
	 * 
	 * <p>Title: updateBookListPic</p> 
	 * <p>Description: 更新书单封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午4:40:07
	 * @param id
	 * @param cover
	 * @return
	 */
	public BookList updateBookListPic(long id, String cover);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除书单
	 * @param id
	 * @return
	 */
	public int deleteBookList(long id);
	
	/**
	 * 
	 * <p>Title: publishBookList</p> 
	 * <p>Description: 发布书单</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午9:54:50
	 * @param id
	 * @return
	 */
	public int publishBookList(long id);
	//public List<BookList> findBookList(ProductContext productContext,long uid);
	
	/**
	 * 
	 * <p>Title: findLatestBookListByDate</p> 
	 * <p>Description: 根据时间查最新书单内容</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午3:28:21
	 * @return
	 */
	public List<BookList> findLatestBookList(Long resId);
	
	/**
	 * 
	 * <p>Title: findBookListByType</p> 
	 * <p>Description: 根据</p> 
	 * @author :changjiang
	 * date 2014-10-31 上午11:08:04
	 * @param bookListId
	 * @param type
	 * @return
	 */
	public List<BookListLink> findBookListByType(long bookListId,String type);
	
	/**
	 * 
	 * <p>Title: findServerBookLists</p> 
	 * <p>Description: 查询系统推荐的书单</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午5:51:27
	 * @param tags
	 * @param resId
	 * @return
	 */
	public List<BookList> findServerBookLists(String tags, Long resId) ;
	
	/**
	 * 
	 * <p>Title: findLatestBookListById</p> 
	 * <p>Description: 根据ID查最新书单信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午3:29:00
	 * @param resourceId
	 * @return
	 */
	//public List<BookList> findLatestBookListById(long resourceId);

	
	/**
	 * 
	 * <p>Title: findBookListByUsersId</p> 
	 * <p>Description: 根据用户ID查询书单列表</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午7:55:42
	 * @param usersIdList
	 * @return
	 */
	//public List<BookList> findBookListByUsersId(List<Long> usersIdList,Long resId);

	/**
	 * 
	 * 方法的描述 :此方法的作用是查询书单目录
	 * @param id 根据主键id
	 * @return
	 */
	public BookList queryByIdBookList(long id);

	/**
	 * 
	 * <p>Title: addBookToList</p> 
	 * <p>Description: 向书单中添加一本书</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午3:27:25
	 * @param bookListId
	 * @return
	 */
	public int addBookToList(long bookListId,int bookId,int isDb,String resType);
	
	/**
	 * 
	 * <p>Title: addMyBookToList</p> 
	 * <p>Description: 书单中添加</p> 
	 * @author :changjiang
	 * date 2014-8-9 上午12:34:18
	 * @param name
	 * @return
	 */
	public int addMyBookToList(String name,long bookListId,long userId,String resType);
	/**
	 * 
	 * <p>Title: findBookListInfo</p> 
	 * <p>Description: 查询书单详情</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午7:52:59
	 * @param bookListId
	 * @param bookId
	 * @return
	 */
	public List<BookListLink> findBookListInfo(long bookListId,Long resId,Integer pageSize);
	
	/**
	 * 
	 * <p>Title: updateBookListLink</p> 
	 * <p>Description: 更新一个书单中的一</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:11:25
	 * @param bookLink
	 * @return
	 */
	public int deleteBookListLink(long bookListId,long bookId,String resType);
	
	/**
	 * 
	 * <p>Title: findDefaultBookList</p> 
	 * <p>Description: 查询用户的默认书单</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午4:59:21
	 * @param userId
	 * @return
	 */
	public List<BookList> findDefaultBookList(long userId);
	
	/**
	 * 
	 * <p>Title: moveOneBook</p> 
	 * <p>Description: 移动一本书</p> 
	 * @author :changjiang
	 * date 2014-10-20 上午10:54:15
	 * @param bookId
	 * @param bookListId
	 * @return
	 */
	public BookListLink moveOneBook(long bookId,long bookListId,long userId,String resType,String scan);
	
	/**
	 * 
	 * <p>Title: updateBookListLinkRemark</p> 
	 * <p>Description: 更新书的备注</p> 
	 * @author :changjiang
	 * date 2014-11-15 上午10:35:59
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @param id
	 * @return
	 */
	public BookListLink updateBookListLinkRemark(String friendinfo,
			String address, String tags, long id,String description);
	
	/**
	 * 
	 * <p>Title: findBookLinkIsExistById</p> 
	 * <p>Description: 根据id查询图书是否存在</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:41:05
	 * @param id
	 * @return
	 */
	public BookListLink findBookLinkIsExistById(long id);
	
	/**
	 * 
	 * <p>Title: addBookToActivityList</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2015-2-4 下午1:47:28
	 * @param activityListName
	 * @param bookId
	 * @param resType
	 * @return
	 */
	public BookListLink addBookToActivityList(String activityListName,long userId,int bookId,String resType,String scan);
	/**
	 * 
	 * <p>Title: queryUserBookListByName</p> 
	 * <p>Description: 根据书单名查询用户书单</p> 
	 * @author :changjiang
	 * date 2014-8-28 上午11:17:10
	 * @return
	 */
	public BookList queryUserBookListByName(BookList bookList);
	/**
	 * 查询书单中的书的数量
	 */
	public long getBookCountByListId(long listid);
	/**
	 * 根据索引查询某个书单中的书的信息列表
	 */
	public List<BookListLink> getBookListLinkByStartIndex(long listid, long start,int pageSize);
	/**
	 * 
	 * <p>Title: addNoLikeBookList</p> 
	 * <p>Description: 我不喜欢的书</p> 
	 * @author :changjiang
	 * date 2015-4-22 下午1:38:53
	 * @param productContext
	 * @param uId
	 * @return
	 */
	public int addNoLikeBookList(ProductContext productContext, long uId);
	
	/**
	 * 
	 * <p>Title: findBookLinkCount</p> 
	 * <p>Description: 查询书单有多少部书</p> 
	 * @author :changjiang
	 * date 2015-5-12 下午6:02:52
	 * @param bookListId
	 * @return
	 */
	public Map<String, Object> findBookLinkCount(long bookListId);
	/**
	 * 根据书单id集合查询书单列表
	 * @param bklistids
	 * @return
	 */
	public List<BookList> findBookListsByIds(List<Long> bklistids);
	
}
