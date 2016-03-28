package com.poison.resource.domain.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poison.resource.dao.BookTalkDao;
import com.poison.resource.dao.MovieListDAO;
import com.poison.resource.dao.MovieTalkDao;
import com.poison.resource.dao.MvListLinkDAO;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MvListLink;

public class BookTalkDomainRepository {
	private BookTalkDao bookTalkDAO;
	
	
	public void setBookTalkDAO(BookTalkDao bookTalkDAO) {
		this.bookTalkDAO = bookTalkDAO;
	}
	/**
	 * 
	 * 方法的描述 :创建一条书摘
	 * @param mv
	 * @return
	 */
	public long addBookTalk(BookTalk bookTalk){
		return bookTalkDAO.addBookTalk(bookTalk);
	}
	/**
	 * 
	 * 方法的描述 :查询书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkList(int bookId,Integer page,String resType){
		return bookTalkDAO.findBookTalkList(bookId, page,resType);
	}
	/**
	 * 
	 * 方法的描述 :查询书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkListByPageAndId(int bookId,Integer page,Long id,String resType,Long userId){
		List<BookTalk> bookTalks = new ArrayList<BookTalk>();
		if(id != null && id != 0){
			bookTalks = bookTalkDAO.findBookTalkListByPageAndId_1(bookId, page, id, resType,userId);
		}
		
		List<BookTalk> bookTalks2 = new ArrayList<BookTalk>();
		bookTalks2 = bookTalkDAO.findBookTalkListByPageAndId_2(bookId, page, id, resType,userId);
		if(bookTalks.size() == 1 ){
			if(bookTalks.get(0).getId() == 0){
				bookTalks.clear();
				bookTalks = new ArrayList<BookTalk>();
			}
		}
		
		bookTalks.addAll(bookTalks2);
		
		
		return bookTalks;
	}
	/**
	 * 
	 * 方法的描述 :查询用户书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkListByUser(int bookId,long uid,Integer page,String resType){
		return bookTalkDAO.findBookTalkListByUser(bookId, uid, page,resType);
	}
	/**
	 * 
	 * 方法的描述 :删除书摘
	 * @param mv
	 * @return
	 */
	public int delBookTalk(Long id){
		return bookTalkDAO.delBookTalk(id);
	}
	
	/**
	 * 查询一些书摘
	 * @param bookId
	 * @return
	 */
	public List<BookTalk> findSomeBookTalkListByBookId(int bookId,String resType){
		return bookTalkDAO.findSomeBookTalkListByBookId(bookId,resType);
	}
	
	/**
	 * 
	 * <p>Title: findBookTalkListByUserId</p> 
	 * <p>Description: 根据用户id查询书摘列表</p> 
	 * @author :changjiang
	 * date 2015-4-10 下午3:45:50
	 * @param userId
	 * @param lastId
	 * @return
	 */
	public List<BookTalk> findBookTalkListByUserId(long userId,Long lastId){
		List<Long> list = bookTalkDAO.findBookTalkListByUserId(userId, lastId);
		List<BookTalk> resultList = new ArrayList<BookTalk>();
		Iterator<Long> listIt = list.iterator();
		while(listIt.hasNext()){
			Long talkId = listIt.next();
			BookTalk bookTalk = bookTalkDAO.findBookTalkById(talkId);
			resultList.add(bookTalk);
		}
		return resultList;
	}
	
	/**
	 * 
	 * <p>Title: findUserOneBookTalkCount</p> 
	 * <p>Description: 查询一个用户的书摘页数</p> 
	 * @author :changjiang
	 * date 2015-4-11 下午2:10:15
	 * @param userId
	 * @param bookId
	 * @return
	 */
	public Map<String, Object> findUserOneBookTalkCount(long userId, long bookId){
		return bookTalkDAO.findUserOneBookTalkCount(userId, bookId);
	}
	
}
