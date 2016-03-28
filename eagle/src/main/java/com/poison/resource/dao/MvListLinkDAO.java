package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MvListLink;

public interface MvListLinkDAO {
	/**
	 * 
	 * 方法的描述 :此方法的作用是向一个影单中添加一个电影
	 * @param bookLink
	 * @return
	 */
	public int addMovieListLink(MvListLink mvLink);
	/**
	 * 
	 * 方法的描述 :更新一个影单详情
	 * @param mvLink
	 * @return
	 */
	public int updateMovieListLink(MvListLink mvLink);
	/**
	 * 
	 * 方法的描述 :查询影单详情
	 * @param filmListId
	 * @param movieId
	 * @return
	 */
	public List<MvListLink> findMovieListInfo(long filmListId,Long resId,Integer pageSize);
	/**
	 * 
	 * 方法的描述 :查询单本影单详情
	 * @param filmListId
	 * @return
	 */
	public MvListLink findMovieListById(long filmListId);
	/**
	 * 
	 * 方法的描述 :查询影单中是否存在这部电影
	 * @param mvLink
	 * @return
	 */
	public MvListLink findMovieIsExist(MvListLink mvLink);
	
	/**
	 * 
	 * <p>Title: updateMovieLinkRemark</p> 
	 * <p>Description: 更新影单备注</p> 
	 * @author :changjiang
	 * date 2014-11-15 下午5:11:20
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @param id
	 * @return
	 */
	public int updateMovieLinkRemark(String friendinfo,String address,String tags,long id,String description);
	
	/**
	 * 
	 * <p>Title: findMovieLinkIsExist</p> 
	 * <p>Description: 查询</p> 
	 * @author :changjiang
	 * date 2014-11-15 下午5:14:30
	 * @param id
	 * @return
	 */
	public MvListLink findMovieLinkIsExist(long id);
	/**
	 * 查询影单中的电影数量
	 */
	public long getMovieCountByListId(long listid);
	/**
	 * 根据索引查询某个影单中的电影信息列表
	 */
	public List<MvListLink> getMovieListLinkByStartIndex(long listid, long start,int pageSize);
	
	/**
	 * 
	 * <p>Title: findMovieLinkCount</p> 
	 * <p>Description: 查询一个影单的电影数量</p> 
	 * @author :changjiang
	 * date 2015-5-13 下午7:32:18
	 * @param movieListId
	 * @return
	 */
	public Map<String, Object> findMovieLinkCount(long movieListId);
}
