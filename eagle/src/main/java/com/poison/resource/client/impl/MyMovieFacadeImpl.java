package com.poison.resource.client.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.MyMovie;
import com.poison.resource.model.ResCollectNum;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.MovieListService;
import com.poison.resource.service.MyMovieService;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;

public class MyMovieFacadeImpl implements MyMovieFacade {

	private MyMovieService myMovieService;
	private MovieListService movieListService;
	private MvFacade mvFacade;
	private UKeyWorker reskeyWork;
	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	
	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}

	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}

	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	public void setMovieListService(MovieListService movieListService) {
		this.movieListService = movieListService;
	}

	public void setMyMovieService(MyMovieService myMovieService) {
		this.myMovieService = myMovieService;
	}
	
	/**
	 * 查询用户的影单列表
	 */
	@Override
	public List<MovieList> queryFilmListByUid(long userId, Long resId) {
		//查询用户是否有默认影单
		List<MovieList> defaultMovieList = movieListService.queryDefaultFilmList(userId);
		//没有默认影单时，添加一个默认影单
		if(null==defaultMovieList||defaultMovieList.size()<3){
			addDefaultMvList(userId);
			addWantWatchMvList(userId);
			//不想看的影单
			addNoLikeMvList(userId);
		}
		List<MovieList> list = new ArrayList<MovieList>();
		list = movieListService.queryFilmListByUid(userId, resId);
		return list;
	}

	
	/**
	 * 此方法的作用是添加一部电影
	 */
	/*@Override
	public int addMyMovie(int uid, String name, String description) {
		
		movie.setUid(uid);
		movie.setName(name);
		movie.setDescribe(description);
		return myMovieService.addMyMovie(movie);
	}*/
	
	/**
	 * 此方法的作用模糊、精准查询
	 *//*
	@Override
	public List<MyMovie> findMyMovieList(String name) {
		return myMovieService.findMyMovieList(name) ;
	}
	*//**
	 * 此方法的作用是查询已经添加的id
	 *//*
	@Override
	public MyMovie findMyMovieIsExist(String name, int uid) {
		movie.setName(name);
		movie.setUid(uid);
		return myMovieService.findMyMovieIsExist(movie);
	}
	*//**
	 * 此方法的作用是根据id查询自建表中信息
	 *//*
	@Override
	public MyMovie findMyMovieInfo(long id) {
		return myMovieService.findMyMovieInfo(id);
	}*/

	/**
	 * 增加一个默认影单
	 */
	@Override
	public int addDefaultMvList(long userId) {
		long sysdate = System.currentTimeMillis();
		MovieList filmList = new MovieList();
		filmList.setId(reskeyWork.getId());
		filmList.setFilmListName("我看过的电影");
		filmList.setReason("");
		filmList.setIsDel(0);
		filmList.setType(0);
		filmList.setTag("");
		filmList.setUid(userId);
		filmList.setIsPublishing(1);
		filmList.setCreateDate(sysdate);
		filmList.setLatestRevisionDate(sysdate);
		return movieListService.addNewFilmList(null, filmList);
	}

	/**
	 * 向影单中添加一部电影
	 */
	@Override
	public int addMovieListLink(long filmListId, final int movieId, int isDb) {
		/*resourceVisitClient.execute(new JedisWorker<Object>(){

			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.MOVIE_COLLECTED_MARK+movieId+ResStatisticConstant.MOVIE_COLLECTED_TYPE;
				String beforeDate = jedis.hget(key, ResStatisticConstant.RESOURCE_COLLECTED_DATE);
				//当没有数据时，置0
				if(null==beforeDate||"".equals(beforeDate)){
					beforeDate = "0";
				}
				long sysdate = System.currentTimeMillis();
				jedis.hset(key, ResStatisticConstant.RESOURCE_COLLECTED_DATE, sysdate+"");
				long falseCollected = jedis.hincrBy(key, ResStatisticConstant.COLLECTED_FALSE_NUM, ResRandomUtils.RandomInt());
				long collectedNumber = jedis.hincrBy(key, ResStatisticConstant.RESOURCE_COLLECTED_NUM, 1);
				//当大于等于十分钟时，更新数据库
				if(sysdate-Long.valueOf(beforeDate)>=ResStatisticConstant.STATISTIC_TIME_INTERVALS){
					ResCollectNum resCollectNum = new ResCollectNum();
					resCollectNum.setResId(movieId);
					resCollectNum.setType(CommentUtils.TYPE_MOVIE);
					resCollectNum.setFalseCollectNum(falseCollected);
					resCollectNum.setIsCollectedNum(collectedNumber);
					resCollectNum.setLatestRevisionDate(sysdate);
					resStatisticService.insertResCollectNum(resCollectNum);
				}
				return jedis.hgetAll(key);
			}
			
		});*/
		int flag = ResultUtils.FILM_IS_ALREADY_EXIST;
		long sysdate = System.currentTimeMillis();
		MvListLink mvLink = new MvListLink();
		mvLink.setId(reskeyWork.getId());
		mvLink.setFilmListId(filmListId);
		mvLink.setMovieId(movieId);
		mvLink.setIsDel(0);
		mvLink.setIsDB(isDb);
		mvLink.setCreateDate(sysdate);
		mvLink.setLatestRevisionDate(sysdate);
		MvListLink link = movieListService.findMovieIsExist(mvLink);
		if(ResultUtils.DATAISNULL==link.getFlag()){
			flag =movieListService.addMovieListLink(mvLink);
		}
		return flag;
	}

	/**
	 * 增加一个新影单
	 */
	@Override
	public int addNewMvList(long userId, String mvListName,String reason,String tag) {
		long sysdate = System.currentTimeMillis();
		MovieList filmList = new MovieList();
		filmList.setId(reskeyWork.getId());
		filmList.setFilmListName(CheckParams.formatStringForInfo(mvListName));
		filmList.setReason(reason);
		filmList.setIsDel(0);
		filmList.setType(1);
		filmList.setTag(tag);
		filmList.setUid(userId);
		filmList.setIsPublishing(1);
		filmList.setCreateDate(sysdate);
		filmList.setLatestRevisionDate(sysdate);
		return movieListService.addNewFilmList(null, filmList);
	}

	/**
	 * 删除一个影单
	 */
	@Override
	public MovieList deleteMovieList(long id) {
		return movieListService.deleteMovieList(id);
	}

	/**
	 * 发布一个书单
	 */
	@Override
	public int publishMovieList(long id) {
		return movieListService.publishMovieList(id);
	}

	/**
	 * 更新影单信息
	 */
	@Override
	public int updateMovieList(long id,String filmListName,String reason,String tag,String cover) {
		return movieListService.updateMovieList(id,filmListName,reason,tag,cover);
	}

	/**
	 * 删除一个影单中的一部电影
	 */
	@Override
	public int deleteMovieListLink(long filmListId, int movieId) {
		MvListLink  mvLink = new MvListLink();
		mvLink.setFilmListId(filmListId);
		mvLink.setMovieId(movieId);
		return movieListService.deleteMvListLink(mvLink);
	}

	/**
	 * 查询一个影单的详细信息
	 */
	@Override
	public List<MvListLink> findMovieListInfo(long filmListId, Long resId,Integer pageSize) {
		return movieListService.findMovieListInfo(filmListId, resId,pageSize);
	}

	/**
	 * 查询发布的影单列表
	 */
	@Override
	public List<MovieList> findPublishMvList(Long id) {
		return movieListService.findPublishMvList(id);
	}

	/**
	 * 查询影单
	 */
	@Override
	public MovieList findMovieListById(final long id) {
		/*resourceVisitClient.execute(new JedisWorker<Object>(){
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.MVLIST_STATISTIC_MARK+id+ResStatisticConstant.MVLIST_STATISTIC_TYPE;
				String beforeDate = jedis.hget(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE);
				//当没有数据时，置0
				if(null==beforeDate||"".equals(beforeDate)){
					beforeDate = "0";
				}
				long sysdate = System.currentTimeMillis();
				jedis.hset(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE, sysdate+"");
				long falseVisit = jedis.hincrBy(key, ResStatisticConstant.STATISTIC_FALSE_VISIT, ResRandomUtils.RandomInt());
				long visitNumber = jedis.hincrBy(key, ResStatisticConstant.RESOURCE_STATISTIC_VISIT, 1);
				//当大于等于十分钟时，更新数据库
				if(sysdate-Long.valueOf(beforeDate)>=ResStatisticConstant.STATISTIC_TIME_INTERVALS){
					ResStatistic resStatistic = new ResStatistic();
					resStatistic.setResId(id);
					resStatistic.setType(CommentUtils.TYPE_MOVIELIST);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});*/
		return movieListService.findMovieListById(id);
	}

	
	/**
	 * 添加系统影单
	 */
	@Override
	public int addServerMvList(String mvListName, String reason, String tag) {
		long sysdate = System.currentTimeMillis();
		MovieList filmList = new MovieList();
		filmList.setId(reskeyWork.getId());
		filmList.setFilmListName(mvListName);
		filmList.setReason(reason);
		filmList.setIsDel(0);
		filmList.setType(2);
		filmList.setTag(tag);
		filmList.setUid(0);
		filmList.setIsPublishing(1);
		filmList.setCreateDate(sysdate);
		filmList.setLatestRevisionDate(sysdate);
		return movieListService.addNewFilmList(null, filmList);
	}

	/**
	 * 查询系统推荐影单
	 */
	@Override
	public List<MovieList> queryServerMvList(String tags, Long resId) {
		return movieListService.queryServerMvList(tags, resId);
	}

	/**
	 * 增加想看的影单
	 */
	@Override
	public int addWantWatchMvList(long userId) {
		long sysdate = System.currentTimeMillis();
		MovieList filmList = new MovieList();
		filmList.setId(reskeyWork.getId());
		filmList.setFilmListName("我想看的电影");
		filmList.setReason("");
		filmList.setIsDel(0);
		filmList.setType(0);
		filmList.setTag("");
		filmList.setUid(userId);
		filmList.setIsPublishing(1);
		filmList.setCreateDate(sysdate);
		filmList.setLatestRevisionDate(sysdate);
		return movieListService.addNewFilmList(null, filmList);
	}
	
	/**
	 * 增加不想看的电影
	 */
	@Override
	public int addNoLikeMvList(long userId) {
		long sysdate = System.currentTimeMillis();
		MovieList filmList = new MovieList();
		filmList.setId(reskeyWork.getId());
		filmList.setFilmListName("我不喜欢的电影");
		filmList.setReason("");
		filmList.setIsDel(0);
		filmList.setType(0);
		filmList.setTag("");
		filmList.setUid(userId);
		filmList.setIsPublishing(1);
		filmList.setCreateDate(sysdate);
		filmList.setLatestRevisionDate(sysdate);
		return movieListService.addNewFilmList(null, filmList);
	}

	/**
	 * 查询用户影单中是否收藏过这部电影
	 */
	@Override
	public MvListLink findUserIsCollectMovie(long uId, long movieId) {
		MvListLink mvListLink = new MvListLink();
		//查询用户的影单列表
		List<MovieList> mvList = movieListService.queryFilmListByUid(uId, null);
		MovieList mv = new MovieList();
		if(null==mvList||mvList.size()==0){
			mvListLink.setFlag(ResultUtils.DATAISNULL);
			return mvListLink;
		}
		mvListLink.setFlag(ResultUtils.DATAISNULL);
		//根据影单查询这本书是否存在影单中
		Iterator<MovieList> mvListIt = mvList.iterator();
		while(mvListIt.hasNext()){
			mvListLink.setMovieId(movieId);
			mv = mvListIt.next();
			long mvListId = mv.getId();
			mvListLink.setFilmListId(mvListId);
			mvListLink = movieListService.findMovieIsExist(mvListLink);
			if(ResultUtils.SUCCESS==mvListLink.getFlag()){//null!=mvListLink&&mvListLink.getId()!=0
				return mvListLink;
			}
			mvListLink = new MvListLink();
		}
		return mvListLink;
	}

	/**
	 * 移动一部电影
	 */
	@Override
	public MvListLink moveOneMovie(long movieId, long mvListId,long userId) {
		MovieList movieList = movieListService.findMovieListById(mvListId);
		MvListLink mvListLink = new MvListLink();
		int flag = ResultUtils.ERROR;
		//影单为空
		if(ResultUtils.DATAISNULL==movieList.getFlag()){
			mvListLink.setFlag(ResultUtils.DATAISNULL);
			return mvListLink;
		}
		//查询用户的默认影单
		/*List<MovieList> list = movieListService.queryDefaultFilmList(userId);
		Iterator<MovieList> listIt = list.iterator();
		List<MvListLink> mvLinkList = new ArrayList<MvListLink>();
		//当要移动的为默认影单时删除
		if(0==movieList.getType()){
			while(listIt.hasNext()){
				MovieList mvListIt = listIt.next();
				long mListId = mvListIt.getId();
				mvLinkList = movieListService.findMovieListInfo(mListId, null);
				//mvListLink = movieListService.findMovieIsExist(mvListLink);
				Iterator<MvListLink> linkListIt = mvLinkList.iterator();
				while(linkListIt.hasNext()){
					mvListLink = linkListIt.next();
					long movieListId = mvListLink.getFilmListId();
					long mvId = mvListLink.getMovieId();
					if(mvListLink.getMovieId()==movieId){
						deleteMovieListLink(movieListId,(int)mvId);
					}
				}
			}
		}*/
		//影单中添加这本书
		flag = addMovieListLink(mvListId, (int)movieId, 0);
		mvListLink.setFilmListId(mvListId);
		mvListLink.setMovieId(movieId);
		mvListLink = movieListService.findMovieIsExist(mvListLink);
		mvListLink.setFlag(flag);
		return mvListLink;
	}

	/**
	 * 根据影单查询tag
	 */
	@Override
	public List<MvListLink> findMvListByType(long mvListId, String type) {
		List<MvListLink> mvLinkList = movieListService.findMovieListInfo(mvListId, null,null);
		Iterator<MvListLink> linkIt = mvLinkList.iterator();
		MvListLink mvListLink = new MvListLink();
		MvInfo mv = new MvInfo();
		List<MvListLink> mvInfoList = new ArrayList<MvListLink>();
		long mvId = 0;
		String tags = "";
		while(linkIt.hasNext()){
			mvListLink = linkIt.next();
			mvId = mvListLink.getMovieId();
			mv = mvFacade.queryById(mvId);
			tags = mv.getTags();
			if(StringUtils.isType(tags,type)){
				mvInfoList.add(mvListLink);
			}
		}
		return mvInfoList;
	}

	/**
	 * 更新影单备注
	 */
	@Override
	public MvListLink updateMvListLinkRemark(String friendinfo, String address,
			String tags, long id,String description) {
		return movieListService.updateMvListLinkRemark(friendinfo, address, tags, id,description);
	}

	/**
	 * 根据id查询电影是否存在
	 */
	@Override
	public MvListLink findMovieLinkIsExistById(long id) {
		return movieListService.findMovieLinkIsExistById(id);
	}

	/**
	 * 更新影单
	 */
	@Override
	public MovieList updateMovieListPic(long id, String cover) {
		return movieListService.updateMovieListPic(id, cover);
	}

	/**
	 * 查询影单的电影数量
	 */
	@Override
	public Map<String, Object> findMovieLinkCount(long movieListId) {
		return movieListService.findMovieLinkCount(movieListId);
	}

	/**
	 * 查询用户的默认影单
	 */
	/*@Override
	public List<MovieList> queryDefaultFilmList(long userId) {
		return movieListService.queryDefaultFilmList(userId);
	}*/
	/**
	 * 查询影单中的电影数量
	 */
	public long getMovieCountByListId(long listid){
		return movieListService.getMovieCountByListId(listid);
	}
	/**
	 * 根据索引查询某个影单中的电影信息列表
	 */
	public List<MvListLink> getMovieListLinkByStartIndex(long listid, long start,int pageSize){
		return movieListService.getMovieListLinkByStartIndex(listid, start, pageSize);
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
		return movieListService.queryUserMvListByName(filmlist);
	}
	/**
	 * 根据影单id集合查询影单列表
	 */
	@Override
	public List<MovieList> findMvListsByIds(List<Long> mvlistids){
		return movieListService.findMvListsByIds(mvlistids);
	}
}
