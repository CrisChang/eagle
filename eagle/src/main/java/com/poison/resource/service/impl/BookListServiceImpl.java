package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.poison.resource.domain.repository.BookListDomainRepository;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.service.BookListService;

public class BookListServiceImpl implements BookListService {
	private BookListDomainRepository bookListDomainRepository;

	public void setBookListDomainRepository(
			BookListDomainRepository bookListDomainRepository) {
		this.bookListDomainRepository = bookListDomainRepository;
	}
	
	/**
	 * 查询该用户的书单列表
	 */
	@Override
	public List<BookList> findBookList(ProductContext productContext,long uid) {
		return bookListDomainRepository.findBookList(uid);
	}
	@Override
	public int singleBookList(ProductContext productContext,long uid,String name, String reason) {
		return bookListDomainRepository.singleBookList(uid,name, reason);
	}

	/*@Override
	public List<Book> findBook(ProductContext productContext,String name) {
		return bookListDomainRepository.findBook(name);
	}*/

	@Override
	public int addBookList(ProductContext productContext,BookList bookList) {
		return bookListDomainRepository.addBookList(bookList);
	}

	@Override
	public int addBook(ProductContext productContext,long id,long uid,long rid,String name,
			String reason, String content) {
		//bookListDomainRepository.insertComment(id, uid, content);
		return bookListDomainRepository.addBook(id,uid, rid, name, reason);
	}
	@Override
	public int singleList(ProductContext productContext, long uid,long rid, String name,
			String reason) {
		return bookListDomainRepository.singleList(uid,rid, name, reason);
	}
	
	/**
	 * 更新书单信息
	 */
	@Override
	public int updateBookList(BookList booklist) {
		return bookListDomainRepository.updateBookList(booklist);
	}
	@Override
	public int deleteBookList(BookList booklist) {
		return bookListDomainRepository.deleteBookList(booklist);
	}
	/**
	 * 根据查询书单详情
	 */
	@Override
	public List<BookList> findLatestBookList(Long resId) {
		return bookListDomainRepository.findLatestBookList(resId);
	}
	@Override
	public List<BookList> findLatestBookListById(long resourceId) {
		return bookListDomainRepository.findLatestBookListById(resourceId);
	}
	@Override
	public BookList queryByIdBookList(long id) {
		return bookListDomainRepository.queryByIdBookList(id);
	}
	/**
	 * 根据用户ID查询书单列表
	 */
	@Override
	public List<BookList> findBookListByUsersId(List<Long> usersIdList,Long resId) {
		return bookListDomainRepository.findBookListByUsersId(usersIdList,resId);
	}

	/**
	 * 向一个书单中添加书
	 */
	@Override
	public int addBookToList(BookListLink bookLink) {
		return bookListDomainRepository.addBookToBookList(bookLink);
	}

	/**
	 * 查询书单详情
	 */
	@Override
	public List<BookListLink> findBookListInfo(long bookListId, Long resId,Integer pageSize) {
		return bookListDomainRepository.findBookListInfo(bookListId, resId,pageSize);
	}

	/**
	 * 更新一个书单中书的信息
	 */
	@Override
	public int updateBookListLink(BookListLink bookLink) {
		return bookListDomainRepository.updateBookListLink(bookLink);
	}

	/**
	 * 查询书单中一本书的信息
	 */
	@Override
	public BookListLink findBookLinkById(long bookListId,long bookId,String resType) {
		return bookListDomainRepository.findBookLinkById(bookListId,bookId,resType);
	}

	/**
	 * 查询书单中这本书是否存在
	 */
	@Override
	public BookListLink findBookLinkIsExist(BookListLink bookLink) {
		return bookListDomainRepository.findBookLinkIsExist(bookLink);
	}

	/**
	 * 查询用户的默认书单
	 */
	@Override
	public List<BookList> findDefaultBookList(long userId) {
		return bookListDomainRepository.findDefaultBookList(userId);
	}

	/**
	 * 查询系统推荐的书单
	 */
	@Override
	public List<BookList> findServerBookLists(String tags, Long resId) {
		return bookListDomainRepository.findServerBookLists(tags, resId);
	}

	/**
	 * 根据名字查询书单
	 */
	@Override
	public BookList queryUserBookListByName(BookList bookList) {
		return bookListDomainRepository.queryUserBookListByName(bookList);
	}

	/**
	 * 更新书的备注
	 */
	@Override
	public BookListLink updateBookListLinkRemark(String friendinfo,
			String address, String tags, long id,String description) {
		return bookListDomainRepository.updateBookListLinkRemark(friendinfo, address, tags, id,description);
	}

	/**
	 * 根据id查询图书是否存在
	 */
	@Override
	public BookListLink findBookLinkIsExistById(long id) {
		return bookListDomainRepository.findBookLinkIsExistById(id);
	}

	/**
	 * 更新书单封面
	 */
	@Override
	public BookList updateBookListPic(long id, String cover) {
		return bookListDomainRepository.updateBookListPic(id, cover);
	}

	/**
	 * 增加书单的评论总数
	 */
	@Override
	public int addBookListCommentCount(long id, long latestRevisionDate) {
		return bookListDomainRepository.addBookListCommentCount(id, latestRevisionDate);
	}

	@Override
	public long getBookCountByListId(long listid) {
		return bookListDomainRepository.getBookCountByListId(listid);
	}

	@Override
	public List<BookListLink> getBookListLinkByStartIndex(long listid,long start, int pageSize) {
		return bookListDomainRepository.getBookListLinkByStartIndex(listid, start, pageSize);
	}

	/**
	 * 查询书单中书的数量
	 */
	@Override
	public Map<String, Object> findBookLinkCount(long bookListId) {
		return bookListDomainRepository.findBookLinkCount(bookListId);
	}
	/**
	 * 根据书单id集合查询书单列表
	 * @param bklistids
	 * @return
	 */
	@Override
	public List<BookList> findBookListsByIds(List<Long> bklistids){
		return bookListDomainRepository.findBookListsByIds(bklistids);
	}
}
