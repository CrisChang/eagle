package com.poison.resource.client;

import java.util.List;
import java.util.Map;

import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.MyMovie;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;

/**
 * 
 * 类的作用：
 * 作者:闫前刚
 * 创建时间:2014-8-9下午3:42:59
 * email :1486488968@qq.com
 * version: 1.0
 */
public interface MyMovieFacade {
	/**
	 * 
	 * 方法的描述 :此方法的作用是添加一部电影
	 * @return
	 */
	//public int addMyMovie(int uid,String name,String description);
	/**
	 * 
	 * 方法的描述 :此方法的作用模糊、精准查询
	 * @param name
	 * @return
	 */
	//public List<MyMovie> findMyMovieList(String name);
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询已经添加到数据库中的书籍
	 * @param mv
	 * @return
	 */
	//public MyMovie findMyMovieIsExist(String name,int uid);
	/**
	 * 
	 * 方法的描述 :此方法的作用是根据id查询自建表信息
	 * @param id
	 * @return
	 */
	//public MyMovie findMyMovieInfo(long id);
	
	/**
	 * 
	 * <p>Title: queryFilmListByUid</p> 
	 * <p>Description: 根据UID查询用户的影单列表</p> 
	 * @author :changjiang
	 * date 2014-8-24 下午3:15:59
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MovieList> queryFilmListByUid(long userId, Long resId);
	
	/**
	 * 
	 * <p>Title: addDefaultMvList</p> 
	 * <p>Description: 增加一个默认影单</p> 
	 * @author :changjiang
	 * date 2014-8-24 下午4:27:29
	 * @return
	 */
	public int addDefaultMvList(long userId);
	
	/**
	 * 
	 * <p>Title: addWantReadMvList</p> 
	 * <p>Description: 增加想看的影单</p> 
	 * @author :changjiang
	 * date 2014-10-13 下午10:38:14
	 * @param userId
	 * @return
	 */
	public int addWantWatchMvList(long userId);
	
	/**
	 * 
	 * <p>Title: addNewMvList</p> 
	 * <p>Description: 增加一个新影单</p> 
	 * @author :changjiang
	 * date 2014-8-24 下午5:39:28
	 * @param userId
	 * @param mvListName
	 * @return
	 */
	public int addNewMvList(long userId,String mvListName,String reason,String tag);
	
	/**
	 * 
	 * <p>Title: addServerMvList</p> 
	 * <p>Description: 添加系统影单</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午5:11:31
	 * @param mvListName
	 * @param reason
	 * @param tag
	 * @return
	 */
	public int addServerMvList(String mvListName,String reason,String tag);
	
	/**
	 * 
	 * <p>Title: addMovieListLink</p> 
	 * <p>Description: 在一个书单中新增一个电影</p> 
	 * @author :changjiang
	 * date 2014-8-24 下午5:34:51
	 * @param filmListId
	 * @param movieId
	 * @param isDb
	 * @return
	 */
	public int addMovieListLink(long filmListId,int movieId,int isDb);
	
	/**
	 * 
	 * <p>Title: deleteMovieListLink</p> 
	 * <p>Description: 删除一个影单中的一部电影</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午11:00:38
	 * @param filmListId
	 * @param movieId
	 * @return
	 */
	public int deleteMovieListLink(long filmListId,int movieId);
	
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
	 * <p>Title: findMovieListInfo</p> 
	 * <p>Description: 查询一个影单的详细信息</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午11:18:59
	 * @param filmListId
	 * @param resId
	 * @return
	 */
	public List<MvListLink> findMovieListInfo(long filmListId, Long resId,Integer pageSize);
	
	/**
	 * 
	 * <p>Title: findPublishMvList</p> 
	 * <p>Description: 查询发布的影单列表</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午11:40:02
	 * @param id
	 * @return
	 */
	public List<MovieList> findPublishMvList(Long id);
	
	/**
	 * 
	 * <p>Title: findMovieListById</p> 
	 * <p>Description: 查询一个影单</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午3:37:30
	 * @param id
	 * @return
	 */
	public MovieList findMovieListById(long id);
	
	/**
	 * 
	 * <p>Title: queryServerMvList</p> 
	 * <p>Description: 查询系统推荐影单</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午6:35:04
	 * @param tags
	 * @param resId
	 * @return
	 */
	public List<MovieList> queryServerMvList(String tags, Long resId);
	
	/**
	 * 
	 * <p>Title: findUserIsCollectMovie</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-10-20 下午12:22:23
	 * @param uId
	 * @param movieId
	 * @return
	 */
	public MvListLink findUserIsCollectMovie(long uId, long movieId);
	
	/**
	 * 
	 * <p>Title: moveOneMovie</p> 
	 * <p>Description: 移动一部电影</p> 
	 * @author :changjiang
	 * date 2014-10-20 下午1:15:10
	 * @param movieId
	 * @param mvListId
	 * @return
	 */
	public MvListLink moveOneMovie(long movieId, long mvListId,long userId);
	
	/**
	 * 
	 * <p>Title: findMvListByType</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-10-31 下午2:25:10
	 * @param bookListId
	 * @param type
	 * @return
	 */
	public List<MvListLink> findMvListByType(long mvListId,String type);
	
	/**
	 * 
	 * <p>Title: updateMvListLinkRemark</p> 
	 * <p>Description: 更新影单备注</p> 
	 * @author :changjiang
	 * date 2014-11-21 下午5:07:02
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @param id
	 * @return
	 */
	public MvListLink updateMvListLinkRemark(String friendinfo, String address,
			String tags, long id,String description);
	
	/**
	 * 
	 * <p>Title: findMovieLinkIsExistById</p> 
	 * <p>Description: 查询这个电影是否存在</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:24:37
	 * @param id
	 * @return
	 */
	public MvListLink findMovieLinkIsExistById(long id);
	
	/**
	 * 
	 * <p>Title: updateMovieListPic</p> 
	 * <p>Description: 更新影单封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午5:06:22
	 * @param id
	 * @param cover
	 * @return
	 */
	public MovieList updateMovieListPic(long id, String cover);
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
	 * <p>Title: queryUserMvListByName</p> 
	 * <p>Description: 根据名字查询影单</p> 
	 * @author :changjiang
	 * date 2014-10-14 上午12:54:52
	 * @param filmlist
	 * @return
	 */
	public MovieList queryUserMvListByName(MovieList filmlist);
	/**
	 * 
	 * <p>Title: addNoLikeMvList</p> 
	 * <p>Description: 增加不想看的电影</p> 
	 * @author :changjiang
	 * date 2015-4-22 下午1:46:09
	 * @param userId
	 * @return
	 */
	public int addNoLikeMvList(long userId);
	
	/**
	 * 
	 * <p>Title: findMovieLinkCount</p> 
	 * <p>Description: 查询影单的电影数量</p> 
	 * @author :changjiang
	 * date 2015-5-13 下午7:30:39
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
