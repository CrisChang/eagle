package com.poison.resource.dao;

import java.util.List;

import com.keel.framework.runtime.ProductContext;
import com.poison.resource.model.BookList;

/**
 * 
 * 类的作用:主要是对booklist表中的数据进行持久化操作 
 * 作者:闫前刚 
 * 创建时间:2014-7-14下午5:12:04 
 * email :1486488968@qq.com 
 * version: 1.0
 */
public interface BookListDAO {
	/**
	 * 
	 * 方法的描述 :对书单的数据进行查询操作
	 * 
	 * @return
	 */
	public List<BookList> findBookList(long uid);
	/**
	 * 
	 * 方法的描述 :向数据库添加单本书
	 * 
	 * @param booklist该参数添加的书单对象
	 * @return
	 */
	public int insertSingleBook(BookList booklist);
	/**
	 * 
	 * 方法的描述 :该方法的作用是查询该书单中是否有该书
	 * @param id根据id查询
	 * @return
	 */
	public int findById(long id);
	
	/**
	 * 
	 * 方法的描述 :该方法主要是完成新建书单的功能
	 * @param booklist
	 */
	public int addBookList(BookList booklist);

	/**
	 * 
	 * 方法的描述 :查询图书表中的信息 是否存在该书
	 * @param id
	 * @return
	 */
	public int queryById(long id);
	/**
	 * 
	 * 方法的描述 :向老书单中追加一本书
	 * @param booklist
	 * @return
	 */
	public int addBook(BookList booklist);
	/**
	 * 
	 * 方法的描述 :插入单本书(库中有)
	 * @return
	 */
	public int insertBook(BookList book);
	/**
	 * 
	 * 方法的描述 :此方法的作用修改书单
	 * @param booklist
	 * @return
	 */
	public int updateBookList(BookList booklist);
	
	/**
	 * 
	 * <p>Title: updateBookListPic</p> 
	 * <p>Description: 更新书单的封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午3:59:37
	 * @param id
	 * @param cover
	 * @return
	 */
	public int updateBookListPic(long id,String cover);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除书单
	 * @param booklist
	 * @return
	 */
	public int deleteBookList(BookList booklist);


	
	/**
	 * 
	 * <p>Title: findLatestBookList</p> 
	 * <p>Description: 查询最新书单信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午2:18:58
	 * @return
	 */
	public List<BookList> findLatestBookList(Long resId); 
	
	/**
	 * 
	 * <p>Title: findLatestBookListById</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午2:20:17
	 * @param resourceId
	 * @return
	 */
	public List<BookList> findLatestBookListById(long resourceId);

	
	/**
	 * 
	 * <p>Title: findBookListByUsersId</p> 
	 * <p>Description: 根据用户ID查询书籍列表</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午7:29:51
	 * @param usersIdList
	 * @return
	 */
	public List<BookList> findBookListByUsersId(List<Long> usersIdList,Long resId);

	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户的目录
	 * @param uid 用户名
	 * @return
	 */
	public BookList queryByIdBookList(long id);

	/**
	 * 
	 * <p>Title: queryDefaultBookList</p> 
	 * <p>Description: 查询用户的默认列表</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午4:43:57
	 * @param userId
	 * @return
	 */
	public List<BookList> queryDefaultBookList(long userId);
	
	/**
	 * 
	 * <p>Title: findServerBookLists</p> 
	 * <p>Description: 查询系统的书单列表</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午5:40:17
	 * @param tags
	 * @param resId
	 * @return
	 */
	public List<BookList> findServerBookLists(String tags,Long resId);
	
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
	 * 
	 * <p>Title: addMvListCommentCount</p> 
	 * <p>Description: </p> 
	 * @author :changjiang
	 * date 2015-1-28 下午10:30:52
	 * @param id
	 * @param latestRevisionDate
	 * @return
	 */
	public int addBookListCommentCount(long id,long latestRevisionDate);
	
	//public List<BookList> findActivityBookListBy
	/**
	 * 根据书单id集合查询书单列表
	 * @param bklistids
	 * @return
	 */
	public List<BookList> findBookListsByIds(List<Long> bklistids);
}
