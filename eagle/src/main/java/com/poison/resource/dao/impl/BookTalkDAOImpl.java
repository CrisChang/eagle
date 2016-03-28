package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BookTalkDao;
import com.poison.resource.dao.MovieTalkDao;
import com.poison.resource.dao.MyMovieDAO;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.MovieTalk;
import com.poison.resource.model.MyMovie;

@SuppressWarnings("deprecation")
public class BookTalkDAOImpl extends SqlMapClientDaoSupport implements BookTalkDao{

	private static final  Log LOG = LogFactory.getLog(BookTalkDAOImpl.class);
	
	@Override
	public long addBookTalk(BookTalk bookTalk) {
		long id = ResultUtils.INSERT_ERROR;
		try {
			id = (Long)getSqlMapClientTemplate().insert("addBookTalk",bookTalk);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			id =  ResultUtils.INSERT_ERROR;
		}
		return id;
	}

	@Override
	public List<BookTalk> findBookTalkList(int bookId, Integer page,String resType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookId", bookId);
		map.put("page", page);
		map.put("resType", resType);
		List<BookTalk> mlist=new ArrayList<BookTalk>();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findBookTalkList",map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			BookTalk bookTalk = new BookTalk();
			mlist=new ArrayList<BookTalk>();
			bookTalk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(bookTalk);
		}
		return mlist;
	}
	@Override
	public List<BookTalk> findBookTalkListByPageAndId_1(int bookId, Integer page,
			Long id, String resType,Long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookId", bookId);
		map.put("page", page);
		map.put("resType", resType);
		map.put("id", id);
		map.put("userId", userId);
		List<BookTalk> mlist=new ArrayList<BookTalk>();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findBookTalkListByPageAndId_1",map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			BookTalk bookTalk = new BookTalk();
			mlist=new ArrayList<BookTalk>();
			bookTalk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(bookTalk);
		}
		return mlist;
	}
	@Override
	public List<BookTalk> findBookTalkListByPageAndId_2(int bookId,
			Integer page, Long id, String resType,Long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookId", bookId);
		map.put("page", page);
		map.put("resType", resType);
		map.put("userId", userId);
//		map.put("id", id);
		List<BookTalk> mlist=new ArrayList<BookTalk>();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findBookTalkListByPageAndId_2",map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			BookTalk bookTalk = new BookTalk();
			mlist=new ArrayList<BookTalk>();
			bookTalk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(bookTalk);
		}
		return mlist;
	}

	@Override
	public int delBookTalk(Long id) {
		int r = 0;
		try {
			r = getSqlMapClientTemplate().update("delBookTalk", id);
			if(r > 0){
				return ResultUtils.SUCCESS;
			}else{
				return ResultUtils.DELETE_ERROR;
			}
		} catch (Exception e) {
			return ResultUtils.DELETE_ERROR;
		}
	}

	@Override
	public List<BookTalk> findBookTalkListByUser(int bookId, long uid,
			Integer page,String resType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookId", bookId);
		map.put("page", page);
		map.put("uid", uid);
		map.put("resType", resType);
		List<BookTalk> mlist=new ArrayList<BookTalk>();
		try {
			mlist=getSqlMapClientTemplate().queryForList("findBookTalkListByUser",map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			BookTalk bookTalk = new BookTalk();
			mlist=new ArrayList<BookTalk>();
			bookTalk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(bookTalk);
		}
		return mlist;
	}

	/**
	 * 
	 */
	@Override
	public List<BookTalk> findSomeBookTalkListByBookId(int bookId,String resType) {
		List<BookTalk> mlist=new ArrayList<BookTalk>();
		BookTalk bookTalk = new BookTalk();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bookId", bookId);
			map.put("resType", resType);
			mlist = getSqlMapClientTemplate().queryForList("findSomeBookTalkList",map);
		}catch (Exception e) {
			e.printStackTrace();
			mlist=new ArrayList<BookTalk>();
			bookTalk.setFlag(ResultUtils.QUERY_ERROR);
			mlist.add(bookTalk);
		}
		return mlist;
	}

	/**
	 * 根据用户id查询书摘列表
	 */
	@Override
	public List<Long> findBookTalkListByUserId(long userId,Long lastId) {
		List<Long> mlist=new ArrayList<Long>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("userId", userId);
			map.put("lastId", lastId);
			mlist = getSqlMapClientTemplate().queryForList("findBookTalkListByUserId",map);
			if(null==mlist||mlist.size()==0){
				mlist=new ArrayList<Long>();
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			e.printStackTrace();
			mlist=new ArrayList<Long>();
		}
		return mlist;
	}

	/**
	 * 查询用户一本书的书摘页数
	 */
	@Override
	public Map<String, Object> findUserOneBookTalkCount(long userId, long bookId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		int flag = ResultUtils.ERROR;
		int count = 0;
		try{
			map.put("userId", userId);
			map.put("bookId", bookId);
			count = (Integer) getSqlMapClientTemplate().queryForObject("findUserOneBookTalkCount",map);
			resultMap.put("flag", ResultUtils.SUCCESS);
			resultMap.put("count", count);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			resultMap = new HashMap<String, Object>();
			resultMap.put("flag", ResultUtils.ERROR);
		}
		return resultMap;
	}

	/**
	 * 根据id查询书摘详情
	 */
	@Override
	public BookTalk findBookTalkById(long id) {
		BookTalk bookTalk = new BookTalk();
		try{
			bookTalk = (BookTalk) getSqlMapClientTemplate().queryForObject("findBookTalkById",id);
			if(null==bookTalk){
				bookTalk = new BookTalk();
				bookTalk.setFlag(ResultUtils.DATAISNULL);
				return bookTalk;
			}
			bookTalk.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookTalk = new BookTalk();
			bookTalk.setFlag(ResultUtils.ERROR);
		}
		return bookTalk;
	}


}
