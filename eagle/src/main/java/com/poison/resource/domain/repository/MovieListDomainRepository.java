package com.poison.resource.domain.repository;

import java.util.List;
import java.util.Map;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MovieListDAO;
import com.poison.resource.dao.MvListLinkDAO;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvListLink;

public class MovieListDomainRepository {
	private MovieListDAO movieListDAO;
	private MvListLinkDAO mvListLinkDAO;
	
	public MvListLinkDAO getMvListLinkDAO() {
		return mvListLinkDAO;
	}
	public void setMvListLinkDAO(MvListLinkDAO mvListLinkDAO) {
		this.mvListLinkDAO = mvListLinkDAO;
	}
	public MovieListDAO getMovieListDAO() {
		return movieListDAO;
	}
	public void setMovieListDAO(MovieListDAO movieListDAO) {
		this.movieListDAO = movieListDAO;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询用户默认列表
	 * @param userId
	 * @return
	 */
	public List<MovieList> queryDefaultFilmList(long userId){
		return movieListDAO.queryDefaultFilmList(userId);
	}
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是向一个书单中添加一本书
	 * @return
	 */
	public int addMovieListLink(MvListLink mvLink){
//		int flag = ResultUtils.ERROR;
//		MvListLink mvListLink = mvListLinkDAO.findMovieIsExist(mvLink);
//		if(ResultUtils.DATAISNULL==mvListLink.getFlag()){
		int	flag = mvListLinkDAO.addMovieListLink(mvLink);
		//}
		return flag;
	}
	/**
	 * 
	 * 方法的描述 :更新一个影单详情
	 * @param mvLink
	 * @return
	 */
	public int updateMovieListLink(MvListLink mvLink){
		return mvListLinkDAO.updateMovieListLink(mvLink);
	}
	
	/**
	 * 
	 * <p>Title: deleteMvListLink</p> 
	 * <p>Description: 删除一个电影</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午10:27:23
	 * @return
	 */
	public int deleteMvListLink(MvListLink mvLink){
		MvListLink link = mvListLinkDAO.findMovieIsExist(mvLink);
		int flag = link.getFlag();
		if(ResultUtils.SUCCESS==flag){
			link.setIsDel(1);
			long sysdate = System.currentTimeMillis();
			link.setLatestRevisionDate(sysdate);
			flag = mvListLinkDAO.updateMovieListLink(link);
		}
		return flag;
	}
	
	
	/**
	 * 
	 * 方法的描述 :查询影单详情
	 * @param filmListId
	 * @param movieId
	 * @return
	 */
	public List<MvListLink> findMovieListInfo(long filmListId,Long resId,Integer pageSize){
		return mvListLinkDAO.findMovieListInfo(filmListId, resId,pageSize);
	}
	
	/**
	 * 
	 * 方法的描述 :查询单本影单详情
	 * @param filmListId
	 * @return
	 */
/*	public MvListLink findMovieListById(long filmListId){
		return mvListLinkDAO.findMovieListById(filmListId);
	}*/
	/**
	 * 
	 * 方法的描述 :查询影单中是否存在这部电影
	 * @param mvLink
	 * @return
	 */
	public MvListLink findMovieIsExist(MvListLink mvLink){
		return mvListLinkDAO.findMovieIsExist(mvLink);
	}
	
	/**
	 * 
	 * 方法的描述 :此方法是新建影单
	 * @param filmlist
	 * @return
	 */
	public int addNewFilmList(MovieList filmlist){
		MovieList mvList = movieListDAO.queryUserMvListByName(filmlist);
		int flag = ResultUtils.FILMNAME_IS_ALREADY_EXIST;
		if(ResultUtils.DATAISNULL==mvList.getFlag()){
			flag = movieListDAO.addNewFilmList(filmlist);
		}
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: queryUserMvListByName</p> 
	 * <p>Description: 根据名字查询影单</p> 
	 * @author :changjiang
	 * date 2014-10-14 上午12:54:52
	 * @param filmlist
	 * @return
	 */
	public MovieList queryUserMvListByName(MovieList filmlist){
		return movieListDAO.queryUserMvListByName(filmlist);
	}
	
	/**
	 * 
	 * <p>Title: findPublishMvList</p> 
	 * <p>Description: 查询发布的影单列表</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午11:35:28
	 * @param id
	 * @return
	 */
	public List<MovieList> findPublishMvList(Long id) {
		return movieListDAO.findPublishMvList(id);
	}
	
	
	/**
	 * 
	 * <p>Title: queryFilmListByUid</p> 
	 * <p>Description: 根据UID查询用户的影单列表</p> 
	 * @author :changjiang
	 * date 2014-8-24 下午3:11:17
	 * @param userId
	 * @param resId
	 * @return
	 */
	public List<MovieList> queryFilmListByUid(long userId, Long resId){
		return movieListDAO.queryFilmListByUid(userId, resId);
	}
	
	/**
	 * 
	 * <p>Title: deleteMovieList</p> 
	 * <p>Description: 删除一个影单</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午5:26:03
	 * @param id
	 * @return
	 */
	public MovieList deleteMovieList(long id){
		MovieList mvList = movieListDAO.findMovieListById(id);
		int flag = mvList.getFlag();
		long sysdate = System.currentTimeMillis();
		//当有这个书单时
		if(ResultUtils.SUCCESS==flag){
			mvList.setIsDel(1);
			mvList.setLatestRevisionDate(sysdate);
			flag = movieListDAO.updateFilmList(mvList);
			mvList.setFlag(flag);
		}
		return mvList;
	}
	
	/**
	 * 
	 * <p>Title: updateMovieList</p> 
	 * <p>Description: 修改影单信息</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午5:30:27
	 * @param movieList
	 * @return
	 */
	public int updateMovieList(long id,String filmListName,String reason,String tag,String cover){
		//查询这个影单是否存在
		MovieList mvList = movieListDAO.findMovieListById(id);
		int flag = mvList.getFlag();
		long sysdate = System.currentTimeMillis();
		if(ResultUtils.SUCCESS==flag){
			mvList.setFilmListName(filmListName);
			mvList.setReason(reason);
			mvList.setTag(tag);
			mvList.setCover(cover);
			mvList.setLatestRevisionDate(sysdate);
			flag = movieListDAO.updateFilmList(mvList);
		}
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: updateMovieListPic</p> 
	 * <p>Description: 更新影单封面</p> 
	 * @author :changjiang
	 * date 2015-1-8 下午5:03:39
	 * @param id
	 * @param cover
	 * @return
	 */
	public MovieList updateMovieListPic(long id, String cover){
		//根据id查询影单
		MovieList MovieList = movieListDAO.findMovieListById(id);
		if(ResultUtils.SUCCESS==MovieList.getFlag()){
			int flag = movieListDAO.updateMovieListPic(id, cover);
			MovieList = movieListDAO.findMovieListById(id);
		}
		return MovieList;
	}
	
	/**
	 * 
	 * <p>Title: publishMovieList</p> 
	 * <p>Description: 发布一个影单</p> 
	 * @author :changjiang
	 * date 2014-8-25 下午5:35:34
	 * @return
	 */
	public int publishMovieList(long id){
		MovieList mvList = movieListDAO.findMovieListById(id);
		int flag = mvList.getFlag();
		long sysdate = System.currentTimeMillis();
		//当有这个书单时
		if(ResultUtils.SUCCESS==flag){
			mvList.setIsPublishing(0);
			mvList.setLatestRevisionDate(sysdate);
			flag = movieListDAO.updateFilmList(mvList);
		}
		return flag;
	}
	
	/**
	 * 
	 * <p>Title: findMovieListById</p> 
	 * <p>Description: 查询影单</p> 
	 * @author :changjiang
	 * date 2014-8-27 下午3:45:46
	 * @return
	 */
	public MovieList findMovieListById(long id){
		return movieListDAO.findMovieListById(id);
	}
	
	/**
	 * 
	 * <p>Title: queryServerMvList</p> 
	 * <p>Description: 查询系统推荐的影单</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午6:29:59
	 * @param tags
	 * @param resId
	 * @return
	 */
	public List<MovieList> queryServerMvList(String tags, Long resId){
		return movieListDAO.queryServerMvList(tags, resId);
	}
	/**
	 * 根据影单id集合查询影单列表
	 * @param mvlistids
	 * @return
	 */
	public List<MovieList> findMvListsByIds(List<Long> mvlistids){
		return movieListDAO.findMvListsByIds(mvlistids);
	}
	
	/**
	 * 
	 * <p>Title: updateMvListLinkRemark</p> 
	 * <p>Description: 更新影单的备注</p> 
	 * @author :changjiang
	 * date 2014-11-21 下午5:03:23
	 * @param friendinfo
	 * @param address
	 * @param tags
	 * @param id
	 * @return
	 */
	public MvListLink updateMvListLinkRemark(String friendinfo, String address,
			String tags,long id,String description){
		MvListLink mvListLink = mvListLinkDAO.findMovieLinkIsExist(id);
		int flag = mvListLink.getFlag();
		if(ResultUtils.SUCCESS==flag){
			flag = mvListLinkDAO.updateMovieLinkRemark(friendinfo, address, tags, id,description);
			mvListLink = mvListLinkDAO.findMovieLinkIsExist(id);
			mvListLink.setFlag(flag);
		}
		return mvListLink;
	}
	
	/**
	 * 
	 * <p>Title: findMovieLinkIsExistById</p> 
	 * <p>Description: 查询电影是否存在</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:19:03
	 * @return
	 */
	public MvListLink findMovieLinkIsExistById(long id){
		return mvListLinkDAO.findMovieLinkIsExist(id);
	}
	
	/**
	 * 
	 * <p>Title: addMvListCommentCount</p> 
	 * <p>Description: 增加影单的评论数</p> 
	 * @author :changjiang
	 * date 2015-1-28 下午9:36:10
	 * @param id
	 * @param latestRevisionDate
	 * @return
	 */
	public int addMvListCommentCount(long id, long latestRevisionDate){
		return movieListDAO.addMvListCommentCount(id, latestRevisionDate);
	}
	/**
	 * 查询影单中的电影数量
	 */
	public long getMovieCountByListId(long listid){
		return mvListLinkDAO.getMovieCountByListId(listid);
	}
	/**
	 * 根据索引查询某个影单中的电影信息列表
	 */
	public List<MvListLink> getMovieListLinkByStartIndex(long listid, long start,int pageSize){
		return mvListLinkDAO.getMovieListLinkByStartIndex(listid, start, pageSize);
	}
	
	/**
	 * 
	 * <p>Title: findMovieLinkCount</p> 
	 * <p>Description: 查询一个影单中的电影数量</p> 
	 * @author :changjiang
	 * date 2015-5-13 下午7:45:45
	 * @param movieListId
	 * @return
	 */
	public Map<String, Object> findMovieLinkCount(long movieListId){
		return mvListLinkDAO.findMovieLinkCount(movieListId);
	}
}
