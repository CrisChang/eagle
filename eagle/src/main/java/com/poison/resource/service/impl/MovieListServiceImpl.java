package com.poison.resource.service.impl;

import java.util.List;
import java.util.Map;

import com.keel.framework.runtime.ProductContext;
import com.poison.resource.domain.repository.MovieListDomainRepository;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvListLink;
import com.poison.resource.service.MovieListService;

public class MovieListServiceImpl implements MovieListService{
	private MovieListDomainRepository movieListDomainRepository;
	
	public void setMovieListDomainRepository(
			MovieListDomainRepository movieListDomainRepository) {
		this.movieListDomainRepository = movieListDomainRepository;
	}
	
	@Override
	public List<MovieList> queryDefaultFilmList(long userId) {
		return movieListDomainRepository.queryDefaultFilmList(userId);
	}
	
	@Override
	public int addMovieListLink(MvListLink mvLink) {
		return movieListDomainRepository.addMovieListLink(mvLink);
	}

	@Override
	public int updateMovieListLink(MvListLink mvLink) {
		return movieListDomainRepository.updateMovieListLink(mvLink);
	}

	/**
	 * 查询影单信息
	 */
	@Override
	public List<MvListLink> findMovieListInfo(long filmListId, Long resId,Integer pageSize) {
		return movieListDomainRepository.findMovieListInfo(filmListId, resId,pageSize);
	}

/*	@Override
	public MvListLink findMovieListById(long filmListId) {
		return null;//movieListDomainRepository.findMovieListById(filmListId);
	}*/

	@Override
	public MvListLink findMovieIsExist(MvListLink mvLink) {
		return movieListDomainRepository.findMovieIsExist(mvLink);
	}

	/**
	 * 添加一个新的书单
	 */
	@Override
	public int addNewFilmList(ProductContext productContext, MovieList flist) {
		return movieListDomainRepository.addNewFilmList(flist);
	}

	/**
	 * 根据uid查询用户影单列表
	 */
	@Override
	public List<MovieList> queryFilmListByUid(long userId, Long resId) {
		return movieListDomainRepository.queryFilmListByUid(userId, resId);
	}

	/**
	 * 删除一个影单
	 */
	@Override
	public MovieList deleteMovieList(long id) {
		return movieListDomainRepository.deleteMovieList(id);
	}

	/**
	 * 发布一个影单
	 */
	@Override
	public int publishMovieList(long id) {
		return movieListDomainRepository.publishMovieList(id);
	}

	/**
	 * 更改一个影单信息
	 */
	@Override
	public int updateMovieList(long id,String filmListName,String reason,String tag,String cover) {
		return movieListDomainRepository.updateMovieList(id,filmListName,reason,tag,cover);
	}

	/**
	 * 删除一个影单中的一部电影
	 */
	@Override
	public int deleteMvListLink(MvListLink mvLink) {
		return movieListDomainRepository.deleteMvListLink(mvLink);
	}

	/**
	 * 查询发布的影单列表
	 */
	@Override
	public List<MovieList> findPublishMvList(Long id) {
		return movieListDomainRepository.findPublishMvList(id);
	}

	/**
	 * 查询一部电影
	 */
	@Override
	public MovieList findMovieListById(long id) {
		return movieListDomainRepository.findMovieListById(id);
	}

	/**
	 * 查询系统推荐影单
	 */
	@Override
	public List<MovieList> queryServerMvList(String tags, Long resId) {
		return movieListDomainRepository.queryServerMvList(tags, resId);
	}

	/**
	 * 根据名字查询影单
	 */
	@Override
	public MovieList queryUserMvListByName(MovieList filmlist) {
		return movieListDomainRepository.queryUserMvListByName(filmlist);
	}

	/**
	 * 更新影单备注
	 */
	@Override
	public MvListLink updateMvListLinkRemark(String friendinfo, String address,
			String tags, long id,String description) {
		return movieListDomainRepository.updateMvListLinkRemark(friendinfo, address, tags, id,description);
	}

	/**
	 * 查询电影是否存在
	 */
	@Override
	public MvListLink findMovieLinkIsExistById(long id) {
		return movieListDomainRepository.findMovieLinkIsExistById(id);
	}

	/**
	 * 更新影单封面
	 */
	@Override
	public MovieList updateMovieListPic(long id, String cover) {
		return movieListDomainRepository.updateMovieListPic(id, cover);
	}

	/**
	 * 增加影单评论
	 */
	@Override
	public int addMvListCommentCount(long id, long latestRevisionDate) {
		return movieListDomainRepository.addMvListCommentCount(id, latestRevisionDate);
	}

	@Override
	public long getMovieCountByListId(long listid) {
		return movieListDomainRepository.getMovieCountByListId(listid);
	}

	@Override
	public List<MvListLink> getMovieListLinkByStartIndex(long listid,
			long start, int pageSize) {
		return movieListDomainRepository.getMovieListLinkByStartIndex(listid, start, pageSize);
	}

	/**
	 * 查询一个影单的多少部电影
	 */
	@Override
	public Map<String, Object> findMovieLinkCount(long movieListId) {
		return movieListDomainRepository.findMovieLinkCount(movieListId);
	}
	@Override
	public List<MovieList> findMvListsByIds(List<Long> mvlistids){
		return movieListDomainRepository.findMvListsByIds(mvlistids);
	}
}
