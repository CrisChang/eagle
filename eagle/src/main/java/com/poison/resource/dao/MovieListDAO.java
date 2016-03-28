package com.poison.resource.dao;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.MovieList;

public interface MovieListDAO {
	/**
	 * 
	 * 方法的描述 :根据用户id查询默认列表
	 * @param userId
	 * @return
	 */
	public List<MovieList> queryDefaultFilmList(long userId);
	
	/**
	 * 
	 * 方法的描述 :该方法主要是完成新建影单的功能
	 * @param booklist
	 */
	public int addNewFilmList(MovieList filmlist);
	
	/**
	 * 
	 * <p>Title: queryFilmListByUid</p> 
	 * <p>Description: 查询用户的书单列表</p> 
	 * @author :changjiang
	 * date 2014-8-24 下午2:08:01
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MovieList> queryFilmListByUid(long userId,Long resId);
	
	/**
	 * 
	 * <p>Title: updateFilmList</p> 
	 * <p>Description: 更新影单信息</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午4:48:12
	 * @param movieList
	 * @return
	 */
	public int updateFilmList(MovieList movieList);
	
	/**
	 * 
	 * <p>Title: updateMovieListPic</p> 
	 * <p>Description: 更新影单封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午4:52:19
	 * @param id
	 * @param cover
	 * @return
	 */
	public int updateMovieListPic(long id,String cover);
	
	/**
	 * 
	 * <p>Title: findMovieListById</p> 
	 * <p>Description: 根据id查询一个影单</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午5:12:39
	 * @param id
	 * @return
	 */
	public MovieList findMovieListById(long id);
	
	/**
	 * 
	 * <p>Title: findPublishMvList</p> 
	 * <p>Description: 查询发布列表的影单</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午11:25:36
	 * @param id
	 * @return
	 */
	public List<MovieList> findPublishMvList(Long id);
	
	/**
	 * 
	 * <p>Title: queryUserMvListByName</p> 
	 * <p>Description: 根据名称查询用户的影单</p> 
	 * @author :changjiang
	 * date 2014-8-28 下午6:29:50
	 * @param mvList
	 * @return
	 */
	public MovieList queryUserMvListByName(MovieList mvList);
	
	/**
	 * 
	 * <p>Title: queryServerBookList</p> 
	 * <p>Description: 查询系统推荐影单</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午6:13:06
	 * @return
	 */
	public List<MovieList> queryServerMvList(String tags,Long resId);
	
	/**
	 * 
	 * <p>Title: addMvListCommentCount</p> 
	 * <p>Description: 增加影单评论数</p> 
	 * @author :changjiang
	 * date 2015-1-28 下午9:30:01
	 * @param id
	 * @param latestRevisionDate
	 * @return
	 */
	public int addMvListCommentCount(long id,long latestRevisionDate);
	/**
	 * 根据影单id集合查询影单列表
	 * @param mvlistids
	 * @return
	 */
	public List<MovieList> findMvListsByIds(List<Long> mvlistids);
}
