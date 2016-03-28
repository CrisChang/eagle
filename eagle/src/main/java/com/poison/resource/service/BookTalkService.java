package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MvListLink;

public interface BookTalkService {
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
	public List<BookTalk> findBookTalkListByPageAndId(int bookId,Integer page,Long id,String resType,Long userId);
	/**
	 * 
	 * 方法的描述 :查询用户书摘列表
	 * @param mv
	 * @return
	 */
	public List<BookTalk> findBookTalkListByUser(int bookId,long uid,Integer page,String resType);
	/**
	 * 
	 * 方法的描述 :删除书摘
	 * @param mv
	 * @return
	 */
	public int delBookTalk(Long id);
	
	/**
	 * 查询一些书摘信息
	 * @param bookId
	 * @return
	 */
	public List<BookTalk> findSomeBookTalkListByBookId(int bookId,String resType);
	
	/**
	 * 
	 * <p>Title: findBookTalkListByUserId</p> 
	 * <p>Description: 根据用户id查询书摘列表</p> 
	 * @author :changjiang
	 * date 2015-4-10 下午3:47:25
	 * @param userId
	 * @param lastId
	 * @return
	 */
	public List<BookTalk> findBookTalkListByUserId(long userId,Long lastId);
	
	/**
	 * 
	 * <p>Title: findUserOneBookTalkCount</p> 
	 * <p>Description: 查询一个用户的书摘总数</p> 
	 * @author :changjiang
	 * date 2015-4-11 下午2:14:32
	 * @param userId
	 * @param bookId
	 * @return
	 */
	public Map<String, Object> findUserOneBookTalkCount(long userId, long bookId);
}
