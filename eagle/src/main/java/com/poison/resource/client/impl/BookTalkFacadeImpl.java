package com.poison.resource.client.impl;

import java.util.List;
import java.util.Map;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.BookTalkFacade;
import com.poison.resource.client.MovieTalkFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyBk;
import com.poison.resource.service.BookTalkService;
import com.poison.resource.service.MovieTalkService;
import com.poison.resource.service.MyBkService;

public class BookTalkFacadeImpl implements BookTalkFacade{
	private BookTalkService bookTalkService;
	private UKeyWorker reskeyWork;

	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	@Override
	public long addBookTalk(int bookId, long uid, int page, String content,String resType) {
		long id = reskeyWork.getId();
		BookTalk bookTalk = new BookTalk();
		bookTalk.setId(id);
		bookTalk.setBookId(bookId);
		bookTalk.setUid(uid);
		bookTalk.setPage(page);
		bookTalk.setContent(content);
		bookTalk.setType(0);
		bookTalk.setResType(resType);
		bookTalk.setIsDel(0);
		bookTalk.setCreateTime(System.currentTimeMillis());
		if(bookTalkService.addBookTalk(bookTalk) != ResultUtils.ERROR){
			return id;
		}else{
			return ResultUtils.ERROR;
		}
	}

	@Override
	public List<BookTalk> findBookTalkList(int bookId, Integer page,String resType) {
		return bookTalkService.findBookTalkList(bookId, page,resType);
	}

	@Override
	public List<BookTalk> findBookTalkListByUser(int bookId, long uid,
			Integer page,String resType) {
		return bookTalkService.findBookTalkListByUser(bookId, uid, page,resType);
	}

	@Override
	public int delBookTalk(Long id) {
		return bookTalkService.delBookTalk(id);
	}

	public void setBookTalkService(BookTalkService bookTalkService) {
		this.bookTalkService = bookTalkService;
	}

	/**
	 * 查询一些书摘信息
	 */
	@Override
	public List<BookTalk> findSomeBookTalkListByBookId(int bookId,String resType) {
		return bookTalkService.findSomeBookTalkListByBookId(bookId,resType);
	}

	@Override
	public List<BookTalk> findBookTalkListByPageAndId(int bookId, Integer page,
			Long id, String resType,Long userId) {
		return bookTalkService.findBookTalkListByPageAndId(bookId, page, id, resType,userId);
	}

	/**
	 * 根据用户id查询书摘列表
	 */
	@Override
	public List<BookTalk> findBookTalkListByUserId(long userId, Long lastId) {
		return bookTalkService.findBookTalkListByUserId(userId, lastId);
	}

	/**
	 * 查询一个用户一本书的书摘总数
	 */
	@Override
	public Map<String, Object> findUserOneBookTalkCount(long userId, long bookId) {
		return bookTalkService.findUserOneBookTalkCount(userId, bookId);
	}
}
