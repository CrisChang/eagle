package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.poison.resource.dao.BookTalkDao;
import com.poison.resource.dao.MovieTalkDao;
import com.poison.resource.domain.repository.BookTalkDomainRepository;
import com.poison.resource.domain.repository.MovieTalkDomainRepository;
import com.poison.resource.domain.repository.MyMovieDomainRepository;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyMovie;
import com.poison.resource.service.BookTalkService;
import com.poison.resource.service.MovieTalkService;
import com.poison.resource.service.MyMovieService;

public class BookTalkServiceImpl implements BookTalkService {
	private BookTalkDomainRepository bookTalkDomainRepository;
	@Override
	public long addBookTalk(BookTalk bookTalk) {
		return bookTalkDomainRepository.addBookTalk(bookTalk);
	}

	@Override
	public List<BookTalk> findBookTalkList(int bookId, Integer page,String resType) {
		
		return bookTalkDomainRepository.findBookTalkList(bookId, page,resType);
	}

	@Override
	public List<BookTalk> findBookTalkListByUser(int bookId, long uid,
			Integer page,String resType) {
		return bookTalkDomainRepository.findBookTalkListByUser(bookId, uid, page,resType);
	}

	@Override
	public int delBookTalk(Long id) {
		return bookTalkDomainRepository.delBookTalk(id);
	}
	public void setBookTalkDomainRepository(
			BookTalkDomainRepository bookTalkDomainRepository) {
		this.bookTalkDomainRepository = bookTalkDomainRepository;
	}

	/**
	 * 查询一些书摘的信息
	 */
	@Override
	public List<BookTalk> findSomeBookTalkListByBookId(int bookId,String resType) {
		return bookTalkDomainRepository.findSomeBookTalkListByBookId(bookId,resType);
	}

	@Override
	public List<BookTalk> findBookTalkListByPageAndId(int bookId, Integer page,
			Long id, String resType,Long userId) {
		return bookTalkDomainRepository.findBookTalkListByPageAndId(bookId, page, id, resType,userId);
	}

	/**
	 * 根据用户id查询书摘列表
	 */
	@Override
	public List<BookTalk> findBookTalkListByUserId(long userId, Long lastId) {
		return bookTalkDomainRepository.findBookTalkListByUserId(userId, lastId);
	}

	/**
	 * 查询一个用户的一本书书摘总数
	 */
	@Override
	public Map<String, Object> findUserOneBookTalkCount(long userId, long bookId) {
		return bookTalkDomainRepository.findUserOneBookTalkCount(userId, bookId);
	}
}
