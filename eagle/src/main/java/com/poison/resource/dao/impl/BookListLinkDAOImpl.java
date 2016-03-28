package com.poison.resource.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.act.dao.impl.ActTransmitDAOImpl;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.BookListLinkDAO;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MvListLink;

public class BookListLinkDAOImpl extends SqlMapClientDaoSupport implements BookListLinkDAO{

	private static final  Log LOG = LogFactory.getLog(BookListLinkDAOImpl.class);
	/**
	 * 向书单中插入一本书
	 */
	@Override
	public int insertBookListLink(BookListLink bookLink) {
		int flag = ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().insert("insertBookLink", bookLink);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag = ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询书单的详情
	 */
	@Override
	public List<BookListLink> findBookListInfo(long bookListId, Long resId,Integer pageSize) {
		List<BookListLink> bookList = new ArrayList<BookListLink>();
		BookListLink bookLink = new BookListLink();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("bookListId", bookListId);
		map.put("resId", resId);
		map.put("pageSize", pageSize);
		try{
			bookList = getSqlMapClientTemplate().queryForList("findBookListInfo",map);
		}catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookList = new ArrayList<BookListLink>();
			bookLink.setFlag(ResultUtils.ERROR);
			bookList.add(bookLink);
		}
		return bookList;
	}

	/**
	 * 更新一个书单详情
	 */
	@Override
	public int updateBookListLink(BookListLink bookLink) {
		int flag=ResultUtils.ERROR;
		try{
			getSqlMapClientTemplate().update("updateBookListLink", bookLink);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag=ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询书单中一本书的
	 */
	@Override
	public BookListLink findBookListById(long bookListId,long bookId,String resType) {
		BookListLink bookLink = new BookListLink();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookListId", bookListId);
		map.put("bookId", bookId);
		map.put("resType", resType);
		try{
			bookLink = (BookListLink) getSqlMapClientTemplate().queryForObject("findBookLinkById",map);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookLink = new BookListLink();
			bookLink.setFlag(ResultUtils.QUERY_ERROR);
		}
		return bookLink;
	}

	/**
	 * 查询这本书是否存在书单
	 */
	@Override
	public BookListLink findBookIsExist(BookListLink bookListLink) {
		BookListLink bookLink = new BookListLink();
		try{
			bookLink = (BookListLink) getSqlMapClientTemplate().queryForObject("findBookLinkIsExist",bookListLink);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookLink = new BookListLink();
			bookLink.setFlag(ResultUtils.ERROR);
		}
		return bookLink;
	}

	/**
	 * 更新书籍备注信息
	 */
	@Override
	public int updateBookListLinkRemark(String friendinfo, String address,
			String tags,long id,String description) {
		int flag = ResultUtils.ERROR;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			long sysdate = System.currentTimeMillis();
			map.put("id", id);
			map.put("friendinfo", friendinfo);
			map.put("address", address);
			map.put("tags", tags);
			map.put("description", description);
			map.put("latestRevisionDate", sysdate);
			getSqlMapClientTemplate().update("updateBookListLinkRemark",map);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			flag=ResultUtils.ERROR;
		}
		return flag;
	}

	/**
	 * 查询书单链接是否存在
	 */
	@Override
	public BookListLink findBookLinkIsExist(long id) {
		BookListLink bookListLink = new BookListLink();
		try{
			bookListLink = (BookListLink) getSqlMapClientTemplate().queryForObject("findBookLinkIsExistById",id);
			if(null==bookListLink){
				bookListLink = new BookListLink();
				bookListLink.setFlag(ResultUtils.DATAISNULL);
				return bookListLink;
			}
			bookListLink.setFlag(ResultUtils.SUCCESS);
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bookListLink = new BookListLink();
			bookListLink.setFlag(ResultUtils.ERROR);
		}
		return bookListLink;
	}
	/**
	 * 查询书单中的书的数量
	 */
	@Override
	public long getBookCountByListId(long listid) {
		long i = 0;
		try{
			Long total = (Long) getSqlMapClientTemplate().queryForObject("getBookCountByListId",listid);
			if(total!=null){
				i = total;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
			i = -1;
		}
		return i;
	}
	/**
	 * 根据索引查询某个书单中的书的信息列表
	 */
	@Override
	public List<BookListLink> getBookListLinkByStartIndex(long listid, long start,int pageSize) {
		List<BookListLink> bllks=new ArrayList<BookListLink>();
		BookListLink bk=new BookListLink();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("listid", listid);
		map.put("start", start);
		map.put("pageSize", pageSize);
		try {
			bllks=getSqlMapClientTemplate().queryForList("getBookListLinkByStartIndex",map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			bllks=new ArrayList<BookListLink>();
			bk=new BookListLink();
			bk.setFlag(ResultUtils.QUERY_ERROR);
			bllks.add(bk);
		}
		return bllks;
	}

	/**
	 * 查询一个书单中有多少本书
	 */
	@Override
	public Map<String, Object> findBookLinkCount(long bookListId) {
		int flag = ResultUtils.ERROR;
		int count = 0 ;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			count = (Integer) getSqlMapClientTemplate().queryForObject("findBookLinkCount",bookListId);
			flag = ResultUtils.SUCCESS;
		}catch (Exception e) {
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		map.put("flag", flag);
		map.put("count", count);
		return map;
	}
	
}
