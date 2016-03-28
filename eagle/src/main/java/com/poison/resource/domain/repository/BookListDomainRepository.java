package com.poison.resource.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BookListDAO;
import com.poison.resource.dao.BookListLinkDAO;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;

public class BookListDomainRepository {
	UKeyWorker uk=new UKeyWorker(0,1);
	private BookListDAO bookListDAO;
	
	private BookListLinkDAO bookListLinkDAO;

	public void setBookListLinkDAO(BookListLinkDAO bookListLinkDAO) {
		this.bookListLinkDAO = bookListLinkDAO;
	}
	public void setBookListDAO(BookListDAO bookListDAO) {
		this.bookListDAO = bookListDAO;
	}
	/**
	 * 
	 * 方法的描述 :插入单本书 库中无
	 * @param name
	 * @param reason
	 * @return
	 */
	public int singleBookList(long uid,String name,String reason) {
		BookList blist = new BookList();
		/*blist.setBookDate(new Date().getTime());
		blist.setBookName(name);
		blist.setId(uk.getId());
		blist.setIsDel(0);
		blist.setType(0);
		blist.setuId(uid);
		blist.setReason(reason);
		blist.setResourceId(0);
		blist.setBookId(0);*/
		return bookListDAO.insertSingleBook(blist);
	}
	/**
	 * 
	 * 方法的描述 :书单中无书 图书表中有书
	 * @param uid 用户名
	 * @param name 图书名称
	 * @param reason 推荐理由
	 * @return
	 */
	public int singleList(long uid,long rid,String name,String reason) {
		BookList blist = new BookList();
		/*blist.setBookDate(new Date().getTime());
		blist.setBookName(name);
		blist.setId(uk.getId());
		blist.setIsDel(0);
		blist.setType(0);
		blist.setuId(uid);
		blist.setReason(reason);
		blist.setResourceId(rid);
		blist.setBookId(0);*/
		return bookListDAO.insertSingleBook(blist);
	}
	
	/**
	 * 
	 * 方法的描述 :根据id查询将Book表的书添加到书单中去
	 * @param id
	 * @param name
	 * @return
	 */
	public int singleBook(int id,String name){
		
		BookList blist=new BookList();
		/*blist.setBookDate(12);
		blist.setBookName(name);
		blist.setId(uk.getId());
		blist.setIsDel(0);
		blist.setType(0);
		blist.setReason("shuangshuang");
		blist.setResourceId(0);*/
		return bookListDAO.insertSingleBook(blist);
	
	}
	/**
	 * 
	 * 方法的描述 :显示书单里的所有的书
	 * @return
	 */
	public List<BookList> findBookList(long uid){
		return bookListDAO.findBookList(uid);
	}
	
	/**
	 * 
	 * <p>Title: queryUserBookListByName</p> 
	 * <p>Description: 根据名字查询书单</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午11:54:55
	 * @return
	 */
	public BookList queryUserBookListByName(BookList bookList){
		return bookListDAO.queryUserBookListByName(bookList);
	}
	
	/**
	 * 
	 * 方法的描述 :新建书单
	 * @param name
	 * @param id
	 * @return
	 */
	public int addBookList(BookList bookList){
		//查询用户建立书单的名字是否重复
		BookList bkList = bookListDAO.queryUserBookListByName(bookList);
		int flag = ResultUtils.BKLISTNAME_IS_ALREADY_EXISTED;
		if(ResultUtils.DATAISNULL==bkList.getFlag()){
			flag = bookListDAO.addBookList(bookList);
		}
		/*BookList blist=new BookList();
		blist.setBookDate(new Date().getTime());
		blist.setBookName(name);
		blist.setId(id);
		blist.setIsDel(0);
		blist.setType(0);
		blist.setBookId(0);
		blist.setReason(reason);
		blist.setResourceId(0);
		blist.setuId(uid);*/
		return flag;
	}
	/**
	 * 
	 * 方法的描述 :该方法的作用是完成插入(库中已有的书单)
	 * @param id 书id
	 * @param name 图书名称
	 * @param reason 推荐理由
	 * @return
	 */
	public int addBook(long id,long uid,long rid, String name, String reason){
		BookList blist=new BookList();
		/*blist.setBookDate(new Date().getTime());
		blist.setBookName(name);
		blist.setId(uk.getId());
		blist.setIsDel(0);
		blist.setType(0);
		blist.setReason(reason);
		blist.setuId(uid);
		blist.setBookId(id);
		blist.setResourceId(rid);*/
		return bookListDAO.insertBook(blist);
	}
	/**
	 * 
	 * 方法的描述 :该方法是模糊查询库中的书籍
	 * @param name 图书名称
	 * @return
	 */
	/*public List<Book> findBook(String name){
		List<Book> book=new ArrayList<Book>();
		book=bookListDAO.findBook(name);
		return book;
	}*/
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改书单名称
	 * @param booklist
	 * @return
	 */
	public int updateBookList(BookList booklist){
		return bookListDAO.updateBookList(booklist);
	}
	
	/**
	 * 
	 * <p>Title: updateBookListPic</p> 
	 * <p>Description: 更新书单封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午4:19:33
	 * @param id
	 * @param cover
	 * @return
	 */
	public BookList updateBookListPic(long id, String cover){
		//根据id查询书单
		BookList bookList = bookListDAO.queryByIdBookList(id);
		if(null==bookList){
			bookList = new BookList();
			bookList.setFlag(ResultUtils.DATAISNULL);
			return bookList;
		}
		int flag = bookListDAO.updateBookListPic(id, cover);
		if(ResultUtils.SUCCESS==flag){
			bookList = bookListDAO.queryByIdBookList(id);
		}
		return bookList;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除书单名称
	 * @param booklist
	 * @return
	 */
	public int deleteBookList(BookList booklist){
		return bookListDAO.deleteBookList(booklist);

	}
	
	/**
	 * 
	 * <p>Title: findLatestBookListByDate</p> 
	 * <p>Description: 查询最新的书单列表</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午3:18:40
	 * @return
	 */
	public List<BookList> findLatestBookList(Long resId){
		return bookListDAO.findLatestBookList(resId);
	}

	/**
	 * 
	 * <p>Title: findLatestBookListById</p> 
	 * <p>Description: 根据ID查询最新书单信息</p> 
	 * @author :changjiang
	 * date 2014-7-29 下午3:20:29
	 * @return
	 */
	public List<BookList> findLatestBookListById(long resourceId){
		return bookListDAO.findLatestBookListById(resourceId);
	}

	
	/**
	 * 
	 * <p>Title: findBookListByUsersId</p> 
	 * <p>Description: 根据用户ID查询书单信息</p> 
	 * @author :changjiang
	 * date 2014-7-31 下午7:45:12
	 * @return
	 */
	public List<BookList> findBookListByUsersId(List<Long> usersIdList,Long resId){
		return bookListDAO.findBookListByUsersId(usersIdList,resId);
	}

	/**
	 * 
	 * 方法的描述 :此方法的作用是查询booklist书单目录
	 * @param id
	 * @return
	 */
	public BookList queryByIdBookList(long id){
		return bookListDAO.queryByIdBookList(id);
	}

	/**
	 * 
	 * <p>Title: findServerBookLists</p> 
	 * <p>Description: 查询系统推荐的书单列表</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午5:47:44
	 * @param tags
	 * @param resId
	 * @return
	 */
	public List<BookList> findServerBookLists(String tags, Long resId){
		return bookListDAO.findServerBookLists(tags, resId);
	}
	
	/**
	 * 
	 * <p>Title: addBookToBookList</p> 
	 * <p>Description: 向书单中插入一本书</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午2:48:18
	 * @param bookLink
	 * @return
	 */
	public int addBookToBookList(BookListLink bookLink){
		int flag = ResultUtils.ERROR;
		BookListLink bookListLink = null;//bookListLinkDAO.findBookIsExist(bookLink);
		if(null==bookListLink||0==bookListLink.getId()){
			flag = bookListLinkDAO.insertBookListLink(bookLink);
		}
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: findBookListInfo</p> 
	 * <p>Description: 查询书单详情</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午7:48:54
	 * @param bookListId
	 * @param resId
	 * @return
	 */
	public List<BookListLink> findBookListInfo(long bookListId,Long resId,Integer pageSize){
		return bookListLinkDAO.findBookListInfo(bookListId, resId,pageSize);
	}
	
	/**
	 * 
	 * <p>Title: updateBookListLink</p> 
	 * <p>Description: 更新书单中书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:07:20
	 * @param bookLink
	 * @return
	 */
	public int updateBookListLink(BookListLink bookLink){
		return bookListLinkDAO.updateBookListLink(bookLink);
	}
	
	/**
	 * 
	 * <p>Title: findBookLinkById</p> 
	 * <p>Description: 根据ID</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:28:01
	 * @param bookLinkId
	 * @return
	 */
	public BookListLink findBookLinkById(long bookListId,long bookId,String resType){
		return bookListLinkDAO.findBookListById(bookListId,bookId,resType);
	}
	
	/**
	 * 
	 * <p>Title: findBookLinkIsExist</p> 
	 * <p>Description: 查询书单中该本书是否存在</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:59:39
	 * @param bookLink
	 * @return
	 */
	public BookListLink findBookLinkIsExist(BookListLink bookLink){
		return bookListLinkDAO.findBookIsExist(bookLink);
	}
	
	/**
	 * 
	 * <p>Title: findDefaultBookList</p> 
	 * <p>Description: 查询用户的默认列表</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午4:54:40
	 * @param userId
	 * @return
	 */
	public List<BookList> findDefaultBookList(long userId){
		return bookListDAO.queryDefaultBookList(userId);
	}
	
	/**
	 * 
	 * <p>Title: updateBookListLinkRemark</p> 
	 * <p>Description: 更新备注</p> 
	 * @author :changjiang
	 * date 2014-11-14 下午6:24:28
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @return
	 */
	public BookListLink updateBookListLinkRemark(String friendinfo, String address,
			String tags,long id,String description){
		BookListLink bookListLink = bookListLinkDAO.findBookLinkIsExist(id);
		int flag = bookListLink.getFlag();
		if(ResultUtils.SUCCESS==flag){
			flag = bookListLinkDAO.updateBookListLinkRemark(friendinfo, address, tags, id,description);
			bookListLink = bookListLinkDAO.findBookLinkIsExist(id);
			bookListLink.setFlag(flag);
		}
		return bookListLink;
	}
	
	/**
	 * 
	 * <p>Title: findBookLinkIsExistById</p> 
	 * <p>Description: 根据id查询书是否存在</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:29:41
	 * @param id
	 * @return
	 */
	public BookListLink findBookLinkIsExistById(long id){
		return bookListLinkDAO.findBookLinkIsExist(id);
	}
	
	/**
	 * 
	 * <p>Title: addBookListCommentCount</p> 
	 * <p>Description: 增加书单评论数</p> 
	 * @author :changjiang
	 * date 2015-1-29 下午3:36:37
	 * @param id
	 * @param latestRevisionDate
	 * @return
	 */
	public int addBookListCommentCount(long id, long latestRevisionDate){
		return bookListDAO.addBookListCommentCount(id, latestRevisionDate);
	}
	/**
	 * 查询书单中的书的数量
	 */
	public long getBookCountByListId(long listid){
		return bookListLinkDAO.getBookCountByListId(listid);
	}
	/**
	 * 根据索引查询某个书单中的书的信息列表
	 */
	public List<BookListLink> getBookListLinkByStartIndex(long listid, long start,int pageSize){
		return bookListLinkDAO.getBookListLinkByStartIndex(listid, start, pageSize);
	}
	
	/**
	 * 
	 * <p>Title: findBookLinkCount</p> 
	 * <p>Description: 查询书单的 </p> 
	 * @author :changjiang
	 * date 2015-5-12 下午5:57:51
	 * @param bookListId
	 * @return
	 */
	public Map<String, Object> findBookLinkCount(long bookListId){
		return bookListLinkDAO.findBookLinkCount(bookListId);
	}
	/**
	 * 根据书单id集合查询书单列表
	 * @param bklistids
	 * @return
	 */
	public List<BookList> findBookListsByIds(List<Long> bklistids){
		return bookListDAO.findBookListsByIds(bklistids);
	}
}