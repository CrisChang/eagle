package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BookListLink;

public interface BookListLinkDAO {

	/**
	 * 
	 * <p>Title: insertBookListLink</p> 
	 * <p>Description: 向一个书单中添加一本书</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午2:25:29
	 * @param bookLink
	 * @return
	 */
	public int insertBookListLink(BookListLink bookLink);
	
	/**
	 * 
	 * <p>Title: updateBookListLink</p> 
	 * <p>Description: 更新一个书单详情</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午10:29:04
	 * @param bookLink
	 * @return
	 */
	public int updateBookListLink(BookListLink bookLink);
	
	/**
	 * 
	 * <p>Title: updateBookListLinkRemark</p> 
	 * <p>Description: 更新书籍备注信息</p> 
	 * @author :changjiang
	 * date 2014-11-14 下午6:14:04
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @return
	 */
	public int updateBookListLinkRemark(String friendinfo,String address,String tags,long id,String description);
	
	/**
	 * 
	 * <p>Title: findBookListInfo</p> 
	 * <p>Description: 查询书单的详情</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午6:09:17
	 * @param bookListId
	 * @param resId
	 * @return
	 */
	public List<BookListLink> findBookListInfo(long bookListId,Long resId,Integer pageSize);
	
	/**
	 * 
	 * <p>Title: findBookLinkIsExist</p> 
	 * <p>Description: 查询书单链接是否存在</p> 
	 * @author :changjiang
	 * date 2014-11-14 下午6:32:18
	 * @param id
	 * @return
	 */
	public BookListLink findBookLinkIsExist(long id);
	
	/**
	 * 
	 * <p>Title: findBookListById</p> 
	 * <p>Description: 查询书单中一本书的详情</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:16:02
	 * @param bookLinkId
	 * @return
	 */
	public BookListLink findBookListById(long bookListId,long bookId,String resType);
	
	/**
	 * 
	 * <p>Title: findBookIsExist</p> 
	 * <p>Description: 查询书单中这本书是否存在</p> 
	 * @author :changjiang
	 * date 2014-8-8 下午11:51:21
	 * @param bookListLink
	 * @return
	 */
	public BookListLink findBookIsExist(BookListLink bookListLink);
	/**
	 * 查询书单中的书的数量
	 */
	public long getBookCountByListId(long listid);
	/**
	 * 根据索引查询某个书单中的书的信息列表
	 */
	public List<BookListLink> getBookListLinkByStartIndex(long listid, long start,int pageSize);
	
	/**
	 * 
	 * <p>Title: findBookLinkCount</p> 
	 * <p>Description: 查询一个书单中有多少本书</p> 
	 * @author :changjiang
	 * date 2015-5-12 下午5:42:40
	 * @param bookListId
	 * @return
	 */
	public Map<String, Object> findBookLinkCount(long bookListId);
}
