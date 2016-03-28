package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyMovie;

public interface BookTalkDao {
	/**
	 * 
	 * 方法的描述 :创建一条书摘
	 * @param mv
	 * @return
	 */
	public long addBookTalk(BookTalk bookTalk);
	/**
	 * 
	 * 方法的描述 :查询书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkList(int bookId,Integer page,String resType);
	/**
	 * 
	 * 方法的描述 :查询书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkListByPageAndId_1(int bookId,Integer page,Long id,String resType,Long userId);
	/**
	 * 
	 * 方法的描述 :查询书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkListByPageAndId_2(int bookId,Integer page,Long id,String resType,Long userId);
	/**
	 * 
	 * 方法的描述 :查询用户书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkListByUser(int bookId,long uid,Integer page,String resType);
	
	/**
	 * 
	 * <p>Title: findBookTalkListByUserId</p> 
	 * <p>Description: 根据用户id查询书摘列表</p> 
	 * @author :changjiang
	 * date 2015-4-10 下午12:43:21
	 * @param userId
	 * @return
	 */
	public List<Long> findBookTalkListByUserId(long userId,Long lastId);
	
	/**
	 * 
	 * <p>Title: findUserOneBookTalkCount</p> 
	 * <p>Description: 查询用户一本书的书摘</p> 
	 * @author :changjiang
	 * date 2015-4-11 上午11:20:09
	 * @param userId
	 * @param bookId
	 * @return
	 */
	public Map<String, Object> findUserOneBookTalkCount(long userId,long bookId);
	
	/**
	 * 查询一些
	 * @param bookId
	 * @return
	 */
	public List<BookTalk> findSomeBookTalkListByBookId(int bookId,String resType);
	/**
	 * 
	 * 方法的描述 :删除书摘
	 * @param mv
	 * @return
	 */
	public int delBookTalk(Long id);
	
	/**
	 * 
	 * <p>Title: findBookTalkById</p> 
	 * <p>Description: 根据id查询书摘</p> 
	 * @author :changjiang
	 * date 2015-4-11 下午6:10:09
	 * @param id
	 * @return
	 */
	public BookTalk findBookTalkById(long id);
}
