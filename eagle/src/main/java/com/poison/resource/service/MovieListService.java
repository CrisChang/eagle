package com.poison.resource.service;

import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvListLink;

public interface MovieListService {
	/**
	 * 
	 * 方法的描述 :根据用户id查询默认列表
	 * @param userId
	 * @return
	 */
	public List<MovieList> queryDefaultFilmList(long userId);
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是向一个书单中添加一本书
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
	 * <p>Title: deleteMvListLink</p> 
	 * <p>Description: 删除一个书单中的一本书</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午10:56:41
	 * @param mvLink
	 * @return
	 */
	public int deleteMvListLink(MvListLink mvLink);
	
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
	//public MvListLink findMovieListById(long filmListId);
	/**
	 * 
	 * 方法的描述 :查询影单中是否存在这部电影
	 * @param mvLink
	 * @return
	 */
	public MvListLink findMovieIsExist(MvListLink mvLink);
	
	/**
	 * 
	 * 方法的描述 :新建影单
	 * @param productContext产品号
	 * @param filmlist
	 * @return
	 */
	public int addNewFilmList(ProductContext productContext,MovieList flist);
	
	/**
	 * 
	 * <p>Title: queryFilmListByUid</p> 
	 * <p>Description: 根据UID查询用户影单列表</p> 
	 * @author :changjiang
	 * date 2014-8-24 下午3:13:14
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MovieList> queryFilmListByUid(long userId, Long resId);
	
	/**
	 * 
	 * <p>Title: findPublishMvList</p> 
	 * <p>Description: 查询发布的影单列表</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午11:38:48
	 * @param id
	 * @return
	 */
	public List<MovieList> findPublishMvList(Long id);
	
	/**
	 * 
	 * <p>Title: deleteMovieList</p> 
	 * <p>Description: 删除一个影单</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午5:46:48
	 * @param id
	 * @return
	 */
	public MovieList  deleteMovieList(long id);
	
	/**
	 * 
	 * <p>Title: publishMovieList</p> 
	 * <p>Description: 发布一个影单</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午5:48:06
	 * @param id
	 * @return
	 */
	public int publishMovieList(long id);
	
	/**
	 * 
	 * <p>Title: updateMovieList</p> 
	 * <p>Description: 更新影单信息</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午5:52:18
	 * @param movieList
	 * @return
	 */
	public int updateMovieList(long id,String filmListName,String reason,String tag,String cover);
	
	/**
	 * 
	 * <p>Title: findMovieListById</p> 
	 * <p>Description: 查询一个影单</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午3:49:49
	 * @param id
	 * @return
	 */
	public MovieList findMovieListById(long id);
	
	/**
	 * 
	 * <p>Title: queryServerMvList</p> 
	 * <p>Description: 查询系统推荐的影单</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午6:31:53
	 * @param tags
	 * @param resId
	 * @return
	 */
	public List<MovieList> queryServerMvList(String tags, Long resId);
	
	/**
	 * 
	 * <p>Title: queryUserMvListByName</p> 
	 * <p>Description: 根据名字查询影单</p> 
	 * @author :changjiang
	 * date 2014-10-14 上午12:53:14
	 * @param filmlist
	 * @return
	 */
	public MovieList queryUserMvListByName(MovieList filmlist);
	
	/**
	 * 
	 * <p>Title: updateMvListLinkRemark</p> 
	 * <p>Description: 更新影单的备注</p> 
	 * @author :changjiang
	 * date 2014-11-21 下午5:04:21
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @param id
	 * @return
	 */
	public MvListLink updateMvListLinkRemark(String friendinfo, String address,
			String tags,long id,String description);
	
	/**
	 * 
	 * <p>Title: findMovieLinkIsExistById</p> 
	 * <p>Description: 查询电影是否存在</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:21:21
	 * @param id
	 * @return
	 */
	public MvListLink findMovieLinkIsExistById(long id);
	
	/**
	 * 
	 * <p>Title: updateMovieListPic</p> 
	 * <p>Description: 更新影单封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午5:05:04
	 * @param id
	 * @param cover
	 * @return
	 */
	public MovieList updateMovieListPic(long id, String cover);
	
	/**
	 * 
	 * <p>Title: addMvListCommentCount</p> 
	 * <p>Description: 更新影单评论数</p> 
	 * @author :changjiang
	 * date 2015-1-28 下午9:46:22
	 * @param id
	 * @param latestRevisionDate
	 * @return
	 */
	public int addMvListCommentCount(long id, long latestRevisionDate);
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
	 * <p>Description: 查询一个影单中有多少部电影</p> 
	 * @author :changjiang
	 * date 2015-5-13 下午7:46:23
	 * @param movieListId
	 * @return
	 */
	public Map<String, Object> findMovieLinkCount(long movieListId);
	/**
	 * 根据影单id集合查询影单列表
	 * @param mvlistids
	 * @return
	 */
	public List<MovieList> findMvListsByIds(List<Long> mvlistids);
}
