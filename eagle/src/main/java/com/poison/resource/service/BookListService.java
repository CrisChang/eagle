package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;

/**
 * 
 * 类的作用:对DAO层进行持久化操作
 * 作者:闫前刚
 * 创建时间:2014-7-14下午7:29:48
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface BookListService {
	/**
	 * 
	 * 方法的描述 :对书单进行查选操作
	 * @return
	 */
	public List<BookList> findBookList(ProductContext productContext,long uid);
	/**
	 * 
	 * 方法的描述 :该方法作用是添加单本书
	 * @param productContext 产品号
	 * @param uid 用户名
	 * @param name 图书名
	 * @param reason 推荐理由
	 * @return
	 */
	public int singleBookList(ProductContext productContext,long uid,String name,String reason);
	/**
	 * 
	 * 方法的描述 :该方法作用是向书单中单本书
	 * @param productContext 产品号
	 * @param uid  用户名
	 * @param name 图书名
	 * @param reason 推荐理由
	 * @return
	 */
	public int singleList(ProductContext productContext,long uid,long rid,String name,String reason);
	/**
	 * 
	 * 方法的描述 :该方法的作用新建书单
	 * @param productContext 产品号
	 * @param uid 用户名
	 * @param name 图书名
	 * @param reason 推荐理由
	 * @param content 评论
	 * @return
	 */
	public int addBookList(ProductContext productContext,BookList bookList);
	/**
	 * 
	 * 方法的描述 :该方法的作用是模糊查询图书表中的信息
	 * @param productContext 产品号
	 * @param name 图书名
	 * @return
	 */
	//public List<Book> findBook(ProductContext productContext,String name);
	/**
	 * 
	 * 方法的描述 :该方法的作用是添加库中有的书
	 * @param id 
	 * @param name 图书名称
	 * @param reason 推荐理由
	 * @param content 评论
	 * @return
	 */
	public int addBook(ProductContext productContext,long uid,long id,long rid,String name,String reason,String content);
	
	/**
	 * 
	 * 方法的描述 :该方法的作用是向书单总添加一本书（库中有）
	 * @param productContext 产品号
	 * @param uid 用户名
	 * @param name 图书名
	 * @param reason 推荐理由
	 * @param content 评论
	 * @return
	 *//*
	public int addOneBook(ProductContext productContext,long uid,String name,String reason,String content);
	*/
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改书单
	 * @param booklist
	 * @return
	 */
	public int updateBookList(BookList booklist);
	
	/**
	 * 
	 * <p>Title: updateBookListPic</p> 
	 * <p>Description: 更新书单封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午4:37:29
	 * @param id
	 * @param cover
	 * @return
	 */
	public BookList updateBookListPic(long id, String cover);
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除书单
	 * @param booklist
	 * @return
	 */
	public int deleteBookList(BookList booklist);
	
	/**
	 * 
	 * <p>Title: findLatestBookListByDate</p> 
	 * <p>Description: 查询最新信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午3:22:24
	 * @return
	 */
	public List<BookList> findLatestBookList(Long resId);
	
	/**
	 * 
	 * <p>Title: findServerBookLists</p> 
	 * <p>Description: 查询系统推荐的书单</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午5:49:46
	 * @param tags
	 * @param resId
	 * @return
	 */
	public List<BookList> findServerBookLists(String tags, Long resId);
	
	/**
	 * 
	 * <p>Title: findLatestBookListById</p> 
	 * <p>Description: 根据ID查询书单最新信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午3:25:28
	 * @param resourceId
	 * @return
	 */
	public List<BookList> findLatestBookListById(long resourceId);

	
	/**
	 * 
	 * <p>Title: findBookListByUsersId</p> 
	 * <p>Description: 根据用户ID查询书单信息</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午7:49:36
	 * @param usersIdList
	 * @return
	 */
	public List<BookList> findBookListByUsersId(List<Long> usersIdList,Long resId);

	
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询书单目录
	 * @param id
	 * @return
	 */
	public BookList queryByIdBookList(long id);
	
	/**
	 * 
	 * <p>Title: addBookToList</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午2:50:51
	 * @param bookLink
	 * @return
	 */
	public int addBookToList(BookListLink bookLink);
	
	/**
	 * 
	 * <p>Title: findBookListInfo</p> 
	 * <p>Description: 查询书单详情</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午7:50:29
	 * @param bookListId
	 * @param resId
	 * @return
	 */
	public List<BookListLink> findBookListInfo(long bookListId,Long resId,Integer pageSize);

	/**
	 * 
	 * <p>Title: updateBookListLink</p> 
	 * <p>Description: 更新书单中一本书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:08:41
	 * @param bookLink
	 * @return
	 */
	public int updateBookListLink(BookListLink bookLink);
	
	/**
	 * 
	 * <p>Title: findBookLinkById</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:32:41
	 * @param bookLinkId
	 * @return
	 */
	public BookListLink findBookLinkById(long bookListId,long bookId,String resType);
	
	/**
	 * 
	 * <p>Title: findBookLinkIsExist</p> 
	 * <p>Description: 查询书单中这本书是否存在</p> 
	 * @author :changjiang
	 * date 2014-8-9 上午12:02:00
	 * @param bookLink
	 * @return
	 */
	public BookListLink findBookLinkIsExist(BookListLink bookLink);
	
	/**
	 * 
	 * <p>Title: findDefaultBookList</p> 
	 * <p>Description: 查询用户的默认书单</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午4:56:27
	 * @param userId
	 * @return
	 */
	public List<BookList> findDefaultBookList(long userId);
	
	/**
	 * 
	 * <p>Title: queryUserBookListByName</p> 
	 * <p>Description: 根据名字查询书单</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午11:56:45
	 * @param bookList
	 * @return
	 */
	public BookList queryUserBookListByName(BookList bookList);
	
	/**
	 * 
	 * <p>Title: updateBookListLinkRemark</p> 
	 * <p>Description: 更新书籍备注</p> 
	 * @author :changjiang
	 * date 2014-11-14 下午7:08:24
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @param id
	 * @return
	 */
	public BookListLink updateBookListLinkRemark(String friendinfo, String address,
			String tags,long id,String description);
	
	/**
	 * 
	 * <p>Title: findBookLinkIsExistById</p> 
	 * <p>Description: 根据id查询书籍是否存在</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:31:02
	 * @param id
	 * @return
	 */
	public BookListLink findBookLinkIsExistById(long id);
	
	/**
	 * 
	 * <p>Title: addBookListCommentCount</p> 
	 * <p>Description: 增加书单的评论总数</p> 
	 * @author :changjiang
	 * date 2015-1-29 下午3:38:32
	 * @param id
	 * @param latestRevisionDate
	 * @return
	 */
	public int addBookListCommentCount(long id, long latestRevisionDate);
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
	 * <p>Title: findBookLinkCount</p> 
	 * <p>Description: 查询一个书单里面有多少本书</p> 
	 * @author :changjiang
	 * date 2015-5-12 下午5:59:53
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

