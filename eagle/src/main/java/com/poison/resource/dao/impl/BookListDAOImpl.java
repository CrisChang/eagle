package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BookListDAO;
import com.poison.resource.model.BookList;
import com.poison.resource.model.MovieList;

@SuppressWarnings("deprecation")
public class BookListDAOImpl extends SqlMapClientDaoSupport implements
		BookListDAO {

	private static final  Log LOG = LogFactory.getLog(BookListDAOImpl.class);
	/**
	 * 查询该用户的书单列表
	 */
	@Override
	public List<BookList> findBookList(long uid) {
		List<BookList> bookList = new ArrayList<BookList>();
		try {
			bookList = getSqlMapClientTemplate().queryForList("findBookList",uid);
		} catch (Exception e) {
			bookList = new ArrayList<BookList>();
			BookList book = new BookList();
			book.setFlag(ResultUtils.ERROR);
			bookList.add(book);
			return bookList;
		}
		return bookList;
	}

	@Override
	public int findById(long id) {
		try {
			getSqlMapClientTemplate().queryForObject("findById", id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.DATAISNULL;
		}
	}

	/**
	 * 新增书单
	 */
	@Override
	public int addBookList(BookList booklist) {
		try {
			getSqlMapClientTemplate().insert("insertBookList", booklist);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.DATAISNULL;
		}
	}

	@Override
	public int insertSingleBook(BookList booklist) {
		try {
			getSqlMapClientTemplate().insert("singleName", booklist);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.ERROR;
		}
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<Book> findBook(String name) {
		List<Book> list = new ArrayList<Book>();
		try {
			list = getSqlMapClientTemplate().queryForList("findbook", name);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			list = new ArrayList<Book>();
			Book book = new Book();
			book.setFlag(ResultUtils.ERROR);
			list.add(book);
			return list;
		}
		return list;
	}*/

	@Override
	public int queryById(long id) {
		try {
			getSqlMapClientTemplate().queryForObject("queryByIdList", id);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.DATAISNULL;
		}
	}

	@Override
	public int addBook(BookList booklist) {
		try {
			getSqlMapClientTemplate().queryForList("addOldBook", booklist);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public int insertBook(BookList book) {
		try {
			getSqlMapClientTemplate().insert("insertBook", book);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.ERROR;
		}
	}

	@Override
	public List<BookList> findLatestBookList(Long resId) {
		List<BookList> bookListByDate = new ArrayList<BookList>();
		BookList bookList = new BookList();
		try {
			bookListByDate = getSqlMapClientTemplate().queryForList(
					"findLatestBookList",resId);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookList.setFlag(ResultUtils.QUERY_ERROR);
			bookListByDate.add(bookList);
		}
		return bookListByDate;
	}

	/**
	 * 查找最新书单列表
	 */
	@Override
	public List<BookList> findLatestBookListById(long resourceId) {
		List<BookList> latestBookListById = new ArrayList<BookList>();
		BookList bookList = new BookList();
		try {
			latestBookListById = getSqlMapClientTemplate().queryForList(
					"findBookListById", resourceId);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookList.setFlag(ResultUtils.QUERY_ERROR);
			latestBookListById.add(bookList);
		}
		return latestBookListById;
	}

	/**
	 * 修改书单信息
	 */
	@Override
	public int updateBookList(BookList booklist) {
		try {
			getSqlMapClientTemplate().update("updateBookList", booklist);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.UPDATE_ERROR;
		}
	}

	@Override
	public int deleteBookList(BookList booklist) {
		try {
			getSqlMapClientTemplate().update("deleteBookList", booklist);
			return ResultUtils.SUCCESS;
		} catch (Exception e) {
			return ResultUtils.DELETE_ERROR;
		}
	}

	@Override
	public BookList queryByIdBookList(long id) {
		BookList blist = new BookList();
		try {
			blist = (BookList) getSqlMapClientTemplate().queryForObject(
					"queryUid", id);
			if(blist!=null){
			  blist.setFlag(ResultUtils.SUCCESS);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			blist = new BookList();
			blist.setFlag(ResultUtils.QUERY_ERROR);
			return blist;
		}
		return blist;
	}

	@Override
	public List<BookList> findBookListByUsersId(List<Long> usersIdList,Long resId) {
		List<BookList> booklist = new ArrayList<BookList>();
		//List<Long> resIdList = new ArrayList<Long>();
		//resIdList.add(userId);
		BookList blist = new BookList();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("usersIdList", usersIdList);
		map.put("resId", resId);
		try {
			if (null == usersIdList || usersIdList.size() == 0) {
				return booklist;
			}
			booklist = getSqlMapClientTemplate().queryForList("findBookListByUsersId", map);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			booklist = new ArrayList<BookList>();
			blist.setFlag(ResultUtils.ERROR);
			booklist.add(blist);
		}
		return booklist;
	}

	/**
	 * 查询用户的默认列表
	 */
	@Override
	public List<BookList> queryDefaultBookList(long userId) {
		List<BookList> bookList = new ArrayList<BookList>();
		//BookList bookList = new BookList();
		try{
			bookList = getSqlMapClientTemplate().queryForList("queryDefaultBookList",userId);
			if(null==bookList||bookList.size()==0){
				bookList = new ArrayList<BookList>();
				/*BookList book = new BookList();
				book.setFlag(ResultUtils.DATAISNULL);
				bookList.add(book);*/
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookList = new ArrayList<BookList>();
		/*	BookList book = new BookList();
			book.setFlag(ResultUtils.QUERY_ERROR);
			bookList.add(book);*/
		}
		return bookList;
	}

	/**
	 * 根据名字查询用户书单
	 */
	@Override
	public BookList queryUserBookListByName(BookList bookList) {
		BookList bkList = new BookList();
		try{
			bkList = (BookList) getSqlMapClientTemplate().queryForObject("queryUserBookListByName",bookList);
			if(null == bkList){
				bkList = new BookList();
				bkList.setFlag(ResultUtils.DATAISNULL);
				return bkList;
			}
			bkList.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkList = new BookList();
			bkList.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bkList;
	}

	/**
	 * 查询系统的书单列表
	 */
	@Override
	public List<BookList> findServerBookLists(String tags, Long resId) {
		List<BookList> bkList = new ArrayList<BookList>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tags", tags);
		map.put("resId", resId);
		try{
			bkList = getSqlMapClientTemplate().queryForList("queryServerBookList",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkList = new ArrayList<BookList>();
			BookList list = new BookList();
			list.setFlag(ResultUtils.QUERY_ERROR);
			bkList.add(list);
		}
		return bkList;
	}
	
	@Override
	public List<BookList> findBookListsByIds(List<Long> bklistids) {
		List<BookList> bkList = new ArrayList<BookList>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bklistids", bklistids);
		try{
			bkList = getSqlMapClientTemplate().queryForList("findBookListsByIds",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bkList = new ArrayList<BookList>();
			BookList list = new BookList();
			list.setFlag(ResultUtils.QUERY_ERROR);
			bkList.add(list);
		}
		return bkList;
	}
	
	/**
	 * 更新书单的封面
	 */
	@Override
	public int updateBookListPic(long id, String cover) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("id", id);
			map.put("cover", cover);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateBookListPic",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}

	/**
	 * 增加书单的评论数
	 */
	@Override
	public int addBookListCommentCount(long id, long latestRevisionDate) {
		int flag = ResultUtils.ERROR;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("latestRevisionDate", latestRevisionDate);
			getSqlMapClientTemplate().update("addBookListCommentCount",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		return flag;
	}
}
